package anetwork.channel.statist;

import android.text.TextUtils;
import anet.channel.util.ALog;
import anet.channel.util.HttpUrl;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;
import org.json.JSONObject;

public class StatisticReqTimes {
    private static final String TAG = "awcn.StatisticReqTimes";
    private static StatisticReqTimes instance;
    private Set<String> currentReqUrls;
    private long finalResult;
    private boolean isStarting;
    private long startPoint;
    private Set<String> whiteReqUrls;

    private StatisticReqTimes() {
        initAttrs();
    }

    public static StatisticReqTimes getIntance() {
        if (instance == null) {
            synchronized (StatisticReqTimes.class) {
                if (instance == null) {
                    instance = new StatisticReqTimes();
                }
            }
        }
        return instance;
    }

    private void initAttrs() {
        this.isStarting = false;
        this.startPoint = 0;
        this.finalResult = 0;
        if (this.currentReqUrls == null) {
            this.currentReqUrls = new HashSet();
        } else {
            this.currentReqUrls.clear();
        }
        if (this.whiteReqUrls == null) {
            this.whiteReqUrls = new HashSet();
        }
    }

    public void updateWhiteReqUrls(String urlsFromOrange) {
        if (this.whiteReqUrls == null) {
            this.whiteReqUrls = new HashSet();
        } else {
            this.whiteReqUrls.clear();
        }
        if (ALog.isPrintLog(2)) {
            ALog.i(TAG, "urlsFromOrange: " + urlsFromOrange, (String) null, new Object[0]);
        }
        if (!TextUtils.isEmpty(urlsFromOrange)) {
            try {
                Iterator keyIter = new JSONObject(urlsFromOrange).keys();
                while (keyIter.hasNext()) {
                    this.whiteReqUrls.add(keyIter.next());
                }
            } catch (Exception e) {
                ALog.e(TAG, "whiteReqUrls from orange isnot json format", (String) null, new Object[0]);
            }
        }
    }

    public void start() {
        if (ALog.isPrintLog(2)) {
            ALog.i(TAG, "start statistic req times", (String) null, new Object[0]);
        }
        initAttrs();
        this.isStarting = true;
    }

    public void putReq(HttpUrl httpUrl) {
        if (this.isStarting && httpUrl != null) {
            String path = httpUrl.path();
            if (this.whiteReqUrls.contains(path)) {
                if (this.currentReqUrls.isEmpty()) {
                    this.startPoint = System.currentTimeMillis();
                }
                this.currentReqUrls.add(path);
            }
        }
    }

    public void updateReqTimes(HttpUrl httpUrl, long endTime) {
        if (this.isStarting && endTime > 0 && httpUrl != null) {
            if (this.currentReqUrls.remove(httpUrl.path()) && this.currentReqUrls.isEmpty()) {
                long curReqTimes = System.currentTimeMillis() - this.startPoint;
                ALog.i(TAG, "this req spend times: " + curReqTimes, (String) null, new Object[0]);
                this.finalResult += curReqTimes;
            }
        }
    }

    public long end() {
        long result = 0;
        if (this.isStarting) {
            result = this.finalResult;
            if (ALog.isPrintLog(2)) {
                ALog.i(TAG, "finalResult:" + this.finalResult, (String) null, new Object[0]);
            }
        }
        initAttrs();
        return result;
    }
}
