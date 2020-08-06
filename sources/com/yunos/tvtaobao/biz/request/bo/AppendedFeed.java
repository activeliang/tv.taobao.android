package com.yunos.tvtaobao.biz.request.bo;

import android.text.TextUtils;
import java.io.Serializable;
import java.util.ArrayList;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class AppendedFeed implements Serializable {
    private static final long serialVersionUID = -8561464526519303091L;
    private ArrayList<String> appendFeedPicPathList;
    private String appendedFeedback;

    public String getAppendedFeedback() {
        return this.appendedFeedback;
    }

    public void setAppendedFeedback(String appendedFeedback2) {
        this.appendedFeedback = appendedFeedback2;
    }

    public ArrayList<String> getAppendFeedPicPathList() {
        return this.appendFeedPicPathList;
    }

    public void setAppendFeedPicPathList(ArrayList<String> appendFeedPicPathList2) {
        this.appendFeedPicPathList = appendFeedPicPathList2;
    }

    public static AppendedFeed resolveFromMTOP(JSONObject obj) throws JSONException {
        if (obj == null) {
            return null;
        }
        AppendedFeed appendedFeed = new AppendedFeed();
        if (!obj.isNull("appendedFeedback")) {
            appendedFeed.setAppendedFeedback(obj.getString("appendedFeedback"));
        }
        if (obj.isNull("appendFeedPicPathList")) {
            return appendedFeed;
        }
        JSONArray picArray = obj.getJSONArray("appendFeedPicPathList");
        ArrayList<String> picList = new ArrayList<>();
        if (picArray == null) {
            return appendedFeed;
        }
        for (int i = 0; i < picArray.length(); i++) {
            String pic_url = picArray.getString(i);
            if (!TextUtils.isEmpty(pic_url)) {
                picList.add(pic_url);
            }
        }
        appendedFeed.setAppendFeedPicPathList(picList);
        return appendedFeed;
    }
}
