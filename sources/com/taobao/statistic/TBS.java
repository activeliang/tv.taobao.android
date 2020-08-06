package com.taobao.statistic;

import android.annotation.TargetApi;
import android.app.Activity;
import android.app.Fragment;
import android.content.Context;
import android.view.View;
import android.webkit.WebView;
import com.alibaba.analytics.core.model.LogField;
import com.alibaba.analytics.utils.Logger;
import com.alibaba.analytics.utils.MapUtils;
import com.alibaba.analytics.utils.StringUtils;
import com.ut.mini.UTAnalytics;
import com.ut.mini.UTConstants;
import com.ut.mini.UTHitBuilders;
import com.ut.mini.UTHybridHelper;
import com.ut.mini.UTPageHitHelper;
import com.ut.mini.UTTracker;
import com.ut.mini.internal.UTOriginalCustomHitBuilder;
import com.ut.mini.internal.UTTeamWork;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Properties;
import org.json.JSONException;
import org.json.JSONObject;

public class TBS {

    @Deprecated
    public interface OnInitFinishListener {
        void onFinish(int i);
    }

    public static class DelayEventObject {
        private String mBeginOrEnd = "begin";
        private Properties mProperties = null;
        private long mTimestamp = 0;

        public void setTimestamp(long pTimestamp) {
            this.mTimestamp = pTimestamp;
        }

        public long getTimestamp() {
            return this.mTimestamp;
        }

        public void setProperties(Properties pProperties) {
            this.mProperties = pProperties;
        }

        public Properties getProperties() {
            return this.mProperties;
        }

        public void setBegin() {
            this.mBeginOrEnd = "begin";
        }

        public void setEnd() {
            this.mBeginOrEnd = "end";
        }

        public boolean isBegin() {
            if (this.mBeginOrEnd.equals("begin")) {
                return true;
            }
            return false;
        }
    }

    public static void updateUserAccount(String userNick) {
        UTAnalytics.getInstance().updateUserAccount(userNick, (String) null);
    }

    public static void updateUserAccount(String userNick, String userid) {
        UTAnalytics.getInstance().updateUserAccount(userNick, userid);
    }

    public static void updateGPSInfo(String pageKeyOrPageName, double longitude, double latitude) {
        UTAnalytics.getInstance().getDefaultTracker().send(new UTOriginalCustomHitBuilder(pageKeyOrPageName, 1005, "" + longitude, "" + latitude, (String) null, (Map<String, String>) null).build());
    }

    @Deprecated
    public static void setVersion(String bundleVersion) {
    }

    public static void updateSessionProperties(Properties properties) {
        UTAnalytics.getInstance().updateSessionProperties(MapUtils.convertPropertiesToMap(properties));
    }

    static void updateNextPageProperties(Properties lProperties) {
        UTAnalytics.getInstance().getDefaultTracker().updateNextPageProperties(MapUtils.convertPropertiesToMap(lProperties));
    }

    public static void userRegister(String userNick) {
        UTAnalytics.getInstance().userRegister(userNick);
    }

    public static class Ext {
        private static final String PAGE_EXTEND = "Page_Extend";
        private static Object lCommitEventLockObj = new Object();
        private static HashMap<String, DelayEventObject> mDelayEventObject = new HashMap<>();

        public static void commitEvent(String eventLable, Properties properties) {
            if (!StringUtils.isEmpty(eventLable)) {
                UTHitBuilders.UTCustomHitBuilder lHitBuilder = new UTHitBuilders.UTCustomHitBuilder(eventLable);
                lHitBuilder.setProperties(MapUtils.convertPropertiesToMap(properties));
                UTTracker tracker = UTAnalytics.getInstance().getDefaultTracker();
                if (tracker != null) {
                    tracker.send(lHitBuilder.build());
                    return;
                }
                Logger.w("TBS", "please call UTAnalytics.getInstance().setAppApplicationInstance() before this method");
            }
        }

        public static void commitEventBegin(String eventID, Properties properties) {
            if (!StringUtils.isEmpty(eventID)) {
                synchronized (lCommitEventLockObj) {
                    if (mDelayEventObject.size() > 2000) {
                        mDelayEventObject.clear();
                    }
                    if (mDelayEventObject.containsKey(eventID)) {
                        mDelayEventObject.remove(eventID);
                    }
                    DelayEventObject lObject = new DelayEventObject();
                    lObject.setTimestamp(System.currentTimeMillis());
                    lObject.setProperties(properties);
                    lObject.setBegin();
                    mDelayEventObject.put(eventID, lObject);
                }
            }
        }

        public static void commitEventEnd(String eventID, Properties properties) {
            if (!StringUtils.isEmpty(eventID)) {
                synchronized (lCommitEventLockObj) {
                    if (mDelayEventObject.containsKey(eventID)) {
                        DelayEventObject lObject = mDelayEventObject.get(eventID);
                        mDelayEventObject.remove(eventID);
                        if (lObject != null && lObject.isBegin()) {
                            Properties lPrp1 = lObject.getProperties();
                            Map<String, String> lPrpMap = new HashMap<>();
                            if (lPrp1 != null) {
                                lPrpMap.putAll(MapUtils.convertPropertiesToMap(lPrp1));
                            }
                            if (properties != null) {
                                lPrpMap.putAll(MapUtils.convertPropertiesToMap(properties));
                            }
                            Map<String, String> lHitMap = new HashMap<>();
                            lHitMap.put(LogField.EVENTID.toString(), "19999");
                            lHitMap.put(LogField.ARG1.toString(), eventID);
                            lHitMap.put(LogField.ARG3.toString(), "" + (System.currentTimeMillis() - lObject.getTimestamp()));
                            lHitMap.putAll(lPrpMap);
                            lHitMap.put(UTConstants.PrivateLogFields.FLAG_BUILD_MAP_BY_UT, "yes");
                            UTAnalytics.getInstance().getDefaultTracker().send(lHitMap);
                        }
                    }
                }
            }
        }

        public static void commitEvent(String pageName, int eventID) {
            commitEvent(pageName, eventID, (Object) null, (Object) null, (Object) null, (String[]) null);
        }

        public static void commitEvent(String pageName, int eventID, Object arg) {
            commitEvent(pageName, eventID, arg, (Object) null, (Object) null, (String[]) null);
        }

        public static void commitEvent(String pageName, int eventID, Object arg1, Object arg2) {
            commitEvent(pageName, eventID, arg1, arg2, (Object) null, (String[]) null);
        }

        public static void commitEvent(String pageName, int eventID, Object arg1, Object arg2, Object arg3) {
            commitEvent(pageName, eventID, arg1, arg2, arg3, (String[]) null);
        }

        /* access modifiers changed from: private */
        public static String _convertStringAToKVSString(String... kvs) {
            if (kvs != null && kvs.length == 0) {
                return null;
            }
            boolean lFlag = false;
            StringBuffer lSB = new StringBuffer();
            if (kvs != null && kvs.length > 0) {
                for (int i = 0; i < kvs.length; i++) {
                    if (!StringUtils.isEmpty(kvs[i])) {
                        if (lFlag) {
                            lSB.append(",");
                        }
                        lSB.append(kvs[i]);
                        lFlag = true;
                    }
                }
            }
            return lSB.toString();
        }

        public static void commitEvent(String pageName, int eventID, Object arg1, Object arg2, Object arg3, String... kvs) {
            String lArgsString = _convertStringAToKVSString(kvs);
            Map<String, String> lHitMap = new HashMap<>();
            lHitMap.put(LogField.PAGE.toString(), pageName);
            lHitMap.put(LogField.EVENTID.toString(), "" + eventID);
            lHitMap.put(LogField.ARG1.toString(), StringUtils.convertObjectToString(arg1));
            lHitMap.put(LogField.ARG2.toString(), StringUtils.convertObjectToString(arg2));
            lHitMap.put(LogField.ARG3.toString(), StringUtils.convertObjectToString(arg3));
            if (lArgsString != null) {
                lHitMap.put(LogField.ARGS.toString(), lArgsString);
            }
            lHitMap.put(UTConstants.PrivateLogFields.FLAG_BUILD_MAP_BY_UT, "yes");
            UTAnalytics.getInstance().getDefaultTracker().send(lHitMap);
        }

        public static void commitEvent(int eventID) {
            commitEvent("Page_Extend", eventID, (Object) null, (Object) null, (Object) null, (String[]) null);
        }

        public static void commitEvent(int eventID, Object arg) {
            commitEvent("Page_Extend", eventID, arg, (Object) null, (Object) null, (String[]) null);
        }

        public static void commitEvent(int eventID, Object arg1, Object arg2) {
            commitEvent("Page_Extend", eventID, arg1, arg2, (Object) null, (String[]) null);
        }

        public static void commitEvent(int eventID, Object arg1, Object arg2, Object arg3) {
            commitEvent("Page_Extend", eventID, arg1, arg2, arg3, (String[]) null);
        }

        public static void commitEvent(int eventID, Object arg1, Object arg2, Object arg3, String... kvs) {
            commitEvent("Page_Extend", eventID, arg1, arg2, arg3, kvs);
        }
    }

    public static class Page {
        @Deprecated
        public static void enter(String pageKey) {
        }

        @Deprecated
        public static void leave(String pageKey) {
        }

        @Deprecated
        public static void destroy(String pageKey) {
        }

        @Deprecated
        public static void create(String className, String page) {
        }

        @Deprecated
        public static void enterWithPageName(String pageKey, String pageName) {
        }

        @Deprecated
        public static void updatePageName(String pageKey, String newPageName) {
        }

        @Deprecated
        public static void create(String pageName) {
        }

        public static void ctrlClicked(CT ct, String controlName) {
            _commitCtrlEvent((String) null, 2101, ct, controlName, 0, new String[0]);
        }

        private static String _getCurPageName() {
            String lPageName = UTPageHitHelper.getInstance().getCurrentPageName();
            if (lPageName == null || lPageName.startsWith("Page_")) {
                return lPageName;
            }
            return "Page_" + lPageName;
        }

        private static String _calPageName(String pageName) {
            if (pageName == null || pageName.startsWith("Page_")) {
                return pageName;
            }
            return "Page_" + pageName;
        }

        private static String _calControlName(String pageName, CT ct, String controlName) {
            if (pageName == null || controlName == null) {
                return null;
            }
            if (!pageName.startsWith("Page_")) {
                pageName = "Page_" + pageName;
            }
            return pageName + "_" + ct.toString() + "-" + controlName;
        }

        /* access modifiers changed from: private */
        public static void _commitCtrlEvent(String aPagename, int aEventID, CT ct, String aControlName, int selectIndex, String... kvs) {
            String lPageName;
            if (aPagename == null) {
                lPageName = _getCurPageName();
            } else {
                lPageName = _calPageName(aPagename);
            }
            if (!StringUtils.isEmpty(lPageName)) {
                String lControlName = _calControlName(lPageName, ct, aControlName);
                if (!StringUtils.isEmpty(lControlName)) {
                    String lArgsString = Ext._convertStringAToKVSString(kvs);
                    Map<String, String> lHitMap = new HashMap<>();
                    lHitMap.put(LogField.PAGE.toString(), lPageName);
                    lHitMap.put(LogField.EVENTID.toString(), "" + aEventID);
                    lHitMap.put(LogField.ARG1.toString(), lControlName);
                    lHitMap.put(UTConstants.PrivateLogFields.FLAG_BUILD_MAP_BY_UT, "yes");
                    if (aEventID == 2102) {
                        lHitMap.put(LogField.ARG3.toString(), "" + selectIndex);
                    }
                    if (lArgsString != null) {
                        lHitMap.put(LogField.ARGS.toString(), lArgsString);
                    }
                    UTAnalytics.getInstance().getDefaultTracker().send(lHitMap);
                }
            }
        }

        public static void buttonClicked(String controlName) {
            ctrlClicked(CT.Button, controlName);
        }

        public static void ctrlLongClicked(CT ct, String controlName) {
            _commitCtrlEvent((String) null, 2103, ct, controlName, 0, new String[0]);
        }

        public static void itemSelected(CT ct, String controlName, int selectedIndex) {
            _commitCtrlEvent((String) null, 2102, ct, controlName, selectedIndex, new String[0]);
        }

        @Deprecated
        public static void updatePageProperties(String pPageKey, Properties properties) {
        }

        @Deprecated
        public static void goBack() {
        }
    }

    public static class Adv {
        @Deprecated
        public static void keepKvs(String pageKey, String... keys) {
        }

        @Deprecated
        public static void unKeepKvs(String pageKey, String... keys) {
        }

        @Deprecated
        public static void putKvs(String key, Object value) {
        }

        @Deprecated
        public static void destroy(String pageKey, String... kvs) {
        }

        @Deprecated
        public static void enterWithPageName(String pageKey, String pageName, String... kvs) {
        }

        @Deprecated
        public static void enter(String pageKey, String... kvs) {
        }

        public static void ctrlClicked(String pageName, CT ct, String controlName, String... kvs) {
            Page._commitCtrlEvent(pageName, 2101, ct, controlName, 0, kvs);
        }

        @Deprecated
        public static String getUtsid() {
            return UTTeamWork.getInstance().getUtsid();
        }

        public static void ctrlClicked(CT ct, String controlName, String... kvs) {
            Page._commitCtrlEvent((String) null, 2101, ct, controlName, 0, kvs);
        }

        public static void ctrlClickedOnPage(String pageKey, CT ct, String controlName, String... kvs) {
            Page._commitCtrlEvent(pageKey, 2101, ct, controlName, 0, kvs);
        }

        public static void ctrlLongClicked(String pageName, CT ct, String controlName, String... kvs) {
            Page._commitCtrlEvent(pageName, 2103, ct, controlName, 0, kvs);
        }

        public static void ctrlLongClicked(CT ct, String controlName, String... kvs) {
            Page._commitCtrlEvent((String) null, 2103, ct, controlName, 0, kvs);
        }

        public static void ctrlLongClickedOnPage(String pageKey, CT ct, String controlName, String... kvs) {
            Page._commitCtrlEvent(pageKey, 2103, ct, controlName, 0, kvs);
        }

        public static void itemSelected(String pageName, CT ct, String controlName, int selectedIndex, String... kvs) {
            Page._commitCtrlEvent(pageName, 2102, ct, controlName, selectedIndex, kvs);
        }

        public static void itemSelected(CT ct, String controlName, int selectedIndex, String... kvs) {
            Page._commitCtrlEvent((String) null, 2102, ct, controlName, selectedIndex, kvs);
        }

        public static void itemSelectedOnPage(String pageKey, CT ct, String controlName, int selectedIndex, String... kvs) {
            Page._commitCtrlEvent(pageKey, 2102, ct, controlName, selectedIndex, kvs);
        }

        public static void ctrlClicked(String pageName, CT ct, String controlName) {
            Page._commitCtrlEvent(pageName, 2101, ct, controlName, 0, new String[0]);
        }

        public static void ctrlClickedOnPage(String pageKey, CT ct, String controlName) {
            Page._commitCtrlEvent(pageKey, 2101, ct, controlName, 0, new String[0]);
        }

        public static void ctrlLongClicked(String pageName, CT ct, String controlName) {
            Page._commitCtrlEvent(pageName, 2103, ct, controlName, 0, new String[0]);
        }

        public static void ctrlLongClickedOnPage(String pageKey, CT ct, String controlName) {
            Page._commitCtrlEvent(pageKey, 2103, ct, controlName, 0, new String[0]);
        }

        public static void itemSelected(String pageName, CT ct, String controlName, int selectedIndex) {
            Page._commitCtrlEvent(pageName, 2102, ct, controlName, selectedIndex, new String[0]);
        }

        public static void itemSelectedOnPage(String pageKey, CT ct, String controlName, int selectedIndex) {
            Page._commitCtrlEvent(pageKey, 2103, ct, controlName, selectedIndex, new String[0]);
        }

        @Deprecated
        public static void easyTraceEnter(String pPageName, boolean isActivity, String... kvs) {
        }

        @Deprecated
        public static void easyTraceLeave(String pageName, boolean isActivity, String... kvs) {
        }

        @Deprecated
        public static void easyTraceInternalCtrlClick(String pageName, String pCtrlName, String... kvs) {
        }

        /* access modifiers changed from: package-private */
        @Deprecated
        public void forceUpload() {
        }

        @Deprecated
        public static void leave(String pageKey, String... kvs) {
        }

        @Deprecated
        public static void turnOffLogFriendly() {
        }

        @Deprecated
        public static void onCaughException(Throwable ex) {
        }
    }

    public static class EasyTrace {
        @Deprecated
        public static void easyTraceUpdatePageNameManual(String activityName, String newPageName) {
        }

        public static void easyTraceEnterManual(Activity activity, String customPageName) {
            UTAnalytics.getInstance().getDefaultTracker().pageAppear(activity);
            UTAnalytics.getInstance().getDefaultTracker().updatePageName(activity, customPageName);
        }

        public static void easyTraceLeaveManual(Activity activity, String customPageName) {
            UTAnalytics.getInstance().getDefaultTracker().updatePageName(activity, customPageName);
            UTAnalytics.getInstance().getDefaultTracker().pageDisAppear(activity);
        }

        public static void easyTraceUpdatePageNameManual(Activity aActivity, String newPageName) {
            UTAnalytics.getInstance().getDefaultTracker().updatePageName(aActivity, newPageName);
        }

        @TargetApi(11)
        public static void easyTraceFragmentEnterManual(Fragment fragment) {
            UTAnalytics.getInstance().getDefaultTracker().pageAppear(fragment);
        }

        @TargetApi(11)
        public static void easyTraceFragmentLeaveManual(Fragment fragment) {
            UTAnalytics.getInstance().getDefaultTracker().pageDisAppear(fragment);
        }

        public static void easyTraceFragmentV4EnterManual(android.support.v4.app.Fragment fragment) {
            UTAnalytics.getInstance().getDefaultTracker().pageAppear(fragment);
        }

        public static void easyTraceFragmentV4LeaveManual(android.support.v4.app.Fragment fragment) {
            UTAnalytics.getInstance().getDefaultTracker().pageDisAppear(fragment);
        }

        @Deprecated
        public static void updateEasyTraceActivityProperties(Activity activity, Properties properties) {
            if (UTAnalytics.getInstance().getDefaultTracker() != null) {
                UTAnalytics.getInstance().getDefaultTracker().updatePageProperties(activity, MapUtils.convertPropertiesToMap(properties));
            }
        }

        @Deprecated
        public static void updateEasyTraceActivityPropertiesManual(Activity activity, String customPage, Properties properties) {
            UTAnalytics.getInstance().getDefaultTracker().updatePageProperties(activity, MapUtils.convertPropertiesToMap(properties));
            UTAnalytics.getInstance().getDefaultTracker().updatePageName(activity, customPage);
        }

        @TargetApi(11)
        public static void updateEasyTraceFragmentProperties(Fragment fragment, Properties properties) {
            UTAnalytics.getInstance().getDefaultTracker().updatePageProperties(fragment, MapUtils.convertPropertiesToMap(properties));
        }

        @Deprecated
        public static void updateEasyTraceFragmentV4Properties(android.support.v4.app.Fragment fragment, Properties properties) {
            UTAnalytics.getInstance().getDefaultTracker().updatePageProperties(fragment, MapUtils.convertPropertiesToMap(properties));
        }

        @Deprecated
        public static Runtime getUserTrackRuntime() {
            return null;
        }

        @Deprecated
        public static void easyTraceCtrlClickManual(View view, String... kvs) {
        }

        @Deprecated
        public static void easyTraceCtrlClickManual(View view, String customCtrlName, String... kvs) {
        }
    }

    @Deprecated
    public static void h5UT(String jsonStr, WebView view) {
        h5UT((String) null, jsonStr, view);
    }

    private static void h5UT(String sep, String jsonStr, Object view) {
        Map<String, String> lDataMap = new HashMap<>();
        try {
            JSONObject lJsonObj = new JSONObject(jsonStr);
            Iterator<String> lKeys = lJsonObj.keys();
            while (lKeys.hasNext()) {
                String lKey = lKeys.next();
                if (!StringUtils.isEmpty(lKey)) {
                    String lValue = lJsonObj.getString(lKey);
                    if (!StringUtils.isEmpty(lValue)) {
                        lDataMap.put(lKey, lValue);
                    }
                }
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        if (lDataMap != null && view != null) {
            UTHybridHelper.getInstance().h5UT(lDataMap, view);
        }
    }

    public static void setH5Url(String url) {
        UTHybridHelper.getInstance().setH5Url(url);
    }

    @Deprecated
    public static void uninit() {
    }

    @Deprecated
    public static void turnDebug() {
    }

    @Deprecated
    public static void swithFromSimplePipeLineToCommon(boolean flag) {
    }

    @Deprecated
    public static void init() {
    }

    @Deprecated
    public static void setChannel(String channel) {
    }

    @Deprecated
    public static synchronized void updateSharedProprety(String pKey, String pValue) {
        synchronized (TBS.class) {
        }
    }

    @Deprecated
    public static synchronized String getSharedProperty(String pKey) {
        synchronized (TBS.class) {
        }
        return null;
    }

    @Deprecated
    public static void setKey(String appKey, String appSecret) {
    }

    @Deprecated
    public static void turnOnSecuritySDKSupport() {
    }

    @Deprecated
    public static void setEnvironment(Context environment) {
    }

    @Deprecated
    public static void setEnvironment(Context environment, String resourceIdentifier) {
    }

    @Deprecated
    public static void trade(String bizOrderID) {
    }

    public static class Network {
        @Deprecated
        public static void pushArrive(String pushCategoryName) {
        }

        @Deprecated
        public static void pushDisplay(String pushCategoryName) {
        }

        @Deprecated
        public static void pushView(String pushCategoryName) {
        }

        @Deprecated
        public static void searchKeyword(String keywordCategory, String keyword) {
        }

        @Deprecated
        public static void updateUTSIDToCookie(String aUrl) {
        }

        @Deprecated
        public static void updateUTCookie(String aUrl, Map<String, String> map) {
        }

        @Deprecated
        public static void weiboShare(String weiboType, String shareContent) {
        }

        @Deprecated
        public static void download(String category, String itemId, long size, long timeConsumer, boolean isContinue) {
        }

        @Deprecated
        public static void download(String category, String itemId, long size, long timeConsumer, String start, String finish, boolean isContinue) {
        }
    }

    @Deprecated
    public static void h5UT(String jsonStr, Context aContext) {
        h5UT((String) null, jsonStr, aContext);
    }

    public static class CrashHandler {

        public interface OnCrashCaughtListener {
            Arg OnCrashCaught(Thread thread, Throwable th, Arg arg);
        }

        public interface OnDaemonThreadCrashCaughtListener {
            void OnDaemonThreadCrashCaught(Thread thread);
        }

        @Deprecated
        public static void turnOff() {
        }

        @Deprecated
        public static void disableEffect() {
        }

        @Deprecated
        public static void setSubmitToSystemFlag() {
        }

        @Deprecated
        public static void withRestart(Activity mainActivityForRestart, int howLongAppRestart, int howManyTimes) {
        }

        @Deprecated
        public static void setToastStyle(int howLongToastShow, String toastString) {
        }

        @Deprecated
        public static void setOnCrashCaughtListener(OnCrashCaughtListener listener) {
        }

        @Deprecated
        public static void setOnDaemonCrashCaughtListener(OnDaemonThreadCrashCaughtListener daemonThreadCrashCaughtListener) {
        }

        @Deprecated
        public static void setOnDaemonCrashCaughtListener(String threadName, OnDaemonThreadCrashCaughtListener daemonThreadCrashCaughtListener) {
        }

        @Deprecated
        public static void removeDaemonCrashCaughtListener(String threadName) {
        }

        @Deprecated
        public static void setContinueWhenDaemonThreadUncaughException() {
        }
    }
}
