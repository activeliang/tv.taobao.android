package org.apache.commons.codec.digest;

import com.yunos.tvtaobao.biz.listener.OnReminderListener;
import java.security.SecureRandom;
import java.util.Random;

class B64 {
    static final String B64T = "./0123456789ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz";

    B64() {
    }

    static void b64from24bit(byte b2, byte b1, byte b0, int outLen, StringBuilder buffer) {
        int w = ((b2 << 16) & OnReminderListener.RET_FULL) | ((b1 << 8) & OnReminderListener.RET_FULL) | (b0 & OnReminderListener.RET_FULL);
        int n = outLen;
        while (true) {
            int n2 = n;
            n = n2 - 1;
            if (n2 > 0) {
                buffer.append(B64T.charAt(w & 63));
                w >>= 6;
            } else {
                return;
            }
        }
    }

    static String getRandomSalt(int num) {
        return getRandomSalt(num, new SecureRandom());
    }

    static String getRandomSalt(int num, Random random) {
        StringBuilder saltString = new StringBuilder(num);
        for (int i = 1; i <= num; i++) {
            saltString.append(B64T.charAt(random.nextInt(B64T.length())));
        }
        return saltString.toString();
    }
}
