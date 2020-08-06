package com.taobao.tao.remotebusiness.listener;

import com.taobao.tao.remotebusiness.IRemoteCacheListener;
import com.taobao.tao.remotebusiness.IRemoteProcessListener;
import com.taobao.tao.remotebusiness.MtopBusiness;
import java.lang.reflect.Proxy;
import java.util.ArrayList;
import java.util.List;
import mtopsdk.mtop.common.MtopCallback;
import mtopsdk.mtop.common.MtopListener;

public class MtopListenerProxyFactory {
    public static MtopListener getMtopListenerProxy(MtopBusiness mtopBusiness, MtopListener listener) {
        List<Class> classList = new ArrayList<>();
        classList.add(MtopCallback.MtopFinishListener.class);
        if (listener instanceof IRemoteProcessListener) {
            classList.add(MtopCallback.MtopProgressListener.class);
            classList.add(MtopCallback.MtopHeaderListener.class);
        }
        if ((listener instanceof IRemoteCacheListener) || mtopBusiness.mtopProp.useCache) {
            classList.add(MtopCallback.MtopCacheListener.class);
        }
        return (MtopListener) Proxy.newProxyInstance(MtopListener.class.getClassLoader(), (Class[]) classList.toArray(new Class[classList.size()]), new DynamicProxyHandler(mtopBusiness, listener));
    }
}
