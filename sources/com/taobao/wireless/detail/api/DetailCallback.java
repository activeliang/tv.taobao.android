package com.taobao.wireless.detail.api;

import com.taobao.detail.clientDomain.TBDetailResultVO;

public interface DetailCallback {
    void onApiError();

    void onApiSuccess(TBDetailResultVO tBDetailResultVO);
}
