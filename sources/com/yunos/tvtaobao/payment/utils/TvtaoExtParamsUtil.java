package com.yunos.tvtaobao.payment.utils;

public class TvtaoExtParamsUtil {
    private static ExtParamsInterface extParamsInterface;

    public interface ExtParamsInterface {
        String getExtParams();
    }

    public static ExtParamsInterface getExtParamsInterface() {
        return extParamsInterface;
    }

    public static void setExtParamsInterface(ExtParamsInterface extParamsInterface2) {
        extParamsInterface = extParamsInterface2;
    }
}
