package com.tvtaobao.voicesdk.register;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Build;
import android.os.IBinder;
import android.os.RemoteException;
import com.tvtaobao.voicesdk.base.SDKInitConfig;
import com.tvtaobao.voicesdk.register.bo.Register;
import com.tvtaobao.voicesdk.register.interfaces.ITVTaoRegister;
import com.tvtaobao.voicesdk.register.type.RegisterType;
import com.tvtaobao.voicesdk.utils.LogPrint;
import com.yunos.tv.core.CoreApplication;
import com.yunos.tv.core.common.RequestListener;
import com.yunos.tvtaobao.biz.request.BusinessRequest;
import java.util.Arrays;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class LPR {
    /* access modifiers changed from: private */
    public static String TAG = "LPR";
    private static LPR lpr;
    private Context context;
    /* access modifiers changed from: private */
    public KDXFRegister kdxfRegister = new KDXFRegister();
    private ServiceConnection mRegistedConn;
    /* access modifiers changed from: private */
    public ITVTaoRegister mTVTaoRegister;
    private String packageName;
    private Register register;
    private RegisterListener registerListener;

    private LPR() {
    }

    public static LPR getInstance() {
        if (lpr == null) {
            synchronized (LPR.class) {
                if (lpr == null) {
                    lpr = new LPR();
                }
            }
        }
        return lpr;
    }

    public void registed(Context context2) {
        this.context = context2;
        this.register = new Register();
        this.register.className = context2.getClass().getName();
        registerRequest(this.register);
    }

    public void registed(Register register2) {
        if (register2 != null) {
            registerRequest(register2);
        }
    }

    public void unregistered() {
        if (this.register != null) {
            this.register.resgistedType = RegisterType.RELIEVE;
        }
        if (SDKInitConfig.needRegister() && this.register != null) {
            sendRegister(this.register, false);
        }
        if (this.kdxfRegister != null) {
            this.kdxfRegister.release();
        }
        this.register = null;
    }

    public void sendRegister(Register register2, boolean isRegister) {
        LogPrint.i(TAG, TAG + ".sendRegister register : " + register2 + " ,isRegister : " + isRegister);
        if (this.mTVTaoRegister == null) {
            LogPrint.i(TAG, TAG + ".sendRegister  bindService");
            bindService(register2, isRegister);
        } else if (isRegister) {
            try {
                this.mTVTaoRegister.onRegister(register2);
            } catch (RemoteException e) {
                e.printStackTrace();
            }
        } else {
            this.mTVTaoRegister.unRegister(register2);
        }
    }

    public void registerRequest(Register register2) {
        if (this.registerListener == null) {
            this.registerListener = new RegisterListener();
        }
        this.registerListener.setRegister(register2);
        BusinessRequest.getBusinessRequest().requestVoiceRegister(SDKInitConfig.getCurrentPage(), register2.className, register2.resgistedType, register2.getParams(), this.registerListener);
    }

    public void release() {
        this.context = null;
    }

    private void bindService(final Register register2, final boolean isRegister) {
        if (SDKInitConfig.needRegister() && this.mTVTaoRegister == null) {
            LogPrint.i(TAG, TAG + ".bindService mRegistedConn : " + this.mRegistedConn);
            if (this.mRegistedConn == null) {
                this.mRegistedConn = new ServiceConnection() {
                    public void onServiceConnected(ComponentName componentName, IBinder iBinder) {
                        LogPrint.i(LPR.TAG, "onServiceConnected");
                        ITVTaoRegister unused = LPR.this.mTVTaoRegister = ITVTaoRegister.Stub.asInterface(iBinder);
                        if (register2 != null) {
                            try {
                                if (isRegister) {
                                    LPR.this.mTVTaoRegister.onRegister(register2);
                                } else {
                                    LPR.this.mTVTaoRegister.unRegister(register2);
                                }
                            } catch (RemoteException e) {
                                e.printStackTrace();
                            }
                        }
                    }

                    public void onServiceDisconnected(ComponentName componentName) {
                        LogPrint.i(LPR.TAG, "onServiceDisconnected");
                        ITVTaoRegister unused = LPR.this.mTVTaoRegister = null;
                    }
                };
            }
            Intent serviceIntent = new Intent();
            serviceIntent.setPackage(SDKInitConfig.getSdkPackageName());
            serviceIntent.setAction("ACTION.TVTAO.AIDL.LPR");
            Context appContext = CoreApplication.getApplication();
            if (Build.VERSION.SDK_INT >= 26) {
                appContext.startForegroundService(serviceIntent);
            }
            appContext.bindService(serviceIntent, this.mRegistedConn, 1);
        }
    }

    private class RegisterListener implements RequestListener<JSONObject> {
        private Register register;

        private RegisterListener() {
        }

        public void setRegister(Register register2) {
            this.register = register2;
        }

        public void onRequestDone(JSONObject data, int resultCode, String msg) {
            LogPrint.i(LPR.TAG, "RegisterListener data : " + data);
            if (resultCode == 200) {
                try {
                    JSONArray keys = data.getJSONArray("asrs");
                    if (keys != null && keys.length() != 0) {
                        LPR.this.kdxfRegister.onRegister(this.register.className, keys);
                        LogPrint.i(LPR.TAG, "SDKInitConfig.needRegister()===" + SDKInitConfig.needRegister());
                        if (SDKInitConfig.needRegister()) {
                            String[] registedArray = new String[keys.length()];
                            for (int i = 0; i < keys.length(); i++) {
                                registedArray[i] = keys.getString(i);
                            }
                            this.register.registedArray = (String[]) registedArray.clone();
                            LPR.this.sendRegister(this.register, true);
                            LogPrint.i(LPR.TAG, "RegisterListener registedArray : " + Arrays.toString(registedArray));
                        }
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            } else {
                LogPrint.e(LPR.TAG, "RegisterListener resultCode : " + resultCode + " ,msg : " + msg);
            }
        }
    }
}
