package android.taobao.windvane.extra.jsbridge;

import android.graphics.Bitmap;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.taobao.windvane.WindVaneSDKForTB;
import android.taobao.windvane.cache.WVCacheManager;
import android.taobao.windvane.connect.HttpConnectListener;
import android.taobao.windvane.extra.upload.UploadFileConnection;
import android.taobao.windvane.extra.upload.UploadFileData;
import android.taobao.windvane.file.FileManager;
import android.taobao.windvane.jsbridge.WVCallBackContext;
import android.taobao.windvane.jsbridge.WVResult;
import android.taobao.windvane.jsbridge.api.WVCamera;
import android.taobao.windvane.jsbridge.api.WVUploadService;
import android.taobao.windvane.jsbridge.utils.WVUtils;
import android.taobao.windvane.thread.WVThreadPool;
import android.taobao.windvane.util.ImageTool;
import android.taobao.windvane.util.MimeTypeEnum;
import android.taobao.windvane.util.TaoLog;
import android.taobao.windvane.util.WVNativeCallbackUtil;
import java.io.File;
import java.io.IOException;
import mtopsdk.mtop.upload.FileUploadBaseListener;
import mtopsdk.mtop.upload.FileUploadMgr;
import mtopsdk.mtop.upload.domain.UploadFileInfo;

public class TBUploadService extends WVUploadService implements Handler.Callback {
    private static final String LAST_PIC = "\"isLastPic\":\"true\"";
    private static final String MUTI_SELECTION = "\"mutipleSelection\":\"1\"";
    private static final int NOTIFY_ERROR = 2003;
    private static final int NOTIFY_FINISH = 2002;
    private static final int NOTIFY_START = 2001;
    private static final String TAG = "TBUploadService";
    private WVCallBackContext mCallback;
    /* access modifiers changed from: private */
    public Handler mHandler;

    public TBUploadService() {
        this.mHandler = null;
        this.mHandler = new Handler(Looper.getMainLooper(), this);
    }

    public void doUpload(final WVCamera.UploadParams params, WVCallBackContext callback) {
        if (params == null) {
            TaoLog.d(TAG, "UploadParams is null.");
            callback.error(new WVResult());
            return;
        }
        this.mCallback = callback;
        if ("2.0".equals(params.v)) {
            if (WindVaneSDKForTB.wvAdapter != null) {
                WindVaneSDKForTB.wvAdapter.getLoginInfo((Handler) null);
            }
            WVThreadPool.getInstance().execute(new Runnable() {
                public void run() {
                    TBUploadService.this.doMtopUpload(params);
                }
            });
            return;
        }
        doNormalUpload(params);
    }

    private void doNormalUpload(final WVCamera.UploadParams params) {
        WVThreadPool.getInstance().execute(new UploadFileConnection(params.filePath, MimeTypeEnum.JPG.getSuffix(), new HttpConnectListener<UploadFileData>() {
            public void onStart() {
                TBUploadService.this.mHandler.sendEmptyMessage(2001);
            }

            public void onFinish(UploadFileData data, int token) {
                Bitmap pic;
                if (data != null) {
                    Message msg = Message.obtain();
                    msg.what = 2002;
                    WVResult result = new WVResult();
                    result.setSuccess();
                    if (params.needBase64 && (pic = ImageTool.readZoomImage(params.filePath, 1024)) != null) {
                        result.addData("base64Data", WVUtils.bitmapToBase64(pic));
                    }
                    result.addData("url", params.localUrl);
                    result.addData("localPath", params.filePath);
                    result.addData("resourceURL", data.resourceUri);
                    result.addData("isLastPic", String.valueOf(params.isLastPic));
                    result.addData("mutipleSelection", params.mutipleSelection);
                    result.addData("tfsKey", data.tfsKey);
                    if (params.isLastPic) {
                        result.addData("images", params.images);
                    }
                    msg.obj = result;
                    TBUploadService.this.mHandler.sendMessage(msg);
                }
            }

            public void onError(int code, String message) {
                if (TaoLog.getLogStatus()) {
                    TaoLog.d(TBUploadService.TAG, "upload file error. code: " + code + ";msg: " + message);
                }
                WVResult result = new WVResult();
                result.addData("localPath", params.filePath);
                result.addData("isLastPic", String.valueOf(params.isLastPic));
                result.addData("mutipleSelection", params.mutipleSelection);
                Message msg = Message.obtain();
                msg.what = 2003;
                msg.obj = result;
                TBUploadService.this.mHandler.sendMessage(msg);
            }
        }));
    }

    /* access modifiers changed from: private */
    public void doMtopUpload(final WVCamera.UploadParams params) {
        try {
            File tmpFile = File.createTempFile("windvane", "." + MimeTypeEnum.JPG.getSuffix(), WVCacheManager.getInstance().getTempDir(true));
            if (!FileManager.copy(new File(params.filePath), tmpFile)) {
                WVResult result = new WVResult();
                result.addData("errorInfo", "Failed to copy file!");
                this.mCallback.error(result);
                return;
            }
            try {
                UploadFileInfo uploadFileInfo = new UploadFileInfo();
                uploadFileInfo.setFilePath(tmpFile.getAbsolutePath());
                uploadFileInfo.setBizCode(params.bizCode);
                uploadFileInfo.setPrivateData(params.extraData);
                final WVResult result2 = new WVResult();
                result2.addData("identifier", params.identifier);
                result2.addData("isLastPic", String.valueOf(params.isLastPic));
                result2.addData("mutipleSelection", params.mutipleSelection);
                FileUploadMgr.getInstance().addTask(uploadFileInfo, new FileUploadBaseListener() {
                    public void onStart() {
                        result2.setResult("");
                        Message.obtain(TBUploadService.this.mHandler, 2001, result2).sendToTarget();
                    }

                    public void onError(String errCode, String errMsg) {
                        if (TaoLog.getLogStatus()) {
                            TaoLog.w(TBUploadService.TAG, "mtop upload file error. code: " + errCode + ";msg: " + errMsg);
                        }
                        result2.addData("errorCode", errCode);
                        result2.addData("errorMsg", errMsg);
                        result2.addData("localPath", params.filePath);
                        Message.obtain(TBUploadService.this.mHandler, 2003, result2).sendToTarget();
                    }

                    public void onFinish(UploadFileInfo fileInfo, String urlLocation) {
                        Bitmap pic;
                        result2.setSuccess();
                        result2.addData("url", params.localUrl);
                        result2.addData("localPath", params.filePath);
                        result2.addData("resourceURL", urlLocation);
                        if (params.needBase64 && (pic = ImageTool.readZoomImage(params.filePath, 1024)) != null) {
                            result2.addData("base64Data", WVUtils.bitmapToBase64(pic));
                        }
                        int index = urlLocation.lastIndexOf(WVNativeCallbackUtil.SEPERATER) + 1;
                        if (index != 0) {
                            result2.addData("tfsKey", urlLocation.substring(index));
                        }
                        if (params.isLastPic) {
                            result2.addData("images", params.images);
                        }
                        Message.obtain(TBUploadService.this.mHandler, 2002, result2).sendToTarget();
                    }

                    public void onFinish(String urlLocation) {
                        result2.setSuccess();
                        result2.addData("url", params.localUrl);
                        result2.addData("localPath", params.filePath);
                        result2.addData("resourceURL", urlLocation);
                        int index = urlLocation.lastIndexOf(WVNativeCallbackUtil.SEPERATER) + 1;
                        if (index != 0) {
                            result2.addData("tfsKey", urlLocation.substring(index));
                        }
                        if (params.isLastPic) {
                            result2.addData("images", params.images);
                        }
                        Message.obtain(TBUploadService.this.mHandler, 2002, result2).sendToTarget();
                    }

                    public void onError(String errType, String errCode, String errMsg) {
                        if (TaoLog.getLogStatus()) {
                            TaoLog.w(TBUploadService.TAG, "mtop upload file error. code: " + errCode + ";msg: " + errMsg + ";type: " + errType);
                        }
                        result2.addData("errorType", errType);
                        result2.addData("errorCode", errCode);
                        result2.addData("errorMsg", errMsg);
                        result2.addData("localPath", params.filePath);
                        Message.obtain(TBUploadService.this.mHandler, 2003, result2).sendToTarget();
                    }

                    public void onProgress(int arg0) {
                    }
                }, params.needLogin);
            } catch (Throwable e) {
                TaoLog.e(TAG, "mtop sdk not exist." + e.getMessage());
            }
        } catch (IOException e2) {
            e2.printStackTrace();
        }
    }

    /* JADX WARNING: Removed duplicated region for block: B:22:0x00be A[Catch:{ JSONException -> 0x00ec }] */
    /* JADX WARNING: Removed duplicated region for block: B:25:0x00cd  */
    /* JADX WARNING: Removed duplicated region for block: B:32:0x00f1  */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public boolean handleMessage(android.os.Message r18) {
        /*
            r17 = this;
            r0 = r18
            int r14 = r0.what
            switch(r14) {
                case 2001: goto L_0x0009;
                case 2002: goto L_0x0021;
                case 2003: goto L_0x0129;
                default: goto L_0x0007;
            }
        L_0x0007:
            r14 = 0
        L_0x0008:
            return r14
        L_0x0009:
            java.lang.String r14 = "TBUploadService"
            java.lang.String r15 = "start upload file ..."
            android.taobao.windvane.util.TaoLog.d(r14, r15)
            r0 = r17
            android.taobao.windvane.jsbridge.WVCallBackContext r14 = r0.mCallback
            java.lang.String r15 = "WVPhoto.Event.prepareUploadPhotoSuccess"
            java.lang.String r16 = "{}"
            r14.fireEvent(r15, r16)
            r14 = 1
            goto L_0x0008
        L_0x0021:
            r0 = r18
            java.lang.Object r14 = r0.obj
            if (r14 == 0) goto L_0x00e4
            boolean r14 = android.taobao.windvane.util.TaoLog.getLogStatus()
            if (r14 == 0) goto L_0x0055
            java.lang.String r15 = "TBUploadService"
            java.lang.StringBuilder r14 = new java.lang.StringBuilder
            r14.<init>()
            java.lang.String r16 = "upload file success, retString: "
            r0 = r16
            java.lang.StringBuilder r16 = r14.append(r0)
            r0 = r18
            java.lang.Object r14 = r0.obj
            android.taobao.windvane.jsbridge.WVResult r14 = (android.taobao.windvane.jsbridge.WVResult) r14
            java.lang.String r14 = r14.toJsonString()
            r0 = r16
            java.lang.StringBuilder r14 = r0.append(r14)
            java.lang.String r14 = r14.toString()
            android.taobao.windvane.util.TaoLog.d(r15, r14)
        L_0x0055:
            r0 = r18
            java.lang.Object r9 = r0.obj
            android.taobao.windvane.jsbridge.WVResult r9 = (android.taobao.windvane.jsbridge.WVResult) r9
            java.lang.String r2 = r9.toJsonString()
            r4 = 0
            r13 = 0
            r8 = 0
            r12 = 0
            r7 = 0
            r1 = 0
            r5 = 0
            org.json.JSONObject r6 = new org.json.JSONObject     // Catch:{ JSONException -> 0x00e7 }
            r6.<init>(r2)     // Catch:{ JSONException -> 0x00e7 }
            java.lang.String r14 = "images"
            org.json.JSONArray r4 = r6.optJSONArray(r14)     // Catch:{ JSONException -> 0x016f }
            java.lang.String r14 = "url"
            java.lang.String r13 = r6.optString(r14)     // Catch:{ JSONException -> 0x016f }
            java.lang.String r14 = "resourceURL"
            java.lang.String r8 = r6.optString(r14)     // Catch:{ JSONException -> 0x016f }
            java.lang.String r14 = "localPath"
            java.lang.String r7 = r6.optString(r14)     // Catch:{ JSONException -> 0x016f }
            java.lang.String r14 = "tfsKey"
            java.lang.String r12 = r6.optString(r14)     // Catch:{ JSONException -> 0x016f }
            java.lang.String r14 = "base64Data"
            boolean r14 = r6.has(r14)     // Catch:{ JSONException -> 0x016f }
            if (r14 == 0) goto L_0x009e
            java.lang.String r14 = "base64Data"
            java.lang.String r1 = r6.optString(r14)     // Catch:{ JSONException -> 0x016f }
        L_0x009e:
            r5 = r6
        L_0x009f:
            org.json.JSONObject r10 = new org.json.JSONObject
            r10.<init>()
            java.lang.String r14 = "url"
            r10.put(r14, r13)     // Catch:{ JSONException -> 0x00ec }
            java.lang.String r14 = "resourceURL"
            r10.put(r14, r8)     // Catch:{ JSONException -> 0x00ec }
            java.lang.String r14 = "localPath"
            r10.put(r14, r7)     // Catch:{ JSONException -> 0x00ec }
            java.lang.String r14 = "tfsKey"
            r10.put(r14, r12)     // Catch:{ JSONException -> 0x00ec }
            if (r1 == 0) goto L_0x00c4
            java.lang.String r14 = "base64Data"
            r10.put(r14, r1)     // Catch:{ JSONException -> 0x00ec }
        L_0x00c4:
            java.lang.String r14 = "\"mutipleSelection\":\"1\""
            boolean r14 = r2.contains(r14)
            if (r14 != 0) goto L_0x00f1
            r0 = r17
            android.taobao.windvane.jsbridge.WVCallBackContext r14 = r0.mCallback
            java.lang.String r15 = r10.toString()
            r14.success((java.lang.String) r15)
        L_0x00d8:
            android.taobao.windvane.cache.WVCacheManager r14 = android.taobao.windvane.cache.WVCacheManager.getInstance()
            r15 = 1
            java.io.File r14 = r14.getTempDir(r15)
            android.taobao.windvane.file.FileAccesser.deleteFile((java.io.File) r14)
        L_0x00e4:
            r14 = 1
            goto L_0x0008
        L_0x00e7:
            r3 = move-exception
        L_0x00e8:
            r3.printStackTrace()
            goto L_0x009f
        L_0x00ec:
            r3 = move-exception
            r3.printStackTrace()
            goto L_0x00c4
        L_0x00f1:
            java.lang.String r14 = "\"isLastPic\":\"true\""
            boolean r14 = r2.contains(r14)
            if (r14 == 0) goto L_0x0107
            if (r4 != 0) goto L_0x0116
            r0 = r17
            android.taobao.windvane.jsbridge.WVCallBackContext r14 = r0.mCallback
            java.lang.String r15 = r10.toString()
            r14.success((java.lang.String) r15)
        L_0x0107:
            r0 = r17
            android.taobao.windvane.jsbridge.WVCallBackContext r14 = r0.mCallback
            java.lang.String r15 = "WVPhoto.Event.uploadPhotoSuccess"
            java.lang.String r16 = r10.toString()
            r14.fireEvent(r15, r16)
            goto L_0x00d8
        L_0x0116:
            android.taobao.windvane.jsbridge.WVResult r11 = new android.taobao.windvane.jsbridge.WVResult
            r11.<init>()
            java.lang.String r14 = "images"
            r11.addData((java.lang.String) r14, (org.json.JSONArray) r4)
            r0 = r17
            android.taobao.windvane.jsbridge.WVCallBackContext r14 = r0.mCallback
            r14.success((android.taobao.windvane.jsbridge.WVResult) r11)
            goto L_0x0107
        L_0x0129:
            r0 = r18
            java.lang.Object r14 = r0.obj
            if (r14 == 0) goto L_0x0167
            r0 = r18
            java.lang.Object r9 = r0.obj
            android.taobao.windvane.jsbridge.WVResult r9 = (android.taobao.windvane.jsbridge.WVResult) r9
            java.lang.String r2 = r9.toJsonString()
            java.lang.String r14 = "\"mutipleSelection\":\"1\""
            boolean r14 = r2.contains(r14)
            if (r14 == 0) goto L_0x015f
            r0 = r17
            android.taobao.windvane.jsbridge.WVCallBackContext r14 = r0.mCallback
            java.lang.String r15 = "WVPhoto.Event.uploadPhotoFailed"
            r14.fireEvent(r15, r2)
            java.lang.String r14 = "\"isLastPic\":\"true\""
            boolean r14 = r2.contains(r14)
            if (r14 == 0) goto L_0x015c
            r0 = r17
            android.taobao.windvane.jsbridge.WVCallBackContext r14 = r0.mCallback
            r14.error((android.taobao.windvane.jsbridge.WVResult) r9)
        L_0x015c:
            r14 = 1
            goto L_0x0008
        L_0x015f:
            r0 = r17
            android.taobao.windvane.jsbridge.WVCallBackContext r14 = r0.mCallback
            r14.error((android.taobao.windvane.jsbridge.WVResult) r9)
            goto L_0x015c
        L_0x0167:
            r0 = r17
            android.taobao.windvane.jsbridge.WVCallBackContext r14 = r0.mCallback
            r14.error()
            goto L_0x015c
        L_0x016f:
            r3 = move-exception
            r5 = r6
            goto L_0x00e8
        */
        throw new UnsupportedOperationException("Method not decompiled: android.taobao.windvane.extra.jsbridge.TBUploadService.handleMessage(android.os.Message):boolean");
    }
}
