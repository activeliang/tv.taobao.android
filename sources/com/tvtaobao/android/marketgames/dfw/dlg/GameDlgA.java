package com.tvtaobao.android.marketgames.dfw.dlg;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.support.constraint.ConstraintLayout;
import android.text.SpannableString;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.Space;
import android.widget.TextView;
import com.tvtaobao.android.marketgames.R;
import com.tvtaobao.android.marketgames.dfw.act.GameDaFuWengActivity;
import com.tvtaobao.android.marketgames.dfw.wares.IImageLoader;
import com.tvtaobao.android.ui3.widget.FullScreenDialog;
import com.tvtaobao.android.ui3.widget.SimpleCardView;

public class GameDlgA extends FullScreenDialog {
    private ImageView aura;
    private ImageView awardImg;
    /* access modifiers changed from: private */
    public TextView btnLeft;
    /* access modifiers changed from: private */
    public TextView btnRight;
    /* access modifiers changed from: private */
    public TextView btnSingle;
    private ConstraintLayout constraintLayout;
    /* access modifiers changed from: private */
    public IDataProvider dataProvider;
    private TextView desc;
    private DlgStyle dlgStyle;
    /* access modifiers changed from: private */
    public IEventListener eventListener;
    private TextView ext;
    private ImageView goodImg;
    private ImageView goodInfoBg;
    private TextView goodName;
    private TextView goodPrice;
    private SimpleCardView guessLikeGoodItem;
    /* access modifiers changed from: private */
    public IImageLoader imageLoader;
    private TextView payCount;
    private Space pos1;
    private Space pos2;
    private Space pos3;
    private Space pos4;
    private TextView tip;
    private TextView title;

    public enum DlgStyle {
        lucky_award,
        special_award,
        lucky_no_award,
        lucky_guess_like_good,
        before_user_leave,
        unsafe_user
    }

    public interface IDataProvider {
        String getAuraImgUrl();

        String getAwardImgUrl();

        String getBgColor();

        String getBtnLeftFocusImgUrl();

        String getBtnLeftNormalImgUrl();

        String getBtnLeftTxt();

        String getBtnLeftTxtFocusColor();

        String getBtnLeftTxtUnFocusColor();

        String getBtnRightFocusImgUrl();

        String getBtnRightNormalImgUrl();

        String getBtnRightTxt();

        String getBtnRightTxtFocusColor();

        String getBtnRightTxtUnFocusColor();

        String getBtnSingleFocusImgUrl();

        String getBtnSingleNormalImgUrl();

        String getBtnSingleTxt();

        String getBtnSingleTxtFocusColor();

        String getBtnSingleTxtUnFocusColor();

        String getDesc();

        String getDescColor();

        SpannableString getExt();

        String getExtColor();

        String getGoodImgUrl();

        String getGoodItemBgUrl();

        String getGoodName();

        String getGoodNameColor();

        SpannableString getGoodPrice();

        String getGoodPriceColor();

        String getPayCount();

        String getPayCountColor();

        String getTip();

        String getTipColor();

        String getTitle();

        String getTitleColor();

        String getUltimatePrizeAuraImgUrl();
    }

    public interface IEventListener {
        void onBtnLeftClick(Dialog dialog, View view);

        void onBtnRightClick(Dialog dialog, View view);

        void onBtnSingleClick(Dialog dialog, View view);

        void onDlgDismiss(Dialog dialog);

        void onDlgShow(Dialog dialog);
    }

    private void findViews() {
        this.constraintLayout = (ConstraintLayout) findViewById(R.id.constraint_layout);
        this.aura = (ImageView) findViewById(R.id.aura);
        this.pos1 = (Space) findViewById(R.id.pos_1);
        this.pos2 = (Space) findViewById(R.id.pos_2);
        this.pos3 = (Space) findViewById(R.id.pos_3);
        this.pos4 = (Space) findViewById(R.id.pos_4);
        this.title = (TextView) findViewById(R.id.title);
        this.desc = (TextView) findViewById(R.id.desc);
        this.ext = (TextView) findViewById(R.id.ext);
        this.awardImg = (ImageView) findViewById(R.id.award_img);
        this.guessLikeGoodItem = (SimpleCardView) findViewById(R.id.guess_like_good_item);
        this.goodInfoBg = (ImageView) findViewById(R.id.good_info_bg);
        this.goodImg = (ImageView) findViewById(R.id.good_img);
        this.goodName = (TextView) findViewById(R.id.good_name);
        this.goodPrice = (TextView) findViewById(R.id.good_price);
        this.payCount = (TextView) findViewById(R.id.pay_count);
        this.btnLeft = (TextView) findViewById(R.id.btn_left);
        this.btnRight = (TextView) findViewById(R.id.btn_right);
        this.btnSingle = (TextView) findViewById(R.id.btn_single);
        this.tip = (TextView) findViewById(R.id.tip);
    }

    public GameDlgA(Context context) {
        super(context);
        findViews();
        setCancelable(true);
        if (context instanceof Activity) {
            setOwnerActivity((Activity) context);
        }
        if ((getOwnerActivity() instanceof GameDaFuWengActivity) && ((GameDaFuWengActivity) getOwnerActivity()).getDfwLauncher() != null) {
            this.imageLoader = ((GameDaFuWengActivity) getOwnerActivity()).getDfwLauncher().imageLoader;
        }
    }

    public View onCreateView() {
        return LayoutInflater.from(getContext()).inflate(R.layout.marketgames_dialog_a, (ViewGroup) null);
    }

    public void show() {
        super.show(true);
        if (this.eventListener != null) {
            this.eventListener.onDlgShow(this);
        }
    }

    public void dismiss() {
        super.dismiss();
        if (this.eventListener != null) {
            this.eventListener.onDlgDismiss(this);
        }
    }

    public DlgStyle getDlgStyle() {
        return this.dlgStyle;
    }

    public GameDlgA setDlgStyle(DlgStyle dlgStyle2) {
        this.dlgStyle = dlgStyle2;
        return this;
    }

    public IDataProvider getDataProvider() {
        return this.dataProvider;
    }

    public GameDlgA setDataProvider(IDataProvider dataProvider2) {
        this.dataProvider = dataProvider2;
        return this;
    }

    public IEventListener getEventListener() {
        return this.eventListener;
    }

    public GameDlgA setEventListener(IEventListener eventListener2) {
        this.eventListener = eventListener2;
        return this;
    }

    public ImageView getAura() {
        return this.aura;
    }

    public TextView getTitle() {
        return this.title;
    }

    public TextView getDesc() {
        return this.desc;
    }

    public TextView getExt() {
        return this.ext;
    }

    public ImageView getAwardImg() {
        return this.awardImg;
    }

    public SimpleCardView getGuessLikeGoodItem() {
        return this.guessLikeGoodItem;
    }

    public ImageView getGoogImg() {
        return this.goodImg;
    }

    public TextView getGoodName() {
        return this.goodName;
    }

    public TextView getGoodPrice() {
        return this.goodPrice;
    }

    public TextView getPayCount() {
        return this.payCount;
    }

    public TextView getBtnLeft() {
        return this.btnLeft;
    }

    public TextView getBtnRight() {
        return this.btnRight;
    }

    public TextView getBtnSingle() {
        return this.btnSingle;
    }

    public TextView getTip() {
        return this.tip;
    }

    public GameDlgA apply() {
        this.aura.setVisibility(4);
        this.title.setVisibility(4);
        this.desc.setVisibility(4);
        this.ext.setVisibility(4);
        this.awardImg.setVisibility(4);
        this.guessLikeGoodItem.setVisibility(4);
        this.btnLeft.setVisibility(4);
        this.btnRight.setVisibility(4);
        this.btnSingle.setVisibility(4);
        this.tip.setVisibility(4);
        if (this.dlgStyle != null) {
            if (this.dlgStyle == DlgStyle.lucky_award) {
                this.aura.setVisibility(0);
                this.title.setVisibility(0);
                this.desc.setVisibility(0);
                this.ext.setVisibility(0);
                this.awardImg.setVisibility(0);
                this.btnLeft.setVisibility(0);
                this.btnRight.setVisibility(0);
                this.tip.setVisibility(0);
            } else if (this.dlgStyle == DlgStyle.lucky_no_award) {
                this.aura.setVisibility(0);
                this.title.setVisibility(0);
                this.desc.setVisibility(0);
                this.awardImg.setVisibility(0);
                this.btnSingle.setVisibility(0);
            } else if (this.dlgStyle == DlgStyle.lucky_guess_like_good) {
                this.aura.setVisibility(0);
                this.title.setVisibility(0);
                this.desc.setVisibility(0);
                this.guessLikeGoodItem.setVisibility(0);
                this.btnLeft.setVisibility(0);
                this.btnRight.setVisibility(0);
            } else if (this.dlgStyle == DlgStyle.before_user_leave) {
                this.aura.setVisibility(0);
                this.title.setVisibility(0);
                this.desc.setVisibility(0);
                this.awardImg.setVisibility(0);
                this.btnLeft.setVisibility(0);
                this.btnRight.setVisibility(0);
            } else if (this.dlgStyle == DlgStyle.unsafe_user) {
                this.aura.setVisibility(0);
                this.title.setVisibility(0);
                this.desc.setVisibility(0);
                this.awardImg.setVisibility(0);
                this.btnSingle.setVisibility(0);
            }
            if (this.dataProvider != null) {
                try {
                    this.constraintLayout.setBackgroundColor(Color.parseColor(this.dataProvider.getBgColor()));
                } catch (Throwable th) {
                }
                if (this.imageLoader != null) {
                    this.imageLoader.loadInto(getContext(), this.dataProvider.getAuraImgUrl(), this.aura);
                }
                if (this.imageLoader != null) {
                    this.imageLoader.loadInto(getContext(), this.dataProvider.getAwardImgUrl(), this.awardImg);
                }
                if (!TextUtils.isEmpty(this.dataProvider.getTitle())) {
                    this.title.setText(this.dataProvider.getTitle());
                    String colorString = this.dataProvider.getTitleColor();
                    if (!TextUtils.isEmpty(colorString)) {
                        try {
                            this.title.setTextColor(Color.parseColor(colorString));
                        } catch (Throwable th2) {
                        }
                    }
                }
                if (!TextUtils.isEmpty(this.dataProvider.getDesc())) {
                    this.desc.setText(this.dataProvider.getDesc());
                    String colorString2 = this.dataProvider.getDescColor();
                    if (!TextUtils.isEmpty(colorString2)) {
                        try {
                            this.desc.setTextColor(Color.parseColor(colorString2));
                        } catch (Throwable th3) {
                        }
                    }
                }
                if (!TextUtils.isEmpty(this.dataProvider.getExt())) {
                    this.ext.setText(this.dataProvider.getExt());
                    String colorString3 = this.dataProvider.getExtColor();
                    if (!TextUtils.isEmpty(colorString3)) {
                        try {
                            this.ext.setTextColor(Color.parseColor(colorString3));
                        } catch (Throwable th4) {
                        }
                    }
                }
                if (!TextUtils.isEmpty(this.dataProvider.getTip())) {
                    this.tip.setText(this.dataProvider.getTip());
                    String colorString4 = this.dataProvider.getTipColor();
                    if (!TextUtils.isEmpty(colorString4)) {
                        try {
                            this.tip.setTextColor(Color.parseColor(colorString4));
                        } catch (Throwable th5) {
                        }
                    }
                }
                if (!TextUtils.isEmpty(this.dataProvider.getGoodName())) {
                    this.goodName.setText(this.dataProvider.getGoodName());
                    String colorString5 = this.dataProvider.getGoodNameColor();
                    if (!TextUtils.isEmpty(colorString5)) {
                        try {
                            this.goodName.setTextColor(Color.parseColor(colorString5));
                        } catch (Throwable th6) {
                        }
                    }
                }
                if (!TextUtils.isEmpty(this.dataProvider.getGoodPrice())) {
                    this.goodPrice.setText(this.dataProvider.getGoodPrice());
                    String colorString6 = this.dataProvider.getGoodPriceColor();
                    if (!TextUtils.isEmpty(colorString6)) {
                        try {
                            this.goodPrice.setTextColor(Color.parseColor(colorString6));
                        } catch (Throwable th7) {
                        }
                    }
                }
                if (!TextUtils.isEmpty(this.dataProvider.getPayCount())) {
                    this.payCount.setText(this.dataProvider.getPayCount());
                    String colorString7 = this.dataProvider.getPayCountColor();
                    if (!TextUtils.isEmpty(colorString7)) {
                        try {
                            this.payCount.setTextColor(Color.parseColor(colorString7));
                        } catch (Throwable th8) {
                        }
                    }
                }
                if (this.imageLoader != null) {
                    this.imageLoader.loadInto(getContext(), this.dataProvider.getGoodImgUrl(), this.goodImg);
                }
                if (this.imageLoader != null) {
                    this.imageLoader.loadInto(getContext(), this.dataProvider.getGoodItemBgUrl(), this.goodInfoBg);
                }
                new Runnable() {
                    Drawable focusBg = null;
                    Integer txtFocusClr = -1;
                    Integer txtUnFocusClr = -1;
                    Drawable unFocusBg = null;

                    public void run() {
                        GameDlgA.this.btnLeft.setText(GameDlgA.this.dataProvider.getBtnLeftTxt());
                        GameDlgA.this.btnLeft.setOnFocusChangeListener(new View.OnFocusChangeListener() {
                            public void onFocusChange(View v, boolean hasFocus) {
                                if (hasFocus) {
                                    GameDlgA.this.btnLeft.setBackgroundDrawable(AnonymousClass1.this.focusBg);
                                    GameDlgA.this.btnLeft.setTextColor(AnonymousClass1.this.txtFocusClr.intValue());
                                    return;
                                }
                                GameDlgA.this.btnLeft.setBackgroundDrawable(AnonymousClass1.this.unFocusBg);
                                GameDlgA.this.btnLeft.setTextColor(AnonymousClass1.this.txtUnFocusClr.intValue());
                            }
                        });
                        GameDlgA.this.btnLeft.setOnClickListener(new View.OnClickListener() {
                            public void onClick(View v) {
                                if (GameDlgA.this.eventListener != null) {
                                    GameDlgA.this.eventListener.onBtnLeftClick(GameDlgA.this, GameDlgA.this.btnLeft);
                                }
                            }
                        });
                        if (!TextUtils.isEmpty(GameDlgA.this.dataProvider.getBtnLeftTxtFocusColor())) {
                            try {
                                this.txtFocusClr = Integer.valueOf(Color.parseColor(GameDlgA.this.dataProvider.getBtnLeftTxtFocusColor()));
                            } catch (Throwable th) {
                            }
                        }
                        if (!TextUtils.isEmpty(GameDlgA.this.dataProvider.getBtnLeftTxtUnFocusColor())) {
                            try {
                                this.txtUnFocusClr = Integer.valueOf(Color.parseColor(GameDlgA.this.dataProvider.getBtnLeftTxtUnFocusColor()));
                            } catch (Throwable th2) {
                            }
                        }
                        if (GameDlgA.this.imageLoader != null) {
                            GameDlgA.this.imageLoader.load(GameDlgA.this.getContext(), GameDlgA.this.dataProvider.getBtnLeftFocusImgUrl(), new IImageLoader.ImgLoadListener() {
                                public void onSuccess(Bitmap bitmap) {
                                    AnonymousClass1.this.focusBg = new BitmapDrawable(bitmap);
                                }

                                public void onFailed() {
                                }
                            });
                            GameDlgA.this.imageLoader.load(GameDlgA.this.getContext(), GameDlgA.this.dataProvider.getBtnLeftNormalImgUrl(), new IImageLoader.ImgLoadListener() {
                                public void onSuccess(Bitmap bitmap) {
                                    AnonymousClass1.this.unFocusBg = new BitmapDrawable(bitmap);
                                    if (GameDlgA.this.btnLeft.getVisibility() == 0 && !GameDlgA.this.btnLeft.hasFocus()) {
                                        GameDlgA.this.btnLeft.setBackgroundDrawable(AnonymousClass1.this.unFocusBg);
                                    }
                                }

                                public void onFailed() {
                                }
                            });
                        }
                    }
                }.run();
                new Runnable() {
                    Drawable focusBg = null;
                    Integer txtFocusClr = -1;
                    Integer txtUnFocusClr = -1;
                    Drawable unFocusBg = null;

                    public void run() {
                        GameDlgA.this.btnRight.setText(GameDlgA.this.dataProvider.getBtnRightTxt());
                        GameDlgA.this.btnRight.setOnFocusChangeListener(new View.OnFocusChangeListener() {
                            public void onFocusChange(View v, boolean hasFocus) {
                                if (hasFocus) {
                                    GameDlgA.this.btnRight.setBackgroundDrawable(AnonymousClass2.this.focusBg);
                                    GameDlgA.this.btnRight.setTextColor(AnonymousClass2.this.txtFocusClr.intValue());
                                    return;
                                }
                                GameDlgA.this.btnRight.setBackgroundDrawable(AnonymousClass2.this.unFocusBg);
                                GameDlgA.this.btnRight.setTextColor(AnonymousClass2.this.txtUnFocusClr.intValue());
                            }
                        });
                        GameDlgA.this.btnRight.setOnClickListener(new View.OnClickListener() {
                            public void onClick(View v) {
                                if (GameDlgA.this.eventListener != null) {
                                    GameDlgA.this.eventListener.onBtnRightClick(GameDlgA.this, GameDlgA.this.btnRight);
                                }
                            }
                        });
                        if (!TextUtils.isEmpty(GameDlgA.this.dataProvider.getBtnRightTxtFocusColor())) {
                            try {
                                this.txtFocusClr = Integer.valueOf(Color.parseColor(GameDlgA.this.dataProvider.getBtnRightTxtFocusColor()));
                            } catch (Throwable th) {
                            }
                        }
                        if (!TextUtils.isEmpty(GameDlgA.this.dataProvider.getBtnRightTxtUnFocusColor())) {
                            try {
                                this.txtUnFocusClr = Integer.valueOf(Color.parseColor(GameDlgA.this.dataProvider.getBtnRightTxtUnFocusColor()));
                            } catch (Throwable th2) {
                            }
                        }
                        if (GameDlgA.this.imageLoader != null) {
                            GameDlgA.this.imageLoader.load(GameDlgA.this.getContext(), GameDlgA.this.dataProvider.getBtnRightFocusImgUrl(), new IImageLoader.ImgLoadListener() {
                                public void onSuccess(Bitmap bitmap) {
                                    AnonymousClass2.this.focusBg = new BitmapDrawable(bitmap);
                                    if (GameDlgA.this.btnRight.getVisibility() == 0) {
                                        GameDlgA.this.btnRight.requestFocus();
                                        GameDlgA.this.btnRight.setBackgroundDrawable(AnonymousClass2.this.focusBg);
                                    }
                                }

                                public void onFailed() {
                                }
                            });
                            GameDlgA.this.imageLoader.load(GameDlgA.this.getContext(), GameDlgA.this.dataProvider.getBtnRightNormalImgUrl(), new IImageLoader.ImgLoadListener() {
                                public void onSuccess(Bitmap bitmap) {
                                    AnonymousClass2.this.unFocusBg = new BitmapDrawable(bitmap);
                                }

                                public void onFailed() {
                                }
                            });
                        }
                    }
                }.run();
                new Runnable() {
                    Drawable focusBg = null;
                    int txtFocusClr = -1;
                    int txtUnFocusClr = -1;
                    Drawable unFocusBg = null;

                    public void run() {
                        GameDlgA.this.btnSingle.setText(GameDlgA.this.dataProvider.getBtnSingleTxt());
                        GameDlgA.this.btnSingle.setOnFocusChangeListener(new View.OnFocusChangeListener() {
                            public void onFocusChange(View v, boolean hasFocus) {
                                if (hasFocus) {
                                    GameDlgA.this.btnSingle.setBackgroundDrawable(AnonymousClass3.this.focusBg);
                                    GameDlgA.this.btnSingle.setTextColor(AnonymousClass3.this.txtFocusClr);
                                    return;
                                }
                                GameDlgA.this.btnSingle.setBackgroundDrawable(AnonymousClass3.this.unFocusBg);
                                GameDlgA.this.btnSingle.setTextColor(AnonymousClass3.this.txtUnFocusClr);
                            }
                        });
                        GameDlgA.this.btnSingle.setOnClickListener(new View.OnClickListener() {
                            public void onClick(View v) {
                                if (GameDlgA.this.eventListener != null) {
                                    GameDlgA.this.eventListener.onBtnSingleClick(GameDlgA.this, GameDlgA.this.btnSingle);
                                }
                            }
                        });
                        if (!TextUtils.isEmpty(GameDlgA.this.dataProvider.getBtnSingleTxtFocusColor())) {
                            try {
                                this.txtFocusClr = Color.parseColor(GameDlgA.this.dataProvider.getBtnSingleTxtFocusColor());
                            } catch (Throwable th) {
                            }
                        }
                        if (!TextUtils.isEmpty(GameDlgA.this.dataProvider.getBtnSingleTxtUnFocusColor())) {
                            try {
                                this.txtUnFocusClr = Color.parseColor(GameDlgA.this.dataProvider.getBtnSingleTxtUnFocusColor());
                            } catch (Throwable th2) {
                            }
                        }
                        if (GameDlgA.this.imageLoader != null) {
                            GameDlgA.this.imageLoader.load(GameDlgA.this.getContext(), GameDlgA.this.dataProvider.getBtnSingleFocusImgUrl(), new IImageLoader.ImgLoadListener() {
                                public void onSuccess(Bitmap bitmap) {
                                    AnonymousClass3.this.focusBg = new BitmapDrawable(bitmap);
                                    if (GameDlgA.this.btnSingle.getVisibility() == 0) {
                                        GameDlgA.this.btnSingle.requestFocus();
                                        GameDlgA.this.btnSingle.setBackgroundDrawable(AnonymousClass3.this.focusBg);
                                    }
                                }

                                public void onFailed() {
                                }
                            });
                            GameDlgA.this.imageLoader.load(GameDlgA.this.getContext(), GameDlgA.this.dataProvider.getBtnSingleNormalImgUrl(), new IImageLoader.ImgLoadListener() {
                                public void onSuccess(Bitmap bitmap) {
                                    AnonymousClass3.this.unFocusBg = new BitmapDrawable(bitmap);
                                }

                                public void onFailed() {
                                }
                            });
                        }
                    }
                }.run();
            }
        }
        return this;
    }
}
