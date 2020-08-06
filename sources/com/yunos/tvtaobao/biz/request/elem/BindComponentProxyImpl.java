package com.yunos.tvtaobao.biz.request.elem;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import com.ali.user.open.ucc.BindComponentProxy;
import com.ali.user.open.ucc.UccCallback;

public class BindComponentProxyImpl implements BindComponentProxy {
    private Activity activity;

    public BindComponentProxyImpl(Activity activity2) {
        this.activity = activity2;
    }

    public void openPage(Context context, Bundle bundle, UccCallback uccCallback) {
        Intent intent = new Intent();
        intent.setFlags(537001984);
        intent.setClassName(this.activity, "com.yunos.tvtaobao.elem.activity.bind.ElemAuthActivity");
        intent.putExtras(bundle);
        this.activity.startActivityForResult(intent, 222);
    }
}
