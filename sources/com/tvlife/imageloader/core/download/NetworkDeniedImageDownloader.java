package com.tvlife.imageloader.core.download;

import com.tvlife.imageloader.core.download.ImageDownloader;
import java.io.IOException;
import java.io.InputStream;

public class NetworkDeniedImageDownloader implements ImageDownloader {
    private final ImageDownloader wrappedDownloader;

    public NetworkDeniedImageDownloader(ImageDownloader wrappedDownloader2) {
        this.wrappedDownloader = wrappedDownloader2;
    }

    public InputStream getStream(String imageUri, Object extra) throws IOException {
        switch (ImageDownloader.Scheme.ofUri(imageUri)) {
            case HTTP:
            case HTTPS:
                throw new IllegalStateException();
            default:
                return this.wrappedDownloader.getStream(imageUri, extra);
        }
    }
}
