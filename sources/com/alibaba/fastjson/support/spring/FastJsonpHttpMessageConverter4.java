package com.alibaba.fastjson.support.spring;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.support.config.FastJsonConfig;
import com.alibaba.fastjson.util.IOUtils;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.lang.reflect.Type;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpInputMessage;
import org.springframework.http.HttpOutputMessage;
import org.springframework.http.MediaType;
import org.springframework.http.converter.AbstractGenericHttpMessageConverter;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.http.converter.HttpMessageNotWritableException;

public class FastJsonpHttpMessageConverter4 extends AbstractGenericHttpMessageConverter<Object> {
    private static final byte[] JSONP_FUNCTION_PREFIX_BYTES = "/**/".getBytes(IOUtils.UTF8);
    private static final byte[] JSONP_FUNCTION_SUFFIX_BYTES = ");".getBytes(IOUtils.UTF8);
    private FastJsonConfig fastJsonConfig = new FastJsonConfig();

    public FastJsonConfig getFastJsonConfig() {
        return this.fastJsonConfig;
    }

    public void setFastJsonConfig(FastJsonConfig fastJsonConfig2) {
        this.fastJsonConfig = fastJsonConfig2;
    }

    public FastJsonpHttpMessageConverter4() {
        super(MediaType.ALL);
    }

    /* access modifiers changed from: protected */
    public boolean supports(Class<?> cls) {
        return true;
    }

    public Object read(Type type, Class<?> cls, HttpInputMessage inputMessage) throws IOException, HttpMessageNotReadableException {
        return JSON.parseObject(inputMessage.getBody(), this.fastJsonConfig.getCharset(), type, this.fastJsonConfig.getFeatures());
    }

    /* access modifiers changed from: protected */
    public Object readInternal(Class<? extends Object> clazz, HttpInputMessage inputMessage) throws IOException, HttpMessageNotReadableException {
        return JSON.parseObject(inputMessage.getBody(), this.fastJsonConfig.getCharset(), (Type) clazz, this.fastJsonConfig.getFeatures());
    }

    /* access modifiers changed from: protected */
    public void writeInternal(Object obj, Type type, HttpOutputMessage outputMessage) throws IOException, HttpMessageNotWritableException {
        HttpHeaders headers = outputMessage.getHeaders();
        ByteArrayOutputStream outnew = new ByteArrayOutputStream();
        int len = writePrefix(outnew, obj);
        Object value = obj;
        if (obj instanceof MappingFastJsonValue) {
            value = ((MappingFastJsonValue) obj).getValue();
        }
        int len2 = len + JSON.writeJSONString(outnew, this.fastJsonConfig.getCharset(), value, this.fastJsonConfig.getSerializeConfig(), this.fastJsonConfig.getSerializeFilters(), this.fastJsonConfig.getDateFormat(), JSON.DEFAULT_GENERATE_FEATURE, this.fastJsonConfig.getSerializerFeatures()) + writeSuffix(outnew, obj);
        if (this.fastJsonConfig.isWriteContentLength()) {
            headers.setContentLength((long) len2);
        }
        outnew.writeTo(outputMessage.getBody());
        outnew.close();
    }

    /* access modifiers changed from: protected */
    public int writePrefix(ByteArrayOutputStream out, Object object) throws IOException {
        String jsonpFunction = object instanceof MappingFastJsonValue ? ((MappingFastJsonValue) object).getJsonpFunction() : null;
        if (jsonpFunction == null) {
            return 0;
        }
        out.write(JSONP_FUNCTION_PREFIX_BYTES);
        byte[] bytes = (jsonpFunction + "(").getBytes(IOUtils.UTF8);
        out.write(bytes);
        return 0 + JSONP_FUNCTION_PREFIX_BYTES.length + bytes.length;
    }

    /* access modifiers changed from: protected */
    public int writeSuffix(ByteArrayOutputStream out, Object object) throws IOException {
        if ((object instanceof MappingFastJsonValue ? ((MappingFastJsonValue) object).getJsonpFunction() : null) == null) {
            return 0;
        }
        out.write(JSONP_FUNCTION_SUFFIX_BYTES);
        return 0 + JSONP_FUNCTION_SUFFIX_BYTES.length;
    }
}
