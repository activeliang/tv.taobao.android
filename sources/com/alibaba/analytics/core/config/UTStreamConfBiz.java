package com.alibaba.analytics.core.config;

import com.alibaba.analytics.core.model.LogField;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import org.json.JSONObject;

public class UTStreamConfBiz extends UTOrangeConfBiz {
    private static UTStreamConfBiz s_instance = null;
    private Map<String, UTStreamItem> mStreamItemMap = new HashMap();

    public static UTStreamConfBiz getInstance() {
        if (s_instance == null) {
            s_instance = new UTStreamConfBiz();
        }
        return s_instance;
    }

    private UTStreamConfBiz() {
    }

    public void resetStreamItemMap() {
        this.mStreamItemMap.clear();
    }

    public synchronized String getStreamName(Map<String, String> aLogMap) {
        String str;
        int lEventID = -1;
        if (aLogMap.containsKey(LogField.EVENTID.toString())) {
            try {
                lEventID = Integer.parseInt(aLogMap.get(LogField.EVENTID.toString()));
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        String lArg1 = null;
        if (aLogMap.containsKey(LogField.ARG1.toString())) {
            lArg1 = aLogMap.get(LogField.ARG1.toString());
        }
        String lResult = _getStreamName(lEventID, lArg1);
        if (lResult != null) {
            str = lResult;
        } else {
            String lResult2 = _getStreamName(lEventID - (lEventID % 10), lArg1);
            if (lResult2 != null) {
                str = lResult2;
            } else {
                String lResult3 = _getStreamName(lEventID - (lEventID % 100), lArg1);
                if (lResult3 != null) {
                    str = lResult3;
                } else {
                    String lResult4 = _getStreamName(lEventID - (lEventID % 1000), lArg1);
                    if (lResult4 != null) {
                        str = lResult4;
                    } else {
                        String lResult5 = _getStreamName(-1, lArg1);
                        str = lResult5 != null ? lResult5 : "stm_d";
                    }
                }
            }
        }
        return str;
    }

    private String _getStreamName(int aEventID, String aArg1) {
        String lEventIDStr = String.valueOf(aEventID);
        if (this.mStreamItemMap.containsKey(lEventIDStr)) {
            return this.mStreamItemMap.get(lEventIDStr).getStmName(aArg1);
        }
        return null;
    }

    public void onNonOrangeConfigurationArrive(String aGroupname) {
        super.onNonOrangeConfigurationArrive(aGroupname);
    }

    public synchronized void onOrangeConfigurationArrive(String aGroupname, Map<String, String> aConfContent) {
        UTStreamItem lUTSampleItem;
        this.mStreamItemMap.clear();
        for (String lKey : aConfContent.keySet()) {
            String lValue = aConfContent.get(lKey);
            if (!(lValue == null || (lUTSampleItem = UTStreamItem.parseJson(lValue)) == null)) {
                this.mStreamItemMap.put(lKey, lUTSampleItem);
            }
        }
    }

    public String[] getOrangeGroupnames() {
        return new String[]{"ut_stream"};
    }

    private static class UTStreamItem {
        private static final String KEY_ARG1 = "arg1";
        private static final String KEY_STM = "stm";
        private Map<String, String> mArg1Stm = new HashMap();
        private String mDefaultStreamName = null;

        private UTStreamItem() {
        }

        /* JADX WARNING: Code restructure failed: missing block: B:2:0x0006, code lost:
            r0 = _getArg1StmName(r3);
         */
        /* Code decompiled incorrectly, please refer to instructions dump. */
        public java.lang.String getStmName(java.lang.String r3) {
            /*
                r2 = this;
                boolean r1 = com.alibaba.analytics.utils.StringUtils.isEmpty(r3)
                if (r1 != 0) goto L_0x000d
                java.lang.String r0 = r2._getArg1StmName(r3)
                if (r0 == 0) goto L_0x000d
            L_0x000c:
                return r0
            L_0x000d:
                java.lang.String r0 = r2.mDefaultStreamName
                goto L_0x000c
            */
            throw new UnsupportedOperationException("Method not decompiled: com.alibaba.analytics.core.config.UTStreamConfBiz.UTStreamItem.getStmName(java.lang.String):java.lang.String");
        }

        private String _getArg1StmName(String lArg1) {
            if (lArg1 != null) {
                for (String lKey : this.mArg1Stm.keySet()) {
                    if (!lKey.startsWith("%") || !lKey.endsWith("%")) {
                        if (lArg1.equals(lKey)) {
                            return this.mArg1Stm.get(lKey);
                        }
                    } else if (lArg1.contains(lKey.substring(1, lKey.length() - 1))) {
                        return this.mArg1Stm.get(lKey);
                    }
                }
            }
            return null;
        }

        public static UTStreamItem parseJson(String aJsonStr) {
            try {
                UTStreamItem lSampleItem = new UTStreamItem();
                JSONObject lDataJson = new JSONObject(aJsonStr);
                if (lDataJson.has(KEY_STM)) {
                    lSampleItem.mDefaultStreamName = lDataJson.optString(KEY_STM);
                }
                if (!lDataJson.has(KEY_ARG1)) {
                    return lSampleItem;
                }
                Map<String, String> lArg1Stm = new HashMap<>();
                JSONObject lJsonArg1 = lDataJson.optJSONObject(KEY_ARG1);
                if (lJsonArg1 != null) {
                    Iterator<String> lKeys = lJsonArg1.keys();
                    while (lKeys.hasNext()) {
                        String lKey = lKeys.next();
                        lArg1Stm.put(lKey, lJsonArg1.optString(lKey));
                    }
                }
                lSampleItem.mArg1Stm = lArg1Stm;
                return lSampleItem;
            } catch (Exception e) {
                e.printStackTrace();
                return null;
            }
        }
    }
}
