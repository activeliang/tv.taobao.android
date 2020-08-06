package com.tvtaobao.voicesdk.control;

import android.app.Service;
import android.content.Intent;
import android.net.Uri;
import com.tvtaobao.voicesdk.bean.DomainResultVo;
import com.tvtaobao.voicesdk.control.base.BizBaseControl;
import com.yunos.tv.tao.speech.client.domain.result.multisearch.trade.BillResultVO;

public class CheckBillControl extends BizBaseControl {
    public void execute(DomainResultVo domainResultVO) {
        BillResultVO billResultVO = (BillResultVO) domainResultVO.getResultVO();
        billResultVO.setSpoken(domainResultVO.getSpoken());
        billResultVO.setTips(domainResultVO.getTips());
        Intent intent = new Intent();
        intent.setData(Uri.parse("tvtaobao://voice?module=billquery"));
        intent.putExtra("data", billResultVO);
        intent.setFlags(268435456);
        ((Service) mWeakService.get()).startActivity(intent);
    }
}
