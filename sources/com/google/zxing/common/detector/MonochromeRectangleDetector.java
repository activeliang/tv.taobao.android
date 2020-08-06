package com.google.zxing.common.detector;

import com.google.zxing.NotFoundException;
import com.google.zxing.ResultPoint;
import com.google.zxing.common.BitMatrix;

@Deprecated
public final class MonochromeRectangleDetector {
    private static final int MAX_MODULES = 32;
    private final BitMatrix image;

    public MonochromeRectangleDetector(BitMatrix image2) {
        this.image = image2;
    }

    public ResultPoint[] detect() throws NotFoundException {
        int height = this.image.getHeight();
        int width = this.image.getWidth();
        int halfHeight = height / 2;
        int halfWidth = width / 2;
        int deltaY = Math.max(1, height / 256);
        int deltaX = Math.max(1, width / 256);
        int top = ((int) findCornerFromCenter(halfWidth, 0, 0, width, halfHeight, -deltaY, 0, height, halfWidth / 2).getY()) - 1;
        ResultPoint pointB = findCornerFromCenter(halfWidth, -deltaX, 0, width, halfHeight, 0, top, height, halfHeight / 2);
        int left = ((int) pointB.getX()) - 1;
        ResultPoint pointC = findCornerFromCenter(halfWidth, deltaX, left, width, halfHeight, 0, top, height, halfHeight / 2);
        int right = ((int) pointC.getX()) + 1;
        ResultPoint pointD = findCornerFromCenter(halfWidth, 0, left, right, halfHeight, deltaY, top, height, halfWidth / 2);
        return new ResultPoint[]{findCornerFromCenter(halfWidth, 0, left, right, halfHeight, -deltaY, top, ((int) pointD.getY()) + 1, halfWidth / 4), pointB, pointC, pointD};
    }

    private ResultPoint findCornerFromCenter(int centerX, int deltaX, int left, int right, int centerY, int deltaY, int top, int bottom, int maxWhiteRun) throws NotFoundException {
        int[] range;
        char c;
        int[] lastRange = null;
        int y = centerY;
        int x = centerX;
        while (y < bottom && y >= top && x < right && x >= left) {
            if (deltaX == 0) {
                range = blackWhiteRange(y, maxWhiteRun, left, right, true);
            } else {
                range = blackWhiteRange(x, maxWhiteRun, top, bottom, false);
            }
            if (range != null) {
                lastRange = range;
                y += deltaY;
                x += deltaX;
            } else if (lastRange == null) {
                throw NotFoundException.getNotFoundInstance();
            } else if (deltaX == 0) {
                int lastY = y - deltaY;
                if (lastRange[0] >= centerX) {
                    return new ResultPoint((float) lastRange[1], (float) lastY);
                }
                if (lastRange[1] <= centerX) {
                    return new ResultPoint((float) lastRange[0], (float) lastY);
                }
                if (deltaY > 0) {
                    c = 0;
                } else {
                    c = 1;
                }
                return new ResultPoint((float) lastRange[c], (float) lastY);
            } else {
                int lastX = x - deltaX;
                if (lastRange[0] >= centerY) {
                    return new ResultPoint((float) lastX, (float) lastRange[1]);
                }
                if (lastRange[1] <= centerY) {
                    return new ResultPoint((float) lastX, (float) lastRange[0]);
                }
                return new ResultPoint((float) lastX, (float) lastRange[deltaX < 0 ? (char) 0 : 1]);
            }
        }
        throw NotFoundException.getNotFoundInstance();
    }

    private int[] blackWhiteRange(int fixedDimension, int maxWhiteRun, int minDim, int maxDim, boolean horizontal) {
        int whiteRunStart;
        int center = (minDim + maxDim) / 2;
        int start = center;
        while (true) {
            if (start < minDim) {
                break;
            } else if (!horizontal ? this.image.get(fixedDimension, start) : this.image.get(start, fixedDimension)) {
                start--;
            } else {
                whiteRunStart = start;
                while (true) {
                    start--;
                    if (start < minDim) {
                        break;
                    } else if (horizontal) {
                        if (this.image.get(start, fixedDimension)) {
                            break;
                        }
                    } else if (this.image.get(fixedDimension, start)) {
                        break;
                    }
                }
                int whiteRunSize = whiteRunStart - start;
                if (start < minDim || whiteRunSize > maxWhiteRun) {
                    start = whiteRunStart;
                }
            }
        }
        start = whiteRunStart;
        int start2 = start + 1;
        int end = center;
        while (true) {
            if (end >= maxDim) {
                break;
            } else if (!horizontal ? this.image.get(fixedDimension, end) : this.image.get(end, fixedDimension)) {
                end++;
            } else {
                int whiteRunStart2 = end;
                while (true) {
                    end++;
                    if (end >= maxDim) {
                        break;
                    } else if (horizontal) {
                        if (this.image.get(end, fixedDimension)) {
                            break;
                        }
                    } else if (this.image.get(fixedDimension, end)) {
                        break;
                    }
                }
                int whiteRunSize2 = end - whiteRunStart2;
                if (end >= maxDim || whiteRunSize2 > maxWhiteRun) {
                    end = whiteRunStart2;
                }
            }
        }
        int end2 = end - 1;
        if (end2 <= start2) {
            return null;
        }
        return new int[]{start2, end2};
    }
}
