package com.yunos.tv.core.debug;

import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import anetwork.channel.util.RequestConstant;
import com.yunos.tv.core.R;
import com.yunos.tv.core.common.SharePreferences;
import com.yunos.tv.core.config.RunMode;
import com.yunos.tv.core.disaster_tolerance.DisasterTolerance;
import com.zhiping.dev.android.logger.ZpLogger;

public class DebugTestDialog extends BaseTestDlg {
    /* access modifiers changed from: private */
    public Action action;
    private Button clickBtn;
    private View.OnClickListener clickListener = new View.OnClickListener() {
        public void onClick(View v) {
            RunMode runMode;
            if (DebugTestDialog.this.etName != null && !TextUtils.isEmpty(DebugTestDialog.this.etName.getText().toString())) {
                if (DebugTestDialog.this.action == Action.SWITCH_CHANEL) {
                    SharePreferences.put("device_appkey", DebugTestDialog.this.etName.getText().toString());
                    DebugTestDialog.this.dismiss();
                } else if (DebugTestDialog.this.action == Action.SWITCH_ENV) {
                    String val = DebugTestDialog.this.etName.getText().toString();
                    if ("0".equals(val)) {
                        runMode = RunMode.DAILY;
                    } else if ("1".equals(val)) {
                        runMode = RunMode.PREDEPLOY;
                    } else {
                        runMode = RunMode.PRODUCTION;
                    }
                    SharePreferences.put("device_env", runMode.toString());
                    DebugTestDialog.this.dismiss();
                } else if (DebugTestDialog.this.action == Action.DisasterTolerance) {
                    if ("1".equals(DebugTestDialog.this.etName.getText().toString())) {
                        new Handler(Looper.getMainLooper()).post(new Runnable() {
                            public void run() {
                                throw new NullPointerException("NullPointerException");
                            }
                        });
                    } else if ("2".equals(DebugTestDialog.this.etName.getText().toString())) {
                        new Handler(Looper.getMainLooper()).post(new Runnable() {
                            public void run() {
                                throw new ClassCastException("ClassCastException");
                            }
                        });
                    } else if ("3".equals(DebugTestDialog.this.etName.getText().toString())) {
                        new Handler(Looper.getMainLooper()).post(new Runnable() {
                            public void run() {
                                DisasterTolerance.getInstance().catchRequestDoneException(new Throwable(RequestConstant.ENV_TEST), new String[0]);
                            }
                        });
                    } else {
                        new Handler(Looper.getMainLooper()).post(new Runnable() {
                            public void run() {
                                throw new NumberFormatException("NumberFormatException");
                            }
                        });
                    }
                    DebugTestDialog.this.dismiss();
                } else if (DebugTestDialog.this.action == Action.utNow) {
                    String txt = DebugTestDialog.this.etName.getText().toString();
                    if (!TextUtils.isEmpty(txt)) {
                        if ("shutdown".equals(txt)) {
                            SharePreferences.rmv("debug_ut_immediately");
                        } else {
                            SharePreferences.put("debug_ut_immediately", txt);
                        }
                        DebugTestDialog.this.dismiss();
                    }
                }
            }
        }
    };
    private TextView desc;
    /* access modifiers changed from: private */
    public EditText etName;

    public enum Action {
        SWITCH_ENV,
        SWITCH_CHANEL,
        DisasterTolerance,
        utNow
    }

    public DebugTestDialog(Context context, Action action2) {
        super(context);
        this.action = action2;
    }

    /* access modifiers changed from: protected */
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ZpLogger.d("DebugTestDialog", "onCreate ++++++");
        setContentView(R.layout.debug_dialog);
        this.desc = (TextView) findViewById(R.id.debug_cmd_desc);
        this.etName = (EditText) findViewById(R.id.debug_editText);
        this.etName.requestFocus();
        this.clickBtn = (Button) findViewById(R.id.debug_button);
        this.clickBtn.setOnClickListener(this.clickListener);
        if (this.action == Action.DisasterTolerance) {
            this.desc.setText("测试容灾能力:\n\t1,空指针容灾;\n\t2,类型转换异常容灾;\n\t3,主线程大循环指定容灾;\n\t其他,不在容灾列表中的异常，不会捕获");
        } else if (this.action == Action.SWITCH_CHANEL) {
            this.desc.setText("切换渠道:(下次进入有效)\n\t请填入渠道号");
        } else if (this.action == Action.SWITCH_ENV) {
            this.desc.setText("切换环境:(下次进入有效)\n\t0,日常环境;\n\t1,预发环境;\n\t2,线上环境;");
        } else if (this.action == Action.utNow) {
            this.desc.setText("配置修改:\n\t1、\"任何字符串\",作为DebugKey打开UT立即上报(未关闭之前每次进入打点都有Toast);\n\t2、shutdown,关闭UT打点事实上报（重启后配置生效）\n");
        }
    }
}
