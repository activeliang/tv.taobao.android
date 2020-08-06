package com.taobao.wireless.detail.model;

import com.taobao.detail.DisplayTypeConstants;
import com.taobao.detail.clientDomain.TBDetailResultVO;
import com.taobao.detail.domain.DetailVO;
import com.taobao.detail.domain.feature.JHSItemInfo;
import com.taobao.wireless.detail.model.helper.DetailVOHelper;
import com.taobao.wireless.detail.model.vo.BaseControl;
import com.taobao.wireless.detail.model.vo.BuyActionVO;
import java.util.Map;
import java.util.Set;

public class BaseDetailModel {
    protected BaseSkuModel baseSkuModel;
    protected TBDetailResultVO detailVO;
    protected Map<String, String> options;

    public BaseDetailModel(TBDetailResultVO detailVO2, Map<String, String> options2) {
        this.detailVO = detailVO2;
        this.options = options2;
        this.baseSkuModel = new BaseSkuModel(detailVO2);
    }

    public BaseSkuModel getSkuModel() {
        return this.baseSkuModel;
    }

    public boolean isDataComplete() {
        return false;
    }

    public String getDegradeUrl() {
        if (this.detailVO.itemControl == null) {
            return null;
        }
        return this.detailVO.itemControl.degradedItemUrl;
    }

    public boolean isInvalid() {
        return false;
    }

    public boolean isFeatureNotSupport(Set<String> features) {
        if (features == null) {
            return true;
        }
        return isMatched(this.detailVO.displayType, features);
    }

    private boolean isMatched(String[] arr, Set<String> featureSet) {
        if (arr == null || arr.length == 0) {
            return true;
        }
        for (String feature : arr) {
            if (!featureSet.contains(feature)) {
                return false;
            }
        }
        return true;
    }

    public JHSItemInfo getJhsItemInfo() {
        if (!isJHS()) {
            return null;
        }
        return (JHSItemInfo) getFeatureObj(DisplayTypeConstants.JHS, JHSItemInfo.class);
    }

    public boolean isJHS() {
        return isThisType(DisplayTypeConstants.JHS);
    }

    public boolean isEbook() {
        return isThisType(DisplayTypeConstants.EBOOK);
    }

    public boolean isPreSale() {
        return isThisType(DisplayTypeConstants.PRE_SALE);
    }

    public boolean isSecKill() {
        return isThisType(DisplayTypeConstants.SEC_KILL);
    }

    public boolean isWRTuan() {
        return isThisType(DisplayTypeConstants.WANRENTUAN);
    }

    public boolean isSuperMarket() {
        return isThisType(DisplayTypeConstants.SUPERMARKET);
    }

    public boolean isThisType(String proType) {
        return DetailVOHelper.hasFeatureType(this.detailVO, proType);
    }

    /* access modifiers changed from: protected */
    public final <T> T getFeatureObj(String key, Class<T> clazz) {
        return DetailVOHelper.getFeatureObj(this.detailVO, key, clazz);
    }

    public BaseControl getControl() {
        BaseControl baseControl = new BaseControl();
        DetailVOHelper.calControl(baseControl, this.detailVO, this.baseSkuModel.skuId);
        return baseControl;
    }

    public BuyActionVO getBuyAction() {
        BuyActionVO buyAction = new BuyActionVO();
        BaseControl controlVO = getControl();
        buyAction.controlVO = controlVO;
        if (!this.baseSkuModel.isSkuSelected()) {
            controlVO.msgTip = "请先选择商品属性";
            controlVO.buySupport = false;
            controlVO.cartSupport = false;
        } else {
            DetailVO.DynamicItem.ItemControl itemControl = this.detailVO.itemControl;
            buyAction.itemId = DetailVOHelper.getItemNumId(this.detailVO);
            buyAction.skuId = this.baseSkuModel.skuId;
            buyAction.options = this.options;
            buyAction.useV3Trade = "buildOrderVersion=3.0".equals(itemControl.buyUrl);
            if (itemControl != null) {
                buyAction.buyUrl = itemControl.buyUrl;
            }
        }
        return buyAction;
    }
}
