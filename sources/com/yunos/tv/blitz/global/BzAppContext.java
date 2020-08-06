package com.yunos.tv.blitz.global;

public class BzAppContext {
    public static void setBzAppParams(BzAppParams params) {
        BzAppConfig.getInstance().initParams(params);
    }

    public static void setEnvMode(BzEnvEnum env) {
        if (env != null) {
            BzAppConfig.env = env;
        }
    }
}
