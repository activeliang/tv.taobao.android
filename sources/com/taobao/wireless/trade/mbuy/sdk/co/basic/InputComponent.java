package com.taobao.wireless.trade.mbuy.sdk.co.basic;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.taobao.wireless.trade.mbuy.sdk.co.Component;
import com.taobao.wireless.trade.mbuy.sdk.engine.BuyEngine;
import com.taobao.wireless.trade.mbuy.sdk.engine.BuyEngineContext;
import com.taobao.wireless.trade.mbuy.sdk.engine.LinkageType;
import com.taobao.wireless.trade.mbuy.sdk.engine.RollbackProtocol;
import com.taobao.wireless.trade.mbuy.sdk.engine.ValidateResult;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class InputComponent extends Component {
    public InputComponent(JSONObject data, BuyEngine engine) {
        super(data, engine);
    }

    public String getName() {
        return this.fields.getString("name");
    }

    public String getTitle() {
        return this.fields.getString("title");
    }

    public String getPlaceholder() {
        return this.fields.getString("placeholder");
    }

    public String getValue() {
        return this.fields.getString("value");
    }

    public void setValue(String value) {
        this.fields.put("value", (Object) value);
    }

    public InputComponentPlugin getPlugin() {
        return InputComponentPlugin.getInputComponentPluginByDesc(this.fields.getString("plugin"));
    }

    public List<InputComponentAttr> getAttr() {
        List<InputComponentAttr> attrList = new ArrayList<>();
        JSONArray attrArray = this.fields.getJSONArray("attr");
        if (attrArray != null && !attrArray.isEmpty()) {
            Iterator<Object> it = attrArray.iterator();
            while (it.hasNext()) {
                try {
                    InputComponentAttr attr = InputComponentAttr.getInputComponentAttrByDesc((String) it.next());
                    if (attr != null) {
                        attrList.add(attr);
                    }
                } catch (Throwable th) {
                }
            }
        }
        return attrList;
    }

    /* access modifiers changed from: protected */
    public String getValidateContent() {
        String value = getValue();
        return value != null ? value : "";
    }

    public ValidateResult validate() {
        String peer = this.fields.getString("peer");
        if (peer == null) {
            return super.validate();
        }
        ValidateResult result = new ValidateResult();
        InputComponent compare = (InputComponent) this.engine.getContext().getIndex().get(peer);
        if (compare == null || compare.getValue() == null || compare.getValue().equals(getValue())) {
            return result;
        }
        result.setValid(false);
        JSONObject validate = this.data.getJSONObject("validate");
        if (validate == null) {
            result.setErrorMsg("输入内容不一致，请重新输入");
            return result;
        }
        JSONArray msgArray = validate.getJSONArray("msg");
        if (msgArray == null || msgArray.isEmpty()) {
            result.setErrorMsg("输入内容不一致，请重新输入");
            return result;
        }
        result.setErrorMsg(msgArray.getString(0));
        return result;
    }

    public void realTimeValidate(String value) {
        if (this.linkageType != LinkageType.REQUEST) {
            setValue(value);
            return;
        }
        setValue(value);
        if (validate().isValid()) {
            this.engine.getContext().setRollbackProtocol(new RollbackProtocol() {
                public void rollback() {
                    InputComponent.this.setValue("");
                }
            });
            notifyLinkageDelegate();
        }
    }

    public JSONObject convertToFinalSubmitData() {
        String value = getValue();
        if (value == null || value.isEmpty()) {
            return null;
        }
        BuyEngineContext context = this.engine.getContext();
        context.addRecoveryEntry(this.fields, "name", getName());
        context.addRecoveryEntry(this.fields, "placeholder", getPlaceholder());
        context.addRecoveryEntry(this.fields, "title", getTitle());
        this.fields.remove("name");
        this.fields.remove("placeholder");
        this.fields.remove("title");
        return super.convertToFinalSubmitData();
    }

    public String toString() {
        return super.toString() + " - InputComponent [name=" + getName() + ", value=" + getValue() + ", placeholder=" + getPlaceholder() + ", plugin=" + getPlugin() + "]";
    }
}
