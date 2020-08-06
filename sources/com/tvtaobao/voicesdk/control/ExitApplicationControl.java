package com.tvtaobao.voicesdk.control;

import android.app.Service;
import android.content.Intent;
import com.tvtaobao.voicesdk.bean.DomainResultVo;
import com.tvtaobao.voicesdk.control.base.BizBaseControl;
import com.tvtaobao.voicesdk.utils.LogPrint;

public class ExitApplicationControl extends BizBaseControl {
    public void execute(DomainResultVo domainResultVO) {
        LogPrint.e(this.TAG, "dealASR Exit TVTaobao!");
        alreadyDeal("欢迎下次光临电视淘宝～");
        Intent intent = new Intent();
        intent.setAction("com.yunos.tvtaobao.exit.application");
        ((Service) mWeakService.get()).sendOrderedBroadcast(intent, (String) null);
    }
}
