package okhttp3;

import java.io.Closeable;
import java.io.File;
import java.io.Flushable;
import java.io.IOException;
import java.security.cert.Certificate;
import java.security.cert.CertificateEncodingException;
import java.security.cert.CertificateException;
import java.security.cert.CertificateFactory;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.NoSuchElementException;
import javax.annotation.Nullable;
import okhttp3.Headers;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.internal.Util;
import okhttp3.internal.cache.CacheRequest;
import okhttp3.internal.cache.CacheStrategy;
import okhttp3.internal.cache.DiskLruCache;
import okhttp3.internal.cache.InternalCache;
import okhttp3.internal.http.HttpHeaders;
import okhttp3.internal.http.HttpMethod;
import okhttp3.internal.http.StatusLine;
import okhttp3.internal.io.FileSystem;
import okhttp3.internal.platform.Platform;
import okio.Buffer;
import okio.BufferedSink;
import okio.BufferedSource;
import okio.ByteString;
import okio.ForwardingSink;
import okio.ForwardingSource;
import okio.Okio;
import okio.Sink;
import okio.Source;

public final class Cache implements Closeable, Flushable {
    private static final int ENTRY_BODY = 1;
    private static final int ENTRY_COUNT = 2;
    private static final int ENTRY_METADATA = 0;
    private static final int VERSION = 201105;
    final DiskLruCache cache;
    private int hitCount;
    final InternalCache internalCache;
    private int networkCount;
    private int requestCount;
    int writeAbortCount;
    int writeSuccessCount;

    public Cache(File directory, long maxSize) {
        this(directory, maxSize, FileSystem.SYSTEM);
    }

    Cache(File directory, long maxSize, FileSystem fileSystem) {
        this.internalCache = new InternalCache() {
            public Response get(Request request) throws IOException {
                return Cache.this.get(request);
            }

            public CacheRequest put(Response response) throws IOException {
                return Cache.this.put(response);
            }

            public void remove(Request request) throws IOException {
                Cache.this.remove(request);
            }

            public void update(Response cached, Response network) {
                Cache.this.update(cached, network);
            }

            public void trackConditionalCacheHit() {
                Cache.this.trackConditionalCacheHit();
            }

            public void trackResponse(CacheStrategy cacheStrategy) {
                Cache.this.trackResponse(cacheStrategy);
            }
        };
        this.cache = DiskLruCache.create(fileSystem, directory, VERSION, 2, maxSize);
    }

    public static String key(HttpUrl url) {
        return ByteString.encodeUtf8(url.toString()).md5().hex();
    }

    /* access modifiers changed from: package-private */
    @Nullable
    public Response get(Request request) {
        try {
            DiskLruCache.Snapshot snapshot = this.cache.get(key(request.url()));
            if (snapshot == null) {
                return null;
            }
            try {
                Entry entry = new Entry(snapshot.getSource(0));
                Response response = entry.response(snapshot);
                if (entry.matches(request, response)) {
                    return response;
                }
                Util.closeQuietly((Closeable) response.body());
                return null;
            } catch (IOException e) {
                Util.closeQuietly((Closeable) snapshot);
                return null;
            }
        } catch (IOException e2) {
            return null;
        }
    }

    /* access modifiers changed from: package-private */
    @Nullable
    public CacheRequest put(Response response) {
        String requestMethod = response.request().method();
        if (HttpMethod.invalidatesCache(response.request().method())) {
            try {
                remove(response.request());
                return null;
            } catch (IOException e) {
                return null;
            }
        } else if (!requestMethod.equals("GET") || HttpHeaders.hasVaryAll(response)) {
            return null;
        } else {
            Entry entry = new Entry(response);
            try {
                DiskLruCache.Editor editor = this.cache.edit(key(response.request().url()));
                if (editor == null) {
                    return null;
                }
                entry.writeTo(editor);
                return new CacheRequestImpl(editor);
            } catch (IOException e2) {
                abortQuietly((DiskLruCache.Editor) null);
                return null;
            }
        }
    }

    /* access modifiers changed from: package-private */
    public void remove(Request request) throws IOException {
        this.cache.remove(key(request.url()));
    }

    /* access modifiers changed from: package-private */
    public void update(Response cached, Response network) {
        Entry entry = new Entry(network);
        try {
            DiskLruCache.Editor editor = ((CacheResponseBody) cached.body()).snapshot.edit();
            if (editor != null) {
                entry.writeTo(editor);
                editor.commit();
            }
        } catch (IOException e) {
            abortQuietly((DiskLruCache.Editor) null);
        }
    }

    private void abortQuietly(@Nullable DiskLruCache.Editor editor) {
        if (editor != null) {
            try {
                editor.abort();
            } catch (IOException e) {
            }
        }
    }

    public void initialize() throws IOException {
        this.cache.initialize();
    }

    public void delete() throws IOException {
        this.cache.delete();
    }

    public void evictAll() throws IOException {
        this.cache.evictAll();
    }

    public Iterator<String> urls() throws IOException {
        return new Iterator<String>() {
            boolean canRemove;
            final Iterator<DiskLruCache.Snapshot> delegate = Cache.this.cache.snapshots();
            @Nullable
            String nextUrl;

            public boolean hasNext() {
                if (this.nextUrl != null) {
                    return true;
                }
                this.canRemove = false;
                while (this.delegate.hasNext()) {
                    DiskLruCache.Snapshot snapshot = this.delegate.next();
                    try {
                        this.nextUrl = Okio.buffer(snapshot.getSource(0)).readUtf8LineStrict();
                        return true;
                    } catch (IOException e) {
                    } finally {
                        snapshot.close();
                    }
                }
                return false;
            }

            public String next() {
                if (!hasNext()) {
                    throw new NoSuchElementException();
                }
                String result = this.nextUrl;
                this.nextUrl = null;
                this.canRemove = true;
                return result;
            }

            public void remove() {
                if (!this.canRemove) {
                    throw new IllegalStateException("remove() before next()");
                }
                this.delegate.remove();
            }
        };
    }

    public synchronized int writeAbortCount() {
        return this.writeAbortCount;
    }

    public synchronized int writeSuccessCount() {
        return this.writeSuccessCount;
    }

    public long size() throws IOException {
        return this.cache.size();
    }

    public long maxSize() {
        return this.cache.getMaxSize();
    }

    public void flush() throws IOException {
        this.cache.flush();
    }

    public void close() throws IOException {
        this.cache.close();
    }

    public File directory() {
        return this.cache.getDirectory();
    }

    public boolean isClosed() {
        return this.cache.isClosed();
    }

    /* access modifiers changed from: package-private */
    public synchronized void trackResponse(CacheStrategy cacheStrategy) {
        this.requestCount++;
        if (cacheStrategy.networkRequest != null) {
            this.networkCount++;
        } else if (cacheStrategy.cacheResponse != null) {
            this.hitCount++;
        }
    }

    /* access modifiers changed from: package-private */
    public synchronized void trackConditionalCacheHit() {
        this.hitCount++;
    }

    public synchronized int networkCount() {
        return this.networkCount;
    }

    public synchronized int hitCount() {
        return this.hitCount;
    }

    public synchronized int requestCount() {
        return this.requestCount;
    }

    private final class CacheRequestImpl implements CacheRequest {
        private Sink body;
        private Sink cacheOut;
        boolean done;
        private final DiskLruCache.Editor editor;

        CacheRequestImpl(final DiskLruCache.Editor editor2) {
            this.editor = editor2;
            this.cacheOut = editor2.newSink(1);
            this.body = new ForwardingSink(this.cacheOut, Cache.this) {
                public void close() throws IOException {
                    synchronized (Cache.this) {
                        if (!CacheRequestImpl.this.done) {
                            CacheRequestImpl.this.done = true;
                            Cache.this.writeSuccessCount++;
                            super.close();
                            editor2.commit();
                        }
                    }
                }
            };
        }

        /* JADX WARNING: No exception handlers in catch block: Catch:{  } */
        /* Code decompiled incorrectly, please refer to instructions dump. */
        public void abort() {
            /*
                r3 = this;
                okhttp3.Cache r1 = okhttp3.Cache.this
                monitor-enter(r1)
                boolean r0 = r3.done     // Catch:{ all -> 0x0022 }
                if (r0 == 0) goto L_0x0009
                monitor-exit(r1)     // Catch:{ all -> 0x0022 }
            L_0x0008:
                return
            L_0x0009:
                r0 = 1
                r3.done = r0     // Catch:{ all -> 0x0022 }
                okhttp3.Cache r0 = okhttp3.Cache.this     // Catch:{ all -> 0x0022 }
                int r2 = r0.writeAbortCount     // Catch:{ all -> 0x0022 }
                int r2 = r2 + 1
                r0.writeAbortCount = r2     // Catch:{ all -> 0x0022 }
                monitor-exit(r1)     // Catch:{ all -> 0x0022 }
                okio.Sink r0 = r3.cacheOut
                okhttp3.internal.Util.closeQuietly((java.io.Closeable) r0)
                okhttp3.internal.cache.DiskLruCache$Editor r0 = r3.editor     // Catch:{ IOException -> 0x0020 }
                r0.abort()     // Catch:{ IOException -> 0x0020 }
                goto L_0x0008
            L_0x0020:
                r0 = move-exception
                goto L_0x0008
            L_0x0022:
                r0 = move-exception
                monitor-exit(r1)     // Catch:{ all -> 0x0022 }
                throw r0
            */
            throw new UnsupportedOperationException("Method not decompiled: okhttp3.Cache.CacheRequestImpl.abort():void");
        }

        public Sink body() {
            return this.body;
        }
    }

    private static final class Entry {
        private static final String RECEIVED_MILLIS = (Platform.get().getPrefix() + "-Received-Millis");
        private static final String SENT_MILLIS = (Platform.get().getPrefix() + "-Sent-Millis");
        private final int code;
        @Nullable
        private final Handshake handshake;
        private final String message;
        private final Protocol protocol;
        private final long receivedResponseMillis;
        private final String requestMethod;
        private final Headers responseHeaders;
        private final long sentRequestMillis;
        private final String url;
        private final Headers varyHeaders;

        Entry(Source in) throws IOException {
            long j;
            long j2;
            TlsVersion tlsVersion;
            try {
                BufferedSource source = Okio.buffer(in);
                this.url = source.readUtf8LineStrict();
                this.requestMethod = source.readUtf8LineStrict();
                Headers.Builder varyHeadersBuilder = new Headers.Builder();
                int varyRequestHeaderLineCount = Cache.readInt(source);
                for (int i = 0; i < varyRequestHeaderLineCount; i++) {
                    varyHeadersBuilder.addLenient(source.readUtf8LineStrict());
                }
                this.varyHeaders = varyHeadersBuilder.build();
                StatusLine statusLine = StatusLine.parse(source.readUtf8LineStrict());
                this.protocol = statusLine.protocol;
                this.code = statusLine.code;
                this.message = statusLine.message;
                Headers.Builder responseHeadersBuilder = new Headers.Builder();
                int responseHeaderLineCount = Cache.readInt(source);
                for (int i2 = 0; i2 < responseHeaderLineCount; i2++) {
                    responseHeadersBuilder.addLenient(source.readUtf8LineStrict());
                }
                String sendRequestMillisString = responseHeadersBuilder.get(SENT_MILLIS);
                String receivedResponseMillisString = responseHeadersBuilder.get(RECEIVED_MILLIS);
                responseHeadersBuilder.removeAll(SENT_MILLIS);
                responseHeadersBuilder.removeAll(RECEIVED_MILLIS);
                if (sendRequestMillisString != null) {
                    j = Long.parseLong(sendRequestMillisString);
                } else {
                    j = 0;
                }
                this.sentRequestMillis = j;
                if (receivedResponseMillisString != null) {
                    j2 = Long.parseLong(receivedResponseMillisString);
                } else {
                    j2 = 0;
                }
                this.receivedResponseMillis = j2;
                this.responseHeaders = responseHeadersBuilder.build();
                if (isHttps()) {
                    String blank = source.readUtf8LineStrict();
                    if (blank.length() > 0) {
                        throw new IOException("expected \"\" but was \"" + blank + "\"");
                    }
                    CipherSuite cipherSuite = CipherSuite.forJavaName(source.readUtf8LineStrict());
                    List<Certificate> peerCertificates = readCertificateList(source);
                    List<Certificate> localCertificates = readCertificateList(source);
                    if (!source.exhausted()) {
                        tlsVersion = TlsVersion.forJavaName(source.readUtf8LineStrict());
                    } else {
                        tlsVersion = TlsVersion.SSL_3_0;
                    }
                    this.handshake = Handshake.get(tlsVersion, cipherSuite, peerCertificates, localCertificates);
                } else {
                    this.handshake = null;
                }
            } finally {
                in.close();
            }
        }

        Entry(Response response) {
            this.url = response.request().url().toString();
            this.varyHeaders = HttpHeaders.varyHeaders(response);
            this.requestMethod = response.request().method();
            this.protocol = response.protocol();
            this.code = response.code();
            this.message = response.message();
            this.responseHeaders = response.headers();
            this.handshake = response.handshake();
            this.sentRequestMillis = response.sentRequestAtMillis();
            this.receivedResponseMillis = response.receivedResponseAtMillis();
        }

        public void writeTo(DiskLruCache.Editor editor) throws IOException {
            BufferedSink sink = Okio.buffer(editor.newSink(0));
            sink.writeUtf8(this.url).writeByte(10);
            sink.writeUtf8(this.requestMethod).writeByte(10);
            sink.writeDecimalLong((long) this.varyHeaders.size()).writeByte(10);
            int size = this.varyHeaders.size();
            for (int i = 0; i < size; i++) {
                sink.writeUtf8(this.varyHeaders.name(i)).writeUtf8(": ").writeUtf8(this.varyHeaders.value(i)).writeByte(10);
            }
            sink.writeUtf8(new StatusLine(this.protocol, this.code, this.message).toString()).writeByte(10);
            sink.writeDecimalLong((long) (this.responseHeaders.size() + 2)).writeByte(10);
            int size2 = this.responseHeaders.size();
            for (int i2 = 0; i2 < size2; i2++) {
                sink.writeUtf8(this.responseHeaders.name(i2)).writeUtf8(": ").writeUtf8(this.responseHeaders.value(i2)).writeByte(10);
            }
            sink.writeUtf8(SENT_MILLIS).writeUtf8(": ").writeDecimalLong(this.sentRequestMillis).writeByte(10);
            sink.writeUtf8(RECEIVED_MILLIS).writeUtf8(": ").writeDecimalLong(this.receivedResponseMillis).writeByte(10);
            if (isHttps()) {
                sink.writeByte(10);
                sink.writeUtf8(this.handshake.cipherSuite().javaName()).writeByte(10);
                writeCertList(sink, this.handshake.peerCertificates());
                writeCertList(sink, this.handshake.localCertificates());
                sink.writeUtf8(this.handshake.tlsVersion().javaName()).writeByte(10);
            }
            sink.close();
        }

        private boolean isHttps() {
            return this.url.startsWith("https://");
        }

        private List<Certificate> readCertificateList(BufferedSource source) throws IOException {
            int length = Cache.readInt(source);
            if (length == -1) {
                return Collections.emptyList();
            }
            try {
                CertificateFactory certificateFactory = CertificateFactory.getInstance("X.509");
                List<Certificate> result = new ArrayList<>(length);
                for (int i = 0; i < length; i++) {
                    String line = source.readUtf8LineStrict();
                    Buffer bytes = new Buffer();
                    bytes.write(ByteString.decodeBase64(line));
                    result.add(certificateFactory.generateCertificate(bytes.inputStream()));
                }
                return result;
            } catch (CertificateException e) {
                throw new IOException(e.getMessage());
            }
        }

        private void writeCertList(BufferedSink sink, List<Certificate> certificates) throws IOException {
            try {
                sink.writeDecimalLong((long) certificates.size()).writeByte(10);
                int size = certificates.size();
                for (int i = 0; i < size; i++) {
                    sink.writeUtf8(ByteString.of(certificates.get(i).getEncoded()).base64()).writeByte(10);
                }
            } catch (CertificateEncodingException e) {
                throw new IOException(e.getMessage());
            }
        }

        public boolean matches(Request request, Response response) {
            return this.url.equals(request.url().toString()) && this.requestMethod.equals(request.method()) && HttpHeaders.varyMatches(response, this.varyHeaders, request);
        }

        public Response response(DiskLruCache.Snapshot snapshot) {
            String contentType = this.responseHeaders.get("Content-Type");
            String contentLength = this.responseHeaders.get("Content-Length");
            return new Response.Builder().request(new Request.Builder().url(this.url).method(this.requestMethod, (RequestBody) null).headers(this.varyHeaders).build()).protocol(this.protocol).code(this.code).message(this.message).headers(this.responseHeaders).body(new CacheResponseBody(snapshot, contentType, contentLength)).handshake(this.handshake).sentRequestAtMillis(this.sentRequestMillis).receivedResponseAtMillis(this.receivedResponseMillis).build();
        }
    }

    static int readInt(BufferedSource source) throws IOException {
        try {
            long result = source.readDecimalLong();
            String line = source.readUtf8LineStrict();
            if (result >= 0 && result <= 2147483647L && line.isEmpty()) {
                return (int) result;
            }
            throw new IOException("expected an int but was \"" + result + line + "\"");
        } catch (NumberFormatException e) {
            throw new IOException(e.getMessage());
        }
    }

    private static class CacheResponseBody extends ResponseBody {
        private final BufferedSource bodySource;
        @Nullable
        private final String contentLength;
        @Nullable
        private final String contentType;
        final DiskLruCache.Snapshot snapshot;

        CacheResponseBody(final DiskLruCache.Snapshot snapshot2, String contentType2, String contentLength2) {
            this.snapshot = snapshot2;
            this.contentType = contentType2;
            this.contentLength = contentLength2;
            this.bodySource = Okio.buffer((Source) new ForwardingSource(snapshot2.getSource(1)) {
                public void close() throws IOException {
                    snapshot2.close();
                    super.close();
                }
            });
        }

        public MediaType contentType() {
            if (this.contentType != null) {
                return MediaType.parse(this.contentType);
            }
            return null;
        }

        public long contentLength() {
            try {
                if (this.contentLength != null) {
                    return Long.parseLong(this.contentLength);
                }
                return -1;
            } catch (NumberFormatException e) {
                return -1;
            }
        }

        public BufferedSource source() {
            return this.bodySource;
        }
    }
}
