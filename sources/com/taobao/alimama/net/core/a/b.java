package com.taobao.alimama.net.core.a;

import android.os.Handler;
import anetwork.channel.NetworkCallBack;
import anetwork.channel.NetworkEvent;
import anetwork.channel.Request;
import anetwork.channel.degrade.DegradableNetwork;
import anetwork.channel.entity.RequestImpl;
import com.taobao.alimama.net.core.b.a;
import com.taobao.alimama.net.core.task.AliHttpRequestTask;
import com.taobao.utils.Global;
import java.util.Map;

public class b extends a<AliHttpRequestTask> {
    private Request a(AliHttpRequestTask aliHttpRequestTask) {
        RequestImpl requestImpl = new RequestImpl(aliHttpRequestTask.getUri());
        requestImpl.setCharset("UTF-8");
        requestImpl.setFollowRedirects(aliHttpRequestTask.isRedirect());
        requestImpl.setRetryTime(aliHttpRequestTask.getRetryTimes());
        requestImpl.setConnectTimeout(aliHttpRequestTask.getConnectTimeout());
        requestImpl.setReadTimeout(aliHttpRequestTask.getReadTimeout());
        if (aliHttpRequestTask.getExtraHeaders() != null) {
            for (Map.Entry next : aliHttpRequestTask.getExtraHeaders().entrySet()) {
                requestImpl.addHeader((String) next.getKey(), (String) next.getValue());
            }
        }
        return requestImpl;
    }

    public void a() {
    }

    public void a(AliHttpRequestTask aliHttpRequestTask, final com.taobao.alimama.net.core.b.b bVar) {
        new DegradableNetwork(Global.getApplication()).asyncSend(a(aliHttpRequestTask), (Object) null, (Handler) null, new NetworkCallBack.FinishListener() {
            public void onFinished(NetworkEvent.FinishEvent finishEvent, Object obj) {
                a aVar = new a();
                if (finishEvent != null) {
                    aVar.a = String.valueOf(finishEvent.getHttpCode());
                    aVar.b = finishEvent.getDesc();
                    aVar.c = obj;
                }
                if (bVar != null) {
                    bVar.a(aVar);
                }
            }
        });
    }

    public String b() {
        return "AliHttpRequest";
    }
}
