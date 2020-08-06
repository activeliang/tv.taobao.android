package android.taobao.windvane.runtimepermission;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.provider.Settings;
import android.support.v4.app.ActivityCompat;
import android.taobao.windvane.runtimepermission.WVManifest;
import java.util.ArrayList;

public final class PermissionProposer {
    /* access modifiers changed from: private */
    public static PermissionRequestTask sCurrentPermissionRequestTask;

    public static synchronized PermissionRequestTask buildPermissionTask(Context context, String[] permissions) {
        PermissionRequestTask task;
        synchronized (PermissionProposer.class) {
            if (context == null) {
                throw new NullPointerException("context can not be null");
            }
            if (permissions != null) {
                if (permissions.length != 0) {
                    task = new PermissionRequestTask();
                    Context unused = task.context = context;
                    String[] unused2 = task.permissions = permissions;
                }
            }
            throw new NullPointerException("permissions can not be null");
        }
        return task;
    }

    public static synchronized PermissionRequestTask buildSystemAlertPermissionTask(Activity activity) {
        PermissionRequestTask buildPermissionTask;
        synchronized (PermissionProposer.class) {
            buildPermissionTask = buildPermissionTask(activity, new String[]{"android.permission.SYSTEM_ALERT_WINDOW"});
        }
        return buildPermissionTask;
    }

    static void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        if (sCurrentPermissionRequestTask != null) {
            sCurrentPermissionRequestTask.onPermissionGranted(verifyPermissions(grantResults));
            sCurrentPermissionRequestTask = null;
        }
    }

    static void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (Build.VERSION.SDK_INT >= 23) {
            sCurrentPermissionRequestTask.onPermissionGranted(Settings.canDrawOverlays(sCurrentPermissionRequestTask.getContext()));
        }
        sCurrentPermissionRequestTask = null;
    }

    private static boolean verifyPermissions(int[] grantResults) {
        if (grantResults.length < 1) {
            return false;
        }
        for (int result : grantResults) {
            if (result != 0) {
                return false;
            }
        }
        return true;
    }

    public static class PermissionRequestTask {
        /* access modifiers changed from: private */
        public Context context;
        private String explain;
        private Runnable permissionDeniedRunnable;
        private Runnable permissionGrantedRunnable;
        /* access modifiers changed from: private */
        public String[] permissions;

        public Context getContext() {
            return this.context;
        }

        public PermissionRequestTask setRationalStr(String explain2) {
            this.explain = explain2;
            return this;
        }

        public PermissionRequestTask setTaskOnPermissionGranted(Runnable runnable) {
            if (runnable == null) {
                throw new NullPointerException("permissionGrantedRunnable is null");
            }
            this.permissionGrantedRunnable = runnable;
            return this;
        }

        public PermissionRequestTask setTaskOnPermissionDenied(Runnable runnable) {
            this.permissionDeniedRunnable = runnable;
            return this;
        }

        public void execute() {
            if (Build.VERSION.SDK_INT >= 23) {
                if (this.permissions.length != 1 || !this.permissions[0].equals("android.permission.SYSTEM_ALERT_WINDOW")) {
                    ArrayList<String> needGrantedPermissions = new ArrayList<>();
                    for (String permission : this.permissions) {
                        if (ActivityCompat.checkSelfPermission(this.context, permission) != 0) {
                            needGrantedPermissions.add(permission);
                        }
                    }
                    if (needGrantedPermissions.size() == 0) {
                        this.permissionGrantedRunnable.run();
                        return;
                    }
                    Intent intent = new Intent();
                    intent.setClass(this.context, PermissionActivity.class);
                    if (!(this.context instanceof Activity)) {
                        intent.addFlags(268435456);
                    }
                    intent.putExtra("permissions", this.permissions);
                    intent.putExtra("explain", this.explain);
                    PermissionRequestTask unused = PermissionProposer.sCurrentPermissionRequestTask = this;
                    this.context.startActivity(intent);
                } else if (!Settings.canDrawOverlays(this.context)) {
                    Intent intent2 = new Intent();
                    intent2.setClass(this.context, PermissionActivity.class);
                    intent2.putExtra("permissions", this.permissions);
                    PermissionRequestTask unused2 = PermissionProposer.sCurrentPermissionRequestTask = this;
                    this.context.startActivity(intent2);
                } else {
                    this.permissionGrantedRunnable.run();
                }
            } else if (Build.VERSION.SDK_INT < 18) {
                this.permissionGrantedRunnable.run();
            } else if (WVManifest.Permission.isPermissionGranted(this.context, this.permissions)) {
                this.permissionGrantedRunnable.run();
            } else {
                this.permissionDeniedRunnable.run();
            }
        }

        /* access modifiers changed from: package-private */
        public void onPermissionGranted(boolean result) {
            if (result) {
                if (this.permissionGrantedRunnable != null) {
                    this.permissionGrantedRunnable.run();
                }
            } else if (this.permissionDeniedRunnable != null) {
                this.permissionDeniedRunnable.run();
            }
            destroy();
        }

        private void destroy() {
            this.context = null;
            this.permissionGrantedRunnable = null;
            this.permissionDeniedRunnable = null;
        }
    }
}
