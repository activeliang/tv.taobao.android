package anetwork.channel.aidl.adapter;

import android.content.Context;
import android.os.Handler;
import android.os.Looper;
import android.os.RemoteException;
import anet.channel.appmonitor.AppMonitor;
import anet.channel.statist.ExceptionStatistic;
import anet.channel.util.ALog;
import anetwork.channel.Network;
import anetwork.channel.NetworkListener;
import anetwork.channel.Request;
import anetwork.channel.Response;
import anetwork.channel.aidl.Connection;
import anetwork.channel.aidl.DefaultFinishEvent;
import anetwork.channel.aidl.IRemoteNetworkGetter;
import anetwork.channel.aidl.NetworkResponse;
import anetwork.channel.aidl.ParcelableFuture;
import anetwork.channel.aidl.ParcelableRequest;
import anetwork.channel.aidl.RemoteNetwork;
import anetwork.channel.config.NetworkConfigCenter;
import anetwork.channel.http.HttpNetworkDelegate;
import java.util.concurrent.Future;

public class NetworkProxy implements Network {
    protected static final int DEGRADE = 1;
    protected static final int HTTP = 0;
    protected static String TAG = "anet.NetworkProxy";
    private Context mContext;
    private RemoteNetwork mDelegate = null;
    private int mType = 0;

    protected NetworkProxy(Context context, int type) {
        this.mContext = context;
        this.mType = type;
    }

    public Connection getConnection(Request request, Object context) {
        initDelegateInstance(true);
        ParcelableRequest pRequest = new ParcelableRequest(request);
        if (pRequest.getURL() == null) {
            return new ConnectionDelegate(-102);
        }
        try {
            return this.mDelegate.getConnection(pRequest);
        } catch (Throwable e) {
            reportRemoteError(e, "[getConnection]call getConnection method failed.");
            return new ConnectionDelegate(-103);
        }
    }

    public Response syncSend(Request request, Object context) {
        initDelegateInstance(true);
        ParcelableRequest pRequest = new ParcelableRequest(request);
        if (pRequest.getURL() == null) {
            return new NetworkResponse(-102);
        }
        try {
            return this.mDelegate.syncSend(pRequest);
        } catch (Throwable e) {
            reportRemoteError(e, "[syncSend]call syncSend method failed.");
            return new NetworkResponse(-103);
        }
    }

    private void initDelegateInstance(boolean wait) {
        if (this.mDelegate == null) {
            if (NetworkConfigCenter.isRemoteNetworkServiceEnable()) {
                RemoteGetterHelper.initRemoteGetterAndWait(this.mContext, wait);
                this.mDelegate = tryGetRemoteNetworkInstance(this.mType);
            }
            if (this.mDelegate == null) {
                if (ALog.isPrintLog(2)) {
                    ALog.i(TAG, "[getLocalNetworkInstance]", (String) null, new Object[0]);
                }
                this.mDelegate = new HttpNetworkDelegate(this.mContext);
            }
        }
    }

    public Future<Response> asyncSend(Request request, Object context, Handler handler, NetworkListener listener) {
        initDelegateInstance(Looper.myLooper() != Looper.getMainLooper());
        FutureResponse response = new FutureResponse();
        ParcelableRequest pRequest = new ParcelableRequest(request);
        ParcelableNetworkListenerWrapper listenerWrapper = null;
        if (!(listener == null && handler == null)) {
            listenerWrapper = new ParcelableNetworkListenerWrapper(listener, handler, context);
        }
        response.setFuture(redirectAsyncCall(this.mDelegate, pRequest, listenerWrapper));
        return response;
    }

    private synchronized RemoteNetwork tryGetRemoteNetworkInstance(int type) {
        RemoteNetwork delegate;
        if (ALog.isPrintLog(2)) {
            ALog.i(TAG, "[tryGetRemoteNetworkInstance] type=" + type, (String) null, new Object[0]);
        }
        delegate = null;
        IRemoteNetworkGetter getter = RemoteGetterHelper.getRemoteGetter();
        if (getter != null) {
            try {
                delegate = getter.get(type);
            } catch (Throwable e) {
                reportRemoteError(e, "[tryGetRemoteNetworkInstance]get RemoteNetwork Delegate failed.");
            }
        }
        return delegate;
    }

    private ParcelableFuture redirectAsyncCall(RemoteNetwork network, ParcelableRequest pRequest, ParcelableNetworkListenerWrapper listenerWrapper) {
        if (network == null) {
            return null;
        }
        if (pRequest.getURL() == null) {
            return handleErrorCallBack(listenerWrapper, -102);
        }
        try {
            return network.asyncSend(pRequest, listenerWrapper);
        } catch (Throwable e) {
            ParcelableFuture future = handleErrorCallBack(listenerWrapper, -103);
            reportRemoteError(e, "[redirectAsyncCall]call asyncSend exception.");
            return future;
        }
    }

    private ParcelableFuture handleErrorCallBack(ParcelableNetworkListenerWrapper listenerWrapper, int code) {
        if (listenerWrapper != null) {
            try {
                listenerWrapper.onFinished(new DefaultFinishEvent(code));
            } catch (RemoteException e) {
                ALog.w(TAG, "[handleErrorCallBack]call listenerWrapper.onFinished exception.", (String) null, e, new Object[0]);
            }
        }
        return new ErrorParcelableFuture(code);
    }

    private void reportRemoteError(Throwable e, String eMsg) {
        ALog.e(TAG, (String) null, eMsg, e, new Object[0]);
        ExceptionStatistic es = new ExceptionStatistic(-103, (String) null, "rt");
        es.exceptionStack = e.toString();
        AppMonitor.getInstance().commitStat(es);
    }
}
