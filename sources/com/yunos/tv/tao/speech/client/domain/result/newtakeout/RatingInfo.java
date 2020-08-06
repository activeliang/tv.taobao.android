package com.yunos.tv.tao.speech.client.domain.result.newtakeout;

public class RatingInfo {
    private double packageScore;
    private double riderScore;
    private double tasteScore;

    public void setRiderScore(double riderScore2) {
        this.riderScore = riderScore2;
    }

    public double getRiderScore() {
        return this.riderScore;
    }

    public void setPackageScore(double packageScore2) {
        this.packageScore = packageScore2;
    }

    public double getPackageScore() {
        return this.packageScore;
    }

    public void setTasteScore(double tasteScore2) {
        this.tasteScore = tasteScore2;
    }

    public double getTasteScore() {
        return this.tasteScore;
    }
}
