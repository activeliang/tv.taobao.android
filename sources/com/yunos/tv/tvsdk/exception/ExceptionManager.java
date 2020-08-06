package com.yunos.tv.tvsdk.exception;

import android.content.Context;
import android.util.Log;
import android.widget.Toast;
import java.io.InterruptedIOException;
import java.net.SocketException;
import java.net.UnknownHostException;
import org.apache.http.conn.HttpHostConnectException;

public class ExceptionManager {
    public static final String TAG = "ExceptionManager";
    public static UnknownExceptionHandler unknownExceptionHandler;

    public interface UnknownExceptionHandler {
        void handle(Exception exc);
    }

    public static boolean handleException(Context context, Exception ex) {
        if (ex == null) {
            return false;
        }
        Log.w(TAG, TAG, ex);
        if (ex instanceof HttpRequestException) {
            return ((HttpRequestException) ex).handle(context);
        }
        if (ex instanceof UnknownHostException) {
            Toast.makeText(context, "请求出错,域名无法解析", 1).show();
            return true;
        } else if (ex instanceof InterruptedIOException) {
            Toast.makeText(context, "服务器连接超时，请检查网络设置", 1).show();
            return true;
        } else if (ex instanceof HttpHostConnectException) {
            Toast.makeText(context, "服务器连接出错:" + ((HttpHostConnectException) ex).getHost().getHostName(), 1).show();
            return true;
        } else if (ex instanceof SocketException) {
            Toast.makeText(context, "网络数据传输出错", 1).show();
            return true;
        } else if (ex instanceof BaseException) {
            return ((BaseException) ex).handle(context);
        } else {
            if (unknownExceptionHandler == null) {
                return false;
            }
            unknownExceptionHandler.handle(ex);
            return false;
        }
    }
}
