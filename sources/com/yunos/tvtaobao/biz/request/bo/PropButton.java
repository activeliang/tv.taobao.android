package com.yunos.tvtaobao.biz.request.bo;

import android.widget.Button;
import java.io.Serializable;

public class PropButton implements Serializable {
    private static final long serialVersionUID = -4715336720433647215L;
    private Button button;
    private Long pvid;

    public PropButton(Long pvid2, Button button2) {
        this.pvid = pvid2;
        this.button = button2;
    }

    public Long getPvid() {
        return this.pvid;
    }

    public Button getButton() {
        return this.button;
    }
}
