package com.bftv.fui.thirdparty;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

public abstract class CommonVoiceBroadcastReceiver extends BroadcastReceiver {
    public abstract VoiceFeedback transferMessage(String str, VoiceFeedback voiceFeedback);

    public void onReceive(Context context, Intent intent) {
        String resultData = getResultData();
        Bundle bundle = new Bundle();
        Bundle lastBundle = getResultExtras(false);
        bundle.putParcelable(VoiceContast.COMMON_KEY, transferMessage(resultData, lastBundle != null ? (VoiceFeedback) lastBundle.getParcelable(VoiceContast.COMMON_KEY) : null));
        setResultExtras(bundle);
    }
}
