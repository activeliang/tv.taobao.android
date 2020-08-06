package mtopsdk.mtop.cache.handler;

import android.os.Handler;
import mtopsdk.common.util.TBSdkLog;
import mtopsdk.mtop.domain.ResponseSource;

public class EmptyCacheParser implements ICacheParser {
    private static final String TAG = "mtopsdk.EmptyCacheParser";

    public void parse(ResponseSource responseSource, Handler handler) {
        if (TBSdkLog.isLogEnable(TBSdkLog.LogEnable.InfoEnable)) {
            TBSdkLog.i(TAG, "[parse]EmptyCacheParser parse called");
        }
    }
}
