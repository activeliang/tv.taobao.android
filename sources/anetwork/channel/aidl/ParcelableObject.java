package anetwork.channel.aidl;

import android.os.Parcel;
import android.os.Parcelable;

public class ParcelableObject implements Parcelable {
    public static final Parcelable.Creator<ParcelableObject> CREATOR = new Parcelable.Creator<ParcelableObject>() {
        public ParcelableObject createFromParcel(Parcel source) {
            return new ParcelableObject(source);
        }

        public ParcelableObject[] newArray(int size) {
            return new ParcelableObject[size];
        }
    };
    private Object object;

    public ParcelableObject(Object object2) {
        this.object = object2;
    }

    public Object getObject() {
        return this.object;
    }

    public ParcelableObject() {
    }

    ParcelableObject(Parcel source) {
        readFromParcel(source);
    }

    public int describeContents() {
        return 0;
    }

    public void writeToParcel(Parcel dest, int flags) {
    }

    /* access modifiers changed from: package-private */
    public ParcelableObject readFromParcel(Parcel source) {
        return new ParcelableObject();
    }
}
