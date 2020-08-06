package com.yunos.tvtaobao.biz.listener;

public interface OnReminderListener {
    public static final byte RET_ERROR = -3;
    public static final byte RET_EXIST = -2;
    public static final byte RET_FULL = -1;
    public static final byte RET_SUCCESS = 0;

    byte addReminder(String str, String str2);

    byte getReminderState();

    long getServerCurrentTime();

    boolean hasReminder(String str);

    void removeReminder(String str);
}
