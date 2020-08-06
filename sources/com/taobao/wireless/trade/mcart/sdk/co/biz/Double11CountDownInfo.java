package com.taobao.wireless.trade.mcart.sdk.co.biz;

import java.util.List;

public class Double11CountDownInfo {
    private boolean closeCountDown;
    private boolean closeDouble11Model;
    private List<CountDown> mCountDownInfo;
    private List<String> redWords;

    public boolean isCloseCountDown() {
        return this.closeCountDown;
    }

    public void setCloseCountDown(boolean closeCountDown2) {
        this.closeCountDown = closeCountDown2;
    }

    public boolean isCloseDouble11Model() {
        return this.closeDouble11Model;
    }

    public void setCloseDouble11Model(boolean closeDouble11Model2) {
        this.closeDouble11Model = closeDouble11Model2;
    }

    public List<String> getRedWords() {
        return this.redWords;
    }

    public void setRedWords(List<String> redWords2) {
        this.redWords = redWords2;
    }

    public List<CountDown> getCountDownInfo() {
        return this.mCountDownInfo;
    }

    public void setCountDownInfo(List<CountDown> countDownInfo) {
        this.mCountDownInfo = countDownInfo;
    }
}
