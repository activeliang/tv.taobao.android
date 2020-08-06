package com.bftv.fui.authentication;

import android.content.ContentProvider;
import android.content.ContentValues;
import android.content.Context;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.MatrixCursor;
import android.net.Uri;
import android.taobao.windvane.util.WVNativeCallbackUtil;

public class AuthenticationProvider extends ContentProvider {
    private static final String AUTHORITY = "com.bftv.voice.provider.";
    private static final int SUPPORT_PCK_CODE = 7777;
    public static Uri sContentUri;
    private static UriMatcher sUriMatcher;

    private void init() {
        Context context;
        if (sContentUri == null && (context = getContext()) != null) {
            String pckName = context.getPackageName();
            String authority = AUTHORITY + pckName;
            sContentUri = Uri.parse("content://" + authority + WVNativeCallbackUtil.SEPERATER + pckName);
            sUriMatcher = new UriMatcher(-1);
            sUriMatcher.addURI(authority, pckName, SUPPORT_PCK_CODE);
        }
    }

    public boolean onCreate() {
        return false;
    }

    public Cursor query(Uri uri, String[] projection, String selection, String[] selectionArgs, String sortOrder) {
        init();
        if (sUriMatcher.match(uri) != SUPPORT_PCK_CODE) {
            return null;
        }
        MatrixCursor cursor = new MatrixCursor(new String[]{"comment"});
        cursor.addRow(new Object[]{"ok20171210"});
        return cursor;
    }

    public String getType(Uri uri) {
        return null;
    }

    public Uri insert(Uri uri, ContentValues values) {
        return Uri.parse(getContext().getPackageName() + WVNativeCallbackUtil.SEPERATER + 0);
    }

    public int delete(Uri uri, String selection, String[] selectionArgs) {
        return 0;
    }

    public int update(Uri uri, ContentValues values, String selection, String[] selectionArgs) {
        return 0;
    }
}
