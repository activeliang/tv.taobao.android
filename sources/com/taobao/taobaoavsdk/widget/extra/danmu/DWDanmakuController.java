package com.taobao.taobaoavsdk.widget.extra.danmu;

import android.content.Context;
import android.text.TextUtils;
import android.view.View;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import org.json.JSONArray;
import org.json.JSONObject;

public class DWDanmakuController {
    /* access modifiers changed from: private */
    public JSONObject mBarrageChart;
    /* access modifiers changed from: private */
    public Context mContext;
    /* access modifiers changed from: private */
    public DWDanmakuConfig mDanmakuConfig;
    /* access modifiers changed from: private */
    public DWDanmakuStyle mDanmakuStyle;
    private IDWDanmakuTimelineAdapter mDanmakuTimelineAdapter;
    /* access modifiers changed from: private */
    public DanmakuView mDanmakuView;
    private boolean mInit;
    private IDWDanmakuNetworkAdapter mNateworkAdapter;
    /* access modifiers changed from: private */
    public JSONObject mProfileData;

    public DWDanmakuController(Context context, DWDanmakuConfig config) {
        this.mContext = context;
        this.mDanmakuConfig = config;
        this.mDanmakuStyle = new DWDanmakuStyle();
        this.mDanmakuStyle.speedFactorX = 1.0f;
        this.mDanmakuStyle.textColor = -1;
        this.mDanmakuStyle.textShadowColor = -12303292;
        this.mDanmakuStyle.textShadowRadius = 2;
        this.mDanmakuStyle.textShadowX = 0;
        this.mDanmakuStyle.textShadowY = 0;
        this.mDanmakuStyle.textSize = 12;
        this.mDanmakuStyle.isBold = true;
    }

    public DWDanmakuController(Context context, DWDanmakuConfig config, DWDanmakuStyle style) {
        this.mContext = context;
        this.mDanmakuConfig = config;
        this.mDanmakuStyle = style;
    }

    public void setIDWDanmakuTimelineAdapter(IDWDanmakuTimelineAdapter adapter) {
        this.mDanmakuTimelineAdapter = adapter;
    }

    public void setIDWDanmakuNetworkAdapter(IDWDanmakuNetworkAdapter adapter) {
        this.mNateworkAdapter = adapter;
    }

    public void start() {
        if (!this.mInit) {
            getDanmaProfile();
            initDanmuView();
            this.mDanmakuView.start();
            this.mInit = true;
        }
    }

    public void pause() {
        if (this.mDanmakuView != null) {
            this.mDanmakuView.pause();
        }
    }

    public void resume() {
        if (this.mDanmakuView != null) {
            this.mDanmakuView.start();
        }
    }

    public void seekTo(int time) {
        if (this.mInit) {
            this.mDanmakuView.seek(time);
            String sMin = Integer.toString(time / 60000);
            if (this.mBarrageChart != null && !this.mBarrageChart.isNull(sMin)) {
                if ("true".equals(this.mBarrageChart.optString(sMin))) {
                    getBarrageList(sMin);
                }
                this.mBarrageChart.remove(sMin);
            }
        }
    }

    public void setProgress(int progress) {
        if (this.mInit) {
            String sMin = Integer.toString(progress / 60000);
            if (this.mBarrageChart != null && !this.mBarrageChart.isNull(sMin)) {
                if ("true".equals(this.mBarrageChart.optString(sMin))) {
                    getBarrageList(sMin);
                }
                this.mBarrageChart.remove(sMin);
            }
        }
    }

    public View getView() {
        return this.mDanmakuView;
    }

    public void showDanmaku() {
        if (this.mDanmakuView != null) {
            this.mDanmakuView.show();
        }
    }

    public void hideDanmaku() {
        if (this.mDanmakuView != null) {
            this.mDanmakuView.hide();
        }
    }

    public void sendMsg(String nick, String content, long showTime, DWDanmakuStyle style) {
        this.mDanmakuView.addItemToHead(new DanmakuItem(this.mContext, nick, content, true, 200 + showTime, this.mDanmakuConfig.danmakuViewWidth, this.mDanmakuView.getWidth(), 0, style));
    }

    public void sendMsg(String content, long showTime, DWDanmakuStyle style) {
        this.mDanmakuView.addItemToHead(new DanmakuItem(this.mContext, content, 200 + showTime, this.mDanmakuConfig.danmakuViewWidth, this.mDanmakuView.getWidth(), 0, style));
    }

    private void initDanmuView() {
        this.mDanmakuView = new DanmakuView(this.mContext, this.mDanmakuTimelineAdapter);
        this.mDanmakuView.setMaxRow(this.mDanmakuConfig.maxRow);
        this.mDanmakuView.setMaxRunningPerRow(this.mDanmakuConfig.itemPerRow);
        this.mDanmakuView.init();
    }

    private void getDanmaProfile() {
        DWDanmakuRequest request = new DWDanmakuRequest();
        request.responseClass = null;
        request.apiName = "mtop.taobao.social.barrage.profile";
        request.apiVersion = "1.0";
        request.paramMap = new HashMap();
        request.paramMap.put("namespace", this.mDanmakuConfig.namespace);
        request.paramMap.put("targetId", this.mDanmakuConfig.targetId);
        this.mNateworkAdapter.sendRequest(new IDWDanmakuNetworkListener() {
            public void onSuccess(DWDanmakuResponse response) {
                JSONObject unused = DWDanmakuController.this.mProfileData = response.data;
                if (DWDanmakuController.this.mProfileData != null) {
                    JSONObject unused2 = DWDanmakuController.this.mBarrageChart = DWDanmakuController.this.mProfileData.optJSONObject("barrageChart");
                }
            }

            public void onError(DWDanmakuResponse response) {
            }
        }, request);
    }

    public void destroy() {
        if (this.mDanmakuView != null) {
            this.mDanmakuView.destroy();
        }
    }

    private void getBarrageList(String min) {
        DWDanmakuRequest request = new DWDanmakuRequest();
        request.responseClass = null;
        request.apiName = "mtop.taobao.social.barrage.list";
        request.apiVersion = "1.0";
        request.paramMap = new HashMap();
        request.paramMap.put("namespace", this.mDanmakuConfig.namespace);
        request.paramMap.put("targetId", this.mDanmakuConfig.targetId);
        request.paramMap.put("startMin", min);
        request.paramMap.put("endMin", min);
        request.paramMap.put("pageSize", Integer.toString(this.mDanmakuConfig.pageSize));
        this.mNateworkAdapter.sendRequest(new IDWDanmakuNetworkListener() {
            public void onSuccess(DWDanmakuResponse response) {
                DanmakuItem danmakuItem;
                JSONObject data = response.data;
                if (data != null) {
                    JSONArray list = data.optJSONArray("result");
                    int count = list == null ? 0 : list.length();
                    List<IDanmakuItem> danmakuItems = new ArrayList<>();
                    for (int i = 0; i < count; i++) {
                        JSONObject item = list.optJSONObject(i);
                        String showTime = item.optString("vtime");
                        if (TextUtils.isDigitsOnly(showTime)) {
                            if (DWDanmakuController.this.mDanmakuConfig.needNick) {
                                danmakuItem = new DanmakuItem(DWDanmakuController.this.mContext, item.optString("accountNick"), item.optString("content"), false, Long.parseLong(showTime), DWDanmakuController.this.mDanmakuConfig.danmakuViewWidth, DWDanmakuController.this.mDanmakuView.getWidth(), 0, DWDanmakuController.this.mDanmakuStyle);
                            } else {
                                danmakuItem = new DanmakuItem(DWDanmakuController.this.mContext, item.optString("content"), Long.parseLong(showTime), DWDanmakuController.this.mDanmakuConfig.danmakuViewWidth, DWDanmakuController.this.mDanmakuView.getWidth(), 0, DWDanmakuController.this.mDanmakuStyle);
                            }
                            danmakuItems.add(danmakuItem);
                        }
                    }
                    DWDanmakuController.this.mDanmakuView.addItem(danmakuItems, false);
                }
            }

            public void onError(DWDanmakuResponse response) {
            }
        }, request);
    }
}
