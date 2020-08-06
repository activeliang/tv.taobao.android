package com.yunos.tv.tvsdk.media;

import android.util.Log;

public class BuildRoundRectData {
    private final int DEFAULT_ROUND_QUARTER_COUNT = 10;
    private final int EACH_POINT_COUNT = 5;
    private final float RECT_SIZE = 1.0f;
    private final String TAG = "BuildRoundRectData";
    private final float TEXTURE_RECT_SIZE = 1.0f;
    private int mHeight;
    private float mLeftBottomRadius;
    private float mLeftTopRadius;
    private boolean mReNeedBuildData = true;
    private float mRectRatio = 1.0f;
    private float mRightBottomRadius;
    private float mRightTopRadius;
    private int mRoundQuarterCount = 10;
    private float mTextureLeftBottomRadius;
    private float mTextureLeftTopRadius;
    private float mTextureRightBottomRadius;
    private float mTextureRightTopRadius;
    private int mWidth;

    public void setRectSize(int width, int height) {
        if (this.mWidth != width || this.mHeight != height) {
            this.mRectRatio = ((float) width) / ((float) height);
            this.mWidth = width;
            this.mHeight = height;
            this.mReNeedBuildData = true;
        }
    }

    public void setRoundedRadiusSize(float leftTop, float rightTop, float leftBottom, float rightBottom) {
        if (this.mLeftTopRadius != leftTop || this.mRightTopRadius != rightTop || this.mLeftBottomRadius != leftBottom || this.mRightBottomRadius != rightBottom) {
            this.mLeftTopRadius = leftTop;
            this.mRightTopRadius = rightTop;
            this.mLeftBottomRadius = leftBottom;
            this.mRightBottomRadius = rightBottom;
            this.mTextureLeftTopRadius = leftBottom;
            this.mTextureRightTopRadius = rightBottom;
            this.mTextureLeftBottomRadius = leftTop;
            this.mTextureRightBottomRadius = rightTop;
            this.mReNeedBuildData = true;
        }
    }

    public void setRoundQuarterCount(int quarterCount) {
        this.mRoundQuarterCount = quarterCount;
        this.mReNeedBuildData = true;
    }

    public boolean getReBuildDataState() {
        return this.mReNeedBuildData;
    }

    public float[] buildData() {
        Log.i("BuildRoundRectData", "buildData width=" + this.mWidth + " height=" + this.mHeight + " mReNeedBuildData=" + this.mReNeedBuildData + " mLeftTopRadius=" + this.mLeftTopRadius + " mRightTopRadius=" + this.mRightTopRadius + " mLeftBottomRadius=" + this.mLeftBottomRadius + " mRightBottomRadius=" + this.mRightBottomRadius);
        if (!this.mReNeedBuildData) {
            return null;
        }
        float[] displayRoundedArray = buildRoundedData(1.0f, true);
        float[] textureRoundedArray = buildRoundedData(1.0f, false);
        float[] vertexArray = new float[(((this.mLeftTopRadius > 0.0f ? this.mRoundQuarterCount : 1) + (this.mRightTopRadius > 0.0f ? this.mRoundQuarterCount : 1) + (this.mLeftBottomRadius > 0.0f ? this.mRoundQuarterCount : 1) + (this.mRightBottomRadius > 0.0f ? this.mRoundQuarterCount : 1)) * 5 * 3)];
        int offset = 0;
        for (int i = 0; i < displayRoundedArray.length; i += 2) {
            if (i >= displayRoundedArray.length - 2) {
                vertexArray[offset] = displayRoundedArray[i];
                vertexArray[offset + 1] = displayRoundedArray[i + 1];
                vertexArray[offset + 2] = 1.00001f;
                vertexArray[offset + 3] = (1.0f + textureRoundedArray[i]) / 2.0f;
                vertexArray[offset + 4] = (1.0f + textureRoundedArray[i + 1]) / 2.0f;
                int offset2 = offset + 5;
                vertexArray[offset2] = displayRoundedArray[0];
                vertexArray[offset2 + 1] = displayRoundedArray[1];
                vertexArray[offset2 + 2] = 1.00001f;
                vertexArray[offset2 + 3] = (1.0f + textureRoundedArray[0]) / 2.0f;
                vertexArray[offset2 + 4] = (1.0f + textureRoundedArray[1]) / 2.0f;
                offset = offset2 + 5;
                vertexArray[offset] = 0.0f;
                vertexArray[offset + 1] = 0.0f;
                vertexArray[offset + 2] = 1.00001f;
                vertexArray[offset + 3] = 0.5f;
                vertexArray[offset + 4] = 0.5f;
            } else {
                vertexArray[offset] = displayRoundedArray[i];
                vertexArray[offset + 1] = displayRoundedArray[i + 1];
                vertexArray[offset + 2] = 1.00001f;
                vertexArray[offset + 3] = (1.0f + textureRoundedArray[i]) / 2.0f;
                vertexArray[offset + 4] = (1.0f + textureRoundedArray[i + 1]) / 2.0f;
                int offset3 = offset + 5;
                vertexArray[offset3] = displayRoundedArray[i + 2];
                vertexArray[offset3 + 1] = displayRoundedArray[i + 3];
                vertexArray[offset3 + 2] = 1.00001f;
                vertexArray[offset3 + 3] = (1.0f + textureRoundedArray[i + 2]) / 2.0f;
                vertexArray[offset3 + 4] = (1.0f + textureRoundedArray[i + 3]) / 2.0f;
                int offset4 = offset3 + 5;
                vertexArray[offset4] = 0.0f;
                vertexArray[offset4 + 1] = 0.0f;
                vertexArray[offset4 + 2] = 1.00001f;
                vertexArray[offset4 + 3] = 0.5f;
                vertexArray[offset4 + 4] = 0.5f;
                offset = offset4 + 5;
            }
        }
        return vertexArray;
    }

    private float[] buildRoundedData(float size, boolean clockwise) {
        float width;
        float height;
        float leftTopRadius;
        float leftBottomRadius;
        float rightTopRadius;
        float rightBottomRadius;
        int rightTopOffset;
        int leftTopOffset;
        int leftBottomOffset;
        int rightBottomOffset;
        int totaleCount;
        double radians;
        int quarterCount = this.mRoundQuarterCount;
        float delatAng = 90.0f / ((float) (quarterCount - 1));
        if (clockwise) {
            width = size;
            height = size;
        } else {
            width = size;
            height = size;
        }
        int offset = quarterCount * 2;
        if (clockwise) {
            leftTopRadius = this.mLeftTopRadius;
            leftBottomRadius = this.mLeftBottomRadius;
            rightTopRadius = this.mRightTopRadius;
            rightBottomRadius = this.mRightBottomRadius;
            rightBottomOffset = 0;
            leftBottomOffset = rightBottomRadius > 0.0f ? offset : 2;
            leftTopOffset = leftBottomOffset + (leftBottomRadius > 0.0f ? offset : 2);
            rightTopOffset = leftTopOffset + (leftTopRadius > 0.0f ? offset : 2);
            if (rightTopRadius <= 0.0f) {
                offset = 2;
            }
            totaleCount = rightTopOffset + offset;
        } else {
            leftTopRadius = this.mTextureLeftTopRadius;
            leftBottomRadius = this.mTextureLeftBottomRadius;
            rightTopRadius = this.mTextureRightTopRadius;
            rightBottomRadius = this.mTextureRightBottomRadius;
            rightTopOffset = 0;
            leftTopOffset = rightTopRadius > 0.0f ? offset : 2;
            leftBottomOffset = leftTopOffset + (leftTopRadius > 0.0f ? offset : 2);
            rightBottomOffset = leftBottomOffset + (leftBottomRadius > 0.0f ? offset : 2);
            if (rightBottomRadius <= 0.0f) {
                offset = 2;
            }
            totaleCount = rightBottomOffset + offset;
        }
        float[] circleVertexArray = new float[totaleCount];
        for (int i = 0; i < quarterCount; i++) {
            if (clockwise) {
                radians = Math.toRadians((double) (((float) i) * delatAng));
            } else {
                radians = Math.toRadians((double) (90.0f - (((float) i) * delatAng)));
            }
            float cosValue = (float) Math.cos(radians);
            float sinValue = (float) Math.sin(radians);
            if (rightBottomRadius > 0.0f) {
                float xRadius = size * rightBottomRadius;
                float yRadius = xRadius * this.mRectRatio;
                int j = (i * 2) + rightBottomOffset;
                circleVertexArray[j] = (xRadius * cosValue) + (width - xRadius);
                circleVertexArray[j + 1] = (yRadius * sinValue) + (height - yRadius);
            } else if (i == 0) {
                int j2 = rightBottomOffset;
                circleVertexArray[j2] = width;
                circleVertexArray[j2 + 1] = height;
            }
            if (leftBottomRadius > 0.0f) {
                float xRadius2 = size * leftBottomRadius;
                float yRadius2 = xRadius2 * this.mRectRatio;
                int j3 = (i * 2) + leftBottomOffset;
                circleVertexArray[j3] = (xRadius2 - width) - (xRadius2 * sinValue);
                circleVertexArray[j3 + 1] = (yRadius2 * cosValue) + (height - yRadius2);
            } else if (i == 0) {
                int j4 = leftBottomOffset;
                circleVertexArray[j4] = -width;
                circleVertexArray[j4 + 1] = height;
            }
            if (leftTopRadius > 0.0f) {
                float xRadius3 = size * leftTopRadius;
                float yRadius3 = xRadius3 * this.mRectRatio;
                int j5 = (i * 2) + leftTopOffset;
                circleVertexArray[j5] = (xRadius3 - width) - (xRadius3 * cosValue);
                circleVertexArray[j5 + 1] = (yRadius3 - height) - (yRadius3 * sinValue);
            } else if (i == 0) {
                int j6 = leftTopOffset;
                circleVertexArray[j6] = -width;
                circleVertexArray[j6 + 1] = -height;
            }
            if (rightTopRadius > 0.0f) {
                float xRadius4 = size * rightTopRadius;
                float yRadius4 = xRadius4 * this.mRectRatio;
                int j7 = (i * 2) + rightTopOffset;
                circleVertexArray[j7] = (xRadius4 * sinValue) + (width - xRadius4);
                circleVertexArray[j7 + 1] = (yRadius4 - height) - (yRadius4 * cosValue);
            } else if (i == 0) {
                int j8 = rightTopOffset;
                circleVertexArray[j8] = width;
                circleVertexArray[j8 + 1] = -height;
            }
        }
        return circleVertexArray;
    }
}
