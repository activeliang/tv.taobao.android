package android.taobao.windvane.connect;

public abstract class HttpConnectListener<T> {
    public abstract void onFinish(T t, int i);

    public void onStart() {
    }

    public void onProcess(int progress) {
    }

    public void onError(int code, String message) {
    }
}
