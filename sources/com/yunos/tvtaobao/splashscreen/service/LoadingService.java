package com.yunos.tvtaobao.splashscreen.service;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.os.IBinder;
import android.taobao.windvane.util.WVNativeCallbackUtil;
import android.text.TextUtils;
import com.alibaba.fastjson.JSON;
import com.bumptech.glide.request.RequestOptions;
import com.bumptech.glide.request.target.CustomTarget;
import com.bumptech.glide.request.target.Target;
import com.bumptech.glide.request.transition.Transition;
import com.tvlife.imageloader.core.DisplayImageOptions;
import com.yunos.CloudUUIDWrapper;
import com.yunos.tv.core.RtEnv;
import com.yunos.tv.core.common.GlideManager;
import com.yunos.tv.core.common.RequestListener;
import com.yunos.tv.core.config.Config;
import com.yunos.tv.core.util.NetWorkUtil;
import com.yunos.tvtaobao.biz.request.BusinessRequest;
import com.yunos.tvtaobao.biz.request.bo.LoadingBo;
import com.yunos.tvtaobao.biz.util.FileUtil;
import com.yunos.tvtaobao.biz.util.MD5Util;
import com.yunos.tvtaobao.biz.util.TimeUtil;
import com.zhiping.dev.android.logger.ZpLogger;
import java.io.File;
import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.json.JSONObject;

public class LoadingService extends Service {
    /* access modifiers changed from: private */
    public String TAG = "LoadingService";
    private String bitmapPath;
    /* access modifiers changed from: private */
    public boolean isDoing = false;
    /* access modifiers changed from: private */
    public List<LoadingBo> listLoading = new ArrayList();
    private BusinessRequest mBusinessRequest;
    private DisplayImageOptions option;

    public IBinder onBind(Intent intent) {
        return null;
    }

    public void onCreate() {
        if (getFilesDir() == null) {
            ZpLogger.w(this.TAG, "onCreate filesDir null stop service");
            stopSelf();
            return;
        }
        this.bitmapPath = getFilesDir().toString() + WVNativeCallbackUtil.SEPERATER + "loading";
        FileUtil.createDir(this.bitmapPath);
        ZpLogger.w(this.TAG, "onCreate filesDir " + this.bitmapPath);
        this.option = new DisplayImageOptions.Builder().cacheOnDisc(false).cacheInMemory(false).setBitmapPath(this.bitmapPath).build();
        this.mBusinessRequest = BusinessRequest.getBusinessRequest();
        super.onCreate();
    }

    public int onStartCommand(Intent intent, int flags, int startId) {
        ZpLogger.i("LoadingService", "Service onStartCommand");
        if (this.isDoing || !NetWorkUtil.isNetWorkAvailable()) {
            return super.onStartCommand(intent, flags, startId);
        }
        this.isDoing = true;
        requestData();
        return super.onStartCommand(intent, flags, startId);
    }

    private void requestData() {
        if (this.mBusinessRequest != null) {
            try {
                PackageInfo packInfo = getPackageManager().getPackageInfo(getPackageName(), 0);
                String uuid = CloudUUIDWrapper.getCloudUUID();
                String versionName = packInfo.versionName;
                JSONObject object = new JSONObject();
                object.put("umToken", Config.getUmtoken(this));
                object.put("appkey", Config.getDeviceAppKey(this));
                object.put("subkey", (String) RtEnv.get(RtEnv.SUBKEY));
                object.put("versionName", versionName);
                object.put("business", "loading");
                object.put("platform", "APK");
                this.mBusinessRequest.getAdvertsRequest(uuid, "APPKEY_LOADING_2", object.toString(), new GetgetAdvertsRequestListener(new WeakReference(this)));
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    /* access modifiers changed from: private */
    public void downloadNewFile() {
        if (this.listLoading.size() != 0) {
            List<String> items = FileUtil.scan(new File(this.bitmapPath));
            Map<String, String> vaildItems = new HashMap<>();
            for (int i = 0; i < this.listLoading.size(); i++) {
                LoadingBo mLoadingBo = this.listLoading.get(i);
                if (TimeUtil.compareToCurrentTime(mLoadingBo.getEndTime()) <= 0) {
                    vaildItems.put(mLoadingBo.getMd5(), mLoadingBo.getMd5());
                    String md5 = "";
                    try {
                        md5 = MD5Util.getFileMD5(new File(getFilesDir() + WVNativeCallbackUtil.SEPERATER + "loading" + WVNativeCallbackUtil.SEPERATER + mLoadingBo.getMd5()));
                    } catch (Exception e) {
                        ZpLogger.i("loading", "file not found");
                    }
                    if (!TextUtils.isEmpty(md5)) {
                        md5 = md5.toUpperCase();
                    }
                    if (!mLoadingBo.getMd5().equals(md5)) {
                        final String filePath = this.bitmapPath + WVNativeCallbackUtil.SEPERATER + mLoadingBo.getMd5();
                        RequestOptions requestOptions = new RequestOptions();
                        requestOptions.override(Integer.MIN_VALUE, Integer.MIN_VALUE);
                        GlideManager.get().loadImage((Context) this, mLoadingBo.getImgUrl(), requestOptions, (Target<Bitmap>) new CustomTarget<Bitmap>() {
                            public void onResourceReady(Bitmap resource, Transition<? super Bitmap> transition) {
                                LoadingService.this.saveBitmap(resource, filePath);
                            }

                            public void onLoadCleared(Drawable placeholder) {
                            }
                        });
                    }
                }
            }
            FileUtil.write(this, getFilesDir() + WVNativeCallbackUtil.SEPERATER + "loading_cache_json", JSON.toJSONString(this.listLoading));
            for (int i2 = 0; i2 < items.size(); i2++) {
                if (!TextUtils.isEmpty(items.get(i2))) {
                    String fileName = items.get(i2).substring(items.get(i2).lastIndexOf(WVNativeCallbackUtil.SEPERATER) + 1, items.get(i2).length());
                    if (vaildItems == null || vaildItems.isEmpty()) {
                        ZpLogger.i("DeleteFile", "delete file " + fileName + " result:" + FileUtil.deleteFile(new File(items.get(i2))));
                    } else if (!vaildItems.containsKey(fileName)) {
                        ZpLogger.i("DeleteFile", "delete file " + fileName + " result:" + FileUtil.deleteFile(new File(items.get(i2))));
                    }
                }
            }
        }
    }

    /* access modifiers changed from: private */
    public void saveBitmap(final Bitmap bitmap, final String filePath) {
        new AsyncTask<Object, Object, Object>() {
            /* access modifiers changed from: protected */
            public Object doInBackground(Object... objects) {
                try {
                    FileUtil.saveBitmap(bitmap, filePath);
                    return null;
                } catch (Throwable e) {
                    e.printStackTrace();
                    return null;
                }
            }

            /* access modifiers changed from: protected */
            public void onPostExecute(Object o) {
            }
        }.execute(new Object[0]);
    }

    private static class GetgetAdvertsRequestListener implements RequestListener<List<LoadingBo>> {
        private WeakReference<LoadingService> ref;

        public GetgetAdvertsRequestListener(WeakReference<LoadingService> service) {
            this.ref = service;
        }

        public void onRequestDone(List<LoadingBo> data, int resultCode, String msg) {
            LoadingService service = (LoadingService) this.ref.get();
            ZpLogger.i(service == null ? "LoadingService" : service.TAG, service == null ? "LoadingService" : service.TAG + ",GetgetAdvertsRequestListener resultCode = " + resultCode);
            if (service != null) {
                boolean unused = service.isDoing = false;
                if (resultCode == 200) {
                    List unused2 = service.listLoading = data;
                    service.downloadNewFile();
                    service.stopSelf();
                }
            }
        }
    }

    private void test() {
    }
}
