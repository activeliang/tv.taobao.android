package mtopsdk.network.domain;

import java.util.List;
import java.util.Map;

public final class Response {
    public final ResponseBody body;
    public final int code;
    public final Map<String, List<String>> headers;
    public final String message;
    public final Request request;
    public final NetworkStats stat;

    private Response(Builder builder) {
        this.request = builder.request;
        this.code = builder.code;
        this.message = builder.message;
        this.headers = builder.headers;
        this.body = builder.body;
        this.stat = builder.stat;
    }

    public String toString() {
        StringBuilder builder = new StringBuilder(64);
        builder.append("Response{ code=").append(this.code);
        builder.append(", message=").append(this.message);
        builder.append(", headers").append(this.headers);
        builder.append(", body").append(this.body);
        builder.append(", request").append(this.request);
        builder.append(", stat").append(this.stat);
        builder.append("}");
        return builder.toString();
    }

    public static class Builder {
        ResponseBody body;
        int code = -1;
        Map<String, List<String>> headers;
        String message;
        Request request;
        NetworkStats stat;

        public Builder request(Request request2) {
            this.request = request2;
            return this;
        }

        public Builder code(int code2) {
            this.code = code2;
            return this;
        }

        public Builder message(String message2) {
            this.message = message2;
            return this;
        }

        public Builder headers(Map<String, List<String>> headers2) {
            this.headers = headers2;
            return this;
        }

        public Builder body(ResponseBody body2) {
            this.body = body2;
            return this;
        }

        public Builder stat(NetworkStats stat2) {
            this.stat = stat2;
            return this;
        }

        public Response build() {
            if (this.request != null) {
                return new Response(this);
            }
            throw new IllegalStateException("request == null");
        }
    }
}
