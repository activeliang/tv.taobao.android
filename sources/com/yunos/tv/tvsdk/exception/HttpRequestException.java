package com.yunos.tv.tvsdk.exception;

import android.content.Context;
import android.widget.Toast;
import java.net.ConnectException;
import java.net.SocketException;
import java.net.UnknownHostException;
import org.apache.http.conn.ConnectTimeoutException;
import org.apache.http.conn.HttpHostConnectException;

public class HttpRequestException extends BaseException {
    private static final long serialVersionUID = 970861156266628483L;

    public HttpRequestException() {
    }

    public HttpRequestException(String message) {
        super(message);
    }

    public HttpRequestException(Throwable cause) {
        super(cause);
    }

    public HttpRequestException(int code, String errorMessage) {
        super(code, errorMessage);
    }

    public HttpRequestException(String detailMessage, Throwable throwable) {
        super(detailMessage, throwable);
    }

    public HttpRequestException(int code, String detailMessage, Throwable throwable) {
        super(code, detailMessage, throwable);
    }

    public boolean handle(Context context) {
        if (this.errorMessage != null) {
            Toast.makeText(context, this.errorMessage, 1).show();
        }
        return true;
    }

    public static boolean isHttpException(Exception ex) {
        if ((ex instanceof ConnectTimeoutException) || (ex instanceof HttpHostConnectException) || (ex instanceof UnknownHostException) || (ex instanceof ConnectException) || (ex instanceof SocketException)) {
            return true;
        }
        return false;
    }
}
