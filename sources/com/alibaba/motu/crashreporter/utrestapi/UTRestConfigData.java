package com.alibaba.motu.crashreporter.utrestapi;

public class UTRestConfigData {
    public static final String G_FIXED_SIGNED_POST_URL = "http://adash.m.taobao.com/rest/ur?ak=21534429&av=1.0&c=Statistic%40taobao.1.0.6&v=1.0&s=1019ae9f470b752f9c2a15f19fb60609&d=U%2Bi7frcx68MDAIO%2BguY5JBVk&sv=2.0.5_m3&p=iPhone%20OS&t=1408074775&u=&type=ch4a_rest";
    private static UTRestConfigData s_instance = new UTRestConfigData();

    private UTRestConfigData() {
    }

    public static UTRestConfigData getInstance() {
        return s_instance;
    }
}
