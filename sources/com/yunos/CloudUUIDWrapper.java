package com.yunos;

import android.content.Context;
import com.yunos.tvtaobao.uuid.CloudUUID;

public class CloudUUIDWrapper {

    public interface IUUIDListener {
        void onCompleted(int i, float f);
    }

    public static String getCloudUUID() {
        return CloudUUID.getCloudUUID();
    }

    public static void init(Context context, boolean log) {
        CloudUUID.init(context, log);
    }

    public static void generateUUIDAsyn(final IUUIDListener listener, String productName, String ttid) {
        CloudUUID.generateUUIDAsyn(new com.yunos.tvtaobao.uuid.IUUIDListener() {
            public void onCompleted(int i, float v) {
                listener.onCompleted(i, v);
            }
        }, productName, ttid);
    }

    public static void setAndroidOnly(boolean flag) {
        CloudUUID.setAndroidOnly(flag);
    }
}
