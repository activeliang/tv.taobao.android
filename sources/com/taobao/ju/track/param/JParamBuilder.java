package com.taobao.ju.track.param;

import android.app.Activity;
import android.text.TextUtils;
import android.util.Log;
import com.taobao.ju.track.JTrack;
import com.taobao.ju.track.constants.Constants;
import com.taobao.ju.track.impl.CtrlTrackImpl;
import com.taobao.ju.track.spm.SpmUtil;
import java.util.Map;

public class JParamBuilder extends BaseParamBuilder {
    private static final String TAG = JParamBuilder.class.getSimpleName();

    private JParamBuilder() {
    }

    public static JParamBuilder make(Object rowKey) {
        return make(JTrack.Ctrl.getTrack(), (Object) String.valueOf(rowKey));
    }

    public static JParamBuilder make(Activity activity, Object rowKey) {
        return make(JTrack.Ctrl.getTrack(), activity, (Object) String.valueOf(rowKey));
    }

    public static JParamBuilder make(String pageNameOrActivityName, Object rowKey) {
        return make(JTrack.Ctrl.getTrack(), pageNameOrActivityName, (Object) String.valueOf(rowKey));
    }

    public static JParamBuilder make(CtrlTrackImpl track, Object rowKey) {
        JParamBuilder utCtrl = (JParamBuilder) makeInternal(track, new JParamBuilder(), rowKey);
        utCtrl.mParams.putAll(track.getParamSpm(utCtrl.mCSVRowName));
        return utCtrl;
    }

    public static JParamBuilder make(CtrlTrackImpl track, Activity activity, Object rowKey) {
        JParamBuilder utCtrl = (JParamBuilder) makeInternal(track, new JParamBuilder(), rowKey);
        utCtrl.mParams.putAll(track.getParamSpm(activity, utCtrl.mCSVRowName));
        return utCtrl;
    }

    public static JParamBuilder make(CtrlTrackImpl track, String pageNameOrActivityName, Object rowKey) {
        JParamBuilder utCtrl = (JParamBuilder) makeInternal(track, new JParamBuilder(), rowKey);
        utCtrl.mParams.putAll(track.getParamSpm(pageNameOrActivityName, utCtrl.mCSVRowName));
        return utCtrl;
    }

    public JParamBuilder add(String key, Object value) {
        if (this.mParams != null) {
            if (this.mParams.containsKey(key)) {
                String str = String.valueOf(value);
                if (!TextUtils.isEmpty(str)) {
                    if (!(value instanceof Number) || !Constants.isPosStartFromOne() || (!Constants.PARAM_POS.equals(key) && !Constants.PARAM_PAGER_POS.equals(key))) {
                        this.mParams.put(key, str);
                    } else {
                        this.mParams.put(key, String.valueOf(((Number) value).longValue() + 1));
                    }
                }
            } else {
                Log.e(TAG, "请先在 " + this.mCSVName + " 中配置，列_key为：" + this.mCSVRowName + "列" + key + "需要有值 否则打点会失败！");
            }
        }
        return this;
    }

    public JParamBuilder forceAdd(String key, Object value) {
        if (this.mParams != null) {
            this.mParams.put(key, String.valueOf(value));
        }
        return this;
    }

    public JParamBuilder add(Map<String, String> params) {
        if (params != null && params.size() > 0) {
            this.mParams.putAll(params);
        }
        return this;
    }

    public String getSpm() {
        return getParamValue(Constants.PARAM_OUTER_SPM_URL, Constants.PARAM_OUTER_SPM_NONE);
    }

    public JParamBuilder setAsPreSpm() {
        SpmUtil.setPreSpm(getSpm());
        return this;
    }
}
