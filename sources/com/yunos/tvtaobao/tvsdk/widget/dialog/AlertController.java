package com.yunos.tvtaobao.tvsdk.widget.dialog;

import android.content.Context;
import android.content.DialogInterface;
import android.content.res.Resources;
import android.content.res.TypedArray;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.TransitionDrawable;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.ScrollView;
import android.widget.TextView;
import com.yunos.tv.aliTvSdk.R;
import com.yunos.tvtaobao.tvsdk.widget.focus.FocusPositionManager;
import com.yunos.tvtaobao.tvsdk.widget.focus.StaticFocusDrawable;
import java.lang.ref.WeakReference;

public class AlertController {
    /* access modifiers changed from: private */
    public ListAdapter mAdapter;
    private View mAlertDialogBgView;
    private int mAlertDialogLayout;
    int mBtnNum = 0;
    View.OnClickListener mButtonHandler = new View.OnClickListener() {
        public void onClick(View v) {
            Message m = null;
            if (v == AlertController.this.mButtonPositive && AlertController.this.mButtonPositiveMessage != null) {
                m = Message.obtain(AlertController.this.mButtonPositiveMessage);
            } else if (v == AlertController.this.mButtonNegative && AlertController.this.mButtonNegativeMessage != null) {
                m = Message.obtain(AlertController.this.mButtonNegativeMessage);
            } else if (v == AlertController.this.mButtonNeutral && AlertController.this.mButtonNeutralMessage != null) {
                m = Message.obtain(AlertController.this.mButtonNeutralMessage);
            } else if (!(v.getTag() == null || !(v.getTag() instanceof Integer) || AlertController.this.mCustomerButtonsMsg == null)) {
                m = AlertController.this.mHandler.obtainMessage(AlertController.this.mCustomerButtonsMsg.what, ((Integer) v.getTag()).intValue(), -1, AlertController.this.mCustomerButtonsMsg.obj);
            }
            if (m != null) {
                m.sendToTarget();
            }
            AlertController.this.mHandler.obtainMessage(1, AlertController.this.mDialogInterface).sendToTarget();
        }
    };
    /* access modifiers changed from: private */
    public Button mButtonNegative;
    /* access modifiers changed from: private */
    public Message mButtonNegativeMessage;
    private CharSequence mButtonNegativeText;
    /* access modifiers changed from: private */
    public Button mButtonNeutral;
    /* access modifiers changed from: private */
    public Message mButtonNeutralMessage;
    private CharSequence mButtonNeutralText;
    /* access modifiers changed from: private */
    public Button mButtonPositive;
    /* access modifiers changed from: private */
    public Message mButtonPositiveMessage;
    private CharSequence mButtonPositiveText;
    /* access modifiers changed from: private */
    public int mCheckedItem = -1;
    private final Context mContext;
    private View mCustomTitleView;
    private CharSequence[] mCustomerButtons;
    /* access modifiers changed from: private */
    public Message mCustomerButtonsMsg;
    /* access modifiers changed from: private */
    public final DialogInterface mDialogInterface;
    private boolean mForceInverseBackground;
    /* access modifiers changed from: private */
    public Handler mHandler;
    private Drawable mIcon;
    private int mIconId = -1;
    private ImageView mIconView;
    /* access modifiers changed from: private */
    public int mListItemLayout;
    /* access modifiers changed from: private */
    public int mListLayout;
    /* access modifiers changed from: private */
    public ListView mListView;
    private CharSequence mMessage;
    private TextView mMessageView;
    /* access modifiers changed from: private */
    public int mMultiChoiceItemLayout;
    private ScrollView mScrollView;
    /* access modifiers changed from: private */
    public int mSingleChoiceItemLayout;
    private CharSequence mTitle;
    private TextView mTitleView;
    private View mView;
    private int mViewSpacingBottom;
    private int mViewSpacingLeft;
    private int mViewSpacingRight;
    private boolean mViewSpacingSpecified = false;
    private int mViewSpacingTop;
    private final Window mWindow;

    private static final class ButtonHandler extends Handler {
        public static final int CUSTOMER_BUTTON = 2;
        private static final int MSG_DISMISS_DIALOG = 1;
        private WeakReference<DialogInterface> mDialog;

        public ButtonHandler(DialogInterface dialog) {
            this.mDialog = new WeakReference<>(dialog);
        }

        public void handleMessage(Message msg) {
            switch (msg.what) {
                case -3:
                case -2:
                case -1:
                    ((DialogInterface.OnClickListener) msg.obj).onClick((DialogInterface) this.mDialog.get(), msg.what);
                    return;
                case 1:
                    ((DialogInterface) msg.obj).dismiss();
                    return;
                case 2:
                    ((DialogInterface.OnClickListener) msg.obj).onClick((DialogInterface) this.mDialog.get(), msg.arg1);
                    return;
                default:
                    return;
            }
        }
    }

    private static boolean shouldCenterSingleButton(Context context) {
        TypedValue outValue = new TypedValue();
        context.getTheme().resolveAttribute(R.attr.alertDialogCenterButtons, outValue, true);
        if (outValue.data != 0) {
            return true;
        }
        return false;
    }

    public AlertController(Context context, DialogInterface di, Window window) {
        this.mContext = context;
        this.mDialogInterface = di;
        this.mWindow = window;
        this.mHandler = new ButtonHandler(di);
        TypedArray a = context.obtainStyledAttributes((AttributeSet) null, R.styleable.TvAlertDialog, R.attr.alertDialogStyle, 0);
        this.mAlertDialogLayout = a.getResourceId(R.styleable.TvAlertDialog_layout, R.layout.tui_alert_dialog);
        this.mListLayout = a.getResourceId(R.styleable.TvAlertDialog_listLayout, R.layout.tui_select_dialog);
        this.mMultiChoiceItemLayout = a.getResourceId(R.styleable.TvAlertDialog_multiChoiceItemLayout, R.layout.select_dialog_multichoice);
        this.mSingleChoiceItemLayout = a.getResourceId(R.styleable.TvAlertDialog_singleChoiceItemLayout, R.layout.select_dialog_singlechoice);
        this.mListItemLayout = a.getResourceId(R.styleable.TvAlertDialog_listItemLayout, R.layout.tui_alert_notification_list_item);
        a.recycle();
    }

    static boolean canTextInput(View v) {
        if (v.onCheckIsTextEditor()) {
            return true;
        }
        if (!(v instanceof ViewGroup)) {
            return false;
        }
        ViewGroup vg = (ViewGroup) v;
        int i = vg.getChildCount();
        while (i > 0) {
            i--;
            if (canTextInput(vg.getChildAt(i))) {
                return true;
            }
        }
        return false;
    }

    public void installContent() {
        this.mWindow.requestFeature(1);
        if (this.mView == null || !canTextInput(this.mView)) {
            this.mWindow.setFlags(131072, 131072);
        }
        this.mWindow.setContentView(this.mAlertDialogLayout);
        setupView();
    }

    public void setBackground(Bitmap bmp) {
        TransitionDrawable td;
        if (bmp != null && (td = new TransitionDrawable(new Drawable[]{this.mContext.getResources().getDrawable(R.drawable.drawable_transparent), new BitmapDrawable((Resources) null, bmp)})) != null && this.mAlertDialogBgView != null) {
            td.startTransition(500);
            this.mAlertDialogBgView.setBackgroundDrawable(td);
        }
    }

    public void setTitle(CharSequence title) {
        this.mTitle = title;
        if (this.mTitleView != null) {
            this.mTitleView.setText(title);
        }
    }

    public void setCustomTitle(View customTitleView) {
        this.mCustomTitleView = customTitleView;
    }

    public void setMessage(CharSequence message) {
        this.mMessage = message;
        if (this.mMessageView != null) {
            this.mMessageView.setText(message);
        }
    }

    public void setView(View view) {
        this.mView = view;
        this.mViewSpacingSpecified = false;
    }

    public void setView(View view, int viewSpacingLeft, int viewSpacingTop, int viewSpacingRight, int viewSpacingBottom) {
        this.mView = view;
        this.mViewSpacingSpecified = true;
        this.mViewSpacingLeft = viewSpacingLeft;
        this.mViewSpacingTop = viewSpacingTop;
        this.mViewSpacingRight = viewSpacingRight;
        this.mViewSpacingBottom = viewSpacingBottom;
    }

    public void setButton(int whichButton, CharSequence text, DialogInterface.OnClickListener listener, Message msg) {
        if (msg == null && listener != null) {
            msg = this.mHandler.obtainMessage(whichButton, listener);
        }
        switch (whichButton) {
            case -3:
                this.mButtonNeutralText = text;
                this.mButtonNeutralMessage = msg;
                return;
            case -2:
                this.mButtonNegativeText = text;
                this.mButtonNegativeMessage = msg;
                return;
            case -1:
                this.mButtonPositiveText = text;
                this.mButtonPositiveMessage = msg;
                return;
            default:
                throw new IllegalArgumentException("Button does not exist");
        }
    }

    public void setCustomerButton(CharSequence[] texts, DialogInterface.OnClickListener listener, Message msg) {
        if (msg == null && listener != null) {
            msg = this.mHandler.obtainMessage(2, listener);
        }
        this.mCustomerButtons = texts;
        this.mCustomerButtonsMsg = msg;
    }

    public void setIcon(int resId) {
        this.mIconId = resId;
        if (this.mIconView == null) {
            return;
        }
        if (resId > 0) {
            this.mIconView.setImageResource(this.mIconId);
        } else if (resId == 0) {
            this.mIconView.setVisibility(8);
        }
    }

    public void setIcon(Drawable icon) {
        this.mIcon = icon;
        if (this.mIconView != null && this.mIcon != null) {
            this.mIconView.setImageDrawable(icon);
        }
    }

    public int getIconAttributeResId(int attrId) {
        TypedValue out = new TypedValue();
        this.mContext.getTheme().resolveAttribute(attrId, out, true);
        return out.resourceId;
    }

    public void setInverseBackgroundForced(boolean forceInverseBackground) {
        this.mForceInverseBackground = forceInverseBackground;
    }

    public ListView getListView() {
        return this.mListView;
    }

    public Button getButton(int whichButton) {
        switch (whichButton) {
            case -3:
                return this.mButtonNeutral;
            case -2:
                return this.mButtonNegative;
            case -1:
                return this.mButtonPositive;
            default:
                return null;
        }
    }

    public boolean onKeyDown(int keyCode, KeyEvent event) {
        return this.mScrollView != null && this.mScrollView.executeKeyEvent(event);
    }

    public boolean onKeyUp(int keyCode, KeyEvent event) {
        return this.mScrollView != null && this.mScrollView.executeKeyEvent(event);
    }

    private void setupView() {
        this.mAlertDialogBgView = this.mWindow.findViewById(R.id.alert_dialog_layout);
        LinearLayout contentPanel = (LinearLayout) this.mWindow.findViewById(R.id.contentPanel);
        setupContent(contentPanel);
        boolean hasButtons = setupButtons();
        LinearLayout topPanel = (LinearLayout) this.mWindow.findViewById(R.id.topPanel);
        TypedArray a = this.mContext.obtainStyledAttributes((AttributeSet) null, R.styleable.TvAlertDialog, R.attr.alertDialogStyle, 0);
        boolean hasTitle = setupTitle(topPanel);
        FocusPositionManager buttonPanel = (FocusPositionManager) this.mWindow.findViewById(R.id.buttonPanel);
        if (!hasButtons) {
            buttonPanel.setVisibility(8);
        } else {
            buttonPanel.setSelector(new StaticFocusDrawable(this.mContext.getResources().getDrawable(R.drawable.tui_dialog_focus_selector)));
            buttonPanel.requestFocus();
        }
        FrameLayout customPanel = null;
        if (this.mView != null) {
            customPanel = (FrameLayout) this.mWindow.findViewById(R.id.customPanel);
            FrameLayout custom = (FrameLayout) this.mWindow.findViewById(R.id.custom);
            custom.addView(this.mView, new ViewGroup.LayoutParams(-1, -1));
            if (this.mViewSpacingSpecified) {
                custom.setPadding(this.mViewSpacingLeft, this.mViewSpacingTop, this.mViewSpacingRight, this.mViewSpacingBottom);
            }
            if (this.mListView != null) {
                ((LinearLayout.LayoutParams) customPanel.getLayoutParams()).weight = 0.0f;
            }
        } else {
            this.mWindow.findViewById(R.id.customPanel).setVisibility(8);
        }
        setBackground(topPanel, contentPanel, customPanel, false, a, hasTitle, buttonPanel);
        a.recycle();
    }

    private boolean setupTitle(LinearLayout topPanel) {
        boolean hasTextTitle = false;
        if (this.mCustomTitleView != null) {
            topPanel.addView(this.mCustomTitleView, 0, new LinearLayout.LayoutParams(-1, -2));
            this.mWindow.findViewById(R.id.title_template).setVisibility(8);
            return true;
        }
        if (!TextUtils.isEmpty(this.mTitle)) {
            hasTextTitle = true;
        }
        this.mIconView = (ImageView) this.mWindow.findViewById(R.id.icon);
        if (hasTextTitle) {
            this.mTitleView = (TextView) this.mWindow.findViewById(R.id.alertTitle);
            this.mTitleView.setText(this.mTitle);
            if (this.mIconId > 0) {
                this.mIconView.setImageResource(this.mIconId);
                return true;
            } else if (this.mIcon != null) {
                this.mIconView.setImageDrawable(this.mIcon);
                return true;
            } else if (this.mIconId != 0) {
                return true;
            } else {
                this.mTitleView.setPadding(this.mIconView.getPaddingLeft(), this.mIconView.getPaddingTop(), this.mIconView.getPaddingRight(), this.mIconView.getPaddingBottom());
                this.mIconView.setVisibility(8);
                return true;
            }
        } else {
            this.mWindow.findViewById(R.id.title_template).setVisibility(8);
            this.mIconView.setVisibility(8);
            topPanel.setVisibility(8);
            return false;
        }
    }

    private void setupContent(LinearLayout contentPanel) {
        this.mScrollView = (ScrollView) this.mWindow.findViewById(R.id.scrollView);
        this.mScrollView.setFocusable(false);
        this.mMessageView = (TextView) this.mWindow.findViewById(R.id.message);
        if (this.mMessageView != null) {
            if (this.mMessage != null) {
                this.mMessageView.setText(this.mMessage);
            } else {
                this.mMessageView.setVisibility(8);
                this.mScrollView.removeView(this.mMessageView);
                if (this.mListView == null) {
                    contentPanel.setVisibility(8);
                }
            }
            if (this.mListView != null) {
                int marginTop = this.mContext.getResources().getDimensionPixelSize(R.dimen.tui_alert_list_margin_top);
                int marginTopBackHint = this.mContext.getResources().getDimensionPixelSize(R.dimen.tui_alert_list_margin_top_backhint);
                contentPanel.removeView(this.mWindow.findViewById(R.id.scrollView));
                LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(-1, -1);
                if (this.mMessage == null) {
                    lp.setMargins(0, marginTop, 0, 0);
                } else {
                    lp.setMargins(0, marginTopBackHint, 0, 0);
                }
                contentPanel.addView(this.mListView, lp);
            }
        }
    }

    private boolean setupButtons() {
        int whichButtons = 0;
        this.mButtonPositive = (Button) this.mWindow.findViewById(R.id.button1);
        this.mButtonPositive.setOnClickListener(this.mButtonHandler);
        View mButtonPositiveBg = this.mWindow.findViewById(R.id.button1_background);
        if (TextUtils.isEmpty(this.mButtonPositiveText)) {
            this.mButtonPositive.setVisibility(8);
            if (mButtonPositiveBg != null) {
                mButtonPositiveBg.setVisibility(8);
            }
        } else {
            this.mButtonPositive.setText(this.mButtonPositiveText);
            this.mButtonPositive.setVisibility(0);
            if (mButtonPositiveBg != null) {
                mButtonPositiveBg.setVisibility(0);
            }
            whichButtons = 0 | 1;
            this.mBtnNum++;
        }
        this.mButtonNegative = (Button) this.mWindow.findViewById(R.id.button2);
        this.mButtonNegative.setOnClickListener(this.mButtonHandler);
        View mButtonNegativeBg = this.mWindow.findViewById(R.id.button2_background);
        if (TextUtils.isEmpty(this.mButtonNegativeText)) {
            this.mButtonNegative.setVisibility(8);
            if (mButtonNegativeBg != null) {
                mButtonNegativeBg.setVisibility(8);
            }
        } else {
            this.mButtonNegative.setText(this.mButtonNegativeText);
            this.mButtonNegative.setVisibility(0);
            if (mButtonNegativeBg != null) {
                mButtonNegativeBg.setVisibility(0);
            }
            whichButtons |= 2;
            this.mBtnNum++;
        }
        this.mButtonNeutral = (Button) this.mWindow.findViewById(R.id.button3);
        this.mButtonNeutral.setOnClickListener(this.mButtonHandler);
        View mButtonNeutralBg = this.mWindow.findViewById(R.id.button3_background);
        if (TextUtils.isEmpty(this.mButtonNeutralText)) {
            this.mButtonNeutral.setVisibility(8);
            if (mButtonNeutralBg != null) {
                mButtonNeutralBg.setVisibility(8);
            }
        } else {
            this.mButtonNeutral.setText(this.mButtonNeutralText);
            this.mButtonNeutral.setVisibility(0);
            if (mButtonNeutralBg != null) {
                mButtonNeutralBg.setVisibility(0);
            }
            whichButtons |= 4;
            this.mBtnNum++;
        }
        if (this.mCustomerButtons != null && this.mCustomerButtons.length > 0) {
            ViewGroup buttonContainer = (ViewGroup) this.mWindow.findViewById(R.id.buttonContainer);
            int index = 0;
            int width = this.mContext.getResources().getDimensionPixelSize(R.dimen.alert_dialog_btn_width);
            int height = this.mContext.getResources().getDimensionPixelSize(R.dimen.alert_dialog_btn_height);
            CharSequence[] charSequenceArr = this.mCustomerButtons;
            int length = charSequenceArr.length;
            for (int i = 0; i < length; i++) {
                CharSequence customer = charSequenceArr[i];
                Button button = (Button) LayoutInflater.from(this.mContext).inflate(R.layout.alert_dialog_button, (ViewGroup) null);
                button.setLayoutParams(new ViewGroup.LayoutParams(width, height));
                button.setTag(Integer.valueOf(index));
                button.setOnClickListener(this.mButtonHandler);
                button.setText(customer);
                buttonContainer.addView(button);
                index++;
                this.mBtnNum++;
            }
            whichButtons |= 8;
        }
        if (shouldCenterSingleButton(this.mContext)) {
            if (whichButtons == 1) {
                centerButton(this.mButtonPositive);
            } else if (whichButtons == 2) {
                centerButton(this.mButtonNeutral);
            } else if (whichButtons == 4) {
                centerButton(this.mButtonNeutral);
            }
        }
        if (whichButtons != 0) {
            return true;
        }
        return false;
    }

    private void centerButton(Button button) {
        LinearLayout.LayoutParams params = (LinearLayout.LayoutParams) button.getLayoutParams();
        params.gravity = 1;
        params.weight = 0.5f;
        button.setLayoutParams(params);
        View leftSpacer = this.mWindow.findViewById(R.id.leftSpacer);
        if (leftSpacer != null) {
            leftSpacer.setVisibility(0);
        }
        View rightSpacer = this.mWindow.findViewById(R.id.rightSpacer);
        if (rightSpacer != null) {
            rightSpacer.setVisibility(0);
        }
    }

    private void setBackground(LinearLayout topPanel, LinearLayout contentPanel, View customPanel, boolean hasButtons, TypedArray a, boolean hasTitle, View buttonPanel) {
        int fullDark = a.getResourceId(R.styleable.TvAlertDialog_fullDark, 17170445);
        int topDark = a.getResourceId(R.styleable.TvAlertDialog_topDark, 17170445);
        int centerDark = a.getResourceId(R.styleable.TvAlertDialog_centerDark, 17170445);
        int bottomDark = a.getResourceId(R.styleable.TvAlertDialog_bottomDark, 17170445);
        int fullBright = a.getResourceId(R.styleable.TvAlertDialog_fullBright, 17170445);
        int topBright = a.getResourceId(R.styleable.TvAlertDialog_topBright, 17170445);
        int centerBright = a.getResourceId(R.styleable.TvAlertDialog_centerBright, 17170445);
        int bottomBright = a.getResourceId(R.styleable.TvAlertDialog_bottomBright, 17170445);
        int bottomMedium = a.getResourceId(R.styleable.TvAlertDialog_bottomMedium, 17170445);
        View[] views = new View[4];
        boolean[] light = new boolean[4];
        View lastView = null;
        boolean lastLight = false;
        int pos = 0;
        if (hasTitle) {
            views[0] = topPanel;
            light[0] = false;
            pos = 0 + 1;
        }
        if (contentPanel.getVisibility() == 8) {
            contentPanel = null;
        }
        views[pos] = contentPanel;
        light[pos] = this.mListView != null;
        int pos2 = pos + 1;
        if (customPanel != null) {
            views[pos2] = customPanel;
            light[pos2] = this.mForceInverseBackground;
            pos2++;
        }
        if (hasButtons) {
            views[pos2] = buttonPanel;
            light[pos2] = true;
        }
        boolean setView = false;
        for (int pos3 = 0; pos3 < views.length; pos3++) {
            View v = views[pos3];
            if (v != null) {
                if (lastView != null) {
                    if (!setView) {
                        lastView.setBackgroundResource(lastLight ? topBright : topDark);
                    } else {
                        lastView.setBackgroundResource(lastLight ? centerBright : centerDark);
                    }
                    setView = true;
                }
                lastView = v;
                lastLight = light[pos3];
            }
        }
        if (lastView != null) {
            if (setView) {
                if (!lastLight) {
                    bottomMedium = bottomDark;
                } else if (!hasButtons) {
                    bottomMedium = bottomBright;
                }
                lastView.setBackgroundResource(bottomMedium);
            } else {
                if (!lastLight) {
                    fullBright = fullDark;
                }
                lastView.setBackgroundResource(fullBright);
            }
        }
        if (this.mListView != null && this.mAdapter != null) {
            this.mListView.setAdapter(this.mAdapter);
            if (this.mCheckedItem > -1) {
                this.mListView.setItemChecked(this.mCheckedItem, true);
                this.mListView.setSelection(this.mCheckedItem);
            }
        }
    }

    public static class RecycleListView extends ListView {
        boolean mRecycleOnMeasure = true;

        public RecycleListView(Context context) {
            super(context);
        }

        public RecycleListView(Context context, AttributeSet attrs) {
            super(context, attrs);
        }

        public RecycleListView(Context context, AttributeSet attrs, int defStyle) {
            super(context, attrs, defStyle);
        }
    }

    public static class AlertParams {
        public ListAdapter mAdapter;
        public boolean mCancelable;
        public int mCheckedItem = -1;
        public boolean[] mCheckedItems;
        public final Context mContext;
        public Cursor mCursor;
        public DialogInterface.OnClickListener mCustomItemClickListener;
        public CharSequence[] mCustomItems;
        public View mCustomTitleView;
        public boolean mForceInverseBackground;
        public Drawable mIcon;
        public int mIconAttrId = 0;
        public int mIconId = 0;
        public final LayoutInflater mInflater;
        public String mIsCheckedColumn;
        public boolean mIsMultiChoice;
        public boolean mIsSingleChoice;
        public CharSequence[] mItems;
        public String mLabelColumn;
        public TextView mLatSelectedItem = null;
        public CharSequence mMessage;
        public DialogInterface.OnClickListener mNegativeButtonListener;
        public CharSequence mNegativeButtonText;
        public DialogInterface.OnClickListener mNeutralButtonListener;
        public CharSequence mNeutralButtonText;
        public DialogInterface.OnCancelListener mOnCancelListener;
        public DialogInterface.OnMultiChoiceClickListener mOnCheckboxClickListener;
        public DialogInterface.OnClickListener mOnClickListener;
        public DialogInterface.OnDismissListener mOnDismissListener;
        public AdapterView.OnItemSelectedListener mOnItemSelectedListener;
        public DialogInterface.OnKeyListener mOnKeyListener;
        public OnPrepareListViewListener mOnPrepareListViewListener;
        public DialogInterface.OnClickListener mPositiveButtonListener;
        public CharSequence mPositiveButtonText;
        public boolean mRecycleOnMeasure = true;
        public CharSequence mTitle;
        public View mView;
        public int mViewSpacingBottom;
        public int mViewSpacingLeft;
        public int mViewSpacingRight;
        public boolean mViewSpacingSpecified = false;
        public int mViewSpacingTop;

        public interface OnPrepareListViewListener {
            void onPrepareListView(ListView listView);
        }

        public AlertParams(Context context) {
            this.mContext = context;
            this.mCancelable = true;
            this.mInflater = (LayoutInflater) context.getSystemService("layout_inflater");
        }

        public void apply(AlertController dialog) {
            if (this.mCustomTitleView != null) {
                dialog.setCustomTitle(this.mCustomTitleView);
            } else {
                if (this.mTitle != null) {
                    dialog.setTitle(this.mTitle);
                }
                if (this.mIcon != null) {
                    dialog.setIcon(this.mIcon);
                }
                if (this.mIconId >= 0) {
                    dialog.setIcon(this.mIconId);
                }
                if (this.mIconAttrId > 0) {
                    dialog.setIcon(dialog.getIconAttributeResId(this.mIconAttrId));
                }
            }
            if (this.mMessage != null) {
                dialog.setMessage(this.mMessage);
            }
            if (this.mPositiveButtonText != null) {
                dialog.setButton(-1, this.mPositiveButtonText, this.mPositiveButtonListener, (Message) null);
            }
            if (this.mNegativeButtonText != null) {
                dialog.setButton(-2, this.mNegativeButtonText, this.mNegativeButtonListener, (Message) null);
            }
            if (this.mNeutralButtonText != null) {
                dialog.setButton(-3, this.mNeutralButtonText, this.mNeutralButtonListener, (Message) null);
            }
            if (this.mCustomItems != null) {
                dialog.setCustomerButton(this.mCustomItems, this.mCustomItemClickListener, (Message) null);
            }
            if (this.mForceInverseBackground) {
                dialog.setInverseBackgroundForced(true);
            }
            if (!(this.mItems == null && this.mCursor == null && this.mAdapter == null)) {
                createListView(dialog);
            }
            if (this.mView == null) {
                return;
            }
            if (this.mViewSpacingSpecified) {
                dialog.setView(this.mView, this.mViewSpacingLeft, this.mViewSpacingTop, this.mViewSpacingRight, this.mViewSpacingBottom);
                return;
            }
            dialog.setView(this.mView);
        }

        /* JADX DEBUG: Multi-variable search result rejected for TypeSearchVarInfo{r0v0, resolved type: android.widget.SimpleCursorAdapter} */
        /* JADX DEBUG: Multi-variable search result rejected for TypeSearchVarInfo{r0v1, resolved type: android.widget.ArrayAdapter} */
        /* JADX DEBUG: Multi-variable search result rejected for TypeSearchVarInfo{r0v2, resolved type: android.widget.ListAdapter} */
        /* JADX DEBUG: Multi-variable search result rejected for TypeSearchVarInfo{r0v5, resolved type: com.yunos.tvtaobao.tvsdk.widget.dialog.AlertController$AlertParams$1} */
        /* JADX DEBUG: Multi-variable search result rejected for TypeSearchVarInfo{r0v6, resolved type: com.yunos.tvtaobao.tvsdk.widget.dialog.AlertController$AlertParams$2} */
        /* JADX DEBUG: Multi-variable search result rejected for TypeSearchVarInfo{r0v7, resolved type: com.yunos.tvtaobao.tvsdk.widget.dialog.AlertController$AlertParams$2} */
        /* JADX DEBUG: Multi-variable search result rejected for TypeSearchVarInfo{r0v8, resolved type: com.yunos.tvtaobao.tvsdk.widget.dialog.AlertController$AlertParams$2} */
        /* JADX DEBUG: Multi-variable search result rejected for TypeSearchVarInfo{r1v26, resolved type: com.yunos.tvtaobao.tvsdk.widget.dialog.AlertController$AlertParams$2} */
        /* JADX DEBUG: Multi-variable search result rejected for TypeSearchVarInfo{r0v9, resolved type: com.yunos.tvtaobao.tvsdk.widget.dialog.AlertController$AlertParams$2} */
        /* JADX WARNING: type inference failed for: r0v3, types: [android.widget.ListAdapter] */
        /* JADX WARNING: Multi-variable type inference failed */
        /* Code decompiled incorrectly, please refer to instructions dump. */
        private void createListView(final com.yunos.tvtaobao.tvsdk.widget.dialog.AlertController r11) {
            /*
                r10 = this;
                r4 = 16908308(0x1020014, float:2.3877285E-38)
                r9 = 1
                r5 = 0
                android.view.LayoutInflater r1 = r10.mInflater
                int r3 = r11.mListLayout
                r7 = 0
                android.view.View r6 = r1.inflate(r3, r7)
                com.yunos.tvtaobao.tvsdk.widget.dialog.AlertController$RecycleListView r6 = (com.yunos.tvtaobao.tvsdk.widget.dialog.AlertController.RecycleListView) r6
                android.content.Context r1 = r10.mContext
                if (r1 == 0) goto L_0x0025
                android.content.Context r1 = r10.mContext
                android.content.res.Resources r1 = r1.getResources()
                int r3 = com.yunos.tv.aliTvSdk.R.drawable.tui_dialog_focus_selector
                android.graphics.drawable.Drawable r1 = r1.getDrawable(r3)
                r6.setSelector(r1)
            L_0x0025:
                boolean r1 = r10.mIsMultiChoice
                if (r1 == 0) goto L_0x007c
                android.database.Cursor r1 = r10.mCursor
                if (r1 != 0) goto L_0x006f
                com.yunos.tvtaobao.tvsdk.widget.dialog.AlertController$AlertParams$1 r0 = new com.yunos.tvtaobao.tvsdk.widget.dialog.AlertController$AlertParams$1
                android.content.Context r2 = r10.mContext
                int r3 = r11.mMultiChoiceItemLayout
                java.lang.CharSequence[] r5 = r10.mItems
                r1 = r10
                r0.<init>(r2, r3, r4, r5, r6)
            L_0x003b:
                com.yunos.tvtaobao.tvsdk.widget.dialog.AlertController$AlertParams$OnPrepareListViewListener r1 = r10.mOnPrepareListViewListener
                if (r1 == 0) goto L_0x0044
                com.yunos.tvtaobao.tvsdk.widget.dialog.AlertController$AlertParams$OnPrepareListViewListener r1 = r10.mOnPrepareListViewListener
                r1.onPrepareListView(r6)
            L_0x0044:
                android.widget.ListAdapter unused = r11.mAdapter = r0
                int r1 = r10.mCheckedItem
                int unused = r11.mCheckedItem = r1
                android.content.DialogInterface$OnClickListener r1 = r10.mOnClickListener
                if (r1 == 0) goto L_0x00b6
                com.yunos.tvtaobao.tvsdk.widget.dialog.AlertController$AlertParams$3 r1 = new com.yunos.tvtaobao.tvsdk.widget.dialog.AlertController$AlertParams$3
                r1.<init>(r11)
                r6.setOnItemClickListener(r1)
            L_0x0058:
                com.yunos.tvtaobao.tvsdk.widget.dialog.AlertController$AlertParams$5 r1 = new com.yunos.tvtaobao.tvsdk.widget.dialog.AlertController$AlertParams$5
                r1.<init>(r6)
                r6.setOnItemSelectedListener(r1)
                boolean r1 = r10.mIsSingleChoice
                if (r1 == 0) goto L_0x00c3
                r6.setChoiceMode(r9)
            L_0x0067:
                boolean r1 = r10.mRecycleOnMeasure
                r6.mRecycleOnMeasure = r1
                android.widget.ListView unused = r11.mListView = r6
                return
            L_0x006f:
                com.yunos.tvtaobao.tvsdk.widget.dialog.AlertController$AlertParams$2 r0 = new com.yunos.tvtaobao.tvsdk.widget.dialog.AlertController$AlertParams$2
                android.content.Context r3 = r10.mContext
                android.database.Cursor r4 = r10.mCursor
                r1 = r0
                r2 = r10
                r7 = r11
                r1.<init>(r3, r4, r5, r6, r7)
                goto L_0x003b
            L_0x007c:
                boolean r1 = r10.mIsSingleChoice
                if (r1 == 0) goto L_0x008f
                int r2 = r11.mSingleChoiceItemLayout
            L_0x0084:
                android.database.Cursor r1 = r10.mCursor
                if (r1 != 0) goto L_0x00a0
                android.widget.ListAdapter r1 = r10.mAdapter
                if (r1 == 0) goto L_0x0094
                android.widget.ListAdapter r0 = r10.mAdapter
            L_0x008e:
                goto L_0x003b
            L_0x008f:
                int r2 = r11.mListItemLayout
                goto L_0x0084
            L_0x0094:
                android.widget.ArrayAdapter r0 = new android.widget.ArrayAdapter
                android.content.Context r1 = r10.mContext
                int r3 = com.yunos.tv.aliTvSdk.R.id.text1
                java.lang.CharSequence[] r4 = r10.mItems
                r0.<init>(r1, r2, r3, r4)
                goto L_0x008e
            L_0x00a0:
                android.widget.SimpleCursorAdapter r0 = new android.widget.SimpleCursorAdapter
                android.content.Context r1 = r10.mContext
                android.database.Cursor r3 = r10.mCursor
                java.lang.String[] r7 = new java.lang.String[r9]
                java.lang.String r8 = r10.mLabelColumn
                r7[r5] = r8
                int[] r8 = new int[r9]
                r8[r5] = r4
                r4 = r7
                r5 = r8
                r0.<init>(r1, r2, r3, r4, r5)
                goto L_0x003b
            L_0x00b6:
                android.content.DialogInterface$OnMultiChoiceClickListener r1 = r10.mOnCheckboxClickListener
                if (r1 == 0) goto L_0x0058
                com.yunos.tvtaobao.tvsdk.widget.dialog.AlertController$AlertParams$4 r1 = new com.yunos.tvtaobao.tvsdk.widget.dialog.AlertController$AlertParams$4
                r1.<init>(r6, r11)
                r6.setOnItemClickListener(r1)
                goto L_0x0058
            L_0x00c3:
                boolean r1 = r10.mIsMultiChoice
                if (r1 == 0) goto L_0x0067
                r1 = 2
                r6.setChoiceMode(r1)
                goto L_0x0067
            */
            throw new UnsupportedOperationException("Method not decompiled: com.yunos.tvtaobao.tvsdk.widget.dialog.AlertController.AlertParams.createListView(com.yunos.tvtaobao.tvsdk.widget.dialog.AlertController):void");
        }
    }
}
