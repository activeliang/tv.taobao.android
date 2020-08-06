package com.yunos.tvtaobao.biz.request.bo;

import com.yunos.tvtaobao.biz.request.bo.PropPath;
import java.io.Serializable;
import java.util.List;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class Sku implements Serializable {
    private static final long serialVersionUID = 4037393397654866350L;
    private Prop[] props;
    private SkuItem[] skuItems;

    public SkuItem[] getSkuItems() {
        return this.skuItems;
    }

    public void setSkuItems(SkuItem[] skuItems2) {
        this.skuItems = skuItems2;
    }

    public Prop[] getProps() {
        return this.props;
    }

    public void setProps(Prop[] props2) {
        this.props = props2;
    }

    public Prop getPropById(Long id) {
        if (this.props == null || this.props.length == 0) {
            return null;
        }
        for (Prop p : this.props) {
            if (p.getPropId().longValue() == id.longValue()) {
                return p;
            }
        }
        return null;
    }

    public Long getSkuId(List<PropPath.Pvid> selectedPvids) {
        if (selectedPvids.size() != this.props.length) {
            throw new RuntimeException("非法的调用,没有选择完SKU");
        }
        for (SkuItem item : this.skuItems) {
            if (item.contains(selectedPvids, (Long) null)) {
                return item.getSkuId();
            }
        }
        return null;
    }

    public static Sku resolveFromMTOP(JSONObject obj) throws JSONException {
        if (obj == null) {
            return null;
        }
        Sku s = new Sku();
        if (!obj.isNull("skus")) {
            JSONArray array = obj.getJSONArray("skus");
            SkuItem[] temp = new SkuItem[array.length()];
            for (int i = 0; i < array.length(); i++) {
                temp[i] = SkuItem.resolveFromMTOP(array.getJSONObject(i));
            }
            s.setSkuItems(temp);
        }
        if (obj.isNull("props")) {
            return s;
        }
        JSONArray array2 = obj.getJSONArray("props");
        Prop[] temp2 = new Prop[array2.length()];
        for (int i2 = 0; i2 < array2.length(); i2++) {
            temp2[i2] = Prop.resolveFromMTOP(array2.getJSONObject(i2));
        }
        s.setProps(temp2);
        return s;
    }
}
