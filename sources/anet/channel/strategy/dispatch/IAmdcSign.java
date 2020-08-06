package anet.channel.strategy.dispatch;

public interface IAmdcSign {
    String getAppkey();

    String sign(String str);

    boolean useSecurityGuard();
}
