package anetwork.channel.entity;

import android.os.Parcel;
import android.os.Parcelable;
import android.os.RemoteException;
import anet.channel.bytes.ByteArray;
import anet.channel.bytes.ByteArrayPool;
import anet.channel.request.BodyEntry;
import anetwork.channel.IBodyHandler;
import anetwork.channel.aidl.ParcelableBodyHandler;
import anetwork.channel.aidl.adapter.ParcelableBodyHandlerWrapper;
import java.io.IOException;
import java.io.OutputStream;

public class BodyHandlerEntry implements BodyEntry {
    public static final Parcelable.Creator<BodyHandlerEntry> CREATOR = new Parcelable.Creator<BodyHandlerEntry>() {
        public BodyHandlerEntry createFromParcel(Parcel parcel) {
            BodyHandlerEntry bodyHandlerEntry = new BodyHandlerEntry();
            bodyHandlerEntry.bodyHandler = ParcelableBodyHandlerWrapper.asInterface(parcel.readStrongBinder());
            return bodyHandlerEntry;
        }

        public BodyHandlerEntry[] newArray(int i) {
            return new BodyHandlerEntry[i];
        }
    };
    ParcelableBodyHandler bodyHandler;

    public BodyHandlerEntry(IBodyHandler bodyHandler2) {
        this.bodyHandler = null;
        this.bodyHandler = new ParcelableBodyHandlerWrapper(bodyHandler2);
    }

    private BodyHandlerEntry() {
        this.bodyHandler = null;
    }

    public String getContentType() {
        return null;
    }

    public int writeTo(OutputStream outputStream) throws IOException {
        int count = 0;
        try {
            ByteArray byteArray = ByteArrayPool.getInstance().retrieve(2048);
            while (!this.bodyHandler.isCompleted()) {
                int len = this.bodyHandler.read(byteArray.getBuffer());
                outputStream.write(byteArray.getBuffer(), 0, len);
                count += len;
            }
            byteArray.recycle();
            return count;
        } catch (RemoteException e) {
            throw new IOException("RemoteException", e);
        }
    }

    public int describeContents() {
        return 0;
    }

    public void writeToParcel(Parcel dest, int flags) {
        dest.writeStrongInterface(this.bodyHandler);
    }
}
