package com.tvtaobao.voicesdk.control;

import android.app.Service;
import android.content.Intent;
import android.net.Uri;
import com.tvtaobao.voicesdk.bean.DomainResultVo;
import com.tvtaobao.voicesdk.control.base.BizBaseControl;
import com.yunos.tv.tao.speech.client.domain.result.multisearch.trade.LogisticsResultVO;

public class LogisticsControl extends BizBaseControl {
    public void execute(DomainResultVo domainResultVO) {
        LogisticsResultVO logisticsResultVO = (LogisticsResultVO) domainResultVO.getResultVO();
        logisticsResultVO.setSpoken(domainResultVO.getSpoken());
        logisticsResultVO.setTips(domainResultVO.getTips());
        Intent intent = new Intent();
        intent.setData(Uri.parse("tvtaobao://voice?module=logisticsquery"));
        intent.putExtra("data", logisticsResultVO);
        intent.setFlags(268435456);
        ((Service) mWeakService.get()).startActivity(intent);
    }
}
