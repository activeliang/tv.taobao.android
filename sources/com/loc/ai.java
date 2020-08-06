package com.loc;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/* compiled from: DB */
public final class ai extends SQLiteOpenHelper {
    private static boolean b = true;
    private static boolean c = false;
    private ae a;

    public ai(Context context, String str, ae aeVar) {
        super(context, str, (SQLiteDatabase.CursorFactory) null, 1);
        this.a = aeVar;
    }

    public final void onCreate(SQLiteDatabase sQLiteDatabase) {
        this.a.a(sQLiteDatabase);
    }

    public final void onUpgrade(SQLiteDatabase sQLiteDatabase, int i, int i2) {
    }
}
