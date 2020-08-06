package com.loc;

import android.os.Parcel;
import android.os.Parcelable;

/* compiled from: AMapLocationStaticsEntity */
public final class dq implements Parcelable {
    public static final Parcelable.Creator<dq> CREATOR = new Parcelable.Creator<dq>() {
        public final /* synthetic */ Object createFromParcel(Parcel parcel) {
            dq dqVar = new dq();
            dqVar.c(parcel.readString());
            dqVar.d(parcel.readString());
            dqVar.e(parcel.readString());
            dqVar.f(parcel.readString());
            dqVar.b(parcel.readString());
            dqVar.c(parcel.readLong());
            dqVar.d(parcel.readLong());
            dqVar.a(parcel.readLong());
            dqVar.b(parcel.readLong());
            dqVar.a(parcel.readString());
            return dqVar;
        }

        public final /* bridge */ /* synthetic */ Object[] newArray(int i) {
            return new dq[i];
        }
    };
    private long a = 0;
    private long b = 0;
    private long c = 0;
    private long d = 0;
    private String e;
    private String f;
    private String g = "first";
    private String h = "";
    private String i = "";
    private String j = null;

    public final long a() {
        if (this.d - this.c <= 0) {
            return 0;
        }
        return this.d - this.c;
    }

    public final void a(long j2) {
        this.c = j2;
    }

    public final void a(String str) {
        this.i = str;
    }

    public final String b() {
        return this.i;
    }

    public final void b(long j2) {
        this.d = j2;
    }

    public final void b(String str) {
        this.j = str;
    }

    public final String c() {
        return this.j;
    }

    public final void c(long j2) {
        this.a = j2;
    }

    public final void c(String str) {
        this.e = str;
    }

    public final String d() {
        return this.e;
    }

    public final void d(long j2) {
        this.b = j2;
    }

    public final void d(String str) {
        this.f = str;
    }

    public final int describeContents() {
        return 0;
    }

    public final String e() {
        return this.f;
    }

    public final void e(String str) {
        this.g = str;
    }

    public final String f() {
        return this.g;
    }

    public final void f(String str) {
        this.h = str;
    }

    public final String g() {
        return this.h;
    }

    public final long h() {
        if (this.b <= this.a) {
            return 0;
        }
        return this.b - this.a;
    }

    public final void writeToParcel(Parcel parcel, int i2) {
        try {
            parcel.writeString(this.e);
            parcel.writeString(this.f);
            parcel.writeString(this.g);
            parcel.writeString(this.h);
            parcel.writeString(this.j);
            parcel.writeLong(this.a);
            parcel.writeLong(this.b);
            parcel.writeLong(this.c);
            parcel.writeLong(this.d);
            parcel.writeString(this.i);
        } catch (Throwable th) {
        }
    }
}
