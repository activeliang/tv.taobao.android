package com.taobao.securityjni.tools;

@Deprecated
public class DataContext {
    public int category;
    public byte[] extData;
    public int index;
    public int type;

    public DataContext() {
        this.index = -1;
        this.extData = null;
        this.category = -1;
        this.type = -1;
    }

    public DataContext(int index2, byte[] extData2) {
        this.index = index2;
        this.extData = extData2;
    }

    public DataContext(int index2, byte[] extData2, int cate, int type2) {
        this.index = index2;
        this.extData = extData2;
        this.category = cate;
        this.type = type2;
    }
}
