package mtopsdk.network.domain;

import android.os.Parcel;
import android.os.Parcelable;
import java.io.IOException;
import java.io.OutputStream;

public class ParcelableRequestBodyImpl extends RequestBody implements Parcelable {
    public static final Parcelable.Creator<ParcelableRequestBodyImpl> CREATOR = new Parcelable.Creator<ParcelableRequestBodyImpl>() {
        public ParcelableRequestBodyImpl createFromParcel(Parcel source) {
            return new ParcelableRequestBodyImpl(source);
        }

        public ParcelableRequestBodyImpl[] newArray(int size) {
            return new ParcelableRequestBodyImpl[size];
        }
    };
    private byte[] content;
    private String contentType;

    public ParcelableRequestBodyImpl(String contentType2, byte[] content2) {
        this.content = content2;
        this.contentType = contentType2;
    }

    public String contentType() {
        return this.contentType;
    }

    public long contentLength() {
        return this.content != null ? (long) this.content.length : super.contentLength();
    }

    public void writeTo(OutputStream os) throws IOException {
        os.write(this.content);
    }

    public int describeContents() {
        return 0;
    }

    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.contentType);
        dest.writeByteArray(this.content);
    }

    protected ParcelableRequestBodyImpl(Parcel in) {
        this.contentType = in.readString();
        this.content = in.createByteArray();
    }
}
