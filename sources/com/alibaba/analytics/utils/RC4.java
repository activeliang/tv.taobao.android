package com.alibaba.analytics.utils;

public class RC4 {
    private static final String RC4_PK = "QrMgt8GGYI6T52ZY5AnhtxkLzb8egpFn3j5JELI8H6wtACbUnZ5cc3aYTsTRbmkAkRJeYbtx92LPBWm7nBO9UIl7y5i5MQNmUZNf5QENurR5tGyo7yJ2G0MBjWvy6iAtlAbacKP0SwOUeUWx5dsBdyhxa7Id1APtybSdDgicBDuNjI0mlZFUzZSS9dmN8lBD0WTVOMz0pRZbR3cysomRXOO1ghqjJdTcyDIxzpNAEszN8RMGjrzyU7Hjbmwi6YNK";

    private static class RC4Key {
        int[] state;
        int x;
        int y;

        private RC4Key() {
            this.state = new int[256];
        }
    }

    public static byte[] rc4(byte[] pData) {
        return rc4(pData, RC4_PK);
    }

    private static byte[] rc4(byte[] pData, String pPrivateKey) {
        RC4Key lRC4Key;
        if (pData == null || pPrivateKey == null || (lRC4Key = prepareKey(pPrivateKey)) == null) {
            return null;
        }
        return doRc4(pData, lRC4Key);
    }

    private static RC4Key prepareKey(String pPrivateKey) {
        if (pPrivateKey == null) {
            return null;
        }
        RC4Key lRC4Key = new RC4Key();
        for (int counter = 0; counter < 256; counter++) {
            lRC4Key.state[counter] = counter;
        }
        lRC4Key.x = 0;
        lRC4Key.y = 0;
        int index1 = 0;
        int index2 = 0;
        int counter2 = 0;
        while (counter2 < 256) {
            try {
                index2 = ((pPrivateKey.charAt(index1) + lRC4Key.state[counter2]) + index2) % 256;
                int swapInt = lRC4Key.state[counter2];
                lRC4Key.state[counter2] = lRC4Key.state[index2];
                lRC4Key.state[index2] = swapInt;
                index1 = (index1 + 1) % pPrivateKey.length();
                counter2++;
            } catch (Exception e) {
                return null;
            }
        }
        return lRC4Key;
    }

    private static byte[] doRc4(byte[] pData, RC4Key pKey) {
        if (pData == null || pKey == null) {
            return null;
        }
        int x = pKey.x;
        int y = pKey.y;
        for (int counter = 0; counter < pData.length; counter++) {
            x = (x + 1) % 256;
            y = (pKey.state[x] + y) % 256;
            int lSwapInt = pKey.state[x];
            pKey.state[x] = pKey.state[y];
            pKey.state[y] = lSwapInt;
            pData[counter] = (byte) (pData[counter] ^ pKey.state[(pKey.state[x] + pKey.state[y]) % 256]);
        }
        pKey.x = x;
        pKey.y = y;
        return pData;
    }
}
