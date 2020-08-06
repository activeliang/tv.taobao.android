package com.yunos.tv.blitz.global;

import android.app.Activity;
import android.app.Application;
import android.app.Dialog;
import android.content.Context;
import android.content.ContextWrapper;
import android.os.Bundle;
import android.os.Handler;
import android.text.TextUtils;
import android.util.Log;
import com.taobao.securityjni.StaticDataStore;
import com.taobao.securityjni.tools.DataContext;
import com.yunos.tv.blitz.BlitzPlugin;
import com.yunos.tv.blitz.JsCallBridge;
import com.yunos.tv.blitz.account.BzJsCallImpAccountListener;
import com.yunos.tv.blitz.account.LoginHelper;
import com.yunos.tv.blitz.activity.BzBaseActivity;
import com.yunos.tv.blitz.contentresolver.BzContentResolver;
import com.yunos.tv.blitz.listener.BzAppGlobalListener;
import com.yunos.tv.blitz.listener.BzJsCallBaseListener;
import com.yunos.tv.blitz.listener.BzJsCallUIListener;
import com.yunos.tv.blitz.listener.BzMiscListener;
import com.yunos.tv.blitz.listener.BzMtopParamSetListner;
import com.yunos.tv.blitz.listener.BzPageStatusListener;
import com.yunos.tv.blitz.listener.internal.BzJsCallAccountListener;
import com.yunos.tv.blitz.listener.internal.BzJsCallImpNetListener;
import com.yunos.tv.blitz.listener.internal.BzJsCallNetListener;
import com.yunos.tv.blitz.listener.internal.BzPayListener;
import com.yunos.tv.blitz.pay.BzJsCallPayImpListener;
import com.yunos.tv.blitz.ucache.BlitzUCacheBridge;
import java.lang.ref.WeakReference;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import mtopsdk.mtop.domain.EnvModeEnum;
import mtopsdk.mtop.global.SDKConfig;
import mtopsdk.mtop.global.SDKUtils;
import mtopsdk.mtop.intf.Mtop;
import mtopsdk.mtop.intf.MtopSetting;

public class BzAppMain {
    static final String TAG = BzAppMain.class.getSimpleName();
    static final String VERSION_BLITZ = "1.1.5.001";
    private static boolean mInitTBS = false;
    public static Mtop mMtopInstance;
    public String DEFAULT_MTOP_DOMAIN = "api.m.taobao.com";
    BzJsCallAccountListener mAccountListener = null;
    int mActivityCount = 0;
    WeakReference<Activity> mActivityRef;
    BzAppGlobalListener mAppGlobalListener;
    BzJsCallBaseListener mBaseListener = null;
    BzAppJniContext mBzAppJniContext = null;
    BzContentResolver mContentResolver = null;
    Context mContext;
    public final Map<Integer, WeakReference<Context>> mContextMap = new HashMap();
    WeakReference<Dialog> mDialogRef;
    JsCallBridge mJsCallBridge;
    BzMiscListener mMiscListener = null;
    BzMtopParamSetListner mMtopParamListener = null;
    BzJsCallNetListener mNetLisenter = null;
    BzPageStatusListener mPageStatusListener = null;
    BzPayListener mPayListener = null;
    Runnable mPostDestroy = new Runnable() {
        public void run() {
            System.gc();
        }
    };
    private BlitzPlugin.JsCallback mPropertyGetListener = new BlitzPlugin.JsCallback() {
        /* JADX WARNING: Removed duplicated region for block: B:16:0x0059  */
        /* JADX WARNING: Removed duplicated region for block: B:18:0x0062  */
        /* JADX WARNING: Removed duplicated region for block: B:24:0x006e  */
        /* JADX WARNING: Removed duplicated region for block: B:26:0x0077  */
        /* JADX WARNING: Removed duplicated region for block: B:32:0x0083  */
        /* JADX WARNING: Removed duplicated region for block: B:34:0x008c  */
        /* JADX WARNING: Removed duplicated region for block: B:40:0x0098  */
        /* JADX WARNING: Removed duplicated region for block: B:42:0x00a1  */
        /* JADX WARNING: Removed duplicated region for block: B:48:0x00ad  */
        /* JADX WARNING: Removed duplicated region for block: B:50:0x00b6  */
        /* JADX WARNING: Removed duplicated region for block: B:56:0x00c2  */
        /* JADX WARNING: Removed duplicated region for block: B:58:0x00cb  */
        /* JADX WARNING: Removed duplicated region for block: B:64:0x00d7  */
        /* JADX WARNING: Removed duplicated region for block: B:66:0x00e1  */
        /* JADX WARNING: Removed duplicated region for block: B:71:0x00eb  */
        /* JADX WARNING: Removed duplicated region for block: B:74:0x00f4  */
        /* JADX WARNING: Unknown top exception splitter block from list: {B:60:0x00d1=Splitter:B:60:0x00d1, B:36:0x0092=Splitter:B:36:0x0092, B:12:0x0053=Splitter:B:12:0x0053, B:44:0x00a7=Splitter:B:44:0x00a7, B:20:0x0068=Splitter:B:20:0x0068, B:52:0x00bc=Splitter:B:52:0x00bc, B:28:0x007d=Splitter:B:28:0x007d} */
        /* Code decompiled incorrectly, please refer to instructions dump. */
        public void onCall(java.lang.String r17, long r18) {
            /*
                r16 = this;
                r8 = 0
                org.json.JSONObject r7 = new org.json.JSONObject     // Catch:{ SecurityException -> 0x0052, InstantiationException -> 0x0067, InvocationTargetException -> 0x007c, NoSuchMethodException -> 0x0091, IllegalAccessException -> 0x00a6, ClassNotFoundException -> 0x00bb, JSONException -> 0x00d0 }
                r0 = r17
                r7.<init>(r0)     // Catch:{ SecurityException -> 0x0052, InstantiationException -> 0x0067, InvocationTargetException -> 0x007c, NoSuchMethodException -> 0x0091, IllegalAccessException -> 0x00a6, ClassNotFoundException -> 0x00bb, JSONException -> 0x00d0 }
                java.lang.String r11 = "key"
                java.lang.String r5 = r7.getString(r11)     // Catch:{ SecurityException -> 0x0052, InstantiationException -> 0x0067, InvocationTargetException -> 0x007c, NoSuchMethodException -> 0x0091, IllegalAccessException -> 0x00a6, ClassNotFoundException -> 0x00bb, JSONException -> 0x00d0 }
                java.lang.String r11 = "android.os.SystemProperties"
                java.lang.Class r2 = java.lang.Class.forName(r11)     // Catch:{ SecurityException -> 0x0052, InstantiationException -> 0x0067, InvocationTargetException -> 0x007c, NoSuchMethodException -> 0x0091, IllegalAccessException -> 0x00a6, ClassNotFoundException -> 0x00bb, JSONException -> 0x00d0 }
                java.lang.String r11 = "get"
                r12 = 1
                java.lang.Class[] r12 = new java.lang.Class[r12]     // Catch:{ SecurityException -> 0x0052, InstantiationException -> 0x0067, InvocationTargetException -> 0x007c, NoSuchMethodException -> 0x0091, IllegalAccessException -> 0x00a6, ClassNotFoundException -> 0x00bb, JSONException -> 0x00d0 }
                r13 = 0
                java.lang.Class<java.lang.String> r14 = java.lang.String.class
                r12[r13] = r14     // Catch:{ SecurityException -> 0x0052, InstantiationException -> 0x0067, InvocationTargetException -> 0x007c, NoSuchMethodException -> 0x0091, IllegalAccessException -> 0x00a6, ClassNotFoundException -> 0x00bb, JSONException -> 0x00d0 }
                java.lang.reflect.Method r4 = r2.getMethod(r11, r12)     // Catch:{ SecurityException -> 0x0052, InstantiationException -> 0x0067, InvocationTargetException -> 0x007c, NoSuchMethodException -> 0x0091, IllegalAccessException -> 0x00a6, ClassNotFoundException -> 0x00bb, JSONException -> 0x00d0 }
                java.lang.Object r6 = r2.newInstance()     // Catch:{ SecurityException -> 0x0052, InstantiationException -> 0x0067, InvocationTargetException -> 0x007c, NoSuchMethodException -> 0x0091, IllegalAccessException -> 0x00a6, ClassNotFoundException -> 0x00bb, JSONException -> 0x00d0 }
                r11 = 1
                java.lang.Object[] r11 = new java.lang.Object[r11]     // Catch:{ SecurityException -> 0x0052, InstantiationException -> 0x0067, InvocationTargetException -> 0x007c, NoSuchMethodException -> 0x0091, IllegalAccessException -> 0x00a6, ClassNotFoundException -> 0x00bb, JSONException -> 0x00d0 }
                r12 = 0
                r11[r12] = r5     // Catch:{ SecurityException -> 0x0052, InstantiationException -> 0x0067, InvocationTargetException -> 0x007c, NoSuchMethodException -> 0x0091, IllegalAccessException -> 0x00a6, ClassNotFoundException -> 0x00bb, JSONException -> 0x00d0 }
                java.lang.Object r10 = r4.invoke(r6, r11)     // Catch:{ SecurityException -> 0x0052, InstantiationException -> 0x0067, InvocationTargetException -> 0x007c, NoSuchMethodException -> 0x0091, IllegalAccessException -> 0x00a6, ClassNotFoundException -> 0x00bb, JSONException -> 0x00d0 }
                java.lang.String r10 = (java.lang.String) r10     // Catch:{ SecurityException -> 0x0052, InstantiationException -> 0x0067, InvocationTargetException -> 0x007c, NoSuchMethodException -> 0x0091, IllegalAccessException -> 0x00a6, ClassNotFoundException -> 0x00bb, JSONException -> 0x00d0 }
                org.json.JSONObject r9 = new org.json.JSONObject     // Catch:{ SecurityException -> 0x0052, InstantiationException -> 0x0067, InvocationTargetException -> 0x007c, NoSuchMethodException -> 0x0091, IllegalAccessException -> 0x00a6, ClassNotFoundException -> 0x00bb, JSONException -> 0x00d0 }
                r9.<init>()     // Catch:{ SecurityException -> 0x0052, InstantiationException -> 0x0067, InvocationTargetException -> 0x007c, NoSuchMethodException -> 0x0091, IllegalAccessException -> 0x00a6, ClassNotFoundException -> 0x00bb, JSONException -> 0x00d0 }
                java.lang.String r11 = "result"
                r9.put(r11, r10)     // Catch:{ SecurityException -> 0x0111, InstantiationException -> 0x010d, InvocationTargetException -> 0x0109, NoSuchMethodException -> 0x0106, IllegalAccessException -> 0x0103, ClassNotFoundException -> 0x0100, JSONException -> 0x00fd, all -> 0x00f9 }
                r12 = 1
                if (r9 != 0) goto L_0x004d
                java.lang.String r11 = ""
            L_0x0046:
                r0 = r18
                com.yunos.tv.blitz.BlitzPlugin.responseJs(r12, r11, r0)
                r8 = r9
            L_0x004c:
                return
            L_0x004d:
                java.lang.String r11 = r9.toString()
                goto L_0x0046
            L_0x0052:
                r3 = move-exception
            L_0x0053:
                r3.printStackTrace()     // Catch:{ all -> 0x00e6 }
                r12 = 1
                if (r8 != 0) goto L_0x0062
                java.lang.String r11 = ""
            L_0x005c:
                r0 = r18
                com.yunos.tv.blitz.BlitzPlugin.responseJs(r12, r11, r0)
                goto L_0x004c
            L_0x0062:
                java.lang.String r11 = r8.toString()
                goto L_0x005c
            L_0x0067:
                r3 = move-exception
            L_0x0068:
                r3.printStackTrace()     // Catch:{ all -> 0x00e6 }
                r12 = 1
                if (r8 != 0) goto L_0x0077
                java.lang.String r11 = ""
            L_0x0071:
                r0 = r18
                com.yunos.tv.blitz.BlitzPlugin.responseJs(r12, r11, r0)
                goto L_0x004c
            L_0x0077:
                java.lang.String r11 = r8.toString()
                goto L_0x0071
            L_0x007c:
                r3 = move-exception
            L_0x007d:
                r3.printStackTrace()     // Catch:{ all -> 0x00e6 }
                r12 = 1
                if (r8 != 0) goto L_0x008c
                java.lang.String r11 = ""
            L_0x0086:
                r0 = r18
                com.yunos.tv.blitz.BlitzPlugin.responseJs(r12, r11, r0)
                goto L_0x004c
            L_0x008c:
                java.lang.String r11 = r8.toString()
                goto L_0x0086
            L_0x0091:
                r3 = move-exception
            L_0x0092:
                r3.printStackTrace()     // Catch:{ all -> 0x00e6 }
                r12 = 1
                if (r8 != 0) goto L_0x00a1
                java.lang.String r11 = ""
            L_0x009b:
                r0 = r18
                com.yunos.tv.blitz.BlitzPlugin.responseJs(r12, r11, r0)
                goto L_0x004c
            L_0x00a1:
                java.lang.String r11 = r8.toString()
                goto L_0x009b
            L_0x00a6:
                r3 = move-exception
            L_0x00a7:
                r3.printStackTrace()     // Catch:{ all -> 0x00e6 }
                r12 = 1
                if (r8 != 0) goto L_0x00b6
                java.lang.String r11 = ""
            L_0x00b0:
                r0 = r18
                com.yunos.tv.blitz.BlitzPlugin.responseJs(r12, r11, r0)
                goto L_0x004c
            L_0x00b6:
                java.lang.String r11 = r8.toString()
                goto L_0x00b0
            L_0x00bb:
                r3 = move-exception
            L_0x00bc:
                r3.printStackTrace()     // Catch:{ all -> 0x00e6 }
                r12 = 1
                if (r8 != 0) goto L_0x00cb
                java.lang.String r11 = ""
            L_0x00c5:
                r0 = r18
                com.yunos.tv.blitz.BlitzPlugin.responseJs(r12, r11, r0)
                goto L_0x004c
            L_0x00cb:
                java.lang.String r11 = r8.toString()
                goto L_0x00c5
            L_0x00d0:
                r3 = move-exception
            L_0x00d1:
                r3.printStackTrace()     // Catch:{ all -> 0x00e6 }
                r12 = 1
                if (r8 != 0) goto L_0x00e1
                java.lang.String r11 = ""
            L_0x00da:
                r0 = r18
                com.yunos.tv.blitz.BlitzPlugin.responseJs(r12, r11, r0)
                goto L_0x004c
            L_0x00e1:
                java.lang.String r11 = r8.toString()
                goto L_0x00da
            L_0x00e6:
                r11 = move-exception
                r12 = r11
            L_0x00e8:
                r13 = 1
                if (r8 != 0) goto L_0x00f4
                java.lang.String r11 = ""
            L_0x00ee:
                r0 = r18
                com.yunos.tv.blitz.BlitzPlugin.responseJs(r13, r11, r0)
                throw r12
            L_0x00f4:
                java.lang.String r11 = r8.toString()
                goto L_0x00ee
            L_0x00f9:
                r11 = move-exception
                r12 = r11
                r8 = r9
                goto L_0x00e8
            L_0x00fd:
                r3 = move-exception
                r8 = r9
                goto L_0x00d1
            L_0x0100:
                r3 = move-exception
                r8 = r9
                goto L_0x00bc
            L_0x0103:
                r3 = move-exception
                r8 = r9
                goto L_0x00a7
            L_0x0106:
                r3 = move-exception
                r8 = r9
                goto L_0x0092
            L_0x0109:
                r3 = move-exception
                r8 = r9
                goto L_0x007d
            L_0x010d:
                r3 = move-exception
                r8 = r9
                goto L_0x0068
            L_0x0111:
                r3 = move-exception
                r8 = r9
                goto L_0x0053
            */
            throw new UnsupportedOperationException("Method not decompiled: com.yunos.tv.blitz.global.BzAppMain.AnonymousClass5.onCall(java.lang.String, long):void");
        }
    };
    private BlitzPlugin.JsCallback mPropertySetListener = new BlitzPlugin.JsCallback() {
        /* JADX WARNING: Removed duplicated region for block: B:16:0x0066  */
        /* JADX WARNING: Removed duplicated region for block: B:18:0x006f  */
        /* JADX WARNING: Removed duplicated region for block: B:24:0x007b  */
        /* JADX WARNING: Removed duplicated region for block: B:26:0x0084  */
        /* JADX WARNING: Removed duplicated region for block: B:32:0x0090  */
        /* JADX WARNING: Removed duplicated region for block: B:34:0x0099  */
        /* JADX WARNING: Removed duplicated region for block: B:40:0x00a5  */
        /* JADX WARNING: Removed duplicated region for block: B:42:0x00ae  */
        /* JADX WARNING: Removed duplicated region for block: B:48:0x00ba  */
        /* JADX WARNING: Removed duplicated region for block: B:50:0x00c3  */
        /* JADX WARNING: Removed duplicated region for block: B:56:0x00cf  */
        /* JADX WARNING: Removed duplicated region for block: B:58:0x00d8  */
        /* JADX WARNING: Removed duplicated region for block: B:64:0x00e4  */
        /* JADX WARNING: Removed duplicated region for block: B:66:0x00ee  */
        /* JADX WARNING: Removed duplicated region for block: B:71:0x00f8  */
        /* JADX WARNING: Removed duplicated region for block: B:74:0x0101  */
        /* JADX WARNING: Unknown top exception splitter block from list: {B:12:0x0060=Splitter:B:12:0x0060, B:44:0x00b4=Splitter:B:44:0x00b4, B:20:0x0075=Splitter:B:20:0x0075, B:52:0x00c9=Splitter:B:52:0x00c9, B:28:0x008a=Splitter:B:28:0x008a, B:60:0x00de=Splitter:B:60:0x00de, B:36:0x009f=Splitter:B:36:0x009f} */
        /* Code decompiled incorrectly, please refer to instructions dump. */
        public void onCall(java.lang.String r17, long r18) {
            /*
                r16 = this;
                r8 = 0
                org.json.JSONObject r7 = new org.json.JSONObject     // Catch:{ SecurityException -> 0x005f, InstantiationException -> 0x0074, InvocationTargetException -> 0x0089, NoSuchMethodException -> 0x009e, IllegalAccessException -> 0x00b3, ClassNotFoundException -> 0x00c8, JSONException -> 0x00dd }
                r0 = r17
                r7.<init>(r0)     // Catch:{ SecurityException -> 0x005f, InstantiationException -> 0x0074, InvocationTargetException -> 0x0089, NoSuchMethodException -> 0x009e, IllegalAccessException -> 0x00b3, ClassNotFoundException -> 0x00c8, JSONException -> 0x00dd }
                java.lang.String r11 = "key"
                java.lang.String r5 = r7.getString(r11)     // Catch:{ SecurityException -> 0x005f, InstantiationException -> 0x0074, InvocationTargetException -> 0x0089, NoSuchMethodException -> 0x009e, IllegalAccessException -> 0x00b3, ClassNotFoundException -> 0x00c8, JSONException -> 0x00dd }
                java.lang.String r11 = "value"
                java.lang.String r10 = r7.getString(r11)     // Catch:{ SecurityException -> 0x005f, InstantiationException -> 0x0074, InvocationTargetException -> 0x0089, NoSuchMethodException -> 0x009e, IllegalAccessException -> 0x00b3, ClassNotFoundException -> 0x00c8, JSONException -> 0x00dd }
                java.lang.String r11 = "android.os.SystemProperties"
                java.lang.Class r2 = java.lang.Class.forName(r11)     // Catch:{ SecurityException -> 0x005f, InstantiationException -> 0x0074, InvocationTargetException -> 0x0089, NoSuchMethodException -> 0x009e, IllegalAccessException -> 0x00b3, ClassNotFoundException -> 0x00c8, JSONException -> 0x00dd }
                java.lang.String r11 = "set"
                r12 = 2
                java.lang.Class[] r12 = new java.lang.Class[r12]     // Catch:{ SecurityException -> 0x005f, InstantiationException -> 0x0074, InvocationTargetException -> 0x0089, NoSuchMethodException -> 0x009e, IllegalAccessException -> 0x00b3, ClassNotFoundException -> 0x00c8, JSONException -> 0x00dd }
                r13 = 0
                java.lang.Class<java.lang.String> r14 = java.lang.String.class
                r12[r13] = r14     // Catch:{ SecurityException -> 0x005f, InstantiationException -> 0x0074, InvocationTargetException -> 0x0089, NoSuchMethodException -> 0x009e, IllegalAccessException -> 0x00b3, ClassNotFoundException -> 0x00c8, JSONException -> 0x00dd }
                r13 = 1
                java.lang.Class<java.lang.String> r14 = java.lang.String.class
                r12[r13] = r14     // Catch:{ SecurityException -> 0x005f, InstantiationException -> 0x0074, InvocationTargetException -> 0x0089, NoSuchMethodException -> 0x009e, IllegalAccessException -> 0x00b3, ClassNotFoundException -> 0x00c8, JSONException -> 0x00dd }
                java.lang.reflect.Method r4 = r2.getMethod(r11, r12)     // Catch:{ SecurityException -> 0x005f, InstantiationException -> 0x0074, InvocationTargetException -> 0x0089, NoSuchMethodException -> 0x009e, IllegalAccessException -> 0x00b3, ClassNotFoundException -> 0x00c8, JSONException -> 0x00dd }
                java.lang.Object r6 = r2.newInstance()     // Catch:{ SecurityException -> 0x005f, InstantiationException -> 0x0074, InvocationTargetException -> 0x0089, NoSuchMethodException -> 0x009e, IllegalAccessException -> 0x00b3, ClassNotFoundException -> 0x00c8, JSONException -> 0x00dd }
                r11 = 2
                java.lang.Object[] r11 = new java.lang.Object[r11]     // Catch:{ SecurityException -> 0x005f, InstantiationException -> 0x0074, InvocationTargetException -> 0x0089, NoSuchMethodException -> 0x009e, IllegalAccessException -> 0x00b3, ClassNotFoundException -> 0x00c8, JSONException -> 0x00dd }
                r12 = 0
                r11[r12] = r5     // Catch:{ SecurityException -> 0x005f, InstantiationException -> 0x0074, InvocationTargetException -> 0x0089, NoSuchMethodException -> 0x009e, IllegalAccessException -> 0x00b3, ClassNotFoundException -> 0x00c8, JSONException -> 0x00dd }
                r12 = 1
                r11[r12] = r10     // Catch:{ SecurityException -> 0x005f, InstantiationException -> 0x0074, InvocationTargetException -> 0x0089, NoSuchMethodException -> 0x009e, IllegalAccessException -> 0x00b3, ClassNotFoundException -> 0x00c8, JSONException -> 0x00dd }
                r4.invoke(r6, r11)     // Catch:{ SecurityException -> 0x005f, InstantiationException -> 0x0074, InvocationTargetException -> 0x0089, NoSuchMethodException -> 0x009e, IllegalAccessException -> 0x00b3, ClassNotFoundException -> 0x00c8, JSONException -> 0x00dd }
                org.json.JSONObject r9 = new org.json.JSONObject     // Catch:{ SecurityException -> 0x005f, InstantiationException -> 0x0074, InvocationTargetException -> 0x0089, NoSuchMethodException -> 0x009e, IllegalAccessException -> 0x00b3, ClassNotFoundException -> 0x00c8, JSONException -> 0x00dd }
                r9.<init>()     // Catch:{ SecurityException -> 0x005f, InstantiationException -> 0x0074, InvocationTargetException -> 0x0089, NoSuchMethodException -> 0x009e, IllegalAccessException -> 0x00b3, ClassNotFoundException -> 0x00c8, JSONException -> 0x00dd }
                java.lang.String r11 = "result"
                r12 = 0
                r9.put(r11, r12)     // Catch:{ SecurityException -> 0x011e, InstantiationException -> 0x011a, InvocationTargetException -> 0x0116, NoSuchMethodException -> 0x0113, IllegalAccessException -> 0x0110, ClassNotFoundException -> 0x010d, JSONException -> 0x010a, all -> 0x0106 }
                r12 = 1
                if (r9 != 0) goto L_0x005a
                java.lang.String r11 = ""
            L_0x0053:
                r0 = r18
                com.yunos.tv.blitz.BlitzPlugin.responseJs(r12, r11, r0)
                r8 = r9
            L_0x0059:
                return
            L_0x005a:
                java.lang.String r11 = r9.toString()
                goto L_0x0053
            L_0x005f:
                r3 = move-exception
            L_0x0060:
                r3.printStackTrace()     // Catch:{ all -> 0x00f3 }
                r12 = 1
                if (r8 != 0) goto L_0x006f
                java.lang.String r11 = ""
            L_0x0069:
                r0 = r18
                com.yunos.tv.blitz.BlitzPlugin.responseJs(r12, r11, r0)
                goto L_0x0059
            L_0x006f:
                java.lang.String r11 = r8.toString()
                goto L_0x0069
            L_0x0074:
                r3 = move-exception
            L_0x0075:
                r3.printStackTrace()     // Catch:{ all -> 0x00f3 }
                r12 = 1
                if (r8 != 0) goto L_0x0084
                java.lang.String r11 = ""
            L_0x007e:
                r0 = r18
                com.yunos.tv.blitz.BlitzPlugin.responseJs(r12, r11, r0)
                goto L_0x0059
            L_0x0084:
                java.lang.String r11 = r8.toString()
                goto L_0x007e
            L_0x0089:
                r3 = move-exception
            L_0x008a:
                r3.printStackTrace()     // Catch:{ all -> 0x00f3 }
                r12 = 1
                if (r8 != 0) goto L_0x0099
                java.lang.String r11 = ""
            L_0x0093:
                r0 = r18
                com.yunos.tv.blitz.BlitzPlugin.responseJs(r12, r11, r0)
                goto L_0x0059
            L_0x0099:
                java.lang.String r11 = r8.toString()
                goto L_0x0093
            L_0x009e:
                r3 = move-exception
            L_0x009f:
                r3.printStackTrace()     // Catch:{ all -> 0x00f3 }
                r12 = 1
                if (r8 != 0) goto L_0x00ae
                java.lang.String r11 = ""
            L_0x00a8:
                r0 = r18
                com.yunos.tv.blitz.BlitzPlugin.responseJs(r12, r11, r0)
                goto L_0x0059
            L_0x00ae:
                java.lang.String r11 = r8.toString()
                goto L_0x00a8
            L_0x00b3:
                r3 = move-exception
            L_0x00b4:
                r3.printStackTrace()     // Catch:{ all -> 0x00f3 }
                r12 = 1
                if (r8 != 0) goto L_0x00c3
                java.lang.String r11 = ""
            L_0x00bd:
                r0 = r18
                com.yunos.tv.blitz.BlitzPlugin.responseJs(r12, r11, r0)
                goto L_0x0059
            L_0x00c3:
                java.lang.String r11 = r8.toString()
                goto L_0x00bd
            L_0x00c8:
                r3 = move-exception
            L_0x00c9:
                r3.printStackTrace()     // Catch:{ all -> 0x00f3 }
                r12 = 1
                if (r8 != 0) goto L_0x00d8
                java.lang.String r11 = ""
            L_0x00d2:
                r0 = r18
                com.yunos.tv.blitz.BlitzPlugin.responseJs(r12, r11, r0)
                goto L_0x0059
            L_0x00d8:
                java.lang.String r11 = r8.toString()
                goto L_0x00d2
            L_0x00dd:
                r3 = move-exception
            L_0x00de:
                r3.printStackTrace()     // Catch:{ all -> 0x00f3 }
                r12 = 1
                if (r8 != 0) goto L_0x00ee
                java.lang.String r11 = ""
            L_0x00e7:
                r0 = r18
                com.yunos.tv.blitz.BlitzPlugin.responseJs(r12, r11, r0)
                goto L_0x0059
            L_0x00ee:
                java.lang.String r11 = r8.toString()
                goto L_0x00e7
            L_0x00f3:
                r11 = move-exception
                r12 = r11
            L_0x00f5:
                r13 = 1
                if (r8 != 0) goto L_0x0101
                java.lang.String r11 = ""
            L_0x00fb:
                r0 = r18
                com.yunos.tv.blitz.BlitzPlugin.responseJs(r13, r11, r0)
                throw r12
            L_0x0101:
                java.lang.String r11 = r8.toString()
                goto L_0x00fb
            L_0x0106:
                r11 = move-exception
                r12 = r11
                r8 = r9
                goto L_0x00f5
            L_0x010a:
                r3 = move-exception
                r8 = r9
                goto L_0x00de
            L_0x010d:
                r3 = move-exception
                r8 = r9
                goto L_0x00c9
            L_0x0110:
                r3 = move-exception
                r8 = r9
                goto L_0x00b4
            L_0x0113:
                r3 = move-exception
                r8 = r9
                goto L_0x009f
            L_0x0116:
                r3 = move-exception
                r8 = r9
                goto L_0x008a
            L_0x011a:
                r3 = move-exception
                r8 = r9
                goto L_0x0075
            L_0x011e:
                r3 = move-exception
                r8 = r9
                goto L_0x0060
            */
            throw new UnsupportedOperationException("Method not decompiled: com.yunos.tv.blitz.global.BzAppMain.AnonymousClass4.onCall(java.lang.String, long):void");
        }
    };
    int mSoftKeyboardHeight = 0;
    boolean mSoftKeyboardOpened = false;
    boolean mUCacheEnable = false;
    Handler mUIHandler = new Handler();
    BzJsCallUIListener mUIListener = null;
    long pageLoadEndTime = 0;
    long pageLoadStartTime = 0;

    static {
        System.loadLibrary("thaibrk");
        System.loadLibrary("freetype_tb_1.0.2");
        System.loadLibrary("cximage_tb_1.0.2");
        System.loadLibrary("curl_tb_1.0.3");
        System.loadLibrary("lightcore_tb_1.0.6");
        System.loadLibrary("blitzview_1.1.5.001");
        System.loadLibrary("blitzview_platform_1.1.5.001");
        System.loadLibrary("plugin-movie_1.1.5.001");
    }

    public BzAppMain(Context context) {
        this.mContext = context;
    }

    public String getAppkey() {
        String appkey = new StaticDataStore(new ContextWrapper(this.mContext)).getAppKey(new DataContext(0, (byte[]) null));
        Log.d(TAG, "test adk getappkey=" + appkey);
        return appkey;
    }

    public void initMtopSdk(int onlineIndex, int dailyIndex) {
        MtopSetting.setAppKeyIndex(onlineIndex, dailyIndex);
        String ttid = BzAppConfig.getInstance().getTtid();
        mMtopInstance = Mtop.instance(BzAppConfig.context.getContext(), ttid);
        LoginHelper helper = BzApplication.getLoginHelper(BzAppConfig.context.getContext());
        if (!TextUtils.isEmpty(helper.getSessionId())) {
            mMtopInstance.registerSessionInfo(helper.getSessionId(), helper.getUserId());
        }
        mMtopInstance.switchEnvMode(EnvModeEnum.ONLINE);
        if (BzAppConfig.env == BzEnvEnum.ONLINE) {
            mMtopInstance.switchEnvMode(EnvModeEnum.ONLINE);
        } else if (BzAppConfig.env == BzEnvEnum.PRE) {
            mMtopInstance.switchEnvMode(EnvModeEnum.PREPARE);
        } else {
            mMtopInstance.switchEnvMode(EnvModeEnum.TEST);
        }
        mMtopInstance.logSwitch(true);
        mMtopInstance.registerDeviceId(SDKConfig.getInstance().getGlobalDeviceId());
        SDKUtils.registerTtid(ttid);
    }

    public boolean initBlitz() {
        BzAppConfig.context = this;
        this.mBzAppJniContext = new BzAppJniContext();
        if (!this.mBzAppJniContext.initAppJniContext() || !this.mBzAppJniContext.nativeReadFromAssets(this.mContext.getAssets())) {
            return false;
        }
        BlitzPlugin.bindingJs("getSystemPro", this.mPropertyGetListener);
        BlitzPlugin.bindingJs("setSystemPro", this.mPropertySetListener);
        this.mContentResolver = new BzContentResolver();
        LoginHelper loginHelper = BzApplication.getLoginHelper(this.mContext);
        if (loginHelper != null) {
            loginHelper.addReceiveLoginListener(new LoginHelper.SyncLoginListener() {
                public void onLogin(boolean isSuccess) {
                    if (BzAppMain.this.mActivityRef != null && BzAppMain.this.mActivityRef.get() != null && (BzAppMain.this.mActivityRef.get() instanceof BzBaseActivity)) {
                        BzBaseActivity bzActivity = (BzBaseActivity) BzAppMain.this.mActivityRef.get();
                        if (bzActivity.getBlitzContext() != null) {
                            bzActivity.getBlitzContext().loginStatusChange(isSuccess);
                        }
                    }
                }
            });
        }
        setJsCallAccountListener(new BzJsCallImpAccountListener());
        setJsCallNetListener(new BzJsCallImpNetListener());
        setJsCallPayListener(new BzJsCallPayImpListener());
        if (this.mContext instanceof Application) {
            ((Application) this.mContext).registerActivityLifecycleCallbacks(new Application.ActivityLifecycleCallbacks() {
                public void onActivityStopped(Activity activity) {
                }

                public void onActivityStarted(Activity activity) {
                }

                public void onActivitySaveInstanceState(Activity activity, Bundle outState) {
                }

                public void onActivityResumed(Activity activity) {
                    BzAppMain.this.mActivityRef = new WeakReference<>(activity);
                }

                public void onActivityPaused(Activity activity) {
                }

                public void onActivityDestroyed(Activity activity) {
                    if (activity instanceof BzBaseActivity) {
                        BzAppMain.this.mUIHandler.postDelayed(BzAppMain.this.mPostDestroy, 1000);
                    }
                    BzAppMain.this.removeContext(activity);
                    BzAppMain bzAppMain = BzAppMain.this;
                    bzAppMain.mActivityCount--;
                }

                public void onActivityCreated(Activity activity, Bundle savedInstanceState) {
                    BzAppMain.this.mActivityCount++;
                }
            });
        }
        this.mJsCallBridge = new JsCallBridge(this.mContext);
        return true;
    }

    public Context getContext() {
        return this.mContext;
    }

    public void updateBlitzUCache() {
        this.mUCacheEnable = true;
        BlitzUCacheBridge.getInstance(this.mContext).updateBlitzUCache();
    }

    public void initBzParam(BzAppParams param) {
        BzAppContext.setBzAppParams(param);
    }

    public void setEnvMode(BzEnvEnum mode) {
        BzAppContext.setEnvMode(mode);
    }

    public void setBackgroundImgFromAssets(String path, int coreIndex) {
        this.mBzAppJniContext.nativeSetBackgroundImgFromAssets(path, coreIndex);
    }

    public void clearBackgroundImg(int coreIndex) {
        this.mBzAppJniContext.nativeClearBackgroundImg(coreIndex);
    }

    public void setAppGlobalListener(BzAppGlobalListener listener) {
        this.mAppGlobalListener = listener;
    }

    public BzAppGlobalListener getAppGlobalListener() {
        return this.mAppGlobalListener;
    }

    public void setPageStatusListener(BzPageStatusListener listener) {
        this.mPageStatusListener = listener;
    }

    public BzPageStatusListener getPageStatusListener() {
        return this.mPageStatusListener;
    }

    public void setJsCallUIListener(BzJsCallUIListener listener) {
        this.mUIListener = listener;
    }

    public BzJsCallUIListener getJsCallUIListener() {
        return this.mUIListener;
    }

    public void setJsCallBaseListener(BzJsCallBaseListener listener) {
        this.mBaseListener = listener;
    }

    public BzJsCallBaseListener getJsCallBaseListener() {
        return this.mBaseListener;
    }

    public void setJsCallNetListener(BzJsCallNetListener listener) {
        this.mNetLisenter = listener;
    }

    public BzJsCallNetListener getJsCallNetListener() {
        return this.mNetLisenter;
    }

    public void setJsCallPayListener(BzPayListener listener) {
        this.mPayListener = listener;
    }

    public BzPayListener getJsCallPayListener() {
        return this.mPayListener;
    }

    public void setJsCallAccountListener(BzJsCallAccountListener listener) {
        this.mAccountListener = listener;
    }

    public BzJsCallAccountListener getJsCallAccountListener() {
        return this.mAccountListener;
    }

    public void setMiscListener(BzMiscListener listener) {
        this.mMiscListener = listener;
    }

    public BzMiscListener getMiscListener() {
        return this.mMiscListener;
    }

    public void setMtopParamListener(BzMtopParamSetListner listener) {
        this.mMtopParamListener = listener;
    }

    public BzMtopParamSetListner getMtopParamListener() {
        return this.mMtopParamListener;
    }

    public boolean replyCallBack(int callback, boolean success, String result) {
        if (this.mJsCallBridge != null) {
            return this.mJsCallBridge.replayCallback(callback, success, result);
        }
        return false;
    }

    public WeakReference<Activity> getCurrentActivity() {
        return this.mActivityRef;
    }

    public WeakReference<Context> getCoreIndexContext(int coreIndex) {
        return this.mContextMap.get(Integer.valueOf(coreIndex));
    }

    public int getCurrentActivityCount() {
        return this.mActivityCount;
    }

    public void setCurrentDialog(Dialog dialog) {
        this.mDialogRef = null;
        this.mDialogRef = new WeakReference<>(dialog);
    }

    public WeakReference<Dialog> getCurrentDialog() {
        return this.mDialogRef;
    }

    public void addContext(int coreIndex, Context context) {
        this.mContextMap.put(Integer.valueOf(coreIndex), new WeakReference(context));
    }

    public void removeContext(Context context) {
        int coreIndex = 0;
        Iterator<Map.Entry<Integer, WeakReference<Context>>> it = this.mContextMap.entrySet().iterator();
        while (true) {
            if (!it.hasNext()) {
                break;
            }
            Map.Entry<Integer, WeakReference<Context>> entry = it.next();
            if (entry.getValue().get() == context) {
                coreIndex = entry.getKey().intValue();
                break;
            }
        }
        this.mContextMap.remove(Integer.valueOf(coreIndex));
    }

    public boolean getUCacheEnable() {
        return this.mUCacheEnable;
    }

    public BzContentResolver getContentResolver() {
        return this.mContentResolver;
    }

    public void pageLoadStart() {
        this.pageLoadStartTime = System.currentTimeMillis();
    }

    public void pageLoadEnd(int coreIndex) {
        this.pageLoadEndTime = System.currentTimeMillis();
        if (this.mActivityRef != null) {
            this.mContextMap.put(Integer.valueOf(coreIndex), new WeakReference(this.mActivityRef.get()));
        }
    }

    public long getPageLoadTime() {
        return this.pageLoadEndTime - this.pageLoadStartTime;
    }

    public void setSoftKeyboardStatus(boolean status) {
        this.mSoftKeyboardOpened = status;
    }

    public boolean getSoftKeyboardStatus() {
        return this.mSoftKeyboardOpened;
    }

    public void setSoftKeyboardHeight(int height) {
        this.mSoftKeyboardHeight = height;
    }

    public int getSoftKeyboardHeight() {
        return this.mSoftKeyboardHeight;
    }

    public boolean setMtopDomain(String domain) {
        if (this.mBzAppJniContext != null) {
            return this.mBzAppJniContext.nativeSetMtopDomain(domain);
        }
        return false;
    }
}
