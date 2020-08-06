package mtopsdk.mtop.common;

import mtopsdk.common.util.TBSdkLog;
import mtopsdk.mtop.common.MtopCallback;

public class DefaultMtopListener extends DefaultMtopCallback implements MtopCallback.MtopCacheListener {
    private static final String TAG = "mtopsdk.DefaultMtopListener";

    public void onCached(MtopCacheEvent event, Object context) {
        if (event != null && event.getMtopResponse() != null && TBSdkLog.isLogEnable(TBSdkLog.LogEnable.DebugEnable)) {
            TBSdkLog.d(TAG, event.seqNo, "[onCached]" + event.getMtopResponse().toString());
        }
    }
}
