package com.taobao.wireless.trade.mbuy.sdk.co.basic;

import android.util.Pair;
import com.alibaba.fastjson.JSONObject;
import com.taobao.wireless.trade.mbuy.sdk.co.Component;
import com.taobao.wireless.trade.mbuy.sdk.co.misc.DatePickerBase;
import com.taobao.wireless.trade.mbuy.sdk.co.misc.DatePickerMode;
import com.taobao.wireless.trade.mbuy.sdk.engine.BuyEngine;
import com.taobao.wireless.trade.mbuy.sdk.engine.BuyEngineContext;
import com.taobao.wireless.trade.mbuy.sdk.engine.LinkageType;
import com.taobao.wireless.trade.mbuy.sdk.engine.RollbackProtocol;

public class DatePickerComponent extends Component {
    /* access modifiers changed from: private */
    public DatePickerBase datePickerBase = new DatePickerBase(this.fields);

    public DatePickerComponent(JSONObject data, BuyEngine engine) throws Exception {
        super(data, engine);
    }

    public void reload(JSONObject data) {
        try {
            this.datePickerBase = new DatePickerBase(data.getJSONObject("fields"));
            super.reload(data);
        } catch (Throwable th) {
        }
    }

    /* access modifiers changed from: protected */
    public String getValidateContent() {
        String selectedDate = this.fields.getString("selectedDate");
        return selectedDate != null ? selectedDate : "";
    }

    public String getTitle() {
        return this.datePickerBase.getTitle();
    }

    public long getBeginDate() {
        return this.datePickerBase.getBeginDate();
    }

    public long getEndDate() {
        return this.datePickerBase.getEndDate();
    }

    public long getSelectedDate() {
        return this.datePickerBase.getSelectedDate();
    }

    public String getSelectedDateText() {
        return this.datePickerBase.getSelectedDateText();
    }

    public void setSelectedDate(long selectedDate) {
        BuyEngineContext context = this.engine.getContext();
        if (getLinkageType() == LinkageType.REQUEST) {
            final long originalSelectedDate = getSelectedDate();
            context.setRollbackProtocol(new RollbackProtocol() {
                public void rollback() {
                    DatePickerComponent.this.datePickerBase.setSelectedDate(originalSelectedDate);
                }
            });
        }
        this.datePickerBase.setSelectedDate(selectedDate);
        notifyLinkageDelegate();
    }

    public DatePickerMode getDatePickerMode() {
        return this.datePickerBase.getDatePickerMode();
    }

    public DatePickerBase getDatePickerBase() {
        return this.datePickerBase;
    }

    public Pair<Boolean, String> isValidWeekday(long date) {
        return this.datePickerBase.isValidWeekday(date);
    }

    public String toString() {
        return super.toString() + " - DatePickerComponent [title=" + getTitle() + ", beginDate=" + getBeginDate() + ", endDate=" + getEndDate() + ", selectedDate=" + getSelectedDate() + "]";
    }
}
