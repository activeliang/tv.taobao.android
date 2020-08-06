package com.ut.mini.exposure;

import android.text.TextUtils;
import android.view.View;
import com.alibaba.motu.videoplayermonitor.VPMConstants;
import com.taobao.muniontaobaosdk.p4p.anticheat.model.ClientTraceData;
import java.util.Map;
import mtopsdk.common.util.SymbolExpUtil;

public class ExposureView {
    public static final int INITIAL = 0;
    public static final int SEEN = 1;
    public static final int UNSEEN = 2;
    public double area = ClientTraceData.b.f47a;
    public long beginTime = 0;
    public String block;
    public long endTime = 0;
    public long lastCalTime = 0;
    public int lastState = 0;
    public String tag;
    public View view;
    public Map<String, Object> viewData;

    public ExposureView(View view2) {
        this.view = view2;
    }

    public boolean equals(Object object) {
        if (this == object) {
            return true;
        }
        if (object == null || !(object instanceof ExposureView)) {
            return false;
        }
        return TextUtils.equals(this.tag, ((ExposureView) object).tag);
    }

    public int hashCode() {
        return this.tag.hashCode();
    }

    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(this.view.getClass().getSimpleName()).append(SymbolExpUtil.SYMBOL_COLON);
        sb.append(this.tag).append(SymbolExpUtil.SYMBOL_COLON);
        sb.append(TextUtils.isEmpty(this.view.getContentDescription()) ? "" : this.view.getContentDescription()).append(SymbolExpUtil.SYMBOL_COLON);
        sb.append(getState(this.lastState));
        return sb.toString();
    }

    private String getState(int lastState2) {
        if (lastState2 == 1) {
            return "可见";
        }
        if (lastState2 == 2) {
            return "不可见";
        }
        return "初始值";
    }

    /* access modifiers changed from: protected */
    public boolean isSatisfyTimeRequired() {
        long duration = System.currentTimeMillis() - this.beginTime;
        ExpLogger.d((String) null, "tag", this.tag, VPMConstants.MEASURE_DURATION, Long.valueOf(duration));
        if (duration <= ((long) ExposureConfigMgr.timeThreshold) || duration >= ((long) ExposureConfigMgr.maxTimeThreshold)) {
            return false;
        }
        return true;
    }
}
