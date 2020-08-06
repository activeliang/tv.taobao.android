package mtopsdk.mtop.domain;

import android.support.annotation.NonNull;
import anetwork.network.cache.RpcCache;
import java.io.Serializable;
import mtopsdk.common.util.StringUtils;
import mtopsdk.framework.domain.MtopContext;
import mtopsdk.mtop.cache.CacheManager;

public class ResponseSource implements Serializable {
    private String cacheBlock;
    private String cacheKey;
    public CacheManager cacheManager;
    public MtopResponse cacheResponse;
    public MtopContext mtopContext;
    public boolean requireConnection = true;
    public RpcCache rpcCache = null;
    public String seqNo;

    public ResponseSource(@NonNull MtopContext mtopContext2, @NonNull CacheManager cacheManager2) {
        this.mtopContext = mtopContext2;
        this.cacheManager = cacheManager2;
        this.seqNo = mtopContext2.seqNo;
    }

    public String getCacheKey() {
        if (StringUtils.isNotBlank(this.cacheKey)) {
            return this.cacheKey;
        }
        this.cacheKey = this.cacheManager.getCacheKey(this.mtopContext);
        return this.cacheKey;
    }

    public String getCacheBlock() {
        if (StringUtils.isNotBlank(this.cacheBlock)) {
            return this.cacheBlock;
        }
        this.cacheBlock = this.cacheManager.getBlockName(this.mtopContext.mtopRequest.getKey());
        return this.cacheBlock;
    }
}
