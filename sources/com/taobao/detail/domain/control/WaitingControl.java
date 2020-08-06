package com.taobao.detail.domain.control;

import com.taobao.detail.domain.component.Button;
import java.io.Serializable;
import java.util.List;

public class WaitingControl implements Serializable {
    public List<Button> buttons;
    public boolean remindSupport;
    public long waitingTime;
    public String waitingTitle;
}
