package com.ali.auth.third.offline.widget;

import android.content.Context;
import android.view.View;
import android.widget.Toast;
import com.ali.auth.third.core.util.NetworkUtils;
import com.ali.auth.third.offline.R;
import com.ali.auth.third.offline.login.util.ActivityUIHelper;

public abstract class NetworkCheckOnClickListener implements View.OnClickListener {
    ActivityUIHelper mActivityHelper;

    public abstract void afterCheck(View view);

    public NetworkCheckOnClickListener(ActivityUIHelper helper) {
        this.mActivityHelper = helper;
    }

    public void onClick(View v) {
        if (NetworkUtils.isNetworkAvaiable(v.getContext())) {
            afterCheck(v);
        } else if (this.mActivityHelper != null) {
            this.mActivityHelper.toast(v.getContext().getString(R.string.aliusersdk_scan_network_error), 0);
        } else {
            toastNetworkError(v.getContext());
        }
    }

    public static void toastNetworkError(Context context) {
        String message = context.getString(R.string.aliusersdk_scan_network_error);
        Toast toast = Toast.makeText(context, message, 0);
        toast.setGravity(17, 0, 0);
        toast.setText(message);
        toast.show();
    }
}
