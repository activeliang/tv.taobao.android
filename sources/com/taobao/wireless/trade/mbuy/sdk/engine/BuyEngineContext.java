package com.taobao.wireless.trade.mbuy.sdk.engine;

import android.util.Pair;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.taobao.wireless.trade.mbuy.sdk.co.Component;
import com.taobao.wireless.trade.mbuy.sdk.utils.NotificationCenterImpl;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Queue;

public class BuyEngineContext {
    private JSONObject common;
    private JSONObject data;
    private JSONObject hierarchy;
    private HashMap<String, Component> index = new HashMap<>();
    private JSONArray input;
    private JSONObject linkage;
    private NotificationCenterImpl notificationCenter = new NotificationCenterImpl();
    private JSONObject origin;
    private List<Component> output;
    private Queue<Pair<JSONObject, Pair<String, Object>>> recovery = new LinkedList();
    private JSONArray request;
    private RollbackProtocol rollbackProtocol;
    private JSONObject structure;

    public JSONObject getOrigin() {
        return this.origin;
    }

    public void setOrigin(JSONObject origin2) {
        this.origin = origin2;
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

    public JSONObject getLinkage() {
        return this.linkage;
    }

    public void setLinkage(JSONObject linkage2) {
        this.linkage = linkage2;
    }

    public JSONArray getRequest() {
        return this.linkage.getJSONArray("request");
    }

    public JSONArray getInput() {
        return this.linkage.getJSONArray("input");
    }

    public Map<String, Component> getIndex() {
        return this.index;
    }

    public void setIndex(HashMap<String, Component> index2) {
        this.index = index2;
    }

    public List<Component> getOutput() {
        return this.output;
    }

    public void setOutput(List<Component> output2) {
        this.output = output2;
    }

    public Queue<Pair<JSONObject, Pair<String, Object>>> getRecovery() {
        return this.recovery;
    }

    public void setRecovery(Queue<Pair<JSONObject, Pair<String, Object>>> recovery2) {
        this.recovery = recovery2;
    }

    public void addRecoveryEntry(JSONObject data2, String key, Object value) {
        if (data2 != null && key != null && value != null) {
            this.recovery.offer(new Pair(data2, new Pair<>(key, value)));
        }
    }

    public RollbackProtocol getRollbackProtocol() {
        return this.rollbackProtocol;
    }

    public void setRollbackProtocol(RollbackProtocol rollbackProtocol2) {
        this.rollbackProtocol = rollbackProtocol2;
    }

    public NotificationCenterImpl getNotificationCenter() {
        return this.notificationCenter;
    }

    public JSONObject getCommon() {
        return this.common;
    }

    public void setCommon(JSONObject common2) {
        this.common = common2;
    }
}
