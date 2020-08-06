package com.tvtaobao.android.takeoutwares;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;
import java.util.ArrayList;
import java.util.List;

public class AddAndReduceView extends FrameLayout {
    private static final int FLAG_apply_ing = 4;
    private static final int FLAG_requestChildFocus_ing = 1;
    private static final int FLAG_syncState_ing = 2;
    private static final String TAG = AddAndReduceView.class.getSimpleName();
    /* access modifiers changed from: private */
    public AARVBtnClickListener AARVBtnClickListener;
    private List<AARVFocusChangeListener> AARVFocusChangeListenerList;
    private int btnHeightInPx;
    private BtnStyle btnStyle;
    private int btnWidthInPx;
    private int countColor;
    private int countSizeInPx;
    private CountStyle countStyle;
    private int countValue;
    private Runnable delayApply;
    /* access modifiers changed from: private */
    public volatile int flag;
    private FocusPos focusPos;
    /* access modifiers changed from: private */
    public ImageView goodAdd;
    private TextView goodCount;
    /* access modifiers changed from: private */
    public ImageView goodReduce;
    private int initBtnHeightInPx;
    private BtnStyle initBtnStyle;
    private int initBtnWidthInPx;
    private int initCountColor;
    private int initCountSizeInPx;
    private CountStyle initCountStyle;
    private int initCountValue;
    private Style initStyle;
    private boolean mergedFocusState;
    /* access modifiers changed from: private */
    public View.OnFocusChangeListener onFocusChangeListener;
    private Style style;
    private Runnable syncTask;
    /* access modifiers changed from: private */
    public Runnable syncTaskTail;

    public interface AARVBtnClickListener {
        void onAddClick();

        void onReduceClick();
    }

    public interface AARVFocusChangeListener {
        void onFocusChange(boolean z);
    }

    public enum BtnStyle {
        inGoodItem,
        inCartItem
    }

    public enum CountStyle {
        withX,
        noX
    }

    public enum FocusPos {
        self,
        addBtn,
        reduceBtn
    }

    public enum Style {
        lmr,
        _mr,
        __r,
        ___,
        _m_
    }

    public AddAndReduceView(Context context) {
        this(context, (AttributeSet) null);
    }

    public AddAndReduceView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public AddAndReduceView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        this.initCountColor = -1;
        this.initCountSizeInPx = -1;
        this.initCountValue = -1;
        this.initBtnWidthInPx = -1;
        this.initBtnHeightInPx = -1;
        this.countColor = -1;
        this.countSizeInPx = -1;
        this.countValue = -1;
        this.btnWidthInPx = -1;
        this.btnHeightInPx = -1;
        this.flag = 0;
        this.mergedFocusState = false;
        this.AARVFocusChangeListenerList = new ArrayList();
        this.onFocusChangeListener = new View.OnFocusChangeListener() {
            public void onFocusChange(View v, boolean hasFocus) {
                if (AddAndReduceView.this.goodAdd.hasFocus() || AddAndReduceView.this.goodReduce.hasFocus()) {
                    AddAndReduceView.this.dispatchFocusChange(AddAndReduceView.this.hasFocus(), "arBtn");
                } else {
                    AddAndReduceView.this.dispatchFocusChange(TOWUtil.isViewHasFocus(AddAndReduceView.this), "arBtn");
                }
            }
        };
        this.syncTask = new Runnable() {
            public void run() {
                AddAndReduceView.this.syncState();
                synchronized (this) {
                    int unused = AddAndReduceView.this.flag = AddAndReduceView.this.flag & -5;
                }
            }
        };
        this.syncTaskTail = null;
        this.delayApply = new Runnable() {
            public void run() {
                AddAndReduceView.this.apply((Runnable) null);
            }
        };
        this.initCountStyle = CountStyle.noX;
        this.initStyle = Style.lmr;
        this.initBtnStyle = BtnStyle.inGoodItem;
        this.initCountColor = -1;
        this.initCountSizeInPx = context.getResources().getDimensionPixelSize(R.dimen.values_dp_24);
        this.initCountValue = -1;
        this.initBtnWidthInPx = context.getResources().getDimensionPixelSize(R.dimen.values_dp_24);
        this.initBtnHeightInPx = this.initBtnWidthInPx;
        if (!(context == null || attrs == null)) {
            TypedArray ta = context.obtainStyledAttributes(attrs, R.styleable.takeoutwares);
            int tmpVar = ta.getInteger(R.styleable.takeoutwares_takeoutwares_addAndReduceViewBtnStyle, this.initBtnStyle.ordinal());
            this.initBtnStyle = (tmpVar < 0 || tmpVar >= BtnStyle.values().length) ? this.initBtnStyle : BtnStyle.values()[tmpVar];
            int tmpVar2 = ta.getInteger(R.styleable.takeoutwares_takeoutwares_addAndReduceViewStyle, this.initStyle.ordinal());
            this.initStyle = (tmpVar2 < 0 || tmpVar2 >= Style.values().length) ? this.initStyle : Style.values()[tmpVar2];
            int tmpVar3 = ta.getInteger(R.styleable.takeoutwares_takeoutwares_addAndReduceViewCountStyle, this.initCountStyle.ordinal());
            this.initCountStyle = (tmpVar3 < 0 || tmpVar3 >= CountStyle.values().length) ? this.initCountStyle : CountStyle.values()[tmpVar3];
            this.initCountValue = ta.getInteger(R.styleable.takeoutwares_takeoutwares_addAndReduceViewCountValue, this.initCountValue);
            this.initCountColor = ta.getColor(R.styleable.takeoutwares_takeoutwares_addAndReduceViewCountColor, this.initCountColor);
            this.initCountSizeInPx = ta.getDimensionPixelSize(R.styleable.takeoutwares_takeoutwares_addAndReduceViewTxtSize, this.initCountSizeInPx);
            this.initBtnWidthInPx = ta.getDimensionPixelSize(R.styleable.takeoutwares_takeoutwares_addAndReduceViewBtnSize, this.initBtnWidthInPx);
            ta.recycle();
        }
        LayoutInflater.from(getContext()).inflate(R.layout.takeoutwares_layout_add_and_reduce, this);
        findViews();
        syncState();
    }

    private void findViews() {
        this.goodAdd = (ImageView) findViewById(R.id.good_add);
        this.goodCount = (TextView) findViewById(R.id.good_count);
        this.goodReduce = (ImageView) findViewById(R.id.good_reduce);
        setFocusable(true);
        setDescendantFocusability(262144);
        setCountValue(this.initCountValue);
        setCountColor(this.initCountColor);
        setCountSizeInPx(this.initCountSizeInPx);
        setBtnWHInPx(this.initBtnWidthInPx, this.initBtnHeightInPx);
        setBtnStyle(this.initBtnStyle);
        setStyle(this.initStyle);
    }

    public void focusableViewAvailable(View v) {
        TOWLogger.i(TAG, "focusableViewAvailable " + TOWUtil.getString(v));
        if ((this.flag & 2) == 2) {
            TOWLogger.i(TAG, "focusableViewAvailable a");
            return;
        }
        super.focusableViewAvailable(v);
        TOWLogger.i(TAG, "focusableViewAvailable b");
    }

    public void requestChildFocus(View child, View focused) {
        TOWLogger.i(TAG, "requestChildFocus " + TOWUtil.getString(child) + "," + TOWUtil.getString(focused));
        this.flag |= 1;
        super.requestChildFocus(child, focused);
        this.flag &= -2;
    }

    /* access modifiers changed from: protected */
    public void onFocusChanged(boolean gainFocus, int direction, Rect previouslyFocusedRect) {
        TOWLogger.i(TAG, "onFocusChanged " + gainFocus + "," + direction + "," + previouslyFocusedRect);
        super.onFocusChanged(gainFocus, direction, previouslyFocusedRect);
        if ((this.flag & 1) != 1) {
            dispatchFocusChange(hasFocus(), "self");
        }
    }

    /* access modifiers changed from: protected */
    public boolean onRequestFocusInDescendants(int direction, Rect previouslyFocusedRect) {
        TOWLogger.i(TAG, "onRequestFocusInDescendants " + direction + "," + previouslyFocusedRect);
        boolean rtn = super.onRequestFocusInDescendants(direction, previouslyFocusedRect);
        TOWLogger.i(TAG, "onRequestFocusInDescendants rtn=" + rtn);
        return rtn;
    }

    public void addFocusables(ArrayList<View> views, int direction) {
        Integer num;
        Integer num2 = null;
        String str = TAG;
        StringBuilder append = new StringBuilder().append("addFocusables ");
        if (views != null) {
            num = Integer.valueOf(views.size());
        } else {
            num = null;
        }
        TOWLogger.i(str, append.append(num).append(",").append(direction).append(",").append(TOWUtil.getString(this)).toString());
        super.addFocusables(views, direction);
        String str2 = TAG;
        StringBuilder append2 = new StringBuilder().append("addFocusables done");
        if (views != null) {
            num2 = Integer.valueOf(views.size());
        }
        TOWLogger.i(str2, append2.append(num2).append(",").append(direction).toString());
    }

    public void clearChildFocus(View child) {
        TOWLogger.i(TAG, "clearChildFocus " + TOWUtil.getString(child));
        if ((this.flag & 2) == 2) {
            TOWLogger.i(TAG, "clearChildFocus a");
            return;
        }
        super.clearChildFocus(child);
        TOWLogger.i(TAG, "clearChildFocus b");
    }

    /* access modifiers changed from: private */
    public void syncState() {
        this.flag |= 2;
        try {
            int oldAddVisibleState = this.goodAdd.getVisibility();
            int oldReduceVisibleState = this.goodReduce.getVisibility();
            boolean oldAddFocusState = this.goodAdd.hasFocus();
            boolean oldReduceFocusState = this.goodReduce.hasFocus();
            if (this.style == Style.lmr) {
                this.goodAdd.setVisibility(0);
                this.goodCount.setVisibility(0);
                this.goodReduce.setVisibility(0);
            } else if (this.style == Style._mr) {
                this.goodAdd.setVisibility(0);
                this.goodCount.setVisibility(0);
                this.goodReduce.setVisibility(8);
            } else if (this.style == Style.__r) {
                this.goodAdd.setVisibility(0);
                this.goodCount.setVisibility(8);
                this.goodReduce.setVisibility(8);
            } else if (this.style == Style._m_) {
                this.goodAdd.setVisibility(8);
                this.goodCount.setVisibility(0);
                this.goodReduce.setVisibility(8);
            } else if (this.style == Style.___) {
                this.goodAdd.setVisibility(8);
                this.goodCount.setVisibility(8);
                this.goodReduce.setVisibility(8);
            }
            int newAddVisibleState = this.goodAdd.getVisibility();
            int newReduceVisibleState = this.goodReduce.getVisibility();
            this.goodAdd.setOnClickListener(new View.OnClickListener() {
                public void onClick(View v) {
                    if (AddAndReduceView.this.AARVBtnClickListener != null) {
                        AddAndReduceView.this.AARVBtnClickListener.onAddClick();
                    }
                }
            });
            this.goodReduce.setOnClickListener(new View.OnClickListener() {
                public void onClick(View v) {
                    if (AddAndReduceView.this.AARVBtnClickListener != null) {
                        AddAndReduceView.this.AARVBtnClickListener.onReduceClick();
                    }
                }
            });
            if (!(this.btnHeightInPx == this.goodAdd.getLayoutParams().height && this.btnWidthInPx == this.goodAdd.getLayoutParams().width)) {
                ViewGroup.LayoutParams lp = this.goodAdd.getLayoutParams();
                lp.width = this.btnWidthInPx;
                lp.height = this.btnHeightInPx;
                this.goodAdd.setLayoutParams(lp);
            }
            if (!(this.btnHeightInPx == this.goodReduce.getLayoutParams().height && this.btnWidthInPx == this.goodReduce.getLayoutParams().width)) {
                ViewGroup.LayoutParams lp2 = this.goodReduce.getLayoutParams();
                lp2.width = this.btnWidthInPx;
                lp2.height = this.btnHeightInPx;
                this.goodReduce.setLayoutParams(lp2);
            }
            if (this.btnStyle == BtnStyle.inCartItem) {
                if (this.goodAdd.getVisibility() == 0) {
                    this.goodAdd.setOnFocusChangeListener(new View.OnFocusChangeListener() {
                        public void onFocusChange(View v, boolean hasFocus) {
                            if (hasFocus) {
                                AddAndReduceView.this.goodAdd.setImageDrawable(AddAndReduceView.this.getResources().getDrawable(R.drawable.takeoutwares_incartitem_add_focus));
                            } else {
                                AddAndReduceView.this.goodAdd.setImageDrawable(AddAndReduceView.this.getResources().getDrawable(R.drawable.takeoutwares_incartitem_add_unfocus));
                            }
                            if (AddAndReduceView.this.onFocusChangeListener != null) {
                                AddAndReduceView.this.onFocusChangeListener.onFocusChange(v, hasFocus);
                            }
                        }
                    });
                    if (this.goodAdd.hasFocus()) {
                        this.goodAdd.setImageDrawable(getResources().getDrawable(R.drawable.takeoutwares_incartitem_add_focus));
                    } else {
                        this.goodAdd.setImageDrawable(getResources().getDrawable(R.drawable.takeoutwares_incartitem_add_unfocus));
                    }
                }
                if (this.goodReduce.getVisibility() == 0) {
                    this.goodReduce.setOnFocusChangeListener(new View.OnFocusChangeListener() {
                        public void onFocusChange(View v, boolean hasFocus) {
                            if (hasFocus) {
                                AddAndReduceView.this.goodReduce.setImageDrawable(AddAndReduceView.this.getResources().getDrawable(R.drawable.takeoutwares_incartitem_reduce_focus));
                            } else {
                                AddAndReduceView.this.goodReduce.setImageDrawable(AddAndReduceView.this.getResources().getDrawable(R.drawable.takeoutwares_incartitem_reduce_unfocus));
                            }
                            if (AddAndReduceView.this.onFocusChangeListener != null) {
                                AddAndReduceView.this.onFocusChangeListener.onFocusChange(v, hasFocus);
                            }
                        }
                    });
                    if (this.goodReduce.hasFocus()) {
                        this.goodReduce.setImageDrawable(getResources().getDrawable(R.drawable.takeoutwares_incartitem_reduce_focus));
                    } else {
                        this.goodReduce.setImageDrawable(getResources().getDrawable(R.drawable.takeoutwares_incartitem_reduce_unfocus));
                    }
                }
            } else if (this.btnStyle == BtnStyle.inGoodItem) {
                if (this.goodAdd.getVisibility() == 0) {
                    this.goodAdd.setOnFocusChangeListener(new View.OnFocusChangeListener() {
                        public void onFocusChange(View v, boolean hasFocus) {
                            if (hasFocus) {
                                AddAndReduceView.this.goodAdd.setImageDrawable(AddAndReduceView.this.getResources().getDrawable(R.drawable.takeoutwares_ingooditem_add_focus));
                            } else {
                                AddAndReduceView.this.goodAdd.setImageDrawable(AddAndReduceView.this.getResources().getDrawable(R.drawable.takeoutwares_ingooditem_add_unfocus));
                            }
                            if (AddAndReduceView.this.onFocusChangeListener != null) {
                                AddAndReduceView.this.onFocusChangeListener.onFocusChange(v, hasFocus);
                            }
                        }
                    });
                    if (this.goodAdd.hasFocus()) {
                        this.goodAdd.setImageDrawable(getResources().getDrawable(R.drawable.takeoutwares_ingooditem_add_focus));
                    } else {
                        this.goodAdd.setImageDrawable(getResources().getDrawable(R.drawable.takeoutwares_ingooditem_add_unfocus));
                    }
                }
                if (this.goodReduce.getVisibility() == 0) {
                    this.goodReduce.setOnFocusChangeListener(new View.OnFocusChangeListener() {
                        public void onFocusChange(View v, boolean hasFocus) {
                            if (hasFocus) {
                                AddAndReduceView.this.goodReduce.setImageDrawable(AddAndReduceView.this.getResources().getDrawable(R.drawable.takeoutwares_ingooditem_reduce_focus));
                            } else {
                                AddAndReduceView.this.goodReduce.setImageDrawable(AddAndReduceView.this.getResources().getDrawable(R.drawable.takeoutwares_ingooditem_reduce_unfocus));
                            }
                            if (AddAndReduceView.this.onFocusChangeListener != null) {
                                AddAndReduceView.this.onFocusChangeListener.onFocusChange(v, hasFocus);
                            }
                        }
                    });
                    if (this.goodReduce.hasFocus()) {
                        this.goodReduce.setImageDrawable(getResources().getDrawable(R.drawable.takeoutwares_ingooditem_reduce_focus));
                    } else {
                        this.goodReduce.setImageDrawable(getResources().getDrawable(R.drawable.takeoutwares_ingooditem_reduce_unfocus));
                    }
                }
            }
            if (this.countValue > 0) {
                this.goodCount.setVisibility(0);
                if (this.countStyle == CountStyle.withX) {
                    this.goodCount.setText("x" + this.countValue);
                } else {
                    this.goodCount.setText("" + this.countValue);
                }
            } else {
                this.goodCount.setText("");
            }
            if (this.countSizeInPx > 0) {
                this.goodCount.setVisibility(0);
                this.goodCount.setTextSize(0, (float) this.countSizeInPx);
            } else {
                this.goodCount.setVisibility(8);
            }
            this.goodCount.setTextColor(this.countColor);
            if (oldAddFocusState || oldReduceFocusState) {
                if (oldReduceVisibleState == 0 && newReduceVisibleState != 0 && oldReduceFocusState && newAddVisibleState == 0) {
                    this.goodAdd.requestFocus();
                } else if (oldAddVisibleState == 0 && newAddVisibleState != 0 && oldAddFocusState && newReduceVisibleState == 0) {
                    this.goodReduce.requestFocus();
                }
            } else if (this.focusPos != null) {
                if (this.focusPos == FocusPos.self) {
                    callSupperRequestFocus();
                } else if (this.focusPos == FocusPos.addBtn) {
                    this.goodAdd.requestFocus();
                } else if (this.focusPos == FocusPos.reduceBtn) {
                    this.goodReduce.requestFocus();
                }
            }
            this.goodAdd.postInvalidate();
            this.goodReduce.postInvalidate();
            TOWUtil.invalidateToRoot(this);
            post(new Runnable() {
                public void run() {
                    if (AddAndReduceView.this.syncTaskTail != null) {
                        try {
                            AddAndReduceView.this.syncTaskTail.run();
                        } catch (Throwable e) {
                            e.printStackTrace();
                        }
                        Runnable unused = AddAndReduceView.this.syncTaskTail = null;
                    }
                }
            });
        } catch (Throwable e) {
            e.printStackTrace();
        }
        this.flag &= -3;
    }

    private void callSupperRequestFocus() {
        super.requestFocus(130, (Rect) null);
    }

    private void doDelayApply() {
        removeCallbacks(this.delayApply);
        post(this.delayApply);
    }

    public boolean apply(Runnable taskWhenApplyOver) {
        removeCallbacks(this.delayApply);
        boolean shouldApply = false;
        if ((this.flag & 4) != 4) {
            synchronized (this) {
                if ((this.flag & 4) != 4) {
                    this.flag |= 4;
                    shouldApply = true;
                    this.syncTaskTail = taskWhenApplyOver;
                }
            }
        }
        if (!shouldApply) {
            return false;
        }
        post(this.syncTask);
        return true;
    }

    public int getCountColor() {
        return this.countColor;
    }

    public AddAndReduceView setCountColor(int countColor2) {
        this.countColor = countColor2;
        doDelayApply();
        return this;
    }

    public int getCountSizeInPx() {
        return this.countSizeInPx;
    }

    public AddAndReduceView setCountSizeInPx(int countSizeInPx2) {
        this.countSizeInPx = countSizeInPx2;
        doDelayApply();
        return this;
    }

    public int getCountValue() {
        return this.countValue;
    }

    public AddAndReduceView setCountValue(int countValue2) {
        this.countValue = countValue2;
        doDelayApply();
        return this;
    }

    public int getBtnWidthInPx() {
        return this.btnWidthInPx;
    }

    public int getBtnHeightInPx() {
        return this.btnHeightInPx;
    }

    public AddAndReduceView setBtnWHInPx(int w, int h) {
        this.btnWidthInPx = w;
        this.btnHeightInPx = h;
        doDelayApply();
        return this;
    }

    public AARVBtnClickListener getAARVBtnClickListener() {
        return this.AARVBtnClickListener;
    }

    public AddAndReduceView setAARVBtnClickListener(AARVBtnClickListener AARVBtnClickListener2) {
        this.AARVBtnClickListener = AARVBtnClickListener2;
        doDelayApply();
        return this;
    }

    public Style getStyle() {
        return this.style;
    }

    public AddAndReduceView setStyle(Style style2) {
        this.style = style2;
        doDelayApply();
        return this;
    }

    public BtnStyle getBtnStyle() {
        return this.btnStyle;
    }

    public CountStyle getCountStyle() {
        return this.countStyle;
    }

    public AddAndReduceView setCountStyle(CountStyle countStyle2) {
        this.countStyle = countStyle2;
        doDelayApply();
        return this;
    }

    public AddAndReduceView setBtnStyle(BtnStyle btnStyle2) {
        this.btnStyle = btnStyle2;
        doDelayApply();
        return this;
    }

    public AddAndReduceView rmvFocusChangeListener(AARVFocusChangeListener listener) {
        if (listener != null) {
            synchronized (this.AARVFocusChangeListenerList) {
                this.AARVFocusChangeListenerList.remove(listener);
            }
        }
        return this;
    }

    public AddAndReduceView addFocusChangeListener(AARVFocusChangeListener listener) {
        if (listener != null) {
            synchronized (this.AARVFocusChangeListenerList) {
                if (!this.AARVFocusChangeListenerList.contains(listener)) {
                    this.AARVFocusChangeListenerList.add(listener);
                }
            }
        }
        return this;
    }

    public FocusPos getFocusPos() {
        return this.focusPos;
    }

    public AddAndReduceView setFocusPos(FocusPos focusPos2) {
        this.focusPos = focusPos2;
        doDelayApply();
        return this;
    }

    /* access modifiers changed from: private */
    public void dispatchFocusChange(boolean hasFocus, String from) {
        TOWLogger.i(TAG, "dispatchFocusChange " + hasFocus + "," + from + "," + this.mergedFocusState);
        if (this.mergedFocusState != hasFocus) {
            this.mergedFocusState = hasFocus;
            if (!"self".equals(from) && "arBtn".equals(from)) {
            }
            if (!this.AARVFocusChangeListenerList.isEmpty()) {
                synchronized (this.AARVFocusChangeListenerList) {
                    for (int i = 0; i < this.AARVFocusChangeListenerList.size(); i++) {
                        AARVFocusChangeListener AARVFocusChangeListener2 = this.AARVFocusChangeListenerList.get(i);
                        if (AARVFocusChangeListener2 != null) {
                            AARVFocusChangeListener2.onFocusChange(hasFocus);
                        }
                    }
                }
            }
        }
    }

    public boolean getMergedFocusState() {
        return this.mergedFocusState;
    }

    public boolean isInEditMode() {
        if (TOWConfig.DEBUG) {
            return true;
        }
        return super.isInEditMode();
    }
}
