package com.alibaba.fastjson.serializer;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONException;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.JSONPath;
import com.alibaba.fastjson.JSONStreamAware;
import com.alibaba.fastjson.parser.DefaultJSONParser;
import com.alibaba.fastjson.parser.JSONLexer;
import com.alibaba.fastjson.parser.deserializer.ObjectDeserializer;
import com.alibaba.fastjson.util.TypeUtils;
import com.tvtaobao.android.runtime.RtCmdHandler;
import java.io.File;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.net.Inet4Address;
import java.net.Inet6Address;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URL;
import java.net.UnknownHostException;
import java.nio.charset.Charset;
import java.text.SimpleDateFormat;
import java.util.Currency;
import java.util.Iterator;
import java.util.Locale;
import java.util.Map;
import java.util.TimeZone;
import java.util.UUID;
import java.util.regex.Pattern;

public class MiscCodec implements ObjectSerializer, ObjectDeserializer {
    public static final MiscCodec instance = new MiscCodec();
    private static Method method_paths_get;
    private static boolean method_paths_get_error = false;

    public void write(JSONSerializer serializer, Object object, Object fieldName, Type fieldType, int features) throws IOException {
        String strVal;
        SerializeWriter out = serializer.out;
        if (object == null) {
            out.writeNull();
            return;
        }
        Class<?> objClass = object.getClass();
        if (objClass == SimpleDateFormat.class) {
            String pattern = ((SimpleDateFormat) object).toPattern();
            if (!out.isEnabled(SerializerFeature.WriteClassName) || object.getClass() == fieldType) {
                strVal = pattern;
            } else {
                out.write(123);
                out.writeFieldName(JSON.DEFAULT_TYPE_KEY);
                serializer.write(object.getClass().getName());
                out.writeFieldValue(',', RtCmdHandler.KEY_VAL, pattern);
                out.write(125);
                return;
            }
        } else if (objClass == Class.class) {
            strVal = ((Class) object).getName();
        } else if (objClass == InetSocketAddress.class) {
            InetSocketAddress address = (InetSocketAddress) object;
            InetAddress inetAddress = address.getAddress();
            out.write(123);
            if (inetAddress != null) {
                out.writeFieldName("address");
                serializer.write((Object) inetAddress);
                out.write(44);
            }
            out.writeFieldName("port");
            out.writeInt(address.getPort());
            out.write(125);
            return;
        } else if (object instanceof File) {
            strVal = ((File) object).getPath();
        } else if (object instanceof InetAddress) {
            strVal = ((InetAddress) object).getHostAddress();
        } else if (object instanceof TimeZone) {
            strVal = ((TimeZone) object).getID();
        } else if (object instanceof Currency) {
            strVal = ((Currency) object).getCurrencyCode();
        } else if (object instanceof JSONStreamAware) {
            ((JSONStreamAware) object).writeJSONString(out);
            return;
        } else if (object instanceof Iterator) {
            writeIterator(serializer, out, (Iterator) object);
            return;
        } else if (object instanceof Iterable) {
            writeIterator(serializer, out, ((Iterable) object).iterator());
            return;
        } else if (object instanceof Map.Entry) {
            Map.Entry entry = (Map.Entry) object;
            Object objKey = entry.getKey();
            Object objVal = entry.getValue();
            if (objKey instanceof String) {
                String key = (String) objKey;
                if (objVal instanceof String) {
                    out.writeFieldValueStringWithDoubleQuote('{', key, (String) objVal);
                } else {
                    out.write(123);
                    out.writeFieldName(key);
                    serializer.write(objVal);
                }
            } else {
                out.write(123);
                serializer.write(objKey);
                out.write(58);
                serializer.write(objVal);
            }
            out.write(125);
            return;
        } else if (object.getClass().getName().equals("net.sf.json.JSONNull")) {
            out.writeNull();
            return;
        } else {
            throw new JSONException("not support class : " + objClass);
        }
        out.writeString(strVal);
    }

    /* access modifiers changed from: protected */
    public void writeIterator(JSONSerializer serializer, SerializeWriter out, Iterator<?> it) {
        int i = 0;
        out.write(91);
        while (it.hasNext()) {
            if (i != 0) {
                out.write(44);
            }
            serializer.write(it.next());
            i++;
        }
        out.write(93);
    }

    public <T> T deserialze(DefaultJSONParser parser, Type clazz, Object fieldName) {
        Object objVal;
        String strVal;
        JSONLexer lexer = parser.lexer;
        if (clazz != InetSocketAddress.class) {
            if (parser.resolveStatus == 2) {
                parser.resolveStatus = 0;
                parser.accept(16);
                if (lexer.token() != 4) {
                    throw new JSONException("syntax error");
                } else if (!RtCmdHandler.KEY_VAL.equals(lexer.stringVal())) {
                    throw new JSONException("syntax error");
                } else {
                    lexer.nextToken();
                    parser.accept(17);
                    objVal = parser.parse();
                    parser.accept(13);
                }
            } else {
                objVal = parser.parse();
            }
            if (objVal == null) {
                strVal = null;
            } else if (objVal instanceof String) {
                strVal = (String) objVal;
            } else if ((objVal instanceof JSONObject) && clazz == Map.Entry.class) {
                return ((JSONObject) objVal).entrySet().iterator().next();
            } else {
                throw new JSONException("expect string");
            }
            if (strVal == null || strVal.length() == 0) {
                return null;
            }
            if (clazz == UUID.class) {
                return UUID.fromString(strVal);
            }
            if (clazz == URI.class) {
                return URI.create(strVal);
            }
            if (clazz == URL.class) {
                try {
                    return new URL(strVal);
                } catch (MalformedURLException e) {
                    throw new JSONException("create url error", e);
                }
            } else if (clazz == Pattern.class) {
                return Pattern.compile(strVal);
            } else {
                if (clazz == Locale.class) {
                    String[] items = strVal.split("_");
                    if (items.length == 1) {
                        return new Locale(items[0]);
                    }
                    if (items.length == 2) {
                        return new Locale(items[0], items[1]);
                    }
                    return new Locale(items[0], items[1], items[2]);
                } else if (clazz == SimpleDateFormat.class) {
                    SimpleDateFormat dateFormat = new SimpleDateFormat(strVal, lexer.getLocale());
                    dateFormat.setTimeZone(lexer.getTimeZone());
                    return dateFormat;
                } else if (clazz == InetAddress.class || clazz == Inet4Address.class || clazz == Inet6Address.class) {
                    try {
                        return InetAddress.getByName(strVal);
                    } catch (UnknownHostException e2) {
                        throw new JSONException("deserialize inet adress error", e2);
                    }
                } else if (clazz == File.class) {
                    return new File(strVal);
                } else {
                    if (clazz == TimeZone.class) {
                        return TimeZone.getTimeZone(strVal);
                    }
                    if (clazz instanceof ParameterizedType) {
                        clazz = ((ParameterizedType) clazz).getRawType();
                    }
                    if (clazz == Class.class) {
                        return TypeUtils.loadClass(strVal, parser.getConfig().getDefaultClassLoader());
                    }
                    if (clazz == Charset.class) {
                        return Charset.forName(strVal);
                    }
                    if (clazz == Currency.class) {
                        return Currency.getInstance(strVal);
                    }
                    if (clazz == JSONPath.class) {
                        return new JSONPath(strVal);
                    }
                    String className = clazz.getTypeName();
                    if (className.equals("java.nio.file.Path")) {
                        try {
                            if (method_paths_get == null && !method_paths_get_error) {
                                method_paths_get = TypeUtils.loadClass("java.nio.file.Paths").getMethod("get", new Class[]{String.class, String[].class});
                            }
                            if (method_paths_get != null) {
                                return method_paths_get.invoke((Object) null, new Object[]{strVal, new String[0]});
                            }
                            throw new JSONException("Path deserialize erorr");
                        } catch (NoSuchMethodException e3) {
                            method_paths_get_error = true;
                        } catch (IllegalAccessException ex) {
                            throw new JSONException("Path deserialize erorr", ex);
                        } catch (InvocationTargetException ex2) {
                            throw new JSONException("Path deserialize erorr", ex2);
                        }
                    }
                    throw new JSONException("MiscCodec not support " + className);
                }
            }
        } else if (lexer.token() == 8) {
            lexer.nextToken();
            return null;
        } else {
            parser.accept(12);
            InetAddress address = null;
            int port = 0;
            while (true) {
                String key = lexer.stringVal();
                lexer.nextToken(17);
                if (key.equals("address")) {
                    parser.accept(17);
                    address = (InetAddress) parser.parseObject(InetAddress.class);
                } else if (key.equals("port")) {
                    parser.accept(17);
                    if (lexer.token() != 2) {
                        throw new JSONException("port is not int");
                    }
                    port = lexer.intValue();
                    lexer.nextToken();
                } else {
                    parser.accept(17);
                    parser.parse();
                }
                if (lexer.token() == 16) {
                    lexer.nextToken();
                } else {
                    parser.accept(13);
                    return new InetSocketAddress(address, port);
                }
            }
        }
    }

    public int getFastMatchToken() {
        return 4;
    }
}
