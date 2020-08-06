package com.ali.auth.third.core.model;

import java.util.ArrayList;

public class RpcRequest {
    public boolean NEED_ECODE = false;
    public boolean NEED_SESSION = false;
    public ArrayList<String> paramNames = new ArrayList<>();
    public ArrayList<Object> paramValues = new ArrayList<>();
    public String target;
    public String version;

    public void addParam(String name, Object value) {
        this.paramNames.add(name);
        this.paramValues.add(value);
    }

    public String toString() {
        return "RpcRequest [target=" + this.target + ", version=" + this.version + ", params=" + "]";
    }
}
