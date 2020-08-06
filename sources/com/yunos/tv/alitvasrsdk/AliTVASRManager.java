package com.yunos.tv.alitvasrsdk;

import android.app.Activity;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.os.Looper;
import android.os.RemoteException;
import android.text.TextUtils;
import android.util.Log;
import com.alibaba.analytics.core.Constants;
import com.alibaba.motu.videoplayermonitor.VPMConstants;
import com.yunos.tv.alitvasr.IAliTVASR;
import com.yunos.tv.alitvasr.IAliTVASRCallback;
import com.yunos.tv.alitvasr.IAliTVASRTTSCallback;
import com.yunos.tv.alitvasrsdk.OnASRCommandListener;
import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Map;
import org.json.JSONArray;
import org.json.JSONObject;

public class AliTVASRManager extends IAliTVASRCallback.Stub {
    public static String AI_HOST = "AliGenie";
    public static final int ASR_MODE_DEFAULT = 0;
    public static final int ASR_MODE_INPUT_TEXT = 1;
    public static final int ASR_RESULT_MODE_FLAG_BACKGROUND = 131072;
    public static final int ASR_RESULT_MODE_FLAG_NLU = 65536;
    public static final int ASR_RESULT_MODE_MASK = 65535;
    private static final String ASR_SDK_VERSION = "1.1.0";
    private static final String ASR_STATUS_ERROR = "error";
    private static final String ASR_STATUS_START_RECOGNIZING = "start_recognizing";
    private static final String ASR_STATUS_START_RECORDING = "start_recording";
    private static final String ASR_STATUS_STOP_RECOGNIZING = "stop_recognizing";
    private static final String ASR_STATUS_STOP_RECORDING = "stop_recording";
    private static final String ASR_STATUS_VOLUME_UPDATE = "volume_update";
    private static final int ID_INVALID = -1;
    public static final String KEY_ACTION = "action";
    public static final String KEY_ACTION_PARAM = "action_param";
    public static final String KEY_DM_RESULT = "dm_result";
    private static final long[] RETRY_TIMEOUT = {3000, 8000, 15000};
    private static final String TAG = "AliTVASRManager";
    private static String alispeechPackageName;
    /* access modifiers changed from: private */
    public static volatile int gId;
    private String AI_BuyMember = "/BuyMember";
    private String AI_CancelChaseDrama = "/CancelChaseDrama";
    private String AI_CancelCollect = "/CancelCollect";
    private String AI_ChaseDrama = "/ChaseDrama";
    private String AI_ExitFullScreen = "/ExitFullScreen";
    private String AI_FastBackward = "/FastBackward";
    private String AI_FastForward = "/FastForward";
    private String AI_FullScreen = "/FullScreen";
    private String AI_OpenMenu = "/OpenMenu";
    private String AI_PAUSE = DMAction.AUDIO_PAUSE;
    private String AI_PlayFromStart = "/PlayFromStart";
    private String AI_PlayIndex = "/PlayIndex";
    private String AI_PlayLatest = "/PlayLatest";
    private String AI_SelectPlay = "/SelectPlay";
    private String AI_Skip = DMAction.VIDEO_SKIP;
    private String AI_SkipAd = "/SkipAd";
    private String AI_SkipTo = "/SkipTo";
    private String AI_SwitchDefinition = "/SwitchDefinition";
    private String AI_SwitchScreenRatio = "/SwitchScreenRatio";
    private String asrAction = null;
    private String asrPackage = null;
    private boolean isNeedVerCode = true;
    private WeakReference<OnASRCommandListener> mASRCommandListener;
    private IAliTVASR mAliTVASRService;
    private ServiceConnection mConnection = new ServiceConnection() {
        public void onServiceConnected(ComponentName name, IBinder service) {
            AliTVASRManager.this.setAsrService(IAliTVASR.Stub.asInterface(service));
            Log.d(AliTVASRManager.TAG, "onServiceConnected mAliTVASRService=" + AliTVASRManager.this.getAsrService() + ", name=" + name);
            AliTVASRManager.this.onASRServiceConnected();
        }

        public void onServiceDisconnected(ComponentName name) {
            AliTVASRManager.this.setAsrService((IAliTVASR) null);
            Log.d(AliTVASRManager.TAG, "onServiceDisconnected name=" + name);
            AliTVASRManager.this.onASRServiceDisConnected();
        }
    };
    private WeakReference<Context> mContext;
    private ArrayList<String> mDefinitionList = new ArrayList<>(Arrays.asList(new String[]{"标清", "720", "720p", "1080p", "4k"}));
    private ArrayList<String> mDefinitionNameList = new ArrayList<>(Arrays.asList(new String[]{"4", "3", "12", "13", Constants.LogTransferLevel.L6}));
    private ArrayList<String> mDefinitionNameListText = new ArrayList<>(Arrays.asList(new String[]{"标清", "高清", "720p", "1080p", "4k"}));
    private boolean mEnable = true;
    private Handler mHandler;
    /* access modifiers changed from: private */
    public volatile int mId = -1;
    private OnASRCommandListener.ASRListenerType mListenerType;
    private boolean mNoRegister = false;
    private WeakReference<OnNluResultListener> mOnNluResultListener;
    private int mResultMode;
    private int mRetryIndex = 0;
    private Runnable mRetryRunnable = new Runnable() {
        public void run() {
            boolean invalid = true;
            if (AliTVASRManager.this.isValidActivity()) {
                AliTVASRManager.access$108(AliTVASRManager.this);
                IAliTVASR unused = AliTVASRManager.this.getIAliTVASR(true);
            } else if (AliTVASRManager.this.isValid()) {
                Log.e(AliTVASRManager.TAG, "release retry: mId = " + AliTVASRManager.this.mId + ", gId = " + AliTVASRManager.gId);
                AliTVASRManager.this.release();
            } else {
                synchronized (AliTVASRManager.class) {
                    if (AliTVASRManager.gId != -1) {
                        invalid = false;
                    }
                }
                if (invalid) {
                    AliTVASRManager.this.unbind();
                    Log.e(AliTVASRManager.TAG, "unbind retry: mId = " + AliTVASRManager.this.mId + ", gId = " + AliTVASRManager.gId);
                    return;
                }
                Log.e(AliTVASRManager.TAG, "not retry: mId = " + AliTVASRManager.this.mId + ", gId = " + AliTVASRManager.gId);
            }
        }
    };
    private int mServerMode;
    private boolean mShowUI = true;

    static /* synthetic */ int access$108(AliTVASRManager x0) {
        int i = x0.mRetryIndex;
        x0.mRetryIndex = i + 1;
        return i;
    }

    public void setAsrServiceIntent(String actionStr, String packageStr) {
        this.asrAction = actionStr;
        this.asrPackage = packageStr;
        if (!TextUtils.isEmpty(this.asrPackage) && !TextUtils.isEmpty(this.asrAction)) {
            this.isNeedVerCode = false;
        }
    }

    private void setId(boolean reset) {
        synchronized (AliTVASRManager.class) {
            if (reset) {
                if (gId == this.mId) {
                    gId = -1;
                }
                this.mId = -1;
            } else {
                gId++;
                if (gId < 0) {
                    gId = 0;
                }
                this.mId = gId;
            }
        }
    }

    /* access modifiers changed from: private */
    public boolean isValid() {
        boolean z;
        synchronized (AliTVASRManager.class) {
            z = this.mId == gId && this.mId != -1;
        }
        return z;
    }

    /* access modifiers changed from: private */
    public boolean isValidActivity() {
        Context context = getContext();
        if (context == null || this.mId != gId || this.mId == -1) {
            return false;
        }
        if (context instanceof Activity) {
            Activity activity = (Activity) context;
            if (activity.isFinishing()) {
                return false;
            }
            if (Build.VERSION.SDK_INT < 17 || !activity.isDestroyed()) {
                return true;
            }
            return false;
        }
        return true;
    }

    private boolean checkValid() {
        if (isValidActivity()) {
            return true;
        }
        Log.e(TAG, "client error, need release");
        this.mRetryIndex = 0;
        retryBindService();
        return false;
    }

    private OnASRCommandListener getOnASRCommandListener() {
        WeakReference<OnASRCommandListener> weakRefListener = this.mASRCommandListener;
        if (weakRefListener != null) {
            return (OnASRCommandListener) weakRefListener.get();
        }
        return null;
    }

    private OnNluResultListener getOnNluResultListener() {
        WeakReference<OnNluResultListener> weakRefListener = this.mOnNluResultListener;
        if (weakRefListener != null) {
            return (OnNluResultListener) weakRefListener.get();
        }
        return null;
    }

    public void setOnNluResultListener(OnNluResultListener listener) {
        this.mOnNluResultListener = new WeakReference<>(listener);
    }

    private Context getContext() {
        WeakReference<Context> weakReference = this.mContext;
        if (weakReference != null) {
            return (Context) weakReference.get();
        }
        return null;
    }

    public void init(Context context, boolean showUI) {
        if (context != null) {
            this.mContext = new WeakReference<>(context);
        }
        Log.e(TAG, "init showUi= " + showUI + "self = " + this);
        this.mShowUI = showUI;
        setId(false);
        getIAliTVASR(true);
    }

    public AliTVASRManager setASRListenerType(OnASRCommandListener.ASRListenerType listenerType) {
        this.mListenerType = listenerType;
        return this;
    }

    public AliTVASRManager setASRResultMode(int mode) {
        this.mResultMode = mode;
        Context context = getContext();
        IAliTVASR asrService = getIAliTVASR();
        if (!(asrService == null || context == null)) {
            try {
                asrService.setResultMode(context.getPackageName(), mode);
            } catch (Throwable e) {
                e.printStackTrace();
            }
        }
        return this;
    }

    public AliTVASRManager setASRServerMode(int mode) {
        this.mServerMode = mode;
        Context context = getContext();
        IAliTVASR asrService = getIAliTVASR();
        if (!(asrService == null || context == null)) {
            try {
                asrService.setASRMode(context.getPackageName(), mode);
            } catch (Throwable e) {
                e.printStackTrace();
            }
        }
        return this;
    }

    public AliTVASRManager showASRUI(boolean showUI) {
        this.mShowUI = showUI;
        Context context = getContext();
        IAliTVASR asrService = getIAliTVASR();
        if (!(asrService == null || context == null)) {
            try {
                asrService.showASRUI(context.getPackageName(), showUI);
            } catch (RemoteException e) {
                e.printStackTrace();
            }
        }
        return this;
    }

    /* access modifiers changed from: private */
    public IAliTVASR getAsrService() {
        IAliTVASR iAliTVASR;
        synchronized (AliTVASRManager.class) {
            iAliTVASR = this.mAliTVASRService;
        }
        return iAliTVASR;
    }

    /* access modifiers changed from: private */
    public void setAsrService(IAliTVASR service) {
        synchronized (AliTVASRManager.class) {
            if (service != this.mAliTVASRService) {
                Context context = getContext();
                if (!(this.mAliTVASRService == null || context == null)) {
                    try {
                        context.unbindService(this.mConnection);
                    } catch (Throwable e) {
                        e.printStackTrace();
                    }
                }
                this.mAliTVASRService = service;
            }
        }
    }

    /* access modifiers changed from: private */
    public void unbind() {
        IAliTVASR asrService = getIAliTVASR(false);
        Context context = getContext();
        if (!(context == null || asrService == null)) {
            try {
                asrService.unregisterCallback(context.getPackageName());
            } catch (Throwable e) {
                e.printStackTrace();
            }
        }
        setAsrService((IAliTVASR) null);
    }

    public void release() {
        Log.e(TAG, "release self " + this);
        setId(true);
        unbind();
        Handler handler = getHandler(false);
        if (handler != null) {
            handler.removeCallbacksAndMessages((Object) null);
        }
        onASRServiceDisConnected();
    }

    public AliTVASRManager setOnASRCommandListener(OnASRCommandListener listener) {
        this.mASRCommandListener = null;
        if (listener != null) {
            this.mASRCommandListener = new WeakReference<>(listener);
        }
        return this;
    }

    public AliTVASRManager setAliTVASREnable(boolean enable) throws RemoteException {
        this.mEnable = enable;
        Context context = getContext();
        IAliTVASR asrService = getIAliTVASR();
        if (!(asrService == null || context == null)) {
            try {
                asrService.setAliTVASREnable(context.getPackageName(), enable);
            } catch (Throwable e) {
                e.printStackTrace();
            }
        }
        return this;
    }

    public boolean playTTS(String tts) throws RemoteException {
        IAliTVASR asrService = getIAliTVASR();
        if (asrService != null) {
            try {
                return asrService.playTTS(tts);
            } catch (Throwable e) {
                e.printStackTrace();
            }
        }
        return false;
    }

    public boolean playTTS(String tts, final OnTtsListener listener) throws RemoteException {
        IAliTVASR asrService = getIAliTVASR();
        if (asrService == null) {
            return false;
        }
        try {
            return asrService.playTTSWithCallback(tts, new IAliTVASRTTSCallback.Stub() {
                public void onTtsStart(String tts) throws RemoteException {
                    if (listener != null) {
                        listener.onTtsStart(tts);
                    }
                }

                public void onTtsStop() throws RemoteException {
                    if (listener != null) {
                        listener.onTtsStop();
                    }
                }

                public void onException(String errorMsg) throws RemoteException {
                    if (listener != null) {
                        listener.onException(errorMsg);
                    }
                }
            });
        } catch (Throwable e) {
            e.printStackTrace();
            return false;
        }
    }

    public void stopTTS() throws RemoteException {
        IAliTVASR asrService = getIAliTVASR();
        if (asrService != null) {
            try {
                asrService.stopTTS();
            } catch (Throwable e) {
                e.printStackTrace();
            }
        }
    }

    private Handler getHandler(boolean isAutoCreate) {
        if (this.mHandler == null && isAutoCreate) {
            synchronized (AliTVASRManager.class) {
                if (this.mHandler == null) {
                    this.mHandler = new Handler(Looper.getMainLooper());
                }
            }
        }
        return this.mHandler;
    }

    private void retryBindService() {
        Handler handler;
        int index = this.mRetryIndex;
        if (index < RETRY_TIMEOUT.length && (handler = getHandler(true)) != null) {
            handler.removeCallbacksAndMessages((Object) null);
            handler.postDelayed(this.mRetryRunnable, RETRY_TIMEOUT[index]);
        }
        Log.e(TAG, "retryBindService: retry bind service: " + index);
    }

    private Intent get3rdAsrServiceIntent() {
        if (TextUtils.isEmpty(this.asrAction) || TextUtils.isEmpty(this.asrPackage)) {
            if (alispeechPackageName == null) {
                alispeechPackageName = getSystemProperties("persist.sys.alispeech.name", "");
            }
            if (TextUtils.isEmpty(alispeechPackageName) || "com.yunos.tv.alitvasr".equals(alispeechPackageName)) {
                return null;
            }
            Intent intent = new Intent();
            intent.setAction(CommonData.ASR_SERVER_ACTION);
            intent.setPackage(alispeechPackageName);
            return intent;
        }
        Intent intent2 = new Intent();
        intent2.setAction(this.asrAction);
        intent2.setPackage(this.asrPackage);
        return intent2;
    }

    public static String getSystemProperties(String key, String def) {
        try {
            return (String) Class.forName("android.os.SystemProperties").getMethod("get", new Class[]{String.class, String.class}).invoke((Object) null, new Object[]{key, def});
        } catch (Exception e) {
            e.printStackTrace();
            return def;
        }
    }

    /* access modifiers changed from: private */
    /* JADX WARNING: Removed duplicated region for block: B:27:0x00be  */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public com.yunos.tv.alitvasr.IAliTVASR getIAliTVASR(boolean r12) {
        /*
            r11 = this;
            r7 = 0
            com.yunos.tv.alitvasr.IAliTVASR r0 = r11.getAsrService()
            if (r0 == 0) goto L_0x0011
            android.os.IBinder r8 = r0.asBinder()
            boolean r8 = r8.isBinderAlive()
            if (r8 != 0) goto L_0x0029
        L_0x0011:
            if (r12 == 0) goto L_0x00c1
            boolean r8 = r11.isValid()
            if (r8 == 0) goto L_0x00c1
            android.content.Context r4 = r11.getContext()
            if (r4 != 0) goto L_0x002a
            java.lang.String r8 = "AliTVASRManager"
            java.lang.String r9 = "getIAliTVASR: context is null"
            android.util.Log.e(r8, r9)
            r0 = r7
        L_0x0029:
            return r0
        L_0x002a:
            r6 = 0
            android.content.Intent r1 = r11.get3rdAsrServiceIntent()
            int r3 = r11.getAppVersionCode(r4)
            r8 = 2100300000(0x7d3008e0, float:1.4624388E37)
            if (r3 >= r8) goto L_0x003a
            if (r1 == 0) goto L_0x00ca
        L_0x003a:
            if (r1 != 0) goto L_0x004e
            android.content.Intent r2 = new android.content.Intent     // Catch:{ Throwable -> 0x00c4 }
            r2.<init>()     // Catch:{ Throwable -> 0x00c4 }
            java.lang.String r8 = "com.yunos.tv.alitvasr.service"
            r2.setAction(r8)     // Catch:{ Throwable -> 0x00d0 }
            java.lang.String r8 = "com.yunos.tv.alitvasr"
            r2.setPackage(r8)     // Catch:{ Throwable -> 0x00d0 }
            r1 = r2
        L_0x004e:
            java.lang.String r8 = "AliTVASRManager"
            java.lang.StringBuilder r9 = new java.lang.StringBuilder     // Catch:{ Throwable -> 0x00c4 }
            r9.<init>()     // Catch:{ Throwable -> 0x00c4 }
            java.lang.String r10 = "bindService ASRService, begin: code = "
            java.lang.StringBuilder r9 = r9.append(r10)     // Catch:{ Throwable -> 0x00c4 }
            java.lang.StringBuilder r9 = r9.append(r3)     // Catch:{ Throwable -> 0x00c4 }
            java.lang.String r10 = ", context = "
            java.lang.StringBuilder r9 = r9.append(r10)     // Catch:{ Throwable -> 0x00c4 }
            java.lang.StringBuilder r9 = r9.append(r4)     // Catch:{ Throwable -> 0x00c4 }
            java.lang.String r10 = ",asrAction="
            java.lang.StringBuilder r9 = r9.append(r10)     // Catch:{ Throwable -> 0x00c4 }
            java.lang.String r10 = r11.asrAction     // Catch:{ Throwable -> 0x00c4 }
            java.lang.StringBuilder r9 = r9.append(r10)     // Catch:{ Throwable -> 0x00c4 }
            java.lang.String r10 = ",asrPackage="
            java.lang.StringBuilder r9 = r9.append(r10)     // Catch:{ Throwable -> 0x00c4 }
            java.lang.String r10 = r11.asrPackage     // Catch:{ Throwable -> 0x00c4 }
            java.lang.StringBuilder r9 = r9.append(r10)     // Catch:{ Throwable -> 0x00c4 }
            java.lang.String r9 = r9.toString()     // Catch:{ Throwable -> 0x00c4 }
            android.util.Log.e(r8, r9)     // Catch:{ Throwable -> 0x00c4 }
            android.content.ServiceConnection r8 = r11.mConnection     // Catch:{ Throwable -> 0x00c4 }
            r9 = 1
            boolean r8 = r4.bindService(r1, r8, r9)     // Catch:{ Throwable -> 0x00c4 }
            if (r8 != 0) goto L_0x0097
            r6 = 1
        L_0x0097:
            java.lang.String r8 = "AliTVASRManager"
            java.lang.StringBuilder r9 = new java.lang.StringBuilder     // Catch:{ Throwable -> 0x00c4 }
            r9.<init>()     // Catch:{ Throwable -> 0x00c4 }
            java.lang.String r10 = "bindService ASRService, end: code = "
            java.lang.StringBuilder r9 = r9.append(r10)     // Catch:{ Throwable -> 0x00c4 }
            java.lang.StringBuilder r9 = r9.append(r3)     // Catch:{ Throwable -> 0x00c4 }
            java.lang.String r10 = ", context = "
            java.lang.StringBuilder r9 = r9.append(r10)     // Catch:{ Throwable -> 0x00c4 }
            java.lang.StringBuilder r9 = r9.append(r4)     // Catch:{ Throwable -> 0x00c4 }
            java.lang.String r9 = r9.toString()     // Catch:{ Throwable -> 0x00c4 }
            android.util.Log.e(r8, r9)     // Catch:{ Throwable -> 0x00c4 }
        L_0x00bc:
            if (r6 == 0) goto L_0x00c1
            r11.retryBindService()
        L_0x00c1:
            r0 = r7
            goto L_0x0029
        L_0x00c4:
            r5 = move-exception
        L_0x00c5:
            r5.printStackTrace()
            r6 = 1
            goto L_0x00bc
        L_0x00ca:
            if (r3 <= 0) goto L_0x00ce
            r6 = 0
            goto L_0x00bc
        L_0x00ce:
            r6 = 1
            goto L_0x00bc
        L_0x00d0:
            r5 = move-exception
            r1 = r2
            goto L_0x00c5
        */
        throw new UnsupportedOperationException("Method not decompiled: com.yunos.tv.alitvasrsdk.AliTVASRManager.getIAliTVASR(boolean):com.yunos.tv.alitvasr.IAliTVASR");
    }

    private IAliTVASR getIAliTVASR() {
        return getIAliTVASR(false);
    }

    private void bindService() {
        Context context = getContext();
        if (context == null) {
            Log.e(TAG, "bindService: context is null");
            return;
        }
        int code = getAppVersionCode(context);
        if (code < 2100300000 && this.isNeedVerCode) {
            Log.e(TAG, "bindService: asr version low, code =" + code + ",isNeedVerCode==" + this.isNeedVerCode);
        } else if (getIAliTVASR() == null) {
            Intent asrServiceIntent = new Intent();
            asrServiceIntent.setAction(CommonData.ASR_SERVER_ACTION);
            asrServiceIntent.setPackage(CommonData.ASR_UI_PACKAGE_NAME);
            Log.d(TAG, "bindService ASRService, begin: " + context);
            context.bindService(asrServiceIntent, this.mConnection, 1);
            Log.d(TAG, "bindService ASRService end: " + context);
        }
    }

    /* access modifiers changed from: private */
    public void onASRServiceConnected() {
        if (!isValid()) {
            Log.e(TAG, "onASRServiceConnected: has release");
            return;
        }
        IAliTVASR asrService = getAsrService();
        WeakReference<Context> weakReference = this.mContext;
        Context context = weakReference != null ? (Context) weakReference.get() : null;
        if (!(asrService == null || context == null || this.mNoRegister)) {
            try {
                String packageName = context.getApplicationInfo().packageName;
                asrService.registerCallback(packageName, this, this.mShowUI);
                asrService.setASRMode(packageName, this.mServerMode);
                asrService.setResultMode(packageName, this.mResultMode);
                asrService.setAliTVASREnable(packageName, this.mEnable);
                this.mRetryIndex = 0;
                Log.e(TAG, "Current alitvasrsdk in " + packageName + " version is: " + ASR_SDK_VERSION + ", mShowUI = " + this.mShowUI + " ,mServerMode =  " + this.mServerMode + ", mResultMode = " + this.mResultMode + ", mEnable = " + this.mEnable);
            } catch (Throwable e) {
                e.printStackTrace();
            }
        }
        OnASRCommandListener listener = getOnASRCommandListener();
        if (listener != null) {
            listener.onASRServiceStatusUpdated(OnASRCommandListener.ASRServiceStatus.ASR_SERVICE_STATUS_CONNECTED);
        }
    }

    /* access modifiers changed from: private */
    public void onASRServiceDisConnected() {
        if (isValid()) {
            retryBindService();
            OnASRCommandListener listener = getOnASRCommandListener();
            if (listener != null) {
                listener.onASRServiceStatusUpdated(OnASRCommandListener.ASRServiceStatus.ASR_SERVICE_STATUS_DISCONNECTED);
            }
        }
    }

    public void onASRStatusUpdate(Bundle asrStatusInfo) throws RemoteException {
        OnASRCommandListener listener = getOnASRCommandListener();
        if (listener == null) {
            Log.d(TAG, "no onASRStatusUpdate listener");
            return;
        }
        OnASRCommandListener.ASRStatus status = OnASRCommandListener.ASRStatus.ASR_STATUS_NULL;
        if (asrStatusInfo != null) {
            String statusString = asrStatusInfo.getString("status");
            if (!TextUtils.isEmpty(statusString)) {
                if (ASR_STATUS_START_RECORDING.equalsIgnoreCase(statusString)) {
                    status = OnASRCommandListener.ASRStatus.ASR_STATUS_RECORD_START;
                } else if (ASR_STATUS_STOP_RECORDING.equalsIgnoreCase(statusString)) {
                    status = OnASRCommandListener.ASRStatus.ASR_STATUS_RECORD_END;
                } else if (ASR_STATUS_START_RECOGNIZING.equalsIgnoreCase(statusString)) {
                    status = OnASRCommandListener.ASRStatus.ASR_STATUS_RECOGNIZE_START;
                } else if (ASR_STATUS_STOP_RECOGNIZING.equalsIgnoreCase(statusString)) {
                    status = OnASRCommandListener.ASRStatus.ASR_STATUS_RECOGNIZE_END;
                } else if (ASR_STATUS_VOLUME_UPDATE.equalsIgnoreCase(statusString)) {
                    status = OnASRCommandListener.ASRStatus.ASR_STATUS_VOLUME_UPDATE;
                } else if (ASR_STATUS_ERROR.equalsIgnoreCase(statusString)) {
                    status = OnASRCommandListener.ASRStatus.ASR_STATUS_ERROR;
                }
            }
        }
        Log.d(TAG, "onASRStatusUpdate status = " + status);
        listener.onASRStatusUpdated(status, asrStatusInfo);
    }

    public Map<String, String> handleASRCommand(Bundle asrCommandBundle) throws RemoteException {
        boolean z = true;
        ASRCommandReturn result = new ASRCommandReturn();
        if (!checkValid()) {
            return result.getReturn();
        }
        OnASRCommandListener listener = getOnASRCommandListener();
        if (!(listener == null || asrCommandBundle == null)) {
            if (this.mResultMode == 1) {
                String string = asrCommandBundle.getString(CommonData.TYPE_ASR);
                if ("0".equals(asrCommandBundle.getString(CommonData.KEY_FINISHED))) {
                    z = false;
                }
                ASRCommandReturn asrResult = listener.onASRResult(string, z);
                if (asrResult != null) {
                    result = asrResult;
                }
            } else {
                ASRCommandReturn nlpResult = onNLPResult(asrCommandBundle);
                if (nlpResult != null) {
                    result = nlpResult;
                }
            }
        }
        return result.getReturn();
    }

    public String getAppContextData(Bundle asrContextBundle) throws RemoteException {
        OnASRCommandListener listener = getOnASRCommandListener();
        if (listener != null) {
            try {
                AppContextData result = new AppContextData();
                listener.getAppContextData(result);
                return AppContextData.toString(result);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return null;
    }

    public Bundle getSceneInfo(Bundle sceneBundle) throws RemoteException {
        OnASRCommandListener listener = getOnASRCommandListener();
        if (listener != null) {
            try {
                return listener.getSceneInfo(sceneBundle);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return null;
    }

    public Bundle asrToClient(int id, Bundle bundle) throws RemoteException {
        String data;
        Bundle result = null;
        switch (id) {
            case 10001:
                result = CommonUtils.isForegroundBundle(getContext());
                break;
            case 10002:
                break;
            default:
                OnASRCommandListener listener = getOnASRCommandListener();
                if (listener == null) {
                    return null;
                }
                try {
                    return listener.asrToClient(bundle);
                } catch (Exception e) {
                    e.printStackTrace();
                    return null;
                }
        }
        OnNluResultListener listener2 = getOnNluResultListener();
        if (listener2 == null || bundle == null || (data = listener2.onNluResult(bundle.getString("data"))) == null) {
            return result;
        }
        Bundle result2 = new Bundle();
        result2.putString("data", data);
        return result2;
    }

    public ASRCommandReturn onNLPResult(Bundle nlpResultBundle) {
        ASRCommandReturn result = new ASRCommandReturn();
        String nlpResult = nlpResultBundle.getString("nlp");
        if (!TextUtils.isEmpty(nlpResult)) {
            try {
                JSONObject dmResultJSON = JSONUtils.getJSONObject(new JSONObject(nlpResult), KEY_DM_RESULT);
                if (dmResultJSON != null) {
                    String actionString = JSONUtils.getJSONString(dmResultJSON, "action");
                    if (!TextUtils.isEmpty(actionString)) {
                        Uri actionUri = Uri.parse(actionString);
                        if (DMAction.ACTION.equalsIgnoreCase(actionUri.getScheme()) && !TextUtils.isEmpty(actionUri.getHost())) {
                            return processAction(actionUri, dmResultJSON, nlpResultBundle);
                        }
                    }
                }
            } catch (Throwable e) {
                e.printStackTrace();
            }
        }
        OnASRCommandListener listener = getOnASRCommandListener();
        if (listener != null) {
            result = listener.onNLUResult("", "", "", nlpResultBundle);
        }
        return result;
    }

    private ASRCommandReturn processAction(Uri actionUri, JSONObject dmResult, Bundle nlpBundle) {
        ASRCommandReturn result = new ASRCommandReturn();
        OnASRCommandListener listener = getOnASRCommandListener();
        if (actionUri != null && actionUri.getHost() != null && !TextUtils.isEmpty(actionUri.getHost()) && actionUri.getHost().contains(AI_HOST)) {
            Log.d(TAG, "AI_HOST has");
            setASRListenerType(OnASRCommandListener.ASRListenerType.DEFAULT_LISTENER);
        }
        OnASRCommandListener.ASRListenerType type = this.mListenerType;
        if (actionUri == null || listener == null || type == null) {
            return result;
        }
        String host = actionUri.getHost();
        String path = actionUri.getPath();
        if (host != null && !TextUtils.isEmpty(host) && host.contains(AI_HOST)) {
            host = getAiHost(path);
            path = getAiPath(path);
            dmResult = switchASRJSON(dmResult, host, path);
            if (this.AI_SwitchScreenRatio.equals(path) && (dmResult == null || (dmResult != null && !dmResult.has("ratio")))) {
                Log.d(TAG, "AI_HOST has open menu");
                path = DMAction.MULTIMEDIA_SETUP;
            }
        }
        switch (type) {
            case MOVIE_LISTENER:
            case MUSIC_LISTENER:
                if (!DMAction.MULTIMEDIA.equals(host) && !DMAction.MUSIC.equals(host) && !DMAction.AUDIO.equals(host) && !DMAction.VIDEO.equals(host) && !isMediaHost(host)) {
                    return result;
                }
                if (TextUtils.isEmpty(path)) {
                    path = host;
                }
                return listener.onNLUResult(host, path, JSONUtils.getJSONString(dmResult, KEY_ACTION_PARAM), nlpBundle);
            case TVSHOW_LISTENER:
                if (!DMAction.SIGNAL.equals(host) && (!DMAction.MULTIMEDIA.equals(host) || !DMAction.MULTIMEDIA_TVCHANNEL_SWITCH.equals(path))) {
                    return result;
                }
                if (TextUtils.isEmpty(path)) {
                    path = host;
                }
                return listener.onNLUResult(host, path, JSONUtils.getJSONString(dmResult, KEY_ACTION_PARAM), nlpBundle);
            case ELECTRICAL_LISTENER:
                if (!DMAction.AIR_CONDITIONER.equals(host) && !DMAction.AIR_CUBE.equals(host) && !DMAction.AIR_CLEANER.equals(host) && !DMAction.WATER_HEATER.equals(host)) {
                    return result;
                }
                if (TextUtils.isEmpty(path)) {
                    path = host;
                }
                return listener.onNLUResult(host, path, JSONUtils.getJSONString(dmResult, KEY_ACTION_PARAM), nlpBundle);
            default:
                if (TextUtils.isEmpty(path)) {
                    path = host;
                }
                return listener.onNLUResult(host, path, JSONUtils.getJSONString(dmResult, KEY_ACTION_PARAM), nlpBundle);
        }
    }

    private boolean isMediaHost(String host) {
        if (TextUtils.isEmpty(host)) {
            return false;
        }
        if (DMAction.PAUSE.equals(host) || DMAction.STOP.equals(host) || DMAction.SCREEN.equals(host) || DMAction.PLAY.equals(host) || DMAction.SELECT.equals(host) || DMAction.CONTINUE.equals(host)) {
            return true;
        }
        return false;
    }

    public int getAppVersionCode(Context context) {
        try {
            return context.getPackageManager().getPackageInfo("com.yunos.tv.alitvasr", 0).versionCode;
        } catch (Exception e) {
            e.printStackTrace();
            return -1;
        }
    }

    public Bundle updateAppScene(Bundle bundle) {
        IAliTVASR asrService = getIAliTVASR();
        if (asrService != null) {
            try {
                return asrService.updateAppScene(bundle);
            } catch (RemoteException e) {
                e.printStackTrace();
            }
        }
        return null;
    }

    public void setNoRegister(boolean noRegister) {
        this.mNoRegister = noRegister;
    }

    public Bundle onFarDeviceData(Bundle data) {
        IAliTVASR service = getIAliTVASR(true);
        if (service != null) {
            try {
                return service.onFarDeviceData(data);
            } catch (Throwable var4) {
                var4.printStackTrace();
            }
        }
        return null;
    }

    private String getAiHost(String path) {
        String result = path;
        if (result.equals(this.AI_PAUSE)) {
            result = DMAction.PAUSE;
        } else if (result.equals(this.AI_PlayLatest) || result.equals(this.AI_BuyMember)) {
            result = DMAction.MULTIMEDIA;
        } else if (result.equals(this.AI_SkipAd) || result.equals(this.AI_Skip)) {
            result = DMAction.VIDEO;
        } else if (result.equals(this.AI_PlayFromStart) || result.equals("/Previous") || result.equals("/Next")) {
            result = DMAction.PLAY;
        }
        Log.d(TAG, "getAiHost=result=" + result + ",path==" + path);
        return result;
    }

    private String getAiPath(String path) {
        String result = path;
        if (result.equals(this.AI_FullScreen)) {
            result = DMAction.MULTIMEDIA_FULLSCREEN_ENTER;
        } else if (result.equals(this.AI_ExitFullScreen)) {
            result = DMAction.MULTIMEDIA_FULLSCREEN_EXIT;
        } else if (result.equals(this.AI_ChaseDrama)) {
            result = DMAction.MULTIMEDIA_FOLLOW;
        } else if (result.equals(this.AI_CancelChaseDrama)) {
            result = DMAction.MULTIMEDIA_FOLLOW_CANCEL;
        } else if (result.equals(this.AI_SwitchDefinition)) {
            result = DMAction.MULTIMEDIA_SETUP_DEFINITION;
        } else if (result.equals(this.AI_OpenMenu)) {
            result = DMAction.MULTIMEDIA_SETUP;
        } else if (result.equals(this.AI_PlayIndex)) {
            result = "/Select";
        } else if (result.equals(this.AI_PlayLatest)) {
            result = DMAction.MULTIMEDIA_LATEST;
        } else if (result.equals(this.AI_SkipAd)) {
            result = DMAction.VIDEO_SKIP;
        } else if (result.equals(this.AI_BuyMember)) {
            result = DMAction.MULTIMEDIA_PURCHASE;
        } else if (result.equals(this.AI_CancelCollect)) {
            result = "/Collect/Cancel";
        } else if (result.equals(this.AI_FastForward) || result.equals(this.AI_SkipTo)) {
            result = DMAction.MULTIMEDIA_FASTFORWARD;
        } else if (result.equals(this.AI_FastBackward)) {
            result = DMAction.MULTIMEDIA_FASTBACKWARD;
        } else if (result.equals(this.AI_PlayFromStart)) {
            result = DMAction.PLAY_AGAIN;
        } else if (result.equals(this.AI_SelectPlay)) {
            result = DMAction.SELECT;
        }
        Log.d(TAG, "getAiPath=result=" + result + ",path==" + path);
        return result;
    }

    private JSONObject switchASRJSON(JSONObject paramsObj, String host, String path) {
        JSONObject jsonObject = new JSONObject();
        try {
            JSONObject result = new JSONObject();
            JSONArray array = new JSONArray();
            JSONObject object = new JSONObject();
            if (paramsObj == null) {
                Log.e(TAG, "==paramsObj null:");
                return jsonObject;
            }
            JSONObject paramsJson = paramsObj.optJSONObject(KEY_ACTION_PARAM);
            if (paramsJson == null) {
                Log.e(TAG, "==paramsJson null:");
                return jsonObject;
            }
            if (DMAction.MULTIMEDIA_SETUP_DEFINITION.equals(path)) {
                String name = paramsJson.optString("resolution");
                int index = this.mDefinitionNameList.indexOf(name);
                if (index >= 0) {
                    object.put("norm", this.mDefinitionList.get(index));
                    object.put("raw", this.mDefinitionNameListText.get(index));
                    array.put(object);
                    result.put("definition", array);
                    jsonObject.put(KEY_ACTION_PARAM, result);
                }
                Log.d(TAG, name + "==switchDefinition:" + jsonObject.toString() + ",index==" + index);
            } else if ("/Select".equals(path)) {
                String name2 = paramsJson.optString("index");
                object.put("norm", StrToInt(name2, 1));
                object.put("raw", name2);
                array.put(object);
                result.put("index", array);
                jsonObject.put(KEY_ACTION_PARAM, result);
                Log.d(TAG, name2 + "==switchSelect:" + jsonObject.toString());
            } else if (DMAction.VIDEO_SKIP.equals(path)) {
                array.put("ad");
                result.put("command_param", array);
                jsonObject.put(KEY_ACTION_PARAM, result);
                Log.d(TAG, "16842755==switchSkipad:" + jsonObject.toString());
            } else if (DMAction.MULTIMEDIA_PURCHASE.equals(path)) {
                array.put("vip");
                result.put("command_param", array);
                jsonObject.put(KEY_ACTION_PARAM, result);
                Log.d(TAG, "16842755==switchBuyMember:" + jsonObject.toString());
            } else if (DMAction.MULTIMEDIA_FASTBACKWARD.equals(path) || DMAction.MULTIMEDIA_FASTFORWARD.equals(path)) {
                if (this.AI_SkipTo.equals(host)) {
                    object.put("norm", StrToInt(paramsJson.optString("modified_time"), 10));
                    object.put("relative_mode", "0");
                } else {
                    object.put("norm", StrToInt(paramsJson.optString("time"), 10));
                    object.put("relative_mode", "1");
                }
                array.put(object);
                result.put(VPMConstants.MEASURE_DURATION, array);
                jsonObject.put(KEY_ACTION_PARAM, result);
                Log.d(TAG, "16842755==switchFastback:" + jsonObject.toString());
            } else {
                jsonObject = paramsObj;
            }
            return jsonObject;
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private int StrToInt(String str, int defaultInt) {
        int result = defaultInt;
        try {
            int old = Integer.valueOf(str).intValue();
            if (old > 0) {
                return old;
            }
            return result;
        } catch (Exception e) {
            e.printStackTrace();
            return result;
        }
    }
}
