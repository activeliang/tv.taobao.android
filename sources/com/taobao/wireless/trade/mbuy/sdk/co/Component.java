package com.taobao.wireless.trade.mbuy.sdk.co;

import android.util.SparseArray;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.taobao.wireless.trade.mbuy.sdk.engine.BuyEngine;
import com.taobao.wireless.trade.mbuy.sdk.engine.LinkageAction;
import com.taobao.wireless.trade.mbuy.sdk.engine.LinkageDelegate;
import com.taobao.wireless.trade.mbuy.sdk.engine.LinkageNotification;
import com.taobao.wireless.trade.mbuy.sdk.engine.LinkageType;
import com.taobao.wireless.trade.mbuy.sdk.engine.ValidateResult;
import java.util.regex.Pattern;

public abstract class Component {
    protected JSONObject data;
    protected BuyEngine engine;
    protected SparseArray<Object> extensions;
    protected JSONObject fields;
    protected LinkageType linkageType = LinkageType.REFRESH;
    protected Component parent;
    protected ComponentStatus status = ComponentStatus.NORMAL;
    protected Object storage;
    protected ComponentType type;

    public Component() {
    }

    protected Component(JSONObject data2, BuyEngine engine2) {
        this.engine = engine2;
        init(data2);
    }

    public void reload(JSONObject data2) {
        init(data2);
    }

    private void init(JSONObject data2) {
        if (data2 == null) {
            throw new IllegalArgumentException();
        }
        this.data = data2;
        JSONObject fields2 = data2.getJSONObject("fields");
        if (fields2 == null) {
            throw new IllegalArgumentException();
        }
        this.fields = fields2;
        this.type = ComponentType.getComponentTypeByDesc(this.data.getString("type"));
        this.status = ComponentStatus.getComponentStatusByDesc(this.data.getString("status"));
        this.storage = null;
        this.extensions = null;
    }

    public JSONObject convertToAsyncSubmitData() {
        return this.data;
    }

    public JSONObject convertToFinalSubmitData() {
        return this.data;
    }

    public void notifyLinkageDelegate() {
        notifyLinkageDelegate(false);
    }

    public void notifyLinkageDelegate(boolean refreshStructure) {
        LinkageDelegate linkageDelegate = this.engine.getLinkageDelegate();
        if (linkageDelegate != null) {
            LinkageNotification linkageNotification = new LinkageNotification(this.linkageType == LinkageType.REFRESH ? LinkageAction.REFRESH : LinkageAction.REQUEST, this);
            linkageNotification.setRefreshStructure(refreshStructure);
            linkageDelegate.respondToLinkage(linkageNotification);
        }
    }

    public ValidateResult validate() {
        JSONObject validate;
        ValidateResult result = new ValidateResult();
        String validateContent = getValidateContent();
        if (validateContent != null && (validate = this.data.getJSONObject("validate")) != null) {
            JSONArray regexArray = validate.getJSONArray("regex");
            JSONArray msgArray = validate.getJSONArray("msg");
            if (regexArray != null && msgArray != null && !regexArray.isEmpty() && regexArray.size() == msgArray.size()) {
                int s = regexArray.size();
                int i = 0;
                while (true) {
                    if (i >= s) {
                        break;
                    }
                    String regex = regexArray.getString(i);
                    String errorMsg = msgArray.getString(i);
                    try {
                        if (!Pattern.compile(regex).matcher(validateContent).find()) {
                            result.setValid(false);
                            result.setErrorMsg(errorMsg);
                            break;
                        }
                        i++;
                    } catch (Throwable th) {
                    }
                }
            }
        }
        return result;
    }

    /* access modifiers changed from: protected */
    public String getValidateContent() {
        return null;
    }

    public JSONObject getData() {
        return this.data;
    }

    public JSONObject getFields() {
        return this.fields;
    }

    public String getTopic() {
        return getKey();
    }

    public ComponentType getType() {
        return this.type;
    }

    public String getTag() {
        return this.data != null ? this.data.getString("tag") : "unknown";
    }

    public ComponentStatus getStatus() {
        return this.status;
    }

    public void setStatus(ComponentStatus status2) {
        this.status = status2;
    }

    public String getId() {
        return this.data.getString("id");
    }

    public String getKey() {
        String tag = getTag();
        String id = getId();
        if (tag == null || id == null) {
            return null;
        }
        return tag + "_" + id;
    }

    public boolean isSubmit() {
        return this.data.getBooleanValue("submit");
    }

    public Component getParent() {
        return this.parent;
    }

    public void setParent(Component parent2) {
        this.parent = parent2;
    }

    public LinkageType getLinkageType() {
        return this.linkageType;
    }

    public void setLinkageType(LinkageType linkageType2) {
        this.linkageType = linkageType2;
    }

    public JSONObject getRender() {
        if (this.data != null) {
            return this.data.getJSONObject("render");
        }
        return null;
    }

    public Object getStorage() {
        return this.storage;
    }

    public void setStorage(Object storage2) {
        this.storage = storage2;
    }

    public Object getExtension(int key) {
        if (this.extensions != null) {
            return this.extensions.get(key);
        }
        return null;
    }

    public void setExtension(int key, Object value) {
        if (this.extensions == null) {
            this.extensions = new SparseArray<>();
        }
        this.extensions.put(key, value);
    }
}
