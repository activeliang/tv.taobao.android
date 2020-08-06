package com.yunos.tvtaobao.biz.request.item;

import android.text.TextUtils;
import com.ali.user.open.tbauth.TbAuthConstants;
import com.yunos.tvtaobao.biz.common.BaseConfig;
import com.yunos.tvtaobao.biz.request.base.BaseMtopRequest;
import com.yunos.tvtaobao.biz.request.bo.GoodsSearchZtcResult;
import com.yunos.tvtaobao.biz.request.info.GlobalConfigInfo;
import java.util.Map;
import org.json.JSONException;
import org.json.JSONObject;

public class SearchRequestMtopZtc extends BaseMtopRequest {
    private String API = "mtop.taobao.tvtao.tvtaosearchservice.getbyq";
    private String cateId = null;
    private String flag = null;
    private String ip = null;
    private String item_ids = null;
    private int n = 40;
    private int per = 4;
    private String q = "";
    private int s = 1;
    private String sort = null;
    private String version = "1.0";
    private int ztcc = 0;

    public void setSort(String sort2) {
        this.sort = sort2;
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
        if (!TextUtils.isEmpty(this.ip)) {
            addParams(TbAuthConstants.IP, this.ip);
        }
        addParams("ztcC", "" + this.ztcc);
        if (TextUtils.isEmpty(this.sort)) {
            return null;
        }
        try {
            JSONObject jsonObject = new JSONObject();
            jsonObject.put("sort", this.sort);
            addParams("sortexts", jsonObject.toString());
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

    public GoodsSearchZtcResult resolveResponse(JSONObject obj) throws Exception {
        return GoodsSearchZtcResult.resolveFromJson(obj);
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

    public void setIp(String ip2) {
        this.ip = ip2;
    }

    public String getIp() {
        return this.ip;
    }

    public void setItemIds(String item_ids2) {
        this.item_ids = item_ids2;
    }
}
