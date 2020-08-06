package com.yunos.tv.tvsdk.media.error;

import com.yunos.tv.tvsdk.media.data.MediaType;

public class MTopInfoError implements IMediaError {
    protected ErrorDetail mErrorDetail;
    protected ErrorType mErrorType;
    protected MediaType mMediaType;

    public MediaType getMediaType() {
        return this.mMediaType;
    }

    public void setMediaType(MediaType type) {
        this.mMediaType = type;
    }

    public ErrorType getErrorType() {
        return this.mErrorType;
    }

    public void setErrorType(ErrorType errorType) {
        this.mErrorType = errorType;
    }

    public ErrorDetail getErrorDetail() {
        return this.mErrorDetail;
    }

    public void setErrorDetail(ErrorDetail detail) {
        this.mErrorDetail = detail;
    }

    public int getCode() {
        if (this.mErrorDetail != null) {
            return this.mErrorDetail.getCode();
        }
        return 0;
    }

    public String getErrorMsg() {
        return this.mErrorDetail != null ? this.mErrorDetail.buildErrormsg() : "";
    }
}
