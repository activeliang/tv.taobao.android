package com.tvtaobao.voicesdk.bean;

import android.os.Parcel;
import android.os.Parcelable;
import android.text.TextUtils;
import com.alibaba.fastjson.JSON;
import java.util.ArrayList;

public class DomainResultVo implements Parcelable {
    public static final Parcelable.Creator<DomainResultVo> CREATOR = new Parcelable.Creator<DomainResultVo>() {
        public DomainResultVo createFromParcel(Parcel in) {
            return new DomainResultVo(in);
        }

        public DomainResultVo[] newArray(int size) {
            return new DomainResultVo[size];
        }
    };
    private Integer addressSwitch;
    private String domain;
    private Integer elemBind;
    private String intent;
    private String loadingTxt;
    private String resultData;
    private Object resultVO;
    private String resultVOType;
    private String semantic;
    private String spoken;
    private Integer taobaoLogin;
    private ArrayList<String> tips;
    private String toUri;

    public DomainResultVo() {
    }

    protected DomainResultVo(Parcel in) {
        this.domain = in.readString();
        this.intent = in.readString();
        this.toUri = in.readString();
        this.semantic = in.readString();
        if (in.readByte() == 0) {
            this.spoken = null;
        } else {
            this.spoken = in.readString();
        }
        if (in.readByte() == 0) {
            this.tips = null;
        } else {
            this.tips = in.createStringArrayList();
        }
        this.loadingTxt = in.readString();
        if (in.readByte() == 0) {
            this.resultVOType = null;
            this.resultData = null;
            return;
        }
        this.resultVOType = in.readString();
        this.resultData = in.readString();
        try {
            this.resultVO = JSON.parseObject(this.resultData, Class.forName(this.resultVOType));
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

    public String toString() {
        return "DomainResultVo{domain='" + this.domain + '\'' + ", intent='" + this.intent + '\'' + ", toUri='" + this.toUri + '\'' + ", spoken='" + this.spoken + '\'' + ", tips=" + this.tips + ", taobaoLogin=" + this.taobaoLogin + ", addressSwitch=" + this.addressSwitch + ", resultVO=" + this.resultVO + '}';
    }

    public String getLoadingTxt() {
        return this.loadingTxt;
    }

    public void setLoadingTxt(String loadingTxt2) {
        this.loadingTxt = loadingTxt2;
    }

    public String getDomain() {
        return this.domain;
    }

    public void setDomain(String domain2) {
        this.domain = domain2;
    }

    public String getIntent() {
        return this.intent;
    }

    public void setIntent(String intent2) {
        this.intent = intent2;
    }

    public String getToUri() {
        return this.toUri;
    }

    public void setToUri(String toUri2) {
        this.toUri = toUri2;
    }

    public ArrayList<String> getTips() {
        return this.tips;
    }

    public void setTips(ArrayList<String> tips2) {
        this.tips = tips2;
    }

    public Integer getTaobaoLogin() {
        return this.taobaoLogin;
    }

    public void setTaobaoLogin(Integer taobaoLogin2) {
        this.taobaoLogin = taobaoLogin2;
    }

    public Integer getAddressSwitch() {
        return this.addressSwitch;
    }

    public void setAddressSwitch(Integer addressSwitch2) {
        this.addressSwitch = addressSwitch2;
    }

    public <T> T getResultVO() {
        return this.resultVO;
    }

    public void setResultVO(Object resultVO2) {
        this.resultVO = resultVO2;
    }

    public String getResultVOType() {
        return this.resultVOType;
    }

    public void setResultVOType(String resultVOType2) {
        this.resultVOType = resultVOType2;
    }

    public String getSpoken() {
        return this.spoken;
    }

    public void setSpoken(String spoken2) {
        this.spoken = spoken2;
    }

    public String getSemantic() {
        return this.semantic;
    }

    public void setSemantic(String semantic2) {
        this.semantic = semantic2;
    }

    public void setResultData(String resultData2) {
        this.resultData = resultData2;
    }

    public String getResultData() {
        return this.resultData;
    }

    public Integer getElemBind() {
        return this.elemBind;
    }

    public void setElemBind(Integer elemBind2) {
        this.elemBind = elemBind2;
    }

    public int describeContents() {
        return 0;
    }

    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.domain);
        dest.writeString(this.intent);
        dest.writeString(this.toUri);
        dest.writeString(this.semantic);
        if (TextUtils.isEmpty(this.spoken)) {
            dest.writeByte((byte) 0);
        } else {
            dest.writeByte((byte) 1);
            dest.writeString(this.spoken);
        }
        if (this.tips == null) {
            dest.writeByte((byte) 0);
        } else {
            dest.writeByte((byte) 1);
            dest.writeStringList(this.tips);
        }
        dest.writeString(this.loadingTxt);
        if (TextUtils.isEmpty(this.resultVOType)) {
            dest.writeByte((byte) 0);
            return;
        }
        dest.writeByte((byte) 1);
        dest.writeString(this.resultVOType);
        this.resultData = JSON.toJSONString(getResultVO());
        dest.writeString(this.resultData);
    }
}
