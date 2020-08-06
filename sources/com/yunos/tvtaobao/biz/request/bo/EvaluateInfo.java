package com.yunos.tvtaobao.biz.request.bo;

import java.io.Serializable;
import org.json.JSONException;
import org.json.JSONObject;

public class EvaluateInfo extends BaseMO implements Serializable {
    private static final long serialVersionUID = 5492984001691665562L;
    private Integer evaluatorCount;
    private Double highGap;
    private Double score;
    private String title;

    public static EvaluateInfo resolveFromMTOP(JSONObject obj) throws JSONException {
        if (obj == null) {
            return null;
        }
        EvaluateInfo e = new EvaluateInfo();
        if (!obj.isNull("title")) {
            e.setTitle(obj.getString("title"));
        }
        if (!obj.isNull("score")) {
            e.setScore(Double.valueOf(obj.getDouble("score")));
        }
        if (!obj.isNull("highGap")) {
            e.setHighGap(Double.valueOf(obj.getDouble("highGap")));
        }
        if (obj.isNull("evaluatorCount")) {
            return e;
        }
        e.setEvaluatorCount(Integer.valueOf(obj.getInt("evaluatorCount")));
        return e;
    }

    public String getTitle() {
        return this.title;
    }

    public void setTitle(String title2) {
        this.title = title2;
    }

    public Double getHighGap() {
        return this.highGap;
    }

    public void setHighGap(Double highGap2) {
        this.highGap = highGap2;
    }

    public Integer getEvaluatorCount() {
        return this.evaluatorCount;
    }

    public void setEvaluatorCount(Integer evaluatorCount2) {
        this.evaluatorCount = evaluatorCount2;
    }

    public Double getScore() {
        return this.score;
    }

    public void setScore(Double score2) {
        this.score = score2;
    }
}
