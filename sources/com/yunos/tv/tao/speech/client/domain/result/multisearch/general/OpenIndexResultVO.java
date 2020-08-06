package com.yunos.tv.tao.speech.client.domain.result.multisearch.general;

import com.yunos.tv.tao.speech.client.domain.result.multisearch.BaseResultVO;

public class OpenIndexResultVO extends BaseResultVO {
    private String norm;
    private String raw;

    public String getNorm() {
        return this.norm;
    }

    public void setNorm(String norm2) {
        this.norm = norm2;
    }

    public String getRaw() {
        return this.raw;
    }

    public void setRaw(String raw2) {
        this.raw = raw2;
    }
}
