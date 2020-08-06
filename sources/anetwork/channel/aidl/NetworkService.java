package anetwork.channel.aidl;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.IBinder;
import android.os.RemoteException;
import anet.channel.util.ALog;
import anetwork.channel.aidl.IRemoteNetworkGetter;
import anetwork.channel.aidl.RemoteNetwork;
import anetwork.channel.degrade.DegradableNetworkDelegate;
import anetwork.channel.http.HttpNetworkDelegate;

public class NetworkService extends Service {
    private static final String TAG = "anet.NetworkService";
    /* access modifiers changed from: private */
    public Context context;
    /* access modifiers changed from: private */
    public RemoteNetwork.Stub[] networkDelegates = new RemoteNetwork.Stub[2];
    IRemoteNetworkGetter.Stub stub = new IRemoteNetworkGetter.Stub() {
        public RemoteNetwork get(int type) throws RemoteException {
            if (NetworkService.this.networkDelegates[type] == null) {
                switch (type) {
                    case 1:
                        NetworkService.this.networkDelegates[type] = new DegradableNetworkDelegate(NetworkService.this.context);
                        break;
                    default:
                        NetworkService.this.networkDelegates[type] = new HttpNetworkDelegate(NetworkService.this.context);
                        break;
                }
            }
            return NetworkService.this.networkDelegates[type];
        }
    };

    public IBinder onBind(Intent intent) {
        this.context = getApplicationContext();
        if (ALog.isPrintLog(2)) {
            ALog.i(TAG, "onBind:" + intent.getAction(), (String) null, new Object[0]);
        }
        if (IRemoteNetworkGetter.class.getName().equals(intent.getAction())) {
            return this.stub;
        }
        return null;
    }

    public int onStartCommand(Intent intent, int flags, int startId) {
        return 2;
    }

    public void onDestroy() {
    }
}
