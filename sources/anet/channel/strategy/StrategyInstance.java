package anet.channel.strategy;

import android.content.Context;
import android.taobao.windvane.jsbridge.utils.WVUtils;
import android.taobao.windvane.util.WVConstants;
import android.text.TextUtils;
import anet.channel.status.NetworkStatusHelper;
import anet.channel.strategy.StrategyResultParser;
import anet.channel.strategy.dispatch.AmdcRuntimeInfo;
import anet.channel.strategy.dispatch.DispatchEvent;
import anet.channel.strategy.dispatch.HttpDispatcher;
import anet.channel.strategy.utils.AmdcThreadPoolExecutor;
import anet.channel.util.ALog;
import anet.channel.util.HttpUrl;
import anet.channel.util.StringUtils;
import anet.channel.util.Utils;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.concurrent.CopyOnWriteArraySet;
import mtopsdk.common.util.SymbolExpUtil;
import org.json.JSONObject;

class StrategyInstance implements IStrategyInstance, HttpDispatcher.IDispatchEventListener {
    private static final String TAG = "awcn.StrategyCenter";
    protected StrategyInfoHolder holder = null;
    private boolean isInitialized = false;
    private long lastPersistentTime = 0;
    private CopyOnWriteArraySet<IStrategyListener> listeners = new CopyOnWriteArraySet<>();

    StrategyInstance() {
    }

    public synchronized void initialize(Context context) {
        if (!this.isInitialized && context != null) {
            try {
                ALog.i(TAG, "StrategyCenter initialize started.", (String) null, new Object[0]);
                AmdcRuntimeInfo.setContext(context);
                StrategySerializeHelper.initialize(context);
                NetworkStatusHelper.startListener(context);
                HttpDispatcher.getInstance().addListener(this);
                this.holder = StrategyInfoHolder.newInstance();
                this.isInitialized = true;
                ALog.i(TAG, "StrategyCenter initialize finished.", (String) null, new Object[0]);
            } catch (Exception e) {
                ALog.e(TAG, "StrategyCenter initialize failed.", (String) null, e, new Object[0]);
            }
        }
        return;
    }

    public synchronized void switchEnv() {
        if (this.holder != null) {
            this.holder.clear();
            this.holder = StrategyInfoHolder.newInstance();
        }
        StrategySerializeHelper.clearStrategyFolder();
        HttpDispatcher.getInstance().switchENV();
    }

    @Deprecated
    public String getSchemeByHost(String host) {
        return getSchemeByHost(host, (String) null);
    }

    public String getSchemeByHost(String host, String dftScheme) {
        if (TextUtils.isEmpty(host)) {
            return null;
        }
        if (checkHolderIsNull()) {
            return dftScheme;
        }
        String safeAisles = this.holder.schemeMap.getSafeAislesByHost(host);
        if (safeAisles == null && !TextUtils.isEmpty(dftScheme)) {
            safeAisles = dftScheme;
        }
        if (safeAisles == null && (safeAisles = SchemeGuesser.getInstance().guessScheme(host)) == null) {
            safeAisles = "http";
        }
        ALog.d(TAG, "getSchemeByHost", (String) null, "host", host, "scheme", safeAisles);
        return safeAisles;
    }

    public String getCNameByHost(String host) {
        if (checkHolderIsNull() || TextUtils.isEmpty(host)) {
            return null;
        }
        return this.holder.getCurrStrategyTable().getCnameByHost(host);
    }

    public String getFormalizeUrl(String rawUrlString) {
        HttpUrl httpUrl = HttpUrl.parse(rawUrlString);
        if (httpUrl == null) {
            ALog.e(TAG, "url is invalid.", (String) null, WVConstants.INTENT_EXTRA_URL, rawUrlString, "stack", Utils.getStackMsg(new Exception("getFormalizeUrl")));
            return null;
        }
        String ret = rawUrlString;
        try {
            String safeAisle = getSchemeByHost(httpUrl.host(), httpUrl.scheme());
            if (!safeAisle.equalsIgnoreCase(httpUrl.scheme())) {
                ret = StringUtils.concatString(safeAisle, SymbolExpUtil.SYMBOL_COLON, rawUrlString.substring(rawUrlString.indexOf(WVUtils.URL_SEPARATOR)));
            }
            if (!ALog.isPrintLog(1)) {
                return ret;
            }
            ALog.d(TAG, "", (String) null, "raw", StringUtils.simplifyString(rawUrlString, 128), "ret", StringUtils.simplifyString(ret, 128));
            return ret;
        } catch (Exception e) {
            ALog.e(TAG, "getFormalizeUrl failed", (String) null, e, "raw", rawUrlString);
            return ret;
        }
    }

    @Deprecated
    public String getFormalizeUrl(String rawUrlString, String dft) {
        return getFormalizeUrl(rawUrlString);
    }

    public List<IConnStrategy> getConnStrategyListByHost(String host) {
        if (TextUtils.isEmpty(host) || checkHolderIsNull()) {
            return Collections.EMPTY_LIST;
        }
        String cname = this.holder.getCurrStrategyTable().getCnameByHost(host);
        if (!TextUtils.isEmpty(cname)) {
            host = cname;
        }
        List<IConnStrategy> list = this.holder.getCurrStrategyTable().queryByHost(host);
        if (list.isEmpty()) {
            list = this.holder.localDnsStrategyTable.queryByHost(host);
        }
        if (!ALog.isPrintLog(1)) {
            return list;
        }
        ALog.d("getConnStrategyListByHost", (String) null, "host", host, "result", list);
        return list;
    }

    public void forceRefreshStrategy(String host) {
        if (!checkHolderIsNull() && !TextUtils.isEmpty(host)) {
            ALog.i(TAG, "force refresh strategy", (String) null, "host", host);
            this.holder.getCurrStrategyTable().sendAmdcRequest(host, true);
        }
    }

    public void registerListener(IStrategyListener listener) {
        if (listener != null) {
            this.listeners.add(listener);
        }
    }

    public void unregisterListener(IStrategyListener listener) {
        this.listeners.remove(listener);
    }

    @Deprecated
    public String getUnitPrefix(String userId, String utdid) {
        return null;
    }

    @Deprecated
    public void setUnitPrefix(String userId, String utdid, String unitPrefix) {
    }

    public String getUnitByHost(String host) {
        if (checkHolderIsNull()) {
            return null;
        }
        return this.holder.hostUnitMap.getUnitByHost(host);
    }

    public String getClientIp() {
        if (checkHolderIsNull()) {
            return "";
        }
        return this.holder.getCurrStrategyTable().clientIp;
    }

    public void notifyConnEvent(String host, IConnStrategy connStrategy, ConnEvent connEvent) {
        if (!checkHolderIsNull() && connStrategy != null && (connStrategy instanceof IPConnStrategy)) {
            IPConnStrategy ips = (IPConnStrategy) connStrategy;
            if (ips.ipSource == 1) {
                this.holder.localDnsStrategyTable.notifyConnEvent(host, connStrategy, connEvent);
            } else if (ips.ipSource == 0) {
                this.holder.getCurrStrategyTable().notifyConnEvent(host, connStrategy, connEvent);
            }
        }
    }

    /* access modifiers changed from: private */
    public boolean checkHolderIsNull() {
        if (this.holder != null) {
            return false;
        }
        ALog.w("StrategyCenter not initialized", (String) null, "isInitialized", Boolean.valueOf(this.isInitialized));
        return true;
    }

    public void onEvent(DispatchEvent event) {
        if (event.eventType == 1 && this.holder != null) {
            ALog.d(TAG, "receive DNS event", (String) null, new Object[0]);
            StrategyResultParser.HttpDnsResponse response = StrategyResultParser.parse((JSONObject) event.extraObject);
            if (response != null) {
                this.holder.update(response);
                saveData();
                Iterator i$ = this.listeners.iterator();
                while (i$.hasNext()) {
                    i$.next().onStrategyUpdated(response);
                }
            }
        }
    }

    public synchronized void saveData() {
        ALog.i(TAG, "saveData", (String) null, new Object[0]);
        long now = System.currentTimeMillis();
        if (now - this.lastPersistentTime > 30000) {
            this.lastPersistentTime = now;
            AmdcThreadPoolExecutor.scheduleTask(new Runnable() {
                public void run() {
                    if (!StrategyInstance.this.checkHolderIsNull()) {
                        StrategyInstance.this.holder.saveData();
                    }
                }
            }, 500);
        }
    }
}
