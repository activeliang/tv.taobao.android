package com.taobao.mediaplay.common;

import com.taobao.mediaplay.model.MediaVideoResponse;

public interface IVideoNetworkListener {
    void onError(MediaVideoResponse mediaVideoResponse);

    void onSuccess(MediaVideoResponse mediaVideoResponse);
}
