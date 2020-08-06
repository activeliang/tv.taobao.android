package com.taobao.wireless.trade.mbuy.sdk.co.biz;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.taobao.muniontaobaosdk.p4p.anticheat.model.ClientTraceData;
import com.taobao.wireless.trade.mbuy.sdk.co.Component;
import com.taobao.wireless.trade.mbuy.sdk.engine.BuyEngine;
import com.taobao.wireless.trade.mbuy.sdk.engine.RollbackProtocol;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

public class InstallmentPickerComponent extends Component {
    /* access modifiers changed from: private */
    public List<OrderInstallmentPicker> details;
    private boolean isIntallmentPanelShown;

    public InstallmentPickerComponent(JSONObject data, BuyEngine engine) {
        super(data, engine);
        loadDetailArray(this.fields.getJSONArray("details"));
    }

    public void reload(JSONObject data) {
        super.reload(data);
        loadDetailArray(this.fields.getJSONArray("details"));
    }

    public String getMemo() {
        return this.fields.getString("memo");
    }

    public HashMap<Integer, Integer> getInterestFreeMap() {
        JSONObject jsonObject = this.fields.getJSONObject("interestFree");
        HashMap<Integer, Integer> outResult = new HashMap<>();
        if (jsonObject != null && jsonObject.size() > 0) {
            for (String parseInt : jsonObject.keySet()) {
                try {
                    Integer key = Integer.valueOf(Integer.parseInt(parseInt));
                    outResult.put(key, Integer.valueOf(Integer.parseInt(jsonObject.getString(key.toString()))));
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
        return outResult;
    }

    public String getCreditTip() {
        return this.fields.getString("creditTip");
    }

    public String getTitle() {
        return this.fields.getString("title");
    }

    public String getSubtitle() {
        return this.fields.getString("subtitle");
    }

    public String getDesc() {
        return this.fields.getString("desc");
    }

    public String getPageTitle() {
        return this.fields.getString("pageTitle");
    }

    public String getTotalPayTitle() {
        return this.fields.getString("totalPayTitle");
    }

    public boolean isNavInstallmentPickerPanel() {
        try {
            return this.fields.getBooleanValue("combineComponent");
        } catch (Exception e) {
            return false;
        }
    }

    public void setInstallmentPickerPanelShowState(boolean isShown) {
        this.isIntallmentPanelShown = isShown;
    }

    public boolean isInstallmentPickerPanelShown() {
        return this.isIntallmentPanelShown;
    }

    public String getCurrencySymbol() {
        return this.engine.getCurrencySymbol();
    }

    public String getTotalPayBySelectedNumArray(List<Integer> selectedNumList) {
        if (selectedNumList.size() != this.details.size()) {
            return "0.00";
        }
        double totalPay = ClientTraceData.b.f47a;
        int size = this.details.size();
        for (int i = 0; i < size; i++) {
            OrderInstallmentPicker picker = this.details.get(i);
            Iterator it = picker.options.iterator();
            while (true) {
                if (it.hasNext()) {
                    if (selectedNumList.get(i).intValue() == ((InstallmentOption) it.next()).getNum()) {
                        totalPay += picker.getOrderPrice();
                        break;
                    }
                } else {
                    break;
                }
            }
        }
        return String.format("%.2f", new Object[]{Double.valueOf(totalPay)});
    }

    public String getPoundageTitle() {
        return this.fields.getString("poundageTitle");
    }

    public String getPoundageBySelectedNumArray(List<Integer> selectedNumList) {
        if (selectedNumList.size() != this.details.size()) {
            return "0.00";
        }
        double poundage = ClientTraceData.b.f47a;
        int size = this.details.size();
        for (int i = 0; i < size; i++) {
            Iterator it = this.details.get(i).options.iterator();
            while (true) {
                if (!it.hasNext()) {
                    break;
                }
                InstallmentOption option = (InstallmentOption) it.next();
                if (selectedNumList.get(i).intValue() == option.getNum()) {
                    poundage += option.getPoundage();
                    break;
                }
            }
        }
        return String.format("%.2f", new Object[]{Double.valueOf(poundage)});
    }

    public List<OrderInstallmentPicker> getDetails() {
        return this.details;
    }

    public List<Integer> getSelectedNumList() {
        List<Integer> list = new ArrayList<>(this.details.size());
        for (OrderInstallmentPicker picker : this.details) {
            list.add(Integer.valueOf(picker.getSelectedNum()));
        }
        return list;
    }

    public void setSelectedNumList(List<Integer> selectedNumList) {
        if (selectedNumList != null && this.details != null && selectedNumList.size() == this.details.size()) {
            final List<Integer> prevSelectedNumList = getSelectedNumList();
            this.engine.getContext().setRollbackProtocol(new RollbackProtocol() {
                public void rollback() {
                    int size = prevSelectedNumList.size();
                    for (int i = 0; i < size; i++) {
                        ((OrderInstallmentPicker) InstallmentPickerComponent.this.details.get(i)).setSelectedNum(String.valueOf(prevSelectedNumList.get(i)));
                    }
                }
            });
            int size = selectedNumList.size();
            for (int i = 0; i < size; i++) {
                this.details.get(i).setSelectedNum(String.valueOf(selectedNumList.get(i)));
            }
            notifyLinkageDelegate();
        }
    }

    private void loadDetailArray(JSONArray array) {
        if (array == null || array.isEmpty()) {
            this.details = null;
            return;
        }
        this.details = new ArrayList(array.size());
        Iterator<Object> it = array.iterator();
        while (it.hasNext()) {
            try {
                this.details.add(new OrderInstallmentPicker((JSONObject) it.next(), this.engine));
            } catch (Throwable th) {
            }
        }
    }

    public static class OrderInstallmentPicker {
        private JSONObject data;
        private BuyEngine engine;
        /* access modifiers changed from: private */
        public List<InstallmentOption> options;

        public OrderInstallmentPicker(JSONObject data2, BuyEngine engine2) {
            if (data2 == null) {
                throw new IllegalArgumentException();
            }
            this.data = data2;
            this.engine = engine2;
            loadOptionArray(data2.getJSONArray("options"));
            if (this.options == null) {
                throw new IllegalArgumentException();
            }
        }

        public List<InstallmentOption> getOptions() {
            return this.options;
        }

        public String getShopIcon() {
            return this.data.getString("shopIcon");
        }

        public String getShopName() {
            return this.data.getString("shopName");
        }

        public double getOrderPrice() {
            return this.data.getDoubleValue("orderPrice");
        }

        public String getCurrencySymbol() {
            return this.engine.getCurrencySymbol();
        }

        public String getOrderPriceText() {
            double orderPrice = getOrderPrice();
            if (orderPrice <= ClientTraceData.b.f47a) {
                return "0.00";
            }
            return String.format("%.2f", new Object[]{Double.valueOf(orderPrice)});
        }

        public int getSelectedNum() {
            return this.data.getIntValue("selectedNum");
        }

        public void setSelectedNum(String selectedNum) {
            this.data.put("selectedNum", (Object) selectedNum);
        }

        public InstallmentOption getOptionBySelectedNum(int selectedNum) {
            for (InstallmentOption option : this.options) {
                if (option.getNum() == selectedNum) {
                    return option;
                }
            }
            return null;
        }

        private void loadOptionArray(JSONArray array) {
            if (array != null && !array.isEmpty()) {
                this.options = new ArrayList(array.size());
                Iterator<Object> it = array.iterator();
                while (it.hasNext()) {
                    try {
                        this.options.add(new InstallmentOption((JSONObject) it.next(), this.engine));
                    } catch (Throwable th) {
                    }
                }
            }
        }
    }
}
