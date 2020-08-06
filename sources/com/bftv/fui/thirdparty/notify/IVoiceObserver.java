package com.bftv.fui.thirdparty.notify;

import com.bftv.fui.thirdparty.InterceptionData;
import com.bftv.fui.thirdparty.VoiceFeedback;

public interface IVoiceObserver {
    VoiceFeedback update(InterceptionData interceptionData);
}
