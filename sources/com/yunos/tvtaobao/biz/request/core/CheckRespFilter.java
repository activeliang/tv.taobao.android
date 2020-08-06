package com.yunos.tvtaobao.biz.request.core;

import android.support.annotation.NonNull;
import android.text.TextUtils;
import com.yunos.tv.blitz.request.blitz.BlitzMtopRequest;
import com.yunos.tv.blitz.request.core.ServiceCode;
import com.yunos.tv.blitz.request.core.ServiceResponse;
import com.yunos.tvtaobao.payment.utils.ErrorReport;
import com.zhiping.dev.android.logger.ZpLogger;
import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.List;
import mtopsdk.framework.domain.FilterResult;
import mtopsdk.framework.domain.MtopContext;
import mtopsdk.framework.filter.IBeforeFilter;
import mtopsdk.framework.manager.FilterManager;
import mtopsdk.framework.manager.impl.AbstractFilterManager;
import mtopsdk.mtop.common.MtopCallback;
import mtopsdk.mtop.common.MtopFinishEvent;
import mtopsdk.mtop.common.MtopListener;
import mtopsdk.mtop.domain.MtopRequest;
import mtopsdk.mtop.domain.MtopResponse;

public class CheckRespFilter implements IBeforeFilter, MtopCallback.MtopFinishListener {
    private static CheckRespFilter instance = new CheckRespFilter();
    private HashMap<String, RequestPeek> listenerMap = new HashMap<>();

    private static class RequestPeek {
        MtopListener listener;
        MtopRequest request;

        RequestPeek(MtopRequest request2, MtopListener listener2) {
            this.request = request2;
            this.listener = listener2;
        }
    }

    public void inject(FilterManager filterManager) {
        List list;
        if (!(filterManager instanceof AbstractFilterManager)) {
            ZpLogger.e("CheckRespFilter", "Failed to add filter, filterManager is not instance of class AbstractFilterManager");
            return;
        }
        try {
            Field field = AbstractFilterManager.class.getDeclaredField("beforeFilters");
            field.setAccessible(true);
            Object object = field.get(filterManager);
            if ((object instanceof List) && (list = (List) object) != null && list.size() > 0) {
                synchronized (list) {
                    if (list.get(0) != this) {
                        list.add(0, this);
                    }
                }
            }
        } catch (NoSuchFieldException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e2) {
            e2.printStackTrace();
        }
    }

    public static CheckRespFilter getInstance() {
        return instance;
    }

    private CheckRespFilter() {
    }

    public String doBefore(MtopContext mtopContext) {
        if (!(mtopContext.mtopRequest instanceof BlitzMtopRequest)) {
            return FilterResult.CONTINUE;
        }
        this.listenerMap.put(mtopContext.seqNo, new RequestPeek(mtopContext.mtopRequest, mtopContext.mtopListener));
        mtopContext.mtopListener = this;
        return FilterResult.CONTINUE;
    }

    @NonNull
    public String getName() {
        return "tvtaobao.checkRespFilter";
    }

    public void onFinished(MtopFinishEvent mtopFinishEvent, Object o) {
        RequestPeek peek = this.listenerMap.remove(mtopFinishEvent.seqNo);
        if (peek == null) {
            ZpLogger.d("ErrorReport", "listner null");
        } else if (peek.listener instanceof MtopCallback.MtopFinishListener) {
            processFinishEvent(peek.request, mtopFinishEvent, o);
            ((MtopCallback.MtopFinishListener) peek.listener).onFinished(mtopFinishEvent, o);
        }
    }

    private void processFinishEvent(MtopRequest request, MtopFinishEvent event, Object o) {
        ServiceCode serviceCode;
        MtopResponse mtopResponse = event.mtopResponse;
        if (request instanceof BlitzMtopRequest) {
            ServiceResponse serviceResponse = null;
            if (mtopResponse != null) {
                try {
                    if (mtopResponse.getBytedata() != null) {
                        serviceResponse = ((BlitzMtopRequest) request).resolveResponse(new String(mtopResponse.getBytedata()));
                    } else {
                        String retCode = mtopResponse.getRetCode();
                        if (!TextUtils.isEmpty(retCode) && (serviceCode = ServiceCode.valueOf(retCode)) != null) {
                            ServiceResponse serviceResponse2 = new ServiceResponse();
                            try {
                                serviceResponse2.update(serviceCode.getCode(), retCode, serviceCode.getMsg());
                                serviceResponse = serviceResponse2;
                            } catch (Exception e) {
                                var8 = e;
                                ServiceResponse serviceResponse3 = serviceResponse2;
                                ErrorReport.getInstance().uploadH5MtopError(request.getApiName(), request.getVersion(), request.dataParams, ErrorReport.ERRORTYPE_PARSEERROR, "" + mtopResponse.getResponseCode(), mtopResponse.getRetCode(), mtopResponse.getRetMsg());
                                var8.printStackTrace();
                                return;
                            }
                        }
                    }
                } catch (Exception e2) {
                    var8 = e2;
                    ErrorReport.getInstance().uploadH5MtopError(request.getApiName(), request.getVersion(), request.dataParams, ErrorReport.ERRORTYPE_PARSEERROR, "" + mtopResponse.getResponseCode(), mtopResponse.getRetCode(), mtopResponse.getRetMsg());
                    var8.printStackTrace();
                    return;
                }
            } else {
                serviceResponse = new ServiceResponse();
                serviceResponse.update(ServiceCode.HTTP_ERROR);
            }
            if (serviceResponse == null) {
                serviceResponse = new ServiceResponse();
                serviceResponse.update(ServiceCode.API_ERROR);
            }
            if (serviceResponse.isSucess()) {
                return;
            }
            if (mtopResponse.getResponseCode() == 419) {
                ErrorReport.getInstance().uploadH5MtopError(request.getApiName(), request.getVersion(), request.dataParams, ErrorReport.ERRORTYPE_FILTERERROR, "" + mtopResponse.getResponseCode(), mtopResponse.getRetCode(), mtopResponse.getRetMsg());
            } else if (!mtopResponse.isApiSuccess()) {
                ErrorReport.getInstance().uploadH5MtopError(request.getApiName(), request.getVersion(), request.dataParams, ErrorReport.ERRORTYPE_APIERROR, "" + mtopResponse.getResponseCode(), mtopResponse.getRetCode(), mtopResponse.getRetMsg());
            }
        }
    }
}
