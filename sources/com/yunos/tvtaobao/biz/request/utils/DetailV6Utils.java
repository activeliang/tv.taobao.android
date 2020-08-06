package com.yunos.tvtaobao.biz.request.utils;

import com.alibaba.fastjson.JSON;
import com.yunos.tvtaobao.biz.request.bo.MockData;
import com.yunos.tvtaobao.biz.request.bo.ResourceBean;
import com.yunos.tvtaobao.biz.request.bo.TBDetailResultV6;
import com.yunos.tvtaobao.biz.request.bo.Unit;
import com.zhiping.dev.android.logger.ZpLogger;
import java.util.List;
import org.json.JSONException;
import org.json.JSONObject;

public class DetailV6Utils {
    public static Unit getUnit(TBDetailResultV6 tbDetailResultV6) {
        if (tbDetailResultV6.getApiStack() != null) {
            List<TBDetailResultV6.ApiStackBean> apiStack = tbDetailResultV6.getApiStack();
            if (apiStack.get(0) != null) {
                return (Unit) JSON.parseObject(apiStack.get(0).getValue(), Unit.class);
            }
        }
        return null;
    }

    public static ResourceBean getResBean(String s) {
        try {
            JSONObject jsonObject = new JSONObject(s);
            try {
                ResourceBean resourceBean = (ResourceBean) JSON.parseObject(jsonObject.getJSONObject("resource").toString(), ResourceBean.class);
                ZpLogger.e("detail", "resourceBean成功");
                JSONObject jSONObject = jsonObject;
                return resourceBean;
            } catch (JSONException e) {
                e = e;
                JSONObject jSONObject2 = jsonObject;
                e.printStackTrace();
                return null;
            }
        } catch (JSONException e2) {
            e = e2;
            e.printStackTrace();
            return null;
        }
    }

    public static MockData getMockdata(TBDetailResultV6 tbDetailResultV6) {
        if (tbDetailResultV6 == null || tbDetailResultV6.getMockData() == null) {
            return null;
        }
        return (MockData) JSON.parseObject(tbDetailResultV6.getMockData(), MockData.class);
    }
}
