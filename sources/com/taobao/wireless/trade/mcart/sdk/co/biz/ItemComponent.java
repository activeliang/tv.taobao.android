package com.taobao.wireless.trade.mcart.sdk.co.biz;

import com.alibaba.appmonitor.sample.SampleConfigConstant;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.taobao.wireless.trade.mcart.sdk.co.Component;
import com.taobao.wireless.trade.mcart.sdk.constant.CartFrom;
import com.taobao.wireless.trade.mcart.sdk.engine.CartEngine;
import com.taobao.wireless.trade.mcart.sdk.engine.CartEngineContext;
import com.taobao.wireless.trade.mcart.sdk.engine.RollbackProtocol;
import com.taobao.wireless.trade.mcart.sdk.utils.McartConstants;
import com.taobao.wireless.trade.mcart.sdk.utils.NotificationCenterImpl;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

public class ItemComponent extends Component {
    private ItemExtra extra = null;
    private HashMap<String, List<Icon>> icons = null;
    private boolean isDouble11Item = false;
    private boolean isPreBuyItem = false;
    private ItemPay itemPay = null;
    private ItemServices itemServices = null;
    private List<ItemLogo> logos;
    private List<String> operateList = null;
    private ItemQuantity quantity = null;
    private long quantityOrigin;
    private Sku sku = null;
    private String skuIdOrigin;
    private Weight weight = null;

    public ItemComponent(JSONObject data, CartFrom cartFrom) {
        super(data, cartFrom);
        JSONObject quantity2 = this.fields.getJSONObject("quantity");
        if (quantity2 != null) {
            this.quantityOrigin = quantity2.getLongValue("quantity");
        }
        JSONObject sku2 = this.fields.getJSONObject("sku");
        if (sku2 != null) {
            this.skuIdOrigin = sku2.getString("skuId");
        }
        if (!isValid() && isPreHotItem()) {
            this.isPreBuyItem = true;
        }
    }

    public boolean isPreBuyItem() {
        return this.isPreBuyItem;
    }

    public void setPreBuyItem(boolean preBuyItem) {
        this.isPreBuyItem = preBuyItem;
    }

    public void reload(JSONObject data) {
        super.reload(data);
        this.itemServices = generateItemServices();
        this.sku = generateSku();
        this.itemPay = generateItemPay();
        this.operateList = generateOperateList();
        this.quantity = generateItemQuantity();
        this.weight = generateWeight();
        this.icons = generateIcons();
        this.logos = generateLogos();
        this.extra = generateExtra();
    }

    private List<ItemLogo> generateLogos() {
        try {
            List<ItemLogo> itemLogos = new ArrayList<>();
            JSONArray logoArray = this.fields.getJSONArray("logos");
            for (int i = 0; i < logoArray.size(); i++) {
                ItemLogo itemLogo = (ItemLogo) logoArray.getObject(i, ItemLogo.class);
                if (itemLogo != null) {
                    itemLogos.add(itemLogo);
                }
            }
            return itemLogos;
        } catch (Exception e) {
            return null;
        }
    }

    public String getCheckedBackgroundColor() {
        if (this.fields != null) {
            return this.fields.getString("checkedBackgroundColor");
        }
        return null;
    }

    public String getCartId() {
        return this.fields.getString("cartId");
    }

    public String getRuleId() {
        return this.fields.getString("ruleId");
    }

    public String getItemId() {
        return this.fields.getString("itemId");
    }

    public String getPic() {
        return this.fields.getString("pic");
    }

    public String getOuterUrl() {
        return this.fields.getString("outerUrl");
    }

    public long getSellerId() {
        return this.fields.getLongValue("sellerId");
    }

    public String getShopId() {
        return this.fields.getString("shopId");
    }

    public boolean isValid() {
        return this.fields.getBooleanValue("valid");
    }

    public boolean isSkuInvalid() {
        Sku sku2 = getSku();
        if (sku2 != null) {
            return sku2.isSkuInvalid();
        }
        return false;
    }

    public String getSkuInvalidMsg() {
        Sku sku2 = getSku();
        if (sku2 != null) {
            return sku2.getInvalidMsg();
        }
        return null;
    }

    public boolean isChecked() {
        return this.fields.getBooleanValue("checked");
    }

    public boolean isPreSell() {
        return isBeforePaymentForPreSell() || isInPaymentForPreSell();
    }

    public boolean isBeforePaymentForPreSell() {
        return "1".equals(this.fields.getString("preSellStatus"));
    }

    public boolean isInPaymentForPreSell() {
        return "2".equals(this.fields.getString("preSellStatus"));
    }

    public String getBundleId() {
        return this.fields.getString("bundleId");
    }

    public String getTitleInCheckBox() {
        return this.fields.getString("titleInCheckBox");
    }

    public boolean isPreHotItem() {
        return "1".equals(this.fields.getString("jhsStatus"));
    }

    public String getBundleType() {
        return this.fields.getString("bundleType");
    }

    public boolean isShowCheckBox() {
        return this.fields.getBooleanValue("showCheckBox");
    }

    public String getItemRecParamId() {
        return this.fields.getString("itemRecParamId");
    }

    public boolean isDouble11Item() {
        return this.isDouble11Item;
    }

    public void setIsDoubleItem(boolean isDoubleItem) {
        this.isDouble11Item = isDoubleItem;
    }

    public void setChecked(boolean checked, boolean notifyMessage) {
        this.fields.put("checked", (Object) Boolean.valueOf(checked));
        refreshRelationItem(this, checked);
        refreshShopComponent(this);
        refreshCheckAllComponent();
        refreshAllComponent();
        if (notifyMessage) {
            NotificationCenterImpl.getInstance().postNotification(McartConstants.CART_CHECK_SUCCESS, this);
        }
    }

    public void setChecked(boolean checked) {
        setChecked(checked, true);
    }

    private void refreshRelationItem(ItemComponent itemComponent, boolean checked) {
        ComponentBizUtil.refreshRelationItemCheckStatus(itemComponent, checked);
    }

    private void refreshShopComponent(ItemComponent itemComponent) {
        ComponentBizUtil.refreshShopComponentCheckStatus(itemComponent);
    }

    public JSONObject convertToSubmitData() {
        JSONObject data = new JSONObject();
        JSONObject value = new JSONObject();
        value.put("itemId", (Object) this.fields.getString("itemId"));
        value.put("cartId", (Object) this.fields.getString("cartId"));
        value.put("ruleId", (Object) this.fields.getString("ruleId"));
        value.put("bundleId", (Object) this.fields.getString("bundleId"));
        if (getSku() != null) {
            value.put("skuId", (Object) getSku().getSkuId());
        }
        value.put("skuInvalid", (Object) "false");
        if (getSku() != null && isSkuInvalid()) {
            value.put("skuInvalid", (Object) "true");
        }
        value.put("valid", (Object) this.fields.getString("valid"));
        if (getItemQuantity() != null) {
            value.put("quantity", (Object) String.valueOf(getItemQuantity().getQuantity()));
        }
        value.put("checked", (Object) this.fields.getString("checked"));
        value.put("shopId", (Object) this.fields.getString("shopId"));
        data.put("fields", (Object) value);
        return data;
    }

    public String getMutex() {
        return this.fields.getString("mutex");
    }

    public String getExclude() {
        return this.fields.getString("exclude");
    }

    public String getSettlement() {
        return this.fields.getString("settlement");
    }

    public String getH5CartParam() {
        return this.fields.getString("h5CartParam");
    }

    public String getCodeMsg() {
        return this.fields.getString("codeMsg");
    }

    public String getInvalidItemParamId() {
        return this.fields.getString("invalidItemParamId");
    }

    public String getToBuy() {
        return this.fields.getString("toBuy");
    }

    public String getJuId() {
        return this.fields.getString("juId");
    }

    public String getTitleInCheckBoxColor() {
        return this.fields.getString("titleInCheckBoxColor");
    }

    public boolean isCanBatchRemove() {
        if (this.fields.containsKey("canBatchRemove")) {
            return this.fields.getBooleanValue("canBatchRemove");
        }
        return true;
    }

    public String getCode() {
        return this.fields.getString("code");
    }

    public ItemServices getItemServices() {
        if (this.itemServices == null) {
            this.itemServices = generateItemServices();
        }
        return this.itemServices;
    }

    private ItemServices generateItemServices() {
        JSONObject itemSerJSONObject = this.fields.getJSONObject("services");
        if (itemSerJSONObject != null) {
            return new ItemServices(itemSerJSONObject);
        }
        return null;
    }

    public String getUrl() {
        JSONObject controlParas;
        String urltmp = this.fields.getString("url");
        if (urltmp == null) {
            return urltmp;
        }
        int beginIndex = urltmp.indexOf("${");
        int endIndex = urltmp.indexOf("}");
        if (beginIndex < 0 || endIndex < 0) {
            return urltmp;
        }
        String urlKey = urltmp.substring(beginIndex + 2, endIndex);
        CartEngineContext context = CartEngine.getInstance(this.cartFrom).getContext();
        if (context == null || (controlParas = context.getControlParas()) == null || !controlParas.containsKey(urlKey)) {
            return urltmp;
        }
        String urltmp2 = urltmp.replace("${" + urlKey + "}", controlParas.getString(urlKey));
        int beginItemIndex = urltmp2.indexOf("${");
        int endItemIndex = urltmp2.indexOf("}");
        if (beginItemIndex < 0 || endItemIndex < 0) {
            return urltmp2;
        }
        return urltmp2.replace("${itemId}", this.fields.getString("itemId"));
    }

    public String getTitle() {
        return this.fields.getString("title");
    }

    public Sku getSku() {
        if (this.sku == null) {
            this.sku = generateSku();
        }
        return this.sku;
    }

    private Sku generateSku() {
        JSONObject sku2 = this.fields.getJSONObject("sku");
        if (sku2 != null) {
            try {
                return new Sku(sku2);
            } catch (Throwable th) {
            }
        }
        return null;
    }

    public ItemPay getItemPay() {
        if (this.itemPay == null) {
            this.itemPay = generateItemPay();
        }
        return this.itemPay;
    }

    private ItemPay generateItemPay() {
        JSONObject pay = this.fields.getJSONObject("pay");
        if (pay != null) {
            try {
                return new ItemPay(pay);
            } catch (Throwable th) {
            }
        }
        return null;
    }

    public List<String> getItemOperate() {
        if (this.operateList == null) {
            this.operateList = generateOperateList();
        }
        return this.operateList;
    }

    private List<String> generateOperateList() {
        List<String> operateList2 = new ArrayList<>();
        JSONArray operateArray = this.fields.getJSONArray("operate");
        if (operateArray != null && !operateArray.isEmpty()) {
            Iterator<Object> it = operateArray.iterator();
            while (it.hasNext()) {
                Object obj = it.next();
                if (obj != null) {
                    try {
                        operateList2.add((String) obj);
                    } catch (Throwable th) {
                    }
                }
            }
        }
        return operateList2;
    }

    public ItemQuantity getItemQuantity() {
        if (this.quantity == null) {
            this.quantity = generateItemQuantity();
        }
        return this.quantity;
    }

    private ItemQuantity generateItemQuantity() {
        JSONObject quantity2 = this.fields.getJSONObject("quantity");
        if (quantity2 != null) {
            try {
                return new ItemQuantity(quantity2);
            } catch (Throwable th) {
            }
        }
        return null;
    }

    public ItemExtra getItemExtra() {
        if (this.extra == null) {
            this.extra = generateExtra();
        }
        return this.extra;
    }

    private ItemExtra generateExtra() {
        JSONObject extra2 = this.fields.getJSONObject(SampleConfigConstant.ACCURATE);
        if (extra2 != null) {
            try {
                return new ItemExtra(extra2);
            } catch (Throwable th) {
            }
        }
        return null;
    }

    public Weight getWeight() {
        if (this.weight == null) {
            this.weight = generateWeight();
        }
        return this.weight;
    }

    private Weight generateWeight() {
        JSONObject weight2 = this.fields.getJSONObject("weight");
        if (weight2 != null) {
            try {
                return new Weight(weight2);
            } catch (Throwable th) {
            }
        }
        return null;
    }

    public HashMap<String, List<Icon>> getBizIcon() {
        if (this.icons == null) {
            this.icons = generateIcons();
        }
        return this.icons;
    }

    private HashMap<String, List<Icon>> generateIcons() {
        List<Icon> iconList;
        HashMap<String, List<Icon>> icons2 = new HashMap<>();
        JSONObject bizIcon = this.fields.getJSONObject("bizIcon");
        if (bizIcon != null) {
            try {
                BizIconType[] values = BizIconType.values();
                int length = values.length;
                int i = 0;
                List<Icon> iconList2 = null;
                while (i < length) {
                    try {
                        BizIconType type = values[i];
                        if (bizIcon.containsKey(type.getCode())) {
                            iconList = new ArrayList<>();
                            JSONArray iconsArray = bizIcon.getJSONArray(type.getCode());
                            if (iconsArray != null && !iconsArray.isEmpty()) {
                                Iterator<Object> it = iconsArray.iterator();
                                while (it.hasNext()) {
                                    Object obj = it.next();
                                    if (obj != null) {
                                        try {
                                            iconList.add(new Icon((JSONObject) obj));
                                        } catch (Throwable th) {
                                        }
                                    }
                                }
                                if (iconList.size() > 0) {
                                    icons2.put(type.getCode(), iconList);
                                }
                            }
                        } else {
                            iconList = iconList2;
                        }
                        i++;
                        iconList2 = iconList;
                    } catch (Throwable th2) {
                        List<Icon> list = iconList2;
                    }
                }
            } catch (Throwable th3) {
            }
        }
        return icons2;
    }

    public void setQuantity(long quantity2) {
        if (quantity2 != getItemQuantity().getQuantity()) {
            CartEngineContext context = CartEngine.getInstance(this.cartFrom).getContext();
            final long originalQuantity = getItemQuantity().getQuantity();
            this.quantityOrigin = originalQuantity;
            if (context != null) {
                context.setRollbackProtocol(new RollbackProtocol() {
                    public void rollback() {
                        JSONObject quantityOb = ItemComponent.this.fields.getJSONObject("quantity");
                        if (quantityOb != null) {
                            quantityOb.put("quantity", (Object) Long.valueOf(originalQuantity));
                        }
                    }
                });
            }
            JSONObject quantityOb = this.fields.getJSONObject("quantity");
            if (quantityOb != null) {
                quantityOb.put("quantity", (Object) Long.valueOf(quantity2));
            }
            notifyLinkageDelegate(CartEngine.getInstance(this.cartFrom));
        }
    }

    public void setSkuId(String skuId) {
        this.skuIdOrigin = skuId;
        JSONObject sku2 = this.fields.getJSONObject("sku");
        if (sku2 != null) {
            sku2.put("skuId", (Object) skuId);
        }
    }

    public void resetOriginData() {
        JSONObject quantityOb = this.fields.getJSONObject("quantity");
        if (quantityOb != null) {
            quantityOb.put("quantity", (Object) Long.valueOf(this.quantityOrigin));
        }
        JSONObject sku2 = this.fields.getJSONObject("sku");
        if (sku2 != null) {
            sku2.put("skuId", (Object) this.skuIdOrigin);
        }
    }

    public String toString() {
        return super.toString() + " - ItemComponent [itemId=" + getItemId() + ",pic=" + getPic() + ",sellerId=" + getSellerId() + ",shopId=" + getShopId() + ",valid=" + isValid() + ",checked=" + isChecked() + ",titleInCheckBox=" + getTitleInCheckBox() + ",cartId=" + getCartId() + ",bundleId=" + getBundleId() + ",bundleType=" + getBundleType() + ",showCheckBox=" + isShowCheckBox() + ",mutex=" + getMutex() + ",exclude=" + getExclude() + ",settlement=" + getSettlement() + ",h5CartParam=" + getH5CartParam() + ",codeMsg=" + getCodeMsg() + ",invalidItemParamId=" + getInvalidItemParamId() + ",toBuy=" + getToBuy() + ",juId=" + getJuId() + ",titleInCheckBoxColor=" + getTitleInCheckBoxColor() + ",canBatchRemove=" + isCanBatchRemove() + ",code=" + getCode() + ",itemServices=" + getItemServices() + ",url=" + getUrl() + ",title=" + getTitle() + ",sku=" + getSku() + ",pay=" + getItemPay() + ",extra=" + getItemExtra() + "]";
    }

    public List<ItemLogo> getLogos() {
        return this.logos;
    }

    public void setLogos(List<ItemLogo> logos2) {
        this.logos = logos2;
    }

    public JSONObject getTopList() {
        return this.fields.getJSONObject("topList");
    }

    public JSONObject getDiscover() {
        return this.fields.getJSONObject("discover");
    }
}
