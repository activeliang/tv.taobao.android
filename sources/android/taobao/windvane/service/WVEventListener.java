package android.taobao.windvane.service;

public interface WVEventListener {
    WVEventResult onEvent(int i, WVEventContext wVEventContext, Object... objArr);
}
