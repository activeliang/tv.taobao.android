package com.tvtaobao.android.ui3.helper;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.ServiceConnection;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Rect;
import android.hardware.display.VirtualDisplay;
import android.media.Image;
import android.media.ImageReader;
import android.media.projection.MediaProjection;
import android.media.projection.MediaProjectionManager;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Process;
import android.util.DisplayMetrics;
import android.view.Display;
import android.view.SurfaceView;
import android.view.TextureView;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.FrameLayout;
import com.bftv.fui.constantplugin.Constant;
import com.tvtaobao.android.ui3.UI3Logger;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.lang.reflect.Method;
import java.nio.ByteBuffer;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ScreenCapture {
    public static final String ACTION_TVTAOBAO_SCREEN_CAPTURE_SAVE = "ACTION_TVTAOBAO_SCREEN_CAPTURE_SAVE";
    public static final String KEY_SCREEN_CAPTURE_SAVE_PATH = "KEY_SCREEN_CAPTURE_SAVE_PATH";
    public static final String RELATIVE_PATH = (File.separator + "tvtaobao" + File.separator);
    /* access modifiers changed from: private */
    public static final String TAG = ScreenCapture.class.getSimpleName();

    public interface CaptureCallBack {
        void done(List<Bitmap> list, Throwable th);
    }

    public enum CaptureWay {
        auto,
        media_projection,
        normal
    }

    public static boolean capture(Context context, final CaptureCallBack captureCallBack, CaptureWay captureWay) {
        if (context != null) {
            if (captureWay == CaptureWay.auto) {
                if (Build.VERSION.SDK_INT >= 21) {
                    Intent intent = new Intent(context, CaptureAct.class);
                    if (context.getPackageManager().resolveActivity(intent, 65536) != null) {
                        intent.addFlags(268435456);
                        context.registerReceiver(new BroadcastReceiver() {
                            private List<Bitmap> getBmpsFromPath(String path) {
                                List<Bitmap> bitmaps;
                                try {
                                    File scDir = new File(path);
                                    if (scDir == null || !scDir.exists()) {
                                        return null;
                                    }
                                    File[] files00 = scDir.listFiles();
                                    List<File> files0 = Arrays.asList(files00);
                                    Collections.sort(files0, new Comparator<File>() {
                                        public int compare(File o1, File o2) {
                                            return o2.getName().compareTo(o1.getName());
                                        }
                                    });
                                    File[] files = (File[]) files0.toArray(new File[files00.length]);
                                    if (files == null || files.length <= 0) {
                                        return null;
                                    }
                                    int i = files.length - 1;
                                    List<Bitmap> bitmaps2 = null;
                                    while (i >= 0) {
                                        try {
                                            File f = files[i];
                                            if (f != null) {
                                                try {
                                                    if (f.getName().contains(".png")) {
                                                        if (bitmaps2 == null) {
                                                            bitmaps = new ArrayList<>();
                                                        } else {
                                                            bitmaps = bitmaps2;
                                                        }
                                                        try {
                                                            Bitmap bmp = BitmapFactory.decodeStream(new FileInputStream(f));
                                                            if (bmp != null) {
                                                                bitmaps.add(bmp);
                                                            }
                                                        } catch (Throwable th) {
                                                            e = th;
                                                            e.printStackTrace();
                                                            i--;
                                                            bitmaps2 = bitmaps;
                                                        }
                                                        i--;
                                                        bitmaps2 = bitmaps;
                                                    }
                                                } catch (Throwable th2) {
                                                    e = th2;
                                                    bitmaps = bitmaps2;
                                                    e.printStackTrace();
                                                    i--;
                                                    bitmaps2 = bitmaps;
                                                }
                                            }
                                            bitmaps = bitmaps2;
                                            i--;
                                            bitmaps2 = bitmaps;
                                        } catch (Throwable th3) {
                                            e = th3;
                                            List<Bitmap> list = bitmaps2;
                                            e.printStackTrace();
                                            return null;
                                        }
                                    }
                                    return bitmaps2;
                                } catch (Throwable th4) {
                                    e = th4;
                                }
                            }

                            public void onReceive(Context context, Intent intent) {
                                String path = ScreenCapture.parseSavePath(intent);
                                if (path != null) {
                                    List<Bitmap> bitmaps = getBmpsFromPath(path);
                                    if (captureCallBack != null) {
                                        captureCallBack.done(bitmaps, bitmaps == null ? new RuntimeException("截屏失败，联系开发") : null);
                                    }
                                }
                            }
                        }, new IntentFilter(ACTION_TVTAOBAO_SCREEN_CAPTURE_SAVE));
                        context.startActivity(intent);
                        return true;
                    }
                } else if (tryAndroidSystemCapture(context, captureCallBack)) {
                    return true;
                } else {
                    if ((context instanceof Activity) && tryActivityCapture((Activity) context, captureCallBack)) {
                        return true;
                    }
                    if (captureCallBack != null) {
                        captureCallBack.done((List<Bitmap>) null, new RuntimeException("系统版本（Api：" + Build.VERSION.SDK_INT + "）,截屏失败(code:0x99)"));
                    }
                }
            } else if (captureWay == CaptureWay.media_projection) {
                Intent intent2 = new Intent(context, CaptureAct.class);
                if (context.getPackageManager().resolveActivity(intent2, 65536) != null) {
                    intent2.setFlags(268435456);
                    context.startActivity(intent2);
                    return true;
                }
            } else if (tryAndroidSystemCapture(context, captureCallBack)) {
                return true;
            } else {
                if ((context instanceof Activity) && tryActivityCapture((Activity) context, captureCallBack)) {
                    return true;
                }
                if (captureCallBack != null) {
                    captureCallBack.done((List<Bitmap>) null, new RuntimeException("系统版本（Api：" + Build.VERSION.SDK_INT + "）,截屏失败(code:0x99)"));
                }
            }
        }
        return false;
    }

    public static boolean capture(Context context, CaptureCallBack captureCallBack) {
        return capture(context, captureCallBack, CaptureWay.auto);
    }

    public static String parseSavePath(Intent intent) {
        if (intent != null) {
            return intent.getStringExtra(KEY_SCREEN_CAPTURE_SAVE_PATH);
        }
        return null;
    }

    private static boolean tryAndroidSystemCapture(Context context, final CaptureCallBack captureCallBack) {
        Method ss;
        try {
            Class sc = Class.forName("android.view.SurfaceControl");
            if (!(sc == null || (ss = sc.getDeclaredMethod("screenshot", new Class[]{Integer.TYPE, Integer.TYPE})) == null)) {
                Object rtn = ss.invoke((Object) null, new Object[]{Integer.valueOf(context.getResources().getDisplayMetrics().widthPixels), Integer.valueOf(context.getResources().getDisplayMetrics().heightPixels)});
                if (rtn != null && (rtn instanceof Bitmap)) {
                    final List<Bitmap> list = Arrays.asList(new Bitmap[]{(Bitmap) rtn});
                    saveOnSdCard(context, list, new Runnable() {
                        public void run() {
                            if (captureCallBack != null) {
                                captureCallBack.done(list, (Throwable) null);
                            }
                        }
                    });
                    return true;
                }
            }
        } catch (Throwable e) {
            e.printStackTrace();
        }
        return false;
    }

    private static boolean tryActivityCapture(Activity activity, final CaptureCallBack captureCallBack) {
        if (!(activity == null || activity.getWindow() == null || activity.getWindow().getDecorView() == null)) {
            View v = activity.getWindow().getDecorView().getRootView();
            if (v == null) {
                v = activity.getWindow().getDecorView();
            }
            final View rootView = v;
            if (rootView != null) {
                try {
                    rootView.setDrawingCacheEnabled(true);
                    rootView.buildDrawingCache();
                    Bitmap bitmap = rootView.getDrawingCache();
                    final List<Bitmap> list = new ArrayList<>();
                    list.add(bitmap);
                    new Runnable() {
                        private Rect getRect(View view) {
                            try {
                                Rect rect = new Rect();
                                view.getDrawingRect(rect);
                                return rect;
                            } catch (Throwable e) {
                                e.printStackTrace();
                                return null;
                            }
                        }

                        private Rect getRelativeRect(View view) {
                            try {
                                Rect rect = new Rect();
                                view.getDrawingRect(rect);
                                ((ViewGroup) rootView).offsetDescendantRectToMyCoords(view, rect);
                                return rect;
                            } catch (Throwable e) {
                                e.printStackTrace();
                                return null;
                            }
                        }

                        public void findSurfaceView(List<SurfaceView> surfaceViewList, ViewGroup viewGroup) {
                            int i = 0;
                            while (viewGroup != null && i < viewGroup.getChildCount()) {
                                View child = viewGroup.getChildAt(i);
                                if (child instanceof SurfaceView) {
                                    surfaceViewList.add((SurfaceView) child);
                                } else if (child instanceof ViewGroup) {
                                    findSurfaceView(surfaceViewList, (ViewGroup) child);
                                }
                                i++;
                            }
                        }

                        private void collectSurfaceViewContent() {
                            List<SurfaceView> surfaceViewList = new ArrayList<>();
                            findSurfaceView(surfaceViewList, (ViewGroup) rootView);
                            if (!surfaceViewList.isEmpty()) {
                                Map<SurfaceView, Bitmap> sb = new HashMap<>();
                                int i = 0;
                                while (i < surfaceViewList.size()) {
                                    SurfaceView item = surfaceViewList.get(i);
                                    i = (item == null || item.getHolder() != null) ? i + 1 : i + 1;
                                }
                                if (!sb.isEmpty()) {
                                    for (Map.Entry<SurfaceView, Bitmap> entry : sb.entrySet()) {
                                        list.add(entry.getValue());
                                    }
                                }
                            }
                        }

                        public void findTextureView(List<TextureView> surfaceViewList, ViewGroup viewGroup) {
                            int i = 0;
                            while (viewGroup != null && i < viewGroup.getChildCount()) {
                                View child = viewGroup.getChildAt(i);
                                if (child instanceof TextureView) {
                                    surfaceViewList.add((TextureView) child);
                                } else if (child instanceof ViewGroup) {
                                    findTextureView(surfaceViewList, (ViewGroup) child);
                                }
                                i++;
                            }
                        }

                        private void collectTextureViewContent() {
                            Bitmap bmp;
                            List<TextureView> list1 = new ArrayList<>();
                            findTextureView(list1, (ViewGroup) rootView);
                            if (!list1.isEmpty()) {
                                Map<TextureView, Bitmap> sb = new HashMap<>();
                                for (int i = 0; i < list1.size(); i++) {
                                    TextureView item = list1.get(i);
                                    if (!(item == null || (bmp = item.getBitmap()) == null)) {
                                        sb.put(item, bmp);
                                    }
                                }
                                if (!sb.isEmpty()) {
                                    for (Map.Entry<TextureView, Bitmap> entry : sb.entrySet()) {
                                        list.add(entry.getValue());
                                    }
                                }
                            }
                        }

                        public void run() {
                            if (rootView instanceof ViewGroup) {
                                collectSurfaceViewContent();
                                collectTextureViewContent();
                            }
                        }
                    }.run();
                    saveOnSdCard(activity, list, new Runnable() {
                        public void run() {
                            if (captureCallBack != null) {
                                captureCallBack.done(list, (Throwable) null);
                            }
                        }
                    });
                    return true;
                } catch (Throwable e) {
                    e.printStackTrace();
                    if (captureCallBack != null) {
                        captureCallBack.done((List<Bitmap>) null, e);
                    }
                }
            }
        }
        return false;
    }

    /* access modifiers changed from: private */
    public static void saveOnSdCard(Context context, List<Bitmap> bitmaps, Runnable task) {
        File extSD;
        UI3Logger.i(TAG, "saveOnSdCard ()");
        if (bitmaps != null && !bitmaps.isEmpty()) {
            File targetDir = null;
            if ("mounted".equals(Environment.getExternalStorageState()) && (extSD = Environment.getExternalStorageDirectory()) != null && extSD.exists()) {
                try {
                    File dstDir = new File(extSD.getPath() + RELATIVE_PATH + "screenshot_" + new SimpleDateFormat("yyyyMMddHHmmss").format(new Date()) + File.separator + File.separator);
                    if (!dstDir.exists()) {
                        boolean mkdirRtn = dstDir.mkdirs();
                    }
                    if (dstDir.exists()) {
                        targetDir = dstDir;
                    }
                } catch (Throwable e) {
                    e.printStackTrace();
                    targetDir = null;
                }
            }
            if (targetDir != null) {
                int i = 0;
                while (i < bitmaps.size()) {
                    try {
                        Bitmap bmp = bitmaps.get(i);
                        if (bmp != null && !bmp.isRecycled()) {
                            File f = new File(targetDir, i + ".png");
                            if (!f.exists()) {
                                f.createNewFile();
                            }
                            FileOutputStream os = new FileOutputStream(f);
                            bmp.compress(Bitmap.CompressFormat.PNG, 100, os);
                            os.flush();
                            os.close();
                            UI3Logger.i(TAG, "save screenshot:" + f.getPath());
                        }
                        i++;
                    } catch (Throwable e2) {
                        e2.printStackTrace();
                        return;
                    }
                }
                UI3Logger.i(TAG, "saveOnSdCard show alertDialog()");
                final File tmpDir = targetDir;
                final Context context2 = context;
                AlertDialog alertDialog = new AlertDialog.Builder(context).setMessage("截屏成功，请返回ApkInstaller查看").setTitle("提示").setPositiveButton(Constant.OK, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        Intent intent = new Intent(ScreenCapture.ACTION_TVTAOBAO_SCREEN_CAPTURE_SAVE);
                        intent.putExtra(ScreenCapture.KEY_SCREEN_CAPTURE_SAVE_PATH, tmpDir.getAbsolutePath());
                        context2.sendBroadcast(intent);
                    }
                }).create();
                final Runnable runnable = task;
                alertDialog.setOnDismissListener(new DialogInterface.OnDismissListener() {
                    public void onDismiss(DialogInterface dialog) {
                        if (runnable != null) {
                            runnable.run();
                        }
                    }
                });
                alertDialog.show();
            }
        }
    }

    public static class CaptureAct extends Activity {
        private ServiceConnection conn;
        private FrameLayout contentView;
        private Handler mHandler;
        /* access modifiers changed from: private */
        public MediaProjection mediaProjection;
        private MediaProjectionManager mediaProjectionManager;
        private int requestCode = -1;

        /* access modifiers changed from: protected */
        public void onActivityResult(int requestCode2, int resultCode, Intent data) {
            UI3Logger.i(ScreenCapture.TAG, "onActivityResult (" + requestCode2 + "," + resultCode + "," + data + ")");
            if (requestCode2 == this.requestCode && Build.VERSION.SDK_INT >= 21 && resultCode == -1 && this.mediaProjectionManager != null) {
                this.mediaProjection = this.mediaProjectionManager.getMediaProjection(resultCode, data);
                startProjection();
            }
        }

        /* access modifiers changed from: protected */
        public void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            UI3Logger.i(ScreenCapture.TAG, "onCreate (" + savedInstanceState + ")");
            this.contentView = new FrameLayout(this);
            setContentView(this.contentView);
            this.mHandler = new Handler();
            if (Build.VERSION.SDK_INT >= 21) {
                this.mediaProjectionManager = (MediaProjectionManager) getSystemService("media_projection");
                this.requestCode = (int) ((Math.random() * 1000.0d) + 10.0d);
                startActivityForResult(this.mediaProjectionManager.createScreenCaptureIntent(), this.requestCode);
                return;
            }
            finish();
        }

        /* access modifiers changed from: protected */
        public void onResume() {
            super.onResume();
            UI3Logger.i(ScreenCapture.TAG, "onResume ()");
        }

        /* access modifiers changed from: protected */
        public void onPause() {
            super.onPause();
            UI3Logger.i(ScreenCapture.TAG, "onPause ()");
        }

        /* access modifiers changed from: protected */
        public void onStop() {
            super.onStop();
            UI3Logger.i(ScreenCapture.TAG, "onStop ()");
        }

        /* access modifiers changed from: protected */
        public void onDestroy() {
            super.onDestroy();
            UI3Logger.i(ScreenCapture.TAG, "onDestroy ()");
            if (this.conn != null) {
                unbindService(this.conn);
                this.conn = null;
            }
            stopProjection();
            Process.killProcess(Process.myPid());
        }

        private void startProjection() {
            UI3Logger.i(ScreenCapture.TAG, "startProjection ()");
            if (this.mediaProjection != null && Build.VERSION.SDK_INT >= 21) {
                Display mDisplay = ((WindowManager) getSystemService("window")).getDefaultDisplay();
                DisplayMetrics metrics = new DisplayMetrics();
                mDisplay.getRealMetrics(metrics);
                final int mWidth = metrics.widthPixels;
                final int mHeight = metrics.heightPixels;
                int mDensity = metrics.densityDpi;
                UI3Logger.i(ScreenCapture.TAG, "CaptureAct.startProjection" + mWidth + "," + mHeight + "," + mDensity);
                final ImageReader imageReader = ImageReader.newInstance(mWidth, mHeight, 1, 2);
                final VirtualDisplay virtualDisplay = this.mediaProjection.createVirtualDisplay("TVTB_SCREEN_CAPTURE", mWidth, mHeight, mDensity, 9, imageReader.getSurface(), (VirtualDisplay.Callback) null, this.mHandler);
                this.mediaProjection.registerCallback(new MediaProjection.Callback() {
                    public void onStop() {
                        virtualDisplay.release();
                        imageReader.setOnImageAvailableListener((ImageReader.OnImageAvailableListener) null, (Handler) null);
                        CaptureAct.this.mediaProjection.unregisterCallback(this);
                    }
                }, this.mHandler);
                imageReader.setOnImageAvailableListener(new ImageReader.OnImageAvailableListener() {
                    long count = 0;

                    private void doCapture(ImageReader reader) {
                        Image image = null;
                        try {
                            Image image2 = reader.acquireLatestImage();
                            if (image2 != null) {
                                Image.Plane[] planes = image2.getPlanes();
                                ByteBuffer buffer = planes[0].getBuffer();
                                int pixelStride = planes[0].getPixelStride();
                                Bitmap bitmap = Bitmap.createBitmap(mWidth + ((planes[0].getRowStride() - (mWidth * pixelStride)) / pixelStride), mHeight, Bitmap.Config.ARGB_8888);
                                bitmap.copyPixelsFromBuffer(buffer);
                                CaptureAct.this.stopProjection();
                                ScreenCapture.saveOnSdCard(CaptureAct.this, Arrays.asList(new Bitmap[]{bitmap}), new Runnable() {
                                    public void run() {
                                        CaptureAct.this.finish();
                                    }
                                });
                            }
                            if (image2 != null) {
                                image2.close();
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                            if (image != null) {
                                image.close();
                            }
                        } catch (Throwable th) {
                            if (image != null) {
                                image.close();
                            }
                            throw th;
                        }
                    }

                    public void onImageAvailable(ImageReader reader) {
                        this.count++;
                        if (this.count == 1) {
                            doCapture(reader);
                        }
                    }
                }, this.mHandler);
            }
        }

        /* access modifiers changed from: private */
        public void stopProjection() {
            UI3Logger.i(ScreenCapture.TAG, "stopProjection ()");
            this.mHandler.post(new Runnable() {
                public void run() {
                    try {
                        if (CaptureAct.this.mediaProjection != null && Build.VERSION.SDK_INT >= 21) {
                            CaptureAct.this.mediaProjection.stop();
                        }
                    } catch (Throwable e) {
                        e.printStackTrace();
                    }
                }
            });
        }
    }
}
