package com.taobao.wireless.trade.mbuy.sdk.co.biz;

import com.alibaba.fastjson.JSONObject;
import com.taobao.wireless.trade.mbuy.sdk.co.Component;
import com.taobao.wireless.trade.mbuy.sdk.engine.BuyEngine;
import com.taobao.wireless.trade.mbuy.sdk.engine.BuyEngineContext;
import com.taobao.wireless.trade.mbuy.sdk.engine.RollbackProtocol;

public class QuantityComponent extends Component {
    public QuantityComponent(JSONObject data, BuyEngine engine) {
        super(data, engine);
    }

    public int getQuantity() {
        return this.fields.getIntValue("quantity");
    }

    public void setQuantity(int quantity) {
        if (quantity != getQuantity()) {
            BuyEngineContext context = this.engine.getContext();
            int min = getMin();
            int max = getMax();
            int step = getStep();
            if (quantity < min) {
                quantity = min;
            } else if (quantity > max) {
                quantity = max;
            }
            final int originalQuantity = getQuantity();
            context.setRollbackProtocol(new RollbackProtocol() {
                public void rollback() {
                    QuantityComponent.this.fields.put("quantity", (Object) Integer.valueOf(originalQuantity));
                }
            });
            this.fields.put("quantity", (Object) String.valueOf(((((quantity - min) + (step - 1)) / step) * step) + min));
            notifyLinkageDelegate();
        }
    }

    public void increaseQuantity() {
        int quantity = getQuantity();
        if (quantity != getMax()) {
            setQuantity(getStep() + quantity);
        }
    }

    public void decreaseQuantity() {
        int quantity = getQuantity();
        if (quantity != getMin()) {
            setQuantity(quantity - getStep());
        }
    }

    public int getMax() {
        int min = getMin();
        int max = this.fields.getIntValue("max");
        return max > min ? max : min;
    }

    public int getMin() {
        int min = this.fields.getIntValue("min");
        if (min > 0) {
            return min;
        }
        return 1;
    }

    public int getStep() {
        int step = this.fields.getIntValue("step");
        if (step != 0) {
            return step;
        }
        return 1;
    }

    public String toString() {
        return super.toString() + " - QuantityComponent [quantity=" + getQuantity() + ", max=" + getMax() + "]";
    }
}
