package com.taobao.media.connectionclass;

import com.taobao.muniontaobaosdk.p4p.anticheat.model.ClientTraceData;

class ExponentialGeometricAverage {
    private int mCount;
    private final int mCutover;
    private final double mDecayConstant;
    private double mValue = -1.0d;

    public ExponentialGeometricAverage(double decayConstant) {
        int ceil;
        this.mDecayConstant = decayConstant;
        if (decayConstant == ClientTraceData.b.f47a) {
            ceil = Integer.MAX_VALUE;
        } else {
            ceil = (int) Math.ceil(1.0d / decayConstant);
        }
        this.mCutover = ceil;
    }

    public void addMeasurement(double measurement) {
        double keepConstant = 1.0d - this.mDecayConstant;
        if (this.mCount > this.mCutover) {
            this.mValue = Math.exp((Math.log(this.mValue) * keepConstant) + (this.mDecayConstant * Math.log(measurement)));
        } else if (this.mCount > 0) {
            double retained = (((double) this.mCount) * keepConstant) / (((double) this.mCount) + 1.0d);
            this.mValue = Math.exp((Math.log(this.mValue) * retained) + (Math.log(measurement) * (1.0d - retained)));
        } else {
            this.mValue = measurement;
        }
        this.mCount++;
    }

    public double getAverage() {
        return this.mValue;
    }

    public void reset() {
        this.mValue = -1.0d;
        this.mCount = 0;
    }
}
