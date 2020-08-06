package com.ali.user.open.ucc;

import java.util.Map;

public interface UccCallback {
    void onFail(String str, int i, String str2);

    void onSuccess(String str, Map map);
}
