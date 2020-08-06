package com.yunos.tvtaobao.biz.activity;

import android.app.ActivityManager;
import android.content.Intent;
import android.os.Bundle;
import android.os.Process;
import android.text.TextUtils;
import com.taobao.detail.domain.tuwen.TuwenConstants;
import com.tvtaobao.voicesdk.utils.TTSUtils;
import com.yunos.tv.core.config.AppInfo;
import com.yunos.tv.core.config.TvOptionsConfig;
import com.yunos.tv.core.util.ActivityPathRecorder;
import com.yunos.tvtaobao.biz.TradeBaseActivity;
import com.yunos.tvtaobao.biz.blitz.TaobaoBzPageStatusListener;
import com.yunos.tvtaobao.biz.common.BaseConfig;
import com.yunos.tvtaobao.biz.h5.plugin.AppInfoPlugin;
import com.yunos.tvtaobao.biz.h5.plugin.CouponPlugin;
import com.yunos.tvtaobao.biz.h5.plugin.GuardPlugin;
import com.yunos.tvtaobao.biz.h5.plugin.GuessYouLikePlugin;
import com.yunos.tvtaobao.biz.h5.plugin.TvTaoBaoBlitzPlugin;
import com.yunos.tvtaobao.biz.h5.plugin.TvTaoBaoPayPlugin;
import com.yunos.tvtaobao.biz.request.info.GlobalConfigInfo;
import com.zhiping.dev.android.logger.ZpLogger;
import java.io.UnsupportedEncodingException;
import java.lang.ref.WeakReference;
import java.net.URLEncoder;
import java.util.List;

public abstract class TaoBaoBlitzActivity extends TradeBaseActivity {
    private static String TAG = TaoBaoBlitzActivity.class.getSimpleName();
    protected static final String appkey = "appkey";
    private static final String queryParamsConcatSign = "&";
    private static final String queryParamsFlag = "?";
    private static final String versionCodeKey = "appVersionCode";
    protected String from = "from";
    private GuardPlugin guardPlugin;
    private GuessYouLikePlugin guessYouLikePlugin;
    protected AppInfoPlugin mAppInfoPlugin;
    protected CouponPlugin mCouponPlugin;
    protected TaobaoBzPageStatusListener.LOAD_MODE mLoadMode = TaobaoBzPageStatusListener.LOAD_MODE.URL_MODE;
    protected TvTaoBaoPayPlugin mPayPlugin;

    /* access modifiers changed from: protected */
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (GlobalConfigInfo.getInstance().getGlobalConfig() == null || !GlobalConfigInfo.getInstance().getGlobalConfig().isBeta()) {
            getWindow().setFlags(16777216, 16777216);
        }
    }

    /* access modifiers changed from: protected */
    public void onResume() {
        super.onResume();
        registerPlugins();
    }

    /* access modifiers changed from: protected */
    public void onPause() {
        super.onPause();
        TvTaoBaoBlitzPlugin.unregister(this);
    }

    /* access modifiers changed from: protected */
    public void registerPlugins() {
        ZpLogger.e(TAG, "registerPlugins  : " + this);
        TvTaoBaoBlitzPlugin.register(this);
        this.guessYouLikePlugin = new GuessYouLikePlugin(new WeakReference(this));
        this.mPayPlugin = new TvTaoBaoPayPlugin(new WeakReference(this));
        this.mCouponPlugin = new CouponPlugin(new WeakReference(this));
        this.mAppInfoPlugin = new AppInfoPlugin(new WeakReference(this));
        this.guardPlugin = new GuardPlugin(new WeakReference(this));
    }

    /* access modifiers changed from: protected */
    public void onInitH5View(String url) {
        loadWithUrl(prepareUrlParams(url));
        setH5BackGroud();
        this.mLoadMode = TaobaoBzPageStatusListener.LOAD_MODE.URL_MODE;
        OnWaitProgressDialog(true);
    }

    /* access modifiers changed from: protected */
    public String prepareUrlParams(String originUrl) {
        String newUrl = originUrl;
        try {
            if (shouldAppendVersionCode()) {
                newUrl = naiveAppendQueryParam(originUrl, versionCodeKey, Integer.toString(AppInfo.getAppVersionNum()));
            }
            return newUrl;
        } catch (UnsupportedEncodingException exception) {
            exception.printStackTrace();
            return newUrl;
        } catch (Throwable th) {
            return newUrl;
        }
    }

    /* access modifiers changed from: protected */
    public String naiveAppendQueryParam(String originUrl, String queryKey, String queryValue) throws UnsupportedEncodingException {
        if (!originUrl.contains("?")) {
            return originUrl + "?" + queryKey + "=" + URLEncoder.encode(queryValue, "UTF-8");
        }
        if (originUrl.contains(queryKey)) {
            return originUrl;
        }
        return originUrl + "&" + queryKey + "=" + URLEncoder.encode(queryValue, "UTF-8");
    }

    /* access modifiers changed from: protected */
    public boolean shouldAppendVersionCode() {
        return true;
    }

    public void loadDataForWeb(String data) {
        this.mLoadMode = TaobaoBzPageStatusListener.LOAD_MODE.DATA_MODE;
        loadWithData(data);
    }

    public void onPageLoadFinished(String param) {
        onWebviewPageDone(param);
    }

    public String buildVoiceUrl(String page, Intent intent) {
        this.from = intent.getStringExtra("from");
        String v_from = intent.getStringExtra(BaseConfig.INTENT_KEY_VOICE_FROM);
        List<String> pathList = ActivityPathRecorder.getInstance().getCurrentPath(this);
        if (pathList != null) {
            int i = 0;
            while (true) {
                if (i >= pathList.size()) {
                    break;
                }
                String currentPath = pathList.get(i);
                if (currentPath.contains(BaseConfig.INTENT_KEY_FROM_VAL_VS)) {
                    v_from = BaseConfig.INTENT_KEY_FROM_VAL_VS;
                    break;
                } else if (currentPath.contains(BaseConfig.INTENT_KEY_FROM_VAL_VA)) {
                    v_from = BaseConfig.INTENT_KEY_FROM_VAL_VA;
                    break;
                } else {
                    i++;
                }
            }
        }
        StringBuilder sb = new StringBuilder();
        sb.append(page);
        if (BaseConfig.INTENT_KEY_FROM_VAL_VS.equals(this.from) || BaseConfig.INTENT_KEY_FROM_VAL_VA.equals(this.from)) {
            if (page.contains("?")) {
                sb.append("&");
            } else {
                sb.append("?");
            }
            sb.append("v_from=").append(this.from);
            TvOptionsConfig.setTvOptionVoiceSystem(this.from);
        } else if (!TextUtils.isEmpty(v_from)) {
            if (page.contains("?")) {
                sb.append("&");
            } else {
                sb.append("?");
            }
            sb.append("v_from=").append(v_from);
            TvOptionsConfig.setTvOptionVoiceSystem(v_from);
        } else {
            TvOptionsConfig.setTvOptionVoiceSystem("");
        }
        return sb.toString();
    }

    public void onWebviewPageDone(String url) {
        if (BaseConfig.INTENT_KEY_FROM_VAL_VS.equals(this.from) || BaseConfig.INTENT_KEY_FROM_VAL_VA.equals(this.from)) {
            TTSUtils.getInstance().showDialog(this);
        }
    }

    /* access modifiers changed from: protected */
    public void onStartActivityNetWorkError() {
    }

    public void interceptBack(boolean isInterceptBack) {
    }

    /* access modifiers changed from: protected */
    public boolean isTbs() {
        return true;
    }

    public void exitChildProcess() {
        String packageName = getPackageName();
        ActivityManager activityMgr = (ActivityManager) getSystemService(TuwenConstants.MODEL_LIST_KEY.ACTIVITY);
        if (activityMgr != null && activityMgr.getRunningAppProcesses() != null && activityMgr.getRunningAppProcesses().size() > 0) {
            for (ActivityManager.RunningAppProcessInfo appProcess : activityMgr.getRunningAppProcesses()) {
                if (appProcess.processName.compareTo(packageName + ":bs_webbroser") == 0) {
                    ZpLogger.i(TAG, "kill processName=" + appProcess.processName);
                    Process.killProcess(appProcess.pid);
                }
                if (appProcess.processName.compareTo(packageName + ":channel") == 0) {
                    ZpLogger.i(TAG, "kill processName=" + appProcess.processName);
                    Process.killProcess(appProcess.pid);
                }
                if (appProcess.processName.compareTo(packageName + ":dexmerge") == 0) {
                    ZpLogger.i(TAG, "kill processName=" + appProcess.processName);
                    Process.killProcess(appProcess.pid);
                }
                if (appProcess.processName.compareTo(packageName + ":dex2oat") == 0) {
                    ZpLogger.i(TAG, "kill processName=" + appProcess.processName);
                    Process.killProcess(appProcess.pid);
                }
            }
        }
    }
}
