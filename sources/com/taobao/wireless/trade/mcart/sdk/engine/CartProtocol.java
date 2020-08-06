package com.taobao.wireless.trade.mcart.sdk.engine;

import android.content.Context;
import com.taobao.tao.remotebusiness.IRemoteBaseListener;
import com.taobao.wireless.trade.mcart.sdk.co.Component;
import com.taobao.wireless.trade.mcart.sdk.co.biz.CartQueryType;
import com.taobao.wireless.trade.mcart.sdk.co.service.CartParam;
import java.util.HashMap;
import java.util.List;

public interface CartProtocol extends CommonProtocol {
    void addBag(String str, String str2, long j, String str3, IRemoteBaseListener iRemoteBaseListener, Context context, String str4, String str5, int i);

    void addFavorites(CartQueryType cartQueryType, List<Component> list, IRemoteBaseListener iRemoteBaseListener, Context context, String str, String str2, int i, boolean z);

    void checkCartItems(CartQueryType cartQueryType, List<Component> list, IRemoteBaseListener iRemoteBaseListener, Context context, String str, String str2, int i, String str3, boolean z);

    void deleteCarts(CartQueryType cartQueryType, List<Component> list, IRemoteBaseListener iRemoteBaseListener, Context context, String str, String str2, int i, String str3, boolean z);

    void deleteInvalidItemCarts(CartQueryType cartQueryType, List<Component> list, IRemoteBaseListener iRemoteBaseListener, Context context, String str, String str2, int i, String str3, boolean z);

    void getRecommendItems(Long l, HashMap<String, String> hashMap, IRemoteBaseListener iRemoteBaseListener, Context context, String str, int i);

    void queryCartsWithParam(CartQueryType cartQueryType, CartParam cartParam, IRemoteBaseListener iRemoteBaseListener, Context context, String str, int i, String str2, boolean z);

    boolean removeAllCartQueryCache(Context context);

    void unfoldShop(List<Component> list, IRemoteBaseListener iRemoteBaseListener, Context context, String str, String str2, int i, String str3, boolean z);

    void updateCartQuantities(CartQueryType cartQueryType, List<Component> list, IRemoteBaseListener iRemoteBaseListener, Context context, String str, String str2, int i, String str3, boolean z);

    void updateCartSKUs(CartQueryType cartQueryType, List<Component> list, IRemoteBaseListener iRemoteBaseListener, Context context, String str, String str2, int i, String str3, boolean z);
}
