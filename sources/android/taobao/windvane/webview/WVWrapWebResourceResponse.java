package android.taobao.windvane.webview;

import java.io.InputStream;

public class WVWrapWebResourceResponse {
    public String mEncoding;
    public InputStream mInputStream;
    public String mMimeType;

    public WVWrapWebResourceResponse(String mimeType, String encoding, InputStream data) {
        this.mMimeType = mimeType;
        this.mEncoding = encoding;
        this.mInputStream = data;
    }
}
