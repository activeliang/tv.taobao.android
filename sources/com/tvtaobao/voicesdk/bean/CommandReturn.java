package com.tvtaobao.voicesdk.bean;

import java.util.List;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class CommandReturn {
    public static final int TYPE_BUY_INDEX = 1004;
    public static final int TYPE_ERROR_INFO = 1008;
    public static final int TYPE_FEEDBACK_INFO = 1005;
    public static final int TYPE_FUSION_SEARCH = 1009;
    public static final int TYPE_GOODS_SHOW = 1002;
    public static final int TYPE_NOT_FOUND_APP = 1007;
    public static final int TYPE_SEE_INDEX = 1003;
    public static final int TYPE_TAKE_OUT_SEARCH = 1006;
    public static final int TYPE_TTS_PLAY = 1001;
    public static final int TYPE_UNKNOW = 1000;
    public String mASRMessage;
    public int mAction = 1000;
    public int mCode = 200;
    public String mData;
    public boolean mIsHandled = false;
    public String mMessage;
    public List<String> mTips;
    public double score;
    public boolean showUI = false;

    public String toString() {
        try {
            JSONObject object = new JSONObject();
            object.put("mIsHandled", this.mIsHandled);
            object.put("score", this.score);
            object.put("mASRMessage", this.mASRMessage);
            object.put("mData", this.mData);
            object.put("mAction", this.mAction);
            object.put("mMessage", this.mMessage);
            object.put("mCode", this.mCode);
            if (this.mTips != null) {
                JSONArray array = new JSONArray();
                for (int i = 0; i < this.mTips.size(); i++) {
                    array.put(this.mTips.get(i));
                }
                object.put("mTips", array);
            }
            return object.toString();
        } catch (JSONException e) {
            return "";
        }
    }
}
