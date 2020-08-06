package com.bftv.fui.thirdparty.notify;

import com.bftv.fui.thirdparty.InterceptionData;
import com.bftv.fui.thirdparty.VoiceFeedback;

public class DataChange extends VoiceObservable {
    private static DataChange sDataChange = null;

    public static DataChange getInstance() {
        if (sDataChange == null) {
            sDataChange = new DataChange();
        }
        return sDataChange;
    }

    public VoiceFeedback notifyDataChange(InterceptionData interceptionData) {
        setChanged();
        return notifyObservers(interceptionData);
    }
}
