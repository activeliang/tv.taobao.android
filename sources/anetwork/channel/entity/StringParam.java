package anetwork.channel.entity;

import anetwork.channel.Param;

public class StringParam implements Param {
    private String key;
    private String value;

    public String getKey() {
        return this.key;
    }

    public String getValue() {
        return this.value;
    }

    public StringParam(String key2, String value2) {
        this.key = key2;
        this.value = value2;
    }
}
