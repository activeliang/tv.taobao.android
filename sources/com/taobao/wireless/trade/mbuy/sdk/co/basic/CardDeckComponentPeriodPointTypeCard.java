package com.taobao.wireless.trade.mbuy.sdk.co.basic;

import android.text.TextUtils;
import com.alibaba.fastjson.JSONObject;
import com.yunos.tvtaobao.biz.common.BaseConfig;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class CardDeckComponentPeriodPointTypeCard extends CardDeckComponentCard {
    private static final SimpleDateFormat ymdhmFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm");

    public CardDeckComponentPeriodPointTypeCard(JSONObject cardData) {
        super(cardData);
    }

    public List<CardDeckComponentCheckBoxItemData> getCheckBoxItems() {
        JSONObject time = this.cardData.getJSONObject("time");
        if (time != null && time.size() > 0) {
            String beginTime = time.getString("beginTime");
            String endTime = time.getString("endTime");
            String format = time.getString("format");
            String interval = time.getString("interval");
            String type = time.getString("type");
            String child = time.getString("child");
            try {
                int intervalInt = Integer.parseInt(interval);
                try {
                    SimpleDateFormat simpleDateFormat = new SimpleDateFormat(format);
                    if (!TextUtils.isEmpty(beginTime) && !TextUtils.isEmpty(endTime) && !TextUtils.isEmpty(format) && intervalInt > 0 && !TextUtils.isEmpty(type) && simpleDateFormat != null) {
                        boolean isPeriod = false;
                        boolean isPoint = false;
                        if ("period".equals(type)) {
                            isPeriod = true;
                        }
                        if (BaseConfig.INTENT_KEY_MODULE_POING.equals(type)) {
                            isPoint = true;
                        }
                        if (isPeriod || isPoint) {
                            try {
                                Date beginTimeDate = ymdhmFormat.parse(beginTime);
                                Date endTimeDate = ymdhmFormat.parse(endTime);
                                if (!(beginTimeDate == null || endTimeDate == null || beginTimeDate.compareTo(endTimeDate) >= 0)) {
                                    long timestamp = beginTimeDate.getTime();
                                    long maxTimestamp = endTimeDate.getTime();
                                    long increaseCount = (long) (intervalInt * 1000);
                                    long baseTime = timestamp;
                                    long increasedTime = baseTime + increaseCount;
                                    if (isPoint) {
                                        increasedTime = baseTime;
                                    }
                                    List<CardDeckComponentCheckBoxItemData> checkBoxItemList = new ArrayList<>();
                                    while (increasedTime <= maxTimestamp) {
                                        String content = null;
                                        if (isPeriod) {
                                            content = String.format("%s-%s", new Object[]{simpleDateFormat.format(new Date(baseTime)), simpleDateFormat.format(new Date(increasedTime))});
                                        } else if (isPoint) {
                                            content = simpleDateFormat.format(new Date(increasedTime));
                                        }
                                        CardDeckComponentCheckBoxItemData item = new CardDeckComponentCheckBoxItemData();
                                        item.setDisplayTitle(content);
                                        item.setRealValue(content);
                                        item.setChild(child);
                                        checkBoxItemList.add(item);
                                        baseTime = increasedTime;
                                        increasedTime = baseTime + increaseCount;
                                    }
                                    return checkBoxItemList;
                                }
                            } catch (Throwable th) {
                                return null;
                            }
                        }
                    }
                } catch (Throwable th2) {
                    return null;
                }
            } catch (Throwable th3) {
                return null;
            }
        }
        return null;
    }
}
