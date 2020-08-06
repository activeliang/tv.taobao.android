package com.ali.auth.third.mtop.rpc.impl;

import com.ali.auth.third.core.MemberSDK;
import com.ali.auth.third.core.config.ConfigManager;
import com.ali.auth.third.core.config.Environment;
import com.ali.auth.third.core.context.KernelContext;
import com.ali.auth.third.core.device.DeviceInfo;
import com.ali.auth.third.core.model.RpcRequest;
import com.ali.auth.third.core.model.RpcRequestCallbackWithCode;
import com.ali.auth.third.core.model.RpcResponse;
import com.ali.auth.third.core.service.RpcService;
import com.ali.auth.third.core.util.CommonUtils;
import com.ali.auth.third.mtop.rpc.MTOPWrapper;
import com.ali.auth.third.mtop.rpc.MtopRemoteLoginImpl;
import com.taobao.tao.remotebusiness.RemoteBusiness;
import com.taobao.tao.remotebusiness.login.RemoteLogin;
import mtopsdk.common.util.TBSdkLog;
import mtopsdk.mtop.domain.EnvModeEnum;
import mtopsdk.mtop.intf.Mtop;
import mtopsdk.mtop.intf.MtopSetting;

public class MtopRpcServiceImpl implements RpcService {
    public MtopRpcServiceImpl() {
        if (ConfigManager.DEBUG) {
            TBSdkLog.setTLogEnabled(false);
            TBSdkLog.setPrintLog(true);
        }
        MtopSetting.setAppKeyIndex(ConfigManager.getAppKeyIndex(), ConfigManager.getDailyAppKeyIndex());
        MtopSetting.setAppVersion(CommonUtils.getAppVersion());
        if (KernelContext.getEnvironment() == Environment.TEST) {
            Mtop.instance(KernelContext.context).switchEnvMode(EnvModeEnum.TEST);
            MtopSetting.setMtopDomain("", "", "acs.waptest.taobao.com");
        } else if (KernelContext.getEnvironment() == Environment.PRE) {
            Mtop.instance(KernelContext.context).switchEnvMode(EnvModeEnum.PREPARE);
            MtopSetting.setMtopDomain("", "acs.wapa.taobao.com", "");
        } else {
            Mtop.instance(KernelContext.context).switchEnvMode(EnvModeEnum.ONLINE);
        }
        RemoteLogin.setLoginImpl(new MtopRemoteLoginImpl());
    }

    public String invoke(RpcRequest rpcRequest) {
        return MTOPWrapper.getInstance().post(rpcRequest);
    }

    public <T> RpcResponse<T> invoke(RpcRequest rpcRequest, Class<T> resultType) {
        return MTOPWrapper.getInstance().post(rpcRequest, resultType);
    }

    public <T> void remoteBusiness(RpcRequest request, Class<T> resultType, RpcRequestCallbackWithCode rpcRequestCallback) {
        RemoteBusiness.init(KernelContext.getApplicationContext(), MemberSDK.ttid);
        MTOPWrapper.getInstance().remoteBusiness(request, resultType, rpcRequestCallback);
    }

    public void registerSessionInfo(String sid, String userId) {
        Mtop.instance(KernelContext.context).registerSessionInfo(sid, userId);
    }

    public String getDeviceId() {
        return DeviceInfo.deviceId;
    }

    public void logout() {
        Mtop.instance(KernelContext.context).logout();
    }
}
