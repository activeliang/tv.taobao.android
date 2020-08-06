package com.taobao.orange.model;

import java.io.Serializable;

public class IndexAckDO implements Serializable {
    public String indexId;
    public String md5;
    public String updateTime;

    public IndexAckDO(String indexId2, String updateTime2, String md52) {
        this.indexId = indexId2;
        this.updateTime = updateTime2;
        this.md5 = md52;
    }

    public String toString() {
        StringBuilder sb = new StringBuilder("IndexAckDO{");
        sb.append("indexId='").append(this.indexId).append('\'');
        sb.append(", updateTime='").append(this.updateTime).append('\'');
        sb.append(", md5='").append(this.md5).append('\'');
        sb.append('}');
        return sb.toString();
    }
}
