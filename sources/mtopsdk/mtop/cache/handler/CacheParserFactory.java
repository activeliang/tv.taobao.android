package mtopsdk.mtop.cache.handler;

import anetwork.network.cache.RpcCache;

public class CacheParserFactory {
    public static ICacheParser createCacheParser(RpcCache.CacheStatus cacheStatus) {
        ICacheParser cacheParser;
        if (cacheStatus == null) {
            return new EmptyCacheParser();
        }
        switch (cacheStatus) {
            case FRESH:
                cacheParser = new FreshCacheParser();
                break;
            case NEED_UPDATE:
                cacheParser = new ExpiredCacheParser();
                break;
            default:
                cacheParser = new EmptyCacheParser();
                break;
        }
        return cacheParser;
    }
}
