package anet.channel.util;

import android.taobao.windvane.jsbridge.utils.WVUtils;
import java.net.MalformedURLException;
import java.net.URL;
import mtopsdk.common.util.SymbolExpUtil;

public class HttpUrl {
    private String host;
    private volatile boolean isSchemeLocked = false;
    private String path;
    private int port;
    private String scheme;
    private String schemeAndHost;
    private String simpleUrl;
    private String url;

    private HttpUrl() {
    }

    public HttpUrl(HttpUrl rhs) {
        this.scheme = rhs.scheme;
        this.host = rhs.host;
        this.path = rhs.path;
        this.url = rhs.url;
        this.simpleUrl = rhs.simpleUrl;
        this.schemeAndHost = rhs.schemeAndHost;
        this.isSchemeLocked = rhs.isSchemeLocked;
    }

    /* JADX WARNING: Removed duplicated region for block: B:66:0x00f4  */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public static anet.channel.util.HttpUrl parse(java.lang.String r17) {
        /*
            boolean r1 = android.text.TextUtils.isEmpty(r17)
            if (r1 == 0) goto L_0x0008
            r10 = 0
        L_0x0007:
            return r10
        L_0x0008:
            java.lang.String r17 = r17.trim()
            anet.channel.util.HttpUrl r10 = new anet.channel.util.HttpUrl
            r10.<init>()
            r0 = r17
            r10.url = r0
            r3 = 0
            java.lang.String r1 = "//"
            r0 = r17
            boolean r1 = r0.startsWith(r1)
            if (r1 == 0) goto L_0x0056
            r1 = 0
            r10.scheme = r1
        L_0x0024:
            int r11 = r17.length()
            int r3 = r3 + 2
            r9 = r3
        L_0x002b:
            if (r3 >= r11) goto L_0x004b
            r0 = r17
            char r7 = r0.charAt(r3)
            r1 = 47
            if (r7 == r1) goto L_0x0043
            r1 = 58
            if (r7 == r1) goto L_0x0043
            r1 = 63
            if (r7 == r1) goto L_0x0043
            r1 = 35
            if (r7 != r1) goto L_0x008d
        L_0x0043:
            r0 = r17
            java.lang.String r1 = r0.substring(r9, r3)
            r10.host = r1
        L_0x004b:
            if (r3 != r11) goto L_0x0090
            r0 = r17
            java.lang.String r1 = r0.substring(r9)
            r10.host = r1
            goto L_0x0007
        L_0x0056:
            r2 = 1
            java.lang.String r4 = "https:"
            r5 = 0
            r6 = 6
            r1 = r17
            boolean r1 = r1.regionMatches(r2, r3, r4, r5, r6)
            if (r1 == 0) goto L_0x0070
            java.lang.String r1 = "https"
            r10.scheme = r1
            r1 = 443(0x1bb, float:6.21E-43)
            r10.port = r1
            int r3 = r3 + 6
            goto L_0x0024
        L_0x0070:
            r2 = 1
            java.lang.String r4 = "http:"
            r5 = 0
            r6 = 5
            r1 = r17
            boolean r1 = r1.regionMatches(r2, r3, r4, r5, r6)
            if (r1 == 0) goto L_0x008a
            java.lang.String r1 = "http"
            r10.scheme = r1
            r1 = 80
            r10.port = r1
            int r3 = r3 + 5
            goto L_0x0024
        L_0x008a:
            r10 = 0
            goto L_0x0007
        L_0x008d:
            int r3 = r3 + 1
            goto L_0x002b
        L_0x0090:
            r15 = 0
            r14 = 0
        L_0x0092:
            if (r3 >= r11) goto L_0x00b2
            r0 = r17
            char r7 = r0.charAt(r3)
            r1 = 58
            if (r7 != r1) goto L_0x00a5
            if (r15 != 0) goto L_0x00a5
            int r15 = r3 + 1
        L_0x00a2:
            int r3 = r3 + 1
            goto L_0x0092
        L_0x00a5:
            r1 = 47
            if (r7 == r1) goto L_0x00b1
            r1 = 35
            if (r7 == r1) goto L_0x00b1
            r1 = 63
            if (r7 != r1) goto L_0x00a2
        L_0x00b1:
            r14 = r3
        L_0x00b2:
            if (r15 == 0) goto L_0x00d6
            if (r14 == 0) goto L_0x00d0
        L_0x00b6:
            r0 = r17
            java.lang.String r16 = r0.substring(r15, r14)
            int r1 = java.lang.Integer.parseInt(r16)     // Catch:{ NumberFormatException -> 0x00d2 }
            r10.port = r1     // Catch:{ NumberFormatException -> 0x00d2 }
            int r1 = r10.port     // Catch:{ NumberFormatException -> 0x00d2 }
            if (r1 <= 0) goto L_0x00cd
            int r1 = r10.port     // Catch:{ NumberFormatException -> 0x00d2 }
            r2 = 65535(0xffff, float:9.1834E-41)
            if (r1 <= r2) goto L_0x00d6
        L_0x00cd:
            r10 = 0
            goto L_0x0007
        L_0x00d0:
            r14 = r11
            goto L_0x00b6
        L_0x00d2:
            r8 = move-exception
            r10 = 0
            goto L_0x0007
        L_0x00d6:
            r13 = 0
            r12 = 0
        L_0x00d8:
            if (r3 >= r11) goto L_0x00f5
            r0 = r17
            char r7 = r0.charAt(r3)
            r1 = 47
            if (r7 != r1) goto L_0x00ea
            if (r13 != 0) goto L_0x00ea
            r13 = r3
        L_0x00e7:
            int r3 = r3 + 1
            goto L_0x00d8
        L_0x00ea:
            r1 = 63
            if (r7 == r1) goto L_0x00f2
            r1 = 35
            if (r7 != r1) goto L_0x00e7
        L_0x00f2:
            if (r13 == 0) goto L_0x00f5
            r12 = r3
        L_0x00f5:
            if (r13 == 0) goto L_0x0113
            if (r12 == 0) goto L_0x010f
            r1 = r12
        L_0x00fa:
            r0 = r17
            java.lang.String r1 = r0.substring(r13, r1)
            r10.path = r1
            r1 = 0
            if (r12 == 0) goto L_0x0111
        L_0x0105:
            r0 = r17
            java.lang.String r1 = r0.substring(r1, r12)
            r10.simpleUrl = r1
            goto L_0x0007
        L_0x010f:
            r1 = r11
            goto L_0x00fa
        L_0x0111:
            r12 = r11
            goto L_0x0105
        L_0x0113:
            java.lang.String r1 = ""
            r10.path = r1
            r1 = 0
            r0 = r17
            java.lang.String r1 = r0.substring(r1, r3)
            r10.simpleUrl = r1
            goto L_0x0007
        */
        throw new UnsupportedOperationException("Method not decompiled: anet.channel.util.HttpUrl.parse(java.lang.String):anet.channel.util.HttpUrl");
    }

    public String scheme() {
        return this.scheme;
    }

    public String host() {
        return this.host;
    }

    public String path() {
        return this.path;
    }

    public int getPort() {
        return this.port;
    }

    public String key() {
        if (this.schemeAndHost == null) {
            this.schemeAndHost = StringUtils.concatString(this.scheme, HttpConstant.SCHEME_SPLIT, this.host);
        }
        return this.schemeAndHost;
    }

    public String urlString() {
        return this.url;
    }

    public String simpleUrlString() {
        return this.simpleUrl;
    }

    public URL toURL() {
        try {
            return new URL(this.url);
        } catch (MalformedURLException e) {
            return null;
        }
    }

    public boolean containsNonDefaultPort() {
        return ("http".equals(this.scheme) && this.port != 80) || ("https".equals(this.scheme) && this.port != 443);
    }

    public void downgradeSchemeAndLock() {
        this.isSchemeLocked = true;
        if (!"http".equals(this.scheme)) {
            this.scheme = "http";
            this.url = StringUtils.concatString(this.scheme, SymbolExpUtil.SYMBOL_COLON, this.url.substring(this.url.indexOf(WVUtils.URL_SEPARATOR)));
            this.schemeAndHost = null;
        }
    }

    public boolean isSchemeLocked() {
        return this.isSchemeLocked;
    }

    public void lockScheme() {
        this.isSchemeLocked = true;
    }

    public void setScheme(String scheme2) {
        if (!this.isSchemeLocked && !scheme2.equalsIgnoreCase(this.scheme)) {
            this.scheme = scheme2;
            this.url = StringUtils.concatString(scheme2, SymbolExpUtil.SYMBOL_COLON, this.url.substring(this.url.indexOf(WVUtils.URL_SEPARATOR)));
            this.simpleUrl = StringUtils.concatString(scheme2, SymbolExpUtil.SYMBOL_COLON, this.simpleUrl.substring(this.url.indexOf(WVUtils.URL_SEPARATOR)));
            this.schemeAndHost = null;
        }
    }

    public void replaceIpAndPort(String ip, int port2) {
        if (port2 != 0 && ip != null) {
            int i = this.url.indexOf(WVUtils.URL_SEPARATOR) + 2;
            while (i < this.url.length() && this.url.charAt(i) != '/') {
                i++;
            }
            StringBuilder sb = new StringBuilder(this.url.length() + ip.length());
            sb.append(this.scheme).append(HttpConstant.SCHEME_SPLIT).append(ip).append(':').append(port2).append(this.url.substring(i));
            this.url = sb.toString();
        }
    }

    public String toString() {
        return this.url;
    }
}
