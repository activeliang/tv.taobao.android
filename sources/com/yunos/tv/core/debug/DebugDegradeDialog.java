package com.yunos.tv.core.debug;

import android.content.Context;
import android.content.DialogInterface;
import android.database.DataSetObserver;
import android.graphics.drawable.Drawable;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.HorizontalScrollView;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.SpinnerAdapter;
import android.widget.TextView;
import android.widget.Toast;
import com.yunos.tv.core.BuildConfig;
import com.yunos.tv.core.R;
import com.yunos.tv.core.common.DeviceJudge;
import com.yunos.tv.core.common.SharePreferences;
import com.yunos.tv.core.degrade.ImageShowDegradeManager;
import com.yunos.tv.core.degrade.PageShowDegradeManager;
import com.yunos.tv.core.degrade.PageStackDegradeManager;
import com.yunos.tv.core.degrade.WidgetDegradeManager;

public class DebugDegradeDialog extends BaseTestDlg {
    ConfigModifyVH configModifyVH;
    Drawable focus;
    HorizontalScrollView horizontalScrollView;
    InfoShowVH infoShowVH;
    LinearLayout linearLayout;
    ViewTreeObserver.OnGlobalFocusChangeListener onGlobalFocusChangeListener = new ViewTreeObserver.OnGlobalFocusChangeListener() {
        public void onGlobalFocusChanged(View oldFocus, View newFocus) {
            DebugDegradeDialog.this.infoShowVH.focusChanged(oldFocus, newFocus);
            DebugDegradeDialog.this.configModifyVH.focusChanged(oldFocus, newFocus);
        }
    };
    Drawable unfocus;

    public DebugDegradeDialog(@NonNull Context context) {
        super(context);
        setContentView(R.layout.debug_degrade_state);
        this.horizontalScrollView = (HorizontalScrollView) findViewById(R.id.degrade_horizontal_scroll_view);
        this.linearLayout = (LinearLayout) findViewById(R.id.degrade_horizontal_linear_layout);
        this.focus = context.getResources().getDrawable(R.drawable.debug_degrade_state_focus);
        this.unfocus = context.getResources().getDrawable(R.drawable.debug_degrade_state_unfocus);
        getWindow().getDecorView().getViewTreeObserver().addOnGlobalFocusChangeListener(this.onGlobalFocusChangeListener);
        setOnDismissListener(new DialogInterface.OnDismissListener() {
            public void onDismiss(DialogInterface dialog) {
                DebugDegradeDialog.this.getWindow().getDecorView().getViewTreeObserver().removeOnGlobalFocusChangeListener(DebugDegradeDialog.this.onGlobalFocusChangeListener);
            }
        });
        this.infoShowVH = new InfoShowVH();
        this.infoShowVH.attach();
        this.configModifyVH = new ConfigModifyVH();
        this.configModifyVH.attach();
    }

    /* access modifiers changed from: private */
    public static boolean isViewGroupHasFocus(ViewGroup vg, View focusView) {
        if (vg == null || focusView == null) {
            return false;
        }
        View v = focusView;
        int loop = 20;
        while (true) {
            loop--;
            if (loop < 0) {
                return false;
            }
            if (v == vg || v.getParent() == vg) {
                return true;
            }
            if (v.getParent() instanceof View) {
                v = (View) v.getParent();
            }
        }
    }

    class InfoShowVH {
        private TextView apkPredefinedDegradeConfig = ((TextView) this.itemView.findViewById(R.id.apk_predefined_degrade_config));
        private TextView deviceState = ((TextView) this.itemView.findViewById(R.id.device_state));
        private TextView imageShowState = ((TextView) this.itemView.findViewById(R.id.image_show_state));
        View itemView;
        private TextView pageShowState = ((TextView) this.itemView.findViewById(R.id.page_show_state));
        private TextView pageStackState = ((TextView) this.itemView.findViewById(R.id.page_stack_state));
        private TextView userDegradeConfig = ((TextView) this.itemView.findViewById(R.id.user_degrade_config));
        private TextView widgetShowState = ((TextView) this.itemView.findViewById(R.id.widget_show_state));
        private TextView zebraDegradeConfig = ((TextView) this.itemView.findViewById(R.id.zebra_degrade_config));

        public InfoShowVH() {
            this.itemView = LayoutInflater.from(DebugDegradeDialog.this.getContext()).inflate(R.layout.debug_runtime_info, DebugDegradeDialog.this.linearLayout, false);
            this.deviceState.setText("设备状态：\n            /**\n             * 设备冒充开关。为了在同一个设备上，模拟不同性能的运行效果\n             *  值定义：\n             *      real：真实设备信息、\n             *      fake_low：冒充低端设备、\n             *      fake_medium：冒充终端设备、\n             *      fake_high：冒充高端设备\n             */\n" + DeviceJudge.getInstance((Context) null).toString());
            this.pageStackState.setText("页面堆栈降级状态：\n            /**\n             * low_memory_page_stack_degrade不为0的时候：开启页面堆栈降级能力，\n             *  关联影响：堆栈维持活动页面减少\n             *  分级划分：\n             *      0 低性能不降级(低性能：4，中性能：9，高性能：16。资源总是有限的，一定的控制和限制是必要的)\n             *      1 低性能盒子上维持3个页面\n             *      2 低性能盒子上维持2个页面\n             */\n" + PageStackDegradeManager.getInstance().toString());
            this.pageShowState.setText("页面显示降级状态：\n            /**\n             * low_memory_page_show_degrade不为0的时候：开启页面显示降级能力，\n             *  关联影响：部分页面显示与正常情况有差异\n             *  分级划分：\n             *      0 默认不降级\n             *      0x01 商品详情页（RecyclerView 替代 BlitzWebView）。\n             *      0x02 原生页面大背景图不展示（大的背景底图用主颜色替代）。\n             */\n" + PageShowDegradeManager.getInstance().toString());
            this.imageShowState.setText("图片相关降级状态：\n            /**\n             * low_memory_img_show_degrade不为0的时候：开启图片展示降级能力，\n             *  受这个开关影响的是：图片质量下降\n             *  分级划分：\n             *      0 默认不降级\n             *      0x01 图片展示优先选取RGB4444格式\n             *      0x02 ImageLoader加载的图片不再缓存在内存中\n             */\n" + ImageShowDegradeManager.getInstance().toString());
            this.widgetShowState.setText("控件相关降级状态：\n            /**\n             * low_memory_widget_degrade不为0的时候：开启控件降级能力，\n             *  受这个开关影响的是：部分控件与正常有差异\n             *  分级划分：\n             *      0 默认不降级\n             *      0x01 RoundImageView降级（内部走低内存实现）\n             */\n" + WidgetDegradeManager.getInstance().toString());
            this.apkPredefinedDegradeConfig.setText("Apk预设降级配置：\n            /**\n             * device_fake = real\n             * low_memory_page_stack_degrade = " + BuildConfig.low_memory_page_stack_degrade + "\n" + "             * low_memory_page_show_degrade = " + BuildConfig.low_memory_page_show_degrade + "\n" + "             * low_memory_img_show_degrade = " + BuildConfig.low_memory_img_show_degrade + "\n" + "             * low_memory_widget_degrade = " + BuildConfig.low_memory_widget_degrade + "\n" + "             */" + "\n");
            this.zebraDegradeConfig.setText("斑马降级配置：\n            /**\n             * low_memory_page_stack_degrade = " + PageStackDegradeManager.getInstance().getSvrCfgValue() + "\n" + "             * low_memory_page_show_degrade = " + PageShowDegradeManager.getInstance().getSvrCfgValue() + "\n" + "             * low_memory_img_show_degrade = " + ImageShowDegradeManager.getInstance().getSvrCfgValue() + "\n" + "             * low_memory_widget_degrade = " + WidgetDegradeManager.getInstance().getSvrCfgValue() + "\n" + "             */" + "\n");
            this.userDegradeConfig.setText("用户降级配置：\n            /**\n             * device_fake = " + DeviceJudge.getUserCfg() + "\n" + "             * low_memory_page_stack_degrade = " + PageStackDegradeManager.getInstance().getUserCfgValue() + "\n" + "             * low_memory_page_show_degrade = " + PageShowDegradeManager.getInstance().getUserCfgValue() + "\n" + "             * low_memory_img_show_degrade = " + ImageShowDegradeManager.getInstance().getUserCfgValue() + "\n" + "             * low_memory_widget_degrade = " + WidgetDegradeManager.getInstance().getUserCfgValue() + "\n" + "             */" + "\n");
        }

        public void attach() {
            DebugDegradeDialog.this.linearLayout.addView(this.itemView);
        }

        public void focusChanged(View oldFocus, View newFocus) {
            if (DebugDegradeDialog.isViewGroupHasFocus((ViewGroup) this.itemView, newFocus)) {
                this.itemView.setBackgroundDrawable(DebugDegradeDialog.this.focus);
            } else {
                this.itemView.setBackgroundDrawable(DebugDegradeDialog.this.unfocus);
            }
        }
    }

    class ConfigModifyVH {
        private Button clear;
        private Spinner deviceFake;
        String deviceFakeVal = null;
        /* access modifiers changed from: private */
        public CheckBox imgShow0x01;
        /* access modifiers changed from: private */
        public CheckBox imgShow0x02;
        /* access modifiers changed from: private */
        public CheckBox imgShowDefault;
        View itemView;
        /* access modifiers changed from: private */
        public CheckBox pageShow0x01;
        /* access modifiers changed from: private */
        public CheckBox pageShow0x02;
        /* access modifiers changed from: private */
        public CheckBox pageShowDefault;
        /* access modifiers changed from: private */
        public CheckBox pageStack0x01;
        /* access modifiers changed from: private */
        public CheckBox pageStack0x02;
        /* access modifiers changed from: private */
        public CheckBox pageStackDefault;
        private Button save;
        /* access modifiers changed from: private */
        public CheckBox widgetShow0x01;
        /* access modifiers changed from: private */
        public CheckBox widgetShowDefault;

        public ConfigModifyVH() {
            this.itemView = LayoutInflater.from(DebugDegradeDialog.this.getContext()).inflate(R.layout.debug_config_modify, DebugDegradeDialog.this.linearLayout, false);
            this.deviceFake = (Spinner) this.itemView.findViewById(R.id.device_fake);
            this.pageStackDefault = (CheckBox) this.itemView.findViewById(R.id.page_stack_default);
            this.pageStack0x01 = (CheckBox) this.itemView.findViewById(R.id.page_stack_0x01);
            this.pageStack0x02 = (CheckBox) this.itemView.findViewById(R.id.page_stack_0x02);
            this.pageShowDefault = (CheckBox) this.itemView.findViewById(R.id.page_show_default);
            this.pageShow0x01 = (CheckBox) this.itemView.findViewById(R.id.page_show_0x01);
            this.pageShow0x02 = (CheckBox) this.itemView.findViewById(R.id.page_show_0x02);
            this.imgShowDefault = (CheckBox) this.itemView.findViewById(R.id.img_show_default);
            this.imgShow0x01 = (CheckBox) this.itemView.findViewById(R.id.img_show_0x01);
            this.imgShow0x02 = (CheckBox) this.itemView.findViewById(R.id.img_show_0x02);
            this.widgetShowDefault = (CheckBox) this.itemView.findViewById(R.id.widget_show_default);
            this.widgetShow0x01 = (CheckBox) this.itemView.findViewById(R.id.widget_show_0x01);
            this.save = (Button) this.itemView.findViewById(R.id.save);
            this.clear = (Button) this.itemView.findViewById(R.id.clear);
            bindDeviceFake();
            bindPageStack();
            bindPageShow();
            bindImageShow();
            bindWidgetSHow();
            this.save.setOnClickListener(new View.OnClickListener(DebugDegradeDialog.this) {
                public void onClick(View v) {
                    int pageStackFlag = PageStackDegradeManager.getInstance().getConfigParser().getFlag();
                    if (ConfigModifyVH.this.pageStackDefault.isChecked()) {
                        pageStackFlag = 0;
                    }
                    if (ConfigModifyVH.this.pageStack0x01.isChecked()) {
                        pageStackFlag = 1;
                    }
                    if (ConfigModifyVH.this.pageStack0x02.isChecked()) {
                        pageStackFlag = 2;
                    }
                    int pageShowFlag = PageShowDegradeManager.getInstance().getConfigParser().getFlag();
                    if (ConfigModifyVH.this.pageShowDefault.isChecked()) {
                        pageShowFlag = 0;
                    }
                    if (ConfigModifyVH.this.pageShow0x01.isChecked()) {
                        pageShowFlag |= 1;
                    }
                    if (ConfigModifyVH.this.pageShow0x02.isChecked()) {
                        pageShowFlag |= 2;
                    }
                    int imgShowFlag = ImageShowDegradeManager.getInstance().getConfigParser().getFlag();
                    if (ConfigModifyVH.this.imgShowDefault.isChecked()) {
                        imgShowFlag = 0;
                    }
                    if (ConfigModifyVH.this.imgShow0x01.isChecked()) {
                        imgShowFlag |= 1;
                    }
                    if (ConfigModifyVH.this.imgShow0x02.isChecked()) {
                        imgShowFlag |= 2;
                    }
                    int widgetShowFlag = WidgetDegradeManager.getInstance().getConfigParser().getFlag();
                    if (ConfigModifyVH.this.widgetShowDefault.isChecked()) {
                        widgetShowFlag = 0;
                    }
                    if (ConfigModifyVH.this.widgetShow0x01.isChecked()) {
                        widgetShowFlag |= 1;
                    }
                    if (!("" + DeviceJudge.getDeviceFake()).equals(ConfigModifyVH.this.deviceFakeVal) && ConfigModifyVH.this.deviceFakeVal != null) {
                        SharePreferences.put("device_fake_by_user", ConfigModifyVH.this.deviceFakeVal);
                    }
                    if (pageStackFlag != PageStackDegradeManager.getInstance().getConfigParser().getFlag()) {
                        SharePreferences.put("low_memory_page_stack_degrade_by_user", pageStackFlag);
                    }
                    if (pageShowFlag != PageShowDegradeManager.getInstance().getConfigParser().getFlag()) {
                        SharePreferences.put("low_memory_page_show_degrade_by_user", pageShowFlag);
                    }
                    if (imgShowFlag != ImageShowDegradeManager.getInstance().getConfigParser().getFlag()) {
                        SharePreferences.put("low_memory_img_show_degrade_by_user", imgShowFlag);
                    }
                    if (widgetShowFlag != WidgetDegradeManager.getInstance().getConfigParser().getFlag()) {
                        SharePreferences.put("low_memory_widget_degrade_by_user", widgetShowFlag);
                    }
                    Toast.makeText(DebugDegradeDialog.this.getContext(), "从首页退出后，重新进入配置生效！", 1).show();
                }
            });
            this.clear.setOnClickListener(new View.OnClickListener(DebugDegradeDialog.this) {
                public void onClick(View v) {
                    SharePreferences.rmv("device_fake_by_user");
                    SharePreferences.rmv("low_memory_page_stack_degrade_by_user");
                    SharePreferences.rmv("low_memory_page_show_degrade_by_user");
                    SharePreferences.rmv("low_memory_img_show_degrade_by_user");
                    SharePreferences.rmv("low_memory_widget_degrade_by_user");
                    Toast.makeText(DebugDegradeDialog.this.getContext(), "从首页退出后，重新进入配置生效！", 1).show();
                }
            });
        }

        private void bindDeviceFake() {
            final String[] deviceFakes = {"(真实设备)real", "(模拟低内存设备)fake_low", "(模拟中内存设备)fake_medium", "(模拟高内存设备)fake_high"};
            this.deviceFake.setAdapter(new SpinnerAdapter() {
                public View getDropDownView(int position, View convertView, ViewGroup parent) {
                    if (convertView != null) {
                        if (convertView instanceof TextView) {
                            ((TextView) convertView).setText(deviceFakes[position]);
                        }
                        return convertView;
                    }
                    TextView textView = new TextView(DebugDegradeDialog.this.getContext());
                    textView.setText(deviceFakes[position]);
                    textView.setTextColor(-16711936);
                    return textView;
                }

                public void registerDataSetObserver(DataSetObserver observer) {
                }

                public void unregisterDataSetObserver(DataSetObserver observer) {
                }

                public int getCount() {
                    return deviceFakes.length;
                }

                public Object getItem(int position) {
                    return null;
                }

                public long getItemId(int position) {
                    return 0;
                }

                public boolean hasStableIds() {
                    return false;
                }

                public View getView(int position, View convertView, ViewGroup parent) {
                    if (convertView != null) {
                        if (convertView instanceof TextView) {
                            ((TextView) convertView).setText(deviceFakes[position]);
                        }
                        return convertView;
                    }
                    TextView textView = new TextView(DebugDegradeDialog.this.getContext());
                    textView.setText(deviceFakes[position]);
                    textView.setTextColor(-16711936);
                    return textView;
                }

                public int getItemViewType(int position) {
                    return 0;
                }

                public int getViewTypeCount() {
                    return 1;
                }

                public boolean isEmpty() {
                    return false;
                }
            });
            this.deviceFake.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                public void onItemSelected(AdapterView<?> adapterView, View view, int position, long id) {
                    if (position == 0) {
                        ConfigModifyVH.this.deviceFakeVal = "real";
                    } else if (position == 1) {
                        ConfigModifyVH.this.deviceFakeVal = DeviceJudge.FAKE_LOW;
                    } else if (position == 2) {
                        ConfigModifyVH.this.deviceFakeVal = DeviceJudge.FAKE_MEDIUM;
                    } else if (position == 3) {
                        ConfigModifyVH.this.deviceFakeVal = DeviceJudge.FAKE_HIGH;
                    }
                }

                public void onNothingSelected(AdapterView<?> adapterView) {
                    ConfigModifyVH.this.deviceFakeVal = null;
                }
            });
            if (DeviceJudge.FAKE_LOW.equals(DeviceJudge.getDeviceFake())) {
                this.deviceFake.setSelection(1);
            } else if (DeviceJudge.FAKE_MEDIUM.equals(DeviceJudge.getDeviceFake())) {
                this.deviceFake.setSelection(2);
            } else if (DeviceJudge.FAKE_HIGH.equals(DeviceJudge.getDeviceFake())) {
                this.deviceFake.setSelection(3);
            } else if ("real".equals(DeviceJudge.getDeviceFake())) {
                this.deviceFake.setSelection(0);
            }
        }

        private void bindPageStack() {
            this.pageStackDefault.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                    if (isChecked) {
                        ConfigModifyVH.this.pageStack0x01.setChecked(false);
                        ConfigModifyVH.this.pageStack0x02.setChecked(false);
                    }
                }
            });
            this.pageStack0x01.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                    if (isChecked) {
                        ConfigModifyVH.this.pageStackDefault.setChecked(false);
                        ConfigModifyVH.this.pageStack0x02.setChecked(false);
                    }
                }
            });
            this.pageStack0x02.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                    if (isChecked) {
                        ConfigModifyVH.this.pageStackDefault.setChecked(false);
                        ConfigModifyVH.this.pageStack0x01.setChecked(false);
                    }
                }
            });
            if (PageStackDegradeManager.getInstance().getConfigParser().isDefault()) {
                this.pageStackDefault.setChecked(true);
            }
            if (PageStackDegradeManager.getInstance().getConfigParser().isLevel1()) {
                this.pageStack0x01.setChecked(true);
            }
            if (PageStackDegradeManager.getInstance().getConfigParser().isLevel2()) {
                this.pageStack0x02.setChecked(true);
            }
        }

        private void bindPageShow() {
            this.pageShowDefault.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                    if (isChecked) {
                        ConfigModifyVH.this.pageShow0x01.setChecked(false);
                        ConfigModifyVH.this.pageShow0x02.setChecked(false);
                    }
                }
            });
            this.pageShow0x01.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                    if (isChecked) {
                        ConfigModifyVH.this.pageShowDefault.setChecked(false);
                    }
                }
            });
            this.pageShow0x02.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                    if (isChecked) {
                        ConfigModifyVH.this.pageShowDefault.setChecked(false);
                    }
                }
            });
            if (PageShowDegradeManager.getInstance().getConfigParser().isDefault()) {
                this.pageShowDefault.setChecked(true);
            }
            if (PageShowDegradeManager.getInstance().getConfigParser().isGoodDetailDegrade()) {
                this.pageShow0x01.setChecked(true);
            }
            if (PageShowDegradeManager.getInstance().getConfigParser().isBigImgBgDegrade()) {
                this.pageShow0x02.setChecked(true);
            }
        }

        private void bindImageShow() {
            this.imgShowDefault.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                    if (isChecked) {
                        ConfigModifyVH.this.imgShow0x01.setChecked(false);
                        ConfigModifyVH.this.imgShow0x02.setChecked(false);
                    }
                }
            });
            this.imgShow0x01.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                    if (isChecked) {
                        ConfigModifyVH.this.imgShowDefault.setChecked(false);
                    }
                }
            });
            this.imgShow0x02.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                    if (isChecked) {
                        ConfigModifyVH.this.imgShowDefault.setChecked(false);
                    }
                }
            });
            if (ImageShowDegradeManager.getInstance().getConfigParser().isDefault()) {
                this.imgShowDefault.setChecked(true);
            }
            if (ImageShowDegradeManager.getInstance().getConfigParser().isImgDegrade()) {
                this.imgShow0x01.setChecked(true);
            }
            if (ImageShowDegradeManager.getInstance().getConfigParser().isImgLoaderDegrade()) {
                this.imgShow0x02.setChecked(true);
            }
        }

        private void bindWidgetSHow() {
            this.widgetShowDefault.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                    if (isChecked) {
                        ConfigModifyVH.this.widgetShow0x01.setChecked(false);
                    }
                }
            });
            this.widgetShow0x01.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                    if (isChecked) {
                        ConfigModifyVH.this.widgetShowDefault.setChecked(false);
                    }
                }
            });
            if (WidgetDegradeManager.getInstance().getConfigParser().isDefault()) {
                this.widgetShowDefault.setChecked(true);
            }
            if (WidgetDegradeManager.getInstance().getConfigParser().isRoundImgViewDegrade()) {
                this.widgetShow0x01.setChecked(true);
            }
        }

        public void attach() {
            DebugDegradeDialog.this.linearLayout.addView(this.itemView);
        }

        public void focusChanged(View oldFocus, View newFocus) {
            if (DebugDegradeDialog.isViewGroupHasFocus((ViewGroup) this.itemView, newFocus)) {
                this.itemView.setBackgroundDrawable(DebugDegradeDialog.this.focus);
            } else {
                this.itemView.setBackgroundDrawable(DebugDegradeDialog.this.unfocus);
            }
        }
    }
}
