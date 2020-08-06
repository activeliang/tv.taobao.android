package com.yunos.tv.tao.speech.client.domain.result.newtakeout;

import java.util.List;

public class PiecewiseAgentFee {
    private String description;
    private String extraFee;
    private String isExtra;
    private String noSubsidyFee;
    private List<Rules> rules;
    private String subsidyFee;
    private String tips;

    public void setSubsidyFee(String subsidyFee2) {
        this.subsidyFee = subsidyFee2;
    }

    public String getSubsidyFee() {
        return this.subsidyFee;
    }

    public void setIsExtra(String isExtra2) {
        this.isExtra = isExtra2;
    }

    public String getIsExtra() {
        return this.isExtra;
    }

    public void setNoSubsidyFee(String noSubsidyFee2) {
        this.noSubsidyFee = noSubsidyFee2;
    }

    public String getNoSubsidyFee() {
        return this.noSubsidyFee;
    }

    public void setDescription(String description2) {
        this.description = description2;
    }

    public String getDescription() {
        return this.description;
    }

    public void setRules(List<Rules> rules2) {
        this.rules = rules2;
    }

    public List<Rules> getRules() {
        return this.rules;
    }

    public void setExtraFee(String extraFee2) {
        this.extraFee = extraFee2;
    }

    public String getExtraFee() {
        return this.extraFee;
    }

    public void setTips(String tips2) {
        this.tips = tips2;
    }

    public String getTips() {
        return this.tips;
    }
}
