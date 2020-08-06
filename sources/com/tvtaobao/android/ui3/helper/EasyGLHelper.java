package com.tvtaobao.android.ui3.helper;

import android.opengl.GLES20;
import android.util.Log;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;
import javax.microedition.khronos.egl.EGL10;
import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.egl.EGLContext;
import javax.microedition.khronos.egl.EGLDisplay;
import javax.microedition.khronos.egl.EGLSurface;
import javax.microedition.khronos.opengles.GL;
import javax.microedition.khronos.opengles.GL10;
import mtopsdk.common.util.SymbolExpUtil;

public class EasyGLHelper {
    private static final String TAG = "EasyGLHelper";
    private BaseConfigChooser configChooser = new BaseConfigChooser(8, 8, 8, 8, 0, 0);
    private DefaultContextFactory contextFactory = new DefaultContextFactory();
    /* access modifiers changed from: private */
    public int mEGLContextClientVersion = 2;
    EGL10 mEgl;
    EGLConfig mEglConfig;
    EGLContext mEglContext;
    EGLDisplay mEglDisplay;
    EGLSurface mEglSurface;
    Render render;
    private DefaultWindowSurfaceFactory windowSurfaceFactory = new DefaultWindowSurfaceFactory();

    public interface Render {
        void onDrawFrame(GL10 gl10);
    }

    public boolean isInit() {
        if (this.mEglContext == null || this.mEglDisplay == null || this.mEglSurface == null) {
            return false;
        }
        return true;
    }

    public void doInit(SurfaceView surfaceView) {
        createContext();
        createSurface(surfaceView.getHolder());
    }

    private void createContext() {
        Log.w(TAG, "createContext start() tid=" + Thread.currentThread().getId());
        this.mEgl = (EGL10) EGLContext.getEGL();
        this.mEglDisplay = this.mEgl.eglGetDisplay(EGL10.EGL_DEFAULT_DISPLAY);
        if (this.mEglDisplay == EGL10.EGL_NO_DISPLAY) {
            throw new RuntimeException("eglGetDisplay failed");
        }
        if (!this.mEgl.eglInitialize(this.mEglDisplay, new int[2])) {
            throw new RuntimeException("eglInitialize failed");
        }
        this.mEglConfig = this.configChooser.chooseConfig(this.mEgl, this.mEglDisplay);
        this.mEglContext = this.contextFactory.createContext(this.mEgl, this.mEglDisplay, this.mEglConfig);
        if (this.mEglContext == null || this.mEglContext == EGL10.EGL_NO_CONTEXT) {
            this.mEglContext = null;
            throw new RuntimeException("createContext error " + this.mEgl.eglGetError());
        } else {
            Log.w(TAG, "createContext end " + this.mEglContext + " tid=" + Thread.currentThread().getId());
        }
    }

    private boolean createSurface(SurfaceHolder surfaceHolder) {
        Log.w(TAG, "createSurface start  tid=" + Thread.currentThread().getId());
        if (this.mEgl == null) {
            throw new RuntimeException("egl not initialized");
        } else if (this.mEglDisplay == null) {
            throw new RuntimeException("mEglDisplay not initialized");
        } else if (this.mEglConfig == null) {
            throw new RuntimeException("mEglConfig not initialized");
        } else {
            this.mEglSurface = this.windowSurfaceFactory.createWindowSurface(this.mEgl, this.mEglDisplay, this.mEglConfig, surfaceHolder);
            if (this.mEglSurface == null || this.mEglSurface == EGL10.EGL_NO_SURFACE) {
                if (this.mEgl.eglGetError() != 12299) {
                    return false;
                }
                Log.e(TAG, "createSurface createWindowSurface returned EGL_BAD_NATIVE_WINDOW.");
                return false;
            } else if (!this.mEgl.eglMakeCurrent(this.mEglDisplay, this.mEglSurface, this.mEglSurface, this.mEglContext)) {
                Log.e(TAG, "createSurface eglMakeCurrent " + this.mEgl.eglGetError());
                return false;
            } else {
                Log.w(TAG, "createSurface end  tid=" + Thread.currentThread().getId());
                return true;
            }
        }
    }

    public int swap() {
        if (!this.mEgl.eglSwapBuffers(this.mEglDisplay, this.mEglSurface)) {
            Log.e(TAG, "swap " + this.mEgl.eglGetError());
            return this.mEgl.eglGetError();
        }
        Log.e(TAG, "swap success");
        return 12288;
    }

    public GL getGL() {
        if (this.mEglContext != null) {
            return this.mEglContext.getGL();
        }
        return null;
    }

    public void destroySurface() {
        if (this.mEglSurface != null && this.mEglSurface != EGL10.EGL_NO_SURFACE) {
            Log.w(TAG, "destroySurface()  tid=" + Thread.currentThread().getId());
            this.mEgl.eglMakeCurrent(this.mEglDisplay, EGL10.EGL_NO_SURFACE, EGL10.EGL_NO_SURFACE, EGL10.EGL_NO_CONTEXT);
            this.windowSurfaceFactory.destroySurface(this.mEgl, this.mEglDisplay, this.mEglSurface);
            this.mEglSurface = null;
        }
    }

    public void destroyContext() {
        Log.w(TAG, "finish() tid=" + Thread.currentThread().getId());
        if (this.mEglContext != null) {
            this.contextFactory.destroyContext(this.mEgl, this.mEglDisplay, this.mEglContext);
            this.mEglContext = null;
        }
        if (this.mEglDisplay != null) {
            this.mEgl.eglTerminate(this.mEglDisplay);
            this.mEglDisplay = null;
        }
    }

    public Render getRender() {
        return this.render;
    }

    public SimpleRender makeSimpleRender() {
        return new SimpleRender();
    }

    public EasyGLHelper setRender(Render render2) {
        this.render = render2;
        return this;
    }

    public void doFrame() {
        GL gl = getGL();
        if (gl != null && this.render != null) {
            this.render.onDrawFrame((GL10) gl);
        }
    }

    private class DefaultWindowSurfaceFactory {
        private DefaultWindowSurfaceFactory() {
        }

        public EGLSurface createWindowSurface(EGL10 egl, EGLDisplay display, EGLConfig config, Object nativeWindow) {
            try {
                return egl.eglCreateWindowSurface(display, config, nativeWindow, (int[]) null);
            } catch (Throwable e) {
                Log.e(EasyGLHelper.TAG, "eglCreateWindowSurface", e);
                return null;
            }
        }

        public void destroySurface(EGL10 egl, EGLDisplay display, EGLSurface surface) {
            egl.eglDestroySurface(display, surface);
        }
    }

    private class DefaultContextFactory {
        private int EGL_CONTEXT_CLIENT_VERSION;

        private DefaultContextFactory() {
            this.EGL_CONTEXT_CLIENT_VERSION = 12440;
        }

        public EGLContext createContext(EGL10 egl, EGLDisplay display, EGLConfig config) {
            int[] attrib_list = {this.EGL_CONTEXT_CLIENT_VERSION, EasyGLHelper.this.mEGLContextClientVersion, 12344};
            EGLContext eGLContext = EGL10.EGL_NO_CONTEXT;
            if (EasyGLHelper.this.mEGLContextClientVersion == 0) {
                attrib_list = null;
            }
            return egl.eglCreateContext(display, config, eGLContext, attrib_list);
        }

        public void destroyContext(EGL10 egl, EGLDisplay display, EGLContext context) {
            if (!egl.eglDestroyContext(display, context)) {
                Log.e(EasyGLHelper.TAG, "display:" + display + " context: " + context + " tid=" + Thread.currentThread().getId());
            }
        }
    }

    private class BaseConfigChooser {
        protected int mAlphaSize;
        protected int mBlueSize;
        protected int[] mConfigSpec;
        protected int mDepthSize;
        protected int mGreenSize;
        protected int mRedSize;
        protected int mStencilSize;
        private int[] mValue = new int[1];

        public BaseConfigChooser(int redSize, int greenSize, int blueSize, int alphaSize, int depthSize, int stencilSize) {
            this.mConfigSpec = new int[]{12324, redSize, 12323, greenSize, 12322, blueSize, 12321, alphaSize, 12325, depthSize, 12326, stencilSize, 12344};
            this.mRedSize = redSize;
            this.mGreenSize = greenSize;
            this.mBlueSize = blueSize;
            this.mAlphaSize = alphaSize;
            this.mDepthSize = depthSize;
            this.mStencilSize = stencilSize;
        }

        public EGLConfig chooseConfig(EGL10 egl, EGLDisplay display) {
            int[] num_config = new int[1];
            if (!egl.eglChooseConfig(display, this.mConfigSpec, (EGLConfig[]) null, 0, num_config)) {
                throw new IllegalArgumentException("eglChooseConfig failed");
            }
            int numConfigs = num_config[0];
            if (numConfigs <= 0) {
                throw new IllegalArgumentException("No configs match configSpec");
            }
            EGLConfig[] configs = new EGLConfig[numConfigs];
            if (!egl.eglChooseConfig(display, this.mConfigSpec, configs, numConfigs, num_config)) {
                throw new IllegalArgumentException("eglChooseConfig#2 failed");
            }
            EGLConfig config = chooseConfig(egl, display, configs);
            if (config != null) {
                return config;
            }
            throw new IllegalArgumentException("No config chosen");
        }

        public EGLConfig chooseConfig(EGL10 egl, EGLDisplay display, EGLConfig[] configs) {
            for (EGLConfig config : configs) {
                int d = findConfigAttrib(egl, display, config, 12325, 0);
                int s = findConfigAttrib(egl, display, config, 12326, 0);
                if (d >= this.mDepthSize && s >= this.mStencilSize) {
                    int r = findConfigAttrib(egl, display, config, 12324, 0);
                    int g = findConfigAttrib(egl, display, config, 12323, 0);
                    int b = findConfigAttrib(egl, display, config, 12322, 0);
                    int a = findConfigAttrib(egl, display, config, 12321, 0);
                    if (r == this.mRedSize && g == this.mGreenSize && b == this.mBlueSize && a == this.mAlphaSize) {
                        return config;
                    }
                }
            }
            return null;
        }

        private int findConfigAttrib(EGL10 egl, EGLDisplay display, EGLConfig config, int attribute, int defaultValue) {
            if (egl.eglGetConfigAttrib(display, config, attribute, this.mValue)) {
                return this.mValue[0];
            }
            return defaultValue;
        }
    }

    public class SimpleRender implements Render {
        private static final String fragmentShader = "precision mediump float;         \nuniform vec4 uColor;             \nvoid main(){                     \n   gl_FragColor = uColor;        \n}";
        private static final String verticesShader = "attribute vec2 vPosition;            \nvoid main(){                         \n   gl_Position = vec4(vPosition,0,1);\n}";
        private int program = -1;
        private int uColor = -1;
        private int vPosition = -1;
        FloatBuffer vertices = null;

        public SimpleRender() {
        }

        private int loadShader(int shaderType, String sourceCode) {
            int shader = GLES20.glCreateShader(shaderType);
            if (shader == 0) {
                return shader;
            }
            GLES20.glShaderSource(shader, sourceCode);
            GLES20.glCompileShader(shader);
            int[] compiled = new int[1];
            GLES20.glGetShaderiv(shader, 35713, compiled, 0);
            if (compiled[0] != 0) {
                return shader;
            }
            Log.e("ES20_ERROR", "Could not compile shader " + shaderType + SymbolExpUtil.SYMBOL_COLON);
            Log.e("ES20_ERROR", GLES20.glGetShaderInfoLog(shader));
            GLES20.glDeleteShader(shader);
            return 0;
        }

        private int createProgram(String vertexSource, String fragmentSource) {
            int vertexShader = loadShader(35633, vertexSource);
            if (vertexShader == 0) {
                return 0;
            }
            int pixelShader = loadShader(35632, fragmentSource);
            if (pixelShader == 0) {
                return 0;
            }
            int program2 = GLES20.glCreateProgram();
            if (program2 == 0) {
                return program2;
            }
            GLES20.glAttachShader(program2, vertexShader);
            GLES20.glAttachShader(program2, pixelShader);
            GLES20.glLinkProgram(program2);
            int[] linkStatus = new int[1];
            GLES20.glGetProgramiv(program2, 35714, linkStatus, 0);
            if (linkStatus[0] == 1) {
                return program2;
            }
            Log.e("ES20_ERROR", "Could not link program: ");
            Log.e("ES20_ERROR", GLES20.glGetProgramInfoLog(program2));
            GLES20.glDeleteProgram(program2);
            return 0;
        }

        private FloatBuffer getVertices() {
            float[] vertices2 = {0.0f, 0.5f, -0.5f, -0.5f, 0.5f, -0.5f};
            ByteBuffer vbb = ByteBuffer.allocateDirect(vertices2.length * 4);
            vbb.order(ByteOrder.nativeOrder());
            FloatBuffer vertexBuf = vbb.asFloatBuffer();
            vertexBuf.put(vertices2);
            vertexBuf.position(0);
            return vertexBuf;
        }

        /* access modifiers changed from: package-private */
        public boolean isInitOver() {
            if (this.program == -1 || this.vPosition == -1) {
                return false;
            }
            return true;
        }

        public void init(GL10 gl) {
            this.program = createProgram(verticesShader, fragmentShader);
            this.vPosition = GLES20.glGetAttribLocation(this.program, "vPosition");
            this.uColor = GLES20.glGetUniformLocation(this.program, "uColor");
            this.vertices = getVertices();
        }

        public void onDrawFrame(GL10 gl) {
            if (!isInitOver()) {
                init(gl);
                gl.glViewport(0, 0, 1000, 1000);
                gl.glClearColor(0.0f, 0.0f, 0.0f, 0.0f);
                gl.glClear(16640);
                GLES20.glUseProgram(this.program);
                GLES20.glVertexAttribPointer(this.vPosition, 2, 5126, false, 0, this.vertices);
                GLES20.glEnableVertexAttribArray(this.vPosition);
                GLES20.glUniform4f(this.uColor, 0.0f, 1.0f, 0.0f, 1.0f);
            }
            GLES20.glDrawArrays(5, 0, 3);
            EasyGLHelper.this.swap();
        }
    }
}
