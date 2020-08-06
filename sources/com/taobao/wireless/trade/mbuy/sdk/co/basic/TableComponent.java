package com.taobao.wireless.trade.mbuy.sdk.co.basic;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.taobao.wireless.trade.mbuy.sdk.co.Component;
import com.taobao.wireless.trade.mbuy.sdk.engine.BuyEngine;

public class TableComponent extends Component {
    private JSONArray meta = this.fields.getJSONArray("meta");

    public TableComponent(JSONObject data, BuyEngine engine) {
        super(data, engine);
        if (this.meta == null) {
            throw new IllegalStateException();
        }
    }

    public void reload(JSONObject data) {
        super.reload(data);
        JSONArray meta2 = this.fields.getJSONArray("meta");
        if (meta2 != null) {
            this.meta = meta2;
        }
    }

    public int getColumnNum(int row) {
        if (row >= getRowNum()) {
            return 0;
        }
        return ((JSONArray) this.meta.get(row)).size();
    }

    public int getRowNum() {
        return this.meta.size();
    }

    public TableSlot getSlot(int row, int col) {
        if (row >= getRowNum() || col > getColumnNum(row)) {
            return null;
        }
        return new TableSlot((JSONObject) ((JSONArray) this.meta.get(row)).get(col));
    }
}
