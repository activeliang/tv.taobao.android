package com.taobao.alimama.utils;

import android.support.annotation.Keep;
import com.taobao.utils.Global;

@Keep
public final class EnvironmentUtils {

    public enum Env {
        UNKNOW(""),
        TAOBAO("com.taobao.taobao"),
        TMALL("com.tmall.wireless"),
        TAOBAO_LITE("com.taobao.htao.android"),
        IDLE_FISH("com.taobao.idlefish");
        
        public String PackageName;

        private Env(String str) {
            this.PackageName = str;
        }
    }

    private EnvironmentUtils() {
    }

    public static Env getCurrentEnv() {
        for (Env env : Env.values()) {
            if (env.PackageName.equals(Global.getPackageName())) {
                return env;
            }
        }
        return Env.UNKNOW;
    }

    public static boolean isInTaobao() {
        return getCurrentEnv() == Env.TAOBAO;
    }
}
