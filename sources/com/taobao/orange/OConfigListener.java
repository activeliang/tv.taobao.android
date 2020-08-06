package com.taobao.orange;

import java.util.Map;

public interface OConfigListener extends OBaseListener {
    void onConfigUpdate(String str, Map<String, String> map);
}
