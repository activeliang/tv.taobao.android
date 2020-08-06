package com.yunos.tv.blitz.utils;

import android.view.KeyEvent;
import android.view.SoundEffectConstants;
import android.view.View;

public class KeySoundUtil {
    private static final String TAG = "KeySoundUtil";

    public static void playSoundEffectOnDirectionKey(View v, int keyCode, KeyEvent event) {
        int direction;
        if (event != null && event.getAction() == 0 && (direction = getFocusDirection(keyCode, event)) != 0) {
            v.playSoundEffect(SoundEffectConstants.getContantForFocusDirection(direction));
        }
    }

    public static void playSoundEffect(View v) {
        if (v != null) {
            v.playSoundEffect(SoundEffectConstants.getContantForFocusDirection(17));
        }
    }

    public static int getFocusDirection(int keyCode, KeyEvent event) {
        if (keyCode == 21) {
            if (event.hasNoModifiers()) {
                return 17;
            }
            return 0;
        } else if (keyCode == 22) {
            if (event.hasNoModifiers()) {
                return 66;
            }
            return 0;
        } else if (keyCode == 19) {
            if (event.hasNoModifiers()) {
                return 33;
            }
            return 0;
        } else if (keyCode == 20) {
            if (event.hasNoModifiers()) {
                return 130;
            }
            return 0;
        } else if (keyCode != 61) {
            return 0;
        } else {
            if (event.hasNoModifiers()) {
                return 2;
            }
            if (event.hasModifiers(1)) {
                return 1;
            }
            return 0;
        }
    }
}
