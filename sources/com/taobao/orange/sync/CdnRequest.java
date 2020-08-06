package com.taobao.orange.sync;

import android.text.TextUtils;
import com.taobao.orange.GlobalOrange;
import com.taobao.orange.impl.HurlNetConnection;
import com.taobao.orange.inner.INetConnection;
import com.taobao.orange.util.MD5Util;
import com.taobao.orange.util.OLog;

public abstract class CdnRequest<T> extends BaseRequest<T> {
    private static final String TAG = "CdnRequest";
    private String mMD5;
    private String mUrl;

    /* access modifiers changed from: protected */
    public abstract T parseResContent(String str);

    public CdnRequest(String url, String md5) {
        this.mUrl = url;
        this.mMD5 = md5;
    }

    public T syncRequest() {
        if (OLog.isPrintLog(1)) {
            OLog.d(TAG, "syncRequest start", "cdn url", this.mUrl);
        }
        String resContent = null;
        try {
            INetConnection netConnection = (INetConnection) GlobalOrange.netConnection.newInstance();
            int retryNum = 1;
            if (netConnection instanceof HurlNetConnection) {
                retryNum = GlobalOrange.reqRetryNum;
            }
            int retryNo = 0;
            while (true) {
                if (retryNo >= retryNum) {
                    break;
                }
                try {
                    netConnection.openConnection(this.mUrl);
                    netConnection.setMethod("GET");
                    netConnection.connect();
                    this.code = netConnection.getResponseCode();
                    if (this.code == 200) {
                        resContent = netConnection.getResponse();
                        netConnection.disconnect();
                        break;
                    }
                    retryNo++;
                } catch (Throwable t) {
                    if (OLog.isPrintLog(3)) {
                        OLog.w(TAG, "syncRequest fail", t, "retryNo", Integer.valueOf(retryNo));
                    }
                    this.message = t.getMessage();
                } finally {
                    netConnection.disconnect();
                }
            }
            if (TextUtils.isEmpty(resContent)) {
                this.message = "content is empty";
                OLog.e(TAG, "syncRequest fail", "code", Integer.valueOf(this.code), "msg", this.message);
                return null;
            } else if (TextUtils.isEmpty(this.mMD5) || this.mMD5.equals(MD5Util.md5(resContent))) {
                try {
                    return parseResContent(resContent);
                } catch (Throwable t2) {
                    this.code = -3;
                    this.message = t2.getMessage();
                    OLog.e(TAG, "syncRequest fail", t2, new Object[0]);
                    return null;
                }
            } else {
                this.code = -2;
                this.message = "content is broken";
                OLog.e(TAG, "syncRequest fail", "code", Integer.valueOf(this.code), "msg", this.message);
                return null;
            }
        } catch (Throwable t3) {
            OLog.e(TAG, "syncRequest", t3, new Object[0]);
            this.message = t3.getMessage();
            return null;
        }
    }
}
