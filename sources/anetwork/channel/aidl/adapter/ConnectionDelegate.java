package anetwork.channel.aidl.adapter;

import android.os.Build;
import android.os.RemoteException;
import anet.channel.util.ErrorConstant;
import anetwork.channel.NetworkCallBack;
import anetwork.channel.NetworkEvent;
import anetwork.channel.aidl.Connection;
import anetwork.channel.aidl.ParcelableFuture;
import anetwork.channel.aidl.ParcelableInputStream;
import anetwork.channel.entity.RequestConfig;
import anetwork.channel.statist.StatisticData;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

public class ConnectionDelegate extends Connection.Stub implements NetworkCallBack.InputStreamListener, NetworkCallBack.FinishListener, NetworkCallBack.ResponseCodeListener {
    private RequestConfig config;
    private String desc;
    private ParcelableFuture future;
    private Map<String, List<String>> header;
    private ParcelableInputStreamImpl inputStream;
    private StatisticData statisticData;
    private int statusCode;
    private CountDownLatch statusLatch = new CountDownLatch(1);
    private CountDownLatch streamLatch = new CountDownLatch(1);

    public ConnectionDelegate(int statusCode2) {
        this.statusCode = statusCode2;
        this.desc = ErrorConstant.getErrMsg(statusCode2);
    }

    public ConnectionDelegate(RequestConfig config2) {
        this.config = config2;
    }

    public String getDesc() throws RemoteException {
        waitCountDownLatch(this.statusLatch);
        return this.desc;
    }

    public StatisticData getStatisticData() {
        return this.statisticData;
    }

    public ParcelableInputStream getInputStream() throws RemoteException {
        waitCountDownLatch(this.streamLatch);
        return this.inputStream;
    }

    public int getStatusCode() throws RemoteException {
        waitCountDownLatch(this.statusLatch);
        return this.statusCode;
    }

    public Map<String, List<String>> getConnHeadFields() throws RemoteException {
        waitCountDownLatch(this.statusLatch);
        return this.header;
    }

    public void cancel() throws RemoteException {
        if (this.future != null) {
            this.future.cancel(true);
        }
    }

    public void setFuture(ParcelableFuture future2) {
        this.future = future2;
    }

    public void onFinished(NetworkEvent.FinishEvent event, Object context) {
        this.statusCode = event.getHttpCode();
        this.desc = event.getDesc() != null ? event.getDesc() : ErrorConstant.getErrMsg(this.statusCode);
        this.statisticData = event.getStatisticData();
        if (this.inputStream != null) {
            this.inputStream.writeEnd();
        }
        this.streamLatch.countDown();
        this.statusLatch.countDown();
    }

    private void waitCountDownLatch(CountDownLatch latch) throws RemoteException {
        try {
            if (!latch.await((long) this.config.getWaitTimeout(), TimeUnit.MILLISECONDS)) {
                if (this.future != null) {
                    this.future.cancel(true);
                }
                throw buildRemoteException("wait time out");
            }
        } catch (InterruptedException e) {
            throw buildRemoteException("thread interrupt");
        }
    }

    private RemoteException buildRemoteException(String msg) {
        if (Build.VERSION.SDK_INT >= 15) {
            return new RemoteException(msg);
        }
        return new RemoteException();
    }

    public boolean onResponseCode(int code, Map<String, List<String>> header2, Object context) {
        this.statusCode = code;
        this.desc = ErrorConstant.getErrMsg(this.statusCode);
        this.header = header2;
        this.statusLatch.countDown();
        return false;
    }

    public void onInputStreamGet(ParcelableInputStream inputStream2, Object context) {
        this.inputStream = (ParcelableInputStreamImpl) inputStream2;
        this.streamLatch.countDown();
    }
}
