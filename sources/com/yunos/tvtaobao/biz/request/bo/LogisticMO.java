package com.yunos.tvtaobao.biz.request.bo;

import java.io.Serializable;
import java.util.ArrayList;

public class LogisticMO implements Serializable {
    private static final long serialVersionUID = -5632146480126208121L;
    private String comName;
    private ArrayList<Des> desList;
    private String outSid;
    private String status;
    private String tId;

    public static class Des {
        public String desc;
        public String time;

        public Des(String time2, String desc2) {
            this.desc = desc2;
            this.time = time2;
        }

        public String toString() {
            return "Des [time=" + this.time + ", desc=" + this.desc + "]";
        }
    }

    public String toString() {
        return "LogisticMO [outSid=" + this.outSid + ", comName=" + this.comName + ", tId=" + this.tId + ", status=" + this.status + ", desList=" + this.desList + "]";
    }

    public String gettId() {
        return this.tId;
    }

    public void settId(String tId2) {
        this.tId = tId2;
    }

    public String getOutSid() {
        return this.outSid;
    }

    public void setOutSid(String outSid2) {
        this.outSid = outSid2;
    }

    public String getComName() {
        return this.comName;
    }

    public void setComName(String comName2) {
        this.comName = comName2;
    }

    public String getStatus() {
        return this.status;
    }

    public void setStatus(String status2) {
        this.status = status2;
    }

    public ArrayList<Des> getDesList() {
        return this.desList;
    }

    public void addDesList(Des des) {
        if (des != null) {
            if (this.desList == null) {
                this.desList = new ArrayList<>();
            }
            this.desList.add(0, des);
        }
    }
}
