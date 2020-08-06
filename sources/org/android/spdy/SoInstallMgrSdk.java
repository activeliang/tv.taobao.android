package org.android.spdy;

import android.content.Context;
import android.os.Build;
import com.taobao.atlas.dexmerge.MergeConstants;
import java.io.File;
import java.io.IOException;
import java.lang.reflect.Method;
import java.util.zip.ZipException;

public class SoInstallMgrSdk {
    private static final String ARMEABI = "armeabi";
    private static final int EventID_SO_INIT = 21033;
    static final String LOGTAG = "INIT_SO";
    private static final String MIPS = "mips";
    private static final String X86 = "x86";
    static Context mContext = null;

    public static void init(Context c) {
        mContext = c;
    }

    public static boolean initSo(String libName, int version) {
        return initSo(libName, version, (ClassLoader) null);
    }

    public static boolean initSo(String libName, int version, ClassLoader classLoader) {
        boolean InitSuc;
        if (classLoader == null) {
            try {
                System.loadLibrary(libName);
            } catch (Exception e) {
                InitSuc = false;
                e.printStackTrace();
            } catch (UnsatisfiedLinkError e2) {
                InitSuc = false;
                e2.printStackTrace();
            } catch (Error e3) {
                InitSuc = false;
                e3.printStackTrace();
            }
        } else {
            Runtime runtime = Runtime.getRuntime();
            Method method = Runtime.class.getDeclaredMethod("loadLibrary", new Class[]{String.class, ClassLoader.class});
            method.setAccessible(true);
            method.invoke(runtime, new Object[]{libName, classLoader});
        }
        InitSuc = true;
        if (!InitSuc) {
            try {
                if (isExist(libName, version)) {
                    boolean res = _loadUnzipSo(libName, version, classLoader);
                    if (res) {
                        return res;
                    }
                    removeSoIfExit(libName, version);
                }
                String cpuType = _cpuType();
                if (!cpuType.equalsIgnoreCase(MIPS) && !cpuType.equalsIgnoreCase(X86)) {
                    try {
                        InitSuc = unZipSelectedFiles(libName, version, classLoader);
                    } catch (ZipException e4) {
                        e4.printStackTrace();
                    } catch (IOException e22) {
                        e22.printStackTrace();
                    }
                }
            } catch (Exception e5) {
                InitSuc = false;
                e5.printStackTrace();
            } catch (UnsatisfiedLinkError e23) {
                InitSuc = false;
                e23.printStackTrace();
            } catch (Error e32) {
                InitSuc = false;
                e32.printStackTrace();
            }
        }
        if (!InitSuc) {
        }
        return InitSuc;
    }

    private static String _getFieldReflectively(Build build, String fieldName) {
        try {
            return Build.class.getField(fieldName).get(build).toString();
        } catch (Exception e) {
            return "Unknown";
        }
    }

    private static String _cpuType() {
        String abi = _getFieldReflectively(new Build(), "CPU_ABI");
        if (abi == null || abi.length() == 0 || abi.equals("Unknown")) {
            abi = ARMEABI;
        }
        return abi.toLowerCase();
    }

    static String _targetSoFile(String libname, int version) {
        Context context = mContext;
        if (context == null) {
            return "";
        }
        String path = "/data/data/" + context.getPackageName() + "/files";
        File f = context.getFilesDir();
        if (f != null) {
            path = f.getPath();
        }
        return path + "/lib" + libname + "bk" + version + MergeConstants.SO_SUFFIX;
    }

    static void removeSoIfExit(String libname, int version) {
        File a = new File(_targetSoFile(libname, version));
        if (a.exists()) {
            a.delete();
        }
    }

    static boolean isExist(String libname, int version) {
        return new File(_targetSoFile(libname, version)).exists();
    }

    static boolean _loadUnzipSo(String libname, int version, ClassLoader classLoader) {
        try {
            if (isExist(libname, version)) {
                if (classLoader == null) {
                    System.load(_targetSoFile(libname, version));
                } else {
                    Runtime runtime = Runtime.getRuntime();
                    Method method = Runtime.class.getDeclaredMethod("load", new Class[]{String.class, ClassLoader.class});
                    method.setAccessible(true);
                    method.invoke(runtime, new Object[]{_targetSoFile(libname, version), classLoader});
                }
            }
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        } catch (UnsatisfiedLinkError e2) {
            e2.printStackTrace();
            return false;
        } catch (Error e3) {
            e3.printStackTrace();
            return false;
        }
    }

    /* JADX WARNING: No exception handlers in catch block: Catch:{  } */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    static boolean unZipSelectedFiles(java.lang.String r17, int r18, java.lang.ClassLoader r19) throws java.util.zip.ZipException, java.io.IOException {
        /*
            java.lang.StringBuilder r15 = new java.lang.StringBuilder
            r15.<init>()
            java.lang.String r16 = "lib/armeabi/lib"
            java.lang.StringBuilder r15 = r15.append(r16)
            r0 = r17
            java.lang.StringBuilder r15 = r15.append(r0)
            java.lang.String r16 = ".so"
            java.lang.StringBuilder r15 = r15.append(r16)
            java.lang.String r11 = r15.toString()
            java.lang.String r14 = ""
            android.content.Context r4 = mContext     // Catch:{ IOException -> 0x00c4 }
            if (r4 != 0) goto L_0x0026
            r15 = 0
        L_0x0025:
            return r15
        L_0x0026:
            android.content.pm.ApplicationInfo r1 = r4.getApplicationInfo()     // Catch:{ IOException -> 0x00c4 }
            if (r1 == 0) goto L_0x002e
            java.lang.String r14 = r1.sourceDir     // Catch:{ IOException -> 0x00c4 }
        L_0x002e:
            java.util.zip.ZipFile r13 = new java.util.zip.ZipFile     // Catch:{ IOException -> 0x00c4 }
            r13.<init>(r14)     // Catch:{ IOException -> 0x00c4 }
            java.util.Enumeration r6 = r13.entries()     // Catch:{ IOException -> 0x00c4 }
        L_0x0037:
            boolean r15 = r6.hasMoreElements()     // Catch:{ IOException -> 0x00c4 }
            if (r15 == 0) goto L_0x00c8
            java.lang.Object r7 = r6.nextElement()     // Catch:{ IOException -> 0x00c4 }
            java.util.zip.ZipEntry r7 = (java.util.zip.ZipEntry) r7     // Catch:{ IOException -> 0x00c4 }
            java.lang.String r15 = r7.getName()     // Catch:{ IOException -> 0x00c4 }
            boolean r15 = r15.startsWith(r11)     // Catch:{ IOException -> 0x00c4 }
            if (r15 == 0) goto L_0x0037
            r8 = 0
            r9 = 0
            r3 = 0
            r12 = 0
            removeSoIfExit(r17, r18)     // Catch:{ all -> 0x00d5 }
            java.io.InputStream r8 = r13.getInputStream(r7)     // Catch:{ all -> 0x00d5 }
            java.lang.StringBuilder r15 = new java.lang.StringBuilder     // Catch:{ all -> 0x00d5 }
            r15.<init>()     // Catch:{ all -> 0x00d5 }
            java.lang.String r16 = "lib"
            java.lang.StringBuilder r15 = r15.append(r16)     // Catch:{ all -> 0x00d5 }
            r0 = r17
            java.lang.StringBuilder r15 = r15.append(r0)     // Catch:{ all -> 0x00d5 }
            java.lang.String r16 = "bk"
            java.lang.StringBuilder r15 = r15.append(r16)     // Catch:{ all -> 0x00d5 }
            r0 = r18
            java.lang.StringBuilder r15 = r15.append(r0)     // Catch:{ all -> 0x00d5 }
            java.lang.String r16 = ".so"
            java.lang.StringBuilder r15 = r15.append(r16)     // Catch:{ all -> 0x00d5 }
            java.lang.String r15 = r15.toString()     // Catch:{ all -> 0x00d5 }
            r16 = 0
            r0 = r16
            java.io.FileOutputStream r9 = r4.openFileOutput(r15, r0)     // Catch:{ all -> 0x00d5 }
            java.nio.channels.FileChannel r3 = r9.getChannel()     // Catch:{ all -> 0x00d5 }
            r15 = 1024(0x400, float:1.435E-42)
            byte[] r2 = new byte[r15]     // Catch:{ all -> 0x00d5 }
        L_0x0092:
            int r10 = r8.read(r2)     // Catch:{ all -> 0x00d5 }
            if (r10 <= 0) goto L_0x00a2
            r15 = 0
            java.nio.ByteBuffer r15 = java.nio.ByteBuffer.wrap(r2, r15, r10)     // Catch:{ all -> 0x00d5 }
            r3.write(r15)     // Catch:{ all -> 0x00d5 }
            int r12 = r12 + r10
            goto L_0x0092
        L_0x00a2:
            if (r8 == 0) goto L_0x00a7
            r8.close()     // Catch:{ Exception -> 0x00bf }
        L_0x00a7:
            if (r3 == 0) goto L_0x00ac
            r3.close()     // Catch:{ Exception -> 0x00cb }
        L_0x00ac:
            if (r9 == 0) goto L_0x00b1
            r9.close()     // Catch:{ Exception -> 0x00d0 }
        L_0x00b1:
            if (r13 == 0) goto L_0x00b7
            r13.close()     // Catch:{ IOException -> 0x00c4 }
            r13 = 0
        L_0x00b7:
            if (r12 <= 0) goto L_0x00fb
            boolean r15 = _loadUnzipSo(r17, r18, r19)     // Catch:{ IOException -> 0x00c4 }
            goto L_0x0025
        L_0x00bf:
            r5 = move-exception
            r5.printStackTrace()     // Catch:{ IOException -> 0x00c4 }
            goto L_0x00a7
        L_0x00c4:
            r5 = move-exception
            r5.printStackTrace()
        L_0x00c8:
            r15 = 0
            goto L_0x0025
        L_0x00cb:
            r5 = move-exception
            r5.printStackTrace()     // Catch:{ IOException -> 0x00c4 }
            goto L_0x00ac
        L_0x00d0:
            r5 = move-exception
            r5.printStackTrace()     // Catch:{ IOException -> 0x00c4 }
            goto L_0x00b1
        L_0x00d5:
            r15 = move-exception
            if (r8 == 0) goto L_0x00db
            r8.close()     // Catch:{ Exception -> 0x00ec }
        L_0x00db:
            if (r3 == 0) goto L_0x00e0
            r3.close()     // Catch:{ Exception -> 0x00f1 }
        L_0x00e0:
            if (r9 == 0) goto L_0x00e5
            r9.close()     // Catch:{ Exception -> 0x00f6 }
        L_0x00e5:
            if (r13 == 0) goto L_0x00eb
            r13.close()     // Catch:{ IOException -> 0x00c4 }
            r13 = 0
        L_0x00eb:
            throw r15     // Catch:{ IOException -> 0x00c4 }
        L_0x00ec:
            r5 = move-exception
            r5.printStackTrace()     // Catch:{ IOException -> 0x00c4 }
            goto L_0x00db
        L_0x00f1:
            r5 = move-exception
            r5.printStackTrace()     // Catch:{ IOException -> 0x00c4 }
            goto L_0x00e0
        L_0x00f6:
            r5 = move-exception
            r5.printStackTrace()     // Catch:{ IOException -> 0x00c4 }
            goto L_0x00e5
        L_0x00fb:
            r15 = 0
            goto L_0x0025
        */
        throw new UnsupportedOperationException("Method not decompiled: org.android.spdy.SoInstallMgrSdk.unZipSelectedFiles(java.lang.String, int, java.lang.ClassLoader):boolean");
    }
}
