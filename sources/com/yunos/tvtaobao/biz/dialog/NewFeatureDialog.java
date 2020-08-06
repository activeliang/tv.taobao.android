package com.yunos.tvtaobao.biz.dialog;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.WindowManager;
import android.widget.LinearLayout;
import android.widget.TextView;
import com.yunos.tv.core.util.Utils;
import com.yunos.tvtaobao.biz.util.StringUtil;
import com.yunos.tvtaobao.businessview.R;
import com.zhiping.dev.android.logger.ZpLogger;
import java.io.File;

public class NewFeatureDialog extends Dialog {
    private String defaultText;
    private TextView feature1;
    private TextView feature2;
    private TextView feature3;
    private LinearLayout layout1;
    private LinearLayout layout2;
    private LinearLayout layout3;
    private CallBack mCallBack;
    private Context mContext;

    public interface CallBack {
        void onCallBack();
    }

    public NewFeatureDialog(Context context) {
        this(context, R.style.update_top_Dialog);
    }

    public NewFeatureDialog(Context context, String str) {
        this(context, R.style.update_top_Dialog);
        this.defaultText = StringUtil.isEmpty(str) ? "" : str;
    }

    public NewFeatureDialog(Context context, int theme) {
        super(context, theme);
        this.defaultText = "";
        this.mContext = context;
        WindowManager.LayoutParams l = getWindow().getAttributes();
        l.dimAmount = 0.7f;
        getWindow().setWindowAnimations(R.style.bs_up_dialog_animation);
        getWindow().setAttributes(l);
    }

    public void setCallBack(CallBack callBack) {
        this.mCallBack = callBack;
    }

    /* access modifiers changed from: protected */
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.bs_up_update_new_feature_dialog);
        initView();
        LinearLayout[] layouts = {this.layout1, this.layout2, this.layout3};
        TextView[] textViews = {this.feature1, this.feature2, this.feature3};
        String updateInfoText = this.mContext.getSharedPreferences("updateInfo", 0).getString("updateInfo", this.defaultText);
        if (!TextUtils.isEmpty(updateInfoText)) {
            String[] tmp = updateInfoText.split("\n");
            ZpLogger.e("NewFeature", "NewFeature.updateInfoText : " + updateInfoText + " ,tmp.length : " + tmp.length);
            for (int i = 0; i < tmp.length; i++) {
                ZpLogger.e("NewFeature", "NewFeature.tmp : " + tmp[i] + " ,textview : " + textViews[i]);
                layouts[i].setVisibility(0);
                textViews[i].setText(tmp[i]);
            }
        }
    }

    public void show() {
        if ((this.mContext instanceof Activity) && !((Activity) this.mContext).isFinishing()) {
            super.show();
            Utils.utCustomHit("Update_success_Expore", Utils.getProperties());
            deleteInstallAPK();
        }
    }

    public void dismiss() {
        super.dismiss();
        if (this.mCallBack != null) {
            this.mCallBack.onCallBack();
        }
    }

    public boolean dispatchKeyEvent(KeyEvent event) {
        if (event.getAction() == 1 && (event.getKeyCode() == 23 || event.getKeyCode() == 66)) {
            dismiss();
        }
        return super.dispatchKeyEvent(event);
    }

    private void initView() {
        this.layout1 = (LinearLayout) findViewById(R.id.bs_up_new_feature_content1);
        this.layout2 = (LinearLayout) findViewById(R.id.bs_up_new_feature_content2);
        this.layout3 = (LinearLayout) findViewById(R.id.bs_up_new_feature_content3);
        this.feature1 = (TextView) findViewById(R.id.bs_up_fature_text1);
        this.feature2 = (TextView) findViewById(R.id.bs_up_fature_text2);
        this.feature3 = (TextView) findViewById(R.id.bs_up_fature_text3);
    }

    private void deleteInstallAPK() {
        File[] files = new File(String.valueOf(this.mContext.getExternalCacheDir())).listFiles();
        if (files != null) {
            for (File name : files) {
                String name2 = name.getName();
                if (name2.endsWith(".apk")) {
                    File fl = new File(this.mContext.getExternalCacheDir(), name2);
                    if (fl.isFile() && fl.exists()) {
                        fl.delete();
                    }
                }
            }
        }
    }
}
