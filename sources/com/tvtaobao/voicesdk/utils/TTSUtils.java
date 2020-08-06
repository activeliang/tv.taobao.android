package com.tvtaobao.voicesdk.utils;

import android.content.Context;
import android.text.TextUtils;
import com.tvtaobao.voicesdk.ASRNotify;
import com.tvtaobao.voicesdk.base.SDKInitConfig;
import com.tvtaobao.voicesdk.bean.DomainResultVo;
import com.tvtaobao.voicesdk.dialog.TipsDialog;
import java.util.List;

public class TTSUtils {
    private static String TAG = "TTSUtils";
    private static TTSUtils ttsUtils;
    private DomainResultVo domainResultVO;
    private TipsDialog tipsDialog;

    public static TTSUtils getInstance() {
        if (ttsUtils == null) {
            synchronized (TTSUtils.class) {
                if (ttsUtils == null) {
                    ttsUtils = new TTSUtils();
                }
            }
        }
        return ttsUtils;
    }

    public void setDomainResult(DomainResultVo domainResultVo) {
        this.domainResultVO = domainResultVo;
    }

    public void showDialog(Context context, String tts) {
        if (!SDKInitConfig.needTakeOutTips()) {
            ASRNotify.getInstance().playTTS(tts);
            return;
        }
        if (this.tipsDialog != null) {
            this.tipsDialog.dismiss();
            this.tipsDialog = null;
        }
        this.tipsDialog = new TipsDialog(context);
        this.tipsDialog.setTts(tts, tts);
        this.tipsDialog.setTips((List<String>) null);
        this.tipsDialog.show();
    }

    public void showDialog(Context context) {
        if (this.domainResultVO != null) {
            if (this.tipsDialog != null) {
                this.tipsDialog.dismiss();
                this.tipsDialog = null;
            }
            if (!TextUtils.isEmpty(this.domainResultVO.getSpoken()) || this.domainResultVO.getTips() != null) {
                if (!SDKInitConfig.needTakeOutTips()) {
                    ASRNotify.getInstance().playTTS(this.domainResultVO.getSpoken(), true);
                } else {
                    this.tipsDialog = new TipsDialog(context);
                    this.tipsDialog.setTts(this.domainResultVO.getSpoken(), this.domainResultVO.getSpoken());
                    this.tipsDialog.setTips(this.domainResultVO.getTips());
                    this.tipsDialog.show();
                }
            }
            this.domainResultVO = null;
        }
    }
}
