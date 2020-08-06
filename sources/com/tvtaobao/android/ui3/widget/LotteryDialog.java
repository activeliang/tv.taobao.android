package com.tvtaobao.android.ui3.widget;

import android.app.Dialog;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.support.constraint.ConstraintLayout;
import android.text.SpannableString;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Space;
import android.widget.TextView;
import com.tvtaobao.android.ui3.R;

public class LotteryDialog extends Dialog {
    BgColorCfg bgColorCfg;
    private ConstraintLayout constraintLayout;
    /* access modifiers changed from: private */
    public ImageView lotteryActionBtn;
    LotteryActionBtnCfg lotteryActionBtnCfg;
    private Space lotteryActionBtnTopAnchor;
    private ImageView lotteryAura;
    LotteryAuraImgCfg lotteryAuraImgCfg;
    private TextView lotteryExtDesc;
    LotteryExtDescCfg lotteryExtDescCfg;
    private ImageView lotteryImg;
    LotteryImgCfg lotteryImgCfg;
    private TextView lotteryMessage1;
    private TextView lotteryMessage2;
    private TextView lotteryMessage3;
    private LinearLayout lotteryMessages;
    LotteryMessagesCfg lotteryMessagesCfg;

    public interface BgColorCfg {
        int getCfgColor();
    }

    public interface LotteryActionBtnCfg {
        Drawable getFocusImg();

        int getHeight();

        int getMarginTop();

        Drawable getNormalImg();

        int getWidth();
    }

    public interface LotteryAuraImgCfg {
        int getHeight();

        Drawable getLotteryAuraImg();

        int getMarginTop();

        int getWidth();
    }

    public interface LotteryExtDescCfg {
        SpannableString getLotteryExtDesc();

        int getMarginTop();
    }

    public interface LotteryImgCfg {
        int getHeight();

        Drawable getLotteryImg();

        int getMarginBottom();

        int getWidth();
    }

    public interface LotteryMessagesCfg {
        SpannableString[] getLotteryMessages();

        int getMarginTop();
    }

    public LotteryDialog(Context context) {
        super(context, R.style.ui3wares_dialogC);
        setContentView(R.layout.ui3wares_layout_lottery_dialog);
        findViews();
        getWindow().setLayout(-1, -1);
    }

    private void findViews() {
        this.constraintLayout = (ConstraintLayout) findViewById(R.id.constraint_layout);
        this.lotteryAura = (ImageView) findViewById(R.id.lottery_aura);
        this.lotteryImg = (ImageView) findViewById(R.id.lottery_img);
        this.lotteryMessages = (LinearLayout) findViewById(R.id.lottery_messages);
        this.lotteryMessage1 = (TextView) findViewById(R.id.lottery_message_1);
        this.lotteryMessage2 = (TextView) findViewById(R.id.lottery_message_2);
        this.lotteryMessage3 = (TextView) findViewById(R.id.lottery_message_3);
        this.lotteryActionBtnTopAnchor = (Space) findViewById(R.id.lottery_action_btn_top_anchor);
        this.lotteryActionBtn = (ImageView) findViewById(R.id.lottery_action_btn);
        this.lotteryExtDesc = (TextView) findViewById(R.id.lottery_ext_desc);
    }

    public ConstraintLayout getConstraintLayout() {
        return this.constraintLayout;
    }

    public ImageView getLotteryAura() {
        return this.lotteryAura;
    }

    public ImageView getLotteryImg() {
        return this.lotteryImg;
    }

    public LinearLayout getLotteryMessages() {
        return this.lotteryMessages;
    }

    public TextView getLotteryMessage1() {
        return this.lotteryMessage1;
    }

    public TextView getLotteryMessage2() {
        return this.lotteryMessage2;
    }

    public TextView getLotteryMessage3() {
        return this.lotteryMessage3;
    }

    public Space getLotteryActionBtnTopAnchor() {
        return this.lotteryActionBtnTopAnchor;
    }

    public ImageView getLotteryActionBtn() {
        return this.lotteryActionBtn;
    }

    public TextView getLotteryExtDesc() {
        return this.lotteryExtDesc;
    }

    public <T> LotteryDialog doCfg(CfgKey cfgKey, T t) {
        if (cfgKey.cfgCls.isInstance(t)) {
            if (cfgKey == CfgKey.bgColor) {
                this.bgColorCfg = (BgColorCfg) t;
            } else if (cfgKey == CfgKey.lotteryAuraImg) {
                this.lotteryAuraImgCfg = (LotteryAuraImgCfg) t;
            } else if (cfgKey == CfgKey.lotteryImg) {
                this.lotteryImgCfg = (LotteryImgCfg) t;
            } else if (cfgKey == CfgKey.lotteryActionBtn) {
                this.lotteryActionBtnCfg = (LotteryActionBtnCfg) t;
            } else if (cfgKey == CfgKey.lotteryExtDesc) {
                this.lotteryExtDescCfg = (LotteryExtDescCfg) t;
            } else if (cfgKey == CfgKey.lotteryMessages) {
                this.lotteryMessagesCfg = (LotteryMessagesCfg) t;
            }
            return this;
        }
        throw new RuntimeException("type not fit !");
    }

    public LotteryDialog applyCfg() {
        if (this.bgColorCfg != null) {
            this.constraintLayout.setBackgroundColor(this.bgColorCfg.getCfgColor());
        }
        if (this.lotteryAuraImgCfg != null) {
            this.lotteryAura.setImageDrawable(this.lotteryAuraImgCfg.getLotteryAuraImg());
            ConstraintLayout.LayoutParams lp = (ConstraintLayout.LayoutParams) this.lotteryAura.getLayoutParams();
            lp.setMargins(0, this.lotteryAuraImgCfg.getMarginTop(), 0, 0);
            lp.width = this.lotteryAuraImgCfg.getWidth();
            lp.height = this.lotteryAuraImgCfg.getHeight();
            this.lotteryAura.setLayoutParams(lp);
        }
        if (this.lotteryImgCfg != null) {
            this.lotteryImg.setImageDrawable(this.lotteryImgCfg.getLotteryImg());
            ConstraintLayout.LayoutParams lp2 = (ConstraintLayout.LayoutParams) this.lotteryImg.getLayoutParams();
            lp2.setMargins(0, 0, 0, this.lotteryImgCfg.getMarginBottom());
            lp2.width = this.lotteryImgCfg.getWidth();
            lp2.height = this.lotteryImgCfg.getHeight();
            this.lotteryImg.setLayoutParams(lp2);
        }
        if (this.lotteryActionBtnCfg != null) {
            if (this.lotteryActionBtn.hasFocus()) {
                this.lotteryActionBtn.setImageDrawable(this.lotteryActionBtnCfg.getFocusImg());
            } else {
                this.lotteryActionBtn.setImageDrawable(this.lotteryActionBtnCfg.getNormalImg());
            }
            this.lotteryActionBtn.setOnFocusChangeListener(new View.OnFocusChangeListener() {
                public void onFocusChange(View v, boolean hasFocus) {
                    if (hasFocus) {
                        LotteryDialog.this.lotteryActionBtn.setImageDrawable(LotteryDialog.this.lotteryActionBtnCfg.getFocusImg());
                    } else {
                        LotteryDialog.this.lotteryActionBtn.setImageDrawable(LotteryDialog.this.lotteryActionBtnCfg.getNormalImg());
                    }
                }
            });
            ConstraintLayout.LayoutParams lp3 = (ConstraintLayout.LayoutParams) this.lotteryActionBtn.getLayoutParams();
            lp3.setMargins(0, this.lotteryActionBtnCfg.getMarginTop(), 0, 0);
            lp3.width = this.lotteryActionBtnCfg.getWidth();
            lp3.height = this.lotteryActionBtnCfg.getHeight();
            this.lotteryActionBtn.setLayoutParams(lp3);
        }
        if (this.lotteryExtDescCfg != null) {
            this.lotteryExtDesc.setText(this.lotteryExtDescCfg.getLotteryExtDesc());
            ConstraintLayout.LayoutParams lp4 = (ConstraintLayout.LayoutParams) this.lotteryExtDesc.getLayoutParams();
            lp4.setMargins(0, this.lotteryExtDescCfg.getMarginTop(), 0, 0);
            this.lotteryExtDesc.setLayoutParams(lp4);
        }
        if (this.lotteryMessagesCfg != null) {
            SpannableString[] messages = this.lotteryMessagesCfg.getLotteryMessages();
            if (messages != null) {
                if (messages.length > 0) {
                    this.lotteryMessage1.setText(messages[0]);
                }
                if (messages.length > 1) {
                    this.lotteryMessage2.setText(messages[1]);
                }
                if (messages.length > 2) {
                    this.lotteryMessage3.setText(messages[2]);
                }
            }
            ConstraintLayout.LayoutParams lp5 = (ConstraintLayout.LayoutParams) this.lotteryMessages.getLayoutParams();
            lp5.setMargins(0, this.lotteryMessagesCfg.getMarginTop(), 0, 0);
            this.lotteryMessages.setLayoutParams(lp5);
        }
        return this;
    }

    public enum CfgKey {
        bgColor(BgColorCfg.class),
        lotteryAuraImg(LotteryAuraImgCfg.class),
        lotteryImg(LotteryImgCfg.class),
        lotteryActionBtn(LotteryActionBtnCfg.class),
        lotteryExtDesc(LotteryExtDescCfg.class),
        lotteryMessages(LotteryMessagesCfg.class);
        
        /* access modifiers changed from: private */
        public Class cfgCls;

        private CfgKey(Class cfgCls2) {
            this.cfgCls = cfgCls2;
        }
    }

    private static class BaseCfg {
        Context context;

        public BaseCfg(Context context2) {
            this.context = context2;
        }
    }

    public static class DefaultBgColorCfg extends BaseCfg implements BgColorCfg {
        public DefaultBgColorCfg(Context context) {
            super(context);
        }

        public int getCfgColor() {
            return Color.parseColor("#d9000000");
        }
    }

    public static class DefaultLotteryAuraImgCfg extends BaseCfg implements LotteryAuraImgCfg {
        public DefaultLotteryAuraImgCfg(Context context) {
            super(context);
        }

        public Drawable getLotteryAuraImg() {
            return new ColorDrawable(Color.parseColor("#80d8d8d8"));
        }

        public int getMarginTop() {
            return this.context.getResources().getDimensionPixelSize(R.dimen.values_dp_0);
        }

        public int getWidth() {
            return this.context.getResources().getDimensionPixelSize(R.dimen.values_dp_640);
        }

        public int getHeight() {
            return this.context.getResources().getDimensionPixelSize(R.dimen.values_dp_544);
        }
    }

    public static class DefaultLotteryImgCfg extends BaseCfg implements LotteryImgCfg {
        public DefaultLotteryImgCfg(Context context) {
            super(context);
        }

        public Drawable getLotteryImg() {
            return new ColorDrawable(Color.parseColor("#80de00ff"));
        }

        public int getMarginBottom() {
            return this.context.getResources().getDimensionPixelSize(R.dimen.values_dp_42);
        }

        public int getWidth() {
            return this.context.getResources().getDimensionPixelSize(R.dimen.values_dp_192);
        }

        public int getHeight() {
            return this.context.getResources().getDimensionPixelSize(R.dimen.values_dp_160);
        }
    }

    public static class DefaultLotteryActionBtnCfg extends BaseCfg implements LotteryActionBtnCfg {
        public DefaultLotteryActionBtnCfg(Context context) {
            super(context);
        }

        public Drawable getFocusImg() {
            return new ColorDrawable(-16711936);
        }

        public Drawable getNormalImg() {
            return new ColorDrawable(-7829368);
        }

        public int getMarginTop() {
            return this.context.getResources().getDimensionPixelSize(R.dimen.values_dp_0);
        }

        public int getWidth() {
            return this.context.getResources().getDimensionPixelSize(R.dimen.values_dp_248);
        }

        public int getHeight() {
            return this.context.getResources().getDimensionPixelSize(R.dimen.values_dp_96);
        }
    }

    public static class DefaultLotteryExtDescCfg extends BaseCfg implements LotteryExtDescCfg {
        public DefaultLotteryExtDescCfg(Context context) {
            super(context);
        }

        public SpannableString getLotteryExtDesc() {
            return new SpannableString("");
        }

        public int getMarginTop() {
            return this.context.getResources().getDimensionPixelSize(R.dimen.values_dp_16);
        }
    }

    public static class DefaultLotteryMessagesCfg extends BaseCfg implements LotteryMessagesCfg {
        public DefaultLotteryMessagesCfg(Context context) {
            super(context);
        }

        public SpannableString[] getLotteryMessages() {
            return new SpannableString[0];
        }

        public int getMarginTop() {
            return this.context.getResources().getDimensionPixelSize(R.dimen.values_dp_184);
        }
    }
}
