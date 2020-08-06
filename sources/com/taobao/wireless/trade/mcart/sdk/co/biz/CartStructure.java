package com.taobao.wireless.trade.mcart.sdk.co.biz;

import com.taobao.wireless.trade.mcart.sdk.co.Component;
import java.util.ArrayList;
import java.util.List;

public class CartStructure {
    private List<Component> body = new ArrayList();
    private List<Component> footer = new ArrayList();
    private List<Component> header = new ArrayList();

    public List<Component> getHeader() {
        return this.header;
    }

    public void setHeader(List<Component> header2) {
        this.header = header2;
    }

    public List<Component> getBody() {
        return this.body;
    }

    public void setBody(List<Component> body2) {
        this.body = body2;
    }

    public List<Component> getFooter() {
        return this.footer;
    }

    public void setFooter(List<Component> footer2) {
        this.footer = footer2;
    }
}
