package mtopsdk.mtop.network;

import android.support.annotation.NonNull;
import mtopsdk.common.util.TBSdkLog;
import mtopsdk.framework.domain.MtopContext;
import mtopsdk.framework.manager.FilterManager;
import mtopsdk.framework.util.FilterUtils;
import mtopsdk.mtop.common.MtopCallback;
import mtopsdk.mtop.common.MtopHeaderEvent;
import mtopsdk.mtop.common.MtopListener;
import mtopsdk.network.Call;
import mtopsdk.network.NetworkCallback;
import mtopsdk.network.domain.Response;

public class NetworkCallbackAdapter implements NetworkCallback {
    private static final String TAG = "mtopsdk.NetworkCallbackAdapter";
    FilterManager filterManager;
    public MtopCallback.MtopFinishListener finishListener;
    public MtopCallback.MtopHeaderListener headerListener;
    final MtopContext mtopContext;

    public NetworkCallbackAdapter(@NonNull MtopContext mtopContext2) {
        this.mtopContext = mtopContext2;
        if (mtopContext2 != null) {
            if (mtopContext2.mtopInstance != null) {
                this.filterManager = mtopContext2.mtopInstance.getMtopConfig().filterManager;
            }
            MtopListener mtopListener = mtopContext2.mtopListener;
            if (mtopListener instanceof MtopCallback.MtopHeaderListener) {
                this.headerListener = (MtopCallback.MtopHeaderListener) mtopListener;
            }
            if (mtopListener instanceof MtopCallback.MtopFinishListener) {
                this.finishListener = (MtopCallback.MtopFinishListener) mtopListener;
            }
        }
    }

    public void onHeader(final Response response, final Object context) {
        FilterUtils.submitCallbackTask(this.mtopContext.property.handler, new Runnable() {
            public void run() {
                try {
                    if (NetworkCallbackAdapter.this.headerListener != null) {
                        MtopHeaderEvent event = new MtopHeaderEvent(response.code, response.headers);
                        event.seqNo = NetworkCallbackAdapter.this.mtopContext.seqNo;
                        NetworkCallbackAdapter.this.headerListener.onHeader(event, context);
                    }
                } catch (Throwable e) {
                    TBSdkLog.e(NetworkCallbackAdapter.TAG, NetworkCallbackAdapter.this.mtopContext.seqNo, "onHeader failed.", e);
                }
            }
        }, this.mtopContext.seqNo.hashCode());
    }

    public void onFinish(final Response response, Object context) {
        this.mtopContext.stats.netSendEndTime = this.mtopContext.stats.currentTimeMillis();
        this.mtopContext.property.reqContext = context;
        FilterUtils.submitCallbackTask(this.mtopContext.property.handler, new Runnable() {
            /* JADX WARNING: No exception handlers in catch block: Catch:{  } */
            /* Code decompiled incorrectly, please refer to instructions dump. */
            public void run() {
                /*
                    r6 = this;
                    mtopsdk.mtop.network.NetworkCallbackAdapter r2 = mtopsdk.mtop.network.NetworkCallbackAdapter.this     // Catch:{ Throwable -> 0x008d }
                    mtopsdk.framework.domain.MtopContext r2 = r2.mtopContext     // Catch:{ Throwable -> 0x008d }
                    mtopsdk.mtop.util.MtopStatistics r2 = r2.stats     // Catch:{ Throwable -> 0x008d }
                    mtopsdk.mtop.network.NetworkCallbackAdapter r3 = mtopsdk.mtop.network.NetworkCallbackAdapter.this     // Catch:{ Throwable -> 0x008d }
                    mtopsdk.framework.domain.MtopContext r3 = r3.mtopContext     // Catch:{ Throwable -> 0x008d }
                    mtopsdk.mtop.util.MtopStatistics r3 = r3.stats     // Catch:{ Throwable -> 0x008d }
                    long r4 = r3.currentTimeMillis()     // Catch:{ Throwable -> 0x008d }
                    r2.startCallbackTime = r4     // Catch:{ Throwable -> 0x008d }
                    mtopsdk.mtop.network.NetworkCallbackAdapter r2 = mtopsdk.mtop.network.NetworkCallbackAdapter.this     // Catch:{ Throwable -> 0x008d }
                    mtopsdk.framework.domain.MtopContext r2 = r2.mtopContext     // Catch:{ Throwable -> 0x008d }
                    mtopsdk.mtop.util.MtopStatistics r2 = r2.stats     // Catch:{ Throwable -> 0x008d }
                    mtopsdk.network.domain.Response r3 = r5     // Catch:{ Throwable -> 0x008d }
                    mtopsdk.network.domain.NetworkStats r3 = r3.stat     // Catch:{ Throwable -> 0x008d }
                    r2.netStats = r3     // Catch:{ Throwable -> 0x008d }
                    mtopsdk.mtop.network.NetworkCallbackAdapter r2 = mtopsdk.mtop.network.NetworkCallbackAdapter.this     // Catch:{ Throwable -> 0x008d }
                    mtopsdk.framework.domain.MtopContext r2 = r2.mtopContext     // Catch:{ Throwable -> 0x008d }
                    mtopsdk.network.domain.Response r3 = r5     // Catch:{ Throwable -> 0x008d }
                    r2.networkResponse = r3     // Catch:{ Throwable -> 0x008d }
                    mtopsdk.mtop.domain.MtopResponse r1 = new mtopsdk.mtop.domain.MtopResponse     // Catch:{ Throwable -> 0x008d }
                    mtopsdk.mtop.network.NetworkCallbackAdapter r2 = mtopsdk.mtop.network.NetworkCallbackAdapter.this     // Catch:{ Throwable -> 0x008d }
                    mtopsdk.framework.domain.MtopContext r2 = r2.mtopContext     // Catch:{ Throwable -> 0x008d }
                    mtopsdk.mtop.domain.MtopRequest r2 = r2.mtopRequest     // Catch:{ Throwable -> 0x008d }
                    java.lang.String r2 = r2.getApiName()     // Catch:{ Throwable -> 0x008d }
                    mtopsdk.mtop.network.NetworkCallbackAdapter r3 = mtopsdk.mtop.network.NetworkCallbackAdapter.this     // Catch:{ Throwable -> 0x008d }
                    mtopsdk.framework.domain.MtopContext r3 = r3.mtopContext     // Catch:{ Throwable -> 0x008d }
                    mtopsdk.mtop.domain.MtopRequest r3 = r3.mtopRequest     // Catch:{ Throwable -> 0x008d }
                    java.lang.String r3 = r3.getVersion()     // Catch:{ Throwable -> 0x008d }
                    r4 = 0
                    r5 = 0
                    r1.<init>(r2, r3, r4, r5)     // Catch:{ Throwable -> 0x008d }
                    mtopsdk.network.domain.Response r2 = r5     // Catch:{ Throwable -> 0x008d }
                    int r2 = r2.code     // Catch:{ Throwable -> 0x008d }
                    r1.setResponseCode(r2)     // Catch:{ Throwable -> 0x008d }
                    mtopsdk.network.domain.Response r2 = r5     // Catch:{ Throwable -> 0x008d }
                    java.util.Map<java.lang.String, java.util.List<java.lang.String>> r2 = r2.headers     // Catch:{ Throwable -> 0x008d }
                    r1.setHeaderFields(r2)     // Catch:{ Throwable -> 0x008d }
                    mtopsdk.mtop.network.NetworkCallbackAdapter r2 = mtopsdk.mtop.network.NetworkCallbackAdapter.this     // Catch:{ Throwable -> 0x008d }
                    mtopsdk.framework.domain.MtopContext r2 = r2.mtopContext     // Catch:{ Throwable -> 0x008d }
                    mtopsdk.mtop.util.MtopStatistics r2 = r2.stats     // Catch:{ Throwable -> 0x008d }
                    r1.setMtopStat(r2)     // Catch:{ Throwable -> 0x008d }
                    mtopsdk.network.domain.Response r2 = r5     // Catch:{ Throwable -> 0x008d }
                    mtopsdk.network.domain.ResponseBody r2 = r2.body     // Catch:{ Throwable -> 0x008d }
                    if (r2 == 0) goto L_0x0069
                    mtopsdk.network.domain.Response r2 = r5     // Catch:{ IOException -> 0x007c }
                    mtopsdk.network.domain.ResponseBody r2 = r2.body     // Catch:{ IOException -> 0x007c }
                    byte[] r2 = r2.getBytes()     // Catch:{ IOException -> 0x007c }
                    r1.setBytedata(r2)     // Catch:{ IOException -> 0x007c }
                L_0x0069:
                    mtopsdk.mtop.network.NetworkCallbackAdapter r2 = mtopsdk.mtop.network.NetworkCallbackAdapter.this     // Catch:{ Throwable -> 0x008d }
                    mtopsdk.framework.domain.MtopContext r2 = r2.mtopContext     // Catch:{ Throwable -> 0x008d }
                    r2.mtopResponse = r1     // Catch:{ Throwable -> 0x008d }
                    mtopsdk.mtop.network.NetworkCallbackAdapter r2 = mtopsdk.mtop.network.NetworkCallbackAdapter.this     // Catch:{ Throwable -> 0x008d }
                    mtopsdk.framework.manager.FilterManager r2 = r2.filterManager     // Catch:{ Throwable -> 0x008d }
                    r3 = 0
                    mtopsdk.mtop.network.NetworkCallbackAdapter r4 = mtopsdk.mtop.network.NetworkCallbackAdapter.this     // Catch:{ Throwable -> 0x008d }
                    mtopsdk.framework.domain.MtopContext r4 = r4.mtopContext     // Catch:{ Throwable -> 0x008d }
                    r2.callback(r3, r4)     // Catch:{ Throwable -> 0x008d }
                L_0x007b:
                    return
                L_0x007c:
                    r0 = move-exception
                    java.lang.String r2 = "mtopsdk.NetworkCallbackAdapter"
                    mtopsdk.mtop.network.NetworkCallbackAdapter r3 = mtopsdk.mtop.network.NetworkCallbackAdapter.this     // Catch:{ Throwable -> 0x008d }
                    mtopsdk.framework.domain.MtopContext r3 = r3.mtopContext     // Catch:{ Throwable -> 0x008d }
                    java.lang.String r3 = r3.seqNo     // Catch:{ Throwable -> 0x008d }
                    java.lang.String r4 = "call getBytes of response.body() error."
                    mtopsdk.common.util.TBSdkLog.e(r2, r3, r4, r0)     // Catch:{ Throwable -> 0x008d }
                    goto L_0x0069
                L_0x008d:
                    r0 = move-exception
                    java.lang.String r2 = "mtopsdk.NetworkCallbackAdapter"
                    mtopsdk.mtop.network.NetworkCallbackAdapter r3 = mtopsdk.mtop.network.NetworkCallbackAdapter.this
                    mtopsdk.framework.domain.MtopContext r3 = r3.mtopContext
                    java.lang.String r3 = r3.seqNo
                    java.lang.String r4 = "onFinish failed."
                    mtopsdk.common.util.TBSdkLog.e(r2, r3, r4, r0)
                    goto L_0x007b
                */
                throw new UnsupportedOperationException("Method not decompiled: mtopsdk.mtop.network.NetworkCallbackAdapter.AnonymousClass2.run():void");
            }
        }, this.mtopContext.seqNo.hashCode());
    }

    public void onFailure(Call call, Exception e) {
        Response response = new Response.Builder().request(call.request()).code(-7).message(e.getMessage()).build();
        onFinish(response, response.request.reqContext);
    }

    public void onResponse(Call call, Response response) {
        onHeader(response, response.request.reqContext);
        onFinish(response, response.request.reqContext);
    }

    public void onCancel(Call call) {
        Response response = new Response.Builder().request(call.request()).code(-8).build();
        onFinish(response, response.request.reqContext);
    }
}
