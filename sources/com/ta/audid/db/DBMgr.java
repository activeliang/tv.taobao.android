package com.ta.audid.db;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.text.TextUtils;
import com.ta.audid.db.annotation.Column;
import com.ta.audid.db.annotation.Ingore;
import com.ta.audid.db.annotation.TableName;
import com.ta.audid.utils.UtdidLogger;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;

public class DBMgr {
    private static final String TAG = "DBMgr";
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

    public synchronized List<? extends Entity> find(Class<? extends Entity> cls, String where, String orderby, int limit) {
        String str;
        String str2;
        String str3;
        List entities;
        Object obj;
        List entities2 = Collections.EMPTY_LIST;
        if (cls == null) {
            entities = entities2;
        } else {
            String tablename = getTablename(cls);
            SQLiteDatabase db = checkTableAvailable(cls, tablename);
            if (db == null) {
                UtdidLogger.d("db is null", new Object[0]);
                entities = entities2;
            } else {
                StringBuilder append = new StringBuilder().append("SELECT * FROM ").append(tablename);
                if (TextUtils.isEmpty(where)) {
                    str = "";
                } else {
                    str = " WHERE " + where;
                }
                StringBuilder append2 = append.append(str);
                if (TextUtils.isEmpty(orderby)) {
                    str2 = "";
                } else {
                    str2 = " ORDER BY " + orderby;
                }
                StringBuilder append3 = append2.append(str2);
                if (limit <= 0) {
                    str3 = "";
                } else {
                    str3 = " LIMIT " + limit;
                }
                String sql = append3.append(str3).toString();
                UtdidLogger.d(TAG, "sql", sql);
                Cursor c = null;
                try {
                    c = db.rawQuery(sql, (String[]) null);
                    List entities3 = new ArrayList();
                    try {
                        List<Field> fields = getAllFields(cls);
                        while (c != null && c.moveToNext()) {
                            Entity entity = (Entity) cls.newInstance();
                            for (int i = 0; i < fields.size(); i++) {
                                Field field = fields.get(i);
                                Class<?> fieldType = field.getType();
                                String columnName = getColumnName(field);
                                int index = c.getColumnIndex(columnName);
                                if (index != -1) {
                                    if (fieldType == Long.class || fieldType == Long.TYPE) {
                                        obj = Long.valueOf(c.getLong(index));
                                    } else {
                                        if (fieldType != Integer.class) {
                                            if (fieldType != Integer.TYPE) {
                                                if (fieldType == Double.TYPE || fieldType == Double.class) {
                                                    obj = Double.valueOf(c.getDouble(index));
                                                } else {
                                                    obj = c.getString(index);
                                                }
                                            }
                                        }
                                        obj = Integer.valueOf(c.getInt(index));
                                    }
                                    try {
                                        field.set(entity, obj);
                                    } catch (Exception e) {
                                    }
                                } else {
                                    UtdidLogger.w(TAG, "can not get field: " + columnName);
                                }
                            }
                            entities3.add(entity);
                        }
                        this.mHelper.closeCursor(c);
                        this.mHelper.closeWritableDatabase(db);
                        entities2 = entities3;
                    } catch (Throwable th) {
                        th = th;
                        List list = entities3;
                        this.mHelper.closeCursor(c);
                        this.mHelper.closeWritableDatabase(db);
                        throw th;
                    }
                } catch (Throwable th2) {
                    e = th2;
                    try {
                        UtdidLogger.w(TAG, "[get]", e);
                        this.mHelper.closeCursor(c);
                        this.mHelper.closeWritableDatabase(db);
                        entities = entities2;
                        return entities;
                    } catch (Throwable th3) {
                        th = th3;
                        this.mHelper.closeCursor(c);
                        this.mHelper.closeWritableDatabase(db);
                        throw th;
                    }
                }
                entities = entities2;
            }
        }
        return entities;
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
                    UtdidLogger.w(TAG, "can not get available db");
                } else {
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
                                    UtdidLogger.d(TAG, "mDbName", this.mDbName, "tablename", tableName, "insert:success", entity);
                                } else {
                                    UtdidLogger.w(TAG, "mDbName", this.mDbName, "tablename", tableName, "insert:error", entity);
                                }
                            } else {
                                String[] strArr = {String.valueOf(entity._id)};
                                UtdidLogger.w(TAG, "db update :" + ((long) db.update(tableName, values, "_id=?", strArr)));
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
                        UtdidLogger.w(TAG, "get field failed", e3);
                    } catch (Throwable e4) {
                        try {
                            UtdidLogger.d(TAG, e4.toString());
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

    /* JADX WARNING: Unknown top exception splitter block from list: {B:32:0x0103=Splitter:B:32:0x0103, B:39:0x0114=Splitter:B:39:0x0114, B:49:0x0124=Splitter:B:49:0x0124} */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public synchronized int delete(java.util.List<? extends com.ta.audid.db.Entity> r15) {
        /*
            r14 = this;
            r7 = 0
            monitor-enter(r14)
            if (r15 == 0) goto L_0x000a
            int r6 = r15.size()     // Catch:{ all -> 0x011a }
            if (r6 != 0) goto L_0x000d
        L_0x000a:
            r6 = r7
        L_0x000b:
            monitor-exit(r14)
            return r6
        L_0x000d:
            r6 = 0
            java.lang.Object r6 = r15.get(r6)     // Catch:{ all -> 0x011a }
            com.ta.audid.db.Entity r6 = (com.ta.audid.db.Entity) r6     // Catch:{ all -> 0x011a }
            java.lang.Class r6 = r6.getClass()     // Catch:{ all -> 0x011a }
            java.lang.String r3 = r14.getTablename(r6)     // Catch:{ all -> 0x011a }
            r6 = 0
            java.lang.Object r6 = r15.get(r6)     // Catch:{ all -> 0x011a }
            com.ta.audid.db.Entity r6 = (com.ta.audid.db.Entity) r6     // Catch:{ all -> 0x011a }
            java.lang.Class r6 = r6.getClass()     // Catch:{ all -> 0x011a }
            android.database.sqlite.SQLiteDatabase r0 = r14.checkTableAvailable(r6, r3)     // Catch:{ all -> 0x011a }
            if (r0 != 0) goto L_0x003e
            java.lang.String r6 = "DBMgr"
            r8 = 1
            java.lang.Object[] r8 = new java.lang.Object[r8]     // Catch:{ all -> 0x011a }
            r9 = 0
            java.lang.String r10 = "db is null"
            r8[r9] = r10     // Catch:{ all -> 0x011a }
            com.ta.audid.utils.UtdidLogger.d((java.lang.String) r6, (java.lang.Object[]) r8)     // Catch:{ all -> 0x011a }
            r6 = r7
            goto L_0x000b
        L_0x003e:
            r0.beginTransaction()     // Catch:{ Throwable -> 0x00ea }
            r2 = 0
        L_0x0042:
            int r6 = r15.size()     // Catch:{ Throwable -> 0x00ea }
            if (r2 >= r6) goto L_0x010e
            java.lang.String r7 = "_id=?"
            r6 = 1
            java.lang.String[] r8 = new java.lang.String[r6]     // Catch:{ Throwable -> 0x00ea }
            r9 = 0
            java.lang.StringBuilder r10 = new java.lang.StringBuilder     // Catch:{ Throwable -> 0x00ea }
            r10.<init>()     // Catch:{ Throwable -> 0x00ea }
            java.lang.Object r6 = r15.get(r2)     // Catch:{ Throwable -> 0x00ea }
            com.ta.audid.db.Entity r6 = (com.ta.audid.db.Entity) r6     // Catch:{ Throwable -> 0x00ea }
            long r12 = r6._id     // Catch:{ Throwable -> 0x00ea }
            java.lang.StringBuilder r6 = r10.append(r12)     // Catch:{ Throwable -> 0x00ea }
            java.lang.String r10 = ""
            java.lang.StringBuilder r6 = r6.append(r10)     // Catch:{ Throwable -> 0x00ea }
            java.lang.String r6 = r6.toString()     // Catch:{ Throwable -> 0x00ea }
            r8[r9] = r6     // Catch:{ Throwable -> 0x00ea }
            int r6 = r0.delete(r3, r7, r8)     // Catch:{ Throwable -> 0x00ea }
            long r4 = (long) r6     // Catch:{ Throwable -> 0x00ea }
            r6 = 0
            int r6 = (r4 > r6 ? 1 : (r4 == r6 ? 0 : -1))
            if (r6 > 0) goto L_0x00ad
            java.lang.String r7 = "DBMgr"
            r6 = 6
            java.lang.Object[] r8 = new java.lang.Object[r6]     // Catch:{ Throwable -> 0x00ea }
            r6 = 0
            java.lang.String r9 = "db"
            r8[r6] = r9     // Catch:{ Throwable -> 0x00ea }
            r6 = 1
            java.lang.String r9 = r14.mDbName     // Catch:{ Throwable -> 0x00ea }
            r8[r6] = r9     // Catch:{ Throwable -> 0x00ea }
            r6 = 2
            java.lang.String r9 = "tableName"
            r8[r6] = r9     // Catch:{ Throwable -> 0x00ea }
            r6 = 3
            r8[r6] = r3     // Catch:{ Throwable -> 0x00ea }
            r6 = 4
            java.lang.String r9 = " delete failed _id"
            r8[r6] = r9     // Catch:{ Throwable -> 0x00ea }
            r9 = 5
            java.lang.Object r6 = r15.get(r2)     // Catch:{ Throwable -> 0x00ea }
            com.ta.audid.db.Entity r6 = (com.ta.audid.db.Entity) r6     // Catch:{ Throwable -> 0x00ea }
            long r10 = r6._id     // Catch:{ Throwable -> 0x00ea }
            java.lang.Long r6 = java.lang.Long.valueOf(r10)     // Catch:{ Throwable -> 0x00ea }
            r8[r9] = r6     // Catch:{ Throwable -> 0x00ea }
            com.ta.audid.utils.UtdidLogger.w(r7, r8)     // Catch:{ Throwable -> 0x00ea }
        L_0x00aa:
            int r2 = r2 + 1
            goto L_0x0042
        L_0x00ad:
            java.lang.String r7 = "DBMgr"
            r6 = 6
            java.lang.Object[] r8 = new java.lang.Object[r6]     // Catch:{ Throwable -> 0x00ea }
            r6 = 0
            java.lang.String r9 = "db "
            r8[r6] = r9     // Catch:{ Throwable -> 0x00ea }
            r6 = 1
            java.lang.String r9 = r14.mDbName     // Catch:{ Throwable -> 0x00ea }
            r8[r6] = r9     // Catch:{ Throwable -> 0x00ea }
            r6 = 2
            java.lang.String r9 = "tableName"
            r8[r6] = r9     // Catch:{ Throwable -> 0x00ea }
            r6 = 3
            r8[r6] = r3     // Catch:{ Throwable -> 0x00ea }
            r6 = 4
            java.lang.String r9 = "delete success _id"
            r8[r6] = r9     // Catch:{ Throwable -> 0x00ea }
            r9 = 5
            java.lang.Object r6 = r15.get(r2)     // Catch:{ Throwable -> 0x00ea }
            com.ta.audid.db.Entity r6 = (com.ta.audid.db.Entity) r6     // Catch:{ Throwable -> 0x00ea }
            long r10 = r6._id     // Catch:{ Throwable -> 0x00ea }
            java.lang.Long r6 = java.lang.Long.valueOf(r10)     // Catch:{ Throwable -> 0x00ea }
            r8[r9] = r6     // Catch:{ Throwable -> 0x00ea }
            com.ta.audid.utils.UtdidLogger.d((java.lang.String) r7, (java.lang.Object[]) r8)     // Catch:{ Throwable -> 0x00ea }
            java.lang.Object r6 = r15.get(r2)     // Catch:{ Throwable -> 0x00ea }
            com.ta.audid.db.Entity r6 = (com.ta.audid.db.Entity) r6     // Catch:{ Throwable -> 0x00ea }
            r8 = -1
            r6._id = r8     // Catch:{ Throwable -> 0x00ea }
            goto L_0x00aa
        L_0x00ea:
            r1 = move-exception
            java.lang.String r6 = "DBMgr"
            r7 = 2
            java.lang.Object[] r7 = new java.lang.Object[r7]     // Catch:{ all -> 0x011d }
            r8 = 0
            java.lang.String r9 = "db delete error:"
            r7[r8] = r9     // Catch:{ all -> 0x011d }
            r8 = 1
            r7[r8] = r1     // Catch:{ all -> 0x011d }
            com.ta.audid.utils.UtdidLogger.w(r6, r7)     // Catch:{ all -> 0x011d }
            r0.setTransactionSuccessful()     // Catch:{ Throwable -> 0x012e }
        L_0x0100:
            r0.endTransaction()     // Catch:{ Throwable -> 0x0130 }
        L_0x0103:
            com.ta.audid.db.SqliteHelper r6 = r14.mHelper     // Catch:{ all -> 0x011a }
            r6.closeWritableDatabase(r0)     // Catch:{ all -> 0x011a }
        L_0x0108:
            int r6 = r15.size()     // Catch:{ all -> 0x011a }
            goto L_0x000b
        L_0x010e:
            r0.setTransactionSuccessful()     // Catch:{ Throwable -> 0x012a }
        L_0x0111:
            r0.endTransaction()     // Catch:{ Throwable -> 0x012c }
        L_0x0114:
            com.ta.audid.db.SqliteHelper r6 = r14.mHelper     // Catch:{ all -> 0x011a }
            r6.closeWritableDatabase(r0)     // Catch:{ all -> 0x011a }
            goto L_0x0108
        L_0x011a:
            r6 = move-exception
            monitor-exit(r14)
            throw r6
        L_0x011d:
            r6 = move-exception
            r0.setTransactionSuccessful()     // Catch:{ Throwable -> 0x0132 }
        L_0x0121:
            r0.endTransaction()     // Catch:{ Throwable -> 0x0134 }
        L_0x0124:
            com.ta.audid.db.SqliteHelper r7 = r14.mHelper     // Catch:{ all -> 0x011a }
            r7.closeWritableDatabase(r0)     // Catch:{ all -> 0x011a }
            throw r6     // Catch:{ all -> 0x011a }
        L_0x012a:
            r6 = move-exception
            goto L_0x0111
        L_0x012c:
            r6 = move-exception
            goto L_0x0114
        L_0x012e:
            r6 = move-exception
            goto L_0x0100
        L_0x0130:
            r6 = move-exception
            goto L_0x0103
        L_0x0132:
            r7 = move-exception
            goto L_0x0121
        L_0x0134:
            r7 = move-exception
            goto L_0x0124
        */
        throw new UnsupportedOperationException("Method not decompiled: com.ta.audid.db.DBMgr.delete(java.util.List):int");
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
        com.ta.audid.utils.UtdidLogger.w(TAG, "setTransactionSuccessful", r4);
     */
    /* JADX WARNING: Failed to process nested try/catch */
    /* JADX WARNING: Removed duplicated region for block: B:57:0x014b A[ExcHandler: all (r10v10 'th' java.lang.Throwable A[CUSTOM_DECLARE]), Splitter:B:15:0x0046] */
    /* JADX WARNING: Unknown top exception splitter block from list: {B:33:0x00ac=Splitter:B:33:0x00ac, B:44:0x00ed=Splitter:B:44:0x00ed, B:60:0x014f=Splitter:B:60:0x014f} */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public synchronized void update(java.util.List<? extends com.ta.audid.db.Entity> r19) {
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
            com.ta.audid.db.Entity r10 = (com.ta.audid.db.Entity) r10     // Catch:{ all -> 0x0043 }
            java.lang.Class r10 = r10.getClass()     // Catch:{ all -> 0x0043 }
            r0 = r18
            java.lang.String r9 = r0.getTablename(r10)     // Catch:{ all -> 0x0043 }
            r10 = 0
            r0 = r19
            java.lang.Object r10 = r0.get(r10)     // Catch:{ all -> 0x0043 }
            com.ta.audid.db.Entity r10 = (com.ta.audid.db.Entity) r10     // Catch:{ all -> 0x0043 }
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
            com.ta.audid.utils.UtdidLogger.w(r10, r11)     // Catch:{ all -> 0x0043 }
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
            com.ta.audid.db.Entity r10 = (com.ta.audid.db.Entity) r10     // Catch:{ Exception -> 0x00a8, all -> 0x014b }
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
            com.ta.audid.db.SqliteHelper r10 = r0.mHelper     // Catch:{ all -> 0x0043 }
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
            com.ta.audid.db.Entity r10 = (com.ta.audid.db.Entity) r10     // Catch:{ Exception -> 0x00a8, all -> 0x014b }
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
            com.ta.audid.db.SqliteHelper r10 = r0.mHelper     // Catch:{ all -> 0x0043 }
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
            com.ta.audid.utils.UtdidLogger.w(r10, r11)     // Catch:{ all -> 0x0043 }
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
            com.ta.audid.utils.UtdidLogger.w(r10, r11)     // Catch:{ all -> 0x0043 }
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
            com.ta.audid.utils.UtdidLogger.w(r10, r11)     // Catch:{ all -> 0x0043 }
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
            com.ta.audid.utils.UtdidLogger.w(r10, r11)     // Catch:{ all -> 0x0043 }
            goto L_0x00af
        L_0x014b:
            r10 = move-exception
            r3.setTransactionSuccessful()     // Catch:{ Exception -> 0x015a }
        L_0x014f:
            r3.endTransaction()     // Catch:{ Exception -> 0x016e }
        L_0x0152:
            r0 = r18
            com.ta.audid.db.SqliteHelper r11 = r0.mHelper     // Catch:{ all -> 0x0043 }
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
            com.ta.audid.utils.UtdidLogger.w(r11, r12)     // Catch:{ all -> 0x0043 }
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
            com.ta.audid.utils.UtdidLogger.w(r11, r12)     // Catch:{ all -> 0x0043 }
            goto L_0x0152
        */
        throw new UnsupportedOperationException("Method not decompiled: com.ta.audid.db.DBMgr.update(java.util.List):void");
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
        com.ta.audid.utils.UtdidLogger.w(TAG, "setTransactionSuccessful", r4);
     */
    /* JADX WARNING: Failed to process nested try/catch */
    /* JADX WARNING: Removed duplicated region for block: B:57:0x0150 A[ExcHandler: all (r11v10 'th' java.lang.Throwable A[CUSTOM_DECLARE]), Splitter:B:15:0x0046] */
    /* JADX WARNING: Unknown top exception splitter block from list: {B:36:0x00e4=Splitter:B:36:0x00e4, B:60:0x0154=Splitter:B:60:0x0154, B:48:0x011c=Splitter:B:48:0x011c} */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public synchronized void updateLogPriority(java.util.List<? extends com.ta.audid.db.Entity> r19) {
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
            com.ta.audid.db.Entity r11 = (com.ta.audid.db.Entity) r11     // Catch:{ all -> 0x0043 }
            java.lang.Class r11 = r11.getClass()     // Catch:{ all -> 0x0043 }
            r0 = r18
            java.lang.String r10 = r0.getTablename(r11)     // Catch:{ all -> 0x0043 }
            r11 = 0
            r0 = r19
            java.lang.Object r11 = r0.get(r11)     // Catch:{ all -> 0x0043 }
            com.ta.audid.db.Entity r11 = (com.ta.audid.db.Entity) r11     // Catch:{ all -> 0x0043 }
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
            com.ta.audid.utils.UtdidLogger.w(r11, r12)     // Catch:{ all -> 0x0043 }
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
            com.ta.audid.db.Entity r11 = (com.ta.audid.db.Entity) r11     // Catch:{ Exception -> 0x0118, all -> 0x0150 }
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
            com.ta.audid.db.Entity r11 = (com.ta.audid.db.Entity) r11     // Catch:{ Exception -> 0x00da, all -> 0x0150 }
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
            com.ta.audid.db.SqliteHelper r11 = r0.mHelper     // Catch:{ all -> 0x0043 }
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
            com.ta.audid.utils.UtdidLogger.w(r11, r12)     // Catch:{ all -> 0x0043 }
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
            com.ta.audid.utils.UtdidLogger.w(r11, r12)     // Catch:{ all -> 0x0043 }
            goto L_0x00e7
        L_0x0118:
            r11 = move-exception
            r3.setTransactionSuccessful()     // Catch:{ Exception -> 0x0128 }
        L_0x011c:
            r3.endTransaction()     // Catch:{ Exception -> 0x013c }
        L_0x011f:
            r0 = r18
            com.ta.audid.db.SqliteHelper r11 = r0.mHelper     // Catch:{ all -> 0x0043 }
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
            com.ta.audid.utils.UtdidLogger.w(r11, r12)     // Catch:{ all -> 0x0043 }
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
            com.ta.audid.utils.UtdidLogger.w(r11, r12)     // Catch:{ all -> 0x0043 }
            goto L_0x011f
        L_0x0150:
            r11 = move-exception
            r3.setTransactionSuccessful()     // Catch:{ Exception -> 0x015f }
        L_0x0154:
            r3.endTransaction()     // Catch:{ Exception -> 0x0173 }
        L_0x0157:
            r0 = r18
            com.ta.audid.db.SqliteHelper r12 = r0.mHelper     // Catch:{ all -> 0x0043 }
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
            com.ta.audid.utils.UtdidLogger.w(r12, r13)     // Catch:{ all -> 0x0043 }
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
            com.ta.audid.utils.UtdidLogger.w(r12, r13)     // Catch:{ all -> 0x0043 }
            goto L_0x0157
        */
        throw new UnsupportedOperationException("Method not decompiled: com.ta.audid.db.DBMgr.updateLogPriority(java.util.List):void");
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
        UtdidLogger.d((String) null, "whereArgs", whereArgs, "", "whereArgs", whereArgs);
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
        if (cls != null && !hasCheckdb.booleanValue()) {
            List<Field> fields = getAllFields(cls);
            ArrayList<Field> updateColumns = new ArrayList<>();
            String sql = " SELECT * FROM " + tableName + " LIMIT 0";
            boolean needCreate = false;
            Cursor cursor = null;
            if (fields != null) {
                try {
                    cursor = db.rawQuery(sql, (String[]) null);
                } catch (Exception e) {
                    UtdidLogger.d(TAG, "has not create talbe:", tableName);
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
                UtdidLogger.w(TAG, "update db error...", e);
            }
            builder.delete(0, updateSql.length());
            UtdidLogger.d(TAG, null, "excute sql:", updateSql);
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
        UtdidLogger.d(TAG, "excute sql:", createSql);
        try {
            db.execSQL(createSql);
        } catch (Exception e) {
            UtdidLogger.w(TAG, "create db error", e);
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
                UtdidLogger.d(TAG, "db is null");
            }
            count = count2;
        }
        return count;
    }

    public synchronized int count(Class<? extends Entity> cls, String where) {
        int count;
        int count2 = 0;
        if (cls == null) {
            count = 0;
        } else {
            String tableName = getTablename(cls);
            SQLiteDatabase db = checkTableAvailable(cls, tableName);
            if (db != null) {
                try {
                    Cursor cursor = db.rawQuery("SELECT count(*) FROM " + tableName + (TextUtils.isEmpty(where) ? "" : " WHERE " + where), (String[]) null);
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
                UtdidLogger.d(TAG, "db is null");
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
                UtdidLogger.e("delete db data", e, new Object[0]);
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
}
