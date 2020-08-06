package com.tvtaobao.android.buildorderwares.styled_a;

import android.app.Dialog;
import android.content.Context;
import android.support.constraint.ConstraintLayout;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;
import com.tvtaobao.android.buildorderwares.R;

public class TVTBAddressChoseDialog extends Dialog {
    private ImageView bottomFlag;
    private ConstraintLayout dialogLayout;
    private FrameLayout listArea;
    private ImageView locationIcon;
    private TextView title;

    public TVTBAddressChoseDialog(Context context, int themeResId) {
        super(context, themeResId);
        setContentView(R.layout.buildorderwares_address_chose_dialog);
        findViews();
    }

    private void findViews() {
        this.dialogLayout = (ConstraintLayout) findViewById(R.id.dialog_layout);
        this.locationIcon = (ImageView) findViewById(R.id.location_icon);
        this.title = (TextView) findViewById(R.id.title);
        this.listArea = (FrameLayout) findViewById(R.id.list_area);
        this.bottomFlag = (ImageView) findViewById(R.id.bottom_flag);
    }

    public ConstraintLayout getDialogLayout() {
        return this.dialogLayout;
    }

    public ImageView getLocationIcon() {
        return this.locationIcon;
    }

    public TextView getTitle() {
        return this.title;
    }

    public FrameLayout getListArea() {
        return this.listArea;
    }

    public ImageView getBottomFlag() {
        return this.bottomFlag;
    }
}
