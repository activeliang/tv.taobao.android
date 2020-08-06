package com.taobao.securityjni;

import android.content.ContextWrapper;
import com.alibaba.wireless.security.open.SecException;
import com.alibaba.wireless.security.open.SecurityGuardManager;
import com.alibaba.wireless.security.open.dynamicdataencrypt.IDynamicDataEncryptComponent;
import com.alibaba.wireless.security.open.staticdataencrypt.IStaticDataEncryptComponent;
import com.alibaba.wireless.security.open.staticdatastore.IStaticDataStoreComponent;
import com.taobao.securityjni.tools.DataContext;

@Deprecated
public class UtilWX {
    private ContextWrapper context;

    public UtilWX(ContextWrapper context2) {
        this.context = context2;
    }

    public String Put(String data, DataContext ctx) {
        String appKey;
        IStaticDataEncryptComponent staticDataEncryptComponent;
        if (!(data == null || data.length() <= 0 || ctx == null)) {
            try {
                IStaticDataStoreComponent staticDataStoreComponent = SecurityGuardManager.getInstance(this.context).getStaticDataStoreComp();
                if (staticDataStoreComponent != null) {
                    if (ctx.extData != null) {
                        appKey = new String(ctx.extData);
                    } else {
                        appKey = staticDataStoreComponent.getAppKeyByIndex(ctx.index < 0 ? 0 : ctx.index, "");
                    }
                    if (!(appKey == null || (staticDataEncryptComponent = SecurityGuardManager.getInstance(this.context).getStaticDataEncryptComp()) == null)) {
                        return staticDataEncryptComponent.staticSafeEncrypt(16, appKey, data, "");
                    }
                }
            } catch (SecException e) {
                e.printStackTrace();
            }
        }
        return null;
    }

    public String Get(String data, DataContext ctx) {
        String appKey;
        IStaticDataEncryptComponent staticDataEncryptComponent;
        if (!(data == null || data.length() <= 0 || ctx == null)) {
            try {
                IStaticDataStoreComponent staticDataStoreComponent = SecurityGuardManager.getInstance(this.context).getStaticDataStoreComp();
                if (staticDataStoreComponent != null) {
                    if (ctx.extData != null) {
                        appKey = new String(ctx.extData);
                    } else {
                        appKey = staticDataStoreComponent.getAppKeyByIndex(ctx.index < 0 ? 0 : ctx.index, "");
                    }
                    if (!(appKey == null || (staticDataEncryptComponent = SecurityGuardManager.getInstance(this.context).getStaticDataEncryptComp()) == null)) {
                        return staticDataEncryptComponent.staticSafeDecrypt(16, appKey, data, "");
                    }
                }
            } catch (SecException e) {
                e.printStackTrace();
            }
        }
        return null;
    }

    public byte[] Put(byte[] data, DataContext ctx) {
        String appKey;
        IStaticDataEncryptComponent staticDataEncryptComponent;
        if (!(data == null || data.length <= 0 || ctx == null)) {
            try {
                IStaticDataStoreComponent staticDataStoreComponent = SecurityGuardManager.getInstance(this.context).getStaticDataStoreComp();
                if (staticDataStoreComponent != null) {
                    if (ctx.extData != null) {
                        appKey = new String(ctx.extData);
                    } else {
                        appKey = staticDataStoreComponent.getAppKeyByIndex(ctx.index < 0 ? 0 : ctx.index, "");
                    }
                    if (!(appKey == null || (staticDataEncryptComponent = SecurityGuardManager.getInstance(this.context).getStaticDataEncryptComp()) == null)) {
                        return staticDataEncryptComponent.staticBinarySafeEncrypt(16, appKey, data, "");
                    }
                }
            } catch (SecException e) {
                e.printStackTrace();
            }
        }
        return null;
    }

    public byte[] Get(byte[] data, DataContext ctx) {
        String appKey;
        IStaticDataEncryptComponent staticDataEncryptComponent;
        if (!(data == null || data.length <= 0 || ctx == null)) {
            try {
                IStaticDataStoreComponent staticDataStoreComponent = SecurityGuardManager.getInstance(this.context).getStaticDataStoreComp();
                if (staticDataStoreComponent != null) {
                    if (ctx.extData != null) {
                        appKey = new String(ctx.extData);
                    } else {
                        appKey = staticDataStoreComponent.getAppKeyByIndex(ctx.index < 0 ? 0 : ctx.index, "");
                    }
                    if (!(appKey == null || (staticDataEncryptComponent = SecurityGuardManager.getInstance(this.context).getStaticDataEncryptComp()) == null)) {
                        return staticDataEncryptComponent.staticBinarySafeDecrypt(16, appKey, data, "");
                    }
                }
            } catch (SecException e) {
                e.printStackTrace();
            }
        }
        return null;
    }

    public String EncryptData(String data, String key) {
        byte[] result;
        if (data != null && data.length() > 0 && key != null && key.length() > 0) {
            try {
                IStaticDataEncryptComponent staticDataEncryptComponent = SecurityGuardManager.getInstance(this.context).getStaticDataEncryptComp();
                if (!(staticDataEncryptComponent == null || (result = staticDataEncryptComponent.staticBinarySafeEncrypt(16, key, data.getBytes(), "")) == null)) {
                    return new String(result);
                }
            } catch (SecException e) {
                e.printStackTrace();
            }
        }
        return null;
    }

    public String DecryptData(String data, String key) {
        byte[] result;
        if (data != null && data.length() > 0 && key != null && key.length() > 0) {
            try {
                IStaticDataEncryptComponent staticDataEncryptComponent = SecurityGuardManager.getInstance(this.context).getStaticDataEncryptComp();
                if (!(staticDataEncryptComponent == null || (result = staticDataEncryptComponent.staticBinarySafeDecrypt(16, key, data.getBytes(), "")) == null)) {
                    return new String(result);
                }
            } catch (SecException e) {
                e.printStackTrace();
            }
        }
        return null;
    }

    public byte[] EncryptData(byte[] data, byte[] key) {
        if (data != null && data.length > 0 && key != null && key.length > 0) {
            try {
                IStaticDataEncryptComponent staticDataEncryptComponent = SecurityGuardManager.getInstance(this.context).getStaticDataEncryptComp();
                if (staticDataEncryptComponent != null) {
                    return staticDataEncryptComponent.staticBinarySafeEncrypt(16, new String(key), data, "");
                }
            } catch (SecException e) {
                e.printStackTrace();
            }
        }
        return null;
    }

    public byte[] DecryptData(byte[] data, byte[] key) {
        if (data != null && data.length > 0 && key != null && key.length > 0) {
            try {
                IStaticDataEncryptComponent staticDataEncryptComponent = SecurityGuardManager.getInstance(this.context).getStaticDataEncryptComp();
                if (staticDataEncryptComponent != null) {
                    return staticDataEncryptComponent.staticBinarySafeDecrypt(16, new String(key), data, "");
                }
            } catch (SecException e) {
                e.printStackTrace();
            }
        }
        return null;
    }

    public String Put(String data) {
        if (data != null && data.length() > 0) {
            try {
                IDynamicDataEncryptComponent dynamicDataEncryptComponent = SecurityGuardManager.getInstance(this.context).getDynamicDataEncryptComp();
                if (dynamicDataEncryptComponent != null) {
                    return dynamicDataEncryptComponent.dynamicEncrypt(data);
                }
            } catch (SecException e) {
                e.printStackTrace();
            }
        }
        return null;
    }

    public String Get(String data) {
        if (data != null && data.length() > 0) {
            try {
                IDynamicDataEncryptComponent dynamicDataEncryptComponent = SecurityGuardManager.getInstance(this.context).getDynamicDataEncryptComp();
                if (dynamicDataEncryptComponent != null) {
                    return dynamicDataEncryptComponent.dynamicDecrypt(data);
                }
            } catch (SecException e) {
                e.printStackTrace();
            }
        }
        return null;
    }

    public byte[] Put(byte[] data) {
        String result;
        if (data != null && data.length > 0) {
            try {
                IDynamicDataEncryptComponent dynamicDataEncryptComponent = SecurityGuardManager.getInstance(this.context).getDynamicDataEncryptComp();
                if (!(dynamicDataEncryptComponent == null || (result = dynamicDataEncryptComponent.dynamicEncrypt(new String(data))) == null || result.length() <= 0)) {
                    return result.getBytes();
                }
            } catch (SecException e) {
                e.printStackTrace();
            }
        }
        return null;
    }

    public byte[] Get(byte[] data) {
        String result;
        if (data != null && data.length > 0) {
            try {
                IDynamicDataEncryptComponent dynamicDataEncryptComponent = SecurityGuardManager.getInstance(this.context).getDynamicDataEncryptComp();
                if (!(dynamicDataEncryptComponent == null || (result = dynamicDataEncryptComponent.dynamicDecrypt(new String(data))) == null || result.length() <= 0)) {
                    return result.getBytes();
                }
            } catch (SecException e) {
                e.printStackTrace();
            }
        }
        return null;
    }
}
