package com.yunos.tvtaobao.biz.request.item;

import android.text.TextUtils;
import com.ali.user.open.tbauth.TbAuthConstants;
import com.yunos.tv.core.CoreApplication;
import com.yunos.tv.core.config.Config;
import com.yunos.tv.core.config.TvOptionsConfig;
import com.yunos.tv.core.util.NetWorkUtil;
import com.yunos.tvtaobao.biz.common.BaseConfig;
import com.yunos.tvtaobao.biz.request.base.BaseMtopRequest;
import com.yunos.tvtaobao.biz.request.bo.GoodsSearchResultDo;
import com.yunos.tvtaobao.biz.request.info.GlobalConfigInfo;
import com.zhiping.dev.android.logger.ZpLogger;
import java.util.List;
import java.util.Map;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class SearchResultRequest extends BaseMtopRequest {
    private String API = "mtop.taobao.tvtao.tvtaosearchservice.getbyq2";
    private String cateId = null;
    private String flag = null;
    private boolean isTmail;
    private String item_ids = null;
    private int n = 40;
    private int per = 4;
    private String q = "";
    private int s = 1;
    private String sort = null;
    private String version = "1.0";
    private int ztcc = 0;

    public SearchResultRequest(List<String> list, boolean isFromCartToBuildOrder, boolean isFromBuildOrder, boolean isTmail2) {
        this.isTmail = isTmail2;
        String tvOptions = TvOptionsConfig.getTvOptions();
        if (!TextUtils.isEmpty(tvOptions)) {
            if (isFromCartToBuildOrder) {
                String tvOptionsSubstring = tvOptions.substring(0, tvOptions.length() - 1);
                addParams("tvOptions", tvOptionsSubstring + "1");
                ZpLogger.v(this.TAG, "tvOptions = " + tvOptionsSubstring + "1");
            } else {
                addParams("tvOptions", tvOptions);
                ZpLogger.v(this.TAG, "tvOptions = " + tvOptions);
            }
        }
        String appKey = Config.getChannel();
        if (!TextUtils.isEmpty(appKey)) {
            ZpLogger.e(this.TAG, "appKey = " + appKey);
            addParams("appKey", appKey);
        }
        addParams("mjf", "true");
        JSONArray array = new JSONArray();
        for (int i = 0; i < list.size(); i++) {
            array.put(list.get(i));
        }
        ZpLogger.v(this.TAG, "traceRoutes =" + array);
        addParams("traceRoutes", array.toString());
        JSONObject object = new JSONObject();
        try {
            object.put("umToken", Config.getUmtoken(CoreApplication.getApplication()));
            object.put("wua", Config.getWua(CoreApplication.getApplication()));
            object.put("isSimulator", Config.isSimulator(CoreApplication.getApplication()));
            object.put("userAgent", Config.getAndroidSystem(CoreApplication.getApplication()));
            object.put("business", "search");
        } catch (JSONException e) {
            e.printStackTrace();
        }
        addParams("extParams", object.toString());
    }

    /* access modifiers changed from: protected */
    public Map<String, String> getAppData() {
        boolean supportZtc = false;
        if (this.s < 0) {
            this.s = 0;
        }
        if (this.s > 100) {
            this.s = 100;
        }
        if (this.n <= 0) {
            this.n = 40;
        }
        if (this.n > 100) {
            this.n = 100;
        }
        addParams("s", String.valueOf(this.s));
        addParams("n", String.valueOf(this.n));
        if (GlobalConfigInfo.getInstance().getGlobalConfig() == null || !GlobalConfigInfo.getInstance().getGlobalConfig().isBeta()) {
            supportZtc = true;
        }
        if (!TextUtils.isEmpty(this.flag) && supportZtc) {
            addParams("flag", this.flag);
        }
        if (!TextUtils.isEmpty(this.item_ids)) {
            addParams(BaseConfig.NID_FROM_CART, this.item_ids);
        } else if (!TextUtils.isEmpty(this.q)) {
            addParams("q", this.q);
        }
        if (!TextUtils.isEmpty(this.cateId)) {
            addParams("cateId", this.cateId);
        }
        if (this.per > 0) {
            addParams("per", "" + this.per);
        }
        if (!TextUtils.isEmpty(NetWorkUtil.getIpAddress())) {
            addParams(TbAuthConstants.IP, NetWorkUtil.getIpAddress());
        }
        try {
            if (!TextUtils.isEmpty(this.sort)) {
                JSONObject jsonObject = new JSONObject();
                jsonObject.put("sort", this.sort);
                if (this.isTmail) {
                    jsonObject.put("user_type", "1");
                }
                addParams("sortexts", jsonObject.toString());
                return null;
            }
            JSONObject jsonObject2 = new JSONObject();
            if (this.isTmail) {
                jsonObject2.put("user_type", "1");
            }
            addParams("sortexts", jsonObject2.toString());
            return null;
        } catch (JSONException e) {
            e.printStackTrace();
            return null;
        }
    }

    public void setCateId(String cateId2) {
        this.cateId = cateId2;
    }

    public void setPer(int per2) {
        this.per = per2;
    }

    public int getPer() {
        return this.per;
    }

    public void setZtcc(int ztcc2) {
        this.ztcc = ztcc2;
    }

    public void setFlag(String flag2) {
        this.flag = flag2;
    }

    /* access modifiers changed from: protected */
    public String getApi() {
        return this.API;
    }

    /* access modifiers changed from: protected */
    public String getApiVersion() {
        return this.version;
    }

    public boolean getNeedEcode() {
        return false;
    }

    public boolean getNeedSession() {
        return false;
    }

    public GoodsSearchResultDo resolveResponse(JSONObject obj) throws Exception {
        return GoodsSearchResultDo.resolveFromJson(obj);
    }

    public void setPageNo(int page_no) {
        this.s = page_no;
    }

    public void setPageSize(int page_size) {
        this.n = page_size;
    }

    public void setQueryKW(String q2) {
        this.q = q2;
    }

    public void setItemIds(String item_ids2) {
        this.item_ids = item_ids2;
    }

    public void setSort(String sort2) {
        this.sort = sort2;
    }
}
