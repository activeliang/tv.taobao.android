package android.taobao.windvane.extra.jsbridge;

import android.content.Context;
import android.taobao.windvane.service.WVEventId;
import android.taobao.windvane.service.WVEventService;
import android.taobao.windvane.util.TaoLog;
import com.taobao.accs.base.TaoBaseService;

public class WVACCSService extends TaoBaseService {
    private static final String TAG = "CallbackService";
    private Context mContext = null;

    /* JADX WARNING: type inference failed for: r2v0, types: [android.content.Context, android.taobao.windvane.extra.jsbridge.WVACCSService, com.taobao.accs.base.TaoBaseService] */
    public void onCreate() {
        WVACCSService.super.onCreate();
        this.mContext = this;
        TaoLog.d(TAG, "onCreate");
    }

    public void onData(String serviceId, String userId, String dataId, byte[] data, TaoBaseService.ExtraInfo info) {
        if (TaoLog.getLogStatus()) {
            TaoLog.i(TAG, "serviceId : " + serviceId + " dataId :" + dataId);
        }
        WVEventService.getInstance().onEvent(WVEventId.ACCS_ONDATA, serviceId, data);
    }

    public void onBind(String serviceId, int errorCode, TaoBaseService.ExtraInfo info) {
        TaoLog.d(TAG, "onBind");
    }

    public void onUnbind(String serviceId, int errorCode, TaoBaseService.ExtraInfo info) {
        TaoLog.d(TAG, "onCreate");
    }

    public void onSendData(String serviceId, String dataId, int errorCode, TaoBaseService.ExtraInfo info) {
        TaoLog.d(TAG, "onSendData");
    }

    public void onResponse(String serviceId, String dataId, int errorCode, byte[] response, TaoBaseService.ExtraInfo info) {
        TaoLog.d(TAG, "onResponse");
    }

    public void onConnected(TaoBaseService.ConnectInfo conninfo) {
        WVEventService.getInstance().onEvent(WVEventId.ACCS_ONCONNECTED);
        TaoLog.d(TAG, "onConnected");
    }

    public void onDisconnected(TaoBaseService.ConnectInfo conninfo) {
        WVEventService.getInstance().onEvent(WVEventId.ACCS_ONDISONNECTED);
        TaoLog.d(TAG, "onDisconnected");
    }
}
