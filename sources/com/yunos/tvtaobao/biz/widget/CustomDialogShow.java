package com.yunos.tvtaobao.biz.widget;

import android.content.Context;
import com.yunos.tvtaobao.biz.widget.CustomDialog;
import com.yunos.tvtaobao.businessview.R;

public class CustomDialogShow {
    public static CustomDialog customDialog;

    public static void onPromptDialog(String prompt, Context context) {
        customDialog = new CustomDialog.Builder(context).setType(1).setResultMessage(prompt).setStyle(R.style.NoBackgroundRoundRoundCornerDialog).create();
        customDialog.show();
    }
}
