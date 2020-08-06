package com.taobao.orange.accssupport;

import com.alibaba.fastjson.JSON;
import com.taobao.accs.base.TaoBaseService;
import com.taobao.orange.ConfigCenter;
import com.taobao.orange.OThreadFactory;
import com.taobao.orange.model.NameSpaceDO;
import com.taobao.orange.util.OLog;

public class OrangeAccsService extends TaoBaseService {
    private static final String TAG = "OrangeAccsService";

    public void onData(String serviceId, String userId, String dataId, byte[] data, TaoBaseService.ExtraInfo extraInfo) {
        if ("orange".equals(serviceId)) {
            OLog.i(TAG, "onData", "dataId", dataId, "userId", userId);
            handleAccsUpdate(data);
        }
    }

    public void onBind(String serviceId, int errorCode, TaoBaseService.ExtraInfo extraInfo) {
    }

    public void onUnbind(String serviceId, int errorCode, TaoBaseService.ExtraInfo extraInfo) {
    }

    public void onSendData(String serviceId, String dataId, int errorCode, TaoBaseService.ExtraInfo extraInfo) {
    }

    public void onResponse(String serviceId, String dataId, int errorCode, byte[] response, TaoBaseService.ExtraInfo extraInfo) {
    }

    public static void handleAccsUpdate(final byte[] data) {
        OThreadFactory.execute(new Runnable() {
            public void run() {
                if (data == null || data.length <= 0) {
                    OLog.e(OrangeAccsService.TAG, "handleAccsUpdate data is empty", new Object[0]);
                    return;
                }
                String json = new String(data);
                OLog.d(OrangeAccsService.TAG, "handleAccsUpdate", "json", json);
                NameSpaceDO nameSpaceDO = (NameSpaceDO) JSON.parseObject(json, NameSpaceDO.class);
                if (nameSpaceDO == null) {
                    OLog.e(OrangeAccsService.TAG, "handleAccsUpdate fail as nameSpaceDO null", new Object[0]);
                } else if (ConfigCenter.getInstance().mIsOrangeInit.get()) {
                    ConfigCenter.getInstance().loadConfig(nameSpaceDO);
                } else {
                    OLog.e(OrangeAccsService.TAG, "handleAccsUpdate fail as not finish orange init", new Object[0]);
                }
            }
        });
    }
}
