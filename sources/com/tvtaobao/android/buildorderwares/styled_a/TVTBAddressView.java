package com.tvtaobao.android.buildorderwares.styled_a;

import android.content.Context;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.ImageView;
import android.widget.TextView;
import com.tvtaobao.android.buildorderwares.R;
import com.tvtaobao.android.buildorderwares.base.CCFrameLayout;

public class TVTBAddressView extends CCFrameLayout {
    private ImageView addressBottomFlag;
    private ImageView addressIcon;
    private TextView preSellAddressDesc;
    private TextView tipMsg;
    private TextView userAddress;
    private TextView userNameNumber;

    public TVTBAddressView(Context context) {
        this(context, (AttributeSet) null);
    }

    public TVTBAddressView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public TVTBAddressView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        LayoutInflater.from(context).inflate(R.layout.buildorderwares_layout_address_view, this);
        findViews();
        init();
    }

    private void findViews() {
        this.addressIcon = (ImageView) findViewById(R.id.address_icon);
        this.addressBottomFlag = (ImageView) findViewById(R.id.address_bottom_flag);
        this.userNameNumber = (TextView) findViewById(R.id.user_name_number);
        this.userAddress = (TextView) findViewById(R.id.user_address);
        this.preSellAddressDesc = (TextView) findViewById(R.id.pre_sell_address_desc);
        this.tipMsg = (TextView) findViewById(R.id.tip_msg);
    }

    private void init() {
        setFocusable(true);
        setDescendantFocusability(393216);
    }

    public void showNoAddress(String tipMsgTxt) {
        this.userAddress.setVisibility(8);
        this.userNameNumber.setVisibility(8);
        this.tipMsg.setVisibility(0);
        this.tipMsg.setText(tipMsgTxt);
        this.addressIcon.setVisibility(4);
    }

    public void showAddress(String nameNumber, String address) {
        this.userAddress.setText(address);
        this.userNameNumber.setText(nameNumber);
        this.userAddress.setVisibility(0);
        this.userNameNumber.setVisibility(0);
        this.tipMsg.setVisibility(8);
    }

    public void showNormalAndPreSellAddressDesc(String desc) {
        if (!TextUtils.isEmpty(desc)) {
            this.preSellAddressDesc.setText(desc);
            this.preSellAddressDesc.setVisibility(0);
            return;
        }
        this.preSellAddressDesc.setVisibility(8);
    }

    public ImageView getAddressIcon() {
        return this.addressIcon;
    }

    public ImageView getAddressBottomFlag() {
        return this.addressBottomFlag;
    }

    public TextView getUserNameNumber() {
        return this.userNameNumber;
    }

    public TextView getUserAddress() {
        return this.userAddress;
    }

    public TextView getPreSellAddressDesc() {
        return this.preSellAddressDesc;
    }

    public TextView getTipMsg() {
        return this.tipMsg;
    }
}
