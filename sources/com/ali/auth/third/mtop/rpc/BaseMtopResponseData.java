package com.ali.auth.third.mtop.rpc;

import mtopsdk.mtop.domain.IMTOPDataObject;

public class BaseMtopResponseData<T> implements IMTOPDataObject {
    public String actionType;
    public int code;
    public String codeGroup;
    public String message;
    public String msgCode;
    public String msgInfo;
    public T returnValue;
}
