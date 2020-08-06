package com.tvtaobao.voicesdk.listener;

import com.tvtaobao.voicesdk.bean.DomainResultVo;
import com.tvtaobao.voicesdk.bean.PageReturn;

public interface ASRHandler {
    PageReturn onASRNotify(DomainResultVo domainResultVo);
}
