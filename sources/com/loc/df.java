package com.loc;

import com.taobao.muniontaobaosdk.p4p.anticheat.model.ClientTraceData;

/* compiled from: AmapLocation */
public class df {
    public String a;
    public long b = 0;
    public long c = 0;
    public double d = ClientTraceData.b.f47a;
    public double e = ClientTraceData.b.f47a;
    public double f = ClientTraceData.b.f47a;
    public float g = 0.0f;
    public float h = 0.0f;
    public float i = 0.0f;
    public boolean j = false;

    public df(String str) {
        this.a = str;
    }

    public final double a(df dfVar) {
        if (dfVar == null) {
            return ClientTraceData.b.f47a;
        }
        double d2 = this.e;
        double d3 = this.d;
        double d4 = dfVar.e;
        double d5 = dfVar.d;
        double d6 = 6356725.0d + ((21412.0d * (90.0d - d3)) / 90.0d);
        double cos = ((d4 * 0.01745329238474369d) - (d2 * 0.01745329238474369d)) * Math.cos((3.1415927410125732d * d3) / 180.0d) * d6;
        double d7 = ((0.01745329238474369d * d5) - (d3 * 0.01745329238474369d)) * d6;
        return Math.pow((cos * cos) + (d7 * d7), 0.5d);
    }
}
