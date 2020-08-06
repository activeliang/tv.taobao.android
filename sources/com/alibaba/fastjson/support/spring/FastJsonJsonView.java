package com.alibaba.fastjson.support.spring;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.serializer.SerializeFilter;
import com.alibaba.fastjson.serializer.SerializerFeature;
import com.alibaba.fastjson.support.config.FastJsonConfig;
import com.alibaba.sdk.android.oss.common.utils.HttpHeaders;
import java.io.ByteArrayOutputStream;
import java.nio.charset.Charset;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import mtopsdk.common.util.HttpHeaderConstant;
import org.springframework.util.CollectionUtils;
import org.springframework.validation.BindingResult;
import org.springframework.web.servlet.view.AbstractView;

public class FastJsonJsonView extends AbstractView {
    public static final String DEFAULT_CONTENT_TYPE = "application/json;charset=UTF-8";
    @Deprecated
    protected Charset charset = Charset.forName("UTF-8");
    @Deprecated
    protected String dateFormat;
    private boolean disableCaching = true;
    private boolean extractValueFromSingleKeyModel = false;
    private FastJsonConfig fastJsonConfig = new FastJsonConfig();
    @Deprecated
    protected SerializerFeature[] features = new SerializerFeature[0];
    @Deprecated
    protected SerializeFilter[] filters = new SerializeFilter[0];
    private Set<String> renderedAttributes;
    private boolean updateContentLength = true;

    public FastJsonJsonView() {
        setContentType(DEFAULT_CONTENT_TYPE);
        setExposePathVariables(false);
    }

    public FastJsonConfig getFastJsonConfig() {
        return this.fastJsonConfig;
    }

    public void setFastJsonConfig(FastJsonConfig fastJsonConfig2) {
        this.fastJsonConfig = fastJsonConfig2;
    }

    @Deprecated
    public void setSerializerFeature(SerializerFeature... features2) {
        this.fastJsonConfig.setSerializerFeatures(features2);
    }

    @Deprecated
    public Charset getCharset() {
        return this.fastJsonConfig.getCharset();
    }

    @Deprecated
    public void setCharset(Charset charset2) {
        this.fastJsonConfig.setCharset(charset2);
    }

    @Deprecated
    public String getDateFormat() {
        return this.fastJsonConfig.getDateFormat();
    }

    @Deprecated
    public void setDateFormat(String dateFormat2) {
        this.fastJsonConfig.setDateFormat(dateFormat2);
    }

    @Deprecated
    public SerializerFeature[] getFeatures() {
        return this.fastJsonConfig.getSerializerFeatures();
    }

    @Deprecated
    public void setFeatures(SerializerFeature... features2) {
        this.fastJsonConfig.setSerializerFeatures(features2);
    }

    @Deprecated
    public SerializeFilter[] getFilters() {
        return this.fastJsonConfig.getSerializeFilters();
    }

    @Deprecated
    public void setFilters(SerializeFilter... filters2) {
        this.fastJsonConfig.setSerializeFilters(filters2);
    }

    public void setRenderedAttributes(Set<String> renderedAttributes2) {
        this.renderedAttributes = renderedAttributes2;
    }

    public boolean isExtractValueFromSingleKeyModel() {
        return this.extractValueFromSingleKeyModel;
    }

    public void setExtractValueFromSingleKeyModel(boolean extractValueFromSingleKeyModel2) {
        this.extractValueFromSingleKeyModel = extractValueFromSingleKeyModel2;
    }

    /* access modifiers changed from: protected */
    public void renderMergedOutputModel(Map<String, Object> model, HttpServletRequest request, HttpServletResponse response) throws Exception {
        Object value = filterModel(model);
        ByteArrayOutputStream outnew = new ByteArrayOutputStream();
        int len = JSON.writeJSONString(outnew, this.fastJsonConfig.getCharset(), value, this.fastJsonConfig.getSerializeConfig(), this.fastJsonConfig.getSerializeFilters(), this.fastJsonConfig.getDateFormat(), JSON.DEFAULT_GENERATE_FEATURE, this.fastJsonConfig.getSerializerFeatures());
        if (this.updateContentLength) {
            response.setContentLength(len);
        }
        ServletOutputStream out = response.getOutputStream();
        outnew.writeTo(out);
        outnew.close();
        out.flush();
    }

    /* access modifiers changed from: protected */
    public void prepareResponse(HttpServletRequest request, HttpServletResponse response) {
        setResponseContentType(request, response);
        response.setCharacterEncoding(this.fastJsonConfig.getCharset().name());
        if (this.disableCaching) {
            response.addHeader("Pragma", HttpHeaderConstant.NO_CACHE);
            response.addHeader("Cache-Control", "no-cache, no-store, max-age=0");
            response.addDateHeader(HttpHeaders.EXPIRES, 1);
        }
    }

    public void setDisableCaching(boolean disableCaching2) {
        this.disableCaching = disableCaching2;
    }

    public void setUpdateContentLength(boolean updateContentLength2) {
        this.updateContentLength = updateContentLength2;
    }

    /* access modifiers changed from: protected */
    public Object filterModel(Map<String, Object> model) {
        Set<String> renderedAttributes2;
        Map<String, Object> result = new HashMap<>(model.size());
        if (!CollectionUtils.isEmpty(this.renderedAttributes)) {
            renderedAttributes2 = this.renderedAttributes;
        } else {
            renderedAttributes2 = model.keySet();
        }
        for (Map.Entry<String, Object> entry : model.entrySet()) {
            if (!(entry.getValue() instanceof BindingResult) && renderedAttributes2.contains(entry.getKey())) {
                result.put(entry.getKey(), entry.getValue());
            }
        }
        if (!this.extractValueFromSingleKeyModel || result.size() != 1) {
            return result;
        }
        Iterator<Map.Entry<String, Object>> it = result.entrySet().iterator();
        if (it.hasNext()) {
            return it.next().getValue();
        }
        return result;
    }
}
