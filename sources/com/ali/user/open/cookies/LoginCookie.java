package com.ali.user.open.cookies;

import android.text.TextUtils;
import java.text.SimpleDateFormat;
import java.util.Locale;
import java.util.TimeZone;
import mtopsdk.common.util.SymbolExpUtil;

public class LoginCookie {
    public boolean discard;
    public String domain;
    public long expires;
    public boolean httpOnly;
    public String name;
    public String path;
    public boolean secure;
    public String value;
    public String version;

    public String toString() {
        StringBuilder builder = new StringBuilder();
        builder.append(this.name);
        builder.append("=");
        builder.append(this.value);
        builder.append("; ");
        builder.append("Domain=");
        builder.append(this.domain);
        if (this.expires > 0) {
            builder.append("; ");
            builder.append("Expires=");
            SimpleDateFormat DATE_FORMAT = new SimpleDateFormat("EEE, dd-MMM-yyyy HH:mm:ss 'GMT'", Locale.ENGLISH);
            DATE_FORMAT.setTimeZone(TimeZone.getTimeZone("GMT+8"));
            builder.append(DATE_FORMAT.format(Long.valueOf(this.expires)));
        }
        builder.append("; ");
        builder.append("Path=");
        builder.append(this.path);
        if (!TextUtils.isEmpty(this.version)) {
            builder.append(SymbolExpUtil.SYMBOL_SEMICOLON);
            builder.append("Version=");
            builder.append(this.version);
        }
        if (this.secure) {
            builder.append("; ");
            builder.append("Secure");
        }
        if (this.httpOnly) {
            builder.append("; ");
            builder.append("HttpOnly");
        }
        if (this.discard) {
            builder.append("; ");
            builder.append("Discard");
        }
        return builder.toString();
    }
}
