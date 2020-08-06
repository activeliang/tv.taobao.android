package com.ali.user.open.core.config;

import com.ali.user.open.core.WebViewProxy;
import com.ali.user.open.core.callback.DataProvider;
import com.ali.user.open.core.context.KernelContext;
import java.util.Locale;
import java.util.Map;

public class ConfigManager {
    public static int APP_KEY_INDEX = 0;
    public static int DAILY_APP_KEY_INDEX = 0;
    public static boolean DEBUG = false;
    public static String ICBU_LOGIN_HOST = "https://passport.alibaba.com/oauth.htm?appName=icbu-oauth&appEntrance=";
    public static String ICBU_LOGIN_HOST_DAILY = "https://passport.daily.alibaba.com/oauth.htm?appName=icbu-oauth&appEntrance=";
    public static String LOGIN_HOST = "https://havanalogin.taobao.com/taobao_oauth_common.htm?appName=taobao-oauth-common&appEntrance=sdk-common&needTopToken=true&topTokenAppName=";
    public static String LOGIN_URLS = "((https|http)://)login.m.taobao.com/login.htm(.*),((https|http)://)login.m.taobao.com/msg_login.htm(.*),((https|http)://)login.tmall.com(.*),((https|http)://)login.taobao.com/member/login.jhtml(.*),((https|http)://)login.wapa.taobao.com/login.htm(.*),((https|http)://)login.waptest.taobao.com/login.htm(.*),((https|http)://)login.waptest.tbsandbox.com/login.htm(.*)";
    public static String LOGOUT_URLS;
    public static String POSTFIX_OF_SECURITY_JPG = "";
    public static String POSTFIX_OF_SECURITY_JPG_USER_SET;
    private static final ConfigManager SINGLETON_INSTANCE = new ConfigManager();
    public static String qrCodeLoginUrl = "http://login.m.taobao.com/qrcodeShow.htm?appKey=%s&from=bcqrlogin";
    public static String qrCodeLoginUrl_daily = "http://login.waptest.taobao.com/qrcodeShow.htm?appKey=%s&from=bcqrlogin";
    public String dailyDomain;
    private Environment env;
    private Locale language = Locale.SIMPLIFIED_CHINESE;
    private DataProvider mLoginEntrenceCallback = null;
    private WebViewOption mWebViewOption = WebViewOption.UC;
    private int maxHistoryAccount = 1;
    public String onlineDomain;
    public String preDomain;
    private boolean registerSidToMtopDefault = true;
    private boolean saveHistoryWithSalt = true;
    Map<String, Object> scanParams;

    public int getMaxHistoryAccount() {
        return this.maxHistoryAccount;
    }

    public void setMaxHistoryAccount(int maxHistoryAccount2) {
        this.maxHistoryAccount = maxHistoryAccount2;
    }

    public boolean isRegisterSidToMtopDefault() {
        return this.registerSidToMtopDefault;
    }

    public void setRegisterSidToMtopDefault(boolean registerSidToMtopDefault2) {
        this.registerSidToMtopDefault = registerSidToMtopDefault2;
    }

    public Locale getCurrentLanguage() {
        return this.language;
    }

    public void setLanguage(Locale language2) {
        this.language = language2;
    }

    private ConfigManager() {
    }

    public void setEnvironment(Environment env2) {
        this.env = env2;
        init();
    }

    public Environment getEnvironment() {
        return this.env;
    }

    public static ConfigManager getInstance() {
        return SINGLETON_INSTANCE;
    }

    public void init() {
        if (this.env == null) {
            this.env = Environment.ONLINE;
        }
        int envIndex = this.env.ordinal();
        LOGIN_HOST = new String[]{"https://havanalogin.taobao.com/taobao_oauth_common.htm?appName=taobao-oauth-common&appEntrance=sdk-common&needTopToken=true&topTokenAppName=", "https://havanalogin.taobao.com/taobao_oauth_common.htm?appName=taobao-oauth-common&appEntrance=sdk-common&needTopToken=true&topTokenAppName=", "http://passport.daily.alibaba.com/taobao_oauth_common.htm?appName=taobao-oauth-common&appEntrance=sdk-common&needTopToken=true&topTokenAppName=", "http://passport.daily.alibaba.com/taobao_oauth_common.htm?appName=taobao-oauth-common&appEntrance=sdk-common&needTopToken=true&topTokenAppName="}[envIndex];
        LOGOUT_URLS = new String[]{"((https|http)://)login.m.taobao.com/logout.htm(.*)", "((https|http)://)login.wapa.taobao.com/logout.htm(.*)", "((https|http)://)login.waptest.taobao.com/logout.htm(.*)", "((https|http)://)login.waptest.tbsandbox.com/logout.htm(.*)"}[envIndex];
        qrCodeLoginUrl = new String[]{"http://login.m.taobao.com/qrcodeShow.htm?appKey=%s&from=bcqrlogin", "http://login.wapa.taobao.com/qrcodeShow.htm?appKey=%s&from=bcqrlogin", "http://login.waptest.taobao.com/qrcodeShow.htm?appKey=%s&from=bcqrlogin", "http://login.waptest.taobao.com/qrcodeShow.htm?appKey=%s&from=bcqrlogin"}[envIndex];
        if (POSTFIX_OF_SECURITY_JPG_USER_SET == null) {
            POSTFIX_OF_SECURITY_JPG = new String[]{"", "", "", ""}[envIndex];
            return;
        }
        POSTFIX_OF_SECURITY_JPG = POSTFIX_OF_SECURITY_JPG_USER_SET;
    }

    public static int getAppKeyIndex() {
        if (getInstance().getEnvironment() == null || !getInstance().getEnvironment().equals(Environment.TEST)) {
            return APP_KEY_INDEX;
        }
        return DAILY_APP_KEY_INDEX;
    }

    public static void setAppKeyIndex(int appKeyIndex) {
        APP_KEY_INDEX = appKeyIndex;
    }

    public static void setAppKeyIndex(int appKeyIndex, int dailyAppKeyIndex) {
        APP_KEY_INDEX = appKeyIndex;
        DAILY_APP_KEY_INDEX = dailyAppKeyIndex;
    }

    public boolean isSaveHistoryWithSalt() {
        return this.saveHistoryWithSalt;
    }

    public void setSaveHistoryWithSalt(boolean saveHistorySaltOnly) {
        this.saveHistoryWithSalt = saveHistorySaltOnly;
    }

    public WebViewOption getWebViewOption() {
        return this.mWebViewOption;
    }

    public void setWebViewOption(WebViewOption webViewOption) {
        this.mWebViewOption = webViewOption;
    }

    public void setWebViewProxy(WebViewProxy webViewProxy) {
        KernelContext.mWebViewProxy = webViewProxy;
    }

    public void setLoginEntrenceCallback(DataProvider dataProvider) {
        this.mLoginEntrenceCallback = dataProvider;
    }

    public DataProvider getLoginEntrenceCallback() {
        return this.mLoginEntrenceCallback;
    }
}
