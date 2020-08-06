package com.yunos.tvtaobao.uuid.utils;

import android.content.Context;
import android.content.SharedPreferences;
import com.alibaba.wireless.security.open.SecException;
import com.alibaba.wireless.security.open.SecurityGuardManager;
import com.alibaba.wireless.security.open.dynamicdatastore.IDynamicDataStoreComponent;
import com.alibaba.wireless.security.open.staticdataencrypt.IStaticDataEncryptComponent;
import com.alibaba.wireless.security.open.staticdatastore.IStaticDataStoreComponent;

public class SGMWrapper {
    private String AUTHCODE = "uuid";
    private boolean isRelease = true;
    private Context mContext;
    private String mSuffix = "_debug";

    public SGMWrapper(Context context, String authcode) {
        this.mContext = context;
        if (this.isRelease) {
            this.mSuffix = "_release";
        }
        this.AUTHCODE = authcode;
    }

    public String getUUID() {
        String uuid = getDynamicData("uuid");
        Logger.log_d("SGM getUUID:" + uuid);
        if (uuid == null) {
            if (this.mContext == null) {
                return null;
            }
            uuid = this.mContext.getSharedPreferences("uuid", 0).getString("uuid", (String) null);
            Logger.log_d("SP getUUID:" + uuid);
        }
        return uuid;
    }

    public boolean saveUUID(String uuid) {
        if (this.mContext != null) {
            return setDynamicData("uuid", uuid);
        }
        Logger.log_d("SGM getUUID ---- ");
        return false;
    }

    public boolean saveWlan(String mac) {
        if (this.mContext == null || mac == null) {
            Logger.log_d("SGM mContext is null ");
            return false;
        }
        try {
            SharedPreferences preferences = this.mContext.getSharedPreferences("uuid", 0);
            String rMac = preferences.getString("mac", (String) null);
            if (rMac == null || !mac.equals(rMac)) {
                return false;
            }
            SharedPreferences.Editor editor = preferences.edit();
            editor.putString("mac", mac);
            editor.commit();
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    public boolean isActivated() {
        boolean rst = "uuid activate success".equals(getDynamicData("activate"));
        Logger.log_d("isActivated: rst = " + rst);
        return rst;
    }

    public boolean saveActMsg() {
        return setDynamicData("activate", "uuid activate success");
    }

    public String getPKMd5() {
        return getExtra("uuid_keymd5" + this.mSuffix);
    }

    public String getPassword() {
        return getExtra("uuid_password" + this.mSuffix);
    }

    public String getPrivateKey() {
        if (!this.isRelease) {
            return "308204bd020100300d06092a864886f70d0101010500048204a7308204a30201000282010100995b30c6f8fdb9a2562bdcb2c08a06e835a3f9946441974f2b73343d0a7c45567e13cb6a468eeaeea386b63ada102c3d2b6a27bfbd829f00a2f4f76035e27191ec69e62b01f27d7e06f023953472bafdfd40e045e1c60e5fe3b662e87c35ccddaf2f83470fc5095051131388cc34845309f043adc659301e87efdcb3921ba20d6408980606d36cdd2257f0cf9b4203186e98e0f283fa6687a33978cbc9e76cdb2395c9ffe447357445377548712fa497b39977cd6d52e872f66ae8d811dc9e91c314e035d2b103688761177358724686e022254086f90719e07dbd68ac6663642b6b0d685953dfa923e64bd5444b75e08deb562bbe3b7a6a6bdd9c57de9e1da102010302820100663ccb2f50a9266c39729321d5b159f023c2a662ed810f8a1cf77828b1a82e39a962879c2f09f1f46d04797c91601d7e1cf16fd52901bf55c1f8a4eace96f66148469972014c53a959f56d0e22f72753fe2b402e9684099542799745a823dde91f75022f5fd8b0e0360cb7b0882302e206a02d1e843b7569aff53dcd0c126c07e50dc2fc6c6a3c17a07b1da923bd0a7de1f1a7a60acabe0b6119ac9e203246d7317c92696f2b10ff7d22fc1ed8f5bdcc317340a210ab5ccaef6ca7fbc6c9e0a48eb1a7b0f11bd44662f945e1ad0109c61fc4c8a7d618901f368f4e229cdfd6dcdd34f5ee2580ca72466be8351f23a0c19520bd57045ee2b49846fa1f01efe6cb02818100cc1a8c20fa6ca2a3613f518c2c1add81c73b90a0327509dedffcd8f345d8402b625857b654423b79d48a80a328cd372120d9a391faf87dc3942149aa0eb0939e60abfc34873883cd38807ed7b93ae0e29f6de5739d4c67c47ab2730be4424a62dfc754ed08f3e72697d37b4be0de7b00388abdd8a9bf6ffedf3b059030ab307502818100c059676a69c77016505ff2c5b98b95d9d472d4d941553f97b1961ceb53c3c26cf70296ab6944607b34f87a7702f1d0c44892f34859595f7efb26a33458fd39fc8c5e6877e1cec131ba6aafc91bb5d6fb110d12d12887c72693f45528dcd456b5ffd44796181ec8d72270f439b4b789bdf5af7c508dedb65ca8381f992b0b12fd028181008811b2c0a6f31717962a365d72bc93abda27b5c021a35be9eaa890a22e902ac796e58fcee2d6d251385c55c21b337a1615e66d0bfca5a92d0d6b86715f20626995c7fd785a25ad337b00548fd0d1eb41bf9e98f7be32efd851cc4cb2982c31973fda389e05f7ef6f0fe2523295e9a7557b07293b1bd4f55494d203b575c775a302818100803b9a46f12fa00ee03ff72e7bb263e68da1e33b80e37fba766413478d2d2c48a4ac6472462d95a778a5a6fa01f68b2d85b74cdae63b94ff5219c222e5fe26a85d9445a5413480cbd19c7530bd23e4a760b361e0c5afda19b7f838c5e88d8f23ffe2da64101485e4c1a0a2d12325067ea3ca52e05e9e79931ad01510c75cb753028181008b20cea8091310941f45264e6a8407e7e7a10d54ada8137f94ca08cdf167c2a206e52eacbb5b2d4e12f437b6cb47fcc406155b6746ed1901bc95a4396c143765a851200be3954f99a91f0feafeab13e31cd90a7140e28c3df593eca62fc4b8af1cc458c309a4c2bbdb410087e6a6b8a1986fc9e2ba67f1610b630968c13ff1e6";
        }
        String key1 = getExtra("uuid_key1" + this.mSuffix);
        String key2 = getExtra("uuid_key2" + this.mSuffix);
        String key3 = getExtra("uuid_key3" + this.mSuffix);
        String key4 = getExtra("uuid_key4" + this.mSuffix);
        return key1 + key2 + key3 + key4 + getExtra("uuid_key5" + this.mSuffix);
    }

    public String encryptUUID(String uuid) {
        IStaticDataEncryptComponent comp;
        String encStr = null;
        SecurityGuardManager sgMgr = null;
        try {
            sgMgr = SecurityGuardManager.getInstance(this.mContext);
        } catch (SecException e1) {
            e1.printStackTrace();
        }
        if (!(sgMgr == null || (comp = sgMgr.getStaticDataEncryptComp()) == null)) {
            encStr = null;
            try {
                encStr = comp.staticSafeEncrypt(18, "encryptuuid", uuid, this.AUTHCODE);
            } catch (SecException e) {
                e.printStackTrace();
            }
            Logger.log_d("encStr = " + encStr);
        }
        return encStr;
    }

    public String deencryptUUID(String encStr) {
        IStaticDataEncryptComponent comp;
        String uuid = null;
        SecurityGuardManager sgMgr = null;
        try {
            sgMgr = SecurityGuardManager.getInstance(this.mContext);
        } catch (SecException e1) {
            e1.printStackTrace();
        }
        if (!(sgMgr == null || (comp = sgMgr.getStaticDataEncryptComp()) == null)) {
            uuid = null;
            try {
                uuid = comp.staticSafeDecrypt(18, "encryptuuid", encStr, this.AUTHCODE);
            } catch (SecException e) {
                e.printStackTrace();
            }
            Logger.log_d("deencry uuid = " + uuid);
        }
        return uuid;
    }

    private String getExtra(String extraKey) {
        IStaticDataStoreComponent sdsComp;
        SecurityGuardManager sgMgr = null;
        try {
            sgMgr = SecurityGuardManager.getInstance(this.mContext);
        } catch (SecException e) {
            e.printStackTrace();
        }
        if (sgMgr == null || (sdsComp = sgMgr.getStaticDataStoreComp()) == null) {
            return null;
        }
        try {
            return sdsComp.getExtraData(extraKey, this.AUTHCODE);
        } catch (SecException e2) {
            e2.printStackTrace();
            return null;
        }
    }

    private String getDynamicData(String key) {
        IDynamicDataStoreComponent ddsComponent;
        SecurityGuardManager sgMgr = null;
        try {
            sgMgr = SecurityGuardManager.getInstance(this.mContext);
        } catch (SecException e) {
            e.printStackTrace();
        }
        if (sgMgr == null || (ddsComponent = sgMgr.getDynamicDataStoreComp()) == null) {
            return null;
        }
        try {
            return ddsComponent.getString(key);
        } catch (SecException e2) {
            e2.printStackTrace();
            return null;
        }
    }

    private boolean setDynamicData(String key, String value) {
        IDynamicDataStoreComponent ddsComponent;
        SecurityGuardManager sgMgr = null;
        try {
            sgMgr = SecurityGuardManager.getInstance(this.mContext);
        } catch (SecException e) {
            e.printStackTrace();
        }
        if (sgMgr == null || (ddsComponent = sgMgr.getDynamicDataStoreComp()) == null) {
            return false;
        }
        try {
            if (ddsComponent.putString(key, value) != 0) {
                return true;
            }
            return false;
        } catch (SecException e2) {
            e2.printStackTrace();
            return false;
        }
    }
}
