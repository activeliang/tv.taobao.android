package com.tvtaobao.voicesdk.listener;

import com.tvtaobao.voicesdk.bean.CommandReturn;

public interface VoiceListener {
    void callback(CommandReturn commandReturn);

    void searchResult(String str);
}
