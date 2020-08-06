package anetwork.network.cache;

import java.io.Serializable;

public class CacheBlockConfig implements Serializable {
    private static final long serialVersionUID = -2567271693706129850L;
    public String blockName;
    public long blockSize;
    public boolean isCompress;
    public boolean isEncrypt;
    public boolean isRemovable;

    public CacheBlockConfig(String blockName2, long blockSize2, boolean isCompress2, boolean isEncrypt2, boolean isRemovable2) {
        this.blockName = blockName2;
        this.blockSize = blockSize2;
        this.isCompress = isCompress2;
        this.isEncrypt = isEncrypt2;
        this.isRemovable = isRemovable2;
    }

    public CacheBlockConfig() {
    }

    public String toString() {
        StringBuilder sb = new StringBuilder(128);
        sb.append("CacheBlockConfig [blockName=").append(this.blockName);
        sb.append(", blockSize=").append(this.blockSize);
        sb.append(", isCompress=").append(this.isCompress);
        sb.append(", isEncrypt=").append(this.isEncrypt);
        sb.append(", isRemovable=").append(this.isRemovable);
        sb.append("]");
        return sb.toString();
    }
}
