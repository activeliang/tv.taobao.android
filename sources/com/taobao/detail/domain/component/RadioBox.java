package com.taobao.detail.domain.component;

import com.taobao.detail.domain.component.BaseInputView;
import java.util.ArrayList;

public class RadioBox extends BaseInputView {
    public static final int TYPE = 2;
    public ArrayList<BaseInputView.Element> elements;

    public RadioBox() {
        this.type = 2;
    }
}
