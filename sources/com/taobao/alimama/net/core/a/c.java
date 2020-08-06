package com.taobao.alimama.net.core.a;

import com.taobao.alimama.net.core.b.b;
import com.taobao.alimama.net.core.task.MtopRequestTask;
import com.taobao.utils.Global;
import mtopsdk.mtop.common.ApiID;
import mtopsdk.mtop.common.MtopCallback;
import mtopsdk.mtop.common.MtopFinishEvent;
import mtopsdk.mtop.domain.MtopResponse;
import mtopsdk.mtop.intf.Mtop;
import mtopsdk.mtop.util.MtopConvert;

public class c extends a<MtopRequestTask> {
    /* access modifiers changed from: private */
    public MtopRequestTask a;
    /* access modifiers changed from: private */
    public b b;
    /* access modifiers changed from: private */
    public ApiID c;

    private class a implements MtopCallback.MtopFinishListener {
        private a() {
        }

        public void onFinished(MtopFinishEvent mtopFinishEvent, Object obj) {
            MtopResponse mtopResponse = mtopFinishEvent.getMtopResponse();
            com.taobao.alimama.net.core.b.a aVar = new com.taobao.alimama.net.core.b.a();
            aVar.a = mtopResponse.getRetCode();
            aVar.b = mtopResponse.getRetMsg();
            if (mtopResponse.isApiSuccess()) {
                aVar.c = MtopConvert.jsonToOutputDO(mtopResponse.getBytedata(), c.this.a.getResponseClass());
            }
            if (c.this.b != null) {
                c.this.b.a(aVar);
            }
            ApiID unused = c.this.c = null;
        }
    }

    public void a() {
        if (this.c != null) {
            this.c.cancelApiCall();
        }
    }

    public void a(MtopRequestTask mtopRequestTask, b bVar) {
        this.a = mtopRequestTask;
        this.b = bVar;
        this.c = Mtop.instance(Global.getApplication()).build(mtopRequestTask.getRequestObject(), (String) null).addListener(new a()).asyncRequest();
    }

    public String b() {
        return "MtopRequest";
    }
}
