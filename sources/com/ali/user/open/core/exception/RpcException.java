package com.ali.user.open.core.exception;

public class RpcException extends RuntimeException {
    private static final long serialVersionUID = -2875437994101380406L;
    private int mCode;
    private String mMsg;

    public interface ErrorCode {
        public static final int ANTI_REFRESH = 401;
        public static final int API_UNAUTHORIZED = 408;
        public static final int CLIENT_DESERIALIZER_ERROR = 10;
        public static final int CLIENT_NETWORK_ERROR = 7;
        public static final int EXPIRED_REQUEST = 402;
        public static final int ILLEGEL_SIGN = 403;
        public static final int LIMIT_ERROR = 400;
        public static final int OK = 1000;
        public static final int SESSION_INVALID = 407;
        public static final int SYSTEM_ERROR = 406;
    }

    public RpcException(Integer code, String msg) {
        super(format(code, msg));
        this.mCode = code.intValue();
        this.mMsg = msg;
    }

    public RpcException(Integer code, Throwable cause) {
        super(cause);
        this.mCode = code.intValue();
    }

    public RpcException(String msg) {
        super(msg);
        this.mCode = 0;
        this.mMsg = msg;
    }

    public int getCode() {
        return this.mCode;
    }

    public String getMsg() {
        return this.mMsg;
    }

    protected static String format(Integer code, String message) {
        StringBuilder str = new StringBuilder();
        str.append("RPCException: ");
        if (code != null) {
            str.append("[").append(code).append("]");
        }
        str.append(" : ");
        if (message != null) {
            str.append(message);
        }
        return str.toString();
    }

    public static boolean isNetworkError(int errorCode) {
        return errorCode == 7;
    }

    public static boolean isSystemError(int errorCode) {
        if (errorCode == 7 || (errorCode >= 400 && errorCode <= 408)) {
            return true;
        }
        return false;
    }
}
