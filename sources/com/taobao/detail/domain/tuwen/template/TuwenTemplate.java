package com.taobao.detail.domain.tuwen.template;

import com.taobao.detail.domain.template.android.ComponentVO;
import com.taobao.detail.domain.tuwen.TuwenConstants;
import com.taobao.ju.track.server.JTrackParams;
import com.yunos.tvtaobao.biz.common.BaseConfig;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class TuwenTemplate extends ComponentVO implements Serializable, Cloneable {
    public List<TuwenTemplate> actions;
    public List<TuwenTemplate> children;
    public Map<String, Object> params;

    public TuwenTemplate() {
    }

    public TuwenTemplate(String id, String type, String key, Map<String, Object> params2, List<TuwenTemplate> children2) {
        this.ID = id;
        this.type = type;
        this.key = key;
        this.params = params2;
        this.children = children2;
    }

    public void addSeemore(Map<String, Object> requestMap) {
        if (this.children == null) {
            this.children = new ArrayList();
        }
        this.children.add(TuwenTemplateFactory.makeTemplate(TuwenConstants.MODEL_LIST_KEY.SEE_MORE, (Long) null, requestMap));
    }

    public void addParams(String key, Object obj) {
        if (this.params == null) {
            this.params = new HashMap();
        }
        this.params.put(key, obj);
    }

    public void addChild(TuwenTemplate child) {
        if (child != null) {
            if (this.children == null) {
                this.children = new ArrayList();
            }
            this.children.add(child);
        }
    }

    public void addChildWithDivsion(TuwenTemplate child) {
        if (child != null) {
            if (this.children == null) {
                this.children = new ArrayList();
            }
            this.children.add(child);
            this.children.add(TuwenTemplateFactory.makeBackDivision("9"));
        }
    }

    public void addFirstChild(TuwenTemplate child) {
        if (child != null) {
            if (this.children == null) {
                this.children = new ArrayList();
            }
            this.children.add(0, child);
        }
    }

    public void addRequestMap(String obj) {
        addParams(TuwenConstants.PARAMS.REQUEST_MAP, obj);
    }

    public void addPicSku(String skuPath, String skuName, double x, double y) {
        Map skuParams = new HashMap();
        skuParams.put(TuwenConstants.PARAMS.SKU_PATH, skuPath);
        skuParams.put("name", skuName);
        Map pos = new HashMap();
        pos.put("x", Double.valueOf(x));
        pos.put("y", Double.valueOf(y));
        skuParams.put("position", pos);
        addPicSku(skuParams);
    }

    public void addPicSku(Map<String, Object> params2) {
        if (params2 != null) {
            if (this.children == null) {
                this.children = new ArrayList();
            }
            StringBuilder append = new StringBuilder().append("pic_sku_");
            int i = TuwenTemplateFactory.count + 1;
            TuwenTemplateFactory.count = i;
            this.children.add(new TuwenTemplate(getUniquePicSkuId(append.append(i).toString(), this.children), "native", TuwenConstants.KEY.DETAIL_SKU_BAR, params2, (List<TuwenTemplate>) null));
        }
    }

    public void addHotArea(int style, double startX, double startY, double endX, double endY, Long itemId) {
        Map actionsParams = new HashMap();
        actionsParams.put("url", "//h5.m.taobao.com/awp/core/detail.htm");
        Map urlParams = new HashMap();
        urlParams.put("id", itemId.toString());
        urlParams.put(BaseConfig.INTENT_KEY_SCM, "20140620.2.1." + itemId);
        actionsParams.put("urlParams", urlParams);
        actionsParams.put("trackName", "DESC-hotarea");
        Map trackParams = new HashMap();
        trackParams.put("style", "1");
        actionsParams.put(JTrackParams.TRACK_PARAMS, trackParams);
        addHotArea(style, startX, startY, endX, endY, actionsParams);
    }

    private void addHotArea(int style, double startX, double startY, double endX, double endY, Map actionsParams) {
        Map params2 = new HashMap();
        params2.put("style", Integer.valueOf(style));
        Map pos = new HashMap();
        pos.put("startX", Double.valueOf(startX));
        pos.put("startY", Double.valueOf(startY));
        pos.put("endX", Double.valueOf(endX));
        pos.put("endY", Double.valueOf(endY));
        params2.put("position", pos);
        addHotArea(params2, actionsParams);
    }

    private void addHotArea(Map params2, Map actionsParams) {
        if (params2 != null) {
            StringBuilder append = new StringBuilder().append("hot_area");
            int i = TuwenTemplateFactory.count + 1;
            TuwenTemplateFactory.count = i;
            TuwenTemplate hotArea = new TuwenTemplate(append.append(i).toString(), "native", TuwenConstants.KEY.DETAIL_HOTAREA, params2, (List<TuwenTemplate>) null);
            StringBuilder append2 = new StringBuilder().append("action_open_url");
            int i2 = TuwenTemplateFactory.count + 1;
            TuwenTemplateFactory.count = i2;
            hotArea.addAction(new TuwenTemplate(append2.append(i2).toString(), (String) null, TuwenConstants.KEY.OPEN_URL, actionsParams, (List<TuwenTemplate>) null));
            addChild(hotArea);
        }
    }

    private void addAction(TuwenTemplate action) {
        if (action != null) {
            if (this.actions == null) {
                this.actions = new ArrayList();
            }
            this.actions.add(action);
        }
    }

    private String getUniquePicSkuId(String id, List<TuwenTemplate> children2) {
        for (TuwenTemplate temp : children2) {
            if (temp.ID.equals(id)) {
                return getUniquePicSkuId(id + TuwenTemplateFactory.count, children2);
            }
        }
        return id;
    }

    public String getParams(String key) {
        if (key == null || key.length() <= 0 || this.params == null || this.params.get(key) == null) {
            return null;
        }
        return String.valueOf(this.params.get(key));
    }
}
