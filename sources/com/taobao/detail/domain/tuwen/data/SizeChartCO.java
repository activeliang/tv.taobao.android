package com.taobao.detail.domain.tuwen.data;

import java.util.List;

public class SizeChartCO {
    public List<SizeChartData> data;
    public String loopStyle;
    public List<ModelWearCO> modelWears;
    public String picUrl;
    public String title;

    public static class SizeChartData {
        public Integer maxLength;
        public String[] rowData;
        public String tip;
        public String title;
    }
}
