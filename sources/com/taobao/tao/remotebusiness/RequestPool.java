package com.taobao.tao.remotebusiness;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import com.taobao.tao.remotebusiness.handler.HandlerMgr;
import com.taobao.tao.remotebusiness.handler.HandlerParam;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;
import mtopsdk.common.util.StringUtils;
import mtopsdk.common.util.TBSdkLog;
import mtopsdk.framework.domain.MtopContext;
import mtopsdk.framework.util.FilterUtils;
import mtopsdk.mtop.common.MtopEvent;
import mtopsdk.mtop.common.MtopListener;
import mtopsdk.mtop.domain.MtopResponse;
import mtopsdk.mtop.global.SwitchConfig;
import mtopsdk.mtop.intf.Mtop;

public class RequestPool {
    private static final String DEFAULT_BIZINFO = "DEFAULT";
    public static final String TAG = "mtopsdk.RequestPool";
    private static Lock lock = new ReentrantLock();
    private static Map<String, List<MtopBusiness>> requestPool = new HashMap();

    public static void addToRequestPool(@NonNull Mtop mtop, @Nullable String bizInfo, MtopBusiness mtopBusiness) {
        lock.lock();
        try {
            String key = getRequestPoolKey(mtop, bizInfo);
            List<MtopBusiness> mtopBusinessList = requestPool.get(key);
            if (mtopBusinessList == null) {
                mtopBusinessList = new ArrayList<>();
            }
            mtopBusinessList.add(mtopBusiness);
            requestPool.put(key, mtopBusinessList);
            if (TBSdkLog.isLogEnable(TBSdkLog.LogEnable.InfoEnable)) {
                StringBuilder builder = new StringBuilder(64);
                builder.append(key).append(" [addToRequestPool] add mtopBusiness to RequestPool.");
                TBSdkLog.i(TAG, mtopBusiness.getSeqNo(), builder.toString());
            }
        } finally {
            lock.unlock();
        }
    }

    public static void removeFromRequestPool(@NonNull Mtop mtop, @Nullable String bizInfo, MtopBusiness mtopBusiness) {
        lock.lock();
        try {
            String key = getRequestPoolKey(mtop, bizInfo);
            List<MtopBusiness> mtopBusinessList = requestPool.get(key);
            if (mtopBusinessList != null) {
                mtopBusinessList.remove(mtopBusiness);
            }
            if (TBSdkLog.isLogEnable(TBSdkLog.LogEnable.InfoEnable)) {
                StringBuilder builder = new StringBuilder(64);
                builder.append(key).append(" [removeFromRequestPool] remove mtopBusiness from RequestPool.");
                TBSdkLog.i(TAG, mtopBusiness.getSeqNo(), builder.toString());
            }
        } catch (Exception e) {
        } finally {
            lock.unlock();
        }
    }

    public static void retryAllRequest(@NonNull Mtop mtop, @Nullable String bizInfo) {
        lock.lock();
        try {
            String key = getRequestPoolKey(mtop, bizInfo);
            List<MtopBusiness> mtopBusinessList = requestPool.remove(key);
            if (mtopBusinessList != null) {
                if (TBSdkLog.isLogEnable(TBSdkLog.LogEnable.InfoEnable)) {
                    StringBuilder builder = new StringBuilder(64);
                    builder.append(key).append(" [retryAllRequest] retry all request,current size=" + mtopBusinessList.size());
                    TBSdkLog.i(TAG, builder.toString());
                }
                for (MtopBusiness mtopBusiness : mtopBusinessList) {
                    mtopBusiness.retryRequest(bizInfo);
                }
                lock.unlock();
            }
        } finally {
            lock.unlock();
        }
    }

    public static void failAllRequest(@NonNull Mtop mtop, @Nullable String bizInfo, String errCode, String errMsg) {
        MtopResponse response;
        lock.lock();
        try {
            String key = getRequestPoolKey(mtop, bizInfo);
            List<MtopBusiness> mtopBusinessList = requestPool.remove(key);
            if (mtopBusinessList == null) {
                lock.unlock();
                return;
            }
            if (TBSdkLog.isLogEnable(TBSdkLog.LogEnable.InfoEnable)) {
                StringBuilder builder = new StringBuilder(64);
                builder.append(key).append(" [failAllRequest]fail all request,current size=" + mtopBusinessList.size());
                TBSdkLog.i(TAG, builder.toString());
            }
            for (MtopBusiness mtopBusiness : mtopBusinessList) {
                if (mtopBusiness.request != null) {
                    response = new MtopResponse(mtopBusiness.request.getApiName(), mtopBusiness.request.getVersion(), errCode, errMsg);
                } else {
                    response = new MtopResponse(errCode, errMsg);
                }
                if (SwitchConfig.getInstance().isGlobalErrorCodeMappingOpen()) {
                    MtopContext mtopContext = mtopBusiness.createMtopContext(mtopBusiness.listener);
                    mtopContext.mtopResponse = response;
                    FilterUtils.errorCodeMappingAfterFilter.doAfter(mtopContext);
                }
                HandlerParam msg = HandlerMgr.getHandlerMsg((MtopListener) null, (MtopEvent) null, mtopBusiness);
                msg.mtopResponse = response;
                HandlerMgr.instance().obtainMessage(3, msg).sendToTarget();
            }
            lock.unlock();
        } catch (Exception e) {
            TBSdkLog.e(TAG, "[failAllRequest] do ErrorCode Mapping error.apiKey=" + response.getFullKey(), (Throwable) e);
        } catch (Throwable th) {
            lock.unlock();
            throw th;
        }
    }

    private static String getRequestPoolKey(@NonNull Mtop mtopInstance, @Nullable String bizInfo) {
        String bizInfoExt;
        if (StringUtils.isBlank(bizInfo)) {
            bizInfoExt = "DEFAULT";
        } else {
            bizInfoExt = bizInfo;
        }
        return StringUtils.concatStr(mtopInstance.getInstanceId(), bizInfoExt);
    }
}
