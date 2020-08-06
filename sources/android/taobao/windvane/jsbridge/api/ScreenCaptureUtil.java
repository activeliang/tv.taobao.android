package android.taobao.windvane.jsbridge.api;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.Fragment;
import android.app.FragmentManager;
import android.content.Context;
import android.content.ContextWrapper;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.taobao.windvane.cache.WVCacheManager;
import android.taobao.windvane.jsbridge.utils.WVUtils;
import android.view.View;
import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public final class ScreenCaptureUtil {
    private static final int IMAGE_SAVE_REQUEST_CODE = 1553;

    private ScreenCaptureUtil() {
    }

    public static long capture(View view, boolean inAlbum) throws IOException {
        Bitmap bmp = getBitmapFromView(view);
        if (bmp == null) {
            throw new RuntimeException("can't get bitmap from the view");
        }
        String cacheDir = WVCacheManager.getInstance().getCacheDir(true);
        long result = WVUtils.saveBitmapToCache(bmp);
        if (inAlbum) {
            Context context = view.getContext();
            if (checkSavePlan(context)) {
                writeToDefaultPathAndShowInAlbum(bmp, context);
            } else {
                writeToCustomizedPathAndShowInAlbum(bmp, context);
            }
        }
        return result;
    }

    @SuppressLint({"InlinedApi"})
    private static void writeToCustomizedPathAndShowInAlbum(Bitmap bmp, Context context) {
        Intent intent = new Intent("android.intent.action.CREATE_DOCUMENT");
        intent.addCategory("android.intent.category.OPENABLE");
        intent.setType("image/jpeg");
        intent.putExtra("android.intent.extra.TITLE", generateFileName());
        Context activityContext = context;
        while ((activityContext instanceof ContextWrapper) && !(activityContext instanceof Activity)) {
            activityContext = ((ContextWrapper) activityContext).getBaseContext();
        }
        if (activityContext == null || (activityContext instanceof Activity)) {
            FragmentManager fm = ((Activity) activityContext).getFragmentManager();
            Fragment fragment = fm.findFragmentByTag("ScreenCaptureBackFragment");
            if (fragment == null) {
                fragment = new BackFragment(bmp);
                fm.beginTransaction().add(fragment, "ScreenCaptureBackFragment").commit();
                fm.executePendingTransactions();
            }
            fragment.startActivityForResult(intent, IMAGE_SAVE_REQUEST_CODE);
            return;
        }
        throw new RuntimeException("can't popup activity for user to choose path");
    }

    private static void writeToDefaultPathAndShowInAlbum(Bitmap bmp, Context context) throws IOException {
        File publicDir = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES);
        if (publicDir.exists() || publicDir.mkdirs()) {
            Uri uri = Uri.fromFile(new File(publicDir, generateFileName()));
            saveBitmapToPath(context, bmp, uri);
            notifyNewMedia(uri, context);
            return;
        }
        throw new RuntimeException("can't create file directory for image");
    }

    private static boolean checkSavePlan(Context context) {
        return Build.VERSION.SDK_INT < 19 || context.checkCallingOrSelfPermission("android.permission.WRITE_EXTERNAL_STORAGE") == 0;
    }

    /* access modifiers changed from: private */
    /* JADX WARNING: Removed duplicated region for block: B:9:0x0028  */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public static void saveBitmapToPath(android.content.Context r5, android.graphics.Bitmap r6, android.net.Uri r7) throws java.io.IOException {
        /*
            r0 = 0
            android.content.ContentResolver r3 = r5.getContentResolver()     // Catch:{ all -> 0x0025 }
            java.lang.String r4 = "w"
            android.os.ParcelFileDescriptor r2 = r3.openFileDescriptor(r7, r4)     // Catch:{ all -> 0x0025 }
            java.io.FileOutputStream r1 = new java.io.FileOutputStream     // Catch:{ all -> 0x0025 }
            java.io.FileDescriptor r3 = r2.getFileDescriptor()     // Catch:{ all -> 0x0025 }
            r1.<init>(r3)     // Catch:{ all -> 0x0025 }
            android.graphics.Bitmap$CompressFormat r3 = android.graphics.Bitmap.CompressFormat.JPEG     // Catch:{ all -> 0x002f }
            r4 = 100
            r6.compress(r3, r4, r1)     // Catch:{ all -> 0x002f }
            if (r1 == 0) goto L_0x0024
            r1.flush()
            r1.close()
        L_0x0024:
            return
        L_0x0025:
            r3 = move-exception
        L_0x0026:
            if (r0 == 0) goto L_0x002e
            r0.flush()
            r0.close()
        L_0x002e:
            throw r3
        L_0x002f:
            r3 = move-exception
            r0 = r1
            goto L_0x0026
        */
        throw new UnsupportedOperationException("Method not decompiled: android.taobao.windvane.jsbridge.api.ScreenCaptureUtil.saveBitmapToPath(android.content.Context, android.graphics.Bitmap, android.net.Uri):void");
    }

    private static Bitmap getBitmapFromView(View view) {
        view.setDrawingCacheEnabled(true);
        view.destroyDrawingCache();
        view.buildDrawingCache();
        return view.getDrawingCache();
    }

    private static String generateFileName() {
        return "SHOUTAO_" + new SimpleDateFormat("yyyy-MM-dd_HH-mm-ss", Locale.US).format(new Date()) + ".jpg";
    }

    /* access modifiers changed from: private */
    public static void notifyNewMedia(Uri uri, Context context) {
        Intent intent = new Intent("android.intent.action.MEDIA_SCANNER_SCAN_FILE");
        intent.setData(uri);
        context.sendBroadcast(intent);
    }

    private static final class BackFragment extends Fragment {
        private final Bitmap bmp;

        public BackFragment(Bitmap bmp2) {
            this.bmp = bmp2;
        }

        public void onActivityResult(int requestCode, int resultCode, Intent data) {
            super.onActivityResult(requestCode, resultCode, data);
            if (requestCode == ScreenCaptureUtil.IMAGE_SAVE_REQUEST_CODE && resultCode == -1) {
                try {
                    ScreenCaptureUtil.saveBitmapToPath(getActivity(), this.bmp, data.getData());
                    ScreenCaptureUtil.notifyNewMedia(data.getData(), getActivity());
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }
}
