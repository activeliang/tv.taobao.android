package com.taobao.orange.model;

import java.io.Serializable;

public class ConfigAckDO implements Serializable {
    public String name;
    public String namespaceId;
    public String updateTime;
    public String version;

    public ConfigAckDO(String name2, String namespaceId2, String updateTime2, String version2) {
        this.name = name2;
        this.namespaceId = namespaceId2;
        this.updateTime = updateTime2;
        this.version = version2;
    }

    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        ConfigAckDO that = (ConfigAckDO) o;
        if (!this.name.equals(that.name) || !this.namespaceId.equals(that.namespaceId) || !this.updateTime.equals(that.updateTime)) {
            return false;
        }
        return this.version.equals(that.version);
    }

    public int hashCode() {
        return (((((this.name.hashCode() * 31) + this.namespaceId.hashCode()) * 31) + this.updateTime.hashCode()) * 31) + this.version.hashCode();
    }
}
