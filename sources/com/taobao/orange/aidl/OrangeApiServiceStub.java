package com.taobao.orange.aidl;

import android.content.Context;
import android.os.RemoteException;
import com.taobao.orange.ConfigCenter;
import com.taobao.orange.GlobalOrange;
import com.taobao.orange.OCandidate;
import com.taobao.orange.OConfig;
import com.taobao.orange.aidl.IOrangeApiService;
import com.taobao.orange.util.OLog;
import java.util.Map;

public class OrangeApiServiceStub extends IOrangeApiService.Stub {
    private static final String TAG = "ApiService";
    private Context mContext;

    public OrangeApiServiceStub(Context context) {
        this.mContext = context.getApplicationContext();
    }

    public void init(OConfig config) {
        ConfigCenter.getInstance().init(this.mContext, config);
    }

    public Map<String, String> getConfigs(String groupName) throws RemoteException {
        return ConfigCenter.getInstance().getConfigs(groupName);
    }

    public void registerListener(String groupName, ParcelableConfigListener listener, boolean append) throws RemoteException {
        ConfigCenter.getInstance().registerListener(groupName, listener, append);
    }

    public void unregisterListener(String groupName, ParcelableConfigListener listener) throws RemoteException {
        ConfigCenter.getInstance().unregisterListener(groupName, listener);
    }

    public void unregisterListeners(String groupName) throws RemoteException {
        ConfigCenter.getInstance().unregisterListeners(groupName);
    }

    public void forceCheckUpdate() throws RemoteException {
        ConfigCenter.getInstance().forceCheckUpdate();
    }

    public void addFails(String[] namespaces) throws RemoteException {
        ConfigCenter.getInstance().addFails(namespaces);
    }

    public void setUserId(String userId) throws RemoteException {
        OLog.d(TAG, "setUserId", "userId", userId);
        GlobalOrange.userId = userId;
    }

    public void addCandidate(String key, String clientVal, ParcelableCandidateCompare compare) throws RemoteException {
        ConfigCenter.getInstance().addCandidate(new OCandidate(key, clientVal, compare));
    }
}
