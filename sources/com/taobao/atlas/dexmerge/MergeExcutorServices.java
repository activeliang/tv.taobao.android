package com.taobao.atlas.dexmerge;

import android.os.RemoteException;
import android.taobao.windvane.util.WVNativeCallbackUtil;
import android.util.Log;
import com.taobao.atlas.dex.Dex;
import com.taobao.atlas.dexmerge.dx.merge.CollisionPolicy;
import com.taobao.atlas.dexmerge.dx.merge.DexMerger;
import io.reactivex.Observable;
import io.reactivex.ObservableSource;
import io.reactivex.Observer;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;
import io.reactivex.functions.Function;
import io.reactivex.plugins.RxJavaPlugins;
import io.reactivex.schedulers.Schedulers;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.Callable;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutionException;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;

public class MergeExcutorServices {
    private static final String TAG = "mergeTask";
    public static OS os = OS.mac;
    public static ZipFile sZipPatch;
    HashMap<String, List<ZipEntry>> bundleEntryGroup = new HashMap<>();
    /* access modifiers changed from: private */
    public IDexMergeCallback mCallback = null;

    enum OS {
        mac,
        windows,
        linux
    }

    public interface PrepareCallBack {
        void prepareMerge(String str, ZipFile zipFile, ZipEntry zipEntry, OutputStream outputStream) throws IOException;
    }

    public MergeExcutorServices(IDexMergeCallback mCallback2) throws RemoteException {
        this.mCallback = mCallback2;
    }

    public void excute(String patchFilePath, List<MergeObject> list, boolean b) throws ExecutionException, InterruptedException {
        Observable.just(patchFilePath).map(new Function<String, Map>() {
            public Map<String, List<ZipEntry>> apply(String s) throws Exception {
                try {
                    MergeExcutorServices.sZipPatch = new ZipFile(s);
                    Enumeration<? extends ZipEntry> zes = MergeExcutorServices.sZipPatch.entries();
                    String key = null;
                    while (zes.hasMoreElements()) {
                        ZipEntry entry = (ZipEntry) zes.nextElement();
                        if (entry.getName().equals("libcom_taobao_maindex.so")) {
                            List<ZipEntry> mainDex = new ArrayList<>();
                            mainDex.add(entry);
                            MergeExcutorServices.this.bundleEntryGroup.put("com_taobao_maindex", mainDex);
                        } else if (entry.getName().startsWith("lib")) {
                            if (entry.getName().indexOf(WVNativeCallbackUtil.SEPERATER) != -1) {
                                key = entry.getName().substring(3, entry.getName().indexOf(WVNativeCallbackUtil.SEPERATER));
                                MergeExcutorServices.os = OS.mac;
                            } else if (entry.getName().indexOf("\\") != -1) {
                                key = entry.getName().substring(3, entry.getName().indexOf("\\"));
                                MergeExcutorServices.os = OS.windows;
                            }
                            if (MergeExcutorServices.this.bundleEntryGroup.get(key) == null) {
                                List<ZipEntry> bundleEntry = new ArrayList<>();
                                MergeExcutorServices.this.bundleEntryGroup.put(key, bundleEntry);
                                bundleEntry.add(entry);
                            } else {
                                MergeExcutorServices.this.bundleEntryGroup.get(key).add(entry);
                            }
                        }
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
                return MergeExcutorServices.this.bundleEntryGroup;
            }
        }).subscribe(new Observer<Map>() {
            public void onError(Throwable e) {
                try {
                    MergeExcutorServices.this.mCallback.onMergeAllFinish(false, e.getMessage());
                } catch (RemoteException e1) {
                    e1.printStackTrace();
                }
            }

            public void onComplete() {
                Log.e("MergeExcutorServices", "merge bundle size:" + MergeExcutorServices.this.bundleEntryGroup.size());
            }

            public void onSubscribe(Disposable disposable) {
            }

            public void onNext(Map map) {
            }
        });
        MergeTask[] tasks = new MergeTask[list.size()];
        for (int i = 0; i < list.size(); i++) {
            tasks[i] = new MergeTask(new File(list.get(i).originalFile), this.bundleEntryGroup.get(list.get(i).patchName.replace(".", "_")), list.get(i).patchName, new File(list.get(i).mergeFile), b);
        }
        System.setProperty("rx2.computation-threads", String.valueOf(Runtime.getRuntime().availableProcessors() / 2));
        RxJavaPlugins.setErrorHandler(new Consumer<Throwable>() {
            public void accept(Throwable throwable) throws Exception {
                MergeExcutorServices.this.mCallback.onMergeAllFinish(false, throwable.getMessage());
            }
        });
        final CountDownLatch countDownLatch = new CountDownLatch(1);
        try {
            Observable.fromArray(tasks).flatMap(new Function<MergeTask, ObservableSource<File>>() {
                public ObservableSource<File> apply(MergeTask mergeTask) throws Exception {
                    return Observable.just(mergeTask).map(new Function<MergeTask, File>() {
                        public File apply(MergeTask mergeTask) throws Exception {
                            try {
                                return mergeTask.call();
                            } catch (IllegalStateException e) {
                                e.printStackTrace();
                                return null;
                            }
                        }
                    }).subscribeOn(Schedulers.computation());
                }
            }).subscribe(new Observer<File>() {
                public void onError(Throwable e) {
                    try {
                        MergeExcutorServices.this.mCallback.onMergeAllFinish(false, e.getMessage());
                    } catch (RemoteException e1) {
                        e1.printStackTrace();
                    }
                    countDownLatch.countDown();
                }

                public void onComplete() {
                    try {
                        MergeExcutorServices.this.mCallback.onMergeAllFinish(true, (String) null);
                    } catch (RemoteException e) {
                        e.printStackTrace();
                    }
                    countDownLatch.countDown();
                }

                public void onSubscribe(Disposable disposable) {
                    if (disposable.isDisposed()) {
                        onError(new IllegalStateException("connection closed!"));
                    }
                }

                public void onNext(File file) {
                    if (file != null && file.exists()) {
                        try {
                            MergeExcutorServices.this.mCallback.onMergeFinish(file.getAbsolutePath(), true, (String) null);
                        } catch (RemoteException e) {
                            e.printStackTrace();
                        }
                    }
                }
            });
            countDownLatch.await();
            Schedulers.shutdown();
            if (sZipPatch != null) {
                try {
                    sZipPatch.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        } catch (Throwable e2) {
            try {
                this.mCallback.onMergeAllFinish(false, e2.getMessage());
            } catch (RemoteException e1) {
                e1.printStackTrace();
            } catch (Throwable th) {
                Schedulers.shutdown();
                if (sZipPatch != null) {
                    try {
                        sZipPatch.close();
                    } catch (IOException e3) {
                        e3.printStackTrace();
                    }
                }
                throw th;
            }
            Schedulers.shutdown();
            if (sZipPatch != null) {
                try {
                    sZipPatch.close();
                } catch (IOException e4) {
                    e4.printStackTrace();
                }
            }
        }
    }

    public class MergeTask implements Callable<File> {
        private boolean diffDex;
        private File outFile;
        private List<ZipEntry> patchEntries;
        private String patchName;
        private File sourceFile;

        public MergeTask(File sourceFile2, List<ZipEntry> patchEntries2, String patchName2, File outFile2, boolean diff) {
            this.sourceFile = sourceFile2;
            this.patchName = patchName2;
            this.patchEntries = patchEntries2;
            this.outFile = outFile2;
            this.diffDex = diff;
        }

        public File call() throws IOException, MergeException {
            MergeTool.mergePrepare(this.sourceFile, this.patchEntries, this.patchName, this.outFile, this.diffDex, new PrepareCallBack() {
                /*  JADX ERROR: StackOverflow in pass: MarkFinallyVisitor
                    jadx.core.utils.exceptions.JadxOverflowException: 
                    	at jadx.core.utils.ErrorsCounter.addError(ErrorsCounter.java:47)
                    	at jadx.core.utils.ErrorsCounter.methodError(ErrorsCounter.java:81)
                    */
                public void prepareMerge(java.lang.String r9, java.util.zip.ZipFile r10, java.util.zip.ZipEntry r11, java.io.OutputStream r12) throws java.io.IOException {
                    /*
                        r8 = this;
                        r5 = 0
                        r0 = 0
                        r4 = 2
                        java.io.InputStream[] r3 = new java.io.InputStream[r4]
                        r4 = 0
                        java.util.zip.ZipEntry r6 = new java.util.zip.ZipEntry     // Catch:{ IOException -> 0x0031 }
                        java.lang.String r7 = "classes.dex"
                        r6.<init>(r7)     // Catch:{ IOException -> 0x0031 }
                        java.io.InputStream r6 = r10.getInputStream(r6)     // Catch:{ IOException -> 0x0031 }
                        r3[r4] = r6     // Catch:{ IOException -> 0x0031 }
                        r4 = 1
                        java.util.zip.ZipFile r6 = com.taobao.atlas.dexmerge.MergeExcutorServices.sZipPatch     // Catch:{ IOException -> 0x0031 }
                        java.io.InputStream r6 = r6.getInputStream(r11)     // Catch:{ IOException -> 0x0031 }
                        r3[r4] = r6     // Catch:{ IOException -> 0x0031 }
                        com.taobao.atlas.dexmerge.MergeExcutorServices$MergeTask r4 = com.taobao.atlas.dexmerge.MergeExcutorServices.MergeTask.this     // Catch:{ IOException -> 0x0031 }
                        com.taobao.atlas.dexmerge.MergeExcutorServices r4 = com.taobao.atlas.dexmerge.MergeExcutorServices.this     // Catch:{ IOException -> 0x0031 }
                        r4.dexMergeInternal(r3, r12, r9)     // Catch:{ IOException -> 0x0031 }
                        int r4 = r3.length
                    L_0x0025:
                        if (r5 >= r4) goto L_0x0051
                        r2 = r3[r5]
                        if (r2 == 0) goto L_0x002e
                        r2.close()
                    L_0x002e:
                        int r5 = r5 + 1
                        goto L_0x0025
                    L_0x0031:
                        r1 = move-exception
                        r1.printStackTrace()     // Catch:{ all -> 0x0042 }
                        int r4 = r3.length
                    L_0x0036:
                        if (r5 >= r4) goto L_0x0051
                        r2 = r3[r5]
                        if (r2 == 0) goto L_0x003f
                        r2.close()
                    L_0x003f:
                        int r5 = r5 + 1
                        goto L_0x0036
                    L_0x0042:
                        r4 = move-exception
                        int r6 = r3.length
                    L_0x0044:
                        if (r5 >= r6) goto L_0x0050
                        r2 = r3[r5]
                        if (r2 == 0) goto L_0x004d
                        r2.close()
                    L_0x004d:
                        int r5 = r5 + 1
                        goto L_0x0044
                    L_0x0050:
                        throw r4
                    L_0x0051:
                        return
                    */
                    throw new UnsupportedOperationException("Method not decompiled: com.taobao.atlas.dexmerge.MergeExcutorServices.MergeTask.AnonymousClass1.prepareMerge(java.lang.String, java.util.zip.ZipFile, java.util.zip.ZipEntry, java.io.OutputStream):void");
                }
            });
            return this.outFile;
        }
    }

    /* access modifiers changed from: private */
    public void dexMergeInternal(InputStream[] inputStreams, OutputStream newDexStream, String bundleName) throws IOException {
        FileOutputStream fileOutputStream = null;
        if (inputStreams[0] == null || inputStreams[1] == null) {
            try {
                this.mCallback.onMergeFinish(bundleName, false, "argNUll");
            } catch (RemoteException e) {
                e.printStackTrace();
            }
        } else {
            try {
                Dex dex1 = new Dex(inputStreams[1]);
                Dex dex2 = new Dex(inputStreams[0]);
                List<Dex> dexs = new ArrayList<>();
                dexs.add(dex1);
                dexs.add(dex2);
                DexMerger mDexMerge = new DexMerger(new Dex[]{dex1, dex2}, CollisionPolicy.KEEP_FIRST);
                mDexMerge.setCompactWasteThreshold(1);
                mDexMerge.merge().writeTo(newDexStream);
                newDexStream.flush();
                if (fileOutputStream != null) {
                    try {
                    } catch (IOException e2) {
                        e2.printStackTrace();
                    }
                }
            } finally {
                if (fileOutputStream != null) {
                    try {
                        fileOutputStream.close();
                    } catch (IOException e3) {
                        e3.printStackTrace();
                    }
                }
            }
        }
    }

    public static void main(String[] args) throws InterruptedException {
        new MergeObject((String) null, (String) null, (String) null);
        new MergeObject((String) null, (String) null, (String) null);
        final CountDownLatch countDownLatch = new CountDownLatch(1);
        Observable.fromArray("a", "b", "c", "d", "e").flatMap(new Function<String, ObservableSource<String>>() {
            public ObservableSource<String> apply(String s) throws Exception {
                return Observable.just(s).map(new Function<String, String>() {
                    public String apply(String s) throws Exception {
                        return s + s;
                    }
                }).subscribeOn(Schedulers.computation());
            }
        }).observeOn(Schedulers.newThread()).subscribe(new Observer<String>() {
            public void onSubscribe(Disposable d) {
            }

            public void onNext(String value) {
                System.out.println(value);
                System.out.println("xxxx");
            }

            public void onError(Throwable e) {
                System.out.println("onError");
            }

            public void onComplete() {
                System.out.println("onComplete");
                countDownLatch.countDown();
            }
        });
        countDownLatch.await();
    }
}
