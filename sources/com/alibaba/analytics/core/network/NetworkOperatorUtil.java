package com.alibaba.analytics.core.network;

import android.content.Context;
import android.os.Build;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.telephony.SubscriptionManager;
import android.telephony.TelephonyManager;
import android.text.TextUtils;
import com.alibaba.analytics.utils.Logger;
import com.alibaba.analytics.utils.UTMCDevice;

public class NetworkOperatorUtil {
    private static final String NETWORK_OPERATOR_MOBILE = "中国移动";
    private static final String NETWORK_OPERATOR_TELECOM = "中国电信";
    private static final String NETWORK_OPERATOR_UNICOM = "中国联通";
    private static final String NETWORK_OPERATOR_UNKNOWN = "Unknown";
    private static final String TAG = "NetworkOperatorUtil";
    /* access modifiers changed from: private */
    public static String mCurrentNetworkOperator = "Unknown";
    private static SubscriptionManager mSubscriptionManager = null;

    public static synchronized String getCurrentNetworkOperatorName() {
        String str;
        synchronized (NetworkOperatorUtil.class) {
            str = mCurrentNetworkOperator;
        }
        return str;
    }

    static synchronized void registerSIMCardChangedInHandler(final Context context) throws Exception {
        synchronized (NetworkOperatorUtil.class) {
            if (Build.VERSION.SDK_INT >= 22) {
                if (mSubscriptionManager == null) {
                    Looper.prepare();
                    new NetworkOperatorHandler(Looper.getMainLooper()).postTask(new Runnable() {
                        public void run() {
                            NetworkOperatorUtil.registerSIMCardChanged(context);
                        }
                    });
                }
            }
        }
    }

    /* access modifiers changed from: private */
    public static synchronized void registerSIMCardChanged(final Context context) {
        synchronized (NetworkOperatorUtil.class) {
            if (Build.VERSION.SDK_INT >= 22) {
                if (mSubscriptionManager == null) {
                    try {
                        mSubscriptionManager = (SubscriptionManager) context.getSystemService("telephony_subscription_service");
                        if (mSubscriptionManager == null) {
                            Logger.d(TAG, "SubscriptionManager is null");
                        } else {
                            mSubscriptionManager.addOnSubscriptionsChangedListener(new SubscriptionManager.OnSubscriptionsChangedListener() {
                                public void onSubscriptionsChanged() {
                                    super.onSubscriptionsChanged();
                                    Logger.d(NetworkOperatorUtil.TAG, "onSubscriptionsChanged");
                                    NetworkOperatorUtil.updateNetworkOperatorName(context);
                                    Logger.d(NetworkOperatorUtil.TAG, "CurrentNetworkOperator", NetworkOperatorUtil.mCurrentNetworkOperator);
                                    UTMCDevice.updateUTMCDeviceNetworkStatus(context);
                                }
                            });
                            Logger.d(TAG, "addOnSubscriptionsChangedListener");
                        }
                    } catch (Throwable throwable) {
                        Logger.e(TAG, throwable, new Object[0]);
                    }
                }
            }
        }
        return;
    }

    static synchronized void updateNetworkOperatorName(Context context) {
        synchronized (NetworkOperatorUtil.class) {
            Logger.d(TAG, "updateNetworkOperatorName");
            try {
                TelephonyManager telephonyManager = (TelephonyManager) context.getSystemService("phone");
                if (telephonyManager == null) {
                    mCurrentNetworkOperator = "Unknown";
                } else {
                    if (telephonyManager.getSimState() == 5) {
                        String operatorCode = telephonyManager.getSimOperator();
                        if (TextUtils.isEmpty(operatorCode)) {
                            String operatorName = telephonyManager.getSimOperatorName();
                            if (TextUtils.isEmpty(operatorName)) {
                                mCurrentNetworkOperator = "Unknown";
                            } else {
                                mCurrentNetworkOperator = operatorName;
                            }
                        } else if (operatorCode.equals("46000") || operatorCode.equals("46002") || operatorCode.equals("46007") || operatorCode.equals("46008")) {
                            mCurrentNetworkOperator = NETWORK_OPERATOR_MOBILE;
                        } else if (operatorCode.equals("46001") || operatorCode.equals("46006") || operatorCode.equals("46009")) {
                            mCurrentNetworkOperator = NETWORK_OPERATOR_UNICOM;
                        } else if (operatorCode.equals("46003") || operatorCode.equals("46005") || operatorCode.equals("46011")) {
                            mCurrentNetworkOperator = NETWORK_OPERATOR_TELECOM;
                        } else {
                            String operatorName2 = telephonyManager.getSimOperatorName();
                            if (TextUtils.isEmpty(operatorName2)) {
                                mCurrentNetworkOperator = "Unknown";
                            } else {
                                mCurrentNetworkOperator = operatorName2;
                            }
                        }
                    }
                    mCurrentNetworkOperator = "Unknown";
                }
            } catch (Exception e) {
                Logger.e(TAG, e, new Object[0]);
            }
        }
    }

    private static class NetworkOperatorHandler extends Handler {
        public NetworkOperatorHandler(Looper looper) {
            super(looper);
        }

        public void postTask(Runnable runnable) {
            Logger.d();
            if (runnable != null) {
                try {
                    Message msg = Message.obtain();
                    msg.what = 1;
                    msg.obj = runnable;
                    sendMessage(msg);
                } catch (Throwable th) {
                }
            }
        }

        public void handleMessage(Message msg) {
            try {
                if (msg.obj != null && (msg.obj instanceof Runnable)) {
                    ((Runnable) msg.obj).run();
                }
            } catch (Throwable e) {
                Logger.e(NetworkOperatorUtil.TAG, e, new Object[0]);
            }
            super.handleMessage(msg);
        }
    }
}
