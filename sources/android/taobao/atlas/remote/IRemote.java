package android.taobao.atlas.remote;

import android.taobao.atlas.remote.RemoteActivityManager;

public interface IRemote extends IRemoteTransactor {
    public static final RemoteActivityManager.EmbeddedActivity realHost = null;
    public static final IRemoteContext remoteContext = null;
}
