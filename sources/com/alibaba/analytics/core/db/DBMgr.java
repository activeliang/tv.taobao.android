package com.alibaba.analytics.core.db;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.text.TextUtils;
import com.alibaba.analytics.core.Constants;
import com.alibaba.analytics.core.Variables;
import com.alibaba.analytics.core.db.annotation.Column;
import com.alibaba.analytics.core.db.annotation.Ingore;
import com.alibaba.analytics.core.db.annotation.TableName;
import com.alibaba.analytics.utils.Logger;
import com.taobao.muniontaobaosdk.p4p.anticheat.model.ClientTraceData;
import java.io.File;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;

public class DBMgr {
    private static final String TAG = "DBMgr";
    private String insertException = null;
    private HashMap<String, Boolean> mCheckdbMap = new HashMap<>();
    private HashMap<Class<?>, List<Field>> mClsFieldsMap = new HashMap<>();
    private HashMap<Class<?>, String> mClsTableNameMap = new HashMap<>();
    private String mDbName;
    private HashMap<Field, String> mFieldNameMap = new HashMap<>();
    private SqliteHelper mHelper;

    public DBMgr(Context context, String dbName) {
        this.mHelper = new SqliteHelper(context, dbName);
        this.mDbName = dbName;
    }

    /* JADX WARNING: Code restructure failed: missing block: B:81:0x01f9, code lost:
        r17 = th;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:82:0x01fa, code lost:
        r6 = r7;
     */
    /* JADX WARNING: Failed to process nested try/catch */
    /* JADX WARNING: Removed duplicated region for block: B:81:0x01f9 A[ExcHandler: all (th java.lang.Throwable), Splitter:B:24:0x0099] */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public synchronized java.util.List<? extends com.alibaba.analytics.core.db.Entity> find(java.lang.Class<? extends com.alibaba.analytics.core.db.Entity> r23, java.lang.String r24, java.lang.String r25, int r26) {
        /*
            r22 = this;
            monitor-enter(r22)
            java.util.List r6 = java.util.Collections.EMPTY_LIST     // Catch:{ all -> 0x0212 }
            if (r23 != 0) goto L_0x0008
            r7 = r6
        L_0x0006:
            monitor-exit(r22)
            return r7
        L_0x0008:
            java.lang.String r15 = r22.getTablename(r23)     // Catch:{ all -> 0x0212 }
            r0 = r22
            r1 = r23
            android.database.sqlite.SQLiteDatabase r4 = r0.checkTableAvailable(r1, r15)     // Catch:{ all -> 0x0212 }
            if (r4 != 0) goto L_0x002d
            java.lang.String r17 = "DBMgr"
            r18 = 1
            r0 = r18
            java.lang.Object[] r0 = new java.lang.Object[r0]     // Catch:{ all -> 0x0212 }
            r18 = r0
            r19 = 0
            java.lang.String r20 = "db is null"
            r18[r19] = r20     // Catch:{ all -> 0x0212 }
            com.alibaba.analytics.utils.Logger.d((java.lang.String) r17, (java.lang.Object[]) r18)     // Catch:{ all -> 0x0212 }
            r7 = r6
            goto L_0x0006
        L_0x002d:
            java.lang.StringBuilder r17 = new java.lang.StringBuilder     // Catch:{ all -> 0x0212 }
            r17.<init>()     // Catch:{ all -> 0x0212 }
            java.lang.String r18 = "SELECT * FROM "
            java.lang.StringBuilder r17 = r17.append(r18)     // Catch:{ all -> 0x0212 }
            r0 = r17
            java.lang.StringBuilder r18 = r0.append(r15)     // Catch:{ all -> 0x0212 }
            boolean r17 = android.text.TextUtils.isEmpty(r24)     // Catch:{ all -> 0x0212 }
            if (r17 == 0) goto L_0x00ec
            java.lang.String r17 = ""
        L_0x0048:
            r0 = r18
            r1 = r17
            java.lang.StringBuilder r18 = r0.append(r1)     // Catch:{ all -> 0x0212 }
            boolean r17 = android.text.TextUtils.isEmpty(r25)     // Catch:{ all -> 0x0212 }
            if (r17 == 0) goto L_0x010a
            java.lang.String r17 = ""
        L_0x0059:
            r0 = r18
            r1 = r17
            java.lang.StringBuilder r18 = r0.append(r1)     // Catch:{ all -> 0x0212 }
            if (r26 > 0) goto L_0x0128
            java.lang.String r17 = ""
        L_0x0066:
            r0 = r18
            r1 = r17
            java.lang.StringBuilder r17 = r0.append(r1)     // Catch:{ all -> 0x0212 }
            java.lang.String r14 = r17.toString()     // Catch:{ all -> 0x0212 }
            java.lang.String r17 = "DBMgr"
            r18 = 2
            r0 = r18
            java.lang.Object[] r0 = new java.lang.Object[r0]     // Catch:{ all -> 0x0212 }
            r18 = r0
            r19 = 0
            java.lang.String r20 = "sql"
            r18[r19] = r20     // Catch:{ all -> 0x0212 }
            r19 = 1
            r18[r19] = r14     // Catch:{ all -> 0x0212 }
            com.alibaba.analytics.utils.Logger.d((java.lang.String) r17, (java.lang.Object[]) r18)     // Catch:{ all -> 0x0212 }
            r2 = 0
            r17 = 0
            r0 = r17
            android.database.Cursor r2 = r4.rawQuery(r14, r0)     // Catch:{ Throwable -> 0x022f }
            java.util.ArrayList r7 = new java.util.ArrayList     // Catch:{ Throwable -> 0x022f }
            r7.<init>()     // Catch:{ Throwable -> 0x022f }
            java.util.List r11 = r22.getAllFields(r23)     // Catch:{ Throwable -> 0x01c0, all -> 0x01f9 }
        L_0x009d:
            if (r2 == 0) goto L_0x0215
            boolean r17 = r2.moveToNext()     // Catch:{ Throwable -> 0x01c0, all -> 0x01f9 }
            if (r17 == 0) goto L_0x0215
            java.lang.Object r8 = r23.newInstance()     // Catch:{ Throwable -> 0x01c0, all -> 0x01f9 }
            com.alibaba.analytics.core.db.Entity r8 = (com.alibaba.analytics.core.db.Entity) r8     // Catch:{ Throwable -> 0x01c0, all -> 0x01f9 }
            r12 = 0
        L_0x00ac:
            int r17 = r11.size()     // Catch:{ Throwable -> 0x01c0, all -> 0x01f9 }
            r0 = r17
            if (r12 >= r0) goto L_0x01f4
            java.lang.Object r9 = r11.get(r12)     // Catch:{ Throwable -> 0x01c0, all -> 0x01f9 }
            java.lang.reflect.Field r9 = (java.lang.reflect.Field) r9     // Catch:{ Throwable -> 0x01c0, all -> 0x01f9 }
            java.lang.Class r10 = r9.getType()     // Catch:{ Throwable -> 0x01c0, all -> 0x01f9 }
            r16 = 0
            r0 = r22
            java.lang.String r3 = r0.getColumnName(r9)     // Catch:{ Throwable -> 0x01c0, all -> 0x01f9 }
            int r13 = r2.getColumnIndex(r3)     // Catch:{ Throwable -> 0x01c0, all -> 0x01f9 }
            r17 = -1
            r0 = r17
            if (r13 == r0) goto L_0x0196
            java.lang.Class<java.lang.Long> r17 = java.lang.Long.class
            r0 = r17
            if (r10 == r0) goto L_0x00dc
            java.lang.Class r17 = java.lang.Long.TYPE     // Catch:{ Throwable -> 0x01c0, all -> 0x01f9 }
            r0 = r17
            if (r10 != r0) goto L_0x0146
        L_0x00dc:
            long r18 = r2.getLong(r13)     // Catch:{ Throwable -> 0x01c0, all -> 0x01f9 }
            java.lang.Long r16 = java.lang.Long.valueOf(r18)     // Catch:{ Throwable -> 0x01c0, all -> 0x01f9 }
        L_0x00e4:
            r0 = r16
            r9.set(r8, r0)     // Catch:{ Exception -> 0x0177 }
        L_0x00e9:
            int r12 = r12 + 1
            goto L_0x00ac
        L_0x00ec:
            java.lang.StringBuilder r17 = new java.lang.StringBuilder     // Catch:{ all -> 0x0212 }
            r17.<init>()     // Catch:{ all -> 0x0212 }
            java.lang.String r19 = " WHERE "
            r0 = r17
            r1 = r19
            java.lang.StringBuilder r17 = r0.append(r1)     // Catch:{ all -> 0x0212 }
            r0 = r17
            r1 = r24
            java.lang.StringBuilder r17 = r0.append(r1)     // Catch:{ all -> 0x0212 }
            java.lang.String r17 = r17.toString()     // Catch:{ all -> 0x0212 }
            goto L_0x0048
        L_0x010a:
            java.lang.StringBuilder r17 = new java.lang.StringBuilder     // Catch:{ all -> 0x0212 }
            r17.<init>()     // Catch:{ all -> 0x0212 }
            java.lang.String r19 = " ORDER BY "
            r0 = r17
            r1 = r19
            java.lang.StringBuilder r17 = r0.append(r1)     // Catch:{ all -> 0x0212 }
            r0 = r17
            r1 = r25
            java.lang.StringBuilder r17 = r0.append(r1)     // Catch:{ all -> 0x0212 }
            java.lang.String r17 = r17.toString()     // Catch:{ all -> 0x0212 }
            goto L_0x0059
        L_0x0128:
            java.lang.StringBuilder r17 = new java.lang.StringBuilder     // Catch:{ all -> 0x0212 }
            r17.<init>()     // Catch:{ all -> 0x0212 }
            java.lang.String r19 = " LIMIT "
            r0 = r17
            r1 = r19
            java.lang.StringBuilder r17 = r0.append(r1)     // Catch:{ all -> 0x0212 }
            r0 = r17
            r1 = r26
            java.lang.StringBuilder r17 = r0.append(r1)     // Catch:{ all -> 0x0212 }
            java.lang.String r17 = r17.toString()     // Catch:{ all -> 0x0212 }
            goto L_0x0066
        L_0x0146:
            java.lang.Class<java.lang.Integer> r17 = java.lang.Integer.class
            r0 = r17
            if (r10 == r0) goto L_0x0152
            java.lang.Class r17 = java.lang.Integer.TYPE     // Catch:{ Throwable -> 0x01c0, all -> 0x01f9 }
            r0 = r17
            if (r10 != r0) goto L_0x015b
        L_0x0152:
            int r17 = r2.getInt(r13)     // Catch:{ Throwable -> 0x01c0, all -> 0x01f9 }
            java.lang.Integer r16 = java.lang.Integer.valueOf(r17)     // Catch:{ Throwable -> 0x01c0, all -> 0x01f9 }
            goto L_0x00e4
        L_0x015b:
            java.lang.Class r17 = java.lang.Double.TYPE     // Catch:{ Throwable -> 0x01c0, all -> 0x01f9 }
            r0 = r17
            if (r10 == r0) goto L_0x0167
            java.lang.Class<java.lang.Double> r17 = java.lang.Double.class
            r0 = r17
            if (r10 != r0) goto L_0x0171
        L_0x0167:
            double r18 = r2.getDouble(r13)     // Catch:{ Throwable -> 0x01c0, all -> 0x01f9 }
            java.lang.Double r16 = java.lang.Double.valueOf(r18)     // Catch:{ Throwable -> 0x01c0, all -> 0x01f9 }
            goto L_0x00e4
        L_0x0171:
            java.lang.String r16 = r2.getString(r13)     // Catch:{ Throwable -> 0x01c0, all -> 0x01f9 }
            goto L_0x00e4
        L_0x0177:
            r5 = move-exception
            boolean r0 = r5 instanceof java.lang.IllegalArgumentException     // Catch:{ Throwable -> 0x0193, all -> 0x01f9 }
            r17 = r0
            if (r17 == 0) goto L_0x00e9
            r0 = r16
            boolean r0 = r0 instanceof java.lang.String     // Catch:{ Throwable -> 0x0193, all -> 0x01f9 }
            r17 = r0
            if (r17 == 0) goto L_0x00e9
            java.lang.String r16 = (java.lang.String) r16     // Catch:{ Throwable -> 0x0193, all -> 0x01f9 }
            java.lang.Boolean r17 = java.lang.Boolean.valueOf(r16)     // Catch:{ Throwable -> 0x0193, all -> 0x01f9 }
            r0 = r17
            r9.set(r8, r0)     // Catch:{ Throwable -> 0x0193, all -> 0x01f9 }
            goto L_0x00e9
        L_0x0193:
            r17 = move-exception
            goto L_0x00e9
        L_0x0196:
            java.lang.String r17 = "DBMgr"
            r18 = 1
            r0 = r18
            java.lang.Object[] r0 = new java.lang.Object[r0]     // Catch:{ Throwable -> 0x01c0, all -> 0x01f9 }
            r18 = r0
            r19 = 0
            java.lang.StringBuilder r20 = new java.lang.StringBuilder     // Catch:{ Throwable -> 0x01c0, all -> 0x01f9 }
            r20.<init>()     // Catch:{ Throwable -> 0x01c0, all -> 0x01f9 }
            java.lang.String r21 = "can not get field: "
            java.lang.StringBuilder r20 = r20.append(r21)     // Catch:{ Throwable -> 0x01c0, all -> 0x01f9 }
            r0 = r20
            java.lang.StringBuilder r20 = r0.append(r3)     // Catch:{ Throwable -> 0x01c0, all -> 0x01f9 }
            java.lang.String r20 = r20.toString()     // Catch:{ Throwable -> 0x01c0, all -> 0x01f9 }
            r18[r19] = r20     // Catch:{ Throwable -> 0x01c0, all -> 0x01f9 }
            com.alibaba.analytics.utils.Logger.w((java.lang.String) r17, (java.lang.Object[]) r18)     // Catch:{ Throwable -> 0x01c0, all -> 0x01f9 }
            goto L_0x00e9
        L_0x01c0:
            r5 = move-exception
            r6 = r7
        L_0x01c2:
            java.lang.String r17 = "DBMgr"
            r18 = 2
            r0 = r18
            java.lang.Object[] r0 = new java.lang.Object[r0]     // Catch:{ all -> 0x022d }
            r18 = r0
            r19 = 0
            java.lang.String r20 = "[get]"
            r18[r19] = r20     // Catch:{ all -> 0x022d }
            r19 = 1
            r18[r19] = r5     // Catch:{ all -> 0x022d }
            com.alibaba.analytics.utils.Logger.w((java.lang.String) r17, (java.lang.Object[]) r18)     // Catch:{ all -> 0x022d }
            r0 = r22
            com.alibaba.analytics.core.db.SqliteHelper r0 = r0.mHelper     // Catch:{ all -> 0x0212 }
            r17 = r0
            r0 = r17
            r0.closeCursor(r2)     // Catch:{ all -> 0x0212 }
            r0 = r22
            com.alibaba.analytics.core.db.SqliteHelper r0 = r0.mHelper     // Catch:{ all -> 0x0212 }
            r17 = r0
            r0 = r17
            r0.closeWritableDatabase(r4)     // Catch:{ all -> 0x0212 }
        L_0x01f1:
            r7 = r6
            goto L_0x0006
        L_0x01f4:
            r7.add(r8)     // Catch:{ Throwable -> 0x01c0, all -> 0x01f9 }
            goto L_0x009d
        L_0x01f9:
            r17 = move-exception
            r6 = r7
        L_0x01fb:
            r0 = r22
            com.alibaba.analytics.core.db.SqliteHelper r0 = r0.mHelper     // Catch:{ all -> 0x0212 }
            r18 = r0
            r0 = r18
            r0.closeCursor(r2)     // Catch:{ all -> 0x0212 }
            r0 = r22
            com.alibaba.analytics.core.db.SqliteHelper r0 = r0.mHelper     // Catch:{ all -> 0x0212 }
            r18 = r0
            r0 = r18
            r0.closeWritableDatabase(r4)     // Catch:{ all -> 0x0212 }
            throw r17     // Catch:{ all -> 0x0212 }
        L_0x0212:
            r17 = move-exception
            monitor-exit(r22)
            throw r17
        L_0x0215:
            r0 = r22
            com.alibaba.analytics.core.db.SqliteHelper r0 = r0.mHelper     // Catch:{ all -> 0x0212 }
            r17 = r0
            r0 = r17
            r0.closeCursor(r2)     // Catch:{ all -> 0x0212 }
            r0 = r22
            com.alibaba.analytics.core.db.SqliteHelper r0 = r0.mHelper     // Catch:{ all -> 0x0212 }
            r17 = r0
            r0 = r17
            r0.closeWritableDatabase(r4)     // Catch:{ all -> 0x0212 }
            r6 = r7
            goto L_0x01f1
        L_0x022d:
            r17 = move-exception
            goto L_0x01fb
        L_0x022f:
            r5 = move-exception
            goto L_0x01c2
        */
        throw new UnsupportedOperationException("Method not decompiled: com.alibaba.analytics.core.db.DBMgr.find(java.lang.Class, java.lang.String, java.lang.String, int):java.util.List");
    }

    public void insert(Entity entity) {
        if (entity != null) {
            ArrayList<Entity> entitys = new ArrayList<>(1);
            entitys.add(entity);
            insert((List<? extends Entity>) entitys);
        }
    }

    public synchronized void insert(List<? extends Entity> entities) {
        if (entities != null) {
            if (entities.size() != 0) {
                String tableName = getTablename(((Entity) entities.get(0)).getClass());
                SQLiteDatabase db = checkTableAvailable(((Entity) entities.get(0)).getClass(), tableName);
                if (db == null) {
                    Logger.w(TAG, "can not get available db");
                } else {
                    if (entities != null) {
                        Logger.d("", "entities.size", Integer.valueOf(entities.size()));
                    }
                    try {
                        List<Field> fields = getAllFields(((Entity) entities.get(0)).getClass());
                        ContentValues values = new ContentValues();
                        db.beginTransaction();
                        for (int i = 0; i < entities.size(); i++) {
                            Entity entity = (Entity) entities.get(i);
                            for (int j = 0; j < fields.size(); j++) {
                                Field field = fields.get(j);
                                String name = getColumnName(field);
                                Object value = field.get(entity);
                                if (value != null) {
                                    values.put(name, value + "");
                                } else {
                                    values.put(name, "");
                                }
                            }
                            if (entity._id == -1) {
                                values.remove("_id");
                                long ret = db.insert(tableName, (String) null, values);
                                if (ret != -1) {
                                    entity._id = ret;
                                }
                            } else {
                                long update = (long) db.update(tableName, values, "_id=?", new String[]{String.valueOf(entity._id)});
                            }
                            values.clear();
                        }
                        try {
                            db.setTransactionSuccessful();
                        } catch (Exception e) {
                        }
                        try {
                            db.endTransaction();
                        } catch (Exception e2) {
                        }
                        this.mHelper.closeWritableDatabase(db);
                    } catch (Exception e3) {
                        Logger.w(TAG, "get field failed", e3);
                    } catch (Throwable e4) {
                        try {
                            this.insertException = e4.getLocalizedMessage();
                            try {
                                db.setTransactionSuccessful();
                            } catch (Exception e5) {
                            }
                            try {
                                db.endTransaction();
                            } catch (Exception e6) {
                            }
                            this.mHelper.closeWritableDatabase(db);
                        } catch (Throwable th) {
                            try {
                                db.setTransactionSuccessful();
                            } catch (Exception e7) {
                            }
                            try {
                                db.endTransaction();
                            } catch (Exception e8) {
                            }
                            this.mHelper.closeWritableDatabase(db);
                            throw th;
                        }
                    }
                }
            }
        }
        return;
    }

    /* JADX WARNING: Removed duplicated region for block: B:34:0x00a6 A[Catch:{ Throwable -> 0x00ea, Throwable -> 0x00ec, Throwable -> 0x0086, all -> 0x00d5, Throwable -> 0x00e6, Throwable -> 0x00e8 }] */
    /* JADX WARNING: Unknown top exception splitter block from list: {B:40:0x00cc=Splitter:B:40:0x00cc, B:50:0x00dc=Splitter:B:50:0x00dc, B:31:0x009f=Splitter:B:31:0x009f} */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public synchronized int delete(java.util.List<? extends com.alibaba.analytics.core.db.Entity> r15) {
        /*
            r14 = this;
            r7 = 0
            monitor-enter(r14)
            if (r15 == 0) goto L_0x000a
            int r6 = r15.size()     // Catch:{ all -> 0x00d2 }
            if (r6 != 0) goto L_0x000d
        L_0x000a:
            r6 = r7
        L_0x000b:
            monitor-exit(r14)
            return r6
        L_0x000d:
            r6 = 0
            java.lang.Object r6 = r15.get(r6)     // Catch:{ all -> 0x00d2 }
            com.alibaba.analytics.core.db.Entity r6 = (com.alibaba.analytics.core.db.Entity) r6     // Catch:{ all -> 0x00d2 }
            java.lang.Class r6 = r6.getClass()     // Catch:{ all -> 0x00d2 }
            java.lang.String r3 = r14.getTablename(r6)     // Catch:{ all -> 0x00d2 }
            r6 = 0
            java.lang.Object r6 = r15.get(r6)     // Catch:{ all -> 0x00d2 }
            com.alibaba.analytics.core.db.Entity r6 = (com.alibaba.analytics.core.db.Entity) r6     // Catch:{ all -> 0x00d2 }
            java.lang.Class r6 = r6.getClass()     // Catch:{ all -> 0x00d2 }
            android.database.sqlite.SQLiteDatabase r0 = r14.checkTableAvailable(r6, r3)     // Catch:{ all -> 0x00d2 }
            if (r0 != 0) goto L_0x003e
            java.lang.String r6 = "DBMgr"
            r8 = 1
            java.lang.Object[] r8 = new java.lang.Object[r8]     // Catch:{ all -> 0x00d2 }
            r9 = 0
            java.lang.String r10 = "db is null"
            r8[r9] = r10     // Catch:{ all -> 0x00d2 }
            com.alibaba.analytics.utils.Logger.d((java.lang.String) r6, (java.lang.Object[]) r8)     // Catch:{ all -> 0x00d2 }
            r6 = r7
            goto L_0x000b
        L_0x003e:
            r0.beginTransaction()     // Catch:{ Throwable -> 0x0086 }
            r2 = 0
        L_0x0042:
            int r6 = r15.size()     // Catch:{ Throwable -> 0x0086 }
            if (r2 >= r6) goto L_0x00c6
            java.lang.String r7 = "_id=?"
            r6 = 1
            java.lang.String[] r8 = new java.lang.String[r6]     // Catch:{ Throwable -> 0x0086 }
            r9 = 0
            java.lang.StringBuilder r10 = new java.lang.StringBuilder     // Catch:{ Throwable -> 0x0086 }
            r10.<init>()     // Catch:{ Throwable -> 0x0086 }
            java.lang.Object r6 = r15.get(r2)     // Catch:{ Throwable -> 0x0086 }
            com.alibaba.analytics.core.db.Entity r6 = (com.alibaba.analytics.core.db.Entity) r6     // Catch:{ Throwable -> 0x0086 }
            long r12 = r6._id     // Catch:{ Throwable -> 0x0086 }
            java.lang.StringBuilder r6 = r10.append(r12)     // Catch:{ Throwable -> 0x0086 }
            java.lang.String r10 = ""
            java.lang.StringBuilder r6 = r6.append(r10)     // Catch:{ Throwable -> 0x0086 }
            java.lang.String r6 = r6.toString()     // Catch:{ Throwable -> 0x0086 }
            r8[r9] = r6     // Catch:{ Throwable -> 0x0086 }
            int r6 = r0.delete(r3, r7, r8)     // Catch:{ Throwable -> 0x0086 }
            long r4 = (long) r6     // Catch:{ Throwable -> 0x0086 }
            r6 = 0
            int r6 = (r4 > r6 ? 1 : (r4 == r6 ? 0 : -1))
            if (r6 > 0) goto L_0x007b
        L_0x0078:
            int r2 = r2 + 1
            goto L_0x0042
        L_0x007b:
            java.lang.Object r6 = r15.get(r2)     // Catch:{ Throwable -> 0x0086 }
            com.alibaba.analytics.core.db.Entity r6 = (com.alibaba.analytics.core.db.Entity) r6     // Catch:{ Throwable -> 0x0086 }
            r8 = -1
            r6._id = r8     // Catch:{ Throwable -> 0x0086 }
            goto L_0x0078
        L_0x0086:
            r1 = move-exception
            java.lang.String r6 = "DBMgr"
            r7 = 2
            java.lang.Object[] r7 = new java.lang.Object[r7]     // Catch:{ all -> 0x00d5 }
            r8 = 0
            java.lang.String r9 = "db delete error:"
            r7[r8] = r9     // Catch:{ all -> 0x00d5 }
            r8 = 1
            r7[r8] = r1     // Catch:{ all -> 0x00d5 }
            com.alibaba.analytics.utils.Logger.w((java.lang.String) r6, (java.lang.Object[]) r7)     // Catch:{ all -> 0x00d5 }
            r0.setTransactionSuccessful()     // Catch:{ Throwable -> 0x00e6 }
        L_0x009c:
            r0.endTransaction()     // Catch:{ Throwable -> 0x00e8 }
        L_0x009f:
            com.alibaba.analytics.core.db.SqliteHelper r6 = r14.mHelper     // Catch:{ all -> 0x00d2 }
            r6.closeWritableDatabase(r0)     // Catch:{ all -> 0x00d2 }
        L_0x00a4:
            if (r15 == 0) goto L_0x00c0
            java.lang.String r6 = ""
            r7 = 2
            java.lang.Object[] r7 = new java.lang.Object[r7]     // Catch:{ all -> 0x00d2 }
            r8 = 0
            java.lang.String r9 = "entities.size"
            r7[r8] = r9     // Catch:{ all -> 0x00d2 }
            r8 = 1
            int r9 = r15.size()     // Catch:{ all -> 0x00d2 }
            java.lang.Integer r9 = java.lang.Integer.valueOf(r9)     // Catch:{ all -> 0x00d2 }
            r7[r8] = r9     // Catch:{ all -> 0x00d2 }
            com.alibaba.analytics.utils.Logger.d((java.lang.String) r6, (java.lang.Object[]) r7)     // Catch:{ all -> 0x00d2 }
        L_0x00c0:
            int r6 = r15.size()     // Catch:{ all -> 0x00d2 }
            goto L_0x000b
        L_0x00c6:
            r0.setTransactionSuccessful()     // Catch:{ Throwable -> 0x00e2 }
        L_0x00c9:
            r0.endTransaction()     // Catch:{ Throwable -> 0x00e4 }
        L_0x00cc:
            com.alibaba.analytics.core.db.SqliteHelper r6 = r14.mHelper     // Catch:{ all -> 0x00d2 }
            r6.closeWritableDatabase(r0)     // Catch:{ all -> 0x00d2 }
            goto L_0x00a4
        L_0x00d2:
            r6 = move-exception
            monitor-exit(r14)
            throw r6
        L_0x00d5:
            r6 = move-exception
            r0.setTransactionSuccessful()     // Catch:{ Throwable -> 0x00ea }
        L_0x00d9:
            r0.endTransaction()     // Catch:{ Throwable -> 0x00ec }
        L_0x00dc:
            com.alibaba.analytics.core.db.SqliteHelper r7 = r14.mHelper     // Catch:{ all -> 0x00d2 }
            r7.closeWritableDatabase(r0)     // Catch:{ all -> 0x00d2 }
            throw r6     // Catch:{ all -> 0x00d2 }
        L_0x00e2:
            r6 = move-exception
            goto L_0x00c9
        L_0x00e4:
            r6 = move-exception
            goto L_0x00cc
        L_0x00e6:
            r6 = move-exception
            goto L_0x009c
        L_0x00e8:
            r6 = move-exception
            goto L_0x009f
        L_0x00ea:
            r7 = move-exception
            goto L_0x00d9
        L_0x00ec:
            r7 = move-exception
            goto L_0x00dc
        */
        throw new UnsupportedOperationException("Method not decompiled: com.alibaba.analytics.core.db.DBMgr.delete(java.util.List):int");
    }

    public int delete(Entity entity) {
        ArrayList<Entity> entities = new ArrayList<>(1);
        entities.add(entity);
        return delete((List<? extends Entity>) entities);
    }

    public void update(Entity entity) {
        ArrayList<Entity> entities = new ArrayList<>(1);
        entities.add(entity);
        update((List<? extends Entity>) entities);
    }

    /* JADX WARNING: Code restructure failed: missing block: B:27:0x00a3, code lost:
        r4 = move-exception;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:29:?, code lost:
        r4.printStackTrace();
     */
    /* JADX WARNING: Code restructure failed: missing block: B:57:0x014b, code lost:
        r10 = move-exception;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:59:?, code lost:
        r3.setTransactionSuccessful();
     */
    /* JADX WARNING: Code restructure failed: missing block: B:66:0x015a, code lost:
        r4 = move-exception;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:67:0x015b, code lost:
        com.alibaba.analytics.utils.Logger.w(TAG, "setTransactionSuccessful", r4);
     */
    /* JADX WARNING: Failed to process nested try/catch */
    /* JADX WARNING: Removed duplicated region for block: B:57:0x014b A[ExcHandler: all (r10v10 'th' java.lang.Throwable A[CUSTOM_DECLARE]), Splitter:B:15:0x0046] */
    /* JADX WARNING: Unknown top exception splitter block from list: {B:33:0x00ac=Splitter:B:33:0x00ac, B:44:0x00ed=Splitter:B:44:0x00ed, B:60:0x014f=Splitter:B:60:0x014f} */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public synchronized void update(java.util.List<? extends com.alibaba.analytics.core.db.Entity> r19) {
        /*
            r18 = this;
            monitor-enter(r18)
            if (r19 == 0) goto L_0x0009
            int r10 = r19.size()     // Catch:{ all -> 0x0043 }
            if (r10 != 0) goto L_0x000b
        L_0x0009:
            monitor-exit(r18)
            return
        L_0x000b:
            r10 = 0
            r0 = r19
            java.lang.Object r10 = r0.get(r10)     // Catch:{ all -> 0x0043 }
            com.alibaba.analytics.core.db.Entity r10 = (com.alibaba.analytics.core.db.Entity) r10     // Catch:{ all -> 0x0043 }
            java.lang.Class r10 = r10.getClass()     // Catch:{ all -> 0x0043 }
            r0 = r18
            java.lang.String r9 = r0.getTablename(r10)     // Catch:{ all -> 0x0043 }
            r10 = 0
            r0 = r19
            java.lang.Object r10 = r0.get(r10)     // Catch:{ all -> 0x0043 }
            com.alibaba.analytics.core.db.Entity r10 = (com.alibaba.analytics.core.db.Entity) r10     // Catch:{ all -> 0x0043 }
            java.lang.Class r10 = r10.getClass()     // Catch:{ all -> 0x0043 }
            r0 = r18
            android.database.sqlite.SQLiteDatabase r3 = r0.checkTableAvailable(r10, r9)     // Catch:{ all -> 0x0043 }
            if (r3 != 0) goto L_0x0046
            java.lang.String r10 = "DBMgr"
            r11 = 1
            java.lang.Object[] r11 = new java.lang.Object[r11]     // Catch:{ all -> 0x0043 }
            r12 = 0
            java.lang.String r13 = "[update] db is null"
            r11[r12] = r13     // Catch:{ all -> 0x0043 }
            com.alibaba.analytics.utils.Logger.w((java.lang.String) r10, (java.lang.Object[]) r11)     // Catch:{ all -> 0x0043 }
            goto L_0x0009
        L_0x0043:
            r10 = move-exception
            monitor-exit(r18)
            throw r10
        L_0x0046:
            r3.beginTransaction()     // Catch:{ Exception -> 0x00a8, all -> 0x014b }
            r10 = 0
            r0 = r19
            java.lang.Object r10 = r0.get(r10)     // Catch:{ Exception -> 0x00a8, all -> 0x014b }
            com.alibaba.analytics.core.db.Entity r10 = (com.alibaba.analytics.core.db.Entity) r10     // Catch:{ Exception -> 0x00a8, all -> 0x014b }
            java.lang.Class r10 = r10.getClass()     // Catch:{ Exception -> 0x00a8, all -> 0x014b }
            r0 = r18
            java.util.List r6 = r0.getAllFields(r10)     // Catch:{ Exception -> 0x00a8, all -> 0x014b }
            r7 = 0
        L_0x005d:
            int r10 = r19.size()     // Catch:{ Exception -> 0x00a8, all -> 0x014b }
            if (r7 >= r10) goto L_0x00ea
            android.content.ContentValues r2 = new android.content.ContentValues     // Catch:{ Exception -> 0x00a8, all -> 0x014b }
            r2.<init>()     // Catch:{ Exception -> 0x00a8, all -> 0x014b }
            r8 = 0
        L_0x0069:
            int r10 = r6.size()     // Catch:{ Exception -> 0x00a8, all -> 0x014b }
            if (r8 >= r10) goto L_0x00b8
            java.lang.Object r5 = r6.get(r8)     // Catch:{ Exception -> 0x00a8, all -> 0x014b }
            java.lang.reflect.Field r5 = (java.lang.reflect.Field) r5     // Catch:{ Exception -> 0x00a8, all -> 0x014b }
            r10 = 1
            r5.setAccessible(r10)     // Catch:{ Exception -> 0x00a3, all -> 0x014b }
            r0 = r18
            java.lang.String r10 = r0.getColumnName(r5)     // Catch:{ Exception -> 0x00a3, all -> 0x014b }
            java.lang.StringBuilder r11 = new java.lang.StringBuilder     // Catch:{ Exception -> 0x00a3, all -> 0x014b }
            r11.<init>()     // Catch:{ Exception -> 0x00a3, all -> 0x014b }
            r0 = r19
            java.lang.Object r12 = r0.get(r7)     // Catch:{ Exception -> 0x00a3, all -> 0x014b }
            java.lang.Object r12 = r5.get(r12)     // Catch:{ Exception -> 0x00a3, all -> 0x014b }
            java.lang.StringBuilder r11 = r11.append(r12)     // Catch:{ Exception -> 0x00a3, all -> 0x014b }
            java.lang.String r12 = ""
            java.lang.StringBuilder r11 = r11.append(r12)     // Catch:{ Exception -> 0x00a3, all -> 0x014b }
            java.lang.String r11 = r11.toString()     // Catch:{ Exception -> 0x00a3, all -> 0x014b }
            r2.put(r10, r11)     // Catch:{ Exception -> 0x00a3, all -> 0x014b }
        L_0x00a0:
            int r8 = r8 + 1
            goto L_0x0069
        L_0x00a3:
            r4 = move-exception
            r4.printStackTrace()     // Catch:{ Exception -> 0x00a8, all -> 0x014b }
            goto L_0x00a0
        L_0x00a8:
            r10 = move-exception
            r3.setTransactionSuccessful()     // Catch:{ Exception -> 0x0121 }
        L_0x00ac:
            r3.endTransaction()     // Catch:{ Exception -> 0x0136 }
        L_0x00af:
            r0 = r18
            com.alibaba.analytics.core.db.SqliteHelper r10 = r0.mHelper     // Catch:{ all -> 0x0043 }
            r10.closeWritableDatabase(r3)     // Catch:{ all -> 0x0043 }
            goto L_0x0009
        L_0x00b8:
            java.lang.String r11 = "_id=?"
            r10 = 1
            java.lang.String[] r12 = new java.lang.String[r10]     // Catch:{ Exception -> 0x00a8, all -> 0x014b }
            r13 = 0
            java.lang.StringBuilder r14 = new java.lang.StringBuilder     // Catch:{ Exception -> 0x00a8, all -> 0x014b }
            r14.<init>()     // Catch:{ Exception -> 0x00a8, all -> 0x014b }
            r0 = r19
            java.lang.Object r10 = r0.get(r7)     // Catch:{ Exception -> 0x00a8, all -> 0x014b }
            com.alibaba.analytics.core.db.Entity r10 = (com.alibaba.analytics.core.db.Entity) r10     // Catch:{ Exception -> 0x00a8, all -> 0x014b }
            long r0 = r10._id     // Catch:{ Exception -> 0x00a8, all -> 0x014b }
            r16 = r0
            r0 = r16
            java.lang.StringBuilder r10 = r14.append(r0)     // Catch:{ Exception -> 0x00a8, all -> 0x014b }
            java.lang.String r14 = ""
            java.lang.StringBuilder r10 = r10.append(r14)     // Catch:{ Exception -> 0x00a8, all -> 0x014b }
            java.lang.String r10 = r10.toString()     // Catch:{ Exception -> 0x00a8, all -> 0x014b }
            r12[r13] = r10     // Catch:{ Exception -> 0x00a8, all -> 0x014b }
            r3.update(r9, r2, r11, r12)     // Catch:{ Exception -> 0x00a8, all -> 0x014b }
            int r7 = r7 + 1
            goto L_0x005d
        L_0x00ea:
            r3.setTransactionSuccessful()     // Catch:{ Exception -> 0x00f9 }
        L_0x00ed:
            r3.endTransaction()     // Catch:{ Exception -> 0x010d }
        L_0x00f0:
            r0 = r18
            com.alibaba.analytics.core.db.SqliteHelper r10 = r0.mHelper     // Catch:{ all -> 0x0043 }
            r10.closeWritableDatabase(r3)     // Catch:{ all -> 0x0043 }
            goto L_0x0009
        L_0x00f9:
            r4 = move-exception
            java.lang.String r10 = "DBMgr"
            r11 = 2
            java.lang.Object[] r11 = new java.lang.Object[r11]     // Catch:{ all -> 0x0043 }
            r12 = 0
            java.lang.String r13 = "setTransactionSuccessful"
            r11[r12] = r13     // Catch:{ all -> 0x0043 }
            r12 = 1
            r11[r12] = r4     // Catch:{ all -> 0x0043 }
            com.alibaba.analytics.utils.Logger.w((java.lang.String) r10, (java.lang.Object[]) r11)     // Catch:{ all -> 0x0043 }
            goto L_0x00ed
        L_0x010d:
            r4 = move-exception
            java.lang.String r10 = "DBMgr"
            r11 = 2
            java.lang.Object[] r11 = new java.lang.Object[r11]     // Catch:{ all -> 0x0043 }
            r12 = 0
            java.lang.String r13 = "endTransaction"
            r11[r12] = r13     // Catch:{ all -> 0x0043 }
            r12 = 1
            r11[r12] = r4     // Catch:{ all -> 0x0043 }
            com.alibaba.analytics.utils.Logger.w((java.lang.String) r10, (java.lang.Object[]) r11)     // Catch:{ all -> 0x0043 }
            goto L_0x00f0
        L_0x0121:
            r4 = move-exception
            java.lang.String r10 = "DBMgr"
            r11 = 2
            java.lang.Object[] r11 = new java.lang.Object[r11]     // Catch:{ all -> 0x0043 }
            r12 = 0
            java.lang.String r13 = "setTransactionSuccessful"
            r11[r12] = r13     // Catch:{ all -> 0x0043 }
            r12 = 1
            r11[r12] = r4     // Catch:{ all -> 0x0043 }
            com.alibaba.analytics.utils.Logger.w((java.lang.String) r10, (java.lang.Object[]) r11)     // Catch:{ all -> 0x0043 }
            goto L_0x00ac
        L_0x0136:
            r4 = move-exception
            java.lang.String r10 = "DBMgr"
            r11 = 2
            java.lang.Object[] r11 = new java.lang.Object[r11]     // Catch:{ all -> 0x0043 }
            r12 = 0
            java.lang.String r13 = "endTransaction"
            r11[r12] = r13     // Catch:{ all -> 0x0043 }
            r12 = 1
            r11[r12] = r4     // Catch:{ all -> 0x0043 }
            com.alibaba.analytics.utils.Logger.w((java.lang.String) r10, (java.lang.Object[]) r11)     // Catch:{ all -> 0x0043 }
            goto L_0x00af
        L_0x014b:
            r10 = move-exception
            r3.setTransactionSuccessful()     // Catch:{ Exception -> 0x015a }
        L_0x014f:
            r3.endTransaction()     // Catch:{ Exception -> 0x016e }
        L_0x0152:
            r0 = r18
            com.alibaba.analytics.core.db.SqliteHelper r11 = r0.mHelper     // Catch:{ all -> 0x0043 }
            r11.closeWritableDatabase(r3)     // Catch:{ all -> 0x0043 }
            throw r10     // Catch:{ all -> 0x0043 }
        L_0x015a:
            r4 = move-exception
            java.lang.String r11 = "DBMgr"
            r12 = 2
            java.lang.Object[] r12 = new java.lang.Object[r12]     // Catch:{ all -> 0x0043 }
            r13 = 0
            java.lang.String r14 = "setTransactionSuccessful"
            r12[r13] = r14     // Catch:{ all -> 0x0043 }
            r13 = 1
            r12[r13] = r4     // Catch:{ all -> 0x0043 }
            com.alibaba.analytics.utils.Logger.w((java.lang.String) r11, (java.lang.Object[]) r12)     // Catch:{ all -> 0x0043 }
            goto L_0x014f
        L_0x016e:
            r4 = move-exception
            java.lang.String r11 = "DBMgr"
            r12 = 2
            java.lang.Object[] r12 = new java.lang.Object[r12]     // Catch:{ all -> 0x0043 }
            r13 = 0
            java.lang.String r14 = "endTransaction"
            r12[r13] = r14     // Catch:{ all -> 0x0043 }
            r13 = 1
            r12[r13] = r4     // Catch:{ all -> 0x0043 }
            com.alibaba.analytics.utils.Logger.w((java.lang.String) r11, (java.lang.Object[]) r12)     // Catch:{ all -> 0x0043 }
            goto L_0x0152
        */
        throw new UnsupportedOperationException("Method not decompiled: com.alibaba.analytics.core.db.DBMgr.update(java.util.List):void");
    }

    /* JADX WARNING: Code restructure failed: missing block: B:30:0x00da, code lost:
        r4 = move-exception;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:32:?, code lost:
        r4.printStackTrace();
     */
    /* JADX WARNING: Code restructure failed: missing block: B:57:0x0150, code lost:
        r11 = move-exception;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:59:?, code lost:
        r3.setTransactionSuccessful();
     */
    /* JADX WARNING: Code restructure failed: missing block: B:66:0x015f, code lost:
        r4 = move-exception;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:67:0x0160, code lost:
        com.alibaba.analytics.utils.Logger.w(TAG, "setTransactionSuccessful", r4);
     */
    /* JADX WARNING: Failed to process nested try/catch */
    /* JADX WARNING: Removed duplicated region for block: B:57:0x0150 A[ExcHandler: all (r11v10 'th' java.lang.Throwable A[CUSTOM_DECLARE]), Splitter:B:15:0x0046] */
    /* JADX WARNING: Unknown top exception splitter block from list: {B:36:0x00e4=Splitter:B:36:0x00e4, B:60:0x0154=Splitter:B:60:0x0154, B:48:0x011c=Splitter:B:48:0x011c} */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public synchronized void updateLogPriority(java.util.List<? extends com.alibaba.analytics.core.db.Entity> r19) {
        /*
            r18 = this;
            monitor-enter(r18)
            if (r19 == 0) goto L_0x0009
            int r11 = r19.size()     // Catch:{ all -> 0x0043 }
            if (r11 != 0) goto L_0x000b
        L_0x0009:
            monitor-exit(r18)
            return
        L_0x000b:
            r11 = 0
            r0 = r19
            java.lang.Object r11 = r0.get(r11)     // Catch:{ all -> 0x0043 }
            com.alibaba.analytics.core.db.Entity r11 = (com.alibaba.analytics.core.db.Entity) r11     // Catch:{ all -> 0x0043 }
            java.lang.Class r11 = r11.getClass()     // Catch:{ all -> 0x0043 }
            r0 = r18
            java.lang.String r10 = r0.getTablename(r11)     // Catch:{ all -> 0x0043 }
            r11 = 0
            r0 = r19
            java.lang.Object r11 = r0.get(r11)     // Catch:{ all -> 0x0043 }
            com.alibaba.analytics.core.db.Entity r11 = (com.alibaba.analytics.core.db.Entity) r11     // Catch:{ all -> 0x0043 }
            java.lang.Class r11 = r11.getClass()     // Catch:{ all -> 0x0043 }
            r0 = r18
            android.database.sqlite.SQLiteDatabase r3 = r0.checkTableAvailable(r11, r10)     // Catch:{ all -> 0x0043 }
            if (r3 != 0) goto L_0x0046
            java.lang.String r11 = "DBMgr"
            r12 = 1
            java.lang.Object[] r12 = new java.lang.Object[r12]     // Catch:{ all -> 0x0043 }
            r13 = 0
            java.lang.String r14 = "[update] db is null"
            r12[r13] = r14     // Catch:{ all -> 0x0043 }
            com.alibaba.analytics.utils.Logger.w((java.lang.String) r11, (java.lang.Object[]) r12)     // Catch:{ all -> 0x0043 }
            goto L_0x0009
        L_0x0043:
            r11 = move-exception
            monitor-exit(r18)
            throw r11
        L_0x0046:
            r3.beginTransaction()     // Catch:{ Exception -> 0x0118, all -> 0x0150 }
            r11 = 0
            r0 = r19
            java.lang.Object r11 = r0.get(r11)     // Catch:{ Exception -> 0x0118, all -> 0x0150 }
            com.alibaba.analytics.core.db.Entity r11 = (com.alibaba.analytics.core.db.Entity) r11     // Catch:{ Exception -> 0x0118, all -> 0x0150 }
            java.lang.Class r11 = r11.getClass()     // Catch:{ Exception -> 0x0118, all -> 0x0150 }
            r0 = r18
            java.util.List r7 = r0.getAllFields(r11)     // Catch:{ Exception -> 0x0118, all -> 0x0150 }
            r8 = 0
        L_0x005d:
            int r11 = r19.size()     // Catch:{ Exception -> 0x0118, all -> 0x0150 }
            if (r8 >= r11) goto L_0x00e1
            android.content.ContentValues r2 = new android.content.ContentValues     // Catch:{ Exception -> 0x0118, all -> 0x0150 }
            r2.<init>()     // Catch:{ Exception -> 0x0118, all -> 0x0150 }
            r9 = 0
        L_0x0069:
            int r11 = r7.size()     // Catch:{ Exception -> 0x0118, all -> 0x0150 }
            if (r9 >= r11) goto L_0x00d7
            java.lang.Object r5 = r7.get(r9)     // Catch:{ Exception -> 0x0118, all -> 0x0150 }
            java.lang.reflect.Field r5 = (java.lang.reflect.Field) r5     // Catch:{ Exception -> 0x0118, all -> 0x0150 }
            r0 = r18
            java.lang.String r6 = r0.getColumnName(r5)     // Catch:{ Exception -> 0x0118, all -> 0x0150 }
            if (r6 == 0) goto L_0x00de
            java.lang.String r11 = "priority"
            boolean r11 = r6.equalsIgnoreCase(r11)     // Catch:{ Exception -> 0x0118, all -> 0x0150 }
            if (r11 == 0) goto L_0x00de
            r11 = 1
            r5.setAccessible(r11)     // Catch:{ Exception -> 0x00da, all -> 0x0150 }
            java.lang.StringBuilder r11 = new java.lang.StringBuilder     // Catch:{ Exception -> 0x00da, all -> 0x0150 }
            r11.<init>()     // Catch:{ Exception -> 0x00da, all -> 0x0150 }
            r0 = r19
            java.lang.Object r12 = r0.get(r8)     // Catch:{ Exception -> 0x00da, all -> 0x0150 }
            java.lang.Object r12 = r5.get(r12)     // Catch:{ Exception -> 0x00da, all -> 0x0150 }
            java.lang.StringBuilder r11 = r11.append(r12)     // Catch:{ Exception -> 0x00da, all -> 0x0150 }
            java.lang.String r12 = ""
            java.lang.StringBuilder r11 = r11.append(r12)     // Catch:{ Exception -> 0x00da, all -> 0x0150 }
            java.lang.String r11 = r11.toString()     // Catch:{ Exception -> 0x00da, all -> 0x0150 }
            r2.put(r6, r11)     // Catch:{ Exception -> 0x00da, all -> 0x0150 }
            java.lang.String r12 = "_id=?"
            r11 = 1
            java.lang.String[] r13 = new java.lang.String[r11]     // Catch:{ Exception -> 0x00da, all -> 0x0150 }
            r14 = 0
            java.lang.StringBuilder r15 = new java.lang.StringBuilder     // Catch:{ Exception -> 0x00da, all -> 0x0150 }
            r15.<init>()     // Catch:{ Exception -> 0x00da, all -> 0x0150 }
            r0 = r19
            java.lang.Object r11 = r0.get(r8)     // Catch:{ Exception -> 0x00da, all -> 0x0150 }
            com.alibaba.analytics.core.db.Entity r11 = (com.alibaba.analytics.core.db.Entity) r11     // Catch:{ Exception -> 0x00da, all -> 0x0150 }
            long r0 = r11._id     // Catch:{ Exception -> 0x00da, all -> 0x0150 }
            r16 = r0
            java.lang.StringBuilder r11 = r15.append(r16)     // Catch:{ Exception -> 0x00da, all -> 0x0150 }
            java.lang.String r15 = ""
            java.lang.StringBuilder r11 = r11.append(r15)     // Catch:{ Exception -> 0x00da, all -> 0x0150 }
            java.lang.String r11 = r11.toString()     // Catch:{ Exception -> 0x00da, all -> 0x0150 }
            r13[r14] = r11     // Catch:{ Exception -> 0x00da, all -> 0x0150 }
            r3.update(r10, r2, r12, r13)     // Catch:{ Exception -> 0x00da, all -> 0x0150 }
        L_0x00d7:
            int r8 = r8 + 1
            goto L_0x005d
        L_0x00da:
            r4 = move-exception
            r4.printStackTrace()     // Catch:{ Exception -> 0x0118, all -> 0x0150 }
        L_0x00de:
            int r9 = r9 + 1
            goto L_0x0069
        L_0x00e1:
            r3.setTransactionSuccessful()     // Catch:{ Exception -> 0x00f0 }
        L_0x00e4:
            r3.endTransaction()     // Catch:{ Exception -> 0x0104 }
        L_0x00e7:
            r0 = r18
            com.alibaba.analytics.core.db.SqliteHelper r11 = r0.mHelper     // Catch:{ all -> 0x0043 }
            r11.closeWritableDatabase(r3)     // Catch:{ all -> 0x0043 }
            goto L_0x0009
        L_0x00f0:
            r4 = move-exception
            java.lang.String r11 = "DBMgr"
            r12 = 2
            java.lang.Object[] r12 = new java.lang.Object[r12]     // Catch:{ all -> 0x0043 }
            r13 = 0
            java.lang.String r14 = "setTransactionSuccessful"
            r12[r13] = r14     // Catch:{ all -> 0x0043 }
            r13 = 1
            r12[r13] = r4     // Catch:{ all -> 0x0043 }
            com.alibaba.analytics.utils.Logger.w((java.lang.String) r11, (java.lang.Object[]) r12)     // Catch:{ all -> 0x0043 }
            goto L_0x00e4
        L_0x0104:
            r4 = move-exception
            java.lang.String r11 = "DBMgr"
            r12 = 2
            java.lang.Object[] r12 = new java.lang.Object[r12]     // Catch:{ all -> 0x0043 }
            r13 = 0
            java.lang.String r14 = "endTransaction"
            r12[r13] = r14     // Catch:{ all -> 0x0043 }
            r13 = 1
            r12[r13] = r4     // Catch:{ all -> 0x0043 }
            com.alibaba.analytics.utils.Logger.w((java.lang.String) r11, (java.lang.Object[]) r12)     // Catch:{ all -> 0x0043 }
            goto L_0x00e7
        L_0x0118:
            r11 = move-exception
            r3.setTransactionSuccessful()     // Catch:{ Exception -> 0x0128 }
        L_0x011c:
            r3.endTransaction()     // Catch:{ Exception -> 0x013c }
        L_0x011f:
            r0 = r18
            com.alibaba.analytics.core.db.SqliteHelper r11 = r0.mHelper     // Catch:{ all -> 0x0043 }
            r11.closeWritableDatabase(r3)     // Catch:{ all -> 0x0043 }
            goto L_0x0009
        L_0x0128:
            r4 = move-exception
            java.lang.String r11 = "DBMgr"
            r12 = 2
            java.lang.Object[] r12 = new java.lang.Object[r12]     // Catch:{ all -> 0x0043 }
            r13 = 0
            java.lang.String r14 = "setTransactionSuccessful"
            r12[r13] = r14     // Catch:{ all -> 0x0043 }
            r13 = 1
            r12[r13] = r4     // Catch:{ all -> 0x0043 }
            com.alibaba.analytics.utils.Logger.w((java.lang.String) r11, (java.lang.Object[]) r12)     // Catch:{ all -> 0x0043 }
            goto L_0x011c
        L_0x013c:
            r4 = move-exception
            java.lang.String r11 = "DBMgr"
            r12 = 2
            java.lang.Object[] r12 = new java.lang.Object[r12]     // Catch:{ all -> 0x0043 }
            r13 = 0
            java.lang.String r14 = "endTransaction"
            r12[r13] = r14     // Catch:{ all -> 0x0043 }
            r13 = 1
            r12[r13] = r4     // Catch:{ all -> 0x0043 }
            com.alibaba.analytics.utils.Logger.w((java.lang.String) r11, (java.lang.Object[]) r12)     // Catch:{ all -> 0x0043 }
            goto L_0x011f
        L_0x0150:
            r11 = move-exception
            r3.setTransactionSuccessful()     // Catch:{ Exception -> 0x015f }
        L_0x0154:
            r3.endTransaction()     // Catch:{ Exception -> 0x0173 }
        L_0x0157:
            r0 = r18
            com.alibaba.analytics.core.db.SqliteHelper r12 = r0.mHelper     // Catch:{ all -> 0x0043 }
            r12.closeWritableDatabase(r3)     // Catch:{ all -> 0x0043 }
            throw r11     // Catch:{ all -> 0x0043 }
        L_0x015f:
            r4 = move-exception
            java.lang.String r12 = "DBMgr"
            r13 = 2
            java.lang.Object[] r13 = new java.lang.Object[r13]     // Catch:{ all -> 0x0043 }
            r14 = 0
            java.lang.String r15 = "setTransactionSuccessful"
            r13[r14] = r15     // Catch:{ all -> 0x0043 }
            r14 = 1
            r13[r14] = r4     // Catch:{ all -> 0x0043 }
            com.alibaba.analytics.utils.Logger.w((java.lang.String) r12, (java.lang.Object[]) r13)     // Catch:{ all -> 0x0043 }
            goto L_0x0154
        L_0x0173:
            r4 = move-exception
            java.lang.String r12 = "DBMgr"
            r13 = 2
            java.lang.Object[] r13 = new java.lang.Object[r13]     // Catch:{ all -> 0x0043 }
            r14 = 0
            java.lang.String r15 = "endTransaction"
            r13[r14] = r15     // Catch:{ all -> 0x0043 }
            r14 = 1
            r13[r14] = r4     // Catch:{ all -> 0x0043 }
            com.alibaba.analytics.utils.Logger.w((java.lang.String) r12, (java.lang.Object[]) r13)     // Catch:{ all -> 0x0043 }
            goto L_0x0157
        */
        throw new UnsupportedOperationException("Method not decompiled: com.alibaba.analytics.core.db.DBMgr.updateLogPriority(java.util.List):void");
    }

    /* JADX INFO: finally extract failed */
    public synchronized void execSQL(Class<? extends Entity> cls, String sql) {
        SQLiteDatabase db;
        if (cls != null) {
            if (!TextUtils.isEmpty(sql) && (db = checkTableAvailable(cls, getTablename(cls))) != null) {
                try {
                    db.execSQL(sql);
                    this.mHelper.closeWritableDatabase(db);
                } catch (Throwable th) {
                    this.mHelper.closeWritableDatabase(db);
                    throw th;
                }
            }
        }
    }

    public synchronized int delete(Class<? extends Entity> cls, String whereClause, String[] whereArgs) {
        int ret;
        Logger.d((String) null, "whereArgs", whereArgs, "", "whereArgs", whereArgs);
        int ret2 = 0;
        if (cls != null) {
            SQLiteDatabase db = checkTableAvailable(cls, getTablename(cls));
            if (db == null) {
                ret = 0;
            } else {
                try {
                    ret2 = db.delete(getTablename(cls), whereClause, whereArgs);
                    this.mHelper.closeWritableDatabase(db);
                } catch (Throwable th) {
                    this.mHelper.closeWritableDatabase(db);
                    throw th;
                }
            }
        }
        ret = ret2;
        return ret;
    }

    public String getTablename(Class<?> cls) {
        String ret;
        if (cls == null) {
            return null;
        }
        if (this.mClsTableNameMap.containsKey(cls)) {
            return this.mClsTableNameMap.get(cls);
        }
        TableName tableName = (TableName) cls.getAnnotation(TableName.class);
        if (tableName == null || TextUtils.isEmpty(tableName.value())) {
            ret = cls.getName().replace(".", "_");
        } else {
            ret = tableName.value();
        }
        this.mClsTableNameMap.put(cls, ret);
        return ret;
    }

    private SQLiteDatabase checkTableAvailable(Class<? extends Entity> cls, String tableName) {
        SQLiteDatabase db = this.mHelper.getWritableDatabase();
        Boolean hasCheckdb = true;
        if (this.mCheckdbMap.get(tableName) == null || !this.mCheckdbMap.get(tableName).booleanValue()) {
            hasCheckdb = false;
        }
        if (!(cls == null || hasCheckdb.booleanValue() || db == null)) {
            List<Field> fields = getAllFields(cls);
            ArrayList<Field> updateColumns = new ArrayList<>();
            String sql = " SELECT * FROM " + tableName + " LIMIT 0";
            boolean needCreate = false;
            Cursor cursor = null;
            if (fields != null) {
                try {
                    cursor = db.rawQuery(sql, (String[]) null);
                } catch (Exception e) {
                    Logger.d(TAG, "has not create talbe:", tableName);
                }
                if (cursor == null) {
                    needCreate = true;
                }
                for (int i = 0; i < fields.size(); i++) {
                    Field field = fields.get(i);
                    if (!"_id".equalsIgnoreCase(getColumnName(field)) && (needCreate || (cursor != null && cursor.getColumnIndex(getColumnName(field)) == -1))) {
                        updateColumns.add(field);
                    }
                }
                this.mHelper.closeCursor(cursor);
            }
            if (needCreate) {
                createTable(db, tableName, updateColumns);
            } else if (updateColumns.size() > 0) {
                updateTable(db, tableName, updateColumns);
            }
            this.mCheckdbMap.put(tableName, true);
        }
        return db;
    }

    private void updateTable(SQLiteDatabase db, String tableName, ArrayList<Field> updateColumns) {
        String updateDbSql = "ALTER TABLE " + tableName + " ADD COLUMN ";
        StringBuilder builder = new StringBuilder();
        for (int i = 0; i < updateColumns.size(); i++) {
            builder.append(updateDbSql);
            builder.append(getColumnName(updateColumns.get(i))).append(" ").append(getSQLType(updateColumns.get(i).getType()));
            String updateSql = builder.toString();
            try {
                db.execSQL(updateSql);
            } catch (Exception e) {
                Logger.w(TAG, "update db error...", e);
            }
            builder.delete(0, updateSql.length());
            Logger.d(TAG, null, "excute sql:", updateSql);
        }
    }

    private void createTable(SQLiteDatabase db, String tableName, ArrayList<Field> updateColumns) {
        StringBuilder builder = new StringBuilder();
        builder.append("CREATE TABLE IF NOT EXISTS " + tableName + " (" + "_id" + " INTEGER PRIMARY KEY AUTOINCREMENT ,");
        if (updateColumns.size() > 0) {
            for (int i = 0; i < updateColumns.size(); i++) {
                if (i != 0) {
                    builder.append(",");
                }
                Class type = updateColumns.get(i).getType();
                builder.append(" ").append(getColumnName(updateColumns.get(i))).append(" ").append(getSQLType(type)).append(" ").append(getDefaultValue(type));
            }
        }
        builder.append(" );");
        String createSql = builder.toString();
        Logger.d(TAG, "excute sql:", createSql);
        try {
            db.execSQL(createSql);
        } catch (Exception e) {
            Logger.w(TAG, "create db error", e);
        }
    }

    private String getSQLType(Class<?> cls) {
        if (cls == Long.TYPE || cls == Integer.TYPE || cls == Long.class) {
            return "INTEGER";
        }
        return "TEXT";
    }

    public synchronized int count(Class<? extends Entity> cls) {
        int count;
        int count2 = 0;
        if (cls == null) {
            count = 0;
        } else {
            String tableName = getTablename(cls);
            SQLiteDatabase db = checkTableAvailable(cls, tableName);
            if (db != null) {
                try {
                    Cursor cursor = db.rawQuery("SELECT count(*) FROM " + tableName, (String[]) null);
                    if (cursor != null) {
                        cursor.moveToFirst();
                        count2 = cursor.getInt(0);
                    }
                    this.mHelper.closeCursor(cursor);
                    this.mHelper.closeWritableDatabase(db);
                } catch (Throwable th) {
                    this.mHelper.closeCursor((Cursor) null);
                    this.mHelper.closeWritableDatabase(db);
                    throw th;
                }
            } else {
                Logger.d(TAG, "db is null");
            }
            count = count2;
        }
        return count;
    }

    public synchronized void clear(Class<? extends Entity> cls) {
        if (cls != null) {
            clear(getTablename(cls));
        }
    }

    public synchronized void clear(String tablename) {
        if (tablename != null) {
            try {
                SQLiteDatabase db = this.mHelper.getWritableDatabase();
                if (db != null) {
                    db.delete(tablename, (String) null, (String[]) null);
                    this.mHelper.closeWritableDatabase(db);
                }
            } catch (Exception e) {
                Logger.e("delete db data", e, new Object[0]);
            }
        }
        return;
    }

    private List<Field> getAllFields(Class cls) {
        if (this.mClsFieldsMap.containsKey(cls)) {
            return this.mClsFieldsMap.get(cls);
        }
        List<Field> fields = Collections.emptyList();
        if (cls != null) {
            fields = new ArrayList<>();
            Field[] selfFields = cls.getDeclaredFields();
            for (Field field : selfFields) {
                if (field.getAnnotation(Ingore.class) == null && !field.isSynthetic()) {
                    field.setAccessible(true);
                    fields.add(field);
                }
            }
            if (!(cls.getSuperclass() == null || cls.getSuperclass() == Object.class)) {
                fields.addAll(getAllFields(cls.getSuperclass()));
            }
            this.mClsFieldsMap.put(cls, fields);
        }
        return fields;
    }

    private String getColumnName(Field field) {
        String ret;
        if (this.mFieldNameMap.containsKey(field)) {
            return this.mFieldNameMap.get(field);
        }
        Column column = (Column) field.getAnnotation(Column.class);
        if (column == null || TextUtils.isEmpty(column.value())) {
            ret = field.getName();
        } else {
            ret = column.value();
        }
        this.mFieldNameMap.put(field, ret);
        return ret;
    }

    private String getDefaultValue(Class cls) {
        if (cls == Long.TYPE || cls == Integer.TYPE || cls == Long.class) {
            return "default 0";
        }
        return "default \"\"";
    }

    public double getDbFileSize() {
        File dbFile = Variables.getInstance().getContext().getDatabasePath(Constants.Database.DATABASE_NAME);
        if (dbFile != null) {
            return (((double) dbFile.length()) / 1024.0d) / 1024.0d;
        }
        return ClientTraceData.b.f47a;
    }
}
