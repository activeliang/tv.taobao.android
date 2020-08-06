package android.taobao.windvane.webview;

public class WindVaneError extends Error {
    private static final long serialVersionUID = 8736004749630607428L;

    public WindVaneError() {
    }

    public WindVaneError(String detailMessage) {
        super(detailMessage);
    }

    public WindVaneError(String detailMessage, Throwable throwable) {
        super(detailMessage, throwable);
    }

    public WindVaneError(Throwable throwable) {
        super(throwable);
    }
}
