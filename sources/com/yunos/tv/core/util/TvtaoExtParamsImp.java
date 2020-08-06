package com.yunos.tv.core.util;

import com.yunos.tv.core.config.Config;
import com.yunos.tvtaobao.payment.utils.TvtaoExtParamsUtil;

public class TvtaoExtParamsImp implements TvtaoExtParamsUtil.ExtParamsInterface {
    public String getExtParams() {
        return Config.getExtParams();
    }
}
