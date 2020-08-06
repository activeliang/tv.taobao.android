package com.taobao.tao.remotebusiness.listener;

import com.taobao.tao.remotebusiness.MtopBusiness;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import mtopsdk.mtop.common.MtopListener;

class DynamicProxyHandler implements InvocationHandler {
    private MtopCacheListenerImpl cacheListener;
    private MtopFinishListenerImpl finishListener;
    private MtopListener listener;
    private MtopBusiness mtopBusiness;
    private MtopProgressListenerImpl progressListener;

    public DynamicProxyHandler(MtopBusiness mtopBusiness2, MtopListener listener2) {
        this.finishListener = new MtopFinishListenerImpl(mtopBusiness2, listener2);
        this.mtopBusiness = mtopBusiness2;
        this.listener = listener2;
    }

    private MtopProgressListenerImpl getProgressListener() {
        if (this.progressListener == null) {
            this.progressListener = new MtopProgressListenerImpl(this.mtopBusiness, this.listener);
        }
        return this.progressListener;
    }

    private MtopCacheListenerImpl getCacheListener() {
        if (this.cacheListener == null) {
            this.cacheListener = new MtopCacheListenerImpl(this.mtopBusiness, this.listener);
        }
        return this.cacheListener;
    }

    public Object invoke(Object o, Method method, Object[] objects) throws Throwable {
        if (method.getName().equals("onFinished")) {
            return method.invoke(this.finishListener, objects);
        }
        if (method.getName().equals("onDataReceived") || method.getName().equals("onHeader")) {
            return method.invoke(getProgressListener(), objects);
        }
        if (method.getName().equals("onCached")) {
            return method.invoke(getCacheListener(), objects);
        }
        return null;
    }
}
