package anet.channel.entity;

public enum ENV {
    ONLINE(0),
    PREPARE(1),
    TEST(2);
    
    private int envMode;

    public int getEnvMode() {
        return this.envMode;
    }

    public void setEnvMode(int envMode2) {
        this.envMode = envMode2;
    }

    public static ENV valueOf(int envMode2) {
        switch (envMode2) {
            case 1:
                return PREPARE;
            case 2:
                return TEST;
            default:
                return ONLINE;
        }
    }

    private ENV(int envMode2) {
        this.envMode = envMode2;
    }
}
