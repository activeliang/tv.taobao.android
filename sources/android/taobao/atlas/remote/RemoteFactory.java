package android.taobao.atlas.remote;

import android.app.Activity;
import android.content.Intent;
import android.taobao.atlas.bundleInfo.AtlasBundleInfoManager;
import android.taobao.atlas.remote.fragment.RemoteFragment;
import android.taobao.atlas.remote.transactor.RemoteTransactor;
import android.taobao.atlas.remote.view.RemoteView;
import android.taobao.atlas.runtime.BundleUtil;
import android.text.TextUtils;

public class RemoteFactory {

    public interface OnRemoteStateListener<T extends IRemoteContext> {
        void onFailed(String str);

        void onRemotePrepared(T t);
    }

    public static <T extends IRemoteContext> void requestRemote(Class<T> remoteClass, Activity activity, Intent intent, final OnRemoteStateListener listener) {
        final String key;
        if (intent.getComponent() != null) {
            key = intent.getComponent().getClassName();
        } else {
            key = intent.getAction();
        }
        String tempBundleName = null;
        if (remoteClass == RemoteView.class) {
            tempBundleName = AtlasBundleInfoManager.instance().getBundleForRemoteView(key);
        } else if (remoteClass == RemoteTransactor.class) {
            tempBundleName = AtlasBundleInfoManager.instance().getBundleForRemoteTransactor(key);
        } else if (remoteClass == RemoteFragment.class) {
            tempBundleName = AtlasBundleInfoManager.instance().getBundleForRemoteFragment(key);
        }
        final String bundleName = tempBundleName;
        if (TextUtils.isEmpty(bundleName)) {
            listener.onFailed("no match remote-item with intent : " + intent);
        }
        final Class<T> cls = remoteClass;
        final Activity activity2 = activity;
        final OnRemoteStateListener onRemoteStateListener = listener;
        BundleUtil.checkBundleStateAsync(bundleName, new Runnable() {
            public void run() {
                String th;
                try {
                    if (cls == RemoteView.class) {
                        onRemoteStateListener.onRemotePrepared(RemoteView.createRemoteView(activity2, key, bundleName));
                    } else if (cls == RemoteTransactor.class) {
                        onRemoteStateListener.onRemotePrepared(RemoteTransactor.crateRemoteTransactor(activity2, key, bundleName));
                    } else if (cls == RemoteFragment.class) {
                        onRemoteStateListener.onRemotePrepared(RemoteFragment.createRemoteFragment(activity2, key, bundleName));
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                    OnRemoteStateListener onRemoteStateListener = onRemoteStateListener;
                    if (e.getCause() == null) {
                        th = e.toString();
                    } else {
                        th = e.getCause().toString();
                    }
                    onRemoteStateListener.onFailed(th);
                }
            }
        }, new Runnable() {
            public void run() {
                listener.onFailed("install bundle failed: " + bundleName);
            }
        });
    }
}
