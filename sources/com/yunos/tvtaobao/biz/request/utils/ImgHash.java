package com.yunos.tvtaobao.biz.request.utils;

import android.text.TextUtils;

public class ImgHash {

    public enum EnvHosts {
        ALPHA("http://cube.alpha.elenet.me"),
        ALTA("http://cube.ar.elenet.me"),
        ALTB("http://cube.ar.elenet.me"),
        ALTC("http://cube.ar.elenet.me"),
        AR("http://cube.ar.elenet.me"),
        PPE("http://cube.elemecdn.com"),
        PRODUCTION("https://cube.elemecdn.com");
        
        String val;

        private EnvHosts(String val2) {
            this.val = val2;
        }
    }

    static String getImageHost() {
        return EnvHosts.PRODUCTION.val;
    }

    public static String buildUrlFromHash(String hash) {
        if (TextUtils.isEmpty(hash)) {
            return "";
        }
        if (hash.startsWith("http://") || hash.startsWith("https://")) {
            return hash;
        }
        if (hash.charAt(0) == '/') {
            hash = hash.substring(1);
        }
        String host = getImageHost();
        return host + '/' + hash.substring(0, 1) + '/' + hash.substring(1, 3) + '/' + hash.substring(3, hash.length()) + '.' + hash.substring(32, hash.length());
    }

    public static String buildUrlFromHash(String hash, int w, int h) {
        return buildUrlFromHash(hash) + "?x-oss-process=image/resize,m_fill,w_" + w + ",h_" + h + "/quality,q_100";
    }
}
