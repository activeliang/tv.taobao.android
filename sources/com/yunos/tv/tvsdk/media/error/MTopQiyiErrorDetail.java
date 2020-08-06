package com.yunos.tv.tvsdk.media.error;

class MTopQiyiErrorDetail extends ErrorDetail {
    private static final long serialVersionUID = 5055709047264703248L;

    public MTopQiyiErrorDetail() {
    }

    public MTopQiyiErrorDetail(String message) {
        super(message);
    }

    public MTopQiyiErrorDetail(Throwable cause) {
        super(cause);
    }

    public MTopQiyiErrorDetail(int code) {
        super(code);
    }

    public MTopQiyiErrorDetail(int code, String errorMessage) {
        super(code, errorMessage);
    }

    public MTopQiyiErrorDetail(String errorMessage, Throwable throwable) {
        super(errorMessage, throwable);
    }

    public MTopQiyiErrorDetail(int code, String errorMessage, Throwable throwable) {
        super(code, errorMessage, throwable);
    }

    public String errorPath() {
        return "/com/yunos/tv/media/error/MTopQiyiErrorDetail";
    }

    public String buildErrormsg() {
        return getErrorMessage();
    }
}
