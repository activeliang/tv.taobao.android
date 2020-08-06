package org.android.spdy;

import java.util.Map;

public class SpdyDataProvider {
    byte[] data;
    public boolean finished;
    Map<String, String> postBody;

    public SpdyDataProvider(byte[] data2) {
        this.finished = true;
        this.data = data2;
        this.postBody = null;
    }

    public SpdyDataProvider(Map<String, String> postBody2) {
        this.finished = true;
        this.data = null;
        this.postBody = postBody2;
    }
}
