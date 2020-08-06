package com.yunos.tv.tvsdk.media.error;

import com.yunos.tv.tvsdk.media.data.MediaType;

public interface IMediaError {
    public static final int MEDIA_ACCOUNT_ERROR = 1048577;
    public static final int MEDIA_AUTH_ERROR = 1048578;
    public static final int MEDIA_DATA_ERROR = 1048579;
    public static final int MEDIA_NATIVE_PLAYER_ERROR = 1048580;
    public static final int MEDIA_SPECIAL_PLAYER_EVENT = 1048582;
    public static final int MEDIA_SYSTEM_PLAYER_ERROR = 1048581;

    int getCode();

    ErrorDetail getErrorDetail();

    String getErrorMsg();

    ErrorType getErrorType();

    MediaType getMediaType();
}
