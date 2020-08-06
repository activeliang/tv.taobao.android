package com.taobao.atlas.dexmerge;

import android.content.Intent;
import android.os.Build;
import android.taobao.atlas.runtime.RuntimeVariables;
import android.taobao.windvane.util.WVNativeCallbackUtil;
import android.text.TextUtils;
import android.util.Log;
import com.alibaba.patch.PatchUtils;
import com.taobao.atlas.dex.Dex;
import com.taobao.atlas.dexmerge.MergeExcutorServices;
import com.taobao.atlas.dexmerge.dx.merge.CollisionPolicy;
import com.taobao.atlas.dexmerge.dx.merge.DexMerger;
import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.Closeable;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;
import java.util.TreeMap;
import java.util.concurrent.atomic.AtomicReference;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;
import java.util.zip.ZipOutputStream;

public class MergeTool {
    private static final int BUFFEREDSIZE = 1024;
    private static TreeMap<Integer, List<String>> classes = new TreeMap<>();
    private static String noPatchDexIndex;
    private static int patchVersion = 2;

    public static void mergePrepare(File oldBundle, List<ZipEntry> entryList, String patchName, File targetBundle, boolean isDiff, MergeExcutorServices.PrepareCallBack prepareCallBack) throws IOException, MergeException {
        if (oldBundle.exists() && entryList != null) {
            ZipFile sourceZip = new ZipFile(oldBundle);
            try {
                File tempFile = File.createTempFile(patchName, (String) null, targetBundle.getParentFile());
                tempFile.deleteOnExit();
                if (patchName.equals(MergeConstants.MAIN_DEX)) {
                    createNewMainApkInternal(sourceZip, entryList, tempFile, isDiff, prepareCallBack);
                } else {
                    createNewBundleInternal(patchName, sourceZip, entryList, tempFile, isDiff, prepareCallBack);
                }
                if (!tempFile.exists() || tempFile.renameTo(targetBundle)) {
                    try {
                        sourceZip.close();
                    } catch (Throwable th) {
                    }
                } else {
                    throw new IOException("merge failed!");
                }
            } catch (IOException e) {
                targetBundle.delete();
                throw new IOException(e);
            } catch (Throwable th2) {
                try {
                    sourceZip.close();
                } catch (Throwable th3) {
                }
                throw th2;
            }
        }
    }

    /* JADX WARNING: Can't fix incorrect switch cases order */
    /* JADX WARNING: Code restructure failed: missing block: B:25:0x018e, code lost:
        writePatch(r6, r12, r2, r0, r7);
     */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    private static void createNewMainApkInternal(java.util.zip.ZipFile r20, java.util.List<java.util.zip.ZipEntry> r21, java.io.File r22, boolean r23, com.taobao.atlas.dexmerge.MergeExcutorServices.PrepareCallBack r24) throws java.io.IOException {
        /*
            r17 = 1024(0x400, float:1.435E-42)
            r0 = r17
            byte[] r3 = new byte[r0]
            java.util.zip.ZipOutputStream r12 = new java.util.zip.ZipOutputStream
            java.io.BufferedOutputStream r17 = new java.io.BufferedOutputStream
            java.io.FileOutputStream r18 = new java.io.FileOutputStream
            r0 = r18
            r1 = r22
            r0.<init>(r1)
            r17.<init>(r18)
            r0 = r17
            r12.<init>(r0)
            java.io.BufferedOutputStream r2 = new java.io.BufferedOutputStream
            r2.<init>(r12)
            java.util.Enumeration r5 = r20.entries()
            r7 = 1
            java.io.File r9 = new java.io.File
            java.io.File r17 = r22.getParentFile()
            java.lang.String r18 = "libcom_taobao_maindex.zip"
            r0 = r17
            r1 = r18
            r9.<init>(r0, r1)
            java.util.zip.ZipFile r18 = com.taobao.atlas.dexmerge.MergeExcutorServices.sZipPatch
            r17 = 0
            r0 = r21
            r1 = r17
            java.lang.Object r17 = r0.get(r1)
            java.util.zip.ZipEntry r17 = (java.util.zip.ZipEntry) r17
            r0 = r18
            r1 = r17
            java.io.InputStream r17 = r0.getInputStream(r1)
            r0 = r17
            inputStreamToFile(r0, r9)
            java.util.zip.ZipFile r16 = new java.util.zip.ZipFile
            r0 = r16
            r0.<init>(r9)
            java.lang.String r17 = "DEX-PATCH-INF"
            java.util.zip.ZipEntry r17 = r16.getEntry(r17)
            if (r17 == 0) goto L_0x006a
            java.lang.String r17 = "DEX-PATCH-INF"
            java.util.zip.ZipEntry r17 = r16.getEntry(r17)
            readPatchClasses(r16, r17)
        L_0x006a:
            java.util.Enumeration r6 = r16.entries()
            int r17 = patchVersion
            switch(r17) {
                case 1: goto L_0x0080;
                case 2: goto L_0x008c;
                default: goto L_0x0073;
            }
        L_0x0073:
            r9.delete()
            r16.close()
            closeQuitely(r12)
            closeQuitely(r2)
            return
        L_0x0080:
            r0 = r16
            int r7 = writePatch(r6, r12, r2, r0, r7)
            r0 = r20
            writeSourceApkDex(r5, r0, r2, r12, r7)
            goto L_0x0073
        L_0x008c:
            boolean r17 = r5.hasMoreElements()
            if (r17 == 0) goto L_0x018e
            java.lang.Object r15 = r5.nextElement()
            java.util.zip.ZipEntry r15 = (java.util.zip.ZipEntry) r15
            java.lang.String r10 = r15.getName()
            java.lang.String r17 = ".dex"
            r0 = r17
            boolean r17 = r10.endsWith(r0)
            if (r17 == 0) goto L_0x008c
            r17 = 7
            r0 = r17
            java.lang.String r13 = r10.substring(r0)
            java.lang.String r17 = "."
            r0 = r17
            boolean r17 = r13.startsWith(r0)
            if (r17 == 0) goto L_0x0148
            r17 = 0
        L_0x00bc:
            java.lang.Integer r14 = java.lang.Integer.valueOf(r17)
            java.util.TreeMap<java.lang.Integer, java.util.List<java.lang.String>> r17 = classes
            r0 = r17
            java.lang.Object r17 = r0.get(r14)
            if (r17 == 0) goto L_0x0165
            java.lang.String r18 = "MergeTool"
            java.lang.StringBuilder r17 = new java.lang.StringBuilder
            r17.<init>()
            java.lang.String r19 = "process sourceDex:"
            r0 = r17
            r1 = r19
            java.lang.StringBuilder r17 = r0.append(r1)
            r0 = r17
            java.lang.StringBuilder r17 = r0.append(r14)
            java.lang.String r19 = " and classes size:"
            r0 = r17
            r1 = r19
            java.lang.StringBuilder r19 = r0.append(r1)
            java.util.TreeMap<java.lang.Integer, java.util.List<java.lang.String>> r17 = classes
            r0 = r17
            java.lang.Object r17 = r0.get(r14)
            java.util.List r17 = (java.util.List) r17
            int r17 = r17.size()
            r0 = r19
            r1 = r17
            java.lang.StringBuilder r17 = r0.append(r1)
            java.lang.String r17 = r17.toString()
            r0 = r18
            r1 = r17
            android.util.Log.e(r0, r1)
            r0 = r20
            java.io.InputStream r8 = r0.getInputStream(r15)
            java.util.TreeMap<java.lang.Integer, java.util.List<java.lang.String>> r17 = classes
            r0 = r17
            java.lang.Object r17 = r0.get(r14)
            java.util.List r17 = (java.util.List) r17
            r0 = r17
            com.taobao.atlas.dex.Dex r4 = processDex(r8, r0)
            java.util.zip.ZipEntry r11 = new java.util.zip.ZipEntry
            java.lang.String r17 = r15.getName()
            r0 = r17
            r11.<init>(r0)
            r12.putNextEntry(r11)
            java.io.ByteArrayInputStream r17 = new java.io.ByteArrayInputStream
            byte[] r18 = r4.getBytes()
            r17.<init>(r18)
            r0 = r17
            write(r0, r12, r3)
            r2.flush()
        L_0x0144:
            int r7 = r7 + 1
            goto L_0x008c
        L_0x0148:
            r17 = 0
            java.lang.String r18 = "."
            r0 = r18
            int r18 = r13.indexOf(r0)
            r0 = r17
            r1 = r18
            java.lang.String r17 = r13.substring(r0, r1)
            java.lang.Integer r17 = java.lang.Integer.valueOf(r17)
            int r17 = r17.intValue()
            goto L_0x00bc
        L_0x0165:
            boolean r17 = isArt()
            if (r17 == 0) goto L_0x0144
            boolean r17 = isEnhanceDex(r10)
            if (r17 != 0) goto L_0x0144
            java.util.zip.ZipEntry r11 = new java.util.zip.ZipEntry
            java.lang.String r17 = r15.getName()
            r0 = r17
            r11.<init>(r0)
            r12.putNextEntry(r11)
            r0 = r20
            java.io.InputStream r17 = r0.getInputStream(r15)
            r0 = r17
            write(r0, r12, r3)
            r2.flush()
            goto L_0x0144
        L_0x018e:
            r0 = r16
            writePatch(r6, r12, r2, r0, r7)
            goto L_0x0073
        */
        throw new UnsupportedOperationException("Method not decompiled: com.taobao.atlas.dexmerge.MergeTool.createNewMainApkInternal(java.util.zip.ZipFile, java.util.List, java.io.File, boolean, com.taobao.atlas.dexmerge.MergeExcutorServices$PrepareCallBack):void");
    }

    private static int writeSourceApkDex(Enumeration e, ZipFile sourceZip, BufferedOutputStream bo, ZipOutputStream out, int i) throws IOException {
        int intValue;
        byte[] buffer = new byte[1024];
        while (e.hasMoreElements()) {
            ZipEntry zipEnt = (ZipEntry) e.nextElement();
            String name = zipEnt.getName();
            if (name.endsWith(MergeConstants.DEX_SUFFIX)) {
                Object[] objArr = new Object[3];
                objArr[0] = MergeConstants.CLASS;
                objArr[1] = i == 1 ? "" : Integer.valueOf(i);
                objArr[2] = MergeConstants.DEX_SUFFIX;
                ZipEntry newEntry = new ZipEntry(String.format("%s%s%s", objArr));
                if (isArt() && !isEnhanceDex(name)) {
                    InputStream inputStream = sourceZip.getInputStream(zipEnt);
                    out.putNextEntry(newEntry);
                    write(inputStream, out, buffer);
                    bo.flush();
                } else if (!isArt()) {
                    String s = name.substring(7);
                    if (s.startsWith(".")) {
                        intValue = 0;
                    } else {
                        intValue = Integer.valueOf(s.substring(0, s.indexOf("."))).intValue();
                    }
                    Integer sourceDexIndex = Integer.valueOf(intValue);
                    if (classes.get(sourceDexIndex) != null) {
                        Log.e("MergeTool", "process sourceDex:" + sourceDexIndex + " and classes size:" + classes.get(sourceDexIndex).size());
                        Dex dex = processDex(sourceZip.getInputStream(zipEnt), classes.get(sourceDexIndex));
                        out.putNextEntry(newEntry);
                        write(new ByteArrayInputStream(dex.getBytes()), out, buffer);
                        bo.flush();
                    }
                }
                i++;
            }
        }
        return i;
    }

    private static int writePatch(Enumeration entries, ZipOutputStream out, BufferedOutputStream bo, ZipFile zipFile, int i) throws IOException {
        byte[] buffer = new byte[1024];
        while (entries.hasMoreElements()) {
            ZipEntry zipEnt = (ZipEntry) entries.nextElement();
            String name = zipEnt.getName();
            if (name.endsWith(MergeConstants.DEX_SUFFIX)) {
                Object[] objArr = new Object[3];
                objArr[0] = MergeConstants.CLASS;
                objArr[1] = i == 1 ? "" : Integer.valueOf(i);
                objArr[2] = MergeConstants.DEX_SUFFIX;
                out.putNextEntry(new ZipEntry(String.format("%s%s%s", objArr)));
                write(zipFile.getInputStream(zipEnt), out, buffer);
                bo.flush();
                i++;
            } else {
                ZipEntry newEntry = new ZipEntry(name);
                if (name.contains("raw/") || name.contains("assets/") || name.equals("resources.arsc")) {
                    newEntry.setMethod(0);
                    newEntry.setCrc(zipEnt.getCrc());
                    newEntry.setSize(zipEnt.getSize());
                }
                out.putNextEntry(newEntry);
                write(zipFile.getInputStream(zipEnt), out, buffer);
                bo.flush();
            }
        }
        return i;
    }

    private static boolean isEnhanceDex(String name) {
        if (!TextUtils.isEmpty(noPatchDexIndex)) {
            if (name.equals(String.format("classes%s.dex", new Object[]{noPatchDexIndex})) && (Build.VERSION.SDK_INT > 27 || Build.VERSION.SDK_INT < 21)) {
                return true;
            }
        }
        return false;
    }

    private static void readPatchClasses(ZipFile zipFile, ZipEntry entry) {
        try {
            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(zipFile.getInputStream(entry)));
            while (true) {
                String s = bufferedReader.readLine();
                if (!TextUtils.isEmpty(s)) {
                    String dexIndex = s.split("-")[0];
                    String clazz = s.split("-")[1];
                    if (!TextUtils.isEmpty(clazz) && clazz.equals(MergeConstants.NO_PATCH_DEX)) {
                        noPatchDexIndex = dexIndex;
                    } else if (!TextUtils.isEmpty(clazz) && clazz.equals(MergeConstants.PATCH_VERSION)) {
                        patchVersion = Integer.valueOf(dexIndex).intValue();
                        notifyPatchVersion(patchVersion);
                    } else if (classes.containsKey(Integer.valueOf(dexIndex))) {
                        classes.get(Integer.valueOf(dexIndex)).add(clazz);
                    } else {
                        List<String> cc = new ArrayList<>();
                        cc.add(clazz);
                        classes.put(Integer.valueOf(dexIndex), cc);
                    }
                } else {
                    return;
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
            classes.clear();
        }
    }

    private static void notifyPatchVersion(int patchVersion2) {
        Intent intent = new Intent("com.taobao.atlas.intent.PATCH_VERSION");
        intent.putExtra("patch_version", patchVersion2);
        RuntimeVariables.androidApplication.sendBroadcast(intent);
    }

    private static Dex processDex(InputStream inputStream, List<String> patchClassNames) throws IOException {
        DexMerger dexMerger = new DexMerger(new Dex[]{new Dex(inputStream), new Dex(0)}, CollisionPolicy.FAIL);
        dexMerger.setRemoveTypeClasses(patchClassNames);
        dexMerger.setCompactWasteThreshold(1);
        return dexMerger.merge();
    }

    private static boolean isBundleFileUpdated(ZipEntry sourceEntry, List<ZipEntry> entryList, AtomicReference patchEntry) {
        for (ZipEntry entry : entryList) {
            if (entry.getName().contains(sourceEntry.getName())) {
                patchEntry.compareAndSet((Object) null, entry);
                return true;
            }
        }
        return false;
    }

    public static void createNewBundleInternal(String patchBundleName, ZipFile source, List<ZipEntry> entryList, File target, boolean isDiff, MergeExcutorServices.PrepareCallBack prepareCallBack) throws IOException, MergeException {
        ZipEntry newEntry;
        byte[] buffer = new byte[1024];
        ZipOutputStream zipOutputStream = new ZipOutputStream(new BufferedOutputStream(new FileOutputStream(target)));
        BufferedOutputStream bo = new BufferedOutputStream(zipOutputStream);
        Enumeration e = source.entries();
        Boolean isSourceHasDex = false;
        Boolean isPatchHasDex = false;
        ZipEntry patchDex = null;
        AtomicReference<ZipEntry> zipEntryAtomicReference = new AtomicReference<>();
        while (e.hasMoreElements()) {
            ZipEntry zipEnt = (ZipEntry) e.nextElement();
            String name = zipEnt.getName();
            if (!isDiff || !name.equals(DexFormat.DEX_IN_JAR_NAME)) {
                boolean toBeDeleted = isBundleFileUpdated(zipEnt, entryList, zipEntryAtomicReference);
                if (!toBeDeleted) {
                    ZipEntry newEntry2 = new ZipEntry(name);
                    if (MergeExcutorServices.os == MergeExcutorServices.OS.mac) {
                        if (name.contains("raw/") || name.contains("assets/")) {
                            newEntry2.setMethod(0);
                            newEntry2.setCrc(zipEnt.getCrc());
                            newEntry2.setSize(zipEnt.getSize());
                        }
                    } else if (name.contains("raw\\") || name.contains("assets\\")) {
                        newEntry2.setMethod(0);
                        newEntry2.setCrc(zipEnt.getCrc());
                        newEntry2.setSize(zipEnt.getSize());
                    }
                    zipOutputStream.putNextEntry(newEntry2);
                    write(source.getInputStream(zipEnt), zipOutputStream, buffer);
                    bo.flush();
                }
                if (toBeDeleted && zipEnt.getName().endsWith(MergeConstants.SO_SUFFIX) && zipEntryAtomicReference.get() != null && zipEntryAtomicReference.get().getName().endsWith(".patch")) {
                    File file = new File(target.getParentFile(), zipEnt.getName());
                    File file2 = new File(target.getParentFile(), zipEnt.getName() + ".patch");
                    File newFileOut = new File(target.getParentFile(), zipEnt.getName() + ".new.so");
                    if (!file.getParentFile().exists()) {
                        file.getParentFile().mkdirs();
                    }
                    inputStreamToFile(source.getInputStream(zipEnt), file);
                    inputStreamToFile(MergeExcutorServices.sZipPatch.getInputStream(zipEntryAtomicReference.get()), file2);
                    errorCheck(file, file2);
                    PatchUtils.applyPatch(file.getAbsolutePath(), newFileOut.getAbsolutePath(), file2.getAbsolutePath());
                    errorCheck(newFileOut);
                    if (!file.delete() || !newFileOut.renameTo(file)) {
                        throw new IOException("file deleted failed or file rename failed:" + file.getAbsolutePath() + " " + newFileOut.getAbsolutePath());
                    }
                    entryList.remove(zipEntryAtomicReference.getAndSet((Object) null));
                    zipOutputStream.putNextEntry(new ZipEntry(zipEnt.getName()));
                    write(new BufferedInputStream(new FileInputStream(file)), zipOutputStream, buffer);
                }
            } else {
                isSourceHasDex = true;
            }
        }
        if (isSourceHasDex.booleanValue() || !isDiff) {
            for (ZipEntry entry : entryList) {
                if (!isDiff || !entry.getName().endsWith(DexFormat.DEX_IN_JAR_NAME)) {
                    if (MergeExcutorServices.os == MergeExcutorServices.OS.mac) {
                        newEntry = new ZipEntry(entry.getName().substring(entry.getName().indexOf(WVNativeCallbackUtil.SEPERATER) + 1));
                        if (newEntry.getName().contains("raw/") || newEntry.getName().contains("assets/")) {
                            newEntry.setMethod(0);
                            newEntry.setCrc(entry.getCrc());
                            newEntry.setSize(entry.getSize());
                        }
                    } else {
                        newEntry = new ZipEntry(entry.getName().substring(entry.getName().indexOf("\\") + 1));
                        if (newEntry.getName().contains("raw\\") || newEntry.getName().contains("assets\\")) {
                            newEntry.setMethod(0);
                            newEntry.setCrc(entry.getCrc());
                            newEntry.setSize(entry.getSize());
                        }
                    }
                    zipOutputStream.putNextEntry(newEntry);
                    write(MergeExcutorServices.sZipPatch.getInputStream(entry), zipOutputStream, buffer);
                    bo.flush();
                } else {
                    patchDex = entry;
                    isPatchHasDex = true;
                }
            }
            if (isDiff) {
                if (isPatchHasDex.booleanValue() && isSourceHasDex.booleanValue()) {
                    ByteArrayOutputStream outDexStream = new ByteArrayOutputStream();
                    dexMerge(patchBundleName, source, patchDex, outDexStream, prepareCallBack);
                    ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(outDexStream.toByteArray());
                    zipOutputStream.putNextEntry(new ZipEntry(DexFormat.DEX_IN_JAR_NAME));
                    write(byteArrayInputStream, zipOutputStream, buffer);
                    bo.flush();
                } else if (isSourceHasDex.booleanValue()) {
                    zipOutputStream.putNextEntry(new ZipEntry(DexFormat.DEX_IN_JAR_NAME));
                    write(source.getInputStream(source.getEntry(DexFormat.DEX_IN_JAR_NAME)), zipOutputStream, buffer);
                    bo.flush();
                }
            }
            closeQuitely(zipOutputStream);
            closeQuitely(bo);
            return;
        }
        throw new MergeException("Original bundle has no dex");
    }

    private static void errorCheck(File... files) throws IOException {
        if (files != null) {
            int length = files.length;
            int i = 0;
            while (i < length) {
                File file = files[i];
                if (file == null) {
                    throw new IOException("file is null");
                } else if (!file.exists()) {
                    throw new IOException("file no exit:" + file.getAbsolutePath());
                } else {
                    i++;
                }
            }
        }
    }

    private static void dexMerge(String bundleName, ZipFile source, ZipEntry patchDex, OutputStream newDexStream, MergeExcutorServices.PrepareCallBack prepareCallBack) throws IOException {
        prepareCallBack.prepareMerge(bundleName, source, patchDex, newDexStream);
    }

    private static void inputStreamToFile(InputStream ins, File file) throws IOException {
        if (file.exists()) {
            file.delete();
        }
        OutputStream os = new FileOutputStream(file);
        byte[] buffer = new byte[8192];
        while (true) {
            int bytesRead = ins.read(buffer, 0, 8192);
            if (bytesRead != -1) {
                os.write(buffer, 0, bytesRead);
            } else {
                os.close();
                ins.close();
                return;
            }
        }
    }

    private static void write(InputStream in, OutputStream out, byte[] buffer) throws IOException {
        int length = in.read(buffer);
        while (length != -1) {
            out.write(buffer, 0, length);
            length = in.read(buffer);
        }
        closeQuitely(in);
    }

    private static void closeQuitely(Closeable closeable) {
        if (closeable != null) {
            try {
                closeable.close();
            } catch (Throwable th) {
            }
        }
    }

    private static boolean deleteDir(File dir) {
        if (dir.isDirectory()) {
            String[] children = dir.list();
            for (String file : children) {
                if (!deleteDir(new File(dir, file))) {
                    return false;
                }
            }
        }
        return dir.delete();
    }

    public static boolean isArt() {
        return Build.VERSION.SDK_INT > 20;
    }
}
