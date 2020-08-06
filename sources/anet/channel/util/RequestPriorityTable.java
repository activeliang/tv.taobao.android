package anet.channel.util;

import anet.channel.request.Request;
import java.util.HashMap;
import java.util.Map;

public class RequestPriorityTable {
    private static Map<String, Integer> extPriorityMap = new HashMap();

    static {
        extPriorityMap.put("jar", 2);
        extPriorityMap.put("json", 3);
        extPriorityMap.put("html", 4);
        extPriorityMap.put("htm", 4);
        extPriorityMap.put("css", 5);
        extPriorityMap.put("js", 5);
        extPriorityMap.put("webp", 6);
        extPriorityMap.put("png", 6);
        extPriorityMap.put("jpg", 6);
        extPriorityMap.put("do", 6);
        extPriorityMap.put("zip", 9);
        extPriorityMap.put("bin", 9);
    }

    public static int lookup(Request request) {
        Integer priority;
        if (request == null) {
            throw new NullPointerException("url is null!");
        } else if (request.getHeaders().containsKey("x-pv")) {
            return 1;
        } else {
            String ext = HttpHelper.trySolveFileExtFromUrlPath(request.getHttpUrl().path());
            if (ext == null || (priority = extPriorityMap.get(ext)) == null) {
                return 6;
            }
            return priority.intValue();
        }
    }
}
