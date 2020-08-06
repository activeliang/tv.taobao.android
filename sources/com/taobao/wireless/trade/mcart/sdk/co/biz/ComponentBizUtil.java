package com.taobao.wireless.trade.mcart.sdk.co.biz;

import android.text.TextUtils;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.taobao.wireless.trade.mcart.sdk.co.Component;
import com.taobao.wireless.trade.mcart.sdk.co.ComponentStatus;
import com.taobao.wireless.trade.mcart.sdk.co.ComponentTag;
import com.taobao.wireless.trade.mcart.sdk.constant.CartFrom;
import com.taobao.wireless.trade.mcart.sdk.engine.CartEngine;
import com.taobao.wireless.trade.mcart.sdk.engine.CartEngineContext;
import com.taobao.wireless.trade.mcart.sdk.engine.CartParseModule;
import com.taobao.wireless.trade.mcart.sdk.utils.CartManageUtil;
import com.taobao.wireless.trade.mcart.sdk.utils.StringUtils;
import java.math.BigDecimal;
import java.util.Currency;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class ComponentBizUtil {
    public static void refreshComponentInfoWithoutCheckStatus(CartFrom cartFrom) {
        refreshComponentInfo(true, cartFrom);
    }

    private static void refreshComponentInfo(boolean includeRealpay, CartFrom cartFrom) {
        Map<String, Component> index;
        BigDecimal amount;
        String currencySymbol;
        CartEngineContext context = CartEngine.getInstance(cartFrom).getContext();
        if (context != null && (index = context.getIndex()) != null && index.size() > 0) {
            FooterComponent footerComponent = null;
            long sumPrice = 0;
            int sumQuantity = 0;
            long sumWeight = 0;
            boolean isSubmitChecked = false;
            Set<String> currencySymbols = new HashSet<>();
            int currencyUnitFactor = 100;
            for (Map.Entry<String, Component> entry : index.entrySet()) {
                Component obj = entry.getValue();
                if (obj != null) {
                    Component component = obj;
                    if (ComponentTag.getComponentTagByDesc(component.getTag()) == ComponentTag.FOOTER && (component instanceof FooterComponent)) {
                        footerComponent = (FooterComponent) component;
                    }
                    if (ComponentTag.getComponentTagByDesc(component.getTag()) == ComponentTag.ITEM && (component instanceof ItemComponent)) {
                        ItemComponent itemComponentTmp = (ItemComponent) component;
                        itemComponentTmp.getItemPay().setAfterPromPrice((Long) null);
                        if (itemComponentTmp.isChecked() && itemComponentTmp.isValid()) {
                            if (itemComponentTmp.getItemPay() != null) {
                                sumPrice += itemComponentTmp.getItemPay().getTotalNowPrice();
                                currencyUnitFactor = itemComponentTmp.getItemPay().getCurrencyUnitFactor();
                            }
                            if (itemComponentTmp.getWeight() != null) {
                                sumWeight += itemComponentTmp.getWeight().getValue();
                            }
                            currencySymbols.add(itemComponentTmp.getItemPay().getCurrencySymbol());
                            sumQuantity++;
                            isSubmitChecked = true;
                        }
                    }
                }
            }
            if (footerComponent != null && footerComponent.getPay() != null) {
                if (includeRealpay) {
                    if (currencyUnitFactor == 100) {
                        amount = BigDecimal.valueOf(sumPrice, 2);
                    } else {
                        amount = new BigDecimal(sumPrice).divide(new BigDecimal(currencyUnitFactor));
                        footerComponent.getPay().setCurrencyUnitFactor(currencyUnitFactor);
                    }
                    footerComponent.getPay().setPrice(sumPrice);
                    if (currencySymbols.size() > 1) {
                        footerComponent.getPay().setPriceTitle("包含多种货币");
                        footerComponent.getPay().setMultipleCurrencySymbol(true);
                        footerComponent.getPay().setCurrencySymbol((String) null);
                    } else {
                        if (currencySymbols.iterator().hasNext()) {
                            currencySymbol = currencySymbols.iterator().next();
                        } else {
                            currencySymbol = "￥";
                        }
                        footerComponent.getPay().setMultipleCurrencySymbol(false);
                        footerComponent.getPay().setCurrencySymbol(currencySymbol);
                        if (sumPrice == 0) {
                            footerComponent.getPay().setPriceTitle("￥ 0");
                            footerComponent.getPay().setCurrencySymbol("￥");
                        } else {
                            footerComponent.getPay().setPriceTitle(currencySymbol + amount.toString());
                        }
                    }
                }
                if (footerComponent.getWeight() != null) {
                    footerComponent.getWeight().setValue(sumWeight);
                    footerComponent.getWeight().setTitle(format((double) sumWeight));
                }
                if (footerComponent.getQuantity() != null) {
                    footerComponent.getQuantity().setValue(sumQuantity);
                }
                if (footerComponent.getSubmit() != null) {
                    if (footerComponent.getSubmit() != null) {
                        footerComponent.getSubmit().setTitle("结算(" + sumQuantity + ")");
                    }
                    if (!isSubmitChecked) {
                        footerComponent.getSubmit().setStatus(ComponentStatus.DISABLE);
                    } else {
                        footerComponent.getSubmit().setStatus(ComponentStatus.NORMAL);
                    }
                }
                long sumTsmDiscount = 0;
                if (sumPrice > 0) {
                    sumTsmDiscount = footerComponent.getPay().getTsmTotalDiscountPrice();
                }
                footerComponent.getPay().setTsmTotalDiscount("已优惠 ￥" + BigDecimal.valueOf(sumTsmDiscount, Math.max(0, Currency.getInstance("CNY").getDefaultFractionDigits())).toString());
                footerComponent.getPay().setFromLocal();
            }
        }
    }

    private static String format(double d) {
        return new BigDecimal(String.valueOf(d)).divide(new BigDecimal(String.valueOf(1000)), 3, 6).doubleValue() + "kg";
    }

    public static void refreshCheckAllComponentCheckStatus(CartFrom cartFrom) {
        JSONObject dataTmp;
        Map<String, Component> index;
        CartEngineContext context = CartEngine.getInstance(cartFrom).getContext();
        boolean double11Mode = CartEngine.getInstance(cartFrom).isDouble11Mode();
        FooterComponent footerComponent = null;
        boolean isChecked = true;
        int validItems = 0;
        if (!(context == null || (index = context.getIndex()) == null || index.size() <= 0)) {
            for (Map.Entry<String, Component> entry : index.entrySet()) {
                Component obj = entry.getValue();
                if (obj != null) {
                    Component component = obj;
                    if (ComponentTag.getComponentTagByDesc(component.getTag()) == ComponentTag.ITEM && (component instanceof ItemComponent)) {
                        ItemComponent itemComponent = (ItemComponent) component;
                        if (itemComponent.isValid()) {
                            validItems++;
                        }
                        if (!itemComponent.isChecked() && itemComponent.isValid()) {
                            if (double11Mode && itemComponent.isDouble11Item()) {
                                isChecked = false;
                            } else if (!double11Mode) {
                                isChecked = false;
                            }
                        }
                    }
                    if (ComponentTag.getComponentTagByDesc(component.getTag()) == ComponentTag.FOOTER && (component instanceof FooterComponent)) {
                        footerComponent = (FooterComponent) component;
                    }
                }
            }
        }
        if (validItems == 0) {
            isChecked = false;
        }
        if (footerComponent != null && footerComponent.getCheckAll() != null && (dataTmp = footerComponent.getCheckAll().getData()) != null) {
            dataTmp.put("checked", (Object) Boolean.valueOf(isChecked));
        }
    }

    public static void refreshRelationItemCheckStatus(ItemComponent itemComponent, boolean checked) {
        Component itemParent;
        CartParseModule cartParseModule;
        List<ItemComponent> itemComponents;
        JSONObject fieldsTmp;
        CartFrom cartFrom = itemComponent != null ? itemComponent.getCartFrom() : CartFrom.DEFAULT_CLIENT;
        if (CartEngine.getInstance(cartFrom).getContext() != null && itemComponent != null && (itemParent = itemComponent.getParent()) != null && ComponentTag.getComponentTagByDesc(itemParent.getTag()) == ComponentTag.GROUP && (itemParent instanceof GroupComponent)) {
            GroupComponent groupComponent = (GroupComponent) itemParent;
            if (groupComponent.getIsRelationItem() && (cartParseModule = CartEngine.getInstance(cartFrom).getParseModule()) != null && (itemComponents = cartParseModule.getItemComponentsByOrderId(groupComponent.getComponentId())) != null && itemComponents.size() > 0) {
                for (ItemComponent itemComponentTmp : itemComponents) {
                    if (!(itemComponentTmp == null || (fieldsTmp = itemComponentTmp.getFields()) == null)) {
                        fieldsTmp.put("checked", (Object) Boolean.valueOf(checked));
                    }
                }
            }
        }
    }

    public static GroupComponent getItemComponentsGroupComponent(ItemComponent itemComponent) {
        Component itemParent;
        if (CartEngine.getInstance(itemComponent != null ? itemComponent.getCartFrom() : CartFrom.DEFAULT_CLIENT).getContext() == null || itemComponent == null || (itemParent = itemComponent.getParent()) == null || ComponentTag.getComponentTagByDesc(itemParent.getTag()) != ComponentTag.GROUP || !(itemParent instanceof GroupComponent)) {
            return null;
        }
        return (GroupComponent) itemParent;
    }

    public static void refreshShopComponentCheckStatus(ItemComponent itemComponent) {
        Component itemParent;
        Component bundleParent;
        CartEngineContext context = CartEngine.getInstance(itemComponent != null ? itemComponent.getCartFrom() : CartFrom.DEFAULT_CLIENT).getContext();
        if (context != null && itemComponent != null && (itemParent = itemComponent.getParent()) != null && ComponentTag.getComponentTagByDesc(itemParent.getTag()) == ComponentTag.GROUP && (itemParent instanceof GroupComponent) && (bundleParent = itemParent.getParent()) != null) {
            refereshShopComponent(context, bundleParent);
        }
    }

    private static void refereshShopComponent(CartEngineContext context, Component bundleParent) {
        JSONObject shopFieldsTmp;
        String subComponentKey;
        ShopComponent shopComponent = null;
        boolean isShopChecked = true;
        JSONObject structure = context.getStructure();
        Map<String, Component> index = context.getIndex();
        if (structure != null && index != null) {
            JSONArray subComponentKeyArray = structure.getJSONArray(bundleParent.getComponentId());
            if (subComponentKeyArray != null) {
                Iterator<Object> it = subComponentKeyArray.iterator();
                while (it.hasNext()) {
                    Object obj = it.next();
                    if (!(obj == null || (subComponentKey = (String) obj) == null)) {
                        Component component = index.get(subComponentKey);
                        if (component != null && ComponentTag.getComponentTagByDesc(component.getTag()) == ComponentTag.SHOP) {
                            shopComponent = (ShopComponent) component;
                        }
                        if (component != null && ComponentTag.getComponentTagByDesc(component.getTag()) == ComponentTag.GROUP && isShopChecked) {
                            isShopChecked = isShopChecked(structure, index, component);
                        }
                    }
                }
            }
            if (shopComponent != null && (shopFieldsTmp = shopComponent.getFields()) != null) {
                shopFieldsTmp.put("checked", (Object) Boolean.valueOf(isShopChecked));
            }
        }
    }

    private static boolean isShopChecked(JSONObject structure, Map<String, Component> index, Component component) {
        String subComponentKeyByOrder;
        Component componentByOrder;
        JSONArray subComponentIdByOrder = structure.getJSONArray(component.getComponentId());
        if (subComponentIdByOrder == null) {
            return true;
        }
        Iterator<Object> it = subComponentIdByOrder.iterator();
        while (it.hasNext()) {
            Object objByOrder = it.next();
            if (!(objByOrder == null || (subComponentKeyByOrder = (String) objByOrder) == null || (componentByOrder = index.get(subComponentKeyByOrder)) == null || ComponentTag.getComponentTagByDesc(componentByOrder.getTag()) != ComponentTag.ITEM || !(componentByOrder instanceof ItemComponent))) {
                ItemComponent itemComponentByOrder = (ItemComponent) componentByOrder;
                if (!itemComponentByOrder.isChecked() && (itemComponentByOrder.isValid() || ((CartManageUtil.isManaging() && itemComponentByOrder.isPreBuyItem()) || (CartManageUtil.isManaging() && itemComponentByOrder.isSkuInvalid())))) {
                    return false;
                }
            }
        }
        return true;
    }

    public static void refreshAllShopAndCheckAllComponentCheckStatus(CartFrom cartFrom) {
        JSONObject dataTmp;
        Map<String, Component> index;
        CartEngineContext context = CartEngine.getInstance(cartFrom).getContext();
        FooterComponent footerComponent = null;
        boolean isChecked = true;
        int validItems = 0;
        if (!(context == null || (index = context.getIndex()) == null || index.size() <= 0)) {
            HashSet<String> bundleIds = new HashSet<>();
            for (Map.Entry<String, Component> entry : index.entrySet()) {
                Component obj = entry.getValue();
                if (obj != null) {
                    Component component = obj;
                    if (ComponentTag.getComponentTagByDesc(component.getTag()) == ComponentTag.ITEM && (component instanceof ItemComponent)) {
                        ItemComponent itemComponent = (ItemComponent) component;
                        if (!itemComponent.isChecked() && (itemComponent.isValid() || (CartManageUtil.isManaging() && itemComponent.isPreBuyItem()))) {
                            validItems++;
                            isChecked = false;
                        }
                        if (itemComponent.isChecked() && (itemComponent.isValid() || (CartManageUtil.isManaging() && itemComponent.isPreBuyItem()))) {
                            validItems++;
                            String bundleId = itemComponent.getBundleId();
                            if (bundleId != null && !bundleIds.contains(bundleId)) {
                                refreshShopComponentCheckStatus(itemComponent);
                                bundleIds.add(bundleId);
                            }
                        }
                    }
                    if (ComponentTag.getComponentTagByDesc(component.getTag()) == ComponentTag.FOOTER && (component instanceof FooterComponent)) {
                        footerComponent = (FooterComponent) component;
                    }
                }
            }
        }
        if (validItems == 0) {
            isChecked = false;
        }
        if (footerComponent != null && footerComponent.getCheckAll() != null && (dataTmp = footerComponent.getCheckAll().getData()) != null) {
            dataTmp.put("checked", (Object) Boolean.valueOf(isChecked));
        }
    }

    public static void refreshRealQuantityWeightAndSubmitComponentInfo(CartFrom cartFrom) {
        boolean exclusiveRealpay;
        boolean z = true;
        if (cartFrom == CartFrom.TSM_NATIVE_TAOBAO || cartFrom == CartFrom.TSM_NATIVE_TMALL) {
            exclusiveRealpay = true;
        } else {
            exclusiveRealpay = false;
        }
        if (exclusiveRealpay) {
            z = false;
        }
        refreshComponentInfo(z, cartFrom);
    }

    public static ComponentCollectInfo getComponentCollectInfoByBundleId(ShopComponent shopComponent) {
        Map<String, Component> index;
        CartFrom cartFrom = shopComponent != null ? shopComponent.getCartFrom() : CartFrom.DEFAULT_CLIENT;
        String bundleId = null;
        ComponentCollectInfo componentCollectInfo = new ComponentCollectInfo();
        if (shopComponent != null) {
            Component shopParentComponent = shopComponent.getParent();
            componentCollectInfo.setSellerId(shopComponent.getSellerId());
            if (shopParentComponent != null && ComponentTag.getComponentTagByDesc(shopParentComponent.getTag()) == ComponentTag.BUNDLE && (shopParentComponent instanceof BundleComponent)) {
                bundleId = ((BundleComponent) shopParentComponent).getBundleId();
            }
        }
        CartEngineContext context = CartEngine.getInstance(cartFrom).getContext();
        if (!(context == null || bundleId == null || (index = context.getIndex()) == null || index.size() <= 0)) {
            long sumPrice = 0;
            long sumWeight = 0;
            StringBuffer itemidBuffer = new StringBuffer();
            for (Map.Entry<String, Component> entry : index.entrySet()) {
                Component obj = entry.getValue();
                if (obj != null) {
                    Component component = obj;
                    if (ComponentTag.getComponentTagByDesc(component.getTag()) == ComponentTag.ITEM && (component instanceof ItemComponent)) {
                        ItemComponent itemComponentTmp = (ItemComponent) component;
                        if (itemComponentTmp.isChecked() && itemComponentTmp.isValid() && itemComponentTmp.getBundleId() != null && itemComponentTmp.getBundleId().equalsIgnoreCase(bundleId)) {
                            if (itemComponentTmp.getItemPay() != null) {
                                sumPrice += itemComponentTmp.getItemPay().getTotalNowPrice();
                            }
                            if (itemComponentTmp.getWeight() != null) {
                                sumWeight += itemComponentTmp.getWeight().getValue();
                            }
                            itemidBuffer.append(itemComponentTmp.getItemId()).append(",");
                        }
                    }
                }
            }
            String itemIds = itemidBuffer.toString();
            int indexlast = itemidBuffer.lastIndexOf(",");
            if (indexlast > 0) {
                componentCollectInfo.setAucs(itemIds.substring(0, indexlast));
            }
            if (sumPrice > 0) {
                componentCollectInfo.setSumPrice(Long.valueOf(sumPrice));
            }
            if (sumWeight > 0) {
                componentCollectInfo.setSumWeight(Long.valueOf(sumWeight));
            }
        }
        return componentCollectInfo;
    }

    public static void refreshRealPayComponentInfo(CartFrom cartFrom) {
        Map<String, Component> index;
        BigDecimal amount;
        Integer pageNo;
        CartEngineContext context = CartEngine.getInstance(cartFrom).getContext();
        if (context != null && (index = context.getIndex()) != null && index.size() > 0) {
            FooterComponent footerComponent = null;
            long sumPrice = 0;
            long sumTsmDiscount = 0;
            Iterator<Map.Entry<String, Component>> itIndex = index.entrySet().iterator();
            while (true) {
                if (!itIndex.hasNext()) {
                    break;
                }
                Component obj = itIndex.next().getValue();
                if (obj != null) {
                    Component component = obj;
                    if (ComponentTag.getComponentTagByDesc(component.getTag()) == ComponentTag.FOOTER && (component instanceof FooterComponent)) {
                        footerComponent = (FooterComponent) component;
                        break;
                    }
                }
            }
            JSONObject pageMeta = context.getPageMeta();
            if (!(pageMeta == null || !pageMeta.containsKey("pageNo") || footerComponent == null || footerComponent.getPay() == null || (pageNo = pageMeta.getInteger("pageNo")) == null)) {
                context.getPerPageRealPay().put(pageNo, Long.valueOf(footerComponent.getPay().getPrice()));
                context.getTsmTotalDiscount().put(pageNo, Long.valueOf(footerComponent.getPay().getTsmTotalDiscountPrice()));
                for (int i = 1; i <= pageNo.intValue(); i++) {
                    Long priceTmp = context.getPerPageRealPay().get(Integer.valueOf(i));
                    Long priceDiscountTmp = context.getTsmTotalDiscount().get(Integer.valueOf(i));
                    if (priceTmp != null) {
                        sumPrice += priceTmp.longValue();
                    }
                    if (priceDiscountTmp != null) {
                        sumTsmDiscount += priceDiscountTmp.longValue();
                    }
                }
            }
            if (footerComponent != null && footerComponent.getPay() != null) {
                footerComponent.getPay().setPrice(sumPrice);
                int currencyUnitFactor = footerComponent.getPay().getCurrencyUnitFactor();
                if (currencyUnitFactor == 100) {
                    amount = BigDecimal.valueOf(sumPrice, 2);
                } else {
                    amount = new BigDecimal(sumPrice).divide(new BigDecimal(currencyUnitFactor));
                }
                String currencySymbol = footerComponent.getPay().getCurrencySymbol();
                if (!footerComponent.getPay().isMultipleCurrencySymbol()) {
                    if (StringUtils.isBlank(currencySymbol)) {
                        currencySymbol = "￥";
                    }
                    footerComponent.getPay().setPriceTitle(currencySymbol + amount.toString());
                } else if (sumPrice == 0) {
                    footerComponent.getPay().setPriceTitle("￥ 0");
                    footerComponent.getPay().setCurrencySymbol("￥");
                } else {
                    footerComponent.getPay().setPriceTitle("包含多种货币");
                }
                footerComponent.getPay().setTsmTotalDiscount("已优惠 ￥" + BigDecimal.valueOf(sumTsmDiscount, 2).toString());
            }
        }
    }

    public static ShopComponent getShopComponentByItemComponent(ItemComponent itemComponent) {
        return getShopComponentByItemComponent(CartEngine.getInstance(itemComponent != null ? itemComponent.getCartFrom() : CartFrom.DEFAULT_CLIENT).getContext(), itemComponent);
    }

    public static ShopComponent getShopComponentByItemComponent(CartEngineContext context, ItemComponent itemComponent) {
        Component itemParent;
        Component bundleParent;
        JSONArray subComponentKeyArray;
        String subComponentKey;
        Component component;
        if (context == null || itemComponent == null || (itemParent = itemComponent.getParent()) == null || ComponentTag.getComponentTagByDesc(itemParent.getTag()) != ComponentTag.GROUP || !(itemParent instanceof GroupComponent) || (bundleParent = itemParent.getParent()) == null) {
            return null;
        }
        JSONObject structure = context.getStructure();
        Map<String, Component> index = context.getIndex();
        if (structure == null || index == null || (subComponentKeyArray = structure.getJSONArray(bundleParent.getComponentId())) == null) {
            return null;
        }
        Iterator<Object> it = subComponentKeyArray.iterator();
        while (it.hasNext()) {
            Object obj = it.next();
            if (obj != null && (subComponentKey = (String) obj) != null && (component = index.get(subComponentKey)) != null && ComponentTag.getComponentTagByDesc(component.getTag()) == ComponentTag.SHOP) {
                return (ShopComponent) component;
            }
        }
        return null;
    }

    public static void refreshAllComponentChangeToCheckedStatus(CartFrom cartFrom) {
        Map<String, Component> index;
        CheckAll checkAll;
        CartEngineContext context = CartEngine.getInstance(cartFrom).getContext();
        if (context != null && (index = context.getIndex()) != null && index.size() > 0) {
            for (Map.Entry<String, Component> entry : index.entrySet()) {
                Component component = entry.getValue();
                if (component != null && ComponentTag.getComponentTagByDesc(component.getTag()) == ComponentTag.FOOTER && (component instanceof FooterComponent) && (checkAll = ((FooterComponent) component).getCheckAll()) != null && checkAll.isChecked()) {
                    refreshAllComponentCheckStatus(true, cartFrom);
                    return;
                }
            }
        }
    }

    /* JADX WARNING: Code restructure failed: missing block: B:15:0x004e, code lost:
        r7 = (com.taobao.wireless.trade.mcart.sdk.co.biz.ItemComponent) r1;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:29:0x0083, code lost:
        r9 = (com.taobao.wireless.trade.mcart.sdk.co.biz.ShopComponent) r1;
     */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public static void refreshAllComponentCheckStatus(boolean r12, com.taobao.wireless.trade.mcart.sdk.constant.CartFrom r13) {
        /*
            com.taobao.wireless.trade.mcart.sdk.engine.CartEngine r10 = com.taobao.wireless.trade.mcart.sdk.engine.CartEngine.getInstance(r13)
            com.taobao.wireless.trade.mcart.sdk.engine.CartEngineContext r0 = r10.getContext()
            com.taobao.wireless.trade.mcart.sdk.engine.CartEngine r10 = com.taobao.wireless.trade.mcart.sdk.engine.CartEngine.getInstance(r13)
            boolean r2 = r10.isDouble11Mode()
            if (r0 == 0) goto L_0x00c0
            java.util.Map r5 = r0.getIndex()
            if (r5 == 0) goto L_0x00c0
            int r10 = r5.size()
            if (r10 <= 0) goto L_0x00c0
            r7 = 0
            r9 = 0
            java.util.Set r10 = r5.entrySet()
            java.util.Iterator r6 = r10.iterator()
            r8 = 0
        L_0x0029:
            boolean r10 = r6.hasNext()
            if (r10 == 0) goto L_0x00c0
            java.lang.Object r3 = r6.next()
            java.util.Map$Entry r3 = (java.util.Map.Entry) r3
            java.lang.Object r8 = r3.getValue()
            com.taobao.wireless.trade.mcart.sdk.co.Component r8 = (com.taobao.wireless.trade.mcart.sdk.co.Component) r8
            if (r8 == 0) goto L_0x0029
            r1 = r8
            java.lang.String r10 = r1.getTag()
            com.taobao.wireless.trade.mcart.sdk.co.ComponentTag r10 = com.taobao.wireless.trade.mcart.sdk.co.ComponentTag.getComponentTagByDesc(r10)
            com.taobao.wireless.trade.mcart.sdk.co.ComponentTag r11 = com.taobao.wireless.trade.mcart.sdk.co.ComponentTag.ITEM
            if (r10 != r11) goto L_0x0073
            boolean r10 = r1 instanceof com.taobao.wireless.trade.mcart.sdk.co.biz.ItemComponent
            if (r10 == 0) goto L_0x0073
            r7 = r1
            com.taobao.wireless.trade.mcart.sdk.co.biz.ItemComponent r7 = (com.taobao.wireless.trade.mcart.sdk.co.biz.ItemComponent) r7
            com.alibaba.fastjson.JSONObject r4 = r7.getFields()
            if (r4 == 0) goto L_0x0073
            boolean r10 = r7.isChecked()
            if (r10 == r12) goto L_0x0073
            if (r12 == 0) goto L_0x00a9
            if (r2 == 0) goto L_0x0067
            boolean r10 = r7.isDouble11Item()
            if (r10 != 0) goto L_0x0069
        L_0x0067:
            if (r2 != 0) goto L_0x0073
        L_0x0069:
            java.lang.String r10 = "checked"
            java.lang.Boolean r11 = java.lang.Boolean.valueOf(r12)
            r4.put((java.lang.String) r10, (java.lang.Object) r11)
        L_0x0073:
            java.lang.String r10 = r1.getTag()
            com.taobao.wireless.trade.mcart.sdk.co.ComponentTag r10 = com.taobao.wireless.trade.mcart.sdk.co.ComponentTag.getComponentTagByDesc(r10)
            com.taobao.wireless.trade.mcart.sdk.co.ComponentTag r11 = com.taobao.wireless.trade.mcart.sdk.co.ComponentTag.SHOP
            if (r10 != r11) goto L_0x0029
            boolean r10 = r1 instanceof com.taobao.wireless.trade.mcart.sdk.co.biz.ShopComponent
            if (r10 == 0) goto L_0x0029
            r9 = r1
            com.taobao.wireless.trade.mcart.sdk.co.biz.ShopComponent r9 = (com.taobao.wireless.trade.mcart.sdk.co.biz.ShopComponent) r9
            com.alibaba.fastjson.JSONObject r4 = r9.getFields()
            if (r4 == 0) goto L_0x0029
            boolean r10 = r9.isChecked()
            if (r10 == r12) goto L_0x0029
            if (r12 == 0) goto L_0x00b4
            if (r2 == 0) goto L_0x009c
            boolean r10 = r9.isDouble11Shop()
            if (r10 != 0) goto L_0x009e
        L_0x009c:
            if (r2 != 0) goto L_0x0029
        L_0x009e:
            java.lang.String r10 = "checked"
            java.lang.Boolean r11 = java.lang.Boolean.valueOf(r12)
            r4.put((java.lang.String) r10, (java.lang.Object) r11)
            goto L_0x0029
        L_0x00a9:
            java.lang.String r10 = "checked"
            java.lang.Boolean r11 = java.lang.Boolean.valueOf(r12)
            r4.put((java.lang.String) r10, (java.lang.Object) r11)
            goto L_0x0073
        L_0x00b4:
            java.lang.String r10 = "checked"
            java.lang.Boolean r11 = java.lang.Boolean.valueOf(r12)
            r4.put((java.lang.String) r10, (java.lang.Object) r11)
            goto L_0x0029
        L_0x00c0:
            return
        */
        throw new UnsupportedOperationException("Method not decompiled: com.taobao.wireless.trade.mcart.sdk.co.biz.ComponentBizUtil.refreshAllComponentCheckStatus(boolean, com.taobao.wireless.trade.mcart.sdk.constant.CartFrom):void");
    }

    public static boolean degradeGroupPromotionAndShopPromotion(CartFrom cartFrom) {
        Map<String, Component> index;
        GroupPromotion groupPromotion;
        boolean state = false;
        CartEngineContext context = CartEngine.getInstance(cartFrom).getContext();
        if (!(context == null || (index = context.getIndex()) == null || index.size() <= 0)) {
            for (Map.Entry<String, Component> entry : index.entrySet()) {
                Component component = entry.getValue();
                if (component != null) {
                    if (ComponentTag.getComponentTagByDesc(component.getTag()) == ComponentTag.SHOP && (component instanceof ShopComponent)) {
                        Coudan coudan = ((ShopComponent) component).getCoudan();
                        if (coudan != null) {
                            coudan.updateKey("subTitleUrl", (Object) null);
                            coudan.updateKey("subTitle", (Object) null);
                            if (TextUtils.isEmpty(coudan.getTitle())) {
                                ((ShopComponent) component).updateCoudan((JSONObject) null);
                                state = true;
                            }
                        }
                    } else if (ComponentTag.getComponentTagByDesc(component.getTag()) == ComponentTag.GROUP && (component instanceof GroupComponent) && (groupPromotion = ((GroupComponent) component).getGroupPromotion()) != null) {
                        groupPromotion.updateKey("subTitle", (Object) null);
                        if (TextUtils.isEmpty(groupPromotion.getTitle())) {
                            ((GroupComponent) component).updatePromotion((JSONObject) null);
                            state = true;
                        }
                    }
                }
            }
        }
        return state;
    }

    public static void showDynamicCalcErrorFooter(CartFrom cartFrom) {
        FooterComponent footerComponent = getFooterComponent(cartFrom);
        if (footerComponent != null) {
            footerComponent.cleanDynamicCrossShopPromotions();
            if (footerComponent.getPay() != null) {
                footerComponent.getPay().setPostTitle("");
                footerComponent.getPay().setTotalDiscountTitle("优惠金额见结算页面");
            }
        }
    }

    public static void setFooterDiscountTitle(CartFrom cartFrom, String title) {
        FooterComponent footerComponent = getFooterComponent(cartFrom);
        if (footerComponent != null && footerComponent.getPay() != null) {
            footerComponent.getPay().setTotalDiscountTitle(title);
        }
    }

    public static FooterComponent getFooterComponent(CartFrom cartFrom) {
        Map<String, Component> index;
        CartEngineContext context = CartEngine.getInstance(cartFrom).getContext();
        if (!(context == null || (index = context.getIndex()) == null || index.size() <= 0)) {
            for (Map.Entry<String, Component> entry : index.entrySet()) {
                Component obj = entry.getValue();
                if (obj != null) {
                    Component component = obj;
                    if (ComponentTag.getComponentTagByDesc(component.getTag()) == ComponentTag.FOOTER && (component instanceof FooterComponent)) {
                        return (FooterComponent) component;
                    }
                }
            }
        }
        return null;
    }
}
