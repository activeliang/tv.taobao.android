package anetwork.channel.interceptor;

import anet.channel.bytes.ByteArray;
import anetwork.channel.aidl.DefaultFinishEvent;
import java.util.List;
import java.util.Map;

public interface Callback {
    void onDataReceiveSize(int i, int i2, ByteArray byteArray);

    void onFinish(DefaultFinishEvent defaultFinishEvent);

    void onResponseCode(int i, Map<String, List<String>> map);
}
