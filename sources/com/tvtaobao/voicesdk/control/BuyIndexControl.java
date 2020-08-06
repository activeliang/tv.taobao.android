package com.tvtaobao.voicesdk.control;

import com.tvtaobao.voicesdk.bean.CommandReturn;
import com.tvtaobao.voicesdk.bean.ConfigVO;
import com.tvtaobao.voicesdk.bean.DomainResultVo;
import com.tvtaobao.voicesdk.control.base.BizBaseControl;
import com.tvtaobao.voicesdk.listener.VoiceListener;
import com.tvtaobao.voicesdk.utils.LogPrint;
import com.yunos.tv.tao.speech.client.domain.result.multisearch.general.BuyIndexResultVO;

public class BuyIndexControl extends BizBaseControl {
    public void execute(DomainResultVo domainResultVO) {
        setActionIndex(((BuyIndexResultVO) domainResultVO.getResultVO()).getNorm());
    }

    public void setActionIndex(String pos) {
        LogPrint.i(this.TAG, this.TAG + ".WeakListener : " + this.mWeakListener + " ,VoiceListener : " + this.mWeakListener.get());
        if (this.mWeakListener == null || this.mWeakListener.get() == null) {
            notDeal();
            return;
        }
        CommandReturn commandReturn = new CommandReturn();
        commandReturn.mIsHandled = true;
        commandReturn.mASRMessage = ConfigVO.asr_text;
        commandReturn.mAction = 1004;
        commandReturn.mData = "{\"index\":" + pos + "}";
        ((VoiceListener) this.mWeakListener.get()).callback(commandReturn);
    }
}
