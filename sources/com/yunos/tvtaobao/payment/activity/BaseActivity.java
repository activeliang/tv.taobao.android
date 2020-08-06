package com.yunos.tvtaobao.payment.activity;

import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import com.yunos.tvtaobao.payment.analytics.Utils;
import java.util.Map;
import java.util.regex.Pattern;

public abstract class BaseActivity extends Activity {
    private String mApp;
    private String mFrom;
    private String mHuoDong;
    protected String mPageName;

    /* access modifiers changed from: protected */
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    /* access modifiers changed from: protected */
    public void onResume() {
        super.onResume();
        enterUT();
    }

    /* access modifiers changed from: protected */
    public void onPause() {
        super.onPause();
        exitUT();
    }

    public String getFullPageName() {
        if (TextUtils.isEmpty(this.mPageName)) {
            this.mPageName = getPageName();
        }
        return this.mPageName;
    }

    public String getPageName() {
        return Pattern.compile("(activity|view|null|page|layout)$", 2).matcher(getClass().getSimpleName()).replaceAll("");
    }

    /* access modifiers changed from: protected */
    public void enterUT() {
        this.mPageName = getFullPageName();
        if (TextUtils.isEmpty(this.mPageName)) {
            throw new IllegalArgumentException("The PageName was null and TBS is open");
        }
        Utils.utPageAppear(this.mPageName, this.mPageName);
    }

    /* access modifiers changed from: protected */
    public void exitUT() {
        if (!TextUtils.isEmpty(this.mPageName)) {
            Utils.utUpdatePageProperties(this.mPageName, getPageProperties());
            Utils.utPageDisAppear(this.mPageName);
        }
    }

    public Map<String, String> getPageProperties() {
        return Utils.getProperties(this.mFrom, this.mHuoDong, this.mApp);
    }
}
