package anet.channel.monitor;

public enum NetworkSpeed {
    Slow("弱网络", 1),
    Fast("强网络", 5);
    
    private final int code;
    private final String desc;

    private NetworkSpeed(String desc2, int code2) {
        this.desc = desc2;
        this.code = code2;
    }

    public String getDesc() {
        return this.desc;
    }

    public int getCode() {
        return this.code;
    }

    public static NetworkSpeed valueOfCode(int code2) {
        return code2 == 1 ? Slow : Fast;
    }
}
