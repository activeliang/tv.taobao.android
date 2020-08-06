package com.alibaba.fastjson.support.retrofit;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.parser.Feature;
import com.alibaba.fastjson.parser.ParserConfig;
import com.alibaba.fastjson.serializer.SerializeConfig;
import com.alibaba.fastjson.serializer.SerializerFeature;
import java.io.IOException;
import java.lang.annotation.Annotation;
import java.lang.reflect.Type;
import okhttp3.MediaType;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Converter;
import retrofit2.Retrofit;

public class Retrofit2ConverterFactory extends Converter.Factory {
    /* access modifiers changed from: private */
    public static final Feature[] EMPTY_SERIALIZER_FEATURES = new Feature[0];
    /* access modifiers changed from: private */
    public static final MediaType MEDIA_TYPE = MediaType.parse("application/json; charset=UTF-8");
    /* access modifiers changed from: private */
    public int featureValues = JSON.DEFAULT_PARSER_FEATURE;
    /* access modifiers changed from: private */
    public Feature[] features;
    /* access modifiers changed from: private */
    public ParserConfig parserConfig = ParserConfig.getGlobalInstance();
    /* access modifiers changed from: private */
    public SerializeConfig serializeConfig;
    /* access modifiers changed from: private */
    public SerializerFeature[] serializerFeatures;

    public Converter<ResponseBody, ?> responseBodyConverter(Type type, Annotation[] annotations, Retrofit retrofit) {
        return new ResponseBodyConverter(type);
    }

    public Converter<?, RequestBody> requestBodyConverter(Type type, Annotation[] parameterAnnotations, Annotation[] methodAnnotations, Retrofit retrofit) {
        return new RequestBodyConverter();
    }

    public ParserConfig getParserConfig() {
        return this.parserConfig;
    }

    public Retrofit2ConverterFactory setParserConfig(ParserConfig config) {
        this.parserConfig = config;
        return this;
    }

    public int getParserFeatureValues() {
        return this.featureValues;
    }

    public Retrofit2ConverterFactory setParserFeatureValues(int featureValues2) {
        this.featureValues = featureValues2;
        return this;
    }

    public Feature[] getParserFeatures() {
        return this.features;
    }

    public Retrofit2ConverterFactory setParserFeatures(Feature[] features2) {
        this.features = features2;
        return this;
    }

    public SerializeConfig getSerializeConfig() {
        return this.serializeConfig;
    }

    public Retrofit2ConverterFactory setSerializeConfig(SerializeConfig serializeConfig2) {
        this.serializeConfig = serializeConfig2;
        return this;
    }

    public SerializerFeature[] getSerializerFeatures() {
        return this.serializerFeatures;
    }

    public Retrofit2ConverterFactory setSerializerFeatures(SerializerFeature[] features2) {
        this.serializerFeatures = features2;
        return this;
    }

    final class ResponseBodyConverter<T> implements Converter<ResponseBody, T> {
        private Type type;

        ResponseBodyConverter(Type type2) {
            this.type = type2;
        }

        public T convert(ResponseBody value) throws IOException {
            Feature[] access$300;
            try {
                String string = value.string();
                Type type2 = this.type;
                ParserConfig access$000 = Retrofit2ConverterFactory.this.parserConfig;
                int access$100 = Retrofit2ConverterFactory.this.featureValues;
                if (Retrofit2ConverterFactory.this.features != null) {
                    access$300 = Retrofit2ConverterFactory.this.features;
                } else {
                    access$300 = Retrofit2ConverterFactory.EMPTY_SERIALIZER_FEATURES;
                }
                return JSON.parseObject(string, type2, access$000, access$100, access$300);
            } finally {
                value.close();
            }
        }
    }

    final class RequestBodyConverter<T> implements Converter<T, RequestBody> {
        RequestBodyConverter() {
        }

        public RequestBody convert(T value) throws IOException {
            SerializeConfig access$400;
            SerializerFeature[] access$500;
            if (Retrofit2ConverterFactory.this.serializeConfig == null) {
                access$400 = SerializeConfig.globalInstance;
            } else {
                access$400 = Retrofit2ConverterFactory.this.serializeConfig;
            }
            if (Retrofit2ConverterFactory.this.serializerFeatures == null) {
                access$500 = SerializerFeature.EMPTY;
            } else {
                access$500 = Retrofit2ConverterFactory.this.serializerFeatures;
            }
            return RequestBody.create(Retrofit2ConverterFactory.MEDIA_TYPE, JSON.toJSONBytes((Object) value, access$400, access$500));
        }
    }
}
