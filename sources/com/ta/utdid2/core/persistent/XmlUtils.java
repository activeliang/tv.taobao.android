package com.ta.utdid2.core.persistent;

import android.util.Xml;
import com.bftv.fui.constantplugin.Constant;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;
import org.xmlpull.v1.XmlSerializer;

class XmlUtils {
    XmlUtils() {
    }

    public static final void writeMapXml(Map val, OutputStream out) throws XmlPullParserException, IOException {
        XmlSerializer serializer = new FastXmlSerializer();
        serializer.setOutput(out, "utf-8");
        serializer.startDocument((String) null, true);
        serializer.setFeature("http://xmlpull.org/v1/doc/features.html#indent-output", true);
        writeMapXml(val, (String) null, serializer);
        serializer.endDocument();
    }

    public static final void writeMapXml(Map val, String name, XmlSerializer out) throws XmlPullParserException, IOException {
        if (val == null) {
            out.startTag((String) null, Constant.NULL);
            out.endTag((String) null, Constant.NULL);
            return;
        }
        out.startTag((String) null, "map");
        if (name != null) {
            out.attribute((String) null, "name", name);
        }
        for (Map.Entry e : val.entrySet()) {
            writeValueXml(e.getValue(), (String) e.getKey(), out);
        }
        out.endTag((String) null, "map");
    }

    public static final void writeListXml(List val, String name, XmlSerializer out) throws XmlPullParserException, IOException {
        if (val == null) {
            out.startTag((String) null, Constant.NULL);
            out.endTag((String) null, Constant.NULL);
            return;
        }
        out.startTag((String) null, "list");
        if (name != null) {
            out.attribute((String) null, "name", name);
        }
        int N = val.size();
        for (int i = 0; i < N; i++) {
            writeValueXml(val.get(i), (String) null, out);
        }
        out.endTag((String) null, "list");
    }

    public static final void writeByteArrayXml(byte[] val, String name, XmlSerializer out) throws XmlPullParserException, IOException {
        int i;
        if (val == null) {
            out.startTag((String) null, Constant.NULL);
            out.endTag((String) null, Constant.NULL);
            return;
        }
        out.startTag((String) null, "byte-array");
        if (name != null) {
            out.attribute((String) null, "name", name);
        }
        out.attribute((String) null, "num", Integer.toString(N));
        StringBuilder sb = new StringBuilder(val.length * 2);
        for (byte b : val) {
            int h = b >> 4;
            sb.append(h >= 10 ? (h + 97) - 10 : h + 48);
            int h2 = b & 255;
            if (h2 >= 10) {
                i = (h2 + 97) - 10;
            } else {
                i = h2 + 48;
            }
            sb.append(i);
        }
        out.text(sb.toString());
        out.endTag((String) null, "byte-array");
    }

    public static final void writeIntArrayXml(int[] val, String name, XmlSerializer out) throws XmlPullParserException, IOException {
        if (val == null) {
            out.startTag((String) null, Constant.NULL);
            out.endTag((String) null, Constant.NULL);
            return;
        }
        out.startTag((String) null, "int-array");
        if (name != null) {
            out.attribute((String) null, "name", name);
        }
        out.attribute((String) null, "num", Integer.toString(N));
        for (int num : val) {
            out.startTag((String) null, "item");
            out.attribute((String) null, "value", Integer.toString(num));
            out.endTag((String) null, "item");
        }
        out.endTag((String) null, "int-array");
    }

    public static final void writeValueXml(Object v, String name, XmlSerializer out) throws XmlPullParserException, IOException {
        String typeStr;
        if (v == null) {
            out.startTag((String) null, Constant.NULL);
            if (name != null) {
                out.attribute((String) null, "name", name);
            }
            out.endTag((String) null, Constant.NULL);
        } else if (v instanceof String) {
            out.startTag((String) null, "string");
            if (name != null) {
                out.attribute((String) null, "name", name);
            }
            out.text(v.toString());
            out.endTag((String) null, "string");
        } else {
            if (v instanceof Integer) {
                typeStr = "int";
            } else if (v instanceof Long) {
                typeStr = "long";
            } else if (v instanceof Float) {
                typeStr = "float";
            } else if (v instanceof Double) {
                typeStr = "double";
            } else if (v instanceof Boolean) {
                typeStr = "boolean";
            } else if (v instanceof byte[]) {
                writeByteArrayXml((byte[]) v, name, out);
                return;
            } else if (v instanceof int[]) {
                writeIntArrayXml((int[]) v, name, out);
                return;
            } else if (v instanceof Map) {
                writeMapXml((Map) v, name, out);
                return;
            } else if (v instanceof List) {
                writeListXml((List) v, name, out);
                return;
            } else if (v instanceof CharSequence) {
                out.startTag((String) null, "string");
                if (name != null) {
                    out.attribute((String) null, "name", name);
                }
                out.text(v.toString());
                out.endTag((String) null, "string");
                return;
            } else {
                throw new RuntimeException("writeValueXml: unable to write value " + v);
            }
            out.startTag((String) null, typeStr);
            if (name != null) {
                out.attribute((String) null, "name", name);
            }
            out.attribute((String) null, "value", v.toString());
            out.endTag((String) null, typeStr);
        }
    }

    public static final HashMap readMapXml(InputStream in) throws XmlPullParserException, IOException {
        XmlPullParser parser = Xml.newPullParser();
        parser.setInput(in, (String) null);
        return (HashMap) readValueXml(parser, new String[1]);
    }

    public static final HashMap readThisMapXml(XmlPullParser parser, String endTag, String[] name) throws XmlPullParserException, IOException {
        HashMap map = new HashMap();
        int eventType = parser.getEventType();
        do {
            if (eventType == 2) {
                Object val = readThisValueXml(parser, name);
                if (name[0] != null) {
                    map.put(name[0], val);
                } else {
                    throw new XmlPullParserException("Map value without name attribute: " + parser.getName());
                }
            } else if (eventType == 3) {
                if (parser.getName().equals(endTag)) {
                    return map;
                }
                throw new XmlPullParserException("Expected " + endTag + " end tag at: " + parser.getName());
            }
            eventType = parser.next();
        } while (eventType != 1);
        throw new XmlPullParserException("Document ended before " + endTag + " end tag");
    }

    public static final ArrayList readThisListXml(XmlPullParser parser, String endTag, String[] name) throws XmlPullParserException, IOException {
        ArrayList list = new ArrayList();
        int eventType = parser.getEventType();
        do {
            if (eventType == 2) {
                list.add(readThisValueXml(parser, name));
            } else if (eventType == 3) {
                if (parser.getName().equals(endTag)) {
                    return list;
                }
                throw new XmlPullParserException("Expected " + endTag + " end tag at: " + parser.getName());
            }
            eventType = parser.next();
        } while (eventType != 1);
        throw new XmlPullParserException("Document ended before " + endTag + " end tag");
    }

    public static final int[] readThisIntArrayXml(XmlPullParser parser, String endTag, String[] name) throws XmlPullParserException, IOException {
        try {
            int[] array = new int[Integer.parseInt(parser.getAttributeValue((String) null, "num"))];
            int i = 0;
            int eventType = parser.getEventType();
            do {
                if (eventType == 2) {
                    if (parser.getName().equals("item")) {
                        try {
                            array[i] = Integer.parseInt(parser.getAttributeValue((String) null, "value"));
                        } catch (NullPointerException e) {
                            throw new XmlPullParserException("Need value attribute in item");
                        } catch (NumberFormatException e2) {
                            throw new XmlPullParserException("Not a number in value attribute in item");
                        }
                    } else {
                        throw new XmlPullParserException("Expected item tag at: " + parser.getName());
                    }
                } else if (eventType == 3) {
                    if (parser.getName().equals(endTag)) {
                        return array;
                    }
                    if (parser.getName().equals("item")) {
                        i++;
                    } else {
                        throw new XmlPullParserException("Expected " + endTag + " end tag at: " + parser.getName());
                    }
                }
                eventType = parser.next();
            } while (eventType != 1);
            throw new XmlPullParserException("Document ended before " + endTag + " end tag");
        } catch (NullPointerException e3) {
            throw new XmlPullParserException("Need num attribute in byte-array");
        } catch (NumberFormatException e4) {
            throw new XmlPullParserException("Not a number in num attribute in byte-array");
        }
    }

    public static final Object readValueXml(XmlPullParser parser, String[] name) throws XmlPullParserException, IOException {
        int eventType = parser.getEventType();
        while (eventType != 2) {
            if (eventType == 3) {
                throw new XmlPullParserException("Unexpected end tag at: " + parser.getName());
            } else if (eventType == 4) {
                throw new XmlPullParserException("Unexpected text: " + parser.getText());
            } else {
                try {
                    eventType = parser.next();
                    if (eventType == 1) {
                        throw new XmlPullParserException("Unexpected end of document");
                    }
                } catch (Exception e) {
                    throw new XmlPullParserException("Unexpected call next(): " + parser.getName());
                }
            }
        }
        return readThisValueXml(parser, name);
    }

    private static Object readThisValueXml(XmlPullParser parser, String[] name) throws XmlPullParserException, IOException {
        Object obj;
        int eventType;
        String valueName = parser.getAttributeValue((String) null, "name");
        String tagName = parser.getName();
        if (tagName.equals(Constant.NULL)) {
            obj = null;
        } else if (tagName.equals("string")) {
            String value = "";
            while (true) {
                int eventType2 = parser.next();
                if (eventType2 == 1) {
                    throw new XmlPullParserException("Unexpected end of document in <string>");
                } else if (eventType2 == 3) {
                    if (parser.getName().equals("string")) {
                        name[0] = valueName;
                        return value;
                    }
                    throw new XmlPullParserException("Unexpected end tag in <string>: " + parser.getName());
                } else if (eventType2 == 4) {
                    value = value + parser.getText();
                } else if (eventType2 == 2) {
                    throw new XmlPullParserException("Unexpected start tag in <string>: " + parser.getName());
                }
            }
        } else if (tagName.equals("int")) {
            obj = Integer.valueOf(Integer.parseInt(parser.getAttributeValue((String) null, "value")));
        } else if (tagName.equals("long")) {
            obj = Long.valueOf(parser.getAttributeValue((String) null, "value"));
        } else if (tagName.equals("float")) {
            obj = Float.valueOf(parser.getAttributeValue((String) null, "value"));
        } else if (tagName.equals("double")) {
            obj = Double.valueOf(parser.getAttributeValue((String) null, "value"));
        } else if (tagName.equals("boolean")) {
            obj = Boolean.valueOf(parser.getAttributeValue((String) null, "value"));
        } else if (tagName.equals("int-array")) {
            parser.next();
            int[] readThisIntArrayXml = readThisIntArrayXml(parser, "int-array", name);
            name[0] = valueName;
            return readThisIntArrayXml;
        } else if (tagName.equals("map")) {
            parser.next();
            HashMap readThisMapXml = readThisMapXml(parser, "map", name);
            name[0] = valueName;
            return readThisMapXml;
        } else if (tagName.equals("list")) {
            parser.next();
            ArrayList readThisListXml = readThisListXml(parser, "list", name);
            name[0] = valueName;
            return readThisListXml;
        } else {
            throw new XmlPullParserException("Unknown tag: " + tagName);
        }
        do {
            eventType = parser.next();
            if (eventType == 1) {
                throw new XmlPullParserException("Unexpected end of document in <" + tagName + ">");
            } else if (eventType == 3) {
                if (parser.getName().equals(tagName)) {
                    name[0] = valueName;
                    return obj;
                }
                throw new XmlPullParserException("Unexpected end tag in <" + tagName + ">: " + parser.getName());
            } else if (eventType == 4) {
                throw new XmlPullParserException("Unexpected text in <" + tagName + ">: " + parser.getName());
            }
        } while (eventType != 2);
        throw new XmlPullParserException("Unexpected start tag in <" + tagName + ">: " + parser.getName());
    }
}
