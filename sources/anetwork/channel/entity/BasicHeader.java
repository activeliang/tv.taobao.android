package anetwork.channel.entity;

import anetwork.channel.Header;

public class BasicHeader implements Header {
    private final String name;
    private final String value;

    public BasicHeader(String name2, String value2) {
        if (name2 == null) {
            throw new IllegalArgumentException("Name may not be null");
        }
        this.name = name2;
        this.value = value2;
    }

    public String getName() {
        return this.name;
    }

    public String getValue() {
        return this.value;
    }
}
