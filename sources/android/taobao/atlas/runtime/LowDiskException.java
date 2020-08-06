package android.taobao.atlas.runtime;

public class LowDiskException extends RuntimeException {
    public static int thredshold = 150;
    private transient Throwable throwable;

    public LowDiskException(String msg, Throwable throwable2) {
        super(msg, throwable2);
        this.throwable = throwable2;
    }

    public LowDiskException(String msg) {
        super(msg);
        this.throwable = null;
    }

    public Throwable getNestedException() {
        return this.throwable;
    }
}
