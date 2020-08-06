package com.taobao.orange;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.IBinder;
import android.text.TextUtils;
import com.ali.auth.third.offline.login.LoginConstants;
import com.taobao.orange.aidl.IOrangeApiService;
import com.taobao.orange.aidl.OrangeApiServiceStub;
import com.taobao.orange.aidl.OrangeConfigListenerStub;
import com.taobao.orange.service.OrangeApiService;
import com.taobao.orange.util.AndroidUtil;
import com.taobao.orange.util.OLog;
import com.taobao.orange.util.OrangeUtils;
import com.uc.webview.export.internal.interfaces.IWaStat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicBoolean;

public class OrangeConfigImpl extends OrangeConfig {
    static final String TAG = "OrangeConfigImpl";
    static OrangeConfigImpl mInstance = new OrangeConfigImpl();
    volatile CountDownLatch mBindServiceLock;
    private ServiceConnection mConnection = new ServiceConnection() {
        public void onServiceDisconnected(ComponentName name) {
            OLog.w(OrangeConfigImpl.TAG, "onServiceDisconnected", new Object[0]);
            OrangeConfigImpl.this.mRemoteService = null;
            OrangeConfigImpl.this.mIsBindingService.set(false);
            if (OrangeConfigImpl.this.mBindServiceLock != null) {
                OrangeConfigImpl.this.mBindServiceLock.countDown();
            }
        }

        public void onServiceConnected(ComponentName name, IBinder service) {
            OLog.i(OrangeConfigImpl.TAG, "onServiceConnected", new Object[0]);
            OrangeConfigImpl.this.mRemoteService = IOrangeApiService.Stub.asInterface(service);
            OrangeConfigImpl.this.mIsBindingService.set(false);
            if (OrangeConfigImpl.this.mBindServiceLock != null) {
                OrangeConfigImpl.this.mBindServiceLock.countDown();
            }
        }
    };
    volatile Context mContext;
    final List<OCandidate> mFailCandidates = Collections.synchronizedList(new ArrayList());
    final Map<String, Set<OrangeConfigListenerStub>> mFailListeners = new ConcurrentHashMap();
    final Set<String> mFailNamespaces = Collections.synchronizedSet(new HashSet());
    volatile String mFailUserId = null;
    AtomicBoolean mIsBindingService = new AtomicBoolean(false);
    private volatile boolean mIsMainProcess = true;
    volatile IOrangeApiService mRemoteService;

    OrangeConfigImpl() {
    }

    public void init(final Context ctx, final OConfig config) {
        boolean isDebug;
        if (ctx == null) {
            OLog.e(TAG, "init error as ctx is null", new Object[0]);
            return;
        }
        this.mIsMainProcess = AndroidUtil.isMainProcess(ctx);
        if ((ctx.getApplicationInfo().flags & 2) != 0) {
            isDebug = true;
        } else {
            isDebug = false;
        }
        if (isDebug) {
            OLog.setUseTlog(false);
        } else {
            OLog.setUseTlog(true);
        }
        OLog.i(TAG, "init", "isDebug", Boolean.valueOf(isDebug), "isMainProcess", Boolean.valueOf(this.mIsMainProcess), LoginConstants.CONFIG, config);
        if (TextUtils.isEmpty(config.appKey) || TextUtils.isEmpty(config.appVersion)) {
            OLog.e(TAG, "init error as appKey or appVersion is empty", new Object[0]);
            return;
        }
        if (this.mContext == null) {
            this.mContext = ctx.getApplicationContext();
        }
        OThreadFactory.execute(new Runnable() {
            public void run() {
                OrangeConfigImpl.this.asyncGetRemoteService(ctx, true);
                if (OrangeConfigImpl.this.mRemoteService != null) {
                    try {
                        OrangeConfigImpl.this.sendFailItems();
                        OrangeConfigImpl.this.mRemoteService.init(config);
                    } catch (Throwable t) {
                        OLog.e(OrangeConfigImpl.TAG, "asyncInit", t, new Object[0]);
                    }
                }
            }
        });
    }

    /* access modifiers changed from: package-private */
    public void sendFailItems() {
        if (this.mRemoteService != null) {
            try {
                OLog.i(TAG, "sendFailItems start", new Object[0]);
                long startNode = System.currentTimeMillis();
                if (this.mFailUserId != null) {
                    this.mRemoteService.setUserId(this.mFailUserId);
                    this.mFailUserId = null;
                }
                if (this.mFailNamespaces.size() > 0) {
                    this.mRemoteService.addFails((String[]) this.mFailNamespaces.toArray(new String[this.mFailNamespaces.size()]));
                }
                this.mFailNamespaces.clear();
                for (Map.Entry<String, Set<OrangeConfigListenerStub>> entry : this.mFailListeners.entrySet()) {
                    for (OrangeConfigListenerStub listenerStub : entry.getValue()) {
                        this.mRemoteService.registerListener(entry.getKey(), listenerStub, listenerStub.isAppend());
                    }
                }
                this.mFailListeners.clear();
                for (OCandidate candidate : this.mFailCandidates) {
                    this.mRemoteService.addCandidate(candidate.getKey(), candidate.getClientVal(), candidate.getCompare());
                }
                this.mFailCandidates.clear();
                OLog.i(TAG, "sendFailItems end", IWaStat.KEY_COST, Long.valueOf(System.currentTimeMillis() - startNode));
            } catch (Throwable t) {
                OLog.e(TAG, "sendFailItems", t, new Object[0]);
            }
        }
    }

    public String getConfig(String namespace, String key, String defaultVal) {
        if (TextUtils.isEmpty(namespace) || TextUtils.isEmpty(key)) {
            OLog.e(TAG, "getConfig error as param is empty", new Object[0]);
            return defaultVal;
        }
        Map<String, String> result = getConfigs(namespace);
        if (result == null || !result.containsKey(key)) {
            return defaultVal;
        }
        return result.get(key);
    }

    public Map<String, String> getConfigs(String namespace) {
        if (TextUtils.isEmpty(namespace)) {
            OLog.e(TAG, "getConfig error as param is empty", new Object[0]);
            return null;
        }
        asyncGetRemoteService(this.mContext, false);
        if (this.mRemoteService != null) {
            try {
                return this.mRemoteService.getConfigs(namespace);
            } catch (Throwable t) {
                OLog.e(TAG, "getConfigs", t, new Object[0]);
                return null;
            }
        } else if (!this.mFailNamespaces.add(namespace)) {
            return null;
        } else {
            OLog.w(TAG, "getConfigs wait", "namespace", namespace);
            return null;
        }
    }

    public void registerListener(String[] namespaces, OrangeConfigListener listenerV0) {
        regCommonListener(namespaces, listenerV0, true);
    }

    public void registerListener(String[] namespaces, OrangeConfigListenerV1 listenerV1) {
        regCommonListener(namespaces, listenerV1, true);
    }

    public void registerListener(String[] namespaces, OConfigListener listener, boolean append) {
        regCommonListener(namespaces, listener, append);
    }

    private <T extends OBaseListener> void regCommonListener(final String[] namespaces, T t, boolean append) {
        if (namespaces == null || namespaces.length == 0 || t == null) {
            OLog.e(TAG, "registerListener error as param null", new Object[0]);
            return;
        }
        final OrangeConfigListenerStub listenerStub = new OrangeConfigListenerStub(t, append);
        if (this.mRemoteService == null) {
            OLog.w(TAG, "registerListener wait", "namespaces", OrangeUtils.getArrayListFromArray(namespaces));
            for (String namespace : namespaces) {
                getFailListenerStubByKey(namespace).add(listenerStub);
            }
            return;
        }
        OThreadFactory.execute(new Runnable() {
            public void run() {
                OrangeConfigImpl.this.registerListener(namespaces, listenerStub);
            }
        });
    }

    /* access modifiers changed from: package-private */
    public void registerListener(String[] namespaces, OrangeConfigListenerStub listener) {
        if (this.mRemoteService != null && namespaces != null && namespaces.length != 0 && listener != null) {
            for (String namespace : namespaces) {
                try {
                    this.mRemoteService.registerListener(namespace, listener, listener.isAppend());
                } catch (Throwable t) {
                    OLog.w(TAG, "registerListener", t, new Object[0]);
                }
            }
        }
    }

    private Set<OrangeConfigListenerStub> getFailListenerStubByKey(String namespace) {
        Set<OrangeConfigListenerStub> listenerStubs = this.mFailListeners.get(namespace);
        if (listenerStubs != null) {
            return listenerStubs;
        }
        Set<OrangeConfigListenerStub> listenerStubs2 = new HashSet<>();
        this.mFailListeners.put(namespace, listenerStubs2);
        return listenerStubs2;
    }

    public void unregisterListener(String[] namespaces, OrangeConfigListenerV1 listenerV1) {
        if (namespaces == null || namespaces.length == 0 || listenerV1 == null) {
            OLog.e(TAG, "unregisterListenerV1 error as param null", new Object[0]);
        } else if (this.mRemoteService != null) {
            try {
                for (String namespace : namespaces) {
                    this.mRemoteService.unregisterListener(namespace, new OrangeConfigListenerStub(listenerV1));
                }
            } catch (Throwable t) {
                OLog.e(TAG, "unregisterListenerV1", t, new Object[0]);
            }
        } else {
            OLog.w(TAG, "unregisterListenerV1 fail", new Object[0]);
        }
    }

    public void unregisterListener(String[] namespaces, OConfigListener listener) {
        if (namespaces == null || namespaces.length == 0 || listener == null) {
            OLog.e(TAG, "unregisterListener error as param null", new Object[0]);
        } else if (this.mRemoteService != null) {
            try {
                for (String namespace : namespaces) {
                    this.mRemoteService.unregisterListener(namespace, new OrangeConfigListenerStub(listener));
                }
            } catch (Throwable t) {
                OLog.e(TAG, "unregisterListener", t, new Object[0]);
            }
        } else {
            OLog.w(TAG, "unregisterListener fail", new Object[0]);
        }
    }

    public void unregisterListener(String[] namespaces) {
        if (namespaces == null || namespaces.length == 0) {
            OLog.e(TAG, "unregisterListeners error as namespaces is null", new Object[0]);
        } else if (this.mRemoteService != null) {
            try {
                for (String namespace : namespaces) {
                    this.mRemoteService.unregisterListeners(namespace);
                }
            } catch (Throwable t) {
                OLog.e(TAG, "unregisterListeners", t, new Object[0]);
            }
        } else {
            OLog.w(TAG, "unregisterListeners fail", new Object[0]);
        }
    }

    public void forceCheckUpdate() {
        if (this.mRemoteService != null) {
            try {
                this.mRemoteService.forceCheckUpdate();
            } catch (Throwable t) {
                OLog.e(TAG, "forceCheckUpdate", t, new Object[0]);
            }
        } else {
            OLog.w(TAG, "forceCheckUpdate fail", new Object[0]);
        }
    }

    public void enterForeground() {
        forceCheckUpdate();
    }

    public void setUserId(String userId) {
        if (userId == null) {
            userId = "";
        }
        if (this.mRemoteService == null) {
            this.mFailUserId = userId;
            return;
        }
        try {
            this.mRemoteService.setUserId(userId);
        } catch (Throwable t) {
            OLog.e(TAG, "setUserId", t, new Object[0]);
        }
    }

    public void addCandidate(OCandidate candidate) {
        if (candidate == null) {
            OLog.e(TAG, "addCandidate error as candidate is null", new Object[0]);
            return;
        }
        String key = candidate.getKey();
        if (OConstant.CANDIDATE_APPVER.equals(key) || OConstant.CANDIDATE_OSVER.equals(key) || OConstant.CANDIDATE_MANUFACTURER.equals(key) || OConstant.CANDIDATE_BRAND.equals(key) || OConstant.CANDIDATE_MODEL.equals(key) || OConstant.CANDIDATE_HASH_DIS.equals(key)) {
            OLog.e(TAG, "addCandidate fail as not allow override build-in candidate", "key", key);
        } else if (this.mRemoteService != null) {
            try {
                this.mRemoteService.addCandidate(candidate.getKey(), candidate.getClientVal(), candidate.getCompare());
            } catch (Throwable t) {
                OLog.e(TAG, "addCandidate", t, new Object[0]);
            }
        } else if (this.mFailCandidates.add(candidate)) {
            OLog.w(TAG, "addCandidate wait", "candidate", candidate);
        }
    }

    public void enterBackground() {
        OLog.e(TAG, "enterBackground api is @Deprecated", new Object[0]);
    }

    public void setAppSecret(String appSecret) {
        OLog.e(TAG, "setAppSecret api is @Deprecated, please set appSecret in init(OConfig config) api", new Object[0]);
    }

    public void setIndexUpdateMode(int indexUpdateMode) {
        OLog.e(TAG, "setIndexUpdateMode api is @Deprecated, please set indexUpdateMode in init(OConfig config) api", new Object[0]);
    }

    public void setHosts(List<String> list) {
        OLog.e(TAG, "setHosts api is @Deprecated, please set probeHosts in init(OConfig config) api", new Object[0]);
    }

    /* access modifiers changed from: package-private */
    public void asyncGetRemoteService(Context ctx, boolean wait) {
        if (this.mRemoteService == null) {
            bindRemoteService(ctx);
            if (wait) {
                if (this.mBindServiceLock == null) {
                    this.mBindServiceLock = new CountDownLatch(1);
                }
                if (this.mRemoteService == null) {
                    try {
                        this.mBindServiceLock.await(20, TimeUnit.SECONDS);
                    } catch (Throwable t) {
                        OLog.e(TAG, "syncGetBindService", t, new Object[0]);
                    }
                    if (this.mRemoteService == null && ctx != null && this.mIsMainProcess) {
                        OLog.w(TAG, "syncGetBindService", "bind service timeout local stub in main process");
                        this.mRemoteService = new OrangeApiServiceStub(ctx);
                    }
                }
            }
        }
    }

    private void bindRemoteService(Context ctx) {
        if (ctx != null && this.mRemoteService == null && this.mIsBindingService.compareAndSet(false, true)) {
            OLog.i(TAG, "bindRemoteService start", new Object[0]);
            try {
                Intent intent = new Intent(ctx, OrangeApiService.class);
                intent.setAction(OrangeApiService.class.getName());
                intent.addCategory("android.intent.category.DEFAULT");
                if (!ctx.bindService(intent, this.mConnection, 1)) {
                    OLog.w(TAG, "bindRemoteService fail", new Object[0]);
                }
            } catch (Throwable t) {
                OLog.e(TAG, "bindRemoteService", t, new Object[0]);
            }
        }
    }
}
