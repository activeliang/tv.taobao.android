package mtopsdk.common.util;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class HeaderHandlerUtil {
    private static final String TAG = "mtopsdk.HeaderHandlerUtil";

    public static List<String> getHeaderFieldByKey(Map<String, List<String>> header, String key) {
        if (header == null || header.isEmpty() || StringUtils.isBlank(key)) {
            return null;
        }
        for (Map.Entry<String, List<String>> entry : header.entrySet()) {
            if (key.equalsIgnoreCase(entry.getKey())) {
                return entry.getValue();
            }
        }
        return null;
    }

    public static String getSingleHeaderFieldByKey(Map<String, List<String>> header, String key) {
        List<String> value = getHeaderFieldByKey(header, key);
        if (value == null || value.isEmpty()) {
            return null;
        }
        return value.get(0);
    }

    public static Map<String, List<String>> cloneHeaderMap(Map<String, List<String>> map) {
        if (map == null) {
            return null;
        }
        if (map.isEmpty()) {
            return Collections.EMPTY_MAP;
        }
        HashMap cloneMap = new HashMap(map.size());
        for (Map.Entry entry : map.entrySet()) {
            cloneMap.put(entry.getKey(), new ArrayList((Collection) entry.getValue()));
        }
        return cloneMap;
    }
}
