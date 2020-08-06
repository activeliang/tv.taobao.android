package com.alibaba.appmonitor.event;

import com.alibaba.appmonitor.pool.BalancedPool;
import com.alibaba.fastjson.JSON;
import com.alibaba.mtl.appmonitor.model.DimensionValueSet;
import com.alibaba.mtl.appmonitor.model.MeasureValueSet;

@Deprecated
public class RawStatEvent extends Event implements IRawEvent {
    private DimensionValueSet dimensionValues;
    private MeasureValueSet measureValues;

    public DimensionValueSet getDimensionValues() {
        return this.dimensionValues;
    }

    public void setDimensionValues(DimensionValueSet dimensionValues2) {
        this.dimensionValues = (DimensionValueSet) BalancedPool.getInstance().poll(DimensionValueSet.class, new Object[0]);
        if (dimensionValues2 != null) {
            this.dimensionValues.setMap(dimensionValues2.getMap());
        }
    }

    public MeasureValueSet getMeasureValues() {
        return this.measureValues;
    }

    public void setMeasureValues(MeasureValueSet measureValues2) {
        this.measureValues = (MeasureValueSet) BalancedPool.getInstance().poll(MeasureValueSet.class, new Object[0]);
        this.measureValues.setMap(measureValues2.getMap());
    }

    public UTEvent dumpToUTEvent() {
        UTEvent event = (UTEvent) BalancedPool.getInstance().poll(UTEvent.class, new Object[0]);
        event.eventId = this.eventId;
        event.page = this.module;
        event.arg1 = this.monitorPoint;
        if (this.dimensionValues != null) {
            event.arg2 = JSON.toJSONString(this.dimensionValues.getMap());
        }
        if (this.measureValues != null) {
            event.arg3 = JSON.toJSONString(this.measureValues.getMap());
        }
        if (this.extraArg != null) {
            event.args.put("arg", this.extraArg);
        }
        return event;
    }

    public void clean() {
        super.clean();
        if (this.measureValues != null) {
            BalancedPool.getInstance().offer(this.measureValues);
            this.measureValues = null;
        }
        if (this.dimensionValues != null) {
            BalancedPool.getInstance().offer(this.dimensionValues);
            this.dimensionValues = null;
        }
    }
}
