package android.taobao.atlas.remote.transactor;

import android.app.Activity;
import android.os.Bundle;
import android.taobao.atlas.bundleInfo.AtlasBundleInfoManager;
import android.taobao.atlas.framework.Atlas;
import android.taobao.atlas.remote.IRemote;
import android.taobao.atlas.remote.IRemoteContext;
import android.taobao.atlas.remote.IRemoteTransactor;
import android.taobao.atlas.remote.RemoteActivityManager;
import android.taobao.atlas.remote.Util;

public class RemoteTransactor implements IRemoteContext, IRemoteTransactor {
    private IRemote hostTransactor;
    private Activity remoteActivity;
    private String targetBundleName;
    private IRemote targetTransactor;

    public static RemoteTransactor crateRemoteTransactor(Activity activity, String key, String bundleName) throws Exception {
        RemoteTransactor remoteTransactor = new RemoteTransactor();
        remoteTransactor.targetBundleName = bundleName;
        if (activity != null) {
            remoteTransactor.remoteActivity = RemoteActivityManager.obtain(activity).getRemoteHost(remoteTransactor);
        }
        remoteTransactor.targetTransactor = (IRemote) Atlas.getInstance().getBundleClassLoader(bundleName).loadClass(AtlasBundleInfoManager.instance().getBundleInfo(bundleName).remoteTransactors.get(key)).newInstance();
        Util.findFieldFromInterface(remoteTransactor.targetTransactor, "remoteContext").set(remoteTransactor.targetTransactor, remoteTransactor);
        Util.findFieldFromInterface(remoteTransactor.targetTransactor, "realHost").set(remoteTransactor.targetTransactor, remoteTransactor.remoteActivity);
        return remoteTransactor;
    }

    public void registerHostTransactor(IRemote transactor) {
        this.hostTransactor = transactor;
    }

    public String getTargetBundle() {
        return this.targetBundleName;
    }

    public IRemote getRemoteTarget() {
        return this.targetTransactor;
    }

    public IRemote getHostTransactor() {
        return this.hostTransactor;
    }

    public Bundle call(String commandName, Bundle args, IRemoteTransactor.IResponse callback) {
        return this.targetTransactor.call(commandName, args, callback);
    }

    public <T> T getRemoteInterface(Class<T> interfaceClass, Bundle args) {
        return this.targetTransactor.getRemoteInterface(interfaceClass, args);
    }
}
