package com.yunos.tv.core.debug;

import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import com.bftv.fui.constantplugin.Constant;
import com.yunos.tv.core.R;
import com.yunos.tv.core.RtEnv;
import com.zhiping.dev.android.logger.ZpLogger;
import java.util.Map;
import mtopsdk.common.util.SymbolExpUtil;

public class DebugConsoleDialog extends BaseTestDlg {
    private LinearLayout container;
    int dp24;
    int dp48;

    public DebugConsoleDialog(Context context) {
        super(context);
    }

    /* access modifiers changed from: protected */
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ZpLogger.d("DebugConsoleDialog", "onCreate ++++++");
        setContentView(R.layout.debug_console_dialog);
        this.container = (LinearLayout) findViewById(R.id.container);
        this.dp48 = getContext().getResources().getDimensionPixelSize(R.dimen.values_dp_48);
        this.dp24 = this.dp48 / 2;
        Map.Entry<String, String>[] cfgItems = RtEnv.cfgList();
        if (cfgItems != null && cfgItems.length > 0) {
            for (int i = 0; i < cfgItems.length; i++) {
                this.container.addView(mkPair(cfgItems[i].getKey(), cfgItems[i].getValue()), mkLPStyleB());
            }
        }
    }

    private TextView mkTV() {
        TextView tv2 = new TextView(getContext());
        tv2.setTextColor(-16711936);
        tv2.setTextSize((float) this.dp24);
        tv2.setBackgroundDrawable(getContext().getResources().getDrawable(R.drawable.debug_bg_slector));
        return tv2;
    }

    private EditText mkET() {
        EditText tv2 = new EditText(getContext());
        tv2.setTextColor(-16711936);
        tv2.setTextSize((float) this.dp24);
        tv2.setBackgroundDrawable(getContext().getResources().getDrawable(R.drawable.debug_bg_slector));
        return tv2;
    }

    private LinearLayout.LayoutParams mkLPStyleA() {
        LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(0, -2);
        lp.weight = 1.0f;
        return lp;
    }

    private LinearLayout.LayoutParams mkLPStyleB() {
        LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(-2, -2);
        lp.setMargins(0, 5, 0, 5);
        return lp;
    }

    /* access modifiers changed from: private */
    public LinearLayout mkPair(String key, String val) {
        final LinearLayout linearLayout = new LinearLayout(getContext());
        linearLayout.setOrientation(1);
        linearLayout.setGravity(16);
        final EditText etKey = mkET();
        etKey.setText(key);
        linearLayout.addView(etKey, mkLPStyleA());
        TextView tv2 = mkTV();
        tv2.setText(SymbolExpUtil.SYMBOL_COLON);
        linearLayout.addView(tv2);
        final EditText etVal = mkET();
        etKey.setText(val);
        linearLayout.addView(etVal, mkLPStyleA());
        TextView deleteBtn = mkTV();
        deleteBtn.setFocusable(true);
        deleteBtn.setText(Constant.DELETE);
        linearLayout.addView(deleteBtn, mkLPStyleB());
        deleteBtn.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                RtEnv.rmv(etKey.getText().toString());
                if (linearLayout.getParent() instanceof ViewGroup) {
                    ((ViewGroup) linearLayout.getParent()).removeView(linearLayout);
                }
            }
        });
        TextView addBtn = mkTV();
        addBtn.setFocusable(true);
        addBtn.setText("新增");
        linearLayout.addView(addBtn, mkLPStyleB());
        addBtn.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                if (linearLayout.getParent() instanceof ViewGroup) {
                    ((ViewGroup) linearLayout.getParent()).addView(DebugConsoleDialog.this.mkPair("", ""));
                }
            }
        });
        TextView saveBtn = mkTV();
        saveBtn.setFocusable(true);
        saveBtn.setText("保存");
        linearLayout.addView(saveBtn, mkLPStyleB());
        saveBtn.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                RtEnv.set(etKey.getText().toString(), etVal.getText().toString());
            }
        });
        return linearLayout;
    }
}
