package android.taobao.windvane.cache;

import android.taobao.windvane.util.TaoLog;
import android.text.TextUtils;
import java.io.UnsupportedEncodingException;

public class WVFileInfo {
    private static final String DEFAULT_ENCODING = "utf-8";
    private static final String DEFAULT_NULL = "";
    private static final String DEFAULT_TIME_ZERO = "0000000000000";
    public static final char DIVISION = '~';
    private static final char PARTITION = '_';
    public String encoding;
    public String etag;
    public long expireTime;
    public String fileName;
    public long lastModified;
    public String mimeType;
    public long pos;
    public String sha256ToHex;
    public boolean valid = true;

    /* Debug info: failed to restart local var, previous not found, register: 4 */
    public WVFileInfo convertToFileInfo() {
        if (getClass().equals(WVFileInfo.class)) {
            return this;
        }
        WVFileInfo newInstance = new WVFileInfo();
        newInstance.expireTime = this.expireTime;
        newInstance.lastModified = this.lastModified;
        newInstance.fileName = this.fileName;
        newInstance.mimeType = this.mimeType;
        newInstance.sha256ToHex = this.sha256ToHex;
        newInstance.etag = this.etag;
        newInstance.encoding = this.encoding;
        newInstance.pos = this.pos;
        newInstance.valid = this.valid;
        return newInstance;
    }

    public int compareTo(WVFileInfo another) {
        if (this == another) {
            return 0;
        }
        if (this.expireTime > another.expireTime) {
            return 1;
        }
        return -1;
    }

    public byte[] composeFileInfoStr() {
        StringBuilder filePath = new StringBuilder();
        if (this.expireTime > 0) {
            filePath.append(this.expireTime);
        } else {
            filePath.append(DEFAULT_TIME_ZERO);
        }
        if (this.valid) {
            filePath.append(DIVISION);
        } else {
            filePath.append(PARTITION);
        }
        if (this.lastModified > 0) {
            filePath.append(this.lastModified);
        } else {
            filePath.append(DEFAULT_TIME_ZERO);
        }
        if (this.valid) {
            filePath.append(DIVISION);
        } else {
            filePath.append(PARTITION);
        }
        if (this.fileName == null) {
            filePath.append("");
        } else {
            filePath.append(this.fileName);
        }
        if (this.valid) {
            filePath.append(DIVISION);
        } else {
            filePath.append(PARTITION);
        }
        if (this.sha256ToHex == null) {
            filePath.append("");
        } else {
            filePath.append(this.sha256ToHex);
        }
        if (this.valid) {
            filePath.append(DIVISION);
        } else {
            filePath.append(PARTITION);
        }
        if (this.mimeType == null) {
            filePath.append("");
        } else {
            filePath.append(this.mimeType);
        }
        if (this.valid) {
            filePath.append(DIVISION);
        } else {
            filePath.append(PARTITION);
        }
        if (this.etag == null) {
            filePath.append("");
        } else {
            filePath.append(this.etag);
        }
        if (this.valid) {
            filePath.append(DIVISION);
        } else {
            filePath.append(PARTITION);
        }
        if (TextUtils.isEmpty(this.encoding)) {
            filePath.append("utf-8");
        } else {
            filePath.append(this.encoding);
        }
        if (TaoLog.getLogStatus()) {
            TaoLog.d("FileInfo", "composeFileInfoStr:" + filePath);
        }
        try {
            return filePath.toString().getBytes("UTF-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
            return null;
        }
    }
}
