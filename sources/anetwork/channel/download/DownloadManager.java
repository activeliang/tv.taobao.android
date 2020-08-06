package anetwork.channel.download;

import android.content.Context;
import android.os.RemoteException;
import android.taobao.windvane.util.WVNativeCallbackUtil;
import android.text.TextUtils;
import android.util.SparseArray;
import anet.channel.util.ALog;
import anet.channel.util.HttpHelper;
import anet.channel.util.StringUtils;
import anetwork.channel.Header;
import anetwork.channel.aidl.Connection;
import anetwork.channel.http.NetworkSdkSetting;
import com.alibaba.sdk.android.oss.common.utils.HttpHeaders;
import java.io.File;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;
import java.util.Map;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.LinkedBlockingDeque;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;

public class DownloadManager {
    private static final String DOWNLOAD_FOLDER = "downloads";
    public static final int ERROR_DOWNLOAD_CANCELLED = -105;
    public static final int ERROR_EXCEPTION_HAPPEN = -104;
    public static final int ERROR_FILE_FOLDER_INVALID = -101;
    public static final int ERROR_IO_EXCEPTION = -103;
    public static final int ERROR_REQUEST_FAIL = -102;
    public static final int ERROR_URL_INVALID = -100;
    public static final String TAG = "anet.DownloadManager";
    /* access modifiers changed from: private */
    public Context context;
    private ThreadPoolExecutor executor;
    /* access modifiers changed from: private */
    public AtomicInteger taskIdGen;
    /* access modifiers changed from: private */
    public SparseArray<DownloadTask> taskMap;

    public interface DownloadListener {
        void onFail(int i, int i2, String str);

        void onProgress(int i, long j, long j2);

        void onSuccess(int i, String str);
    }

    public static DownloadManager getInstance() {
        return ClassHolder.instance;
    }

    private DownloadManager() {
        this.taskMap = new SparseArray<>(6);
        this.taskIdGen = new AtomicInteger(0);
        this.executor = new ThreadPoolExecutor(2, 2, 30, TimeUnit.SECONDS, new LinkedBlockingDeque());
        this.context = null;
        this.context = NetworkSdkSetting.getContext();
        this.executor.allowCoreThreadTimeOut(true);
        prepareDownloadFolder();
    }

    private static class ClassHolder {
        static DownloadManager instance = new DownloadManager();

        private ClassHolder() {
        }
    }

    public int enqueue(String urlString, String fileName, DownloadListener listener) {
        return enqueue(urlString, (String) null, fileName, listener);
    }

    public int enqueue(String urlString, String fileFolder, String fileName, DownloadListener listener) {
        if (ALog.isPrintLog(2)) {
            ALog.i(TAG, "enqueue", (String) null, "folder", fileFolder, "filename", fileName, "url", urlString);
        }
        if (this.context == null) {
            ALog.e(TAG, "network not initial.", (String) null, new Object[0]);
            return -1;
        }
        try {
            URL url = new URL(urlString);
            if (TextUtils.isEmpty(fileFolder) || prepareFolder(fileFolder)) {
                synchronized (this.taskMap) {
                    int size = this.taskMap.size();
                    int i = 0;
                    while (true) {
                        if (i < size) {
                            DownloadTask downloadTask = this.taskMap.valueAt(i);
                            if (!url.equals(downloadTask.url)) {
                                i++;
                            } else if (downloadTask.attachListener(listener)) {
                                int i2 = downloadTask.taskId;
                                return i2;
                            }
                        }
                    }
                    DownloadTask downloadTask2 = new DownloadTask(url, fileFolder, fileName, listener);
                    this.taskMap.put(downloadTask2.taskId, downloadTask2);
                    this.executor.submit(downloadTask2);
                    int i3 = downloadTask2.taskId;
                    return i3;
                }
            }
            ALog.e(TAG, "file folder invalid.", (String) null, new Object[0]);
            if (listener != null) {
                listener.onFail(-1, -101, "file folder path invalid");
            }
            return -1;
        } catch (MalformedURLException e) {
            ALog.e(TAG, "url invalid.", (String) null, e, new Object[0]);
            if (listener != null) {
                listener.onFail(-1, -100, "url invalid");
            }
            return -1;
        }
    }

    public void cancel(int taskId) {
        synchronized (this.taskMap) {
            DownloadTask task = this.taskMap.get(taskId);
            if (task != null) {
                if (ALog.isPrintLog(2)) {
                    ALog.i(TAG, "try cancel task" + taskId + " url=" + task.url.toString(), (String) null, new Object[0]);
                }
                this.taskMap.remove(taskId);
                task.cancel();
            }
        }
    }

    class DownloadTask implements Runnable {
        private volatile Connection conn = null;
        private final String filePath;
        private final AtomicBoolean isCancelled = new AtomicBoolean(false);
        private final AtomicBoolean isFinish = new AtomicBoolean(false);
        private final CopyOnWriteArrayList<DownloadListener> listenerList;
        final int taskId;
        final URL url;

        DownloadTask(URL url2, String fileFolder, String fileName, DownloadListener listener) {
            this.taskId = DownloadManager.this.taskIdGen.getAndIncrement();
            this.url = url2;
            fileName = TextUtils.isEmpty(fileName) ? parseFileNameForURL(url2) : fileName;
            if (TextUtils.isEmpty(fileFolder)) {
                this.filePath = DownloadManager.this.getDownloadFilePath(fileName);
            } else if (fileFolder.endsWith(WVNativeCallbackUtil.SEPERATER)) {
                this.filePath = fileFolder + fileName;
            } else {
                this.filePath = fileFolder + '/' + fileName;
            }
            this.listenerList = new CopyOnWriteArrayList<>();
            this.listenerList.add(listener);
        }

        public boolean attachListener(DownloadListener listener) {
            if (this.isFinish.get()) {
                return false;
            }
            this.listenerList.add(listener);
            return true;
        }

        public void cancel() {
            this.isCancelled.set(true);
            notifyFail(-105, "download canceled.");
            if (this.conn != null) {
                try {
                    this.conn.cancel();
                } catch (RemoteException e) {
                }
            }
        }

        /*  JADX ERROR: IndexOutOfBoundsException in pass: RegionMakerVisitor
            java.lang.IndexOutOfBoundsException: Index: 0, Size: 0
            	at java.util.ArrayList.rangeCheck(ArrayList.java:657)
            	at java.util.ArrayList.get(ArrayList.java:433)
            	at jadx.core.dex.nodes.InsnNode.getArg(InsnNode.java:101)
            	at jadx.core.dex.visitors.regions.RegionMaker.traverseMonitorExits(RegionMaker.java:611)
            	at jadx.core.dex.visitors.regions.RegionMaker.traverseMonitorExits(RegionMaker.java:619)
            	at jadx.core.dex.visitors.regions.RegionMaker.traverseMonitorExits(RegionMaker.java:619)
            	at jadx.core.dex.visitors.regions.RegionMaker.traverseMonitorExits(RegionMaker.java:619)
            	at jadx.core.dex.visitors.regions.RegionMaker.traverseMonitorExits(RegionMaker.java:619)
            	at jadx.core.dex.visitors.regions.RegionMaker.processMonitorEnter(RegionMaker.java:561)
            	at jadx.core.dex.visitors.regions.RegionMaker.traverse(RegionMaker.java:133)
            	at jadx.core.dex.visitors.regions.RegionMaker.makeRegion(RegionMaker.java:86)
            	at jadx.core.dex.visitors.regions.RegionMaker.processExcHandler(RegionMaker.java:1043)
            	at jadx.core.dex.visitors.regions.RegionMaker.processTryCatchBlocks(RegionMaker.java:975)
            	at jadx.core.dex.visitors.regions.RegionMakerVisitor.visit(RegionMakerVisitor.java:52)
            */
        /* JADX WARNING: Removed duplicated region for block: B:217:0x03f1 A[SYNTHETIC, Splitter:B:217:0x03f1] */
        /* JADX WARNING: Removed duplicated region for block: B:220:0x03f6 A[SYNTHETIC, Splitter:B:220:0x03f6] */
        /* JADX WARNING: Removed duplicated region for block: B:223:0x03fb A[SYNTHETIC, Splitter:B:223:0x03fb] */
        public void run() {
            /*
                r28 = this;
                r0 = r28
                java.util.concurrent.atomic.AtomicBoolean r0 = r0.isCancelled
                r24 = r0
                boolean r24 = r24.get()
                if (r24 == 0) goto L_0x000d
            L_0x000c:
                return
            L_0x000d:
                r6 = 0
                r17 = 0
                r14 = 0
                java.io.File r9 = new java.io.File     // Catch:{ Exception -> 0x02f2 }
                r0 = r28
                anetwork.channel.download.DownloadManager r0 = anetwork.channel.download.DownloadManager.this     // Catch:{ Exception -> 0x02f2 }
                r24 = r0
                r0 = r28
                java.net.URL r0 = r0.url     // Catch:{ Exception -> 0x02f2 }
                r25 = r0
                java.lang.String r25 = r25.toString()     // Catch:{ Exception -> 0x02f2 }
                java.lang.String r24 = r24.getTempFile(r25)     // Catch:{ Exception -> 0x02f2 }
                r0 = r24
                r9.<init>(r0)     // Catch:{ Exception -> 0x02f2 }
                boolean r15 = r9.exists()     // Catch:{ Exception -> 0x02f2 }
                anetwork.channel.entity.RequestImpl r19 = new anetwork.channel.entity.RequestImpl     // Catch:{ Exception -> 0x02f2 }
                r0 = r28
                java.net.URL r0 = r0.url     // Catch:{ Exception -> 0x02f2 }
                r24 = r0
                r0 = r19
                r1 = r24
                r0.<init>((java.net.URL) r1)     // Catch:{ Exception -> 0x02f2 }
                r24 = 0
                r0 = r19
                r1 = r24
                r0.setRetryTime(r1)     // Catch:{ Exception -> 0x02f2 }
                r24 = 1
                r0 = r19
                r1 = r24
                r0.setFollowRedirects(r1)     // Catch:{ Exception -> 0x02f2 }
                if (r15 == 0) goto L_0x007e
                java.lang.String r24 = "Range"
                java.lang.StringBuilder r25 = new java.lang.StringBuilder     // Catch:{ Exception -> 0x02f2 }
                r25.<init>()     // Catch:{ Exception -> 0x02f2 }
                java.lang.String r26 = "bytes="
                java.lang.StringBuilder r25 = r25.append(r26)     // Catch:{ Exception -> 0x02f2 }
                long r26 = r9.length()     // Catch:{ Exception -> 0x02f2 }
                java.lang.StringBuilder r25 = r25.append(r26)     // Catch:{ Exception -> 0x02f2 }
                java.lang.String r26 = "-"
                java.lang.StringBuilder r25 = r25.append(r26)     // Catch:{ Exception -> 0x02f2 }
                java.lang.String r25 = r25.toString()     // Catch:{ Exception -> 0x02f2 }
                r0 = r19
                r1 = r24
                r2 = r25
                r0.addHeader(r1, r2)     // Catch:{ Exception -> 0x02f2 }
            L_0x007e:
                anetwork.channel.degrade.DegradableNetwork r16 = new anetwork.channel.degrade.DegradableNetwork     // Catch:{ Exception -> 0x02f2 }
                r0 = r28
                anetwork.channel.download.DownloadManager r0 = anetwork.channel.download.DownloadManager.this     // Catch:{ Exception -> 0x02f2 }
                r24 = r0
                android.content.Context r24 = r24.context     // Catch:{ Exception -> 0x02f2 }
                r0 = r16
                r1 = r24
                r0.<init>(r1)     // Catch:{ Exception -> 0x02f2 }
                r24 = 0
                r0 = r16
                r1 = r19
                r2 = r24
                anetwork.channel.aidl.Connection r24 = r0.getConnection(r1, r2)     // Catch:{ Exception -> 0x02f2 }
                r0 = r24
                r1 = r28
                r1.conn = r0     // Catch:{ Exception -> 0x02f2 }
                r0 = r28
                anetwork.channel.aidl.Connection r0 = r0.conn     // Catch:{ Exception -> 0x02f2 }
                r24 = r0
                int r20 = r24.getStatusCode()     // Catch:{ Exception -> 0x02f2 }
                if (r20 <= 0) goto L_0x00c7
                r24 = 200(0xc8, float:2.8E-43)
                r0 = r20
                r1 = r24
                if (r0 == r1) goto L_0x0121
                r24 = 206(0xce, float:2.89E-43)
                r0 = r20
                r1 = r24
                if (r0 == r1) goto L_0x0121
                r24 = 416(0x1a0, float:5.83E-43)
                r0 = r20
                r1 = r24
                if (r0 == r1) goto L_0x0121
            L_0x00c7:
                r24 = -102(0xffffffffffffff9a, float:NaN)
                java.lang.StringBuilder r25 = new java.lang.StringBuilder     // Catch:{ Exception -> 0x02f2 }
                r25.<init>()     // Catch:{ Exception -> 0x02f2 }
                java.lang.String r26 = "ResponseCode:"
                java.lang.StringBuilder r25 = r25.append(r26)     // Catch:{ Exception -> 0x02f2 }
                r0 = r25
                r1 = r20
                java.lang.StringBuilder r25 = r0.append(r1)     // Catch:{ Exception -> 0x02f2 }
                java.lang.String r25 = r25.toString()     // Catch:{ Exception -> 0x02f2 }
                r0 = r28
                r1 = r24
                r2 = r25
                r0.notifyFail(r1, r2)     // Catch:{ Exception -> 0x02f2 }
                if (r6 == 0) goto L_0x00ef
                r6.close()     // Catch:{ Exception -> 0x0421 }
            L_0x00ef:
                if (r17 == 0) goto L_0x00f4
                r17.close()     // Catch:{ Exception -> 0x0424 }
            L_0x00f4:
                if (r14 == 0) goto L_0x00f9
                r14.close()     // Catch:{ Exception -> 0x0427 }
            L_0x00f9:
                r0 = r28
                anetwork.channel.download.DownloadManager r0 = anetwork.channel.download.DownloadManager.this
                r24 = r0
                android.util.SparseArray r25 = r24.taskMap
                monitor-enter(r25)
                r0 = r28
                anetwork.channel.download.DownloadManager r0 = anetwork.channel.download.DownloadManager.this     // Catch:{ all -> 0x011e }
                r24 = r0
                android.util.SparseArray r24 = r24.taskMap     // Catch:{ all -> 0x011e }
                r0 = r28
                int r0 = r0.taskId     // Catch:{ all -> 0x011e }
                r26 = r0
                r0 = r24
                r1 = r26
                r0.remove(r1)     // Catch:{ all -> 0x011e }
                monitor-exit(r25)     // Catch:{ all -> 0x011e }
                goto L_0x000c
            L_0x011e:
                r24 = move-exception
                monitor-exit(r25)     // Catch:{ all -> 0x011e }
                throw r24
            L_0x0121:
                if (r15 == 0) goto L_0x0195
                r24 = 416(0x1a0, float:5.83E-43)
                r0 = r20
                r1 = r24
                if (r0 != r1) goto L_0x018c
                r15 = 0
                java.util.List r24 = r19.getHeaders()     // Catch:{ Exception -> 0x02f2 }
                r0 = r28
                r1 = r24
                r0.removeRangeHeader(r1)     // Catch:{ Exception -> 0x02f2 }
                r0 = r28
                java.util.concurrent.atomic.AtomicBoolean r0 = r0.isCancelled     // Catch:{ Exception -> 0x02f2 }
                r24 = r0
                boolean r24 = r24.get()     // Catch:{ Exception -> 0x02f2 }
                if (r24 == 0) goto L_0x017a
                if (r6 == 0) goto L_0x0148
                r6.close()     // Catch:{ Exception -> 0x042a }
            L_0x0148:
                if (r17 == 0) goto L_0x014d
                r17.close()     // Catch:{ Exception -> 0x042d }
            L_0x014d:
                if (r14 == 0) goto L_0x0152
                r14.close()     // Catch:{ Exception -> 0x0430 }
            L_0x0152:
                r0 = r28
                anetwork.channel.download.DownloadManager r0 = anetwork.channel.download.DownloadManager.this
                r24 = r0
                android.util.SparseArray r25 = r24.taskMap
                monitor-enter(r25)
                r0 = r28
                anetwork.channel.download.DownloadManager r0 = anetwork.channel.download.DownloadManager.this     // Catch:{ all -> 0x0177 }
                r24 = r0
                android.util.SparseArray r24 = r24.taskMap     // Catch:{ all -> 0x0177 }
                r0 = r28
                int r0 = r0.taskId     // Catch:{ all -> 0x0177 }
                r26 = r0
                r0 = r24
                r1 = r26
                r0.remove(r1)     // Catch:{ all -> 0x0177 }
                monitor-exit(r25)     // Catch:{ all -> 0x0177 }
                goto L_0x000c
            L_0x0177:
                r24 = move-exception
                monitor-exit(r25)     // Catch:{ all -> 0x0177 }
                throw r24
            L_0x017a:
                r24 = 0
                r0 = r16
                r1 = r19
                r2 = r24
                anetwork.channel.aidl.Connection r24 = r0.getConnection(r1, r2)     // Catch:{ Exception -> 0x02f2 }
                r0 = r24
                r1 = r28
                r1.conn = r0     // Catch:{ Exception -> 0x02f2 }
            L_0x018c:
                r24 = 200(0xc8, float:2.8E-43)
                r0 = r20
                r1 = r24
                if (r0 != r1) goto L_0x0195
                r15 = 0
            L_0x0195:
                r0 = r28
                java.util.concurrent.atomic.AtomicBoolean r0 = r0.isCancelled     // Catch:{ Exception -> 0x02f2 }
                r24 = r0
                boolean r24 = r24.get()     // Catch:{ Exception -> 0x02f2 }
                if (r24 == 0) goto L_0x01d8
                if (r6 == 0) goto L_0x01a6
                r6.close()     // Catch:{ Exception -> 0x0433 }
            L_0x01a6:
                if (r17 == 0) goto L_0x01ab
                r17.close()     // Catch:{ Exception -> 0x0436 }
            L_0x01ab:
                if (r14 == 0) goto L_0x01b0
                r14.close()     // Catch:{ Exception -> 0x0439 }
            L_0x01b0:
                r0 = r28
                anetwork.channel.download.DownloadManager r0 = anetwork.channel.download.DownloadManager.this
                r24 = r0
                android.util.SparseArray r25 = r24.taskMap
                monitor-enter(r25)
                r0 = r28
                anetwork.channel.download.DownloadManager r0 = anetwork.channel.download.DownloadManager.this     // Catch:{ all -> 0x01d5 }
                r24 = r0
                android.util.SparseArray r24 = r24.taskMap     // Catch:{ all -> 0x01d5 }
                r0 = r28
                int r0 = r0.taskId     // Catch:{ all -> 0x01d5 }
                r26 = r0
                r0 = r24
                r1 = r26
                r0.remove(r1)     // Catch:{ all -> 0x01d5 }
                monitor-exit(r25)     // Catch:{ all -> 0x01d5 }
                goto L_0x000c
            L_0x01d5:
                r24 = move-exception
                monitor-exit(r25)     // Catch:{ all -> 0x01d5 }
                throw r24
            L_0x01d8:
                r12 = 0
                if (r15 != 0) goto L_0x0250
                java.io.BufferedOutputStream r7 = new java.io.BufferedOutputStream     // Catch:{ Exception -> 0x02f2 }
                java.io.FileOutputStream r24 = new java.io.FileOutputStream     // Catch:{ Exception -> 0x02f2 }
                r0 = r24
                r0.<init>(r9)     // Catch:{ Exception -> 0x02f2 }
                r0 = r24
                r7.<init>(r0)     // Catch:{ Exception -> 0x02f2 }
                r6 = r7
            L_0x01eb:
                r0 = r28
                anetwork.channel.aidl.Connection r0 = r0.conn     // Catch:{ Exception -> 0x02f2 }
                r24 = r0
                java.util.Map r24 = r24.getConnHeadFields()     // Catch:{ Exception -> 0x02f2 }
                r0 = r28
                r1 = r20
                r2 = r24
                long r22 = r0.parseContentLength(r1, r2, r12)     // Catch:{ Exception -> 0x02f2 }
                r0 = r28
                anetwork.channel.aidl.Connection r0 = r0.conn     // Catch:{ Exception -> 0x02f2 }
                r24 = r0
                anetwork.channel.aidl.ParcelableInputStream r14 = r24.getInputStream()     // Catch:{ Exception -> 0x02f2 }
                if (r14 != 0) goto L_0x0279
                r24 = -103(0xffffffffffffff99, float:NaN)
                java.lang.String r25 = "input stream is null."
                r0 = r28
                r1 = r24
                r2 = r25
                r0.notifyFail(r1, r2)     // Catch:{ Exception -> 0x02f2 }
                if (r6 == 0) goto L_0x021e
                r6.close()     // Catch:{ Exception -> 0x043c }
            L_0x021e:
                if (r17 == 0) goto L_0x0223
                r17.close()     // Catch:{ Exception -> 0x043f }
            L_0x0223:
                if (r14 == 0) goto L_0x0228
                r14.close()     // Catch:{ Exception -> 0x0442 }
            L_0x0228:
                r0 = r28
                anetwork.channel.download.DownloadManager r0 = anetwork.channel.download.DownloadManager.this
                r24 = r0
                android.util.SparseArray r25 = r24.taskMap
                monitor-enter(r25)
                r0 = r28
                anetwork.channel.download.DownloadManager r0 = anetwork.channel.download.DownloadManager.this     // Catch:{ all -> 0x024d }
                r24 = r0
                android.util.SparseArray r24 = r24.taskMap     // Catch:{ all -> 0x024d }
                r0 = r28
                int r0 = r0.taskId     // Catch:{ all -> 0x024d }
                r26 = r0
                r0 = r24
                r1 = r26
                r0.remove(r1)     // Catch:{ all -> 0x024d }
                monitor-exit(r25)     // Catch:{ all -> 0x024d }
                goto L_0x000c
            L_0x024d:
                r24 = move-exception
                monitor-exit(r25)     // Catch:{ all -> 0x024d }
                throw r24
            L_0x0250:
                java.io.RandomAccessFile r18 = new java.io.RandomAccessFile     // Catch:{ Exception -> 0x02f2 }
                java.lang.String r24 = "rw"
                r0 = r18
                r1 = r24
                r0.<init>(r9, r1)     // Catch:{ Exception -> 0x02f2 }
                long r12 = r18.length()     // Catch:{ Exception -> 0x0474, all -> 0x046f }
                r0 = r18
                r0.seek(r12)     // Catch:{ Exception -> 0x0474, all -> 0x046f }
                java.io.BufferedOutputStream r7 = new java.io.BufferedOutputStream     // Catch:{ Exception -> 0x0474, all -> 0x046f }
                java.nio.channels.FileChannel r24 = r18.getChannel()     // Catch:{ Exception -> 0x0474, all -> 0x046f }
                java.io.OutputStream r24 = java.nio.channels.Channels.newOutputStream(r24)     // Catch:{ Exception -> 0x0474, all -> 0x046f }
                r0 = r24
                r7.<init>(r0)     // Catch:{ Exception -> 0x0474, all -> 0x046f }
                r17 = r18
                r6 = r7
                goto L_0x01eb
            L_0x0279:
                r10 = -1
                r21 = 0
                r24 = 2048(0x800, float:2.87E-42)
                r0 = r24
                byte[] r8 = new byte[r0]     // Catch:{ Exception -> 0x02f2 }
            L_0x0282:
                int r10 = r14.read(r8)     // Catch:{ Exception -> 0x02f2 }
                r24 = -1
                r0 = r24
                if (r10 == r0) goto L_0x0354
                r0 = r28
                java.util.concurrent.atomic.AtomicBoolean r0 = r0.isCancelled     // Catch:{ Exception -> 0x02f2 }
                r24 = r0
                boolean r24 = r24.get()     // Catch:{ Exception -> 0x02f2 }
                if (r24 == 0) goto L_0x02d8
                r0 = r28
                anetwork.channel.aidl.Connection r0 = r0.conn     // Catch:{ Exception -> 0x02f2 }
                r24 = r0
                r24.cancel()     // Catch:{ Exception -> 0x02f2 }
                if (r6 == 0) goto L_0x02a6
                r6.close()     // Catch:{ Exception -> 0x0445 }
            L_0x02a6:
                if (r17 == 0) goto L_0x02ab
                r17.close()     // Catch:{ Exception -> 0x0448 }
            L_0x02ab:
                if (r14 == 0) goto L_0x02b0
                r14.close()     // Catch:{ Exception -> 0x044b }
            L_0x02b0:
                r0 = r28
                anetwork.channel.download.DownloadManager r0 = anetwork.channel.download.DownloadManager.this
                r24 = r0
                android.util.SparseArray r25 = r24.taskMap
                monitor-enter(r25)
                r0 = r28
                anetwork.channel.download.DownloadManager r0 = anetwork.channel.download.DownloadManager.this     // Catch:{ all -> 0x02d5 }
                r24 = r0
                android.util.SparseArray r24 = r24.taskMap     // Catch:{ all -> 0x02d5 }
                r0 = r28
                int r0 = r0.taskId     // Catch:{ all -> 0x02d5 }
                r26 = r0
                r0 = r24
                r1 = r26
                r0.remove(r1)     // Catch:{ all -> 0x02d5 }
                monitor-exit(r25)     // Catch:{ all -> 0x02d5 }
                goto L_0x000c
            L_0x02d5:
                r24 = move-exception
                monitor-exit(r25)     // Catch:{ all -> 0x02d5 }
                throw r24
            L_0x02d8:
                int r21 = r21 + r10
                r24 = 0
                r0 = r24
                r6.write(r8, r0, r10)     // Catch:{ Exception -> 0x02f2 }
                r0 = r21
                long r0 = (long) r0     // Catch:{ Exception -> 0x02f2 }
                r24 = r0
                long r24 = r24 + r12
                r0 = r28
                r1 = r24
                r3 = r22
                r0.notifyProgress(r1, r3)     // Catch:{ Exception -> 0x02f2 }
                goto L_0x0282
            L_0x02f2:
                r11 = move-exception
            L_0x02f3:
                java.lang.String r24 = "anet.DownloadManager"
                java.lang.String r25 = "file download failed!"
                r26 = 0
                r27 = 0
                r0 = r27
                java.lang.Object[] r0 = new java.lang.Object[r0]     // Catch:{ all -> 0x03ee }
                r27 = r0
                r0 = r24
                r1 = r25
                r2 = r26
                r3 = r27
                anet.channel.util.ALog.e(r0, r1, r2, r11, r3)     // Catch:{ all -> 0x03ee }
                r24 = -104(0xffffffffffffff98, float:NaN)
                java.lang.String r25 = r11.toString()     // Catch:{ all -> 0x03ee }
                r0 = r28
                r1 = r24
                r2 = r25
                r0.notifyFail(r1, r2)     // Catch:{ all -> 0x03ee }
                if (r6 == 0) goto L_0x0322
                r6.close()     // Catch:{ Exception -> 0x0460 }
            L_0x0322:
                if (r17 == 0) goto L_0x0327
                r17.close()     // Catch:{ Exception -> 0x0463 }
            L_0x0327:
                if (r14 == 0) goto L_0x032c
                r14.close()     // Catch:{ Exception -> 0x0466 }
            L_0x032c:
                r0 = r28
                anetwork.channel.download.DownloadManager r0 = anetwork.channel.download.DownloadManager.this
                r24 = r0
                android.util.SparseArray r25 = r24.taskMap
                monitor-enter(r25)
                r0 = r28
                anetwork.channel.download.DownloadManager r0 = anetwork.channel.download.DownloadManager.this     // Catch:{ all -> 0x0351 }
                r24 = r0
                android.util.SparseArray r24 = r24.taskMap     // Catch:{ all -> 0x0351 }
                r0 = r28
                int r0 = r0.taskId     // Catch:{ all -> 0x0351 }
                r26 = r0
                r0 = r24
                r1 = r26
                r0.remove(r1)     // Catch:{ all -> 0x0351 }
                monitor-exit(r25)     // Catch:{ all -> 0x0351 }
                goto L_0x000c
            L_0x0351:
                r24 = move-exception
                monitor-exit(r25)     // Catch:{ all -> 0x0351 }
                throw r24
            L_0x0354:
                r6.flush()     // Catch:{ Exception -> 0x02f2 }
                r0 = r28
                java.util.concurrent.atomic.AtomicBoolean r0 = r0.isCancelled     // Catch:{ Exception -> 0x02f2 }
                r24 = r0
                boolean r24 = r24.get()     // Catch:{ Exception -> 0x02f2 }
                if (r24 == 0) goto L_0x039a
                if (r6 == 0) goto L_0x0368
                r6.close()     // Catch:{ Exception -> 0x044e }
            L_0x0368:
                if (r17 == 0) goto L_0x036d
                r17.close()     // Catch:{ Exception -> 0x0451 }
            L_0x036d:
                if (r14 == 0) goto L_0x0372
                r14.close()     // Catch:{ Exception -> 0x0454 }
            L_0x0372:
                r0 = r28
                anetwork.channel.download.DownloadManager r0 = anetwork.channel.download.DownloadManager.this
                r24 = r0
                android.util.SparseArray r25 = r24.taskMap
                monitor-enter(r25)
                r0 = r28
                anetwork.channel.download.DownloadManager r0 = anetwork.channel.download.DownloadManager.this     // Catch:{ all -> 0x0397 }
                r24 = r0
                android.util.SparseArray r24 = r24.taskMap     // Catch:{ all -> 0x0397 }
                r0 = r28
                int r0 = r0.taskId     // Catch:{ all -> 0x0397 }
                r26 = r0
                r0 = r24
                r1 = r26
                r0.remove(r1)     // Catch:{ all -> 0x0397 }
                monitor-exit(r25)     // Catch:{ all -> 0x0397 }
                goto L_0x000c
            L_0x0397:
                r24 = move-exception
                monitor-exit(r25)     // Catch:{ all -> 0x0397 }
                throw r24
            L_0x039a:
                java.io.File r24 = new java.io.File     // Catch:{ Exception -> 0x02f2 }
                r0 = r28
                java.lang.String r0 = r0.filePath     // Catch:{ Exception -> 0x02f2 }
                r25 = r0
                r24.<init>(r25)     // Catch:{ Exception -> 0x02f2 }
                r0 = r24
                r9.renameTo(r0)     // Catch:{ Exception -> 0x02f2 }
                r0 = r28
                java.lang.String r0 = r0.filePath     // Catch:{ Exception -> 0x02f2 }
                r24 = r0
                r0 = r28
                r1 = r24
                r0.notifySuccess(r1)     // Catch:{ Exception -> 0x02f2 }
                if (r6 == 0) goto L_0x03bc
                r6.close()     // Catch:{ Exception -> 0x0457 }
            L_0x03bc:
                if (r17 == 0) goto L_0x03c1
                r17.close()     // Catch:{ Exception -> 0x045a }
            L_0x03c1:
                if (r14 == 0) goto L_0x03c6
                r14.close()     // Catch:{ Exception -> 0x045d }
            L_0x03c6:
                r0 = r28
                anetwork.channel.download.DownloadManager r0 = anetwork.channel.download.DownloadManager.this
                r24 = r0
                android.util.SparseArray r25 = r24.taskMap
                monitor-enter(r25)
                r0 = r28
                anetwork.channel.download.DownloadManager r0 = anetwork.channel.download.DownloadManager.this     // Catch:{ all -> 0x03eb }
                r24 = r0
                android.util.SparseArray r24 = r24.taskMap     // Catch:{ all -> 0x03eb }
                r0 = r28
                int r0 = r0.taskId     // Catch:{ all -> 0x03eb }
                r26 = r0
                r0 = r24
                r1 = r26
                r0.remove(r1)     // Catch:{ all -> 0x03eb }
                monitor-exit(r25)     // Catch:{ all -> 0x03eb }
                goto L_0x000c
            L_0x03eb:
                r24 = move-exception
                monitor-exit(r25)     // Catch:{ all -> 0x03eb }
                throw r24
            L_0x03ee:
                r24 = move-exception
            L_0x03ef:
                if (r6 == 0) goto L_0x03f4
                r6.close()     // Catch:{ Exception -> 0x0469 }
            L_0x03f4:
                if (r17 == 0) goto L_0x03f9
                r17.close()     // Catch:{ Exception -> 0x046b }
            L_0x03f9:
                if (r14 == 0) goto L_0x03fe
                r14.close()     // Catch:{ Exception -> 0x046d }
            L_0x03fe:
                r0 = r28
                anetwork.channel.download.DownloadManager r0 = anetwork.channel.download.DownloadManager.this
                r25 = r0
                android.util.SparseArray r25 = r25.taskMap
                monitor-enter(r25)
                r0 = r28
                anetwork.channel.download.DownloadManager r0 = anetwork.channel.download.DownloadManager.this     // Catch:{ all -> 0x041e }
                r26 = r0
                android.util.SparseArray r26 = r26.taskMap     // Catch:{ all -> 0x041e }
                r0 = r28
                int r0 = r0.taskId     // Catch:{ all -> 0x041e }
                r27 = r0
                r26.remove(r27)     // Catch:{ all -> 0x041e }
                monitor-exit(r25)     // Catch:{ all -> 0x041e }
                throw r24
            L_0x041e:
                r24 = move-exception
                monitor-exit(r25)     // Catch:{ all -> 0x041e }
                throw r24
            L_0x0421:
                r24 = move-exception
                goto L_0x00ef
            L_0x0424:
                r24 = move-exception
                goto L_0x00f4
            L_0x0427:
                r24 = move-exception
                goto L_0x00f9
            L_0x042a:
                r24 = move-exception
                goto L_0x0148
            L_0x042d:
                r24 = move-exception
                goto L_0x014d
            L_0x0430:
                r24 = move-exception
                goto L_0x0152
            L_0x0433:
                r24 = move-exception
                goto L_0x01a6
            L_0x0436:
                r24 = move-exception
                goto L_0x01ab
            L_0x0439:
                r24 = move-exception
                goto L_0x01b0
            L_0x043c:
                r24 = move-exception
                goto L_0x021e
            L_0x043f:
                r24 = move-exception
                goto L_0x0223
            L_0x0442:
                r24 = move-exception
                goto L_0x0228
            L_0x0445:
                r24 = move-exception
                goto L_0x02a6
            L_0x0448:
                r24 = move-exception
                goto L_0x02ab
            L_0x044b:
                r24 = move-exception
                goto L_0x02b0
            L_0x044e:
                r24 = move-exception
                goto L_0x0368
            L_0x0451:
                r24 = move-exception
                goto L_0x036d
            L_0x0454:
                r24 = move-exception
                goto L_0x0372
            L_0x0457:
                r24 = move-exception
                goto L_0x03bc
            L_0x045a:
                r24 = move-exception
                goto L_0x03c1
            L_0x045d:
                r24 = move-exception
                goto L_0x03c6
            L_0x0460:
                r24 = move-exception
                goto L_0x0322
            L_0x0463:
                r24 = move-exception
                goto L_0x0327
            L_0x0466:
                r24 = move-exception
                goto L_0x032c
            L_0x0469:
                r25 = move-exception
                goto L_0x03f4
            L_0x046b:
                r25 = move-exception
                goto L_0x03f9
            L_0x046d:
                r25 = move-exception
                goto L_0x03fe
            L_0x046f:
                r24 = move-exception
                r17 = r18
                goto L_0x03ef
            L_0x0474:
                r11 = move-exception
                r17 = r18
                goto L_0x02f3
            */
            throw new UnsupportedOperationException("Method not decompiled: anetwork.channel.download.DownloadManager.DownloadTask.run():void");
        }

        private void notifySuccess(String filePath2) {
            if (this.isFinish.compareAndSet(false, true)) {
                Iterator i$ = this.listenerList.iterator();
                while (i$.hasNext()) {
                    i$.next().onSuccess(this.taskId, filePath2);
                }
            }
        }

        private void notifyFail(int errorCode, String errorMsg) {
            if (this.isFinish.compareAndSet(false, true)) {
                Iterator i$ = this.listenerList.iterator();
                while (i$.hasNext()) {
                    i$.next().onFail(this.taskId, errorCode, errorMsg);
                }
            }
        }

        private void notifyProgress(long pos, long total) {
            if (!this.isFinish.get()) {
                Iterator i$ = this.listenerList.iterator();
                while (i$.hasNext()) {
                    i$.next().onProgress(this.taskId, pos, total);
                }
            }
        }

        private long parseContentLength(int statusCode, Map<String, List<String>> header, long rangeStart) {
            int idx;
            long contentLength = 0;
            if (statusCode == 200) {
                try {
                    return Long.parseLong(HttpHelper.getSingleHeaderFieldByKey(header, "Content-Length"));
                } catch (Exception e) {
                    return 0;
                }
            } else if (statusCode != 206) {
                return 0;
            } else {
                String contentRange = HttpHelper.getSingleHeaderFieldByKey(header, "Content-Range");
                if (!(contentRange == null || (idx = contentRange.lastIndexOf(47)) == -1)) {
                    contentLength = Long.parseLong(contentRange.substring(idx + 1));
                }
                if (contentLength == 0) {
                    return Long.parseLong(HttpHelper.getSingleHeaderFieldByKey(header, "Content-Length")) + rangeStart;
                }
                return contentLength;
            }
        }

        private void removeRangeHeader(List<Header> headers) {
            if (headers != null) {
                ListIterator<Header> iterator = headers.listIterator();
                while (iterator.hasNext()) {
                    if (HttpHeaders.RANGE.equalsIgnoreCase(iterator.next().getName())) {
                        iterator.remove();
                        return;
                    }
                }
            }
        }

        private String parseFileNameForURL(URL url2) {
            String path = url2.getPath();
            int index = path.lastIndexOf(47);
            String fileName = null;
            if (index != -1) {
                fileName = path.substring(index + 1, path.length());
            }
            if (!TextUtils.isEmpty(fileName)) {
                return fileName;
            }
            String fileName2 = StringUtils.md5ToHex(url2.toString());
            if (fileName2 == null) {
                return url2.getFile();
            }
            return fileName2;
        }
    }

    private void prepareDownloadFolder() {
        if (this.context != null) {
            File file = new File(this.context.getExternalFilesDir((String) null), DOWNLOAD_FOLDER);
            if (!file.exists()) {
                file.mkdir();
            }
        }
    }

    private boolean prepareFolder(String fileFolder) {
        if (this.context != null) {
            try {
                File file = new File(fileFolder);
                if (!file.exists()) {
                    return file.mkdir();
                }
                return true;
            } catch (Exception e) {
                ALog.e(TAG, "create folder failed", (String) null, "folder", fileFolder);
            }
        }
        return false;
    }

    /* access modifiers changed from: private */
    public String getDownloadFilePath(String fileName) {
        StringBuilder sb = new StringBuilder(32);
        sb.append(this.context.getExternalFilesDir((String) null)).append(WVNativeCallbackUtil.SEPERATER).append(DOWNLOAD_FOLDER).append(WVNativeCallbackUtil.SEPERATER).append(fileName);
        return sb.toString();
    }

    /* access modifiers changed from: private */
    public String getTempFile(String url) {
        String tempFile = StringUtils.md5ToHex(url);
        if (tempFile == null) {
            tempFile = url;
        }
        return this.context.getExternalCacheDir() + WVNativeCallbackUtil.SEPERATER + tempFile;
    }
}
