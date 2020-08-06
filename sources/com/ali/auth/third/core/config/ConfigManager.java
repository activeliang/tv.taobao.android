package com.ali.auth.third.core.config;

import com.ali.auth.third.core.model.SNSConfig;
import java.util.Map;

public class ConfigManager {
    public static int APP_KEY_INDEX = 0;
    public static int DAILY_APP_KEY_INDEX = 0;
    public static boolean DEBUG = false;
    public static String LOGIN_HOST = "https://login.m.taobao.com/minisdk/login.htm";
    public static String LOGIN_URLS = "((https|http)://)login.m.taobao.com/login.htm(.*),((https|http)://)login.tmall.com(.*),((https|http)://)login.taobao.com/member/login.jhtml(.*)";
    public static String LOGOUT_URLS;
    public static String POSTFIX_OF_SECURITY_JPG = "";
    public static String POSTFIX_OF_SECURITY_JPG_USER_SET;
    public static final Version SDK_VERSION = new Version(1, 7, 0);
    private static final ConfigManager SINGLETON_INSTANCE = new ConfigManager();
    public static String bindUrl = "http://login.m.taobao.com/cooperation/bindLogin.htm?code=%s&IBB=%s&appkey=%s";
    public static String getQrCodeContentUrl = "https://qrlogin.taobao.com/qrcodelogin/generateNoLoginQRCode.do?lt=m";
    public static String qrCodeLoginUrl = "https://login.m.taobao.com/qrcodeShow.htm?appKey=%s&from=bcqrlogin";
    public static String qrCodeLoginUrl_daily = "http://login.waptest.taobao.com/qrcodeShow.htm?appKey=%s&from=bcqrlogin";
    public static String qrLoginConfirmUrl = "https://login.m.taobao.com/qrcodeLogin.htm?shortURL=%s&from=bcqrlogin";
    public static int site = 0;
    public static String unbindUrl = "https://accountlink.taobao.com/sdkUnbind.htm";
    private Environment env;
    private Class<?> fullyCustomizedLoginFragment;
    private SNSConfig mAlipay3Config;
    private int maxHistoryAccount = 3;
    private String offlineDeviceID;
    private int orientation = 0;
    private boolean saveHistoryWithSalt = true;
    Map<String, Object> scanParams;
    private String ssoToken;

    public SNSConfig getAlipay3Config() {
        return this.mAlipay3Config;
    }

    public void setAlipay3Config(SNSConfig alipay3Config) {
        this.mAlipay3Config = alipay3Config;
    }

    private ConfigManager() {
    }

    public Environment getEnvironment() {
        return this.env;
    }

    public static ConfigManager getInstance() {
        return SINGLETON_INSTANCE;
    }

    public void init(int index) {
        this.env = Environment.values()[index];
        LOGIN_HOST = new String[]{"http://login.waptest.tbsandbox.com/minisdk/login.htm", "http://login.waptest.taobao.com/minisdk/login.htm", "http://login.wapa.taobao.com/minisdk/login.htm", "https://login.m.taobao.com/minisdk/login.htm"}[index];
        LOGIN_URLS = new String[]{"((https|http)://)login.waptest.tbsandbox.com/login.htm(.*)", "((https|http)://)login.waptest.taobao.com/login.htm(.*)", "((https|http)://)login.wapa.taobao.com/login.htm(.*)", "((https|http)://)login.m.taobao.com/login.htm(.*),((https|http)://)login.tmall.com(.*),((https|http)://)login.taobao.com/member/login.jhtml(.*)"}[index];
        LOGOUT_URLS = new String[]{"((https|http)://)login.waptest.tbsandbox.com/logout.htm(.*)", "((https|http)://)login.waptest.taobao.com/logout.htm(.*)", "((https|http)://)login.wapa.taobao.com/logout.htm(.*)", "((https|http)://)login.m.taobao.com/logout.htm(.*)"}[index];
        bindUrl = new String[]{"http://login.waptest.tbsandbox.com/cooperation/bindLogin.htm?code=%s&IBB=%s&appkey=%s", "http://login.waptest.taobao.com/cooperation/bindLogin.htm?code=%s&IBB=%s&appkey=%s", "http://login.wapa.taobao.com/cooperation/bindLogin.htm?code=%s&IBB=%s&appkey=%s", "http://login.m.taobao.com/cooperation/bindLogin.htm?code=%s&IBB=%s&appkey=%s"}[index];
        unbindUrl = new String[]{"https://accountlink.daily.taobao.net/sdkUnbind.htm", "https://accountlink.daily.taobao.net/sdkUnbind.htm", "https://accountlink.taobao.com/sdkUnbind.htm", "https://accountlink.taobao.com/sdkUnbind.htm"}[index];
        qrCodeLoginUrl = new String[]{"http://login.waptest.taobao.com/qrcodeShow.htm?appKey=%s&from=bcqrlogin", "http://login.waptest.taobao.com/qrcodeShow.htm?appKey=%s&from=bcqrlogin", "http://login.wapa.taobao.com/qrcodeShow.htm?appKey=%s&from=bcqrlogin", "http://login.m.taobao.com/qrcodeShow.htm?appKey=%s&from=bcqrlogin"}[index];
        getQrCodeContentUrl = new String[]{"http://qrlogin.daily.taobao.net/qrcodelogin/generateNoLoginQRCode.do?lt=m", "http://qrlogin.daily.taobao.net/qrcodelogin/generateNoLoginQRCode.do?lt=m", "https://qrlogin.taobao.com/qrcodelogin/generateNoLoginQRCode.do?lt=m", "https://qrlogin.taobao.com/qrcodelogin/generateNoLoginQRCode.do?lt=m"}[index];
        if (POSTFIX_OF_SECURITY_JPG_USER_SET == null) {
            POSTFIX_OF_SECURITY_JPG = new String[]{"", "", "", ""}[index];
            return;
        }
        POSTFIX_OF_SECURITY_JPG = POSTFIX_OF_SECURITY_JPG_USER_SET;
    }

    public int getMaxHistoryAccount() {
        return this.maxHistoryAccount;
    }

    public void setMaxHistoryAccount(int maxHistoryAccount2) {
        this.maxHistoryAccount = maxHistoryAccount2;
    }

    public static int getSite() {
        return site;
    }

    public static void setSite(int site2) {
        site = site2;
    }

    public static int getAppKeyIndex() {
        if (getInstance().getEnvironment() == null || !getInstance().getEnvironment().equals(Environment.TEST)) {
            return APP_KEY_INDEX;
        }
        return DAILY_APP_KEY_INDEX;
    }

    public static int getDailyAppKeyIndex() {
        return DAILY_APP_KEY_INDEX;
    }

    public static void setAppKeyIndex(int appKeyIndex) {
        APP_KEY_INDEX = appKeyIndex;
    }

    public static void setAppKeyIndex(int appKeyIndex, int dailyAppKeyIndex) {
        APP_KEY_INDEX = appKeyIndex;
        DAILY_APP_KEY_INDEX = dailyAppKeyIndex;
    }

    public String getQrCodeLoginUrl() {
        if (getEnvironment() == null || !getEnvironment().equals(Environment.TEST)) {
            return qrCodeLoginUrl;
        }
        return qrCodeLoginUrl_daily;
    }

    public String getSsoToken() {
        return this.ssoToken;
    }

    public void setSsoToken(String ssoToken2) {
        this.ssoToken = ssoToken2;
    }

    public Map<String, Object> getScanParams() {
        return this.scanParams;
    }

    public void setScanParams(Map<String, Object> scanParams2) {
        this.scanParams = scanParams2;
    }

    public String getOfflineDeviceID() {
        return this.offlineDeviceID;
    }

    public void setOfflineDeviceID(String offlineDeviceID2) {
        this.offlineDeviceID = offlineDeviceID2;
    }

    public int getOrientation() {
        return this.orientation;
    }

    public void setOrientation(int orientation2) {
        this.orientation = orientation2;
    }

    public boolean isSaveHistoryWithSalt() {
        return this.saveHistoryWithSalt;
    }

    public void setSaveHistoryWithSalt(boolean saveHistorySaltOnly) {
        this.saveHistoryWithSalt = saveHistorySaltOnly;
    }

    public Class<?> getFullyCustomizedLoginFragment() {
        return this.fullyCustomizedLoginFragment;
    }

    public void setFullyCustomizedLoginFragment(Class<?> fullyCustomizedLoginFragment2) {
        this.fullyCustomizedLoginFragment = fullyCustomizedLoginFragment2;
    }
}
