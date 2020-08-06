package com.taobao.orange.sync;

import android.text.TextUtils;
import anet.channel.util.HttpConstant;
import com.taobao.orange.GlobalOrange;
import com.taobao.orange.OConstant;
import com.taobao.orange.impl.HmacSign;
import com.taobao.orange.impl.HurlNetConnection;
import com.taobao.orange.impl.TBGuardSign;
import com.taobao.orange.inner.INetConnection;
import com.taobao.orange.inner.ISign;
import com.taobao.orange.util.MD5Util;
import com.taobao.orange.util.OLog;
import com.taobao.orange.util.OrangeUtils;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

public abstract class AuthRequest<T> extends BaseRequest<T> {
    private static final String CURVER_SIGN = "1.0";
    private static final String REQUEST_APP_KEY = "o-app-key";
    private static final String REQUEST_APP_VERSION = "o-app-version";
    private static final String REQUEST_DEVICEID = "o-device-id";
    private static final String REQUEST_HOST = "host";
    private static final String REQUEST_NUMBER = "o-request-unique";
    private static final String REQUEST_SDK_VERSION = "o-sdk-version";
    private static final String REQUEST_SIGN_INFO = "o-sign";
    private static final String REQUEST_SIGN_VERSION = "o-sign-version";
    private static final String REQUEST_TIMESTAMP = "o-timestamp";
    private static final String REQUEST_USER_INFO = "o-user-info";
    private static final String RESPONSE_CODE = "o-code";
    private static final String RESPONSE_CODE_EXPIRED = "10002";
    private static final String RESPONSE_SERVER_TIMESTAMP = "o-server-timestamp";
    private static final String SIGN_SEPARETOR = "&";
    private static final String TAG = "AuthRequest";
    public static volatile long reqTimestampOffset = 0;
    private long mCurTimestamp;
    private String mHost;
    private boolean mIsAckReq;
    private String mMD5;
    private String mReqNo;
    private String mReqType;
    private ISign mSign;

    /* access modifiers changed from: protected */
    public abstract Map<String, String> getReqParams();

    /* access modifiers changed from: protected */
    public abstract String getReqPostBody();

    /* access modifiers changed from: protected */
    public abstract T parseResContent(String str);

    public AuthRequest(String md5, boolean isAckReq, String reqType) {
        this.mMD5 = md5;
        this.mIsAckReq = isAckReq;
        this.mHost = this.mIsAckReq ? GlobalOrange.ackHost : GlobalOrange.dcHost;
        this.mReqType = reqType;
        updateReqTimestamp();
        if (TextUtils.isEmpty(GlobalOrange.appSecret)) {
            this.mSign = new TBGuardSign();
        } else {
            this.mSign = new HmacSign();
        }
    }

    private void updateReqTimestamp() {
        this.mCurTimestamp = (System.currentTimeMillis() / 1000) + reqTimestampOffset;
        this.mReqNo = GlobalOrange.deviceId + "_" + this.mCurTimestamp;
    }

    public T syncRequest() {
        if (OLog.isPrintLog(1)) {
            OLog.d(TAG, "syncRequest start", "isAckReq", Boolean.valueOf(this.mIsAckReq), "reqType", this.mReqType);
        }
        try {
            INetConnection netConnection = (INetConnection) GlobalOrange.netConnection.newInstance();
            String resContent = null;
            if (netConnection instanceof HurlNetConnection) {
                List<String> hosts = OrangeUtils.randomListFromSet(this.mIsAckReq ? GlobalOrange.ackVips : GlobalOrange.dcVips);
                hosts.add(0, this.mHost);
                Iterator<String> it = hosts.iterator();
                while (true) {
                    if (!it.hasNext()) {
                        break;
                    }
                    String host = it.next();
                    try {
                        formatNetConnection(netConnection, formateReqUrl(host, this.mReqType));
                        this.code = netConnection.getResponseCode();
                        if (this.code == 200) {
                            checkResposeHeads(netConnection.getHeadFields());
                            resContent = netConnection.getResponse();
                            netConnection.disconnect();
                            break;
                        }
                    } catch (Throwable t) {
                        if (OLog.isPrintLog(3)) {
                            OLog.w(TAG, "syncRequest fail", t, REQUEST_HOST, host);
                        }
                    } finally {
                        netConnection.disconnect();
                    }
                }
            } else {
                try {
                    formatNetConnection(netConnection, formateReqUrl(this.mHost, this.mReqType));
                    this.code = netConnection.getResponseCode();
                    if (this.code == 200) {
                        checkResposeHeads(netConnection.getHeadFields());
                        resContent = netConnection.getResponse();
                    }
                } catch (Throwable t2) {
                    if (OLog.isPrintLog(3)) {
                        OLog.w(TAG, "syncRequest fail", t2, REQUEST_HOST, this.mHost);
                    }
                    this.message = t2.getMessage();
                } finally {
                    netConnection.disconnect();
                }
            }
            if (this.mIsAckReq) {
                return null;
            }
            if (TextUtils.isEmpty(resContent)) {
                this.message = "content is empty";
                OLog.e(TAG, "syncRequest fail", "code", Integer.valueOf(this.code), "message", this.message);
                return null;
            } else if (TextUtils.isEmpty(this.mMD5) || this.mMD5.equals(MD5Util.md5(resContent))) {
                try {
                    return parseResContent(resContent);
                } catch (Throwable t3) {
                    this.code = -3;
                    this.message = t3.getMessage();
                    OLog.e(TAG, "syncRequest fail", t3, new Object[0]);
                    return null;
                }
            } else {
                this.code = -2;
                this.message = "content is broken";
                OLog.e(TAG, "syncRequest fail", "code", Integer.valueOf(this.code), "message", this.message);
                return null;
            }
        } catch (Throwable t4) {
            OLog.e(TAG, "syncRequest", t4, new Object[0]);
            this.message = t4.getMessage();
            return null;
        }
    }

    private void checkResposeHeads(Map<String, List<String>> resHeader) {
        if (resHeader != null && !resHeader.isEmpty() && RESPONSE_CODE_EXPIRED.equals(OrangeUtils.getDecodeValue((String) resHeader.get(RESPONSE_CODE).get(0)))) {
            OLog.w(TAG, "checkResposeHeads", "expired, correct timestamp");
            long server_timestamp = OrangeUtils.parseLong(OrangeUtils.getDecodeValue((String) resHeader.get(RESPONSE_SERVER_TIMESTAMP).get(0)));
            if (server_timestamp != 0 && this.mCurTimestamp != 0) {
                long offsetValue = server_timestamp - this.mCurTimestamp;
                OLog.w(TAG, "checkResposeHeads", "update global reqTimestampOffset(s)", Long.valueOf(offsetValue));
                reqTimestampOffset = offsetValue;
                updateReqTimestamp();
            }
        }
    }

    private String formateReqUrl(String host, String type) {
        if (TextUtils.isEmpty(host)) {
            return null;
        }
        StringBuilder configDownloadUrl = new StringBuilder(GlobalOrange.env == OConstant.ENV.ONLINE ? "https" : "http").append(HttpConstant.SCHEME_SPLIT).append(host);
        if (!TextUtils.isEmpty(type)) {
            configDownloadUrl.append(type);
        }
        return configDownloadUrl.toString();
    }

    private void formatNetConnection(INetConnection netConnection, String url) throws Throwable {
        String appKey = OrangeUtils.getEncodeValue(GlobalOrange.appKey);
        String appVersion = OrangeUtils.getEncodeValue(GlobalOrange.appVersion);
        String deviceId = OrangeUtils.getEncodeValue(GlobalOrange.deviceId);
        String postReqBody = getReqPostBody();
        String signInfo = OrangeUtils.getEncodeValue(getSignInfoHeader(postReqBody));
        if (TextUtils.isEmpty(appKey) || TextUtils.isEmpty(deviceId) || TextUtils.isEmpty(appVersion) || TextUtils.isEmpty(signInfo)) {
            OLog.e(TAG, "getRequestImpl error", "signInfo", signInfo, "appKey", appKey, "appVersion", appVersion, "deviceId", deviceId);
            return;
        }
        netConnection.setParams(getReqParams());
        netConnection.openConnection(url);
        if (this.mIsAckReq) {
            netConnection.addHeader(REQUEST_NUMBER, OrangeUtils.getEncodeValue(this.mReqNo));
        }
        netConnection.addHeader(REQUEST_TIMESTAMP, OrangeUtils.getEncodeValue(String.valueOf(this.mCurTimestamp)));
        netConnection.addHeader(REQUEST_SIGN_VERSION, OrangeUtils.getEncodeValue("1.0"));
        netConnection.addHeader(REQUEST_SDK_VERSION, OrangeUtils.getEncodeValue(OConstant.SDK_VERSION));
        netConnection.addHeader(REQUEST_APP_KEY, appKey);
        netConnection.addHeader(REQUEST_APP_VERSION, appVersion);
        netConnection.addHeader(REQUEST_DEVICEID, deviceId);
        netConnection.addHeader(REQUEST_SIGN_INFO, signInfo);
        String userInfo = GlobalOrange.userId;
        if (!TextUtils.isEmpty(userInfo)) {
            netConnection.addHeader(REQUEST_USER_INFO, userInfo);
        }
        netConnection.addHeader(REQUEST_HOST, OrangeUtils.getEncodeValue(this.mHost));
        if (!TextUtils.isEmpty(postReqBody)) {
            netConnection.setMethod("POST");
            netConnection.setBody(postReqBody.getBytes());
        } else {
            netConnection.setMethod("GET");
        }
        netConnection.connect();
    }

    private String getSignInfoHeader(String reqBody) {
        StringBuilder signKey = new StringBuilder(this.mReqType).append("&").append(GlobalOrange.appKey).append("&").append(GlobalOrange.appVersion).append("&").append(GlobalOrange.deviceId).append("&").append(this.mCurTimestamp);
        if (this.mIsAckReq) {
            signKey.append("&").append(this.mReqNo);
            if (!TextUtils.isEmpty(reqBody)) {
                signKey.append("&").append(reqBody);
            }
        }
        return this.mSign.sign(GlobalOrange.context, GlobalOrange.appKey, GlobalOrange.appSecret, signKey.toString(), GlobalOrange.authCode);
    }
}
