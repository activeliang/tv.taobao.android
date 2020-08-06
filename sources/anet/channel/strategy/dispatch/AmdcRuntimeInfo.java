package anet.channel.strategy.dispatch;

import android.content.Context;
import anet.channel.util.ALog;
import com.taobao.muniontaobaosdk.p4p.anticheat.model.ClientTraceData;

public class AmdcRuntimeInfo {
    private static final String TAG = "awcn.AmdcRuntimeInfo";
    private static volatile int amdcLimitLevel = 0;
    private static volatile long amdcLimitTime = 0;
    private static volatile Context context;
    private static IAmdcSign iSign = null;
    public static volatile double latitude = ClientTraceData.b.f47a;
    public static volatile double longitude = ClientTraceData.b.f47a;

    public static void updateLocation(double lat, double lng) {
        latitude = lat;
        longitude = lng;
    }

    public static void updateAmdcLimit(int level, int time) {
        ALog.i(TAG, "set amdc limit", (String) null, "level", Integer.valueOf(level), "time", Integer.valueOf(time));
        if (amdcLimitLevel != level) {
            amdcLimitLevel = level;
            amdcLimitTime = System.currentTimeMillis() + (((long) time) * 1000);
        }
    }

    public static int getAmdcLimitLevel() {
        if (amdcLimitLevel > 0 && System.currentTimeMillis() - amdcLimitTime > 0) {
            amdcLimitTime = 0;
            amdcLimitLevel = 0;
        }
        return amdcLimitLevel;
    }

    public static void setContext(Context context2) {
        context = context2;
    }

    public static Context getContext() {
        return context;
    }

    public static void setSign(IAmdcSign sign) {
        iSign = sign;
    }

    public static IAmdcSign getSign() {
        return iSign;
    }
}
