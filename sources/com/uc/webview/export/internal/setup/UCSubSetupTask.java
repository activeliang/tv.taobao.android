package com.uc.webview.export.internal.setup;

import android.content.Context;
import android.util.Pair;
import android.webkit.ValueCallback;
import com.uc.webview.export.annotations.Reflection;
import com.uc.webview.export.internal.setup.UCSubSetupTask;
import java.util.HashMap;

/* compiled from: ProGuard */
public class UCSubSetupTask<RETURN_TYPE extends UCSubSetupTask<RETURN_TYPE, CALLBACK_TYPE>, CALLBACK_TYPE extends UCSubSetupTask<RETURN_TYPE, CALLBACK_TYPE>> extends UCAsyncTask<RETURN_TYPE, CALLBACK_TYPE> {
    protected static final String EVENT_STAT = "stat";
    protected ClassLoader mCL;
    protected HashMap<String, Object> mOptions = new HashMap<>();
    protected ClassLoader mShellCL;
    protected Pair<String, HashMap<String, String>> mStat;
    protected UCMPackageInfo mUCM;

    public UCSubSetupTask(UCAsyncTask uCAsyncTask) {
        super(uCAsyncTask);
    }

    public UCSubSetupTask() {
        super((Runnable) null);
    }

    public final RETURN_TYPE setUCM(UCMPackageInfo uCMPackageInfo) {
        this.mUCM = uCMPackageInfo;
        return this;
    }

    public final RETURN_TYPE setClassLoader(ClassLoader classLoader) {
        this.mCL = classLoader;
        return this;
    }

    public final RETURN_TYPE setup(String str, Object obj) {
        this.mOptions.put(str, obj);
        return this;
    }

    @Reflection
    public final Pair<String, HashMap<String, String>> getStat() {
        return this.mStat;
    }

    /* access modifiers changed from: protected */
    public final RETURN_TYPE setOptions(HashMap<String, Object> hashMap) {
        this.mOptions = (HashMap) hashMap.clone();
        return this;
    }

    /* access modifiers changed from: protected */
    public final Object getOption(String str) {
        return this.mOptions.get(str);
    }

    /* access modifiers changed from: protected */
    public final Context getContext() {
        return (Context) getOption("CONTEXT");
    }

    public final void callbackStat(Pair<String, HashMap<String, String>> pair) {
        this.mStat = pair;
        callback(EVENT_STAT);
    }

    /* compiled from: ProGuard */
    public class a<CB_TYPE extends UCSubSetupTask<CB_TYPE, CB_TYPE>> implements ValueCallback<CB_TYPE> {
        public a() {
        }

        public final /* synthetic */ void onReceiveValue(Object obj) {
            UCSubSetupTask.this.callbackStat(((UCSubSetupTask) obj).getStat());
        }
    }
}
