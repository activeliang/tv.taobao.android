package com.bumptech.glide.disklrucache;

import java.io.BufferedWriter;
import java.io.Closeable;
import java.io.EOFException;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.concurrent.Callable;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

public final class DiskLruCache implements Closeable {
    static final long ANY_SEQUENCE_NUMBER = -1;
    private static final String CLEAN = "CLEAN";
    private static final String DIRTY = "DIRTY";
    static final String JOURNAL_FILE = "journal";
    static final String JOURNAL_FILE_BACKUP = "journal.bkp";
    static final String JOURNAL_FILE_TEMP = "journal.tmp";
    static final String MAGIC = "libcore.io.DiskLruCache";
    private static final String READ = "READ";
    private static final String REMOVE = "REMOVE";
    static final String VERSION_1 = "1";
    private final int appVersion;
    private final Callable<Void> cleanupCallable = new Callable<Void>() {
        public Void call() throws Exception {
            synchronized (DiskLruCache.this) {
                if (DiskLruCache.this.journalWriter != null) {
                    DiskLruCache.this.trimToSize();
                    if (DiskLruCache.this.journalRebuildRequired()) {
                        DiskLruCache.this.rebuildJournal();
                        int unused = DiskLruCache.this.redundantOpCount = 0;
                    }
                }
            }
            return null;
        }
    };
    /* access modifiers changed from: private */
    public final File directory;
    final ThreadPoolExecutor executorService = new ThreadPoolExecutor(0, 1, 60, TimeUnit.SECONDS, new LinkedBlockingQueue(), new DiskLruCacheThreadFactory());
    private final File journalFile;
    private final File journalFileBackup;
    private final File journalFileTmp;
    /* access modifiers changed from: private */
    public Writer journalWriter;
    private final LinkedHashMap<String, Entry> lruEntries = new LinkedHashMap<>(0, 0.75f, true);
    private long maxSize;
    private long nextSequenceNumber = 0;
    /* access modifiers changed from: private */
    public int redundantOpCount;
    private long size = 0;
    /* access modifiers changed from: private */
    public final int valueCount;

    private DiskLruCache(File directory2, int appVersion2, int valueCount2, long maxSize2) {
        this.directory = directory2;
        this.appVersion = appVersion2;
        this.journalFile = new File(directory2, JOURNAL_FILE);
        this.journalFileTmp = new File(directory2, JOURNAL_FILE_TEMP);
        this.journalFileBackup = new File(directory2, JOURNAL_FILE_BACKUP);
        this.valueCount = valueCount2;
        this.maxSize = maxSize2;
    }

    public static DiskLruCache open(File directory2, int appVersion2, int valueCount2, long maxSize2) throws IOException {
        if (maxSize2 <= 0) {
            throw new IllegalArgumentException("maxSize <= 0");
        } else if (valueCount2 <= 0) {
            throw new IllegalArgumentException("valueCount <= 0");
        } else {
            File backupFile = new File(directory2, JOURNAL_FILE_BACKUP);
            if (backupFile.exists()) {
                File journalFile2 = new File(directory2, JOURNAL_FILE);
                if (journalFile2.exists()) {
                    backupFile.delete();
                } else {
                    renameTo(backupFile, journalFile2, false);
                }
            }
            DiskLruCache cache = new DiskLruCache(directory2, appVersion2, valueCount2, maxSize2);
            if (cache.journalFile.exists()) {
                try {
                    cache.readJournal();
                    cache.processJournal();
                    return cache;
                } catch (IOException journalIsCorrupt) {
                    System.out.println("DiskLruCache " + directory2 + " is corrupt: " + journalIsCorrupt.getMessage() + ", removing");
                    cache.delete();
                }
            }
            directory2.mkdirs();
            DiskLruCache cache2 = new DiskLruCache(directory2, appVersion2, valueCount2, maxSize2);
            cache2.rebuildJournal();
            return cache2;
        }
    }

    private void readJournal() throws IOException {
        int lineCount;
        StrictLineReader reader = new StrictLineReader(new FileInputStream(this.journalFile), Util.US_ASCII);
        try {
            String magic = reader.readLine();
            String version = reader.readLine();
            String appVersionString = reader.readLine();
            String valueCountString = reader.readLine();
            String blank = reader.readLine();
            if (!MAGIC.equals(magic) || !"1".equals(version) || !Integer.toString(this.appVersion).equals(appVersionString) || !Integer.toString(this.valueCount).equals(valueCountString) || !"".equals(blank)) {
                throw new IOException("unexpected journal header: [" + magic + ", " + version + ", " + valueCountString + ", " + blank + "]");
            }
            lineCount = 0;
            while (true) {
                readJournalLine(reader.readLine());
                lineCount++;
            }
        } catch (EOFException e) {
            this.redundantOpCount = lineCount - this.lruEntries.size();
            if (reader.hasUnterminatedLine()) {
                rebuildJournal();
            } else {
                this.journalWriter = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(this.journalFile, true), Util.US_ASCII));
            }
            Util.closeQuietly(reader);
        } catch (Throwable th) {
            Util.closeQuietly(reader);
            throw th;
        }
    }

    private void readJournalLine(String line) throws IOException {
        String key;
        int firstSpace = line.indexOf(32);
        if (firstSpace == -1) {
            throw new IOException("unexpected journal line: " + line);
        }
        int keyBegin = firstSpace + 1;
        int secondSpace = line.indexOf(32, keyBegin);
        if (secondSpace == -1) {
            key = line.substring(keyBegin);
            if (firstSpace == REMOVE.length() && line.startsWith(REMOVE)) {
                this.lruEntries.remove(key);
                return;
            }
        } else {
            key = line.substring(keyBegin, secondSpace);
        }
        Entry entry = this.lruEntries.get(key);
        if (entry == null) {
            entry = new Entry(key);
            this.lruEntries.put(key, entry);
        }
        if (secondSpace != -1 && firstSpace == CLEAN.length() && line.startsWith(CLEAN)) {
            String[] parts = line.substring(secondSpace + 1).split(" ");
            boolean unused = entry.readable = true;
            Editor unused2 = entry.currentEditor = null;
            entry.setLengths(parts);
        } else if (secondSpace == -1 && firstSpace == DIRTY.length() && line.startsWith(DIRTY)) {
            Editor unused3 = entry.currentEditor = new Editor(entry);
        } else if (secondSpace != -1 || firstSpace != READ.length() || !line.startsWith(READ)) {
            throw new IOException("unexpected journal line: " + line);
        }
    }

    private void processJournal() throws IOException {
        deleteIfExists(this.journalFileTmp);
        Iterator<Entry> i = this.lruEntries.values().iterator();
        while (i.hasNext()) {
            Entry entry = i.next();
            if (entry.currentEditor == null) {
                for (int t = 0; t < this.valueCount; t++) {
                    this.size += entry.lengths[t];
                }
            } else {
                Editor unused = entry.currentEditor = null;
                for (int t2 = 0; t2 < this.valueCount; t2++) {
                    deleteIfExists(entry.getCleanFile(t2));
                    deleteIfExists(entry.getDirtyFile(t2));
                }
                i.remove();
            }
        }
    }

    /* access modifiers changed from: private */
    public synchronized void rebuildJournal() throws IOException {
        if (this.journalWriter != null) {
            this.journalWriter.close();
        }
        Writer writer = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(this.journalFileTmp), Util.US_ASCII));
        try {
            writer.write(MAGIC);
            writer.write("\n");
            writer.write("1");
            writer.write("\n");
            writer.write(Integer.toString(this.appVersion));
            writer.write("\n");
            writer.write(Integer.toString(this.valueCount));
            writer.write("\n");
            writer.write("\n");
            for (Entry entry : this.lruEntries.values()) {
                if (entry.currentEditor != null) {
                    writer.write("DIRTY " + entry.key + 10);
                } else {
                    writer.write("CLEAN " + entry.key + entry.getLengths() + 10);
                }
            }
            writer.close();
            if (this.journalFile.exists()) {
                renameTo(this.journalFile, this.journalFileBackup, true);
            }
            renameTo(this.journalFileTmp, this.journalFile, false);
            this.journalFileBackup.delete();
            this.journalWriter = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(this.journalFile, true), Util.US_ASCII));
        } catch (Throwable th) {
            writer.close();
            throw th;
        }
    }

    private static void deleteIfExists(File file) throws IOException {
        if (file.exists() && !file.delete()) {
            throw new IOException();
        }
    }

    private static void renameTo(File from, File to, boolean deleteDestination) throws IOException {
        if (deleteDestination) {
            deleteIfExists(to);
        }
        if (!from.renameTo(to)) {
            throw new IOException();
        }
    }

    public synchronized Value get(String key) throws IOException {
        Value value = null;
        synchronized (this) {
            checkNotClosed();
            Entry entry = this.lruEntries.get(key);
            if (entry != null) {
                if (entry.readable) {
                    File[] fileArr = entry.cleanFiles;
                    int length = fileArr.length;
                    int i = 0;
                    while (true) {
                        if (i < length) {
                            if (!fileArr[i].exists()) {
                                break;
                            }
                            i++;
                        } else {
                            this.redundantOpCount++;
                            this.journalWriter.append(READ);
                            this.journalWriter.append(' ');
                            this.journalWriter.append(key);
                            this.journalWriter.append(10);
                            if (journalRebuildRequired()) {
                                this.executorService.submit(this.cleanupCallable);
                            }
                            value = new Value(key, entry.sequenceNumber, entry.cleanFiles, entry.lengths);
                        }
                    }
                }
            }
        }
        return value;
    }

    public Editor edit(String key) throws IOException {
        return edit(key, -1);
    }

    /* access modifiers changed from: private */
    /* JADX WARNING: Code restructure failed: missing block: B:20:0x005d, code lost:
        if (com.bumptech.glide.disklrucache.DiskLruCache.Entry.access$800(r1) != null) goto L_0x001d;
     */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public synchronized com.bumptech.glide.disklrucache.DiskLruCache.Editor edit(java.lang.String r5, long r6) throws java.io.IOException {
        /*
            r4 = this;
            r0 = 0
            monitor-enter(r4)
            r4.checkNotClosed()     // Catch:{ all -> 0x0056 }
            java.util.LinkedHashMap<java.lang.String, com.bumptech.glide.disklrucache.DiskLruCache$Entry> r2 = r4.lruEntries     // Catch:{ all -> 0x0056 }
            java.lang.Object r1 = r2.get(r5)     // Catch:{ all -> 0x0056 }
            com.bumptech.glide.disklrucache.DiskLruCache$Entry r1 = (com.bumptech.glide.disklrucache.DiskLruCache.Entry) r1     // Catch:{ all -> 0x0056 }
            r2 = -1
            int r2 = (r6 > r2 ? 1 : (r6 == r2 ? 0 : -1))
            if (r2 == 0) goto L_0x001f
            if (r1 == 0) goto L_0x001d
            long r2 = r1.sequenceNumber     // Catch:{ all -> 0x0056 }
            int r2 = (r2 > r6 ? 1 : (r2 == r6 ? 0 : -1))
            if (r2 == 0) goto L_0x001f
        L_0x001d:
            monitor-exit(r4)
            return r0
        L_0x001f:
            if (r1 != 0) goto L_0x0059
            com.bumptech.glide.disklrucache.DiskLruCache$Entry r1 = new com.bumptech.glide.disklrucache.DiskLruCache$Entry     // Catch:{ all -> 0x0056 }
            r2 = 0
            r1.<init>(r5)     // Catch:{ all -> 0x0056 }
            java.util.LinkedHashMap<java.lang.String, com.bumptech.glide.disklrucache.DiskLruCache$Entry> r2 = r4.lruEntries     // Catch:{ all -> 0x0056 }
            r2.put(r5, r1)     // Catch:{ all -> 0x0056 }
        L_0x002c:
            com.bumptech.glide.disklrucache.DiskLruCache$Editor r0 = new com.bumptech.glide.disklrucache.DiskLruCache$Editor     // Catch:{ all -> 0x0056 }
            r2 = 0
            r0.<init>(r1)     // Catch:{ all -> 0x0056 }
            com.bumptech.glide.disklrucache.DiskLruCache.Editor unused = r1.currentEditor = r0     // Catch:{ all -> 0x0056 }
            java.io.Writer r2 = r4.journalWriter     // Catch:{ all -> 0x0056 }
            java.lang.String r3 = "DIRTY"
            r2.append(r3)     // Catch:{ all -> 0x0056 }
            java.io.Writer r2 = r4.journalWriter     // Catch:{ all -> 0x0056 }
            r3 = 32
            r2.append(r3)     // Catch:{ all -> 0x0056 }
            java.io.Writer r2 = r4.journalWriter     // Catch:{ all -> 0x0056 }
            r2.append(r5)     // Catch:{ all -> 0x0056 }
            java.io.Writer r2 = r4.journalWriter     // Catch:{ all -> 0x0056 }
            r3 = 10
            r2.append(r3)     // Catch:{ all -> 0x0056 }
            java.io.Writer r2 = r4.journalWriter     // Catch:{ all -> 0x0056 }
            r2.flush()     // Catch:{ all -> 0x0056 }
            goto L_0x001d
        L_0x0056:
            r2 = move-exception
            monitor-exit(r4)
            throw r2
        L_0x0059:
            com.bumptech.glide.disklrucache.DiskLruCache$Editor r2 = r1.currentEditor     // Catch:{ all -> 0x0056 }
            if (r2 == 0) goto L_0x002c
            goto L_0x001d
        */
        throw new UnsupportedOperationException("Method not decompiled: com.bumptech.glide.disklrucache.DiskLruCache.edit(java.lang.String, long):com.bumptech.glide.disklrucache.DiskLruCache$Editor");
    }

    public File getDirectory() {
        return this.directory;
    }

    public synchronized long getMaxSize() {
        return this.maxSize;
    }

    public synchronized void setMaxSize(long maxSize2) {
        this.maxSize = maxSize2;
        this.executorService.submit(this.cleanupCallable);
    }

    public synchronized long size() {
        return this.size;
    }

    /* access modifiers changed from: private */
    public synchronized void completeEdit(Editor editor, boolean success) throws IOException {
        Entry entry = editor.entry;
        if (entry.currentEditor != editor) {
            throw new IllegalStateException();
        }
        if (success) {
            if (!entry.readable) {
                int i = 0;
                while (true) {
                    if (i >= this.valueCount) {
                        break;
                    } else if (!editor.written[i]) {
                        editor.abort();
                        throw new IllegalStateException("Newly created entry didn't create value for index " + i);
                    } else if (!entry.getDirtyFile(i).exists()) {
                        editor.abort();
                        break;
                    } else {
                        i++;
                    }
                }
            }
        }
        for (int i2 = 0; i2 < this.valueCount; i2++) {
            File dirty = entry.getDirtyFile(i2);
            if (!success) {
                deleteIfExists(dirty);
            } else if (dirty.exists()) {
                File clean = entry.getCleanFile(i2);
                dirty.renameTo(clean);
                long oldLength = entry.lengths[i2];
                long newLength = clean.length();
                entry.lengths[i2] = newLength;
                this.size = (this.size - oldLength) + newLength;
            }
        }
        this.redundantOpCount++;
        Editor unused = entry.currentEditor = null;
        if (entry.readable || success) {
            boolean unused2 = entry.readable = true;
            this.journalWriter.append(CLEAN);
            this.journalWriter.append(' ');
            this.journalWriter.append(entry.key);
            this.journalWriter.append(entry.getLengths());
            this.journalWriter.append(10);
            if (success) {
                long j = this.nextSequenceNumber;
                this.nextSequenceNumber = 1 + j;
                long unused3 = entry.sequenceNumber = j;
            }
        } else {
            this.lruEntries.remove(entry.key);
            this.journalWriter.append(REMOVE);
            this.journalWriter.append(' ');
            this.journalWriter.append(entry.key);
            this.journalWriter.append(10);
        }
        this.journalWriter.flush();
        if (this.size > this.maxSize || journalRebuildRequired()) {
            this.executorService.submit(this.cleanupCallable);
        }
    }

    /* access modifiers changed from: private */
    public boolean journalRebuildRequired() {
        return this.redundantOpCount >= 2000 && this.redundantOpCount >= this.lruEntries.size();
    }

    public synchronized boolean remove(String key) throws IOException {
        boolean z;
        checkNotClosed();
        Entry entry = this.lruEntries.get(key);
        if (entry == null || entry.currentEditor != null) {
            z = false;
        } else {
            int i = 0;
            while (i < this.valueCount) {
                File file = entry.getCleanFile(i);
                if (!file.exists() || file.delete()) {
                    this.size -= entry.lengths[i];
                    entry.lengths[i] = 0;
                    i++;
                } else {
                    throw new IOException("failed to delete " + file);
                }
            }
            this.redundantOpCount++;
            this.journalWriter.append(REMOVE);
            this.journalWriter.append(' ');
            this.journalWriter.append(key);
            this.journalWriter.append(10);
            this.lruEntries.remove(key);
            if (journalRebuildRequired()) {
                this.executorService.submit(this.cleanupCallable);
            }
            z = true;
        }
        return z;
    }

    public synchronized boolean isClosed() {
        return this.journalWriter == null;
    }

    private void checkNotClosed() {
        if (this.journalWriter == null) {
            throw new IllegalStateException("cache is closed");
        }
    }

    public synchronized void flush() throws IOException {
        checkNotClosed();
        trimToSize();
        this.journalWriter.flush();
    }

    public synchronized void close() throws IOException {
        if (this.journalWriter != null) {
            Iterator it = new ArrayList(this.lruEntries.values()).iterator();
            while (it.hasNext()) {
                Entry entry = (Entry) it.next();
                if (entry.currentEditor != null) {
                    entry.currentEditor.abort();
                }
            }
            trimToSize();
            this.journalWriter.close();
            this.journalWriter = null;
        }
    }

    /* access modifiers changed from: private */
    public void trimToSize() throws IOException {
        while (this.size > this.maxSize) {
            remove(this.lruEntries.entrySet().iterator().next().getKey());
        }
    }

    public void delete() throws IOException {
        close();
        Util.deleteContents(this.directory);
    }

    /* access modifiers changed from: private */
    public static String inputStreamToString(InputStream in) throws IOException {
        return Util.readFully(new InputStreamReader(in, Util.UTF_8));
    }

    public final class Value {
        private final File[] files;
        private final String key;
        private final long[] lengths;
        private final long sequenceNumber;

        private Value(String key2, long sequenceNumber2, File[] files2, long[] lengths2) {
            this.key = key2;
            this.sequenceNumber = sequenceNumber2;
            this.files = files2;
            this.lengths = lengths2;
        }

        public Editor edit() throws IOException {
            return DiskLruCache.this.edit(this.key, this.sequenceNumber);
        }

        public File getFile(int index) {
            return this.files[index];
        }

        public String getString(int index) throws IOException {
            return DiskLruCache.inputStreamToString(new FileInputStream(this.files[index]));
        }

        public long getLength(int index) {
            return this.lengths[index];
        }
    }

    public final class Editor {
        private boolean committed;
        /* access modifiers changed from: private */
        public final Entry entry;
        /* access modifiers changed from: private */
        public final boolean[] written;

        private Editor(Entry entry2) {
            this.entry = entry2;
            this.written = entry2.readable ? null : new boolean[DiskLruCache.this.valueCount];
        }

        private InputStream newInputStream(int index) throws IOException {
            synchronized (DiskLruCache.this) {
                if (this.entry.currentEditor != this) {
                    throw new IllegalStateException();
                } else if (!this.entry.readable) {
                    return null;
                } else {
                    try {
                        FileInputStream fileInputStream = new FileInputStream(this.entry.getCleanFile(index));
                        return fileInputStream;
                    } catch (FileNotFoundException e) {
                        return null;
                    }
                }
            }
        }

        public String getString(int index) throws IOException {
            InputStream in = newInputStream(index);
            if (in != null) {
                return DiskLruCache.inputStreamToString(in);
            }
            return null;
        }

        public File getFile(int index) throws IOException {
            File dirtyFile;
            synchronized (DiskLruCache.this) {
                if (this.entry.currentEditor != this) {
                    throw new IllegalStateException();
                }
                if (!this.entry.readable) {
                    this.written[index] = true;
                }
                dirtyFile = this.entry.getDirtyFile(index);
                if (!DiskLruCache.this.directory.exists()) {
                    DiskLruCache.this.directory.mkdirs();
                }
            }
            return dirtyFile;
        }

        public void set(int index, String value) throws IOException {
            Writer writer = null;
            try {
                Writer writer2 = new OutputStreamWriter(new FileOutputStream(getFile(index)), Util.UTF_8);
                try {
                    writer2.write(value);
                    Util.closeQuietly(writer2);
                } catch (Throwable th) {
                    th = th;
                    writer = writer2;
                    Util.closeQuietly(writer);
                    throw th;
                }
            } catch (Throwable th2) {
                th = th2;
                Util.closeQuietly(writer);
                throw th;
            }
        }

        public void commit() throws IOException {
            DiskLruCache.this.completeEdit(this, true);
            this.committed = true;
        }

        public void abort() throws IOException {
            DiskLruCache.this.completeEdit(this, false);
        }

        public void abortUnlessCommitted() {
            if (!this.committed) {
                try {
                    abort();
                } catch (IOException e) {
                }
            }
        }
    }

    private final class Entry {
        File[] cleanFiles;
        /* access modifiers changed from: private */
        public Editor currentEditor;
        File[] dirtyFiles;
        /* access modifiers changed from: private */
        public final String key;
        /* access modifiers changed from: private */
        public final long[] lengths;
        /* access modifiers changed from: private */
        public boolean readable;
        /* access modifiers changed from: private */
        public long sequenceNumber;

        private Entry(String key2) {
            this.key = key2;
            this.lengths = new long[DiskLruCache.this.valueCount];
            this.cleanFiles = new File[DiskLruCache.this.valueCount];
            this.dirtyFiles = new File[DiskLruCache.this.valueCount];
            StringBuilder fileBuilder = new StringBuilder(key2).append('.');
            int truncateTo = fileBuilder.length();
            for (int i = 0; i < DiskLruCache.this.valueCount; i++) {
                fileBuilder.append(i);
                this.cleanFiles[i] = new File(DiskLruCache.this.directory, fileBuilder.toString());
                fileBuilder.append(".tmp");
                this.dirtyFiles[i] = new File(DiskLruCache.this.directory, fileBuilder.toString());
                fileBuilder.setLength(truncateTo);
            }
        }

        public String getLengths() throws IOException {
            StringBuilder result = new StringBuilder();
            for (long size : this.lengths) {
                result.append(' ').append(size);
            }
            return result.toString();
        }

        /* access modifiers changed from: private */
        public void setLengths(String[] strings) throws IOException {
            if (strings.length != DiskLruCache.this.valueCount) {
                throw invalidLengths(strings);
            }
            int i = 0;
            while (i < strings.length) {
                try {
                    this.lengths[i] = Long.parseLong(strings[i]);
                    i++;
                } catch (NumberFormatException e) {
                    throw invalidLengths(strings);
                }
            }
        }

        private IOException invalidLengths(String[] strings) throws IOException {
            throw new IOException("unexpected journal line: " + Arrays.toString(strings));
        }

        public File getCleanFile(int i) {
            return this.cleanFiles[i];
        }

        public File getDirtyFile(int i) {
            return this.dirtyFiles[i];
        }
    }

    private static final class DiskLruCacheThreadFactory implements ThreadFactory {
        private DiskLruCacheThreadFactory() {
        }

        public synchronized Thread newThread(Runnable runnable) {
            Thread result;
            result = new Thread(runnable, "glide-disk-lru-cache-thread");
            result.setPriority(1);
            return result;
        }
    }
}
