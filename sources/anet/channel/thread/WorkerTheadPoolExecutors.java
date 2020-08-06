package anet.channel.thread;

import anet.channel.monitor.BandWidthListenerHelper;
import anet.channel.monitor.BandWidthSampler;
import anet.channel.monitor.INetworkQualityChangeListener;
import anet.channel.monitor.NetworkSpeed;
import anet.channel.monitor.QualityChangeFilter;
import anet.channel.thread.ThreadPoolExecutorFactory;
import anet.channel.util.ALog;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.LinkedBlockingDeque;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

class WorkerTheadPoolExecutors {
    private static volatile ThreadPoolExecutor highPriorityWorks;
    private static volatile ThreadPoolExecutor lowPriorityWorks;

    WorkerTheadPoolExecutors() {
    }

    static ThreadPoolExecutor getHighPriorityExecutor() {
        if (highPriorityWorks == null) {
            synchronized (ThreadPoolExecutorFactory.class) {
                if (highPriorityWorks == null) {
                    highPriorityWorks = new ThreadPoolExecutor(2, 6, 60, TimeUnit.SECONDS, new ArrayBlockingQueue(8), new ThreadPoolExecutorFactory.TBThreadFactory("AWCN Worker(H)"));
                    highPriorityWorks.allowCoreThreadTimeOut(true);
                    registerSlowSpeedListener();
                }
            }
        }
        return highPriorityWorks;
    }

    static ThreadPoolExecutor getLowPriorityExecutor() {
        if (lowPriorityWorks == null) {
            synchronized (ThreadPoolExecutorFactory.class) {
                if (lowPriorityWorks == null) {
                    lowPriorityWorks = new ThreadPoolExecutor(2, 2, 60, TimeUnit.SECONDS, new LinkedBlockingDeque(), new ThreadPoolExecutorFactory.TBThreadFactory("AWCN Worker(L)"));
                    lowPriorityWorks.allowCoreThreadTimeOut(true);
                }
            }
        }
        return lowPriorityWorks;
    }

    private static void registerSlowSpeedListener() {
        BandWidthListenerHelper.getInstance().addQualityChangeListener(new INetworkQualityChangeListener() {
            public void onNetworkQualityChanged(NetworkSpeed networkSpeed) {
                int i = 3;
                ALog.i("awcn.ThreadPoolExecutorFactory", "", (String) null, "Network", networkSpeed, "Speed", Integer.valueOf(((int) BandWidthSampler.getInstance().getNetSpeedValue()) * 1024));
                ThreadPoolExecutor highPriorityExecutor = WorkerTheadPoolExecutors.getHighPriorityExecutor();
                if (networkSpeed != NetworkSpeed.Slow) {
                    i = 2;
                }
                highPriorityExecutor.setCorePoolSize(i);
            }
        }, new QualityChangeFilter() {
            public boolean detectNetSpeedSlow(double speed) {
                return speed <= 30.0d;
            }
        });
    }
}
