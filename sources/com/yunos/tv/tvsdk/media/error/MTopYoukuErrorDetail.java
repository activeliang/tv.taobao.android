package com.yunos.tv.tvsdk.media.error;

class MTopYoukuErrorDetail extends ErrorDetail {
    private static final long serialVersionUID = 5055709047264703248L;

    public MTopYoukuErrorDetail() {
    }

    public MTopYoukuErrorDetail(String message) {
        super(message);
    }

    public MTopYoukuErrorDetail(Throwable cause) {
        super(cause);
    }

    public MTopYoukuErrorDetail(int code) {
        super(code);
    }

    public MTopYoukuErrorDetail(int code, String errorMessage) {
        super(code, errorMessage);
    }

    public MTopYoukuErrorDetail(String errorMessage, Throwable throwable) {
        super(errorMessage, throwable);
    }

    public MTopYoukuErrorDetail(int code, String errorMessage, Throwable throwable) {
        super(code, errorMessage, throwable);
    }

    public String errorPath() {
        return "/com/yunos/tv/media/error/MTopYoukuErrorDetail";
    }

    public String buildErrormsg() {
        return getErrorMessage();
    }
}
