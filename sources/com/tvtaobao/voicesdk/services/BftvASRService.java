package com.tvtaobao.voicesdk.services;

import android.os.RemoteException;
import android.text.TextUtils;
import com.bftv.fui.tell.TTS;
import com.bftv.fui.tell.TellManager;
import com.bftv.fui.thirdparty.IRemoteVoice;
import com.bftv.fui.thirdparty.RomoteVoiceService;
import com.bftv.fui.thirdparty.VoiceFeedback;
import com.google.gson.reflect.TypeToken;
import com.tvtaobao.voicesdk.ASRInput;
import com.tvtaobao.voicesdk.ASRNotify;
import com.tvtaobao.voicesdk.base.SDKInitConfig;
import com.tvtaobao.voicesdk.bean.CommandReturn;
import com.tvtaobao.voicesdk.bean.JinnangDo;
import com.tvtaobao.voicesdk.listener.VoiceListener;
import com.tvtaobao.voicesdk.utils.BFDataController;
import com.tvtaobao.voicesdk.utils.JSONUtil;
import com.tvtaobao.voicesdk.utils.LogPrint;
import com.yunos.tv.core.CoreApplication;
import com.yunos.tv.core.config.AppInfo;
import com.yunos.tv.core.util.GsonUtil;
import com.yunos.tv.tao.speech.client.domain.result.multisearch.item.ProductDo;
import java.util.ArrayList;
import java.util.List;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class BftvASRService extends RomoteVoiceService {
    private final String TAG = "BftvASRService";
    private ASRInput asrInput;
    private ASRNotify asrNotify;
    private Listener mListener;

    public void onCreate() {
        super.onCreate();
        this.asrNotify = ASRNotify.getInstance();
        this.asrInput = ASRInput.getInstance();
        this.asrInput.setContext(this);
        this.mListener = new Listener();
        try {
            JSONObject object = new JSONObject();
            object.put("needTakeOutTips", false);
            SDKInitConfig.init(object);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public void send(String s, String s1, IRemoteVoice iRemoteVoice) {
        LogPrint.e("BftvASRService", "BftvASRService.send s : " + s + " ,s1 : " + s1);
        this.asrNotify.setFeedBack(this.mListener);
        this.mListener.destroy();
        this.mListener.setIRemoteVoice(iRemoteVoice, s);
        this.asrInput.handleInput(s, s1, this.mListener);
    }

    public void onDestroy() {
        super.onDestroy();
        this.asrInput.destroy();
        LogPrint.e("BftvASRService", "onDestroy() executed");
    }

    class Listener implements VoiceListener {
        private String asr_text;
        private IRemoteVoice iRemoteVoice;

        Listener() {
        }

        /* access modifiers changed from: private */
        public void setIRemoteVoice(IRemoteVoice iRemoteVoice2, String asr) {
            this.iRemoteVoice = iRemoteVoice2;
            this.asr_text = asr;
        }

        public void destroy() {
            this.iRemoteVoice = null;
        }

        public void callback(CommandReturn command) {
            LogPrint.e("BftvASRService", "BftvASRServiceListener callback mIsHandled : " + command.mIsHandled + " ,Action : " + command.mAction);
            try {
                VoiceFeedback voiceFeedback = new VoiceFeedback();
                voiceFeedback.isHasResult = command.mIsHandled;
                switch (command.mAction) {
                    case 1001:
                        if (!TextUtils.isEmpty(command.mMessage)) {
                            TTS tts = new TTS();
                            tts.pck = AppInfo.getPackageName();
                            tts.tts = command.mMessage;
                            tts.userTxt = this.asr_text;
                            tts.isDisplayLayout = command.showUI;
                            TellManager.getInstance().tts(CoreApplication.getApplication(), tts);
                            break;
                        }
                        break;
                }
                if (this.iRemoteVoice != null) {
                    this.iRemoteVoice.sendMessage(voiceFeedback);
                }
            } catch (RemoteException e) {
                e.printStackTrace();
            }
        }

        public void searchResult(String data) {
            LogPrint.e("BftvASRService", "BftvASRService.Listener.searchResult");
            try {
                JSONObject object = new JSONObject(data);
                String keyword = object.getString("keyword");
                List<ProductDo> mProducts = new ArrayList<>();
                if (object.has("model")) {
                    JSONArray model = object.getJSONArray("model");
                    LogPrint.e("BftvASRService", "BftvASRService.SearchResponse size : " + model.length());
                    for (int i = 0; i < model.length(); i++) {
                        mProducts.add(GsonUtil.parseJson(model.getJSONObject(i).toString(), new TypeToken<ProductDo>() {
                        }));
                    }
                }
                List<JinnangDo> mJinnangs = new ArrayList<>();
                if (object.has("jinNangItems")) {
                    JSONArray jinnang = object.getJSONArray("jinNangItems");
                    for (int i2 = 0; i2 < jinnang.length(); i2++) {
                        mJinnangs.add(JinnangDo.resolverData(jinnang.getJSONObject(i2)));
                    }
                }
                String mMessage = JSONUtil.getString(object, "spoken");
                JSONArray tipsArray = JSONUtil.getArray(object, "tips");
                List<String> tips = new ArrayList<>();
                if (tipsArray != null) {
                    for (int i3 = 0; i3 < tipsArray.length(); i3++) {
                        tips.add(tipsArray.getString(i3));
                    }
                }
                VoiceFeedback voiceFeedback = BFDataController.onSearchSuccess(keyword, mProducts, mJinnangs, mMessage, tips);
                if (this.iRemoteVoice != null) {
                    this.iRemoteVoice.sendMessage(voiceFeedback);
                }
            } catch (JSONException e) {
                e.printStackTrace();
            } catch (RemoteException e2) {
                e2.printStackTrace();
            }
        }
    }
}
