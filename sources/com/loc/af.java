package com.loc;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.text.TextUtils;
import java.lang.annotation.Annotation;
import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.util.HashMap;
import java.util.Map;

/* compiled from: DBOperation */
public final class af {
    private static Map<Class<? extends ae>, ae> d = new HashMap();
    private ai a;
    private SQLiteDatabase b;
    private ae c;

    public af(Context context, ae aeVar) {
        try {
            this.a = new ai(context.getApplicationContext(), aeVar.a(), aeVar);
        } catch (Throwable th) {
            th.printStackTrace();
        }
        this.c = aeVar;
    }

    /* JADX WARNING: Can't fix incorrect switch cases order */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    private static android.content.ContentValues a(java.lang.Object r8, com.loc.ag r9) {
        /*
            android.content.ContentValues r3 = new android.content.ContentValues
            r3.<init>()
            java.lang.Class r0 = r8.getClass()
            boolean r1 = r9.b()
            java.lang.reflect.Field[] r4 = a((java.lang.Class<?>) r0, (boolean) r1)
            int r5 = r4.length
            r0 = 0
            r2 = r0
        L_0x0014:
            if (r2 >= r5) goto L_0x00a4
            r1 = r4[r2]
            r0 = 1
            r1.setAccessible(r0)
            java.lang.Class<com.loc.ah> r0 = com.loc.ah.class
            java.lang.annotation.Annotation r0 = r1.getAnnotation(r0)
            if (r0 == 0) goto L_0x002d
            com.loc.ah r0 = (com.loc.ah) r0
            int r6 = r0.b()
            switch(r6) {
                case 1: goto L_0x0046;
                case 2: goto L_0x0031;
                case 3: goto L_0x0084;
                case 4: goto L_0x0066;
                case 5: goto L_0x0056;
                case 6: goto L_0x0076;
                case 7: goto L_0x0094;
                default: goto L_0x002d;
            }
        L_0x002d:
            int r0 = r2 + 1
            r2 = r0
            goto L_0x0014
        L_0x0031:
            int r1 = r1.getInt(r8)     // Catch:{ IllegalAccessException -> 0x0041 }
            java.lang.String r0 = r0.a()     // Catch:{ IllegalAccessException -> 0x0041 }
            java.lang.Integer r1 = java.lang.Integer.valueOf(r1)     // Catch:{ IllegalAccessException -> 0x0041 }
            r3.put(r0, r1)     // Catch:{ IllegalAccessException -> 0x0041 }
            goto L_0x002d
        L_0x0041:
            r0 = move-exception
            r0.printStackTrace()
            goto L_0x002d
        L_0x0046:
            short r1 = r1.getShort(r8)     // Catch:{ IllegalAccessException -> 0x0041 }
            java.lang.String r0 = r0.a()     // Catch:{ IllegalAccessException -> 0x0041 }
            java.lang.Short r1 = java.lang.Short.valueOf(r1)     // Catch:{ IllegalAccessException -> 0x0041 }
            r3.put(r0, r1)     // Catch:{ IllegalAccessException -> 0x0041 }
            goto L_0x002d
        L_0x0056:
            long r6 = r1.getLong(r8)     // Catch:{ IllegalAccessException -> 0x0041 }
            java.lang.String r0 = r0.a()     // Catch:{ IllegalAccessException -> 0x0041 }
            java.lang.Long r1 = java.lang.Long.valueOf(r6)     // Catch:{ IllegalAccessException -> 0x0041 }
            r3.put(r0, r1)     // Catch:{ IllegalAccessException -> 0x0041 }
            goto L_0x002d
        L_0x0066:
            double r6 = r1.getDouble(r8)     // Catch:{ IllegalAccessException -> 0x0041 }
            java.lang.String r0 = r0.a()     // Catch:{ IllegalAccessException -> 0x0041 }
            java.lang.Double r1 = java.lang.Double.valueOf(r6)     // Catch:{ IllegalAccessException -> 0x0041 }
            r3.put(r0, r1)     // Catch:{ IllegalAccessException -> 0x0041 }
            goto L_0x002d
        L_0x0076:
            java.lang.Object r1 = r1.get(r8)     // Catch:{ IllegalAccessException -> 0x0041 }
            java.lang.String r1 = (java.lang.String) r1     // Catch:{ IllegalAccessException -> 0x0041 }
            java.lang.String r0 = r0.a()     // Catch:{ IllegalAccessException -> 0x0041 }
            r3.put(r0, r1)     // Catch:{ IllegalAccessException -> 0x0041 }
            goto L_0x002d
        L_0x0084:
            float r1 = r1.getFloat(r8)     // Catch:{ IllegalAccessException -> 0x0041 }
            java.lang.String r0 = r0.a()     // Catch:{ IllegalAccessException -> 0x0041 }
            java.lang.Float r1 = java.lang.Float.valueOf(r1)     // Catch:{ IllegalAccessException -> 0x0041 }
            r3.put(r0, r1)     // Catch:{ IllegalAccessException -> 0x0041 }
            goto L_0x002d
        L_0x0094:
            java.lang.Object r1 = r1.get(r8)     // Catch:{ IllegalAccessException -> 0x0041 }
            byte[] r1 = (byte[]) r1     // Catch:{ IllegalAccessException -> 0x0041 }
            byte[] r1 = (byte[]) r1     // Catch:{ IllegalAccessException -> 0x0041 }
            java.lang.String r0 = r0.a()     // Catch:{ IllegalAccessException -> 0x0041 }
            r3.put(r0, r1)     // Catch:{ IllegalAccessException -> 0x0041 }
            goto L_0x002d
        L_0x00a4:
            return r3
        */
        throw new UnsupportedOperationException("Method not decompiled: com.loc.af.a(java.lang.Object, com.loc.ag):android.content.ContentValues");
    }

    private SQLiteDatabase a() {
        try {
            if (this.b == null || this.b.isReadOnly()) {
                if (this.b != null) {
                    this.b.close();
                }
                this.b = this.a.getWritableDatabase();
            }
        } catch (Throwable th) {
            y.a(th, "dbs", "gwd");
        }
        return this.b;
    }

    private SQLiteDatabase a(boolean z) {
        try {
            if (this.b == null) {
                this.b = this.a.getReadableDatabase();
            }
        } catch (Throwable th) {
            if (!z) {
                y.a(th, "dbs", "grd");
            } else {
                th.printStackTrace();
            }
        }
        return this.b;
    }

    public static synchronized ae a(Class<? extends ae> cls) throws IllegalAccessException, InstantiationException {
        ae aeVar;
        synchronized (af.class) {
            if (d.get(cls) == null) {
                d.put(cls, cls.newInstance());
            }
            aeVar = d.get(cls);
        }
        return aeVar;
    }

    private static <T> T a(Cursor cursor, Class<T> cls, ag agVar) throws IllegalAccessException, InstantiationException, NoSuchMethodException, InvocationTargetException {
        Field[] a2 = a((Class<?>) cls, agVar.b());
        Constructor<T> declaredConstructor = cls.getDeclaredConstructor(new Class[0]);
        declaredConstructor.setAccessible(true);
        T newInstance = declaredConstructor.newInstance(new Object[0]);
        for (Field field : a2) {
            field.setAccessible(true);
            Annotation annotation = field.getAnnotation(ah.class);
            if (annotation != null) {
                ah ahVar = (ah) annotation;
                int b2 = ahVar.b();
                int columnIndex = cursor.getColumnIndex(ahVar.a());
                switch (b2) {
                    case 1:
                        field.set(newInstance, Short.valueOf(cursor.getShort(columnIndex)));
                        break;
                    case 2:
                        field.set(newInstance, Integer.valueOf(cursor.getInt(columnIndex)));
                        break;
                    case 3:
                        field.set(newInstance, Float.valueOf(cursor.getFloat(columnIndex)));
                        break;
                    case 4:
                        field.set(newInstance, Double.valueOf(cursor.getDouble(columnIndex)));
                        break;
                    case 5:
                        field.set(newInstance, Long.valueOf(cursor.getLong(columnIndex)));
                        break;
                    case 6:
                        field.set(newInstance, cursor.getString(columnIndex));
                        break;
                    case 7:
                        field.set(newInstance, cursor.getBlob(columnIndex));
                        break;
                }
            }
        }
        return newInstance;
    }

    private static <T> String a(ag agVar) {
        if (agVar == null) {
            return null;
        }
        return agVar.a();
    }

    public static String a(Map<String, String> map) {
        StringBuilder sb = new StringBuilder();
        boolean z = true;
        for (String next : map.keySet()) {
            if (z) {
                sb.append(next).append(" = '").append(map.get(next)).append("'");
                z = false;
            } else {
                sb.append(" and ").append(next).append(" = '").append(map.get(next)).append("'");
            }
        }
        return sb.toString();
    }

    private static Field[] a(Class<?> cls, boolean z) {
        if (cls == null) {
            return null;
        }
        return z ? cls.getSuperclass().getDeclaredFields() : cls.getDeclaredFields();
    }

    private static <T> ag b(Class<T> cls) {
        Annotation annotation = cls.getAnnotation(ag.class);
        if (!(annotation != null)) {
            return null;
        }
        return (ag) annotation;
    }

    /* JADX WARNING: Code restructure failed: missing block: B:100:?, code lost:
        return r8;
     */
    /* JADX WARNING: Removed duplicated region for block: B:50:0x0086 A[SYNTHETIC, Splitter:B:50:0x0086] */
    /* JADX WARNING: Removed duplicated region for block: B:55:0x008d A[Catch:{ Throwable -> 0x00a6 }] */
    /* JADX WARNING: Unknown top exception splitter block from list: {B:25:0x0051=Splitter:B:25:0x0051, B:40:0x0073=Splitter:B:40:0x0073, B:56:0x0095=Splitter:B:56:0x0095, B:21:0x0045=Splitter:B:21:0x0045, B:52:0x0089=Splitter:B:52:0x0089, B:44:0x007f=Splitter:B:44:0x007f} */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public final <T> java.util.List<T> a(java.lang.String r13, java.lang.Class<T> r14, boolean r15) {
        /*
            r12 = this;
            r9 = 0
            com.loc.ae r10 = r12.c
            monitor-enter(r10)
            java.util.ArrayList r8 = new java.util.ArrayList     // Catch:{ all -> 0x0096 }
            r8.<init>()     // Catch:{ all -> 0x0096 }
            com.loc.ag r11 = b(r14)     // Catch:{ all -> 0x0096 }
            java.lang.String r1 = a((com.loc.ag) r11)     // Catch:{ all -> 0x0096 }
            android.database.sqlite.SQLiteDatabase r0 = r12.b     // Catch:{ all -> 0x0096 }
            if (r0 != 0) goto L_0x001b
            android.database.sqlite.SQLiteDatabase r0 = r12.a((boolean) r15)     // Catch:{ all -> 0x0096 }
            r12.b = r0     // Catch:{ all -> 0x0096 }
        L_0x001b:
            android.database.sqlite.SQLiteDatabase r0 = r12.b     // Catch:{ all -> 0x0096 }
            if (r0 == 0) goto L_0x0027
            boolean r0 = android.text.TextUtils.isEmpty(r1)     // Catch:{ all -> 0x0096 }
            if (r0 != 0) goto L_0x0027
            if (r13 != 0) goto L_0x002a
        L_0x0027:
            monitor-exit(r10)     // Catch:{ all -> 0x0096 }
            r0 = r8
        L_0x0029:
            return r0
        L_0x002a:
            android.database.sqlite.SQLiteDatabase r0 = r12.b     // Catch:{ Throwable -> 0x0119, all -> 0x0082 }
            r2 = 0
            r4 = 0
            r5 = 0
            r6 = 0
            r7 = 0
            r3 = r13
            android.database.Cursor r1 = r0.query(r1, r2, r3, r4, r5, r6, r7)     // Catch:{ Throwable -> 0x0119, all -> 0x0082 }
            if (r1 != 0) goto L_0x0054
            android.database.sqlite.SQLiteDatabase r0 = r12.b     // Catch:{ Throwable -> 0x0062 }
            r0.close()     // Catch:{ Throwable -> 0x0062 }
            r0 = 0
            r12.b = r0     // Catch:{ Throwable -> 0x0062 }
            if (r1 == 0) goto L_0x0045
            r1.close()     // Catch:{ Throwable -> 0x00cd }
        L_0x0045:
            android.database.sqlite.SQLiteDatabase r0 = r12.b     // Catch:{ Throwable -> 0x00db }
            if (r0 == 0) goto L_0x0051
            android.database.sqlite.SQLiteDatabase r0 = r12.b     // Catch:{ Throwable -> 0x00db }
            r0.close()     // Catch:{ Throwable -> 0x00db }
            r0 = 0
            r12.b = r0     // Catch:{ Throwable -> 0x00db }
        L_0x0051:
            monitor-exit(r10)     // Catch:{ all -> 0x0096 }
            r0 = r8
            goto L_0x0029
        L_0x0054:
            boolean r0 = r1.moveToNext()     // Catch:{ Throwable -> 0x0062 }
            if (r0 == 0) goto L_0x00e9
            java.lang.Object r0 = a((android.database.Cursor) r1, r14, (com.loc.ag) r11)     // Catch:{ Throwable -> 0x0062 }
            r8.add(r0)     // Catch:{ Throwable -> 0x0062 }
            goto L_0x0054
        L_0x0062:
            r0 = move-exception
        L_0x0063:
            if (r15 != 0) goto L_0x006e
            java.lang.String r2 = "dbs"
            java.lang.String r3 = "sld"
            com.loc.y.a((java.lang.Throwable) r0, (java.lang.String) r2, (java.lang.String) r3)     // Catch:{ all -> 0x0116 }
        L_0x006e:
            if (r1 == 0) goto L_0x0073
            r1.close()     // Catch:{ Throwable -> 0x00b3 }
        L_0x0073:
            android.database.sqlite.SQLiteDatabase r0 = r12.b     // Catch:{ Throwable -> 0x00c0 }
            if (r0 == 0) goto L_0x007f
            android.database.sqlite.SQLiteDatabase r0 = r12.b     // Catch:{ Throwable -> 0x00c0 }
            r0.close()     // Catch:{ Throwable -> 0x00c0 }
            r0 = 0
            r12.b = r0     // Catch:{ Throwable -> 0x00c0 }
        L_0x007f:
            monitor-exit(r10)     // Catch:{ all -> 0x0096 }
            r0 = r8
            goto L_0x0029
        L_0x0082:
            r0 = move-exception
            r1 = r9
        L_0x0084:
            if (r1 == 0) goto L_0x0089
            r1.close()     // Catch:{ Throwable -> 0x0099 }
        L_0x0089:
            android.database.sqlite.SQLiteDatabase r1 = r12.b     // Catch:{ Throwable -> 0x00a6 }
            if (r1 == 0) goto L_0x0095
            android.database.sqlite.SQLiteDatabase r1 = r12.b     // Catch:{ Throwable -> 0x00a6 }
            r1.close()     // Catch:{ Throwable -> 0x00a6 }
            r1 = 0
            r12.b = r1     // Catch:{ Throwable -> 0x00a6 }
        L_0x0095:
            throw r0     // Catch:{ all -> 0x0096 }
        L_0x0096:
            r0 = move-exception
            monitor-exit(r10)     // Catch:{ all -> 0x0096 }
            throw r0
        L_0x0099:
            r1 = move-exception
            if (r15 != 0) goto L_0x0089
            java.lang.String r2 = "dbs"
            java.lang.String r3 = "sld"
            com.loc.y.a((java.lang.Throwable) r1, (java.lang.String) r2, (java.lang.String) r3)     // Catch:{ all -> 0x0096 }
            goto L_0x0089
        L_0x00a6:
            r1 = move-exception
            if (r15 != 0) goto L_0x0095
            java.lang.String r2 = "dbs"
            java.lang.String r3 = "sld"
            com.loc.y.a((java.lang.Throwable) r1, (java.lang.String) r2, (java.lang.String) r3)     // Catch:{ all -> 0x0096 }
            goto L_0x0095
        L_0x00b3:
            r0 = move-exception
            if (r15 != 0) goto L_0x0073
            java.lang.String r1 = "dbs"
            java.lang.String r2 = "sld"
            com.loc.y.a((java.lang.Throwable) r0, (java.lang.String) r1, (java.lang.String) r2)     // Catch:{ all -> 0x0096 }
            goto L_0x0073
        L_0x00c0:
            r0 = move-exception
            if (r15 != 0) goto L_0x007f
            java.lang.String r1 = "dbs"
            java.lang.String r2 = "sld"
            com.loc.y.a((java.lang.Throwable) r0, (java.lang.String) r1, (java.lang.String) r2)     // Catch:{ all -> 0x0096 }
            goto L_0x007f
        L_0x00cd:
            r0 = move-exception
            if (r15 != 0) goto L_0x0045
            java.lang.String r1 = "dbs"
            java.lang.String r2 = "sld"
            com.loc.y.a((java.lang.Throwable) r0, (java.lang.String) r1, (java.lang.String) r2)     // Catch:{ all -> 0x0096 }
            goto L_0x0045
        L_0x00db:
            r0 = move-exception
            if (r15 != 0) goto L_0x0051
            java.lang.String r1 = "dbs"
            java.lang.String r2 = "sld"
            com.loc.y.a((java.lang.Throwable) r0, (java.lang.String) r1, (java.lang.String) r2)     // Catch:{ all -> 0x0096 }
            goto L_0x0051
        L_0x00e9:
            if (r1 == 0) goto L_0x00ee
            r1.close()     // Catch:{ Throwable -> 0x0109 }
        L_0x00ee:
            android.database.sqlite.SQLiteDatabase r0 = r12.b     // Catch:{ Throwable -> 0x00fb }
            if (r0 == 0) goto L_0x007f
            android.database.sqlite.SQLiteDatabase r0 = r12.b     // Catch:{ Throwable -> 0x00fb }
            r0.close()     // Catch:{ Throwable -> 0x00fb }
            r0 = 0
            r12.b = r0     // Catch:{ Throwable -> 0x00fb }
            goto L_0x007f
        L_0x00fb:
            r0 = move-exception
            if (r15 != 0) goto L_0x007f
            java.lang.String r1 = "dbs"
            java.lang.String r2 = "sld"
            com.loc.y.a((java.lang.Throwable) r0, (java.lang.String) r1, (java.lang.String) r2)     // Catch:{ all -> 0x0096 }
            goto L_0x007f
        L_0x0109:
            r0 = move-exception
            if (r15 != 0) goto L_0x00ee
            java.lang.String r1 = "dbs"
            java.lang.String r2 = "sld"
            com.loc.y.a((java.lang.Throwable) r0, (java.lang.String) r1, (java.lang.String) r2)     // Catch:{ all -> 0x0096 }
            goto L_0x00ee
        L_0x0116:
            r0 = move-exception
            goto L_0x0084
        L_0x0119:
            r0 = move-exception
            r1 = r9
            goto L_0x0063
        */
        throw new UnsupportedOperationException("Method not decompiled: com.loc.af.a(java.lang.String, java.lang.Class, boolean):java.util.List");
    }

    /* JADX INFO: finally extract failed */
    public final <T> void a(T t) {
        synchronized (this.c) {
            this.b = a();
            if (this.b != null) {
                try {
                    SQLiteDatabase sQLiteDatabase = this.b;
                    ag b2 = b(t.getClass());
                    String a2 = a(b2);
                    if (!(TextUtils.isEmpty(a2) || t == null || sQLiteDatabase == null)) {
                        sQLiteDatabase.insert(a2, (String) null, a((Object) t, b2));
                    }
                    if (this.b != null) {
                        this.b.close();
                        this.b = null;
                    }
                } catch (Throwable th) {
                    if (this.b != null) {
                        this.b.close();
                        this.b = null;
                    }
                    throw th;
                }
            }
        }
    }

    public final void a(Object obj, String str) {
        synchronized (this.c) {
            if (a(str, obj.getClass(), false).size() == 0) {
                a(obj);
            } else {
                a(str, obj);
            }
        }
    }

    /* JADX INFO: finally extract failed */
    /* JADX WARNING: No exception handlers in catch block: Catch:{  } */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public final <T> void a(java.lang.String r6, java.lang.Object r7) {
        /*
            r5 = this;
            com.loc.ae r1 = r5.c
            monitor-enter(r1)
            if (r7 != 0) goto L_0x0007
            monitor-exit(r1)     // Catch:{ all -> 0x001b }
        L_0x0006:
            return
        L_0x0007:
            java.lang.Class r0 = r7.getClass()     // Catch:{ all -> 0x001b }
            com.loc.ag r0 = b(r0)     // Catch:{ all -> 0x001b }
            java.lang.String r2 = a((com.loc.ag) r0)     // Catch:{ all -> 0x001b }
            boolean r3 = android.text.TextUtils.isEmpty(r2)     // Catch:{ all -> 0x001b }
            if (r3 == 0) goto L_0x001e
            monitor-exit(r1)     // Catch:{ all -> 0x001b }
            goto L_0x0006
        L_0x001b:
            r0 = move-exception
            monitor-exit(r1)     // Catch:{ all -> 0x001b }
            throw r0
        L_0x001e:
            android.content.ContentValues r0 = a((java.lang.Object) r7, (com.loc.ag) r0)     // Catch:{ all -> 0x001b }
            android.database.sqlite.SQLiteDatabase r3 = r5.a()     // Catch:{ all -> 0x001b }
            r5.b = r3     // Catch:{ all -> 0x001b }
            android.database.sqlite.SQLiteDatabase r3 = r5.b     // Catch:{ all -> 0x001b }
            if (r3 != 0) goto L_0x002e
            monitor-exit(r1)     // Catch:{ all -> 0x001b }
            goto L_0x0006
        L_0x002e:
            android.database.sqlite.SQLiteDatabase r3 = r5.b     // Catch:{ Throwable -> 0x0042 }
            r4 = 0
            r3.update(r2, r0, r6, r4)     // Catch:{ Throwable -> 0x0042 }
            android.database.sqlite.SQLiteDatabase r0 = r5.b     // Catch:{ all -> 0x001b }
            if (r0 == 0) goto L_0x0040
            android.database.sqlite.SQLiteDatabase r0 = r5.b     // Catch:{ all -> 0x001b }
            r0.close()     // Catch:{ all -> 0x001b }
            r0 = 0
            r5.b = r0     // Catch:{ all -> 0x001b }
        L_0x0040:
            monitor-exit(r1)     // Catch:{ all -> 0x001b }
            goto L_0x0006
        L_0x0042:
            r0 = move-exception
            java.lang.String r2 = "dbs"
            java.lang.String r3 = "udd"
            com.loc.y.a((java.lang.Throwable) r0, (java.lang.String) r2, (java.lang.String) r3)     // Catch:{ all -> 0x0059 }
            android.database.sqlite.SQLiteDatabase r0 = r5.b     // Catch:{ all -> 0x001b }
            if (r0 == 0) goto L_0x0040
            android.database.sqlite.SQLiteDatabase r0 = r5.b     // Catch:{ all -> 0x001b }
            r0.close()     // Catch:{ all -> 0x001b }
            r0 = 0
            r5.b = r0     // Catch:{ all -> 0x001b }
            goto L_0x0040
        L_0x0059:
            r0 = move-exception
            android.database.sqlite.SQLiteDatabase r2 = r5.b     // Catch:{ all -> 0x001b }
            if (r2 == 0) goto L_0x0066
            android.database.sqlite.SQLiteDatabase r2 = r5.b     // Catch:{ all -> 0x001b }
            r2.close()     // Catch:{ all -> 0x001b }
            r2 = 0
            r5.b = r2     // Catch:{ all -> 0x001b }
        L_0x0066:
            throw r0     // Catch:{ all -> 0x001b }
        */
        throw new UnsupportedOperationException("Method not decompiled: com.loc.af.a(java.lang.String, java.lang.Object):void");
    }
}
