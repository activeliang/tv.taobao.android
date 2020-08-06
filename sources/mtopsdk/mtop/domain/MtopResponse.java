package mtopsdk.mtop.domain;

import java.io.Serializable;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import mtopsdk.common.util.StringUtils;
import mtopsdk.common.util.TBSdkLog;
import mtopsdk.mtop.util.ErrorConstant;
import mtopsdk.mtop.util.MtopStatistics;
import org.json.JSONArray;
import org.json.JSONObject;

public class MtopResponse implements Serializable, IMTOPDataObject {
    private static final String SHARP = "::";
    private static final String TAG = "mtopsdk.MtopResponse";
    private static final long serialVersionUID = 1566423746968673499L;
    private String api;
    private volatile boolean bParsed = false;
    private byte[] bytedata;
    @Deprecated
    private byte[] data;
    private JSONObject dataJsonObject;
    private Map<String, List<String>> headerFields;
    public String mappingCode;
    public String mappingCodeSuffix;
    private MtopStatistics mtopStat;
    private int responseCode;
    private ResponseSource responseSource = ResponseSource.NETWORK_REQUEST;
    @Deprecated
    private String[] ret;
    private String retCode;
    private String retMsg;
    private String v;

    public enum ResponseSource {
        FRESH_CACHE,
        EXPIRED_CACHE,
        NETWORK_REQUEST
    }

    public MtopResponse() {
    }

    public MtopResponse(String retCode2, String retMsg2) {
        this.retCode = retCode2;
        this.retMsg = retMsg2;
    }

    public MtopResponse(String api2, String v2, String retCode2, String retMsg2) {
        this.api = api2;
        this.v = v2;
        this.retCode = retCode2;
        this.retMsg = retMsg2;
    }

    public String getRetCode() {
        return this.retCode;
    }

    public void setRetCode(String retCode2) {
        this.retCode = retCode2;
    }

    public String getMappingCode() {
        return this.mappingCode;
    }

    public String getRetMsg() {
        if (this.retMsg == null && !this.bParsed) {
            parseJsonByte();
        }
        return this.retMsg;
    }

    public void setRetMsg(String retMsg2) {
        this.retMsg = retMsg2;
    }

    public String getApi() {
        if (this.api == null && !this.bParsed) {
            parseJsonByte();
        }
        return this.api;
    }

    public void setApi(String api2) {
        this.api = api2;
    }

    public String getV() {
        if (this.v == null && !this.bParsed) {
            parseJsonByte();
        }
        return this.v;
    }

    public void setV(String v2) {
        this.v = v2;
    }

    @Deprecated
    public String[] getRet() {
        if (this.ret == null && !this.bParsed) {
            parseJsonByte();
        }
        return this.ret;
    }

    @Deprecated
    public void setRet(String[] ret2) {
        this.ret = ret2;
    }

    @Deprecated
    public byte[] getData() {
        return this.data;
    }

    @Deprecated
    public void setData(byte[] data2) {
        this.data = data2;
    }

    public JSONObject getDataJsonObject() {
        if (this.dataJsonObject == null && !this.bParsed) {
            parseJsonByte();
        }
        return this.dataJsonObject;
    }

    public void setDataJsonObject(JSONObject dataJsonObject2) {
        this.dataJsonObject = dataJsonObject2;
    }

    public byte[] getBytedata() {
        return this.bytedata;
    }

    public void setBytedata(byte[] bytedata2) {
        this.bytedata = bytedata2;
    }

    public Map<String, List<String>> getHeaderFields() {
        return this.headerFields;
    }

    public void setHeaderFields(Map<String, List<String>> headerFields2) {
        this.headerFields = headerFields2;
    }

    public int getResponseCode() {
        return this.responseCode;
    }

    public void setResponseCode(int responseCode2) {
        this.responseCode = responseCode2;
    }

    public MtopStatistics getMtopStat() {
        return this.mtopStat;
    }

    public void setMtopStat(MtopStatistics mtopStat2) {
        this.mtopStat = mtopStat2;
    }

    public void parseJsonByte() {
        String[] message;
        if (!this.bParsed) {
            synchronized (this) {
                if (!this.bParsed) {
                    if (this.bytedata == null || this.bytedata.length == 0) {
                        if (TBSdkLog.isLogEnable(TBSdkLog.LogEnable.ErrorEnable)) {
                            TBSdkLog.e(TAG, "[parseJsonByte]MtopResponse bytedata is blank,api=" + this.api + ",v=" + this.v);
                        }
                        if (StringUtils.isBlank(this.retCode)) {
                            this.retCode = ErrorConstant.ERRCODE_JSONDATA_BLANK;
                        }
                        if (StringUtils.isBlank(this.retMsg)) {
                            this.retMsg = ErrorConstant.ERRMSG_JSONDATA_BLANK;
                        }
                        this.bParsed = true;
                        return;
                    }
                    try {
                        String jsonStr = new String(this.bytedata);
                        if (TBSdkLog.isLogEnable(TBSdkLog.LogEnable.DebugEnable)) {
                            TBSdkLog.d(TAG, "[parseJsonByte]MtopResponse bytedata : " + jsonStr);
                        }
                        JSONObject jsonObject = new JSONObject(jsonStr);
                        if (this.api == null) {
                            this.api = jsonObject.getString("api");
                        }
                        if (this.v == null) {
                            this.v = jsonObject.getString("v");
                        }
                        JSONArray jsArray = jsonObject.getJSONArray("ret");
                        int size = jsArray.length();
                        this.ret = new String[size];
                        for (int i = 0; i < size; i++) {
                            this.ret[i] = jsArray.getString(i);
                        }
                        if (size > 0) {
                            String result = this.ret[0];
                            if (StringUtils.isNotBlank(result) && (message = result.split(SHARP)) != null && message.length > 1) {
                                if (StringUtils.isBlank(this.retCode)) {
                                    this.retCode = message[0];
                                }
                                if (StringUtils.isBlank(this.retMsg)) {
                                    this.retMsg = message[1];
                                }
                            }
                        }
                        this.dataJsonObject = jsonObject.optJSONObject("data");
                        this.bParsed = true;
                    } catch (Throwable th) {
                        this.bParsed = true;
                        throw th;
                    }
                }
            }
        }
    }

    public void setSource(ResponseSource source) {
        this.responseSource = source;
    }

    public ResponseSource getSource() {
        return this.responseSource;
    }

    public String toString() {
        StringBuilder str = new StringBuilder(128);
        try {
            str.append("MtopResponse[ api=").append(this.api);
            str.append(",v=").append(this.v);
            str.append(",retCode=").append(this.retCode);
            str.append(",retMsg=").append(this.retMsg);
            str.append(",mappingCode=").append(this.mappingCode);
            str.append(",mappingCodeSuffix=").append(this.mappingCodeSuffix);
            str.append(",ret=").append(Arrays.toString(this.ret));
            str.append(",data=").append(this.dataJsonObject);
            str.append(",responseCode=").append(this.responseCode);
            str.append(",headerFields=").append(this.headerFields);
            str.append(",bytedata=").append(this.bytedata == null ? null : new String(this.bytedata));
            str.append("]");
            return str.toString();
        } catch (Throwable e) {
            e.printStackTrace();
            return super.toString();
        }
    }

    public String getFullKey() {
        if (StringUtils.isBlank(this.api) || StringUtils.isBlank(this.v)) {
            return null;
        }
        return StringUtils.concatStr2LowerCase(this.api, this.v);
    }

    public boolean isApiSuccess() {
        return ErrorConstant.isSuccess(getRetCode()) && getBytedata() != null;
    }

    public boolean isExpiredRequest() {
        return ErrorConstant.isExpiredRequest(getRetCode());
    }

    @Deprecated
    public boolean isSystemError() {
        return ErrorConstant.isSystemError(getRetCode());
    }

    public boolean isNetworkError() {
        return ErrorConstant.isNetworkError(getRetCode());
    }

    public boolean isNoNetwork() {
        return ErrorConstant.isNoNetwork(getRetCode());
    }

    public boolean isSessionInvalid() {
        return ErrorConstant.isSessionInvalid(getRetCode());
    }

    @Deprecated
    public boolean isIllegelSign() {
        return ErrorConstant.isIllegelSign(getRetCode());
    }

    public boolean is41XResult() {
        return ErrorConstant.is41XResult(getRetCode());
    }

    public boolean isApiLockedResult() {
        return 420 == this.responseCode || ErrorConstant.isApiLockedResult(getRetCode());
    }

    public boolean isApiLockedAndRequestQueued() {
        return 420 == this.responseCode && ErrorConstant.ERRCODE_FAIL_SYS_REQUEST_QUEUED.equalsIgnoreCase(getRetCode());
    }

    public boolean isMtopSdkError() {
        return ErrorConstant.isMtopSdkError(getRetCode());
    }

    public boolean isMtopServerError() {
        return ErrorConstant.isMtopServerError(getRetCode());
    }
}
