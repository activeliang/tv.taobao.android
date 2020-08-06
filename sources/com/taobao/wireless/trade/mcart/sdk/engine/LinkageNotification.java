package com.taobao.wireless.trade.mcart.sdk.engine;

import com.taobao.wireless.trade.mcart.sdk.co.Component;

public class LinkageNotification {
    private boolean isRefreshStructure;
    private LinkageAction linkageAction = LinkageAction.REFRESH;
    private Component trigger;

    public LinkageNotification() {
    }

    public LinkageNotification(LinkageAction linkageAction2, Component trigger2) {
        this.linkageAction = linkageAction2;
        this.trigger = trigger2;
    }

    public LinkageAction getLinkageAction() {
        return this.linkageAction;
    }

    public void setLinkageAction(LinkageAction linkageAction2) {
        this.linkageAction = linkageAction2;
    }

    public Component getTrigger() {
        return this.trigger;
    }

    public void setTrigger(Component invoker) {
        this.trigger = invoker;
    }

    public boolean isRefreshStructure() {
        return this.isRefreshStructure;
    }

    public void setRefreshStructure(boolean refreshStructure) {
        this.isRefreshStructure = refreshStructure;
    }
}
