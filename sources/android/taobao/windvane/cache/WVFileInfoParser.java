package android.taobao.windvane.cache;

import android.taobao.windvane.util.TaoLog;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;

public class WVFileInfoParser {
    public static final long DEFAULT_MAX_AGE = 2592000000L;
    public static final int FILE_INFO_MIN_LEN = 60;
    public static final long S_MAX_AGE = 300000;

    public static WVFileInfo getFileInfo(byte[] info, int offset, int len) {
        try {
            return getFileInfo(new String(info, offset, len, "UTF-8"));
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
            return null;
        }
    }

    private static WVFileInfo getFileInfo(String infoStr) {
        if (infoStr.length() <= 60 || infoStr.charAt(13) != '~' || infoStr.charAt(27) != '~' || infoStr.charAt(60) != '~') {
            return null;
        }
        WVFileInfo fileInfo = new WVFileInfo();
        String[] infoStrArray = infoStr.split("~");
        if (7 != infoStrArray.length) {
            return null;
        }
        try {
            fileInfo.expireTime = Long.parseLong(infoStrArray[0]);
            try {
                fileInfo.lastModified = Long.parseLong(infoStrArray[1]);
                fileInfo.fileName = infoStrArray[2];
                fileInfo.sha256ToHex = infoStrArray[3];
                fileInfo.mimeType = infoStrArray[4];
                fileInfo.etag = infoStrArray[5];
                fileInfo.encoding = infoStrArray[6];
                return fileInfo;
            } catch (NumberFormatException e) {
                e.printStackTrace();
                return null;
            }
        } catch (NumberFormatException e2) {
            e2.printStackTrace();
            return null;
        }
    }

    public static WVFileInfo updateFileInfo(int operation, WVFileInfo fileInfo, FileChannel fChannel) {
        if (TaoLog.getLogStatus()) {
            TaoLog.d("FileInfoParser", "updateFileInfo filename:" + fileInfo.fileName + "operation:" + operation);
        }
        switch (operation) {
            case 1:
                refreshFileInfo(fileInfo, fChannel);
                break;
            case 2:
                long now = System.currentTimeMillis();
                if (fileInfo.expireTime == 0) {
                    fileInfo.expireTime = now + 300000;
                }
                refreshFileInfo(fileInfo, fChannel);
                break;
            case 3:
                fileInfo.valid = false;
                refreshFileInfo(fileInfo, fChannel);
                break;
            case 4:
                long now2 = System.currentTimeMillis();
                if (fileInfo.expireTime == 0) {
                    fileInfo.expireTime = now2 + 300000;
                }
                try {
                    fileInfo.pos = fChannel.size();
                } catch (IOException e) {
                    TaoLog.e("FileInfoParser", "updateFileInfo setPos error:" + fileInfo.fileName + ". fChannel.size():" + e.getMessage());
                }
                refreshFileInfo(fileInfo, fChannel);
                break;
        }
        return fileInfo;
    }

    private static void refreshFileInfo(WVFileInfo fileInfo, FileChannel fInfoChannel) {
        long time = System.currentTimeMillis();
        byte[] infoByte = fileInfo.composeFileInfoStr();
        if (infoByte != null) {
            ByteBuffer buffer = ByteBuffer.allocate(infoByte.length + 1);
            buffer.put(infoByte);
            buffer.put((byte) 10);
            buffer.position(0);
            try {
                fInfoChannel.write(buffer, fileInfo.pos);
            } catch (IOException e) {
                TaoLog.e("FileInfoParser", "refreshFileInfo: write error. " + e.getMessage());
            }
            if (TaoLog.getLogStatus()) {
                TaoLog.d("FileInfoParser", "refreshFileInfo time cost:" + (System.currentTimeMillis() - time));
            }
        }
    }
}
