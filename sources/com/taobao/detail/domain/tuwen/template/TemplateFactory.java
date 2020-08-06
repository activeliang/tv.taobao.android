package com.taobao.detail.domain.tuwen.template;

import com.taobao.detail.domain.tuwen.TuwenConstants;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class TemplateFactory {
    public static int count = 0;

    protected static TuwenTemplate makeChildren(String id, String type, String key, String lineColor, String titleColor, String backgroundColor, String title) {
        Map maps = new HashMap();
        maps.put(TuwenConstants.PARAMS.TITLE_COLOR, titleColor);
        maps.put(TuwenConstants.PARAMS.LINE_COLOR, lineColor);
        maps.put("title", title);
        maps.put(TuwenConstants.PARAMS.BACKGROUND_COLOR, backgroundColor);
        return new TuwenTemplate(id, type, key, maps, (List<TuwenTemplate>) null);
    }

    protected static TuwenTemplate makeChildren(String id, String type, String key, String loopStyle) {
        Map maps = new HashMap();
        maps.put(TuwenConstants.PARAMS.LOOP_STYLE, loopStyle);
        return new TuwenTemplate(id, type, key, maps, (List<TuwenTemplate>) null);
    }

    protected static TuwenTemplate makeChildren(String id, String type, String key, String loopStyle, double ratio) {
        Map maps = new HashMap();
        maps.put(TuwenConstants.PARAMS.LOOP_STYLE, loopStyle);
        maps.put(TuwenConstants.PARAMS.WIDTH_RADIO, Double.valueOf(ratio));
        return new TuwenTemplate(id, type, key, maps, (List<TuwenTemplate>) null);
    }

    protected static List<TuwenTemplate> makeChildren(TuwenTemplate... tuwenTemplates) {
        List<TuwenTemplate> templates = new ArrayList<>();
        for (TuwenTemplate template : tuwenTemplates) {
            templates.add(template);
        }
        return templates;
    }

    public static TuwenTemplate makeFrontDivision() {
        Map maps = new HashMap();
        maps.put("type", "blank");
        maps.put("height", "12");
        maps.put("bgcolor", "0xeeeeee");
        StringBuilder append = new StringBuilder().append("division_f");
        int i = count + 1;
        count = i;
        return new TuwenTemplate(append.append(i).toString(), "native", TuwenConstants.KEY.DIVISION, maps, (List<TuwenTemplate>) null);
    }

    public static TuwenTemplate makeBackDivision() {
        Map maps = new HashMap();
        maps.put("type", "blank");
        maps.put("height", "12");
        maps.put("bgcolor", "0xeeeeee");
        StringBuilder append = new StringBuilder().append("division_b");
        int i = count + 1;
        count = i;
        return new TuwenTemplate(append.append(i).toString(), "native", TuwenConstants.KEY.DIVISION, maps, (List<TuwenTemplate>) null);
    }

    public static TuwenTemplate makeBackDivision(String height) {
        Map maps = new HashMap();
        maps.put("type", "blank");
        maps.put("height", height);
        maps.put("bgcolor", "0xeeeeee");
        StringBuilder append = new StringBuilder().append("division_b");
        int i = count + 1;
        count = i;
        return new TuwenTemplate(append.append(i).toString(), "native", TuwenConstants.KEY.DIVISION, maps, (List<TuwenTemplate>) null);
    }

    public static TuwenTemplate makeEndDivision() {
        Map maps = new HashMap();
        maps.put("type", TuwenConstants.MODEL_LIST_KEY.TEXT);
        maps.put("title", "已经到最底了");
        maps.put("topLine", "double");
        maps.put("bgcolor", "0xeeeeee");
        StringBuilder append = new StringBuilder().append("division_b");
        int i = count + 1;
        count = i;
        return new TuwenTemplate(append.append(i).toString(), "native", TuwenConstants.KEY.DIVISION, maps, (List<TuwenTemplate>) null);
    }

    public static String getJumpUrl(Long itemId, String scm) {
        return new String("//a.m.taobao.com/i" + itemId + ".htm?scm=" + scm);
    }

    public static String getJumpUrl(Long itemId) {
        return new String("//a.m.taobao.com/i" + itemId + ".htm");
    }

    public static String getMatchingJumpUrl(Long itemId) {
        return getJumpUrl(itemId, "20140620.1.1." + itemId);
    }
}
