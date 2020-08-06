package com.alibaba.fastjson.support.jaxrs;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.serializer.SerializeFilter;
import com.alibaba.fastjson.serializer.SerializerFeature;
import com.alibaba.fastjson.support.config.FastJsonConfig;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.lang.annotation.Annotation;
import java.lang.reflect.Type;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import javax.ws.rs.Consumes;
import javax.ws.rs.Produces;
import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.MultivaluedMap;
import javax.ws.rs.ext.MessageBodyReader;
import javax.ws.rs.ext.MessageBodyWriter;
import javax.ws.rs.ext.Provider;

@Consumes({"*/*"})
@Produces({"*/*"})
@Provider
public class FastJsonProvider implements MessageBodyReader<Object>, MessageBodyWriter<Object> {
    @Deprecated
    protected Charset charset = Charset.forName("UTF-8");
    private Class<?>[] clazzes = null;
    @Deprecated
    protected String dateFormat;
    private FastJsonConfig fastJsonConfig = new FastJsonConfig();
    @Deprecated
    protected SerializerFeature[] features = new SerializerFeature[0];
    @Deprecated
    protected SerializeFilter[] filters = new SerializeFilter[0];
    private boolean pretty;

    public FastJsonConfig getFastJsonConfig() {
        return this.fastJsonConfig;
    }

    public void setFastJsonConfig(FastJsonConfig fastJsonConfig2) {
        this.fastJsonConfig = fastJsonConfig2;
    }

    public FastJsonProvider() {
    }

    public FastJsonProvider(Class<?>[] clazzes2) {
        this.clazzes = clazzes2;
    }

    public FastJsonProvider setPretty(boolean p) {
        this.pretty = p;
        return this;
    }

    @Deprecated
    public FastJsonProvider(String charset2) {
        this.fastJsonConfig.setCharset(Charset.forName(charset2));
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

    /* access modifiers changed from: protected */
    public boolean isValidType(Class<?> type, Annotation[] classAnnotations) {
        if (type == null) {
            return false;
        }
        if (this.clazzes == null) {
            return true;
        }
        for (Class<?> cls : this.clazzes) {
            if (cls == type) {
                return true;
            }
        }
        return false;
    }

    /* access modifiers changed from: protected */
    public boolean hasMatchingMediaType(MediaType mediaType) {
        if (mediaType == null) {
            return true;
        }
        String subtype = mediaType.getSubtype();
        if ("json".equalsIgnoreCase(subtype) || subtype.endsWith("+json") || "javascript".equals(subtype) || "x-javascript".equals(subtype) || "x-json".equals(subtype) || "x-www-form-urlencoded".equalsIgnoreCase(subtype) || subtype.endsWith("x-www-form-urlencoded")) {
            return true;
        }
        return false;
    }

    public boolean isWriteable(Class<?> type, Type genericType, Annotation[] annotations, MediaType mediaType) {
        if (!hasMatchingMediaType(mediaType)) {
            return false;
        }
        return isValidType(type, annotations);
    }

    public long getSize(Object t, Class<?> cls, Type genericType, Annotation[] annotations, MediaType mediaType) {
        return -1;
    }

    public void writeTo(Object obj, Class<?> cls, Type genericType, Annotation[] annotations, MediaType mediaType, MultivaluedMap<String, Object> httpHeaders, OutputStream entityStream) throws IOException, WebApplicationException {
        SerializerFeature[] serializerFeatures;
        SerializerFeature[] serializerFeatures2 = this.fastJsonConfig.getSerializerFeatures();
        if (this.pretty) {
            if (serializerFeatures2 == null) {
                serializerFeatures = new SerializerFeature[]{SerializerFeature.PrettyFormat};
            } else {
                List<SerializerFeature> featureList = new ArrayList<>(Arrays.asList(serializerFeatures2));
                featureList.add(SerializerFeature.PrettyFormat);
                serializerFeatures = (SerializerFeature[]) featureList.toArray(serializerFeatures2);
            }
            this.fastJsonConfig.setSerializerFeatures(serializerFeatures);
        }
        MultivaluedMap<String, Object> multivaluedMap = httpHeaders;
        multivaluedMap.add("Content-Length", Integer.valueOf(JSON.writeJSONString(entityStream, this.fastJsonConfig.getCharset(), obj, this.fastJsonConfig.getSerializeConfig(), this.fastJsonConfig.getSerializeFilters(), this.fastJsonConfig.getDateFormat(), JSON.DEFAULT_GENERATE_FEATURE, this.fastJsonConfig.getSerializerFeatures())));
        entityStream.flush();
    }

    public boolean isReadable(Class<?> type, Type genericType, Annotation[] annotations, MediaType mediaType) {
        if (!hasMatchingMediaType(mediaType)) {
            return false;
        }
        return isValidType(type, annotations);
    }

    public Object readFrom(Class<Object> cls, Type genericType, Annotation[] annotations, MediaType mediaType, MultivaluedMap<String, String> multivaluedMap, InputStream entityStream) throws IOException, WebApplicationException {
        return JSON.parseObject(entityStream, this.fastJsonConfig.getCharset(), genericType, this.fastJsonConfig.getFeatures());
    }
}
