package com.nostra13.universalimageloader.core.download;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import com.nostra13.universalimageloader.core.assist.ContentLengthInputStream;
import com.nostra13.universalimageloader.core.download.ImageDownloader;
import com.nostra13.universalimageloader.utils.IoUtils;
import java.io.BufferedInputStream;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

public class BaseImageDownloader implements ImageDownloader {
    protected static final String ALLOWED_URI_CHARS = "@#&=*+-_.,:!?()/~'%";
    protected static final int BUFFER_SIZE = 32768;
    public static final int DEFAULT_HTTP_CONNECT_TIMEOUT = 5000;
    public static final int DEFAULT_HTTP_READ_TIMEOUT = 20000;
    private static final String ERROR_UNSUPPORTED_SCHEME = "UIL doesn't support scheme(protocol) by default [%s]. You should implement this support yourself (BaseImageDownloader.getStreamFromOtherSource(...))";
    protected static final int MAX_REDIRECT_COUNT = 5;
    protected final int connectTimeout;
    protected final Context context;
    protected final int readTimeout;

    public BaseImageDownloader(Context context2) {
        this.context = context2.getApplicationContext();
        this.connectTimeout = 5000;
        this.readTimeout = 20000;
    }

    public BaseImageDownloader(Context context2, int connectTimeout2, int readTimeout2) {
        this.context = context2.getApplicationContext();
        this.connectTimeout = connectTimeout2;
        this.readTimeout = readTimeout2;
    }

    public InputStream getStream(String imageUri, Object extra) throws IOException {
        switch (ImageDownloader.Scheme.ofUri(imageUri)) {
            case HTTP:
            case HTTPS:
                return getStreamFromNetwork(imageUri, extra);
            case FILE:
                return getStreamFromFile(imageUri, extra);
            case CONTENT:
                return getStreamFromContent(imageUri, extra);
            case ASSETS:
                return getStreamFromAssets(imageUri, extra);
            case DRAWABLE:
                return getStreamFromDrawable(imageUri, extra);
            default:
                return getStreamFromOtherSource(imageUri, extra);
        }
    }

    /* access modifiers changed from: protected */
    public InputStream getStreamFromNetwork(String imageUri, Object extra) throws IOException {
        HttpURLConnection conn = createConnection(imageUri, extra);
        int redirectCount = 0;
        while (conn.getResponseCode() / 100 == 3 && redirectCount < 5) {
            conn = createConnection(conn.getHeaderField("Location"), extra);
            redirectCount++;
        }
        try {
            InputStream imageStream = conn.getInputStream();
            if (shouldBeProcessed(conn)) {
                return new ContentLengthInputStream(new BufferedInputStream(imageStream, 32768), (long) conn.getContentLength());
            }
            IoUtils.closeSilently(imageStream);
            throw new IOException("Image request failed with response code " + conn.getResponseCode());
        } catch (IOException e) {
            IoUtils.readAndCloseStream(conn.getErrorStream());
            throw e;
        }
    }

    /* access modifiers changed from: protected */
    public boolean shouldBeProcessed(HttpURLConnection conn) throws IOException {
        return conn.getResponseCode() == 200;
    }

    /* access modifiers changed from: protected */
    public HttpURLConnection createConnection(String url, Object extra) throws IOException {
        HttpURLConnection conn = (HttpURLConnection) new URL(Uri.encode(url, "@#&=*+-_.,:!?()/~'%")).openConnection();
        conn.setConnectTimeout(this.connectTimeout);
        conn.setReadTimeout(this.readTimeout);
        return conn;
    }

    /* access modifiers changed from: protected */
    public InputStream getStreamFromFile(String imageUri, Object extra) throws IOException {
        String filePath = ImageDownloader.Scheme.FILE.crop(imageUri);
        return new ContentLengthInputStream(new BufferedInputStream(new FileInputStream(filePath), 32768), new File(filePath).length());
    }

    /* access modifiers changed from: protected */
    public InputStream getStreamFromContent(String imageUri, Object extra) throws FileNotFoundException {
        return this.context.getContentResolver().openInputStream(Uri.parse(imageUri));
    }

    /* access modifiers changed from: protected */
    public InputStream getStreamFromAssets(String imageUri, Object extra) throws IOException {
        return this.context.getAssets().open(ImageDownloader.Scheme.ASSETS.crop(imageUri));
    }

    /* access modifiers changed from: protected */
    public InputStream getStreamFromDrawable(String imageUri, Object extra) {
        Bitmap bitmap = ((BitmapDrawable) this.context.getResources().getDrawable(Integer.parseInt(ImageDownloader.Scheme.DRAWABLE.crop(imageUri)))).getBitmap();
        ByteArrayOutputStream os = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG, 0, os);
        return new ByteArrayInputStream(os.toByteArray());
    }

    /* access modifiers changed from: protected */
    public InputStream getStreamFromOtherSource(String imageUri, Object extra) throws IOException {
        throw new UnsupportedOperationException(String.format("UIL doesn't support scheme(protocol) by default [%s]. You should implement this support yourself (BaseImageDownloader.getStreamFromOtherSource(...))", new Object[]{imageUri}));
    }
}
