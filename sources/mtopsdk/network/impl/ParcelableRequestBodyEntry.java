package mtopsdk.network.impl;

import android.os.Parcel;
import android.os.Parcelable;
import anet.channel.request.BodyEntry;
import java.io.IOException;
import java.io.OutputStream;
import mtopsdk.network.domain.ParcelableRequestBodyImpl;

public class ParcelableRequestBodyEntry implements BodyEntry {
    public static final Parcelable.Creator<ParcelableRequestBodyEntry> CREATOR = new Parcelable.Creator<ParcelableRequestBodyEntry>() {
        public ParcelableRequestBodyEntry createFromParcel(Parcel source) {
            return new ParcelableRequestBodyEntry(source);
        }

        public ParcelableRequestBodyEntry[] newArray(int size) {
            return new ParcelableRequestBodyEntry[size];
        }
    };
    ParcelableRequestBodyImpl requestBody;

    public ParcelableRequestBodyEntry(ParcelableRequestBodyImpl requestBody2) {
        this.requestBody = requestBody2;
    }

    public String getContentType() {
        return this.requestBody.contentType();
    }

    public int writeTo(OutputStream outputStream) throws IOException {
        this.requestBody.writeTo(outputStream);
        return (int) this.requestBody.contentLength();
    }

    public int describeContents() {
        return 0;
    }

    public void writeToParcel(Parcel dest, int flags) {
        dest.writeParcelable(this.requestBody, flags);
    }

    protected ParcelableRequestBodyEntry(Parcel in) {
        this.requestBody = (ParcelableRequestBodyImpl) in.readParcelable(ParcelableRequestBodyImpl.class.getClassLoader());
    }
}
