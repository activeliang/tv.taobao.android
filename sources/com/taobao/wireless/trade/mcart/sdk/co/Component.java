package com.taobao.wireless.trade.mcart.sdk.co;

import com.alibaba.fastjson.JSONObject;
import com.bftv.fui.constantplugin.Constant;
import com.taobao.wireless.trade.mcart.sdk.co.biz.ComponentBizUtil;
import com.taobao.wireless.trade.mcart.sdk.constant.CartFrom;
import com.taobao.wireless.trade.mcart.sdk.engine.CartEngine;
import com.taobao.wireless.trade.mcart.sdk.engine.LinkageAction;
import com.taobao.wireless.trade.mcart.sdk.engine.LinkageDelegate;
import com.taobao.wireless.trade.mcart.sdk.engine.LinkageNotification;
import com.taobao.wireless.trade.mcart.sdk.engine.LinkageType;
import java.util.Observable;
import java.util.Observer;

public abstract class Component implements Observer {
    protected CartFrom cartFrom = CartFrom.DEFAULT_CLIENT;
    protected CornerType cornerType = CornerType.NONE;
    protected JSONObject data;
    protected boolean editable = true;
    /* access modifiers changed from: protected */
    public JSONObject fields;
    protected LinkageType linkageType = LinkageType.REFRESH;
    protected Component parent;
    protected Component watcher;

    public enum CornerType {
        TOP,
        BOTTOM,
        BOTH,
        NONE
    }

    private Component() {
    }

    protected Component(CartFrom cartFrom2) {
        this.cartFrom = cartFrom2 == null ? CartFrom.DEFAULT_CLIENT : cartFrom2;
    }

    protected Component(JSONObject data2, CartFrom cartFrom2) {
        this.cartFrom = cartFrom2 == null ? CartFrom.DEFAULT_CLIENT : cartFrom2;
        reload(data2);
    }

    public CartFrom getCartFrom() {
        return this.cartFrom;
    }

    public void reload(JSONObject data2) {
        if (data2 == null) {
            throw new IllegalArgumentException();
        }
        this.data = data2;
        JSONObject fields2 = data2.getJSONObject("fields");
        if (fields2 == null) {
            throw new IllegalArgumentException();
        }
        this.fields = fields2;
        if (this.data.containsKey("editable")) {
            this.editable = this.data.getBooleanValue("editable");
        }
    }

    public void notifyLinkageDelegate(CartEngine engine) {
        if (engine != null) {
            notifyLinkageDelegate(false, engine);
        }
    }

    public void notifyLinkageDelegate(boolean refreshStructure, CartEngine engine) {
        LinkageDelegate linkageDelegate = engine.getLinkageDelegate();
        if (linkageDelegate != null) {
            LinkageNotification linkageNotification = new LinkageNotification(this.linkageType == LinkageType.REFRESH ? LinkageAction.REFRESH : LinkageAction.REQUEST, this);
            linkageNotification.setRefreshStructure(refreshStructure);
            linkageDelegate.respondToLinkage(linkageNotification);
        }
    }

    public boolean isEditable() {
        return this.editable;
    }

    public JSONObject convertToSubmitData() {
        return this.data;
    }

    public void update(Observable o, Object arg) {
    }

    /* access modifiers changed from: protected */
    public void refreshAllComponent() {
        ComponentBizUtil.refreshComponentInfoWithoutCheckStatus(this.cartFrom);
    }

    /* access modifiers changed from: protected */
    public void refreshCheckAllComponent() {
        ComponentBizUtil.refreshCheckAllComponentCheckStatus(this.cartFrom);
    }

    public String getTag() {
        return this.data != null ? this.data.getString("tag") : "unkown";
    }

    public String getComponentId() {
        String tag = getTag();
        String id = getId();
        return (tag == null || id == null) ? id : tag + "_" + id;
    }

    public String getId() {
        return this.data != null ? this.data.getString("id") : "unkown";
    }

    public Component getParent() {
        return this.parent;
    }

    public void setParent(Component parent2) {
        this.parent = parent2;
    }

    public Component getWatcher() {
        return this.watcher;
    }

    public void setWatcher(Component watcher2) {
        this.watcher = watcher2;
    }

    public JSONObject getFields() {
        return this.fields;
    }

    public CornerType getCornerType() {
        return this.cornerType;
    }

    public void setCornerType(CornerType cornerType2) {
        this.cornerType = cornerType2;
    }

    public String toString() {
        return "Component [tag=" + getTag() + ", componentId=" + getComponentId() + ", parent=" + ((this.parent == null || this.parent.getComponentId() == null) ? Constant.NULL : this.parent.getComponentId()) + ", id=" + getId() + "]";
    }
}
