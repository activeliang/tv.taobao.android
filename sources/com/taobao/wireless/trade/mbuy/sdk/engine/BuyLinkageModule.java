package com.taobao.wireless.trade.mbuy.sdk.engine;

import com.alibaba.fastjson.JSONArray;
import com.taobao.wireless.trade.mbuy.sdk.co.Component;
import java.lang.ref.WeakReference;
import java.util.Iterator;
import java.util.Map;

public class BuyLinkageModule {
    protected BuyEngine engine;
    private WeakReference<LinkageDelegate> linkageDelegateRef;

    public BuyLinkageModule(BuyEngine engine2) {
        this.engine = engine2;
    }

    public LinkageDelegate getLinkageDelegate() {
        if (this.linkageDelegateRef != null) {
            return (LinkageDelegate) this.linkageDelegateRef.get();
        }
        return null;
    }

    public void setLinkageDelegate(LinkageDelegate linkageDelegate) {
        this.linkageDelegateRef = new WeakReference<>(linkageDelegate);
    }

    /* access modifiers changed from: protected */
    public void startLinkageEngine() {
        BuyEngineContext context = this.engine.getContext();
        Map<String, Component> index = context.getIndex();
        JSONArray request = context.getRequest();
        if (request == null) {
            request = new JSONArray();
            context.getLinkage().put("request", (Object) request);
        }
        Iterator<Object> it = request.iterator();
        while (it.hasNext()) {
            Component component = index.get((String) it.next());
            if (component != null) {
                component.setLinkageType(LinkageType.REQUEST);
            }
        }
    }

    /* access modifiers changed from: protected */
    public void freeLinkageEngine() {
    }
}
