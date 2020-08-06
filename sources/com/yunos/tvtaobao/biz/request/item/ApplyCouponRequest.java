package com.yunos.tvtaobao.biz.request.item;

import anetwork.channel.NetworkListenerState;
import com.yunos.tv.core.util.RSASign;
import com.yunos.tvtaobao.biz.request.base.BaseHttpRequest;
import com.zhiping.dev.android.logger.ZpLogger;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.apache.http.Header;
import org.apache.http.message.BasicHeader;

public class ApplyCouponRequest extends BaseHttpRequest {
    public static final String TAG = ApplyCouponRequest.class.getSimpleName();
    private static final String apiVersion = "1.0";
    private static final byte[] pubKeyBytes = {48, -126, 1, 34, 48, 13, 6, 9, 42, -122, 72, -122, -9, 13, 1, 1, 1, 5, 0, 3, -126, 1, 15, 0, 48, -126, 1, 10, 2, -126, 1, 1, 0, -67, 54, Byte.MIN_VALUE, -94, -106, 16, 25, 66, -23, 98, -81, -28, -38, 90, 61, 119, -61, -85, 122, -104, 53, -30, -92, 10, 38, 80, 75, 81, 62, -108, 11, -36, -69, 58, -28, 28, 25, 4, 34, -36, 91, -69, -8, 61, -93, Byte.MAX_VALUE, 115, 101, 60, -77, 89, -90, -105, 28, -127, -8, -3, 49, -22, -55, 71, 76, 24, -5, 74, -14, -3, 83, 126, 10, -44, -83, -44, 103, 51, -92, -35, -71, 53, 46, 116, -44, 33, -61, -74, -82, 66, -12, -36, 9, 105, -21, -61, -66, -117, -12, -4, -87, 56, 12, 47, 108, -88, -87, 67, 89, -116, 71, 4, -104, 35, 105, -17, -60, -97, 22, 50, 3, 30, 98, -2, -47, 64, -68, 88, 126, -90, 79, 96, -9, -111, 53, 29, 70, 122, -72, 85, -10, -28, -27, 10, 10, 12, -87, -117, 122, -36, -13, -96, -8, -78, -11, -44, Byte.MAX_VALUE, -93, -10, 118, -11, -43, 101, 58, -23, 122, Byte.MAX_VALUE, -93, 85, -96, 99, -55, NetworkListenerState.ALL, 55, -68, 89, 34, -92, -25, 17, 71, 108, -83, -45, -119, 39, -8, -57, 21, -113, 114, -17, -76, 35, 2, 56, -103, Byte.MIN_VALUE, -112, 29, 51, -122, -124, 20, 86, 76, -116, -48, 11, 58, -47, -29, 11, -27, -92, -41, 56, -19, 67, 45, -73, -31, 46, 89, -117, 82, -15, 63, -56, -117, 53, 49, 33, -110, -106, 97, -108, -84, 64, 43, 85, -44, 15, 123, -20, -37, 85, 34, -61, 47, 111, -25, -103, -37, -88, 111, 114, -89, 33, 2, 3, 1, 0, 1};
    public String couponKey = "";
    public String itemId = "";
    public String pid = "";
    private String reqTimeStamp = "";
    public String userId = "";

    public String getItemId() {
        return this.itemId;
    }

    public void setItemId(String itemId2) {
        this.itemId = itemId2;
    }

    public String getCouponKey() {
        return this.couponKey;
    }

    public void setCouponKey(String conponKey) {
        this.couponKey = conponKey;
    }

    public String getUserId() {
        return this.userId;
    }

    public void setUserId(String userId2) {
        this.userId = userId2;
    }

    public String getPid() {
        return this.pid;
    }

    public void setPid(String pid2) {
        this.pid = pid2;
    }

    public ApplyCouponRequest(String itemId2, String couponKey2, String userId2) {
        this.itemId = itemId2;
        this.couponKey = couponKey2;
        this.userId = userId2;
        this.pid = "mm_113859958_12524229_70604430";
    }

    public ApplyCouponRequest(String itemId2, String pid2, String couponKey2, String userId2) {
        this.itemId = itemId2;
        this.couponKey = couponKey2;
        this.userId = userId2;
        this.pid = pid2;
    }

    public String resolveResult(String result) throws Exception {
        ZpLogger.v(TAG, "response: " + result);
        return result;
    }

    /* access modifiers changed from: protected */
    public Map<String, String> getAppData() {
        Map<String, String> params = new HashMap<>();
        this.reqTimeStamp = Long.valueOf(System.currentTimeMillis() / 1000).toString();
        String encryptedData = encryptBizParams();
        ZpLogger.v(TAG, "encryptedData:=====\n" + encryptedData);
        params.put("apiVersion", "1.0");
        params.put("timestamp", this.reqTimeStamp);
        params.put("encryptStr", encryptedData);
        return params;
    }

    /* access modifiers changed from: protected */
    public String getHttpDomain() {
        return "http://uland.taobao.com/cp/outer_coupon_apply";
    }

    public String toString() {
        return null;
    }

    public List<Header> getHttpHeader() {
        List<Header> headers = new ArrayList<>();
        headers.add(new BasicHeader("X-Requested-With", "XMLHttpRequest"));
        return headers;
    }

    private String encryptBizParams() {
        try {
            return URLEncoder.encode(RSASign.encryptWithPubkey(pubKeyBytes, "itemId=" + this.itemId + "&couponKey=" + this.couponKey + "&userId=" + this.userId + "&pid=" + this.pid + "&ts=" + this.reqTimeStamp), "UTF-8");
        } catch (Exception e) {
            ZpLogger.v(TAG, "encrypted error:" + e.getMessage());
            e.printStackTrace();
            return "";
        }
    }

    private String decryptBizParams(String encryptedData) {
        return "";
    }
}
