package com.ut.mini;

import com.alibaba.analytics.AnalyticsMgr;
import com.alibaba.analytics.core.model.LogField;
import com.alibaba.analytics.utils.Logger;
import com.alibaba.analytics.utils.MapUtils;
import com.alibaba.analytics.utils.StringUtils;
import com.ut.mini.UTConstants;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

public class UTHitBuilders {

    public static class UTHitBuilder {
        public static final String FIELD_ARG1 = "_field_arg1";
        public static final String FIELD_ARG2 = "_field_arg2";
        public static final String FIELD_ARG3 = "_field_arg3";
        public static final String FIELD_ARGS = "_field_args";
        public static final String FIELD_EVENT_ID = "_field_event_id";
        public static final String FIELD_PAGE = "_field_page";
        private Map<String, String> mHitMap = new HashMap(64);

        public UTHitBuilder() {
            if (!this.mHitMap.containsKey(FIELD_PAGE)) {
                this.mHitMap.put(FIELD_PAGE, "UT");
            }
        }

        public UTHitBuilder setProperty(String aKey, String aValue) {
            if (StringUtils.isEmpty(aKey) || aValue == null) {
                Logger.e("setProperty", "key is null or key is empty or value is null,please check it!");
            } else {
                if (this.mHitMap.containsKey(aKey)) {
                    this.mHitMap.remove(aKey);
                }
                this.mHitMap.put(aKey, aValue);
            }
            return this;
        }

        public UTHitBuilder setProperties(Map<String, String> aProperties) {
            Set<Map.Entry<String, String>> entitys;
            if (!(aProperties == null || (entitys = aProperties.entrySet()) == null)) {
                for (Map.Entry entity : entitys) {
                    Object key = entity.getKey();
                    Object value = entity.getValue();
                    if (key == null || !(key instanceof String)) {
                        Logger.w("param aProperties key error", "key", key, "value", value);
                    } else if (value == null || !(value instanceof String)) {
                        Logger.w("param aProperties value error", "key" + key, "value", value);
                    } else {
                        setProperty((String) key, (String) value);
                    }
                }
            }
            return this;
        }

        public String getProperty(String aKey) {
            if (aKey == null || !this.mHitMap.containsKey(aKey)) {
                return null;
            }
            return this.mHitMap.get(aKey);
        }

        public Map<String, String> build() {
            Map<String, String> lTmpHitMap = this.mHitMap;
            lTmpHitMap.put(UTConstants.PrivateLogFields.FLAG_BUILD_MAP_BY_UT, "yes");
            if (!_checkIlleagleProperty(lTmpHitMap)) {
                return null;
            }
            _dropAllIllegalFields(lTmpHitMap);
            _translateFieldsName(lTmpHitMap);
            if (!lTmpHitMap.containsKey(LogField.EVENTID.toString())) {
                return null;
            }
            return lTmpHitMap;
        }

        private static boolean _checkIlleagleProperty(Map<String, String> aHitMap) {
            if (aHitMap != null) {
                if (aHitMap.containsKey((Object) null)) {
                    aHitMap.remove((Object) null);
                }
                if (aHitMap.containsKey("")) {
                    aHitMap.remove("");
                }
                if (aHitMap.containsKey(LogField.PAGE.toString())) {
                    Logger.e("checkIlleagleProperty", "IlleaglePropertyKey(PAGE) is setted when you call the method setProperty or setProperties ,please use another key to replace it!");
                    return false;
                } else if (aHitMap.containsKey(LogField.EVENTID.toString())) {
                    Logger.e("checkIlleagleProperty", "IlleaglePropertyKey(EVENTID) is setted when you call the method setProperty or setProperties ,please use another key to replace it!");
                    return false;
                } else if (aHitMap.containsKey(LogField.ARG1.toString())) {
                    Logger.e("checkIlleagleProperty", "IlleaglePropertyKey(ARG1) is setted when you call the method setProperty or setProperties ,please use another key to replace it!");
                    return false;
                } else if (aHitMap.containsKey(LogField.ARG2.toString())) {
                    Logger.e("checkIlleagleProperty", "IlleaglePropertyKey(ARG2) is setted when you call the method setProperty or setProperties ,please use another key to replace it!");
                    return false;
                } else if (aHitMap.containsKey(LogField.ARG3.toString())) {
                    Logger.e("checkIlleagleProperty", "IlleaglePropertyKey(ARG3) is setted when you call the method setProperty or setProperties ,please use another key to replace it!");
                    return false;
                }
            }
            return true;
        }

        private static void _translateFieldsName(Map<String, String> aHitMap) {
            if (aHitMap != null) {
                String lValue = aHitMap.remove(FIELD_PAGE);
                if (lValue != null) {
                    aHitMap.put(LogField.PAGE.toString(), lValue);
                }
                String lValue2 = aHitMap.remove(FIELD_ARG1);
                if (lValue2 != null) {
                    aHitMap.put(LogField.ARG1.toString(), lValue2);
                }
                String lValue3 = aHitMap.remove(FIELD_ARG2);
                if (lValue3 != null) {
                    aHitMap.put(LogField.ARG2.toString(), lValue3);
                }
                String lValue4 = aHitMap.remove(FIELD_ARG3);
                if (lValue4 != null) {
                    aHitMap.put(LogField.ARG3.toString(), lValue4);
                }
                String lValue5 = aHitMap.remove(FIELD_ARGS);
                if (lValue5 != null) {
                    aHitMap.put(LogField.ARGS.toString(), lValue5);
                }
                String lValue6 = aHitMap.remove(FIELD_EVENT_ID);
                if (lValue6 != null) {
                    aHitMap.put(LogField.EVENTID.toString(), lValue6);
                }
            }
        }

        private static void _dropAllIllegalFields(Map<String, String> aHitMap) {
            if (aHitMap != null) {
                aHitMap.remove(LogField.PAGE.toString());
                aHitMap.remove(LogField.EVENTID.toString());
                aHitMap.remove(LogField.ARG1.toString());
                aHitMap.remove(LogField.ARG2.toString());
                aHitMap.remove(LogField.ARG3.toString());
                aHitMap.remove(LogField.ARGS.toString());
            }
        }
    }

    public static class UTCustomHitBuilder extends UTHitBuilder {
        public UTCustomHitBuilder(String aEventLabel) {
            if (!StringUtils.isEmpty(aEventLabel)) {
                super.setProperty(UTHitBuilder.FIELD_ARG1, aEventLabel);
            }
            super.setProperty(UTHitBuilder.FIELD_EVENT_ID, "19999");
            super.setProperty(UTHitBuilder.FIELD_ARG3, "0");
            super.setProperty("_priority", "4");
        }

        public UTCustomHitBuilder setDurationOnEvent(long aDuration) {
            if (aDuration < 0) {
                aDuration = 0;
            }
            super.setProperty(UTHitBuilder.FIELD_ARG3, "" + aDuration);
            return this;
        }

        public UTCustomHitBuilder setEventPage(String aPage) {
            if (!StringUtils.isEmpty(aPage)) {
                super.setProperty(UTHitBuilder.FIELD_PAGE, aPage);
            }
            return this;
        }

        public Map<String, String> build() {
            Map<String, String> lOMap = super.build();
            if (lOMap != null) {
                String lPageName = lOMap.get(LogField.PAGE.toString());
                String lEventLabel = lOMap.get(LogField.ARG1.toString());
                if (lEventLabel != null) {
                    lOMap.remove(LogField.ARG1.toString());
                    lOMap.remove(LogField.PAGE.toString());
                    Map<String, String> lNMap = MapUtils.convertToUrlEncodedMap(lOMap);
                    lNMap.put(LogField.ARG1.toString(), lEventLabel);
                    lNMap.put(LogField.PAGE.toString(), lPageName);
                    return lNMap;
                }
            }
            return lOMap;
        }
    }

    public static class UTPageHitBuilder extends UTHitBuilder {
        public UTPageHitBuilder(String aPageName) {
            if (!StringUtils.isEmpty(aPageName)) {
                super.setProperty(UTHitBuilder.FIELD_PAGE, aPageName);
            }
            super.setProperty(UTHitBuilder.FIELD_EVENT_ID, "2001");
            super.setProperty(UTHitBuilder.FIELD_ARG3, "0");
        }

        public UTPageHitBuilder setReferPage(String aReferPage) {
            if (!StringUtils.isEmpty(aReferPage)) {
                super.setProperty(UTHitBuilder.FIELD_ARG1, aReferPage);
            }
            return this;
        }

        public UTPageHitBuilder setDurationOnPage(long aDuration) {
            if (aDuration < 0) {
                aDuration = 0;
            }
            super.setProperty(UTHitBuilder.FIELD_ARG3, "" + aDuration);
            return this;
        }
    }

    public static class UTControlHitBuilder extends UTHitBuilder {
        public UTControlHitBuilder(String aControlName) {
            if (!StringUtils.isEmpty(aControlName)) {
                String lPageName = UTPageHitHelper.getInstance().getCurrentPageName();
                if (!StringUtils.isEmpty(lPageName)) {
                    super.setProperty(UTHitBuilder.FIELD_PAGE, lPageName);
                    super.setProperty(UTHitBuilder.FIELD_EVENT_ID, "2101");
                    super.setProperty(UTHitBuilder.FIELD_ARG1, lPageName + "_" + aControlName);
                } else if (AnalyticsMgr.isDebug) {
                    throw new IllegalArgumentException("Please call in at PageAppear and PageDisAppear.");
                } else {
                    Logger.e("Please call in at PageAppear and PageDisAppear.", new Object[0]);
                }
            } else if (AnalyticsMgr.isDebug) {
                throw new IllegalArgumentException("Control name can not be empty.");
            } else {
                Logger.e("Control name can not be empty.", new Object[0]);
            }
        }

        public UTControlHitBuilder(String aPageName, String aControlName) {
            if (StringUtils.isEmpty(aControlName)) {
                if (AnalyticsMgr.isDebug) {
                    throw new IllegalArgumentException("Control name can not be empty.");
                }
                Logger.e("Control name can not be empty.", new Object[0]);
            } else if (!StringUtils.isEmpty(aPageName)) {
                super.setProperty(UTHitBuilder.FIELD_PAGE, aPageName);
                super.setProperty(UTHitBuilder.FIELD_EVENT_ID, "2101");
                super.setProperty(UTHitBuilder.FIELD_ARG1, aPageName + "_" + aControlName);
            } else if (AnalyticsMgr.isDebug) {
                throw new IllegalArgumentException("Page name can not be empty.");
            } else {
                Logger.e("Page name can not be empty.", new Object[0]);
            }
        }
    }
}
