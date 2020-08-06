package android.taobao.atlas.remote.view;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.taobao.atlas.bundleInfo.AtlasBundleInfoManager;
import android.taobao.atlas.remote.IRemote;
import android.taobao.atlas.remote.IRemoteContext;
import android.taobao.atlas.remote.IRemoteTransactor;
import android.taobao.atlas.remote.RemoteActivityManager;
import android.taobao.atlas.remote.Util;
import android.view.View;
import android.widget.FrameLayout;
import java.lang.reflect.Constructor;

public class RemoteView extends FrameLayout implements IRemoteTransactor, IRemoteContext {
    private IRemote hostTransactor;
    private String targetBundleName;
    private IRemote targetView;

    public static RemoteView createRemoteView(Activity activity, String remoteViewKey, String bundleName) throws Exception {
        RemoteView remoteView = new RemoteView(activity);
        remoteView.targetBundleName = bundleName;
        Activity remoteActivity = RemoteActivityManager.obtain(activity).getRemoteHost(remoteView);
        Constructor cons = remoteActivity.getClassLoader().loadClass(AtlasBundleInfoManager.instance().getBundleInfo(bundleName).remoteViews.get(remoteViewKey)).getDeclaredConstructor(new Class[]{Context.class});
        cons.setAccessible(true);
        remoteView.targetView = (IRemote) cons.newInstance(new Object[]{remoteActivity});
        Util.findFieldFromInterface(remoteView.targetView, "remoteContext").set(remoteView.targetView, remoteView);
        Util.findFieldFromInterface(remoteView.targetView, "realHost").set(remoteView.targetView, remoteActivity);
        remoteView.addView((View) remoteView.targetView);
        return remoteView;
    }

    public Bundle call(String commandName, Bundle args, IRemoteTransactor.IResponse callback) {
        if (this.targetView instanceof IRemote) {
            return this.targetView.call(commandName, args, callback);
        }
        throw new IllegalAccessError("targetView is not an implementation of : RemoteTransactor");
    }

    public <T> T getRemoteInterface(Class<T> interfaceClass, Bundle args) {
        if (this.targetView instanceof IRemote) {
            return this.targetView.getRemoteInterface(interfaceClass, args);
        }
        throw new IllegalAccessError("targetView is not an implementation of : RemoteTransactor");
    }

    public void registerHostTransactor(IRemote transactor) {
        this.hostTransactor = transactor;
    }

    public String getTargetBundle() {
        return this.targetBundleName;
    }

    public IRemote getRemoteTarget() {
        return this.targetView;
    }

    public IRemote getHostTransactor() {
        return this.hostTransactor;
    }

    public RemoteView(@NonNull Context context) {
        super(context);
    }
}
