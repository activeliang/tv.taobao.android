package com.taobao.detail.domain.component;

import com.taobao.detail.domain.component.BaseInputView;
import java.util.ArrayList;

public class CheckBox extends BaseInputView {
    public static final int TYPE = 1;
    public ArrayList<BaseInputView.Element> elements;

    public CheckBox() {
        this.type = 1;
    }
}
