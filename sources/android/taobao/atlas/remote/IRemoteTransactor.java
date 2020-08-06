package android.taobao.atlas.remote;

import android.os.Bundle;
import java.io.Serializable;

public interface IRemoteTransactor extends Serializable {

    public interface IResponse {
        void OnResponse(Bundle bundle);
    }

    Bundle call(String str, Bundle bundle, IResponse iResponse);

    <T> T getRemoteInterface(Class<T> cls, Bundle bundle);
}
