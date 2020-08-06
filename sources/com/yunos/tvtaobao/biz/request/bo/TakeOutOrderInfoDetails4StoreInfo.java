package com.yunos.tvtaobao.biz.request.bo;

import com.taobao.muniontaobaosdk.p4p.anticheat.model.ClientTraceData;
import java.io.Serializable;
import java.util.ArrayList;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class TakeOutOrderInfoDetails4StoreInfo implements Serializable {
    private static final long serialVersionUID = -610484326142980001L;
    private int dataSource;
    private int dataSourceType;
    private boolean distRst;
    private String latitude;
    private String longitude;
    private ArrayList<String> servingTimes;
    private String shopStatus;
    private String storeId;
    private String storeLogo;
    private String storeName;

    public String getStoreName() {
        return this.storeName;
    }

    public void setStoreName(String storeName2) {
        this.storeName = storeName2;
    }

    public String getStoreLogo() {
        return this.storeLogo;
    }

    public void setStoreLogo(String storeLogo2) {
        this.storeLogo = storeLogo2;
    }

    public String getStoreId() {
        return this.storeId;
    }

    public void setStoreId(String storeId2) {
        this.storeId = storeId2;
    }

    public String getShopStatus() {
        return this.shopStatus;
    }

    public void setShopStatus(String shopStatus2) {
        this.shopStatus = shopStatus2;
    }

    public ArrayList<String> getServingTimes() {
        return this.servingTimes;
    }

    public void setServingTimes(ArrayList<String> servingTimes2) {
        this.servingTimes = servingTimes2;
    }

    public String getLongitude() {
        return this.longitude;
    }

    public void setLongitude(String longitude2) {
        this.longitude = longitude2;
    }

    public String getLatitude() {
        return this.latitude;
    }

    public void setLatitude(String latitude2) {
        this.latitude = latitude2;
    }

    public boolean isDistRst() {
        return this.distRst;
    }

    public void setDistRst(boolean distRst2) {
        this.distRst = distRst2;
    }

    public int getDataSourceType() {
        return this.dataSourceType;
    }

    public void setDataSourceType(int dataSourceType2) {
        this.dataSourceType = dataSourceType2;
    }

    public int getDataSource() {
        return this.dataSource;
    }

    public void setDataSource(int dataSource2) {
        this.dataSource = dataSource2;
    }

    public static TakeOutOrderInfoDetails4StoreInfo resolverFromMtop(JSONObject obj) throws JSONException {
        if (obj == null) {
            return null;
        }
        TakeOutOrderInfoDetails4StoreInfo onTimeInfo = new TakeOutOrderInfoDetails4StoreInfo();
        if (obj == null) {
            return onTimeInfo;
        }
        if (!obj.isNull("servingTimes")) {
            JSONArray array = obj.getJSONArray("servingTimes");
            ArrayList<String> temp = new ArrayList<>();
            for (int i = 0; i < array.length(); i++) {
                temp.add((String) array.get(i));
            }
            onTimeInfo.setServingTimes(temp);
        }
        onTimeInfo.setDataSourceType(obj.optInt("dataSourceType", 0));
        onTimeInfo.setDataSource(obj.optInt("dataSource", 0));
        onTimeInfo.setDistRst(obj.optBoolean("distRst", false));
        onTimeInfo.setLatitude(obj.optString(ClientTraceData.b.d));
        onTimeInfo.setLongitude(obj.optString(ClientTraceData.b.f54c));
        onTimeInfo.setStoreName(obj.optString("storeName"));
        onTimeInfo.setStoreLogo(obj.optString("storeLogo"));
        onTimeInfo.setStoreId(obj.optString("storeId"));
        onTimeInfo.setShopStatus(obj.optString("shopStatus"));
        return onTimeInfo;
    }
}
