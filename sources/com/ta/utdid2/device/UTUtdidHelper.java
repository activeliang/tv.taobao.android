package com.ta.utdid2.device;

import com.ta.utdid2.android.utils.AESUtils;
import com.ta.utdid2.android.utils.Base64;

public class UTUtdidHelper {
    public String pack(byte[] pUtdid) {
        return AESUtils.encrypt(Base64.encodeToString(pUtdid, 2));
    }

    public String packUtdidStr(String pUtdid) {
        return AESUtils.encrypt(pUtdid);
    }

    public String dePack(String pPackedUtdid) {
        return AESUtils.decrypt(pPackedUtdid);
    }
}
