package com.taobao.alimama.net.core.task;

import com.taobao.alimama.net.core.state.NetRequestRetryPolicy;
import java.util.Arrays;
import java.util.List;
import mtopsdk.mtop.domain.IMTOPDataObject;
import mtopsdk.mtop.util.ErrorConstant;

public class MtopRequestTask extends AbsNetRequestTask {
    private static final String SUCCESS_CODE = "SUCCESS";
    private static final List SYSTEM_ERROR_CODES = Arrays.asList(new String[]{ErrorConstant.ERRCODE_NO_NETWORK, ErrorConstant.ERRCODE_NETWORK_ERROR});
    private IMTOPDataObject requestObject;
    private Class<?> responseClass;

    public MtopRequestTask(String str, NetRequestRetryPolicy netRequestRetryPolicy, IMTOPDataObject iMTOPDataObject, Class<?> cls) {
        super(str, netRequestRetryPolicy);
        this.requestObject = iMTOPDataObject;
        this.responseClass = cls;
    }

    public IMTOPDataObject getRequestObject() {
        return this.requestObject;
    }

    public Class<?> getResponseClass() {
        return this.responseClass;
    }

    public boolean isRequestSuccess(String str) {
        return "SUCCESS".equals(str);
    }

    public boolean isRequestSystemError(String str) {
        return SYSTEM_ERROR_CODES.contains(str);
    }
}
