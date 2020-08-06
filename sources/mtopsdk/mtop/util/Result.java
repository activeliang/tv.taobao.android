package mtopsdk.mtop.util;

import java.io.Serializable;

public class Result<T> implements Serializable {
    private static final long serialVersionUID = 8852253200756618077L;
    protected String errCode;
    protected String errInfo;
    protected String errType;
    protected T model;
    protected int statusCode;
    protected boolean success;

    public Result(T model2) {
        this.success = true;
        this.model = model2;
    }

    public Result() {
        this.success = true;
    }

    public Result(boolean success2, String errCode2, String errInfo2) {
        this(success2, (String) null, errCode2, errInfo2);
    }

    public Result(boolean success2, String errType2, String errCode2, String errInfo2) {
        this.success = true;
        this.success = success2;
        this.errType = errType2;
        this.errCode = errCode2;
        this.errInfo = errInfo2;
    }

    public T getModel() {
        return this.model;
    }

    public void setModel(T model2) {
        this.model = model2;
    }

    public String getErrCode() {
        return this.errCode;
    }

    public void setErrCode(String errCode2) {
        this.errCode = errCode2;
    }

    public String getErrInfo() {
        return this.errInfo;
    }

    public void setErrInfo(String errInfo2) {
        this.errInfo = errInfo2;
    }

    public String getErrType() {
        return this.errType;
    }

    public void setErrType(String errType2) {
        this.errType = errType2;
    }

    public boolean isSuccess() {
        return this.success;
    }

    public void setSuccess(boolean success2) {
        this.success = success2;
    }

    public int getStatusCode() {
        return this.statusCode;
    }

    public void setStatusCode(int statusCode2) {
        this.statusCode = statusCode2;
    }
}
