package android.taobao.windvane.service;

public class WVEventResult {
    public boolean isSuccess;
    public Object resultObj;

    public WVEventResult(boolean isSuccess2, Object resultObj2) {
        this.isSuccess = isSuccess2;
        this.resultObj = resultObj2;
    }

    public WVEventResult(boolean isSuccess2) {
        this.isSuccess = isSuccess2;
        this.resultObj = null;
    }
}
