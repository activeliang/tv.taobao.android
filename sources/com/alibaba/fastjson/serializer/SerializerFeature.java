package com.alibaba.fastjson.serializer;

public enum SerializerFeature {
    QuoteFieldNames,
    UseSingleQuotes,
    WriteMapNullValue,
    WriteEnumUsingToString,
    WriteEnumUsingName,
    UseISO8601DateFormat,
    WriteNullListAsEmpty,
    WriteNullStringAsEmpty,
    WriteNullNumberAsZero,
    WriteNullBooleanAsFalse,
    SkipTransientField,
    SortField,
    WriteTabAsSpecial,
    PrettyFormat,
    WriteClassName,
    DisableCircularReferenceDetect,
    WriteSlashAsSpecial,
    BrowserCompatible,
    WriteDateUseDateFormat,
    NotWriteRootClassName,
    DisableCheckSpecialChar,
    BeanToArray,
    WriteNonStringKeyAsString,
    NotWriteDefaultValue,
    BrowserSecure,
    IgnoreNonFieldGetter,
    WriteNonStringValueAsString,
    IgnoreErrorGetter,
    WriteBigDecimalAsPlain;
    
    public static final SerializerFeature[] EMPTY = null;
    public static final int WRITE_MAP_NULL_FEATURES = 0;
    public final int mask;

    static {
        EMPTY = new SerializerFeature[0];
        WRITE_MAP_NULL_FEATURES = WriteMapNullValue.getMask() | WriteNullBooleanAsFalse.getMask() | WriteNullListAsEmpty.getMask() | WriteNullNumberAsZero.getMask() | WriteNullStringAsEmpty.getMask();
    }

    public final int getMask() {
        return this.mask;
    }

    public static boolean isEnabled(int features, SerializerFeature feature) {
        return (feature.mask & features) != 0;
    }

    public static boolean isEnabled(int features, int fieaturesB, SerializerFeature feature) {
        int mask2 = feature.mask;
        return ((features & mask2) == 0 && (fieaturesB & mask2) == 0) ? false : true;
    }

    public static int config(int features, SerializerFeature feature, boolean state) {
        if (state) {
            return features | feature.mask;
        }
        return features & (feature.mask ^ -1);
    }

    public static int of(SerializerFeature[] features) {
        if (features == null) {
            return 0;
        }
        int value = 0;
        for (SerializerFeature feature : features) {
            value |= feature.mask;
        }
        return value;
    }
}
