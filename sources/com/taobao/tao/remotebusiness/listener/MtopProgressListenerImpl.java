package com.taobao.tao.remotebusiness.listener;

import com.taobao.tao.remotebusiness.IRemoteProcessListener;
import com.taobao.tao.remotebusiness.MtopBusiness;
import com.taobao.tao.remotebusiness.handler.HandlerMgr;
import mtopsdk.common.util.TBSdkLog;
import mtopsdk.mtop.common.MtopCallback;
import mtopsdk.mtop.common.MtopHeaderEvent;
import mtopsdk.mtop.common.MtopListener;
import mtopsdk.mtop.common.MtopProgressEvent;

class MtopProgressListenerImpl extends MtopBaseListener implements MtopCallback.MtopProgressListener, MtopCallback.MtopHeaderListener {
    private static final String TAG = "mtopsdk.MtopProgressListenerImpl";

    public MtopProgressListenerImpl(MtopBusiness mtopBusiness, MtopListener listener) {
        super(mtopBusiness, listener);
    }

    public void onDataReceived(MtopProgressEvent event, Object context) {
        String seqNo = this.mtopBusiness.getSeqNo();
        if (TBSdkLog.isLogEnable(TBSdkLog.LogEnable.InfoEnable)) {
            TBSdkLog.i(TAG, seqNo, "Mtop onDataReceived event received.");
        }
        if (this.mtopBusiness.isTaskCanceled()) {
            if (TBSdkLog.isLogEnable(TBSdkLog.LogEnable.InfoEnable)) {
                TBSdkLog.i(TAG, seqNo, "The request of MtopBusiness is cancelled.");
            }
        } else if (!(this.listener instanceof IRemoteProcessListener)) {
        } else {
            if (this.mtopBusiness.mtopProp.handler != null) {
                if (TBSdkLog.isLogEnable(TBSdkLog.LogEnable.InfoEnable)) {
                    TBSdkLog.i(TAG, seqNo, "onReceive: ON_DATA_RECEIVED in self-defined handler.");
                }
                try {
                    ((IRemoteProcessListener) this.listener).onDataReceived(event, context);
                } catch (Throwable e) {
                    TBSdkLog.e(TAG, seqNo, "listener onDataReceived callback error in self-defined handler.", e);
                }
            } else {
                HandlerMgr.instance().obtainMessage(1, HandlerMgr.getHandlerMsg(this.listener, event, this.mtopBusiness)).sendToTarget();
            }
        }
    }

    public void onHeader(MtopHeaderEvent event, Object context) {
        String seqNo = this.mtopBusiness.getSeqNo();
        if (TBSdkLog.isLogEnable(TBSdkLog.LogEnable.InfoEnable)) {
            TBSdkLog.i(TAG, seqNo, "Mtop onHeader event received.");
        }
        if (this.mtopBusiness.isTaskCanceled()) {
            if (TBSdkLog.isLogEnable(TBSdkLog.LogEnable.InfoEnable)) {
                TBSdkLog.i(TAG, seqNo, "The request of MtopBusiness is cancelled.");
            }
        } else if (!(this.listener instanceof IRemoteProcessListener)) {
        } else {
            if (this.mtopBusiness.mtopProp.handler != null) {
                if (TBSdkLog.isLogEnable(TBSdkLog.LogEnable.InfoEnable)) {
                    TBSdkLog.i(TAG, seqNo, "onReceive: ON_HEADER in self-defined handler.");
                }
                try {
                    ((IRemoteProcessListener) this.listener).onHeader(event, context);
                } catch (Throwable e) {
                    TBSdkLog.e(TAG, seqNo, "listener onHeader callback error in self-defined handler.", e);
                }
            } else {
                HandlerMgr.instance().obtainMessage(2, HandlerMgr.getHandlerMsg(this.listener, event, this.mtopBusiness)).sendToTarget();
            }
        }
    }
}
