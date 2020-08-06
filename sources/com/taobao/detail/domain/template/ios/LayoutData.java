package com.taobao.detail.domain.template.ios;

import java.io.Serializable;
import java.util.List;

public class LayoutData implements Serializable {
    public Component bottom;
    public List<Component> components;
    public String group = "default";
    public Component head;
}
