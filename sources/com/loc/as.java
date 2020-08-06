package com.loc;

import java.io.BufferedWriter;
import java.io.Closeable;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FilterOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
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
import java.util.concurrent.atomic.AtomicInteger;
import java.util.regex.Pattern;

/* compiled from: DiskLruCache */
public final class as implements Closeable {
    static final Pattern a = Pattern.compile("[a-z0-9_-]{1,120}");
    static ThreadPoolExecutor b = new ThreadPoolExecutor(0, 1, 60, TimeUnit.SECONDS, new LinkedBlockingQueue(), p);
    private static final ThreadFactory p = new ThreadFactory() {
        private final AtomicInteger a = new AtomicInteger(1);

        public final Thread newThread(Runnable runnable) {
            return new Thread(runnable, "disklrucache#" + this.a.getAndIncrement());
        }
    };
    /* access modifiers changed from: private */
    public static final OutputStream r = new OutputStream() {
        public final void write(int i) throws IOException {
        }
    };
    /* access modifiers changed from: private */
    public final File c;
    private final File d;
    private final File e;
    private final File f;
    private final int g;
    private long h;
    /* access modifiers changed from: private */
    public final int i;
    private long j = 0;
    /* access modifiers changed from: private */
    public Writer k;
    private int l = 1000;
    private final LinkedHashMap<String, c> m = new LinkedHashMap<>(0, 0.75f, true);
    /* access modifiers changed from: private */
    public int n;
    private long o = 0;
    private final Callable<Void> q = new Callable<Void>() {
        /* access modifiers changed from: private */
        /* renamed from: a */
        public Void call() throws Exception {
            synchronized (as.this) {
                if (as.this.k != null) {
                    as.this.l();
                    if (as.this.j()) {
                        as.this.i();
                        int unused = as.this.n = 0;
                    }
                }
            }
            return null;
        }
    };

    /* compiled from: DiskLruCache */
    public final class a {
        /* access modifiers changed from: private */
        public final c b;
        /* access modifiers changed from: private */
        public final boolean[] c;
        /* access modifiers changed from: private */
        public boolean d;
        private boolean e;

        /* renamed from: com.loc.as$a$a  reason: collision with other inner class name */
        /* compiled from: DiskLruCache */
        private class C0001a extends FilterOutputStream {
            private C0001a(OutputStream outputStream) {
                super(outputStream);
            }

            /* synthetic */ C0001a(a aVar, OutputStream outputStream, byte b) {
                this(outputStream);
            }

            public final void close() {
                try {
                    this.out.close();
                } catch (IOException e) {
                    boolean unused = a.this.d = true;
                }
            }

            public final void flush() {
                try {
                    this.out.flush();
                } catch (IOException e) {
                    boolean unused = a.this.d = true;
                }
            }

            public final void write(int i) {
                try {
                    this.out.write(i);
                } catch (IOException e) {
                    boolean unused = a.this.d = true;
                }
            }

            public final void write(byte[] bArr, int i, int i2) {
                try {
                    this.out.write(bArr, i, i2);
                } catch (IOException e) {
                    boolean unused = a.this.d = true;
                }
            }
        }

        private a(c cVar) {
            this.b = cVar;
            this.c = cVar.d ? null : new boolean[as.this.i];
        }

        /* synthetic */ a(as asVar, c cVar, byte b2) {
            this(cVar);
        }

        public final OutputStream a() throws IOException {
            OutputStream e2;
            FileOutputStream fileOutputStream;
            if (as.this.i <= 0) {
                throw new IllegalArgumentException("Expected index 0 to be greater than 0 and less than the maximum value count of " + as.this.i);
            }
            synchronized (as.this) {
                if (this.b.e != this) {
                    throw new IllegalStateException();
                }
                if (!this.b.d) {
                    this.c[0] = true;
                }
                File b2 = this.b.b(0);
                try {
                    fileOutputStream = new FileOutputStream(b2);
                } catch (FileNotFoundException e3) {
                    as.this.c.mkdirs();
                    try {
                        fileOutputStream = new FileOutputStream(b2);
                    } catch (FileNotFoundException e4) {
                        e2 = as.r;
                    }
                }
                e2 = new C0001a(this, fileOutputStream, (byte) 0);
            }
            return e2;
        }

        public final void b() throws IOException {
            if (this.d) {
                as.this.a(this, false);
                as.this.c(this.b.b);
            } else {
                as.this.a(this, true);
            }
            this.e = true;
        }

        public final void c() throws IOException {
            as.this.a(this, false);
        }
    }

    /* compiled from: DiskLruCache */
    public final class b implements Closeable {
        private final String b;
        private final long c;
        private final InputStream[] d;
        private final long[] e;

        private b(String str, long j, InputStream[] inputStreamArr, long[] jArr) {
            this.b = str;
            this.c = j;
            this.d = inputStreamArr;
            this.e = jArr;
        }

        /* synthetic */ b(as asVar, String str, long j, InputStream[] inputStreamArr, long[] jArr, byte b2) {
            this(str, j, inputStreamArr, jArr);
        }

        public final InputStream a() {
            return this.d[0];
        }

        public final void close() {
            for (InputStream a2 : this.d) {
                au.a((Closeable) a2);
            }
        }
    }

    /* compiled from: DiskLruCache */
    private final class c {
        /* access modifiers changed from: private */
        public final String b;
        /* access modifiers changed from: private */
        public final long[] c;
        /* access modifiers changed from: private */
        public boolean d;
        /* access modifiers changed from: private */
        public a e;
        /* access modifiers changed from: private */
        public long f;

        private c(String str) {
            this.b = str;
            this.c = new long[as.this.i];
        }

        /* synthetic */ c(as asVar, String str, byte b2) {
            this(str);
        }

        private static IOException a(String[] strArr) throws IOException {
            throw new IOException("unexpected journal line: " + Arrays.toString(strArr));
        }

        static /* synthetic */ void a(c cVar, String[] strArr) throws IOException {
            if (strArr.length != as.this.i) {
                throw a(strArr);
            }
            int i = 0;
            while (i < strArr.length) {
                try {
                    cVar.c[i] = Long.parseLong(strArr[i]);
                    i++;
                } catch (NumberFormatException e2) {
                    throw a(strArr);
                }
            }
        }

        public final File a(int i) {
            return new File(as.this.c, this.b + "." + i);
        }

        public final String a() throws IOException {
            StringBuilder sb = new StringBuilder();
            for (long append : this.c) {
                sb.append(' ').append(append);
            }
            return sb.toString();
        }

        public final File b(int i) {
            return new File(as.this.c, this.b + "." + i + ".tmp");
        }
    }

    private as(File file, long j2) {
        this.c = file;
        this.g = 1;
        this.d = new File(file, "journal");
        this.e = new File(file, "journal.tmp");
        this.f = new File(file, "journal.bkp");
        this.i = 1;
        this.h = j2;
    }

    public static as a(File file, long j2) throws IOException {
        if (j2 <= 0) {
            throw new IllegalArgumentException("maxSize <= 0");
        }
        File file2 = new File(file, "journal.bkp");
        if (file2.exists()) {
            File file3 = new File(file, "journal");
            if (file3.exists()) {
                file2.delete();
            } else {
                a(file2, file3, false);
            }
        }
        as asVar = new as(file, j2);
        if (asVar.d.exists()) {
            try {
                asVar.g();
                asVar.h();
                asVar.k = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(asVar.d, true), au.a));
                return asVar;
            } catch (Throwable th) {
                asVar.d();
            }
        }
        file.mkdirs();
        as asVar2 = new as(file, j2);
        asVar2.i();
        return asVar2;
    }

    public static void a() {
        if (b != null && !b.isShutdown()) {
            b.shutdown();
        }
    }

    /* access modifiers changed from: private */
    public synchronized void a(a aVar, boolean z) throws IOException {
        synchronized (this) {
            c a2 = aVar.b;
            if (a2.e != aVar) {
                throw new IllegalStateException();
            }
            if (z) {
                if (!a2.d) {
                    int i2 = 0;
                    while (true) {
                        if (i2 < this.i) {
                            if (!aVar.c[i2]) {
                                aVar.c();
                                throw new IllegalStateException("Newly created entry didn't create value for index " + i2);
                            } else if (!a2.b(i2).exists()) {
                                aVar.c();
                                break;
                            } else {
                                i2++;
                            }
                        }
                    }
                }
            }
            for (int i3 = 0; i3 < this.i; i3++) {
                File b2 = a2.b(i3);
                if (!z) {
                    a(b2);
                } else if (b2.exists()) {
                    File a3 = a2.a(i3);
                    b2.renameTo(a3);
                    long j2 = a2.c[i3];
                    long length = a3.length();
                    a2.c[i3] = length;
                    this.j = (this.j - j2) + length;
                }
            }
            this.n++;
            a unused = a2.e = null;
            if (a2.d || z) {
                boolean unused2 = a2.d = true;
                this.k.write("CLEAN " + a2.b + a2.a() + 10);
                if (z) {
                    long j3 = this.o;
                    this.o = 1 + j3;
                    long unused3 = a2.f = j3;
                }
            } else {
                this.m.remove(a2.b);
                this.k.write("REMOVE " + a2.b + 10);
            }
            this.k.flush();
            if (this.j > this.h || j()) {
                f().submit(this.q);
            }
        }
    }

    private static void a(File file) throws IOException {
        if (file.exists() && !file.delete()) {
            throw new IOException();
        }
    }

    private static void a(File file, File file2, boolean z) throws IOException {
        if (z) {
            a(file2);
        }
        if (!file.renameTo(file2)) {
            throw new IOException();
        }
    }

    private synchronized a d(String str) throws IOException {
        c cVar;
        a aVar;
        k();
        e(str);
        c cVar2 = this.m.get(str);
        if (-1 == -1 || (cVar2 != null && cVar2.f == -1)) {
            if (cVar2 == null) {
                c cVar3 = new c(this, str, (byte) 0);
                this.m.put(str, cVar3);
                cVar = cVar3;
            } else if (cVar2.e != null) {
                aVar = null;
            } else {
                cVar = cVar2;
            }
            aVar = new a(this, cVar, (byte) 0);
            a unused = cVar.e = aVar;
            this.k.write("DIRTY " + str + 10);
            this.k.flush();
        } else {
            aVar = null;
        }
        return aVar;
    }

    private static void e(String str) {
        if (!a.matcher(str).matches()) {
            throw new IllegalArgumentException("keys must match regex [a-z0-9_-]{1,120}: \"" + str + "\"");
        }
    }

    private static ThreadPoolExecutor f() {
        try {
            if (b == null || b.isShutdown()) {
                b = new ThreadPoolExecutor(0, 1, 60, TimeUnit.SECONDS, new LinkedBlockingQueue(256), p);
            }
        } catch (Throwable th) {
            th.printStackTrace();
        }
        return b;
    }

    /* JADX WARNING: Removed duplicated region for block: B:41:0x00ff A[Catch:{ EOFException -> 0x00be, all -> 0x0096 }] */
    /* JADX WARNING: Removed duplicated region for block: B:57:0x0154 A[Catch:{ EOFException -> 0x00be, all -> 0x0096 }, EDGE_INSN: B:60:0x0154->B:57:0x0154 ?: BREAK  
    EDGE_INSN: B:61:0x0154->B:57:0x0154 ?: BREAK  
    EDGE_INSN: B:62:0x0154->B:57:0x0154 ?: BREAK  ] */
    /* JADX WARNING: Removed duplicated region for block: B:67:0x00ec A[SYNTHETIC] */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    private void g() throws java.io.IOException {
        /*
            r10 = this;
            r9 = 5
            r0 = 0
            r8 = -1
            com.loc.at r3 = new com.loc.at
            java.io.FileInputStream r1 = new java.io.FileInputStream
            java.io.File r2 = r10.d
            r1.<init>(r2)
            java.nio.charset.Charset r2 = com.loc.au.a
            r3.<init>(r1, r2)
            java.lang.String r1 = r3.a()     // Catch:{ all -> 0x0096 }
            java.lang.String r2 = r3.a()     // Catch:{ all -> 0x0096 }
            java.lang.String r4 = r3.a()     // Catch:{ all -> 0x0096 }
            java.lang.String r5 = r3.a()     // Catch:{ all -> 0x0096 }
            java.lang.String r6 = r3.a()     // Catch:{ all -> 0x0096 }
            java.lang.String r7 = "libcore.io.DiskLruCache"
            boolean r7 = r7.equals(r1)     // Catch:{ all -> 0x0096 }
            if (r7 == 0) goto L_0x0058
            java.lang.String r7 = "1"
            boolean r7 = r7.equals(r2)     // Catch:{ all -> 0x0096 }
            if (r7 == 0) goto L_0x0058
            int r7 = r10.g     // Catch:{ all -> 0x0096 }
            java.lang.String r7 = java.lang.Integer.toString(r7)     // Catch:{ all -> 0x0096 }
            boolean r4 = r7.equals(r4)     // Catch:{ all -> 0x0096 }
            if (r4 == 0) goto L_0x0058
            int r4 = r10.i     // Catch:{ all -> 0x0096 }
            java.lang.String r4 = java.lang.Integer.toString(r4)     // Catch:{ all -> 0x0096 }
            boolean r4 = r4.equals(r5)     // Catch:{ all -> 0x0096 }
            if (r4 == 0) goto L_0x0058
            java.lang.String r4 = ""
            boolean r4 = r4.equals(r6)     // Catch:{ all -> 0x0096 }
            if (r4 != 0) goto L_0x009b
        L_0x0058:
            java.io.IOException r0 = new java.io.IOException     // Catch:{ all -> 0x0096 }
            java.lang.StringBuilder r4 = new java.lang.StringBuilder     // Catch:{ all -> 0x0096 }
            java.lang.String r7 = "unexpected journal header: ["
            r4.<init>(r7)     // Catch:{ all -> 0x0096 }
            java.lang.StringBuilder r1 = r4.append(r1)     // Catch:{ all -> 0x0096 }
            java.lang.String r4 = ", "
            java.lang.StringBuilder r1 = r1.append(r4)     // Catch:{ all -> 0x0096 }
            java.lang.StringBuilder r1 = r1.append(r2)     // Catch:{ all -> 0x0096 }
            java.lang.String r2 = ", "
            java.lang.StringBuilder r1 = r1.append(r2)     // Catch:{ all -> 0x0096 }
            java.lang.StringBuilder r1 = r1.append(r5)     // Catch:{ all -> 0x0096 }
            java.lang.String r2 = ", "
            java.lang.StringBuilder r1 = r1.append(r2)     // Catch:{ all -> 0x0096 }
            java.lang.StringBuilder r1 = r1.append(r6)     // Catch:{ all -> 0x0096 }
            java.lang.String r2 = "]"
            java.lang.StringBuilder r1 = r1.append(r2)     // Catch:{ all -> 0x0096 }
            java.lang.String r1 = r1.toString()     // Catch:{ all -> 0x0096 }
            r0.<init>(r1)     // Catch:{ all -> 0x0096 }
            throw r0     // Catch:{ all -> 0x0096 }
        L_0x0096:
            r0 = move-exception
            com.loc.au.a((java.io.Closeable) r3)
            throw r0
        L_0x009b:
            r1 = r0
        L_0x009c:
            java.lang.String r4 = r3.a()     // Catch:{ EOFException -> 0x00be }
            r0 = 32
            int r5 = r4.indexOf(r0)     // Catch:{ EOFException -> 0x00be }
            if (r5 != r8) goto L_0x00cd
            java.io.IOException r0 = new java.io.IOException     // Catch:{ EOFException -> 0x00be }
            java.lang.StringBuilder r2 = new java.lang.StringBuilder     // Catch:{ EOFException -> 0x00be }
            java.lang.String r5 = "unexpected journal line: "
            r2.<init>(r5)     // Catch:{ EOFException -> 0x00be }
            java.lang.StringBuilder r2 = r2.append(r4)     // Catch:{ EOFException -> 0x00be }
            java.lang.String r2 = r2.toString()     // Catch:{ EOFException -> 0x00be }
            r0.<init>(r2)     // Catch:{ EOFException -> 0x00be }
            throw r0     // Catch:{ EOFException -> 0x00be }
        L_0x00be:
            r0 = move-exception
            java.util.LinkedHashMap<java.lang.String, com.loc.as$c> r0 = r10.m     // Catch:{ all -> 0x0096 }
            int r0 = r0.size()     // Catch:{ all -> 0x0096 }
            int r0 = r1 - r0
            r10.n = r0     // Catch:{ all -> 0x0096 }
            com.loc.au.a((java.io.Closeable) r3)
            return
        L_0x00cd:
            int r0 = r5 + 1
            r2 = 32
            int r6 = r4.indexOf(r2, r0)     // Catch:{ EOFException -> 0x00be }
            if (r6 != r8) goto L_0x00f0
            java.lang.String r0 = r4.substring(r0)     // Catch:{ EOFException -> 0x00be }
            r2 = 6
            if (r5 != r2) goto L_0x016a
            java.lang.String r2 = "REMOVE"
            boolean r2 = r4.startsWith(r2)     // Catch:{ EOFException -> 0x00be }
            if (r2 == 0) goto L_0x016a
            java.util.LinkedHashMap<java.lang.String, com.loc.as$c> r2 = r10.m     // Catch:{ EOFException -> 0x00be }
            r2.remove(r0)     // Catch:{ EOFException -> 0x00be }
        L_0x00ec:
            int r0 = r1 + 1
            r1 = r0
            goto L_0x009c
        L_0x00f0:
            java.lang.String r0 = r4.substring(r0, r6)     // Catch:{ EOFException -> 0x00be }
            r2 = r0
        L_0x00f5:
            java.util.LinkedHashMap<java.lang.String, com.loc.as$c> r0 = r10.m     // Catch:{ EOFException -> 0x00be }
            java.lang.Object r0 = r0.get(r2)     // Catch:{ EOFException -> 0x00be }
            com.loc.as$c r0 = (com.loc.as.c) r0     // Catch:{ EOFException -> 0x00be }
            if (r0 != 0) goto L_0x010a
            com.loc.as$c r0 = new com.loc.as$c     // Catch:{ EOFException -> 0x00be }
            r7 = 0
            r0.<init>(r10, r2, r7)     // Catch:{ EOFException -> 0x00be }
            java.util.LinkedHashMap<java.lang.String, com.loc.as$c> r7 = r10.m     // Catch:{ EOFException -> 0x00be }
            r7.put(r2, r0)     // Catch:{ EOFException -> 0x00be }
        L_0x010a:
            if (r6 == r8) goto L_0x012f
            if (r5 != r9) goto L_0x012f
            java.lang.String r2 = "CLEAN"
            boolean r2 = r4.startsWith(r2)     // Catch:{ EOFException -> 0x00be }
            if (r2 == 0) goto L_0x012f
            int r2 = r6 + 1
            java.lang.String r2 = r4.substring(r2)     // Catch:{ EOFException -> 0x00be }
            java.lang.String r4 = " "
            java.lang.String[] r2 = r2.split(r4)     // Catch:{ EOFException -> 0x00be }
            boolean unused = r0.d = true     // Catch:{ EOFException -> 0x00be }
            r4 = 0
            com.loc.as.a unused = r0.e = r4     // Catch:{ EOFException -> 0x00be }
            com.loc.as.c.a((com.loc.as.c) r0, (java.lang.String[]) r2)     // Catch:{ EOFException -> 0x00be }
            goto L_0x00ec
        L_0x012f:
            if (r6 != r8) goto L_0x0146
            if (r5 != r9) goto L_0x0146
            java.lang.String r2 = "DIRTY"
            boolean r2 = r4.startsWith(r2)     // Catch:{ EOFException -> 0x00be }
            if (r2 == 0) goto L_0x0146
            com.loc.as$a r2 = new com.loc.as$a     // Catch:{ EOFException -> 0x00be }
            r4 = 0
            r2.<init>(r10, r0, r4)     // Catch:{ EOFException -> 0x00be }
            com.loc.as.a unused = r0.e = r2     // Catch:{ EOFException -> 0x00be }
            goto L_0x00ec
        L_0x0146:
            if (r6 != r8) goto L_0x0154
            r0 = 4
            if (r5 != r0) goto L_0x0154
            java.lang.String r0 = "READ"
            boolean r0 = r4.startsWith(r0)     // Catch:{ EOFException -> 0x00be }
            if (r0 != 0) goto L_0x00ec
        L_0x0154:
            java.io.IOException r0 = new java.io.IOException     // Catch:{ EOFException -> 0x00be }
            java.lang.StringBuilder r2 = new java.lang.StringBuilder     // Catch:{ EOFException -> 0x00be }
            java.lang.String r5 = "unexpected journal line: "
            r2.<init>(r5)     // Catch:{ EOFException -> 0x00be }
            java.lang.StringBuilder r2 = r2.append(r4)     // Catch:{ EOFException -> 0x00be }
            java.lang.String r2 = r2.toString()     // Catch:{ EOFException -> 0x00be }
            r0.<init>(r2)     // Catch:{ EOFException -> 0x00be }
            throw r0     // Catch:{ EOFException -> 0x00be }
        L_0x016a:
            r2 = r0
            goto L_0x00f5
        */
        throw new UnsupportedOperationException("Method not decompiled: com.loc.as.g():void");
    }

    private void h() throws IOException {
        a(this.e);
        Iterator<c> it = this.m.values().iterator();
        while (it.hasNext()) {
            c next = it.next();
            if (next.e == null) {
                for (int i2 = 0; i2 < this.i; i2++) {
                    this.j += next.c[i2];
                }
            } else {
                a unused = next.e = null;
                for (int i3 = 0; i3 < this.i; i3++) {
                    a(next.a(i3));
                    a(next.b(i3));
                }
                it.remove();
            }
        }
    }

    /* access modifiers changed from: private */
    public synchronized void i() throws IOException {
        if (this.k != null) {
            this.k.close();
        }
        BufferedWriter bufferedWriter = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(this.e), au.a));
        try {
            bufferedWriter.write("libcore.io.DiskLruCache");
            bufferedWriter.write("\n");
            bufferedWriter.write("1");
            bufferedWriter.write("\n");
            bufferedWriter.write(Integer.toString(this.g));
            bufferedWriter.write("\n");
            bufferedWriter.write(Integer.toString(this.i));
            bufferedWriter.write("\n");
            bufferedWriter.write("\n");
            for (c next : this.m.values()) {
                if (next.e != null) {
                    bufferedWriter.write("DIRTY " + next.b + 10);
                } else {
                    bufferedWriter.write("CLEAN " + next.b + next.a() + 10);
                }
            }
            bufferedWriter.close();
            if (this.d.exists()) {
                a(this.d, this.f, true);
            }
            a(this.e, this.d, false);
            this.f.delete();
            this.k = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(this.d, true), au.a));
        } catch (Throwable th) {
            bufferedWriter.close();
            throw th;
        }
    }

    /* access modifiers changed from: private */
    public boolean j() {
        return this.n >= 2000 && this.n >= this.m.size();
    }

    private void k() {
        if (this.k == null) {
            throw new IllegalStateException("cache is closed");
        }
    }

    /* access modifiers changed from: private */
    public void l() throws IOException {
        while (true) {
            if (this.j > this.h || this.m.size() > this.l) {
                c((String) this.m.entrySet().iterator().next().getKey());
            } else {
                return;
            }
        }
    }

    public final synchronized b a(String str) throws IOException {
        b bVar = null;
        synchronized (this) {
            k();
            e(str);
            c cVar = this.m.get(str);
            if (cVar != null) {
                if (cVar.d) {
                    InputStream[] inputStreamArr = new InputStream[this.i];
                    int i2 = 0;
                    while (i2 < this.i) {
                        try {
                            inputStreamArr[i2] = new FileInputStream(cVar.a(i2));
                            i2++;
                        } catch (FileNotFoundException e2) {
                            int i3 = 0;
                            while (i3 < this.i && inputStreamArr[i3] != null) {
                                au.a((Closeable) inputStreamArr[i3]);
                                i3++;
                            }
                        }
                    }
                    this.n++;
                    this.k.append("READ " + str + 10);
                    if (j()) {
                        f().submit(this.q);
                    }
                    bVar = new b(this, str, cVar.f, inputStreamArr, cVar.c, (byte) 0);
                }
            }
        }
        return bVar;
    }

    public final void a(int i2) {
        if (i2 < 10) {
            i2 = 10;
        } else if (i2 > 10000) {
            i2 = 10000;
        }
        this.l = i2;
    }

    public final a b(String str) throws IOException {
        return d(str);
    }

    public final File b() {
        return this.c;
    }

    public final synchronized void c() throws IOException {
        k();
        l();
        this.k.flush();
    }

    public final synchronized boolean c(String str) throws IOException {
        boolean z;
        int i2 = 0;
        synchronized (this) {
            k();
            e(str);
            c cVar = this.m.get(str);
            if (cVar == null || cVar.e != null) {
                z = false;
            } else {
                while (i2 < this.i) {
                    File a2 = cVar.a(i2);
                    if (!a2.exists() || a2.delete()) {
                        this.j -= cVar.c[i2];
                        cVar.c[i2] = 0;
                        i2++;
                    } else {
                        throw new IOException("failed to delete " + a2);
                    }
                }
                this.n++;
                this.k.append("REMOVE " + str + 10);
                this.m.remove(str);
                if (j()) {
                    f().submit(this.q);
                }
                z = true;
            }
        }
        return z;
    }

    public final synchronized void close() throws IOException {
        if (this.k != null) {
            Iterator it = new ArrayList(this.m.values()).iterator();
            while (it.hasNext()) {
                c cVar = (c) it.next();
                if (cVar.e != null) {
                    cVar.e.c();
                }
            }
            l();
            this.k.close();
            this.k = null;
        }
    }

    public final void d() throws IOException {
        close();
        au.a(this.c);
    }
}
