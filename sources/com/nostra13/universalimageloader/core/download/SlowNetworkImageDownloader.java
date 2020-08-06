package com.nostra13.universalimageloader.core.download;

import com.nostra13.universalimageloader.core.assist.FlushedInputStream;
import com.nostra13.universalimageloader.core.download.ImageDownloader;
import java.io.IOException;
import java.io.InputStream;

public class SlowNetworkImageDownloader implements ImageDownloader {
    private final ImageDownloader wrappedDownloader;

    public SlowNetworkImageDownloader(ImageDownloader wrappedDownloader2) {
        this.wrappedDownloader = wrappedDownloader2;
    }

    public InputStream getStream(String imageUri, Object extra) throws IOException {
        InputStream imageStream = this.wrappedDownloader.getStream(imageUri, extra);
        switch (ImageDownloader.Scheme.ofUri(imageUri)) {
            case HTTP:
            case HTTPS:
                return new FlushedInputStream(imageStream);
            default:
                return imageStream;
        }
    }
}
