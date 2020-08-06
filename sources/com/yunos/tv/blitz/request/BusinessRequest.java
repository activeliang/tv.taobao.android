package com.yunos.tv.blitz.request;

import android.text.TextUtils;
import anetwork.channel.Network;
import anetwork.channel.Response;
import anetwork.channel.degrade.DegradableNetwork;
import anetwork.channel.entity.RequestImpl;
import com.yunos.tv.blitz.global.BzAppConfig;
import com.yunos.tv.blitz.request.base.BaseHttpRequest;
import com.yunos.tv.blitz.request.base.BaseMtopRequest;
import com.yunos.tv.blitz.request.common.AppDebug;
import com.yunos.tv.blitz.request.common.RequestListener;
import com.yunos.tv.blitz.request.core.AsyncDataLoader;
import com.yunos.tv.blitz.request.core.ServiceCode;
import com.yunos.tv.blitz.request.core.ServiceResponse;
import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.InputStreamReader;

public class BusinessRequest {
    public static final Long RECOMMEND_CATEGORY_ID = 0L;
    private static BusinessRequest mBusinessRequest;
    protected String TAG = getClass().getSimpleName();

    public interface RequestLoadListener<T> {
        ServiceResponse<T> load();
    }

    public static BusinessRequest getBusinessRequest() {
        if (mBusinessRequest == null) {
            mBusinessRequest = new BusinessRequest();
        }
        return mBusinessRequest;
    }

    public void destory() {
        mBusinessRequest = null;
    }

    public <T> void baseRequest(final RequestLoadListener<T> requestLoadListener, final RequestListener<T> listener, boolean needLogin) {
        if (needLogin) {
            AsyncDataLoader.execute(new AsyncDataLoader.DataLoadCallback<ServiceResponse<T>>() {
                public ServiceResponse<T> load() {
                    return requestLoadListener.load();
                }

                public void postExecute(ServiceResponse<T> result) {
                    BusinessRequest.this.normalPostExecute(result, listener);
                }

                public void preExecute() {
                }
            });
        } else {
            AsyncDataLoader.executeWithNoAutoLogin(new AsyncDataLoader.DataLoadCallback<ServiceResponse<T>>() {
                public ServiceResponse<T> load() {
                    return requestLoadListener.load();
                }

                public void postExecute(ServiceResponse<T> result) {
                    BusinessRequest.this.normalPostExecute(result, listener);
                }

                public void preExecute() {
                }
            });
        }
    }

    public <T> void baseRequest(BaseHttpRequest request, RequestListener<T> listener, boolean needLogin) {
        baseRequest(request, listener, needLogin, false);
    }

    public <T> void baseRequest(final BaseHttpRequest request, final RequestListener<T> listener, boolean needLogin, final boolean post) {
        if (request != null) {
            if (needLogin) {
                AsyncDataLoader.execute(new AsyncDataLoader.DataLoadCallback<ServiceResponse<T>>() {
                    public ServiceResponse<T> load() {
                        return BusinessRequest.this.processHttpRequest(request, post);
                    }

                    public void postExecute(ServiceResponse<T> result) {
                        BusinessRequest.this.normalPostExecute(result, listener);
                    }

                    public void preExecute() {
                    }
                });
            } else {
                AsyncDataLoader.executeWithNoAutoLogin(new AsyncDataLoader.DataLoadCallback<ServiceResponse<T>>() {
                    public ServiceResponse<T> load() {
                        return BusinessRequest.this.processHttpRequest(request, post);
                    }

                    public void postExecute(ServiceResponse<T> result) {
                        BusinessRequest.this.normalPostExecute(result, listener);
                    }

                    public void preExecute() {
                    }
                });
            }
        }
    }

    public <T> void baseRequest(BaseMtopRequest request, RequestListener<T> listener, boolean needLogin) {
        baseRequest(request, listener, needLogin, false);
    }

    public <T> void baseRequest(final BaseMtopRequest request, final RequestListener<T> listener, boolean needLogin, final boolean post) {
        if (request != null) {
            if (needLogin) {
                AsyncDataLoader.execute(new AsyncDataLoader.DataLoadCallback<ServiceResponse<T>>() {
                    public ServiceResponse<T> load() {
                        return BusinessRequest.this.normalLoad(request, post);
                    }

                    public void postExecute(ServiceResponse<T> result) {
                        BusinessRequest.this.normalPostExecute(result, listener);
                    }

                    public void preExecute() {
                    }
                });
            } else {
                AsyncDataLoader.executeWithNoAutoLogin(new AsyncDataLoader.DataLoadCallback<ServiceResponse<T>>() {
                    public ServiceResponse<T> load() {
                        return BusinessRequest.this.normalLoad(request, post);
                    }

                    public void postExecute(ServiceResponse<T> result) {
                        BusinessRequest.this.normalPostExecute(result, listener);
                    }

                    public void preExecute() {
                    }
                });
            }
        }
    }

    /* access modifiers changed from: private */
    /* JADX WARNING: Removed duplicated region for block: B:11:0x01c8  */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public <T> com.yunos.tv.blitz.request.core.ServiceResponse<T> normalLoad(com.yunos.tv.blitz.request.base.BaseMtopRequest r11, boolean r12) {
        /*
            r10 = this;
            r5 = 0
            r11.setParamsData()
            java.lang.String r7 = r10.TAG
            java.lang.StringBuilder r8 = new java.lang.StringBuilder
            r8.<init>()
            java.lang.String r9 = r10.TAG
            java.lang.StringBuilder r8 = r8.append(r9)
            java.lang.String r9 = ".normalLoad.request = "
            java.lang.StringBuilder r8 = r8.append(r9)
            java.lang.StringBuilder r8 = r8.append(r11)
            java.lang.String r9 = ", post = "
            java.lang.StringBuilder r8 = r8.append(r9)
            java.lang.StringBuilder r8 = r8.append(r12)
            java.lang.String r8 = r8.toString()
            com.yunos.tv.blitz.request.common.AppDebug.i(r7, r8)
            mtopsdk.mtop.intf.Mtop r7 = com.yunos.tv.blitz.global.BzAppMain.mMtopInstance
            com.yunos.tv.blitz.global.BzAppConfig r8 = com.yunos.tv.blitz.global.BzAppConfig.getInstance()
            java.lang.String r8 = r8.getTtid()
            mtopsdk.mtop.intf.MtopBuilder r7 = r7.build((mtopsdk.mtop.domain.MtopRequest) r11, (java.lang.String) r8)
            mtopsdk.mtop.intf.MtopBuilder r0 = r7.useWua()
            if (r12 == 0) goto L_0x0047
            mtopsdk.mtop.domain.MethodEnum r7 = mtopsdk.mtop.domain.MethodEnum.POST
            r0.reqMethod(r7)
        L_0x0047:
            mtopsdk.mtop.domain.MtopResponse r2 = r0.syncRequest()
            java.lang.String r7 = r10.TAG
            java.lang.StringBuilder r8 = new java.lang.StringBuilder
            r8.<init>()
            java.lang.String r9 = r10.TAG
            java.lang.StringBuilder r8 = r8.append(r9)
            java.lang.String r9 = ".normalLoad, mtopResponse = "
            java.lang.StringBuilder r8 = r8.append(r9)
            java.lang.StringBuilder r8 = r8.append(r2)
            java.lang.String r8 = r8.toString()
            android.util.Log.i(r7, r8)
            if (r2 == 0) goto L_0x025f
            java.lang.String r7 = r10.TAG
            java.lang.StringBuilder r8 = new java.lang.StringBuilder
            r8.<init>()
            java.lang.String r9 = r10.TAG
            java.lang.StringBuilder r8 = r8.append(r9)
            java.lang.String r9 = ".normalLoad.isApiSuccess = "
            java.lang.StringBuilder r8 = r8.append(r9)
            boolean r9 = r2.isApiSuccess()
            java.lang.StringBuilder r8 = r8.append(r9)
            java.lang.String r9 = ",isNetworkError = "
            java.lang.StringBuilder r8 = r8.append(r9)
            boolean r9 = r2.isNetworkError()
            java.lang.StringBuilder r8 = r8.append(r9)
            java.lang.String r9 = ", isSessionInvalid = "
            java.lang.StringBuilder r8 = r8.append(r9)
            boolean r9 = r2.isSessionInvalid()
            java.lang.StringBuilder r8 = r8.append(r9)
            java.lang.String r9 = ", isSystemError = "
            java.lang.StringBuilder r8 = r8.append(r9)
            boolean r9 = r2.isSystemError()
            java.lang.StringBuilder r8 = r8.append(r9)
            java.lang.String r9 = ", isExpiredRequest = "
            java.lang.StringBuilder r8 = r8.append(r9)
            boolean r9 = r2.isExpiredRequest()
            java.lang.StringBuilder r8 = r8.append(r9)
            java.lang.String r9 = ", is41XResult = "
            java.lang.StringBuilder r8 = r8.append(r9)
            boolean r9 = r2.is41XResult()
            java.lang.StringBuilder r8 = r8.append(r9)
            java.lang.String r9 = ", isApiLockedResult = "
            java.lang.StringBuilder r8 = r8.append(r9)
            boolean r9 = r2.isApiLockedResult()
            java.lang.StringBuilder r8 = r8.append(r9)
            java.lang.String r9 = ", isMtopSdkError = "
            java.lang.StringBuilder r8 = r8.append(r9)
            boolean r9 = r2.isMtopSdkError()
            java.lang.StringBuilder r8 = r8.append(r9)
            java.lang.String r8 = r8.toString()
            com.yunos.tv.blitz.request.common.AppDebug.v(r7, r8)
            java.lang.String r7 = r10.TAG
            java.lang.StringBuilder r8 = new java.lang.StringBuilder
            r8.<init>()
            java.lang.String r9 = r10.TAG
            java.lang.StringBuilder r8 = r8.append(r9)
            java.lang.String r9 = ".normalLoad.getResponseCode = "
            java.lang.StringBuilder r8 = r8.append(r9)
            int r9 = r2.getResponseCode()
            java.lang.StringBuilder r8 = r8.append(r9)
            java.lang.String r8 = r8.toString()
            com.yunos.tv.blitz.request.common.AppDebug.i(r7, r8)
            java.lang.String r7 = r10.TAG
            java.lang.StringBuilder r8 = new java.lang.StringBuilder
            r8.<init>()
            java.lang.String r9 = r10.TAG
            java.lang.StringBuilder r8 = r8.append(r9)
            java.lang.String r9 = ".normalLoad.getRetCode = "
            java.lang.StringBuilder r8 = r8.append(r9)
            java.lang.String r9 = r2.getRetCode()
            java.lang.StringBuilder r8 = r8.append(r9)
            java.lang.String r8 = r8.toString()
            com.yunos.tv.blitz.request.common.AppDebug.i(r7, r8)
            java.lang.String r7 = r10.TAG
            java.lang.StringBuilder r8 = new java.lang.StringBuilder
            r8.<init>()
            java.lang.String r9 = r10.TAG
            java.lang.StringBuilder r8 = r8.append(r9)
            java.lang.String r9 = ".normalLoad.getRetMsg = "
            java.lang.StringBuilder r8 = r8.append(r9)
            java.lang.String r9 = r2.getRetMsg()
            java.lang.StringBuilder r8 = r8.append(r9)
            java.lang.String r8 = r8.toString()
            com.yunos.tv.blitz.request.common.AppDebug.i(r7, r8)
            java.lang.String r7 = r10.TAG
            java.lang.StringBuilder r8 = new java.lang.StringBuilder
            r8.<init>()
            java.lang.String r9 = r10.TAG
            java.lang.StringBuilder r8 = r8.append(r9)
            java.lang.String r9 = ".normalLoad.getFullKey = "
            java.lang.StringBuilder r8 = r8.append(r9)
            java.lang.String r9 = r2.getFullKey()
            java.lang.StringBuilder r8 = r8.append(r9)
            java.lang.String r8 = r8.toString()
            com.yunos.tv.blitz.request.common.AppDebug.i(r7, r8)
            java.lang.String r7 = r10.TAG
            java.lang.StringBuilder r8 = new java.lang.StringBuilder
            r8.<init>()
            java.lang.String r9 = r10.TAG
            java.lang.StringBuilder r8 = r8.append(r9)
            java.lang.String r9 = ".normalLoad, getBytedata = "
            java.lang.StringBuilder r8 = r8.append(r9)
            byte[] r9 = r2.getBytedata()
            java.lang.StringBuilder r8 = r8.append(r9)
            java.lang.String r8 = r8.toString()
            com.yunos.tv.blitz.request.common.AppDebug.i(r7, r8)
            byte[] r7 = r2.getBytedata()     // Catch:{ Exception -> 0x024f }
            if (r7 == 0) goto L_0x020d
            java.lang.String r7 = new java.lang.String     // Catch:{ Exception -> 0x024f }
            byte[] r8 = r2.getBytedata()     // Catch:{ Exception -> 0x024f }
            r7.<init>(r8)     // Catch:{ Exception -> 0x024f }
            com.yunos.tv.blitz.request.core.ServiceResponse r5 = r11.resolveResponse((java.lang.String) r7)     // Catch:{ Exception -> 0x024f }
            java.lang.String r7 = new java.lang.String     // Catch:{ Exception -> 0x024f }
            byte[] r8 = r2.getBytedata()     // Catch:{ Exception -> 0x024f }
            r7.<init>(r8)     // Catch:{ Exception -> 0x024f }
            r5.setRawData(r7)     // Catch:{ Exception -> 0x024f }
        L_0x01c6:
            if (r5 != 0) goto L_0x01ed
            java.lang.String r7 = r10.TAG
            java.lang.StringBuilder r8 = new java.lang.StringBuilder
            r8.<init>()
            java.lang.String r9 = r10.TAG
            java.lang.StringBuilder r8 = r8.append(r9)
            java.lang.String r9 = ".normalLoad.custom serviceResponse"
            java.lang.StringBuilder r8 = r8.append(r9)
            java.lang.String r8 = r8.toString()
            com.yunos.tv.blitz.request.common.AppDebug.i(r7, r8)
            com.yunos.tv.blitz.request.core.ServiceResponse r5 = new com.yunos.tv.blitz.request.core.ServiceResponse
            r5.<init>()
            com.yunos.tv.blitz.request.core.ServiceCode r7 = com.yunos.tv.blitz.request.core.ServiceCode.API_ERROR
            r5.update(r7)
        L_0x01ed:
            java.lang.String r7 = r10.TAG
            java.lang.StringBuilder r8 = new java.lang.StringBuilder
            r8.<init>()
            java.lang.String r9 = r10.TAG
            java.lang.StringBuilder r8 = r8.append(r9)
            java.lang.String r9 = ".normalLoad, serviceResponse = "
            java.lang.StringBuilder r8 = r8.append(r9)
            java.lang.StringBuilder r8 = r8.append(r5)
            java.lang.String r8 = r8.toString()
            android.util.Log.i(r7, r8)
            return r5
        L_0x020d:
            java.lang.String r3 = r2.getRetCode()     // Catch:{ Exception -> 0x024f }
            java.lang.String r7 = r10.TAG     // Catch:{ Exception -> 0x024f }
            java.lang.StringBuilder r8 = new java.lang.StringBuilder     // Catch:{ Exception -> 0x024f }
            r8.<init>()     // Catch:{ Exception -> 0x024f }
            java.lang.String r9 = r10.TAG     // Catch:{ Exception -> 0x024f }
            java.lang.StringBuilder r8 = r8.append(r9)     // Catch:{ Exception -> 0x024f }
            java.lang.String r9 = ".normalLoad.retCode = "
            java.lang.StringBuilder r8 = r8.append(r9)     // Catch:{ Exception -> 0x024f }
            java.lang.StringBuilder r8 = r8.append(r3)     // Catch:{ Exception -> 0x024f }
            java.lang.String r8 = r8.toString()     // Catch:{ Exception -> 0x024f }
            com.yunos.tv.blitz.request.common.AppDebug.i(r7, r8)     // Catch:{ Exception -> 0x024f }
            boolean r7 = android.text.TextUtils.isEmpty(r3)     // Catch:{ Exception -> 0x024f }
            if (r7 != 0) goto L_0x01c6
            com.yunos.tv.blitz.request.core.ServiceCode r4 = com.yunos.tv.blitz.request.core.ServiceCode.valueOf(r3)     // Catch:{ Exception -> 0x024f }
            if (r4 == 0) goto L_0x01c6
            com.yunos.tv.blitz.request.core.ServiceResponse r6 = new com.yunos.tv.blitz.request.core.ServiceResponse     // Catch:{ Exception -> 0x024f }
            r6.<init>()     // Catch:{ Exception -> 0x024f }
            int r7 = r4.getCode()     // Catch:{ Exception -> 0x026b }
            java.lang.String r8 = r4.getMsg()     // Catch:{ Exception -> 0x026b }
            r6.update(r7, r3, r8)     // Catch:{ Exception -> 0x026b }
            r5 = r6
            goto L_0x01c6
        L_0x024f:
            r1 = move-exception
        L_0x0250:
            com.yunos.tv.blitz.request.core.ServiceResponse r5 = new com.yunos.tv.blitz.request.core.ServiceResponse
            r5.<init>()
            com.yunos.tv.blitz.request.core.ServiceCode r7 = com.yunos.tv.blitz.request.core.ServiceCode.DATA_PARSE_ERROR
            r5.update(r7)
            r1.printStackTrace()
            goto L_0x01c6
        L_0x025f:
            com.yunos.tv.blitz.request.core.ServiceResponse r5 = new com.yunos.tv.blitz.request.core.ServiceResponse
            r5.<init>()
            com.yunos.tv.blitz.request.core.ServiceCode r7 = com.yunos.tv.blitz.request.core.ServiceCode.HTTP_ERROR
            r5.update(r7)
            goto L_0x01c6
        L_0x026b:
            r1 = move-exception
            r5 = r6
            goto L_0x0250
        */
        throw new UnsupportedOperationException("Method not decompiled: com.yunos.tv.blitz.request.BusinessRequest.normalLoad(com.yunos.tv.blitz.request.base.BaseMtopRequest, boolean):com.yunos.tv.blitz.request.core.ServiceResponse");
    }

    /* access modifiers changed from: private */
    public <T> void normalPostExecute(ServiceResponse<T> result, RequestListener<T> listener) {
        if (listener != null) {
            listener.onRequestDone(result.getData(), result.getStatusCode().intValue(), result.getErrorMsg(), result.getRetArray(), result.getRawData());
        }
    }

    public <T> ServiceResponse<T> processHttpRequest(BaseHttpRequest theRequest, boolean post) {
        ServiceResponse<T> resolveResponse;
        Network network = new DegradableNetwork(BzAppConfig.context.getContext());
        String url = theRequest.getUrl();
        AppDebug.v(this.TAG, this.TAG + ".processHttpRequest.url = " + url + ", post = " + post);
        Response response = null;
        if (!TextUtils.isEmpty(url)) {
            RequestImpl request = new RequestImpl(url);
            if (!(theRequest == null || theRequest.getHttpHeader() == null || theRequest.getHttpHeader().isEmpty())) {
                request.setHeaders(theRequest.getHttpHeader());
            }
            response = network.syncSend(request, (Object) null);
            AppDebug.v(this.TAG, this.TAG + ".processHttpRequest.request = " + request);
            AppDebug.v(this.TAG, this.TAG + ".processHttpRequest.response = " + response);
        }
        byte[] data = null;
        if (response != null) {
            data = response.getBytedata();
        }
        if (data != null) {
            try {
                BufferedReader reader = new BufferedReader(new InputStreamReader(new ByteArrayInputStream(data), theRequest.getResponseEncode()));
                StringBuilder sb = new StringBuilder();
                while (true) {
                    String inputLine = reader.readLine();
                    if (inputLine == null) {
                        break;
                    }
                    sb.append(inputLine);
                }
                resolveResponse = theRequest.resolveResponse(sb.toString());
            } catch (Exception e) {
                resolveResponse = new ServiceResponse<>();
                resolveResponse.update(ServiceCode.DATA_PARSE_ERROR);
                e.printStackTrace();
            }
        } else {
            resolveResponse = new ServiceResponse<>();
            resolveResponse.update(ServiceCode.DATA_PARSE_ERROR);
        }
        AppDebug.v(this.TAG, this.TAG + ".processHttpRequest.resolveResponse = " + resolveResponse);
        return resolveResponse;
    }
}
