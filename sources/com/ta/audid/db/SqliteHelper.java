package com.ta.audid.db;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import com.ta.audid.utils.UtdidLogger;
import java.util.concurrent.Future;
import java.util.concurrent.atomic.AtomicInteger;

public class SqliteHelper extends SQLiteOpenHelper {
    private static final int DATABASE_VERSION = 2;
    private DelayCloseDbTask mCloseDbTask = new DelayCloseDbTask();
    /* access modifiers changed from: private */
    public AtomicInteger mWritableCounter = new AtomicInteger();
    /* access modifiers changed from: private */
    public SQLiteDatabase mWritableDatabase;
    private Future<?> mcloseFuture;

    public SqliteHelper(Context context, String dbname) {
        super(context, dbname, (SQLiteDatabase.CursorFactory) null, 2);
    }

    public void onOpen(SQLiteDatabase db) {
        Cursor cursor = null;
        try {
            cursor = db.rawQuery("PRAGMA journal_mode=DELETE", (String[]) null);
        } catch (Throwable th) {
        } finally {
            closeCursor(cursor);
        }
        super.onOpen(db);
    }

    public void onCreate(SQLiteDatabase db) {
    }

    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
    }

    public synchronized SQLiteDatabase getWritableDatabase() {
        try {
            if (this.mWritableDatabase == null) {
                this.mWritableDatabase = super.getWritableDatabase();
            }
            this.mWritableCounter.incrementAndGet();
        } catch (Throwable e) {
            UtdidLogger.w("TAG", "e", e);
        }
        return this.mWritableDatabase;
    }

    /* JADX WARNING: Exception block dominator not found, dom blocks: [] */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public synchronized void closeWritableDatabase(android.database.sqlite.SQLiteDatabase r7) {
        /*
            r6 = this;
            monitor-enter(r6)
            if (r7 != 0) goto L_0x0005
        L_0x0003:
            monitor-exit(r6)
            return
        L_0x0005:
            java.util.concurrent.atomic.AtomicInteger r0 = r6.mWritableCounter     // Catch:{ Throwable -> 0x0027, all -> 0x0029 }
            int r0 = r0.decrementAndGet()     // Catch:{ Throwable -> 0x0027, all -> 0x0029 }
            if (r0 != 0) goto L_0x0003
            java.util.concurrent.Future<?> r0 = r6.mcloseFuture     // Catch:{ Throwable -> 0x0027, all -> 0x0029 }
            if (r0 == 0) goto L_0x0017
            java.util.concurrent.Future<?> r0 = r6.mcloseFuture     // Catch:{ Throwable -> 0x0027, all -> 0x0029 }
            r1 = 0
            r0.cancel(r1)     // Catch:{ Throwable -> 0x0027, all -> 0x0029 }
        L_0x0017:
            com.ta.audid.utils.TaskExecutor r0 = com.ta.audid.utils.TaskExecutor.getInstance()     // Catch:{ Throwable -> 0x0027, all -> 0x0029 }
            r1 = 0
            com.ta.audid.db.SqliteHelper$DelayCloseDbTask r2 = r6.mCloseDbTask     // Catch:{ Throwable -> 0x0027, all -> 0x0029 }
            r4 = 30000(0x7530, double:1.4822E-319)
            java.util.concurrent.ScheduledFuture r0 = r0.schedule(r1, r2, r4)     // Catch:{ Throwable -> 0x0027, all -> 0x0029 }
            r6.mcloseFuture = r0     // Catch:{ Throwable -> 0x0027, all -> 0x0029 }
            goto L_0x0003
        L_0x0027:
            r0 = move-exception
            goto L_0x0003
        L_0x0029:
            r0 = move-exception
            monitor-exit(r6)
            throw r0
        */
        throw new UnsupportedOperationException("Method not decompiled: com.ta.audid.db.SqliteHelper.closeWritableDatabase(android.database.sqlite.SQLiteDatabase):void");
    }

    public void closeCursor(Cursor cursor) {
        if (cursor != null) {
            try {
                cursor.close();
            } catch (Throwable th) {
            }
        }
    }

    class DelayCloseDbTask implements Runnable {
        DelayCloseDbTask() {
        }

        public void run() {
            synchronized (SqliteHelper.this) {
                if (SqliteHelper.this.mWritableCounter.get() == 0 && SqliteHelper.this.mWritableDatabase != null) {
                    SqliteHelper.this.mWritableDatabase.close();
                    SQLiteDatabase unused = SqliteHelper.this.mWritableDatabase = null;
                }
            }
        }
    }
}
