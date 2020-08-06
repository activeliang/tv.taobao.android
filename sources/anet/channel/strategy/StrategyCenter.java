package anet.channel.strategy;

public class StrategyCenter {
    private static volatile IStrategyInstance instance = null;

    public static IStrategyInstance getInstance() {
        if (instance == null) {
            synchronized (StrategyCenter.class) {
                if (instance == null) {
                    instance = new StrategyInstance();
                }
            }
        }
        return instance;
    }

    public static void setInstance(IStrategyInstance instance2) {
        instance = instance2;
    }

    private StrategyCenter() {
    }
}
