package com.tvtaobao.voicesdk.control;

import android.app.Service;
import android.content.Intent;
import com.alibaba.fastjson.JSON;
import com.tvtaobao.voicesdk.base.SDKInitConfig;
import com.tvtaobao.voicesdk.bean.CommandReturn;
import com.tvtaobao.voicesdk.bean.ConfigVO;
import com.tvtaobao.voicesdk.bean.DomainResultVo;
import com.tvtaobao.voicesdk.control.base.BizBaseControl;
import com.tvtaobao.voicesdk.listener.VoiceListener;
import com.tvtaobao.voicesdk.utils.IntentSearchData;
import com.tvtaobao.voicesdk.utils.LogPrint;
import com.yunos.tv.core.config.AppInfo;
import com.yunos.tv.tao.speech.client.domain.result.multisearch.integration.SearchIntegrationResultVO;

public class FusionSearchControl extends BizBaseControl {
    private String TAG = "FusionSearchControl";

    public void execute(DomainResultVo domainResultVO) {
        LogPrint.i(this.TAG, this.TAG + ".execute");
        SearchIntegrationResultVO searchIntegrationResultVO = (SearchIntegrationResultVO) domainResultVO.getResultVO();
        LogPrint.i(this.TAG, this.TAG + ".execute showUI : " + SDKInitConfig.needShowUI());
        searchIntegrationResultVO.setSpoken(domainResultVO.getSpoken());
        searchIntegrationResultVO.setTips(domainResultVO.getTips());
        if (SDKInitConfig.needShowUI()) {
            Intent intent = new Intent();
            intent.setClassName(AppInfo.getPackageName(), "com.yunos.voice.activity.FusionSearchActivity");
            IntentSearchData.getIntence().setSearchIntegrationResultVO(searchIntegrationResultVO);
            intent.putExtra("keywords", searchIntegrationResultVO.getKeywords());
            intent.setFlags(402653184);
            ((Service) mWeakService.get()).startActivity(intent);
        } else if (SDKInitConfig.sdkVersion > 1010407) {
            String resultVO = JSON.toJSONString(searchIntegrationResultVO);
            CommandReturn commandReturn = new CommandReturn();
            commandReturn.mIsHandled = true;
            commandReturn.mAction = 1009;
            commandReturn.mASRMessage = ConfigVO.asr_text;
            commandReturn.mData = resultVO;
            ((VoiceListener) this.mWeakListener.get()).callback(commandReturn);
        } else {
            ((VoiceListener) this.mWeakListener.get()).searchResult(JSON.toJSONString(searchIntegrationResultVO.getSearchItemDO()));
        }
    }
}
