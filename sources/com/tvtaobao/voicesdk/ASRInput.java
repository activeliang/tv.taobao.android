package com.tvtaobao.voicesdk;

import android.app.Service;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import com.bftv.fui.constantplugin.Constant;
import com.tvtaobao.voicesdk.base.SDKInitConfig;
import com.tvtaobao.voicesdk.bean.ConfigVO;
import com.tvtaobao.voicesdk.bean.SearchObject;
import com.tvtaobao.voicesdk.control.GoodsSearchControl;
import com.tvtaobao.voicesdk.control.TakeOutSearchControl;
import com.tvtaobao.voicesdk.listener.VoiceListener;
import com.tvtaobao.voicesdk.request.ASRUTRequest;
import com.tvtaobao.voicesdk.utils.DialogManager;
import com.tvtaobao.voicesdk.utils.LogPrint;
import com.yunos.tv.alitvasrsdk.CommonData;
import com.yunos.tv.core.common.RequestListener;
import com.yunos.tv.core.util.Utils;
import com.yunos.tv.tvsdk.media.data.HuasuVideo;
import com.yunos.tvtaobao.biz.request.BusinessRequest;
import com.yunos.tvtaobao.biz.request.base.BaseMtopRequest;
import java.lang.ref.WeakReference;
import java.util.Map;
import org.json.JSONException;
import org.json.JSONObject;

public class ASRInput extends NLPDeal {
    private static final String TAG = "ASRInput";
    private static final int TYPE_MESSAGE_ASR = 1;
    private static final int TYPE_MESSAGE_JSON = 0;
    private static final int TYPE_MESSAGE_TVTAOMSG = 3;
    private static final int TYPE_MESSAGE_TVTAOSDK = 2;
    /* access modifiers changed from: private */
    public static ASRInput asrInput;
    private VoiceHandler mHandler = new VoiceHandler();
    private ASRNotify mNotify = ASRNotify.getInstance();

    public static ASRInput getInstance() {
        if (asrInput == null) {
            synchronized (ASRInput.class) {
                if (asrInput == null) {
                    asrInput = new ASRInput();
                }
            }
        }
        return asrInput;
    }

    private ASRInput() {
    }

    public void destroy() {
    }

    public void setContext(Service service) {
        mWeakService = new WeakReference(service);
    }

    public void handleInput(String s, String s1, VoiceListener listener) {
        LogPrint.e(TAG, "handleInput s : " + s + " ,s1 : " + s1);
        Bundle bundle = new Bundle();
        bundle.putString(CommonData.TYPE_ASR, s);
        bundle.putString("json", s1);
        Message msg = new Message();
        msg.setData(bundle);
        if (s1 != null && !s1.equals(Constant.NULL)) {
            msg.what = 0;
        } else if (s != null && !s.equals(Constant.NULL)) {
            msg.what = 1;
        }
        this.mHandler.sendMessage(msg);
        this.mWeakListener = new WeakReference(listener);
    }

    public void setMessage(String aString, VoiceListener listener) {
        LogPrint.e(TAG, "setMessage aString : " + aString);
        this.mHandler.sendMessage(this.mHandler.obtainMessage(3, aString));
        this.mWeakListener = new WeakReference(listener);
    }

    public void setMessage(String asr_text, String searchConfig, String json, VoiceListener listener) {
        LogPrint.i(TAG, "sendMessage searchConfig : " + searchConfig);
        LogPrint.e(TAG, "sendMessage asr : " + asr_text + " ,json : " + json);
        Bundle bundle = new Bundle();
        bundle.putString(CommonData.TYPE_ASR, asr_text);
        bundle.putString("json", json);
        bundle.putString("searchConfig", searchConfig);
        Message msg = new Message();
        msg.setData(bundle);
        msg.what = 2;
        this.mHandler.sendMessage(msg);
        this.mWeakListener = new WeakReference(listener);
    }

    private static class VoiceHandler extends Handler {
        private VoiceHandler() {
        }

        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            LogPrint.e(ASRInput.TAG, "Handler msg : " + msg);
            String asr = msg.getData().getString(CommonData.TYPE_ASR);
            switch (msg.what) {
                case 0:
                    ASRInput.asrInput.dealBaoFengJSON(asr, msg.getData().getString("json"));
                    return;
                case 1:
                    ASRInput.asrInput.dealASR(asr);
                    return;
                case 2:
                    Bundle bundle = msg.getData();
                    ASRInput.asrInput.dealTVTaoJson(asr, bundle.getString("json"), bundle.getString("searchConfig"));
                    return;
                case 3:
                    LogPrint.i(ASRInput.TAG, "VoiceHandler aString : " + ((String) msg.obj));
                    return;
                default:
                    return;
            }
        }
    }

    /* access modifiers changed from: private */
    public void dealTVTaoJson(String asr, String json, String search) {
        LogPrint.e(TAG, "dealTVTaoJson searchConfig : " + search);
        ConfigVO.searchConfig = SearchObject.resolverData(search);
        if (TextUtils.isEmpty(json)) {
            dealASR(asr);
            return;
        }
        try {
            JSONObject object = new JSONObject(json);
            int type = object.getInt("type");
            LogPrint.e(TAG, "sendMessage type is " + type);
            switch (type) {
                case 0:
                    SearchObject searchObject = SearchObject.resolverData(search);
                    GoodsSearchControl control = new GoodsSearchControl();
                    control.init(mWeakService, this.mWeakListener);
                    control.setSearchConfig(searchObject);
                    control.requestSearch();
                    recordVoiceToService("system", searchObject.keyword);
                    return;
                case 1:
                    gotoActivity("tvtaobao://home?app=voice&module=createorder&itemId=" + object.getString("itemId") + "&from=voice_system&notshowloading=true");
                    recordVoiceToService("system", "buy_index");
                    return;
                case 2:
                    addCart(object.getString("itemId"));
                    return;
                case 3:
                    manageFav(object.getString("itemId"));
                    return;
                case 4:
                    gotoActivity("tvtaobao://home?module=detail&itemId=" + object.getString("itemId") + "&from=voice_system&notshowloading=true");
                    recordVoiceToService("system", "see_index");
                    return;
                case 5:
                    TakeOutSearchControl takeOutSearchControl = new TakeOutSearchControl();
                    takeOutSearchControl.init(mWeakService, this.mWeakListener);
                    String keywords = object.getString("keywords");
                    int i = object.getInt("pageNo");
                    takeOutSearchControl.takeoutSearch(keywords, "0", object.getInt("pageSize"), object.getString("orderType"));
                    return;
                case 6:
                    String takeoutshophome = "tvtaobao://home?app=takeout&module=takeouthome&shopId=" + object.getString("shopId") + "&v_from=voice_system&notshowloading=true";
                    if (!(mWeakService == null || mWeakService.get() == null)) {
                        Intent intent = new Intent();
                        intent.setData(Uri.parse(takeoutshophome));
                        intent.setFlags(268435456);
                        ((Service) mWeakService.get()).startActivity(intent);
                        DialogManager.getManager().dismissAllDialog();
                    }
                    recordVoiceToService("system", "to_takeout_home");
                    return;
                case 101:
                    SDKInitConfig.setNeedTakeOutTips(object.getBoolean("needTakeOutTips"));
                    return;
                case 102:
                    SDKInitConfig.setNeedTVTaobaoSearch(object.getBoolean("needTVTaobaoSearch"));
                    return;
                case 999:
                    SDKInitConfig.init(object);
                    return;
                default:
                    return;
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        e.printStackTrace();
    }

    /* access modifiers changed from: private */
    public void dealBaoFengJSON(String asr, String json) {
        try {
            if (TextUtils.isEmpty(json)) {
                json = "{}";
            }
            JSONObject object = new JSONObject(json);
            LogPrint.e(TAG, "Handler object : " + object.toString());
            if (object.has("PRICE") || object.has("SellNum")) {
                String good = object.getJSONArray("Good").getString(0);
                if (object.has("PRICE")) {
                    Utils.utCustomHit("Voice_search_screen_price", getProperties(asr));
                    ConfigVO.priceScope = object.getJSONArray("PRICE").getString(0);
                }
                if (object.has("SellNum")) {
                    Utils.utCustomHit("Voice_search_screen_sales", getProperties(asr));
                    ConfigVO.saleSorting = object.getJSONArray("SellNum").getString(0);
                }
                LogPrint.e(TAG, "asrJson: " + good);
                nlpRequest(good);
            } else if (object.has("tips")) {
                Map<String, String> map = getProperties(asr);
                map.put("screencase", object.getString("tips"));
                Utils.utCustomHit("Voice_search_screen", map);
                SearchObject searchConfig = new SearchObject();
                searchConfig.keyword = object.getString("tips");
                GoodsSearchControl control = new GoodsSearchControl();
                control.init(mWeakService, this.mWeakListener);
                control.setSearchConfig(searchConfig);
                control.requestSearch();
                recordVoiceToService("system", searchConfig.keyword);
            } else if (object.has(HuasuVideo.TAG_URI)) {
                resolverUri(object.getString(HuasuVideo.TAG_URI));
            } else {
                dealASR(asr);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void dealASR(String asr_text) {
        LogPrint.e(TAG, "dealASR asr : " + asr_text);
        if (!TextUtils.isEmpty(asr_text)) {
            Utils.utCustomHit("Voice_asr", getProperties(asr_text));
            nlpRequest(asr_text);
        }
    }

    private void resolverUri(String uri) {
        if (uri != null) {
            if (uri.startsWith("tvtaobao://home")) {
                gotoActivity(uri);
            }
            if (uri.startsWith("tvtaobao://slideshow")) {
                gotoActivity(uri);
            }
            if (uri.startsWith("tvtaobao://addcart")) {
                Utils.utCustomHit("Voice_search_cart_add", getProperties());
                addCart(decodeUri(Uri.parse(uri)).getString("itemId"));
            }
            if (uri.startsWith("tvtaobao://collection")) {
                Utils.utCustomHit("Voice_search_collect", getProperties());
                manageFav(decodeUri(Uri.parse(uri)).getString("itemId"));
            }
            if (uri.startsWith("tvtaobao://voice")) {
                gotoActivity(uri);
            }
        }
    }

    private Bundle decodeUri(Uri uri) {
        if (uri.getEncodedQuery() == null) {
            return null;
        }
        Bundle bundle = new Bundle();
        for (String key : uri.getQueryParameterNames()) {
            bundle.putString(key, uri.getQueryParameter(key));
        }
        return bundle;
    }

    private void recordVoiceToService(String referrer, String asr) {
        BusinessRequest.getBusinessRequest().baseRequest((BaseMtopRequest) new ASRUTRequest(asr, referrer), (RequestListener) null, false);
    }
}
