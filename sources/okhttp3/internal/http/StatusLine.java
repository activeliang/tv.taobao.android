package okhttp3.internal.http;

import java.io.IOException;
import java.net.ProtocolException;
import okhttp3.Protocol;
import okhttp3.Response;

public final class StatusLine {
    public static final int HTTP_CONTINUE = 100;
    public static final int HTTP_PERM_REDIRECT = 308;
    public static final int HTTP_TEMP_REDIRECT = 307;
    public final int code;
    public final String message;
    public final Protocol protocol;

    public StatusLine(Protocol protocol2, int code2, String message2) {
        this.protocol = protocol2;
        this.code = code2;
        this.message = message2;
    }

    public static StatusLine get(Response response) {
        return new StatusLine(response.protocol(), response.code(), response.message());
    }

    public static StatusLine parse(String statusLine) throws IOException {
        Protocol protocol2;
        int codeStart;
        if (statusLine.startsWith("HTTP/1.")) {
            if (statusLine.length() < 9 || statusLine.charAt(8) != ' ') {
                throw new ProtocolException("Unexpected status line: " + statusLine);
            }
            int httpMinorVersion = statusLine.charAt(7) - '0';
            codeStart = 9;
            if (httpMinorVersion == 0) {
                protocol2 = Protocol.HTTP_1_0;
            } else if (httpMinorVersion == 1) {
                protocol2 = Protocol.HTTP_1_1;
            } else {
                throw new ProtocolException("Unexpected status line: " + statusLine);
            }
        } else if (statusLine.startsWith("ICY ")) {
            protocol2 = Protocol.HTTP_1_0;
            codeStart = 4;
        } else {
            throw new ProtocolException("Unexpected status line: " + statusLine);
        }
        if (statusLine.length() < codeStart + 3) {
            throw new ProtocolException("Unexpected status line: " + statusLine);
        }
        try {
            int code2 = Integer.parseInt(statusLine.substring(codeStart, codeStart + 3));
            String message2 = "";
            if (statusLine.length() > codeStart + 3) {
                if (statusLine.charAt(codeStart + 3) != ' ') {
                    throw new ProtocolException("Unexpected status line: " + statusLine);
                }
                message2 = statusLine.substring(codeStart + 4);
            }
            return new StatusLine(protocol2, code2, message2);
        } catch (NumberFormatException e) {
            throw new ProtocolException("Unexpected status line: " + statusLine);
        }
    }

    public String toString() {
        StringBuilder result = new StringBuilder();
        result.append(this.protocol == Protocol.HTTP_1_0 ? "HTTP/1.0" : "HTTP/1.1");
        result.append(' ').append(this.code);
        if (this.message != null) {
            result.append(' ').append(this.message);
        }
        return result.toString();
    }
}
