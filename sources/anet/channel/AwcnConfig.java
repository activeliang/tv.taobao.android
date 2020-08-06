package anet.channel;

public class AwcnConfig {
    private static boolean isAccsSessionCreateForbiddenInBg = true;
    private static boolean isHttpsSniEnable = true;

    public static boolean isAccsSessionCreateForbiddenInBg() {
        return isAccsSessionCreateForbiddenInBg;
    }

    public static void setAccsSessionCreateForbiddenInBg(boolean b) {
        isAccsSessionCreateForbiddenInBg = b;
    }

    public static void setHttpsSniEnable(boolean enable) {
        isHttpsSniEnable = enable;
    }

    public static boolean isHttpsSniEnable() {
        return isHttpsSniEnable;
    }
}
