package anet.channel.strategy.dispatch;

import android.text.TextUtils;
import anet.channel.GlobalAppRuntimeInfo;
import anet.channel.util.ALog;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeSet;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArraySet;
import java.util.concurrent.atomic.AtomicBoolean;

public class HttpDispatcher {
    private AmdcTaskExecutor executor;
    private Set<String> initHosts;
    private volatile boolean isEnable;
    private AtomicBoolean isInitHostsFilled;
    private CopyOnWriteArraySet<IDispatchEventListener> listeners;
    private Set<String> uniqueIdSet;

    public interface IDispatchEventListener {
        void onEvent(DispatchEvent dispatchEvent);
    }

    private static class Singleton {
        static HttpDispatcher instance = new HttpDispatcher();

        private Singleton() {
        }
    }

    public static HttpDispatcher getInstance() {
        return Singleton.instance;
    }

    private HttpDispatcher() {
        this.listeners = new CopyOnWriteArraySet<>();
        this.executor = new AmdcTaskExecutor();
        this.isEnable = true;
        this.uniqueIdSet = Collections.newSetFromMap(new ConcurrentHashMap());
        this.initHosts = new TreeSet();
        this.isInitHostsFilled = new AtomicBoolean();
        fillInitHosts();
    }

    public void sendAmdcRequest(Set<String> hostSet, String preIp, int configVersion) {
        if (this.isEnable && hostSet != null && !hostSet.isEmpty()) {
            if (ALog.isPrintLog(2)) {
                ALog.i("awcn.HttpDispatcher", "sendAmdcRequest", (String) null, DispatchConstants.HOSTS, hostSet.toString());
            }
            Map<String, Object> params = new HashMap<>();
            params.put(DispatchConstants.HOSTS, hostSet);
            params.put(DispatchConstants.PRE_IP, preIp);
            params.put(DispatchConstants.CONFIG_VERSION, String.valueOf(configVersion));
            this.executor.addTask(params);
        }
    }

    public void addListener(IDispatchEventListener listener) {
        this.listeners.add(listener);
    }

    public void removeListener(IDispatchEventListener listener) {
        this.listeners.remove(listener);
    }

    /* access modifiers changed from: package-private */
    public void fireEvent(DispatchEvent event) {
        Iterator i$ = this.listeners.iterator();
        while (i$.hasNext()) {
            try {
                i$.next().onEvent(event);
            } catch (Exception e) {
            }
        }
    }

    public void setEnable(boolean bEnable) {
        this.isEnable = bEnable;
    }

    public synchronized void addHosts(List<String> host) {
        if (host != null) {
            this.initHosts.addAll(host);
            this.uniqueIdSet.clear();
        }
    }

    public static void setInitHosts(List<String> initHosts2) {
        if (initHosts2 != null) {
            DispatchConstants.initHostArray = (String[]) initHosts2.toArray(new String[0]);
        }
    }

    public synchronized Set<String> getInitHosts() {
        fillInitHosts();
        return new HashSet(this.initHosts);
    }

    private void fillInitHosts() {
        if (!this.isInitHostsFilled.get() && GlobalAppRuntimeInfo.getContext() != null && this.isInitHostsFilled.compareAndSet(false, true)) {
            this.initHosts.add(DispatchConstants.getAmdcServerDomain());
            if (GlobalAppRuntimeInfo.isTargetProcess()) {
                this.initHosts.addAll(Arrays.asList(DispatchConstants.initHostArray));
            }
        }
    }

    public boolean isInitHostsChanged(String uniqueId) {
        if (TextUtils.isEmpty(uniqueId)) {
            return false;
        }
        boolean b = this.uniqueIdSet.contains(uniqueId);
        if (!b) {
            this.uniqueIdSet.add(uniqueId);
        }
        if (!b) {
            return true;
        }
        return false;
    }

    public void switchENV() {
        this.uniqueIdSet.clear();
        this.initHosts.clear();
        this.isInitHostsFilled.set(false);
    }
}
