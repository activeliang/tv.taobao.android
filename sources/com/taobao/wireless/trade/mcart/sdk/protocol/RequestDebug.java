package com.taobao.wireless.trade.mcart.sdk.protocol;

import mtopsdk.mtop.domain.BaseOutDo;
import mtopsdk.mtop.domain.IMTOPDataObject;
import mtopsdk.mtop.domain.MtopResponse;

public interface RequestDebug {
    void onRequestStart(IMTOPDataObject iMTOPDataObject);

    void onResponseBack(int i, MtopResponse mtopResponse, BaseOutDo baseOutDo);
}
