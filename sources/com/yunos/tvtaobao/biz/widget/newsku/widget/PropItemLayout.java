package com.yunos.tvtaobao.biz.widget.newsku.widget;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.support.v4.internal.view.SupportMenu;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;
import com.tvtaobao.android.ui3.widget.UI3Toast;
import com.yunos.tv.core.util.BitMapUtil;
import com.yunos.tvtaobao.businessview.R;
import java.util.List;

public class PropItemLayout extends LinearLayout {
    private static final String TAG = PropItemLayout.class.getSimpleName();
    /* access modifiers changed from: private */
    public IValueListener innerValueListener;
    /* access modifiers changed from: private */
    public IDesc lastSelected;
    private TextView nameTxt;
    private View.OnClickListener onClickListener;
    private IPropData propData;
    /* access modifiers changed from: private */
    public LinearLayout valueLayout;
    /* access modifiers changed from: private */
    public IValueListener valueListener;

    public interface IDesc {
        String getTxt();

        Object getValue();
    }

    public interface IPropData {
        IDesc getPropDesc();

        List<IDesc> getValueList();
    }

    public interface IValueListener {
        void onSelected(IDesc iDesc, boolean z);
    }

    public enum State {
        UNSELECT,
        SELECT,
        DISABLE
    }

    public PropItemLayout(Context context) {
        this(context, (AttributeSet) null);
    }

    public PropItemLayout(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public PropItemLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        this.innerValueListener = new IValueListener() {
            public void onSelected(IDesc value, boolean chose) {
                if (chose) {
                    IDesc unused = PropItemLayout.this.lastSelected = value;
                } else if (PropItemLayout.this.lastSelected == value) {
                    IDesc unused2 = PropItemLayout.this.lastSelected = null;
                }
                if (PropItemLayout.this.valueListener != null) {
                    PropItemLayout.this.valueListener.onSelected(value, chose);
                }
            }
        };
        this.onClickListener = new View.OnClickListener() {
            public void onClick(View view) {
                if ((view instanceof ValueItemView) && ((ValueItemView) view).getCurrentState() == State.DISABLE) {
                    UI3Toast.makeToast(PropItemLayout.this.getContext(), PropItemLayout.this.getContext().getResources().getString(R.string.new_shop_product_stockout)).show();
                } else if (PropItemLayout.this.valueLayout != null) {
                    for (int i = 0; i < PropItemLayout.this.valueLayout.getChildCount(); i++) {
                        View line = PropItemLayout.this.valueLayout.getChildAt(i);
                        if (line instanceof LinearLayout) {
                            for (int j = 0; j < ((LinearLayout) line).getChildCount(); j++) {
                                View item = ((LinearLayout) line).getChildAt(j);
                                if (item instanceof ValueItemView) {
                                    if (view == item) {
                                        if (((ValueItemView) item).getCurrentState() == State.SELECT) {
                                            ((ValueItemView) item).setState(State.UNSELECT);
                                        } else {
                                            ((ValueItemView) item).setState(State.SELECT);
                                        }
                                    } else if (((ValueItemView) item).getCurrentState() == State.SELECT) {
                                        ((ValueItemView) item).setState(State.UNSELECT);
                                    }
                                }
                            }
                        }
                    }
                }
            }
        };
        initView();
    }

    private void initView() {
        setOrientation(1);
        this.nameTxt = new TextView(getContext());
        this.nameTxt.setTextSize(0, (float) getResources().getDimensionPixelSize(R.dimen.dp_24));
        this.nameTxt.setTextColor(Color.parseColor("#202020"));
        addView(this.nameTxt);
        this.valueLayout = new LinearLayout(getContext());
        this.valueLayout.setOrientation(1);
        LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(-1, -1);
        lp.setMargins(0, (int) getResources().getDimension(R.dimen.dp_6), 0, 0);
        this.valueLayout.setLayoutParams(lp);
        addView(this.valueLayout);
    }

    public IValueListener getValueListener() {
        return this.valueListener;
    }

    public void setValueListener(IValueListener valueListener2) {
        this.valueListener = valueListener2;
    }

    public IDesc getLastSelected() {
        return this.lastSelected;
    }

    public void setPropData(IPropData pd) {
        int line;
        if (pd != null) {
            this.propData = pd;
            if (this.propData.getPropDesc() != null) {
                String name = this.propData.getPropDesc().getTxt();
                if (!TextUtils.isEmpty(name)) {
                    this.nameTxt.setText(name);
                }
            }
            List<IDesc> propValues = this.propData.getValueList();
            if (propValues != null) {
                int size = propValues.size();
                if (size % 3 == 0) {
                    line = size / 3;
                } else {
                    line = (size / 3) + 1;
                }
                for (int i = 0; i < line; i++) {
                    LinearLayout view = mkGroup();
                    if (i > 0) {
                        LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(-1, -1);
                        lp.setMargins(0, getResources().getDimensionPixelSize(R.dimen.dp_f2), 0, 0);
                        view.setLayoutParams(lp);
                    }
                    int column = 3;
                    if (size > 3) {
                        size -= 3;
                    } else {
                        column = size;
                    }
                    for (int j = 0; j < column; j++) {
                        IDesc propValItem = propValues.get(((i + 1) * 3) - (3 - j));
                        if (propValItem != null) {
                            ValueItemView item = (ValueItemView) view.getChildAt(j);
                            item.setTag(propValItem);
                            item.setVisibility(0);
                            item.setHeight((int) getResources().getDimension(R.dimen.dp_60));
                            item.setText(propValItem.getTxt());
                            item.setTextSize(0, (float) getResources().getDimensionPixelSize(R.dimen.dp_24));
                            item.setPadding(getResources().getDimensionPixelSize(R.dimen.dp_6), 0, getResources().getDimensionPixelSize(R.dimen.dp_6), getResources().getDimensionPixelSize(R.dimen.dp_3));
                            item.setFocusable(true);
                            item.setOnClickListener(this.onClickListener);
                            if (propValues.size() == 1) {
                                item.setState(State.SELECT);
                            }
                        }
                    }
                    this.valueLayout.addView(view);
                }
            }
        }
    }

    public LinearLayout mkGroup() {
        LinearLayout linearLayout = new LinearLayout(getContext());
        linearLayout.setOrientation(0);
        linearLayout.setLayoutParams(new LinearLayout.LayoutParams(-1, -2));
        for (int i = 0; i < 3; i++) {
            ValueItemView a = new ValueItemView(getContext());
            a.setGravity(16);
            a.setVisibility(4);
            a.setFocusable(false);
            LinearLayout.LayoutParams alp = new LinearLayout.LayoutParams(0, -2);
            alp.weight = 1.0f;
            if (i != 0) {
                alp.setMargins(getResources().getDimensionPixelSize(R.dimen.dp_f2), 0, 0, 0);
            }
            linearLayout.addView(a, alp);
        }
        return linearLayout;
    }

    public class ValueItemView extends TextView {
        private State currentState = State.UNSELECT;
        private Bitmap disableUnfocusBG;
        private int pading = getResources().getDimensionPixelSize(R.dimen.dp_1);
        private Paint paint;
        private Bitmap selectFocused;
        private Bitmap selectUnfocus;

        public ValueItemView(Context context) {
            super(context);
            initView();
            this.paint = new Paint();
            this.paint.setStyle(Paint.Style.FILL);
            this.paint.setAntiAlias(true);
            this.paint.setColor(SupportMenu.CATEGORY_MASK);
        }

        private void initBitmap() {
            this.selectFocused = BitMapUtil.readBmp(getContext(), R.drawable.iv_sku_item_select_focused);
            this.selectUnfocus = BitMapUtil.readBmp(getContext(), R.drawable.iv_sku_item_select_unfocus);
            this.disableUnfocusBG = BitMapUtil.readBmp(getContext(), R.drawable.iv_sku_item_disable_bg);
        }

        private void initView() {
            initBitmap();
            setSingleLine();
            setEllipsize(TextUtils.TruncateAt.END);
            setBackgroundResource(R.drawable.bg_sku_item_unfocused_enable);
            setTextColor(Color.parseColor("#202020"));
        }

        public void setState(State state) {
            if (this.currentState != state) {
                this.currentState = state;
                updateBackground(isFocused());
                invalidate();
                if (getTag() instanceof IDesc) {
                    PropItemLayout.this.innerValueListener.onSelected((IDesc) getTag(), state == State.SELECT);
                }
            }
        }

        public State getCurrentState() {
            return this.currentState;
        }

        /* access modifiers changed from: protected */
        public void onFocusChanged(boolean gainFocus, int direction, Rect previouslyFocusedRect) {
            super.onFocusChanged(gainFocus, direction, previouslyFocusedRect);
            startMarquee(gainFocus);
            updateBackground(gainFocus);
        }

        private void updateBackground(boolean isFocused) {
            if (isFocused) {
                setBackgroundResource(R.drawable.bg_sku_item_focused_color);
                setTextColor(Color.parseColor("#FFFFFF"));
                return;
            }
            switch (this.currentState) {
                case SELECT:
                case UNSELECT:
                    setBackgroundResource(R.drawable.bg_sku_item_unfocused_enable);
                    setTextColor(Color.parseColor("#202020"));
                    return;
                case DISABLE:
                    setBackgroundResource(R.drawable.bg_sku_item_unfocused_disable);
                    setTextColor(Color.parseColor("#afafaf"));
                    return;
                default:
                    return;
            }
        }

        private void startMarquee(boolean gainFocus) {
            if (gainFocus) {
                setSelected(true);
                setEllipsize(TextUtils.TruncateAt.MARQUEE);
                return;
            }
            setEllipsize(TextUtils.TruncateAt.END);
        }

        /* access modifiers changed from: protected */
        public void onDraw(Canvas canvas) {
            super.onDraw(canvas);
            if (State.SELECT != this.currentState) {
                return;
            }
            if (isFocused()) {
                Rect src = new Rect();
                src.left = 0;
                src.top = 0;
                src.right = this.selectUnfocus.getWidth();
                src.bottom = this.selectUnfocus.getHeight();
                Rect target = new Rect();
                target.right = getWidth() - this.pading;
                target.left = target.right - src.width();
                target.top = this.pading;
                target.bottom = getHeight() - this.pading;
                canvas.drawBitmap(this.selectFocused, src, target, this.paint);
                return;
            }
            Rect src2 = new Rect();
            src2.left = 0;
            src2.top = 0;
            src2.right = this.selectUnfocus.getWidth();
            src2.bottom = this.selectUnfocus.getHeight();
            Rect target2 = new Rect();
            target2.right = getWidth() - this.pading;
            target2.left = target2.right - src2.width();
            target2.top = this.pading;
            target2.bottom = getHeight() - this.pading;
            canvas.drawBitmap(this.selectUnfocus, src2, target2, this.paint);
        }
    }

    public static class Desc implements IDesc {
        String txt;
        Object value;

        public Desc(String txt2, Object value2) {
            this.txt = txt2;
            this.value = value2;
        }

        public String getTxt() {
            return this.txt;
        }

        public Object getValue() {
            return this.value;
        }
    }
}
