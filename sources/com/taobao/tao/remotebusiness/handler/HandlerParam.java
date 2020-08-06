package com.taobao.tao.remotebusiness.handler;

import com.taobao.tao.remotebusiness.MtopBusiness;
import java.io.Serializable;
import mtopsdk.mtop.common.MtopEvent;
import mtopsdk.mtop.common.MtopListener;
import mtopsdk.mtop.domain.BaseOutDo;
import mtopsdk.mtop.domain.MtopResponse;

public class HandlerParam implements Serializable {
    private static final long serialVersionUID = 9196408638670689787L;
    public MtopEvent event;
    public MtopListener listener;
    public MtopBusiness mtopBusiness;
    public MtopResponse mtopResponse;
    public BaseOutDo pojo;

    public HandlerParam(MtopListener listener2, MtopEvent event2, MtopBusiness mtopBusiness2) {
        this.listener = listener2;
        this.event = event2;
        this.mtopBusiness = mtopBusiness2;
    }
}
