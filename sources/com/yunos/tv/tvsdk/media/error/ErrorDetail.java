package com.yunos.tv.tvsdk.media.error;

import com.yunos.tv.tvsdk.media.data.MediaType;

public abstract class ErrorDetail extends Exception implements IMediaErrorPath, IMediaErrorMsg {
    protected static final String NULL = "";
    private static final long serialVersionUID = 896028334449209295L;
    protected int code;
    protected String errorMessage = "";

    public static ErrorDetail createErrorDetail(MediaType type, int code2) {
        switch (type) {
            case FROM_TAOTV:
                return new MTopErrorDetail(code2);
            case FROM_YOUKU:
                return new MTopYoukuErrorDetail(code2);
            case FROM_SOHU:
                return new MTopSohuErrorDetail(code2);
            case FROM_QIYI:
                return new MTopQiyiErrorDetail(code2);
            default:
                return new MTopErrorDetail(code2);
        }
    }

    public static ErrorDetail createErrorDetail(MediaType type) {
        switch (type) {
            case FROM_TAOTV:
                return new MTopTaoTvErrorDetail();
            case FROM_YOUKU:
                return new MTopYoukuErrorDetail();
            case FROM_SOHU:
                return new MTopSohuErrorDetail();
            case FROM_QIYI:
                return new MTopQiyiErrorDetail();
            default:
                return new MTopErrorDetail();
        }
    }

    public static ErrorDetail createErrorDetail(MediaType type, int code2, String errorMessage2) {
        switch (type) {
            case FROM_TAOTV:
                return new MTopErrorDetail(code2, errorMessage2);
            case FROM_YOUKU:
                return new MTopYoukuErrorDetail(code2, errorMessage2);
            case FROM_SOHU:
                return new MTopSohuErrorDetail(code2, errorMessage2);
            case FROM_QIYI:
                return new MTopQiyiErrorDetail(code2, errorMessage2);
            default:
                return new MTopErrorDetail(code2, errorMessage2);
        }
    }

    protected ErrorDetail() {
    }

    protected ErrorDetail(String message) {
        super(message);
    }

    protected ErrorDetail(Throwable cause) {
        super(cause);
    }

    protected ErrorDetail(int code2) {
        this.code = code2;
    }

    protected ErrorDetail(int code2, String errorMessage2) {
        this.code = code2;
        this.errorMessage = errorMessage2;
    }

    protected ErrorDetail(String errorMessage2, Throwable throwable) {
        super(throwable);
        this.errorMessage = errorMessage2;
    }

    protected ErrorDetail(int code2, String errorMessage2, Throwable throwable) {
        super(throwable);
        this.code = code2;
        this.errorMessage = errorMessage2;
    }

    public int getCode() {
        return this.code;
    }

    public void setCode(int code2) {
        this.code = code2;
    }

    public String getErrorMessage() {
        if (this.errorMessage != null) {
            return this.errorMessage;
        }
        return super.getMessage();
    }

    public void setErrorMessage(String errorMessage2) {
        this.errorMessage = errorMessage2;
    }
}
