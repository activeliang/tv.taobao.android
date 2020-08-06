package com.taobao.wireless.detail.api;

import com.taobao.detail.clientDomain.TBDetailResultVO;
import com.taobao.detail.domain.base.Unit;

public interface DetailApiRequestor {
    TBDetailResultVO syncRequest(Unit unit);
}
