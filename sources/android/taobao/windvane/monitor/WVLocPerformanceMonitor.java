package android.taobao.windvane.monitor;

import android.content.Context;
import android.taobao.windvane.config.GlobalConfig;
import android.taobao.windvane.service.WVEventContext;
import android.taobao.windvane.service.WVEventListener;
import android.taobao.windvane.service.WVEventResult;
import android.taobao.windvane.util.TaoLog;
import android.taobao.windvane.webview.IWVWebView;
import com.alibaba.fastjson.JSON;
import java.util.HashMap;

public class WVLocPerformanceMonitor implements WVEventListener {
    public static final int APP_STATUS = 1;
    public static final int LOADURL_STATUS = 3;
    private static String TAG = WVLocPerformanceMonitor.class.getSimpleName();
    public static final int WEBVIEW_STATUS = 2;
    private static WVLocPerformanceMonitor instance = null;
    private static boolean isOpenLocPerformanceMonitor = false;
    public float cpu_app = 0.0f;
    public float cpu_loadurl = 0.0f;
    public float cpu_webview = 0.0f;
    private boolean isInit = false;
    public float mem_app = 0.0f;
    public float mem_loadurl = 0.0f;
    public float mem_webview = 0.0f;
    private HashMap<String, Object> monitorData;
    private long time_load = 0;
    private long time_load_start = 0;
    private long time_webview = 0;
    private long time_webview_start = 0;

    public static WVLocPerformanceMonitor getInstance() {
        if (instance == null) {
            synchronized (WVMonitorConfigManager.class) {
                instance = new WVLocPerformanceMonitor();
            }
        }
        return instance;
    }

    public void setCpuAndMemery(Context cxt, int type) {
        if (!isOpenLocPerformanceMonitor()) {
            TaoLog.d(TAG, "非debug状态，不开启性能数据采集模式");
            return;
        }
        switch (type) {
            case 1:
                this.cpu_app = 0.0f;
                this.mem_app = 0.0f;
                break;
            case 2:
                this.cpu_webview = 0.0f;
                this.mem_webview = 0.0f;
                break;
            case 3:
                this.cpu_loadurl = 0.0f;
                this.mem_loadurl = 0.0f;
                break;
        }
        this.isInit = true;
    }

    public void reset() {
        this.cpu_app = 0.0f;
        this.mem_app = 0.0f;
        this.cpu_webview = 0.0f;
        this.mem_webview = 0.0f;
        this.time_webview = 0;
        this.cpu_loadurl = 0.0f;
        this.mem_loadurl = 0.0f;
        this.time_load = 0;
        this.isInit = false;
    }

    public String toString() {
        if (this.isInit) {
            try {
                String data = JSON.toJSONString(getInstance());
                TaoLog.d(TAG, "data: " + data);
                return data;
            } catch (Exception e) {
                e.printStackTrace();
                TaoLog.d(TAG, "性能数据采集失败，json解析异常 json 解析异常：" + e.getMessage());
            }
        } else {
            TaoLog.d(TAG, "性能数据未初始化");
            return null;
        }
    }

    public HashMap<String, Object> getMonitorData() {
        return this.monitorData;
    }

    public void setMonitorData(HashMap<String, Object> data) {
        this.monitorData = data;
        setCpuAndMemery(GlobalConfig.context, 3);
    }

    public WVEventResult onEvent(int id, WVEventContext ctx, Object... obj) {
        if (3009 == id) {
            setCpuAndMemery(GlobalConfig.context, 1);
            return null;
        } else if (3008 == id) {
            this.time_webview_start = System.currentTimeMillis();
            return null;
        } else if (3010 == id) {
            this.time_load_start = System.currentTimeMillis();
            this.time_webview = this.time_load_start - this.time_webview_start;
            setCpuAndMemery(GlobalConfig.context, 2);
            return null;
        } else if (1001 == id) {
            this.time_load_start = System.currentTimeMillis();
            return null;
        } else if (1002 != id) {
            return null;
        } else {
            try {
                this.time_load = System.currentTimeMillis() - this.time_load_start;
                setCpuAndMemery(GlobalConfig.context, 3);
                IWVWebView iWVWebView = ctx.webView;
                IWVWebView.JsbridgeHis.clear();
                return null;
            } catch (Exception e) {
                return null;
            }
        }
    }

    public static boolean isOpenLocPerformanceMonitor() {
        return isOpenLocPerformanceMonitor;
    }

    public static void setOpenLocPerformanceMonitor(boolean isOpenLocPerformanceMonitor2) {
        isOpenLocPerformanceMonitor = isOpenLocPerformanceMonitor2;
    }
}
