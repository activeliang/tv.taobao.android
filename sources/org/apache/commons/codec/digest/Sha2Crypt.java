package org.apache.commons.codec.digest;

import com.yunos.tvtaobao.biz.listener.OnReminderListener;
import java.security.MessageDigest;
import java.util.Arrays;
import java.util.Random;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import mtopsdk.common.util.SymbolExpUtil;
import org.apache.commons.codec.Charsets;

public class Sha2Crypt {
    private static final int ROUNDS_DEFAULT = 5000;
    private static final int ROUNDS_MAX = 999999999;
    private static final int ROUNDS_MIN = 1000;
    private static final String ROUNDS_PREFIX = "rounds=";
    private static final Pattern SALT_PATTERN = Pattern.compile("^\\$([56])\\$(rounds=(\\d+)\\$)?([\\.\\/a-zA-Z0-9]{1,16}).*");
    private static final int SHA256_BLOCKSIZE = 32;
    static final String SHA256_PREFIX = "$5$";
    private static final int SHA512_BLOCKSIZE = 64;
    static final String SHA512_PREFIX = "$6$";

    public static String sha256Crypt(byte[] keyBytes) {
        return sha256Crypt(keyBytes, (String) null);
    }

    public static String sha256Crypt(byte[] keyBytes, String salt) {
        if (salt == null) {
            salt = SHA256_PREFIX + B64.getRandomSalt(8);
        }
        return sha2Crypt(keyBytes, salt, SHA256_PREFIX, 32, MessageDigestAlgorithms.SHA_256);
    }

    public static String sha256Crypt(byte[] keyBytes, String salt, Random random) {
        if (salt == null) {
            salt = SHA256_PREFIX + B64.getRandomSalt(8, random);
        }
        return sha2Crypt(keyBytes, salt, SHA256_PREFIX, 32, MessageDigestAlgorithms.SHA_256);
    }

    private static String sha2Crypt(byte[] keyBytes, String salt, String saltPrefix, int blocksize, String algorithm) {
        int keyLen = keyBytes.length;
        int rounds = 5000;
        boolean roundsCustom = false;
        if (salt == null) {
            throw new IllegalArgumentException("Salt must not be null");
        }
        Matcher m = SALT_PATTERN.matcher(salt);
        if (!m.find()) {
            throw new IllegalArgumentException("Invalid salt value: " + salt);
        }
        if (m.group(3) != null) {
            rounds = Math.max(1000, Math.min(ROUNDS_MAX, Integer.parseInt(m.group(3))));
            roundsCustom = true;
        }
        String saltString = m.group(4);
        byte[] saltBytes = saltString.getBytes(Charsets.UTF_8);
        int saltLen = saltBytes.length;
        MessageDigest ctx = DigestUtils.getDigest(algorithm);
        ctx.update(keyBytes);
        ctx.update(saltBytes);
        MessageDigest altCtx = DigestUtils.getDigest(algorithm);
        altCtx.update(keyBytes);
        altCtx.update(saltBytes);
        altCtx.update(keyBytes);
        byte[] altResult = altCtx.digest();
        int cnt = keyBytes.length;
        while (cnt > blocksize) {
            ctx.update(altResult, 0, blocksize);
            cnt -= blocksize;
        }
        ctx.update(altResult, 0, cnt);
        for (int cnt2 = keyBytes.length; cnt2 > 0; cnt2 >>= 1) {
            if ((cnt2 & 1) != 0) {
                ctx.update(altResult, 0, blocksize);
            } else {
                ctx.update(keyBytes);
            }
        }
        byte[] altResult2 = ctx.digest();
        MessageDigest altCtx2 = DigestUtils.getDigest(algorithm);
        for (int i = 1; i <= keyLen; i++) {
            altCtx2.update(keyBytes);
        }
        byte[] tempResult = altCtx2.digest();
        byte[] pBytes = new byte[keyLen];
        int cp = 0;
        while (cp < keyLen - blocksize) {
            System.arraycopy(tempResult, 0, pBytes, cp, blocksize);
            cp += blocksize;
        }
        System.arraycopy(tempResult, 0, pBytes, cp, keyLen - cp);
        MessageDigest altCtx3 = DigestUtils.getDigest(algorithm);
        for (int i2 = 1; i2 <= (altResult2[0] & OnReminderListener.RET_FULL) + 16; i2++) {
            altCtx3.update(saltBytes);
        }
        byte[] tempResult2 = altCtx3.digest();
        byte[] sBytes = new byte[saltLen];
        int cp2 = 0;
        while (cp2 < saltLen - blocksize) {
            System.arraycopy(tempResult2, 0, sBytes, cp2, blocksize);
            cp2 += blocksize;
        }
        System.arraycopy(tempResult2, 0, sBytes, cp2, saltLen - cp2);
        for (int i3 = 0; i3 <= rounds - 1; i3++) {
            ctx = DigestUtils.getDigest(algorithm);
            if ((i3 & 1) != 0) {
                ctx.update(pBytes, 0, keyLen);
            } else {
                ctx.update(altResult2, 0, blocksize);
            }
            if (i3 % 3 != 0) {
                ctx.update(sBytes, 0, saltLen);
            }
            if (i3 % 7 != 0) {
                ctx.update(pBytes, 0, keyLen);
            }
            if ((i3 & 1) != 0) {
                ctx.update(altResult2, 0, blocksize);
            } else {
                ctx.update(pBytes, 0, keyLen);
            }
            altResult2 = ctx.digest();
        }
        StringBuilder buffer = new StringBuilder(saltPrefix);
        if (roundsCustom) {
            buffer.append(ROUNDS_PREFIX);
            buffer.append(rounds);
            buffer.append(SymbolExpUtil.SYMBOL_DOLLAR);
        }
        buffer.append(saltString);
        buffer.append(SymbolExpUtil.SYMBOL_DOLLAR);
        if (blocksize == 32) {
            B64.b64from24bit(altResult2[0], altResult2[10], altResult2[20], 4, buffer);
            B64.b64from24bit(altResult2[21], altResult2[1], altResult2[11], 4, buffer);
            B64.b64from24bit(altResult2[12], altResult2[22], altResult2[2], 4, buffer);
            B64.b64from24bit(altResult2[3], altResult2[13], altResult2[23], 4, buffer);
            B64.b64from24bit(altResult2[24], altResult2[4], altResult2[14], 4, buffer);
            B64.b64from24bit(altResult2[15], altResult2[25], altResult2[5], 4, buffer);
            B64.b64from24bit(altResult2[6], altResult2[16], altResult2[26], 4, buffer);
            B64.b64from24bit(altResult2[27], altResult2[7], altResult2[17], 4, buffer);
            B64.b64from24bit(altResult2[18], altResult2[28], altResult2[8], 4, buffer);
            B64.b64from24bit(altResult2[9], altResult2[19], altResult2[29], 4, buffer);
            B64.b64from24bit((byte) 0, altResult2[31], altResult2[30], 3, buffer);
        } else {
            B64.b64from24bit(altResult2[0], altResult2[21], altResult2[42], 4, buffer);
            B64.b64from24bit(altResult2[22], altResult2[43], altResult2[1], 4, buffer);
            B64.b64from24bit(altResult2[44], altResult2[2], altResult2[23], 4, buffer);
            B64.b64from24bit(altResult2[3], altResult2[24], altResult2[45], 4, buffer);
            B64.b64from24bit(altResult2[25], altResult2[46], altResult2[4], 4, buffer);
            B64.b64from24bit(altResult2[47], altResult2[5], altResult2[26], 4, buffer);
            B64.b64from24bit(altResult2[6], altResult2[27], altResult2[48], 4, buffer);
            B64.b64from24bit(altResult2[28], altResult2[49], altResult2[7], 4, buffer);
            B64.b64from24bit(altResult2[50], altResult2[8], altResult2[29], 4, buffer);
            B64.b64from24bit(altResult2[9], altResult2[30], altResult2[51], 4, buffer);
            B64.b64from24bit(altResult2[31], altResult2[52], altResult2[10], 4, buffer);
            B64.b64from24bit(altResult2[53], altResult2[11], altResult2[32], 4, buffer);
            B64.b64from24bit(altResult2[12], altResult2[33], altResult2[54], 4, buffer);
            B64.b64from24bit(altResult2[34], altResult2[55], altResult2[13], 4, buffer);
            B64.b64from24bit(altResult2[56], altResult2[14], altResult2[35], 4, buffer);
            B64.b64from24bit(altResult2[15], altResult2[36], altResult2[57], 4, buffer);
            B64.b64from24bit(altResult2[37], altResult2[58], altResult2[16], 4, buffer);
            B64.b64from24bit(altResult2[59], altResult2[17], altResult2[38], 4, buffer);
            B64.b64from24bit(altResult2[18], altResult2[39], altResult2[60], 4, buffer);
            B64.b64from24bit(altResult2[40], altResult2[61], altResult2[19], 4, buffer);
            B64.b64from24bit(altResult2[62], altResult2[20], altResult2[41], 4, buffer);
            B64.b64from24bit((byte) 0, (byte) 0, altResult2[63], 2, buffer);
        }
        Arrays.fill(tempResult2, (byte) 0);
        Arrays.fill(pBytes, (byte) 0);
        Arrays.fill(sBytes, (byte) 0);
        ctx.reset();
        altCtx3.reset();
        Arrays.fill(keyBytes, (byte) 0);
        Arrays.fill(saltBytes, (byte) 0);
        return buffer.toString();
    }

    public static String sha512Crypt(byte[] keyBytes) {
        return sha512Crypt(keyBytes, (String) null);
    }

    public static String sha512Crypt(byte[] keyBytes, String salt) {
        if (salt == null) {
            salt = SHA512_PREFIX + B64.getRandomSalt(8);
        }
        return sha2Crypt(keyBytes, salt, SHA512_PREFIX, 64, MessageDigestAlgorithms.SHA_512);
    }

    public static String sha512Crypt(byte[] keyBytes, String salt, Random random) {
        if (salt == null) {
            salt = SHA512_PREFIX + B64.getRandomSalt(8, random);
        }
        return sha2Crypt(keyBytes, salt, SHA512_PREFIX, 64, MessageDigestAlgorithms.SHA_512);
    }
}
