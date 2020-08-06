package com.alibaba.appmonitor.event;

import com.alibaba.appmonitor.pool.BalancedPool;
import com.taobao.muniontaobaosdk.p4p.anticheat.model.ClientTraceData;

@Deprecated
public class RawCountEvent extends Event implements IRawEvent {
    private int count;
    private double value;

    public UTEvent dumpToUTEvent() {
        UTEvent event = (UTEvent) BalancedPool.getInstance().poll(UTEvent.class, new Object[0]);
        event.eventId = this.eventId;
        event.page = this.module;
        event.arg1 = this.monitorPoint;
        event.arg2 = String.valueOf(this.count);
        event.arg3 = String.valueOf(this.value);
        if (this.extraArg != null) {
            event.args.put("arg", this.extraArg);
        }
        return event;
    }

    public double getValue() {
        return this.value;
    }

    public void setValue(double value2) {
        this.count = 1;
        this.value = value2;
    }

    public void clean() {
        super.clean();
        this.count = 0;
        this.value = ClientTraceData.b.f47a;
    }
}
