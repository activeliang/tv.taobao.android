package com.taobao.wireless.trade.mcart.sdk.co.service;

import android.content.Context;
import android.text.TextUtils;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.taobao.tao.remotebusiness.IRemoteBaseListener;
import com.taobao.wireless.trade.mcart.sdk.co.Component;
import com.taobao.wireless.trade.mcart.sdk.co.biz.ActionType;
import com.taobao.wireless.trade.mcart.sdk.co.biz.CartQueryType;
import com.taobao.wireless.trade.mcart.sdk.co.business.AbstractCartRemoteBaseListener;
import com.taobao.wireless.trade.mcart.sdk.co.business.CartBusiness;
import com.taobao.wireless.trade.mcart.sdk.co.mtop.MtopTradeAddBagRequest;
import com.taobao.wireless.trade.mcart.sdk.co.mtop.MtopTradeBagToFavorRequest;
import com.taobao.wireless.trade.mcart.sdk.co.mtop.MtopTradeItemRecommendRequest;
import com.taobao.wireless.trade.mcart.sdk.co.mtop.MtopTradeQueryBagListRequest;
import com.taobao.wireless.trade.mcart.sdk.co.mtop.MtopTradeUnfoldShopRequest;
import com.taobao.wireless.trade.mcart.sdk.co.mtop.MtopTradeUpdateRequest;
import com.taobao.wireless.trade.mcart.sdk.constant.CartFrom;
import com.taobao.wireless.trade.mcart.sdk.engine.CartEngine;
import com.taobao.wireless.trade.mcart.sdk.engine.CartEngineContext;
import com.taobao.wireless.trade.mcart.sdk.engine.CartEngineForMtop;
import com.taobao.wireless.trade.mcart.sdk.engine.CartSubmitModule;
import com.taobao.wireless.trade.mcart.sdk.protocol.RequestDebug;
import com.taobao.wireless.trade.mcart.sdk.utils.McartConstants;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import mtopsdk.mtop.domain.IMTOPDataObject;

public class CartService {
    private CartBusiness cartBusiness;
    private CartFrom mCartFrom = CartFrom.DEFAULT_CLIENT;
    private RequestDebug requestDebug;
    private CartSubmitModule submitModule;

    private CartService() {
    }

    public CartService(CartFrom cartFrom) {
        this.mCartFrom = cartFrom == null ? CartFrom.DEFAULT_CLIENT : cartFrom;
        this.submitModule = CartEngine.getInstance(this.mCartFrom).getSubmitModule();
        this.cartBusiness = new CartBusiness();
    }

    public void setRequestDebug(RequestDebug requestDebug2) {
        this.requestDebug = requestDebug2;
    }

    private void registerDebug(IMTOPDataObject request, IRemoteBaseListener listener) {
        if (this.requestDebug != null) {
            this.requestDebug.onRequestStart(request);
        }
        if (listener instanceof AbstractCartRemoteBaseListener) {
            ((AbstractCartRemoteBaseListener) listener).setRequestDebug(this.requestDebug);
        }
    }

    public void queryCartWithParam(CartQueryType type, CartParam param, IRemoteBaseListener listener, Context context, String ttid, int bizId, String divisionCode, boolean queryCartNextByPost) {
        JSONObject jsonObject;
        if (this.cartBusiness != null && this.submitModule != null) {
            MtopTradeQueryBagListRequest request = new MtopTradeQueryBagListRequest();
            request.setAPI_NAME(McartConstants.QUERYBAG_API_NAME);
            request.setVERSION(McartConstants.QUERYBAG_API_VERSION);
            request.setExtStatus(type.getCode());
            boolean refreshCache = false;
            if (param != null && param.isPage()) {
                request.setPage(param.isPage());
                if (param.getNetType() != null) {
                    request.setNetType(param.getNetType().getCode());
                }
                refreshCache = param.isRefreshCache();
                if (!refreshCache && (jsonObject = this.submitModule.generatePageData()) != null) {
                    request.setP(processRequestParameter(JSONObject.toJSONString(jsonObject)));
                    request.setFeature(getFeature());
                    processDataMd5(request);
                }
            }
            if (param != null) {
                request.setCartFrom(param.getCartFrom());
            }
            HashMap<String, String> map = new HashMap<>();
            map.put("mergeCombo", McartConstants.MERGE_COMBO);
            if (param != null && !TextUtils.isEmpty(param.getCartId())) {
                map.put("pushCartId", param.getCartId());
            }
            if (!TextUtils.isEmpty(divisionCode)) {
                map.put("divisionCode", divisionCode);
            }
            map.put("globalSell", "1");
            map.put("version", "1.1.1");
            expandExparamsInternal(map, RequestType.Query_Carts, (List<Component>) null);
            this.submitModule.expandExparams(map, RequestType.Query_Carts);
            request.setExParams(JSON.toJSONString(map));
            registerDebug(request, listener);
            this.cartBusiness.queryBagList(request, listener, context, ttid, refreshCache, bizId, queryCartNextByPost);
        }
    }

    private void processDataMd5(MtopTradeQueryBagListRequest request) {
        JSONObject controlParas;
        CartEngineContext context = CartEngineForMtop.getInstance(this.mCartFrom).getContext();
        if (context != null && context.getPageMeta() != null && context.getPageMeta().getInteger("pageNo") != null && context.getPageMeta().getIntValue("pageNo") == 1 && (controlParas = context.getControlParas()) != null) {
            request.setDataMd5(controlParas.getString("eTag"));
        }
    }

    public void updateCartSKUs(CartQueryType type, List<Component> updateComponents, IRemoteBaseListener listener, Context context, String ttid, String cartFrom, int bizId, String divisionCode, boolean needAllCheckedComponents) {
        JSONObject josnObject;
        if (this.cartBusiness != null && this.submitModule != null && (josnObject = this.submitModule.generateAsyncRequestData(updateComponents, ActionType.UPDATE_SKU, needAllCheckedComponents)) != null) {
            MtopTradeUpdateRequest request = new MtopTradeUpdateRequest();
            request.setExtStatus(type.getCode());
            request.setP(processRequestParameter(JSONObject.toJSONString(josnObject)));
            request.setFeature(getFeature());
            request.setCartFrom(cartFrom);
            HashMap<String, String> map = new HashMap<>();
            map.put("mergeCombo", McartConstants.MERGE_COMBO);
            if (!TextUtils.isEmpty(divisionCode)) {
                map.put("divisionCode", divisionCode);
            }
            map.put("globalSell", "1");
            map.put("version", "1.1.1");
            if (updateComponents != null) {
                Iterator<Component> it = updateComponents.iterator();
                while (true) {
                    if (it.hasNext()) {
                        Component c = it.next();
                        if (c != null && c.getFields() != null && c.getFields().getJSONObject("sku") != null && c.getFields().getJSONObject("sku").getBooleanValue("skuInvalid")) {
                            map.put("reselectionForSkuInvalid", "true");
                            break;
                        }
                    } else {
                        break;
                    }
                }
            }
            expandExparamsInternal(map, RequestType.Update_Carts, updateComponents);
            this.submitModule.expandExparams(map, RequestType.Update_Carts);
            request.setExParams(JSON.toJSONString(map));
            registerDebug(request, listener);
            this.cartBusiness.updateCartSku(request, listener, context, ttid, bizId);
        }
    }

    public void updateCartQuantities(CartQueryType type, List<Component> updateComponents, IRemoteBaseListener listener, Context context, String ttid, String cartFrom, int bizId, String divisionCode, boolean needAllCheckedComponents) {
        JSONObject jsonObject;
        if (this.cartBusiness != null && this.submitModule != null && (jsonObject = this.submitModule.generateAsyncRequestData(updateComponents, ActionType.UPDATE_QUANTITY, needAllCheckedComponents)) != null) {
            MtopTradeUpdateRequest request = new MtopTradeUpdateRequest();
            request.setExtStatus(type.getCode());
            request.setP(processRequestParameter(JSONObject.toJSONString(jsonObject)));
            request.setFeature(getFeature());
            request.setCartFrom(cartFrom);
            HashMap<String, String> map = new HashMap<>();
            map.put("mergeCombo", McartConstants.MERGE_COMBO);
            if (!TextUtils.isEmpty(divisionCode)) {
                map.put("divisionCode", divisionCode);
            }
            map.put("globalSell", "1");
            map.put("version", "1.1.1");
            expandExparamsInternal(map, RequestType.Update_Quantities, updateComponents);
            this.submitModule.expandExparams(map, RequestType.Update_Quantities);
            request.setExParams(JSON.toJSONString(map));
            registerDebug(request, listener);
            this.cartBusiness.updateBagCount(request, listener, context, ttid, bizId);
        }
    }

    public void addFavorites(CartQueryType type, List<Component> addComponents, IRemoteBaseListener listener, Context context, String ttid, String cartFrom, int bizId, boolean needAllCheckedComponents) {
        JSONObject jsonObject;
        if (this.cartBusiness != null && this.submitModule != null && (jsonObject = this.submitModule.generateAsyncRequestData(addComponents, ActionType.ADD_FAVORITE, needAllCheckedComponents)) != null) {
            MtopTradeBagToFavorRequest request = new MtopTradeBagToFavorRequest();
            request.setExtStatus(type.getCode());
            request.setP(processRequestParameter(JSONObject.toJSONString(jsonObject)));
            request.setFeature(getFeature());
            request.setCartFrom(cartFrom);
            HashMap<String, String> map = new HashMap<>();
            map.put("mergeCombo", McartConstants.MERGE_COMBO);
            map.put("version", "1.1.1");
            expandExparamsInternal(map, RequestType.Add_Favorites, addComponents);
            this.submitModule.expandExparams(map, RequestType.Add_Favorites);
            request.setExParams(JSON.toJSONString(map));
            registerDebug(request, listener);
            this.cartBusiness.bagToFavor(request, listener, context, ttid, bizId);
        }
    }

    public void addBag(String itemId, String skuId, long quantity, String exParams, IRemoteBaseListener listener, Context context, String ttid, String cartFrom, int bizId) {
        if (this.cartBusiness != null) {
            MtopTradeAddBagRequest request = new MtopTradeAddBagRequest();
            request.setItemId(itemId);
            request.setSkuId(skuId);
            request.setQuantity(quantity);
            request.setExParams(exParams);
            request.setFeature(getFeature());
            request.setCartFrom(cartFrom);
            registerDebug(request, listener);
            this.cartBusiness.addBag(request, listener, context, ttid, bizId);
        }
    }

    public void deleteCarts(CartQueryType type, List<Component> deleteComponents, IRemoteBaseListener listener, Context context, String ttid, String cartFrom, int bizId, String divisionCode, boolean needAllCheckedComponents) {
        JSONObject jsonObject;
        if (this.cartBusiness != null && this.submitModule != null && (jsonObject = this.submitModule.generateAsyncRequestData(deleteComponents, ActionType.DELETE, needAllCheckedComponents)) != null) {
            MtopTradeUpdateRequest request = new MtopTradeUpdateRequest();
            request.setExtStatus(type.getCode());
            request.setP(processRequestParameter(JSONObject.toJSONString(jsonObject)));
            request.setFeature(getFeature());
            request.setCartFrom(cartFrom);
            HashMap<String, String> map = new HashMap<>();
            map.put("mergeCombo", McartConstants.MERGE_COMBO);
            if (!TextUtils.isEmpty(divisionCode)) {
                map.put("divisionCode", divisionCode);
            }
            map.put("globalSell", "1");
            map.put("version", "1.1.1");
            expandExparamsInternal(map, RequestType.Delete_Carts, deleteComponents);
            this.submitModule.expandExparams(map, RequestType.Delete_Carts);
            request.setExParams(JSON.toJSONString(map));
            registerDebug(request, listener);
            this.cartBusiness.batchDelBag(request, listener, context, ttid, bizId);
        }
    }

    public void unfoldShop(List<Component> foldingBarComponents, IRemoteBaseListener listener, Context context, String ttid, String cartFrom, int bizId, String divisionCode, boolean needAllCheckedComponents) {
        JSONObject jsonObject;
        if (this.cartBusiness != null && this.submitModule != null && (jsonObject = this.submitModule.generateAsyncRequestData(foldingBarComponents, ActionType.UNFOLD_SHOP, needAllCheckedComponents)) != null) {
            MtopTradeUnfoldShopRequest request = new MtopTradeUnfoldShopRequest();
            request.setP(processRequestParameter(JSONObject.toJSONString(jsonObject)));
            request.setFeature(getFeature());
            request.setCartFrom(cartFrom);
            HashMap<String, String> map = new HashMap<>();
            map.put("mergeCombo", McartConstants.MERGE_COMBO);
            if (!TextUtils.isEmpty(divisionCode)) {
                map.put("divisionCode", divisionCode);
            }
            map.put("globalSell", "1");
            map.put("version", "1.1.1");
            expandExparamsInternal(map, RequestType.Unfold_Shop, foldingBarComponents);
            this.submitModule.expandExparams(map, RequestType.Unfold_Shop);
            request.setExParams(JSON.toJSONString(map));
            registerDebug(request, listener);
            this.cartBusiness.unfoldShop(request, listener, context, ttid, bizId);
        }
    }

    public void deleteInvalidItemCarts(CartQueryType type, List<Component> deleteComponents, IRemoteBaseListener listener, Context context, String ttid, String cartFrom, int bizId, String divisionCode, boolean needAllCheckedComponents) {
        JSONObject jsonObject;
        if (this.cartBusiness != null && this.submitModule != null && (jsonObject = this.submitModule.generateAsyncRequestData(deleteComponents, ActionType.DELETE_INVALID, needAllCheckedComponents)) != null) {
            MtopTradeUpdateRequest request = new MtopTradeUpdateRequest();
            request.setExtStatus(type.getCode());
            request.setP(processRequestParameter(JSONObject.toJSONString(jsonObject)));
            request.setFeature(getFeature());
            request.setCartFrom(cartFrom);
            HashMap<String, String> map = new HashMap<>();
            map.put("mergeCombo", McartConstants.MERGE_COMBO);
            if (!TextUtils.isEmpty(divisionCode)) {
                map.put("divisionCode", divisionCode);
            }
            map.put("globalSell", "1");
            map.put("version", "1.1.1");
            expandExparamsInternal(map, RequestType.Delete_Invalid, deleteComponents);
            this.submitModule.expandExparams(map, RequestType.Delete_Invalid);
            request.setExParams(JSON.toJSONString(map));
            registerDebug(request, listener);
            this.cartBusiness.batchDelBag(request, listener, context, ttid, bizId);
        }
    }

    public String buyCartIds() {
        JSONObject submitData;
        JSONArray cartIdsArray;
        if (!(this.cartBusiness == null || this.submitModule == null || (submitData = this.submitModule.generateFinalSubmitData()) == null || (cartIdsArray = submitData.getJSONArray("cartIds")) == null)) {
            StringBuffer cartIdsBuffer = new StringBuffer();
            Iterator<Object> it = cartIdsArray.iterator();
            while (it.hasNext()) {
                Object obj = it.next();
                if (obj != null) {
                    cartIdsBuffer.append((String) obj).append(",");
                }
            }
            String cartIds = cartIdsBuffer.toString();
            int indexLast = cartIds.lastIndexOf(",");
            if (indexLast > 0) {
                return cartIds.substring(0, indexLast);
            }
        }
        return null;
    }

    public void checkCartItems(CartQueryType type, List<Component> checkComponents, IRemoteBaseListener listener, Context context, String ttid, String cartFrom, int bizId, String divisionCode, boolean needAllCheckedComponents) {
        JSONObject jsonObject;
        if (this.cartBusiness != null && this.submitModule != null && (jsonObject = this.submitModule.generateAsyncRequestData(checkComponents, ActionType.CHECK, needAllCheckedComponents)) != null) {
            MtopTradeUpdateRequest request = new MtopTradeUpdateRequest();
            request.setExtStatus(type.getCode());
            request.setP(processRequestParameter(JSONObject.toJSONString(jsonObject)));
            request.setFeature(getFeature());
            request.setCartFrom(cartFrom);
            HashMap<String, String> map = new HashMap<>();
            map.put("mergeCombo", McartConstants.MERGE_COMBO);
            if (!TextUtils.isEmpty(divisionCode)) {
                map.put("divisionCode", divisionCode);
            }
            map.put("globalSell", "1");
            map.put("version", "1.1.1");
            expandExparamsInternal(map, RequestType.Check_Carts, checkComponents);
            this.submitModule.expandExparams(map, RequestType.Check_Carts);
            request.setExParams(JSON.toJSONString(map));
            registerDebug(request, listener);
            this.cartBusiness.checkCartItems(request, listener, context, ttid, bizId);
        }
    }

    private String processRequestParameter(String p) {
        if (isGzip()) {
            return compress(p);
        }
        return p;
    }

    private boolean isGzip() {
        JSONObject feature;
        CartEngineContext context = CartEngineForMtop.getInstance(this.mCartFrom).getContext();
        if (context == null || (feature = context.getFeature()) == null) {
            return false;
        }
        return feature.getBooleanValue("gzip");
    }

    private String getFeature() {
        JSONObject feature;
        CartEngineContext context = CartEngineForMtop.getInstance(this.mCartFrom).getContext();
        if (context == null || (feature = context.getFeature()) == null) {
            return "";
        }
        return JSONObject.toJSONString(feature);
    }

    /* JADX WARNING: Removed duplicated region for block: B:15:0x002f A[SYNTHETIC, Splitter:B:15:0x002f] */
    /* JADX WARNING: Removed duplicated region for block: B:23:0x003d A[SYNTHETIC, Splitter:B:23:0x003d] */
    /* JADX WARNING: Removed duplicated region for block: B:29:0x0047 A[SYNTHETIC, Splitter:B:29:0x0047] */
    /* JADX WARNING: Removed duplicated region for block: B:39:0x0053 A[SYNTHETIC, Splitter:B:39:0x0053] */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    private java.lang.String compress(java.lang.String r9) {
        /*
            r8 = this;
            if (r9 == 0) goto L_0x0008
            boolean r6 = r9.isEmpty()
            if (r6 == 0) goto L_0x000c
        L_0x0008:
            java.lang.String r9 = ""
        L_0x000b:
            return r9
        L_0x000c:
            r0 = 0
            java.io.ByteArrayOutputStream r4 = new java.io.ByteArrayOutputStream
            int r6 = r9.length()
            r4.<init>(r6)
            r5 = 1
            r2 = 0
            java.util.zip.GZIPOutputStream r3 = new java.util.zip.GZIPOutputStream     // Catch:{ IOException -> 0x0039, all -> 0x0044 }
            r3.<init>(r4)     // Catch:{ IOException -> 0x0039, all -> 0x0044 }
            java.lang.String r6 = "utf-8"
            byte[] r6 = r9.getBytes(r6)     // Catch:{ IOException -> 0x006a, all -> 0x0067 }
            r3.write(r6)     // Catch:{ IOException -> 0x006a, all -> 0x0067 }
            if (r3 == 0) goto L_0x002c
            r3.close()     // Catch:{ IOException -> 0x0035, all -> 0x0064 }
        L_0x002c:
            r2 = r3
        L_0x002d:
            if (r5 != 0) goto L_0x0053
            r4.close()     // Catch:{ IOException -> 0x0033 }
            goto L_0x000b
        L_0x0033:
            r6 = move-exception
            goto L_0x000b
        L_0x0035:
            r1 = move-exception
            r5 = 0
            r2 = r3
            goto L_0x002d
        L_0x0039:
            r1 = move-exception
        L_0x003a:
            r5 = 0
            if (r2 == 0) goto L_0x002d
            r2.close()     // Catch:{ IOException -> 0x0041 }
            goto L_0x002d
        L_0x0041:
            r1 = move-exception
            r5 = 0
            goto L_0x002d
        L_0x0044:
            r6 = move-exception
        L_0x0045:
            if (r2 == 0) goto L_0x004a
            r2.close()     // Catch:{ IOException -> 0x0050 }
        L_0x004a:
            throw r6     // Catch:{ all -> 0x004b }
        L_0x004b:
            r6 = move-exception
        L_0x004c:
            r4.close()     // Catch:{ IOException -> 0x0062 }
        L_0x004f:
            throw r6
        L_0x0050:
            r1 = move-exception
            r5 = 0
            goto L_0x004a
        L_0x0053:
            byte[] r0 = r4.toByteArray()     // Catch:{ all -> 0x004b }
            r4.close()     // Catch:{ IOException -> 0x0060 }
        L_0x005a:
            r6 = 0
            java.lang.String r9 = android.util.Base64.encodeToString(r0, r6)
            goto L_0x000b
        L_0x0060:
            r6 = move-exception
            goto L_0x005a
        L_0x0062:
            r7 = move-exception
            goto L_0x004f
        L_0x0064:
            r6 = move-exception
            r2 = r3
            goto L_0x004c
        L_0x0067:
            r6 = move-exception
            r2 = r3
            goto L_0x0045
        L_0x006a:
            r1 = move-exception
            r2 = r3
            goto L_0x003a
        */
        throw new UnsupportedOperationException("Method not decompiled: com.taobao.wireless.trade.mcart.sdk.co.service.CartService.compress(java.lang.String):java.lang.String");
    }

    public void getRecommendItems(Long appId, HashMap<String, String> params, IRemoteBaseListener listener, Context context, String ttid, int bizId) {
        if (this.cartBusiness != null) {
            MtopTradeItemRecommendRequest request = new MtopTradeItemRecommendRequest();
            request.setAppId(appId);
            request.setParams(params);
            registerDebug(request, listener);
            this.cartBusiness.getRecommendItems(request, listener, context, ttid, bizId);
        }
    }

    /* JADX WARNING: Code restructure failed: missing block: B:15:0x0029, code lost:
        r2 = (com.taobao.wireless.trade.mcart.sdk.co.biz.ItemComponent) r0;
     */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    private void expandExparamsInternal(java.util.Map<java.lang.String, java.lang.String> r8, com.taobao.wireless.trade.mcart.sdk.co.service.RequestType r9, java.util.List<com.taobao.wireless.trade.mcart.sdk.co.Component> r10) {
        /*
            r7 = this;
            com.taobao.wireless.trade.mcart.sdk.co.service.RequestType r4 = com.taobao.wireless.trade.mcart.sdk.co.service.RequestType.Delete_Carts
            if (r9 == r4) goto L_0x000c
            com.taobao.wireless.trade.mcart.sdk.co.service.RequestType r4 = com.taobao.wireless.trade.mcart.sdk.co.service.RequestType.Delete_Invalid
            if (r9 == r4) goto L_0x000c
            com.taobao.wireless.trade.mcart.sdk.co.service.RequestType r4 = com.taobao.wireless.trade.mcart.sdk.co.service.RequestType.Add_Favorites
            if (r9 != r4) goto L_0x0067
        L_0x000c:
            r3 = 0
            if (r10 == 0) goto L_0x005b
            int r4 = r10.size()
            if (r4 <= 0) goto L_0x005b
            java.util.Iterator r4 = r10.iterator()
        L_0x0019:
            boolean r5 = r4.hasNext()
            if (r5 == 0) goto L_0x005b
            java.lang.Object r0 = r4.next()
            com.taobao.wireless.trade.mcart.sdk.co.Component r0 = (com.taobao.wireless.trade.mcart.sdk.co.Component) r0
            boolean r5 = r0 instanceof com.taobao.wireless.trade.mcart.sdk.co.biz.ItemComponent
            if (r5 == 0) goto L_0x0019
            r2 = r0
            com.taobao.wireless.trade.mcart.sdk.co.biz.ItemComponent r2 = (com.taobao.wireless.trade.mcart.sdk.co.biz.ItemComponent) r2
            com.taobao.wireless.trade.mcart.sdk.co.biz.ItemExtra r1 = r2.getItemExtra()
            if (r1 == 0) goto L_0x0019
            boolean r5 = r1.isPriorityBuy()
            if (r5 == 0) goto L_0x0019
            java.lang.String r5 = r2.getCartId()
            boolean r5 = android.text.TextUtils.isEmpty(r5)
            if (r5 != 0) goto L_0x0019
            com.alibaba.fastjson.JSONObject r5 = r1.getData()
            if (r5 == 0) goto L_0x0019
            if (r3 != 0) goto L_0x004f
            com.alibaba.fastjson.JSONObject r3 = new com.alibaba.fastjson.JSONObject
            r3.<init>()
        L_0x004f:
            java.lang.String r5 = r2.getCartId()
            com.alibaba.fastjson.JSONObject r6 = r1.getData()
            r3.put((java.lang.String) r5, (java.lang.Object) r6)
            goto L_0x0019
        L_0x005b:
            if (r3 == 0) goto L_0x0067
            java.lang.String r4 = "itemsExtra"
            java.lang.String r5 = r3.toJSONString()
            r8.put(r4, r5)
        L_0x0067:
            return
        */
        throw new UnsupportedOperationException("Method not decompiled: com.taobao.wireless.trade.mcart.sdk.co.service.CartService.expandExparamsInternal(java.util.Map, com.taobao.wireless.trade.mcart.sdk.co.service.RequestType, java.util.List):void");
    }
}
