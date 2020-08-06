package android.taobao.atlas.remote;

import android.app.Activity;
import android.os.Bundle;
import android.taobao.atlas.remote.IRemoteTransactor;
import android.util.Log;
import java.util.HashMap;

public class HostTransactor implements IRemoteTransactor {
    private static HashMap<IRemote, HostTransactor> sHostTransactors = new HashMap<>();
    private final Activity embeddedActivity;
    private final IRemote host;

    public static HostTransactor get(IRemote remoteItem) {
        if (sHostTransactors.containsKey(remoteItem)) {
            return sHostTransactors.get(remoteItem);
        }
        IRemoteContext context = IRemote.remoteContext;
        if (context.getHostTransactor() == null) {
            Log.e("HostTransactor", "no host-transactor,maybe has not been registered");
        }
        HostTransactor transactor = new HostTransactor(context.getHostTransactor(), IRemote.realHost);
        sHostTransactors.put(remoteItem, transactor);
        return transactor;
    }

    private HostTransactor(IRemote remote, Activity activity) {
        this.host = remote;
        this.embeddedActivity = activity;
    }

    public Bundle call(String commandName, Bundle args, IRemoteTransactor.IResponse callback) {
        if (this.host != null) {
            return this.host.call(commandName, args, callback);
        }
        Log.e("HostTransactor", "no real transactor");
        return null;
    }

    public <T> T getRemoteInterface(Class<T> interfaceClass, Bundle args) {
        if (this.host != null) {
            return this.host.getRemoteInterface(interfaceClass, args);
        }
        Log.e("HostTransactor", "no real transactor");
        return null;
    }

    public Activity getDelegateActivity() {
        return this.embeddedActivity;
    }
}
