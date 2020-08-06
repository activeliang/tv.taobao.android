package com.loc;

import android.os.Bundle;
import com.amap.api.fence.DistrictItem;
import com.amap.api.fence.GeoFence;
import com.amap.api.fence.PoiItem;
import com.amap.api.location.DPoint;
import com.taobao.muniontaobaosdk.p4p.anticheat.model.ClientTraceData;
import java.util.ArrayList;
import java.util.List;
import mtopsdk.common.util.SymbolExpUtil;
import org.json.JSONArray;
import org.json.JSONObject;

/* compiled from: GeoFenceSearchResultParser */
public final class c {
    private static long a = 0;

    public static int a(String str, List<GeoFence> list, Bundle bundle) {
        JSONArray optJSONArray;
        try {
            JSONObject jSONObject = new JSONObject(str);
            int optInt = jSONObject.optInt("status", 0);
            int optInt2 = jSONObject.optInt("infocode", 0);
            if (optInt != 1 || (optJSONArray = jSONObject.optJSONArray("pois")) == null) {
                return optInt2;
            }
            for (int i = 0; i < optJSONArray.length(); i++) {
                GeoFence geoFence = new GeoFence();
                PoiItem poiItem = new PoiItem();
                JSONObject jSONObject2 = optJSONArray.getJSONObject(i);
                poiItem.setPoiId(jSONObject2.optString("id"));
                poiItem.setPoiName(jSONObject2.optString("name"));
                poiItem.setPoiType(jSONObject2.optString("type"));
                poiItem.setTypeCode(jSONObject2.optString("typecode"));
                poiItem.setAddress(jSONObject2.optString("address"));
                String optString = jSONObject2.optString("location");
                if (optString != null) {
                    String[] split = optString.split(",");
                    poiItem.setLongitude(Double.parseDouble(split[0]));
                    poiItem.setLatitude(Double.parseDouble(split[1]));
                    ArrayList arrayList = new ArrayList();
                    ArrayList arrayList2 = new ArrayList();
                    DPoint dPoint = new DPoint(poiItem.getLatitude(), poiItem.getLongitude());
                    arrayList2.add(dPoint);
                    arrayList.add(arrayList2);
                    geoFence.setPointList(arrayList);
                    geoFence.setCenter(dPoint);
                }
                poiItem.setTel(jSONObject2.optString("tel"));
                poiItem.setProvince(jSONObject2.optString("pname"));
                poiItem.setCity(jSONObject2.optString("cityname"));
                poiItem.setAdname(jSONObject2.optString("adname"));
                geoFence.setPoiItem(poiItem);
                geoFence.setFenceId(new StringBuilder().append(a()).toString());
                geoFence.setCustomId(bundle.getString(GeoFence.BUNDLE_KEY_CUSTOMID));
                geoFence.setPendingIntentAction(bundle.getString("pendingIntentAction"));
                geoFence.setType(2);
                geoFence.setRadius(bundle.getFloat("fenceRadius"));
                geoFence.setExpiration(bundle.getLong("expiration"));
                geoFence.setActivatesAction(bundle.getInt("activatesAction", 1));
                list.add(geoFence);
            }
            return optInt2;
        } catch (Throwable th) {
            return 5;
        }
    }

    public static synchronized long a() {
        long j;
        synchronized (c.class) {
            long b = et.b();
            if (b > a) {
                a = b;
            } else {
                a++;
            }
            j = a;
        }
        return j;
    }

    private List<DPoint> a(List<DPoint> list, float f) {
        double d;
        double d2;
        if (list == null) {
            return null;
        }
        if (list.size() <= 2) {
            return list;
        }
        ArrayList arrayList = new ArrayList();
        DPoint dPoint = list.get(0);
        DPoint dPoint2 = list.get(list.size() - 1);
        int i = 1;
        double d3 = 0.0d;
        int i2 = 0;
        while (true) {
            int i3 = i;
            if (i3 >= list.size() - 1) {
                break;
            }
            DPoint dPoint3 = list.get(i3);
            double longitude = dPoint3.getLongitude() - dPoint.getLongitude();
            double latitude = dPoint3.getLatitude() - dPoint.getLatitude();
            double longitude2 = dPoint2.getLongitude() - dPoint.getLongitude();
            double latitude2 = dPoint2.getLatitude() - dPoint.getLatitude();
            double d4 = ((longitude * longitude2) + (latitude * latitude2)) / ((longitude2 * longitude2) + (latitude2 * latitude2));
            boolean z = dPoint.getLongitude() == dPoint2.getLongitude() && dPoint.getLatitude() == dPoint2.getLatitude();
            if (d4 < ClientTraceData.b.f47a || z) {
                d = dPoint.getLongitude();
                d2 = dPoint.getLatitude();
            } else if (d4 > 1.0d) {
                d = dPoint2.getLongitude();
                d2 = dPoint2.getLatitude();
            } else {
                d = dPoint.getLongitude() + (longitude2 * d4);
                d2 = (d4 * latitude2) + dPoint.getLatitude();
            }
            double a2 = (double) et.a(new DPoint(dPoint3.getLatitude(), dPoint3.getLongitude()), new DPoint(d2, d));
            if (a2 > d3) {
                i2 = i3;
                d3 = a2;
            }
            i = i3 + 1;
        }
        if (d3 < ((double) f)) {
            arrayList.add(dPoint);
            arrayList.add(dPoint2);
            return arrayList;
        }
        List<DPoint> a3 = a(list.subList(0, i2 + 1), f);
        List<DPoint> a4 = a(list.subList(i2, list.size()), f);
        arrayList.addAll(a3);
        arrayList.remove(arrayList.size() - 1);
        arrayList.addAll(a4);
        return arrayList;
    }

    public final int b(String str, List<GeoFence> list, Bundle bundle) {
        JSONArray optJSONArray;
        try {
            JSONObject jSONObject = new JSONObject(str);
            int optInt = jSONObject.optInt("status", 0);
            int optInt2 = jSONObject.optInt("infocode", 0);
            String string = bundle.getString(GeoFence.BUNDLE_KEY_CUSTOMID);
            String string2 = bundle.getString("pendingIntentAction");
            float f = bundle.getFloat("fenceRadius");
            long j = bundle.getLong("expiration");
            int i = bundle.getInt("activatesAction", 1);
            if (optInt != 1 || (optJSONArray = jSONObject.optJSONArray("districts")) == null) {
                return optInt2;
            }
            for (int i2 = 0; i2 < optJSONArray.length(); i2++) {
                ArrayList arrayList = new ArrayList();
                ArrayList arrayList2 = new ArrayList();
                GeoFence geoFence = new GeoFence();
                JSONObject jSONObject2 = optJSONArray.getJSONObject(i2);
                String optString = jSONObject2.optString("citycode");
                String optString2 = jSONObject2.optString("adcode");
                String optString3 = jSONObject2.optString("name");
                String string3 = jSONObject2.getString("center");
                DPoint dPoint = new DPoint();
                if (string3 != null) {
                    String[] split = string3.split(",");
                    dPoint.setLatitude(Double.parseDouble(split[1]));
                    dPoint.setLongitude(Double.parseDouble(split[0]));
                    geoFence.setCenter(dPoint);
                }
                geoFence.setCustomId(string);
                geoFence.setPendingIntentAction(string2);
                geoFence.setType(3);
                geoFence.setRadius(f);
                geoFence.setExpiration(j);
                geoFence.setActivatesAction(i);
                geoFence.setFenceId(new StringBuilder().append(a()).toString());
                String optString4 = jSONObject2.optString("polyline");
                if (optString4 != null) {
                    String[] split2 = optString4.split(SymbolExpUtil.SYMBOL_VERTICALBAR);
                    int length = split2.length;
                    float f2 = Float.MAX_VALUE;
                    float f3 = Float.MIN_VALUE;
                    int i3 = 0;
                    while (i3 < length) {
                        String str2 = split2[i3];
                        DistrictItem districtItem = new DistrictItem();
                        List arrayList3 = new ArrayList();
                        districtItem.setCitycode(optString);
                        districtItem.setAdcode(optString2);
                        districtItem.setDistrictName(optString3);
                        String[] split3 = str2.split(SymbolExpUtil.SYMBOL_SEMICOLON);
                        for (String split4 : split3) {
                            String[] split5 = split4.split(",");
                            if (split5.length > 1) {
                                arrayList3.add(new DPoint(Double.parseDouble(split5[1]), Double.parseDouble(split5[0])));
                            }
                        }
                        if (((float) arrayList3.size()) > 100.0f) {
                            arrayList3 = a(arrayList3, 100.0f);
                        }
                        arrayList2.add(arrayList3);
                        districtItem.setPolyline(arrayList3);
                        arrayList.add(districtItem);
                        f3 = Math.max(f3, a.b(dPoint, (List<DPoint>) arrayList3));
                        i3++;
                        f2 = Math.min(f2, a.a(dPoint, (List<DPoint>) arrayList3));
                    }
                    geoFence.setMaxDis2Center(f3);
                    geoFence.setMinDis2Center(f2);
                    geoFence.setDistrictItemList(arrayList);
                    geoFence.setPointList(arrayList2);
                    list.add(geoFence);
                }
            }
            return optInt2;
        } catch (Throwable th) {
            return 5;
        }
    }
}
