package android.taobao.atlas.runtime;

import android.app.ActivityManager;
import android.content.Context;
import android.os.Handler;
import android.os.HandlerThread;
import android.os.Looper;
import android.os.Message;
import android.os.Process;
import android.taobao.atlas.framework.Atlas;
import android.taobao.atlas.versionInfo.BaselineInfoManager;
import android.text.TextUtils;
import android.util.Log;
import android.widget.Toast;
import com.taobao.detail.domain.tuwen.TuwenConstants;
import java.io.File;
import org.osgi.framework.Bundle;
import org.osgi.framework.BundleEvent;
import org.osgi.framework.BundleListener;

public class SecurityHandler implements BundleListener {
    static final String TAG = "SecurityBundleListner";
    /* access modifiers changed from: private */
    public boolean isSecurityCheckFailed;
    private Handler mHandler;
    private HandlerThread mHandlerThread;
    ShutdownProcessHandler shutdownProcessHandler;

    public SecurityHandler() {
        this.mHandlerThread = null;
        this.shutdownProcessHandler = new ShutdownProcessHandler();
        this.isSecurityCheckFailed = false;
        this.mHandlerThread = new HandlerThread("Check bundle security");
        this.mHandlerThread.start();
        this.mHandler = new SecurityBundleHandler(this.mHandlerThread.getLooper());
    }

    public void bundleChanged(BundleEvent event) {
        switch (event.getType()) {
            case 1:
            case 8:
                Message msg = Message.obtain();
                msg.obj = event.getBundle();
                this.mHandler.sendMessageDelayed(msg, 3000);
                return;
            default:
                return;
        }
    }

    private final class SecurityBundleHandler extends Handler {
        public SecurityBundleHandler(Looper looper) {
            super(looper);
        }

        public void handleMessage(Message msg) {
            if (msg != null && !SecurityHandler.this.isSecurityCheckFailed) {
                Bundle bundle = (Bundle) msg.obj;
                if (!BaselineInfoManager.instance().isUpdated(bundle.getLocation()) && !BaselineInfoManager.instance().isDexPatched(bundle.getLocation())) {
                    String location = bundle.getLocation();
                    File file = null;
                    if ((TextUtils.isEmpty(location) || (file = Atlas.getInstance().getBundleFile(location)) != null) && file != null) {
                        if (!RuntimeVariables.verifyBundle(file.getAbsolutePath())) {
                            Log.e(SecurityHandler.TAG, "Security check failed. " + location);
                            new Handler(Looper.getMainLooper()).post(new postInvalidBundle());
                            boolean unused = SecurityHandler.this.isSecurityCheckFailed = true;
                        }
                        if (!SecurityHandler.this.isSecurityCheckFailed) {
                            Log.d(SecurityHandler.TAG, "Security check success. " + location);
                        }
                    }
                }
            }
        }
    }

    public static class ShutdownProcessHandler extends Handler {
        public void handleMessage(Message msg) {
            Process.killProcess(Process.myPid());
        }
    }

    private class postInvalidBundle implements Runnable {
        private postInvalidBundle() {
        }

        public void run() {
            if (SecurityHandler.this.isRunningForeground(RuntimeVariables.androidApplication)) {
                Toast.makeText(RuntimeVariables.androidApplication, "检测到安装文件被损坏，请卸载后重新安装！", 1).show();
            }
            SecurityHandler.this.shutdownProcessHandler.sendEmptyMessageDelayed(0, 5000);
        }
    }

    /* access modifiers changed from: private */
    public boolean isRunningForeground(Context context) {
        String currentPackageName = ((ActivityManager) context.getSystemService(TuwenConstants.MODEL_LIST_KEY.ACTIVITY)).getRunningTasks(1).get(0).topActivity.getPackageName();
        if (TextUtils.isEmpty(currentPackageName) || !currentPackageName.equals(context.getPackageName())) {
            return false;
        }
        return true;
    }
}
