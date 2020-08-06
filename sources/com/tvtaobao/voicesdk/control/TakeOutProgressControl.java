package com.tvtaobao.voicesdk.control;

import android.app.Service;
import android.content.Intent;
import android.text.TextUtils;
import com.tvtaobao.voicesdk.bean.ConfigVO;
import com.tvtaobao.voicesdk.bean.DomainResultVo;
import com.tvtaobao.voicesdk.control.base.BizBaseControl;
import com.tvtaobao.voicesdk.utils.ActivityUtil;
import com.tvtaobao.voicesdk.utils.TTSUtils;
import com.yunos.tv.alitvasrsdk.CommonData;
import com.yunos.tv.core.config.AppInfo;
import com.yunos.tv.core.util.Utils;
import com.yunos.tv.tao.speech.client.domain.result.multisearch.takeout.TakeoutProgressResultVO;
import java.util.Map;

public class TakeOutProgressControl extends BizBaseControl {
    public void execute(DomainResultVo domainResultVO) {
        TakeoutProgressResultVO takeoutProgressResultVO = (TakeoutProgressResultVO) domainResultVO.getResultVO();
        if (takeoutProgressResultVO != null && takeoutProgressResultVO.getTbMainOrderId() != null) {
            Intent intent = new Intent();
            intent.setClassName(AppInfo.getPackageName(), "com.yunos.voice.activity.OrderDeliveryActivity");
            intent.putExtra("data", takeoutProgressResultVO);
            intent.setFlags(268435456);
            ((Service) mWeakService.get()).startActivity(intent);
        } else if (!TextUtils.isEmpty(domainResultVO.getToUri())) {
            gotoActivity(domainResultVO.getToUri());
        } else {
            TTSUtils.getInstance().showDialog(ActivityUtil.getTopActivity());
        }
        Map<String, String> paramProgress = Utils.getProperties();
        paramProgress.put(CommonData.TYPE_ASR, ConfigVO.asr_text);
        paramProgress.put("tts", domainResultVO.getSpoken());
        Utils.utCustomHit("Voice_FoodCheck_order", paramProgress);
    }
}
