package com.ali.user.open.mtop.rpc.impl;

import com.ali.user.open.core.device.DeviceInfo;
import com.ali.user.open.core.model.RpcRequest;
import com.ali.user.open.core.model.RpcRequestCallbackWithCode;
import com.ali.user.open.core.service.RpcService;
import com.ali.user.open.mtop.rpc.MTOPWrapper;

public class MtopRpcServiceImpl implements RpcService {
    public <T> void remoteBusiness(RpcRequest request, Class<T> resultType, RpcRequestCallbackWithCode rpcRequestCallback) {
        MTOPWrapper.getInstance().remoteBusiness(request, resultType, rpcRequestCallback);
    }

    public void registerSessionInfo(String instanceTag, String sid, String userId) {
    }

    public String getDeviceId() {
        return DeviceInfo.deviceId;
    }

    public void logout(String instanceTag) {
    }
}
