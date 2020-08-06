package com.yunos.tvtaobao.biz.widget;

import android.content.Context;
import android.util.AttributeSet;

public class GoodListLifeUiGridView extends RefreshDataFocusFlipGridView {
    public GoodListLifeUiGridView(Context contxt) {
        super(contxt);
        onInitGoodListLifeUiGridView(contxt);
    }

    public GoodListLifeUiGridView(Context contxt, AttributeSet attrs) {
        super(contxt, attrs);
        onInitGoodListLifeUiGridView(contxt);
    }

    public GoodListLifeUiGridView(Context contxt, AttributeSet attrs, int defStyle) {
        super(contxt, attrs, defStyle);
        onInitGoodListLifeUiGridView(contxt);
    }

    public void onInitGoodListLifeUiGridView(Context contxt) {
    }
}
