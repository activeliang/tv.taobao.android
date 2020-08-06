package android.taobao.windvane.cache;

import android.os.Process;
import android.taobao.windvane.file.FileAccesser;
import android.taobao.windvane.file.NotEnoughSpace;
import android.taobao.windvane.util.TaoLog;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Set;

public class WVFileCache {
    public static final int CREATE = 4;
    public static final int DELETE = 3;
    private static final String FILE_INFO = "wv_web_info.dat";
    public static final int READ = 1;
    /* access modifiers changed from: private */
    public static String TAG = "WVFileCache";
    public static final int WRITE = 2;
    /* access modifiers changed from: private */
    public String baseDirPath;
    /* access modifiers changed from: private */
    public FileChannel fInfoChannel;
    private RandomAccessFile fInfoOs;
    private String infoDirPath;
    private boolean isInit;
    private boolean isNoSpaceClear = true;
    /* access modifiers changed from: private */
    public int maxCapacity = 100;
    private boolean sdcard;
    private Map<String, WVFileInfo> storedFile = Collections.synchronizedMap(new FixedSizeLinkedHashMap());

    public WVFileCache(String path, String infoDirPath2, int capacity, boolean sdcard2) {
        this.baseDirPath = path;
        this.infoDirPath = infoDirPath2;
        this.maxCapacity = capacity;
        this.sdcard = sdcard2;
        this.isInit = false;
    }

    public String getDirPath() {
        return this.baseDirPath;
    }

    public boolean isSdcard() {
        return this.sdcard;
    }

    private void setCapacity(int maxCapacity2) {
        if (this.storedFile.size() > maxCapacity2) {
            onFileOverflow();
        }
    }

    public int size() {
        if (this.isInit) {
            return this.storedFile.size();
        }
        return 0;
    }

    /* access modifiers changed from: protected */
    public void finalize() throws Throwable {
        if (this.fInfoOs != null) {
            try {
                this.fInfoOs.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        if (this.fInfoChannel != null) {
            try {
                this.fInfoChannel.close();
            } catch (Exception e2) {
                e2.printStackTrace();
            }
        }
        super.finalize();
    }

    public synchronized boolean init() {
        boolean z = false;
        synchronized (this) {
            if (!this.isInit) {
                File infoFile = new File(this.infoDirPath, FILE_INFO);
                if (!infoFile.exists()) {
                    new File(this.infoDirPath).mkdirs();
                    try {
                        infoFile.createNewFile();
                    } catch (IOException e1) {
                        TaoLog.e(TAG, "init createNewFile:" + e1.getMessage());
                    }
                }
                new File(this.baseDirPath).mkdirs();
                try {
                    this.fInfoOs = new RandomAccessFile(infoFile.getAbsolutePath(), "rw");
                    if (this.fInfoChannel == null) {
                        this.fInfoChannel = this.fInfoOs.getChannel();
                    }
                    if (TaoLog.getLogStatus()) {
                        TaoLog.d(TAG, "lock success process is " + Process.myPid());
                    }
                    long time = System.currentTimeMillis();
                    if (collectFiles()) {
                        if (TaoLog.getLogStatus()) {
                            TaoLog.d(TAG, "init time cost:" + (System.currentTimeMillis() - time));
                        }
                        this.isInit = true;
                        setCapacity(this.maxCapacity);
                        if (this.storedFile.size() == 0) {
                            clear();
                        }
                    }
                } catch (Exception e) {
                    TaoLog.e(TAG, "init fInfoOs RandomAccessFile:" + e.getMessage());
                }
            }
            z = true;
        }
        return z;
    }

    public WVFileInfo getFileInfo(String fileName) {
        if (!this.isInit) {
            return null;
        }
        WVFileInfo info = this.storedFile.get(fileName);
        if (info == null) {
            return null;
        }
        if (new File(this.baseDirPath, fileName).exists()) {
            return info;
        }
        WVFileInfoParser.updateFileInfo(3, info, this.fInfoChannel);
        return null;
    }

    public void updateFileInfo(WVFileInfo fileInfo) {
        String fileName;
        WVFileInfo _info;
        if (this.isInit && fileInfo != null && (fileName = fileInfo.fileName) != null && (_info = this.storedFile.get(fileName)) != null) {
            TaoLog.d(TAG, "update info success");
            fileInfo.pos = _info.pos;
            this.storedFile.put(fileName, WVFileInfoParser.updateFileInfo(2, fileInfo, this.fInfoChannel));
        }
    }

    public byte[] read(String fileName) {
        byte[] bArr = null;
        if (TaoLog.getLogStatus()) {
            TaoLog.d(TAG, "read:" + fileName);
        }
        if (this.isInit) {
            long time = System.currentTimeMillis();
            WVFileInfo fileInfo = this.storedFile.get(fileName);
            if (fileInfo != null) {
                this.storedFile.remove(fileName);
                this.storedFile.put(fileName, WVFileInfoParser.updateFileInfo(1, fileInfo, this.fInfoChannel));
                bArr = FileAccesser.read(new File(this.baseDirPath, fileName));
                if (TaoLog.getLogStatus()) {
                    TaoLog.d(TAG, "read time cost:" + (System.currentTimeMillis() - time));
                }
            }
        }
        return bArr;
    }

    public boolean write(WVFileInfo fileInfo, ByteBuffer data) {
        String fileName;
        if (fileInfo == null || (fileName = fileInfo.fileName) == null) {
            return false;
        }
        if (TaoLog.getLogStatus()) {
            TaoLog.d(TAG, "write:" + fileName);
        }
        if (!this.isInit) {
            return false;
        }
        boolean ret = false;
        File file = new File(this.baseDirPath, fileName);
        try {
            ret = FileAccesser.write(file, data);
        } catch (NotEnoughSpace e) {
            TaoLog.e(TAG, "write error. fileName=" + fileName + ". NotEnoughSpace: " + e.getMessage());
            if (this.isNoSpaceClear) {
                clear();
                try {
                    ret = FileAccesser.write(file, data);
                } catch (NotEnoughSpace e1) {
                    e1.printStackTrace();
                }
            }
        }
        if (!ret) {
            return false;
        }
        WVFileInfo _info = this.storedFile.get(fileName);
        if (_info != null) {
            TaoLog.d(TAG, "writed success, file exist");
            fileInfo.pos = _info.pos;
            this.storedFile.put(fileName, WVFileInfoParser.updateFileInfo(2, fileInfo, this.fInfoChannel).convertToFileInfo());
        } else {
            TaoLog.d(TAG, "writed success, file do not exist");
            this.storedFile.put(fileName, WVFileInfoParser.updateFileInfo(4, fileInfo, this.fInfoChannel).convertToFileInfo());
        }
        return true;
    }

    public boolean delete(String fileName) {
        WVFileInfo fileInfo;
        if (!this.isInit || fileName == null) {
            return false;
        }
        long time = System.currentTimeMillis();
        File file = new File(this.baseDirPath, fileName);
        boolean ret = false;
        if (file.isFile()) {
            ret = file.delete();
        }
        if ((!ret && file.exists()) || (fileInfo = this.storedFile.get(fileName)) == null) {
            return ret;
        }
        TaoLog.d(TAG, "delete success");
        WVFileInfoParser.updateFileInfo(3, fileInfo, this.fInfoChannel);
        this.storedFile.remove(fileName);
        if (TaoLog.getLogStatus()) {
            TaoLog.d(TAG, "delete time cost:" + (System.currentTimeMillis() - time));
        }
        return true;
    }

    public boolean clear() {
        if (this.isInit) {
            boolean ret = true;
            String[] list = new File(this.baseDirPath).list();
            if (list != null) {
                for (String fileName : list) {
                    ret &= delete(fileName);
                }
                return ret;
            }
        }
        return false;
    }

    private void onFileOverflow() {
        TaoLog.d(TAG, "onFileOverflow");
        ArrayList<WVFileInfo> buffer = new ArrayList<>();
        Set<Map.Entry<String, WVFileInfo>> entrySet = this.storedFile.entrySet();
        int size = this.storedFile.size();
        for (Map.Entry<String, WVFileInfo> entry : entrySet) {
            if (size < this.maxCapacity) {
                break;
            }
            WVFileInfo info = entry.getValue();
            if (info != null) {
                buffer.add(info);
            }
            size--;
        }
        Iterator i$ = buffer.iterator();
        while (i$.hasNext()) {
            delete(i$.next().fileName);
        }
    }

    private boolean collectFiles() {
        long time = System.currentTimeMillis();
        byte[] infoByte = null;
        try {
            ByteBuffer buffer = ByteBuffer.allocate((int) this.fInfoChannel.size());
            this.fInfoChannel.read(buffer);
            infoByte = buffer.array();
        } catch (IOException e) {
            TaoLog.e(TAG, "collectFiles fInfoChannel.read error:" + e.getMessage());
        }
        if (TaoLog.getLogStatus()) {
            TaoLog.d(TAG, "collectFiles read fileinfo:" + (System.currentTimeMillis() - time));
        }
        long time2 = System.currentTimeMillis();
        if (infoByte == null) {
            return false;
        }
        boolean reWrite = false;
        TaoLog.d("collectFiles", "read fileinfo success");
        int offset = 60;
        int startIndex = 0;
        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        while (offset < infoByte.length) {
            if (infoByte[offset] == 10) {
                WVFileInfo fileInfo = WVFileInfoParser.getFileInfo(infoByte, startIndex, offset - startIndex);
                if (fileInfo != null) {
                    String fileName = fileInfo.fileName;
                    if (!this.storedFile.containsKey(fileName)) {
                        fileInfo.pos = (long) bos.size();
                        this.storedFile.put(fileName, fileInfo);
                        bos.write(infoByte, startIndex, (offset - startIndex) + 1);
                    } else {
                        reWrite = true;
                    }
                } else {
                    reWrite = true;
                }
                startIndex = offset + 1;
                offset += 60;
            }
            offset++;
        }
        if (TaoLog.getLogStatus()) {
            TaoLog.d(TAG, "parse fileinfo:" + (System.currentTimeMillis() - time2));
        }
        long time3 = System.currentTimeMillis();
        if (reWrite) {
            try {
                this.fInfoChannel.truncate(0);
                this.fInfoChannel.position(0);
                ByteBuffer buffer2 = ByteBuffer.wrap(bos.toByteArray());
                buffer2.position(0);
                this.fInfoChannel.write(buffer2);
            } catch (IOException e2) {
                TaoLog.e(TAG, "collectFiles fInfoChannel.write error:" + e2.getMessage());
            }
        }
        try {
            bos.close();
        } catch (IOException e3) {
            e3.printStackTrace();
        }
        if (TaoLog.getLogStatus()) {
            TaoLog.d(TAG, "write fileinfo:" + (System.currentTimeMillis() - time3));
        }
        return true;
    }

    protected class FixedSizeLinkedHashMap<K, V> extends LinkedHashMap<K, V> {
        private static final long serialVersionUID = 1;

        protected FixedSizeLinkedHashMap() {
        }

        /* access modifiers changed from: protected */
        public boolean removeEldestEntry(Map.Entry<K, V> eldest) {
            if (size() <= WVFileCache.this.maxCapacity) {
                return false;
            }
            if (TaoLog.getLogStatus()) {
                TaoLog.d(WVFileCache.TAG, "removeEldestEntry, size:" + size() + " " + eldest.getKey());
            }
            V value = eldest.getValue();
            if (value instanceof WVFileInfo) {
                WVFileInfo info = (WVFileInfo) value;
                if (FileAccesser.deleteFile(new File(WVFileCache.this.baseDirPath, info.fileName))) {
                    WVFileInfoParser.updateFileInfo(3, info, WVFileCache.this.fInfoChannel);
                }
            }
            return true;
        }
    }
}
