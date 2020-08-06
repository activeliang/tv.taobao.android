package android.taobao.windvane.connect.api;

public interface IApiAdapter {
    String formatBody(ApiRequest apiRequest);

    String formatUrl(ApiRequest apiRequest);
}
