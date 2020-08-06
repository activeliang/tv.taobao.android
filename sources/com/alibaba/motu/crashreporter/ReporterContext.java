package com.alibaba.motu.crashreporter;

import android.content.Context;
import com.alibaba.motu.crashreporter.Propertys;
import com.alibaba.motu.crashreporter.utils.StringUtils;
import com.ta.utdid2.device.UTDevice;

public class ReporterContext {
    Context mContext;
    Propertys mPropertys = new Propertys();

    ReporterContext(Context context) {
        this.mContext = context;
    }

    public void setProperty(Propertys.Property property) {
        this.mPropertys.add(property);
    }

    public String getProperty(String name) {
        return this.mPropertys.getString(name, "");
    }

    public String getProperty(String name, String defaultVal) {
        return this.mPropertys.getString(name, defaultVal);
    }

    public String getPropertyAndSet(String name) {
        if (StringUtils.isBlank(this.mPropertys.getValue(name)) && (Constants.UTDID.equals(name) || "IMEI".equals(name) || "IMSI".equals(name) || Constants.DEVICE_ID.equals(name))) {
            String utdid = UTDevice.getUtdid(this.mContext);
            String imei = Utils.getImei(this.mContext);
            String imsi = Utils.getImsi(this.mContext);
            this.mPropertys.add(new Propertys.Property(Constants.UTDID, utdid, true));
            this.mPropertys.add(new Propertys.Property("IMEI", imei, true));
            this.mPropertys.add(new Propertys.Property("IMSI", imsi, true));
            this.mPropertys.add(new Propertys.Property(Constants.DEVICE_ID, imei, true));
        }
        return this.mPropertys.getValue(name);
    }
}
