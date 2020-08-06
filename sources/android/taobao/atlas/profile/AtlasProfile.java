package android.taobao.atlas.profile;

import java.util.HashMap;
import java.util.Map;

public class AtlasProfile {
    private static final Map<String, AtlasProfile> cacheMap = new HashMap();
    private long start;
    private String tag;

    private AtlasProfile(String tag2) {
        this.tag = tag2;
    }

    public static AtlasProfile profile(String tag2) {
        synchronized (cacheMap) {
            if (cacheMap.containsKey(tag2)) {
                AtlasProfile atlasProfile = cacheMap.get(tag2);
                return atlasProfile;
            }
            AtlasProfile atlasProfile2 = new AtlasProfile(tag2);
            cacheMap.put(tag2, atlasProfile2);
            return atlasProfile2;
        }
    }

    public void start() {
        this.start = System.currentTimeMillis();
    }

    public String stop() {
        return String.format("%s |cost: %sms", new Object[]{this.tag, String.valueOf(System.currentTimeMillis() - this.start)});
    }
}
