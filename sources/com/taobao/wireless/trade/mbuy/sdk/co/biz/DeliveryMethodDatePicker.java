package com.taobao.wireless.trade.mbuy.sdk.co.biz;

import android.annotation.SuppressLint;
import android.taobao.windvane.packageapp.zipapp.utils.ZipAppConstants;
import android.text.TextUtils;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.taobao.wireless.trade.mbuy.sdk.co.Component;
import com.taobao.wireless.trade.mbuy.sdk.co.basic.LabelComponent;
import com.taobao.wireless.trade.mbuy.sdk.engine.BuyEngine;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Iterator;

public class DeliveryMethodDatePicker {
    private Component component;
    private JSONObject data;
    private ArrayList<String> dates;
    private ArrayList<String> disable;
    protected SimpleDateFormat fmt1;
    protected SimpleDateFormat fmt2;
    private String nDaysLater;
    private ArrayList<Long> originalDates;
    private String payTimeTip;
    private ArrayList<String> periods;
    private LabelComponent topLabelComponent;

    @SuppressLint({"SimpleDateFormat"})
    public DeliveryMethodDatePicker(JSONObject data2, Component component2) throws Exception {
        this.component = component2;
        this.data = data2;
        if (this.data == null) {
            throw new IllegalArgumentException();
        }
        if (!this.data.getBooleanValue("useDefault")) {
            this.data.remove("selectedPeriods");
            this.data.remove("selectedDate");
        }
        String begin = data2.getString("beginDate");
        String end = data2.getString("endDate");
        if (begin == null || begin.isEmpty() || end == null || end.isEmpty()) {
            throw new IllegalArgumentException();
        }
        this.fmt1 = new SimpleDateFormat("yyyy-MM-dd");
        this.fmt2 = new SimpleDateFormat("EEEE MM月dd日");
        long beginDate = this.fmt1.parse(begin).getTime();
        long endDate = this.fmt1.parse(end).getTime();
        if (beginDate > endDate) {
            throw new IllegalArgumentException();
        }
        this.originalDates = new ArrayList<>();
        this.dates = new ArrayList<>();
        while (beginDate <= endDate) {
            this.originalDates.add(Long.valueOf(beginDate));
            this.dates.add(this.fmt2.format(new Date(beginDate)));
            beginDate += ZipAppConstants.UPDATEGROUPID_AGE;
        }
        this.nDaysLater = data2.getString("nDaysLater");
        if (!TextUtils.isEmpty(this.nDaysLater)) {
            this.dates.add(this.nDaysLater);
        }
        JSONArray a = this.data.getJSONArray("periods");
        if (a == null || a.isEmpty()) {
            throw new IllegalArgumentException();
        }
        this.periods = new ArrayList<>(a.size());
        Iterator<Object> it = a.iterator();
        while (it.hasNext()) {
            this.periods.add((String) it.next());
        }
        JSONArray b = this.data.getJSONArray("disable");
        if (b != null && !b.isEmpty()) {
            this.disable = new ArrayList<>(b.size());
            Iterator<Object> it2 = b.iterator();
            while (it2.hasNext()) {
                this.disable.add((String) it2.next());
            }
        }
        this.payTimeTip = data2.getString("payTimeTip");
        if (!TextUtils.isEmpty(this.payTimeTip)) {
            this.payTimeTip = this.payTimeTip.replace("{m}", "MM");
            this.payTimeTip = this.payTimeTip.replace("{d}", "dd");
        } else {
            this.payTimeTip = "请及时付款";
        }
        try {
            JSONObject labelJSON = new JSONObject();
            labelJSON.put("fields", (Object) data2.getJSONObject("tips"));
            labelJSON.put("type", (Object) "label");
            this.topLabelComponent = new LabelComponent(labelJSON, (BuyEngine) null);
        } catch (Throwable e) {
            e.printStackTrace();
        }
    }

    public String getSelectedPeriod() {
        if (this.periods == null || this.periods.isEmpty()) {
            return null;
        }
        return this.data.getString("selectedPeriods");
    }

    public String getSelectedDateText() {
        long date = getSelectedDate();
        if (date != -1) {
            return this.fmt2.format(Long.valueOf(date));
        }
        if (this.nDaysLater == null || !this.nDaysLater.equals(this.data.getString("selectedDate"))) {
            return null;
        }
        return this.nDaysLater;
    }

    public long getSelectedDate() {
        String selectedDate = this.data.getString("selectedDate");
        if (selectedDate == null) {
            return -1;
        }
        try {
            return this.fmt1.parse(selectedDate).getTime();
        } catch (ParseException e) {
            return -1;
        }
    }

    public ArrayList<String> getPeriods() {
        return this.periods;
    }

    public ArrayList<String> getDates() {
        return this.dates;
    }

    public String getPayTimeText(int curIndex) {
        return getPayTimeTips(curIndex) + "，以确保我们在指定的时间配送（另：不可抗力因素可致延误）";
    }

    @SuppressLint({"SimpleDateFormat"})
    private String getPayTimeTips(int curIndex) {
        if (curIndex == -1) {
            return "请及时付款";
        }
        int wayDay = this.data.getIntValue("wayDay");
        Calendar cal = Calendar.getInstance();
        if (curIndex == this.originalDates.size()) {
            cal.setTime(new Date(this.originalDates.get(curIndex - 1).longValue()));
        } else {
            cal.setTime(new Date(this.originalDates.get(curIndex).longValue()));
        }
        cal.add(6, -wayDay);
        return new SimpleDateFormat(this.payTimeTip).format(cal.getTime());
    }

    public String getSelectedTimeText() {
        String selectedDate = getSelectedDateText();
        String selectPeriod = getSelectedPeriod();
        if (selectedDate == null || selectPeriod == null) {
            return null;
        }
        return selectedDate + " " + selectPeriod + " 配送\n（" + getPayTimeTips(this.dates.indexOf(selectedDate)) + "）";
    }

    public void setSelectedDateAndPeriod(String selectedDate, String selectedPeriod) {
        if (selectedDate == null || selectedPeriod == null) {
            this.data.remove("selectedPeriods");
            this.data.remove("selectedDate");
            this.component.notifyLinkageDelegate();
            return;
        }
        int index = this.dates.indexOf(selectedDate);
        if (index != -1) {
            this.data.put("selectedPeriods", (Object) selectedPeriod);
            if (index == this.originalDates.size()) {
                this.data.put("selectedDate", (Object) selectedDate);
            } else {
                this.data.put("selectedDate", (Object) this.fmt1.format(new Date(this.originalDates.get(index).longValue())));
            }
            this.component.notifyLinkageDelegate();
        }
    }

    public boolean isEnoughCapacityWithDateAndPeriod(String date, String period) {
        if (this.disable == null) {
            return true;
        }
        int index1 = this.dates.indexOf(date);
        if (this.disable.indexOf(index1 + "_" + this.periods.indexOf(period)) != -1) {
            return false;
        }
        return true;
    }

    public boolean isEnoughCapacityWithDate(String date) {
        Iterator<String> it = this.periods.iterator();
        while (it.hasNext()) {
            if (isEnoughCapacityWithDateAndPeriod(date, it.next())) {
                return true;
            }
        }
        return false;
    }

    public String getRecommendedPeriodWithDate(String date) {
        if (this.periods.size() > 1) {
            return null;
        }
        String period = this.periods.get(0);
        if (!isEnoughCapacityWithDateAndPeriod(date, period)) {
            period = null;
        }
        return period;
    }

    public boolean isAvailableCapacity() {
        if (this.disable != null && this.disable.size() >= this.periods.size() * this.dates.size()) {
            return false;
        }
        return true;
    }

    public boolean isEnableCancel() {
        if (this.data.get("enableCancel") == null) {
            return true;
        }
        return this.data.getBooleanValue("enableCancel");
    }

    public LabelComponent getTopLabelCompnent() {
        return this.topLabelComponent;
    }
}
