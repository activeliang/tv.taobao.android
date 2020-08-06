package com.ali.user.open.ucc;

import android.app.Activity;
import android.content.Context;
import android.support.annotation.NonNull;
import java.util.Map;

public interface UccService {
    void bind(Activity activity, String str, UccCallback uccCallback);

    void bind(Activity activity, @NonNull String str, String str2, UccCallback uccCallback);

    void bind(Activity activity, @NonNull String str, String str2, Map<String, String> map, UccCallback uccCallback);

    void bind(Activity activity, String str, Map<String, String> map, UccCallback uccCallback);

    void bind(@NonNull String str, String str2, Map<String, String> map, UccCallback uccCallback);

    void bind(String str, Map<String, String> map, UccCallback uccCallback);

    void bindWithIBB(Activity activity, String str, String str2, Map<String, String> map, UccCallback uccCallback);

    void cleanUp();

    UccDataProvider getUccDataProvider();

    boolean isLoginUrl(String str, String str2);

    void logout(Context context, String str);

    void logoutAll(Context context);

    void setBindComponentProxy(BindComponentProxy bindComponentProxy);

    void setUccDataProvider(UccDataProvider uccDataProvider);

    void setUccDataProvider(String str, UccDataProvider uccDataProvider);

    void trustLogin(Activity activity, String str, UccCallback uccCallback);

    void trustLogin(Activity activity, String str, Map<String, String> map, UccCallback uccCallback);

    void trustLogin(String str, Map<String, String> map, UccCallback uccCallback);

    void uccOAuthLogin(Activity activity, String str, Map<String, String> map, UccCallback uccCallback);

    void unbind(String str, UccCallback uccCallback);

    void unbind(String str, Map<String, String> map, UccCallback uccCallback);
}
