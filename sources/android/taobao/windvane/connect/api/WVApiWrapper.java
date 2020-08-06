package android.taobao.windvane.connect.api;

public class WVApiWrapper {
    public static String formatUrl(ApiRequest request, Class<? extends IApiAdapter> cls) {
        if (!(request == null || cls == null)) {
            try {
                return ((IApiAdapter) cls.newInstance()).formatUrl(request);
            } catch (InstantiationException e) {
                e.printStackTrace();
            } catch (IllegalAccessException e2) {
                e2.printStackTrace();
            }
        }
        return "";
    }

    public static String formatBody(ApiRequest request, Class<? extends IApiAdapter> cls) {
        if (!(request == null || cls == null)) {
            try {
                return ((IApiAdapter) cls.newInstance()).formatBody(request);
            } catch (InstantiationException e) {
                e.printStackTrace();
            } catch (IllegalAccessException e2) {
                e2.printStackTrace();
            }
        }
        return "";
    }
}
