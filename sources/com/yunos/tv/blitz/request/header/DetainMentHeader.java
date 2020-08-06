package com.yunos.tv.blitz.request.header;

import org.apache.http.Header;
import org.apache.http.HeaderElement;
import org.apache.http.ParseException;

public class DetainMentHeader implements Header {
    private String mHeaderName = "";
    private String mHeaderValue = "";

    public HeaderElement[] getElements() throws ParseException {
        return null;
    }

    public String getName() {
        return this.mHeaderName;
    }

    public String getValue() {
        return this.mHeaderValue;
    }

    public void setHeaderNameAndValue(String name, String value) {
        this.mHeaderName = name;
        this.mHeaderValue = value;
    }

    public String toString() {
        return "DetainMentHeader [mHeaderName=" + this.mHeaderName + ", mHeaderValue=" + this.mHeaderValue + "]";
    }
}
