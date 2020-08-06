package com.ali.auth.third.core.util;

import android.app.ActivityManager;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Environment;
import android.os.Process;
import android.os.StatFs;
import android.provider.Settings;
import android.text.format.Formatter;
import android.util.Log;
import android.widget.Toast;
import com.ali.auth.third.core.broadcast.LoginAction;
import com.ali.auth.third.core.callback.FailureCallback;
import com.ali.auth.third.core.context.KernelContext;
import com.ali.auth.third.core.message.Message;
import com.ali.auth.third.core.model.ResultCode;
import com.taobao.detail.domain.tuwen.TuwenConstants;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.net.Inet4Address;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.util.Enumeration;
import java.util.List;

public class CommonUtils {
    private static String CURRENT_PROCESS_NAME;
    public static String mAppLabel;
    public static String mAppVersion;

    public static void onFailure(final FailureCallback failureCallback, final Message message) {
        KernelContext.executorService.postUITask(new Runnable() {
            public void run() {
                if (failureCallback != null) {
                    failureCallback.onFailure(message.code, message.message);
                }
            }
        });
    }

    public static void onFailure(final FailureCallback failureCallback, final ResultCode resultCode) {
        KernelContext.executorService.postUITask(new Runnable() {
            public void run() {
                if (failureCallback != null) {
                    failureCallback.onFailure(resultCode.code, resultCode.message);
                }
            }
        });
    }

    public static void onFailure(final FailureCallback failureCallback, final int resultCode, final String msg) {
        KernelContext.executorService.postUITask(new Runnable() {
            public void run() {
                if (failureCallback != null) {
                    failureCallback.onFailure(resultCode, msg);
                }
            }
        });
    }

    public static void toastSystemException() {
        toast("com_taobao_tae_sdk_system_exception");
    }

    public static void toast(final String strId) {
        KernelContext.executorService.postUITask(new Runnable() {
            public void run() {
                Toast.makeText(KernelContext.getApplicationContext(), ResourceUtils.getString(strId), 0).show();
            }
        });
    }

    public static boolean isNetworkAvailable() {
        if (KernelContext.context == null) {
            return true;
        }
        return isNetworkAvailable(KernelContext.context);
    }

    public static boolean isNetworkAvailable(Context context) {
        int i = 0;
        boolean isNetworkAvailable = false;
        ConnectivityManager connectivity = (ConnectivityManager) context.getSystemService("connectivity");
        if (connectivity == null) {
            return false;
        }
        NetworkInfo[] networkInfos = connectivity.getAllNetworkInfo();
        if (networkInfos == null) {
            return false;
        }
        int length = networkInfos.length;
        while (true) {
            if (i >= length) {
                break;
            }
            NetworkInfo itemInfo = networkInfos[i];
            if (itemInfo == null || !(itemInfo.getState() == NetworkInfo.State.CONNECTED || itemInfo.getState() == NetworkInfo.State.CONNECTING)) {
                i++;
            }
        }
        isNetworkAvailable = true;
        return isNetworkAvailable;
    }

    public static String getLocalIPAddress() {
        try {
            Enumeration<NetworkInterface> mEnumeration = NetworkInterface.getNetworkInterfaces();
            while (mEnumeration.hasMoreElements()) {
                Enumeration<InetAddress> enumIPAddr = mEnumeration.nextElement().getInetAddresses();
                while (true) {
                    if (enumIPAddr.hasMoreElements()) {
                        InetAddress inetAddress = enumIPAddr.nextElement();
                        if (!inetAddress.isLoopbackAddress() && (inetAddress instanceof Inet4Address)) {
                            return inetAddress.getHostAddress().toString();
                        }
                    }
                }
            }
        } catch (SocketException ex) {
            Log.e("Error", ex.toString());
        }
        return null;
    }

    public static String toString(Throwable t) {
        StringWriter sw = new StringWriter();
        t.printStackTrace(new PrintWriter(sw));
        return sw.toString();
    }

    public static String getCurrentProcessName() {
        if (KernelContext.context == null) {
            return null;
        }
        if (CURRENT_PROCESS_NAME != null) {
            return CURRENT_PROCESS_NAME;
        }
        try {
            List<ActivityManager.RunningAppProcessInfo> runningApps = ((ActivityManager) KernelContext.context.getSystemService(TuwenConstants.MODEL_LIST_KEY.ACTIVITY)).getRunningAppProcesses();
            if (runningApps == null) {
                return null;
            }
            int pid = Process.myPid();
            for (ActivityManager.RunningAppProcessInfo procInfo : runningApps) {
                if (procInfo.pid == pid) {
                    CURRENT_PROCESS_NAME = procInfo.processName;
                    return procInfo.processName;
                }
            }
            return null;
        } catch (Exception e) {
            return null;
        }
    }

    public static String getHashString(byte[] bytes) {
        if (bytes == null) {
            return "";
        }
        StringBuilder builder = new StringBuilder();
        for (byte b : bytes) {
            builder.append(Integer.toHexString((b >> 4) & 15));
            builder.append(Integer.toHexString(b & 15));
        }
        return builder.toString();
    }

    public static String getTotalMemory() {
        long initial_memory = 0;
        try {
            BufferedReader localBufferedReader = new BufferedReader(new FileReader("/proc/meminfo"), 8192);
            String str2 = localBufferedReader.readLine();
            String[] arrayOfString = str2.split("\\s+");
            int length = arrayOfString.length;
            for (int i = 0; i < length; i++) {
                Log.i(str2, arrayOfString[i] + "\t");
            }
            initial_memory = (long) Integer.valueOf(arrayOfString[1]).intValue();
            localBufferedReader.close();
        } catch (IOException e) {
        }
        return initial_memory + " KB";
    }

    public static String getSDCardSize() {
        if (!"mounted".equals(Environment.getExternalStorageState())) {
            return "0";
        }
        StatFs sf = new StatFs(Environment.getExternalStorageDirectory().getPath());
        return Formatter.formatFileSize(KernelContext.getApplicationContext(), ((long) sf.getBlockSize()) * ((long) sf.getBlockCount()));
    }

    public static String getSystemSize() {
        StatFs sf = new StatFs(Environment.getRootDirectory().getPath());
        return Formatter.formatFileSize(KernelContext.getApplicationContext(), ((long) sf.getBlockSize()) * ((long) sf.getBlockCount()));
    }

    public static String getAndroidId() {
        return Settings.Secure.getString(KernelContext.getApplicationContext().getContentResolver(), "android_id");
    }

    public static void sendBroadcast(LoginAction loginAction) {
        Intent intent = new Intent();
        intent.setAction(loginAction.name());
        intent.setPackage(KernelContext.getApplicationContext().getPackageName());
        KernelContext.getApplicationContext().sendBroadcast(intent);
    }

    public static String getAppVersion() {
        if (mAppVersion == null) {
            try {
                PackageManager packageManager = KernelContext.getApplicationContext().getPackageManager();
                PackageInfo packageInfo = packageManager.getPackageInfo(KernelContext.getApplicationContext().getPackageName(), 0);
                if (packageInfo != null) {
                    mAppVersion = packageInfo.versionName;
                    mAppLabel = "" + packageManager.getApplicationLabel(packageInfo.applicationInfo);
                }
            } catch (PackageManager.NameNotFoundException e) {
                e.printStackTrace();
            } catch (Exception e2) {
                e2.printStackTrace();
            }
        }
        return mAppVersion;
    }

    public static String getAppLabel() {
        if (mAppLabel == null) {
            try {
                PackageManager packageManager = KernelContext.getApplicationContext().getPackageManager();
                PackageInfo packageInfo = packageManager.getPackageInfo(KernelContext.getApplicationContext().getPackageName(), 0);
                if (packageInfo != null) {
                    mAppVersion = packageInfo.versionName;
                    mAppLabel = "" + packageManager.getApplicationLabel(packageInfo.applicationInfo);
                }
            } catch (Throwable th) {
            }
        }
        return mAppLabel;
    }

    public static String getAndroidAppVersion() {
        return "android_" + getAppVersion();
    }
}
