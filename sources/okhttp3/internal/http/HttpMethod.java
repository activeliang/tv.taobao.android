package okhttp3.internal.http;

import anet.channel.request.Request;

public final class HttpMethod {
    public static boolean invalidatesCache(String method) {
        return method.equals("POST") || method.equals("PATCH") || method.equals(Request.Method.PUT) || method.equals("DELETE") || method.equals("MOVE");
    }

    public static boolean requiresRequestBody(String method) {
        return method.equals("POST") || method.equals(Request.Method.PUT) || method.equals("PATCH") || method.equals("PROPPATCH") || method.equals("REPORT");
    }

    public static boolean permitsRequestBody(String method) {
        return !method.equals("GET") && !method.equals(Request.Method.HEAD);
    }

    public static boolean redirectsWithBody(String method) {
        return method.equals("PROPFIND");
    }

    public static boolean redirectsToGet(String method) {
        return !method.equals("PROPFIND");
    }

    private HttpMethod() {
    }
}
