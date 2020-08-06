package com.taobao.business.mtop;

import android.app.Application;
import android.content.Context;
import android.support.annotation.Keep;
import android.text.TextUtils;
import com.taobao.tao.remotebusiness.IRemoteBaseListener;
import com.taobao.tao.remotebusiness.IRemoteListener;
import com.taobao.tao.remotebusiness.RemoteBusiness;
import mtopsdk.mtop.domain.IMTOPDataObject;

@Keep
public class MunionRemoteBusiness {
    public Application mApplication;
    private Integer mBizId;
    private IRemoteBaseListener mMtopListener;
    private RemoteBusiness mRemoteBusiness;

    public MunionRemoteBusiness(Application application) {
        this.mApplication = application;
    }

    private void startRequest(int i, IMTOPDataObject iMTOPDataObject, Class<?> cls, IRemoteBaseListener iRemoteBaseListener) {
        this.mRemoteBusiness = RemoteBusiness.build((Context) this.mApplication, iMTOPDataObject, (String) null);
        this.mRemoteBusiness.showLoginUI(false);
        if (this.mBizId != null) {
            this.mRemoteBusiness.setBizId(this.mBizId.intValue());
        }
        if (iRemoteBaseListener != null) {
            this.mRemoteBusiness.registeListener((IRemoteListener) iRemoteBaseListener);
        }
        this.mRemoteBusiness.startRequest(i, cls);
    }

    public void registeListener(IRemoteBaseListener iRemoteBaseListener) {
        this.mMtopListener = iRemoteBaseListener;
    }

    public void setBizId(String str) {
        if (!TextUtils.isEmpty(str) && TextUtils.isDigitsOnly(str)) {
            this.mBizId = Integer.valueOf(str);
        }
    }

    public void startRequest(int i, IMTOPDataObject iMTOPDataObject, Class<?> cls) {
        startRequest(i, iMTOPDataObject, cls, this.mMtopListener);
    }
}
