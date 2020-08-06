package com.tvtaobao.voicesdk.register.bo;

import android.os.Parcel;
import android.os.Parcelable;
import com.tvtaobao.voicesdk.register.type.RegisterType;
import com.yunos.tv.core.CoreApplication;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class Register implements Parcelable {
    public static final Parcelable.Creator<Register> CREATOR = new Parcelable.Creator<Register>() {
        public Register createFromParcel(Parcel in) {
            return new Register(in);
        }

        public Register[] newArray(int size) {
            return new Register[size];
        }
    };
    public String bizType;
    public String className;
    private JSONArray dataArray;
    private String packageName;
    public String[] registedArray;
    private ConcurrentHashMap<String, String> registedMap;
    public String resgistedType;

    public Register() {
        this.resgistedType = RegisterType.ADD;
        this.registedMap = new ConcurrentHashMap<>();
        this.packageName = CoreApplication.getApplication().getPackageName();
    }

    public ConcurrentHashMap<String, String> getRegistedMap() {
        return this.registedMap;
    }

    public void setRegistedMap(ConcurrentHashMap<String, String> registedMap2) {
        this.registedMap = registedMap2;
        this.dataArray = new JSONArray();
        for (Map.Entry<String, String> entry : registedMap2.entrySet()) {
            try {
                JSONObject object = new JSONObject();
                JSONArray key = new JSONArray();
                key.put(entry.getKey());
                object.put("key", key.toString());
                object.put("value", entry.getValue());
                this.dataArray.put(object);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }

    protected Register(Parcel in) {
        this.resgistedType = RegisterType.ADD;
        this.registedMap = new ConcurrentHashMap<>();
        this.packageName = in.readString();
        this.className = in.readString();
        this.resgistedType = in.readString();
        this.registedArray = in.createStringArray();
    }

    public int describeContents() {
        return 0;
    }

    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(this.packageName);
        parcel.writeString(this.className);
        parcel.writeString(this.resgistedType);
        if (this.registedArray != null) {
            parcel.writeStringArray(this.registedArray);
        }
    }

    public String getParams() {
        JSONObject object = new JSONObject();
        try {
            object.put("bizType", this.bizType);
            object.put("data", this.dataArray);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return object.toString();
    }
}
