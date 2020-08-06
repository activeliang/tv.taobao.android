package com.taobao.detail.domain.base;

import com.taobao.wireless.lang.CheckUtils;
import java.io.Serializable;

public class BaseVO implements Serializable {
    public Unit actionUrl;
    public Unit asynUrl;
    public Unit h5Url;
    public String hide;
    public String html;

    public boolean visuable() {
        return CheckUtils.isEmpty(this.hide);
    }
}
