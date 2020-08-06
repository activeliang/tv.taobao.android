package android.taobao.windvane.file;

import java.io.IOException;

public class NotEnoughSpace extends IOException {
    public NotEnoughSpace(String message) {
        super(message);
    }
}
