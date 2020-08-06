package com.alibaba.sdk.android.oss.common;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class OSSSQLiteHelper extends SQLiteOpenHelper {
    private static final String CREATE_TABLE_PART_INFO = "create table if not exists part_info(id INTEGER primary key,upload_id VARCHAR(255),num INTEGER,crc64 INTEGER,size INTEGER,etag VARCHAR(255))";
    public static final String TABLE_NAME_PART_INFO = "part_info";

    public OSSSQLiteHelper(Context context) {
        this(context, "oss_android_sdk.db", (SQLiteDatabase.CursorFactory) null, 1);
    }

    public OSSSQLiteHelper(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
        super(context, name, factory, version);
    }

    public void onCreate(SQLiteDatabase db) {
        db.execSQL(CREATE_TABLE_PART_INFO);
    }

    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
    }
}
