package com.yunos.tv.alitvasrsdk;

import android.os.Bundle;

public interface OnASRCommandListener {

    public enum ASRListenerType {
        DEFAULT_LISTENER,
        MOVIE_LISTENER,
        MUSIC_LISTENER,
        TVSHOW_LISTENER,
        ELECTRICAL_LISTENER
    }

    public enum ASRServiceStatus {
        ASR_SERVICE_STATUS_CONNECTED,
        ASR_SERVICE_STATUS_DISCONNECTED
    }

    public enum ASRStatus {
        ASR_STATUS_NULL,
        ASR_STATUS_RECORD_START,
        ASR_STATUS_RECORD_END,
        ASR_STATUS_RECOGNIZE_START,
        ASR_STATUS_RECOGNIZE_END,
        ASR_STATUS_VOLUME_UPDATE,
        ASR_STATUS_ERROR
    }

    Bundle asrToClient(Bundle bundle);

    void getAppContextData(AppContextData appContextData);

    Bundle getSceneInfo(Bundle bundle);

    ASRCommandReturn onASRResult(String str, boolean z);

    void onASRServiceStatusUpdated(ASRServiceStatus aSRServiceStatus);

    void onASRStatusUpdated(ASRStatus aSRStatus, Bundle bundle);

    ASRCommandReturn onNLUResult(String str, String str2, String str3, Bundle bundle);
}
