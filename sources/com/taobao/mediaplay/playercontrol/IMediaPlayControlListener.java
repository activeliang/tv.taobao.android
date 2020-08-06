package com.taobao.mediaplay.playercontrol;

public interface IMediaPlayControlListener {
    boolean onPlayRateChanged(float f);

    void screenButtonClick();

    void seekTo(int i);
}
