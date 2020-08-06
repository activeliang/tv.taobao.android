package com.ta.utdid2.core.persistent;

import com.ta.utdid2.core.persistent.MySharedPreferences;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.util.HashMap;
import java.util.Map;
import java.util.WeakHashMap;

public class TransactionXMLFile {
    /* access modifiers changed from: private */
    public static final Object GLOBAL_COMMIT_LOCK = new Object();
    public static final int MODE_PRIVATE = 0;
    private File mPreferencesDir;
    private final Object mSync = new Object();
    private HashMap<File, MySharedPreferencesImpl> sSharedPrefs = new HashMap<>();

    public TransactionXMLFile(String dir) {
        if (dir == null || dir.length() <= 0) {
            throw new RuntimeException("Directory can not be empty");
        }
        this.mPreferencesDir = new File(dir);
    }

    private File makeFilename(File base, String name) {
        if (name.indexOf(File.separatorChar) < 0) {
            return new File(base, name);
        }
        throw new IllegalArgumentException("File " + name + " contains a path separator");
    }

    private File getPreferencesDir() {
        File file;
        synchronized (this.mSync) {
            file = this.mPreferencesDir;
        }
        return file;
    }

    private File getSharedPrefsFile(String name) {
        return makeFilename(getPreferencesDir(), name + ".xml");
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
    /* JADX WARNING: Removed duplicated region for block: B:48:0x0073 A[SYNTHETIC, Splitter:B:48:0x0073] */
    public com.ta.utdid2.core.persistent.MySharedPreferences getMySharedPreferences(java.lang.String r13, int r14) {
        /*
            r12 = this;
            java.io.File r4 = r12.getSharedPrefsFile(r13)
            java.lang.Object r11 = GLOBAL_COMMIT_LOCK
            monitor-enter(r11)
            java.util.HashMap<java.io.File, com.ta.utdid2.core.persistent.TransactionXMLFile$MySharedPreferencesImpl> r10 = r12.sSharedPrefs     // Catch:{ all -> 0x0056 }
            java.lang.Object r6 = r10.get(r4)     // Catch:{ all -> 0x0056 }
            com.ta.utdid2.core.persistent.TransactionXMLFile$MySharedPreferencesImpl r6 = (com.ta.utdid2.core.persistent.TransactionXMLFile.MySharedPreferencesImpl) r6     // Catch:{ all -> 0x0056 }
            if (r6 == 0) goto L_0x001a
            boolean r10 = r6.hasFileChanged()     // Catch:{ all -> 0x0056 }
            if (r10 != 0) goto L_0x001a
            monitor-exit(r11)     // Catch:{ all -> 0x0056 }
            r7 = r6
        L_0x0019:
            return r7
        L_0x001a:
            monitor-exit(r11)     // Catch:{ all -> 0x0056 }
            r8 = 0
            java.io.File r1 = makeBackupFile(r4)
            boolean r10 = r1.exists()
            if (r10 == 0) goto L_0x002c
            r4.delete()
            r1.renameTo(r4)
        L_0x002c:
            r5 = 0
            boolean r10 = r4.exists()
            if (r10 == 0) goto L_0x004b
            boolean r10 = r4.canRead()
            if (r10 == 0) goto L_0x004b
            java.io.FileInputStream r9 = new java.io.FileInputStream     // Catch:{ XmlPullParserException -> 0x005c, Exception -> 0x0092 }
            r9.<init>(r4)     // Catch:{ XmlPullParserException -> 0x005c, Exception -> 0x0092 }
            java.util.HashMap r5 = com.ta.utdid2.core.persistent.XmlUtils.readMapXml(r9)     // Catch:{ XmlPullParserException -> 0x00c9, Exception -> 0x00c2, all -> 0x00bf }
            r9.close()     // Catch:{ XmlPullParserException -> 0x00c9, Exception -> 0x00c2, all -> 0x00bf }
            if (r9 == 0) goto L_0x00cb
            r9.close()     // Catch:{ Throwable -> 0x0059 }
            r8 = r9
        L_0x004b:
            java.lang.Object r11 = GLOBAL_COMMIT_LOCK
            monitor-enter(r11)
            if (r6 == 0) goto L_0x009b
            r6.replace(r5)     // Catch:{ all -> 0x00b3 }
        L_0x0053:
            monitor-exit(r11)     // Catch:{ all -> 0x00b3 }
            r7 = r6
            goto L_0x0019
        L_0x0056:
            r10 = move-exception
            monitor-exit(r11)     // Catch:{ all -> 0x0056 }
            throw r10
        L_0x0059:
            r10 = move-exception
            r8 = r9
            goto L_0x004b
        L_0x005c:
            r3 = move-exception
            r9 = r8
        L_0x005e:
            java.io.FileInputStream r8 = new java.io.FileInputStream     // Catch:{ Exception -> 0x0079, all -> 0x0083 }
            r8.<init>(r4)     // Catch:{ Exception -> 0x0079, all -> 0x0083 }
            int r10 = r8.available()     // Catch:{ Exception -> 0x00c7, all -> 0x00c5 }
            byte[] r2 = new byte[r10]     // Catch:{ Exception -> 0x00c7, all -> 0x00c5 }
            r8.read(r2)     // Catch:{ Exception -> 0x00c7, all -> 0x00c5 }
            if (r8 == 0) goto L_0x0071
            r8.close()     // Catch:{ Throwable -> 0x00b6 }
        L_0x0071:
            if (r8 == 0) goto L_0x004b
            r8.close()     // Catch:{ Throwable -> 0x0077 }
            goto L_0x004b
        L_0x0077:
            r10 = move-exception
            goto L_0x004b
        L_0x0079:
            r10 = move-exception
            r8 = r9
        L_0x007b:
            if (r8 == 0) goto L_0x0071
            r8.close()     // Catch:{ Throwable -> 0x0081 }
            goto L_0x0071
        L_0x0081:
            r10 = move-exception
            goto L_0x0071
        L_0x0083:
            r10 = move-exception
            r8 = r9
        L_0x0085:
            if (r8 == 0) goto L_0x008a
            r8.close()     // Catch:{ Throwable -> 0x00b8 }
        L_0x008a:
            throw r10     // Catch:{ all -> 0x008b }
        L_0x008b:
            r10 = move-exception
        L_0x008c:
            if (r8 == 0) goto L_0x0091
            r8.close()     // Catch:{ Throwable -> 0x00ba }
        L_0x0091:
            throw r10
        L_0x0092:
            r10 = move-exception
        L_0x0093:
            if (r8 == 0) goto L_0x004b
            r8.close()     // Catch:{ Throwable -> 0x0099 }
            goto L_0x004b
        L_0x0099:
            r10 = move-exception
            goto L_0x004b
        L_0x009b:
            java.util.HashMap<java.io.File, com.ta.utdid2.core.persistent.TransactionXMLFile$MySharedPreferencesImpl> r10 = r12.sSharedPrefs     // Catch:{ all -> 0x00b3 }
            java.lang.Object r10 = r10.get(r4)     // Catch:{ all -> 0x00b3 }
            r0 = r10
            com.ta.utdid2.core.persistent.TransactionXMLFile$MySharedPreferencesImpl r0 = (com.ta.utdid2.core.persistent.TransactionXMLFile.MySharedPreferencesImpl) r0     // Catch:{ all -> 0x00b3 }
            r6 = r0
            if (r6 != 0) goto L_0x0053
            com.ta.utdid2.core.persistent.TransactionXMLFile$MySharedPreferencesImpl r7 = new com.ta.utdid2.core.persistent.TransactionXMLFile$MySharedPreferencesImpl     // Catch:{ all -> 0x00b3 }
            r7.<init>(r4, r14, r5)     // Catch:{ all -> 0x00b3 }
            java.util.HashMap<java.io.File, com.ta.utdid2.core.persistent.TransactionXMLFile$MySharedPreferencesImpl> r10 = r12.sSharedPrefs     // Catch:{ all -> 0x00bc }
            r10.put(r4, r7)     // Catch:{ all -> 0x00bc }
            r6 = r7
            goto L_0x0053
        L_0x00b3:
            r10 = move-exception
        L_0x00b4:
            monitor-exit(r11)     // Catch:{ all -> 0x00b3 }
            throw r10
        L_0x00b6:
            r10 = move-exception
            goto L_0x0071
        L_0x00b8:
            r11 = move-exception
            goto L_0x008a
        L_0x00ba:
            r11 = move-exception
            goto L_0x0091
        L_0x00bc:
            r10 = move-exception
            r6 = r7
            goto L_0x00b4
        L_0x00bf:
            r10 = move-exception
            r8 = r9
            goto L_0x008c
        L_0x00c2:
            r10 = move-exception
            r8 = r9
            goto L_0x0093
        L_0x00c5:
            r10 = move-exception
            goto L_0x0085
        L_0x00c7:
            r10 = move-exception
            goto L_0x007b
        L_0x00c9:
            r3 = move-exception
            goto L_0x005e
        L_0x00cb:
            r8 = r9
            goto L_0x004b
        */
        throw new UnsupportedOperationException("Method not decompiled: com.ta.utdid2.core.persistent.TransactionXMLFile.getMySharedPreferences(java.lang.String, int):com.ta.utdid2.core.persistent.MySharedPreferences");
    }

    /* access modifiers changed from: private */
    public static File makeBackupFile(File prefsFile) {
        return new File(prefsFile.getPath() + ".bak");
    }

    private static final class MySharedPreferencesImpl implements MySharedPreferences {
        private static final Object mContent = new Object();
        private boolean hasChange = false;
        private final File mBackupFile;
        private final File mFile;
        /* access modifiers changed from: private */
        public WeakHashMap<MySharedPreferences.OnSharedPreferenceChangeListener, Object> mListeners;
        /* access modifiers changed from: private */
        public Map mMap;
        private final int mMode;

        MySharedPreferencesImpl(File file, int mode, Map initialContents) {
            this.mFile = file;
            this.mBackupFile = TransactionXMLFile.makeBackupFile(file);
            this.mMode = mode;
            this.mMap = initialContents == null ? new HashMap() : initialContents;
            this.mListeners = new WeakHashMap<>();
        }

        public boolean checkFile() {
            if (this.mFile == null || !new File(this.mFile.getAbsolutePath()).exists()) {
                return false;
            }
            return true;
        }

        public void setHasChange(boolean hasChange2) {
            synchronized (this) {
                this.hasChange = hasChange2;
            }
        }

        public boolean hasFileChanged() {
            boolean z;
            synchronized (this) {
                z = this.hasChange;
            }
            return z;
        }

        public void replace(Map newContents) {
            if (newContents != null) {
                synchronized (this) {
                    this.mMap = newContents;
                }
            }
        }

        public void registerOnSharedPreferenceChangeListener(MySharedPreferences.OnSharedPreferenceChangeListener listener) {
            synchronized (this) {
                this.mListeners.put(listener, mContent);
            }
        }

        public void unregisterOnSharedPreferenceChangeListener(MySharedPreferences.OnSharedPreferenceChangeListener listener) {
            synchronized (this) {
                this.mListeners.remove(listener);
            }
        }

        public Map<String, ?> getAll() {
            HashMap hashMap;
            synchronized (this) {
                hashMap = new HashMap(this.mMap);
            }
            return hashMap;
        }

        public String getString(String key, String defValue) {
            String v;
            synchronized (this) {
                v = (String) this.mMap.get(key);
                if (v == null) {
                    v = defValue;
                }
            }
            return v;
        }

        public int getInt(String key, int defValue) {
            synchronized (this) {
                Integer v = (Integer) this.mMap.get(key);
                if (v != null) {
                    defValue = v.intValue();
                }
            }
            return defValue;
        }

        public long getLong(String key, long defValue) {
            synchronized (this) {
                Long v = (Long) this.mMap.get(key);
                if (v != null) {
                    defValue = v.longValue();
                }
            }
            return defValue;
        }

        public float getFloat(String key, float defValue) {
            synchronized (this) {
                Float v = (Float) this.mMap.get(key);
                if (v != null) {
                    defValue = v.floatValue();
                }
            }
            return defValue;
        }

        public boolean getBoolean(String key, boolean defValue) {
            synchronized (this) {
                Boolean v = (Boolean) this.mMap.get(key);
                if (v != null) {
                    defValue = v.booleanValue();
                }
            }
            return defValue;
        }

        public boolean contains(String key) {
            boolean containsKey;
            synchronized (this) {
                containsKey = this.mMap.containsKey(key);
            }
            return containsKey;
        }

        public final class EditorImpl implements MySharedPreferences.MyEditor {
            private boolean mClear = false;
            private final Map<String, Object> mModified = new HashMap();

            public EditorImpl() {
            }

            public MySharedPreferences.MyEditor putString(String key, String value) {
                synchronized (this) {
                    this.mModified.put(key, value);
                }
                return this;
            }

            public MySharedPreferences.MyEditor putInt(String key, int value) {
                synchronized (this) {
                    this.mModified.put(key, Integer.valueOf(value));
                }
                return this;
            }

            public MySharedPreferences.MyEditor putLong(String key, long value) {
                synchronized (this) {
                    this.mModified.put(key, Long.valueOf(value));
                }
                return this;
            }

            public MySharedPreferences.MyEditor putFloat(String key, float value) {
                synchronized (this) {
                    this.mModified.put(key, Float.valueOf(value));
                }
                return this;
            }

            public MySharedPreferences.MyEditor putBoolean(String key, boolean value) {
                synchronized (this) {
                    this.mModified.put(key, Boolean.valueOf(value));
                }
                return this;
            }

            public MySharedPreferences.MyEditor remove(String key) {
                synchronized (this) {
                    this.mModified.put(key, this);
                }
                return this;
            }

            public MySharedPreferences.MyEditor clear() {
                synchronized (this) {
                    this.mClear = true;
                }
                return this;
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
                	at jadx.core.dex.visitors.regions.RegionMaker.traverseMonitorExits(RegionMaker.java:619)
                	at jadx.core.dex.visitors.regions.RegionMaker.traverseMonitorExits(RegionMaker.java:619)
                	at jadx.core.dex.visitors.regions.RegionMaker.traverseMonitorExits(RegionMaker.java:619)
                	at jadx.core.dex.visitors.regions.RegionMaker.traverseMonitorExits(RegionMaker.java:619)
                	at jadx.core.dex.visitors.regions.RegionMaker.traverseMonitorExits(RegionMaker.java:619)
                	at jadx.core.dex.visitors.regions.RegionMaker.traverseMonitorExits(RegionMaker.java:619)
                	at jadx.core.dex.visitors.regions.RegionMaker.traverseMonitorExits(RegionMaker.java:619)
                	at jadx.core.dex.visitors.regions.RegionMaker.traverseMonitorExits(RegionMaker.java:619)
                	at jadx.core.dex.visitors.regions.RegionMaker.traverseMonitorExits(RegionMaker.java:619)
                	at jadx.core.dex.visitors.regions.RegionMaker.traverseMonitorExits(RegionMaker.java:619)
                	at jadx.core.dex.visitors.regions.RegionMaker.traverseMonitorExits(RegionMaker.java:619)
                	at jadx.core.dex.visitors.regions.RegionMaker.processMonitorEnter(RegionMaker.java:561)
                	at jadx.core.dex.visitors.regions.RegionMaker.traverse(RegionMaker.java:133)
                	at jadx.core.dex.visitors.regions.RegionMaker.makeRegion(RegionMaker.java:86)
                	at jadx.core.dex.visitors.regions.RegionMaker.processMonitorEnter(RegionMaker.java:598)
                	at jadx.core.dex.visitors.regions.RegionMaker.traverse(RegionMaker.java:133)
                	at jadx.core.dex.visitors.regions.RegionMaker.makeRegion(RegionMaker.java:86)
                	at jadx.core.dex.visitors.regions.RegionMakerVisitor.visit(RegionMakerVisitor.java:49)
                */
            public boolean commit() {
                /*
                    r15 = this;
                    r1 = 1
                    r12 = 0
                    r5 = 0
                    r8 = 0
                    java.lang.Object r13 = com.ta.utdid2.core.persistent.TransactionXMLFile.GLOBAL_COMMIT_LOCK
                    monitor-enter(r13)
                    com.ta.utdid2.core.persistent.TransactionXMLFile$MySharedPreferencesImpl r14 = com.ta.utdid2.core.persistent.TransactionXMLFile.MySharedPreferencesImpl.this     // Catch:{ all -> 0x0072 }
                    java.util.WeakHashMap r14 = r14.mListeners     // Catch:{ all -> 0x0072 }
                    int r14 = r14.size()     // Catch:{ all -> 0x0072 }
                    if (r14 <= 0) goto L_0x0075
                L_0x0015:
                    if (r1 == 0) goto L_0x002d
                    java.util.ArrayList r6 = new java.util.ArrayList     // Catch:{ all -> 0x0072 }
                    r6.<init>()     // Catch:{ all -> 0x0072 }
                    java.util.HashSet r9 = new java.util.HashSet     // Catch:{ all -> 0x00c2 }
                    com.ta.utdid2.core.persistent.TransactionXMLFile$MySharedPreferencesImpl r12 = com.ta.utdid2.core.persistent.TransactionXMLFile.MySharedPreferencesImpl.this     // Catch:{ all -> 0x00c2 }
                    java.util.WeakHashMap r12 = r12.mListeners     // Catch:{ all -> 0x00c2 }
                    java.util.Set r12 = r12.keySet()     // Catch:{ all -> 0x00c2 }
                    r9.<init>(r12)     // Catch:{ all -> 0x00c2 }
                    r8 = r9
                    r5 = r6
                L_0x002d:
                    monitor-enter(r15)     // Catch:{ all -> 0x0072 }
                    boolean r12 = r15.mClear     // Catch:{ all -> 0x006f }
                    if (r12 == 0) goto L_0x003e
                    com.ta.utdid2.core.persistent.TransactionXMLFile$MySharedPreferencesImpl r12 = com.ta.utdid2.core.persistent.TransactionXMLFile.MySharedPreferencesImpl.this     // Catch:{ all -> 0x006f }
                    java.util.Map r12 = r12.mMap     // Catch:{ all -> 0x006f }
                    r12.clear()     // Catch:{ all -> 0x006f }
                    r12 = 0
                    r15.mClear = r12     // Catch:{ all -> 0x006f }
                L_0x003e:
                    java.util.Map<java.lang.String, java.lang.Object> r12 = r15.mModified     // Catch:{ all -> 0x006f }
                    java.util.Set r12 = r12.entrySet()     // Catch:{ all -> 0x006f }
                    java.util.Iterator r12 = r12.iterator()     // Catch:{ all -> 0x006f }
                L_0x0048:
                    boolean r14 = r12.hasNext()     // Catch:{ all -> 0x006f }
                    if (r14 == 0) goto L_0x0081
                    java.lang.Object r0 = r12.next()     // Catch:{ all -> 0x006f }
                    java.util.Map$Entry r0 = (java.util.Map.Entry) r0     // Catch:{ all -> 0x006f }
                    java.lang.Object r3 = r0.getKey()     // Catch:{ all -> 0x006f }
                    java.lang.String r3 = (java.lang.String) r3     // Catch:{ all -> 0x006f }
                    java.lang.Object r11 = r0.getValue()     // Catch:{ all -> 0x006f }
                    if (r11 != r15) goto L_0x0077
                    com.ta.utdid2.core.persistent.TransactionXMLFile$MySharedPreferencesImpl r14 = com.ta.utdid2.core.persistent.TransactionXMLFile.MySharedPreferencesImpl.this     // Catch:{ all -> 0x006f }
                    java.util.Map r14 = r14.mMap     // Catch:{ all -> 0x006f }
                    r14.remove(r3)     // Catch:{ all -> 0x006f }
                L_0x0069:
                    if (r1 == 0) goto L_0x0048
                    r5.add(r3)     // Catch:{ all -> 0x006f }
                    goto L_0x0048
                L_0x006f:
                    r12 = move-exception
                    monitor-exit(r15)     // Catch:{ all -> 0x006f }
                    throw r12     // Catch:{ all -> 0x0072 }
                L_0x0072:
                    r12 = move-exception
                L_0x0073:
                    monitor-exit(r13)     // Catch:{ all -> 0x0072 }
                    throw r12
                L_0x0075:
                    r1 = r12
                    goto L_0x0015
                L_0x0077:
                    com.ta.utdid2.core.persistent.TransactionXMLFile$MySharedPreferencesImpl r14 = com.ta.utdid2.core.persistent.TransactionXMLFile.MySharedPreferencesImpl.this     // Catch:{ all -> 0x006f }
                    java.util.Map r14 = r14.mMap     // Catch:{ all -> 0x006f }
                    r14.put(r3, r11)     // Catch:{ all -> 0x006f }
                    goto L_0x0069
                L_0x0081:
                    java.util.Map<java.lang.String, java.lang.Object> r12 = r15.mModified     // Catch:{ all -> 0x006f }
                    r12.clear()     // Catch:{ all -> 0x006f }
                    monitor-exit(r15)     // Catch:{ all -> 0x006f }
                    com.ta.utdid2.core.persistent.TransactionXMLFile$MySharedPreferencesImpl r12 = com.ta.utdid2.core.persistent.TransactionXMLFile.MySharedPreferencesImpl.this     // Catch:{ all -> 0x0072 }
                    boolean r10 = r12.writeFileLocked()     // Catch:{ all -> 0x0072 }
                    if (r10 == 0) goto L_0x0095
                    com.ta.utdid2.core.persistent.TransactionXMLFile$MySharedPreferencesImpl r12 = com.ta.utdid2.core.persistent.TransactionXMLFile.MySharedPreferencesImpl.this     // Catch:{ all -> 0x0072 }
                    r14 = 1
                    r12.setHasChange(r14)     // Catch:{ all -> 0x0072 }
                L_0x0095:
                    monitor-exit(r13)     // Catch:{ all -> 0x0072 }
                    if (r1 == 0) goto L_0x00c1
                    int r12 = r5.size()
                    int r2 = r12 + -1
                L_0x009e:
                    if (r2 < 0) goto L_0x00c1
                    java.lang.Object r4 = r5.get(r2)
                    java.lang.String r4 = (java.lang.String) r4
                    java.util.Iterator r12 = r8.iterator()
                L_0x00aa:
                    boolean r13 = r12.hasNext()
                    if (r13 == 0) goto L_0x00be
                    java.lang.Object r7 = r12.next()
                    com.ta.utdid2.core.persistent.MySharedPreferences$OnSharedPreferenceChangeListener r7 = (com.ta.utdid2.core.persistent.MySharedPreferences.OnSharedPreferenceChangeListener) r7
                    if (r7 == 0) goto L_0x00aa
                    com.ta.utdid2.core.persistent.TransactionXMLFile$MySharedPreferencesImpl r13 = com.ta.utdid2.core.persistent.TransactionXMLFile.MySharedPreferencesImpl.this
                    r7.onSharedPreferenceChanged(r13, r4)
                    goto L_0x00aa
                L_0x00be:
                    int r2 = r2 + -1
                    goto L_0x009e
                L_0x00c1:
                    return r10
                L_0x00c2:
                    r12 = move-exception
                    r5 = r6
                    goto L_0x0073
                */
                throw new UnsupportedOperationException("Method not decompiled: com.ta.utdid2.core.persistent.TransactionXMLFile.MySharedPreferencesImpl.EditorImpl.commit():boolean");
            }
        }

        public MySharedPreferences.MyEditor edit() {
            return new EditorImpl();
        }

        private FileOutputStream createFileOutputStream(File file) {
            FileOutputStream str = null;
            try {
                str = new FileOutputStream(file);
            } catch (FileNotFoundException e) {
                if (!file.getParentFile().mkdir()) {
                    return null;
                }
                try {
                    str = new FileOutputStream(file);
                } catch (FileNotFoundException e2) {
                }
            }
            return str;
        }

        /* access modifiers changed from: private */
        public boolean writeFileLocked() {
            if (this.mFile.exists()) {
                if (this.mBackupFile.exists()) {
                    this.mFile.delete();
                } else if (!this.mFile.renameTo(this.mBackupFile)) {
                    return false;
                }
            }
            try {
                FileOutputStream str = createFileOutputStream(this.mFile);
                if (str == null) {
                    return false;
                }
                XmlUtils.writeMapXml(this.mMap, str);
                str.close();
                this.mBackupFile.delete();
                return true;
            } catch (Exception e) {
                if (!this.mFile.exists() || !this.mFile.delete()) {
                }
                return false;
            }
        }
    }
}
