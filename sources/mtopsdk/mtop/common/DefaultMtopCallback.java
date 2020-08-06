package mtopsdk.mtop.common;

import mtopsdk.common.util.TBSdkLog;
import mtopsdk.mtop.common.MtopCallback;

public class DefaultMtopCallback implements MtopCallback.MtopProgressListener, MtopCallback.MtopFinishListener, MtopCallback.MtopHeaderListener {
    private static final String TAG = "mtopsdk.DefaultMtopCallback";

    public void onFinished(MtopFinishEvent event, Object context) {
        if (event != null && event.getMtopResponse() != null && TBSdkLog.isLogEnable(TBSdkLog.LogEnable.DebugEnable)) {
            TBSdkLog.d(TAG, event.seqNo, "[onFinished]" + event.getMtopResponse().toString());
        }
    }

    public void onDataReceived(MtopProgressEvent event, Object context) {
        if (event != null && TBSdkLog.isLogEnable(TBSdkLog.LogEnable.DebugEnable)) {
            TBSdkLog.d(TAG, event.seqNo, "[onDataReceived]" + event.toString());
        }
    }

    public void onHeader(MtopHeaderEvent event, Object context) {
        if (event != null && TBSdkLog.isLogEnable(TBSdkLog.LogEnable.DebugEnable)) {
            TBSdkLog.d(TAG, event.seqNo, "[onHeader]" + event.toString());
        }
    }
}
