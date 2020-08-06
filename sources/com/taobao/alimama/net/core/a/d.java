package com.taobao.alimama.net.core.a;

import com.taobao.alimama.net.core.task.AbsNetRequestTask;
import com.taobao.alimama.net.core.task.AliHttpRequestTask;
import com.taobao.alimama.net.core.task.MtopRequestTask;

public class d {
    public static a a(AbsNetRequestTask absNetRequestTask) {
        if (absNetRequestTask instanceof MtopRequestTask) {
            return new c();
        }
        if (absNetRequestTask instanceof AliHttpRequestTask) {
            return new b();
        }
        return null;
    }
}
