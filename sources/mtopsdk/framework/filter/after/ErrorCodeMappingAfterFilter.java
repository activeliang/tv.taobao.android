package mtopsdk.framework.filter.after;

import android.support.annotation.NonNull;
import mtopsdk.common.util.MtopUtils;
import mtopsdk.common.util.StringUtils;
import mtopsdk.common.util.TBSdkLog;
import mtopsdk.framework.domain.FilterResult;
import mtopsdk.framework.domain.MtopContext;
import mtopsdk.framework.filter.IAfterFilter;
import mtopsdk.mtop.domain.MtopResponse;
import mtopsdk.mtop.global.SwitchConfig;
import mtopsdk.mtop.util.ErrorConstant;

public class ErrorCodeMappingAfterFilter implements IAfterFilter {
    private static final String TAG = "mtopsdk.ErrorCodeMappingAfterFilter";

    @NonNull
    public String getName() {
        return TAG;
    }

    public String doAfter(MtopContext mtopContext) {
        String str;
        String retCode;
        String str2;
        String str3;
        String seqNo = mtopContext.seqNo;
        if (!SwitchConfig.getInstance().isGlobalErrorCodeMappingOpen()) {
            TBSdkLog.i(TAG, seqNo, "GlobalErrorCodeMappingOpen=false,Don't do ErrorCode Mapping.");
        } else {
            MtopResponse mtopResponse = mtopContext.mtopResponse;
            if (!mtopResponse.isApiSuccess()) {
                try {
                    if (mtopResponse.isNetworkError()) {
                        mtopResponse.mappingCodeSuffix = ErrorConstant.getMappingCodeByErrorCode(mtopResponse.getRetCode());
                        mtopResponse.mappingCode = ErrorConstant.appendMappingCode(mtopResponse.getResponseCode(), mtopResponse.mappingCodeSuffix);
                        String mappingMsg = SwitchConfig.errorMappingMsgMap.get(ErrorConstant.ErrorMappingType.NETWORK_ERROR_MAPPING);
                        mtopResponse.setRetMsg(mappingMsg != null ? mappingMsg : ErrorConstant.MappingMsg.NETWORK_MAPPING_MSG);
                        mtopContext.stats.retType = 1;
                        StringBuilder log = new StringBuilder(128);
                        log.append("api=").append(mtopResponse.getApi());
                        log.append(" , v=").append(mtopResponse.getV());
                        log.append(" , retCode=").append(mtopResponse.getRetCode());
                        log.append(" , retMsg=").append(mtopResponse.getRetMsg());
                        log.append(" , mappingCode=").append(mtopResponse.getMappingCode());
                        log.append(" , responseHeader=").append(mtopResponse.getHeaderFields());
                        TBSdkLog.e(TAG, seqNo, log.toString());
                    } else {
                        mtopContext.stats.retType = 2;
                        if (mtopResponse.is41XResult() || mtopResponse.isApiLockedResult()) {
                            String mappingCodeSuffix = ErrorConstant.getMappingCodeByErrorCode(mtopResponse.getRetCode());
                            if (!StringUtils.isNotBlank(mappingCodeSuffix)) {
                                mappingCodeSuffix = ErrorConstant.UNKNOWN_SERVER_MAPPING_CODE_SUFFIX;
                            }
                            mtopResponse.mappingCodeSuffix = mappingCodeSuffix;
                            mtopResponse.mappingCode = ErrorConstant.appendMappingCode(mtopResponse.getResponseCode(), mtopResponse.mappingCodeSuffix);
                            String mappingMsg2 = SwitchConfig.errorMappingMsgMap.get(ErrorConstant.ErrorMappingType.FLOW_LIMIT_ERROR_MAPPING);
                            if (mappingMsg2 != null) {
                                str = mappingMsg2;
                            } else {
                                str = ErrorConstant.MappingMsg.FLOW_LIMIT_MAPPING_MSG;
                            }
                            mtopResponse.setRetMsg(str);
                            StringBuilder log2 = new StringBuilder(128);
                            log2.append("api=").append(mtopResponse.getApi());
                            log2.append(" , v=").append(mtopResponse.getV());
                            log2.append(" , retCode=").append(mtopResponse.getRetCode());
                            log2.append(" , retMsg=").append(mtopResponse.getRetMsg());
                            log2.append(" , mappingCode=").append(mtopResponse.getMappingCode());
                            log2.append(" , responseHeader=").append(mtopResponse.getHeaderFields());
                            TBSdkLog.e(TAG, seqNo, log2.toString());
                        } else if (mtopResponse.isMtopServerError()) {
                            if (StringUtils.isBlank(mtopResponse.mappingCodeSuffix)) {
                                String localMappingCode = ErrorConstant.getMappingCodeByErrorCode(mtopResponse.getRetCode());
                                if (!StringUtils.isNotBlank(localMappingCode)) {
                                    localMappingCode = ErrorConstant.UNKNOWN_SERVER_MAPPING_CODE_SUFFIX;
                                }
                                mtopResponse.mappingCodeSuffix = localMappingCode;
                            }
                            mtopResponse.mappingCode = ErrorConstant.appendMappingCode(mtopResponse.getResponseCode(), mtopResponse.mappingCodeSuffix);
                            String mappingMsg3 = SwitchConfig.errorMappingMsgMap.get(ErrorConstant.ErrorMappingType.SERVICE_ERROR_MAPPING);
                            if (mappingMsg3 != null) {
                                str3 = mappingMsg3;
                            } else {
                                str3 = ErrorConstant.MappingMsg.SERVICE_MAPPING_MSG;
                            }
                            mtopResponse.setRetMsg(str3);
                            StringBuilder log3 = new StringBuilder(128);
                            log3.append("api=").append(mtopResponse.getApi());
                            log3.append(" , v=").append(mtopResponse.getV());
                            log3.append(" , retCode=").append(mtopResponse.getRetCode());
                            log3.append(" , retMsg=").append(mtopResponse.getRetMsg());
                            log3.append(" , mappingCode=").append(mtopResponse.getMappingCode());
                            log3.append(" , responseHeader=").append(mtopResponse.getHeaderFields());
                            TBSdkLog.e(TAG, seqNo, log3.toString());
                        } else if (mtopResponse.isMtopSdkError()) {
                            String retCode2 = mtopResponse.getRetCode();
                            String localMappingCode2 = ErrorConstant.getMappingCodeByErrorCode(retCode2);
                            if (retCode2 != null && retCode2.startsWith(ErrorConstant.ERRCODE_GENERATE_MTOP_SIGN_ERROR)) {
                                localMappingCode2 = ErrorConstant.MAPPING_CODE_GENERATE_MTOP_SIGN_ERROR;
                            }
                            if (!StringUtils.isNotBlank(localMappingCode2)) {
                                localMappingCode2 = ErrorConstant.UNKNOWN_CLIENT_MAPPING_CODE_SUFFIX;
                            }
                            mtopResponse.mappingCodeSuffix = localMappingCode2;
                            mtopResponse.mappingCode = ErrorConstant.appendMappingCode(mtopResponse.getResponseCode(), mtopResponse.mappingCodeSuffix);
                            String mappingMsg4 = SwitchConfig.errorMappingMsgMap.get(ErrorConstant.ErrorMappingType.SERVICE_ERROR_MAPPING);
                            if (mappingMsg4 != null) {
                                str2 = mappingMsg4;
                            } else {
                                str2 = ErrorConstant.MappingMsg.SERVICE_MAPPING_MSG;
                            }
                            mtopResponse.setRetMsg(str2);
                            StringBuilder log4 = new StringBuilder(128);
                            log4.append("api=").append(mtopResponse.getApi());
                            log4.append(" , v=").append(mtopResponse.getV());
                            log4.append(" , retCode=").append(mtopResponse.getRetCode());
                            log4.append(" , retMsg=").append(mtopResponse.getRetMsg());
                            log4.append(" , mappingCode=").append(mtopResponse.getMappingCode());
                            log4.append(" , responseHeader=").append(mtopResponse.getHeaderFields());
                            TBSdkLog.e(TAG, seqNo, log4.toString());
                        } else {
                            mtopContext.stats.retType = 3;
                            if (StringUtils.isNotBlank(mtopResponse.mappingCodeSuffix)) {
                                mtopResponse.mappingCode = mtopResponse.mappingCodeSuffix;
                                StringBuilder log5 = new StringBuilder(128);
                                log5.append("api=").append(mtopResponse.getApi());
                                log5.append(" , v=").append(mtopResponse.getV());
                                log5.append(" , retCode=").append(mtopResponse.getRetCode());
                                log5.append(" , retMsg=").append(mtopResponse.getRetMsg());
                                log5.append(" , mappingCode=").append(mtopResponse.getMappingCode());
                                log5.append(" , responseHeader=").append(mtopResponse.getHeaderFields());
                                TBSdkLog.e(TAG, seqNo, log5.toString());
                            } else {
                                retCode = mtopResponse.getRetCode();
                                mtopResponse.mappingCode = retCode;
                                if (StringUtils.isBlank(retCode)) {
                                    StringBuilder log6 = new StringBuilder(128);
                                    log6.append("api=").append(mtopResponse.getApi());
                                    log6.append(" , v=").append(mtopResponse.getV());
                                    log6.append(" , retCode=").append(mtopResponse.getRetCode());
                                    log6.append(" , retMsg=").append(mtopResponse.getRetMsg());
                                    log6.append(" , mappingCode=").append(mtopResponse.getMappingCode());
                                    log6.append(" , responseHeader=").append(mtopResponse.getHeaderFields());
                                    TBSdkLog.e(TAG, seqNo, log6.toString());
                                } else if (!SwitchConfig.getInstance().isBizErrorCodeMappingOpen()) {
                                    TBSdkLog.i(TAG, seqNo, "BizErrorCodeMappingOpen=false,Don't do BizErrorCode Mapping.");
                                    StringBuilder log7 = new StringBuilder(128);
                                    log7.append("api=").append(mtopResponse.getApi());
                                    log7.append(" , v=").append(mtopResponse.getV());
                                    log7.append(" , retCode=").append(mtopResponse.getRetCode());
                                    log7.append(" , retMsg=").append(mtopResponse.getRetMsg());
                                    log7.append(" , mappingCode=").append(mtopResponse.getMappingCode());
                                    log7.append(" , responseHeader=").append(mtopResponse.getHeaderFields());
                                    TBSdkLog.e(TAG, seqNo, log7.toString());
                                } else if (retCode.length() == 17 && retCode.charAt(1) == '-') {
                                    StringBuilder log8 = new StringBuilder(128);
                                    log8.append("api=").append(mtopResponse.getApi());
                                    log8.append(" , v=").append(mtopResponse.getV());
                                    log8.append(" , retCode=").append(mtopResponse.getRetCode());
                                    log8.append(" , retMsg=").append(mtopResponse.getRetMsg());
                                    log8.append(" , mappingCode=").append(mtopResponse.getMappingCode());
                                    log8.append(" , responseHeader=").append(mtopResponse.getHeaderFields());
                                    TBSdkLog.e(TAG, seqNo, log8.toString());
                                } else {
                                    if (SwitchConfig.getInstance().degradeBizErrorMappingApiSet != null) {
                                        String apiKey = mtopContext.mtopRequest.getKey();
                                        if (SwitchConfig.getInstance().degradeBizErrorMappingApiSet.contains(apiKey)) {
                                            if (TBSdkLog.isLogEnable(TBSdkLog.LogEnable.InfoEnable)) {
                                                TBSdkLog.i(TAG, seqNo, "apiKey in degradeBizErrorMappingApiSet,apiKey=" + apiKey);
                                            }
                                            StringBuilder log9 = new StringBuilder(128);
                                            log9.append("api=").append(mtopResponse.getApi());
                                            log9.append(" , v=").append(mtopResponse.getV());
                                            log9.append(" , retCode=").append(mtopResponse.getRetCode());
                                            log9.append(" , retMsg=").append(mtopResponse.getRetMsg());
                                            log9.append(" , mappingCode=").append(mtopResponse.getMappingCode());
                                            log9.append(" , responseHeader=").append(mtopResponse.getHeaderFields());
                                            TBSdkLog.e(TAG, seqNo, log9.toString());
                                        }
                                    }
                                    if (MtopUtils.isContainChineseCharacter(retCode)) {
                                        mtopResponse.mappingCode = ErrorConstant.UNKNOWN_SERVICE_PROVIDER_MAPPING_CODE_SUFFIX;
                                        TBSdkLog.e(TAG, seqNo, "retCode contain chinese character,retCode=" + retCode);
                                        StringBuilder log10 = new StringBuilder(128);
                                        log10.append("api=").append(mtopResponse.getApi());
                                        log10.append(" , v=").append(mtopResponse.getV());
                                        log10.append(" , retCode=").append(mtopResponse.getRetCode());
                                        log10.append(" , retMsg=").append(mtopResponse.getRetMsg());
                                        log10.append(" , mappingCode=").append(mtopResponse.getMappingCode());
                                        log10.append(" , responseHeader=").append(mtopResponse.getHeaderFields());
                                        TBSdkLog.e(TAG, seqNo, log10.toString());
                                    } else {
                                        String mappingCode = MtopUtils.caesarEncrypt(retCode);
                                        if (StringUtils.isNotBlank(mappingCode)) {
                                            long length = SwitchConfig.getInstance().getGlobalBizErrorMappingCodeLength();
                                            if (((long) mappingCode.length()) <= length || length <= 0) {
                                                mtopResponse.mappingCode = mappingCode;
                                            } else {
                                                mtopResponse.mappingCode = mappingCode.substring(0, (int) length);
                                            }
                                        }
                                        StringBuilder log11 = new StringBuilder(128);
                                        log11.append("api=").append(mtopResponse.getApi());
                                        log11.append(" , v=").append(mtopResponse.getV());
                                        log11.append(" , retCode=").append(mtopResponse.getRetCode());
                                        log11.append(" , retMsg=").append(mtopResponse.getRetMsg());
                                        log11.append(" , mappingCode=").append(mtopResponse.getMappingCode());
                                        log11.append(" , responseHeader=").append(mtopResponse.getHeaderFields());
                                        TBSdkLog.e(TAG, seqNo, log11.toString());
                                    }
                                }
                            }
                        }
                    }
                } catch (Exception e) {
                    TBSdkLog.e(TAG, seqNo, "Mapping biz retCode to mappingCode error.retCode=" + retCode, e);
                } catch (Throwable th) {
                    StringBuilder log12 = new StringBuilder(128);
                    log12.append("api=").append(mtopResponse.getApi());
                    log12.append(" , v=").append(mtopResponse.getV());
                    log12.append(" , retCode=").append(mtopResponse.getRetCode());
                    log12.append(" , retMsg=").append(mtopResponse.getRetMsg());
                    log12.append(" , mappingCode=").append(mtopResponse.getMappingCode());
                    log12.append(" , responseHeader=").append(mtopResponse.getHeaderFields());
                    TBSdkLog.e(TAG, seqNo, log12.toString());
                    throw th;
                }
            }
        }
        return FilterResult.CONTINUE;
    }
}
