package com.yunos.tvtaobao.biz.request.item;

import com.yunos.tv.core.common.RequestListener;
import com.yunos.tvtaobao.biz.request.BusinessRequest;
import com.yunos.tvtaobao.biz.request.base.BaseMtopRequest;
import com.yunos.tvtaobao.biz.request.bo.DeviceBo;

public class TvTaobaoBusinessRequest extends BusinessRequest {
    public static final Long RECOMMEND_CATEGORY_ID = 0L;
    private static TvTaobaoBusinessRequest mBusinessRequest;

    private TvTaobaoBusinessRequest() {
    }

    public static TvTaobaoBusinessRequest getBusinessRequest() {
        if (mBusinessRequest == null) {
            mBusinessRequest = new TvTaobaoBusinessRequest();
        }
        return mBusinessRequest;
    }

    public void destory() {
        mBusinessRequest = null;
    }

    public void requestDevice(String model, RequestListener<DeviceBo> listener) {
        baseRequest((BaseMtopRequest) new GetDeviceRequest(model), listener, false);
    }
}
