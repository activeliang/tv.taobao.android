package com.tvtaobao.android.ui3.widget;

import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;
import com.tvtaobao.android.ui3.R;
import com.uc.webview.export.extension.UCCore;
import java.util.ArrayList;
import java.util.List;

public class AutoAdjustGridLayout extends ViewGroup {
    private int columnGap;
    private int columnNum;
    private int rowGap;

    public AutoAdjustGridLayout(Context context) {
        this(context, (AttributeSet) null);
    }

    public AutoAdjustGridLayout(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public AutoAdjustGridLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        this.columnGap = 0;
        this.rowGap = 0;
        this.columnNum = 5;
        setClipChildren(false);
        setClipToPadding(false);
        this.columnGap = getResources().getDimensionPixelSize(R.dimen.values_dp_16);
        this.rowGap = (int) (((float) this.columnGap) * 1.0f * 2.1538463f);
        if (attrs != null) {
            TypedArray ta = context.obtainStyledAttributes(attrs, R.styleable.ui3wares_AutoAdjustGridLayout);
            this.columnGap = ta.getDimensionPixelSize(R.styleable.ui3wares_AutoAdjustGridLayout_ui3wares_aagl_columnGap, this.columnGap);
            this.rowGap = ta.getDimensionPixelSize(R.styleable.ui3wares_AutoAdjustGridLayout_ui3wares_aagl_rowGap, this.rowGap);
            this.columnNum = ta.getInteger(R.styleable.ui3wares_AutoAdjustGridLayout_ui3wares_aagl_columnNum, this.columnNum);
            ta.recycle();
        }
    }

    /* access modifiers changed from: protected */
    public void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int rowCount;
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        int wMode = View.MeasureSpec.getMode(widthMeasureSpec);
        int wSize = View.MeasureSpec.getSize(widthMeasureSpec);
        int hMode = View.MeasureSpec.getMode(heightMeasureSpec);
        int hSize = View.MeasureSpec.getSize(heightMeasureSpec);
        List<View> children = new ArrayList<>(getChildCount());
        for (int i = 0; i < getChildCount(); i++) {
            View child = getChildAt(i);
            if (child.getVisibility() != 8) {
                children.add(child);
            }
        }
        if (children.size() % this.columnNum != 0) {
            rowCount = (children.size() / this.columnNum) + 1;
        } else {
            rowCount = children.size() / this.columnNum;
        }
        int childrenW = 0;
        int childrenH = 0;
        if (wMode == 0) {
            if (children.size() > 0) {
                for (int row = 1; row <= rowCount; row++) {
                    int rowWidth = 0;
                    int rowHeight = 0;
                    int j = 0;
                    int bgn = (row - 1) * this.columnNum;
                    int i2 = bgn;
                    while (i2 < children.size() && j < this.columnNum) {
                        View child2 = children.get(i2);
                        measureChild(child2, widthMeasureSpec, heightMeasureSpec);
                        rowWidth = rowWidth + child2.getMeasuredWidth() + this.columnGap;
                        rowHeight = Math.max(rowHeight, child2.getMeasuredHeight());
                        j++;
                        i2 = bgn + j;
                    }
                    childrenW = Math.max(childrenW, rowWidth - this.columnGap);
                    childrenH += rowHeight;
                    if (row != rowCount) {
                        childrenH += this.rowGap;
                    }
                }
            } else {
                childrenW = 0;
            }
            if (hMode != 0) {
                if (hMode == Integer.MIN_VALUE) {
                    childrenH = Math.min(childrenH, hSize);
                } else if (hMode == 1073741824) {
                    childrenH = hSize;
                }
            }
            setMeasuredDimension(childrenW, childrenH);
        } else if (wMode == Integer.MIN_VALUE) {
            if (children.size() > 0) {
                int childWMS = View.MeasureSpec.makeMeasureSpec((wSize - (this.columnGap * (this.columnNum - 1))) / this.columnNum, UCCore.VERIFY_POLICY_QUICK);
                for (int row2 = 1; row2 <= rowCount; row2++) {
                    int rowWidth2 = 0;
                    int rowHeight2 = 0;
                    int j2 = 0;
                    int bgn2 = (row2 - 1) * this.columnNum;
                    int i3 = bgn2;
                    while (i3 < children.size() && j2 < this.columnNum) {
                        View child3 = children.get(i3);
                        measureChild(child3, childWMS, heightMeasureSpec);
                        rowWidth2 = rowWidth2 + child3.getMeasuredWidth() + this.columnGap;
                        rowHeight2 = Math.max(rowHeight2, child3.getMeasuredHeight());
                        j2++;
                        i3 = bgn2 + j2;
                    }
                    childrenW = Math.max(childrenW, rowWidth2 - this.columnGap);
                    int childrenH2 = childrenH + rowHeight2;
                    if (row2 != rowCount) {
                        childrenH2 += this.rowGap;
                    }
                }
            } else {
                childrenW = 0;
            }
            if (hMode != 0) {
                if (hMode == Integer.MIN_VALUE) {
                    childrenH = Math.min(childrenH, hSize);
                } else if (hMode == 1073741824) {
                    childrenH = hSize;
                }
            }
            setMeasuredDimension(childrenW, childrenH);
        } else if (wMode == 1073741824) {
            if (children.size() > 0) {
                int childWMS2 = View.MeasureSpec.makeMeasureSpec((wSize - (this.columnGap * (this.columnNum - 1))) / this.columnNum, UCCore.VERIFY_POLICY_QUICK);
                for (int row3 = 1; row3 <= rowCount; row3++) {
                    int rowWidth3 = 0;
                    int rowHeight3 = 0;
                    int j3 = 0;
                    int bgn3 = (row3 - 1) * this.columnNum;
                    int i4 = bgn3;
                    while (i4 < children.size() && j3 < this.columnNum) {
                        View child4 = children.get(i4);
                        measureChild(child4, childWMS2, heightMeasureSpec);
                        rowWidth3 = rowWidth3 + child4.getMeasuredWidth() + this.columnGap;
                        rowHeight3 = Math.max(rowHeight3, child4.getMeasuredHeight());
                        j3++;
                        i4 = bgn3 + j3;
                    }
                    childrenW = Math.max(childrenW, rowWidth3 - this.columnGap);
                    int childrenH3 = childrenH + rowHeight3;
                    if (row3 != rowCount) {
                        childrenH3 += this.rowGap;
                    }
                }
            } else {
                childrenW = wSize;
            }
            if (hMode != 0) {
                if (hMode == Integer.MIN_VALUE) {
                    childrenH = Math.min(childrenH, hSize);
                } else if (hMode == 1073741824) {
                    childrenH = hSize;
                }
            }
            setMeasuredDimension(childrenW, childrenH);
        }
    }

    /* access modifiers changed from: protected */
    public void onLayout(boolean changed, int l, int t, int r, int b) {
        int leftStart = getPaddingLeft();
        int topStart = getPaddingTop();
        int rowHeight = 0;
        List<View> children = new ArrayList<>(getChildCount());
        for (int i = 0; i < getChildCount(); i++) {
            View child = getChildAt(i);
            if (child.getVisibility() != 8) {
                children.add(child);
            }
        }
        for (int i2 = 0; i2 < children.size(); i2++) {
            View child2 = children.get(i2);
            if (child2 != null) {
                if (i2 != 0 && i2 % 5 == 0) {
                    leftStart = getPaddingLeft();
                    topStart = topStart + rowHeight + this.rowGap;
                }
                rowHeight = Math.max(rowHeight, child2.getMeasuredHeight());
                child2.layout(leftStart, topStart, child2.getMeasuredWidth() + leftStart, child2.getMeasuredHeight() + topStart);
                leftStart = leftStart + child2.getMeasuredWidth() + this.columnGap;
            }
        }
    }

    /* access modifiers changed from: protected */
    public ViewGroup.LayoutParams generateDefaultLayoutParams() {
        return new ViewGroup.LayoutParams(-1, -2);
    }

    public int getColumnGap() {
        return this.columnGap;
    }

    public void setColumnGap(int columnGap2) {
        this.columnGap = columnGap2;
    }

    public int getColumnNum() {
        return this.columnNum;
    }

    public void setColumnNum(int columnNum2) {
        this.columnNum = columnNum2;
    }
}
