package android.taobao.windvane.monitor;

import java.util.Arrays;
import java.util.List;

public class WVJsMonitor implements WVJSBrdigeMonitorInterface {
    public static final int EVENT_ID = 15302;
    public static final int TYPE_CALL = 0;
    public static final int TYPE_CALLBACK = 1;
    public static final int TYPE_ERROR = 3;
    public static final int TYPE_FIREEVENT = 2;
    private static final List<String> ignoreEvent = Arrays.asList(new String[]{"WindVaneReady", "WV.Event.Key.Back", "WV.Event.APP.Background", "WV.Event.APP.Active"});
    private static final List<String> ignoreObjectName = Arrays.asList(new String[]{"GUtil", "GCanvas", "GMedia", "WVTBUserTrack"});

    private static void sendToUt(int type, String objectName, String methodName, String ret, String url) {
        if (objectName == null || !ignoreObjectName.contains(objectName)) {
            String valueOf = String.valueOf(type);
            String format = (objectName == null || methodName == null) ? "-" : String.format("%s.%s", new Object[]{objectName, methodName});
            if (ret == null) {
                ret = "-";
            }
            UserTrackUtil.commitEvent(EVENT_ID, valueOf, format, ret, url != null ? new String[]{"url=" + url} : new String[0]);
        }
    }

    public void didCallAtURL(String objectName, String methodName, String url) {
        sendToUt(0, objectName, methodName, (String) null, url);
    }

    public void didOccurError(String objectName, String methodName, String error, String url) {
        sendToUt(3, objectName, methodName, error, url);
    }

    public void didCallBackAtURL(String objectName, String methodName, String url, String returnCode) {
        sendToUt(1, objectName, methodName, returnCode, url);
    }

    public void didFireEvent(String url, String eventName) {
        if (eventName == null || !ignoreEvent.contains(eventName)) {
            sendToUt(2, (String) null, (String) null, eventName, url);
        }
    }
}
