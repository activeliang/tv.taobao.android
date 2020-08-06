package com.yunos.tv.tvsdk.media.error;

import org.json.JSONObject;

class MTopErrorDetail extends ErrorDetail {
    private static final long serialVersionUID = 5055709047264703248L;

    public MTopErrorDetail() {
    }

    public MTopErrorDetail(String message) {
        super(message);
    }

    public MTopErrorDetail(Throwable cause) {
        super(cause);
    }

    public MTopErrorDetail(int code) {
        super(code);
    }

    public MTopErrorDetail(int code, String errorMessage) {
        super(code, errorMessage);
    }

    public MTopErrorDetail(String errorMessage, Throwable throwable) {
        super(errorMessage, throwable);
    }

    public MTopErrorDetail(int code, String errorMessage, Throwable throwable) {
        super(code, errorMessage, throwable);
    }

    public String errorPath() {
        return "/com/yunos/tv/media/error/MTopErrorDetail";
    }

    public String buildErrormsg() {
        try {
            JSONObject object = new JSONObject();
            object.put("error", getErrorMessage());
            return object.toString();
        } catch (Exception e) {
            e.printStackTrace();
            return "";
        }
    }
}
