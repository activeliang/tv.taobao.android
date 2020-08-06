package android.taobao.windvane.urlintercept;

import android.content.Context;
import android.taobao.windvane.config.ModuleConfig;
import android.taobao.windvane.config.WVCommonConfig;
import android.taobao.windvane.config.WVConfigUtils;
import android.taobao.windvane.config.WVServerConfig;
import android.taobao.windvane.connect.ApiUrlManager;
import android.taobao.windvane.connect.ConnectManager;
import android.taobao.windvane.connect.HttpConnectListener;
import android.taobao.windvane.connect.HttpResponse;
import android.taobao.windvane.connect.api.ApiResponse;
import android.taobao.windvane.urlintercept.WVURLInterceptData;
import android.taobao.windvane.util.ConfigStorage;
import android.taobao.windvane.util.TaoLog;
import android.taobao.windvane.util.WVUrlUtil;
import android.taobao.windvane.webview.IWVWebView;
import android.text.TextUtils;
import java.io.UnsupportedEncodingException;
import java.util.List;
import java.util.Map;
import org.json.JSONObject;

public class WVURLIntercepterDefault implements WVURLIntercepterInterface {
    private static final String ADDRESS = "http://my.m.taobao.com/deliver/wap_deliver_address_list.htm";
    private static final String CART = "http://h5.m.taobao.com/awp/base/cart.htm";
    private static final String DETAIL = "http://a.m.taobao.com/i";
    private static final String FAV = "http://fav.m.taobao.com/my_collect_list.htm";
    private static final String LOGIN = "http://login.m.taobao.com/login.htm";
    private static final String MYTAOBAO = "http://my.m.taobao.com/myTaobao.htm";
    private static final String ORDER_LIST = "http://trade.taobao.com/trade/itemlist/list_bought_items.htm";
    private static final String SEARCH = "http://s.m.taobao.com/search.htm?q=";
    private static final String SHOP = "http://shop.m.taobao.com/shop/shop_index.htm";
    private static final String TAG = "WVUrlResolver";
    /* access modifiers changed from: private */
    public boolean isUpdating = false;

    public WVURLIntercepterDefault() {
        if (isNeedupdateURLRule(true)) {
            updateURLRule();
        }
        refreshConfig((List<WVURLInterceptData.RuleData>) null);
    }

    private void refreshConfig(List<WVURLInterceptData.RuleData> list) {
        if (list == null) {
            list = WVURLInterceptHelper.parseRuleData(readConfigFile());
        }
        if (WVCommonConfig.commonConfig.urlRuleStatus == 2 && list != null && WVServerConfig.URL_FILTER) {
            WVURLInterceptService.resetRulesAndPat();
            for (WVURLInterceptData.RuleData rule : list) {
                WVURLInterceptService.getWVURLinterceptRules().add(rule);
            }
        }
    }

    public static WVURLInterceptData.URLInfo parse(String url) {
        if (TextUtils.isEmpty(url)) {
            return null;
        }
        if (WVURLInterceptService.getWVURLinterceptRules() == null || !WVURLInterceptService.getWVURLinterceptRules().isEmpty()) {
            WVURLInterceptData.URLInfo info = WVURLInterceptHelper.parseByTag(url);
            if (info == null || info.code <= 0) {
                return WVURLInterceptHelper.parseByRule(url, WVURLInterceptService.getWVURLinterceptRules(), WVURLInterceptService.getWVURLInterceptRulePats());
            }
            TaoLog.d(TAG, "parse url success through tag.");
            return info;
        }
        TaoLog.w(TAG, "parse url fail, urlRule is empty.");
        return null;
    }

    public static String hitURLInterceptRules(String url) {
        WVURLInterceptData.URLInfo info;
        if (TextUtils.isEmpty(url) || (info = parse(url)) == null || info.code == 0) {
            return null;
        }
        return WVUrlUtil.rebuildWVurl(url, toUri(info));
    }

    /* access modifiers changed from: protected */
    public String getConfigUrl() {
        return ApiUrlManager.getConfigUrl("urlRule.json", "2");
    }

    /* access modifiers changed from: protected */
    public boolean needSaveConfig(String result) {
        List<WVURLInterceptData.RuleData> list;
        if (TextUtils.isEmpty(result)) {
            return false;
        }
        JSONObject jsonObj = null;
        ApiResponse response = new ApiResponse();
        if (response.parseJsonResult(result).success) {
            jsonObj = response.data;
        }
        if (jsonObj == null || (list = WVURLInterceptHelper.parseRuleData(jsonObj.toString())) == null || list.isEmpty()) {
            return false;
        }
        refreshConfig(list);
        return true;
    }

    public boolean shouldOverrideUrlLoading(Context context, IWVWebView view, String url) {
        WVURLInterceptData.URLInfo info = parse(url);
        if (info == null || WVURLInterceptService.getWVURLInterceptHandler() == null) {
            return false;
        }
        return WVURLInterceptService.getWVURLInterceptHandler().doURLIntercept(context, view, url, info);
    }

    public void updateURLRule() {
        if (!this.isUpdating) {
            if (TaoLog.getLogStatus()) {
                TaoLog.d(TAG, "doUpdateConfig: " + getConfigUrl());
            }
            this.isUpdating = true;
            ConnectManager.getInstance().connect(getConfigUrl(), (HttpConnectListener<HttpResponse>) new HttpConnectListener<HttpResponse>() {
                public void onFinish(HttpResponse data, int token) {
                    if (data == null || data.getData() == null) {
                        boolean unused = WVURLIntercepterDefault.this.isUpdating = false;
                        return;
                    }
                    try {
                        String content = new String(data.getData(), "utf-8");
                        if (TaoLog.getLogStatus()) {
                            TaoLog.d(WVURLIntercepterDefault.TAG, "callback: Download config successfully.\nclass = " + getClass().getName() + "\ncontent=" + content);
                        }
                        if (WVURLIntercepterDefault.this.needSaveConfig(content)) {
                            ConfigStorage.putLongVal(WVConfigUtils.SPNAME, WVURLIntercepterDefault.this.getStorageKeyPrefix() + ConfigStorage.KEY_TIME, System.currentTimeMillis());
                            WVURLIntercepterDefault.this.saveConfigFile(content);
                        }
                    } catch (UnsupportedEncodingException e) {
                        TaoLog.e(WVURLIntercepterDefault.TAG, "config encoding error. " + e.getMessage());
                    } finally {
                        boolean unused2 = WVURLIntercepterDefault.this.isUpdating = false;
                    }
                }
            });
        }
    }

    public boolean isNeedupdateURLRule(boolean isBoot) {
        if (!isOpenURLIntercept()) {
            return false;
        }
        return WVConfigUtils.isNeedUpdate(isBoot, WVConfigUtils.SPNAME, getStorageKeyPrefix());
    }

    public boolean isOpenURLIntercept() {
        return ModuleConfig.getInstance().url_updateConfig;
    }

    public static String toUri(WVURLInterceptData.URLInfo urlInfo) {
        if (urlInfo == null) {
            return null;
        }
        int code = urlInfo.code;
        Map<String, String> params = urlInfo.params;
        if (code == 100) {
            return DETAIL + params.get(WVURLRuleConstants.WV_PARAM_HY_ITM_ID) + ".htm";
        }
        if (code == 200) {
            return SEARCH + params.get(WVURLRuleConstants.WV_PARAM_HY_S_Q);
        }
        if (code == 300) {
            String shopId = params.get(WVURLRuleConstants.WV_PARAM_HY_SHOP_ID);
            String userId = params.get(WVURLRuleConstants.WV_PARAM_HY_USER_ID);
            if (!TextUtils.isEmpty(userId)) {
                return "http://shop.m.taobao.com/shop/shop_index.htm?user_id=" + userId;
            }
            if (!TextUtils.isEmpty(shopId)) {
                return "http://shop.m.taobao.com/shop/shop_index.htm?shop_id=" + shopId;
            }
        } else if (code == 400) {
            return CART;
        } else {
            if (code == 600) {
                return MYTAOBAO;
            }
            if (code == 700) {
                return FAV;
            }
            if (code == 500) {
                return ORDER_LIST;
            }
            if (code == 800) {
                return ADDRESS;
            }
            if (code == 1000) {
                return LOGIN;
            }
        }
        return urlInfo.url;
    }

    /* access modifiers changed from: protected */
    public String readConfigFile() {
        return ConfigStorage.getStringVal(WVConfigUtils.SPNAME, getStorageKeyPrefix() + ConfigStorage.KEY_DATA);
    }

    /* access modifiers changed from: protected */
    public void saveConfigFile(String data) {
        ConfigStorage.putStringVal(WVConfigUtils.SPNAME, getStorageKeyPrefix() + ConfigStorage.KEY_DATA, data);
    }

    /* access modifiers changed from: private */
    public String getStorageKeyPrefix() {
        return getClass().getName();
    }
}
