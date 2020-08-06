package com.taobao.tao.remotebusiness.listener;

import com.taobao.tao.remotebusiness.IRemoteCacheListener;
import com.taobao.tao.remotebusiness.IRemoteListener;
import com.taobao.tao.remotebusiness.MtopBusiness;
import com.taobao.tao.remotebusiness.handler.HandlerMgr;
import com.taobao.tao.remotebusiness.handler.HandlerParam;
import mtopsdk.common.util.TBSdkLog;
import mtopsdk.mtop.common.MtopCacheEvent;
import mtopsdk.mtop.common.MtopCallback;
import mtopsdk.mtop.common.MtopListener;
import mtopsdk.mtop.domain.BaseOutDo;
import mtopsdk.mtop.domain.MtopResponse;
import mtopsdk.mtop.util.MtopConvert;
import mtopsdk.mtop.util.MtopStatistics;

class MtopCacheListenerImpl extends MtopBaseListener implements MtopCallback.MtopCacheListener {
    private static final String TAG = "mtopsdk.MtopCacheListenerImpl";

    public MtopCacheListenerImpl(MtopBusiness mtopBusiness, MtopListener listener) {
        super(mtopBusiness, listener);
    }

    public void onCached(MtopCacheEvent event, Object context) {
        String seqNo = this.mtopBusiness.getSeqNo();
        if (TBSdkLog.isLogEnable(TBSdkLog.LogEnable.InfoEnable)) {
            TBSdkLog.i(TAG, seqNo, "Mtop onCached event received. apiKey=" + this.mtopBusiness.request.getKey());
        }
        if (this.mtopBusiness.isTaskCanceled()) {
            if (TBSdkLog.isLogEnable(TBSdkLog.LogEnable.InfoEnable)) {
                TBSdkLog.i(TAG, seqNo, "The request of MtopBusiness is cancelled.");
            }
        } else if (this.listener == null) {
            TBSdkLog.e(TAG, seqNo, "The listener of MtopBusiness is null.");
        } else if (event == null) {
            TBSdkLog.e(TAG, seqNo, "MtopCacheEvent is null.");
        } else {
            MtopResponse mtopResponse = event.getMtopResponse();
            if (mtopResponse == null) {
                TBSdkLog.e(TAG, seqNo, "The MtopResponse of MtopCacheEvent is null.");
                return;
            }
            long cacheReturnTime = System.currentTimeMillis();
            BaseOutDo pojo = null;
            long sTime = System.currentTimeMillis();
            if (mtopResponse.isApiSuccess() && this.mtopBusiness.clazz != null) {
                pojo = MtopConvert.mtopResponseToOutputDO(mtopResponse, this.mtopBusiness.clazz);
            }
            long eTime = System.currentTimeMillis();
            this.mtopBusiness.onBgFinishTime = eTime;
            MtopStatistics mtopStat = mtopResponse.getMtopStat();
            MtopStatistics.RbStatisticData statData = null;
            if (mtopStat != null) {
                statData = mtopStat.getRbStatData();
                statData.jsonParseTime = eTime - sTime;
                statData.jsonTime = statData.jsonParseTime;
                statData.isCache = 1;
                statData.mtopReqTime = cacheReturnTime - this.mtopBusiness.sendStartTime;
                statData.rbReqTime = this.mtopBusiness.onBgFinishTime - this.mtopBusiness.reqStartTime;
                statData.totalTime = statData.rbReqTime;
            }
            HandlerParam hMsg = HandlerMgr.getHandlerMsg(this.listener, event, this.mtopBusiness);
            hMsg.pojo = pojo;
            hMsg.mtopResponse = mtopResponse;
            this.mtopBusiness.isCached = true;
            if (this.mtopBusiness.mtopProp.handler != null) {
                if (TBSdkLog.isLogEnable(TBSdkLog.LogEnable.InfoEnable)) {
                    TBSdkLog.i(TAG, seqNo, "onReceive: ON_CACHED in self-defined handler.");
                }
                if (mtopStat != null) {
                    if (statData != null && TBSdkLog.isLogEnable(TBSdkLog.LogEnable.DebugEnable)) {
                        TBSdkLog.d(TAG, seqNo, statData.toString());
                    }
                    mtopStat.commitStatData(true);
                }
                try {
                    if (hMsg.listener instanceof IRemoteCacheListener) {
                        TBSdkLog.i(TAG, seqNo, "listener onCached callback");
                        ((IRemoteCacheListener) hMsg.listener).onCached(event, hMsg.pojo, context);
                        return;
                    }
                    TBSdkLog.i(TAG, seqNo, "listener onCached transfer to onSuccess callback");
                    ((IRemoteListener) hMsg.listener).onSuccess(hMsg.mtopBusiness.getRequestType(), hMsg.mtopResponse, hMsg.pojo, context);
                } catch (Throwable e) {
                    TBSdkLog.e(TAG, seqNo, "listener onCached callback error in self-defined handler.", e);
                }
            } else {
                HandlerMgr.instance().obtainMessage(4, hMsg).sendToTarget();
            }
        }
    }
}
