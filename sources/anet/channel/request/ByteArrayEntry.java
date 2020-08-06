package anet.channel.request;

import android.os.Parcel;
import android.os.Parcelable;
import java.io.IOException;
import java.io.OutputStream;

public class ByteArrayEntry implements BodyEntry {
    public static final Parcelable.Creator<ByteArrayEntry> CREATOR = new Parcelable.Creator<ByteArrayEntry>() {
        public ByteArrayEntry createFromParcel(Parcel parcel) {
            ByteArrayEntry byteArrayEntry = new ByteArrayEntry();
            byte[] unused = byteArrayEntry.bytes = new byte[parcel.readInt()];
            parcel.readByteArray(byteArrayEntry.bytes);
            int unused2 = byteArrayEntry.offset = parcel.readInt();
            int unused3 = byteArrayEntry.count = parcel.readInt();
            return byteArrayEntry;
        }

        public ByteArrayEntry[] newArray(int i) {
            return new ByteArrayEntry[i];
        }
    };
    /* access modifiers changed from: private */
    public byte[] bytes;
    private String contentType;
    /* access modifiers changed from: private */
    public int count;
    /* access modifiers changed from: private */
    public int offset;

    public ByteArrayEntry(byte[] bytes2) {
        this(bytes2, 0, bytes2.length);
    }

    public ByteArrayEntry(byte[] bytes2, int offset2, int count2) {
        this.offset = 0;
        this.count = 0;
        this.contentType = null;
        this.bytes = bytes2;
        this.offset = offset2;
        this.count = count2;
    }

    private ByteArrayEntry() {
        this.offset = 0;
        this.count = 0;
        this.contentType = null;
    }

    public String getContentType() {
        return this.contentType;
    }

    public void setContentType(String contentType2) {
        this.contentType = contentType2;
    }

    public int writeTo(OutputStream outputStream) throws IOException {
        outputStream.write(this.bytes, this.offset, this.count);
        return this.count;
    }

    public int describeContents() {
        return 0;
    }

    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeInt(this.bytes.length);
        parcel.writeByteArray(this.bytes);
        parcel.writeInt(this.offset);
        parcel.writeInt(this.count);
    }
}
