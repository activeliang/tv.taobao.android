package com.taobao.tao.remotebusiness.handler;

import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import com.taobao.tao.remotebusiness.IRemoteCacheListener;
import com.taobao.tao.remotebusiness.IRemoteListener;
import com.taobao.tao.remotebusiness.IRemoteProcessListener;
import com.taobao.tao.remotebusiness.MtopBusiness;
import mtopsdk.common.util.TBSdkLog;
import mtopsdk.mtop.common.MtopCacheEvent;
import mtopsdk.mtop.common.MtopEvent;
import mtopsdk.mtop.common.MtopHeaderEvent;
import mtopsdk.mtop.common.MtopListener;
import mtopsdk.mtop.common.MtopProgressEvent;
import mtopsdk.mtop.util.MtopStatistics;

public class HandlerMgr extends Handler {
    public static final int ON_CACHED = 4;
    public static final int ON_DATA_RECEIVED = 1;
    public static final int ON_FINISHED = 3;
    public static final int ON_HEADER = 2;
    private static final String TAG = "mtopsdk.HandlerMgr";
    private static volatile Handler mHandler;

    private HandlerMgr(Looper looper) {
        super(looper);
    }

    public static Handler instance() {
        if (mHandler == null) {
            synchronized (HandlerMgr.class) {
                if (mHandler == null) {
                    mHandler = new HandlerMgr(Looper.getMainLooper());
                }
            }
        }
        return mHandler;
    }

    public void handleMessage(Message msg) {
        MtopStatistics mtopStat;
        HandlerParam hMsg = (HandlerParam) msg.obj;
        if (checkBeforeCallback(hMsg)) {
            String seqNo = hMsg.mtopBusiness.getSeqNo();
            Object reqContext = hMsg.mtopBusiness.getReqContext();
            switch (msg.what) {
                case 1:
                    if (TBSdkLog.isLogEnable(TBSdkLog.LogEnable.InfoEnable)) {
                        TBSdkLog.i(TAG, seqNo, "onReceive: ON_DATA_RECEIVED.");
                    }
                    try {
                        ((IRemoteProcessListener) hMsg.listener).onDataReceived((MtopProgressEvent) hMsg.event, reqContext);
                        break;
                    } catch (Throwable e) {
                        TBSdkLog.e(TAG, seqNo, "listener onDataReceived callback error.", e);
                        break;
                    }
                case 2:
                    if (TBSdkLog.isLogEnable(TBSdkLog.LogEnable.InfoEnable)) {
                        TBSdkLog.i(TAG, seqNo, "onReceive: ON_HEADER.");
                    }
                    try {
                        ((IRemoteProcessListener) hMsg.listener).onHeader((MtopHeaderEvent) hMsg.event, reqContext);
                        break;
                    } catch (Throwable e2) {
                        TBSdkLog.e(TAG, seqNo, "listener onHeader callback error.", e2);
                        break;
                    }
                case 3:
                    if (TBSdkLog.isLogEnable(TBSdkLog.LogEnable.InfoEnable)) {
                        TBSdkLog.i(TAG, seqNo, "onReceive: ON_FINISHED.");
                    }
                    long sTime = System.currentTimeMillis();
                    long dataSize = 0;
                    MtopStatistics mtopStat2 = null;
                    MtopStatistics.RbStatisticData statData = null;
                    if (!(hMsg.mtopResponse == null || (mtopStat2 = hMsg.mtopResponse.getMtopStat()) == null)) {
                        statData = mtopStat2.getRbStatData();
                        statData.toMainThTime = sTime - hMsg.mtopBusiness.onBgFinishTime;
                        if (hMsg.mtopResponse.getBytedata() != null) {
                            dataSize = (long) hMsg.mtopResponse.getBytedata().length;
                        }
                    }
                    hMsg.mtopBusiness.doFinish(hMsg.mtopResponse, hMsg.pojo);
                    if (TBSdkLog.isLogEnable(TBSdkLog.LogEnable.InfoEnable)) {
                        StringBuilder sb = new StringBuilder(128);
                        sb.append("onReceive: ON_FINISHED. ").append("doFinishTime=").append(System.currentTimeMillis() - sTime).append("; dataSize=").append(dataSize).append("; ");
                        if (statData != null) {
                            sb.append(statData.toString());
                        }
                        TBSdkLog.i(TAG, seqNo, sb.toString());
                    }
                    if (mtopStat2 != null) {
                        mtopStat2.commitStatData(true);
                        break;
                    }
                    break;
                case 4:
                    if (TBSdkLog.isLogEnable(TBSdkLog.LogEnable.InfoEnable)) {
                        TBSdkLog.i(TAG, seqNo, "onReceive: ON_CACHED");
                    }
                    MtopCacheEvent event = (MtopCacheEvent) hMsg.event;
                    if (event != null) {
                        if (!(event.getMtopResponse() == null || (mtopStat = event.getMtopResponse().getMtopStat()) == null)) {
                            MtopStatistics.RbStatisticData statData2 = mtopStat.getRbStatData();
                            statData2.toMainThTime = System.currentTimeMillis() - hMsg.mtopBusiness.onBgFinishTime;
                            if (TBSdkLog.isLogEnable(TBSdkLog.LogEnable.DebugEnable)) {
                                TBSdkLog.d(TAG, seqNo, statData2.toString());
                            }
                            mtopStat.commitStatData(true);
                        }
                        try {
                            if (!(hMsg.listener instanceof IRemoteCacheListener)) {
                                TBSdkLog.i(TAG, hMsg.mtopBusiness.getSeqNo(), "listener onCached transfer to onSuccess callback");
                                ((IRemoteListener) hMsg.listener).onSuccess(hMsg.mtopBusiness.getRequestType(), hMsg.mtopResponse, hMsg.pojo, reqContext);
                                break;
                            } else {
                                TBSdkLog.i(TAG, seqNo, "listener onCached callback");
                                ((IRemoteCacheListener) hMsg.listener).onCached(event, hMsg.pojo, reqContext);
                                break;
                            }
                        } catch (Throwable e3) {
                            TBSdkLog.e(TAG, seqNo, "listener onCached callback error.", e3);
                            break;
                        }
                    } else {
                        TBSdkLog.e(TAG, seqNo, "HandlerMsg.event is null.");
                        return;
                    }
            }
            msg.obj = null;
        }
    }

    public static HandlerParam getHandlerMsg(MtopListener listener, MtopEvent event, MtopBusiness mtopBusiness) {
        return new HandlerParam(listener, event, mtopBusiness);
    }

    private boolean checkBeforeCallback(HandlerParam hMsg) {
        if (hMsg == null) {
            TBSdkLog.e(TAG, "", "HandlerMsg is null.");
            return false;
        }
        if (hMsg.mtopBusiness != null) {
            String seqNo = hMsg.mtopBusiness.getSeqNo();
            if (hMsg.mtopBusiness.isTaskCanceled()) {
                TBSdkLog.i(TAG, seqNo, "The request of MtopBusiness is cancelled.");
                return false;
            }
        }
        return true;
    }
}
