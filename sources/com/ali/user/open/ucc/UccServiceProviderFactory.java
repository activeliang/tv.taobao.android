package com.ali.user.open.ucc;

import android.text.TextUtils;
import com.ali.user.open.core.Site;
import com.ali.user.open.ucc.alipay3.AlipayUccServiceProviderImpl;
import com.ali.user.open.ucc.eleme.ElemeUccServiceProviderImpl;
import com.ali.user.open.ucc.icbu.IcbuUccServiceProviderImpl;
import com.ali.user.open.ucc.taobao.TaobaoUccServiceProviderImpl;

public class UccServiceProviderFactory {
    private static volatile UccServiceProviderFactory instance;
    private static UccServiceProvider mAlipayUccServiceProvider;
    private UccServiceProvider mDefaultUccServiceProvider;
    private UccServiceProvider mElemeUccServiceProvider;
    private UccServiceProvider mIcbuUccServiceProvider;
    private UccServiceProvider mTaobaoUccServiceProvider;

    public static UccServiceProviderFactory getInstance() {
        if (instance == null) {
            synchronized (UccServiceProviderFactory.class) {
                if (instance == null) {
                    instance = new UccServiceProviderFactory();
                }
            }
        }
        return instance;
    }

    public UccServiceProvider getUccServiceProvider(String targetSite) {
        if (TextUtils.equals(targetSite, "alipay")) {
            if (mAlipayUccServiceProvider == null) {
                mAlipayUccServiceProvider = new AlipayUccServiceProviderImpl();
            }
            return mAlipayUccServiceProvider;
        } else if (TextUtils.equals(targetSite, Site.TAOBAO)) {
            if (this.mTaobaoUccServiceProvider == null) {
                this.mTaobaoUccServiceProvider = new TaobaoUccServiceProviderImpl();
            }
            return this.mTaobaoUccServiceProvider;
        } else if (TextUtils.equals(targetSite, Site.ELEME)) {
            if (this.mElemeUccServiceProvider == null) {
                this.mElemeUccServiceProvider = new ElemeUccServiceProviderImpl();
            }
            return this.mElemeUccServiceProvider;
        } else if (TextUtils.equals(targetSite, Site.ICBU)) {
            if (this.mIcbuUccServiceProvider == null) {
                this.mIcbuUccServiceProvider = new IcbuUccServiceProviderImpl();
            }
            return this.mIcbuUccServiceProvider;
        } else {
            if (this.mDefaultUccServiceProvider == null) {
                this.mDefaultUccServiceProvider = new DefaultUccServiceProviderImpl();
            }
            return this.mDefaultUccServiceProvider;
        }
    }

    public void cleanUp() {
        mAlipayUccServiceProvider = null;
        this.mTaobaoUccServiceProvider = null;
        this.mElemeUccServiceProvider = null;
        this.mIcbuUccServiceProvider = null;
        this.mDefaultUccServiceProvider = null;
    }
}
