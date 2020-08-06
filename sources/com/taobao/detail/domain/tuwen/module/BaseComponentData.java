package com.taobao.detail.domain.tuwen.module;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

public class BaseComponentData implements Serializable {
    public String ID;
    public Map<String, BaseComponentData> children;
    public List<Map<String, String>> dataList;
    public String name;
    public String trackName;
    public Map<String, String> trackParams;
}
