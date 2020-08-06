package com.taobao.alimama.net.core.future;

import com.taobao.alimama.net.NetRequestCallback;
import com.taobao.alimama.net.core.task.AbsNetRequestTask;

public class a implements NetFuture {
    private AbsNetRequestTask a;

    public a(AbsNetRequestTask absNetRequestTask) {
        this.a = absNetRequestTask;
    }

    public void cancel() {
        com.taobao.alimama.net.core.a.a().a(this.a);
    }

    public void retryNow() {
        com.taobao.alimama.net.core.a.a().b(this.a);
    }

    public void setCallback(NetRequestCallback netRequestCallback) {
        if (this.a != null) {
            this.a.setCallback(netRequestCallback);
        }
    }
}
