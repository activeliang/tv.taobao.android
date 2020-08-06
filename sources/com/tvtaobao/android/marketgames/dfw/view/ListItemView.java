package com.tvtaobao.android.marketgames.dfw.view;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;
import com.tvtaobao.android.marketgames.R;
import com.tvtaobao.android.marketgames.dfw.wares.IImageLoader;
import com.tvtaobao.android.ui3.widget.RoundImageView;

public class ListItemView extends FrameLayout {
    /* access modifiers changed from: private */
    public TextView btnDo;
    /* access modifiers changed from: private */
    public Drawable btnFocus;
    /* access modifiers changed from: private */
    public Drawable btnInvalid;
    /* access modifiers changed from: private */
    public Drawable btnNormal;
    /* access modifiers changed from: private */
    public int btnTxtClrFocus;
    private int btnTxtClrInvalid;
    /* access modifiers changed from: private */
    public int btnTxtClrNormal;
    private TextView desc;
    private TextView ext;
    /* access modifiers changed from: private */
    public Drawable focusDrawable;
    /* access modifiers changed from: private */
    public RoundImageView icon;
    /* access modifiers changed from: private */
    public Drawable iconDrawable;
    /* access modifiers changed from: private */
    public RoundImageView img;
    private int itemImgBgColor;
    /* access modifiers changed from: private */
    public Drawable itemImgDrawable;
    /* access modifiers changed from: private */
    public ImageView mark;
    /* access modifiers changed from: private */
    public Drawable markDrawable;
    IItemData myItemData;
    private TextView name;

    public interface IItemData {
        String getBtnBgFocusImgUrl();

        String getBtnBgInvalidImgUrl();

        String getBtnBgNormalImgUrl();

        String getBtnTxt();

        String getBtnTxtFocusColor();

        String getBtnTxtInvalidColor();

        String getBtnTxtNormalColor();

        String getDesc();

        String getDescColor();

        String getIconImgUrl();

        IImageLoader getImgLoader();

        String getItemImgBgColor();

        String getItemImgUrl();

        String getMarkImgUrl();

        String getName();

        String getNameColor();

        boolean isInvalid();
    }

    public ListItemView(Context context) {
        this(context, (AttributeSet) null);
    }

    public ListItemView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public ListItemView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        this.btnTxtClrInvalid = -1;
        this.btnTxtClrNormal = -1;
        this.btnTxtClrFocus = -1;
        this.itemImgBgColor = -1;
        LayoutInflater.from(context).inflate(R.layout.marketgames_dialog_b_list_item, this, true);
        findViews();
        this.focusDrawable = context.getResources().getDrawable(R.drawable.marketgames_award_item_focus_frame);
        this.btnDo.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus) {
                    ListItemView.this.setBackgroundDrawable(ListItemView.this.focusDrawable);
                    if (ListItemView.this.btnTxtClrFocus != -1) {
                        ListItemView.this.btnDo.setTextColor(ListItemView.this.btnTxtClrFocus);
                    }
                    if (ListItemView.this.btnFocus != null) {
                        ListItemView.this.btnDo.setBackgroundDrawable(ListItemView.this.btnFocus);
                        return;
                    }
                    return;
                }
                ListItemView.this.setBackgroundDrawable((Drawable) null);
                if (ListItemView.this.btnTxtClrNormal != -1) {
                    ListItemView.this.btnDo.setTextColor(ListItemView.this.btnTxtClrNormal);
                }
                if (ListItemView.this.btnNormal != null) {
                    ListItemView.this.btnDo.setBackgroundDrawable(ListItemView.this.btnNormal);
                }
            }
        });
        setBackgroundDrawable((Drawable) null);
    }

    private void findViews() {
        this.img = (RoundImageView) findViewById(R.id.item_bg_img);
        this.icon = (RoundImageView) findViewById(R.id.icon);
        this.btnDo = (TextView) findViewById(R.id.btn_do);
        this.name = (TextView) findViewById(R.id.name);
        this.desc = (TextView) findViewById(R.id.desc);
        this.ext = (TextView) findViewById(R.id.ext);
        this.mark = (ImageView) findViewById(R.id.mark);
    }

    public RoundImageView getImg() {
        return this.img;
    }

    public RoundImageView getIcon() {
        return this.icon;
    }

    public TextView getBtnDo() {
        return this.btnDo;
    }

    public TextView getName() {
        return this.name;
    }

    public TextView getDesc() {
        return this.desc;
    }

    public TextView getExt() {
        return this.ext;
    }

    public ImageView getMark() {
        return this.mark;
    }

    public void inflate(final IItemData itemData) {
        if (itemData != null) {
            this.myItemData = itemData;
            IImageLoader imgLoader = itemData.getImgLoader();
            this.name.setText(itemData.getName());
            if (!TextUtils.isEmpty(itemData.getNameColor())) {
                try {
                    this.name.setTextColor(Color.parseColor(itemData.getNameColor()));
                } catch (Throwable th) {
                }
            }
            this.desc.setText(itemData.getDesc());
            if (!TextUtils.isEmpty(itemData.getDescColor())) {
                try {
                    this.desc.setTextColor(Color.parseColor(itemData.getDescColor()));
                } catch (Throwable th2) {
                }
            }
            try {
                this.btnTxtClrInvalid = Color.parseColor(itemData.getBtnTxtInvalidColor());
            } catch (Throwable th3) {
            }
            try {
                this.btnTxtClrNormal = Color.parseColor(itemData.getBtnTxtNormalColor());
            } catch (Throwable th4) {
            }
            try {
                this.btnTxtClrFocus = Color.parseColor(itemData.getBtnTxtFocusColor());
            } catch (Throwable th5) {
            }
            try {
                this.itemImgBgColor = Color.parseColor(itemData.getItemImgBgColor());
            } catch (Throwable th6) {
            }
            final Runnable applyBtnBgtask = new Runnable() {
                public void run() {
                    if (itemData.isInvalid()) {
                        if (ListItemView.this.btnDo.getBackground() != ListItemView.this.btnInvalid) {
                            ListItemView.this.btnDo.setBackgroundDrawable(ListItemView.this.btnInvalid);
                        }
                    } else if (ListItemView.this.btnDo.hasFocus()) {
                        if (ListItemView.this.btnDo.getBackground() != ListItemView.this.btnFocus) {
                            ListItemView.this.btnDo.setBackgroundDrawable(ListItemView.this.btnFocus);
                        }
                    } else if (ListItemView.this.btnDo.getBackground() != ListItemView.this.btnNormal) {
                        ListItemView.this.btnDo.setBackgroundDrawable(ListItemView.this.btnNormal);
                    }
                }
            };
            if (imgLoader != null) {
                imgLoader.load(getContext(), itemData.getBtnBgInvalidImgUrl(), new IImageLoader.ImgLoadListener() {
                    public void onSuccess(Bitmap bitmap) {
                        Drawable unused = ListItemView.this.btnInvalid = new BitmapDrawable(bitmap);
                        applyBtnBgtask.run();
                    }

                    public void onFailed() {
                    }
                });
                imgLoader.load(getContext(), itemData.getBtnBgFocusImgUrl(), new IImageLoader.ImgLoadListener() {
                    public void onSuccess(Bitmap bitmap) {
                        Drawable unused = ListItemView.this.btnFocus = new BitmapDrawable(bitmap);
                        applyBtnBgtask.run();
                    }

                    public void onFailed() {
                    }
                });
                imgLoader.load(getContext(), itemData.getBtnBgNormalImgUrl(), new IImageLoader.ImgLoadListener() {
                    public void onSuccess(Bitmap bitmap) {
                        Drawable unused = ListItemView.this.btnNormal = new BitmapDrawable(bitmap);
                        applyBtnBgtask.run();
                    }

                    public void onFailed() {
                    }
                });
                imgLoader.load(getContext(), itemData.getIconImgUrl(), new IImageLoader.ImgLoadListener() {
                    public void onSuccess(Bitmap bitmap) {
                        Drawable unused = ListItemView.this.iconDrawable = new BitmapDrawable(bitmap);
                        ListItemView.this.icon.setImageDrawable(ListItemView.this.iconDrawable);
                    }

                    public void onFailed() {
                    }
                });
                imgLoader.load(getContext(), itemData.getItemImgUrl(), new IImageLoader.ImgLoadListener() {
                    public void onSuccess(Bitmap bitmap) {
                        Drawable unused = ListItemView.this.itemImgDrawable = new BitmapDrawable(bitmap);
                        ListItemView.this.img.setImageDrawable(ListItemView.this.itemImgDrawable);
                    }

                    public void onFailed() {
                    }
                });
                imgLoader.load(getContext(), itemData.getMarkImgUrl(), new IImageLoader.ImgLoadListener() {
                    public void onSuccess(Bitmap bitmap) {
                        Drawable unused = ListItemView.this.markDrawable = new BitmapDrawable(bitmap);
                        ListItemView.this.mark.setImageDrawable(ListItemView.this.markDrawable);
                    }

                    public void onFailed() {
                    }
                });
            }
            this.btnDo.setText(itemData.getBtnTxt());
            if (itemData.isInvalid()) {
                this.btnDo.setTextColor(this.btnTxtClrInvalid);
            } else if (this.btnDo.hasFocus()) {
                this.btnDo.setTextColor(this.btnTxtClrFocus);
            } else {
                this.btnDo.setTextColor(this.btnTxtClrNormal);
            }
            this.img.setBackgroundColor(this.itemImgBgColor);
            applyBtnBgtask.run();
        }
    }
}
