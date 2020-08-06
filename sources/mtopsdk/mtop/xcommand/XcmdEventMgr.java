package mtopsdk.mtop.xcommand;

import java.util.Set;
import java.util.concurrent.CopyOnWriteArraySet;
import mtopsdk.common.util.StringUtils;

public class XcmdEventMgr {
    private static final String TAG = "mtopsdk.XcmdEventMgr";
    static Set<NewXcmdListener> oxcmdListeners = new CopyOnWriteArraySet();

    private static class SingletonHolder {
        static XcmdEventMgr xm = new XcmdEventMgr();

        private SingletonHolder() {
        }
    }

    public static XcmdEventMgr getInstance() {
        return SingletonHolder.xm;
    }

    private XcmdEventMgr() {
    }

    public void addOrangeXcmdListener(NewXcmdListener oXcmdlistener) {
        oxcmdListeners.add(oXcmdlistener);
    }

    public void removeOrangeXcmdListener(NewXcmdListener oXcmdlistener) {
        oxcmdListeners.remove(oXcmdlistener);
    }

    public void onOrangeEvent(String value) {
        if (!StringUtils.isBlank(value)) {
            NewXcmdEvent oxcmdEvent = new NewXcmdEvent(value);
            for (NewXcmdListener oxcmdListener : oxcmdListeners) {
                try {
                    oxcmdListener.onEvent(oxcmdEvent);
                } catch (Throwable th) {
                }
            }
        }
    }
}
