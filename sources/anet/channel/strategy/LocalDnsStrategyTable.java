package anet.channel.strategy;

import android.text.TextUtils;
import anet.channel.strategy.dispatch.DispatchConstants;
import anet.channel.strategy.utils.AmdcThreadPoolExecutor;
import anet.channel.strategy.utils.Utils;
import anet.channel.util.ALog;
import com.ali.user.open.tbauth.TbAuthConstants;
import java.net.InetAddress;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;

class LocalDnsStrategyTable {
    private static final String TAG = "awcn.LocalDnsStrategyTable";
    final ConcurrentHashMap<String, List<IPConnStrategy>> localStrategyMap = new ConcurrentHashMap<>();
    final HashMap<String, Object> lockObjMap = new HashMap<>();

    LocalDnsStrategyTable() {
    }

    /* access modifiers changed from: package-private */
    public List queryByHost(String host) {
        Object lockObj;
        if (TextUtils.isEmpty(host) || !Utils.checkHostValidAndNotIp(host) || DispatchConstants.getAmdcServerDomain().equalsIgnoreCase(host)) {
            return Collections.EMPTY_LIST;
        }
        if (ALog.isPrintLog(1)) {
            ALog.d(TAG, "try resolve ip with local dns", (String) null, "host", host);
        }
        List list = Collections.EMPTY_LIST;
        if (!this.localStrategyMap.containsKey(host)) {
            synchronized (this.lockObjMap) {
                if (!this.lockObjMap.containsKey(host)) {
                    lockObj = new Object();
                    this.lockObjMap.put(host, lockObj);
                    startLocalDnsLookup(host, lockObj);
                } else {
                    lockObj = this.lockObjMap.get(host);
                }
            }
            if (lockObj != null) {
                try {
                    synchronized (lockObj) {
                        lockObj.wait(500);
                    }
                } catch (InterruptedException e) {
                }
            }
        }
        List<IPConnStrategy> localStrategyList = this.localStrategyMap.get(host);
        if (localStrategyList == null || localStrategyList == Collections.EMPTY_LIST) {
            return list;
        }
        return new ArrayList<>(localStrategyList);
    }

    /* access modifiers changed from: package-private */
    public void notifyConnEvent(String host, IConnStrategy connStrategy, ConnEvent connEvent) {
        List<IPConnStrategy> list;
        if (!connEvent.isSuccess && !TextUtils.isEmpty(host) && (list = this.localStrategyMap.get(host)) != null && list != Collections.EMPTY_LIST) {
            Iterator<IPConnStrategy> iterator = list.iterator();
            while (iterator.hasNext()) {
                if (iterator.next() == connStrategy) {
                    iterator.remove();
                }
            }
            if (list.isEmpty()) {
                this.localStrategyMap.put(host, Collections.EMPTY_LIST);
            }
        }
    }

    private void startLocalDnsLookup(final String host, final Object lockObj) {
        AmdcThreadPoolExecutor.submitTask(new Runnable() {
            public void run() {
                int i = 80;
                boolean isSsl = false;
                try {
                    String ip = InetAddress.getByName(host).getHostAddress();
                    if (Utils.isIPV4Address(ip)) {
                        ConnProtocol connProtocol = StrategyTemplate.getInstance().getConnProtocol(host);
                        List<IPConnStrategy> list = new LinkedList<>();
                        if (connProtocol != null) {
                            if (connProtocol.protocol.equalsIgnoreCase("https") || !TextUtils.isEmpty(connProtocol.publicKey)) {
                                isSsl = true;
                            }
                            if (isSsl) {
                                i = 443;
                            }
                            list.add(IPConnStrategy.create(ip, i, connProtocol, 0, 0, 1, 45000));
                            LocalDnsStrategyTable.this.localStrategyMap.put(host, list);
                        } else {
                            list.add(IPConnStrategy.create(ip, 80, ConnProtocol.HTTP, 0, 0, 0, 0));
                            list.add(IPConnStrategy.create(ip, 443, ConnProtocol.HTTPS, 0, 0, 0, 0));
                            LocalDnsStrategyTable.this.localStrategyMap.put(host, list);
                        }
                        if (ALog.isPrintLog(1)) {
                            ALog.d(LocalDnsStrategyTable.TAG, "resolve ip by local dns", (String) null, "host", host, TbAuthConstants.IP, ip);
                        }
                    } else {
                        LocalDnsStrategyTable.this.localStrategyMap.put(host, Collections.EMPTY_LIST);
                    }
                    synchronized (LocalDnsStrategyTable.this.lockObjMap) {
                        LocalDnsStrategyTable.this.lockObjMap.remove(host);
                    }
                    synchronized (lockObj) {
                        lockObj.notifyAll();
                    }
                } catch (Exception e) {
                    if (ALog.isPrintLog(1)) {
                        ALog.d(LocalDnsStrategyTable.TAG, "resolve ip by local dns failed", (String) null, "host", host);
                    }
                    synchronized (LocalDnsStrategyTable.this.lockObjMap) {
                        LocalDnsStrategyTable.this.lockObjMap.remove(host);
                        synchronized (lockObj) {
                            lockObj.notifyAll();
                        }
                    }
                } catch (Throwable th) {
                    synchronized (LocalDnsStrategyTable.this.lockObjMap) {
                        LocalDnsStrategyTable.this.lockObjMap.remove(host);
                        synchronized (lockObj) {
                            lockObj.notifyAll();
                            throw th;
                        }
                    }
                }
            }
        });
    }
}
