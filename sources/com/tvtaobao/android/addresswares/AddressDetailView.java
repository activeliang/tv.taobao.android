package com.tvtaobao.android.addresswares;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.FrameLayout;
import android.widget.TextView;

public class AddressDetailView extends FrameLayout {
    private TextView tvAddressDetail;
    private TextView tvDistrict;
    private TextView tvNameNum;

    public AddressDetailView(Context context) {
        super(context);
        initViews(context);
    }

    public AddressDetailView(Context context, AttributeSet attrs) {
        super(context, attrs);
        initViews(context);
    }

    public AddressDetailView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initViews(context);
    }

    private void initViews(Context context) {
        LayoutInflater.from(context).inflate(R.layout.addresswares_address_detail, this, true);
        this.tvNameNum = (TextView) findViewById(R.id.address_namenum);
        this.tvAddressDetail = (TextView) findViewById(R.id.address_detail);
        this.tvDistrict = (TextView) findViewById(R.id.address_district);
    }

    public TextView getTvAddressDetail() {
        return this.tvAddressDetail;
    }

    public TextView getTvDistrict() {
        return this.tvDistrict;
    }

    public TextView getTvNameNum() {
        return this.tvNameNum;
    }
}
