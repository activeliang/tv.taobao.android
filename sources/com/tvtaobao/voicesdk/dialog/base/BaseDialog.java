package com.tvtaobao.voicesdk.dialog.base;

import android.app.Dialog;
import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.NonNull;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import com.tvtaobao.voicesdk.ASRNotify;
import com.tvtaobao.voicesdk.R;
import com.tvtaobao.voicesdk.bean.DomainResultVo;
import com.tvtaobao.voicesdk.bean.PageReturn;
import com.tvtaobao.voicesdk.listener.ASRHandler;
import com.tvtaobao.voicesdk.register.type.ActionType;
import com.tvtaobao.voicesdk.utils.ActivityUtil;
import com.tvtaobao.voicesdk.utils.DialogManager;
import com.tvtaobao.voicesdk.utils.LogPrint;
import com.yunos.CloudUUIDWrapper;
import com.yunos.tv.alitvasrsdk.CommonData;
import com.yunos.tv.core.config.Config;
import java.util.HashMap;
import java.util.Map;

public class BaseDialog extends Dialog implements ASRHandler {
    private final String TAG;
    private Animation mAnimationIn;
    private Animation mAnimationOut;
    private ASRNotify mNotify;
    private TimeHandler timeHandler;

    public BaseDialog(Context context) {
        this(context, R.style.voice_card_dialog);
    }

    private BaseDialog(Context context, int themeResId) {
        super(context, themeResId);
        this.TAG = "BaseDialog";
        LogPrint.e("BaseDialog", "BaseDialog.构造");
        this.mNotify = ASRNotify.getInstance();
        Window mWindow = getWindow();
        mWindow.addFlags(16777216);
        WindowManager.LayoutParams l = mWindow.getAttributes();
        l.dimAmount = 0.0f;
        mWindow.setAttributes(l);
        this.mAnimationIn = AnimationUtils.loadAnimation(mWindow.getContext(), R.anim.voice_dialog_enter);
        this.mAnimationOut = AnimationUtils.loadAnimation(mWindow.getContext(), R.anim.voice_dialog_exit);
    }

    public void show() {
        LogPrint.e("BaseDialog", "BaseDialog.show : " + getClass().getSimpleName());
        DialogManager.getManager().dismissAllDialog();
        getWindow().setBackgroundDrawable(new ColorDrawable(0));
        super.show();
        this.mNotify.setLayerHandler(this);
        ActivityUtil.addVoiceDialog(this);
    }

    public PageReturn onASRNotify(DomainResultVo object) {
        PageReturn pageReturn = new PageReturn();
        if (ActionType.BACK.equals(object.getIntent())) {
            pageReturn.isHandler = true;
            dismiss();
        }
        return pageReturn;
    }

    public void dismiss() {
        LogPrint.e("BaseDialog", "BaseDialog.dismiss : " + getClass().getSimpleName());
        LogPrint.i("dialogASRNotify", "setHandler(null)");
        this.mNotify.setLayerHandler((ASRHandler) null);
        ActivityUtil.addVoiceDialog((Dialog) null);
        super.dismiss();
        if (this.timeHandler != null) {
            this.timeHandler.removeCallbacksAndMessages((Object) null);
        }
    }

    public boolean dispatchKeyEvent(@NonNull KeyEvent event) {
        if (event.getAction() != 1 || event.getKeyCode() != 4) {
            return super.dispatchKeyEvent(event);
        }
        dismiss();
        return true;
    }

    public void playTTS(String msg) {
        this.mNotify.playTTS(msg);
    }

    public void delayDismiss(int delayMillis) {
        if (this.timeHandler == null) {
            this.timeHandler = new TimeHandler();
        }
        this.timeHandler.sendEmptyMessageDelayed(0, (long) delayMillis);
    }

    private static class TimeHandler extends Handler {
        private TimeHandler() {
        }

        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            DialogManager.getManager().dismissAllDialog();
        }
    }

    /* access modifiers changed from: protected */
    public Map<String, String> getProperties() {
        return getProperties((String) null);
    }

    /* access modifiers changed from: protected */
    public Map<String, String> getProperties(String asr) {
        Map<String, String> p = new HashMap<>();
        String uuid = CloudUUIDWrapper.getCloudUUID();
        if (!TextUtils.isEmpty(uuid)) {
            p.put("uuid", uuid);
        }
        p.put("channel", Config.getChannelName());
        if (!TextUtils.isEmpty(asr)) {
            p.put(CommonData.TYPE_ASR, asr);
        }
        return p;
    }
}
