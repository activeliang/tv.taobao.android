package anetwork.channel.config;

public interface IRemoteConfig {
    String getConfig(String... strArr);

    void onConfigUpdate(String str);

    void register();

    void unRegister();
}
