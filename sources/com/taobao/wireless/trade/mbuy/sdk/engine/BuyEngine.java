package com.taobao.wireless.trade.mbuy.sdk.engine;

import com.alibaba.fastjson.JSONObject;
import com.taobao.wireless.trade.mbuy.sdk.co.Component;
import com.taobao.wireless.trade.mbuy.sdk.co.ComponentTag;
import com.taobao.wireless.trade.mbuy.sdk.co.ComponentType;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class BuyEngine {
    private BuyEngineContext context = new BuyEngineContext();
    private String currencySymbol;
    protected BuyLinkageModule linkageModule = new BuyLinkageModule(this);
    protected BuyParseModule parseModule = new BuyParseModule(this);
    protected BuyProfileModule profileModule = new BuyProfileModule(this);
    protected BuySubmitModule submitModule = new BuySubmitModule(this);
    private Object tempObject;
    private String token;
    protected BuyValidateModule validateModule = new BuyValidateModule(this);

    public BuyEngine() {
        initContext();
    }

    public String getToken() {
        if (this.token == null) {
            this.token = String.valueOf(System.currentTimeMillis());
        }
        return this.token;
    }

    public Object tempObject() {
        return this.tempObject;
    }

    public void setTempObject(Object tempObject2) {
        this.tempObject = tempObject2;
    }

    public String getCurrencySymbol() {
        return this.currencySymbol;
    }

    public void setCurrencySymbol(String currencySymbol2) {
        this.currencySymbol = currencySymbol2;
    }

    public List<Component> parse(JSONObject origin) {
        return this.parseModule.parse(origin);
    }

    public void registerSplitJoinRule(ComponentTag componentTag, SplitJoinRule rule) {
        this.parseModule.registerInternalSplitJoinRule();
        this.parseModule.registerSplitJoinRule(componentTag, rule);
    }

    public void registerExpandParseRule(ExpandParseRule rule) {
        this.parseModule.registerExpandParseRule(rule);
    }

    public ExpandParseRule getExpandParseRule() {
        return this.parseModule.getExpandParseRule();
    }

    public Component getComponentByTag(ComponentTag selfTag, ComponentTag parentTag) {
        return this.parseModule.getComponentByTag(selfTag, parentTag);
    }

    public Component getComponentByType(ComponentType type) {
        return this.parseModule.getComponentByType(type);
    }

    public ValidateResult validate() {
        return this.validateModule.execute();
    }

    public JSONObject generateFinalSubmitData() {
        return this.submitModule.generateFinalSubmitData();
    }

    public String generateFinalSubmitDataWithZip() {
        return this.submitModule.generateFinalSubmitDataWithZip();
    }

    public String generateCurrentBuyDataWithZip() {
        return this.submitModule.generateCurrentBuyDataWithZip();
    }

    public JSONObject generateAsyncRequestData(Component trigger) {
        return this.submitModule.generateAsyncRequestData(trigger);
    }

    public String generateAsyncRequestDataWithZip(Component trigger) {
        return this.submitModule.generateAsyncRequestDataWithZip(trigger);
    }

    public LinkageDelegate getLinkageDelegate() {
        return this.linkageModule.getLinkageDelegate();
    }

    public void setLinkageDelegate(LinkageDelegate linkageDelegate) {
        this.linkageModule.setLinkageDelegate(linkageDelegate);
    }

    public ProfileDelegate getProfileDelegate() {
        return this.profileModule.getProfileDelegate();
    }

    public void setProfileDelegate(ProfileDelegate profileDelegate) {
        this.profileModule.setProfileDelegate(profileDelegate);
    }

    public boolean executeRollback() {
        return this.submitModule.executeRollback();
    }

    public BuyEngineContext getContext() {
        return this.context;
    }

    /* access modifiers changed from: protected */
    public void initContext() {
        this.context = new BuyEngineContext();
    }

    /* access modifiers changed from: protected */
    public void startLinkageEngine() {
        this.linkageModule.startLinkageEngine();
    }

    /* access modifiers changed from: protected */
    public void freeLinkageEngine() {
        this.linkageModule.freeLinkageEngine();
    }

    public Map<String, String> getRequestParams() {
        HashMap<String, String> params = new HashMap<>();
        params.put("coupon", "true");
        params.put("coVersion", "2.0");
        return params;
    }
}
