package com.taobao.detail.domain.template;

import java.io.Serializable;
import java.util.HashMap;

public class LayoutInfo implements Serializable {
    public String data;
    public String layoutId;
    public String refreshLayoutData;
    public String refreshLayoutId;
    public HashMap<String, String> replaceDataMap;
}
