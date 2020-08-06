package com.powyin.slide.tool;

public class ColorUtil {
    public static int calculationColor(int startColor, int endColor, float radio) {
        float radio2 = Math.min(1.0f, Math.max(0.0f, radio));
        int sA = startColor >>> 24;
        int sR = (startColor << 8) >>> 24;
        int rR = (int) (((float) sR) + (((float) (((endColor << 8) >>> 24) - sR)) * radio2));
        int sG = (startColor << 16) >>> 24;
        int rG = (int) (((float) sG) + (((float) (((endColor << 16) >>> 24) - sG)) * radio2));
        int sB = (startColor << 24) >>> 24;
        return ((((((0 | ((int) (((float) sA) + (((float) ((endColor >>> 24) - sA)) * radio2)))) << 8) | rR) << 8) | rG) << 8) | ((int) (((float) sB) + (((float) (((endColor << 24) >>> 24) - sB)) * radio2)));
    }
}
