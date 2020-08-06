package android.taobao.windvane.packageapp.zipapp.utils;

import android.annotation.TargetApi;
import android.taobao.windvane.config.WVCommonConfig;
import android.taobao.windvane.monitor.WVMonitorService;
import android.taobao.windvane.packageapp.zipapp.ZipAppManager;
import android.taobao.windvane.packageapp.zipapp.data.ZipAppResultCode;
import android.taobao.windvane.util.DigestUtils;
import android.taobao.windvane.util.TaoLog;
import android.taobao.windvane.util.WVNativeCallbackUtil;
import android.taobao.windvane.util.WVUrlUtil;
import android.text.TextUtils;
import android.util.LruCache;
import com.taobao.muniontaobaosdk.p4p.anticheat.model.ClientTraceData;
import java.util.HashMap;
import java.util.Iterator;
import org.json.JSONObject;

public class WVZipSecurityManager {
    private static int MAX_LRU_CACHE_SIZE = 1000;
    private static WVZipSecurityManager mSecTokenCache;
    private String TAG = WVZipSecurityManager.class.getSimpleName();
    private LruCache<String, String> mLruCache = new LruCache<>(MAX_LRU_CACHE_SIZE);
    private HashMap<String, String> mSampleMap = new HashMap<>();

    public void setSampleMap(HashMap<String, String> SampleMap) {
        this.mSampleMap = SampleMap;
    }

    public void parseSampleMap(String data) {
        try {
            if (TaoLog.getLogStatus()) {
                TaoLog.d(this.TAG, "每个app的采样率配置信息  data = " + data);
            }
            this.mSampleMap = new HashMap<>();
            JSONObject jsonObject = new JSONObject(data);
            Iterator<String> keys = jsonObject.keys();
            while (keys.hasNext()) {
                String key = keys.next();
                this.mSampleMap.put(key, jsonObject.getString(key));
            }
        } catch (Exception e) {
            TaoLog.e(this.TAG, "app的采样率配置信息  error = " + e.getMessage());
        }
    }

    public static synchronized WVZipSecurityManager getInstance() {
        WVZipSecurityManager wVZipSecurityManager;
        synchronized (WVZipSecurityManager.class) {
            if (mSecTokenCache == null) {
                mSecTokenCache = new WVZipSecurityManager();
            }
            wVZipSecurityManager = mSecTokenCache;
        }
        return wVZipSecurityManager;
    }

    @TargetApi(12)
    WVZipSecurityManager() {
    }

    @TargetApi(12)
    public void put(String key, String value) {
        if (this.mLruCache != null && !TextUtils.isEmpty(key) && !TextUtils.isEmpty(value)) {
            this.mLruCache.put(key, value);
        }
    }

    @TargetApi(12)
    public String get(String key) {
        if (this.mLruCache == null || TextUtils.isEmpty(key)) {
            return null;
        }
        return this.mLruCache.get(key);
    }

    @TargetApi(12)
    public int getLruSize() {
        if (this.mLruCache != null) {
            return this.mLruCache.size();
        }
        return 0;
    }

    @TargetApi(12)
    public void remove(String key) {
        if (this.mLruCache != null && key != null) {
            this.mLruCache.remove(key);
            if (TaoLog.getLogStatus()) {
                TaoLog.d(this.TAG, "remove cache, " + key);
            }
        }
    }

    public void evictAll() {
        if (this.mLruCache != null) {
            this.mLruCache.evictAll();
        }
    }

    public boolean isFileSecrity(String url, byte[] bytes, String path, String appName) {
        try {
            TaoLog.d(this.TAG, "开始安全校验 ");
            long begin = System.currentTimeMillis();
            VerifyUtData utdata = new VerifyUtData();
            boolean result = isFileSecrity(url, bytes, path, utdata, appName);
            if (WVMonitorService.getPerformanceMonitor() == null) {
                return result;
            }
            if (TaoLog.getLogStatus()) {
                TaoLog.d(this.TAG, "  安全校验 埋点信息 utdata.verifyResTime=【" + utdata.verifyResTime + "】  utdata.verifyTime=【" + utdata.verifyTime + "】  utdata.verifyError=【" + utdata.verifyError + "】 LRUcache size =【 " + getLruSize() + "】");
            }
            WVMonitorService.getPerformanceMonitor().didGetResourceVerifyCode(url, utdata.verifyResTime, utdata.verifyTime, utdata.verifyError, getLruSize());
            if (!result && TaoLog.getLogStatus()) {
                TaoLog.d(this.TAG, "  安全校验 失败 url=" + url);
            }
            if (!TaoLog.getLogStatus()) {
                return result;
            }
            TaoLog.d(this.TAG, "  安全校验 成功 result =" + result + "cost time【" + (System.currentTimeMillis() - begin) + "】");
            return result;
        } catch (Exception e) {
            return false;
        }
    }

    public double getAppSample(String name) {
        parseSampleMap(WVCommonConfig.commonConfig.verifySampleRate);
        if (!(name == null || this.mSampleMap == null || this.mSampleMap.size() <= 0)) {
            try {
                double rate = Double.parseDouble(this.mSampleMap.get(name));
                if (rate < ClientTraceData.b.f47a || rate > 1.0d) {
                    return -1.0d;
                }
                return rate;
            } catch (Exception e) {
                TaoLog.d(this.TAG, "获取【" + name + "】采样率失败" + "数据格式错误error :" + e.getMessage());
            }
        }
        return -1.0d;
    }

    @TargetApi(12)
    public boolean isFileSecrity(String url, byte[] bytes, String path, VerifyUtData utdata, String appName) {
        String url2 = WVUrlUtil.removeQueryParam(url);
        long begin = System.currentTimeMillis();
        if (this.mLruCache.get(url2) == null) {
            int index = path.lastIndexOf(WVNativeCallbackUtil.SEPERATER);
            if (index < 0) {
                TaoLog.d(this.TAG, "本地资源的绝对路径出错 path= " + path);
                return false;
            }
            int val = ZipAppManager.getInstance().validRunningZipPackage(path.substring(0, index + 1) + ZipAppConstants.APP_RES_INC_NAME);
            int val2 = ZipAppManager.getInstance().validRunningZipPackage(path.substring(0, index + 1) + ZipAppConstants.APP_RES_NAME);
            if (val != ZipAppResultCode.SECCUSS) {
                utdata.verifyError = val;
            } else if (val2 != ZipAppResultCode.SECCUSS) {
                utdata.verifyError = val2;
            }
            utdata.verifyResTime = System.currentTimeMillis() - begin;
            TaoLog.e(this.TAG, "validRunningZipPackage all time =【" + utdata.verifyResTime + "】");
            if (utdata.verifyError != ZipAppResultCode.SECCUSS) {
                return false;
            }
        }
        String datamd5 = DigestUtils.md5ToHex(bytes);
        utdata.verifyTime = System.currentTimeMillis() - begin;
        if (this.mLruCache != null && datamd5.equals(this.mLruCache.get(url2))) {
            return true;
        }
        utdata.verifyError = ZipAppResultCode.ERR_MD5_RES;
        return false;
    }
}
