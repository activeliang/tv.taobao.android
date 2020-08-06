package anet.channel;

import android.text.TextUtils;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

class SessionAttributeManager {
    Map<String, Integer> publicKeyMap = new HashMap();
    Map<String, SessionInfo> sessionInfoMap = new ConcurrentHashMap();

    SessionAttributeManager() {
    }

    /* access modifiers changed from: package-private */
    public void registerSessionInfo(SessionInfo info) {
        if (info == null) {
            throw new NullPointerException("info is null");
        } else if (TextUtils.isEmpty(info.host)) {
            throw new IllegalArgumentException("host cannot be null or empty");
        } else {
            this.sessionInfoMap.put(info.host, info);
        }
    }

    /* access modifiers changed from: package-private */
    public SessionInfo unregisterSessionInfo(String host) {
        return this.sessionInfoMap.remove(host);
    }

    /* access modifiers changed from: package-private */
    public SessionInfo getSessionInfo(String host) {
        return this.sessionInfoMap.get(host);
    }

    /* access modifiers changed from: package-private */
    public Collection<SessionInfo> getSessionInfos() {
        return this.sessionInfoMap.values();
    }

    /* access modifiers changed from: package-private */
    public void registerPublicKey(String host, int publicKey) {
        if (TextUtils.isEmpty(host)) {
            throw new IllegalArgumentException("host cannot be null or empty");
        }
        synchronized (this.publicKeyMap) {
            this.publicKeyMap.put(host, Integer.valueOf(publicKey));
        }
    }

    public int getPublicKey(String host) {
        Integer publicKey;
        synchronized (this.publicKeyMap) {
            publicKey = this.publicKeyMap.get(host);
        }
        if (publicKey == null) {
            return -1;
        }
        return publicKey.intValue();
    }
}
