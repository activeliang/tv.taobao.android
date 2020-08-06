package com.yunos.tv.tvsdk.media.error;

class MTopTaoTvErrorDetail extends ErrorDetail {
    private static final long serialVersionUID = 5055709047264703248L;

    public MTopTaoTvErrorDetail() {
    }

    public MTopTaoTvErrorDetail(String message) {
        super(message);
    }

    public MTopTaoTvErrorDetail(Throwable cause) {
        super(cause);
    }

    public MTopTaoTvErrorDetail(int code) {
        super(code);
    }

    public MTopTaoTvErrorDetail(int code, String errorMessage) {
        super(code, errorMessage);
    }

    public MTopTaoTvErrorDetail(String errorMessage, Throwable throwable) {
        super(errorMessage, throwable);
    }

    public MTopTaoTvErrorDetail(int code, String errorMessage, Throwable throwable) {
        super(code, errorMessage, throwable);
    }

    public String errorPath() {
        return "/com/yunos/tv/media/error/MTopTaoTvErrorDetail";
    }

    public String buildErrormsg() {
        return getErrorMessage();
    }
}
