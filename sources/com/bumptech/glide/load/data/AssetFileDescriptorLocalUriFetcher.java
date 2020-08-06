package com.bumptech.glide.load.data;

import android.content.ContentResolver;
import android.content.res.AssetFileDescriptor;
import android.net.Uri;
import android.support.annotation.NonNull;
import com.alibaba.analytics.core.sync.UploadQueueMgr;
import java.io.FileNotFoundException;
import java.io.IOException;

public final class AssetFileDescriptorLocalUriFetcher extends LocalUriFetcher<AssetFileDescriptor> {
    public AssetFileDescriptorLocalUriFetcher(ContentResolver contentResolver, Uri uri) {
        super(contentResolver, uri);
    }

    /* access modifiers changed from: protected */
    public AssetFileDescriptor loadResource(Uri uri, ContentResolver contentResolver) throws FileNotFoundException {
        AssetFileDescriptor result = contentResolver.openAssetFileDescriptor(uri, UploadQueueMgr.MSGTYPE_REALTIME);
        if (result != null) {
            return result;
        }
        throw new FileNotFoundException("FileDescriptor is null for: " + uri);
    }

    /* access modifiers changed from: protected */
    public void close(AssetFileDescriptor data) throws IOException {
        data.close();
    }

    @NonNull
    public Class<AssetFileDescriptor> getDataClass() {
        return AssetFileDescriptor.class;
    }
}
