package com.yunos.tvtaobao.tvsdk.widget.dialog;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.TypedValue;
import android.view.ContextThemeWrapper;
import android.view.KeyEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.FrostedGlass;
import android.widget.ListAdapter;
import android.widget.ListView;
import com.yunos.tv.aliTvSdk.R;
import com.yunos.tvtaobao.tvsdk.widget.dialog.AlertController;
import com.zhiping.dev.android.logger.ZpLogger;

public class TvAlertDialog extends Dialog implements DialogInterface {
    public static final int THEME_BACKHINT = 9;
    public static final int THEME_COMMON = 10;
    public static final int THEME_DEVICE_DEFAULT_DARK = 4;
    public static final int THEME_DEVICE_DEFAULT_LIGHT = 5;
    public static final int THEME_HOLO_DARK = 2;
    public static final int THEME_HOLO_LIGHT = 3;
    public static final int THEME_SET_NETWORK = 8;
    public static final int THEME_SYS_UPDATE = 6;
    public static final int THEME_TRADITIONAL = 1;
    public static final int THEME_WARNING = 7;
    private final String TAG = "TvAlertDialog";
    /* access modifiers changed from: private */
    public AlertController mAlert = new AlertController(getContext(), this, getWindow());
    /* access modifiers changed from: private */
    public int mFrostedGlassRadius = 5;
    private boolean mNeedFrostedGlass = true;
    /* access modifiers changed from: private */
    public Bitmap mNeedFrostedGlassBmp;

    protected TvAlertDialog(Context context) {
        super(context, R.style.Theme_Ali_TV_Dialog_Alert);
    }

    protected TvAlertDialog(Context context, int theme) {
        super(context, theme);
    }

    static int resolveDialogTheme(Context context, int resid) {
        if (resid == 6) {
            return R.style.Theme_Ali_TV_Dialog_Alert_SysUpdate;
        }
        if (resid == 7) {
            return R.style.Theme_Ali_TV_Dialog_Alert_Warning;
        }
        if (resid == 8) {
            return R.style.Theme_Ali_TV_Dialog_Alert_SetNetwork;
        }
        if (resid == 9) {
            return R.style.Theme_Ali_TV_Dialog_Alert_BackHint;
        }
        if (resid == 10) {
            return R.style.Theme_Ali_TV_Dialog_Alert;
        }
        return R.style.Theme_Ali_TV_Dialog_Alert;
    }

    public Button getButton(int whichButton) {
        return this.mAlert.getButton(whichButton);
    }

    public ListView getListView() {
        return this.mAlert.getListView();
    }

    public void setTitle(CharSequence title) {
        super.setTitle(title);
        this.mAlert.setTitle(title);
    }

    public void setCustomTitle(View customTitleView) {
        this.mAlert.setCustomTitle(customTitleView);
    }

    public void setMessage(CharSequence message) {
        this.mAlert.setMessage(message);
    }

    public void setView(View view) {
        this.mAlert.setView(view);
    }

    public void setView(View view, int viewSpacingLeft, int viewSpacingTop, int viewSpacingRight, int viewSpacingBottom) {
        this.mAlert.setView(view, viewSpacingLeft, viewSpacingTop, viewSpacingRight, viewSpacingBottom);
    }

    public void setButton(int whichButton, CharSequence text, Message msg) {
        this.mAlert.setButton(whichButton, text, (DialogInterface.OnClickListener) null, msg);
    }

    public void setButton(int whichButton, CharSequence text, DialogInterface.OnClickListener listener) {
        this.mAlert.setButton(whichButton, text, listener, (Message) null);
    }

    @Deprecated
    public void setButton(CharSequence text, Message msg) {
        setButton(-1, text, msg);
    }

    @Deprecated
    public void setButton2(CharSequence text, Message msg) {
        setButton(-2, text, msg);
    }

    @Deprecated
    public void setButton3(CharSequence text, Message msg) {
        setButton(-3, text, msg);
    }

    @Deprecated
    public void setButton(CharSequence text, DialogInterface.OnClickListener listener) {
        setButton(-1, text, listener);
    }

    @Deprecated
    public void setButton2(CharSequence text, DialogInterface.OnClickListener listener) {
        setButton(-2, text, listener);
    }

    @Deprecated
    public void setButton3(CharSequence text, DialogInterface.OnClickListener listener) {
        setButton(-3, text, listener);
    }

    public void setIcon(int resId) {
        this.mAlert.setIcon(resId);
    }

    public void setIcon(Drawable icon) {
        this.mAlert.setIcon(icon);
    }

    public void setIconAttribute(int attrId) {
        TypedValue out = new TypedValue();
        getContext().getTheme().resolveAttribute(attrId, out, true);
        this.mAlert.setIcon(out.resourceId);
    }

    public void setInverseBackgroundForced(boolean forceInverseBackground) {
        this.mAlert.setInverseBackgroundForced(forceInverseBackground);
    }

    public void setFrostedGlass(boolean frostedGlass) {
        this.mNeedFrostedGlass = frostedGlass;
    }

    public void setFrostedGlassRadius(int radius) {
        this.mFrostedGlassRadius = radius;
    }

    public void setFrostedGlassBitmap(Bitmap bmp) {
        this.mNeedFrostedGlassBmp = bmp;
    }

    /* access modifiers changed from: protected */
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.mAlert.installContent();
        final Handler handler = new Handler();
        getWindow().setFlags(256, 256);
        getWindow().addFlags(16777216);
        getWindow().setLayout(-1, -1);
        if (this.mNeedFrostedGlass) {
            MessageHandler msgHandler = MessageHandler.getInstance();
            if (this.mNeedFrostedGlassBmp != null) {
                msgHandler.getHandler().postDelayed(new Runnable() {
                    public void run() {
                        new FrostedGlass().stackBlur(TvAlertDialog.this.mNeedFrostedGlassBmp, TvAlertDialog.this.mFrostedGlassRadius);
                        handler.post(new Runnable() {
                            public void run() {
                                TvAlertDialog.this.mAlert.setBackground(TvAlertDialog.this.mNeedFrostedGlassBmp);
                            }
                        });
                    }
                }, 200);
            } else {
                msgHandler.getHandler().postDelayed(new Runnable() {
                    public void run() {
                        final Bitmap screenBmp = new FrostedGlass().getFrostedGlassBitmap(TvAlertDialog.this.getContext());
                        if (screenBmp == null) {
                            ZpLogger.e("TvAlertDialog", "getFrostedGlassBitmap  screenBmp is null!");
                        } else {
                            handler.post(new Runnable() {
                                public void run() {
                                    TvAlertDialog.this.mAlert.setBackground(screenBmp);
                                }
                            });
                        }
                    }
                }, 200);
            }
        }
    }

    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (this.mAlert.onKeyDown(keyCode, event)) {
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }

    public boolean onKeyUp(int keyCode, KeyEvent event) {
        if (this.mAlert.onKeyUp(keyCode, event)) {
            return true;
        }
        return super.onKeyUp(keyCode, event);
    }

    public static class Builder {
        private final AlertController.AlertParams P;
        private int mFrostedGlassRadius;
        private boolean mNeedFrostedGlass;
        private Bitmap mNeedFrostedGlassBmp;
        private int mTheme;

        public Builder(Context context) {
            this(context, TvAlertDialog.resolveDialogTheme(context, 0));
        }

        public Builder(Context context, int theme) {
            this.mNeedFrostedGlass = false;
            this.mFrostedGlassRadius = 5;
            this.mTheme = TvAlertDialog.resolveDialogTheme(context, theme);
            this.P = new AlertController.AlertParams(new ContextThemeWrapper(context, this.mTheme));
        }

        public Context getContext() {
            return this.P.mContext;
        }

        public Builder setTitle(int titleId) {
            this.P.mTitle = this.P.mContext.getText(titleId);
            return this;
        }

        public Builder setTitle(CharSequence title) {
            this.P.mTitle = title;
            return this;
        }

        public Builder setCustomTitle(View customTitleView) {
            this.P.mCustomTitleView = customTitleView;
            return this;
        }

        public Builder setMessage(int messageId) {
            this.P.mMessage = this.P.mContext.getText(messageId);
            return this;
        }

        public Builder setMessage(CharSequence message) {
            this.P.mMessage = message;
            return this;
        }

        public Builder setIcon(int iconId) {
            this.P.mIconId = iconId;
            return this;
        }

        public Builder setIcon(Drawable icon) {
            this.P.mIcon = icon;
            return this;
        }

        public Builder setIconAttribute(int attrId) {
            TypedValue out = new TypedValue();
            this.P.mContext.getTheme().resolveAttribute(attrId, out, true);
            this.P.mIconId = out.resourceId;
            return this;
        }

        public Builder setPositiveButton(int textId, DialogInterface.OnClickListener listener) {
            this.P.mPositiveButtonText = this.P.mContext.getText(textId);
            this.P.mPositiveButtonListener = listener;
            return this;
        }

        public Builder setPositiveButton(CharSequence text, DialogInterface.OnClickListener listener) {
            this.P.mPositiveButtonText = text;
            this.P.mPositiveButtonListener = listener;
            return this;
        }

        public Builder setNegativeButton(int textId, DialogInterface.OnClickListener listener) {
            this.P.mNegativeButtonText = this.P.mContext.getText(textId);
            this.P.mNegativeButtonListener = listener;
            return this;
        }

        public Builder setFrostedGlass(boolean frostedGlass) {
            this.mNeedFrostedGlass = frostedGlass;
            return this;
        }

        public Builder setFrostedGlassRadius(int radius) {
            this.mFrostedGlassRadius = radius;
            return this;
        }

        public Builder setFrostedGlassBitmap(Bitmap bmp) {
            this.mNeedFrostedGlassBmp = bmp;
            return this;
        }

        public Builder setNegativeButton(CharSequence text, DialogInterface.OnClickListener listener) {
            this.P.mNegativeButtonText = text;
            this.P.mNegativeButtonListener = listener;
            return this;
        }

        public Builder setNeutralButton(int textId, DialogInterface.OnClickListener listener) {
            this.P.mNeutralButtonText = this.P.mContext.getText(textId);
            this.P.mNeutralButtonListener = listener;
            return this;
        }

        public Builder setNeutralButton(CharSequence text, DialogInterface.OnClickListener listener) {
            this.P.mNeutralButtonText = text;
            this.P.mNeutralButtonListener = listener;
            return this;
        }

        public Builder setCustomerButtons(CharSequence[] texts, DialogInterface.OnClickListener listener) {
            this.P.mCustomItems = texts;
            this.P.mCustomItemClickListener = listener;
            return this;
        }

        public Builder setCancelable(boolean cancelable) {
            this.P.mCancelable = cancelable;
            return this;
        }

        public Builder setOnCancelListener(DialogInterface.OnCancelListener onCancelListener) {
            this.P.mOnCancelListener = onCancelListener;
            return this;
        }

        public Builder setOnKeyListener(DialogInterface.OnKeyListener onKeyListener) {
            this.P.mOnKeyListener = onKeyListener;
            return this;
        }

        public Builder setItems(int itemsId, DialogInterface.OnClickListener listener) {
            this.P.mItems = this.P.mContext.getResources().getTextArray(itemsId);
            this.P.mOnClickListener = listener;
            return this;
        }

        public Builder setItems(CharSequence[] items, DialogInterface.OnClickListener listener) {
            this.P.mItems = items;
            this.P.mOnClickListener = listener;
            return this;
        }

        public Builder setAdapter(ListAdapter adapter, DialogInterface.OnClickListener listener) {
            this.P.mAdapter = adapter;
            this.P.mOnClickListener = listener;
            return this;
        }

        public Builder setCursor(Cursor cursor, DialogInterface.OnClickListener listener, String labelColumn) {
            this.P.mCursor = cursor;
            this.P.mLabelColumn = labelColumn;
            this.P.mOnClickListener = listener;
            return this;
        }

        public Builder setMultiChoiceItems(int itemsId, boolean[] checkedItems, DialogInterface.OnMultiChoiceClickListener listener) {
            this.P.mItems = this.P.mContext.getResources().getTextArray(itemsId);
            this.P.mOnCheckboxClickListener = listener;
            this.P.mCheckedItems = checkedItems;
            this.P.mIsMultiChoice = true;
            return this;
        }

        public Builder setMultiChoiceItems(CharSequence[] items, boolean[] checkedItems, DialogInterface.OnMultiChoiceClickListener listener) {
            this.P.mItems = items;
            this.P.mOnCheckboxClickListener = listener;
            this.P.mCheckedItems = checkedItems;
            this.P.mIsMultiChoice = true;
            return this;
        }

        public Builder setMultiChoiceItems(Cursor cursor, String isCheckedColumn, String labelColumn, DialogInterface.OnMultiChoiceClickListener listener) {
            this.P.mCursor = cursor;
            this.P.mOnCheckboxClickListener = listener;
            this.P.mIsCheckedColumn = isCheckedColumn;
            this.P.mLabelColumn = labelColumn;
            this.P.mIsMultiChoice = true;
            return this;
        }

        public Builder setSingleChoiceItems(int itemsId, int checkedItem, DialogInterface.OnClickListener listener) {
            this.P.mItems = this.P.mContext.getResources().getTextArray(itemsId);
            this.P.mOnClickListener = listener;
            this.P.mCheckedItem = checkedItem;
            this.P.mIsSingleChoice = true;
            return this;
        }

        public Builder setSingleChoiceItems(Cursor cursor, int checkedItem, String labelColumn, DialogInterface.OnClickListener listener) {
            this.P.mCursor = cursor;
            this.P.mOnClickListener = listener;
            this.P.mCheckedItem = checkedItem;
            this.P.mLabelColumn = labelColumn;
            this.P.mIsSingleChoice = true;
            return this;
        }

        public Builder setSingleChoiceItems(CharSequence[] items, int checkedItem, DialogInterface.OnClickListener listener) {
            this.P.mItems = items;
            this.P.mOnClickListener = listener;
            this.P.mCheckedItem = checkedItem;
            this.P.mIsSingleChoice = true;
            return this;
        }

        public Builder setSingleChoiceItems(ListAdapter adapter, int checkedItem, DialogInterface.OnClickListener listener) {
            this.P.mAdapter = adapter;
            this.P.mOnClickListener = listener;
            this.P.mCheckedItem = checkedItem;
            this.P.mIsSingleChoice = true;
            return this;
        }

        public Builder setOnItemSelectedListener(AdapterView.OnItemSelectedListener listener) {
            this.P.mOnItemSelectedListener = listener;
            return this;
        }

        public Builder setView(View view) {
            this.P.mView = view;
            this.P.mViewSpacingSpecified = false;
            return this;
        }

        public Builder setView(View view, int viewSpacingLeft, int viewSpacingTop, int viewSpacingRight, int viewSpacingBottom) {
            this.P.mView = view;
            this.P.mViewSpacingSpecified = true;
            this.P.mViewSpacingLeft = viewSpacingLeft;
            this.P.mViewSpacingTop = viewSpacingTop;
            this.P.mViewSpacingRight = viewSpacingRight;
            this.P.mViewSpacingBottom = viewSpacingBottom;
            return this;
        }

        public Builder setInverseBackgroundForced(boolean useInverseBackground) {
            this.P.mForceInverseBackground = useInverseBackground;
            return this;
        }

        public Builder setRecycleOnMeasureEnabled(boolean enabled) {
            this.P.mRecycleOnMeasure = enabled;
            return this;
        }

        public TvAlertDialog create() {
            TvAlertDialog dialog = new TvAlertDialog(this.P.mContext, this.mTheme);
            this.P.apply(dialog.mAlert);
            dialog.setCancelable(this.P.mCancelable);
            dialog.setFrostedGlass(this.mNeedFrostedGlass);
            dialog.setFrostedGlassRadius(this.mFrostedGlassRadius);
            dialog.setFrostedGlassBitmap(this.mNeedFrostedGlassBmp);
            if (this.P.mCancelable) {
                dialog.setCanceledOnTouchOutside(true);
            }
            dialog.setOnCancelListener(this.P.mOnCancelListener);
            if (this.P.mOnKeyListener != null) {
                dialog.setOnKeyListener(this.P.mOnKeyListener);
            }
            if (needEntryAnimation()) {
                dialog.getWindow().setWindowAnimations(R.style.DialogWindowAnim);
            }
            return dialog;
        }

        public TvAlertDialog show() {
            TvAlertDialog dialog = create();
            dialog.show();
            return dialog;
        }

        public boolean needEntryAnimation() {
            if (this.mTheme == R.style.Theme_Ali_TV_Dialog_Alert_SysUpdate || this.mTheme == R.style.Theme_Ali_TV_Dialog_Alert_SetNetwork) {
                return true;
            }
            return false;
        }
    }
}
