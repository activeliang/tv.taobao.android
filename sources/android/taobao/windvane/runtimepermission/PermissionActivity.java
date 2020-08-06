package android.taobao.windvane.runtimepermission;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.widget.FrameLayout;

public class PermissionActivity extends Activity implements ActivityCompat.OnRequestPermissionsResultCallback {
    private static final int OVERLAY_PERMISSION_REQ_CODE = 123;
    private static final int PERMISSION_REQUEST = 0;
    public static final String TAG = "PermissionActivity";

    /* access modifiers changed from: protected */
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(new FrameLayout(this));
        String[] permissions = getIntent().getStringArrayExtra("permissions");
        String explain = getIntent().getStringExtra("explain");
        if (permissions == null || permissions.length != 1 || !permissions[0].equals("android.permission.SYSTEM_ALERT_WINDOW")) {
            requestCustomPermission(permissions, explain);
        } else {
            startActivityForResult(new Intent("android.settings.action.MANAGE_OVERLAY_PERMISSION", Uri.parse("package:" + getPackageName())), 123);
        }
    }

    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        if (requestCode == 0) {
            PermissionProposer.onRequestPermissionsResult(requestCode, permissions, grantResults);
        }
        finish();
    }

    /* access modifiers changed from: protected */
    public void onResume() {
        super.onResume();
    }

    /* access modifiers changed from: protected */
    public void onRestart() {
        super.onRestart();
    }

    /* access modifiers changed from: protected */
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 123) {
            PermissionProposer.onActivityResult(requestCode, resultCode, data);
        }
        finish();
    }

    private void requestCustomPermission(final String[] permissions, String explain) {
        if (!shouldShowRequestPermissionRationale(permissions) || TextUtils.isEmpty(explain)) {
            ActivityCompat.requestPermissions(this, permissions, 0);
        } else {
            new AlertDialog.Builder(this).setPositiveButton("我知道了", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int which) {
                    ActivityCompat.requestPermissions(PermissionActivity.this, permissions, 0);
                    dialog.dismiss();
                }
            }).setMessage(explain).setCancelable(false).show();
        }
    }

    private boolean shouldShowRequestPermissionRationale(String[] permissions) {
        for (String permission : permissions) {
            if (ActivityCompat.shouldShowRequestPermissionRationale(this, permission)) {
                return true;
            }
        }
        return false;
    }

    public boolean onKeyDown(int keyCode, KeyEvent event) {
        return true;
    }

    public boolean onKeyUp(int keyCode, KeyEvent event) {
        return true;
    }
}
