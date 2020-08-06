package anetwork.channel;

import anetwork.channel.NetworkEvent;
import anetwork.channel.aidl.ParcelableInputStream;
import java.util.List;
import java.util.Map;

public class NetworkCallBack {

    public interface FinishListener extends NetworkListener {
        void onFinished(NetworkEvent.FinishEvent finishEvent, Object obj);
    }

    public interface InputStreamListener extends NetworkListener {
        void onInputStreamGet(ParcelableInputStream parcelableInputStream, Object obj);
    }

    public interface ProgressListener extends NetworkListener {
        void onDataReceived(NetworkEvent.ProgressEvent progressEvent, Object obj);
    }

    public interface ResponseCodeListener extends NetworkListener {
        boolean onResponseCode(int i, Map<String, List<String>> map, Object obj);
    }
}
