package com.yunos.tv.tvsdk.media;

import android.graphics.SurfaceTexture;
import android.opengl.GLES20;
import android.opengl.GLSurfaceView;
import android.opengl.Matrix;
import android.os.Handler;
import android.util.Log;
import android.view.Surface;
import android.view.SurfaceHolder;
import com.alibaba.analytics.core.device.Constants;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;
import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;
import mtopsdk.common.util.SymbolExpUtil;

public class MediaPlayerRender implements GLSurfaceView.Renderer, SurfaceTexture.OnFrameAvailableListener {
    private static final int FLOAT_SIZE_BYTES = 4;
    private static int GL_TEXTURE_EXTERNAL_OES = 36197;
    private static String TAG = "MediaPlayerRender";
    private static final int TRIANGLE_VERTICES_DATA_POS_OFFSET = 0;
    private static final int TRIANGLE_VERTICES_DATA_STRIDE_BYTES = 20;
    private static final int TRIANGLE_VERTICES_DATA_UV_OFFSET = 3;
    private final int VERTICES_DATA_COUNT = 5;
    private BuildRoundRectData mBuildData;
    private int mDataCount;
    private float mEyeZ = 1.0E-4f;
    private final String mFragmentShader = "#extension GL_OES_EGL_image_external : require\nprecision mediump float;\nvarying vec2 vTextureCoord;\nuniform samplerExternalOES sTexture;\nvoid main() {\n  gl_FragColor = texture2D(sTexture, vTextureCoord);\n}\n";
    private final String mFragmentShaderUnwrap = "#extension GL_OES_EGL_image_external : require\nprecision mediump float;\nvarying vec2 vTextureCoord;\nuniform samplerExternalOES sTexture;\nuniform vec4 vColor;\nuniform int vIsColor;\nvoid main() {\n    if(vIsColor == 0){\n     gl_FragColor = texture2D(sTexture, vTextureCoord);\n    }\n    else{\n      gl_FragColor = vColor;\n    }\n}\n";
    private Handler mHandler = new Handler();
    private float[] mInitColor = {0.0f, 0.0f, 0.0f, 0.0f};
    private boolean mInitData;
    private float[] mMVPMatrix = new float[16];
    protected MediaPlayerView mMediaPlayerView;
    protected float[] mModelMatrix = new float[16];
    private int mProgram;
    private float[] mProjMatrix = new float[16];
    private Float mRotate = Float.valueOf(0.0f);
    private float[] mSTMatrix = new float[16];
    protected Surface mSurface;
    private SurfaceTexture mSurfaceTexture;
    private int mTextureID;
    private FloatBuffer mTriangleVertices;
    protected float[] mVPMatrix = new float[16];
    private final String mVertexShader = "uniform mat4 uMVPMatrix;\nuniform mat4 uSTMatrix;\nattribute vec4 aPosition;\nattribute vec4 aTextureCoord;\nvarying vec2 vTextureCoord;\nvoid main() {\n  gl_Position = uMVPMatrix * aPosition;\n  vTextureCoord = (uSTMatrix * aTextureCoord).xy;\n}\n";
    private Object mVerticesDataLocker = new Object();
    private float[] mViewMatrix = new float[16];
    private int maPositionHandle;
    private int maTextureHandle;
    private int muMVPMatrixHandle;
    private int muSTMatrixHandle;
    private boolean unwrapMode = true;
    private boolean updateSurface = false;

    public MediaPlayerRender(MediaPlayerView view) {
        this.mMediaPlayerView = view;
        initRender();
    }

    public void setRoundedRadiusSize(float leftTop, float rightTop, float leftBottom, float rightBottom) {
        this.mBuildData.setRoundedRadiusSize(leftTop, rightTop, leftBottom, rightBottom);
    }

    public void setRoundedRectSize(int width, int height) {
        Log.i(TAG, "setRoundedRectSize width=" + width + " height=" + height);
        this.mBuildData.setRectSize(width, height);
    }

    public void setInitColor(float r, float g, float b, float a) {
        synchronized (this) {
            if (!this.mInitData) {
                this.mInitColor[0] = r;
                this.mInitColor[1] = g;
                this.mInitColor[2] = b;
                this.mInitColor[3] = a;
            }
        }
    }

    public void reBuildData() {
        if (this.mBuildData.getReBuildDataState()) {
            resetVerticesData(this.mBuildData.buildData());
        }
    }

    public void setRotate(float rotate) {
        synchronized (this.mRotate) {
            this.mRotate = Float.valueOf(rotate);
            this.mEyeZ = ((float) Math.sin(Math.toRadians((double) this.mRotate.floatValue()))) + 1.0E-4f;
            Log.i(TAG, "setRotate eyeZ=" + this.mEyeZ + " mRotate=" + this.mRotate);
        }
    }

    public Surface getSurface() {
        Surface surface;
        synchronized (this) {
            surface = this.mSurface;
        }
        return surface;
    }

    public void stop() {
        synchronized (this) {
            this.mInitData = false;
        }
    }

    public void createSurface() {
        synchronized (this) {
            Log.i(TAG, "createSurface mTextureID=" + this.mTextureID + " mSurface=" + this.mSurface);
            if (this.mTextureID > 0 && this.mSurface == null) {
                if (this.mSurfaceTexture != null) {
                    this.mSurfaceTexture.release();
                }
                this.mSurfaceTexture = new SurfaceTexture(this.mTextureID);
                this.mSurfaceTexture.setOnFrameAvailableListener(this);
                this.mSurface = new Surface(this.mSurfaceTexture);
            }
        }
    }

    public void releaseSurface() {
        synchronized (this) {
            Log.i(TAG, "releaseSurface");
            if (this.mSurface != null) {
                this.mSurface.release();
                this.mSurface = null;
            }
            if (this.mSurfaceTexture != null) {
                this.mSurfaceTexture.release();
                this.mSurfaceTexture = null;
            }
        }
    }

    public void onDrawFrame(GL10 glUnused) {
        synchronized (this) {
            if (this.updateSurface) {
                this.mSurfaceTexture.updateTexImage();
                this.mSurfaceTexture.getTransformMatrix(this.mSTMatrix);
                this.updateSurface = false;
                this.mInitData = true;
            }
            GLES20.glEnable(3042);
            GLES20.glBlendFunc(770, 771);
            GLES20.glClearColor(0.0f, 0.0f, 0.0f, 0.0f);
            GLES20.glClear(16640);
            GLES20.glUseProgram(this.mProgram);
            checkGlError("glUseProgram");
            int isColorHandle = GLES20.glGetUniformLocation(this.mProgram, "vIsColor");
            if (!this.mInitData) {
                GLES20.glUniform1i(isColorHandle, 1);
                GLES20.glUniform4fv(GLES20.glGetUniformLocation(this.mProgram, "vColor"), 1, this.mInitColor, 0);
            } else {
                GLES20.glUniform1i(isColorHandle, 0);
                GLES20.glActiveTexture(33984);
                GLES20.glBindTexture(GL_TEXTURE_EXTERNAL_OES, this.mTextureID);
            }
        }
        synchronized (this.mVerticesDataLocker) {
            this.mTriangleVertices.position(0);
            GLES20.glVertexAttribPointer(this.maPositionHandle, 3, 5126, false, 20, this.mTriangleVertices);
            checkGlError("glVertexAttribPointer maPosition");
            this.mTriangleVertices.position(3);
            GLES20.glEnableVertexAttribArray(this.maPositionHandle);
            checkGlError("glEnableVertexAttribArray maPositionHandle");
            GLES20.glVertexAttribPointer(this.maTextureHandle, 2, 5126, false, 20, this.mTriangleVertices);
            checkGlError("glVertexAttribPointer maTextureHandle");
            GLES20.glEnableVertexAttribArray(this.maTextureHandle);
            checkGlError("glEnableVertexAttribArray maTextureHandle");
        }
        Matrix.setIdentityM(this.mViewMatrix, 0);
        Matrix.setIdentityM(this.mModelMatrix, 0);
        Matrix.translateM(this.mModelMatrix, 0, 0.0f, 0.0f, -1.0f);
        synchronized (this.mRotate) {
            Matrix.setLookAtM(this.mViewMatrix, 0, 0.0f, 0.0f, this.mEyeZ, 0.0f, 0.0f, 0.0f, 0.0f, 1.0f, 0.0f);
            Matrix.rotateM(this.mModelMatrix, 0, this.mRotate.floatValue(), 0.0f, 1.0f, 0.0f);
        }
        Matrix.translateM(this.mModelMatrix, 0, 0.0f, 0.0f, 1.0f);
        Matrix.rotateM(this.mModelMatrix, 0, 180.0f, 1.0f, 0.0f, 0.0f);
        Matrix.multiplyMM(this.mVPMatrix, 0, this.mProjMatrix, 0, this.mViewMatrix, 0);
        Matrix.multiplyMM(this.mMVPMatrix, 0, this.mVPMatrix, 0, this.mModelMatrix, 0);
        GLES20.glUniformMatrix4fv(this.muMVPMatrixHandle, 1, false, this.mMVPMatrix, 0);
        GLES20.glUniformMatrix4fv(this.muSTMatrixHandle, 1, false, this.mSTMatrix, 0);
        GLES20.glDrawArrays(4, 0, this.mDataCount);
        checkGlError("glDrawArrays");
        GLES20.glFinish();
    }

    public void onSurfaceChanged(GL10 glUnused, int width, int height) {
        Log.i(TAG, "MediaPlayerRender onSurfaceChanged");
        GLES20.glViewport(0, 0, width, height);
        float f = ((float) width) / ((float) height);
        Matrix.frustumM(this.mProjMatrix, 0, -1.0f, 1.0f, -1.0f, 1.0f, 1.0f, 10.0f);
    }

    public void onSurfaceCreated(GL10 glUnused, EGLConfig config) {
        Log.i(TAG, "MediaPlayerRender onSurfaceCreated");
        this.mProgram = createProgram("uniform mat4 uMVPMatrix;\nuniform mat4 uSTMatrix;\nattribute vec4 aPosition;\nattribute vec4 aTextureCoord;\nvarying vec2 vTextureCoord;\nvoid main() {\n  gl_Position = uMVPMatrix * aPosition;\n  vTextureCoord = (uSTMatrix * aTextureCoord).xy;\n}\n", this.unwrapMode ? "#extension GL_OES_EGL_image_external : require\nprecision mediump float;\nvarying vec2 vTextureCoord;\nuniform samplerExternalOES sTexture;\nuniform vec4 vColor;\nuniform int vIsColor;\nvoid main() {\n    if(vIsColor == 0){\n     gl_FragColor = texture2D(sTexture, vTextureCoord);\n    }\n    else{\n      gl_FragColor = vColor;\n    }\n}\n" : "#extension GL_OES_EGL_image_external : require\nprecision mediump float;\nvarying vec2 vTextureCoord;\nuniform samplerExternalOES sTexture;\nvoid main() {\n  gl_FragColor = texture2D(sTexture, vTextureCoord);\n}\n");
        if (this.mProgram != 0) {
            this.maPositionHandle = GLES20.glGetAttribLocation(this.mProgram, "aPosition");
            checkGlError("glGetAttribLocation aPosition");
            if (this.maPositionHandle == -1) {
                throw new RuntimeException("Could not get attrib location for aPosition");
            }
            this.maTextureHandle = GLES20.glGetAttribLocation(this.mProgram, "aTextureCoord");
            checkGlError("glGetAttribLocation aTextureCoord");
            if (this.maTextureHandle == -1) {
                throw new RuntimeException("Could not get attrib location for aTextureCoord");
            }
            this.muMVPMatrixHandle = GLES20.glGetUniformLocation(this.mProgram, "uMVPMatrix");
            checkGlError("glGetUniformLocation uMVPMatrix");
            if (this.muMVPMatrixHandle == -1) {
                throw new RuntimeException("Could not get attrib location for uMVPMatrix");
            }
            this.muSTMatrixHandle = GLES20.glGetUniformLocation(this.mProgram, "uSTMatrix");
            checkGlError("glGetUniformLocation uSTMatrix");
            if (this.muSTMatrixHandle == -1) {
                throw new RuntimeException("Could not get attrib location for uSTMatrix");
            }
            int[] textures = new int[1];
            GLES20.glGenTextures(1, textures, 0);
            this.mTextureID = textures[0];
            GLES20.glBindTexture(GL_TEXTURE_EXTERNAL_OES, this.mTextureID);
            checkGlError("glBindTexture mTextureID");
            GLES20.glTexParameterf(GL_TEXTURE_EXTERNAL_OES, 10241, 9728.0f);
            GLES20.glTexParameterf(GL_TEXTURE_EXTERNAL_OES, Constants.MAX_UPLOAD_SIZE, 9729.0f);
            createSurface();
            this.mHandler.post(new Runnable() {
                public void run() {
                    MediaPlayerRender.this.mMediaPlayerView.onRenderSurfaceCreate();
                }
            });
            synchronized (this) {
                this.updateSurface = false;
            }
        }
    }

    public synchronized void onFrameAvailable(SurfaceTexture surface) {
        this.updateSurface = true;
    }

    private void initRender() {
        this.mBuildData = new BuildRoundRectData();
        this.mMediaPlayerView.setEGLConfigChooser(new MediaEGLConfigChooser());
        this.mMediaPlayerView.setEGLContextClientVersion(2);
        this.mMediaPlayerView.setRenderer(this);
        this.mMediaPlayerView.getHolder().addCallback(new SurfaceHolder.Callback() {
            public void surfaceDestroyed(SurfaceHolder holder) {
                holder.setFormat(-2);
            }

            public void surfaceCreated(SurfaceHolder holder) {
                holder.setFormat(-2);
            }

            public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {
                holder.setFormat(-2);
            }
        });
        this.mMediaPlayerView.getHolder().setFormat(-2);
        Matrix.setIdentityM(this.mSTMatrix, 0);
        resetVerticesData(this.mBuildData.buildData());
    }

    private int loadShader(int shaderType, String source) {
        int shader = GLES20.glCreateShader(shaderType);
        if (shader == 0) {
            return shader;
        }
        GLES20.glShaderSource(shader, source);
        GLES20.glCompileShader(shader);
        int[] compiled = new int[1];
        GLES20.glGetShaderiv(shader, 35713, compiled, 0);
        if (compiled[0] != 0) {
            return shader;
        }
        Log.e(TAG, "Could not compile shader " + shaderType + SymbolExpUtil.SYMBOL_COLON);
        Log.e(TAG, GLES20.glGetShaderInfoLog(shader));
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
        int program = GLES20.glCreateProgram();
        if (program == 0) {
            return program;
        }
        GLES20.glAttachShader(program, vertexShader);
        checkGlError("glAttachShader");
        GLES20.glAttachShader(program, pixelShader);
        checkGlError("glAttachShader");
        GLES20.glLinkProgram(program);
        int[] linkStatus = new int[1];
        GLES20.glGetProgramiv(program, 35714, linkStatus, 0);
        if (linkStatus[0] == 1) {
            return program;
        }
        Log.e(TAG, "Could not link program: ");
        Log.e(TAG, GLES20.glGetProgramInfoLog(program));
        GLES20.glDeleteProgram(program);
        return 0;
    }

    private void checkGlError(String op) {
        int error = GLES20.glGetError();
        if (error != 0) {
            Log.e(TAG, op + ": glError " + error);
            throw new RuntimeException(op + ": glError " + error);
        }
    }

    private void resetVerticesData(float[] verticesData) {
        if (verticesData != null) {
            this.mDataCount = verticesData.length / 5;
            Log.i(TAG, "resetVerticesData mDataCount=" + this.mDataCount + " verticesData length=" + verticesData.length);
            synchronized (this.mVerticesDataLocker) {
                this.mTriangleVertices = ByteBuffer.allocateDirect(verticesData.length * 4).order(ByteOrder.nativeOrder()).asFloatBuffer();
                this.mTriangleVertices.put(verticesData).position(0);
            }
        }
    }
}
