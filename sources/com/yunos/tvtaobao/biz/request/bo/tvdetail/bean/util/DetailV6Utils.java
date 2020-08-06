package com.yunos.tvtaobao.biz.request.bo.tvdetail.bean.util;

import com.alibaba.fastjson.JSON;
import com.google.gson.reflect.TypeToken;
import com.yunos.tv.core.util.GsonUtil;
import com.yunos.tvtaobao.biz.request.bo.tvdetail.bean.TBDetailResultV6;
import com.yunos.tvtaobao.biz.request.bo.tvdetail.bean.taobao.Unit;
import org.json.JSONObject;

public class DetailV6Utils {
    public static Unit getUnit(TBDetailResultV6 tbDetailResultV6) {
        try {
            return (Unit) JSON.parseObject(tbDetailResultV6.getApiStack().get(0).getValue(), Unit.class);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public static JSONObject getUnitJsonObject(TBDetailResultV6 tbDetailResultV6) {
        try {
            return new JSONObject(tbDetailResultV6.getApiStack().get(0).getValue());
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public static MockData getMockdata(TBDetailResultV6 tbDetailResultV6) {
        if (tbDetailResultV6 == null || tbDetailResultV6.getMockData() == null) {
            return null;
        }
        return (MockData) GsonUtil.parseJson(tbDetailResultV6.getMockData(), new TypeToken<MockData>() {
        });
    }
}
