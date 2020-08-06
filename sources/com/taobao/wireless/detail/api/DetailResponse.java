package com.taobao.wireless.detail.api;

import com.taobao.detail.clientDomain.TBDetailResultVO;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class DetailResponse implements Serializable {
    private static final String ERR_CODE = "ERR_CODE";
    private static final String FAIL = "FAIL";
    private static final String KEY = "KEY";
    private static final String SHARP = "::";
    private static final String VALUE = "VALUE";
    private TBDetailResultVO data;
    private String[] ret;
    private String retCode;
    private String retMsg;

    public TBDetailResultVO getData() {
        return this.data;
    }

    public void setData(TBDetailResultVO data2) {
        this.data = data2;
    }

    public String[] getRet() {
        return this.ret;
    }

    public void setRet(String[] ret2) {
        this.ret = ret2;
        parserRet(ret2);
    }

    public void parserRet(String[] ret2) {
        if (ret2 != null && ret2.length >= 1) {
            List<Map<String, String>> list = new ArrayList<>();
            for (String result : ret2) {
                if (result.indexOf(SHARP) >= 0) {
                    Map<String, String> map = new HashMap<>();
                    String[] message = result.split(SHARP);
                    if (message != null && message.length > 1) {
                        map.put("KEY", message[0]);
                        map.put("VALUE", message[1]);
                        list.add(map);
                    }
                }
            }
            if (list.size() == 1) {
                Map<String, String> map2 = list.get(0);
                this.retCode = map2.get("KEY");
                this.retMsg = map2.get("VALUE");
            } else if (list.size() == 2) {
                Map<String, String> map1 = list.get(0);
                Map<String, String> map22 = list.get(1);
                if (!"FAIL".equals(map1.get("KEY")) || !"ERR_CODE".equals(map22.get("KEY"))) {
                    this.retCode = map22.get("KEY");
                    this.retMsg = map22.get("VALUE");
                    return;
                }
                this.retCode = map22.get("VALUE");
                this.retMsg = map1.get("VALUE");
            }
        }
    }

    public String getRetCode() {
        return this.retCode;
    }

    public void setRetCode(String retCode2) {
        this.retCode = retCode2;
    }

    public String getRetMsg() {
        return this.retMsg;
    }

    public void setRetMsg(String retMsg2) {
        this.retMsg = retMsg2;
    }
}
