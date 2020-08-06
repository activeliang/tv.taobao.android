package com.alibaba.fastjson.parser.deserializer;

import com.alibaba.fastjson.JSONException;
import com.alibaba.fastjson.parser.DefaultJSONParser;
import com.alibaba.fastjson.parser.JSONScanner;
import java.lang.reflect.Type;
import java.sql.Date;
import java.sql.Timestamp;
import java.text.ParseException;

public class SqlDateDeserializer extends AbstractDateDeserializer implements ObjectDeserializer {
    public static final SqlDateDeserializer instance = new SqlDateDeserializer();
    public static final SqlDateDeserializer instance_timestamp = new SqlDateDeserializer(true);
    private boolean timestamp;

    public SqlDateDeserializer() {
        this.timestamp = false;
    }

    public SqlDateDeserializer(boolean timestmap) {
        this.timestamp = false;
        this.timestamp = true;
    }

    /* access modifiers changed from: protected */
    public <T> T cast(DefaultJSONParser parser, Type clazz, Object fieldName, Object val) {
        long longVal;
        Date val2;
        if (this.timestamp) {
            return castTimestamp(parser, clazz, fieldName, val);
        }
        if (val == null) {
            return null;
        }
        if (val instanceof java.util.Date) {
            val2 = new Date(((java.util.Date) val).getTime());
        } else if (val instanceof Number) {
            val2 = new Date(((Number) val).longValue());
        } else if (val instanceof String) {
            String strVal = (String) val;
            if (strVal.length() == 0) {
                return null;
            }
            JSONScanner dateLexer = new JSONScanner(strVal);
            try {
                if (dateLexer.scanISO8601DateIfMatch()) {
                    longVal = dateLexer.getCalendar().getTimeInMillis();
                } else {
                    T date = new Date(parser.getDateFormat().parse(strVal).getTime());
                    dateLexer.close();
                    return date;
                }
            } catch (ParseException e) {
                longVal = Long.parseLong(strVal);
            } catch (Throwable th) {
                dateLexer.close();
                throw th;
            }
            dateLexer.close();
            return new Date(longVal);
        } else {
            throw new JSONException("parse error : " + val);
        }
        return val2;
    }

    /* access modifiers changed from: protected */
    public <T> T castTimestamp(DefaultJSONParser parser, Type clazz, Object fieldName, Object val) {
        long longVal;
        if (val == null) {
            return null;
        }
        if (val instanceof java.util.Date) {
            return new Timestamp(((java.util.Date) val).getTime());
        }
        if (val instanceof Number) {
            return new Timestamp(((Number) val).longValue());
        }
        if (val instanceof String) {
            String strVal = (String) val;
            if (strVal.length() == 0) {
                return null;
            }
            JSONScanner dateLexer = new JSONScanner(strVal);
            try {
                if (dateLexer.scanISO8601DateIfMatch()) {
                    longVal = dateLexer.getCalendar().getTimeInMillis();
                } else {
                    T timestamp2 = new Timestamp(parser.getDateFormat().parse(strVal).getTime());
                    dateLexer.close();
                    return timestamp2;
                }
            } catch (ParseException e) {
                longVal = Long.parseLong(strVal);
            } catch (Throwable th) {
                dateLexer.close();
                throw th;
            }
            dateLexer.close();
            return new Timestamp(longVal);
        }
        throw new JSONException("parse error");
    }

    public int getFastMatchToken() {
        return 2;
    }
}
