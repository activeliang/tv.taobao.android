package com.yunos.tvtaobao.tvsdk.utils;

import android.content.Context;
import android.content.res.ColorStateList;
import android.content.res.Resources;
import android.graphics.Color;
import android.text.Editable;
import android.text.Html;
import android.text.TextUtils;
import android.text.style.AbsoluteSizeSpan;
import android.text.style.ForegroundColorSpan;
import android.text.style.TextAppearanceSpan;
import anet.channel.strategy.dispatch.DispatchConstants;
import com.bftv.fui.constantplugin.Constant;
import com.yunos.tv.lib.DisplayUtil;
import com.yunos.tvtaobao.biz.preference.UpdatePreference;
import com.zhiping.dev.android.logger.ZpLogger;
import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.Map;
import mtopsdk.common.util.SymbolExpUtil;
import org.xml.sax.XMLReader;

public class CustomTagHandler implements Html.TagHandler {
    private final String TAG = "CustomTagHandler";
    final HashMap<String, String> attributes = new HashMap<>();
    private Context mContext;
    private ColorStateList mOriginColors;
    private int startIndex = 0;
    private int stopIndex = 0;

    public CustomTagHandler(Context context, ColorStateList originColors) {
        this.mContext = context;
        this.mOriginColors = originColors;
    }

    public void handleTag(boolean opening, String tag, Editable output, XMLReader xmlReader) {
        processAttributes(xmlReader);
        if (!tag.equalsIgnoreCase("span")) {
            return;
        }
        if (opening) {
            startSpan(tag, output, xmlReader);
            return;
        }
        endSpan(tag, output, xmlReader);
        this.attributes.clear();
    }

    public void startSpan(String tag, Editable output, XMLReader xmlReader) {
        this.startIndex = output.length();
    }

    public void endSpan(String tag, Editable output, XMLReader xmlReader) {
        this.stopIndex = output.length();
        String color = this.attributes.get(UpdatePreference.COLOR);
        String size = this.attributes.get("size");
        String style = this.attributes.get("style");
        if (!TextUtils.isEmpty(style)) {
            analysisStyle(this.startIndex, this.stopIndex, output, style);
        }
        if (!TextUtils.isEmpty(size)) {
            size = size.split("px")[0];
        }
        if (!TextUtils.isEmpty(color)) {
            if (color.startsWith(Constant.NLP_CACHE_TYPE)) {
                int colorRes = Resources.getSystem().getIdentifier(color.substring(1), UpdatePreference.COLOR, DispatchConstants.ANDROID);
                if (colorRes != 0) {
                    output.setSpan(new ForegroundColorSpan(colorRes), this.startIndex, this.stopIndex, 33);
                }
            } else {
                try {
                    output.setSpan(new ForegroundColorSpan(Color.parseColor(color)), this.startIndex, this.stopIndex, 33);
                } catch (Exception e) {
                    e.printStackTrace();
                    reductionFontColor(this.startIndex, this.stopIndex, output);
                }
            }
        }
        if (!TextUtils.isEmpty(size)) {
            int fontSizePx = 16;
            if (this.mContext != null) {
                fontSizePx = DisplayUtil.sp2px(this.mContext, (float) Integer.parseInt(size));
            }
            output.setSpan(new AbsoluteSizeSpan(fontSizePx), this.startIndex, this.stopIndex, 33);
        }
    }

    private void processAttributes(XMLReader xmlReader) {
        try {
            Field elementField = xmlReader.getClass().getDeclaredField("theNewElement");
            elementField.setAccessible(true);
            Object element = elementField.get(xmlReader);
            Field attsField = element.getClass().getDeclaredField("theAtts");
            attsField.setAccessible(true);
            Object atts = attsField.get(element);
            Field dataField = atts.getClass().getDeclaredField("data");
            dataField.setAccessible(true);
            String[] data = (String[]) dataField.get(atts);
            Field lengthField = atts.getClass().getDeclaredField("length");
            lengthField.setAccessible(true);
            int len = ((Integer) lengthField.get(atts)).intValue();
            for (int i = 0; i < len; i++) {
                this.attributes.put(data[(i * 5) + 1], data[(i * 5) + 4]);
            }
        } catch (Exception e) {
        }
    }

    private void reductionFontColor(int startIndex2, int stopIndex2, Editable editable) {
        if (this.mOriginColors != null) {
            editable.setSpan(new TextAppearanceSpan((String) null, 0, 0, this.mOriginColors, (ColorStateList) null), startIndex2, stopIndex2, 33);
        } else {
            editable.setSpan(new ForegroundColorSpan(-13948117), startIndex2, stopIndex2, 33);
        }
    }

    private void analysisStyle(int startIndex2, int stopIndex2, Editable editable, String style) {
        ZpLogger.e("CustomTagHandler", "style：" + style);
        String[] attrArray = style.split(SymbolExpUtil.SYMBOL_SEMICOLON);
        Map<String, String> attrMap = new HashMap<>();
        if (attrArray != null) {
            for (String attr : attrArray) {
                String[] keyValueArray = attr.split(SymbolExpUtil.SYMBOL_COLON);
                if (keyValueArray != null && keyValueArray.length == 2) {
                    attrMap.put(keyValueArray[0].trim(), keyValueArray[1].trim());
                }
            }
        }
        ZpLogger.e("CustomTagHandler", "attrMap：" + attrMap.toString());
        String color = attrMap.get(UpdatePreference.COLOR);
        String fontSize = attrMap.get("font-size");
        if (!TextUtils.isEmpty(fontSize)) {
            fontSize = fontSize.split("px")[0];
        }
        if (!TextUtils.isEmpty(color)) {
            if (color.startsWith(Constant.NLP_CACHE_TYPE)) {
                int colorRes = Resources.getSystem().getIdentifier(color.substring(1), UpdatePreference.COLOR, DispatchConstants.ANDROID);
                if (colorRes != 0) {
                    editable.setSpan(new ForegroundColorSpan(colorRes), startIndex2, stopIndex2, 33);
                }
            } else {
                try {
                    editable.setSpan(new ForegroundColorSpan(Color.parseColor(color)), startIndex2, stopIndex2, 33);
                } catch (Exception e) {
                    e.printStackTrace();
                    reductionFontColor(startIndex2, stopIndex2, editable);
                }
            }
        }
        if (!TextUtils.isEmpty(fontSize)) {
            int fontSizePx = 16;
            if (this.mContext != null) {
                fontSizePx = DisplayUtil.sp2px(this.mContext, (float) Integer.parseInt(fontSize));
            }
            editable.setSpan(new AbsoluteSizeSpan(fontSizePx), startIndex2, stopIndex2, 33);
        }
    }
}
