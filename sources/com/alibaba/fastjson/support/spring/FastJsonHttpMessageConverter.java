package com.alibaba.fastjson.support.spring;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.serializer.SerializeFilter;
import com.alibaba.fastjson.serializer.SerializerFeature;
import com.alibaba.fastjson.support.config.FastJsonConfig;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.lang.reflect.Type;
import java.nio.charset.Charset;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpInputMessage;
import org.springframework.http.HttpOutputMessage;
import org.springframework.http.MediaType;
import org.springframework.http.converter.AbstractHttpMessageConverter;
import org.springframework.http.converter.GenericHttpMessageConverter;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.http.converter.HttpMessageNotWritableException;

public class FastJsonHttpMessageConverter extends AbstractHttpMessageConverter<Object> implements GenericHttpMessageConverter<Object> {
    private Charset charset = Charset.forName("UTF-8");
    @Deprecated
    protected String dateFormat;
    private FastJsonConfig fastJsonConfig = new FastJsonConfig();
    @Deprecated
    protected SerializerFeature[] features = new SerializerFeature[0];
    @Deprecated
    protected SerializeFilter[] filters = new SerializeFilter[0];

    public FastJsonConfig getFastJsonConfig() {
        return this.fastJsonConfig;
    }

    public void setFastJsonConfig(FastJsonConfig fastJsonConfig2) {
        this.fastJsonConfig = fastJsonConfig2;
    }

    public FastJsonHttpMessageConverter() {
        super(MediaType.ALL);
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

    @Deprecated
    public void addSerializeFilter(SerializeFilter filter) {
        if (filter != null) {
            int length = this.fastJsonConfig.getSerializeFilters().length;
            SerializeFilter[] filters2 = new SerializeFilter[(length + 1)];
            System.arraycopy(this.fastJsonConfig.getSerializeFilters(), 0, filters2, 0, length);
            filters2[filters2.length - 1] = filter;
            this.fastJsonConfig.setSerializeFilters(filters2);
        }
    }

    /* access modifiers changed from: protected */
    public boolean supports(Class<?> cls) {
        return true;
    }

    /* access modifiers changed from: protected */
    public Object readInternal(Class<? extends Object> clazz, HttpInputMessage inputMessage) throws IOException, HttpMessageNotReadableException {
        return JSON.parseObject(inputMessage.getBody(), this.fastJsonConfig.getCharset(), (Type) clazz, this.fastJsonConfig.getFeatures());
    }

    /* access modifiers changed from: protected */
    public void writeInternal(Object obj, HttpOutputMessage outputMessage) throws IOException, HttpMessageNotWritableException {
        HttpHeaders headers = outputMessage.getHeaders();
        ByteArrayOutputStream outnew = new ByteArrayOutputStream();
        int len = JSON.writeJSONString(outnew, this.fastJsonConfig.getCharset(), obj, this.fastJsonConfig.getSerializeConfig(), this.fastJsonConfig.getSerializeFilters(), this.fastJsonConfig.getDateFormat(), JSON.DEFAULT_GENERATE_FEATURE, this.fastJsonConfig.getSerializerFeatures());
        if (this.fastJsonConfig.isWriteContentLength()) {
            headers.setContentLength((long) len);
        }
        outnew.writeTo(outputMessage.getBody());
        outnew.close();
    }

    public boolean canRead(Type type, Class<?> contextClass, MediaType mediaType) {
        return FastJsonHttpMessageConverter.super.canRead(contextClass, mediaType);
    }

    public boolean canWrite(Type type, Class<?> contextClass, MediaType mediaType) {
        return FastJsonHttpMessageConverter.super.canWrite(contextClass, mediaType);
    }

    public Object read(Type type, Class<?> cls, HttpInputMessage inputMessage) throws IOException, HttpMessageNotReadableException {
        return JSON.parseObject(inputMessage.getBody(), this.fastJsonConfig.getCharset(), type, this.fastJsonConfig.getFeatures());
    }

    public void write(Object t, Type type, MediaType contentType, HttpOutputMessage outputMessage) throws IOException, HttpMessageNotWritableException {
        Long contentLength;
        HttpHeaders headers = outputMessage.getHeaders();
        if (headers.getContentType() == null) {
            if (contentType == null || contentType.isWildcardType() || contentType.isWildcardSubtype()) {
                contentType = getDefaultContentType(t);
            }
            if (contentType != null) {
                headers.setContentType(contentType);
            }
        }
        if (headers.getContentLength() == -1 && (contentLength = getContentLength(t, headers.getContentType())) != null) {
            headers.setContentLength(contentLength.longValue());
        }
        writeInternal(t, outputMessage);
        outputMessage.getBody().flush();
    }
}
