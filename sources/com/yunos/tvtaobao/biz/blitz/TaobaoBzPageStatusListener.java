package com.yunos.tvtaobao.biz.blitz;

public class TaobaoBzPageStatusListener {

    public enum LOAD_MODE {
        URL_MODE(0),
        DATA_MODE(1);
        
        private int value;

        private LOAD_MODE(int num) {
            this.value = num;
        }

        public int getValue() {
            return this.value;
        }
    }
}
