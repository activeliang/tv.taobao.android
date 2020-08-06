package mtopsdk.mtop.domain;

import anet.channel.request.Request;

public enum MethodEnum {
    GET("GET"),
    POST("POST"),
    HEAD(Request.Method.HEAD),
    PATCH("PATCH");
    
    private String method;

    public String getMethod() {
        return this.method;
    }

    private MethodEnum(String method2) {
        this.method = method2;
    }
}
