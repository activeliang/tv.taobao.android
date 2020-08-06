package com.taobao.orange.util;

import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import com.alibaba.fastjson.JSON;
import com.taobao.orange.GlobalOrange;
import com.taobao.orange.OConstant;
import com.taobao.orange.OThreadFactory;
import com.taobao.orange.model.ConfigAckDO;
import com.taobao.orange.model.IndexAckDO;
import com.taobao.orange.sync.AuthRequest;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public class ReportAckUtils {
    static final int MSG_REPORT_CONFIGACKS = 1;
    static final int MSG_WAIT_CONFIGACKS = 0;
    static final String TAG = "ReportAck";
    static final Set<ConfigAckDO> mConfigAckDOSet = new HashSet();
    private static Handler mHandler = new ConfigHandler(Looper.getMainLooper());

    static class ConfigHandler extends Handler {
        ConfigHandler(Looper looper) {
            super(looper);
        }

        public void handleMessage(Message msg) {
            switch (msg.what) {
                case 0:
                    if (OLog.isPrintLog(1)) {
                        OLog.d(ReportAckUtils.TAG, "wait config acks", new Object[0]);
                    }
                    sendEmptyMessageDelayed(1, 15000);
                    return;
                case 1:
                    synchronized (ReportAckUtils.mConfigAckDOSet) {
                        if (OLog.isPrintLog(1)) {
                            OLog.d(ReportAckUtils.TAG, "report config acks", "size", Integer.valueOf(ReportAckUtils.mConfigAckDOSet.size()));
                        }
                        Set<ConfigAckDO> tempConfigAckDOSet = new HashSet<>();
                        tempConfigAckDOSet.addAll(ReportAckUtils.mConfigAckDOSet);
                        ReportAckUtils.reportConfigAcks(tempConfigAckDOSet);
                        ReportAckUtils.mConfigAckDOSet.clear();
                    }
                    return;
                default:
                    return;
            }
        }
    }

    public static void reportConfigAck(ConfigAckDO configAck) {
        if (GlobalOrange.reportUpdateAck && configAck != null) {
            synchronized (mConfigAckDOSet) {
                if (mConfigAckDOSet.size() == 0) {
                    mHandler.sendEmptyMessage(0);
                }
                mConfigAckDOSet.add(configAck);
            }
        }
    }

    static void reportConfigAcks(final Set<ConfigAckDO> configAckDOSet) {
        if (GlobalOrange.reportUpdateAck && configAckDOSet.size() != 0) {
            OThreadFactory.execute(new Runnable() {
                public void run() {
                    if (GlobalOrange.reportUpdateAck) {
                        new AuthRequest((String) null, true, OConstant.REQTYPE_ACK_CONFIG_UPDATE) {
                            /* access modifiers changed from: protected */
                            public Map<String, String> getReqParams() {
                                return null;
                            }

                            /* access modifiers changed from: protected */
                            public String getReqPostBody() {
                                return JSON.toJSONString(configAckDOSet);
                            }

                            /* access modifiers changed from: protected */
                            public Object parseResContent(String content) {
                                return null;
                            }
                        }.syncRequest();
                        OrangeMonitor.commitCount(OConstant.MONITOR_PRIVATE_MODULE, OConstant.POINT_CONFIG_ACK_COUNTS, "", (double) configAckDOSet.size());
                    }
                }
            }, GlobalOrange.randomDelayAckInterval);
        }
    }

    public static void reportIndexAck(final IndexAckDO indexAck) {
        if (GlobalOrange.reportUpdateAck) {
            if (OLog.isPrintLog(1)) {
                OLog.d(TAG, "report index ack", indexAck);
            }
            OThreadFactory.execute(new Runnable() {
                public void run() {
                    if (GlobalOrange.reportUpdateAck) {
                        new AuthRequest((String) null, true, OConstant.REQTYPE_ACK_INDEX_UPDATE) {
                            /* access modifiers changed from: protected */
                            public Map<String, String> getReqParams() {
                                return null;
                            }

                            /* access modifiers changed from: protected */
                            public String getReqPostBody() {
                                return JSON.toJSONString(indexAck);
                            }

                            /* access modifiers changed from: protected */
                            public Object parseResContent(String content) {
                                return null;
                            }
                        }.syncRequest();
                        OrangeMonitor.commitCount(OConstant.MONITOR_PRIVATE_MODULE, OConstant.POINT_INDEX_ACK_COUNTS, indexAck.indexId, 1.0d);
                    }
                }
            }, GlobalOrange.randomDelayAckInterval);
        }
    }
}
