package com.loc;

import android.content.Context;
import android.os.Bundle;
import android.text.TextUtils;
import com.amap.api.location.AMapLocationClientOption;
import org.json.JSONArray;
import org.json.JSONObject;

/* compiled from: Parser */
public final class ek {
    private StringBuilder a = new StringBuilder();
    private AMapLocationClientOption b = new AMapLocationClientOption();

    private void a(dw dwVar, String str, String str2, String str3, String str4, String str5, String str6, String str7) {
        StringBuilder sb = new StringBuilder();
        if (!TextUtils.isEmpty(str)) {
            sb.append(str).append(" ");
        }
        if (!TextUtils.isEmpty(str2)) {
            if (this.b.getGeoLanguage() == AMapLocationClientOption.GeoLanguage.EN) {
                if (!str2.equals(str)) {
                    sb.append(str2).append(" ");
                }
            } else if (!str.contains("市") || !str.equals(str2)) {
                sb.append(str2).append(" ");
            }
        }
        if (!TextUtils.isEmpty(str3)) {
            sb.append(str3).append(" ");
        }
        if (!TextUtils.isEmpty(str4)) {
            sb.append(str4).append(" ");
        }
        if (!TextUtils.isEmpty(str5)) {
            sb.append(str5).append(" ");
        }
        if (!TextUtils.isEmpty(str6)) {
            if (TextUtils.isEmpty(str7) || this.b.getGeoLanguage() == AMapLocationClientOption.GeoLanguage.EN) {
                sb.append("Near " + str6);
                dwVar.setDescription("Near " + str6);
            } else {
                sb.append("靠近");
                sb.append(str6).append(" ");
                dwVar.setDescription("在" + str6 + "附近");
            }
        }
        Bundle bundle = new Bundle();
        bundle.putString("citycode", dwVar.getCityCode());
        bundle.putString("desc", sb.toString());
        bundle.putString("adcode", dwVar.getAdCode());
        dwVar.setExtras(bundle);
        dwVar.g(sb.toString());
        String adCode = dwVar.getAdCode();
        if (adCode == null || adCode.trim().length() <= 0 || this.b.getGeoLanguage() == AMapLocationClientOption.GeoLanguage.EN) {
            dwVar.setAddress(sb.toString());
        } else {
            dwVar.setAddress(sb.toString().replace(" ", ""));
        }
    }

    private static String b(String str) {
        return "[]".equals(str) ? "" : str;
    }

    /* JADX WARNING: Code restructure failed: missing block: B:124:0x0272, code lost:
        r0 = "";
     */
    /* JADX WARNING: Code restructure failed: missing block: B:125:0x0273, code lost:
        r2 = r0;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:127:0x0277, code lost:
        r0 = "";
     */
    /* JADX WARNING: Code restructure failed: missing block: B:128:0x0278, code lost:
        r3 = r0;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:130:0x027c, code lost:
        r0 = "";
     */
    /* JADX WARNING: Code restructure failed: missing block: B:131:0x027d, code lost:
        r4 = r0;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:133:0x0281, code lost:
        r0 = "";
     */
    /* JADX WARNING: Code restructure failed: missing block: B:134:0x0282, code lost:
        r5 = r0;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:136:0x0286, code lost:
        r0 = "";
     */
    /* JADX WARNING: Code restructure failed: missing block: B:137:0x0287, code lost:
        r6 = r0;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:139:0x028b, code lost:
        r0 = "";
     */
    /* JADX WARNING: Code restructure failed: missing block: B:140:0x028c, code lost:
        r7 = r0;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:142:0x0290, code lost:
        r0 = "";
     */
    /* JADX WARNING: Code restructure failed: missing block: B:143:0x0291, code lost:
        r8 = r0;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:151:0x02a2, code lost:
        r0 = th;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:152:0x02a3, code lost:
        r1 = r9;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:160:0x02f0, code lost:
        r9.clear();
     */
    /* JADX WARNING: Code restructure failed: missing block: B:162:0x02f4, code lost:
        r0 = th;
     */
    /* JADX WARNING: Failed to process nested try/catch */
    /* JADX WARNING: Removed duplicated region for block: B:100:0x0204 A[Catch:{ Throwable -> 0x02a2, all -> 0x02f4 }] */
    /* JADX WARNING: Removed duplicated region for block: B:108:0x0222 A[Catch:{ Throwable -> 0x02a2, all -> 0x02f4 }] */
    /* JADX WARNING: Removed duplicated region for block: B:111:0x023b A[Catch:{ Throwable -> 0x02a2, all -> 0x02f4 }] */
    /* JADX WARNING: Removed duplicated region for block: B:119:0x0258  */
    /* JADX WARNING: Removed duplicated region for block: B:122:0x0263  */
    /* JADX WARNING: Removed duplicated region for block: B:160:0x02f0  */
    /* JADX WARNING: Removed duplicated region for block: B:162:0x02f4 A[ExcHandler: all (th java.lang.Throwable), Splitter:B:7:0x002f] */
    /* JADX WARNING: Removed duplicated region for block: B:81:0x01a4 A[Catch:{ Throwable -> 0x02a2, all -> 0x02f4 }] */
    /* JADX WARNING: Removed duplicated region for block: B:84:0x01b4 A[Catch:{ Throwable -> 0x02a2, all -> 0x02f4 }] */
    /* JADX WARNING: Removed duplicated region for block: B:94:0x01e7 A[Catch:{ Throwable -> 0x02a2, all -> 0x02f4 }] */
    /* JADX WARNING: Removed duplicated region for block: B:97:0x01f7 A[Catch:{ Throwable -> 0x02a2, all -> 0x02f4 }] */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public final com.loc.dw a(com.loc.dw r12, byte[] r13, com.loc.dq r14) {
        /*
            r11 = this;
            r1 = 0
            if (r13 != 0) goto L_0x002b
            r0 = 5
            r12.setErrorCode(r0)     // Catch:{ Throwable -> 0x02f9, all -> 0x02ec }
            java.lang.String r0 = "#0504"
            r14.f(r0)     // Catch:{ Throwable -> 0x02f9, all -> 0x02ec }
            java.lang.StringBuilder r0 = r11.a     // Catch:{ Throwable -> 0x02f9, all -> 0x02ec }
            java.lang.String r2 = "binaryResult is null#0504"
            r0.append(r2)     // Catch:{ Throwable -> 0x02f9, all -> 0x02ec }
            java.lang.StringBuilder r0 = r11.a     // Catch:{ Throwable -> 0x02f9, all -> 0x02ec }
            java.lang.String r0 = r0.toString()     // Catch:{ Throwable -> 0x02f9, all -> 0x02ec }
            r12.setLocationDetail(r0)     // Catch:{ Throwable -> 0x02f9, all -> 0x02ec }
            java.lang.StringBuilder r0 = r11.a     // Catch:{ Throwable -> 0x02f9, all -> 0x02ec }
            r2 = 0
            java.lang.StringBuilder r3 = r11.a     // Catch:{ Throwable -> 0x02f9, all -> 0x02ec }
            int r3 = r3.length()     // Catch:{ Throwable -> 0x02f9, all -> 0x02ec }
            r0.delete(r2, r3)     // Catch:{ Throwable -> 0x02f9, all -> 0x02ec }
        L_0x002a:
            return r12
        L_0x002b:
            java.nio.ByteBuffer r9 = java.nio.ByteBuffer.wrap(r13)     // Catch:{ Throwable -> 0x02f9, all -> 0x02ec }
            byte r0 = r9.get()     // Catch:{ Throwable -> 0x02a2, all -> 0x02f4 }
            if (r0 != 0) goto L_0x0049
            short r0 = r9.getShort()     // Catch:{ Throwable -> 0x02a2, all -> 0x02f4 }
            java.lang.String r0 = java.lang.String.valueOf(r0)     // Catch:{ Throwable -> 0x02a2, all -> 0x02f4 }
            r12.b((java.lang.String) r0)     // Catch:{ Throwable -> 0x02a2, all -> 0x02f4 }
            r9.clear()     // Catch:{ Throwable -> 0x02a2, all -> 0x02f4 }
            if (r9 == 0) goto L_0x002a
            r9.clear()
            goto L_0x002a
        L_0x0049:
            int r0 = r9.getInt()     // Catch:{ Throwable -> 0x02a2, all -> 0x02f4 }
            double r0 = (double) r0     // Catch:{ Throwable -> 0x02a2, all -> 0x02f4 }
            r2 = 4696837146684686336(0x412e848000000000, double:1000000.0)
            double r0 = r0 / r2
            double r0 = com.loc.et.a((double) r0)     // Catch:{ Throwable -> 0x02a2, all -> 0x02f4 }
            r12.setLongitude(r0)     // Catch:{ Throwable -> 0x02a2, all -> 0x02f4 }
            int r0 = r9.getInt()     // Catch:{ Throwable -> 0x02a2, all -> 0x02f4 }
            double r0 = (double) r0     // Catch:{ Throwable -> 0x02a2, all -> 0x02f4 }
            r2 = 4696837146684686336(0x412e848000000000, double:1000000.0)
            double r0 = r0 / r2
            double r0 = com.loc.et.a((double) r0)     // Catch:{ Throwable -> 0x02a2, all -> 0x02f4 }
            r12.setLatitude(r0)     // Catch:{ Throwable -> 0x02a2, all -> 0x02f4 }
            short r0 = r9.getShort()     // Catch:{ Throwable -> 0x02a2, all -> 0x02f4 }
            float r0 = (float) r0     // Catch:{ Throwable -> 0x02a2, all -> 0x02f4 }
            r12.setAccuracy(r0)     // Catch:{ Throwable -> 0x02a2, all -> 0x02f4 }
            byte r0 = r9.get()     // Catch:{ Throwable -> 0x02a2, all -> 0x02f4 }
            java.lang.String r0 = java.lang.String.valueOf(r0)     // Catch:{ Throwable -> 0x02a2, all -> 0x02f4 }
            r12.c(r0)     // Catch:{ Throwable -> 0x02a2, all -> 0x02f4 }
            byte r0 = r9.get()     // Catch:{ Throwable -> 0x02a2, all -> 0x02f4 }
            java.lang.String r0 = java.lang.String.valueOf(r0)     // Catch:{ Throwable -> 0x02a2, all -> 0x02f4 }
            r12.d(r0)     // Catch:{ Throwable -> 0x02a2, all -> 0x02f4 }
            byte r0 = r9.get()     // Catch:{ Throwable -> 0x02a2, all -> 0x02f4 }
            r1 = 1
            if (r0 != r1) goto L_0x0192
            java.lang.String r1 = ""
            java.lang.String r3 = ""
            java.lang.String r4 = ""
            java.lang.String r5 = ""
            java.lang.String r6 = ""
            java.lang.String r7 = ""
            java.lang.String r8 = ""
            byte r0 = r9.get()     // Catch:{ Throwable -> 0x02a2, all -> 0x02f4 }
            r0 = r0 & 255(0xff, float:3.57E-43)
            byte[] r0 = new byte[r0]     // Catch:{ Throwable -> 0x02a2, all -> 0x02f4 }
            r9.get(r0)     // Catch:{ Throwable -> 0x02a2, all -> 0x02f4 }
            java.lang.String r2 = new java.lang.String     // Catch:{ Throwable -> 0x031e, all -> 0x02f4 }
            java.lang.String r10 = "UTF-8"
            r2.<init>(r0, r10)     // Catch:{ Throwable -> 0x031e, all -> 0x02f4 }
            r12.setCountry(r2)     // Catch:{ Throwable -> 0x031e, all -> 0x02f4 }
        L_0x00bd:
            byte r0 = r9.get()     // Catch:{ Throwable -> 0x02a2, all -> 0x02f4 }
            r0 = r0 & 255(0xff, float:3.57E-43)
            byte[] r2 = new byte[r0]     // Catch:{ Throwable -> 0x02a2, all -> 0x02f4 }
            r9.get(r2)     // Catch:{ Throwable -> 0x02a2, all -> 0x02f4 }
            java.lang.String r0 = new java.lang.String     // Catch:{ Throwable -> 0x0271, all -> 0x02f4 }
            java.lang.String r10 = "UTF-8"
            r0.<init>(r2, r10)     // Catch:{ Throwable -> 0x0271, all -> 0x02f4 }
            r12.setProvince(r0)     // Catch:{ Throwable -> 0x031b, all -> 0x02f4 }
            r2 = r0
        L_0x00d4:
            byte r0 = r9.get()     // Catch:{ Throwable -> 0x02a2, all -> 0x02f4 }
            r0 = r0 & 255(0xff, float:3.57E-43)
            byte[] r1 = new byte[r0]     // Catch:{ Throwable -> 0x02a2, all -> 0x02f4 }
            r9.get(r1)     // Catch:{ Throwable -> 0x02a2, all -> 0x02f4 }
            java.lang.String r0 = new java.lang.String     // Catch:{ Throwable -> 0x0276, all -> 0x02f4 }
            java.lang.String r10 = "UTF-8"
            r0.<init>(r1, r10)     // Catch:{ Throwable -> 0x0276, all -> 0x02f4 }
            r12.setCity(r0)     // Catch:{ Throwable -> 0x0318, all -> 0x02f4 }
            r3 = r0
        L_0x00eb:
            byte r0 = r9.get()     // Catch:{ Throwable -> 0x02a2, all -> 0x02f4 }
            r0 = r0 & 255(0xff, float:3.57E-43)
            byte[] r1 = new byte[r0]     // Catch:{ Throwable -> 0x02a2, all -> 0x02f4 }
            r9.get(r1)     // Catch:{ Throwable -> 0x02a2, all -> 0x02f4 }
            java.lang.String r0 = new java.lang.String     // Catch:{ Throwable -> 0x027b, all -> 0x02f4 }
            java.lang.String r10 = "UTF-8"
            r0.<init>(r1, r10)     // Catch:{ Throwable -> 0x027b, all -> 0x02f4 }
            r12.setDistrict(r0)     // Catch:{ Throwable -> 0x0315, all -> 0x02f4 }
            r4 = r0
        L_0x0102:
            byte r0 = r9.get()     // Catch:{ Throwable -> 0x02a2, all -> 0x02f4 }
            r0 = r0 & 255(0xff, float:3.57E-43)
            byte[] r1 = new byte[r0]     // Catch:{ Throwable -> 0x02a2, all -> 0x02f4 }
            r9.get(r1)     // Catch:{ Throwable -> 0x02a2, all -> 0x02f4 }
            java.lang.String r0 = new java.lang.String     // Catch:{ Throwable -> 0x0280, all -> 0x02f4 }
            java.lang.String r10 = "UTF-8"
            r0.<init>(r1, r10)     // Catch:{ Throwable -> 0x0280, all -> 0x02f4 }
            r12.setStreet(r0)     // Catch:{ Throwable -> 0x0312, all -> 0x02f4 }
            r12.setRoad(r0)     // Catch:{ Throwable -> 0x0312, all -> 0x02f4 }
            r5 = r0
        L_0x011c:
            byte r0 = r9.get()     // Catch:{ Throwable -> 0x02a2, all -> 0x02f4 }
            r0 = r0 & 255(0xff, float:3.57E-43)
            byte[] r1 = new byte[r0]     // Catch:{ Throwable -> 0x02a2, all -> 0x02f4 }
            r9.get(r1)     // Catch:{ Throwable -> 0x02a2, all -> 0x02f4 }
            java.lang.String r0 = new java.lang.String     // Catch:{ Throwable -> 0x0285, all -> 0x02f4 }
            java.lang.String r10 = "UTF-8"
            r0.<init>(r1, r10)     // Catch:{ Throwable -> 0x0285, all -> 0x02f4 }
            r12.setNumber(r0)     // Catch:{ Throwable -> 0x030f, all -> 0x02f4 }
            r6 = r0
        L_0x0133:
            byte r0 = r9.get()     // Catch:{ Throwable -> 0x02a2, all -> 0x02f4 }
            r0 = r0 & 255(0xff, float:3.57E-43)
            byte[] r1 = new byte[r0]     // Catch:{ Throwable -> 0x02a2, all -> 0x02f4 }
            r9.get(r1)     // Catch:{ Throwable -> 0x02a2, all -> 0x02f4 }
            java.lang.String r0 = new java.lang.String     // Catch:{ Throwable -> 0x028a, all -> 0x02f4 }
            java.lang.String r10 = "UTF-8"
            r0.<init>(r1, r10)     // Catch:{ Throwable -> 0x028a, all -> 0x02f4 }
            r12.setPoiName(r0)     // Catch:{ Throwable -> 0x030c, all -> 0x02f4 }
            r7 = r0
        L_0x014a:
            byte r0 = r9.get()     // Catch:{ Throwable -> 0x02a2, all -> 0x02f4 }
            r0 = r0 & 255(0xff, float:3.57E-43)
            byte[] r0 = new byte[r0]     // Catch:{ Throwable -> 0x02a2, all -> 0x02f4 }
            r9.get(r0)     // Catch:{ Throwable -> 0x02a2, all -> 0x02f4 }
            java.lang.String r1 = new java.lang.String     // Catch:{ Throwable -> 0x0309, all -> 0x02f4 }
            java.lang.String r10 = "UTF-8"
            r1.<init>(r0, r10)     // Catch:{ Throwable -> 0x0309, all -> 0x02f4 }
            r12.setAoiName(r1)     // Catch:{ Throwable -> 0x0309, all -> 0x02f4 }
        L_0x0160:
            byte r0 = r9.get()     // Catch:{ Throwable -> 0x02a2, all -> 0x02f4 }
            r0 = r0 & 255(0xff, float:3.57E-43)
            byte[] r1 = new byte[r0]     // Catch:{ Throwable -> 0x02a2, all -> 0x02f4 }
            r9.get(r1)     // Catch:{ Throwable -> 0x02a2, all -> 0x02f4 }
            java.lang.String r0 = new java.lang.String     // Catch:{ Throwable -> 0x028f, all -> 0x02f4 }
            java.lang.String r10 = "UTF-8"
            r0.<init>(r1, r10)     // Catch:{ Throwable -> 0x028f, all -> 0x02f4 }
            r12.setAdCode(r0)     // Catch:{ Throwable -> 0x0307, all -> 0x02f4 }
            r8 = r0
        L_0x0177:
            byte r0 = r9.get()     // Catch:{ Throwable -> 0x02a2, all -> 0x02f4 }
            r0 = r0 & 255(0xff, float:3.57E-43)
            byte[] r0 = new byte[r0]     // Catch:{ Throwable -> 0x02a2, all -> 0x02f4 }
            r9.get(r0)     // Catch:{ Throwable -> 0x02a2, all -> 0x02f4 }
            java.lang.String r1 = new java.lang.String     // Catch:{ Throwable -> 0x0304, all -> 0x02f4 }
            java.lang.String r10 = "UTF-8"
            r1.<init>(r0, r10)     // Catch:{ Throwable -> 0x0304, all -> 0x02f4 }
            r12.setCityCode(r1)     // Catch:{ Throwable -> 0x0304, all -> 0x02f4 }
        L_0x018d:
            r0 = r11
            r1 = r12
            r0.a(r1, r2, r3, r4, r5, r6, r7, r8)     // Catch:{ Throwable -> 0x02a2, all -> 0x02f4 }
        L_0x0192:
            byte r0 = r9.get()     // Catch:{ Throwable -> 0x02a2, all -> 0x02f4 }
            r0 = r0 & 255(0xff, float:3.57E-43)
            byte[] r0 = new byte[r0]     // Catch:{ Throwable -> 0x02a2, all -> 0x02f4 }
            r9.get(r0)     // Catch:{ Throwable -> 0x02a2, all -> 0x02f4 }
            byte r0 = r9.get()     // Catch:{ Throwable -> 0x02a2, all -> 0x02f4 }
            r1 = 1
            if (r0 != r1) goto L_0x01ad
            r9.getInt()     // Catch:{ Throwable -> 0x02a2, all -> 0x02f4 }
            r9.getInt()     // Catch:{ Throwable -> 0x02a2, all -> 0x02f4 }
            r9.getShort()     // Catch:{ Throwable -> 0x02a2, all -> 0x02f4 }
        L_0x01ad:
            byte r0 = r9.get()     // Catch:{ Throwable -> 0x02a2, all -> 0x02f4 }
            r1 = 1
            if (r0 != r1) goto L_0x01e0
            byte r0 = r9.get()     // Catch:{ Throwable -> 0x02a2, all -> 0x02f4 }
            r0 = r0 & 255(0xff, float:3.57E-43)
            byte[] r0 = new byte[r0]     // Catch:{ Throwable -> 0x02a2, all -> 0x02f4 }
            r9.get(r0)     // Catch:{ Throwable -> 0x02a2, all -> 0x02f4 }
            java.lang.String r1 = new java.lang.String     // Catch:{ Throwable -> 0x0301, all -> 0x02f4 }
            java.lang.String r2 = "UTF-8"
            r1.<init>(r0, r2)     // Catch:{ Throwable -> 0x0301, all -> 0x02f4 }
            r12.setBuildingId(r1)     // Catch:{ Throwable -> 0x0301, all -> 0x02f4 }
        L_0x01ca:
            byte r0 = r9.get()     // Catch:{ Throwable -> 0x02a2, all -> 0x02f4 }
            r0 = r0 & 255(0xff, float:3.57E-43)
            byte[] r0 = new byte[r0]     // Catch:{ Throwable -> 0x02a2, all -> 0x02f4 }
            r9.get(r0)     // Catch:{ Throwable -> 0x02a2, all -> 0x02f4 }
            java.lang.String r1 = new java.lang.String     // Catch:{ Throwable -> 0x02fe, all -> 0x02f4 }
            java.lang.String r2 = "UTF-8"
            r1.<init>(r0, r2)     // Catch:{ Throwable -> 0x02fe, all -> 0x02f4 }
            r12.setFloor(r1)     // Catch:{ Throwable -> 0x02fe, all -> 0x02f4 }
        L_0x01e0:
            byte r0 = r9.get()     // Catch:{ Throwable -> 0x02a2, all -> 0x02f4 }
            r1 = 1
            if (r0 != r1) goto L_0x01f0
            r9.get()     // Catch:{ Throwable -> 0x02a2, all -> 0x02f4 }
            r9.getInt()     // Catch:{ Throwable -> 0x02a2, all -> 0x02f4 }
            r9.get()     // Catch:{ Throwable -> 0x02a2, all -> 0x02f4 }
        L_0x01f0:
            byte r0 = r9.get()     // Catch:{ Throwable -> 0x02a2, all -> 0x02f4 }
            r1 = 1
            if (r0 != r1) goto L_0x01fe
            long r0 = r9.getLong()     // Catch:{ Throwable -> 0x02a2, all -> 0x02f4 }
            r12.setTime(r0)     // Catch:{ Throwable -> 0x02a2, all -> 0x02f4 }
        L_0x01fe:
            short r0 = r9.getShort()     // Catch:{ Throwable -> 0x02a2, all -> 0x02f4 }
            if (r0 <= 0) goto L_0x021c
            byte[] r0 = new byte[r0]     // Catch:{ Throwable -> 0x02a2, all -> 0x02f4 }
            r9.get(r0)     // Catch:{ Throwable -> 0x02a2, all -> 0x02f4 }
            int r1 = r0.length     // Catch:{ Throwable -> 0x02fb, all -> 0x02f4 }
            if (r1 <= 0) goto L_0x021c
            r1 = 0
            byte[] r0 = android.util.Base64.decode(r0, r1)     // Catch:{ Throwable -> 0x02fb, all -> 0x02f4 }
            java.lang.String r1 = new java.lang.String     // Catch:{ Throwable -> 0x02fb, all -> 0x02f4 }
            java.lang.String r2 = "UTF-8"
            r1.<init>(r0, r2)     // Catch:{ Throwable -> 0x02fb, all -> 0x02f4 }
            r12.a((java.lang.String) r1)     // Catch:{ Throwable -> 0x02fb, all -> 0x02f4 }
        L_0x021c:
            short r0 = r9.getShort()     // Catch:{ Throwable -> 0x02a2, all -> 0x02f4 }
            if (r0 <= 0) goto L_0x0227
            byte[] r0 = new byte[r0]     // Catch:{ Throwable -> 0x02a2, all -> 0x02f4 }
            r9.get(r0)     // Catch:{ Throwable -> 0x02a2, all -> 0x02f4 }
        L_0x0227:
            java.lang.String r0 = "5.1"
            java.lang.Double r0 = java.lang.Double.valueOf(r0)     // Catch:{ Throwable -> 0x02a2, all -> 0x02f4 }
            double r0 = r0.doubleValue()     // Catch:{ Throwable -> 0x02a2, all -> 0x02f4 }
            r2 = 4617428107952285286(0x4014666666666666, double:5.1)
            int r0 = (r0 > r2 ? 1 : (r0 == r2 ? 0 : -1))
            if (r0 < 0) goto L_0x0256
            short r0 = r9.getShort()     // Catch:{ Throwable -> 0x02a2, all -> 0x02f4 }
            java.lang.String r1 = "-1"
            java.lang.String r2 = r12.d()     // Catch:{ Throwable -> 0x02a2, all -> 0x02f4 }
            boolean r1 = r1.equals(r2)     // Catch:{ Throwable -> 0x02a2, all -> 0x02f4 }
            if (r1 != 0) goto L_0x0298
            r1 = -1
            if (r0 != r1) goto L_0x0294
            r0 = 0
        L_0x0250:
            r12.setConScenario(r0)     // Catch:{ Throwable -> 0x02a2, all -> 0x02f4 }
        L_0x0253:
            r9.get()     // Catch:{ Throwable -> 0x02a2, all -> 0x02f4 }
        L_0x0256:
            if (r9 == 0) goto L_0x025b
            r9.clear()
        L_0x025b:
            java.lang.StringBuilder r0 = r11.a
            int r0 = r0.length()
            if (r0 <= 0) goto L_0x002a
            java.lang.StringBuilder r0 = r11.a
            r1 = 0
            java.lang.StringBuilder r2 = r11.a
            int r2 = r2.length()
            r0.delete(r1, r2)
            goto L_0x002a
        L_0x0271:
            r0 = move-exception
            r0 = r1
        L_0x0273:
            r2 = r0
            goto L_0x00d4
        L_0x0276:
            r0 = move-exception
            r0 = r3
        L_0x0278:
            r3 = r0
            goto L_0x00eb
        L_0x027b:
            r0 = move-exception
            r0 = r4
        L_0x027d:
            r4 = r0
            goto L_0x0102
        L_0x0280:
            r0 = move-exception
            r0 = r5
        L_0x0282:
            r5 = r0
            goto L_0x011c
        L_0x0285:
            r0 = move-exception
            r0 = r6
        L_0x0287:
            r6 = r0
            goto L_0x0133
        L_0x028a:
            r0 = move-exception
            r0 = r7
        L_0x028c:
            r7 = r0
            goto L_0x014a
        L_0x028f:
            r0 = move-exception
            r0 = r8
        L_0x0291:
            r8 = r0
            goto L_0x0177
        L_0x0294:
            if (r0 != 0) goto L_0x0250
            r0 = -1
            goto L_0x0250
        L_0x0298:
            r1 = 101(0x65, float:1.42E-43)
            if (r0 != r1) goto L_0x029e
            r0 = 100
        L_0x029e:
            r12.setConScenario(r0)     // Catch:{ Throwable -> 0x02a2, all -> 0x02f4 }
            goto L_0x0253
        L_0x02a2:
            r0 = move-exception
            r1 = r9
        L_0x02a4:
            com.loc.dw r12 = new com.loc.dw     // Catch:{ all -> 0x02f6 }
            java.lang.String r2 = ""
            r12.<init>(r2)     // Catch:{ all -> 0x02f6 }
            r2 = 5
            r12.setErrorCode(r2)     // Catch:{ all -> 0x02f6 }
            java.lang.String r2 = "#0505"
            r14.f(r2)     // Catch:{ all -> 0x02f6 }
            java.lang.StringBuilder r2 = r11.a     // Catch:{ all -> 0x02f6 }
            java.lang.StringBuilder r3 = new java.lang.StringBuilder     // Catch:{ all -> 0x02f6 }
            java.lang.String r4 = "parser data error:"
            r3.<init>(r4)     // Catch:{ all -> 0x02f6 }
            java.lang.String r0 = r0.getMessage()     // Catch:{ all -> 0x02f6 }
            java.lang.StringBuilder r0 = r3.append(r0)     // Catch:{ all -> 0x02f6 }
            java.lang.String r3 = "#0505"
            java.lang.StringBuilder r0 = r0.append(r3)     // Catch:{ all -> 0x02f6 }
            java.lang.String r0 = r0.toString()     // Catch:{ all -> 0x02f6 }
            r2.append(r0)     // Catch:{ all -> 0x02f6 }
            r0 = 0
            r2 = 2054(0x806, float:2.878E-42)
            com.loc.er.a((java.lang.String) r0, (int) r2)     // Catch:{ all -> 0x02f6 }
            java.lang.StringBuilder r0 = r11.a     // Catch:{ all -> 0x02f6 }
            java.lang.String r0 = r0.toString()     // Catch:{ all -> 0x02f6 }
            r12.setLocationDetail(r0)     // Catch:{ all -> 0x02f6 }
            if (r1 == 0) goto L_0x025b
            r1.clear()
            goto L_0x025b
        L_0x02ec:
            r0 = move-exception
            r9 = r1
        L_0x02ee:
            if (r9 == 0) goto L_0x02f3
            r9.clear()
        L_0x02f3:
            throw r0
        L_0x02f4:
            r0 = move-exception
            goto L_0x02ee
        L_0x02f6:
            r0 = move-exception
            r9 = r1
            goto L_0x02ee
        L_0x02f9:
            r0 = move-exception
            goto L_0x02a4
        L_0x02fb:
            r0 = move-exception
            goto L_0x021c
        L_0x02fe:
            r0 = move-exception
            goto L_0x01e0
        L_0x0301:
            r0 = move-exception
            goto L_0x01ca
        L_0x0304:
            r0 = move-exception
            goto L_0x018d
        L_0x0307:
            r1 = move-exception
            goto L_0x0291
        L_0x0309:
            r0 = move-exception
            goto L_0x0160
        L_0x030c:
            r1 = move-exception
            goto L_0x028c
        L_0x030f:
            r1 = move-exception
            goto L_0x0287
        L_0x0312:
            r1 = move-exception
            goto L_0x0282
        L_0x0315:
            r1 = move-exception
            goto L_0x027d
        L_0x0318:
            r1 = move-exception
            goto L_0x0278
        L_0x031b:
            r1 = move-exception
            goto L_0x0273
        L_0x031e:
            r0 = move-exception
            goto L_0x00bd
        */
        throw new UnsupportedOperationException("Method not decompiled: com.loc.ek.a(com.loc.dw, byte[], com.loc.dq):com.loc.dw");
    }

    public final dw a(String str) {
        String str2;
        try {
            dw dwVar = new dw("");
            JSONObject optJSONObject = new JSONObject(str).optJSONObject("regeocode");
            JSONObject optJSONObject2 = optJSONObject.optJSONObject("addressComponent");
            dwVar.setCountry(b(optJSONObject2.optString("country")));
            String b2 = b(optJSONObject2.optString("province"));
            dwVar.setProvince(b2);
            String b3 = b(optJSONObject2.optString("citycode"));
            dwVar.setCityCode(b3);
            String optString = optJSONObject2.optString("city");
            if (!b3.endsWith("010") && !b3.endsWith("021") && !b3.endsWith("022") && !b3.endsWith("023")) {
                optString = b(optString);
                dwVar.setCity(optString);
            } else if (b2 != null && b2.length() > 0) {
                dwVar.setCity(b2);
                optString = b2;
            }
            if (TextUtils.isEmpty(optString)) {
                dwVar.setCity(b2);
                optString = b2;
            }
            String b4 = b(optJSONObject2.optString("district"));
            dwVar.setDistrict(b4);
            String b5 = b(optJSONObject2.optString("adcode"));
            dwVar.setAdCode(b5);
            JSONObject optJSONObject3 = optJSONObject2.optJSONObject("streetNumber");
            String b6 = b(optJSONObject3.optString("street"));
            dwVar.setStreet(b6);
            dwVar.setRoad(b6);
            String b7 = b(optJSONObject3.optString("number"));
            dwVar.setNumber(b7);
            JSONArray optJSONArray = optJSONObject.optJSONArray("pois");
            if (optJSONArray.length() > 0) {
                str2 = b(optJSONArray.getJSONObject(0).optString("name"));
                dwVar.setPoiName(str2);
            } else {
                str2 = null;
            }
            JSONArray optJSONArray2 = optJSONObject.optJSONArray("aois");
            if (optJSONArray2.length() > 0) {
                dwVar.setAoiName(b(optJSONArray2.getJSONObject(0).optString("name")));
            }
            a(dwVar, b2, optString, b4, b6, b7, str2, b5);
            return dwVar;
        } catch (Throwable th) {
            return null;
        }
    }

    public final dw a(String str, Context context, ba baVar, dq dqVar) {
        dw dwVar = new dw("");
        dwVar.setErrorCode(7);
        StringBuffer stringBuffer = new StringBuffer();
        try {
            stringBuffer.append("#SHA1AndPackage#").append(k.e(context));
            String str2 = (String) baVar.b.get("gsid").get(0);
            if (!TextUtils.isEmpty(str2)) {
                stringBuffer.append("#gsid#").append(str2);
            }
            String str3 = baVar.c;
            if (!TextUtils.isEmpty(str3)) {
                stringBuffer.append("#csid#" + str3);
            }
        } catch (Throwable th) {
        }
        try {
            JSONObject jSONObject = new JSONObject(str);
            if (!jSONObject.has("status") || !jSONObject.has("info")) {
                dqVar.f("#0702");
                this.a.append("json is error:").append(str).append(stringBuffer).append("#0702");
            }
            String string = jSONObject.getString("status");
            String string2 = jSONObject.getString("info");
            String string3 = jSONObject.getString("infocode");
            if ("0".equals(string)) {
                dqVar.f("#0701");
                this.a.append("auth fail:").append(string2).append(stringBuffer).append("#0701");
                er.a(baVar.d, string3, string2);
            }
        } catch (Throwable th2) {
            dqVar.f("#0703");
            this.a.append("json exception error:").append(th2.getMessage()).append(stringBuffer).append("#0703");
            en.a(th2, "parser", "paseAuthFailurJson");
        }
        dwVar.setLocationDetail(this.a.toString());
        if (this.a.length() > 0) {
            this.a.delete(0, this.a.length());
        }
        return dwVar;
    }

    public final void a(AMapLocationClientOption aMapLocationClientOption) {
        if (aMapLocationClientOption == null) {
            this.b = new AMapLocationClientOption();
        } else {
            this.b = aMapLocationClientOption;
        }
    }
}
