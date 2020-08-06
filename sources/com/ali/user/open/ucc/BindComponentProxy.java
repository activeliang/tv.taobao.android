package com.ali.user.open.ucc;

import android.content.Context;
import android.os.Bundle;

public interface BindComponentProxy {
    void openPage(Context context, Bundle bundle, UccCallback uccCallback);
}
