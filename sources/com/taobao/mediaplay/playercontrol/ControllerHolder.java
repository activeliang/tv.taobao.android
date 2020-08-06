package com.taobao.mediaplay.playercontrol;

import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.TextView;

class ControllerHolder {
    public View controllerLayout;
    public TextView currentTimeTv;
    public int fullscreenResId;
    public View parentLayout;
    public SeekBar seekBar;
    public ImageView toggleScreenButton;
    public FrameLayout toggleScreenButtonContainer;
    public TextView totalTimeTv;
    public int unFullscreenResId;

    ControllerHolder() {
    }
}
