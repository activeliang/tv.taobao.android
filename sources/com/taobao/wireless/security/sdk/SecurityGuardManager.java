package com.taobao.wireless.security.sdk;

import android.content.Context;
import com.alibaba.wireless.security.open.SecException;
import com.taobao.wireless.security.sdk.atlasencrypt.IAtlasEncryptComponent;
import com.taobao.wireless.security.sdk.datacollection.IDataCollectionComponent;
import com.taobao.wireless.security.sdk.dynamicdataencrypt.IDynamicDataEncryptComponent;
import com.taobao.wireless.security.sdk.dynamicdatastore.IDynamicDataStoreComponent;
import com.taobao.wireless.security.sdk.indiekit.IIndieKitComponent;
import com.taobao.wireless.security.sdk.initialize.IInitializeComponent;
import com.taobao.wireless.security.sdk.initialize.a;
import com.taobao.wireless.security.sdk.nocaptcha.INoCaptchaComponent;
import com.taobao.wireless.security.sdk.pkgvaliditycheck.IPkgValidityCheckComponent;
import com.taobao.wireless.security.sdk.rootdetect.IRootDetectComponent;
import com.taobao.wireless.security.sdk.safetoken.ISafeTokenComponent;
import com.taobao.wireless.security.sdk.securesignature.ISecureSignatureComponent;
import com.taobao.wireless.security.sdk.securityDNS.ISecurityDNSComponent;
import com.taobao.wireless.security.sdk.securitybody.ISecurityBodyComponent;
import com.taobao.wireless.security.sdk.simulatordetect.ISimulatorDetectComponent;
import com.taobao.wireless.security.sdk.staticdataencrypt.IStaticDataEncryptComponent;
import com.taobao.wireless.security.sdk.staticdatastore.IStaticDataStoreComponent;

public class SecurityGuardManager {
    private static volatile SecurityGuardManager a;
    private static volatile IInitializeComponent b;
    private static final Object c = new Object();
    private com.alibaba.wireless.security.open.SecurityGuardManager d;

    private SecurityGuardManager(Context context) {
        try {
            this.d = com.alibaba.wireless.security.open.SecurityGuardManager.getInstance(context);
        } catch (SecException e) {
            e.printStackTrace();
        }
    }

    private <T> T a(Class<T> cls) {
        try {
            if (this.d != null) {
                return this.d.getInterface(cls);
            }
        } catch (SecException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static IInitializeComponent getInitializer() {
        if (b == null) {
            synchronized (c) {
                if (b == null) {
                    b = new a();
                }
            }
        }
        return b;
    }

    public static SecurityGuardManager getInstance(Context context) {
        if (a == null) {
            synchronized (SecurityGuardManager.class) {
                if (context == null) {
                    return null;
                }
                if (a == null && getInitializer().initialize(context) == 0) {
                    a = new SecurityGuardManager(context);
                }
            }
        }
        return a;
    }

    public IAtlasEncryptComponent getAtlasEncryptComp() {
        return (IAtlasEncryptComponent) getComponent(15);
    }

    public IComponent getComponent(int i) {
        switch (i) {
            case 1:
                return (IComponent) a(ISecureSignatureComponent.class);
            case 2:
                return (IComponent) a(IDynamicDataStoreComponent.class);
            case 3:
                return (IComponent) a(IIndieKitComponent.class);
            case 4:
                return (IComponent) a(IStaticDataStoreComponent.class);
            case 5:
                return (IComponent) a(IRootDetectComponent.class);
            case 6:
                return (IComponent) a(IDataCollectionComponent.class);
            case 7:
                return (IComponent) a(IStaticDataEncryptComponent.class);
            case 8:
                return (IComponent) a(ISecurityBodyComponent.class);
            case 9:
                return (IComponent) a(IDynamicDataEncryptComponent.class);
            case 10:
                return (IComponent) a(ISimulatorDetectComponent.class);
            case 11:
                return (IComponent) a(ISecurityDNSComponent.class);
            case 12:
                return (IComponent) a(IPkgValidityCheckComponent.class);
            case 14:
                return (IComponent) a(INoCaptchaComponent.class);
            case 15:
                return (IComponent) a(IAtlasEncryptComponent.class);
            case 16:
                return (IComponent) a(ISafeTokenComponent.class);
            default:
                return null;
        }
    }

    public IDataCollectionComponent getDataCollectionComp() {
        return (IDataCollectionComponent) getComponent(6);
    }

    public IDynamicDataEncryptComponent getDynamicDataEncryptComp() {
        return (IDynamicDataEncryptComponent) getComponent(9);
    }

    public IDynamicDataStoreComponent getDynamicDataStoreComp() {
        return (IDynamicDataStoreComponent) getComponent(2);
    }

    public IIndieKitComponent getIndieKitComp() {
        return (IIndieKitComponent) getComponent(3);
    }

    public <T> T getInterface(Class<T> cls) {
        try {
            return this.d.getInterface(cls);
        } catch (SecException e) {
            e.printStackTrace();
            return null;
        }
    }

    public INoCaptchaComponent getNoCaptchaComp() {
        return (INoCaptchaComponent) getComponent(14);
    }

    public IPkgValidityCheckComponent getPackageValidityCheckComp() {
        return (IPkgValidityCheckComponent) getComponent(12);
    }

    public IRootDetectComponent getRootDetectComp() {
        return (IRootDetectComponent) getComponent(5);
    }

    public String getSDKVerison() {
        if (this.d != null) {
            return this.d.getSDKVerison();
        }
        return null;
    }

    public ISafeTokenComponent getSafeTokenComp() {
        return (ISafeTokenComponent) getComponent(16);
    }

    public ISecureSignatureComponent getSecureSignatureComp() {
        return (ISecureSignatureComponent) getComponent(1);
    }

    public ISecurityBodyComponent getSecurityBodyComp() {
        return (ISecurityBodyComponent) getComponent(8);
    }

    public ISecurityDNSComponent getSecurityDNSComp() {
        return (ISecurityDNSComponent) getComponent(11);
    }

    public ISimulatorDetectComponent getSimulatorDetectComp() {
        return (ISimulatorDetectComponent) getComponent(10);
    }

    public IStaticDataEncryptComponent getStaticDataEncryptComp() {
        return (IStaticDataEncryptComponent) getComponent(7);
    }

    public IStaticDataStoreComponent getStaticDataStoreComp() {
        return (IStaticDataStoreComponent) getComponent(4);
    }

    public Boolean isOpen() {
        return false;
    }
}
