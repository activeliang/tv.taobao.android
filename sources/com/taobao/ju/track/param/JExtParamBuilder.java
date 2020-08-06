package com.taobao.ju.track.param;

import android.text.TextUtils;
import com.taobao.ju.track.JTrack;
import com.taobao.ju.track.constants.Constants;
import com.taobao.ju.track.impl.TrackImpl;
import java.util.Map;

public class JExtParamBuilder extends BaseParamBuilder {
    private static final String TAG = JExtParamBuilder.class.getSimpleName();

    private JExtParamBuilder() {
    }

    public static JExtParamBuilder make(Object rowKey) {
        return make(JTrack.Ext.getTrack(), String.valueOf(rowKey));
    }

    public static JExtParamBuilder make(TrackImpl track, Object rowKey) {
        return (JExtParamBuilder) makeInternal(track, new JExtParamBuilder(), rowKey);
    }

    public JExtParamBuilder add(String key, Object value) {
        if (this.mParams != null) {
            String str = String.valueOf(value);
            if (!TextUtils.isEmpty(str)) {
                if (!(value instanceof Number) || !Constants.isPosStartFromOne() || (!Constants.PARAM_POS.equals(key) && !Constants.PARAM_PAGER_POS.equals(key))) {
                    this.mParams.put(key, str);
                } else {
                    this.mParams.put(key, String.valueOf(((Number) value).longValue() + 1));
                }
            }
        }
        return this;
    }

    public JExtParamBuilder forceAdd(String key, Object value) {
        if (this.mParams != null) {
            this.mParams.put(key, String.valueOf(value));
        }
        return this;
    }

    public JExtParamBuilder add(Map<String, String> params) {
        if (params != null && params.size() > 0) {
            this.mParams.putAll(params);
        }
        return this;
    }
}
