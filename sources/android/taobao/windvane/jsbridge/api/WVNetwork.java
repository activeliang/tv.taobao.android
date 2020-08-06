package android.taobao.windvane.jsbridge.api;

import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.taobao.windvane.jsbridge.WVApiPlugin;
import android.taobao.windvane.jsbridge.WVCallBackContext;
import android.taobao.windvane.jsbridge.WVResult;

public class WVNetwork extends WVApiPlugin {
    private final int NETWORK_TYPE_1xRTT = 7;
    private final int NETWORK_TYPE_CDMA = 4;
    private final int NETWORK_TYPE_EDGE = 2;
    private final int NETWORK_TYPE_EHRPD = 14;
    private final int NETWORK_TYPE_EVDO_0 = 5;
    private final int NETWORK_TYPE_EVDO_A = 6;
    private final int NETWORK_TYPE_EVDO_B = 12;
    private final int NETWORK_TYPE_GPRS = 1;
    private final int NETWORK_TYPE_HSDPA = 8;
    private final int NETWORK_TYPE_HSPA = 10;
    private final int NETWORK_TYPE_HSPAP = 15;
    private final int NETWORK_TYPE_HSUPA = 9;
    private final int NETWORK_TYPE_IDEN = 11;
    private final int NETWORK_TYPE_LTE = 13;
    private final int NETWORK_TYPE_UMTS = 3;

    public boolean execute(String action, String params, WVCallBackContext callback) {
        if (!"getNetworkType".equals(action)) {
            return false;
        }
        getNetworkType(params, callback);
        return true;
    }

    public final void getNetworkType(String params, WVCallBackContext callback) {
        WVResult result = new WVResult();
        NetworkInfo info = ((ConnectivityManager) this.mContext.getSystemService("connectivity")).getActiveNetworkInfo();
        if (info == null) {
            result.addData("type", "NONE");
            callback.success(result);
        } else if (info.getType() == 1) {
            result.addData("type", "WIFI");
            callback.success(result);
        } else {
            switch (info.getSubtype()) {
                case 1:
                    result.addData("message", "GPRS");
                    result.addData("type", "2G");
                    break;
                case 2:
                    result.addData("message", "EDGE");
                    result.addData("type", "2G");
                    break;
                case 3:
                    result.addData("message", "UMTS");
                    result.addData("type", "3G");
                    break;
                case 4:
                    result.addData("message", "CDMA");
                    result.addData("type", "2G");
                    break;
                case 5:
                    result.addData("message", "EVDO_0");
                    result.addData("type", "3G");
                    break;
                case 6:
                    result.addData("message", "EVDO_A");
                    result.addData("type", "3G");
                    break;
                case 7:
                    result.addData("message", "1xRTT");
                    result.addData("type", "2G");
                    break;
                case 8:
                    result.addData("message", "HSDPA");
                    result.addData("type", "3G");
                    break;
                case 9:
                    result.addData("message", "HSUPA");
                    result.addData("type", "3G");
                    break;
                case 10:
                    result.addData("message", "HSPA");
                    result.addData("type", "3G");
                    break;
                case 11:
                    result.addData("message", "IDEN");
                    result.addData("type", "2G");
                    break;
                case 12:
                    result.addData("message", "EVDO_B");
                    result.addData("type", "3G");
                    break;
                case 13:
                    result.addData("message", "LTE");
                    result.addData("type", "4G");
                    break;
                case 14:
                    result.addData("message", "EHRPD");
                    result.addData("type", "3G");
                    break;
                case 15:
                    result.addData("message", "HSPAP");
                    result.addData("type", "3G");
                    break;
                default:
                    result.addData("type", "UNKNOWN");
                    break;
            }
            callback.success(result);
        }
    }
}
