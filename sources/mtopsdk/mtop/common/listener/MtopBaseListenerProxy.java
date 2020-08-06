package mtopsdk.mtop.common.listener;

import mtopsdk.common.util.TBSdkLog;
import mtopsdk.mtop.common.DefaultMtopCallback;
import mtopsdk.mtop.common.MtopCallback;
import mtopsdk.mtop.common.MtopFinishEvent;
import mtopsdk.mtop.common.MtopHeaderEvent;
import mtopsdk.mtop.common.MtopListener;
import mtopsdk.mtop.common.MtopProgressEvent;
import mtopsdk.mtop.domain.MtopResponse;

public class MtopBaseListenerProxy extends DefaultMtopCallback {
    private static final String TAG = "mtopsdk.MtopListenerProxy";
    protected boolean isCached = false;
    protected MtopListener listener = null;
    public Object reqContext = null;
    public MtopResponse response = null;

    public MtopBaseListenerProxy(MtopListener listener2) {
        this.listener = listener2;
    }

    public void onFinished(MtopFinishEvent event, Object context) {
        if (!(event == null || event.getMtopResponse() == null)) {
            this.response = event.getMtopResponse();
            this.reqContext = context;
        }
        synchronized (this) {
            try {
                notifyAll();
            } catch (Exception e) {
                TBSdkLog.e(TAG, "[onFinished] notify error");
            }
        }
        if (!(this.listener instanceof MtopCallback.MtopFinishListener)) {
            return;
        }
        if (!this.isCached || (this.response != null && this.response.isApiSuccess())) {
            ((MtopCallback.MtopFinishListener) this.listener).onFinished(event, context);
        }
    }

    public void onDataReceived(MtopProgressEvent event, Object context) {
        if (this.listener instanceof MtopCallback.MtopProgressListener) {
            ((MtopCallback.MtopProgressListener) this.listener).onDataReceived(event, context);
        }
    }

    public void onHeader(MtopHeaderEvent event, Object context) {
        if (this.listener instanceof MtopCallback.MtopHeaderListener) {
            ((MtopCallback.MtopHeaderListener) this.listener).onHeader(event, context);
        }
    }
}
