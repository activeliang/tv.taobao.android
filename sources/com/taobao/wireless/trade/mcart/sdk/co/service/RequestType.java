package com.taobao.wireless.trade.mcart.sdk.co.service;

import com.taobao.wireless.trade.mcart.sdk.co.biz.ActionType;

public enum RequestType {
    Query_Carts,
    Update_Carts,
    Update_Quantities,
    Add_Favorites,
    Delete_Carts,
    Delete_Invalid,
    Unfold_Shop,
    Check_Carts;

    public static RequestType getByActionType(ActionType type) {
        switch (type) {
            case UNKOWN:
                return null;
            case UPDATE_SKU:
                return Update_Carts;
            case UPDATE_QUANTITY:
                return Update_Quantities;
            case DELETE:
                return Delete_Carts;
            case ADD_FAVORITE:
                return Add_Favorites;
            case CHECK:
                return Check_Carts;
            case UNCHECK:
                return Check_Carts;
            case DELETE_INVALID:
                return Delete_Invalid;
            case UNFOLD_SHOP:
                return Unfold_Shop;
            default:
                return Query_Carts;
        }
    }
}
