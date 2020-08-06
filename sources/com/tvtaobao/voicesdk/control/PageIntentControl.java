package com.tvtaobao.voicesdk.control;

import android.os.Handler;
import android.text.TextUtils;
import com.tvtaobao.voicesdk.base.SDKInitConfig;
import com.tvtaobao.voicesdk.bean.DomainResultVo;
import com.tvtaobao.voicesdk.control.base.BizBaseControl;
import com.tvtaobao.voicesdk.type.DomainType;
import com.tvtaobao.voicesdk.utils.ActivityUtil;
import com.tvtaobao.voicesdk.utils.LogPrint;
import com.tvtaobao.voicesdk.utils.TTSUtils;
import com.yunos.tv.core.util.Utils;

public class PageIntentControl extends BizBaseControl {
    public void execute(DomainResultVo domainResultVO) {
        if (TextUtils.isEmpty(domainResultVO.getToUri())) {
            return;
        }
        if (!DomainType.TAKEOUT_GOTO_INDEX.equals(domainResultVO.getIntent()) || !"TtCommon_tvtaobao-waimai".equals(SDKInitConfig.getCurrentPage())) {
            LogPrint.i("TVTao_" + this.TAG, this.TAG + ".PageIntentControl");
            gotoActivity(domainResultVO.getToUri());
            Utils.utCustomHit("Voice_jump", getProperties());
            return;
        }
        new Handler().post(new Runnable() {
            public void run() {
                TTSUtils.getInstance().showDialog(ActivityUtil.getTopActivity());
            }
        });
    }
}
