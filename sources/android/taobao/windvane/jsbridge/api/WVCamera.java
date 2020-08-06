package android.taobao.windvane.jsbridge.api;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.hardware.Camera;
import android.net.Uri;
import android.os.AsyncTask;
import android.provider.MediaStore;
import android.taobao.windvane.cache.WVCacheManager;
import android.taobao.windvane.cache.WVFileInfo;
import android.taobao.windvane.cache.WVFileInfoParser;
import android.taobao.windvane.file.FileAccesser;
import android.taobao.windvane.jsbridge.WVApiPlugin;
import android.taobao.windvane.jsbridge.WVCallBackContext;
import android.taobao.windvane.jsbridge.WVResult;
import android.taobao.windvane.runtimepermission.PermissionProposer;
import android.taobao.windvane.util.DigestUtils;
import android.taobao.windvane.util.ImageTool;
import android.taobao.windvane.util.TaoLog;
import android.taobao.windvane.view.PopupWindowController;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import com.taobao.detail.domain.tuwen.TuwenConstants;
import com.taobao.tao.remotebusiness.js.MtopJSBridge;
import java.io.File;
import java.util.ArrayList;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class WVCamera extends WVApiPlugin {
    public static final String LOCAL_IMAGE = "//127.0.0.1/wvcache/photo.jpg?_wvcrc=1&t=";
    private static final String TAG = "WVCamera";
    public static int maxLength = 480;
    private static String multiActivityClass = null;
    private static String uploadServiceClass = null;
    private long lastAccess = 0;
    /* access modifiers changed from: private */
    public WVCallBackContext mCallback = null;
    private String mLocalPath = null;
    private UploadParams mParams;
    /* access modifiers changed from: private */
    public PopupWindowController mPopupController;
    /* access modifiers changed from: private */
    public String[] mPopupMenuTags = {"拍照", "从相册选择"};
    protected View.OnClickListener popupClickListener = new View.OnClickListener() {
        public void onClick(View v) {
            WVCamera.this.mPopupController.hide();
            if (WVCamera.this.mPopupMenuTags[0].equals(v.getTag())) {
                WVCamera.this.openCamara();
            } else if (WVCamera.this.mPopupMenuTags[1].equals(v.getTag())) {
                WVCamera.this.chosePhoto();
            } else {
                TaoLog.w("WVCamera", "take photo cancel, and callback.");
                WVCamera.this.mCallback.error(new WVResult());
            }
        }
    };
    private WVUploadService uploadService;

    public boolean execute(String action, final String params, final WVCallBackContext callback) {
        if ("takePhoto".equals(action)) {
            try {
                PermissionProposer.buildPermissionTask(callback.getWebview().getContext(), new String[]{"android.permission.WRITE_EXTERNAL_STORAGE", "android.permission.READ_EXTERNAL_STORAGE", "android.permission.CAMERA"}).setTaskOnPermissionGranted(new Runnable() {
                    public void run() {
                        boolean unused = WVCamera.this.isAlive = true;
                        WVCamera.this.takePhoto(callback, params);
                    }
                }).setTaskOnPermissionDenied(new Runnable() {
                    public void run() {
                        WVResult result = new WVResult();
                        result.addData("msg", "NO_PERMISSION");
                        callback.error(result);
                    }
                }).execute();
            } catch (Exception e) {
            }
        } else if (!"confirmUploadPhoto".equals(action)) {
            return false;
        } else {
            confirmUploadPhoto(callback, params);
        }
        return true;
    }

    public void takePhotoPlus(WVCallBackContext callback, String localPath, String params) {
        if (callback == null || localPath == null || params == null) {
            TaoLog.e("WVCamera", "takePhotoPlus fail, params error");
            return;
        }
        initTakePhoto(callback, params);
        this.mLocalPath = localPath;
        zoomPicAndCallback(localPath, localPath, this.mParams);
    }

    public synchronized void takePhoto(WVCallBackContext callback, String params) {
        View view;
        initTakePhoto(callback, params);
        if ((this.mContext instanceof Activity) && (view = ((Activity) this.mContext).getWindow().peekDecorView()) != null) {
            ((InputMethodManager) this.mContext.getSystemService("input_method")).hideSoftInputFromWindow(view.getWindowToken(), 0);
        }
        if ("camera".equals(this.mParams.mode)) {
            openCamara();
        } else if ("photo".equals(this.mParams.mode)) {
            chosePhoto();
        } else {
            this.mPopupController = new PopupWindowController(this.mContext, this.mWebView.getView(), this.mPopupMenuTags, this.popupClickListener);
            this.mPopupController.show();
        }
    }

    /* access modifiers changed from: private */
    public void openCamara() {
        if (isHasCamaraPermission()) {
            TaoLog.d("WVCamera", "start to open system camera.");
            Intent intent = new Intent("android.media.action.IMAGE_CAPTURE");
            this.mParams.localUrl = "//127.0.0.1/wvcache/photo.jpg?_wvcrc=1&t=" + System.currentTimeMillis();
            String cacheDir = WVCacheManager.getInstance().getCacheDir(true);
            if (cacheDir != null) {
                File file = new File(cacheDir);
                if (!file.exists()) {
                    file.mkdirs();
                }
                this.mLocalPath = cacheDir + File.separator + DigestUtils.md5ToHex(this.mParams.localUrl);
                intent.putExtra("output", Uri.fromFile(new File(this.mLocalPath)));
                if (this.mContext instanceof Activity) {
                    ((Activity) this.mContext).startActivityForResult(intent, 4001);
                }
            } else if (this.mCallback != null) {
                this.mCallback.error();
            }
        } else if (this.mCallback != null) {
            WVResult result = new WVResult();
            result.addData("msg", "NO_PERMISSION");
            this.mCallback.error(result);
        }
    }

    private boolean isHasCamaraPermission() {
        try {
            Camera.open().release();
            return true;
        } catch (Throwable th) {
            return false;
        }
    }

    /* access modifiers changed from: private */
    public void chosePhoto() {
        Intent intent;
        int requestCode;
        TaoLog.d("WVCamera", "start to pick photo from system album.");
        if (!"1".equals(this.mParams.mutipleSelection)) {
            intent = new Intent("android.intent.action.PICK", MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
            requestCode = WVApiPlugin.REQUEST_PICK_PHOTO;
        } else if (!this.mContext.getPackageName().equals("com.taobao.taobao")) {
            WVResult result = new WVResult();
            result.addData("msg", "mutipleSelection only support in taobao!");
            this.mCallback.error(result);
            return;
        } else {
            intent = new Intent();
            intent.setAction("android.intent.action.VIEW");
            intent.setData(Uri.parse("taobao://go/ImgFileListActivity"));
            intent.putExtra("maxSelect", this.mParams.maxSelect);
            requestCode = 4003;
        }
        if (this.mContext instanceof Activity) {
            try {
                ((Activity) this.mContext).startActivityForResult(intent, requestCode);
            } catch (Throwable e) {
                e.printStackTrace();
                this.mCallback.error();
            }
        }
    }

    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (TaoLog.getLogStatus()) {
            TaoLog.d("WVCamera", "takePhoto callback, requestCode: " + requestCode + ";resultCode: " + resultCode);
        }
        WVResult result = new WVResult();
        switch (requestCode) {
            case 4001:
                if (resultCode == -1) {
                    zoomPicAndCallback(this.mLocalPath, this.mLocalPath, this.mParams);
                    return;
                }
                TaoLog.w("WVCamera", "call takePhoto fail. resultCode: " + resultCode);
                result.addData("msg", "CANCELED_BY_USER");
                this.mCallback.error(result);
                return;
            case WVApiPlugin.REQUEST_PICK_PHOTO:
                if (resultCode != -1 || data == null) {
                    TaoLog.w("WVCamera", "call pick photo fail. resultCode: " + resultCode);
                    result.addData("msg", "CANCELED_BY_USER");
                    this.mCallback.error(result);
                    return;
                }
                Uri imageUri = data.getData();
                String picturePath = null;
                if (imageUri != null) {
                    if ("file".equalsIgnoreCase(imageUri.getScheme())) {
                        picturePath = imageUri.getPath();
                    } else {
                        String[] fileColumns = {"_data"};
                        Cursor c = this.mContext.getContentResolver().query(imageUri, fileColumns, (String) null, (String[]) null, (String) null);
                        if (c == null || !c.moveToFirst()) {
                            TaoLog.w("WVCamera", "pick photo fail, Cursor is empty, imageUri: " + imageUri.toString());
                        } else {
                            picturePath = c.getString(c.getColumnIndex(fileColumns[0]));
                            c.close();
                        }
                    }
                }
                if (FileAccesser.exists(picturePath)) {
                    UploadParams uploadParams = new UploadParams(this.mParams);
                    uploadParams.localUrl = "//127.0.0.1/wvcache/photo.jpg?_wvcrc=1&t=" + System.currentTimeMillis();
                    zoomPicAndCallback(picturePath, WVCacheManager.getInstance().getCacheDir(true) + File.separator + DigestUtils.md5ToHex(uploadParams.localUrl), uploadParams);
                    return;
                }
                TaoLog.w("WVCamera", "pick photo fail, picture not exist, picturePath: " + picturePath);
                return;
            case 4003:
                if (data == null || data.getExtras() == null || data.getExtras().get("fileList") == null) {
                    result.addData("msg", "CANCELED_BY_USER");
                    this.mCallback.error(result);
                    return;
                }
                ArrayList<String> fileList = (ArrayList) data.getExtras().get("fileList");
                int size = fileList.size();
                if (size == 0) {
                    result.addData("msg", "CANCELED_BY_USER");
                    this.mCallback.error(result);
                    return;
                }
                JSONArray images = new JSONArray();
                for (int i = 0; i < size; i++) {
                    String picturePath2 = fileList.get(i);
                    if (FileAccesser.exists(picturePath2)) {
                        UploadParams uploadParams2 = new UploadParams(this.mParams);
                        uploadParams2.localUrl = "//127.0.0.1/wvcache/photo.jpg?_wvcrc=1&t=" + System.currentTimeMillis();
                        String localPath = WVCacheManager.getInstance().getCacheDir(true) + File.separator + DigestUtils.md5ToHex(uploadParams2.localUrl);
                        JSONObject imgData = new JSONObject();
                        try {
                            imgData.put("url", uploadParams2.localUrl);
                            imgData.put("localPath", localPath);
                            images.put(imgData);
                            TaoLog.d("WVCamera", "url:" + uploadParams2.localUrl + " localPath:" + localPath);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                        if (i == size - 1) {
                            uploadParams2.images = images;
                        } else {
                            uploadParams2.isLastPic = false;
                        }
                        zoomPicAndCallback(picturePath2, localPath, uploadParams2);
                        try {
                            Thread.sleep(100);
                        } catch (InterruptedException e2) {
                            e2.printStackTrace();
                        }
                    } else {
                        TaoLog.w("WVCamera", "pick photo fail, picture not exist, picturePath: " + picturePath2);
                    }
                }
                return;
            default:
                return;
        }
    }

    @SuppressLint({"NewApi"})
    private void zoomPicAndCallback(String srcPath, String targetPath, UploadParams params) {
        Bitmap bm = null;
        try {
            if (this.mParams.needZoom) {
                int degree = ImageTool.readRotationDegree(srcPath);
                Bitmap bm2 = ImageTool.readZoomImage(srcPath, maxLength);
                if (bm2 != null) {
                    bm = ImageTool.rotate(ImageTool.zoomBitmap(bm2, maxLength), degree);
                } else if (bm2 != null && !bm2.isRecycled()) {
                    bm2.recycle();
                    return;
                } else {
                    return;
                }
            }
            final WVFileInfo fileInfo = new WVFileInfo();
            fileInfo.fileName = DigestUtils.md5ToHex(params.localUrl);
            fileInfo.mimeType = "image/jpeg";
            fileInfo.expireTime = WVFileInfoParser.DEFAULT_MAX_AGE + System.currentTimeMillis();
            if (TaoLog.getLogStatus()) {
                TaoLog.d("WVCamera", "write pic to file, name: " + fileInfo.fileName);
            }
            final Bitmap bm22 = bm;
            Bitmap bm3 = null;
            final String str = targetPath;
            final UploadParams uploadParams = params;
            AsyncTask.execute(new Runnable() {
                /* JADX WARNING: Removed duplicated region for block: B:26:0x00d8 A[SYNTHETIC, Splitter:B:26:0x00d8] */
                /* JADX WARNING: Removed duplicated region for block: B:36:0x00f2 A[SYNTHETIC, Splitter:B:36:0x00f2] */
                /* JADX WARNING: Removed duplicated region for block: B:46:? A[RETURN, SYNTHETIC] */
                /* Code decompiled incorrectly, please refer to instructions dump. */
                public void run() {
                    /*
                        r8 = this;
                        r7 = 1
                        r6 = 0
                        android.taobao.windvane.jsbridge.WVResult r4 = new android.taobao.windvane.jsbridge.WVResult
                        r4.<init>()
                        android.graphics.Bitmap r5 = r2
                        if (r5 == 0) goto L_0x004f
                        byte[] r0 = new byte[r7]
                        r0[r6] = r6
                        android.taobao.windvane.cache.WVCacheManager r5 = android.taobao.windvane.cache.WVCacheManager.getInstance()
                        android.taobao.windvane.cache.WVFileInfo r6 = r3
                        r5.writeToFile(r6, r0)
                        r2 = 0
                        java.io.FileOutputStream r3 = new java.io.FileOutputStream     // Catch:{ FileNotFoundException -> 0x00a9 }
                        java.io.File r5 = new java.io.File     // Catch:{ FileNotFoundException -> 0x00a9 }
                        android.taobao.windvane.cache.WVCacheManager r6 = android.taobao.windvane.cache.WVCacheManager.getInstance()     // Catch:{ FileNotFoundException -> 0x00a9 }
                        r7 = 1
                        java.lang.String r6 = r6.getCacheDir(r7)     // Catch:{ FileNotFoundException -> 0x00a9 }
                        android.taobao.windvane.cache.WVFileInfo r7 = r3     // Catch:{ FileNotFoundException -> 0x00a9 }
                        java.lang.String r7 = r7.fileName     // Catch:{ FileNotFoundException -> 0x00a9 }
                        r5.<init>(r6, r7)     // Catch:{ FileNotFoundException -> 0x00a9 }
                        r3.<init>(r5)     // Catch:{ FileNotFoundException -> 0x00a9 }
                        android.graphics.Bitmap r5 = r2     // Catch:{ FileNotFoundException -> 0x00fe, all -> 0x00fb }
                        android.graphics.Bitmap$CompressFormat r6 = android.graphics.Bitmap.CompressFormat.JPEG     // Catch:{ FileNotFoundException -> 0x00fe, all -> 0x00fb }
                        r7 = 100
                        r5.compress(r6, r7, r3)     // Catch:{ FileNotFoundException -> 0x00fe, all -> 0x00fb }
                        android.graphics.Bitmap r5 = r2
                        if (r5 == 0) goto L_0x004a
                        android.graphics.Bitmap r5 = r2
                        boolean r5 = r5.isRecycled()
                        if (r5 != 0) goto L_0x004a
                        android.graphics.Bitmap r5 = r2
                        r5.recycle()
                    L_0x004a:
                        if (r3 == 0) goto L_0x004f
                        r3.close()     // Catch:{ Exception -> 0x00f6 }
                    L_0x004f:
                        android.taobao.windvane.jsbridge.api.WVCamera r5 = android.taobao.windvane.jsbridge.api.WVCamera.this
                        java.lang.String r6 = r4
                        android.taobao.windvane.jsbridge.api.WVCamera$UploadParams r7 = r5
                        r5.takePhotoSuccess(r6, r7)
                        r4.setSuccess()
                        java.lang.String r5 = "url"
                        android.taobao.windvane.jsbridge.api.WVCamera$UploadParams r6 = r5
                        java.lang.String r6 = r6.localUrl
                        r4.addData((java.lang.String) r5, (java.lang.String) r6)
                        java.lang.String r5 = "localPath"
                        java.lang.String r6 = r4
                        r4.addData((java.lang.String) r5, (java.lang.String) r6)
                        java.lang.String r5 = "WVCamera"
                        java.lang.StringBuilder r6 = new java.lang.StringBuilder
                        r6.<init>()
                        java.lang.String r7 = "url:"
                        java.lang.StringBuilder r6 = r6.append(r7)
                        android.taobao.windvane.jsbridge.api.WVCamera$UploadParams r7 = r5
                        java.lang.String r7 = r7.localUrl
                        java.lang.StringBuilder r6 = r6.append(r7)
                        java.lang.String r7 = " localPath:"
                        java.lang.StringBuilder r6 = r6.append(r7)
                        java.lang.String r7 = r4
                        java.lang.StringBuilder r6 = r6.append(r7)
                        java.lang.String r6 = r6.toString()
                        android.taobao.windvane.util.TaoLog.d(r5, r6)
                        android.taobao.windvane.jsbridge.api.WVCamera r5 = android.taobao.windvane.jsbridge.api.WVCamera.this
                        android.taobao.windvane.jsbridge.WVCallBackContext r5 = r5.mCallback
                        java.lang.String r6 = "WVPhoto.Event.takePhotoSuccess"
                        java.lang.String r7 = r4.toJsonString()
                        r5.fireEvent(r6, r7)
                    L_0x00a8:
                        return
                    L_0x00a9:
                        r1 = move-exception
                    L_0x00aa:
                        java.lang.String r5 = "WVCamera"
                        java.lang.String r6 = "fail to create bitmap file"
                        android.taobao.windvane.util.TaoLog.e(r5, r6)     // Catch:{ all -> 0x00de }
                        java.lang.String r5 = "reason"
                        java.lang.String r6 = "fail to create bitmap file"
                        r4.addData((java.lang.String) r5, (java.lang.String) r6)     // Catch:{ all -> 0x00de }
                        android.taobao.windvane.jsbridge.api.WVCamera r5 = android.taobao.windvane.jsbridge.api.WVCamera.this     // Catch:{ all -> 0x00de }
                        android.taobao.windvane.jsbridge.WVCallBackContext r5 = r5.mCallback     // Catch:{ all -> 0x00de }
                        r5.error((android.taobao.windvane.jsbridge.WVResult) r4)     // Catch:{ all -> 0x00de }
                        android.graphics.Bitmap r5 = r2
                        if (r5 == 0) goto L_0x00d6
                        android.graphics.Bitmap r5 = r2
                        boolean r5 = r5.isRecycled()
                        if (r5 != 0) goto L_0x00d6
                        android.graphics.Bitmap r5 = r2
                        r5.recycle()
                    L_0x00d6:
                        if (r2 == 0) goto L_0x00a8
                        r2.close()     // Catch:{ Exception -> 0x00dc }
                        goto L_0x00a8
                    L_0x00dc:
                        r5 = move-exception
                        goto L_0x00a8
                    L_0x00de:
                        r5 = move-exception
                    L_0x00df:
                        android.graphics.Bitmap r6 = r2
                        if (r6 == 0) goto L_0x00f0
                        android.graphics.Bitmap r6 = r2
                        boolean r6 = r6.isRecycled()
                        if (r6 != 0) goto L_0x00f0
                        android.graphics.Bitmap r6 = r2
                        r6.recycle()
                    L_0x00f0:
                        if (r2 == 0) goto L_0x00f5
                        r2.close()     // Catch:{ Exception -> 0x00f9 }
                    L_0x00f5:
                        throw r5
                    L_0x00f6:
                        r5 = move-exception
                        goto L_0x004f
                    L_0x00f9:
                        r6 = move-exception
                        goto L_0x00f5
                    L_0x00fb:
                        r5 = move-exception
                        r2 = r3
                        goto L_0x00df
                    L_0x00fe:
                        r1 = move-exception
                        r2 = r3
                        goto L_0x00aa
                    */
                    throw new UnsupportedOperationException("Method not decompiled: android.taobao.windvane.jsbridge.api.WVCamera.AnonymousClass4.run():void");
                }
            });
            if (bm3 != null && !bm3.isRecycled()) {
                bm3.recycle();
            }
        } catch (Exception e) {
            WVResult res = new WVResult();
            res.addData("reason", "write photo io error.");
            this.mCallback.error(res);
            TaoLog.e("WVCamera", "write photo io error.");
            if (bm != null && !bm.isRecycled()) {
                bm.recycle();
            }
        } catch (Throwable th) {
            if (bm != null && !bm.isRecycled()) {
                bm.recycle();
            }
            throw th;
        }
    }

    public synchronized void confirmUploadPhoto(WVCallBackContext callback, String params) {
        this.mCallback = callback;
        UploadParams paramsData = new UploadParams();
        try {
            JSONObject jso = new JSONObject(params);
            String path = jso.getString(TuwenConstants.PARAMS.SKU_PATH);
            paramsData.identifier = jso.optString("identifier");
            paramsData.v = jso.optString("v");
            paramsData.bizCode = jso.optString("bizCode");
            String cacheDir = WVCacheManager.getInstance().getCacheDir(true);
            if (path == null || cacheDir == null || !path.startsWith(cacheDir)) {
                callback.error(new WVResult("HY_PARAM_ERR"));
            } else {
                paramsData.filePath = path;
                upload(paramsData);
            }
        } catch (JSONException e) {
            TaoLog.e("WVCamera", "confirmUploadPhoto fail, params: " + params);
            WVResult result = new WVResult();
            result.setResult("HY_PARAM_ERR");
            callback.error(result);
        }
        return;
    }

    /* access modifiers changed from: private */
    public void takePhotoSuccess(String picPath, UploadParams params) {
        if (params.type == 1) {
            String cacheDir = WVCacheManager.getInstance().getCacheDir(true);
            if (picPath == null || cacheDir == null || !picPath.startsWith(cacheDir)) {
                this.mCallback.error();
                return;
            }
            params.filePath = picPath;
            upload(params);
            return;
        }
        WVResult result = new WVResult();
        result.setSuccess();
        if (!"1".equals(params.mutipleSelection)) {
            result.addData("url", params.localUrl);
            result.addData("localPath", picPath);
            TaoLog.d("WVCamera", "url:" + params.localUrl + " localPath:" + picPath);
            this.mCallback.success(result);
        } else if (params.isLastPic) {
            if (params.images == null) {
                result.addData("url", params.localUrl);
                result.addData("localPath", picPath);
            } else {
                result.addData("images", params.images);
            }
            this.mCallback.success(result);
        } else {
            return;
        }
        if (TaoLog.getLogStatus()) {
            TaoLog.d("WVCamera", "pic not upload and call success, retString: " + result.toJsonString());
        }
    }

    public class UploadParams {
        public String bizCode;
        public String extraData;
        public String filePath;
        public String identifier = "";
        public JSONArray images = null;
        public boolean isLastPic = true;
        public String localUrl;
        public int maxSelect = 9;
        public String mode = "both";
        public String mutipleSelection = "0";
        public boolean needBase64 = false;
        public boolean needLogin = false;
        public boolean needZoom = true;
        public int type;
        public String v;

        public UploadParams() {
        }

        public UploadParams(UploadParams params) {
            this.filePath = params.filePath;
            this.localUrl = params.localUrl;
            this.type = params.type;
            this.v = params.v;
            this.bizCode = params.bizCode;
            this.extraData = params.extraData;
            this.identifier = params.identifier;
            this.mode = params.mode;
            this.mutipleSelection = params.mutipleSelection;
            this.maxSelect = params.maxSelect;
            this.isLastPic = params.isLastPic;
            this.images = params.images;
            this.needZoom = params.needZoom;
            this.needLogin = params.needLogin;
            this.needBase64 = params.needBase64;
        }
    }

    private void upload(UploadParams params) {
        if (this.uploadService == null && uploadServiceClass != null) {
            try {
                Class<?> clazz = Class.forName(uploadServiceClass);
                if (clazz != null && WVUploadService.class.isAssignableFrom(clazz)) {
                    this.uploadService = (WVUploadService) clazz.newInstance();
                    this.uploadService.initialize(this.mContext, this.mWebView);
                }
            } catch (Exception e) {
                TaoLog.e("WVCamera", "create upload service error: " + uploadServiceClass + ". " + e.getMessage());
            }
        }
        if (this.uploadService != null) {
            this.uploadService.doUpload(params, this.mCallback);
        }
    }

    public static void registerUploadService(Class<? extends WVUploadService> cls) {
        if (cls != null) {
            uploadServiceClass = cls.getName();
        }
    }

    public static void registerMultiActivity(Class<? extends Activity> cls) {
        if (cls != null) {
            multiActivityClass = cls.getName();
        }
    }

    public static void registerMultiActivityName(String classname) {
        if (classname != null) {
            multiActivityClass = classname;
        }
    }

    private void initTakePhoto(WVCallBackContext callback, String params) {
        boolean z = false;
        if (this.isAlive) {
            long now = System.currentTimeMillis();
            long interval = now - this.lastAccess;
            this.lastAccess = now;
            if (interval < 1000) {
                TaoLog.w("WVCamera", "takePhoto, call this method too frequent,  " + interval);
                return;
            }
            this.mCallback = callback;
            this.mParams = new UploadParams();
            try {
                JSONObject jso = new JSONObject(params);
                this.mParams.type = jso.optInt("type", 1);
                this.mParams.mode = jso.optString("mode");
                this.mParams.v = jso.optString("v");
                this.mParams.bizCode = jso.optString("bizCode");
                this.mParams.extraData = jso.optString("extraData");
                this.mParams.identifier = jso.optString("identifier");
                this.mParams.maxSelect = jso.optInt("maxSelect");
                this.mParams.mutipleSelection = jso.optString("mutipleSelection");
                UploadParams uploadParams = this.mParams;
                if (!"false".equals(jso.optString("needZoom"))) {
                    z = true;
                }
                uploadParams.needZoom = z;
                this.mParams.isLastPic = true;
                this.mParams.needLogin = jso.optBoolean(MtopJSBridge.MtopJSParam.NEED_LOGIN, false);
                this.mParams.needBase64 = jso.optBoolean("needBase64", false);
                maxLength = jso.optInt("maxLength", 480);
                if (jso.has("localUrl")) {
                    this.mParams.localUrl = jso.optString("localUrl");
                }
            } catch (JSONException e) {
                TaoLog.e("WVCamera", "takePhoto fail, params: " + params);
                WVResult result = new WVResult();
                result.setResult("HY_PARAM_ERR");
                this.mCallback.error(result);
            }
        }
    }
}
