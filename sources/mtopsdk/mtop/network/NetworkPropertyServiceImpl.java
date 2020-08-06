package mtopsdk.mtop.network;

import anet.channel.GlobalAppRuntimeInfo;
import mtopsdk.common.util.TBSdkLog;

public class NetworkPropertyServiceImpl implements NetworkPropertyService {
    private static final String TAG = "mtopsdk.NetworkPropertyServiceImpl";

    public void setUserId(String userId) {
        if (TBSdkLog.isLogEnable(TBSdkLog.LogEnable.InfoEnable)) {
            TBSdkLog.i(TAG, "[setUserId] set NetworkProperty UserId =" + userId);
        }
        GlobalAppRuntimeInfo.setUserId(userId);
    }

    public void setTtid(String ttid) {
        if (TBSdkLog.isLogEnable(TBSdkLog.LogEnable.InfoEnable)) {
            TBSdkLog.i(TAG, "[setTtid] set NetworkProperty ttid =" + ttid);
        }
        GlobalAppRuntimeInfo.setTtid(ttid);
    }
}
