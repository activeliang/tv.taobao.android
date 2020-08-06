package com.ta.utdid2.device;

import com.ta.utdid2.android.utils.AESUtils;
import com.ta.utdid2.android.utils.Base64;
import com.ta.utdid2.android.utils.StringUtils;

public class UTUtdidHelper2 {
    public String dePack(String pPackedUtdid) {
        return AESUtils.decrypt(pPackedUtdid);
    }

    public String dePackWithBase64(String pUtdidWithBase64) {
        String lEResult = AESUtils.decrypt(pUtdidWithBase64);
        if (StringUtils.isEmpty(lEResult)) {
            return null;
        }
        try {
            return new String(Base64.decode(lEResult, 0));
        } catch (IllegalArgumentException e) {
            return null;
        }
    }
}
