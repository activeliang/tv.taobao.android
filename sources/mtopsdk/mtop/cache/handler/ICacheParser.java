package mtopsdk.mtop.cache.handler;

import android.os.Handler;
import mtopsdk.mtop.domain.ResponseSource;

public interface ICacheParser {
    void parse(ResponseSource responseSource, Handler handler);
}
