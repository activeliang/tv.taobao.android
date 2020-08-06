package com.taobao.detail.domain.component;

import com.alibaba.fastjson.JSON;
import java.io.Serializable;

public class BaseInputView extends BaseComponent {
    public String data;
    public String key;
    public int type;

    public static class Element implements Serializable {
        public String altImg;
        public String altText;
        public boolean changeable;
        public boolean selected;
        public String text;
        public String value;
    }

    public BaseInputView parse() {
        switch (this.type) {
            case 1:
                return (BaseInputView) JSON.parseObject(this.data, CheckBox.class);
            case 2:
                return (BaseInputView) JSON.parseObject(this.data, RadioBox.class);
            case 3:
                return (BaseInputView) JSON.parseObject(this.data, Tree.class);
            default:
                return null;
        }
    }
}
