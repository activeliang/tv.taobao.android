package com.alibaba.analytics.core.config;

import com.alibaba.analytics.core.Variables;
import com.alibaba.analytics.core.model.LogField;
import com.alibaba.analytics.utils.Logger;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Random;
import org.json.JSONObject;

public class UTSampleConfBiz extends UTOrangeConfBiz {
    private static UTSampleConfBiz s_instance = null;
    private Map<String, UTSampleItem> mSampleItemMap = new HashMap();

    public static UTSampleConfBiz getInstance() {
        if (s_instance == null) {
            s_instance = new UTSampleConfBiz();
        }
        return s_instance;
    }

    private UTSampleConfBiz() {
    }

    public void resetSampleItemMap() {
        this.mSampleItemMap.clear();
    }

    public synchronized boolean isSampleSuccess(Map<String, String> aLogMap) {
        boolean z;
        try {
            z = isSampleSuccess(Integer.parseInt(aLogMap.get(LogField.EVENTID.toString())), aLogMap.get(LogField.ARG1.toString()));
        } catch (Exception e) {
            Logger.e("UTSampleConfBiz", e, new Object[0]);
            z = false;
        }
        return z;
    }

    public synchronized boolean isSampleSuccess(int eventId, String arg1) {
        boolean z = true;
        synchronized (this) {
            if (!Variables.getInstance().getDebugSamplingOption()) {
                if (this.mSampleItemMap.size() != 0) {
                    UTSampleResult lResult = _getSampleResult(eventId, arg1);
                    if (!lResult.getResult()) {
                        if (lResult.isRuleExist()) {
                            z = false;
                        } else {
                            UTSampleResult lResult2 = _getSampleResult(eventId - (eventId % 10), arg1);
                            if (!lResult2.getResult()) {
                                if (lResult2.isRuleExist()) {
                                    z = false;
                                } else {
                                    UTSampleResult lResult3 = _getSampleResult(eventId - (eventId % 100), arg1);
                                    if (!lResult3.getResult()) {
                                        if (lResult3.isRuleExist()) {
                                            z = false;
                                        } else {
                                            UTSampleResult lResult4 = _getSampleResult(eventId - (eventId % 1000), arg1);
                                            if (!lResult4.getResult()) {
                                                if (lResult4.isRuleExist()) {
                                                    z = false;
                                                } else {
                                                    UTSampleResult lResult5 = _getSampleResult(-1, arg1);
                                                    if (!lResult5.getResult()) {
                                                        z = lResult5.isRuleExist() ? false : false;
                                                    }
                                                }
                                            }
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
        return z;
    }

    private UTSampleResult _getSampleResult(int aEventID, String aArg1) {
        String lEventIDStr = String.valueOf(aEventID);
        UTSampleResult lResult = new UTSampleResult();
        if (this.mSampleItemMap.containsKey(lEventIDStr)) {
            lResult.setIsRuleExist(true);
            lResult.setResult(this.mSampleItemMap.get(lEventIDStr).isSampleSuccess(aArg1));
        } else {
            lResult.setResult(false);
        }
        return lResult;
    }

    public void onNonOrangeConfigurationArrive(String aGroupname) {
        super.onNonOrangeConfigurationArrive(aGroupname);
    }

    public synchronized void onOrangeConfigurationArrive(String aGroupname, Map<String, String> aConfContent) {
        UTSampleItem lUTSampleItem;
        this.mSampleItemMap.clear();
        for (String lKey : aConfContent.keySet()) {
            String lValue = aConfContent.get(lKey);
            if (!(lValue == null || (lUTSampleItem = UTSampleItem.parseJson(lValue)) == null)) {
                this.mSampleItemMap.put(lKey, lUTSampleItem);
            }
        }
    }

    public String[] getOrangeGroupnames() {
        return new String[]{"ut_sample"};
    }

    private static class UTSampleResult {
        private boolean mIsRuleExist;
        private boolean mResult;

        private UTSampleResult() {
            this.mResult = false;
            this.mIsRuleExist = false;
        }

        public void setResult(boolean aResult) {
            this.mResult = aResult;
        }

        public boolean getResult() {
            return this.mResult;
        }

        public void setIsRuleExist(boolean aResult) {
            this.mIsRuleExist = aResult;
        }

        public boolean isRuleExist() {
            return this.mIsRuleExist;
        }
    }

    private static class UTSampleItem {
        private static final String KEY_ARG1 = "arg1";
        private static final String KEY_CP = "cp";
        private static final Random s_random = new Random();
        private Map<String, Integer> mArg1CP = new HashMap();
        private int mDefaultCP = 0;

        private UTSampleItem() {
        }

        public boolean isSampleSuccess(String aArg1) {
            return _isArg1SampleSuccess(aArg1);
        }

        private boolean _isArg1SampleSuccess(String lArg1) {
            if (lArg1 != null) {
                try {
                    for (String lKey : this.mArg1CP.keySet()) {
                        if (!lKey.startsWith("%") || !lKey.endsWith("%")) {
                            if (lArg1.equals(lKey)) {
                                if (_isSuccess(this.mArg1CP.get(lKey).intValue())) {
                                    return true;
                                }
                                return false;
                            }
                        } else if (lArg1.contains(lKey.substring(1, lKey.length() - 1))) {
                            return _isSuccess(this.mArg1CP.get(lKey).intValue());
                        }
                    }
                } catch (Throwable th) {
                }
            }
            return _isSuccess(this.mDefaultCP);
        }

        private boolean _isSuccess(int aNumber) {
            if (aNumber != 0 && s_random.nextInt(10000) < aNumber) {
                return true;
            }
            return false;
        }

        public static UTSampleItem parseJson(String aJsonStr) {
            try {
                UTSampleItem lSampleItem = new UTSampleItem();
                JSONObject lDataJson = new JSONObject(aJsonStr);
                if (lDataJson.has("cp")) {
                    lSampleItem.mDefaultCP = lDataJson.optInt("cp");
                }
                if (!lDataJson.has(KEY_ARG1)) {
                    return lSampleItem;
                }
                Map<String, Integer> lArg1CP = new HashMap<>();
                JSONObject lJsonArg1 = lDataJson.optJSONObject(KEY_ARG1);
                if (lJsonArg1 != null) {
                    Iterator<String> lKeys = lJsonArg1.keys();
                    while (lKeys.hasNext()) {
                        String lKey = lKeys.next();
                        lArg1CP.put(lKey, Integer.valueOf(Integer.parseInt(lJsonArg1.optString(lKey))));
                    }
                }
                lSampleItem.mArg1CP = lArg1CP;
                return lSampleItem;
            } catch (Exception e) {
                e.printStackTrace();
                return null;
            }
        }
    }
}
