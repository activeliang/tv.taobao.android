package com.taobao.wireless.trade.mcart.sdk.engine;

import com.alibaba.fastjson.JSONObject;
import com.taobao.wireless.trade.mcart.sdk.co.Component;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class CartEngineContext {
    private JSONObject controlParas;
    private JSONObject data;
    private JSONObject excludes;
    private JSONObject feature;
    private JSONObject hierarchy;
    private Map<String, Component> index = new HashMap();
    private List<Component> output;
    private JSONObject pageMeta;
    private HashMap<Integer, Long> perPageRealPay = new HashMap<>();
    private RollbackProtocol rollbackProtocol;
    private JSONObject structure;
    private HashMap<Integer, Long> tsmTotalDiscount = new HashMap<>();

    public HashMap<Integer, Long> getPerPageRealPay() {
        return this.perPageRealPay;
    }

    public HashMap<Integer, Long> getTsmTotalDiscount() {
        return this.tsmTotalDiscount;
    }

    public JSONObject getExcludes() {
        return this.excludes;
    }

    public void setExcludes(JSONObject excludes2) {
        this.excludes = excludes2;
    }

    public JSONObject getControlParas() {
        return this.controlParas;
    }

    public void setControlParas(JSONObject controlParas2) {
        this.controlParas = controlParas2;
    }

    public JSONObject getPageMeta() {
        return this.pageMeta;
    }

    public void setPageMeta(JSONObject pageMeta2) {
        this.pageMeta = pageMeta2;
    }

    public JSONObject getHierarchy() {
        return this.hierarchy;
    }

    public void setHierarchy(JSONObject hierarchy2) {
        this.hierarchy = hierarchy2;
    }

    public JSONObject getStructure() {
        return this.structure;
    }

    public void setStructure(JSONObject structure2) {
        this.structure = structure2;
    }

    public JSONObject getData() {
        return this.data;
    }

    public void setData(JSONObject data2) {
        this.data = data2;
    }

    public Map<String, Component> getIndex() {
        return this.index;
    }

    public void setIndex(Map<String, Component> index2) {
        this.index = index2;
    }

    public List<Component> getOutput() {
        return this.output;
    }

    public void setOutput(List<Component> output2) {
        this.output = output2;
    }

    public JSONObject getFeature() {
        return this.feature;
    }

    public void setFeature(JSONObject feature2) {
        this.feature = feature2;
    }

    public RollbackProtocol getRollbackProtocol() {
        return this.rollbackProtocol;
    }

    public void setRollbackProtocol(RollbackProtocol rollbackProtocol2) {
        this.rollbackProtocol = rollbackProtocol2;
    }
}
