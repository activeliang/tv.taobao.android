package com.yunos.tvtaobao.biz.request.bo;

import java.io.Serializable;
import org.json.JSONException;
import org.json.JSONObject;

public class TakeOutOrderInfoDetails4Process implements Serializable {
    private static final long serialVersionUID = -610484326142980001L;
    private String processStatus;
    private boolean processed;
    private String time;

    public String getProcessStatus() {
        return this.processStatus;
    }

    public void setProcessStatus(String processStatus2) {
        this.processStatus = processStatus2;
    }

    public boolean isProcessed() {
        return this.processed;
    }

    public void setProcessed(boolean processed2) {
        this.processed = processed2;
    }

    public String getTime() {
        return this.time;
    }

    public void setTime(String time2) {
        this.time = time2;
    }

    public static TakeOutOrderInfoDetails4Process resolverFromMtop(JSONObject obj) throws JSONException {
        if (obj == null) {
            return null;
        }
        TakeOutOrderInfoDetails4Process details4Process = new TakeOutOrderInfoDetails4Process();
        if (obj == null) {
            return details4Process;
        }
        details4Process.setProcessStatus(obj.optString("processStatus"));
        details4Process.setProcessed(obj.optBoolean("processed", false));
        details4Process.setTime(obj.optString("time"));
        return details4Process;
    }
}
