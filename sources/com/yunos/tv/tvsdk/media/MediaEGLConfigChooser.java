package com.yunos.tv.tvsdk.media;

import android.opengl.GLSurfaceView;
import javax.microedition.khronos.egl.EGL10;
import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.egl.EGLDisplay;

class MediaEGLConfigChooser implements GLSurfaceView.EGLConfigChooser {
    private static final String TAG = "MediaEGLConfigChooser";
    private final int[] mConfigSpec888 = {12324, 8, 12323, 8, 12322, 8, 12321, 0, 12338, 1, 12337, 2, 12344};

    MediaEGLConfigChooser() {
    }

    public EGLConfig chooseConfig(EGL10 egl, EGLDisplay display) {
        int[] numConfig = new int[1];
        int[] mConfigSpec = this.mConfigSpec888;
        if (!egl.eglChooseConfig(display, mConfigSpec, (EGLConfig[]) null, 0, numConfig)) {
            throw new RuntimeException("eglChooseConfig failed");
        } else if (numConfig[0] <= 0) {
            throw new RuntimeException("No configs match configSpec");
        } else {
            EGLConfig[] configs = new EGLConfig[numConfig[0]];
            if (egl.eglChooseConfig(display, mConfigSpec, configs, configs.length, numConfig)) {
                return chooseConfig(egl, display, configs);
            }
            throw new RuntimeException();
        }
    }

    private EGLConfig chooseConfig(EGL10 egl, EGLDisplay display, EGLConfig[] configs) {
        EGLConfig result = null;
        int minStencil = Integer.MAX_VALUE;
        int[] value = new int[1];
        int i = 0;
        int n = configs.length;
        while (i < n) {
            if (egl.eglGetConfigAttrib(display, configs[i], 12326, value)) {
                if (value[0] != 0 && value[0] < minStencil) {
                    minStencil = value[0];
                    result = configs[i];
                }
                i++;
            } else {
                throw new RuntimeException("eglGetConfigAttrib error: " + egl.eglGetError());
            }
        }
        if (result == null) {
            result = configs[0];
        }
        egl.eglGetConfigAttrib(display, result, 12326, value);
        return result;
    }
}
