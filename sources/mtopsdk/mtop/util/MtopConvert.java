package mtopsdk.mtop.util;

import com.alibaba.fastjson.JSON;
import mtopsdk.common.util.TBSdkLog;
import mtopsdk.mtop.domain.BaseOutDo;
import mtopsdk.mtop.domain.IMTOPDataObject;
import mtopsdk.mtop.domain.MtopRequest;
import mtopsdk.mtop.domain.MtopResponse;

public class MtopConvert {
    private static final String TAG = "mtopsdk.MtopConvert";

    @Deprecated
    public static BaseOutDo jsonToOutputDO(byte[] jsonData, Class<?> outputClass) {
        if (outputClass == null || jsonData == null || jsonData.length == 0) {
            TBSdkLog.e(TAG, "[jsonToOutputDO]outClass is null or jsonData is blank");
            return null;
        }
        try {
            return (BaseOutDo) JSON.parseObject(new String(jsonData, "UTF-8"), outputClass);
        } catch (Throwable e) {
            TBSdkLog.e(TAG, "[jsonToOutputDO]invoke JSON.parseObject error ---Class=" + outputClass.getName(), e);
            return null;
        }
    }

    @Deprecated
    public static BaseOutDo mtopResponseToOutputDO(MtopResponse mtopResponse, Class<?> outputClass) {
        if (outputClass != null && mtopResponse != null) {
            return jsonToOutputDO(mtopResponse.getBytedata(), outputClass);
        }
        TBSdkLog.e(TAG, "outClass is null or response is null");
        return null;
    }

    public static <T> T convertJsonToOutputDO(byte[] jsonData, Class<T> outputClass) {
        if (outputClass == null || jsonData == null || jsonData.length == 0) {
            TBSdkLog.e(TAG, "[jsonToOutputDO]outputClass is null or jsonData is blank");
            return null;
        }
        try {
            return JSON.parseObject(new String(jsonData, "UTF-8"), outputClass);
        } catch (Throwable e) {
            TBSdkLog.e(TAG, "[jsonToOutputDO]invoke JSON.parseObject error ---Class=" + outputClass.getName(), e);
            return null;
        }
    }

    public static <T> T convertMtopResponseToOutputDO(MtopResponse mtopResponse, Class<T> outputClass) {
        if (outputClass != null && mtopResponse != null) {
            return convertJsonToOutputDO(mtopResponse.getBytedata(), outputClass);
        }
        TBSdkLog.e(TAG, "outputClass is null or mtopResponse is null");
        return null;
    }

    public static MtopRequest inputDoToMtopRequest(IMTOPDataObject inputDO) {
        if (inputDO == null) {
            return null;
        }
        return ReflectUtil.convertToMtopRequest(inputDO);
    }

    public static MtopRequest inputDoToMtopRequest(Object input) {
        if (input == null) {
            return null;
        }
        return ReflectUtil.convertToMtopRequest(input);
    }
}
