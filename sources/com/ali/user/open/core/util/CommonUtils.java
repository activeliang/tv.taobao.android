package com.ali.user.open.core.util;

import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.widget.Toast;
import com.ali.user.open.core.broadcast.LoginAction;
import com.ali.user.open.core.callback.FailureCallback;
import com.ali.user.open.core.context.KernelContext;
import com.ali.user.open.core.model.ResultCode;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.net.Inet4Address;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.util.Enumeration;

public class CommonUtils {
    public static String mAppVersion;

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
        toast("member_sdk_system_exception");
    }

    public static void toast(final String strId) {
        KernelContext.executorService.postUITask(new Runnable() {
            public void run() {
                Toast.makeText(KernelContext.getApplicationContext(), ResourceUtils.getString(strId), 0).show();
            }
        });
    }

    public static boolean isNetworkAvailable() {
        if (KernelContext.getApplicationContext() == null) {
            return true;
        }
        return isNetworkAvailable(KernelContext.getApplicationContext());
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
                            return inetAddress.getHostAddress();
                        }
                    }
                }
            }
        } catch (SocketException e) {
        }
        return null;
    }

    public static String toString(Throwable t) {
        StringWriter sw = new StringWriter();
        t.printStackTrace(new PrintWriter(sw));
        return sw.toString();
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

    public static void sendBroadcast(LoginAction loginAction) {
        Intent intent = new Intent();
        intent.setAction(loginAction.name());
        intent.setPackage(KernelContext.getApplicationContext().getPackageName());
        KernelContext.getApplicationContext().sendBroadcast(intent);
    }

    public static String getAppVersion() {
        if (mAppVersion == null) {
            try {
                PackageInfo packageInfo = KernelContext.getApplicationContext().getPackageManager().getPackageInfo(KernelContext.getApplicationContext().getPackageName(), 0);
                if (packageInfo != null) {
                    mAppVersion = packageInfo.versionName;
                }
            } catch (PackageManager.NameNotFoundException e) {
                e.printStackTrace();
            } catch (Exception e2) {
                e2.printStackTrace();
            }
        }
        return mAppVersion;
    }

    public static String getAndroidAppVersion() {
        return "android_" + getAppVersion();
    }
}
