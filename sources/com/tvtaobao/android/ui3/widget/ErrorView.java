package com.tvtaobao.android.ui3.widget;

import android.content.Context;
import android.content.res.TypedArray;
import android.os.CountDownTimer;
import android.support.constraint.ConstraintLayout;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import com.tvtaobao.android.ui3.R;

public class ErrorView extends ConstraintLayout {
    private ImageView ivIcon;
    private LinearLayout llError;
    private CountDownTimer mTimer;
    /* access modifiers changed from: private */
    public OnBackListener onBackListener;
    /* access modifiers changed from: private */
    public OnCountDownTimerFinishListener onCountDownTimerFinishListener;
    private RelativeLayout rlNetwork;
    private RelativeLayout rlView;
    /* access modifiers changed from: private */
    public TextView tvBack;
    private TextView tvInfoMajor;
    private TextView tvInfoMinor;
    private TextView tvInfoNewwork;
    private ErrViewType type;

    public interface OnBackListener {
        void onBack();
    }

    public interface OnCountDownTimerFinishListener {
        void onFinish();
    }

    public ErrorView(Context context) {
        this(context, (AttributeSet) null);
    }

    public ErrorView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public ErrorView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        this.type = ErrViewType.NETWORK_ERR;
        LayoutInflater.from(getContext()).inflate(R.layout.ui3wares_layout_error_view, this, true);
        this.rlView = (RelativeLayout) findViewById(R.id.rl_view);
        this.tvInfoMajor = (TextView) findViewById(R.id.tv_info_major);
        this.ivIcon = (ImageView) findViewById(R.id.iv_icon);
        this.tvInfoMinor = (TextView) findViewById(R.id.tv_info_minor);
        this.tvBack = (TextView) findViewById(R.id.tv_back);
        this.llError = (LinearLayout) findViewById(R.id.ll_error);
        this.rlNetwork = (RelativeLayout) findViewById(R.id.rl_network);
        this.tvInfoNewwork = (TextView) findViewById(R.id.tv_info_network);
        this.tvBack.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                if (ErrorView.this.onBackListener != null) {
                    ErrorView.this.onBackListener.onBack();
                }
            }
        });
        TypedArray ta = context.obtainStyledAttributes(attrs, R.styleable.ui3wares_ErrorView);
        if (ta != null && context != null) {
            String majorText = ta.getString(R.styleable.ui3wares_ErrorView_ui3wares_evMajorText);
            String minorText = ta.getString(R.styleable.ui3wares_ErrorView_ui3wares_evMinorText);
            int errorIcon = ta.getResourceId(R.styleable.ui3wares_ErrorView_ui3wares_errorSrc, R.drawable.ui3wares_error_newwork);
            int type2 = ta.getInt(R.styleable.ui3wares_ErrorView_ui3wares_type, ErrViewType.NETWORK_ERR.type);
            String backMessage = ta.getString(R.styleable.ui3wares_ErrorView_ui3wares_backText);
            int countDown = ta.getInt(R.styleable.ui3wares_ErrorView_ui3wares_backCountDown, 0);
            String infoNetwork = ta.getString(R.styleable.ui3wares_ErrorView_ui3wares_infoNetwork);
            setType(ErrViewType.from(type2));
            setTvInfoMajor(majorText);
            setTvInfoMinor(minorText);
            setIcon(errorIcon);
            setTvInfoNetwork(infoNetwork);
            setTvBackMessage(backMessage, countDown);
            if (ta != null) {
                ta.recycle();
            }
        }
    }

    public ErrViewType getType() {
        return this.type;
    }

    public void setType(ErrViewType type2) {
        if (type2 != null) {
            this.type = type2;
        }
        if (type2 == ErrViewType.WITH_BUTTON) {
            this.llError.setVisibility(0);
            this.rlNetwork.setVisibility(8);
        } else if (type2 == ErrViewType.NETWORK_ERR) {
            this.llError.setVisibility(8);
            this.rlNetwork.setVisibility(0);
        }
    }

    public void setTvInfoMajor(String tvInfoMajorText) {
        if (this.tvInfoMajor != null) {
            this.tvInfoMajor.setText(tvInfoMajorText);
        }
    }

    public void setTvInfoMinor(String tvInfoMinorText) {
        if (this.tvInfoMinor != null) {
            this.tvInfoMinor.setText(tvInfoMinorText);
        }
    }

    public void setIcon(int iconResId) {
        if (this.ivIcon != null) {
            this.ivIcon.setImageResource(iconResId);
        }
    }

    public void setTvInfoNetwork(String tvInfoNewworkText) {
        if (this.tvInfoNewwork != null) {
            this.tvInfoNewwork.setText(tvInfoNewworkText);
        }
    }

    public void setTvBackMessage(String backMessage, int countdownSeconds) {
        if (this.tvBack != null && this.type == ErrViewType.WITH_BUTTON) {
            if (countdownSeconds > 0) {
                this.tvBack.setText(backMessage + "（" + countdownSeconds + "）");
                final String str = backMessage;
                this.mTimer = new CountDownTimer((long) (countdownSeconds * 1000), 1000) {
                    public void onTick(long millisUntilFinished) {
                        int remainTime = (int) (millisUntilFinished / 1000);
                        if (!TextUtils.isEmpty(str)) {
                            ErrorView.this.tvBack.setText(str + "（" + remainTime + "）");
                        } else {
                            ErrorView.this.tvBack.setText("（" + remainTime + "）");
                        }
                    }

                    public void onFinish() {
                        if (ErrorView.this.onCountDownTimerFinishListener != null) {
                            ErrorView.this.onCountDownTimerFinishListener.onFinish();
                        }
                    }
                };
                return;
            }
            this.tvBack.setText(backMessage);
        }
    }

    public void setOnBackListener(OnBackListener onBackListener2) {
        this.onBackListener = onBackListener2;
    }

    public void setOnCountDownTimerFinishListener(OnCountDownTimerFinishListener onCountDownTimerFinishListener2) {
        this.onCountDownTimerFinishListener = onCountDownTimerFinishListener2;
    }

    public void startCountDown() {
        if (this.mTimer != null) {
            this.mTimer.start();
        }
    }

    public void setErrorViewBackground(int resid) {
        this.rlView.setBackgroundResource(resid);
    }

    public enum ErrViewType {
        WITH_BUTTON(1),
        NETWORK_ERR(2);
        
        /* access modifiers changed from: private */
        public int type;

        private ErrViewType(int type2) {
            this.type = type2;
        }

        public static ErrViewType from(int val) {
            if (val == 1) {
                return WITH_BUTTON;
            }
            if (val == 2) {
                return NETWORK_ERR;
            }
            return null;
        }
    }
}
