package com.yunos.tv.tvsdk.media.error;

class MTopSohuErrorDetail extends ErrorDetail {
    private static final long serialVersionUID = 5055709047264703248L;

    public MTopSohuErrorDetail() {
    }

    public MTopSohuErrorDetail(String message) {
        super(message);
    }

    public MTopSohuErrorDetail(Throwable cause) {
        super(cause);
    }

    public MTopSohuErrorDetail(int code) {
        super(code);
    }

    public MTopSohuErrorDetail(int code, String errorMessage) {
        super(code, errorMessage);
    }

    public MTopSohuErrorDetail(String errorMessage, Throwable throwable) {
        super(errorMessage, throwable);
    }

    public MTopSohuErrorDetail(int code, String errorMessage, Throwable throwable) {
        super(code, errorMessage, throwable);
    }

    public String errorPath() {
        return "/com/yunos/tv/media/error/MTopSohuErrorDetail";
    }

    public String buildErrormsg() {
        return getErrorMessage();
    }
}
