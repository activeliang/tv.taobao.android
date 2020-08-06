package com.taobao.detail.domain.meal;

import com.taobao.detail.clientDomain.TBDetailResultVO;
import com.taobao.detail.domain.base.BaseVO;
import java.io.Serializable;
import java.util.List;

public class ComboInfo extends BaseVO implements Serializable {
    public List<SimpleCombo> combos;
    public Combo currentCombo;

    public static class Combo implements Serializable {
        public boolean areaSell;
        public String comboDescription;
        public String comboH5Url;
        public String id;
        public List<TBDetailResultVO> itemsForApp;
        public String name;
        @Deprecated
        public String reservePrice;
        public String reservePriceForTaobaoApp;
        public String tmalAppH5Url;
        public int type;
    }

    public static class SimpleCombo implements Serializable {
        public String comboDescription;
        public String id;
        public String name;
    }
}
