package com.taobao.tao.remotebusiness.listener;

import com.taobao.tao.remotebusiness.IRemoteParserListener;
import com.taobao.tao.remotebusiness.MtopBusiness;
import com.taobao.tao.remotebusiness.handler.HandlerMgr;
import com.taobao.tao.remotebusiness.handler.HandlerParam;
import mtopsdk.common.util.TBSdkLog;
import mtopsdk.mtop.common.MtopCallback;
import mtopsdk.mtop.common.MtopFinishEvent;
import mtopsdk.mtop.common.MtopListener;
import mtopsdk.mtop.domain.MtopResponse;
import mtopsdk.mtop.util.MtopConvert;
import mtopsdk.mtop.util.MtopStatistics;

class MtopFinishListenerImpl extends MtopBaseListener implements MtopCallback.MtopFinishListener {
    private static final String TAG = "mtopsdk.MtopFinishListenerImpl";

    public MtopFinishListenerImpl(MtopBusiness mtopBusiness, MtopListener listener) {
        super(mtopBusiness, listener);
    }

    public void onFinished(MtopFinishEvent event, Object context) {
        String seqNo = this.mtopBusiness.getSeqNo();
        if (TBSdkLog.isLogEnable(TBSdkLog.LogEnable.InfoEnable)) {
            TBSdkLog.i(TAG, seqNo, "Mtop onFinished event received.");
        }
        if (this.mtopBusiness.isTaskCanceled()) {
            if (TBSdkLog.isLogEnable(TBSdkLog.LogEnable.InfoEnable)) {
                TBSdkLog.i(TAG, seqNo, "The request of MtopBusiness is canceled.");
            }
        } else if (this.listener == null) {
            TBSdkLog.e(TAG, seqNo, "The listener of MtopBusiness is null.");
        } else if (event == null) {
            TBSdkLog.e(TAG, seqNo, "MtopFinishEvent is null.");
        } else {
            MtopResponse mtopResponse = event.getMtopResponse();
            if (mtopResponse == null) {
                TBSdkLog.e(TAG, seqNo, "The MtopResponse of MtopFinishEvent is null.");
                return;
            }
            long sendEndTime = System.currentTimeMillis();
            if (this.listener instanceof IRemoteParserListener) {
                try {
                    ((IRemoteParserListener) this.listener).parseResponse(mtopResponse);
                } catch (Exception e) {
                    TBSdkLog.e(TAG, seqNo, "listener parseResponse callback error.", e);
                }
            }
            HandlerParam hMsg = HandlerMgr.getHandlerMsg(this.listener, event, this.mtopBusiness);
            hMsg.mtopResponse = mtopResponse;
            long parseStartTime = System.currentTimeMillis();
            long parseEndTime = parseStartTime;
            if (mtopResponse.isApiSuccess() && this.mtopBusiness.clazz != null) {
                hMsg.pojo = MtopConvert.mtopResponseToOutputDO(mtopResponse, this.mtopBusiness.clazz);
                parseEndTime = System.currentTimeMillis();
            }
            this.mtopBusiness.onBgFinishTime = parseEndTime;
            MtopStatistics mtopStat = mtopResponse.getMtopStat();
            MtopStatistics.RbStatisticData statData = null;
            if (mtopStat != null) {
                statData = mtopStat.getRbStatData();
                statData.beforeReqTime = this.mtopBusiness.sendStartTime - this.mtopBusiness.reqStartTime;
                statData.mtopReqTime = sendEndTime - this.mtopBusiness.sendStartTime;
                statData.afterReqTime = this.mtopBusiness.onBgFinishTime - sendEndTime;
                statData.parseTime = parseStartTime - sendEndTime;
                statData.jsonParseTime = parseEndTime - parseStartTime;
                statData.jsonTime = statData.jsonParseTime;
                statData.rbReqTime = this.mtopBusiness.onBgFinishTime - this.mtopBusiness.reqStartTime;
                statData.totalTime = statData.rbReqTime;
            }
            if (this.mtopBusiness.mtopProp.handler != null) {
                if (TBSdkLog.isLogEnable(TBSdkLog.LogEnable.InfoEnable)) {
                    TBSdkLog.i(TAG, seqNo, "onReceive: ON_FINISHED in self-defined handler.");
                }
                long sTime = System.currentTimeMillis();
                hMsg.mtopBusiness.doFinish(hMsg.mtopResponse, hMsg.pojo);
                if (TBSdkLog.isLogEnable(TBSdkLog.LogEnable.InfoEnable)) {
                    long dataSize = 0;
                    if (hMsg.mtopResponse.getBytedata() != null) {
                        dataSize = (long) hMsg.mtopResponse.getBytedata().length;
                    }
                    StringBuilder sb = new StringBuilder(128);
                    sb.append("onReceive: ON_FINISHED in self-defined handler.").append("doFinishTime=").append(System.currentTimeMillis() - sTime).append(", dataSize=").append(dataSize).append("; ");
                    if (statData != null) {
                        sb.append(statData.toString());
                    }
                    TBSdkLog.i(TAG, seqNo, sb.toString());
                }
                if (mtopStat != null) {
                    mtopStat.commitStatData(true);
                    return;
                }
                return;
            }
            HandlerMgr.instance().obtainMessage(3, hMsg).sendToTarget();
        }
    }
}
