package com.tvtaobao.android.addresswares;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;
import android.support.constraint.ConstraintLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;
import com.tvtaobao.android.ui3.widget.FullScreenDialog;
import java.util.List;

public class AddressPickDlg extends FullScreenDialog {
    /* access modifiers changed from: private */
    public List<IAddress> addresses;
    private ImageView bottomFlag = ((ImageView) findViewById(R.id.bottom_flag));
    private ConstraintLayout dialogLayout = ((ConstraintLayout) findViewById(R.id.dialog_layout));
    private FrameLayout listArea = ((FrameLayout) findViewById(R.id.list_area));
    /* access modifiers changed from: private */
    public IEventListener listener;
    private ImageView locationIcon = ((ImageView) findViewById(R.id.location_icon));
    private IAddressProvider provider;
    /* access modifiers changed from: private */
    public RecyclerView recyclerView;
    private TextView title = ((TextView) findViewById(R.id.title));

    public interface IAddress {
        String getDetail();

        String getName();

        String getPhoneNumber();
    }

    public interface IAddressProvider {
        List<IAddress> getAddresses();

        CharSequence getTitleText();
    }

    public interface IEventListener {
        boolean onClickAddress(int i, IAddress iAddress);

        boolean onClickEditAddress();
    }

    public void setProvider(IAddressProvider provider2) {
        this.provider = provider2;
        if (provider2 != null) {
            this.addresses = provider2.getAddresses();
        }
    }

    public void setListener(IEventListener listener2) {
        this.listener = listener2;
    }

    public AddressPickDlg(Context context) {
        super(context);
        this.recyclerView = new RecyclerView(context);
        this.listArea.addView(this.recyclerView, new FrameLayout.LayoutParams(-1, -1));
    }

    public View onCreateView() {
        return LayoutInflater.from(getContext()).inflate(R.layout.addresswares_address_pick_dlg, (ViewGroup) null);
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

    public void show() {
        prepareData();
        super.show(true);
    }

    private void prepareData() {
        this.recyclerView.setAdapter((RecyclerView.Adapter) null);
        syncState();
    }

    private void syncState() {
        if (this.provider != null) {
            this.title.setText(this.provider.getTitleText());
            this.recyclerView.setLayoutManager(new LinearLayoutManager(getContext()) {
                public boolean onRequestChildFocus(RecyclerView parent, RecyclerView.State state, View child, View focused) {
                    if (focused == null) {
                        return super.onRequestChildFocus(parent, state, child, focused);
                    }
                    parent.smoothScrollBy(0, ((focused.getTop() + focused.getBottom()) / 2) - (parent.getHeight() / 2));
                    return true;
                }
            });
            this.recyclerView.setAdapter(new RecyclerView.Adapter() {
                public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
                    View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.addresswares_address_item, (ViewGroup) null);
                    itemView.setFocusable(true);
                    return new AddressVH(itemView);
                }

                public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
                    int viewType = getItemViewType(position);
                    if (!(holder instanceof AddressVH)) {
                        return;
                    }
                    if (viewType == 1) {
                        ((AddressVH) holder).inflateAddress(position, (IAddress) AddressPickDlg.this.addresses.get(position));
                    } else {
                        ((AddressVH) holder).inflateEditAddress("添加或编辑地址", "按【OK键】添加或编辑地址");
                    }
                }

                public int getItemViewType(int position) {
                    if (AddressPickDlg.this.addresses != null && position < AddressPickDlg.this.addresses.size()) {
                        return 1;
                    }
                    return 0;
                }

                public int getItemCount() {
                    return (AddressPickDlg.this.addresses != null ? AddressPickDlg.this.addresses.size() : 0) + 1;
                }
            });
            this.recyclerView.setHasFixedSize(false);
            this.recyclerView.addItemDecoration(new RecyclerView.ItemDecoration() {
                int color = AddressPickDlg.this.getContext().getResources().getColor(R.color.values_color_d2d2d2);
                Paint paint;
                Rect rect = new Rect();

                public void onDrawOver(Canvas c, RecyclerView parent, RecyclerView.State state) {
                    super.onDrawOver(c, parent, state);
                    if (this.paint == null) {
                        this.paint = new Paint();
                        this.paint.setAntiAlias(true);
                        this.paint.setColor(this.color);
                        this.paint.setStrokeWidth(1.0f);
                        this.paint.setStyle(Paint.Style.STROKE);
                    }
                    for (int i = 0; i < parent.getChildCount(); i++) {
                        View child = parent.getChildAt(i);
                        if (i != parent.getChildCount() - 1) {
                            this.rect.set(0, child.getBottom(), AddressPickDlg.this.recyclerView.getWidth(), child.getBottom() + 1);
                            c.drawLine(0.0f, (float) child.getBottom(), (float) AddressPickDlg.this.recyclerView.getWidth(), (float) child.getBottom(), this.paint);
                        }
                    }
                }

                public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
                    outRect.set(0, 0, 0, AddressPickDlg.this.getContext().getResources().getDimensionPixelOffset(R.dimen.values_dp_2));
                    super.getItemOffsets(outRect, view, parent, state);
                }
            });
            this.recyclerView.getAdapter().notifyDataSetChanged();
        }
    }

    class AddressVH extends RecyclerView.ViewHolder implements View.OnFocusChangeListener {
        private TextView tvAddressDetail;
        private TextView tvNameNum;

        public AddressVH(View itemView) {
            super(itemView);
            this.tvNameNum = (TextView) itemView.findViewById(R.id.user_name_number);
            this.tvAddressDetail = (TextView) itemView.findViewById(R.id.user_address);
            itemView.setLayoutParams(new ViewGroup.LayoutParams(-1, -2));
            itemView.setClickable(true);
            itemView.setOnFocusChangeListener(this);
        }

        public void onFocusChange(View v, boolean hasFocus) {
            if (v != this.itemView) {
                return;
            }
            if (hasFocus) {
                this.tvNameNum.setTextColor(-1);
                this.tvAddressDetail.setTextColor(-1);
                return;
            }
            this.tvNameNum.setTextColor(-14671840);
            this.tvAddressDetail.setTextColor(-14671840);
        }

        public void inflateAddress(final int position, final IAddress data) {
            if (data != null) {
                String nameNumber = data.getName();
                if (!TextUtils.isEmpty(data.getPhoneNumber())) {
                    nameNumber = nameNumber + "  " + data.getPhoneNumber();
                }
                this.tvNameNum.setText(nameNumber);
                this.tvAddressDetail.setText(data.getDetail());
                this.itemView.setOnClickListener(new View.OnClickListener() {
                    public void onClick(View v) {
                        if (AddressPickDlg.this.listener == null) {
                            AddressPickDlg.this.dismiss();
                        } else if (!AddressPickDlg.this.listener.onClickAddress(position, data)) {
                            AddressPickDlg.this.dismiss();
                        }
                    }
                });
            }
        }

        public void inflateEditAddress(CharSequence name, CharSequence detail) {
            this.tvNameNum.setText(name);
            this.tvAddressDetail.setText(detail);
            this.itemView.setOnClickListener(new View.OnClickListener() {
                public void onClick(View v) {
                    if (AddressPickDlg.this.listener == null) {
                        AddressPickDlg.this.dismiss();
                    } else if (!AddressPickDlg.this.listener.onClickEditAddress()) {
                        AddressPickDlg.this.dismiss();
                    }
                }
            });
        }
    }
}
