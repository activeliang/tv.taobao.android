package com.alibaba.sdk.android.oss.internal;

import com.alibaba.sdk.android.oss.common.OSSHeaders;
import com.alibaba.sdk.android.oss.common.OSSLog;
import com.alibaba.sdk.android.oss.common.utils.CaseInsensitiveHashMap;
import com.alibaba.sdk.android.oss.model.OSSResult;
import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.ParameterizedType;
import java.math.BigInteger;
import java.util.zip.CheckedInputStream;
import okhttp3.Headers;
import okhttp3.Response;

public abstract class AbstractResponseParser<T extends OSSResult> implements ResponseParser {
    /* access modifiers changed from: package-private */
    public abstract T parseData(ResponseMessage responseMessage, T t) throws Exception;

    public static void safeCloseResponse(ResponseMessage response) {
        try {
            response.close();
        } catch (Exception e) {
        }
    }

    public boolean needCloseResponse() {
        return true;
    }

    public T parse(ResponseMessage response) throws IOException {
        try {
            T result = (OSSResult) ((Class) ((ParameterizedType) getClass().getGenericSuperclass()).getActualTypeArguments()[0]).newInstance();
            if (result != null) {
                result.setRequestId((String) response.getHeaders().get(OSSHeaders.OSS_HEADER_REQUEST_ID));
                result.setStatusCode(response.getStatusCode());
                result.setResponseHeader(parseResponseHeader(response.getResponse()));
                setCRC(result, response);
                result = parseData(response, result);
            }
            if (needCloseResponse()) {
                safeCloseResponse(response);
            }
            return result;
        } catch (Exception e) {
            IOException ioException = new IOException(e.getMessage(), e);
            e.printStackTrace();
            OSSLog.logThrowable2Local(e);
            throw ioException;
        } catch (Throwable th) {
            if (needCloseResponse()) {
                safeCloseResponse(response);
            }
            throw th;
        }
    }

    private CaseInsensitiveHashMap<String, String> parseResponseHeader(Response response) {
        CaseInsensitiveHashMap<String, String> result = new CaseInsensitiveHashMap<>();
        Headers headers = response.headers();
        for (int i = 0; i < headers.size(); i++) {
            result.put(headers.name(i), headers.value(i));
        }
        return result;
    }

    public <Result extends OSSResult> void setCRC(Result result, ResponseMessage response) {
        InputStream inputStream = response.getRequest().getContent();
        if (inputStream != null && (inputStream instanceof CheckedInputStream)) {
            result.setClientCRC(Long.valueOf(((CheckedInputStream) inputStream).getChecksum().getValue()));
        }
        String strSrvCrc = (String) response.getHeaders().get(OSSHeaders.OSS_HASH_CRC64_ECMA);
        if (strSrvCrc != null) {
            result.setServerCRC(Long.valueOf(new BigInteger(strSrvCrc).longValue()));
        }
    }
}
