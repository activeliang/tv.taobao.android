package com.edge.pcdn;

import android.os.Build;
import android.os.Handler;
import android.os.Looper;
import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.text.MessageFormat;
import java.util.Iterator;
import java.util.Vector;

public class ReportLogManager {
    public static final String ADDRESS_LOG = "pcdn-sdk-address";
    public static final String START_LOG = "pcdn-start";
    public static final String UPGRADE_LOG = "pcdn-upgrade";
    private static volatile ReportLogManager instance = null;
    /* access modifiers changed from: private */
    public int DELAY = 60000;
    /* access modifiers changed from: private */
    public Vector<byte[]> addressData = new Vector<>();
    /* access modifiers changed from: private */
    public byte[] dataheader = null;
    /* access modifiers changed from: private */
    public Handler handler = new Handler(Looper.getMainLooper());
    Runnable runnable = new Runnable() {
        public void run() {
            ReportLogManager.this.handler.postDelayed(this, (long) ReportLogManager.this.DELAY);
            if (ReportLogManager.this.addressData.size() > 0) {
                PcdnLog.d("report pcdnaddress log :" + ReportLogManager.this.addressData.size());
                ByteArrayOutputStream boutput = new ByteArrayOutputStream();
                DataOutputStream doutput = new DataOutputStream(boutput);
                try {
                    doutput.write(ReportLogManager.this.dataheader);
                    Iterator it = ReportLogManager.this.addressData.iterator();
                    while (it.hasNext()) {
                        doutput.write((byte[]) it.next());
                    }
                    ReportLogManager.this.addressData.removeAllElements();
                    new Thread(new PcdnReportLogTask(ConfigManager.REPORT_LOG_URL, boutput.toByteArray())).start();
                    try {
                        boutput.flush();
                        boutput.close();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    try {
                        doutput.flush();
                        doutput.close();
                    } catch (Exception e2) {
                        e2.printStackTrace();
                    }
                } catch (Exception e3) {
                    PcdnLog.e("report exception :" + PcdnLog.toString(e3));
                    try {
                        boutput.flush();
                        boutput.close();
                    } catch (Exception e4) {
                        e4.printStackTrace();
                    }
                    try {
                        doutput.flush();
                        doutput.close();
                    } catch (Exception e5) {
                        e5.printStackTrace();
                    }
                } catch (Throwable th) {
                    try {
                        boutput.flush();
                        boutput.close();
                    } catch (Exception e6) {
                        e6.printStackTrace();
                    }
                    try {
                        doutput.flush();
                        doutput.close();
                    } catch (Exception e7) {
                        e7.printStackTrace();
                    }
                    throw th;
                }
            }
        }
    };

    private ReportLogManager() {
        init();
        this.handler.postDelayed(this.runnable, (long) this.DELAY);
        PcdnLog.d("start pcdnaddress timer");
    }

    public static ReportLogManager getInstance() {
        if (instance == null) {
            synchronized (ReportLogManager.class) {
                if (instance == null) {
                    instance = new ReportLogManager();
                }
            }
        }
        return instance;
    }

    private void init() {
        ByteArrayOutputStream boutput = new ByteArrayOutputStream();
        DataOutputStream doutput = new DataOutputStream(boutput);
        try {
            doutput.write(75);
            doutput.write(76);
            doutput.writeInt(4);
            doutput.write("peer".getBytes("UTF-8"));
            doutput.writeInt(1);
            byte[] bytepeer = MessageFormat.format("{0}\t{1}\t{2}\t{3}\t{4}\t{5}\t{6}", new Object[]{0, 0, 0, 0, 0, Build.VERSION.RELEASE, 0}).getBytes("UTF-8");
            doutput.writeInt(bytepeer.length);
            doutput.write(bytepeer);
            this.dataheader = boutput.toByteArray();
            try {
                boutput.flush();
                boutput.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
            try {
                doutput.flush();
                doutput.close();
            } catch (Exception e2) {
                e2.printStackTrace();
            }
        } catch (Exception e3) {
            e3.printStackTrace();
            try {
                boutput.flush();
                boutput.close();
            } catch (Exception e4) {
                e4.printStackTrace();
            }
            try {
                doutput.flush();
                doutput.close();
            } catch (Exception e5) {
                e5.printStackTrace();
            }
        } catch (Throwable th) {
            try {
                boutput.flush();
                boutput.close();
            } catch (Exception e6) {
                e6.printStackTrace();
            }
            try {
                doutput.flush();
                doutput.close();
            } catch (Exception e7) {
                e7.printStackTrace();
            }
            throw th;
        }
    }

    public void reportStartLog(String type, String clientId, String pcdnver, int startcode, String appname, String appversion, String cacheDir, String pid, String ext) {
        ByteArrayOutputStream boutput = new ByteArrayOutputStream();
        DataOutputStream doutput = new DataOutputStream(boutput);
        try {
            doutput.write(this.dataheader);
            doutput.write(75);
            doutput.write(76);
            doutput.writeInt(10);
            doutput.write(START_LOG.getBytes("UTF-8"));
            doutput.writeInt(1);
            byte[] bytebody = MessageFormat.format("{0}\t{1}\t{2}\t{3}\t{4}\t{5}\t{6}\t{7}\t{8}\t{9}\t{10}\t{11}\t{12}\t{13}\t{14}\t{15}", new Object[]{Integer.valueOf(startcode), type, pcdnver, clientId, System.getProperty("os.name"), Build.VERSION.RELEASE, appname, appversion, Build.CPU_ABI, Build.BRAND, Build.MODEL, "0", "0", cacheDir, pid, ext}).getBytes("UTF-8");
            doutput.writeInt(bytebody.length);
            doutput.write(bytebody);
            new Thread(new PcdnReportLogTask(ConfigManager.REPORT_LOG_URL, boutput.toByteArray())).start();
            try {
                boutput.flush();
                boutput.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
            try {
                doutput.flush();
                doutput.close();
            } catch (Exception e2) {
                e2.printStackTrace();
            }
        } catch (IOException e3) {
            e3.printStackTrace();
            try {
                boutput.flush();
                boutput.close();
            } catch (Exception e4) {
                e4.printStackTrace();
            }
            try {
                doutput.flush();
                doutput.close();
            } catch (Exception e5) {
                e5.printStackTrace();
            }
        } catch (Throwable th) {
            try {
                boutput.flush();
                boutput.close();
            } catch (Exception e6) {
                e6.printStackTrace();
            }
            try {
                doutput.flush();
                doutput.close();
            } catch (Exception e7) {
                e7.printStackTrace();
            }
            throw th;
        }
    }

    public void reportStopLog(String type, String clientId, String pcdnver, int stopcode, String appname, String appversion, String cacheDir, String pid, String ext) {
        ByteArrayOutputStream boutput = new ByteArrayOutputStream();
        DataOutputStream doutput = new DataOutputStream(boutput);
        try {
            doutput.write(this.dataheader);
            doutput.write(75);
            doutput.write(76);
            doutput.writeInt(10);
            doutput.write(START_LOG.getBytes("UTF-8"));
            doutput.writeInt(1);
            byte[] bytebody = MessageFormat.format("{0}\t{1}\t{2}\t{3}\t{4}\t{5}\t{6}\t{7}\t{8}\t{9}\t{10}\t{11}\t{12}\t{13}\t{14}\t{15}", new Object[]{Integer.valueOf(stopcode), type, pcdnver, clientId, System.getProperty("os.name"), Build.VERSION.RELEASE, appname, appversion, Build.CPU_ABI, Build.BRAND, Build.MODEL, "0", "0", cacheDir, pid, ext}).getBytes("UTF-8");
            doutput.writeInt(bytebody.length);
            doutput.write(bytebody);
            new Thread(new PcdnReportLogTask(ConfigManager.REPORT_LOG_URL, boutput.toByteArray())).start();
            try {
                boutput.flush();
                boutput.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
            try {
                doutput.flush();
                doutput.close();
            } catch (Exception e2) {
                e2.printStackTrace();
            }
        } catch (IOException e3) {
            e3.printStackTrace();
            try {
                boutput.flush();
                boutput.close();
            } catch (Exception e4) {
                e4.printStackTrace();
            }
            try {
                doutput.flush();
                doutput.close();
            } catch (Exception e5) {
                e5.printStackTrace();
            }
        } catch (Throwable th) {
            try {
                boutput.flush();
                boutput.close();
            } catch (Exception e6) {
                e6.printStackTrace();
            }
            try {
                doutput.flush();
                doutput.close();
            } catch (Exception e7) {
                e7.printStackTrace();
            }
            throw th;
        }
    }

    public void reportUpgradeLog(String type, String clientId, String pcdnver, int code, String appname, String appversion) {
        ByteArrayOutputStream boutput = new ByteArrayOutputStream();
        DataOutputStream doutput = new DataOutputStream(boutput);
        try {
            doutput.write(this.dataheader);
            doutput.write(75);
            doutput.write(76);
            doutput.writeInt(12);
            doutput.write(UPGRADE_LOG.getBytes("UTF-8"));
            doutput.writeInt(1);
            byte[] bytebody = MessageFormat.format("{0}\t{1}\t{2}\t{3}\t{4}\t{5}\t{6}\t{7}\t{8}\t{9}\t{10}\t{11}\t{12}", new Object[]{Integer.valueOf(code), type, pcdnver, clientId, System.getProperty("os.name"), Build.VERSION.RELEASE, appname, appversion, Build.CPU_ABI, Build.BRAND, Build.MODEL, "0", "0"}).getBytes("UTF-8");
            doutput.writeInt(bytebody.length);
            doutput.write(bytebody);
            new Thread(new PcdnReportLogTask(ConfigManager.REPORT_LOG_URL, boutput.toByteArray())).start();
            try {
                boutput.flush();
                boutput.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
            try {
                doutput.flush();
                doutput.close();
            } catch (Exception e2) {
                e2.printStackTrace();
            }
        } catch (IOException e3) {
            e3.printStackTrace();
            try {
                boutput.flush();
                boutput.close();
            } catch (Exception e4) {
                e4.printStackTrace();
            }
            try {
                doutput.flush();
                doutput.close();
            } catch (Exception e5) {
                e5.printStackTrace();
            }
        } catch (Throwable th) {
            try {
                boutput.flush();
                boutput.close();
            } catch (Exception e6) {
                e6.printStackTrace();
            }
            try {
                doutput.flush();
                doutput.close();
            } catch (Exception e7) {
                e7.printStackTrace();
            }
            throw th;
        }
    }

    /* JADX WARNING: Code restructure failed: missing block: B:42:0x00ec, code lost:
        r4 = move-exception;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:43:0x00ed, code lost:
        r4.printStackTrace();
     */
    /* JADX WARNING: Exception block dominator not found, dom blocks: [] */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public synchronized void reportAddressLog(java.lang.String r10, java.lang.String r11, java.lang.String r12, int r13, int r14, java.lang.String r15, java.lang.String r16, java.lang.String r17) {
        /*
            r9 = this;
            monitor-enter(r9)
            java.io.ByteArrayOutputStream r1 = new java.io.ByteArrayOutputStream     // Catch:{ all -> 0x00b6 }
            r1.<init>()     // Catch:{ all -> 0x00b6 }
            java.io.DataOutputStream r3 = new java.io.DataOutputStream     // Catch:{ all -> 0x00b6 }
            r3.<init>(r1)     // Catch:{ all -> 0x00b6 }
            r6 = 75
            r3.write(r6)     // Catch:{ IOException -> 0x00be }
            r6 = 76
            r3.write(r6)     // Catch:{ IOException -> 0x00be }
            java.lang.String r6 = "pcdn-sdk-address"
            int r6 = r6.length()     // Catch:{ IOException -> 0x00be }
            r3.writeInt(r6)     // Catch:{ IOException -> 0x00be }
            java.lang.String r6 = "pcdn-sdk-address"
            java.lang.String r7 = "UTF-8"
            byte[] r6 = r6.getBytes(r7)     // Catch:{ IOException -> 0x00be }
            r3.write(r6)     // Catch:{ IOException -> 0x00be }
            r6 = 1
            r3.writeInt(r6)     // Catch:{ IOException -> 0x00be }
            java.lang.String r0 = "{0}\t{1}\t{2}\t{3}\t{4}\t{5}\t{6}\t{7}\t{8}\t{9}\t{10}\t{11}\t{12}\t{13}\t{14}"
            r6 = 15
            java.lang.Object[] r6 = new java.lang.Object[r6]     // Catch:{ IOException -> 0x00be }
            r7 = 0
            java.lang.Integer r8 = java.lang.Integer.valueOf(r13)     // Catch:{ IOException -> 0x00be }
            r6[r7] = r8     // Catch:{ IOException -> 0x00be }
            r7 = 1
            java.lang.Integer r8 = java.lang.Integer.valueOf(r14)     // Catch:{ IOException -> 0x00be }
            r6[r7] = r8     // Catch:{ IOException -> 0x00be }
            r7 = 2
            r6[r7] = r10     // Catch:{ IOException -> 0x00be }
            r7 = 3
            r6[r7] = r17     // Catch:{ IOException -> 0x00be }
            r7 = 4
            r6[r7] = r12     // Catch:{ IOException -> 0x00be }
            r7 = 5
            r6[r7] = r11     // Catch:{ IOException -> 0x00be }
            r7 = 6
            java.lang.String r8 = "os.name"
            java.lang.String r8 = java.lang.System.getProperty(r8)     // Catch:{ IOException -> 0x00be }
            r6[r7] = r8     // Catch:{ IOException -> 0x00be }
            r7 = 7
            java.lang.String r8 = android.os.Build.VERSION.RELEASE     // Catch:{ IOException -> 0x00be }
            r6[r7] = r8     // Catch:{ IOException -> 0x00be }
            r7 = 8
            r6[r7] = r15     // Catch:{ IOException -> 0x00be }
            r7 = 9
            r6[r7] = r16     // Catch:{ IOException -> 0x00be }
            r7 = 10
            java.lang.String r8 = android.os.Build.CPU_ABI     // Catch:{ IOException -> 0x00be }
            r6[r7] = r8     // Catch:{ IOException -> 0x00be }
            r7 = 11
            java.lang.String r8 = android.os.Build.BRAND     // Catch:{ IOException -> 0x00be }
            r6[r7] = r8     // Catch:{ IOException -> 0x00be }
            r7 = 12
            java.lang.String r8 = android.os.Build.MODEL     // Catch:{ IOException -> 0x00be }
            r6[r7] = r8     // Catch:{ IOException -> 0x00be }
            r7 = 13
            java.lang.String r8 = "0"
            r6[r7] = r8     // Catch:{ IOException -> 0x00be }
            r7 = 14
            java.lang.String r8 = "0"
            r6[r7] = r8     // Catch:{ IOException -> 0x00be }
            java.lang.String r0 = java.text.MessageFormat.format(r0, r6)     // Catch:{ IOException -> 0x00be }
            java.lang.String r6 = "UTF-8"
            byte[] r2 = r0.getBytes(r6)     // Catch:{ IOException -> 0x00be }
            int r6 = r2.length     // Catch:{ IOException -> 0x00be }
            r3.writeInt(r6)     // Catch:{ IOException -> 0x00be }
            r3.write(r2)     // Catch:{ IOException -> 0x00be }
            byte[] r5 = r1.toByteArray()     // Catch:{ IOException -> 0x00be }
            java.util.Vector<byte[]> r6 = r9.addressData     // Catch:{ IOException -> 0x00be }
            r6.add(r5)     // Catch:{ IOException -> 0x00be }
            r1.flush()     // Catch:{ Exception -> 0x00b1 }
            r1.close()     // Catch:{ Exception -> 0x00b1 }
        L_0x00a9:
            r3.flush()     // Catch:{ Exception -> 0x00b9 }
            r3.close()     // Catch:{ Exception -> 0x00b9 }
        L_0x00af:
            monitor-exit(r9)
            return
        L_0x00b1:
            r4 = move-exception
            r4.printStackTrace()     // Catch:{ all -> 0x00b6 }
            goto L_0x00a9
        L_0x00b6:
            r6 = move-exception
            monitor-exit(r9)
            throw r6
        L_0x00b9:
            r4 = move-exception
            r4.printStackTrace()     // Catch:{ all -> 0x00b6 }
            goto L_0x00af
        L_0x00be:
            r4 = move-exception
            r4.printStackTrace()     // Catch:{ all -> 0x00d9 }
            r1.flush()     // Catch:{ Exception -> 0x00d4 }
            r1.close()     // Catch:{ Exception -> 0x00d4 }
        L_0x00c8:
            r3.flush()     // Catch:{ Exception -> 0x00cf }
            r3.close()     // Catch:{ Exception -> 0x00cf }
            goto L_0x00af
        L_0x00cf:
            r4 = move-exception
            r4.printStackTrace()     // Catch:{ all -> 0x00b6 }
            goto L_0x00af
        L_0x00d4:
            r4 = move-exception
            r4.printStackTrace()     // Catch:{ all -> 0x00b6 }
            goto L_0x00c8
        L_0x00d9:
            r6 = move-exception
            r1.flush()     // Catch:{ Exception -> 0x00e7 }
            r1.close()     // Catch:{ Exception -> 0x00e7 }
        L_0x00e0:
            r3.flush()     // Catch:{ Exception -> 0x00ec }
            r3.close()     // Catch:{ Exception -> 0x00ec }
        L_0x00e6:
            throw r6     // Catch:{ all -> 0x00b6 }
        L_0x00e7:
            r4 = move-exception
            r4.printStackTrace()     // Catch:{ all -> 0x00b6 }
            goto L_0x00e0
        L_0x00ec:
            r4 = move-exception
            r4.printStackTrace()     // Catch:{ all -> 0x00b6 }
            goto L_0x00e6
        */
        throw new UnsupportedOperationException("Method not decompiled: com.edge.pcdn.ReportLogManager.reportAddressLog(java.lang.String, java.lang.String, java.lang.String, int, int, java.lang.String, java.lang.String, java.lang.String):void");
    }
}
