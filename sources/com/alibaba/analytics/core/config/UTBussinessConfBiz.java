package com.alibaba.analytics.core.config;

import com.alibaba.analytics.core.Variables;
import java.util.Map;

public class UTBussinessConfBiz extends UTOrangeConfBiz {
    public void onOrangeConfigurationArrive(String aConfName, Map<String, String> aConfContent) {
        String lTPKConfStr;
        if (aConfContent.containsKey("tpk") && (lTPKConfStr = aConfContent.get("tpk")) != null) {
            Variables.getInstance().setTPKString(lTPKConfStr);
            UTConfigMgr.postServerConfig("tpk_md5", Variables.getInstance().getTpkMD5());
        }
    }

    public void onNonOrangeConfigurationArrive(String aGroupname) {
        super.onNonOrangeConfigurationArrive(aGroupname);
    }

    public String[] getOrangeGroupnames() {
        return new String[]{"ut_bussiness"};
    }
}
