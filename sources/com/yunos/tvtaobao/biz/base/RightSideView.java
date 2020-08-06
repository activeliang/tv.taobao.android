package com.yunos.tvtaobao.biz.base;

import android.content.Context;
import android.os.Handler;
import android.util.AttributeSet;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import com.yunos.tvtaobao.businessview.R;
import com.zhiping.dev.android.logger.ZpLogger;

public class RightSideView extends RelativeLayout implements View.OnClickListener, View.OnFocusChangeListener {
    public static String TAG = "RightSideView";
    private Context context;
    private ImageView fiv_pierce_background;
    private ImageView fiv_pierce_block_focusd;
    private ImageView fiv_pierce_cart_focusd;
    private ImageView fiv_pierce_come_back_focusd;
    private ImageView fiv_pierce_contact_focusd;
    private ImageView fiv_pierce_home_focusd;
    private ImageView fiv_pierce_my_focusd;
    private ImageView fiv_pierce_red_jifen_focusd;
    private ImageView fiv_pierce_red_packet_focusd;
    private ImageView iv_pierce_cart_active;
    private Handler mHandler;
    private OnItemViewClickListener onItemViewClickListener;
    private OnItemViewFocusChangeListener onItemViewFocusChangeListener;
    private TextView tv_pierce_block;
    private TextView tv_pierce_cart;
    private TextView tv_pierce_come_back;
    private TextView tv_pierce_contact_focusd;
    private TextView tv_pierce_home;
    private TextView tv_pierce_my;
    private TextView tv_pierce_red_jifen;
    private TextView tv_pierce_red_packet;

    public interface OnItemViewClickListener {
        void onItemViewClick(View view);
    }

    public interface OnItemViewFocusChangeListener {
        void onItemViewFocusChange(View view, boolean z);
    }

    public RightSideView(Context context2) {
        super(context2);
        initView(context2);
    }

    public RightSideView(Context context2, AttributeSet attrs) {
        super(context2, attrs);
        initView(context2);
    }

    public RightSideView(Context context2, AttributeSet attrs, int defStyle) {
        super(context2, attrs, defStyle);
        initView(context2);
    }

    public void setOnItemViewClickListener(OnItemViewClickListener onItemViewClickListener2) {
        this.onItemViewClickListener = onItemViewClickListener2;
    }

    public void setOnItemViewFocusChangeListener(OnItemViewFocusChangeListener onItemViewFocusChangeListener2) {
        this.onItemViewFocusChangeListener = onItemViewFocusChangeListener2;
    }

    public void initView(Context context2) {
        this.context = context2;
        if (context2 instanceof BaseTabMVPActivity) {
        }
        LayoutInflater.from(context2).inflate(R.layout.layout_right_sidebar, this);
        this.mHandler = new Handler();
        initPierceViews();
        setListener();
    }

    public View findFocus() {
        View view = super.findFocus();
        ZpLogger.i(TAG, "findFocus view : " + view);
        return view;
    }

    public void requestChildFocus(View child, View focused) {
        super.requestChildFocus(child, focused);
        ZpLogger.i(TAG, TAG + ".requestChildFocus child : " + child + " ,focused : " + focused);
    }

    public View focusSearch(View focused, int direction) {
        ZpLogger.i(TAG, TAG + ".focusSearch");
        return super.focusSearch(focused, direction);
    }

    public View findNextFocused(View focused, int direction) {
        ZpLogger.i(TAG, TAG + ".findNextFocused lastFocusItem : ");
        return this.fiv_pierce_home_focusd;
    }

    public boolean dispatchKeyEvent(KeyEvent event) {
        ZpLogger.i(TAG, TAG + ".dispatchKeyEvent  : ");
        return super.dispatchKeyEvent(event);
    }

    private void initPierceViews() {
        this.fiv_pierce_home_focusd = (ImageView) findViewById(R.id.fiv_pierce_home_focusd);
        this.fiv_pierce_my_focusd = (ImageView) findViewById(R.id.fiv_pierce_my_focusd);
        this.fiv_pierce_cart_focusd = (ImageView) findViewById(R.id.fiv_pierce_cart_focusd);
        this.fiv_pierce_red_packet_focusd = (ImageView) findViewById(R.id.fiv_pierce_red_packet_focusd);
        this.fiv_pierce_block_focusd = (ImageView) findViewById(R.id.fiv_pierce_block_focusd);
        this.fiv_pierce_contact_focusd = (ImageView) findViewById(R.id.fiv_pierce_contact_focusd);
        this.fiv_pierce_come_back_focusd = (ImageView) findViewById(R.id.fiv_pierce_come_back_focusd);
        this.fiv_pierce_red_jifen_focusd = (ImageView) findViewById(R.id.fiv_pierce_red_jifen_focusd);
        this.fiv_pierce_background = (ImageView) findViewById(R.id.fiv_pierce_background);
        this.tv_pierce_home = (TextView) findViewById(R.id.tv_pierce_home);
        this.tv_pierce_my = (TextView) findViewById(R.id.tv_pierce_my);
        this.tv_pierce_cart = (TextView) findViewById(R.id.tv_pierce_cart);
        this.tv_pierce_red_packet = (TextView) findViewById(R.id.tv_pierce_red_packet);
        this.tv_pierce_block = (TextView) findViewById(R.id.tv_pierce_block);
        this.tv_pierce_contact_focusd = (TextView) findViewById(R.id.tv_pierce_contact_focusd);
        this.tv_pierce_come_back = (TextView) findViewById(R.id.tv_pierce_come_back);
        this.tv_pierce_red_jifen = (TextView) findViewById(R.id.tv_pierce_red_jifen);
        this.iv_pierce_cart_active = (ImageView) findViewById(R.id.iv_pierce_cart_active);
    }

    public void setListener() {
        this.fiv_pierce_home_focusd.setOnClickListener(this);
        this.fiv_pierce_my_focusd.setOnClickListener(this);
        this.fiv_pierce_cart_focusd.setOnClickListener(this);
        this.fiv_pierce_red_packet_focusd.setOnClickListener(this);
        this.fiv_pierce_block_focusd.setOnClickListener(this);
        this.fiv_pierce_contact_focusd.setOnClickListener(this);
        this.fiv_pierce_red_jifen_focusd.setOnClickListener(this);
        this.fiv_pierce_home_focusd.setOnFocusChangeListener(this);
        this.fiv_pierce_my_focusd.setOnFocusChangeListener(this);
        this.fiv_pierce_cart_focusd.setOnFocusChangeListener(this);
        this.fiv_pierce_red_packet_focusd.setOnFocusChangeListener(this);
        this.fiv_pierce_block_focusd.setOnFocusChangeListener(this);
        this.fiv_pierce_contact_focusd.setOnFocusChangeListener(this);
        this.fiv_pierce_come_back_focusd.setOnFocusChangeListener(this);
        this.fiv_pierce_red_jifen_focusd.setOnFocusChangeListener(this);
    }

    public void onClick(View v) {
        if (this.onItemViewClickListener != null) {
            this.onItemViewClickListener.onItemViewClick(v);
        }
    }

    public void onFocusChange(View v, boolean hasFocus) {
        if (this.onItemViewFocusChangeListener != null) {
            this.onItemViewFocusChangeListener.onItemViewFocusChange(v, hasFocus);
        }
    }

    public ImageView getFiv_pierce_home_focusd() {
        return this.fiv_pierce_home_focusd;
    }

    public ImageView getFiv_pierce_my_focusd() {
        return this.fiv_pierce_my_focusd;
    }

    public ImageView getFiv_pierce_cart_focusd() {
        return this.fiv_pierce_cart_focusd;
    }

    public ImageView getFiv_pierce_red_packet_focusd() {
        return this.fiv_pierce_red_packet_focusd;
    }

    public ImageView getFiv_pierce_block_focusd() {
        return this.fiv_pierce_block_focusd;
    }

    public ImageView getFiv_pierce_contact_focusd() {
        return this.fiv_pierce_contact_focusd;
    }

    public ImageView getFiv_pierce_come_back_focusd() {
        return this.fiv_pierce_come_back_focusd;
    }

    public ImageView getFiv_pierce_red_jifen_focusd() {
        return this.fiv_pierce_red_jifen_focusd;
    }

    public ImageView getFiv_pierce_background() {
        return this.fiv_pierce_background;
    }

    public TextView getTv_pierce_home() {
        return this.tv_pierce_home;
    }

    public TextView getTv_pierce_my() {
        return this.tv_pierce_my;
    }

    public TextView getTv_pierce_cart() {
        return this.tv_pierce_cart;
    }

    public TextView getTv_pierce_red_packet() {
        return this.tv_pierce_red_packet;
    }

    public TextView getTv_pierce_block() {
        return this.tv_pierce_block;
    }

    public TextView getTv_pierce_contact_focusd() {
        return this.tv_pierce_contact_focusd;
    }

    public TextView getTv_pierce_come_back() {
        return this.tv_pierce_come_back;
    }

    public TextView getTv_pierce_red_jifen() {
        return this.tv_pierce_red_jifen;
    }

    public ImageView getIv_pierce_cart_active() {
        return this.iv_pierce_cart_active;
    }
}
