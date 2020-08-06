package com.yunos.alitvcompliance.types;

public class RetData {
    private RetCode code;
    private String result;

    public RetData(RetCode c, String r) {
        this.code = c;
        this.result = r;
    }

    public RetCode getCode() {
        return this.code;
    }

    public void setCode(RetCode code2) {
        this.code = code2;
    }

    public String getResult() {
        return this.result;
    }

    public void setResult(String result2) {
        this.result = result2;
    }

    public String toString() {
        return "RetData [code=" + this.code + ", result=" + this.result + "]";
    }
}
