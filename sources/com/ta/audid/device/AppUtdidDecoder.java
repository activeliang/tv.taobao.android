package com.ta.audid.device;

import com.ta.audid.utils.ByteUtils;
import com.ta.utdid2.android.utils.Base64;
import com.ta.utdid2.android.utils.IntUtils;
import com.ta.utdid2.android.utils.StringUtils;
import com.ta.utdid2.device.UTUtdid;
import java.util.Arrays;

public class AppUtdidDecoder {
    public static UtdidObj decode(String utdid) {
        UtdidObj utdidObj = new UtdidObj();
        if (utdid == null || utdid.length() != 24) {
            utdidObj.setValid(false);
        } else {
            try {
                byte[] value = Base64.decode(utdid, 2);
                if (value.length == 18) {
                    byte[] needCheckBytes = new byte[14];
                    byte[] timestamp = new byte[4];
                    byte[] checkSum = new byte[4];
                    System.arraycopy(value, 0, needCheckBytes, 0, 14);
                    System.arraycopy(value, 0, timestamp, 0, 4);
                    byte version = value[8];
                    System.arraycopy(value, 14, checkSum, 0, 4);
                    try {
                        if (Arrays.equals(checkSum, IntUtils.getBytes(StringUtils.hashCode(UTUtdid.calcHmac(needCheckBytes))))) {
                            utdidObj.setValid(true);
                            utdidObj.setTimestamp(ByteUtils.getLongByByte4(timestamp));
                            utdidObj.setVersion(version);
                        }
                    } catch (Exception e) {
                    }
                }
            } catch (Exception e2) {
                utdidObj.setValid(false);
            }
        }
        return utdidObj;
    }
}
