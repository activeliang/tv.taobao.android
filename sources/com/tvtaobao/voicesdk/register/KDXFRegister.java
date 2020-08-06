package com.tvtaobao.voicesdk.register;

import android.content.Intent;
import android.text.TextUtils;
import com.iflytek.xiri.Feedback;
import com.iflytek.xiri.scene.ISceneListener;
import com.iflytek.xiri.scene.Scene;
import com.tvtaobao.voicesdk.ASRInput;
import com.tvtaobao.voicesdk.utils.LogPrint;
import com.yunos.tv.core.CoreApplication;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class KDXFRegister {
    private final String TAG = "KDXFRegister";
    /* access modifiers changed from: private */
    public String _scene = null;
    /* access modifiers changed from: private */
    public Feedback feedback = new Feedback(CoreApplication.getApplication());
    private boolean isRegister = false;
    private Scene scene = new Scene(CoreApplication.getApplication());

    public void onRegister(String className, JSONArray jsonArray) {
        JSONObject object = new JSONObject();
        try {
            JSONObject tvtaobaoASR = new JSONObject();
            tvtaobaoASR.put("tvtaobaoASR", jsonArray);
            object.put("_commands", tvtaobaoASR);
            object.put("_scene", className);
            this._scene = object.toString();
            LogPrint.e("KDXFRegister", "onExecute _scene : " + this._scene);
            this.scene.init(new KDXFBackListener());
            this.isRegister = true;
        } catch (JSONException e) {
            e.printStackTrace();
        } catch (SecurityException e2) {
            e2.printStackTrace();
        } catch (Exception e3) {
            e3.printStackTrace();
        }
    }

    public void release() {
        try {
            if (this.isRegister) {
                this.scene.release();
                this.isRegister = false;
            }
        } catch (Exception e) {
            LogPrint.e("KDXFRegister", "release e : " + e.getMessage());
        }
    }

    private class KDXFBackListener implements ISceneListener {
        private KDXFBackListener() {
        }

        public String onQuery() {
            return KDXFRegister.this._scene;
        }

        public void onExecute(Intent intent) {
            KDXFRegister.this.feedback.begin(intent);
            String asr = intent.getStringExtra("_rawtext");
            LogPrint.i("KDXFRegister", "KDXFRegister.onExecute asr : " + asr);
            if (!TextUtils.isEmpty(asr)) {
                ASRInput.getInstance().nlpRequest(asr);
            }
        }
    }
}
