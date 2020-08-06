package com.taobao.tao.remotebusiness.listener;

import com.taobao.tao.remotebusiness.MtopBusiness;
import mtopsdk.mtop.common.MtopListener;

abstract class MtopBaseListener {
    protected MtopListener listener = null;
    protected MtopBusiness mtopBusiness = null;

    protected MtopBaseListener(MtopBusiness mtopBusiness2, MtopListener listener2) {
        this.mtopBusiness = mtopBusiness2;
        this.listener = listener2;
    }
}
