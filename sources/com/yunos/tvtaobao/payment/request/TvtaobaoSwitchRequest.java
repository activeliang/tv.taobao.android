package com.yunos.tvtaobao.payment.request;

import com.zhiping.dev.android.logger.ZpLogger;
import java.util.Arrays;
import mtopsdk.mtop.domain.MtopRequest;
import org.json.JSONException;
import org.json.JSONObject;

public class TvtaobaoSwitchRequest extends MtopRequest {
    private static final String API = "mtop.taobao.tvtao.bootstrapInitService.bootstrapInit";
    public static final String PLATFORM = "电视淘宝";
    public static final String SCORE_TYPE = "scoreSwitch";
    public static final String SOURCE = "tvtao";
    private static final String TAG = TvtaobaoSwitchRequest.class.getSimpleName();
    public static final String TYPE = "login2_3";
    public String API_VERSION = "1.0";

    public TvtaobaoSwitchRequest(boolean needGlobalData, String[] queryParams, String extParams) {
        setApiName(API);
        setVersion(this.API_VERSION);
        JSONObject data = new JSONObject();
        try {
            data.put("needGlobalData", String.valueOf(needGlobalData));
            data.put("queryParams", Arrays.toString(queryParams));
            data.put("extParams", extParams);
            ZpLogger.v(TAG, TAG + ".TvtaobaoSwitchRequest --> needGlobalData = " + needGlobalData + "--> queryParams = " + Arrays.toString(queryParams) + "--> extParams = " + extParams);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        setData(data.toString());
        setNeedEcode(false);
        setNeedSession(false);
    }
}
