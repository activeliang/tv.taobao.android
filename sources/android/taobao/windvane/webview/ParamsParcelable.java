package android.taobao.windvane.webview;

import android.os.Parcel;
import android.os.Parcelable;

public class ParamsParcelable implements Parcelable {
    public static final Parcelable.Creator<ParamsParcelable> CREATOR = new Parcelable.Creator<ParamsParcelable>() {
        public ParamsParcelable createFromParcel(Parcel in) {
            return new ParamsParcelable(in);
        }

        public ParamsParcelable[] newArray(int size) {
            return new ParamsParcelable[size];
        }
    };
    private boolean jsbridgeEnabled = true;
    private boolean navBarEnabled = true;
    private boolean showLoading = true;
    private boolean supportPullRefresh = false;

    public boolean isShowLoading() {
        return this.showLoading;
    }

    public void setShowLoading(boolean showLoading2) {
        this.showLoading = showLoading2;
    }

    public boolean isSupportPullRefresh() {
        return this.supportPullRefresh;
    }

    public void setSupportPullRefresh(boolean supportPullRefresh2) {
        this.supportPullRefresh = supportPullRefresh2;
    }

    public boolean isNavBarEnabled() {
        return this.navBarEnabled;
    }

    public void setNavBarEnabled(boolean navBarEnabled2) {
        this.navBarEnabled = navBarEnabled2;
    }

    public boolean isJsbridgeEnabled() {
        return this.jsbridgeEnabled;
    }

    public void setJsbridgeEnabled(boolean jsbridgeEnabled2) {
        this.jsbridgeEnabled = jsbridgeEnabled2;
    }

    public ParamsParcelable() {
    }

    public ParamsParcelable(Parcel in) {
        boolean z;
        boolean z2;
        boolean z3;
        boolean z4 = true;
        if (in.readInt() == 1) {
            z = true;
        } else {
            z = false;
        }
        this.showLoading = z;
        if (in.readInt() == 1) {
            z2 = true;
        } else {
            z2 = false;
        }
        this.supportPullRefresh = z2;
        if (in.readInt() == 1) {
            z3 = true;
        } else {
            z3 = false;
        }
        this.navBarEnabled = z3;
        this.jsbridgeEnabled = in.readInt() != 1 ? false : z4;
    }

    public int describeContents() {
        return 0;
    }

    public void writeToParcel(Parcel dest, int flags) {
        int i;
        int i2;
        int i3;
        int i4 = 1;
        if (this.showLoading) {
            i = 1;
        } else {
            i = 0;
        }
        dest.writeInt(i);
        if (this.supportPullRefresh) {
            i2 = 1;
        } else {
            i2 = 0;
        }
        dest.writeInt(i2);
        if (this.navBarEnabled) {
            i3 = 1;
        } else {
            i3 = 0;
        }
        dest.writeInt(i3);
        if (!this.jsbridgeEnabled) {
            i4 = 0;
        }
        dest.writeInt(i4);
    }
}
