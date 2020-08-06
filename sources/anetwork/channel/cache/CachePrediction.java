package anetwork.channel.cache;

import java.util.Map;

public interface CachePrediction {
    boolean handleCache(String str, Map<String, String> map);
}
