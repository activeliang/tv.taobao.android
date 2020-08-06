package anetwork.channel.unified;

import android.content.Context;
import android.os.Handler;
import android.os.RemoteException;
import android.text.TextUtils;
import anet.channel.bytes.ByteArray;
import anet.channel.bytes.ByteArrayPool;
import anet.channel.util.ALog;
import anet.channel.util.ErrorConstant;
import anet.channel.util.StringUtils;
import anetwork.channel.Response;
import anetwork.channel.aidl.Connection;
import anetwork.channel.aidl.NetworkResponse;
import anetwork.channel.aidl.ParcelableFuture;
import anetwork.channel.aidl.ParcelableInputStream;
import anetwork.channel.aidl.ParcelableNetworkListener;
import anetwork.channel.aidl.ParcelableRequest;
import anetwork.channel.aidl.RemoteNetwork;
import anetwork.channel.aidl.adapter.ConnectionDelegate;
import anetwork.channel.aidl.adapter.ParcelableFutureResponse;
import anetwork.channel.aidl.adapter.ParcelableNetworkListenerWrapper;
import anetwork.channel.entity.Repeater;
import anetwork.channel.entity.RequestConfig;
import anetwork.channel.http.NetworkSdkSetting;
import java.io.ByteArrayOutputStream;
import java.util.concurrent.Future;

public abstract class UnifiedNetworkDelegate extends RemoteNetwork.Stub {
    public static final int DEGRADABLE = 1;
    public static final int HTTP = 0;
    private static final String TAG = "anet.UnifiedNetworkDelegate";
    protected int type = 1;

    protected UnifiedNetworkDelegate(Context context) {
        NetworkSdkSetting.init(context);
    }

    public NetworkResponse syncSend(ParcelableRequest request) throws RemoteException {
        return convertToSync(request);
    }

    public ParcelableFuture asyncSend(ParcelableRequest request, ParcelableNetworkListener listener) throws RemoteException {
        try {
            return asyncSend(new RequestConfig(request, this.type), listener);
        } catch (Exception e) {
            ALog.e(TAG, "asyncSend failed", request.getSeqNo(), e, new Object[0]);
            throw new RemoteException(e.getMessage());
        }
    }

    private ParcelableFuture asyncSend(RequestConfig config, ParcelableNetworkListener listener) throws RemoteException {
        return new ParcelableFutureResponse((Future<Response>) new UnifiedRequestTask(config, new Repeater(listener, config)).request());
    }

    public Connection getConnection(ParcelableRequest request) throws RemoteException {
        try {
            RequestConfig config = new RequestConfig(request, this.type);
            ConnectionDelegate connDelegate = new ConnectionDelegate(config);
            connDelegate.setFuture(asyncSend(config, (ParcelableNetworkListener) new ParcelableNetworkListenerWrapper(connDelegate, (Handler) null, (Object) null)));
            return connDelegate;
        } catch (Exception e) {
            ALog.e(TAG, "asyncSend failed", request.getSeqNo(), e, new Object[0]);
            throw new RemoteException(e.getMessage());
        }
    }

    private NetworkResponse convertToSync(ParcelableRequest request) {
        NetworkResponse networkResponse = new NetworkResponse();
        try {
            ConnectionDelegate connDelegate = (ConnectionDelegate) getConnection(request);
            networkResponse.setStatusCode(connDelegate.getStatusCode());
            networkResponse.setConnHeadFields(connDelegate.getConnHeadFields());
            ParcelableInputStream is = connDelegate.getInputStream();
            if (is != null) {
                ByteArrayOutputStream bos = new ByteArrayOutputStream(connDelegate.getInputStream().length());
                ByteArray byteArray = ByteArrayPool.getInstance().retrieve(2048);
                while (true) {
                    int count = is.read(byteArray.getBuffer());
                    if (count == -1) {
                        break;
                    }
                    bos.write(byteArray.getBuffer(), 0, count);
                }
                networkResponse.setBytedata(bos.toByteArray());
            }
            networkResponse.setStatisticData(connDelegate.getStatisticData());
        } catch (RemoteException e) {
            networkResponse.setStatusCode(-103);
            String errMsg = e.getMessage();
            if (!TextUtils.isEmpty(errMsg)) {
                networkResponse.setDesc(StringUtils.concatString(networkResponse.getDesc(), "|", errMsg));
            }
        } catch (Exception e2) {
            networkResponse.setStatusCode(ErrorConstant.ERROR_REQUEST_FAIL);
        }
        return networkResponse;
    }
}
