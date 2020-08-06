package com.taobao.wireless.trade.mbuy.sdk.co.biz;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.taobao.wireless.trade.mbuy.sdk.co.Component;
import com.taobao.wireless.trade.mbuy.sdk.engine.BuyEngine;
import com.taobao.wireless.trade.mbuy.sdk.engine.BuyEngineContext;
import com.taobao.wireless.trade.mbuy.sdk.engine.RollbackProtocol;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class InstallmentComponent extends Component {
    private List<InstallmentOption> options;

    public InstallmentComponent(JSONObject data, BuyEngine engine) {
        super(data, engine);
        try {
            this.options = loadOptions(this.fields.getJSONArray("details"));
        } catch (Throwable th) {
            this.options = new ArrayList();
        }
    }

    public void reload(JSONObject data) {
        super.reload(data);
        try {
            this.options = loadOptions(this.fields.getJSONArray("details"));
        } catch (Throwable th) {
            this.options = new ArrayList();
        }
    }

    public String getTitle() {
        return this.fields.getString("display");
    }

    public boolean isChecked() {
        return this.fields.getBooleanValue("checked");
    }

    public void setChecked(final boolean checked) {
        this.engine.getContext().setRollbackProtocol(new RollbackProtocol() {
            public void rollback() {
                InstallmentComponent.this.fields.put("checked", (Object) Boolean.valueOf(!checked));
            }
        });
        this.fields.put("checked", (Object) Boolean.valueOf(checked));
        notifyLinkageDelegate();
    }

    public String getWarning() {
        return this.fields.getString("warning");
    }

    public int getNum() {
        return this.fields.getIntValue("selectedNum");
    }

    public void setNum(int num) {
        if (num != getNum()) {
            BuyEngineContext context = this.engine.getContext();
            final int originalNum = getNum();
            context.setRollbackProtocol(new RollbackProtocol() {
                public void rollback() {
                    InstallmentComponent.this.fields.put("selectedNum", (Object) Integer.valueOf(originalNum));
                }
            });
            this.fields.put("selectedNum", (Object) Integer.valueOf(num));
            notifyLinkageDelegate();
        }
    }

    public List<InstallmentOption> getOptions() {
        return this.options;
    }

    private List<InstallmentOption> loadOptions(JSONArray optionJSONArray) {
        if (optionJSONArray == null) {
            throw new IllegalStateException();
        }
        List<InstallmentOption> details = new ArrayList<>(optionJSONArray.size());
        Iterator<Object> it = optionJSONArray.iterator();
        while (it.hasNext()) {
            details.add(new InstallmentOption((JSONObject) it.next(), this.engine));
        }
        return details;
    }

    public InstallmentOption getOptionByNum(int num) {
        for (InstallmentOption option : this.options) {
            if (num == option.getNum()) {
                return option;
            }
        }
        return null;
    }

    public String getSelectedOptionName() {
        InstallmentOption option = getOptionByNum(getNum());
        return option != null ? option.getTitle() : "";
    }

    public String getTip() {
        return this.fields.getString("tip");
    }
}
