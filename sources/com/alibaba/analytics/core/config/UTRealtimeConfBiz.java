package com.alibaba.analytics.core.config;

import android.text.TextUtils;
import com.alibaba.analytics.core.Variables;
import com.alibaba.analytics.core.model.LogField;
import com.alibaba.analytics.utils.Logger;
import com.alibaba.analytics.utils.StringUtils;
import com.ta.audid.Constants;
import com.ut.device.UTDevice;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import org.json.JSONObject;

public class UTRealtimeConfBiz extends UTOrangeConfBiz {
    private static final int DefaultEffectiveTime = 10;
    private static UTRealtimeConfBiz s_instance = null;
    private int mEffectiveTime = 10;
    private int mHashcode = -1;
    private int mSample = 0;
    private Map<String, UTTopicItem> mTopicItemMap = new HashMap();

    public static UTRealtimeConfBiz getInstance() {
        if (s_instance == null) {
            s_instance = new UTRealtimeConfBiz();
        }
        return s_instance;
    }

    private UTRealtimeConfBiz() {
    }

    public boolean isRealtimeClosed() {
        if (Variables.getInstance().isRealtimeServiceClosed() || Variables.getInstance().isHttpService() || Variables.getInstance().isAllServiceClosed()) {
            return true;
        }
        return false;
    }

    public boolean isRealtimeLogSampled() {
        if (isRealtimeClosed()) {
            return false;
        }
        if (Variables.getInstance().getDebugSamplingOption()) {
            return true;
        }
        if (this.mHashcode == -1) {
            String utdid = UTDevice.getUtdid(Variables.getInstance().getContext());
            if (utdid == null || utdid.equals(Constants.UTDID_INVALID)) {
                return false;
            }
            this.mHashcode = Math.abs(StringUtils.hashCode(utdid));
        }
        Logger.d("", "hashcode", Integer.valueOf(this.mHashcode), "sample", Integer.valueOf(this.mSample));
        if (this.mHashcode % 10000 < this.mSample) {
            return true;
        }
        return false;
    }

    public int getEffectiveTime() {
        return this.mEffectiveTime;
    }

    public void resetRealtimeConf() {
        this.mTopicItemMap.clear();
        this.mEffectiveTime = 10;
        this.mSample = 0;
    }

    public synchronized int getTopicId(Map<String, String> aLogMap) {
        String lEventID;
        String lPage;
        String lArg1;
        lEventID = "";
        if (aLogMap.containsKey(LogField.EVENTID.toString())) {
            lEventID = aLogMap.get(LogField.EVENTID.toString());
        }
        lPage = null;
        if (aLogMap.containsKey(LogField.PAGE.toString())) {
            lPage = aLogMap.get(LogField.PAGE.toString());
        }
        lArg1 = null;
        if (aLogMap.containsKey(LogField.ARG1.toString())) {
            lArg1 = aLogMap.get(LogField.ARG1.toString());
        }
        return getTopicId(lEventID, lPage, lArg1);
    }

    private int getTopicId(String aEventID, String aPage, String aArg1) {
        UTTopicItem lItem;
        if (TextUtils.isEmpty(aEventID) || !this.mTopicItemMap.containsKey(aEventID) || (lItem = this.mTopicItemMap.get(aEventID)) == null) {
            return 0;
        }
        if (!TextUtils.isEmpty(aPage) || !TextUtils.isEmpty(aArg1)) {
            return lItem.getTopicId(aPage, aArg1);
        }
        return 0;
    }

    public void onNonOrangeConfigurationArrive(String aGroupname) {
        super.onNonOrangeConfigurationArrive(aGroupname);
    }

    public synchronized void onOrangeConfigurationArrive(String aGroupname, Map<String, String> aConfContent) {
        Logger.d("", "aGroupname", aGroupname, "aConfContent", aConfContent);
        resetRealtimeConf();
        for (String lKey : aConfContent.keySet()) {
            String lValue = aConfContent.get(lKey);
            if (!TextUtils.isEmpty(lKey) && !TextUtils.isEmpty(lValue)) {
                if (lKey.equals("time")) {
                    int time = convertStringToInt(lValue);
                    if (time >= 3 && time <= 20) {
                        this.mEffectiveTime = time;
                    }
                } else if (lKey.equals("sample")) {
                    int sample = convertStringToInt(lValue);
                    if (sample >= 0 && sample <= 10000) {
                        this.mSample = sample;
                    }
                } else {
                    UTTopicItem lUTSampleItem = UTTopicItem.parseJson(lValue);
                    if (lUTSampleItem != null) {
                        this.mTopicItemMap.put(lKey, lUTSampleItem);
                    }
                }
            }
        }
    }

    public String[] getOrangeGroupnames() {
        return new String[]{"ut_realtime"};
    }

    private static class UTTopicItem {
        private static final String KEY_ARG1 = "arg1";
        private static final String KEY_PG = "pg";
        private static final String KEY_TOPICID = "tp";
        private Map<String, String> mArg1TopicMap = new HashMap();
        private int mDefaultTopicId = 0;
        private Map<String, String> mPageTopicMap = new HashMap();

        private UTTopicItem() {
        }

        public int getTopicId(String aPage, String aArg1) {
            String arg1TopicName;
            String pageTopicName;
            if (!StringUtils.isEmpty(aPage) && (pageTopicName = getTopicName(this.mPageTopicMap, aPage)) != null) {
                return UTRealtimeConfBiz.convertStringToInt(pageTopicName);
            }
            if (StringUtils.isEmpty(aArg1) || (arg1TopicName = getTopicName(this.mArg1TopicMap, aArg1)) == null) {
                return this.mDefaultTopicId;
            }
            return UTRealtimeConfBiz.convertStringToInt(arg1TopicName);
        }

        private String getTopicName(Map<String, String> topicMap, String arg) {
            if (arg != null) {
                for (String lKey : topicMap.keySet()) {
                    if (!lKey.startsWith("%") || !lKey.endsWith("%")) {
                        if (arg.equals(lKey)) {
                            return topicMap.get(lKey);
                        }
                    } else if (arg.contains(lKey.substring(1, lKey.length() - 1))) {
                        return topicMap.get(lKey);
                    }
                }
            }
            return null;
        }

        public static UTTopicItem parseJson(String aJsonStr) {
            try {
                UTTopicItem lUTTopicItem = new UTTopicItem();
                JSONObject lDataJson = new JSONObject(aJsonStr);
                if (lDataJson.has(KEY_TOPICID)) {
                    lUTTopicItem.mDefaultTopicId = UTRealtimeConfBiz.convertStringToInt(lDataJson.optString(KEY_TOPICID));
                }
                if (lDataJson.has(KEY_PG)) {
                    Map<String, String> lPageTopic = new HashMap<>();
                    JSONObject lJsonPage = lDataJson.optJSONObject(KEY_PG);
                    if (lJsonPage != null) {
                        Iterator<String> lKeys = lJsonPage.keys();
                        while (lKeys.hasNext()) {
                            String lKey = lKeys.next();
                            lPageTopic.put(lKey, lJsonPage.optString(lKey));
                        }
                    }
                    lUTTopicItem.mPageTopicMap = lPageTopic;
                }
                if (!lDataJson.has(KEY_ARG1)) {
                    return lUTTopicItem;
                }
                Map<String, String> lArg1Topic = new HashMap<>();
                JSONObject lJsonArg1 = lDataJson.optJSONObject(KEY_ARG1);
                if (lJsonArg1 != null) {
                    Iterator<String> lKeys2 = lJsonArg1.keys();
                    while (lKeys2.hasNext()) {
                        String lKey2 = lKeys2.next();
                        lArg1Topic.put(lKey2, lJsonArg1.optString(lKey2));
                    }
                }
                lUTTopicItem.mArg1TopicMap = lArg1Topic;
                return lUTTopicItem;
            } catch (Exception e) {
                e.printStackTrace();
                return null;
            }
        }
    }

    /* access modifiers changed from: private */
    public static int convertStringToInt(String str) {
        try {
            return Integer.parseInt(str);
        } catch (Exception e) {
            Logger.d("", e);
            return 0;
        }
    }
}
