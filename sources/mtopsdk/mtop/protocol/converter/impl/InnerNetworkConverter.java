package mtopsdk.mtop.protocol.converter.impl;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import mtopsdk.common.util.HttpHeaderConstant;
import mtopsdk.xstate.util.XStateConstants;

public class InnerNetworkConverter extends AbstractNetworkConverter {
    private static final String TAG = "mtopsdk.InnerNetworkConverter";
    private static final Map<String, String> headerConversionMap = new ConcurrentHashMap(32);

    static {
        headerConversionMap.put(HttpHeaderConstant.X_SID, "sid");
        headerConversionMap.put(HttpHeaderConstant.X_T, "t");
        headerConversionMap.put(HttpHeaderConstant.X_APPKEY, "appKey");
        headerConversionMap.put(HttpHeaderConstant.X_TTID, "ttid");
        headerConversionMap.put(HttpHeaderConstant.X_DEVID, "deviceId");
        headerConversionMap.put(HttpHeaderConstant.X_UTDID, "utdid");
        headerConversionMap.put(HttpHeaderConstant.X_SIGN, "sign");
        headerConversionMap.put(HttpHeaderConstant.X_NQ, XStateConstants.KEY_NQ);
        headerConversionMap.put(HttpHeaderConstant.X_NETTYPE, "netType");
        headerConversionMap.put("x-pv", XStateConstants.KEY_PV);
        headerConversionMap.put(HttpHeaderConstant.X_UID, XStateConstants.KEY_UID);
        headerConversionMap.put(HttpHeaderConstant.X_UMID_TOKEN, XStateConstants.KEY_UMID_TOKEN);
        headerConversionMap.put(HttpHeaderConstant.X_REQBIZ_EXT, XStateConstants.KEY_REQBIZ_EXT);
        headerConversionMap.put(HttpHeaderConstant.X_MINI_WUA, HttpHeaderConstant.X_MINI_WUA);
        headerConversionMap.put(HttpHeaderConstant.X_APP_CONF_V, HttpHeaderConstant.X_APP_CONF_V);
        headerConversionMap.put(HttpHeaderConstant.X_EXTTYPE, HttpHeaderConstant.KEY_EXTTYPE);
        headerConversionMap.put(HttpHeaderConstant.X_EXTDATA, "extdata");
        headerConversionMap.put("x-features", "x-features");
        headerConversionMap.put(HttpHeaderConstant.X_PAGE_NAME, HttpHeaderConstant.X_PAGE_NAME);
        headerConversionMap.put(HttpHeaderConstant.X_PAGE_URL, HttpHeaderConstant.X_PAGE_URL);
        headerConversionMap.put(HttpHeaderConstant.X_PAGE_MAB, HttpHeaderConstant.X_PAGE_MAB);
        headerConversionMap.put(HttpHeaderConstant.X_APP_VER, HttpHeaderConstant.X_APP_VER);
        headerConversionMap.put(HttpHeaderConstant.X_ORANGE_Q, HttpHeaderConstant.X_ORANGE_Q);
        headerConversionMap.put("user-agent", "user-agent");
        headerConversionMap.put(HttpHeaderConstant.CLIENT_TRACE_ID, HttpHeaderConstant.CLIENT_TRACE_ID);
        headerConversionMap.put("f-refer", "f-refer");
        headerConversionMap.put(HttpHeaderConstant.X_NETINFO, HttpHeaderConstant.X_NETINFO);
    }

    /* access modifiers changed from: protected */
    public Map<String, String> getHeaderConversionMap() {
        return headerConversionMap;
    }
}
