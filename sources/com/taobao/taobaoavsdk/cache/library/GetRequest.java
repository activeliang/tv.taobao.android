package com.taobao.taobaoavsdk.cache.library;

import android.net.Uri;
import android.text.TextUtils;
import com.taobao.taobaoavsdk.cache.PlayerEnvironment;
import com.taobao.taobaoavsdk.util.AndroidUtils;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

class GetRequest {
    private static final Pattern RANGE_HEADER_PATTERN = Pattern.compile("[R,r]ange:[ ]?bytes=(\\d*)-");
    private static final Pattern URL_PATTERN = Pattern.compile("(GET|HEAD) /(.*) HTTP");
    private static final Pattern USER_AGENT_PATTERN = Pattern.compile("User-Agent:(.*;systemName/Android)");
    public final String cdnIp;
    public final int length;
    private final String[] params = {PlayerEnvironment.PLAY_TOKEN_ID, PlayerEnvironment.USE_TBNET_PROXY, PlayerEnvironment.CDN_IP, PlayerEnvironment.VIDEO_LENGTH};
    public final boolean partial;
    public final String playToken;
    public final long rangeOffset;
    public final String uri;
    public final boolean useTBNet;
    public final String userAgent;

    public GetRequest(String request) {
        boolean z = true;
        Preconditions.checkNotNull(request);
        long offset = findRangeOffset(request);
        this.rangeOffset = Math.max(0, offset);
        this.partial = offset < 0 ? false : z;
        String tmp_url = ProxyCacheUtils.decode(findUri(request));
        this.userAgent = findUserAgent(request);
        this.useTBNet = findUseTBNet(tmp_url);
        this.playToken = findPlayToken(tmp_url);
        this.cdnIp = findCDNIp(tmp_url);
        this.length = findLength(tmp_url);
        this.uri = removeParams(tmp_url, this.params);
    }

    private String findCDNIp(String uri2) {
        try {
            return Uri.parse(uri2).getQueryParameter(PlayerEnvironment.CDN_IP);
        } catch (Throwable th) {
            return "";
        }
    }

    private int findLength(String uri2) {
        try {
            return Integer.valueOf(Uri.parse(uri2).getQueryParameter(PlayerEnvironment.VIDEO_LENGTH)).intValue();
        } catch (Throwable th) {
            return Integer.MIN_VALUE;
        }
    }

    private String findPlayToken(String uri2) {
        try {
            return Uri.parse(uri2.replace("+", "%2B")).getQueryParameter(PlayerEnvironment.PLAY_TOKEN_ID);
        } catch (Throwable th) {
            return "";
        }
    }

    public String removeParams(String url, String[] params2) {
        if (params2 != null) {
            try {
                if (params2.length != 0) {
                    StringBuffer ps = new StringBuffer();
                    ps.append("(");
                    for (String append : params2) {
                        ps.append(append).append("|");
                    }
                    ps.deleteCharAt(ps.length() - 1);
                    ps.append(")");
                    return url.replaceAll("(?<=[\\?&])" + ps.toString() + "=[^&]*&?", "").replaceAll("(\\?|\\?&|&+)$", "");
                }
            } catch (Exception e) {
                return url;
            }
        }
        return url;
    }

    private boolean findUseTBNet(String uri2) {
        try {
            return AndroidUtils.parseBoolean(Uri.parse(uri2).getQueryParameter(PlayerEnvironment.USE_TBNET_PROXY));
        } catch (Throwable th) {
            return false;
        }
    }

    public static GetRequest read(InputStream inputStream) throws IOException {
        BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream, "UTF-8"));
        StringBuilder stringRequest = new StringBuilder();
        while (true) {
            String line = reader.readLine();
            if (TextUtils.isEmpty(line)) {
                return new GetRequest(stringRequest.toString());
            }
            stringRequest.append(line).append(10);
        }
    }

    private long findRangeOffset(String request) {
        Matcher matcher = RANGE_HEADER_PATTERN.matcher(request);
        if (matcher.find()) {
            return Long.parseLong(matcher.group(1));
        }
        return -1;
    }

    private String findUri(String request) {
        Matcher matcher = URL_PATTERN.matcher(request);
        if (matcher.find()) {
            return matcher.group(2);
        }
        throw new IllegalArgumentException("Invalid request `" + request + "`: url not found!");
    }

    private String findUserAgent(String request) {
        Matcher matcher = USER_AGENT_PATTERN.matcher(request);
        if (matcher.find()) {
            return matcher.group(1);
        }
        return null;
    }

    public String toString() {
        return "GetRequest{rangeOffset=" + this.rangeOffset + ", partial=" + this.partial + ", uri='" + this.uri + '\'' + '}';
    }
}
