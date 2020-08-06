package com.taobao.wireless.trade.mbuy.sdk.co.misc;

import android.text.TextUtils;
import android.util.Pair;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.util.TypeUtils;
import com.taobao.tao.remotebusiness.js.MtopJSBridge;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Iterator;

public class DatePickerBase {
    protected long beginDate;
    private Calendar calendar = Calendar.getInstance();
    protected JSONObject data;
    protected DatePickerMode datePickerMode;
    protected long endDate;
    protected SimpleDateFormat fmt1;
    protected SimpleDateFormat fmt2;
    protected int invalidWeekday = 0;
    protected long selectedDate;
    protected String title;
    private String[] weekdayText = {"", "周一", "周二", "周三", "周四", "周五", "周六", "周日"};

    public DatePickerBase(JSONObject data2) throws Exception {
        this.data = data2;
        if (this.data == null) {
            throw new IllegalArgumentException();
        }
        this.title = data2.getString("title");
        String beginDate2 = data2.getString("beginDate");
        String endDate2 = data2.getString("endDate");
        if (beginDate2 == null || beginDate2.isEmpty() || endDate2 == null || endDate2.isEmpty()) {
            throw new IllegalArgumentException();
        }
        String dateType = data2.getString(MtopJSBridge.MtopJSParam.DATA_TYPE);
        if (TextUtils.isEmpty(dateType) || !dateType.equals("yyyy-MM-dd HH:mm:ss")) {
            this.datePickerMode = DatePickerMode.DATE;
            this.fmt1 = new SimpleDateFormat("yyyy-MM-dd");
            this.fmt2 = new SimpleDateFormat("yyyy年MM月dd日");
        } else {
            this.datePickerMode = DatePickerMode.DATE_AND_TIME;
            this.fmt1 = new SimpleDateFormat(dateType);
            this.fmt2 = new SimpleDateFormat("yyyy年MM月dd日 HH:mm");
        }
        this.beginDate = this.fmt1.parse(beginDate2).getTime();
        this.endDate = this.fmt1.parse(endDate2).getTime();
        String selectedDate2 = data2.getString("selectedDate");
        if (selectedDate2 != null) {
            this.selectedDate = this.fmt1.parse(selectedDate2).getTime();
        } else {
            this.selectedDate = -1;
        }
        JSONArray invalidWeekdayArray = data2.getJSONArray("invalidWeekday");
        if (invalidWeekdayArray != null) {
            Iterator<Object> it = invalidWeekdayArray.iterator();
            while (it.hasNext()) {
                this.invalidWeekday |= 1 << TypeUtils.castToInt(it.next()).intValue();
            }
        }
    }

    public long getSelectedDate() {
        return this.selectedDate;
    }

    public String getSelectedDateText() {
        if (this.selectedDate != -1) {
            return this.fmt2.format(new Date(this.selectedDate));
        }
        return null;
    }

    public void setSelectedDate(long selectedDate2) {
        if (selectedDate2 == -1) {
            this.selectedDate = selectedDate2;
            this.data.remove("selectedDate");
        } else if (selectedDate2 >= this.beginDate && selectedDate2 <= this.endDate) {
            this.selectedDate = selectedDate2;
            this.data.put("selectedDate", (Object) this.fmt1.format(new Date(selectedDate2)));
        }
    }

    public String getTitle() {
        return this.title;
    }

    public long getBeginDate() {
        return this.beginDate;
    }

    public long getEndDate() {
        return this.endDate;
    }

    public DatePickerMode getDatePickerMode() {
        return this.datePickerMode;
    }

    public Pair<Boolean, String> isValidWeekday(long date) {
        this.calendar.setTime(new Date(date));
        int w = this.calendar.get(7);
        int w2 = w == 1 ? 7 : w - 1;
        if (((1 << w2) & this.invalidWeekday) == 0) {
            return Pair.create(true, (Object) null);
        }
        return Pair.create(false, String.format("%s不可选", new Object[]{this.weekdayText[w2]}));
    }
}
