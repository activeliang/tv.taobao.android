package com.tvtaobao.voicesdk.provider;

import android.content.ContentProvider;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.MatrixCursor;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import com.tvtaobao.voicesdk.base.SDKInitConfig;
import com.tvtaobao.voicesdk.utils.LogPrint;
import com.yunos.tv.core.CoreApplication;

public class VoiceContentProvider extends ContentProvider {
    private static final String ADDRESS = "address";
    private static final String AUTHORITY = "com.yunos.tvtaobao.voiceProvider";
    private static final String CUR_CLASSNAME = "classname";
    private static final String CUR_PAGE = "page";
    private static final int MATCH_CODE = 200;
    private static final String SDKINIT = "sdkInit_Str";
    private static final String SDKINITCONFIG = "sdkInitConfig";
    private static final String SDKINITSTATUS = "sdkInitStatus";
    private static final String SEPARATOR = "/";
    private static final String TYPE_INT = "tvtaobao";
    private static UriMatcher uriMatcher = new UriMatcher(-1);
    private final String TAG = "VoiceContentProvider";

    static {
        uriMatcher.addURI(AUTHORITY, "tvtaobao/address", 1);
        uriMatcher.addURI(AUTHORITY, "tvtaobao/page", 2);
        uriMatcher.addURI(AUTHORITY, "tvtaobao/classname", 3);
        uriMatcher.addURI(AUTHORITY, "tvtaobao/sdkInitStatus", 4);
        uriMatcher.addURI(AUTHORITY, "tvtaobao/sdkInitConfig", 5);
    }

    public boolean onCreate() {
        return false;
    }

    @Nullable
    public Cursor query(@NonNull Uri uri, @Nullable String[] projection, @Nullable String selection, @Nullable String[] selectionArgs, @Nullable String sortOrder) {
        LogPrint.i("VoiceContentProvider", "VoiceContentProvider.query uri=" + uri);
        if (CoreApplication.getApplication() == null) {
            return null;
        }
        int math = uriMatcher.match(uri);
        LogPrint.i("VoiceContentProvider", "VoiceContentProvider.query math=" + math);
        switch (math) {
            case 1:
                return buildCursor("address", SDKInitConfig.getLocation());
            case 2:
                return buildCursor("page", SDKInitConfig.getCurrentPage());
            case 3:
                return buildCursor(CUR_CLASSNAME, SDKInitConfig.getCurrentClassName());
            case 4:
                return buildCursor(SDKINITSTATUS, SDKInitConfig.getSDKInitInfo());
            default:
                return null;
        }
    }

    @Nullable
    public String getType(@NonNull Uri uri) {
        LogPrint.i("VoiceContentProvider", "VoiceContentProvider.getType uri=" + uri);
        return null;
    }

    @Nullable
    public Uri insert(@NonNull Uri uri, @Nullable ContentValues values) {
        LogPrint.i("VoiceContentProvider", "VoiceContentProvider.insert uri=" + uri);
        switch (uriMatcher.match(uri)) {
            case 5:
                SDKInitConfig.init(values.getAsString(SDKINIT));
                return null;
            default:
                return null;
        }
    }

    public int delete(@NonNull Uri uri, @Nullable String selection, @Nullable String[] selectionArgs) {
        return 0;
    }

    public int update(@NonNull Uri uri, @Nullable ContentValues values, @Nullable String selection, @Nullable String[] selectionArgs) {
        LogPrint.i("VoiceContentProvider", "VoiceContentProvider.update uri=" + uri);
        return 0;
    }

    private Cursor buildCursor(String COLUMNNAME, String value) {
        if (TextUtils.isEmpty(value)) {
            return null;
        }
        MatrixCursor cursor = new MatrixCursor(new String[]{COLUMNNAME});
        cursor.addRow(new Object[]{value});
        return cursor;
    }
}
