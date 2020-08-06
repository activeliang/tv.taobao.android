package com.loc;

/* compiled from: AMapCoreException */
public final class j extends Exception {
    private String a;
    private String b;
    private String c;
    private String d;
    private String e;
    private int f;

    public j(String str) {
        super(str);
        this.a = "未知的错误";
        this.b = "";
        this.c = "";
        this.d = "1900";
        this.e = "UnknownError";
        this.f = -1;
        this.a = str;
        if ("IO 操作异常 - IOException".equals(str)) {
            this.f = 21;
            this.d = "1902";
            this.e = "IOException";
        } else if ("socket 连接异常 - SocketException".equals(str)) {
            this.f = 22;
        } else if ("socket 连接超时 - SocketTimeoutException".equals(str)) {
            this.f = 23;
            this.d = "1802";
            this.e = "SocketTimeoutException";
        } else if ("无效的参数 - IllegalArgumentException".equals(str)) {
            this.f = 24;
            this.d = "1901";
            this.e = "IllegalArgumentException";
        } else if ("空指针异常 - NullPointException".equals(str)) {
            this.f = 25;
            this.d = "1903";
            this.e = "NullPointException";
        } else if ("url异常 - MalformedURLException".equals(str)) {
            this.f = 26;
            this.d = "1803";
            this.e = "MalformedURLException";
        } else if ("未知主机 - UnKnowHostException".equals(str)) {
            this.f = 27;
            this.d = "1804";
            this.e = "UnknownHostException";
        } else if ("服务器连接失败 - UnknownServiceException".equals(str)) {
            this.f = 28;
            this.d = "1805";
            this.e = "CannotConnectToHostException";
        } else if ("协议解析错误 - ProtocolException".equals(str)) {
            this.f = 29;
            this.d = "1801";
            this.e = "ProtocolException";
        } else if ("http连接失败 - ConnectionException".equals(str)) {
            this.f = 30;
            this.d = "1806";
            this.e = "ConnectionException";
        } else if ("未知的错误".equals(str)) {
            this.f = 31;
        } else if ("key鉴权失败".equals(str)) {
            this.f = 32;
        } else if ("requeust is null".equals(str)) {
            this.f = 1;
        } else if ("request url is empty".equals(str)) {
            this.f = 2;
        } else if ("response is null".equals(str)) {
            this.f = 3;
        } else if ("thread pool has exception".equals(str)) {
            this.f = 4;
        } else if ("sdk name is invalid".equals(str)) {
            this.f = 5;
        } else if ("sdk info is null".equals(str)) {
            this.f = 6;
        } else if ("sdk packages is null".equals(str)) {
            this.f = 7;
        } else if ("线程池为空".equals(str)) {
            this.f = 8;
        } else if ("获取对象错误".equals(str)) {
            this.f = 101;
        } else {
            this.f = -1;
        }
    }

    public j(String str, String str2, String str3) {
        this(str);
        this.b = str2;
        this.c = str3;
    }

    public final String a() {
        return this.a;
    }

    public final void a(int i) {
        this.f = i;
    }

    public final String b() {
        return this.d;
    }

    public final String c() {
        return this.e;
    }

    public final String d() {
        return this.b;
    }

    public final String e() {
        return this.c;
    }

    public final int f() {
        return this.f;
    }
}
