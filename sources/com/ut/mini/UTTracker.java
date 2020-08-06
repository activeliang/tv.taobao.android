package com.ut.mini;

import android.app.Activity;
import android.net.Uri;
import android.text.TextUtils;
import android.view.View;
import com.alibaba.analytics.core.ClientVariables;
import com.alibaba.analytics.core.Constants;
import com.alibaba.analytics.core.config.UTTPKBiz;
import com.alibaba.analytics.core.config.UTTPKItem;
import com.alibaba.analytics.core.model.LogField;
import com.alibaba.analytics.utils.Logger;
import com.alibaba.analytics.utils.StringUtils;
import com.ut.mini.exposure.ExposureUtils;
import com.ut.mini.exposure.TrackerFrameLayout;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.regex.Pattern;

public class UTTracker {
    public static final int PAGE_STATUS_CODE_302 = 1;
    /* access modifiers changed from: private */
    public static List<String> s_logfield_cache;
    private static Pattern s_p = Pattern.compile("(\\|\\||[\t\r\n]|\u0001|\u0000)+");
    private String mAppkey = null;
    private Map<String, String> mArgsMap = new ConcurrentHashMap();
    private String mTrackerId = null;

    static {
        s_logfield_cache = null;
        s_logfield_cache = new ArrayList(34);
        for (Enum lKey : LogField.values()) {
            s_logfield_cache.add(String.valueOf(lKey).toLowerCase());
        }
    }

    /* access modifiers changed from: package-private */
    public void setTrackId(String aTrackerId) {
        this.mTrackerId = aTrackerId;
    }

    public synchronized void setGlobalProperty(String aKey, String aValue) {
        if (StringUtils.isEmpty(aKey) || aValue == null) {
            Logger.e("setGlobalProperty", "key is null or key is empty or value is null,please check it!");
        } else {
            this.mArgsMap.put(aKey, aValue);
        }
    }

    public synchronized String getGlobalProperty(String aKey) {
        String str;
        if (aKey != null) {
            str = this.mArgsMap.get(aKey);
        } else {
            str = null;
        }
        return str;
    }

    public synchronized void removeGlobalProperty(String aKey) {
        if (aKey != null) {
            if (this.mArgsMap.containsKey(aKey)) {
                this.mArgsMap.remove(aKey);
            }
        }
    }

    private static String getStringNoBlankAndDLine(String pStr) {
        if (pStr == null || "".equals(pStr)) {
            return pStr;
        }
        return s_p.matcher(pStr).replaceAll("");
    }

    private static String checkField(String pField) {
        return getStringNoBlankAndDLine(pField);
    }

    private Map<String, String> checkMapFields(Map<String, String> aLogMap) {
        if (aLogMap == null) {
            return null;
        }
        Map<String, String> lNewMap = new HashMap<>();
        Iterator<String> lKeyItor = aLogMap.keySet().iterator();
        if (lKeyItor == null) {
            return lNewMap;
        }
        while (lKeyItor.hasNext()) {
            try {
                String lKey = lKeyItor.next();
                if (lKey != null) {
                    lNewMap.put(lKey, checkField(aLogMap.get(lKey)));
                }
            } catch (Throwable e) {
                Logger.e("[checkMapFields]", e, new Object[0]);
            }
        }
        return lNewMap;
    }

    /* JADX WARNING: Code restructure failed: missing block: B:24:0x00b8, code lost:
        if ("2102".equals(r17.get(com.alibaba.analytics.core.model.LogField.EVENTID.toString())) != false) goto L_0x00ba;
     */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public void send(java.util.Map<java.lang.String, java.lang.String> r17) {
        /*
            r16 = this;
            if (r17 == 0) goto L_0x018c
            r12 = 0
            java.lang.String r1 = "_bmbu"
            r0 = r17
            boolean r1 = r0.containsKey(r1)
            if (r1 == 0) goto L_0x018d
            java.lang.String r1 = "_bmbu"
            r0 = r17
            r0.remove(r1)
            r12 = r17
        L_0x0018:
            r0 = r16
            java.util.Map<java.lang.String, java.lang.String> r1 = r0.mArgsMap
            r12.putAll(r1)
            r0 = r17
            if (r12 == r0) goto L_0x0028
            r0 = r17
            r12.putAll(r0)
        L_0x0028:
            r0 = r16
            java.lang.String r1 = r0.mTrackerId
            boolean r1 = com.alibaba.analytics.utils.StringUtils.isEmpty(r1)
            if (r1 != 0) goto L_0x003c
            java.lang.String r1 = "_track_id"
            r0 = r16
            java.lang.String r2 = r0.mTrackerId
            r12.put(r1, r2)
        L_0x003c:
            com.alibaba.analytics.core.ClientVariables r1 = com.alibaba.analytics.core.ClientVariables.getInstance()
            boolean r1 = r1.isAliyunOSPlatform()
            if (r1 == 0) goto L_0x0052
            com.alibaba.analytics.core.model.UTMCLogFields r1 = com.alibaba.analytics.core.model.UTMCLogFields.ALIYUN_PLATFORM_FLAG
            java.lang.String r1 = r1.toString()
            java.lang.String r2 = "yes"
            r12.put(r1, r2)
        L_0x0052:
            java.lang.String r1 = "_fuamf"
            r0 = r17
            boolean r1 = r0.containsKey(r1)
            if (r1 != 0) goto L_0x0194
            dropAllIllegalKey(r12)
        L_0x0060:
            translateFieldsName(r12)
            r0 = r16
            r0.fillReserve1Fields(r12)
            fillReservesFields(r12)
            int r1 = android.os.Build.VERSION.SDK_INT
            r2 = 14
            if (r1 >= r2) goto L_0x0090
            java.util.HashMap r11 = new java.util.HashMap
            r11.<init>()
            r0 = r17
            r11.putAll(r0)
            com.ut.mini.UTVariables r1 = com.ut.mini.UTVariables.getInstance()
            com.ut.mini.UTMI1010_2001Event r1 = r1.getUTMI1010_2001EventInstance()
            if (r1 == 0) goto L_0x0090
            com.ut.mini.UTVariables r1 = com.ut.mini.UTVariables.getInstance()
            com.ut.mini.UTMI1010_2001Event r1 = r1.getUTMI1010_2001EventInstance()
            r1.onEventArrive(r11)
        L_0x0090:
            java.lang.String r1 = "2101"
            com.alibaba.analytics.core.model.LogField r2 = com.alibaba.analytics.core.model.LogField.EVENTID
            java.lang.String r2 = r2.toString()
            r0 = r17
            java.lang.Object r2 = r0.get(r2)
            boolean r1 = r1.equals(r2)
            if (r1 != 0) goto L_0x00ba
            java.lang.String r1 = "2102"
            com.alibaba.analytics.core.model.LogField r2 = com.alibaba.analytics.core.model.LogField.EVENTID
            java.lang.String r2 = r2.toString()
            r0 = r17
            java.lang.Object r2 = r0.get(r2)
            boolean r1 = r1.equals(r2)
            if (r1 == 0) goto L_0x00ec
        L_0x00ba:
            com.ut.mini.module.UTOperationStack r2 = com.ut.mini.module.UTOperationStack.getInstance()
            java.lang.StringBuilder r1 = new java.lang.StringBuilder
            r1.<init>()
            java.lang.String r14 = "ctrlClicked:"
            java.lang.StringBuilder r14 = r1.append(r14)
            com.alibaba.analytics.core.model.LogField r1 = com.alibaba.analytics.core.model.LogField.ARG1
            java.lang.String r1 = r1.toString()
            r0 = r17
            java.lang.Object r1 = r0.get(r1)
            java.lang.String r1 = (java.lang.String) r1
            java.lang.StringBuilder r1 = r14.append(r1)
            java.lang.String r1 = r1.toString()
            r2.addAction(r1)
            java.lang.String r1 = "_priority"
            java.lang.String r2 = "4"
            r12.put(r1, r2)
        L_0x00ec:
            com.alibaba.analytics.core.config.UTClientConfigMgr r1 = com.alibaba.analytics.core.config.UTClientConfigMgr.getInstance()
            java.lang.String r2 = "sw_plugin"
            java.lang.String r13 = r1.get(r2)
            java.lang.String r1 = ""
            r2 = 2
            java.lang.Object[] r2 = new java.lang.Object[r2]
            r14 = 0
            java.lang.String r15 = "sw_plugin"
            r2[r14] = r15
            r14 = 1
            r2[r14] = r13
            com.alibaba.analytics.utils.Logger.d((java.lang.String) r1, (java.lang.Object[]) r2)
            java.lang.String r1 = "true"
            boolean r1 = r1.equals(r13)
            if (r1 == 0) goto L_0x016d
            com.alibaba.analytics.core.model.LogField r1 = com.alibaba.analytics.core.model.LogField.EVENTID
            java.lang.String r1 = r1.toString()
            r0 = r17
            java.lang.Object r1 = r0.get(r1)
            java.lang.String r1 = (java.lang.String) r1
            int r3 = java.lang.Integer.parseInt(r1)
            com.alibaba.analytics.core.model.LogField r1 = com.alibaba.analytics.core.model.LogField.PAGE
            java.lang.String r1 = r1.toString()
            r0 = r17
            java.lang.Object r4 = r0.get(r1)
            java.lang.String r4 = (java.lang.String) r4
            com.alibaba.analytics.core.model.LogField r1 = com.alibaba.analytics.core.model.LogField.ARG1
            java.lang.String r1 = r1.toString()
            r0 = r17
            java.lang.Object r5 = r0.get(r1)
            java.lang.String r5 = (java.lang.String) r5
            com.alibaba.analytics.core.model.LogField r1 = com.alibaba.analytics.core.model.LogField.ARG2
            java.lang.String r1 = r1.toString()
            r0 = r17
            java.lang.Object r6 = r0.get(r1)
            java.lang.String r6 = (java.lang.String) r6
            com.alibaba.analytics.core.model.LogField r1 = com.alibaba.analytics.core.model.LogField.ARG3
            java.lang.String r1 = r1.toString()
            r0 = r17
            java.lang.Object r7 = r0.get(r1)
            java.lang.String r7 = (java.lang.String) r7
            r8 = r17
            r9 = r12
            com.ut.mini.module.plugin.UTPluginMgr r14 = com.ut.mini.module.plugin.UTPluginMgr.getInstance()
            com.ut.mini.UTTracker$1 r1 = new com.ut.mini.UTTracker$1
            r2 = r16
            r1.<init>(r3, r4, r5, r6, r7, r8, r9)
            r14.forEachPlugin(r1)
        L_0x016d:
            com.alibaba.analytics.core.model.LogField r1 = com.alibaba.analytics.core.model.LogField.EVENTID
            java.lang.String r1 = r1.toString()
            java.lang.Object r10 = r12.get(r1)
            java.lang.String r10 = (java.lang.String) r10
            java.lang.String r1 = "2001"
            boolean r1 = r1.equals(r10)
            if (r1 == 0) goto L_0x0185
            com.ut.mini.UTPageHitHelper.encodeUtParam(r12)
        L_0x0185:
            com.ut.mini.UTAnalytics r1 = com.ut.mini.UTAnalytics.getInstance()
            r1.transferLog(r12)
        L_0x018c:
            return
        L_0x018d:
            java.util.HashMap r12 = new java.util.HashMap
            r12.<init>()
            goto L_0x0018
        L_0x0194:
            java.lang.String r1 = "_fuamf"
            r12.remove(r1)
            goto L_0x0060
        */
        throw new UnsupportedOperationException("Method not decompiled: com.ut.mini.UTTracker.send(java.util.Map):void");
    }

    private static void dropAllIllegalKey(Map<String, String> aLogMap) {
        if (aLogMap != null) {
            if (aLogMap.containsKey(LogField.IMEI.toString())) {
                aLogMap.remove(LogField.IMEI.toString());
            }
            if (aLogMap.containsKey(LogField.IMSI.toString())) {
                aLogMap.remove(LogField.IMSI.toString());
            }
            if (aLogMap.containsKey(LogField.CARRIER.toString())) {
                aLogMap.remove(LogField.CARRIER.toString());
            }
            if (aLogMap.containsKey(LogField.ACCESS.toString())) {
                aLogMap.remove(LogField.ACCESS.toString());
            }
            if (aLogMap.containsKey(LogField.ACCESS_SUBTYPE.toString())) {
                aLogMap.remove(LogField.ACCESS_SUBTYPE.toString());
            }
            if (aLogMap.containsKey(LogField.CHANNEL.toString())) {
                aLogMap.remove(LogField.CHANNEL.toString());
            }
            if (aLogMap.containsKey(LogField.LL_USERNICK.toString())) {
                aLogMap.remove(LogField.LL_USERNICK.toString());
            }
            if (aLogMap.containsKey(LogField.USERNICK.toString())) {
                aLogMap.remove(LogField.USERNICK.toString());
            }
            if (aLogMap.containsKey(LogField.LL_USERID.toString())) {
                aLogMap.remove(LogField.LL_USERID.toString());
            }
            if (aLogMap.containsKey(LogField.USERID.toString())) {
                aLogMap.remove(LogField.USERID.toString());
            }
            if (aLogMap.containsKey(LogField.SDKVERSION.toString())) {
                aLogMap.remove(LogField.SDKVERSION.toString());
            }
            if (aLogMap.containsKey(LogField.START_SESSION_TIMESTAMP.toString())) {
                aLogMap.remove(LogField.START_SESSION_TIMESTAMP.toString());
            }
            if (aLogMap.containsKey(LogField.UTDID.toString())) {
                aLogMap.remove(LogField.UTDID.toString());
            }
            if (aLogMap.containsKey(LogField.SDKTYPE.toString())) {
                aLogMap.remove(LogField.SDKTYPE.toString());
            }
            if (aLogMap.containsKey(LogField.RESERVE2.toString())) {
                aLogMap.remove(LogField.RESERVE2.toString());
            }
            if (aLogMap.containsKey(LogField.RESERVE3.toString())) {
                aLogMap.remove(LogField.RESERVE3.toString());
            }
            if (aLogMap.containsKey(LogField.RESERVE4.toString())) {
                aLogMap.remove(LogField.RESERVE4.toString());
            }
            if (aLogMap.containsKey(LogField.RESERVE5.toString())) {
                aLogMap.remove(LogField.RESERVE5.toString());
            }
            if (aLogMap.containsKey(LogField.RESERVES.toString())) {
                aLogMap.remove(LogField.RESERVES.toString());
            }
        }
    }

    private static void translateFieldsName(Map<String, String> aLogMap) {
        if (aLogMap != null) {
            if (aLogMap.containsKey("_field_os")) {
                aLogMap.remove("_field_os");
                aLogMap.put(LogField.OS.toString(), aLogMap.get("_field_os"));
            }
            if (aLogMap.containsKey("_field_os_version")) {
                aLogMap.remove("_field_os_version");
                aLogMap.put(LogField.OSVERSION.toString(), aLogMap.get("_field_os_version"));
            }
        }
    }

    private void fillReserve1Fields(Map<String, String> aLogMap) {
        aLogMap.put(LogField.SDKTYPE.toString(), Constants.SDK_TYPE);
        if (!TextUtils.isEmpty(this.mAppkey)) {
            aLogMap.put(LogField.APPKEY.toString(), this.mAppkey);
        } else {
            aLogMap.put(LogField.APPKEY.toString(), ClientVariables.getInstance().getAppKey());
        }
    }

    private static void fillReservesFields(Map<String, String> aLogMap) {
        Map<String, String> lReservesMap = new HashMap<>();
        if (aLogMap.containsKey("_track_id")) {
            String lValue = aLogMap.get("_track_id");
            aLogMap.remove("_track_id");
            if (!StringUtils.isEmpty(lValue)) {
                lReservesMap.put("_tkid", lValue);
            }
        }
        if (lReservesMap.size() > 0) {
            aLogMap.put(LogField.RESERVES.toString(), StringUtils.convertMapToString(lReservesMap));
        }
        if (!aLogMap.containsKey(LogField.PAGE.toString())) {
            aLogMap.put(LogField.PAGE.toString(), "UT");
        }
    }

    public void pageAppear(Object aPageObject) {
        UTPageHitHelper.getInstance().pageAppear(aPageObject);
    }

    public void pageAppear(Object aPageObject, String aCustomPageName) {
        UTPageHitHelper.getInstance().pageAppear(aPageObject, aCustomPageName);
    }

    public void pageAppearDonotSkip(Object aPageObject) {
        UTPageHitHelper.getInstance().pageAppear(aPageObject, (String) null, true);
    }

    public void pageAppearDonotSkip(Object aPageObject, String aCustomPageName) {
        UTPageHitHelper.getInstance().pageAppear(aPageObject, aCustomPageName, true);
    }

    public void pageDisAppear(Object aPageObject) {
        UTPageHitHelper.getInstance().pageDisAppear(aPageObject, this);
    }

    public void updateNextPageProperties(Map<String, String> aProperties) {
        UTPageHitHelper.getInstance().updateNextPageProperties(aProperties);
    }

    public void updateNextPageUtparam(String aPageUtparam) {
        UTPageHitHelper.getInstance().updateNextPageUtparam(aPageUtparam);
    }

    public void setPageStatusCode(Object aPageObject, int aPageStatusCode) {
        UTPageHitHelper.getInstance().setPageStatusCode(aPageObject, aPageStatusCode);
    }

    public void updatePageName(Object aPageObject, String aPageName) {
        UTPageHitHelper.getInstance().updatePageName(aPageObject, aPageName);
    }

    public void updatePageProperties(Object aPageObject, Map<String, String> aProperties) {
        UTPageHitHelper.getInstance().updatePageProperties(aPageObject, aProperties);
    }

    public Map<String, String> getPageProperties(Object aPageObject) {
        return UTPageHitHelper.getInstance().getPageProperties(aPageObject);
    }

    public void updatePageUtparam(Object aPageObject, String aPageUtparam) {
        UTPageHitHelper.getInstance().updatePageUtparam(aPageObject, aPageUtparam);
    }

    public void updatePageStatus(Object aPageObject, UTPageStatus aPageStatus) {
        UTPageHitHelper.getInstance().updatePageStatus(aPageObject, aPageStatus);
    }

    public void skipPageBack(Activity aPageObject) {
        UTPageHitHelper.getInstance().skipBack(aPageObject);
    }

    public void skipNextPageBack() {
        UTPageHitHelper.getInstance().skipNextPageBack();
    }

    @Deprecated
    public void skipPageBackForever(Activity aPageObject, boolean aSkip) {
        UTPageHitHelper.getInstance().skipBackForever(aPageObject, aSkip);
    }

    public String getPageSpmUrl(Activity aPageObject) {
        return UTPageHitHelper.getInstance().getPageSpmUrl(aPageObject);
    }

    public String getPageSpmPre(Activity aPageObject) {
        return UTPageHitHelper.getInstance().getPageSpmPre(aPageObject);
    }

    public Map<String, String> getPageAllProperties(Activity activity) {
        return UTPageHitHelper.getInstance().getPageAllProperties(activity);
    }

    public String getPageScmPre(Activity aPageObject) {
        return UTPageHitHelper.getInstance().getPageScmPre(aPageObject);
    }

    public void updatePageUrl(Object aPageObject, Uri aUri) {
        UTPageHitHelper.getInstance().updatePageUrl(aPageObject, aUri);
    }

    public void addTPKItem(UTTPKItem aTPKItem) {
        UTTPKBiz.getInstance().addTPKItem(aTPKItem);
    }

    public void addTPKCache(String tpkKey, String tpkValue) {
        UTTPKBiz.getInstance().addTPKCache(tpkKey, tpkValue);
    }

    public void skipPage(Object aPageObject) {
        UTPageHitHelper.getInstance().skipPage(aPageObject);
    }

    /* access modifiers changed from: protected */
    public void setAppKey(String appKey) {
        this.mAppkey = appKey;
    }

    public void setExposureTag(View view, String block, String viewId, Map<String, String> args) {
        ExposureUtils.setExposure(view, block, viewId, args);
    }

    public void refreshExposureData() {
        TrackerFrameLayout.refreshExposureData();
    }

    public void refreshExposureData(String block) {
        TrackerFrameLayout.refreshExposureData(block);
    }

    public void refreshExposureDataByViewId(String block, String viewId) {
        TrackerFrameLayout.refreshExposureDataByViewId(block, viewId);
    }

    public void commitExposureData() {
        TrackerFrameLayout.commitExposureData();
    }

    public void setCommitImmediatelyExposureBlock(String block) {
        TrackerFrameLayout.setCommitImmediatelyExposureBlock(block);
    }
}
