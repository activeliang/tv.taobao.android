package com.taobao.detail.domain.component;

import com.taobao.detail.domain.base.Unit;
import java.io.Serializable;
import java.util.List;

public class Tree extends BaseInputView {
    public static final int TYPE = 3;
    public Unit api;
    public List<Branch> branches;
    public Boolean sync;

    public static class Branch implements Serializable {
        public List<Branch> branches;
        public Boolean leaf;
        public String query;
        public Boolean sync;
        public String text;
    }

    public Tree() {
        this.type = 3;
    }
}
