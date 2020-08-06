package com.yunos.tvtaobao.biz.util;

import android.content.Context;
import android.graphics.Color;
import android.util.DisplayMetrics;
import com.yunos.tv.core.util.DeviceUtil;
import com.yunos.tvtaobao.businessview.R;

public final class VisualMarkConfig {
    public static final int COL_COUNT = 4;
    public static float Display_Scale = 1.0f;
    public static int EACH_HEIGHT = 0;
    public static final int EACH_REQUEST_PAGE_COUNT = 4;
    public static int EACH_WIDTH = 0;
    public static int ITEM_SHADOW = 0;
    public static int ITEM_SPACE = 7;
    public static final int LIMIT_WORDS = 15;
    public static int PAGEVIEW_GAP = 0;
    public static final int PAGE_COUNT = 8;
    public static int PAGE_HEIGHT = ((EACH_HEIGHT * 2) + (ITEM_SPACE * 1));
    public static final int PAGE_TOTAL = 52;
    public static int PAGE_VIEW_MARGIN_LEFT = 0;
    public static int PAGE_VIEW_MARGIN_TOP = 0;
    public static int PAGE_WIDTH = ((EACH_WIDTH * 4) + (ITEM_SPACE * 3));
    public static final int ROW_COUNT = 2;
    public static int SCREEN_HEIGHT = 0;
    public static int SCREEN_WIDTH = 0;
    public static final int SD_CARD_CACHE_FILE_COUNT = 20;
    public static int TABEL_HEIGHT = 0;
    public static String URL_Bitmap_Size = URL_Bitmap_Size_270;
    private static String URL_Bitmap_Size_270 = "_270x270.jpg";
    private static String URL_Bitmap_Size_400 = "_400x400.jpg";
    public static final int color_b = 51;
    public static final int color_focus_b = 0;
    public static final int color_focus_g = 0;
    public static final int color_focus_r = 255;
    public static final int color_g = 51;
    public static final int color_r = 51;
    public static final int color_select_b = 0;
    public static final int color_select_g = 102;
    public static final int color_select_r = 255;
    public static final int mBitmapBound_Color = Color.rgb(246, 246, 246);
    public static final int mGoodName_Color = Color.rgb(51, 51, 51);
    public static String mItemDefaultImage = "goodlist_default_image_270.png";
    public static int mPageTextSize = 0;
    public static int mPage_Offsex_X = 0;
    public static int mPage_Offsex_Y = 0;
    public static final int mPriceText_Color = Color.rgb(255, 102, 0);
    public static final String mSelectedBoxImage = "page_item_selector.9.png";
    public static int mSelectedBoxImageResId = R.drawable.ytbv_common_focus;
    public static final int mSoldText_Color = Color.rgb(153, 153, 153);
    public static int mTableBar_Offset_X = 0;
    public static int mTableBar_Offset_Y = 0;
    public static final int mUiBackgroundColor = Color.argb(255, 232, 229, 229);
    public static int offset_space = 0;
    public static int textHeight = 0;
    public static int textSize = 0;

    public static final String onGetURL_Bitmap_Size(int size) {
        if (size <= 0) {
            return "";
        }
        String w = String.valueOf(size);
        return "_" + w + "x" + w + ".jpg";
    }

    public static void getScreenTypeFromDevice(Context context) {
        if (context != null) {
            new DisplayMetrics();
            int screenWidth = context.getResources().getDisplayMetrics().widthPixels;
            mItemDefaultImage = "goodlist_default_image_270.png";
            Display_Scale = 1.0f;
            if (screenWidth == 1920) {
                mItemDefaultImage = "goodlist_default_image_400.png";
                Display_Scale = 1.5f;
            }
        }
    }

    public static void onInitValue() {
        ITEM_SPACE = 7;
        EACH_WIDTH = 270;
        EACH_HEIGHT = 270;
        PAGEVIEW_GAP = 14;
        PAGE_VIEW_MARGIN_LEFT = 85;
        PAGE_VIEW_MARGIN_TOP = 118;
        ITEM_SHADOW = 50;
        SCREEN_WIDTH = DeviceUtil.SCREENTYPE.ScreenType_720p;
        SCREEN_HEIGHT = 672;
        TABEL_HEIGHT = 69;
        mPageTextSize = 30;
        mPage_Offsex_X = 82;
        mPage_Offsex_Y = 36;
        textSize = 24;
        textHeight = 25;
        mTableBar_Offset_X = 780;
        mTableBar_Offset_Y = 40;
        offset_space = 50;
        URL_Bitmap_Size = URL_Bitmap_Size_270;
        if (Display_Scale != 1.0f) {
            onHandleScale();
        }
        PAGE_WIDTH = (EACH_WIDTH * 4) + (ITEM_SPACE * 3);
        PAGE_HEIGHT = (EACH_HEIGHT * 2) + (ITEM_SPACE * 1);
    }

    public static void onHandleScale() {
        ITEM_SPACE = (int) (((float) ITEM_SPACE) * Display_Scale);
        ITEM_SPACE += 5;
        EACH_WIDTH = (int) (((float) EACH_WIDTH) * Display_Scale);
        EACH_HEIGHT = (int) (((float) EACH_HEIGHT) * Display_Scale);
        PAGEVIEW_GAP = (int) (((float) PAGEVIEW_GAP) * Display_Scale);
        PAGEVIEW_GAP += 5;
        PAGE_VIEW_MARGIN_LEFT = (int) (((float) PAGE_VIEW_MARGIN_LEFT) * Display_Scale);
        PAGE_VIEW_MARGIN_TOP = (int) (((float) PAGE_VIEW_MARGIN_TOP) * Display_Scale);
        ITEM_SHADOW = (int) (((float) ITEM_SHADOW) * Display_Scale);
        SCREEN_WIDTH = (int) (((float) SCREEN_WIDTH) * Display_Scale);
        SCREEN_HEIGHT = (int) (((float) SCREEN_HEIGHT) * Display_Scale);
        TABEL_HEIGHT = (int) (((float) TABEL_HEIGHT) * Display_Scale);
        mPageTextSize = (int) (((float) mPageTextSize) * Display_Scale);
        mPage_Offsex_X = (int) (((float) mPage_Offsex_X) * Display_Scale);
        mPage_Offsex_Y = (int) (((float) mPage_Offsex_Y) * Display_Scale);
        textSize = (int) (((float) textSize) * Display_Scale);
        textHeight = (int) (((float) textHeight) * Display_Scale);
        mTableBar_Offset_X = (int) (((float) mTableBar_Offset_X) * Display_Scale);
        mTableBar_Offset_Y = (int) (((float) mTableBar_Offset_Y) * Display_Scale);
        offset_space = (int) (((float) offset_space) * Display_Scale);
        URL_Bitmap_Size = URL_Bitmap_Size_400;
    }
}
