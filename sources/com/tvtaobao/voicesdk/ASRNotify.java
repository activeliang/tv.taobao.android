package com.tvtaobao.voicesdk;

import com.tvtaobao.voicesdk.bean.CommandReturn;
import com.tvtaobao.voicesdk.bean.DomainResultVo;
import com.tvtaobao.voicesdk.bean.PageReturn;
import com.tvtaobao.voicesdk.listener.ASRHandler;
import com.tvtaobao.voicesdk.listener.ASRSearchHandler;
import com.tvtaobao.voicesdk.listener.VoiceListener;
import com.tvtaobao.voicesdk.utils.LogPrint;
import java.lang.ref.WeakReference;
import java.util.List;

public class ASRNotify {
    private static String TAG = "ASRNotify";
    private static ASRNotify notify;
    private ASRSearchHandler asrHandlerSearch;
    private ASRHandler layerHandler;
    private ASRHandler pageHandler;
    private WeakReference<VoiceListener> voiceListener;

    public static ASRNotify getInstance() {
        if (notify == null) {
            synchronized (ASRNotify.class) {
                if (notify == null) {
                    notify = new ASRNotify();
                }
            }
        }
        return notify;
    }

    private ASRNotify() {
    }

    public PageReturn isAction(DomainResultVo domainResultVo) {
        LogPrint.i(TAG, TAG + ".isAction layerHandler : " + this.layerHandler);
        PageReturn pageReturn = null;
        if (this.layerHandler != null) {
            pageReturn = this.layerHandler.onASRNotify(domainResultVo);
        }
        if (pageReturn != null && pageReturn.isHandler) {
            return pageReturn;
        }
        LogPrint.i(TAG, TAG + ".isAction pageHandler : " + this.pageHandler);
        if (this.pageHandler != null) {
            pageReturn = this.pageHandler.onASRNotify(domainResultVo);
        }
        return pageReturn;
    }

    public boolean isActionSearch(String keywords, String tts, List<String> tips) {
        LogPrint.i(TAG, TAG + ".isAction asrHandlerSearch=" + this.asrHandlerSearch);
        if (this.asrHandlerSearch != null) {
            return this.asrHandlerSearch.onSearch(keywords, tts, tips);
        }
        return false;
    }

    public void setLayerHandler(ASRHandler handler) {
        LogPrint.i(TAG, TAG + ".setLayerHandler handler : " + handler);
        this.layerHandler = handler;
    }

    public void setHandler(ASRHandler handler) {
        LogPrint.i(TAG, TAG + ".setHandler handler : " + handler);
        this.pageHandler = handler;
    }

    public void setHandlerSearch(ASRSearchHandler handler) {
        LogPrint.i(TAG, TAG + ".setHandlerSearch handler : " + handler);
        this.asrHandlerSearch = handler;
    }

    public void setFeedBack(VoiceListener listener) {
        this.voiceListener = new WeakReference<>(listener);
    }

    public void playTTS(String msg) {
        playTTS(msg, false);
    }

    public void playTTS(String msg, boolean show) {
        if (this.voiceListener != null && this.voiceListener.get() != null) {
            CommandReturn commandReturn = new CommandReturn();
            commandReturn.mIsHandled = true;
            commandReturn.mAction = 1001;
            commandReturn.showUI = show;
            commandReturn.mMessage = msg;
            ((VoiceListener) this.voiceListener.get()).callback(commandReturn);
        }
    }
}
