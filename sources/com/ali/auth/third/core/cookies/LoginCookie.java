package com.ali.auth.third.core.cookies;

import java.text.SimpleDateFormat;
import java.util.Locale;
import java.util.TimeZone;

public class LoginCookie {
    public String domain;
    public long expires;
    public boolean httpOnly;
    public String name;
    public String path;
    public boolean secure;
    public String value;

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
        if (this.secure) {
            builder.append("; ");
            builder.append("Secure");
        }
        if (this.httpOnly) {
            builder.append("; ");
            builder.append("HttpOnly");
        }
        return builder.toString();
    }
}
