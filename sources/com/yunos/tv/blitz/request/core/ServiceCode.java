package com.yunos.tv.blitz.request.core;

import com.alibaba.wireless.security.SecExceptionCode;
import com.yunos.tvtaobao.biz.request.elem.ElemResultCode;

public enum ServiceCode {
    NET_WORK_ERROR(1, "网络连接错误"),
    HTTP_ERROR(100, "网络连接错误或读取数据超时"),
    DATA_PARSE_ERROR(101, "数据解析错误"),
    SERVICE_OK(200, "成功"),
    API_NOT_LOGIN(102, "您尚未登录淘宝账号，请先登录。"),
    API_SID_INVALID(104, "您登录的账号已过期,请重新登录或者换账号登录。"),
    API_ERROR(106, "API其他错误"),
    API_ERRCODE_AUTH_REJECT(107, "非法签名"),
    USER_LOGIN_OTHER_FAIL(Integer.valueOf(ElemResultCode.RETURN_REQUEST_CODE), "登陆失败，请重新尝试登录。"),
    ANDROID_SYS_NO_NETWORK(1, "网络连接错误"),
    ANDROID_SYS_NETWORK_ERROR(1, "网络连接错误"),
    FAIL_SYS_SESSION_EXPIRED(104, "您登录的账号已过期,请重新登录或者换账号登录。"),
    FAIL_SYS_ILEGEL_SIGN(104, "您登录的账号已过期,请重新登录或者换账号登录。"),
    ANDROID_SYS_JSONDATA_BLANK(500, "返回数据为空"),
    ANDROID_SYS_JSONDATA_PARSE_ERROR(Integer.valueOf(SecExceptionCode.SEC_ERROR_DYN_STORE_INVALID_PARAM), "解析数据错误"),
    ANDROID_SYS_MTOPREQUEST_INVALID_ERROR(Integer.valueOf(SecExceptionCode.SEC_ERROR_DYN_STORE_NO_MEMORY), "非法请求"),
    ANDROID_SYS_MTOPPROXYBASE_INIT_ERROR(Integer.valueOf(SecExceptionCode.SEC_ERROR_DYN_STORE_GET_SYS_PROPERTIES_FAILED), "初始化失败"),
    ANDROID_SYS_GENERATE_MTOP_SIGN_ERROR(Integer.valueOf(SecExceptionCode.SEC_ERROR_DYN_STORE_GET_DATA_FILE_KEY_FAILED), "签名失败"),
    ANDROID_SYS_API_FLOW_LIMIT_LOCKED(Integer.valueOf(SecExceptionCode.SEC_ERROR_DYN_STORE_GET_ENCRYPT_KEY_FAILED), "哎哟喂,被挤爆啦,请稍后重试"),
    ANDROID_SYS_API_41X_ANTI_ATTACK(506, "哎哟喂,被挤爆啦,请稍后重试"),
    ANDROID_SYS_MTOP_APICALL_ASYNC_TIMEOUT(507, "请求超时");
    
    private int code;
    private String msg;

    private ServiceCode(Integer code2, String msg2) {
        this.code = code2.intValue();
        this.msg = msg2;
    }

    public int getCode() {
        return this.code;
    }

    public String getMsg() {
        return this.msg;
    }
}
