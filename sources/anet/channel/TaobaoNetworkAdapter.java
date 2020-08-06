package anet.channel;

import anet.channel.analysis.DefaultNetworkAnalysis;
import anet.channel.appmonitor.AppMonitor;
import anet.channel.appmonitor.DefaultAppMonitor;
import anet.channel.cache.AVFSCacheImpl;
import anet.channel.config.OrangeConfigImpl;
import anet.channel.flow.NetworkAnalysis;
import anet.channel.log.TLogAdapter;
import anet.channel.thread.ThreadPoolExecutorFactory;
import anet.channel.util.ALog;
import anetwork.channel.cache.CacheManager;
import anetwork.channel.cache.CachePrediction;
import anetwork.channel.config.NetworkConfigCenter;
import java.io.Serializable;
import java.util.Map;
import java.util.concurrent.atomic.AtomicBoolean;

public class TaobaoNetworkAdapter implements Serializable {
    public static AtomicBoolean isInited = new AtomicBoolean();

    public static void init() {
        if (isInited.compareAndSet(false, true)) {
            ALog.setLog(new TLogAdapter());
            NetworkConfigCenter.setRemoteConfig(new OrangeConfigImpl());
            AppMonitor.setInstance(new DefaultAppMonitor());
            NetworkAnalysis.setInstance(new DefaultNetworkAnalysis());
            ThreadPoolExecutorFactory.submitScheduledTask(new Runnable() {
                public void run() {
                    try {
                        AVFSCacheImpl avfsCacheImpl = new AVFSCacheImpl();
                        avfsCacheImpl.initialize();
                        CacheManager.addCache(avfsCacheImpl, new CachePrediction() {
                            public boolean handleCache(String key, Map<String, String> headers) {
                                return "weex".equals(headers.get("f-refer"));
                            }
                        }, 1);
                    } catch (Exception e) {
                    }
                }
            });
        }
    }
}
