package com.ut.mini;

import android.app.Activity;
import android.net.Uri;
import android.taobao.windvane.jsbridge.utils.WVUtils;
import android.taobao.windvane.service.WVEventId;
import android.text.TextUtils;
import com.alibaba.analytics.core.config.UTTPKBiz;
import com.alibaba.analytics.utils.Logger;
import com.alibaba.analytics.utils.StringUtils;
import com.taobao.ju.track.JTrack;
import com.taobao.ju.track.constants.Constants;
import com.ut.mini.UTPageHitHelper;
import com.ut.mini.internal.UTOriginalCustomHitBuilder;
import com.yunos.tv.core.config.SPMConfig;
import com.yunos.tvtaobao.biz.common.BaseConfig;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class UTHybridHelper {
    private static UTHybridHelper s_instance = new UTHybridHelper();

    public static UTHybridHelper getInstance() {
        return s_instance;
    }

    public void setH5Url(String url) {
        if (url != null) {
            UTVariables.getInstance().setH5Url(url);
        }
    }

    public void h5UT(Map<String, String> dataMap, Object view) {
        if (dataMap == null || dataMap.size() == 0) {
            Logger.e("h5UT", "dataMap is empty");
            return;
        }
        String funcType = dataMap.get("functype");
        if (funcType == null) {
            Logger.e("h5UT", "funcType is null");
            return;
        }
        String lutjstype = dataMap.get("utjstype");
        if (lutjstype == null || lutjstype.equals("0") || lutjstype.equals("1")) {
            dataMap.remove("functype");
            Date opTime = new Date();
            if (funcType.equals("2001")) {
                h5Page(opTime, dataMap, view);
            } else {
                h5Ctrl(funcType, opTime, dataMap);
            }
        } else {
            Logger.e("h5UT", "utjstype should be 1 or 0 or null");
        }
    }

    public void h5UT2(Map<String, String> dataMap, Object view) {
        if (dataMap == null || dataMap.size() == 0) {
            Logger.e("h5UT", "dataMap is empty");
            return;
        }
        String funcType = dataMap.remove("functype");
        if (funcType == null) {
            Logger.e("h5UT", "funcType is null");
        } else if (funcType.equals("page")) {
            dataMap.remove("funcId");
            h5Page2(dataMap, view);
        } else if (funcType.equals("ctrl")) {
            h5Ctrl2(dataMap);
        }
    }

    private void h5Ctrl2(Map<String, String> dataMap) {
        if (dataMap != null && dataMap.size() != 0) {
            int eventId = -1;
            try {
                eventId = Integer.parseInt(dataMap.remove("funcId"));
            } catch (Throwable th) {
            }
            if (eventId != -1) {
                String pageName = dataMap.remove("url");
                if (pageName == null || StringUtils.isEmpty(pageName)) {
                    Logger.i("h5Ctrl", "pageName is null,return");
                    return;
                }
                String logKey = dataMap.remove("logkey");
                if (logKey == null || StringUtils.isEmpty(logKey)) {
                    Logger.i("h5Ctrl", "logkey is null,return");
                    return;
                }
                UTOriginalCustomHitBuilder lOchb = new UTOriginalCustomHitBuilder(pageName, eventId, logKey, (String) null, (String) null, dataMap);
                UTTracker lTracker = UTAnalytics.getInstance().getDefaultTracker();
                if (lTracker != null) {
                    lTracker.send(lOchb.build());
                    return;
                }
                Logger.e("h5Ctrl event error", "Fatal Error,must call setRequestAuthentication method first.");
            }
        }
    }

    private void h5Page2(Map<String, String> dataMap, Object view) {
        Map<String, String> pageProperties;
        Logger.d();
        if (dataMap == null || dataMap.size() == 0) {
            Logger.i("h5Page2", "dataMap is null or empty,return");
            return;
        }
        String pageName = dataMap.remove("url");
        if (pageName == null || StringUtils.isEmpty(pageName)) {
            Logger.i("h5Page2", "pageName is null,return");
            return;
        }
        String prePageName = UTVariables.getInstance().getRefPage();
        int lEventID = WVEventId.PAGE_onCreateWindow;
        if (UTPageHitHelper.getInstance().isH52001(view)) {
            lEventID = 2001;
        }
        if (2001 == lEventID) {
            UTVariables.getInstance().setRefPage(pageName);
            pageProperties = UTPageHitHelper.getInstance().getNextPageProperties(view);
            if (pageProperties == null || pageProperties.size() <= 0) {
                pageProperties = dataMap;
            } else {
                pageProperties.putAll(dataMap);
            }
            if (view instanceof Activity) {
                String _h5url = dataMap.get("_h5url");
                String nextPageUtparam = "";
                if (pageProperties != null) {
                    nextPageUtparam = pageProperties.get("utparam-url");
                }
                Map<String, String> encodedMap = getUTPageStateObjectSpmMap(view, dataMap, pageName, _h5url, nextPageUtparam);
                if (encodedMap != null) {
                    pageProperties.putAll(encodedMap);
                }
            }
        } else {
            pageProperties = dataMap;
        }
        UTOriginalCustomHitBuilder lHitBuilder = new UTOriginalCustomHitBuilder(pageName, lEventID, prePageName, (String) null, (String) null, pageProperties);
        try {
            String lTPKString = UTTPKBiz.getInstance().getTpkString(Uri.parse(dataMap.get("_h5url")), (Map<String, String>) null);
            if (!StringUtils.isEmpty(lTPKString)) {
                lHitBuilder.setProperty("_tpk", lTPKString);
            }
        } catch (Throwable e) {
            Logger.e((String) null, e, new Object[0]);
        }
        UTTracker lTracker = UTAnalytics.getInstance().getDefaultTracker();
        if (lTracker != null) {
            lTracker.send(lHitBuilder.build());
        } else {
            Logger.e("h5Page event error", "Fatal Error,must call setRequestAuthentication method first.");
        }
        UTPageHitHelper.getInstance().setH5Called(view);
    }

    private void h5Page(Date opTime, Map<String, String> dataMap, Object webview) {
        Logger.d();
        if (dataMap != null && dataMap.size() != 0) {
            String url = dataMap.get("url");
            String pageName = getH5PageName(dataMap.get("urlpagename"), url);
            if (pageName == null || StringUtils.isEmpty(pageName)) {
                Logger.e("h5Page", "pageName is null,return");
                return;
            }
            Map<String, String> args = null;
            String utjstype = dataMap.get("utjstype");
            dataMap.remove("utjstype");
            if (utjstype == null || utjstype.equals("0")) {
                args = h5PageParseArgsWithAplus(dataMap);
            } else if (utjstype.equals("1")) {
                args = h5PageParseArgsWithOutAplus(dataMap);
            }
            int lEventID = WVEventId.PAGE_onCreateWindow;
            if (UTPageHitHelper.getInstance().isH52001(webview)) {
                lEventID = 2001;
            }
            UTOriginalCustomHitBuilder lHitBuilder = new UTOriginalCustomHitBuilder(pageName, lEventID, UTVariables.getInstance().getRefPage(), (String) null, (String) null, args);
            if (2001 == lEventID) {
                UTVariables.getInstance().setRefPage(pageName);
                Map<String, String> lNextPageProperties = UTPageHitHelper.getInstance().getNextPageProperties(webview);
                if (lNextPageProperties != null && lNextPageProperties.size() > 0) {
                    lHitBuilder.setProperties(lNextPageProperties);
                }
                if (webview instanceof Activity) {
                    String nextPageUtparam = "";
                    if (lNextPageProperties != null) {
                        nextPageUtparam = lNextPageProperties.get("utparam-url");
                    }
                    lHitBuilder.setProperties(getUTPageStateObjectSpmMap(webview, dataMap, url, nextPageUtparam));
                }
            }
            try {
                String lTPKString = UTTPKBiz.getInstance().getTpkString(Uri.parse(url), (Map<String, String>) null);
                if (!StringUtils.isEmpty(lTPKString)) {
                    lHitBuilder.setProperty("_tpk", lTPKString);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
            UTTracker lTracker = UTAnalytics.getInstance().getDefaultTracker();
            if (lTracker != null) {
                lTracker.send(lHitBuilder.build());
            } else {
                Logger.e("h5Page event error", "Fatal Error,must call setRequestAuthentication method first.");
            }
            UTPageHitHelper.getInstance().setH5Called(webview);
        }
    }

    private void h5Ctrl(String eventIdStr, Date oTime, Map<String, String> dataMap) {
        int eventId = -1;
        try {
            eventId = Integer.parseInt(eventIdStr);
        } catch (Throwable th) {
        }
        if (eventId != -1 && dataMap != null && dataMap.size() != 0) {
            String pageName = getH5PageName(dataMap.get("urlpagename"), dataMap.get("url"));
            if (pageName == null || StringUtils.isEmpty(pageName)) {
                Logger.i("h5Ctrl", "pageName is null,return");
                return;
            }
            String logKey = dataMap.get("logkey");
            if (logKey == null || StringUtils.isEmpty(logKey)) {
                Logger.i("h5Ctrl", "logkey is null,return");
                return;
            }
            Map<String, String> args = null;
            String utjstype = dataMap.get("utjstype");
            dataMap.remove("utjstype");
            if (utjstype == null || utjstype.equals("0")) {
                args = h5CtrlParseArgsWithAplus(dataMap);
            } else if (utjstype.equals("1")) {
                args = h5CtrlParseArgsWithOutAplus(dataMap);
            }
            UTOriginalCustomHitBuilder lOchb = new UTOriginalCustomHitBuilder(pageName, eventId, logKey, (String) null, (String) null, args);
            UTTracker lTracker = UTAnalytics.getInstance().getDefaultTracker();
            if (lTracker != null) {
                lTracker.send(lOchb.build());
                return;
            }
            Logger.e("h5Ctrl event error", "Fatal Error,must call setRequestAuthentication method first.");
        }
    }

    private Map<String, String> h5PageParseArgsWithAplus(Map<String, String> dataMap) {
        Map<String, String> lArgsMap;
        if (dataMap == null || dataMap.size() == 0) {
            return null;
        }
        HashMap<String, String> lArgs = new HashMap<>();
        String url = dataMap.get("url");
        lArgs.put("_h5url", url == null ? "" : url);
        if (url != null) {
            try {
                int lUrlPos = url.indexOf(63);
                if (lUrlPos > 0 && (lArgsMap = JTrack.Page.getArgsMap(url.substring(0, lUrlPos), Uri.parse(url))) != null) {
                    lArgs.putAll(lArgsMap);
                }
            } catch (Throwable th) {
            }
        }
        if (url != null) {
            Uri lUrl = Uri.parse(url);
            String spm = lUrl.getQueryParameter(SPMConfig.SPM);
            if (spm == null || StringUtils.isEmpty(spm)) {
                lArgs.put(SPMConfig.SPM, Constants.PARAM_OUTER_SPM_NONE);
            } else {
                lArgs.put(SPMConfig.SPM, spm);
            }
            String scm = lUrl.getQueryParameter(BaseConfig.INTENT_KEY_SCM);
            if (scm != null && !StringUtils.isEmpty(scm)) {
                lArgs.put(BaseConfig.INTENT_KEY_SCM, scm);
            }
            String pg1stepk = lUrl.getQueryParameter("pg1stepk");
            if (pg1stepk != null && !StringUtils.isEmpty(pg1stepk)) {
                lArgs.put("pg1stepk", pg1stepk);
            }
            if (!StringUtils.isEmpty(lUrl.getQueryParameter(BaseConfig.INTENT_KEY_MODULE_POING))) {
                lArgs.put("issb", "1");
            }
        } else {
            lArgs.put(SPMConfig.SPM, Constants.PARAM_OUTER_SPM_NONE);
        }
        String spmcnt = dataMap.get("spmcnt");
        if (spmcnt == null) {
            spmcnt = "";
        }
        lArgs.put("_spmcnt", spmcnt);
        String spmpre = dataMap.get("spmpre");
        if (spmpre == null) {
            spmpre = "";
        }
        lArgs.put("_spmpre", spmpre);
        String lzsid = dataMap.get("lzsid");
        if (lzsid == null) {
            lzsid = "";
        }
        lArgs.put("_lzsid", lzsid);
        String eargs = dataMap.get("extendargs");
        if (eargs == null) {
            eargs = "";
        }
        lArgs.put("_h5ea", eargs);
        String cna = dataMap.get("cna");
        if (cna == null) {
            cna = "";
        }
        lArgs.put("_cna", cna);
        lArgs.put("_ish5", "1");
        return lArgs;
    }

    private Map<String, String> h5PageParseArgsWithOutAplus(Map<String, String> dataMap) {
        if (dataMap == null || dataMap.size() == 0) {
            return null;
        }
        HashMap<String, String> lArgs = new HashMap<>();
        String url = dataMap.get("url");
        if (url == null) {
            url = "";
        }
        lArgs.put("_h5url", url);
        String extendargs = dataMap.get("extendargs");
        if (extendargs == null) {
            extendargs = "";
        }
        lArgs.put("_h5ea", extendargs);
        lArgs.put("_ish5", "1");
        return lArgs;
    }

    private Map<String, String> h5CtrlParseArgsWithAplus(Map<String, String> dataMap) {
        if (dataMap == null || dataMap.size() == 0) {
            return null;
        }
        Map<String, String> lArgs = new HashMap<>();
        String logkeyargs = dataMap.get("logkeyargs");
        if (logkeyargs == null) {
            logkeyargs = "";
        }
        lArgs.put("_lka", logkeyargs);
        String cna = dataMap.get("cna");
        if (cna == null) {
            cna = "";
        }
        lArgs.put("_cna", cna);
        String extendargs = dataMap.get("extendargs");
        if (extendargs == null) {
            extendargs = "";
        }
        lArgs.put("_h5ea", extendargs);
        lArgs.put("_ish5", "1");
        return lArgs;
    }

    private Map<String, String> h5CtrlParseArgsWithOutAplus(Map<String, String> dataMap) {
        if (dataMap == null || dataMap.size() == 0) {
            return null;
        }
        Map<String, String> lArgs = new HashMap<>();
        String extendargs = dataMap.get("extendargs");
        if (extendargs == null) {
            extendargs = "";
        }
        lArgs.put("_h5ea", extendargs);
        lArgs.put("_ish5", "1");
        return lArgs;
    }

    private String getH5PageName(String urlPageName, String url) {
        if (urlPageName != null && !StringUtils.isEmpty(urlPageName)) {
            return urlPageName;
        }
        if (StringUtils.isEmpty(url)) {
            return "";
        }
        int pos = url.indexOf(WVUtils.URL_DATA_CHAR);
        if (pos == -1) {
            return url;
        }
        return url.substring(0, pos);
    }

    private Map<String, String> getUTPageStateObjectSpmMap(Object object, Map<String, String> dataMap, String url, String nextPageUtparam) {
        return getUTPageStateObjectSpmMap(object, dataMap, url, "", nextPageUtparam, false);
    }

    private Map<String, String> getUTPageStateObjectSpmMap(Object object, Map<String, String> dataMap, String url, String h5url, String nextPageUtparam) {
        return getUTPageStateObjectSpmMap(object, dataMap, url, h5url, nextPageUtparam, true);
    }

    private Map<String, String> getUTPageStateObjectSpmMap(Object object, Map<String, String> dataMap, String url, String h5url, String nextPageUtparam, boolean isH5UT2) {
        String spmcnt;
        String utparamcnt;
        String spmpre;
        String scmpre;
        String utparampre;
        UTPageHitHelper.UTPageStateObject lPageStateObject = UTPageHitHelper.getInstance().getOrNewUTPageStateObject(object);
        if (lPageStateObject == null) {
            return null;
        }
        String spm = null;
        String utparam = null;
        String scm = null;
        if (isH5UT2) {
            spmcnt = dataMap.get("spm-cnt");
            utparamcnt = dataMap.get("utparam-cnt");
            spm = dataMap.get(Constants.PARAM_OUTER_SPM_URL);
            utparam = dataMap.get("utparam-url");
            scm = dataMap.get(BaseConfig.INTENT_KEY_SCM);
            try {
                if (!TextUtils.isEmpty(h5url)) {
                    Uri lUrl_h5 = Uri.parse(h5url);
                    if (TextUtils.isEmpty(spm)) {
                        spm = lUrl_h5.getQueryParameter(SPMConfig.SPM);
                    }
                    if (TextUtils.isEmpty(utparam)) {
                        utparam = lUrl_h5.getQueryParameter("utparam");
                    }
                    if (TextUtils.isEmpty(scm)) {
                        scm = lUrl_h5.getQueryParameter(BaseConfig.INTENT_KEY_SCM);
                    }
                }
            } catch (Exception e) {
            }
            try {
                if (!TextUtils.isEmpty(url)) {
                    Uri lUrl = Uri.parse(url);
                    if (TextUtils.isEmpty(spm)) {
                        spm = lUrl.getQueryParameter(SPMConfig.SPM);
                    }
                    if (TextUtils.isEmpty(utparam)) {
                        utparam = lUrl.getQueryParameter("utparam");
                    }
                    if (TextUtils.isEmpty(scm)) {
                        scm = lUrl.getQueryParameter(BaseConfig.INTENT_KEY_SCM);
                    }
                }
            } catch (Exception e2) {
            }
        } else {
            spmcnt = dataMap.get("spmcnt");
            utparamcnt = dataMap.get("utparamcnt");
            try {
                Uri lUrl2 = Uri.parse(url);
                spm = lUrl2.getQueryParameter(SPMConfig.SPM);
                utparam = lUrl2.getQueryParameter("utparam");
                scm = lUrl2.getQueryParameter(BaseConfig.INTENT_KEY_SCM);
            } catch (Exception e3) {
            }
        }
        if (TextUtils.isEmpty(lPageStateObject.mSpmUrl)) {
            lPageStateObject.mSpmUrl = spm;
        }
        if (TextUtils.isEmpty(lPageStateObject.mUtparamUrl)) {
            lPageStateObject.mUtparamUrl = utparam;
        }
        if (TextUtils.isEmpty(lPageStateObject.mScmUrl)) {
            lPageStateObject.mScmUrl = scm;
        }
        if (!lPageStateObject.mIsH5Page) {
            lPageStateObject.mSpmCnt = spmcnt;
            lPageStateObject.mSpmUrl = spm;
            if (!TextUtils.isEmpty(UTPageHitHelper.getInstance().getLastCacheKey())) {
                lPageStateObject.mSpmPre = UTPageHitHelper.getInstance().getLastCacheKeySpmUrl();
            }
            lPageStateObject.mScmUrl = scm;
            if (!TextUtils.isEmpty(UTPageHitHelper.getInstance().getLastCacheKey())) {
                lPageStateObject.mScmPre = UTPageHitHelper.getInstance().getLastCacheKeyScmUrl();
            }
            lPageStateObject.mIsBack = true;
            lPageStateObject.mUtparamCnt = utparamcnt;
            utparam = UTPageHitHelper.getInstance().refreshUtParam(UTPageHitHelper.getInstance().refreshUtParam(utparam, nextPageUtparam), UTPageHitHelper.getInstance().getLastCacheKeyUtParamCnt());
            lPageStateObject.mUtparamUrl = utparam;
            if (!TextUtils.isEmpty(UTPageHitHelper.getInstance().getLastCacheKeyUtParam())) {
                lPageStateObject.mUtparamPre = UTPageHitHelper.getInstance().getLastCacheKeyUtParam();
            }
            UTPageHitHelper.getInstance().setLastCacheKey(_getPageEventObjectCacheKey(object));
            UTPageHitHelper.getInstance().setLastCacheKeySpmUrl(spm);
            UTPageHitHelper.getInstance().setLastCacheKeyScmUrl(scm);
            UTPageHitHelper.getInstance().setLastCacheKeyUtParam(utparam);
            UTPageHitHelper.getInstance().setLastCacheKeyUtParamCnt(utparamcnt);
            Logger.d("h5Page", "mLastCacheKey:" + UTPageHitHelper.getInstance().getLastCacheKey() + ",mLastCacheKeySpmUrl:" + UTPageHitHelper.getInstance().getLastCacheKeySpmUrl() + ",mLastCacheKeyUtParam:" + UTPageHitHelper.getInstance().getLastCacheKeyUtParam() + ",mLastCacheKeyUtParamCnt:" + UTPageHitHelper.getInstance().getLastCacheKeyUtParamCnt());
            Logger.d("h5Page", "UTHybridHelper lPageStateObject:" + lPageStateObject.getPageStatMap(false).toString());
        }
        lPageStateObject.mIsH5Page = true;
        if (isH5UT2) {
            spmpre = dataMap.get("spm-pre");
            Logger.d("h5Page", "UTHybridHelper spm-pre:" + spmpre);
            if (TextUtils.isEmpty(spmpre)) {
                spmpre = lPageStateObject.mSpmPre;
                Logger.d("h5Page", "UTHybridHelper mSpmPre:" + spmpre);
            }
            scmpre = dataMap.get("scm-pre");
            Logger.d("h5Page", "UTHybridHelper scm-pre:" + scmpre);
            if (TextUtils.isEmpty(scmpre)) {
                scmpre = lPageStateObject.mScmPre;
                Logger.d("h5Page", "UTHybridHelper mScmPre:" + scmpre);
            }
            utparampre = dataMap.get("utparam-pre");
            Logger.d("h5Page", "UTHybridHelper utparam-pre:" + utparampre);
            if (TextUtils.isEmpty(utparampre)) {
                utparampre = lPageStateObject.mUtparamPre;
                Logger.d("h5Page", "UTHybridHelper mUtparamPre:" + utparampre);
            }
        } else {
            String spmpre2 = dataMap.get("spmpre");
            Logger.d("h5Page", "UTHybridHelper _spmpre:" + spmpre2);
            if (TextUtils.isEmpty(spmpre2)) {
                spmpre2 = lPageStateObject.mSpmPre;
                Logger.d("h5Page", "UTHybridHelper mSpmPre:" + spmpre2);
            }
            String scmpre2 = dataMap.get("scmpre");
            Logger.d("h5Page", "UTHybridHelper _scmpre:" + scmpre2);
            if (TextUtils.isEmpty(scmpre2)) {
                scmpre2 = lPageStateObject.mScmPre;
                Logger.d("h5Page", "UTHybridHelper mScmPre:" + scmpre2);
            }
            utparampre = dataMap.get("utparampre");
            Logger.d("h5Page", "UTHybridHelper _utparampre:" + utparampre);
            if (TextUtils.isEmpty(utparampre)) {
                utparampre = lPageStateObject.mUtparamPre;
                Logger.d("h5Page", "UTHybridHelper mUtparamPre:" + utparampre);
            }
        }
        Map<String, String> map = new HashMap<>();
        if (!TextUtils.isEmpty(spmcnt)) {
            map.put("spm-cnt", spmcnt);
        }
        if (!TextUtils.isEmpty(spm)) {
            map.put(Constants.PARAM_OUTER_SPM_URL, spm);
        }
        if (!TextUtils.isEmpty(spmpre)) {
            map.put("spm-pre", spmpre);
        }
        if (!TextUtils.isEmpty(scm)) {
            map.put(BaseConfig.INTENT_KEY_SCM, scm);
        }
        if (!TextUtils.isEmpty(scmpre)) {
            map.put("scm-pre", scmpre);
        }
        if (!TextUtils.isEmpty(utparamcnt)) {
            map.put("utparam-cnt", utparamcnt);
        }
        if (!TextUtils.isEmpty(utparam)) {
            map.put("utparam-url", utparam);
        }
        if (TextUtils.isEmpty(utparampre)) {
            return map;
        }
        map.put("utparam-pre", utparampre);
        return map;
    }

    private String _getPageEventObjectCacheKey(Object aPageObject) {
        String lPageName;
        if (aPageObject instanceof String) {
            lPageName = (String) aPageObject;
        } else {
            lPageName = aPageObject.getClass().getSimpleName();
        }
        return lPageName + aPageObject.hashCode();
    }
}
