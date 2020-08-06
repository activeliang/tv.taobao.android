package mtopsdk.mtop.domain;

public enum EnvModeEnum {
    ONLINE(0),
    PREPARE(1),
    TEST(2),
    TEST_SANDBOX(3);
    
    private int envMode;

    public int getEnvMode() {
        return this.envMode;
    }

    private EnvModeEnum(int envMode2) {
        this.envMode = envMode2;
    }
}
