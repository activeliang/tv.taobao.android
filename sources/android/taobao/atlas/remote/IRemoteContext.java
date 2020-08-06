package android.taobao.atlas.remote;

public interface IRemoteContext {
    IRemote getHostTransactor();

    IRemote getRemoteTarget();

    String getTargetBundle();

    void registerHostTransactor(IRemote iRemote);
}
