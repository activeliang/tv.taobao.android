package com.alibaba.analytics.core.sync;

import com.alibaba.analytics.core.Variables;
import com.alibaba.analytics.core.config.SystemConfigMgr;
import com.alibaba.analytics.utils.AppInfoUtil;
import com.alibaba.analytics.utils.SpSetting;

public class TnetHostPortMgr implements SystemConfigMgr.IKVChangeListener {
    public static final String TAG_TNET_HOST_PORT = "utanalytics_tnet_host_port";
    public static TnetHostPortMgr instance;
    public TnetHostPort entity;

    public static synchronized TnetHostPortMgr getInstance() {
        TnetHostPortMgr tnetHostPortMgr;
        synchronized (TnetHostPortMgr.class) {
            if (instance == null) {
                instance = new TnetHostPortMgr();
            }
            tnetHostPortMgr = instance;
        }
        return tnetHostPortMgr;
    }

    TnetHostPortMgr() {
        try {
            this.entity = new TnetHostPort();
            parseConifg(AppInfoUtil.getString(Variables.getInstance().getContext(), TAG_TNET_HOST_PORT));
            parseConifg(SpSetting.get(Variables.getInstance().getContext(), TAG_TNET_HOST_PORT));
            parseConifg(SystemConfigMgr.getInstance().get(TAG_TNET_HOST_PORT));
            SystemConfigMgr.getInstance().register(TAG_TNET_HOST_PORT, this);
        } catch (Throwable th) {
        }
    }

    public TnetHostPort getEntity() {
        return this.entity;
    }

    public void onChange(String key, String value) {
        parseConifg(value);
    }

    /* JADX WARNING: Code restructure failed: missing block: B:2:0x0006, code lost:
        r6 = r6.trim();
     */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    private void parseConifg(java.lang.String r6) {
        /*
            r5 = this;
            boolean r3 = android.text.TextUtils.isEmpty(r6)
            if (r3 != 0) goto L_0x0037
            java.lang.String r6 = r6.trim()
            java.lang.String r3 = ":"
            int r0 = r6.indexOf(r3)
            r3 = -1
            if (r0 == r3) goto L_0x0037
            r3 = 0
            java.lang.String r1 = r6.substring(r3, r0)
            int r3 = r0 + 1
            int r4 = r6.length()
            java.lang.String r3 = r6.substring(r3, r4)
            int r2 = java.lang.Integer.parseInt(r3)
            boolean r3 = android.text.TextUtils.isEmpty(r1)
            if (r3 != 0) goto L_0x0037
            if (r2 <= 0) goto L_0x0037
            com.alibaba.analytics.core.sync.TnetHostPortMgr$TnetHostPort r3 = r5.entity
            r3.host = r1
            com.alibaba.analytics.core.sync.TnetHostPortMgr$TnetHostPort r3 = r5.entity
            r3.port = r2
        L_0x0037:
            return
        */
        throw new UnsupportedOperationException("Method not decompiled: com.alibaba.analytics.core.sync.TnetHostPortMgr.parseConifg(java.lang.String):void");
    }

    public static class TnetHostPort {
        public String host = "adashx.m.taobao.com";
        public int port = 443;

        public String getHost() {
            return this.host;
        }

        public int getPort() {
            return this.port;
        }
    }
}
