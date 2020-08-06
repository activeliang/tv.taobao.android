package mtopsdk.mtop.util;

import com.alibaba.fastjson.JSON;
import java.lang.reflect.Field;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;
import mtopsdk.common.util.SymbolExpUtil;
import mtopsdk.common.util.TBSdkLog;
import mtopsdk.mtop.domain.IMTOPDataObject;
import mtopsdk.mtop.domain.MtopRequest;

public class ReflectUtil {
    private static final String API_NAME = "API_NAME";
    private static final String NEED_ECODE = "NEED_ECODE";
    private static final String NEED_SESSION = "NEED_SESSION";
    private static final String ORIGINALJSON = "ORIGINALJSON";
    private static final String SERIAL_VERSION_UID = "serialVersionUID";
    private static final String TAG = "mtopsdk.ReflectUtil";
    private static final String VERSION = "VERSION";

    public static MtopRequest convertToMtopRequest(Object input) {
        MtopRequest mtopRequest = new MtopRequest();
        if (input != null) {
            parseParams(mtopRequest, input);
        }
        return mtopRequest;
    }

    public static MtopRequest convertToMtopRequest(IMTOPDataObject inputDO) {
        MtopRequest mtopRequest = new MtopRequest();
        if (inputDO != null) {
            parseParams(mtopRequest, inputDO);
        }
        return mtopRequest;
    }

    private static void parseParams(MtopRequest mtopRequest, Object inputDO) {
        try {
            Map<String, String> params = new HashMap<>();
            Class cls = inputDO.getClass();
            HashSet<Field> fieldSet = new HashSet<>();
            fieldSet.addAll(Arrays.asList(cls.getFields()));
            fieldSet.addAll(Arrays.asList(cls.getDeclaredFields()));
            Iterator i$ = fieldSet.iterator();
            while (i$.hasNext()) {
                Field field = i$.next();
                String fieldName = field.getName();
                if (fieldName.indexOf(SymbolExpUtil.SYMBOL_DOLLAR) == -1 && !fieldName.equals(SERIAL_VERSION_UID) && !fieldName.equals(ORIGINALJSON)) {
                    field.setAccessible(true);
                    if (fieldName.equals(API_NAME)) {
                        Object apiName = field.get(inputDO);
                        if (apiName != null) {
                            mtopRequest.setApiName(apiName.toString());
                        }
                    } else if (fieldName.equals(VERSION)) {
                        Object version = field.get(inputDO);
                        if (version != null) {
                            mtopRequest.setVersion(version.toString());
                        }
                    } else if (fieldName.equals(NEED_ECODE)) {
                        Boolean b = Boolean.valueOf(field.getBoolean(inputDO));
                        mtopRequest.setNeedEcode(b != null && b.booleanValue());
                    } else if (fieldName.equals(NEED_SESSION)) {
                        Boolean b2 = Boolean.valueOf(field.getBoolean(inputDO));
                        mtopRequest.setNeedSession(b2 != null && b2.booleanValue());
                    } else {
                        Object obj = field.get(inputDO);
                        if (obj != null) {
                            if (obj instanceof String) {
                                params.put(fieldName, obj.toString());
                            } else {
                                params.put(fieldName, JSON.toJSONString(obj));
                            }
                        }
                    }
                }
            }
            mtopRequest.dataParams = params;
            mtopRequest.setData(convertMapToDataStr(params));
        } catch (Exception e) {
            TBSdkLog.e(TAG, "parseParams failed.", (Throwable) e);
        }
    }

    public static String convertMapToDataStr(Map<String, String> map) {
        StringBuilder dataStr = new StringBuilder(64);
        dataStr.append("{");
        if (map != null && !map.isEmpty()) {
            for (Map.Entry<String, String> entry : map.entrySet()) {
                String key = entry.getKey();
                String value = entry.getValue();
                if (!(key == null || value == null)) {
                    try {
                        dataStr.append(JSON.toJSONString(key));
                        dataStr.append(SymbolExpUtil.SYMBOL_COLON);
                        dataStr.append(JSON.toJSONString(value));
                        dataStr.append(",");
                    } catch (Throwable e) {
                        StringBuilder log = new StringBuilder(64);
                        log.append("[convertMapToDataStr] convert key=").append(key);
                        log.append(",value=").append(value).append(" to dataStr error.");
                        TBSdkLog.e(TAG, log.toString(), e);
                    }
                }
            }
            int length = dataStr.length();
            if (length > 1) {
                dataStr.deleteCharAt(length - 1);
            }
        }
        dataStr.append("}");
        return dataStr.toString();
    }

    @Deprecated
    public static String converMapToDataStr(Map<String, String> map) {
        return convertMapToDataStr(map);
    }

    @Deprecated
    public static Map<String, String> parseDataParams(IMTOPDataObject input) {
        if (input == null) {
            return new HashMap();
        }
        return parseFields(input, input.getClass());
    }

    @Deprecated
    private static Map<String, String> parseDataParams(Object input) {
        if (input == null) {
            return new HashMap();
        }
        return parseFields(input, input.getClass());
    }

    @Deprecated
    private static Map<String, String> parseFields(Object input, Class<?> clz) {
        HashMap<String, String> dataParams = new HashMap<>();
        parseFieldsToMap(input, clz.getDeclaredFields(), dataParams, false);
        parseFieldsToMap(input, clz.getFields(), dataParams, true);
        return dataParams;
    }

    @Deprecated
    private static void parseFieldsToMap(Object input, Field[] fields, HashMap<String, String> dataParams, boolean checkFieldInMap) {
        if (fields != null && fields.length != 0) {
            String fieldName = null;
            int i = 0;
            while (i < fields.length) {
                Object value = null;
                try {
                    fieldName = fields[i].getName();
                    if (excludeField(fieldName, dataParams, checkFieldInMap)) {
                        i++;
                    } else {
                        fields[i].setAccessible(true);
                        value = fields[i].get(input);
                        if (value != null) {
                            try {
                                if (value instanceof String) {
                                    dataParams.put(fieldName, value.toString());
                                } else {
                                    dataParams.put(fieldName, JSON.toJSONString(value));
                                }
                            } catch (Throwable e) {
                                TBSdkLog.e(TAG, "[parseFieldsToMap]transform biz param to json string error.---" + e.toString());
                            }
                        }
                        i++;
                    }
                } catch (Throwable e2) {
                    TBSdkLog.e(TAG, "[parseFieldsToMap]get biz param error through reflection.---" + e2.toString());
                }
            }
        }
    }

    @Deprecated
    private static boolean excludeField(String fieldName, HashMap<String, String> dataParams, boolean checkFieldInMap) {
        if (fieldName.indexOf(SymbolExpUtil.SYMBOL_DOLLAR) != -1 || API_NAME.equals(fieldName) || VERSION.equals(fieldName) || NEED_ECODE.equals(fieldName) || NEED_SESSION.equals(fieldName) || SERIAL_VERSION_UID.equalsIgnoreCase(fieldName) || ORIGINALJSON.equalsIgnoreCase(fieldName)) {
            return true;
        }
        if (!checkFieldInMap || !dataParams.containsKey(fieldName)) {
            return false;
        }
        return true;
    }

    @Deprecated
    public static void parseUrlParams(MtopRequest request, Object input) {
        if (input != null) {
            Object apiObject = getFieldValueByName(API_NAME, input);
            if (apiObject != null) {
                request.setApiName(apiObject.toString());
            }
            Object versionObject = getFieldValueByName(VERSION, input);
            if (versionObject != null) {
                request.setVersion(versionObject.toString());
            }
            if (needEcode(input)) {
                request.setNeedEcode(true);
            }
            if (needSession(input)) {
                request.setNeedSession(true);
            }
        }
    }

    @Deprecated
    public static Object getFieldValueByName(String fieldName, Object o) {
        Object obj = null;
        if (o != null && fieldName != null) {
            Field[] fs = o.getClass().getDeclaredFields();
            int i = 0;
            while (i < fs.length) {
                Field f = fs[i];
                f.setAccessible(true);
                if (f.getName().equals(fieldName)) {
                    try {
                        obj = f.get(o);
                        break;
                    } catch (IllegalArgumentException e) {
                        TBSdkLog.e(TAG, e.toString());
                    } catch (IllegalAccessException e2) {
                        TBSdkLog.e(TAG, e2.toString());
                    }
                } else {
                    i++;
                }
            }
        }
        return obj;
    }

    @Deprecated
    public static boolean needEcode(Object inputObj) {
        Object o = getFieldValueByName(NEED_ECODE, inputObj);
        Boolean need = false;
        if (o != null) {
            need = (Boolean) o;
        }
        return need.booleanValue();
    }

    @Deprecated
    public static boolean needSession(Object inputObj) {
        Object o = getFieldValueByName(NEED_SESSION, inputObj);
        Boolean need = false;
        if (o != null) {
            need = (Boolean) o;
        }
        return need.booleanValue();
    }

    @Deprecated
    public static boolean needJsonType(Object inputObj) {
        Object o = getFieldValueByName(ORIGINALJSON, inputObj);
        Boolean need = false;
        if (o != null) {
            need = (Boolean) o;
        }
        return need.booleanValue();
    }
}
