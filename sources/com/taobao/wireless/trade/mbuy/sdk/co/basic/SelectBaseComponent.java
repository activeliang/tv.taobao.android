package com.taobao.wireless.trade.mbuy.sdk.co.basic;

import android.text.TextUtils;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.taobao.wireless.trade.mbuy.sdk.co.Component;
import com.taobao.wireless.trade.mbuy.sdk.engine.BuyEngine;
import com.taobao.wireless.trade.mbuy.sdk.engine.BuyEngineContext;
import com.taobao.wireless.trade.mbuy.sdk.engine.LinkageType;
import com.taobao.wireless.trade.mbuy.sdk.engine.RollbackProtocol;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public abstract class SelectBaseComponent<T> extends Component {
    protected List<T> options = loadOptions(this.fields.getJSONArray("options"));

    /* access modifiers changed from: protected */
    public abstract String getOptionId(T t);

    /* access modifiers changed from: protected */
    public abstract T newOption(JSONObject jSONObject) throws Exception;

    public SelectBaseComponent(JSONObject data, BuyEngine engine) {
        super(data, engine);
    }

    public void reload(JSONObject data) {
        super.reload(data);
        try {
            this.options = loadOptions(this.fields.getJSONArray("options"));
        } catch (Throwable th) {
        }
    }

    /* access modifiers changed from: protected */
    public String getValidateContent() {
        String selectedId = getSelectedId();
        return selectedId != null ? selectedId : "";
    }

    private List<T> loadOptions(JSONArray optionJSONArray) {
        if (optionJSONArray == null) {
            throw new IllegalStateException();
        }
        List<T> options2 = new ArrayList<>(optionJSONArray.size());
        Iterator<Object> it = optionJSONArray.iterator();
        while (it.hasNext()) {
            T option = null;
            try {
                option = newOption((JSONObject) it.next());
            } catch (Throwable th) {
            }
            if (option != null) {
                options2.add(option);
            }
        }
        return options2;
    }

    public void setSelectedId(String selectedId) {
        setSelectedId(selectedId, false);
    }

    public void setSelectedId(String selectedId, boolean refreshStructure) {
        if (selectedId != null && !selectedId.equals(getSelectedId())) {
            BuyEngineContext context = this.engine.getContext();
            if (getLinkageType() == LinkageType.REQUEST) {
                final String originalSelectedId = getSelectedId();
                context.setRollbackProtocol(new RollbackProtocol() {
                    public void rollback() {
                        SelectBaseComponent.this.fields.put("selectedId", (Object) originalSelectedId);
                    }
                });
            }
            this.fields.put("selectedId", (Object) selectedId);
            notifyLinkageDelegate(refreshStructure);
        }
    }

    public T getOptionById(String id) {
        if (TextUtils.isEmpty(id)) {
            return null;
        }
        for (T option : this.options) {
            if (id.equals(getOptionId(option))) {
                return option;
            }
        }
        return null;
    }

    public T getSelectedOption() {
        return getOptionById(getSelectedId());
    }

    public String getSelectedId() {
        return this.fields.getString("selectedId");
    }

    public String getTitle() {
        return this.fields.getString("title");
    }

    public List<T> getOptions() {
        return this.options;
    }

    public String getUrl() {
        return this.fields.getString("url");
    }

    public String getOptionWarn() {
        return this.fields.getString("optionWarn");
    }
}
