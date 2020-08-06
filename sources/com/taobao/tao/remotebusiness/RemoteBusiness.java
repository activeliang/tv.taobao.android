package com.taobao.tao.remotebusiness;

import android.content.Context;
import mtopsdk.mtop.common.MtopListener;
import mtopsdk.mtop.domain.IMTOPDataObject;
import mtopsdk.mtop.domain.MtopRequest;
import mtopsdk.mtop.intf.Mtop;

@Deprecated
public class RemoteBusiness extends MtopBusiness {
    protected RemoteBusiness(Mtop mtop, IMTOPDataObject request, String ttid) {
        super(mtop, request, ttid);
    }

    protected RemoteBusiness(Mtop mtop, MtopRequest request, String ttid) {
        super(mtop, request, ttid);
    }

    @Deprecated
    public static void init(Context appContext, String ttid) {
        Mtop.instance(appContext, ttid);
    }

    public static RemoteBusiness build(IMTOPDataObject inputData, String ttid) {
        return new RemoteBusiness(Mtop.instance((Context) null, ttid), inputData, ttid);
    }

    public static RemoteBusiness build(IMTOPDataObject inputData) {
        return build(inputData, (String) null);
    }

    public static RemoteBusiness build(MtopRequest request, String ttid) {
        return new RemoteBusiness(Mtop.instance((Context) null, ttid), request, ttid);
    }

    public static RemoteBusiness build(MtopRequest request) {
        return build(request, (String) null);
    }

    @Deprecated
    public static RemoteBusiness build(Context appContext, IMTOPDataObject inputData, String ttid) {
        init(appContext, ttid);
        return build(inputData, ttid);
    }

    @Deprecated
    public static RemoteBusiness build(Context appContext, MtopRequest request, String ttid) {
        init(appContext, ttid);
        return build(request, ttid);
    }

    @Deprecated
    public RemoteBusiness setErrorNotifyAfterCache(boolean bNotify) {
        return (RemoteBusiness) super.setErrorNotifyAfterCache(bNotify);
    }

    @Deprecated
    public RemoteBusiness registeListener(MtopListener listener) {
        return (RemoteBusiness) super.registerListener(listener);
    }

    @Deprecated
    public RemoteBusiness registeListener(IRemoteListener listener) {
        return (RemoteBusiness) super.registerListener(listener);
    }

    public RemoteBusiness retryTime(int retryTimes) {
        return (RemoteBusiness) super.retryTime(retryTimes);
    }

    @Deprecated
    public RemoteBusiness showLoginUI(boolean showUI) {
        return (RemoteBusiness) super.showLoginUI(showUI);
    }

    @Deprecated
    public RemoteBusiness setBizId(int bizId) {
        return (RemoteBusiness) super.setBizId(bizId);
    }

    @Deprecated
    public RemoteBusiness addListener(MtopListener listener) {
        return (RemoteBusiness) super.addListener(listener);
    }

    @Deprecated
    public void setErrorNotifyNeedAfterCache(boolean bNeed) {
        super.setErrorNotifyAfterCache(bNeed);
    }

    @Deprecated
    public RemoteBusiness reqContext(Object requestContext) {
        return (RemoteBusiness) super.reqContext(requestContext);
    }
}
