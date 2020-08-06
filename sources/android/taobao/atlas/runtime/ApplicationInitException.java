package android.taobao.atlas.runtime;

import org.osgi.framework.BundleException;

public class ApplicationInitException extends BundleException {
    public ApplicationInitException(Throwable throwable) {
        super(throwable);
    }

    public ApplicationInitException(String detailMessage, Throwable throwable) {
        super(detailMessage, throwable);
    }

    public ApplicationInitException(String detailMessage) {
        super(detailMessage);
    }
}
