package com.taobao.alimama.net;

import com.taobao.alimama.net.core.future.NetFuture;
import com.taobao.alimama.net.core.task.AbsNetRequestTask;

public abstract class NetRequestManager {

    public static class a {
        private static final int c = 500;
        private static final int d = 5;
        public int a = 500;
        public int b = 5;
    }

    public static NetRequestManager getInstance() {
        return com.taobao.alimama.net.core.a.a();
    }

    public abstract void setGlobalConfig(a aVar);

    public abstract NetFuture startRequest(AbsNetRequestTask absNetRequestTask);
}
