package com.bumptech.glide.load.data;

import android.content.ContentResolver;
import android.content.UriMatcher;
import android.net.Uri;
import android.provider.ContactsContract;
import android.support.annotation.NonNull;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;

public class StreamLocalUriFetcher extends LocalUriFetcher<InputStream> {
    private static final int ID_CONTACTS_CONTACT = 3;
    private static final int ID_CONTACTS_LOOKUP = 1;
    private static final int ID_CONTACTS_PHOTO = 4;
    private static final int ID_CONTACTS_THUMBNAIL = 2;
    private static final int ID_LOOKUP_BY_PHONE = 5;
    private static final UriMatcher URI_MATCHER = new UriMatcher(-1);

    static {
        URI_MATCHER.addURI("com.android.contacts", "contacts/lookup/*/#", 1);
        URI_MATCHER.addURI("com.android.contacts", "contacts/lookup/*", 1);
        URI_MATCHER.addURI("com.android.contacts", "contacts/#/photo", 2);
        URI_MATCHER.addURI("com.android.contacts", "contacts/#", 3);
        URI_MATCHER.addURI("com.android.contacts", "contacts/#/display_photo", 4);
        URI_MATCHER.addURI("com.android.contacts", "phone_lookup/*", 5);
    }

    public StreamLocalUriFetcher(ContentResolver resolver, Uri uri) {
        super(resolver, uri);
    }

    /* access modifiers changed from: protected */
    public InputStream loadResource(Uri uri, ContentResolver contentResolver) throws FileNotFoundException {
        InputStream inputStream = loadResourceFromUri(uri, contentResolver);
        if (inputStream != null) {
            return inputStream;
        }
        throw new FileNotFoundException("InputStream is null for " + uri);
    }

    private InputStream loadResourceFromUri(Uri uri, ContentResolver contentResolver) throws FileNotFoundException {
        switch (URI_MATCHER.match(uri)) {
            case 1:
            case 5:
                Uri uri2 = ContactsContract.Contacts.lookupContact(contentResolver, uri);
                if (uri2 != null) {
                    return openContactPhotoInputStream(contentResolver, uri2);
                }
                throw new FileNotFoundException("Contact cannot be found");
            case 3:
                return openContactPhotoInputStream(contentResolver, uri);
            default:
                return contentResolver.openInputStream(uri);
        }
    }

    private InputStream openContactPhotoInputStream(ContentResolver contentResolver, Uri contactUri) {
        return ContactsContract.Contacts.openContactPhotoInputStream(contentResolver, contactUri, true);
    }

    /* access modifiers changed from: protected */
    public void close(InputStream data) throws IOException {
        data.close();
    }

    @NonNull
    public Class<InputStream> getDataClass() {
        return InputStream.class;
    }
}
