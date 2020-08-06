package com.ut.mini;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.SystemClock;
import android.taobao.windvane.jsbridge.utils.WVUtils;
import android.text.TextUtils;
import com.alibaba.analytics.core.ClientVariables;
import com.alibaba.analytics.core.config.UTTPKBiz;
import com.alibaba.analytics.core.model.LogField;
import com.alibaba.analytics.utils.Logger;
import com.alibaba.analytics.utils.StringUtils;
import com.alibaba.fastjson.JSON;
import com.bftv.fui.constantplugin.Constant;
import com.taobao.detail.domain.tuwen.TuwenConstants;
import com.taobao.ju.track.constants.Constants;
import com.ut.mini.UTHitBuilders;
import com.ut.mini.extend.JTrackExtend;
import com.ut.mini.extend.UTExtendSwitch;
import com.ut.mini.module.UTOperationStack;
import com.yunos.tv.core.config.SPMConfig;
import com.yunos.tvtaobao.biz.common.BaseConfig;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Queue;

public class UTPageHitHelper {
    private static final String FORCE_SPM_CNT = "force-spm-cnt";
    private static final String FORCE_SPM_URL = "force-spm-url";
    private static final int MAX_SKIP_CLEAR_PAGE_OBJECT_CACHE_CAPACITY = 100;
    private static final int MAX_SPM_OBJECT_CACHE_CAPACITY = 50;
    static final String SKIPBK = "skipbk";
    static final String UTPARAM_CNT = "utparam-cnt";
    private static ArrayList<PageChangeListener> mPageChangerListeners = new ArrayList<>();
    private static UTPageHitHelper s_instance = new UTPageHitHelper();
    private Map<String, String> mBackupNextPageProperties = null;
    private Queue<String> mClearUTPageStateObjectList = new LinkedList();
    private String mCurPage = null;
    private String mCurrentPageCacheKey = null;
    private boolean mIsTurnOff = false;
    private String mLastCacheKey = null;
    private String mLastCacheKeyScmUrl = null;
    private String mLastCacheKeySpmUrl = null;
    private String mLastCacheKeyUtParam = null;
    private String mLastCacheKeyUtParamCnt = null;
    private Map<String, String> mNextPageProperties = new HashMap();
    private boolean mNextPageSkipBack = false;
    private Map<String, UTPageEventObject> mPageEventObjects = new HashMap();
    private Map<String, String> mPageProperties = new HashMap();
    private Map<String, UTPageStateObject> mPageStateObjects = new HashMap();
    private Queue<String> mSPMObjectList = new LinkedList();
    private Map<String, String> mSPMObjectMap = new HashMap();
    private Queue<UTPageEventObject> mSkipClearPageObjectList = new LinkedList();

    public interface PageChangeListener {
        void onPageAppear(Object obj);

        void onPageDisAppear(Object obj);
    }

    public static class UTPageStateObject {
        public boolean mIsBack = false;
        public boolean mIsFrame = false;
        public boolean mIsH5Page = false;
        boolean mIsSkipBack = false;
        boolean mIsSkipBackForever = false;
        public boolean mIsSwitchBackground = false;
        public String mScmPre = null;
        public String mScmUrl = null;
        public String mSpmCnt = null;
        public String mSpmPre = null;
        public String mSpmUrl = null;
        public String mUtparamCnt = null;
        public String mUtparamPre = null;
        public String mUtparamUrl = null;

        public Map<String, String> getPageStatMap(boolean isFromFragment) {
            Map<String, String> map = new HashMap<>();
            if (!TextUtils.isEmpty(this.mSpmCnt)) {
                map.put("spm-cnt", this.mSpmCnt);
            }
            if (!TextUtils.isEmpty(this.mSpmUrl)) {
                map.put(Constants.PARAM_OUTER_SPM_URL, this.mSpmUrl);
            }
            if (!TextUtils.isEmpty(this.mSpmPre)) {
                map.put("spm-pre", this.mSpmPre);
            }
            if (!TextUtils.isEmpty(this.mScmPre)) {
                map.put("scm-pre", this.mScmPre);
            }
            if (this.mIsSwitchBackground) {
                map.put("isbf", "1");
            } else if (this.mIsFrame && isFromFragment) {
                map.put("isfm", "1");
            } else if (this.mIsBack) {
                map.put("ut_isbk", "1");
            }
            if (!TextUtils.isEmpty(this.mUtparamCnt)) {
                map.put(UTPageHitHelper.UTPARAM_CNT, this.mUtparamCnt);
            }
            if (!TextUtils.isEmpty(this.mUtparamUrl)) {
                map.put("utparam-url", this.mUtparamUrl);
            }
            if (!TextUtils.isEmpty(this.mUtparamPre)) {
                map.put("utparam-pre", this.mUtparamPre);
            }
            return map;
        }
    }

    private UTPageStateObject copyUTPageStateObject(UTPageStateObject pageStateObject) {
        if (pageStateObject == null) {
            return null;
        }
        UTPageStateObject utPageStateObject = new UTPageStateObject();
        utPageStateObject.mSpmCnt = pageStateObject.mSpmCnt;
        utPageStateObject.mSpmUrl = pageStateObject.mSpmUrl;
        utPageStateObject.mSpmPre = pageStateObject.mSpmPre;
        utPageStateObject.mIsBack = pageStateObject.mIsBack;
        utPageStateObject.mIsFrame = pageStateObject.mIsFrame;
        utPageStateObject.mIsSwitchBackground = pageStateObject.mIsSwitchBackground;
        utPageStateObject.mUtparamCnt = pageStateObject.mUtparamCnt;
        utPageStateObject.mUtparamUrl = pageStateObject.mUtparamUrl;
        utPageStateObject.mUtparamPre = pageStateObject.mUtparamPre;
        utPageStateObject.mScmUrl = pageStateObject.mScmUrl;
        utPageStateObject.mScmPre = pageStateObject.mScmPre;
        utPageStateObject.mIsSkipBack = pageStateObject.mIsSkipBack;
        utPageStateObject.mIsSkipBackForever = pageStateObject.mIsSkipBackForever;
        return utPageStateObject;
    }

    public void setLastCacheKey(String lastCacheKey) {
        this.mLastCacheKey = lastCacheKey;
    }

    public void setLastCacheKeySpmUrl(String lastCacheKeySpmUrl) {
        this.mLastCacheKeySpmUrl = lastCacheKeySpmUrl;
    }

    public void setLastCacheKeyScmUrl(String lastCacheKeyScmUrl) {
        this.mLastCacheKeyScmUrl = lastCacheKeyScmUrl;
    }

    public void setLastCacheKeyUtParam(String lastCacheKeyUtParam) {
        this.mLastCacheKeyUtParam = lastCacheKeyUtParam;
    }

    public void setLastCacheKeyUtParamCnt(String lastCacheKeyUtParamCnt) {
        this.mLastCacheKeyUtParamCnt = lastCacheKeyUtParamCnt;
    }

    public String getLastCacheKey() {
        return this.mLastCacheKey;
    }

    public String getLastCacheKeySpmUrl() {
        return this.mLastCacheKeySpmUrl;
    }

    public String getLastCacheKeyScmUrl() {
        return this.mLastCacheKeyScmUrl;
    }

    public String getLastCacheKeyUtParam() {
        return this.mLastCacheKeyUtParam;
    }

    public String getLastCacheKeyUtParamCnt() {
        return this.mLastCacheKeyUtParamCnt;
    }

    public static class UTPageEventObject {
        private String mCacheKey = null;
        private boolean mIsH5Called = false;
        private boolean mIsPageAppearCalled = false;
        private boolean mIsSkipPage = false;
        private Map<String, String> mNextPageProperties = null;
        private long mPageAppearTimestamp = 0;
        private String mPageName = null;
        private Map<String, String> mPageProperties = new HashMap();
        private UTPageStatus mPageStatus = null;
        private int mPageStatusCode = 0;
        private long mPageStayTimstamp = 0;
        private Uri mPageUrl = null;
        private String mRefPage = null;

        public void setNextPageProperties(Map<String, String> aNextPageProperties) {
            this.mNextPageProperties = aNextPageProperties;
        }

        public Map<String, String> getNextPageProperties() {
            return this.mNextPageProperties;
        }

        public void setCacheKey(String aCacheKey) {
            this.mCacheKey = aCacheKey;
        }

        public String getCacheKey() {
            return this.mCacheKey;
        }

        public void resetPropertiesWithoutSkipFlagAndH5Flag() {
            this.mPageProperties = new HashMap();
            this.mPageAppearTimestamp = 0;
            this.mPageStayTimstamp = 0;
            this.mPageUrl = null;
            this.mPageName = null;
            this.mRefPage = null;
            if (this.mPageStatus == null || this.mPageStatus != UTPageStatus.UT_H5_IN_WebView) {
                this.mPageStatus = null;
            }
            this.mIsPageAppearCalled = false;
            this.mIsH5Called = false;
            this.mPageStatusCode = 0;
            this.mNextPageProperties = null;
        }

        public boolean isH5Called() {
            return this.mIsH5Called;
        }

        public void setH5Called() {
            this.mIsH5Called = true;
        }

        public void setToSkipPage() {
            this.mIsSkipPage = true;
        }

        public boolean isSkipPage() {
            return this.mIsSkipPage;
        }

        public void setPageAppearCalled() {
            this.mIsPageAppearCalled = true;
        }

        public boolean isPageAppearCalled() {
            return this.mIsPageAppearCalled;
        }

        public void setPageStatus(UTPageStatus aPageStatus) {
            this.mPageStatus = aPageStatus;
        }

        public UTPageStatus getPageStatus() {
            return this.mPageStatus;
        }

        public Map<String, String> getPageProperties() {
            return this.mPageProperties;
        }

        public void setPageProperties(Map<String, String> aPageProperties) {
            this.mPageProperties = aPageProperties;
        }

        public long getPageAppearTimestamp() {
            return this.mPageAppearTimestamp;
        }

        public void setPageAppearTimestamp(long aPageAppearTimestamp) {
            this.mPageAppearTimestamp = aPageAppearTimestamp;
        }

        public long getPageStayTimstamp() {
            return this.mPageStayTimstamp;
        }

        public void setPageStayTimstamp(long aPageStayTimstamp) {
            this.mPageStayTimstamp = aPageStayTimstamp;
        }

        public Uri getPageUrl() {
            return this.mPageUrl;
        }

        public void setPageUrl(Uri aPageUrl) {
            this.mPageUrl = aPageUrl;
        }

        public void setPageName(String aPageName) {
            this.mPageName = aPageName;
        }

        public String getPageName() {
            return this.mPageName;
        }

        public void setRefPage(String aRefPage) {
            this.mRefPage = aRefPage;
        }

        public String getRefPage() {
            return this.mRefPage;
        }

        public void setPageStatusCode(int aPageStatusCode) {
            this.mPageStatusCode = aPageStatusCode;
        }

        public int getPageStatusCode() {
            return this.mPageStatusCode;
        }
    }

    public static synchronized void addPageChangerListener(PageChangeListener listener) {
        synchronized (UTPageHitHelper.class) {
            if (listener != null) {
                if (!mPageChangerListeners.contains(listener)) {
                    mPageChangerListeners.add(listener);
                }
            }
        }
    }

    static synchronized void disPathcherPageChangerEvent(int eventType, Object pageObject) {
        synchronized (UTPageHitHelper.class) {
            int size = mPageChangerListeners.size();
            for (int i = 0; i < size; i++) {
                PageChangeListener pageChangeListener = mPageChangerListeners.get(i);
                if (pageChangeListener != null) {
                    if (eventType == 0) {
                        pageChangeListener.onPageAppear(pageObject);
                    } else {
                        pageChangeListener.onPageDisAppear(pageObject);
                    }
                }
            }
        }
    }

    public static UTPageHitHelper getInstance() {
        return s_instance;
    }

    /* access modifiers changed from: package-private */
    public synchronized Map<String, String> getNextPageProperties(Object aPageObject) {
        Map<String, String> map;
        if (aPageObject != null) {
            map = _getOrNewAUTPageEventObject(aPageObject).getNextPageProperties();
        } else {
            map = null;
        }
        return map;
    }

    /* access modifiers changed from: package-private */
    public synchronized void _releaseSkipFlagAndH5FlagPageObject(UTPageEventObject aPageEventObject) {
        aPageEventObject.resetPropertiesWithoutSkipFlagAndH5Flag();
        if (!this.mSkipClearPageObjectList.contains(aPageEventObject)) {
            this.mSkipClearPageObjectList.add(aPageEventObject);
        }
        if (this.mSkipClearPageObjectList.size() > 200) {
            for (int i = 0; i < 100; i++) {
                UTPageEventObject lPageEventObject = this.mSkipClearPageObjectList.poll();
                if (lPageEventObject != null && this.mPageEventObjects.containsKey(lPageEventObject.getCacheKey())) {
                    this.mPageEventObjects.remove(lPageEventObject.getCacheKey());
                }
            }
        }
    }

    /* access modifiers changed from: package-private */
    public synchronized void _releaseSPMCacheObj(String aCacheKey) {
        if (!this.mSPMObjectList.contains(aCacheKey)) {
            this.mSPMObjectList.add(aCacheKey);
        }
        if (this.mSPMObjectList.size() > 100) {
            for (int i = 0; i < 50; i++) {
                String lPCacheKey = this.mSPMObjectList.poll();
                if (lPCacheKey != null && this.mSPMObjectMap.containsKey(lPCacheKey)) {
                    this.mSPMObjectMap.remove(lPCacheKey);
                }
            }
        }
    }

    @Deprecated
    public synchronized void turnOffAutoPageTrack() {
        this.mIsTurnOff = true;
    }

    public String getCurrentPageName() {
        return this.mCurPage;
    }

    /* access modifiers changed from: package-private */
    public void pageAppearByAuto(Activity aActivity) {
        if (!this.mIsTurnOff) {
            pageAppear(aActivity);
        }
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

    /* access modifiers changed from: package-private */
    public synchronized boolean isH52001(Object aPageObject) {
        boolean z;
        if (aPageObject != null) {
            UTPageEventObject lPEObject = _getOrNewAUTPageEventObject(aPageObject);
            if (lPEObject.getPageStatus() != null && lPEObject.getPageStatus() == UTPageStatus.UT_H5_IN_WebView) {
                z = true;
            }
        }
        z = false;
        return z;
    }

    /* access modifiers changed from: package-private */
    public synchronized void setH5Called(Object aPageObject) {
        if (aPageObject != null) {
            UTPageEventObject lPEObject = _getOrNewAUTPageEventObject(aPageObject);
            if (lPEObject.getPageStatus() != null) {
                lPEObject.setH5Called();
            }
        }
    }

    private synchronized UTPageEventObject _getOrNewAUTPageEventObject(Object aPageObject) {
        UTPageEventObject uTPageEventObject;
        String lCacheKey = _getPageEventObjectCacheKey(aPageObject);
        if (this.mPageEventObjects.containsKey(lCacheKey)) {
            uTPageEventObject = this.mPageEventObjects.get(lCacheKey);
        } else {
            UTPageEventObject lPageEventObject = new UTPageEventObject();
            this.mPageEventObjects.put(lCacheKey, lPageEventObject);
            lPageEventObject.setCacheKey(lCacheKey);
            uTPageEventObject = lPageEventObject;
        }
        return uTPageEventObject;
    }

    private synchronized void _putUTPageEventObjectToCache(String aCacheKey, UTPageEventObject aPageObject) {
        this.mPageEventObjects.put(aCacheKey, aPageObject);
    }

    private synchronized void _clearUTPageEventObjectCache(UTPageEventObject aPageEventObject) {
        if (this.mPageEventObjects.containsKey(aPageEventObject.getCacheKey())) {
            this.mPageEventObjects.remove(aPageEventObject.getCacheKey());
        }
    }

    private synchronized void _removeUTPageEventObject(Object aPageObject) {
        String lCacheKey = _getPageEventObjectCacheKey(aPageObject);
        if (this.mPageEventObjects.containsKey(lCacheKey)) {
            this.mPageEventObjects.remove(lCacheKey);
        }
    }

    @Deprecated
    public synchronized void pageAppear(Object aPageObject) {
        pageAppear(aPageObject, (String) null, false);
    }

    /* access modifiers changed from: package-private */
    public synchronized void pageAppear(Object aPageObject, String aCustomPageName, boolean aIsDonotSkipFlag) {
        UTPageStateObject lUTPageStateObject;
        UTPageStateObject lUTPageStateObject2;
        Logger.d();
        if (aPageObject != null) {
            String lPageCacheKey = _getPageEventObjectCacheKey(aPageObject);
            if (lPageCacheKey == null || !lPageCacheKey.equals(this.mCurrentPageCacheKey)) {
                if (this.mCurrentPageCacheKey != null) {
                    Logger.e("lost 2001", "Last page requires leave(" + this.mCurrentPageCacheKey + ").");
                }
                UTPageEventObject lPEObject = _getOrNewAUTPageEventObject(aPageObject);
                if (aIsDonotSkipFlag || !lPEObject.isSkipPage()) {
                    disPathcherPageChangerEvent(0, aPageObject);
                    UTOperationStack.getInstance().addAction("pageAppear:" + aPageObject.getClass().getSimpleName());
                    String lH5Url = UTVariables.getInstance().getH5Url();
                    if (lH5Url != null) {
                        UTVariables.getInstance().setBackupH5Url(lH5Url);
                        try {
                            Uri lUrl = Uri.parse(lH5Url);
                            String lSpm = lUrl.getQueryParameter(SPMConfig.SPM);
                            String lScm = lUrl.getQueryParameter(BaseConfig.INTENT_KEY_SCM);
                            this.mPageProperties.put(SPMConfig.SPM, lSpm);
                            this.mPageProperties.put(BaseConfig.INTENT_KEY_SCM, lScm);
                        } catch (Throwable e) {
                            Logger.d("", e);
                        }
                        UTVariables.getInstance().setH5Url((String) null);
                    }
                    String lCurPage = _getPageName(aPageObject);
                    if (UTExtendSwitch.bJTrackExtend) {
                        try {
                            String lPageName = JTrackExtend.getPageName(aPageObject.getClass().getSimpleName());
                            if (!TextUtils.isEmpty(lPageName)) {
                                if (lPageName.toLowerCase().endsWith(TuwenConstants.MODEL_LIST_KEY.ACTIVITY)) {
                                    lPageName = lPageName.substring(0, lPageName.length() - 8);
                                }
                                Logger.i("JTrack", "getPageName:" + lPageName);
                                lCurPage = lPageName;
                            }
                        } catch (Throwable th) {
                        }
                    }
                    if (!StringUtils.isEmpty(aCustomPageName)) {
                        lCurPage = aCustomPageName;
                    }
                    if (!StringUtils.isEmpty(lPEObject.getPageName())) {
                        lCurPage = lPEObject.getPageName();
                    }
                    this.mCurPage = lCurPage;
                    lPEObject.setPageName(lCurPage);
                    lPEObject.setPageAppearTimestamp(System.currentTimeMillis());
                    lPEObject.setPageStayTimstamp(SystemClock.elapsedRealtime());
                    lPEObject.setRefPage(UTVariables.getInstance().getRefPage());
                    lPEObject.setPageAppearCalled();
                    if (this.mNextPageProperties != null) {
                        this.mBackupNextPageProperties = this.mNextPageProperties;
                        lPEObject.setNextPageProperties(this.mNextPageProperties);
                        Map<String, String> lPageProperties = lPEObject.getPageProperties();
                        if (lPageProperties == null) {
                            lPEObject.setPageProperties(this.mNextPageProperties);
                        } else {
                            Map<String, String> lNewPageProperties = new HashMap<>();
                            lNewPageProperties.putAll(lPageProperties);
                            lNewPageProperties.putAll(this.mNextPageProperties);
                            lPEObject.setPageProperties(lNewPageProperties);
                        }
                    }
                    this.mNextPageProperties = null;
                    this.mCurrentPageCacheKey = _getPageEventObjectCacheKey(aPageObject);
                    if (this.mNextPageSkipBack && (lUTPageStateObject2 = getOrNewUTPageStateObject(aPageObject)) != null) {
                        lUTPageStateObject2.mIsSkipBack = true;
                        this.mNextPageSkipBack = false;
                    }
                    _clearUTPageEventObjectCache(lPEObject);
                    _putUTPageEventObjectToCache(_getPageEventObjectCacheKey(aPageObject), lPEObject);
                    if (aIsDonotSkipFlag && lPEObject.isSkipPage() && (lUTPageStateObject = getOrNewUTPageStateObject(aPageObject)) != null) {
                        lUTPageStateObject.mIsFrame = true;
                    }
                } else {
                    Logger.i("skip page[pageAppear]", "page name:" + aPageObject.getClass().getSimpleName());
                }
            }
        } else {
            Logger.e("pageAppear", "The page object should not be null");
        }
    }

    /* access modifiers changed from: package-private */
    public synchronized void pageAppear(Object aPageObject, String aCustomPageName) {
        pageAppear(aPageObject, aCustomPageName, false);
    }

    @Deprecated
    public synchronized void updatePageProperties(Map<String, String> aProperties) {
        if (aProperties != null) {
            this.mPageProperties.putAll(aProperties);
        }
    }

    /* access modifiers changed from: package-private */
    public synchronized void updatePageProperties(Object aPageObject, Map<String, String> aProperties) {
        if (!(aPageObject == null || aProperties == null)) {
            if (aProperties.size() != 0) {
                Map<String, String> lAMap = new HashMap<>();
                lAMap.putAll(aProperties);
                UTPageEventObject lPEObject = _getOrNewAUTPageEventObject(aPageObject);
                Map<String, String> lPageProperties = lPEObject.getPageProperties();
                if (lPageProperties == null) {
                    lPEObject.setPageProperties(lAMap);
                } else {
                    Map<String, String> lNewPageProperties = new HashMap<>();
                    lNewPageProperties.putAll(lPageProperties);
                    lNewPageProperties.putAll(lAMap);
                    lPEObject.setPageProperties(lNewPageProperties);
                }
            }
        }
        Logger.e("", "failed to update project properties");
    }

    public Map<String, String> getPageProperties(Object aPageObject) {
        if (aPageObject == null) {
            return null;
        }
        Map<String, String> ret = new HashMap<>();
        if (this.mPageProperties != null) {
            ret.putAll(this.mPageProperties);
        }
        Map<String, String> lPageProperties = _getOrNewAUTPageEventObject(aPageObject).getPageProperties();
        if (lPageProperties == null) {
            return ret;
        }
        ret.putAll(lPageProperties);
        return ret;
    }

    /* access modifiers changed from: package-private */
    public synchronized void updatePageUtparam(Object aPageObject, String aPageUtparam) {
        if (aPageObject != null) {
            if (!StringUtils.isEmpty(aPageUtparam)) {
                Map<String, String> pageProperties = getPageProperties(aPageObject);
                String pageUtparamCnt = "";
                if (pageProperties != null) {
                    pageUtparamCnt = pageProperties.get(UTPARAM_CNT);
                }
                String targetPageUtparamCnt = refreshUtParam(aPageUtparam, pageUtparamCnt);
                if (!TextUtils.isEmpty(targetPageUtparamCnt)) {
                    Map<String, String> properties = new HashMap<>();
                    properties.put(UTPARAM_CNT, targetPageUtparamCnt);
                    updatePageProperties(aPageObject, properties);
                }
            }
        }
    }

    /* access modifiers changed from: package-private */
    public synchronized void updatePageName(Object aPageObject, String aPageName) {
        if (aPageObject != null) {
            if (!StringUtils.isEmpty(aPageName)) {
                _getOrNewAUTPageEventObject(aPageObject).setPageName(aPageName);
                this.mCurPage = aPageName;
            }
        }
    }

    /* access modifiers changed from: package-private */
    public synchronized void updatePageUrl(Object aPageObject, Uri aUrl) {
        if (aPageObject != null) {
            _getOrNewAUTPageEventObject(aPageObject).setPageUrl(aUrl);
        }
    }

    public synchronized String getPageUrl(Object aPageObject) {
        String str = null;
        synchronized (this) {
            if (aPageObject != null) {
                UTPageEventObject lPEObject = _getOrNewAUTPageEventObject(aPageObject);
                if (!(lPEObject == null || lPEObject.getPageUrl() == null)) {
                    str = lPEObject.getPageUrl().toString();
                }
            }
        }
        return str;
    }

    /* access modifiers changed from: package-private */
    public synchronized void updatePageStatus(Object aPageObject, UTPageStatus aPageStatus) {
        if (aPageObject != null) {
            _getOrNewAUTPageEventObject(aPageObject).setPageStatus(aPageStatus);
        }
    }

    /* access modifiers changed from: package-private */
    public synchronized void updateNextPageProperties(Map<String, String> aProperties) {
        if (aProperties != null) {
            Map<String, String> lAMap = new HashMap<>();
            lAMap.putAll(aProperties);
            if (this.mNextPageProperties == null) {
                this.mNextPageProperties = lAMap;
            } else {
                String pageUtparam = this.mNextPageProperties.get("utparam-url");
                this.mNextPageProperties = lAMap;
                if (!TextUtils.isEmpty(pageUtparam)) {
                    this.mNextPageProperties.put("utparam-url", pageUtparam);
                }
            }
        }
    }

    /* access modifiers changed from: package-private */
    public synchronized void updateNextPageUtparam(String aPageUtparam) {
        if (!TextUtils.isEmpty(aPageUtparam)) {
            String pageUtparam = "";
            if (this.mNextPageProperties != null) {
                pageUtparam = this.mNextPageProperties.get("utparam-url");
            } else {
                this.mNextPageProperties = new HashMap();
            }
            String targetPageUtparam = refreshUtParam(aPageUtparam, pageUtparam);
            if (!TextUtils.isEmpty(targetPageUtparam)) {
                Map<String, String> properties = new HashMap<>();
                properties.put("utparam-url", targetPageUtparam);
                this.mNextPageProperties.putAll(properties);
            }
        }
    }

    /* access modifiers changed from: package-private */
    public synchronized void setPageStatusCode(Object aPageObject, int aPageStatusCode) {
        if (aPageObject != null) {
            _getOrNewAUTPageEventObject(aPageObject).setPageStatusCode(aPageStatusCode);
        }
    }

    /* access modifiers changed from: package-private */
    public void pageDisAppearByAuto(Activity aActivity) {
        if (!this.mIsTurnOff) {
            pageDisAppear(aActivity, UTAnalytics.getInstance().getDefaultTracker());
        }
    }

    /* access modifiers changed from: package-private */
    public synchronized void skipPage(Object aPageObject) {
        if (aPageObject != null) {
            _getOrNewAUTPageEventObject(aPageObject).setToSkipPage();
        }
    }

    /* access modifiers changed from: package-private */
    public synchronized void skipBack(Object aPageObject) {
        if (aPageObject != null) {
            UTPageStateObject lPageStateObject = getOrNewUTPageStateObject(aPageObject);
            if (lPageStateObject != null) {
                lPageStateObject.mIsSkipBack = true;
            }
        }
    }

    /* access modifiers changed from: package-private */
    public synchronized void skipNextPageBack() {
        this.mNextPageSkipBack = true;
    }

    /* access modifiers changed from: package-private */
    public synchronized void skipBackForever(Object aPageObject, boolean skipback) {
        if (aPageObject != null) {
            UTPageStateObject lPageStateObject = getOrNewUTPageStateObject(aPageObject);
            if (lPageStateObject != null) {
                lPageStateObject.mIsSkipBackForever = skipback;
            }
        }
    }

    private void _clearPageDisAppearContext() {
        this.mPageProperties = new HashMap();
        this.mCurrentPageCacheKey = null;
        this.mCurPage = null;
        this.mBackupNextPageProperties = null;
        UTVariables.getInstance().setBackupH5Url((String) null);
    }

    @Deprecated
    public synchronized void pageDisAppear(Object aPageObject, UTTracker aTrackerInstance) {
        Logger.d();
        if (aPageObject == null) {
            Logger.e("pageDisAppear", "The page object should not be null");
        } else if (this.mCurrentPageCacheKey == null) {
            Logger.e("pageDisAppear", "UT has already recorded the page disappear event on this page");
        } else {
            UTPageEventObject lPageEventObject = _getOrNewAUTPageEventObject(aPageObject);
            if (lPageEventObject.isPageAppearCalled()) {
                UTOperationStack.getInstance().addAction("pageDisAppear:" + aPageObject.getClass().getSimpleName());
                if (lPageEventObject.getPageStatus() != null && UTPageStatus.UT_H5_IN_WebView == lPageEventObject.getPageStatus()) {
                    if (1 == lPageEventObject.getPageStatusCode()) {
                        this.mNextPageProperties = this.mBackupNextPageProperties;
                        UTVariables.getInstance().setH5Url(UTVariables.getInstance().getBackupH5Url());
                    }
                    if (1 == lPageEventObject.getPageStatusCode() || lPageEventObject.isH5Called()) {
                        Logger.d("pageDisAppear", "UTTracker.PAGE_STATUS_CODE_302 or PageEventObject.isH5Called");
                        UTPageStateObject lUTPageStateObject = getOrNewUTPageStateObject(aPageObject);
                        if (lUTPageStateObject != null) {
                            lUTPageStateObject.mIsH5Page = false;
                        }
                        _releaseSkipFlagAndH5FlagPageObject(lPageEventObject);
                        _clearPageDisAppearContext();
                    }
                }
                long lPageApearTimeStamp = lPageEventObject.getPageAppearTimestamp();
                long lPageStayConsume = SystemClock.elapsedRealtime() - lPageEventObject.getPageStayTimstamp();
                if (aPageObject instanceof Activity) {
                    disPathcherPageChangerEvent(1, aPageObject);
                    if (!(!Logger.isDebug() || ((Activity) aPageObject).getIntent() == null || ((Activity) aPageObject).getIntent().getData() == null)) {
                        Logger.i("pageDisAppear", "uri=" + ((Activity) aPageObject).getIntent().getData().toString());
                    }
                    boolean lIsNeedRefreshUri = false;
                    Uri lPEOUrl = lPageEventObject.getPageUrl();
                    String lPEOUrlString = null;
                    if (lPEOUrl != null) {
                        lPEOUrlString = lPEOUrl.toString();
                    }
                    String lPOUrlString = null;
                    Uri lPOUrl = null;
                    Intent intent = ((Activity) aPageObject).getIntent();
                    if (intent != null) {
                        lPOUrl = intent.getData();
                    }
                    if (lPOUrl != null) {
                        lPOUrlString = lPOUrl.toString();
                    }
                    if ((lPEOUrlString != null && !lPEOUrlString.equals(lPOUrlString)) || (lPOUrlString != null && !lPOUrlString.equals(lPEOUrlString))) {
                        lIsNeedRefreshUri = true;
                    }
                    if (lPageEventObject.getPageUrl() == null && lIsNeedRefreshUri) {
                        lPageEventObject.setPageUrl(lPOUrl);
                    }
                }
                String lPageName = lPageEventObject.getPageName();
                String lRefPage = lPageEventObject.getRefPage();
                if (lRefPage == null || lRefPage.length() == 0) {
                    lRefPage = "-";
                }
                Map<String, String> lPageProperties = this.mPageProperties;
                if (lPageProperties == null) {
                    lPageProperties = new HashMap<>();
                }
                if (UTExtendSwitch.bJTrackExtend) {
                    try {
                        if (aPageObject instanceof Activity) {
                            Uri lArgsUri = ((Activity) aPageObject).getIntent().getData();
                            if (lArgsUri != null) {
                                Logger.i("JTrack", "uri:" + lArgsUri.toString());
                            }
                            Map<String, String> lArgsMap = null;
                            if (!StringUtils.isEmpty(lPageEventObject.getPageName())) {
                                lArgsMap = JTrackExtend.getArgsMap(lPageEventObject.getPageName(), lArgsUri);
                                Logger.i("JTrack", "getArgsMap by pagename:" + lPageEventObject.getPageName());
                            }
                            if (lArgsMap == null || lArgsMap.size() == 0) {
                                lArgsMap = JTrackExtend.getArgsMap((Activity) aPageObject, lArgsUri);
                                Logger.i("JTrack", "getArgsMap by activity:" + aPageObject.getClass().getName());
                            }
                            if (lArgsMap != null && lArgsMap.size() > 0) {
                                lPageProperties.putAll(lArgsMap);
                                Logger.i("JTrack", "ArgsMap:" + StringUtils.convertMapToString(lArgsMap));
                            }
                        }
                    } catch (Throwable th) {
                    }
                }
                if (lPageEventObject.getPageProperties() != null) {
                    lPageProperties.putAll(lPageEventObject.getPageProperties());
                }
                if (aPageObject instanceof IUTPageTrack) {
                    IUTPageTrack lUTActivity = (IUTPageTrack) aPageObject;
                    String lARefPage = lUTActivity.getReferPage();
                    if (!StringUtils.isEmpty(lARefPage)) {
                        lRefPage = lARefPage;
                    }
                    Map<String, String> lPageProperties2 = lUTActivity.getPageProperties();
                    if (lPageProperties2 != null && lPageProperties2.size() > 0) {
                        this.mPageProperties.putAll(lPageProperties2);
                        lPageProperties = this.mPageProperties;
                    }
                    String lUTPageName = lUTActivity.getPageName();
                    if (!StringUtils.isEmpty(lUTPageName)) {
                        lPageName = lUTPageName;
                    }
                }
                String lUrlSPM = "";
                String lUrlUtParam = "";
                String lUrlSCM = "";
                Uri lUrl = lPageEventObject.getPageUrl();
                if (lUrl != null) {
                    try {
                        HashMap hashMap = new HashMap();
                        String lSPM = _getSpmByUri(lUrl);
                        if (!StringUtils.isEmpty(lSPM)) {
                            boolean mIsSPMSkip = false;
                            String lCacheKey = aPageObject.getClass().getSimpleName() + aPageObject.hashCode();
                            if (this.mSPMObjectMap.containsKey(lCacheKey)) {
                                if (lSPM.equals(this.mSPMObjectMap.get(lCacheKey))) {
                                    mIsSPMSkip = true;
                                }
                            }
                            if (!mIsSPMSkip) {
                                hashMap.put(SPMConfig.SPM, lSPM);
                                this.mSPMObjectMap.put(lCacheKey, lSPM);
                                _releaseSPMCacheObj(lCacheKey);
                            }
                        }
                        lUrlSPM = lSPM;
                        lUrlUtParam = lUrl.getQueryParameter("utparam");
                        lUrlSCM = lUrl.getQueryParameter(BaseConfig.INTENT_KEY_SCM);
                        if (!StringUtils.isEmpty(lUrlSCM)) {
                            hashMap.put(BaseConfig.INTENT_KEY_SCM, lUrlSCM);
                        }
                        String pg1stepk = lUrl.getQueryParameter("pg1stepk");
                        if (!StringUtils.isEmpty(pg1stepk)) {
                            hashMap.put("pg1stepk", pg1stepk);
                        }
                        if (!StringUtils.isEmpty(lUrl.getQueryParameter(BaseConfig.INTENT_KEY_MODULE_POING))) {
                            hashMap.put("issb", "1");
                        }
                        String lOutsideTTID = _getOutsideTTID(lUrl);
                        if (!StringUtils.isEmpty(lOutsideTTID)) {
                            ClientVariables.getInstance().setOutsideTTID(lOutsideTTID);
                        }
                        if (hashMap.size() > 0) {
                            lPageProperties.putAll(hashMap);
                        }
                    } catch (Throwable e) {
                        Logger.d("", e);
                    }
                }
                UTPageStateObject lUTPageStateObject2 = getOrNewUTPageStateObject(aPageObject);
                if (lUTPageStateObject2 != null) {
                    if (lPageEventObject.getPageStatus() == null || UTPageStatus.UT_H5_IN_WebView != lPageEventObject.getPageStatus()) {
                        boolean isFromFragment = _getPageEventObjectCacheKey(aPageObject).equals(this.mLastCacheKey);
                        if (!lUTPageStateObject2.mIsSwitchBackground) {
                            if ("1".equals(lPageProperties.get(SKIPBK)) || lUTPageStateObject2.mIsSkipBackForever || lUTPageStateObject2.mIsSkipBack) {
                                lUTPageStateObject2.mIsBack = false;
                                lUTPageStateObject2.mIsSkipBack = false;
                            }
                            if (!lUTPageStateObject2.mIsBack || (lUTPageStateObject2.mIsFrame && isFromFragment)) {
                                refreshUTPageStateObject(lUTPageStateObject2, lPageProperties, lUrlSPM, lUrlUtParam, lUrlSCM);
                            }
                        } else {
                            lUTPageStateObject2.mIsBack = false;
                            clearUTPageStateObject(lPageProperties);
                        }
                        if (lUTPageStateObject2.mIsBack) {
                            clearUTPageStateObject(lPageProperties);
                        }
                        forceSetSpm(lUTPageStateObject2, lPageEventObject.getPageProperties());
                        lPageProperties.putAll(lUTPageStateObject2.getPageStatMap(isFromFragment));
                    } else {
                        if (lUTPageStateObject2.mIsBack) {
                            clearUTPageStateObject(lPageProperties);
                        }
                        lPageProperties.putAll(lUTPageStateObject2.getPageStatMap(false));
                    }
                    setLastCacheKey(_getPageEventObjectCacheKey(aPageObject));
                    setLastCacheKeySpmUrl(lUTPageStateObject2.mSpmUrl);
                    setLastCacheKeyScmUrl(lUTPageStateObject2.mScmUrl);
                    setLastCacheKeyUtParam(lUTPageStateObject2.mUtparamUrl);
                    setLastCacheKeyUtParamCnt(lUTPageStateObject2.mUtparamCnt);
                    Logger.d("", "mLastCacheKey:" + this.mLastCacheKey + ",mLastCacheKeySpmUrl:" + this.mLastCacheKeySpmUrl + ",mLastCacheKeyUtParam:" + this.mLastCacheKeyUtParam + ",mLastCacheKeyUtParamCnt:" + this.mLastCacheKeyUtParamCnt);
                    lUTPageStateObject2.mIsBack = true;
                    lUTPageStateObject2.mIsSwitchBackground = false;
                }
                try {
                    String lTPKString = UTTPKBiz.getInstance().getTpkString(lPageEventObject.getPageUrl(), lPageProperties);
                    if (!StringUtils.isEmpty(lTPKString)) {
                        lPageProperties.put("_tpk", lTPKString);
                    }
                } catch (Exception e2) {
                    Logger.d("", e2.toString());
                }
                if (lPageEventObject.getPageProperties() != null && lPageEventObject.getPageProperties().containsKey("_allow_override_value")) {
                    lPageProperties.putAll(lPageEventObject.getPageProperties());
                    lPageProperties.remove("_allow_override_value");
                }
                if ("Page_Webview".equalsIgnoreCase(lPageName) && lUrl != null) {
                    String temp = lUrl.toString();
                    if (!TextUtils.isEmpty(temp)) {
                        int index = temp.indexOf(WVUtils.URL_DATA_CHAR);
                        String urlForPageName = temp;
                        if (index != -1) {
                            urlForPageName = temp.substring(0, index);
                        }
                        if (!TextUtils.isEmpty(urlForPageName)) {
                            lPageName = urlForPageName;
                        }
                        Logger.d("", "temp", temp, "urlForPageName", urlForPageName);
                    }
                }
                UTHitBuilders.UTPageHitBuilder uTPageHitBuilder = new UTHitBuilders.UTPageHitBuilder(lPageName);
                uTPageHitBuilder.setReferPage(lRefPage).setDurationOnPage(lPageStayConsume).setProperties(lPageProperties);
                uTPageHitBuilder.setProperty(LogField.RECORD_TIMESTAMP.toString(), "" + lPageApearTimeStamp);
                uTPageHitBuilder.setProperty("_priority", "4");
                UTVariables.getInstance().setRefPage(lPageName);
                if (aTrackerInstance == null) {
                    throw new NullPointerException("Tracker instance is null,please init sdk first.");
                }
                aTrackerInstance.send(uTPageHitBuilder.build());
            } else {
                Logger.e("UT", "Please call pageAppear first(" + _getPageName(aPageObject) + ").");
            }
            if (lPageEventObject.isSkipPage()) {
                _releaseSkipFlagAndH5FlagPageObject(lPageEventObject);
            } else if (lPageEventObject.getPageStatus() == null || UTPageStatus.UT_H5_IN_WebView != lPageEventObject.getPageStatus()) {
                _removeUTPageEventObject(aPageObject);
            } else {
                _releaseSkipFlagAndH5FlagPageObject(lPageEventObject);
            }
            _clearPageDisAppearContext();
        }
    }

    private void forceSetSpm(UTPageStateObject pageStateObject, Map<String, String> pageProperties) {
        if (pageStateObject != null && pageProperties != null) {
            String forceSpmCnt = pageProperties.get(FORCE_SPM_CNT);
            if (!TextUtils.isEmpty(forceSpmCnt)) {
                pageStateObject.mSpmCnt = forceSpmCnt;
            }
            String forceSpmUrl = pageProperties.get(FORCE_SPM_URL);
            if (!TextUtils.isEmpty(forceSpmUrl)) {
                pageStateObject.mSpmUrl = forceSpmUrl;
            }
        }
    }

    /* access modifiers changed from: package-private */
    public synchronized Map<String, String> getPageAllProperties(Activity activity) {
        Intent intent;
        Map<String, String> pageProperties = null;
        synchronized (this) {
            if (activity != null) {
                if (this.mCurrentPageCacheKey != null) {
                    UTPageEventObject lPageEventObject = _getOrNewAUTPageEventObject(activity);
                    if (!lPageEventObject.isPageAppearCalled()) {
                        Logger.e("getPagePropertiesWithSpmAndUtparam", "activity isPageAppearCalled is false");
                    } else if (lPageEventObject.getPageStatus() == null || UTPageStatus.UT_H5_IN_WebView != lPageEventObject.getPageStatus()) {
                        UTPageStateObject pageStateObject = copyUTPageStateObject(getOrNewUTPageStateObject(activity));
                        if (pageStateObject == null) {
                            Logger.e("getPagePropertiesWithSpmAndUtparam", "getOrNewUTPageStateObject is null");
                        } else {
                            pageProperties = new HashMap<>();
                            pageProperties.putAll(this.mPageProperties);
                            if (lPageEventObject.getPageProperties() != null) {
                                pageProperties.putAll(lPageEventObject.getPageProperties());
                            }
                            String lUrlSPM = "";
                            String lUrlUtParam = "";
                            String lUrlSCM = "";
                            Uri pageUri = lPageEventObject.getPageUrl();
                            if (pageUri == null && (intent = activity.getIntent()) != null) {
                                pageUri = intent.getData();
                            }
                            if (pageUri != null) {
                                try {
                                    lUrlSPM = _getSpmByUri(pageUri);
                                } catch (Throwable e) {
                                    Logger.e("getPagePropertiesWithSpmAndUtparam", e, new Object[0]);
                                }
                                try {
                                    lUrlUtParam = pageUri.getQueryParameter("utparam");
                                } catch (Throwable e2) {
                                    Logger.e("getPagePropertiesWithSpmAndUtparam", e2, new Object[0]);
                                }
                                try {
                                    lUrlSCM = pageUri.getQueryParameter(BaseConfig.INTENT_KEY_SCM);
                                } catch (Throwable e3) {
                                    Logger.e("getPagePropertiesWithSpmAndUtparam", e3, new Object[0]);
                                }
                            }
                            boolean isFromFragment = _getPageEventObjectCacheKey(activity).equals(this.mLastCacheKey);
                            if (!pageStateObject.mIsSwitchBackground) {
                                if ("1".equals(pageProperties.get(SKIPBK)) || pageStateObject.mIsSkipBackForever || pageStateObject.mIsSkipBack) {
                                    pageStateObject.mIsBack = false;
                                    pageStateObject.mIsSkipBack = false;
                                }
                                if (!pageStateObject.mIsBack || (pageStateObject.mIsFrame && isFromFragment)) {
                                    refreshUTPageStateObject(pageStateObject, pageProperties, lUrlSPM, lUrlUtParam, lUrlSCM);
                                }
                            } else {
                                pageStateObject.mIsBack = false;
                                clearUTPageStateObject(pageProperties);
                            }
                            if (pageStateObject.mIsBack) {
                                clearUTPageStateObject(pageProperties);
                            }
                            forceSetSpm(pageStateObject, pageProperties);
                            pageProperties.putAll(pageStateObject.getPageStatMap(isFromFragment));
                        }
                    } else {
                        Logger.e("getPagePropertiesWithSpmAndUtparam", "activity is UT_H5_IN_WebView");
                    }
                }
            }
            Logger.e("getPagePropertiesWithSpmAndUtparam", "activity or CurrentPageCacheKey is null");
        }
        return pageProperties;
    }

    public static Map<String, String> encodeUtParam(Map<String, String> pageStatMap) {
        if (pageStatMap != null) {
            try {
                String utparamCnt = pageStatMap.get(UTPARAM_CNT);
                if (!TextUtils.isEmpty(utparamCnt)) {
                    pageStatMap.put(UTPARAM_CNT, URLEncoder.encode(utparamCnt));
                }
            } catch (Throwable e) {
                Logger.e((String) null, e, new Object[0]);
            }
            try {
                String utparamUrl = pageStatMap.get("utparam-url");
                if (!TextUtils.isEmpty(utparamUrl)) {
                    pageStatMap.put("utparam-url", URLEncoder.encode(utparamUrl));
                }
            } catch (Throwable e2) {
                Logger.e((String) null, e2, new Object[0]);
            }
            try {
                String utparamPre = pageStatMap.get("utparam-pre");
                if (!TextUtils.isEmpty(utparamPre)) {
                    pageStatMap.put("utparam-pre", URLEncoder.encode(utparamPre));
                }
            } catch (Throwable e3) {
                Logger.e((String) null, e3, new Object[0]);
            }
        }
        return pageStatMap;
    }

    /* access modifiers changed from: package-private */
    public synchronized String getPageSpmUrl(Activity activity) {
        String spmUrl;
        Intent intent;
        if (activity == null) {
            spmUrl = "";
        } else {
            UTPageEventObject lPageEventObject = _getOrNewAUTPageEventObject(activity);
            if (lPageEventObject.getPageStatus() == null || UTPageStatus.UT_H5_IN_WebView != lPageEventObject.getPageStatus()) {
                spmUrl = "";
                Uri lUrl = lPageEventObject.getPageUrl();
                if (lUrl == null && (intent = activity.getIntent()) != null) {
                    lUrl = intent.getData();
                }
                if (lUrl != null) {
                    try {
                        spmUrl = _getSpmByUri(lUrl);
                    } catch (Throwable th) {
                    }
                }
                if (TextUtils.isEmpty(spmUrl)) {
                    UTPageStateObject lUTPageStateObject = getOrNewUTPageStateObject(activity);
                    if (lUTPageStateObject != null) {
                        boolean isBack = lUTPageStateObject.mIsBack;
                        if (!lUTPageStateObject.mIsSwitchBackground) {
                            if (lUTPageStateObject.mIsSkipBackForever || lUTPageStateObject.mIsSkipBack) {
                                isBack = false;
                            }
                            boolean isFromFragment = _getPageEventObjectCacheKey(activity).equals(this.mLastCacheKey);
                            if (lUTPageStateObject.mIsFrame && isFromFragment) {
                                isBack = false;
                            }
                            if (!isBack) {
                                Map<String, String> lPageProperties = lPageEventObject.getPageProperties();
                                String spm0 = lPageProperties.get(Constants.PARAM_OUTER_SPM_URL);
                                String spm1 = lPageProperties.get("spm_url");
                                if (!TextUtils.isEmpty(spm0)) {
                                    spmUrl = spm0;
                                } else if (!TextUtils.isEmpty(spm1)) {
                                    spmUrl = spm1;
                                } else {
                                    spmUrl = lPageProperties.get(SPMConfig.SPM);
                                }
                            } else {
                                spmUrl = lUTPageStateObject.mSpmUrl;
                            }
                        } else {
                            spmUrl = lUTPageStateObject.mSpmUrl;
                        }
                    }
                    if (spmUrl == null) {
                        spmUrl = "";
                    }
                }
            } else {
                spmUrl = "";
            }
        }
        return spmUrl;
    }

    /* access modifiers changed from: package-private */
    public synchronized String getPageSpmPre(Activity activity) {
        String spmPre;
        if (activity == null) {
            spmPre = "";
        } else {
            UTPageEventObject lPageEventObject = _getOrNewAUTPageEventObject(activity);
            if (lPageEventObject.getPageStatus() == null || UTPageStatus.UT_H5_IN_WebView != lPageEventObject.getPageStatus()) {
                spmPre = "";
                UTPageStateObject lUTPageStateObject = getOrNewUTPageStateObject(activity);
                if (lUTPageStateObject != null) {
                    boolean isBack = lUTPageStateObject.mIsBack;
                    if (!lUTPageStateObject.mIsSwitchBackground) {
                        if (lUTPageStateObject.mIsSkipBackForever || lUTPageStateObject.mIsSkipBack) {
                            isBack = false;
                        }
                        boolean isFromFragment = _getPageEventObjectCacheKey(activity).equals(this.mLastCacheKey);
                        if (lUTPageStateObject.mIsFrame && isFromFragment) {
                            isBack = false;
                        }
                        if (isBack) {
                            spmPre = lUTPageStateObject.mSpmPre;
                        } else if (!TextUtils.isEmpty(this.mLastCacheKey)) {
                            spmPre = this.mLastCacheKeySpmUrl;
                        }
                    } else {
                        spmPre = lUTPageStateObject.mSpmPre;
                    }
                }
                if (spmPre == null) {
                    spmPre = "";
                }
            } else {
                spmPre = "";
            }
        }
        return spmPre;
    }

    /* access modifiers changed from: package-private */
    public synchronized String getPageScmPre(Activity activity) {
        String scmPre;
        if (activity == null) {
            scmPre = "";
        } else {
            UTPageEventObject lPageEventObject = _getOrNewAUTPageEventObject(activity);
            if (lPageEventObject.getPageStatus() == null || UTPageStatus.UT_H5_IN_WebView != lPageEventObject.getPageStatus()) {
                scmPre = "";
                UTPageStateObject lUTPageStateObject = getOrNewUTPageStateObject(activity);
                if (lUTPageStateObject != null) {
                    boolean isBack = lUTPageStateObject.mIsBack;
                    if (!lUTPageStateObject.mIsSwitchBackground) {
                        if (lUTPageStateObject.mIsSkipBackForever || lUTPageStateObject.mIsSkipBack) {
                            isBack = false;
                        }
                        boolean isFromFragment = _getPageEventObjectCacheKey(activity).equals(this.mLastCacheKey);
                        if (lUTPageStateObject.mIsFrame && isFromFragment) {
                            isBack = false;
                        }
                        if (isBack) {
                            scmPre = lUTPageStateObject.mScmPre;
                        } else if (!TextUtils.isEmpty(this.mLastCacheKey)) {
                            scmPre = this.mLastCacheKeyScmUrl;
                        }
                    } else {
                        scmPre = lUTPageStateObject.mScmPre;
                    }
                }
                if (scmPre == null) {
                    scmPre = "";
                }
            } else {
                scmPre = "";
            }
        }
        return scmPre;
    }

    @Deprecated
    public synchronized void pageDisAppear(Object aPageObject) {
        pageDisAppear(aPageObject, UTAnalytics.getInstance().getDefaultTracker());
    }

    private static String _getOutsideTTID(Uri pUri) {
        List<String> lResult;
        if (!(pUri == null || (lResult = pUri.getQueryParameters("ttid")) == null)) {
            for (String str : lResult) {
                if (!str.contains(Constant.NLP_CACHE_TYPE) && !str.contains("%40")) {
                    return str;
                }
            }
        }
        return null;
    }

    private static String _getPageName(Object aPageObject) {
        String lPageName = aPageObject.getClass().getSimpleName();
        if (lPageName == null || !lPageName.toLowerCase().endsWith(TuwenConstants.MODEL_LIST_KEY.ACTIVITY)) {
            return lPageName;
        }
        return lPageName.substring(0, lPageName.length() - 8);
    }

    public void pageDestroyed(Activity activity) {
        String lCacheKey = _getPageEventObjectCacheKey(activity);
        if (this.mPageStateObjects.containsKey(lCacheKey)) {
            this.mPageStateObjects.remove(lCacheKey);
        }
        if (this.mClearUTPageStateObjectList.contains(lCacheKey)) {
            this.mClearUTPageStateObjectList.remove(lCacheKey);
        }
        _releaseUTPageStateObject();
    }

    public void pageSwitchBackground() {
        if (this.mPageStateObjects.containsKey(this.mLastCacheKey)) {
            this.mPageStateObjects.get(this.mLastCacheKey).mIsSwitchBackground = true;
        }
    }

    public synchronized UTPageStateObject getOrNewUTPageStateObject(Object aPageObject) {
        UTPageStateObject uTPageStateObject;
        if (aPageObject instanceof Activity) {
            String lCacheKey = _getPageEventObjectCacheKey(aPageObject);
            if (!this.mClearUTPageStateObjectList.contains(lCacheKey)) {
                this.mClearUTPageStateObjectList.add(lCacheKey);
            }
            if (this.mPageStateObjects.containsKey(lCacheKey)) {
                uTPageStateObject = this.mPageStateObjects.get(lCacheKey);
            } else {
                UTPageStateObject lPageStateObject = new UTPageStateObject();
                this.mPageStateObjects.put(lCacheKey, lPageStateObject);
                uTPageStateObject = lPageStateObject;
            }
        } else {
            uTPageStateObject = null;
        }
        return uTPageStateObject;
    }

    private synchronized void _releaseUTPageStateObject() {
        if (this.mClearUTPageStateObjectList.size() > 100) {
            for (int i = 0; i < 50; i++) {
                String lPCacheKey = this.mClearUTPageStateObjectList.poll();
                if (lPCacheKey != null && this.mPageStateObjects.containsKey(lPCacheKey)) {
                    this.mPageStateObjects.remove(lPCacheKey);
                }
            }
        }
    }

    private String _getSpmByUri(Uri lUrl) throws Exception {
        String lSPM = lUrl.getQueryParameter(SPMConfig.SPM);
        if (StringUtils.isEmpty(lSPM)) {
            try {
                lUrl = Uri.parse(URLDecoder.decode(lUrl.toString(), "UTF-8"));
                lSPM = lUrl.getQueryParameter(SPMConfig.SPM);
            } catch (Exception e) {
                Logger.w("", e, new Object[0]);
            }
        }
        if (!StringUtils.isEmpty(lSPM)) {
            return lSPM;
        }
        String lSPM2 = lUrl.getQueryParameter("spm_url");
        if (!StringUtils.isEmpty(lSPM2)) {
            return lSPM2;
        }
        try {
            return Uri.parse(URLDecoder.decode(lUrl.toString(), "UTF-8")).getQueryParameter("spm_url");
        } catch (Exception e2) {
            Logger.w("", e2, new Object[0]);
            return lSPM2;
        }
    }

    private void clearUTPageStateObject(Map<String, String> lPageProperties) {
        if (lPageProperties != null && lPageProperties.size() > 0) {
            lPageProperties.remove("spm-cnt");
            lPageProperties.remove(Constants.PARAM_OUTER_SPM_URL);
            lPageProperties.remove("spm-pre");
            lPageProperties.remove(UTPARAM_CNT);
            lPageProperties.remove("utparam-url");
            lPageProperties.remove("utparam-pre");
            lPageProperties.remove("scm-pre");
        }
    }

    private void refreshUTPageStateObject(UTPageStateObject lPageStateObject, Map<String, String> lPageProperties, String lUrlSPM, String lUrlUtParam, String lUrlSCM) {
        String spmcnt = lPageProperties.get("spm-cnt");
        if (!TextUtils.isEmpty(spmcnt)) {
            lPageStateObject.mSpmCnt = spmcnt;
        } else {
            lPageStateObject.mSpmCnt = lPageProperties.get("spm_cnt");
        }
        if (!TextUtils.isEmpty(lUrlSPM)) {
            lPageStateObject.mSpmUrl = lUrlSPM;
        } else {
            String spm0 = lPageProperties.get(Constants.PARAM_OUTER_SPM_URL);
            String spm1 = lPageProperties.get("spm_url");
            if (!TextUtils.isEmpty(spm0)) {
                lPageStateObject.mSpmUrl = spm0;
            } else if (!TextUtils.isEmpty(spm1)) {
                lPageStateObject.mSpmUrl = spm1;
            } else {
                lPageStateObject.mSpmUrl = lPageProperties.get(SPMConfig.SPM);
            }
        }
        if (!TextUtils.isEmpty(this.mLastCacheKey)) {
            lPageStateObject.mSpmPre = this.mLastCacheKeySpmUrl;
        } else {
            lPageStateObject.mSpmPre = "";
        }
        if (!TextUtils.isEmpty(lUrlSCM)) {
            lPageStateObject.mScmUrl = lUrlSCM;
        } else {
            lPageStateObject.mScmUrl = lPageProperties.get(BaseConfig.INTENT_KEY_SCM);
        }
        if (!TextUtils.isEmpty(this.mLastCacheKey)) {
            lPageStateObject.mScmPre = this.mLastCacheKeyScmUrl;
        } else {
            lPageStateObject.mScmPre = "";
        }
        String utparamcnt = lPageProperties.get(UTPARAM_CNT);
        if (!TextUtils.isEmpty(utparamcnt)) {
            lPageStateObject.mUtparamCnt = utparamcnt;
        } else {
            lPageStateObject.mUtparamCnt = "";
        }
        String utparam = "";
        if (!TextUtils.isEmpty(this.mLastCacheKey)) {
            utparam = this.mLastCacheKeyUtParamCnt;
        }
        lPageStateObject.mUtparamUrl = refreshUtParam(lUrlUtParam, refreshUtParam(lPageProperties.get("utparam-url"), utparam));
        if (!TextUtils.isEmpty(this.mLastCacheKey)) {
            lPageStateObject.mUtparamPre = this.mLastCacheKeyUtParam;
        } else {
            lPageStateObject.mUtparamPre = "";
        }
    }

    public String refreshUtParam(String utParam, String targetUtParam) {
        Map<String, String> utParamMap;
        try {
            if (TextUtils.isEmpty(utParam) || (utParamMap = parseJsonToMap(utParam)) == null || utParamMap.size() < 1) {
                return targetUtParam;
            }
            if (TextUtils.isEmpty(targetUtParam)) {
                return utParam;
            }
            Map<String, String> targetUtParamMap = parseJsonToMap(targetUtParam);
            if (targetUtParamMap == null || targetUtParamMap.size() < 1) {
                return utParam;
            }
            targetUtParamMap.putAll(utParamMap);
            return JSON.toJSONString(targetUtParamMap);
        } catch (Exception e) {
            Logger.d("", e);
            return "";
        }
    }

    private Map<String, String> parseJsonToMap(String json) {
        try {
            return (Map) JSON.parseObject(json, Map.class);
        } catch (Exception e) {
            return null;
        }
    }
}
