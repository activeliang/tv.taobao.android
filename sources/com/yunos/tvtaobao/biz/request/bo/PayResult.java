package com.yunos.tvtaobao.biz.request.bo;

import android.text.TextUtils;
import com.yunos.tv.blitz.service.BlitzServiceUtils;
import com.zhiping.dev.android.logger.ZpLogger;
import java.io.Serializable;
import mtopsdk.common.util.SymbolExpUtil;

public class PayResult implements Serializable {
    private static final long serialVersionUID = 450541528396783649L;
    private String externToken;
    private String memo;
    private String partner;
    private String resultStatus;
    private String success;
    private String tradeNo;

    public String getResultStatus() {
        return this.resultStatus;
    }

    public void setResultStatus(String resultStatus2) {
        this.resultStatus = resultStatus2;
    }

    public String getMemo() {
        return this.memo;
    }

    public void setMemo(String memo2) {
        this.memo = memo2;
    }

    public String getTradeNo() {
        return this.tradeNo;
    }

    public void setTradeNo(String tradeNo2) {
        this.tradeNo = tradeNo2;
    }

    public String getExternToken() {
        return this.externToken;
    }

    public void setExternToken(String externToken2) {
        this.externToken = externToken2;
    }

    public String getPartner() {
        return this.partner;
    }

    public void setPartner(String partner2) {
        this.partner = partner2;
    }

    public String getSuccess() {
        return this.success;
    }

    public void setSuccess(String success2) {
        this.success = success2;
    }

    public static PayResult fromStr(String res) {
        PayResult payResult = new PayResult();
        try {
            for (String payResultItemStr : res.split(SymbolExpUtil.SYMBOL_SEMICOLON)) {
                String[] payResultItemKv = payResultItemStr.split("=");
                if (payResultItemKv[0].equals("resultStatus")) {
                    payResult.setResultStatus(payResultItemKv[1].substring(1, payResultItemKv[1].length() - 1));
                } else if (payResultItemKv[0].equals("memo")) {
                    payResult.setMemo(payResultItemKv[1].substring(1, payResultItemKv[1].length() - 1));
                } else if (payResultItemKv[0].equals("result") && !payResultItemKv[1].equals("{}")) {
                    for (String resultItemStr : payResultItemKv[1].split("&")) {
                        String[] resultItemKv = resultItemStr.split("=");
                        if (resultItemKv[0].equals("trade_no")) {
                            payResult.setTradeNo(resultItemKv[1]);
                        } else if (resultItemKv[0].equals("extern_token")) {
                            payResult.setExternToken(resultItemKv[1]);
                        } else if (resultItemKv[0].equals("partner")) {
                            payResult.setPartner(resultItemKv[1]);
                        } else if (resultItemKv[0].equals(BlitzServiceUtils.CSUCCESS)) {
                            payResult.setSuccess(resultItemKv[1]);
                        }
                    }
                }
            }
        } catch (Exception e) {
            ZpLogger.e("PayResult", "数据解析错误", e);
        }
        return payResult;
    }

    public boolean isSuccess() {
        if (TextUtils.isEmpty(this.resultStatus) || TextUtils.isEmpty(this.success) || !this.resultStatus.equalsIgnoreCase("9000") || !this.success.equalsIgnoreCase("true")) {
            return false;
        }
        return true;
    }

    public boolean isCancel() {
        return this.resultStatus.equals("6001");
    }

    public String getErrorMsg() {
        if (this.resultStatus.equals("4003")) {
            return "支付宝账户被冻结或不允许支付";
        }
        if (this.resultStatus.equals("6000")) {
            return "支付服务正在进行升级,请稍后再试";
        }
        return "支付未成功";
    }
}
