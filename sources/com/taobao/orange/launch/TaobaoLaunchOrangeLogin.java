package com.taobao.orange.launch;

import android.app.Application;
import com.taobao.orange.OrangeConfig;
import java.io.Serializable;
import java.util.HashMap;

public class TaobaoLaunchOrangeLogin implements Serializable {
    public void init(Application application, HashMap<String, Object> params) {
        String userId = null;
        try {
            userId = (String) params.get("userId");
        } catch (Throwable th) {
        }
        if (userId == null) {
            userId = "";
        }
        OrangeConfig.getInstance().setUserId(userId);
    }
}
