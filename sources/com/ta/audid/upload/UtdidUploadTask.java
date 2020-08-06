package com.ta.audid.upload;

import android.content.Context;
import android.text.TextUtils;
import com.ta.audid.device.AppUtdid;
import com.ta.audid.store.UtdidContent;
import com.ta.audid.store.UtdidContentBuilder;
import com.ta.audid.store.UtdidContentSqliteStore;
import com.ta.audid.store.UtdidContentUtil;
import com.ta.audid.utils.UtdidLogger;
import java.util.List;

public class UtdidUploadTask implements Runnable {
    private static final String POST_HTTP_URL = "https://audid-api.taobao.com/v2.0/a/audid/req/";
    private static volatile boolean bRunning = false;
    private Context mContext = null;

    public UtdidUploadTask(Context context) {
        this.mContext = context;
    }

    public void run() {
        try {
            upload();
        } catch (Throwable e) {
            UtdidLogger.e("", e, new Object[0]);
        }
    }

    /* JADX WARNING: Code restructure failed: missing block: B:22:0x004d, code lost:
        r2 = move-exception;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:23:0x004e, code lost:
        bRunning = false;
        com.ta.audid.utils.MutiProcessLock.releaseUpload();
     */
    /* JADX WARNING: Code restructure failed: missing block: B:24:0x0053, code lost:
        throw r2;
     */
    /* JADX WARNING: Failed to process nested try/catch */
    /* JADX WARNING: Removed duplicated region for block: B:22:0x004d A[ExcHandler:  FINALLY, Splitter:B:5:0x0014] */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    private void upload() {
        /*
            r7 = this;
            r4 = 1
            r6 = 0
            com.ta.audid.utils.UtdidLogger.d()
            android.content.Context r2 = r7.mContext
            boolean r2 = com.ta.audid.utils.NetworkInfoUtils.isConnectInternet(r2)
            if (r2 != 0) goto L_0x000e
        L_0x000d:
            return
        L_0x000e:
            boolean r2 = bRunning
            if (r2 != 0) goto L_0x000d
            bRunning = r4
            boolean r1 = com.ta.audid.utils.MutiProcessLock.trylockUpload()     // Catch:{ Throwable -> 0x0046, all -> 0x004d }
            if (r1 != 0) goto L_0x002f
            java.lang.String r2 = ""
            r3 = 1
            java.lang.Object[] r3 = new java.lang.Object[r3]     // Catch:{ Throwable -> 0x0046, all -> 0x004d }
            r4 = 0
            java.lang.String r5 = "Other Process is Uploading"
            r3[r4] = r5     // Catch:{ Throwable -> 0x0046, all -> 0x004d }
            com.ta.audid.utils.UtdidLogger.d((java.lang.String) r2, (java.lang.Object[]) r3)     // Catch:{ Throwable -> 0x0046, all -> 0x004d }
            bRunning = r6
            com.ta.audid.utils.MutiProcessLock.releaseUpload()
            goto L_0x000d
        L_0x002f:
            r0 = 0
        L_0x0030:
            if (r0 >= r4) goto L_0x0038
            boolean r2 = r7.uploadFromDataBase()     // Catch:{ Throwable -> 0x0054, all -> 0x004d }
            if (r2 == 0) goto L_0x003e
        L_0x0038:
            bRunning = r6
            com.ta.audid.utils.MutiProcessLock.releaseUpload()
            goto L_0x000d
        L_0x003e:
            r2 = 1000(0x3e8, double:4.94E-321)
            java.lang.Thread.sleep(r2)     // Catch:{ Throwable -> 0x0054, all -> 0x004d }
        L_0x0043:
            int r0 = r0 + 1
            goto L_0x0030
        L_0x0046:
            r2 = move-exception
            bRunning = r6
            com.ta.audid.utils.MutiProcessLock.releaseUpload()
            goto L_0x000d
        L_0x004d:
            r2 = move-exception
            bRunning = r6
            com.ta.audid.utils.MutiProcessLock.releaseUpload()
            throw r2
        L_0x0054:
            r2 = move-exception
            goto L_0x0043
        */
        throw new UnsupportedOperationException("Method not decompiled: com.ta.audid.upload.UtdidUploadTask.upload():void");
    }

    private boolean uploadFromDataBase() {
        UtdidLogger.d();
        List<UtdidContent> logs = UtdidContentSqliteStore.getInstance().get(4);
        if (logs == null || logs.size() == 0) {
            UtdidLogger.d("log is empty", new Object[0]);
            return true;
        }
        String postData = buildPostDataFromDB(logs);
        if (TextUtils.isEmpty(postData)) {
            UtdidLogger.d("postData is empty", new Object[0]);
            return true;
        }
        if (reqServer(postData)) {
            UtdidContentSqliteStore.getInstance().delete(logs);
            UtdidLogger.d("", "upload success");
        } else {
            UtdidLogger.d("", "upload fail");
        }
        return false;
    }

    private boolean reqServer(String postData) {
        HttpResponse response = HttpUtils.sendRequest(POST_HTTP_URL, postData, true);
        String result = "";
        try {
            result = new String(response.data, "UTF-8");
        } catch (Exception e) {
            UtdidLogger.d("", e);
        }
        if (HttpResponse.checkSignature(result, response.signature)) {
            return BizResponse.isSuccess(BizResponse.parseResult(result).code);
        }
        return false;
    }

    private String buildPostDataFromDB(List<UtdidContent> logs) {
        if (logs == null || logs.size() == 0) {
            return null;
        }
        String appUtdid = AppUtdid.getInstance().getCurrentAppUtdid();
        if (TextUtils.isEmpty(appUtdid)) {
            return null;
        }
        String audidModle = UtdidContentBuilder.buildUDID(appUtdid);
        StringBuilder builder = new StringBuilder();
        builder.append(audidModle);
        for (int i = 0; i < logs.size(); i++) {
            String line = logs.get(i).getDecodedContent();
            builder.append("\n");
            builder.append(line);
        }
        if (UtdidLogger.isDebug()) {
            UtdidLogger.sd("", builder.toString());
        }
        return UtdidContentUtil.getEncodedContent(builder.toString());
    }
}
