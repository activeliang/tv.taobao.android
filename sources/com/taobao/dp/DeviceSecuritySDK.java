package com.taobao.dp;

import android.content.Context;
import com.alibaba.wireless.security.open.SecException;
import com.alibaba.wireless.security.open.SecurityGuardManager;
import com.alibaba.wireless.security.open.umid.IUMIDComponent;
import com.alibaba.wireless.security.open.umid.IUMIDInitListenerEx;
import com.taobao.dp.client.IInitResultListener;
import com.taobao.dp.http.IUrlRequestService;

@Deprecated
public final class DeviceSecuritySDK {
    public static final int ENVIRONMENT_DAILY = 2;
    public static final int ENVIRONMENT_ONLINE = 0;
    public static final int ENVIRONMENT_PRE = 1;
    private static DeviceSecuritySDK instance = null;
    private IUMIDComponent mUmidComponent = null;
    private String mVersion;

    private DeviceSecuritySDK(Context context) {
        try {
            SecurityGuardManager mamanger = SecurityGuardManager.getInstance(context);
            this.mUmidComponent = (IUMIDComponent) mamanger.getInterface(IUMIDComponent.class);
            this.mVersion = mamanger.getSDKVerison();
        } catch (SecException e) {
            e.printStackTrace();
        }
    }

    public static DeviceSecuritySDK getInstance(Context context) {
        if (instance == null) {
            synchronized (DeviceSecuritySDK.class) {
                if (instance == null) {
                    instance = new DeviceSecuritySDK(context);
                }
            }
        }
        return instance;
    }

    public int initSync(String appKey, int envMode, IUrlRequestService reqService) {
        return initSync(appKey, "", envMode, reqService);
    }

    public int initSync(String appKey, String authCode, int envMode, IUrlRequestService reqService) {
        try {
            if (this.mUmidComponent != null) {
                return this.mUmidComponent.initUMIDSync(envMode);
            }
            return -1;
        } catch (SecException e) {
            e.printStackTrace();
            return -1;
        }
    }

    public void initAsync(String appKey, int envMode, IUrlRequestService reqService, IInitResultListener listener) {
        initAsync(appKey, "", envMode, reqService, listener);
    }

    public void initAsync(String appKey, String authCode, int envMode, IUrlRequestService reqService, final IInitResultListener listener) {
        IUMIDInitListenerEx umidInitListenerEx = null;
        if (listener != null) {
            umidInitListenerEx = new IUMIDInitListenerEx() {
                public void onUMIDInitFinishedEx(String token, int resultCode) {
                    listener.onInitFinished(token, resultCode);
                }
            };
        }
        try {
            if (this.mUmidComponent != null) {
                this.mUmidComponent.initUMID(envMode, umidInitListenerEx);
            }
        } catch (SecException e) {
            e.printStackTrace();
            if (listener != null) {
                listener.onInitFinished((String) null, -1);
            }
        }
    }

    @Deprecated
    public void init() {
        try {
            if (this.mUmidComponent != null) {
                this.mUmidComponent.initUMID();
            }
        } catch (SecException e) {
            e.printStackTrace();
        }
    }

    @Deprecated
    public void init(IUrlRequestService reqService) {
        try {
            if (this.mUmidComponent != null) {
                this.mUmidComponent.initUMID();
            }
        } catch (SecException e) {
            e.printStackTrace();
        }
    }

    @Deprecated
    public String getSecurityToken() {
        try {
            if (this.mUmidComponent != null) {
                return this.mUmidComponent.getSecurityToken();
            }
            return "000000000000000000000000";
        } catch (SecException e) {
            e.printStackTrace();
            return "000000000000000000000000";
        }
    }

    public String getSecurityToken(int env) {
        try {
            if (this.mUmidComponent != null) {
                return this.mUmidComponent.getSecurityToken(env);
            }
            return "000000000000000000000000";
        } catch (SecException e) {
            e.printStackTrace();
            return "000000000000000000000000";
        }
    }

    @Deprecated
    public void sendLoginResult(String nick) {
    }

    public void setEnvironment(int environment) {
        try {
            if (this.mUmidComponent != null) {
                this.mUmidComponent.setEnvironment(environment);
            }
        } catch (SecException e) {
            e.printStackTrace();
        }
    }

    public synchronized void setOnlineHost(OnlineHost host) throws IllegalArgumentException {
        if (host == null) {
            throw new IllegalArgumentException("host is null");
        }
        try {
            if (this.mUmidComponent != null) {
                this.mUmidComponent.setOnlineHost(host.getHost());
            }
        } catch (SecException e) {
            e.printStackTrace();
        }
        return;
    }

    public String getVersion() {
        return this.mVersion;
    }
}
