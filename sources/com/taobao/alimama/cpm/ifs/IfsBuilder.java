package com.taobao.alimama.cpm.ifs;

import android.app.Application;
import android.support.annotation.Keep;
import android.support.annotation.NonNull;
import android.text.TextUtils;
import com.taobao.alimama.utils.BucketTools;
import com.taobao.munion.taosdk.CpmIfsCommitter;
import java.util.HashMap;
import java.util.Map;

@Keep
public class IfsBuilder {
    private Application mApplication;
    private Map<String, String> mArgsMap = new HashMap();
    private String mUrl;

    public IfsBuilder(@NonNull Application application, @NonNull String str) {
        this.mApplication = application;
        this.mUrl = str;
    }

    public String commit() {
        return BucketTools.c() ? new NEW_IfsCommitter(this.mUrl, this.mArgsMap).a() : new CpmIfsCommitter(this.mApplication, this.mArgsMap).a(this.mUrl);
    }

    public IfsBuilder withArg(String str, String str2) {
        if (!TextUtils.isEmpty(str) && !TextUtils.isEmpty(str2)) {
            this.mArgsMap.put(str, str2);
        }
        return this;
    }

    public IfsBuilder withArgNamespace(String str) {
        if (!TextUtils.isEmpty(str)) {
            this.mArgsMap.put("namespace", str);
        }
        return this;
    }

    public IfsBuilder withArgPid(String str) {
        if (!TextUtils.isEmpty(str)) {
            this.mArgsMap.put("pid", str);
        }
        return this;
    }
}
