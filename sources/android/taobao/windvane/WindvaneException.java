package android.taobao.windvane;

public class WindvaneException extends Exception {
    private static final long serialVersionUID = -4046535764429263458L;
    private int errorCode;

    public WindvaneException() {
    }

    public WindvaneException(String detailMessage) {
        super(detailMessage);
    }

    public WindvaneException(String detailMessage, int errorCode2) {
        super(detailMessage);
        this.errorCode = errorCode2;
    }

    public WindvaneException(String detailMessage, Throwable throwable) {
        super(detailMessage, throwable);
    }

    public WindvaneException(Throwable throwable) {
        super(throwable);
    }

    public WindvaneException(Throwable throwable, int errorCode2) {
        super(throwable);
        this.errorCode = errorCode2;
    }

    public int getErrorCode() {
        return this.errorCode;
    }
}
