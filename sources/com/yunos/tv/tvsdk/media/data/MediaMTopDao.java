package com.yunos.tv.tvsdk.media.data;

import android.text.TextUtils;
import android.util.Log;
import com.alibaba.analytics.core.Constants;
import com.bftv.fui.constantplugin.Constant;
import com.yunos.tv.sdk.lib.http.HttpConstant;
import com.yunos.tv.sdk.lib.http.HttpReadResponse;
import com.yunos.tv.sdk.lib.http.exception.HttpRequestException;
import com.yunos.tv.sdk.lib.utils.HttpRequestGetUtil;
import com.yunos.tv.sdk.lib.utils.HttpRequestPostUtil;
import com.yunos.tv.tvsdk.media.error.ErrorDetail;
import com.yunos.tv.tvsdk.media.error.ErrorType;
import com.yunos.tv.tvsdk.media.error.IMediaError;
import com.yunos.tv.tvsdk.media.error.MTopInfoError;
import java.util.HashMap;
import java.util.Map;
import mtopsdk.common.util.HttpHeaderConstant;
import mtopsdk.common.util.SymbolExpUtil;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class MediaMTopDao {
    public static final int RETRY_COUNT = 3;
    public static final String TAG = MediaMTopDao.class.getSimpleName();
    private static boolean mNeedHttpDns = true;

    public static void setNeedHttpDns(boolean needDns) {
        mNeedHttpDns = needDns;
    }

    public static <T extends MTopInfoBase> T getMTopInfo(MediaType sourceType, HttpConstant.HttpMethod method, MediaMTopParams params, Class<T> c) {
        switch (sourceType) {
            case FROM_TAOTV:
                return getMtopTaoTv(sourceType, method, params, c);
            case FROM_YOUKU:
                return getMtopYouku(sourceType, method, params, c);
            case FROM_SOHU:
                return getMtopSohu(sourceType, method, params, c);
            case FROM_QIYI:
                return getMtopQiyi(sourceType, method, params, c);
            default:
                return getMtopTaoTv(sourceType, method, params, c);
        }
    }

    private static <T extends MTopInfoBase> T getMtopYouku(MediaType type, HttpConstant.HttpMethod method, MediaMTopParams params, Class<T> cls) {
        return null;
    }

    private static <T extends MTopInfoBase> T getMtopSohu(MediaType type, HttpConstant.HttpMethod method, MediaMTopParams params, Class<T> cls) {
        return null;
    }

    private static <T extends MTopInfoBase> T getMtopTaoTv(MediaType type, HttpConstant.HttpMethod method, MediaMTopParams params, Class<T> c) {
        Log.w(TAG, "getMtopTaoTv object= " + (params != null ? params.getAppdata() : Constant.NULL));
        return requestPlayJSONObject(type, method, params, c);
    }

    private static <T extends MTopInfoBase> T getMtopQiyi(MediaType type, HttpConstant.HttpMethod method, MediaMTopParams params, Class<T> c) {
        Log.w(TAG, "getMtopQiyi object= " + (params != null ? params.getAppdata() : Constant.NULL));
        return requestPlayJSONObject(type, method, params, c);
    }

    private static <T extends MTopInfoBase> T requestPlayJSONObject(MediaType type, HttpConstant.HttpMethod method, MediaMTopParams params, Class<T> c) {
        JSONObject objectResult;
        String str;
        T mTopInfo = null;
        MTopInfoError topInfoError = new MTopInfoError();
        topInfoError.setMediaType(type);
        ErrorDetail errorDetail = ErrorDetail.createErrorDetail(type);
        topInfoError.setErrorDetail(errorDetail);
        try {
            MediaMTopParams mediaMTopParams = params;
            String url = mediaMTopParams.getHttpParams(params.getApiName(), params.getAppKey(), params.getSecret(), params.getAppdata(), getServertime(String.valueOf(params.getServerTime())));
            HttpReadResponse s = getHttpReadResponse(method, url);
            int httpCode = s.getResponseCode();
            if (httpCode != 200) {
                Log.e(TAG, "requestPlayJSONObject() httpCode!=200, httpCode = " + httpCode + ", requestUrl = " + url);
                topInfoError.setErrorType(ErrorType.SERVER_ERROR);
                if (httpCode == 404) {
                    errorDetail.setCode(404);
                } else if (httpCode >= 500) {
                    errorDetail.setCode(500);
                } else {
                    errorDetail.setCode(24);
                }
                errorDetail.setErrorMessage("requestPlayJSONObject() httpCode!=200, httpCode = " + httpCode + ", requestUrl = " + url);
                mTopInfo = (MTopInfoBase) c.newInstance();
            } else {
                JSONObject jSONObject = new JSONObject(s.getResponse());
                mTopInfo = (MTopInfoBase) c.newInstance();
                if (jSONObject != null) {
                    mTopInfo.parseFromJson(jSONObject.toString());
                }
                if (jSONObject == null) {
                    objectResult = null;
                } else {
                    objectResult = jSONObject.optJSONObject("data");
                }
                if (objectResult == null || !objectResult.has("result")) {
                    topInfoError.setErrorType(ErrorType.AUTH_ERROR);
                    if (jSONObject == null || !jSONObject.has("ret")) {
                        Log.w(TAG, "requestPlayJSONObject() error! objectResult is null!");
                        errorDetail.setCode(34);
                    } else {
                        Log.e(TAG, " error! objectResult==:" + jSONObject.toString());
                        JSONArray array = jSONObject.optJSONArray("ret");
                        if (array != null && array.length() > 0 && (str = array.opt(0).toString()) != null && str.length() > 0) {
                            Log.e(TAG, " error! message==:" + str.toString());
                            if (str.contains("NO_AUTHORITY")) {
                                errorDetail.setCode(26);
                            } else {
                                errorDetail.setCode(25);
                                errorDetail.setErrorMessage(str.substring(str.lastIndexOf(SymbolExpUtil.SYMBOL_COLON) + 1, str.length()));
                            }
                        }
                    }
                } else {
                    Log.v(TAG, String.format("requestPlayJSONObject end, success", new Object[0]));
                    JSONObject result = objectResult.optJSONObject("result");
                    if (result == null) {
                        topInfoError.setErrorType(ErrorType.DATA_ERROR);
                        errorDetail.setCode(18);
                    } else if (result.has("errCode")) {
                        String errCode = result.optString("errCode");
                        if (errCode == null) {
                            topInfoError = null;
                        } else if (errCode.equals("1")) {
                            topInfoError.setErrorType(ErrorType.DATA_ERROR);
                            errorDetail.setCode(28);
                        } else if (errCode.equals("2")) {
                            topInfoError.setErrorType(ErrorType.DATA_ERROR);
                            errorDetail.setCode(29);
                        } else if (errCode.equals("3")) {
                            topInfoError.setErrorType(ErrorType.DATA_ERROR);
                            errorDetail.setCode(30);
                        } else if (errCode.equals("4")) {
                            topInfoError.setErrorType(ErrorType.DATA_ERROR);
                            errorDetail.setCode(31);
                        } else if (errCode.equals("5")) {
                            topInfoError.setErrorType(ErrorType.DATA_ERROR);
                            errorDetail.setCode(32);
                        } else if (errCode.equals(Constants.LogTransferLevel.L6)) {
                            topInfoError.setErrorType(ErrorType.DATA_ERROR);
                            errorDetail.setCode(33);
                        } else if (errCode.equals("101")) {
                            topInfoError.setErrorType(ErrorType.DATA_ERROR);
                            errorDetail.setCode(101);
                        } else if (errCode.equals("102")) {
                            topInfoError.setErrorType(ErrorType.DATA_ERROR);
                            errorDetail.setCode(102);
                        } else if (errCode.equals("103")) {
                            topInfoError.setErrorType(ErrorType.DATA_ERROR);
                            errorDetail.setCode(103);
                        } else if (!errCode.equals("0")) {
                            topInfoError.setErrorType(ErrorType.DATA_ERROR);
                            int code = 0;
                            if (!TextUtils.isEmpty(errCode) && TextUtils.isDigitsOnly(errCode)) {
                                code = Integer.valueOf(errCode).intValue();
                            }
                            errorDetail.setCode(code);
                        } else {
                            topInfoError = null;
                        }
                    } else {
                        topInfoError = null;
                    }
                }
            }
            if (mTopInfo != null) {
                mTopInfo.setMediaError(topInfoError);
            }
            return mTopInfo;
        } catch (JSONException ex) {
            mTopInfo = (MTopInfoBase) c.newInstance();
            Log.w(TAG, "requestPlayJSONObject() JSON Error ?", ex);
            topInfoError.setErrorType(ErrorType.DATA_ERROR);
            errorDetail.setCode(35);
            errorDetail.setErrorMessage(ex.toString());
            if (mTopInfo != null) {
                mTopInfo.setMediaError(topInfoError);
            }
            return mTopInfo;
        } catch (Exception e) {
            mTopInfo = (MTopInfoBase) c.newInstance();
            Log.w(TAG, "requestPlayJSONObject() MTOP_NETWORK_ERROR?", e);
            topInfoError.setErrorType(ErrorType.NETWORK_ERROR);
            if (e instanceof HttpRequestException) {
                errorDetail.setCode(e.getCode());
                errorDetail.setErrorMessage(e.getMessage());
            } else {
                errorDetail.setCode(37);
                errorDetail.setErrorMessage(e.getLocalizedMessage());
            }
            if (mTopInfo != null) {
                mTopInfo.setMediaError(topInfoError);
            }
            return mTopInfo;
        } catch (Throwable th) {
            if (mTopInfo != null) {
                mTopInfo.setMediaError(topInfoError);
            }
            return mTopInfo;
        }
    }

    private static HttpReadResponse getHttpReadResponse(HttpConstant.HttpMethod method, String url) throws Exception {
        if (method.equals(HttpConstant.HttpMethod.GET)) {
            HttpRequestGetUtil util = new HttpRequestGetUtil(mNeedHttpDns, url, (Map) null);
            util.setRetryCount(3);
            return util.request();
        } else if (!method.equals(HttpConstant.HttpMethod.POST)) {
            return null;
        } else {
            HashMap<String, String> properties = new HashMap<>();
            properties.put("Content-Type", "application/x-www-form-urlencoded");
            properties.put("Accept", "*/*");
            properties.put("Accept-Charset", "UTF8");
            properties.put("Connection", "Keep-Alive");
            properties.put("Cache-Control", HttpHeaderConstant.NO_CACHE);
            HttpRequestPostUtil util2 = new HttpRequestPostUtil(mNeedHttpDns, url, "", properties);
            util2.setRetryCount(3);
            return util2.request();
        }
    }

    public static long getServertime(String time) {
        long serverTime = 0;
        String sTime = time;
        try {
            if (!TextUtils.isEmpty(sTime)) {
                serverTime = Long.valueOf(sTime).longValue() / 1000;
            }
            if (serverTime <= 0) {
                return System.currentTimeMillis() / 1000;
            }
            return serverTime;
        } catch (Exception e) {
            e.printStackTrace();
            if (0 <= 0) {
                return System.currentTimeMillis() / 1000;
            }
            return 0;
        } catch (Throwable th) {
            if (0 <= 0) {
                long serverTime2 = System.currentTimeMillis() / 1000;
            }
            throw th;
        }
    }

    public static IMediaError createMediaError(MediaType type, ErrorType errType, int code, String msg) {
        MTopInfoError topInfoError = new MTopInfoError();
        topInfoError.setMediaType(type);
        topInfoError.setErrorType(errType);
        ErrorDetail errorDetail = ErrorDetail.createErrorDetail(type);
        topInfoError.setErrorDetail(errorDetail);
        errorDetail.setCode(code);
        errorDetail.setErrorMessage(msg);
        return topInfoError;
    }
}
