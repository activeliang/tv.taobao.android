package android.taobao.windvane.extra.upload;

import android.taobao.windvane.connect.HttpConnectListener;

public class UploadFileConnection implements Runnable {
    private static final int DEFAULT_CONNECT_TIMEOUT = 60000;
    private static final int DEFAULT_READ_TIMEOUT = 60000;
    public static final int ERROE_CODE_FAIL = 1;
    public static final String ERROE_MSG_FAIL = "FAIL";
    public static final int ERR_CODE_TOKEN_INVALID = 2;
    public static final String ERR_MSG_TOKEN_INVALID = "TOKEN_IS_INVALID";
    private static final String TAG = "UploadFileConnection";
    private String accessToken;
    private String mFileExt;
    private String mFilePath;
    private HttpConnectListener<UploadFileData> mListener;
    private int tryNum;

    public UploadFileConnection(String filePath, String fileExt, HttpConnectListener<UploadFileData> listener) {
        this.mListener = listener;
        this.mFilePath = filePath;
        this.mFileExt = fileExt;
    }

    /* JADX WARNING: Removed duplicated region for block: B:19:0x008b  */
    /* JADX WARNING: Removed duplicated region for block: B:27:0x00ad  */
    /* JADX WARNING: Removed duplicated region for block: B:50:0x0116 A[Catch:{ JSONException -> 0x0188 }] */
    /* JADX WARNING: Removed duplicated region for block: B:57:0x0190  */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public void run() {
        /*
            r15 = this;
            java.lang.StringBuilder r12 = new java.lang.StringBuilder
            r12.<init>()
            java.lang.String r13 = "wv-a-8.0.0-"
            java.lang.StringBuilder r12 = r12.append(r13)
            android.app.Application r13 = android.taobao.windvane.config.GlobalConfig.context
            java.lang.String r13 = android.taobao.windvane.util.PhoneInfo.getImei(r13)
            java.lang.StringBuilder r12 = r12.append(r13)
            java.lang.String r9 = r12.toString()
            android.taobao.windvane.connect.HttpConnector r12 = new android.taobao.windvane.connect.HttpConnector
            r12.<init>()
            java.lang.String r13 = android.taobao.windvane.extra.mtop.ApiUrlManager.getUploadTokenUrl(r9)
            android.taobao.windvane.connect.HttpResponse r6 = r12.syncConnect((java.lang.String) r13)
            boolean r12 = r6.isSuccess()
            if (r12 == 0) goto L_0x0083
            byte[] r12 = r6.getData()
            if (r12 == 0) goto L_0x0083
            r7 = 0
            java.lang.String r8 = new java.lang.String     // Catch:{ UnsupportedEncodingException -> 0x00a8 }
            byte[] r12 = r6.getData()     // Catch:{ UnsupportedEncodingException -> 0x00a8 }
            java.lang.String r13 = "utf-8"
            r8.<init>(r12, r13)     // Catch:{ UnsupportedEncodingException -> 0x00a8 }
            boolean r12 = android.taobao.windvane.util.TaoLog.getLogStatus()     // Catch:{ UnsupportedEncodingException -> 0x01a7 }
            if (r12 == 0) goto L_0x0060
            java.lang.String r12 = "UploadFileConnection"
            java.lang.StringBuilder r13 = new java.lang.StringBuilder     // Catch:{ UnsupportedEncodingException -> 0x01a7 }
            r13.<init>()     // Catch:{ UnsupportedEncodingException -> 0x01a7 }
            java.lang.String r14 = "get upload token success, content="
            java.lang.StringBuilder r13 = r13.append(r14)     // Catch:{ UnsupportedEncodingException -> 0x01a7 }
            java.lang.StringBuilder r13 = r13.append(r8)     // Catch:{ UnsupportedEncodingException -> 0x01a7 }
            java.lang.String r13 = r13.toString()     // Catch:{ UnsupportedEncodingException -> 0x01a7 }
            android.taobao.windvane.util.TaoLog.d(r12, r13)     // Catch:{ UnsupportedEncodingException -> 0x01a7 }
        L_0x0060:
            r7 = r8
        L_0x0061:
            android.taobao.windvane.connect.api.ApiResponse r2 = new android.taobao.windvane.connect.api.ApiResponse
            r2.<init>()
            r2.parseResult(r7)
            boolean r12 = r2.success
            if (r12 == 0) goto L_0x0083
            org.json.JSONObject r1 = r2.data
            if (r1 == 0) goto L_0x0083
            java.lang.String r12 = "accessToken"
            java.lang.String r12 = r1.optString(r12)
            r15.accessToken = r12
            java.lang.String r12 = "tryNum"
            int r12 = r1.optInt(r12)
            r15.tryNum = r12
        L_0x0083:
            java.lang.String r12 = r15.accessToken
            boolean r12 = android.text.TextUtils.isEmpty(r12)
            if (r12 == 0) goto L_0x00ad
            boolean r12 = android.taobao.windvane.util.TaoLog.getLogStatus()
            if (r12 == 0) goto L_0x009a
            java.lang.String r12 = "UploadFileConnection"
            java.lang.String r13 = "get upload token fail, accessToken is empty"
            android.taobao.windvane.util.TaoLog.d(r12, r13)
        L_0x009a:
            android.taobao.windvane.connect.HttpConnectListener<android.taobao.windvane.extra.upload.UploadFileData> r12 = r15.mListener
            if (r12 == 0) goto L_0x00a7
            android.taobao.windvane.connect.HttpConnectListener<android.taobao.windvane.extra.upload.UploadFileData> r12 = r15.mListener
            r13 = 2
            java.lang.String r14 = "TOKEN_IS_INVALID"
            r12.onError(r13, r14)
        L_0x00a7:
            return
        L_0x00a8:
            r0 = move-exception
        L_0x00a9:
            r0.printStackTrace()
            goto L_0x0061
        L_0x00ad:
            android.taobao.windvane.connect.HttpConnectListener<android.taobao.windvane.extra.upload.UploadFileData> r12 = r15.mListener
            if (r12 == 0) goto L_0x00b6
            android.taobao.windvane.connect.HttpConnectListener<android.taobao.windvane.extra.upload.UploadFileData> r12 = r15.mListener
            r12.onStart()
        L_0x00b6:
            java.lang.String r12 = r15.accessToken
            java.lang.String r12 = android.taobao.windvane.extra.mtop.ApiUrlManager.getUploadUrl(r9, r12)
            java.lang.String r13 = r15.mFilePath
            android.taobao.windvane.connect.HttpResponse r3 = r15.uploadFile(r12, r13)
            boolean r12 = r3.isSuccess()
            if (r12 == 0) goto L_0x018c
            byte[] r12 = r3.getData()
            if (r12 == 0) goto L_0x018c
            r4 = 0
            java.lang.String r5 = new java.lang.String     // Catch:{ UnsupportedEncodingException -> 0x019e }
            byte[] r12 = r3.getData()     // Catch:{ UnsupportedEncodingException -> 0x019e }
            java.lang.String r13 = "utf-8"
            r5.<init>(r12, r13)     // Catch:{ UnsupportedEncodingException -> 0x019e }
            boolean r12 = android.taobao.windvane.util.TaoLog.getLogStatus()     // Catch:{ UnsupportedEncodingException -> 0x01a4 }
            if (r12 == 0) goto L_0x00fb
            java.lang.String r12 = "UploadFileConnection"
            java.lang.StringBuilder r13 = new java.lang.StringBuilder     // Catch:{ UnsupportedEncodingException -> 0x01a4 }
            r13.<init>()     // Catch:{ UnsupportedEncodingException -> 0x01a4 }
            java.lang.String r14 = "upload file success, response: "
            java.lang.StringBuilder r13 = r13.append(r14)     // Catch:{ UnsupportedEncodingException -> 0x01a4 }
            java.lang.StringBuilder r13 = r13.append(r5)     // Catch:{ UnsupportedEncodingException -> 0x01a4 }
            java.lang.String r13 = r13.toString()     // Catch:{ UnsupportedEncodingException -> 0x01a4 }
            android.taobao.windvane.util.TaoLog.d(r12, r13)     // Catch:{ UnsupportedEncodingException -> 0x01a4 }
        L_0x00fb:
            r4 = r5
        L_0x00fc:
            android.taobao.windvane.connect.api.ApiResponse r2 = new android.taobao.windvane.connect.api.ApiResponse
            r2.<init>()
            android.taobao.windvane.connect.api.ApiResponse r2 = r2.parseResult(r4)
            boolean r12 = r2.success
            if (r12 == 0) goto L_0x018c
            org.json.JSONObject r1 = r2.data
            if (r1 == 0) goto L_0x018c
            java.lang.String r12 = "status"
            boolean r12 = r1.getBoolean(r12)     // Catch:{ JSONException -> 0x0188 }
            if (r12 == 0) goto L_0x018c
            java.lang.String r12 = "uploadInfo"
            org.json.JSONObject r11 = r1.getJSONObject(r12)     // Catch:{ JSONException -> 0x0188 }
            android.taobao.windvane.extra.upload.UploadFileData r10 = new android.taobao.windvane.extra.upload.UploadFileData     // Catch:{ JSONException -> 0x0188 }
            r10.<init>()     // Catch:{ JSONException -> 0x0188 }
            java.lang.String r12 = "accessToken"
            java.lang.String r12 = r11.getString(r12)     // Catch:{ JSONException -> 0x0188 }
            r10.accessToken = r12     // Catch:{ JSONException -> 0x0188 }
            java.lang.String r12 = "blockNum"
            int r12 = r11.getInt(r12)     // Catch:{ JSONException -> 0x0188 }
            r10.blockNum = r12     // Catch:{ JSONException -> 0x0188 }
            java.lang.String r12 = "fileExt"
            java.lang.String r12 = r11.getString(r12)     // Catch:{ JSONException -> 0x0188 }
            r10.fileExt = r12     // Catch:{ JSONException -> 0x0188 }
            java.lang.String r12 = "fileName"
            java.lang.String r12 = r11.getString(r12)     // Catch:{ JSONException -> 0x0188 }
            r10.fileName = r12     // Catch:{ JSONException -> 0x0188 }
            java.lang.String r12 = "fileSize"
            long r12 = r11.getLong(r12)     // Catch:{ JSONException -> 0x0188 }
            r10.fileSize = r12     // Catch:{ JSONException -> 0x0188 }
            java.lang.String r12 = "resourceUri"
            java.lang.String r12 = r11.getString(r12)     // Catch:{ JSONException -> 0x0188 }
            r10.resourceUri = r12     // Catch:{ JSONException -> 0x0188 }
            java.lang.String r12 = "tfsKey"
            java.lang.String r12 = r11.getString(r12)     // Catch:{ JSONException -> 0x0188 }
            r10.tfsKey = r12     // Catch:{ JSONException -> 0x0188 }
            java.lang.String r12 = "tryNum"
            int r12 = r11.getInt(r12)     // Catch:{ JSONException -> 0x0188 }
            r10.tryNum = r12     // Catch:{ JSONException -> 0x0188 }
            java.lang.String r12 = "validTime"
            long r12 = r11.getLong(r12)     // Catch:{ JSONException -> 0x0188 }
            r10.validTime = r12     // Catch:{ JSONException -> 0x0188 }
            java.lang.String r12 = "finish"
            boolean r12 = r11.getBoolean(r12)     // Catch:{ JSONException -> 0x0188 }
            r10.finish = r12     // Catch:{ JSONException -> 0x0188 }
            android.taobao.windvane.connect.HttpConnectListener<android.taobao.windvane.extra.upload.UploadFileData> r12 = r15.mListener     // Catch:{ JSONException -> 0x0188 }
            if (r12 == 0) goto L_0x00a7
            android.taobao.windvane.connect.HttpConnectListener<android.taobao.windvane.extra.upload.UploadFileData> r12 = r15.mListener     // Catch:{ JSONException -> 0x0188 }
            r13 = 0
            r12.onFinish(r10, r13)     // Catch:{ JSONException -> 0x0188 }
            goto L_0x00a7
        L_0x0188:
            r0 = move-exception
            r0.printStackTrace()
        L_0x018c:
            android.taobao.windvane.connect.HttpConnectListener<android.taobao.windvane.extra.upload.UploadFileData> r12 = r15.mListener
            if (r12 == 0) goto L_0x0199
            android.taobao.windvane.connect.HttpConnectListener<android.taobao.windvane.extra.upload.UploadFileData> r12 = r15.mListener
            r13 = 1
            java.lang.String r14 = "FAIL"
            r12.onError(r13, r14)
        L_0x0199:
            r12 = 0
            r15.mListener = r12
            goto L_0x00a7
        L_0x019e:
            r0 = move-exception
        L_0x019f:
            r0.printStackTrace()
            goto L_0x00fc
        L_0x01a4:
            r0 = move-exception
            r4 = r5
            goto L_0x019f
        L_0x01a7:
            r0 = move-exception
            r7 = r8
            goto L_0x00a9
        */
        throw new UnsupportedOperationException("Method not decompiled: android.taobao.windvane.extra.upload.UploadFileConnection.run():void");
    }

    /* JADX WARNING: type inference failed for: r19v5, types: [java.net.URLConnection] */
    /* JADX WARNING: Code restructure failed: missing block: B:21:0x015e, code lost:
        if (android.taobao.windvane.util.TaoLog.getLogStatus() == false) goto L_0x0169;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:22:0x0160, code lost:
        android.taobao.windvane.util.TaoLog.d(TAG, "upload file fail.");
     */
    /* JADX WARNING: Code restructure failed: missing block: B:83:?, code lost:
        return new android.taobao.windvane.connect.HttpResponse();
     */
    /* JADX WARNING: Multi-variable type inference failed */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public final android.taobao.windvane.connect.HttpResponse uploadFile(java.lang.String r23, java.lang.String r24) {
        /*
            r22 = this;
            android.taobao.windvane.connect.HttpResponse r16 = new android.taobao.windvane.connect.HttpResponse
            r16.<init>()
            r7 = 0
            r10 = 0
            java.io.ByteArrayOutputStream r2 = new java.io.ByteArrayOutputStream
            r2.<init>()
            java.io.File r13 = new java.io.File     // Catch:{ IOException -> 0x012e, Exception -> 0x01ec }
            r0 = r24
            r13.<init>(r0)     // Catch:{ IOException -> 0x012e, Exception -> 0x01ec }
            java.net.URL r18 = new java.net.URL     // Catch:{ IOException -> 0x012e, Exception -> 0x01ec }
            r0 = r18
            r1 = r23
            r0.<init>(r1)     // Catch:{ IOException -> 0x012e, Exception -> 0x01ec }
            java.net.URLConnection r19 = r18.openConnection()     // Catch:{ IOException -> 0x012e, Exception -> 0x01ec }
            r0 = r19
            java.net.HttpURLConnection r0 = (java.net.HttpURLConnection) r0     // Catch:{ IOException -> 0x012e, Exception -> 0x01ec }
            r7 = r0
            r19 = 60000(0xea60, float:8.4078E-41)
            r0 = r19
            r7.setReadTimeout(r0)     // Catch:{ IOException -> 0x012e, Exception -> 0x01ec }
            r19 = 60000(0xea60, float:8.4078E-41)
            r0 = r19
            r7.setConnectTimeout(r0)     // Catch:{ IOException -> 0x012e, Exception -> 0x01ec }
            r19 = 1
            r0 = r19
            r7.setDoInput(r0)     // Catch:{ IOException -> 0x012e, Exception -> 0x01ec }
            r19 = 1
            r0 = r19
            r7.setDoOutput(r0)     // Catch:{ IOException -> 0x012e, Exception -> 0x01ec }
            r19 = 0
            r0 = r19
            r7.setUseCaches(r0)     // Catch:{ IOException -> 0x012e, Exception -> 0x01ec }
            java.lang.String r19 = "POST"
            r0 = r19
            r7.setRequestMethod(r0)     // Catch:{ IOException -> 0x012e, Exception -> 0x01ec }
            java.lang.String r19 = "Connection"
            java.lang.String r20 = "keep-alive"
            r0 = r19
            r1 = r20
            r7.setRequestProperty(r0, r1)     // Catch:{ IOException -> 0x012e, Exception -> 0x01ec }
            java.lang.String r19 = "Host"
            java.lang.String r20 = r18.getHost()     // Catch:{ IOException -> 0x012e, Exception -> 0x01ec }
            r0 = r19
            r1 = r20
            r7.setRequestProperty(r0, r1)     // Catch:{ IOException -> 0x012e, Exception -> 0x01ec }
            java.util.UUID r19 = java.util.UUID.randomUUID()     // Catch:{ IOException -> 0x012e, Exception -> 0x01ec }
            java.lang.String r3 = r19.toString()     // Catch:{ IOException -> 0x012e, Exception -> 0x01ec }
            java.lang.String r19 = "Content-Type"
            java.lang.StringBuilder r20 = new java.lang.StringBuilder     // Catch:{ IOException -> 0x012e, Exception -> 0x01ec }
            r20.<init>()     // Catch:{ IOException -> 0x012e, Exception -> 0x01ec }
            java.lang.String r21 = "multipart/form-data;boundary="
            java.lang.StringBuilder r20 = r20.append(r21)     // Catch:{ IOException -> 0x012e, Exception -> 0x01ec }
            r0 = r20
            java.lang.StringBuilder r20 = r0.append(r3)     // Catch:{ IOException -> 0x012e, Exception -> 0x01ec }
            java.lang.String r20 = r20.toString()     // Catch:{ IOException -> 0x012e, Exception -> 0x01ec }
            r0 = r19
            r1 = r20
            r7.setRequestProperty(r0, r1)     // Catch:{ IOException -> 0x012e, Exception -> 0x01ec }
            java.io.DataOutputStream r15 = new java.io.DataOutputStream     // Catch:{ IOException -> 0x012e, Exception -> 0x01ec }
            java.io.OutputStream r19 = r7.getOutputStream()     // Catch:{ IOException -> 0x012e, Exception -> 0x01ec }
            r0 = r19
            r15.<init>(r0)     // Catch:{ IOException -> 0x012e, Exception -> 0x01ec }
            java.lang.StringBuilder r19 = new java.lang.StringBuilder     // Catch:{ IOException -> 0x012e, Exception -> 0x01ec }
            r19.<init>()     // Catch:{ IOException -> 0x012e, Exception -> 0x01ec }
            java.lang.String r20 = "--"
            java.lang.StringBuilder r19 = r19.append(r20)     // Catch:{ IOException -> 0x012e, Exception -> 0x01ec }
            r0 = r19
            java.lang.StringBuilder r19 = r0.append(r3)     // Catch:{ IOException -> 0x012e, Exception -> 0x01ec }
            java.lang.String r20 = "\r\n"
            java.lang.StringBuilder r19 = r19.append(r20)     // Catch:{ IOException -> 0x012e, Exception -> 0x01ec }
            java.lang.String r19 = r19.toString()     // Catch:{ IOException -> 0x012e, Exception -> 0x01ec }
            r0 = r19
            r15.writeBytes(r0)     // Catch:{ IOException -> 0x012e, Exception -> 0x01ec }
            r0 = r22
            java.lang.String r0 = r0.mFileExt     // Catch:{ IOException -> 0x012e, Exception -> 0x01ec }
            r19 = r0
            if (r19 != 0) goto L_0x00d3
            java.lang.String r19 = ""
            r0 = r19
            r1 = r22
            r1.mFileExt = r0     // Catch:{ IOException -> 0x012e, Exception -> 0x01ec }
        L_0x00d3:
            java.lang.StringBuilder r19 = new java.lang.StringBuilder     // Catch:{ IOException -> 0x012e, Exception -> 0x01ec }
            r19.<init>()     // Catch:{ IOException -> 0x012e, Exception -> 0x01ec }
            java.lang.String r20 = "Content-Disposition:form-data;name=\"file\";filename=\""
            java.lang.StringBuilder r19 = r19.append(r20)     // Catch:{ IOException -> 0x012e, Exception -> 0x01ec }
            java.lang.String r20 = r13.getName()     // Catch:{ IOException -> 0x012e, Exception -> 0x01ec }
            java.lang.StringBuilder r19 = r19.append(r20)     // Catch:{ IOException -> 0x012e, Exception -> 0x01ec }
            java.lang.String r20 = "."
            java.lang.StringBuilder r19 = r19.append(r20)     // Catch:{ IOException -> 0x012e, Exception -> 0x01ec }
            r0 = r22
            java.lang.String r0 = r0.mFileExt     // Catch:{ IOException -> 0x012e, Exception -> 0x01ec }
            r20 = r0
            java.lang.StringBuilder r19 = r19.append(r20)     // Catch:{ IOException -> 0x012e, Exception -> 0x01ec }
            java.lang.String r20 = "\"\r\n"
            java.lang.StringBuilder r19 = r19.append(r20)     // Catch:{ IOException -> 0x012e, Exception -> 0x01ec }
            java.lang.String r19 = r19.toString()     // Catch:{ IOException -> 0x012e, Exception -> 0x01ec }
            r0 = r19
            r15.writeBytes(r0)     // Catch:{ IOException -> 0x012e, Exception -> 0x01ec }
            java.lang.String r19 = "Content-Transfer-Encoding:binary\r\n\r\n"
            r0 = r19
            r15.writeBytes(r0)     // Catch:{ IOException -> 0x012e, Exception -> 0x01ec }
            java.io.FileInputStream r14 = new java.io.FileInputStream     // Catch:{ IOException -> 0x012e, Exception -> 0x01ec }
            r14.<init>(r13)     // Catch:{ IOException -> 0x012e, Exception -> 0x01ec }
            r19 = 4096(0x1000, float:5.74E-42)
            r0 = r19
            byte[] r5 = new byte[r0]     // Catch:{ IOException -> 0x012e, Exception -> 0x01ec }
            r9 = 0
        L_0x011c:
            int r9 = r14.read(r5)     // Catch:{ IOException -> 0x012e, Exception -> 0x01ec }
            r19 = -1
            r0 = r19
            if (r9 == r0) goto L_0x016f
            r19 = 0
            r0 = r19
            r15.write(r5, r0, r9)     // Catch:{ IOException -> 0x012e, Exception -> 0x01ec }
            goto L_0x011c
        L_0x012e:
            r12 = move-exception
            java.lang.String r19 = "UploadFileConnection"
            java.lang.StringBuilder r20 = new java.lang.StringBuilder     // Catch:{ all -> 0x0261 }
            r20.<init>()     // Catch:{ all -> 0x0261 }
            java.lang.String r21 = "upload file IO exception, "
            java.lang.StringBuilder r20 = r20.append(r21)     // Catch:{ all -> 0x0261 }
            java.lang.String r21 = r12.getMessage()     // Catch:{ all -> 0x0261 }
            java.lang.StringBuilder r20 = r20.append(r21)     // Catch:{ all -> 0x0261 }
            java.lang.String r20 = r20.toString()     // Catch:{ all -> 0x0261 }
            android.taobao.windvane.util.TaoLog.e(r19, r20)     // Catch:{ all -> 0x0261 }
            if (r10 == 0) goto L_0x0152
            r10.close()     // Catch:{ IOException -> 0x024b }
        L_0x0152:
            r2.close()     // Catch:{ Exception -> 0x0251 }
        L_0x0155:
            if (r7 == 0) goto L_0x015a
            r7.disconnect()
        L_0x015a:
            boolean r19 = android.taobao.windvane.util.TaoLog.getLogStatus()
            if (r19 == 0) goto L_0x0169
            java.lang.String r19 = "UploadFileConnection"
            java.lang.String r20 = "upload file fail."
            android.taobao.windvane.util.TaoLog.d(r19, r20)
        L_0x0169:
            android.taobao.windvane.connect.HttpResponse r16 = new android.taobao.windvane.connect.HttpResponse
            r16.<init>()
        L_0x016e:
            return r16
        L_0x016f:
            r14.close()     // Catch:{ IOException -> 0x012e, Exception -> 0x01ec }
            java.lang.String r19 = "\r\n"
            r0 = r19
            r15.writeBytes(r0)     // Catch:{ IOException -> 0x012e, Exception -> 0x01ec }
            java.lang.StringBuilder r19 = new java.lang.StringBuilder     // Catch:{ IOException -> 0x012e, Exception -> 0x01ec }
            r19.<init>()     // Catch:{ IOException -> 0x012e, Exception -> 0x01ec }
            java.lang.String r20 = "--"
            java.lang.StringBuilder r19 = r19.append(r20)     // Catch:{ IOException -> 0x012e, Exception -> 0x01ec }
            r0 = r19
            java.lang.StringBuilder r19 = r0.append(r3)     // Catch:{ IOException -> 0x012e, Exception -> 0x01ec }
            java.lang.String r20 = "--\r\n"
            java.lang.StringBuilder r19 = r19.append(r20)     // Catch:{ IOException -> 0x012e, Exception -> 0x01ec }
            java.lang.String r19 = r19.toString()     // Catch:{ IOException -> 0x012e, Exception -> 0x01ec }
            r0 = r19
            r15.writeBytes(r0)     // Catch:{ IOException -> 0x012e, Exception -> 0x01ec }
            r15.flush()     // Catch:{ IOException -> 0x012e, Exception -> 0x01ec }
            r15.close()     // Catch:{ IOException -> 0x012e, Exception -> 0x01ec }
            int r17 = r7.getResponseCode()     // Catch:{ IOException -> 0x012e, Exception -> 0x01ec }
            r16.setHttpCode(r17)     // Catch:{ IOException -> 0x012e, Exception -> 0x01ec }
            r19 = 200(0xc8, float:2.8E-43)
            r0 = r17
            r1 = r19
            if (r0 != r1) goto L_0x0232
            java.lang.String r8 = r7.getContentEncoding()     // Catch:{ IOException -> 0x012e, Exception -> 0x01ec }
            if (r8 == 0) goto L_0x021a
            java.lang.String r19 = "gzip"
            r0 = r19
            boolean r19 = r0.equals(r8)     // Catch:{ IOException -> 0x012e, Exception -> 0x01ec }
            if (r19 == 0) goto L_0x021a
            java.io.DataInputStream r11 = new java.io.DataInputStream     // Catch:{ IOException -> 0x012e, Exception -> 0x01ec }
            java.util.zip.GZIPInputStream r19 = new java.util.zip.GZIPInputStream     // Catch:{ IOException -> 0x012e, Exception -> 0x01ec }
            java.io.InputStream r20 = r7.getInputStream()     // Catch:{ IOException -> 0x012e, Exception -> 0x01ec }
            r19.<init>(r20)     // Catch:{ IOException -> 0x012e, Exception -> 0x01ec }
            r0 = r19
            r11.<init>(r0)     // Catch:{ IOException -> 0x012e, Exception -> 0x01ec }
            r10 = r11
        L_0x01d3:
            r19 = 2048(0x800, float:2.87E-42)
            r0 = r19
            byte[] r4 = new byte[r0]     // Catch:{ IOException -> 0x012e, Exception -> 0x01ec }
            r6 = 0
        L_0x01da:
            int r6 = r10.read(r4)     // Catch:{ IOException -> 0x012e, Exception -> 0x01ec }
            r19 = -1
            r0 = r19
            if (r6 == r0) goto L_0x0227
            r19 = 0
            r0 = r19
            r2.write(r4, r0, r6)     // Catch:{ IOException -> 0x012e, Exception -> 0x01ec }
            goto L_0x01da
        L_0x01ec:
            r12 = move-exception
            java.lang.String r19 = "UploadFileConnection"
            java.lang.StringBuilder r20 = new java.lang.StringBuilder     // Catch:{ all -> 0x0261 }
            r20.<init>()     // Catch:{ all -> 0x0261 }
            java.lang.String r21 = "upload file error, "
            java.lang.StringBuilder r20 = r20.append(r21)     // Catch:{ all -> 0x0261 }
            java.lang.String r21 = r12.getMessage()     // Catch:{ all -> 0x0261 }
            java.lang.StringBuilder r20 = r20.append(r21)     // Catch:{ all -> 0x0261 }
            java.lang.String r20 = r20.toString()     // Catch:{ all -> 0x0261 }
            android.taobao.windvane.util.TaoLog.e(r19, r20)     // Catch:{ all -> 0x0261 }
            if (r10 == 0) goto L_0x0210
            r10.close()     // Catch:{ IOException -> 0x0257 }
        L_0x0210:
            r2.close()     // Catch:{ Exception -> 0x025c }
        L_0x0213:
            if (r7 == 0) goto L_0x015a
            r7.disconnect()
            goto L_0x015a
        L_0x021a:
            java.io.DataInputStream r11 = new java.io.DataInputStream     // Catch:{ IOException -> 0x012e, Exception -> 0x01ec }
            java.io.InputStream r19 = r7.getInputStream()     // Catch:{ IOException -> 0x012e, Exception -> 0x01ec }
            r0 = r19
            r11.<init>(r0)     // Catch:{ IOException -> 0x012e, Exception -> 0x01ec }
            r10 = r11
            goto L_0x01d3
        L_0x0227:
            byte[] r19 = r2.toByteArray()     // Catch:{ IOException -> 0x012e, Exception -> 0x01ec }
            r0 = r16
            r1 = r19
            r0.setData(r1)     // Catch:{ IOException -> 0x012e, Exception -> 0x01ec }
        L_0x0232:
            if (r10 == 0) goto L_0x0237
            r10.close()     // Catch:{ IOException -> 0x0241 }
        L_0x0237:
            r2.close()     // Catch:{ Exception -> 0x0246 }
        L_0x023a:
            if (r7 == 0) goto L_0x016e
            r7.disconnect()
            goto L_0x016e
        L_0x0241:
            r12 = move-exception
            r12.printStackTrace()
            goto L_0x0237
        L_0x0246:
            r12 = move-exception
            r12.printStackTrace()
            goto L_0x023a
        L_0x024b:
            r12 = move-exception
            r12.printStackTrace()
            goto L_0x0152
        L_0x0251:
            r12 = move-exception
            r12.printStackTrace()
            goto L_0x0155
        L_0x0257:
            r12 = move-exception
            r12.printStackTrace()
            goto L_0x0210
        L_0x025c:
            r12 = move-exception
            r12.printStackTrace()
            goto L_0x0213
        L_0x0261:
            r19 = move-exception
            if (r10 == 0) goto L_0x0267
            r10.close()     // Catch:{ IOException -> 0x0270 }
        L_0x0267:
            r2.close()     // Catch:{ Exception -> 0x0275 }
        L_0x026a:
            if (r7 == 0) goto L_0x026f
            r7.disconnect()
        L_0x026f:
            throw r19
        L_0x0270:
            r12 = move-exception
            r12.printStackTrace()
            goto L_0x0267
        L_0x0275:
            r12 = move-exception
            r12.printStackTrace()
            goto L_0x026a
        */
        throw new UnsupportedOperationException("Method not decompiled: android.taobao.windvane.extra.upload.UploadFileConnection.uploadFile(java.lang.String, java.lang.String):android.taobao.windvane.connect.HttpResponse");
    }
}
