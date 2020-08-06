package com.yunos.tvtaobao.biz.widget.newsku.view;

import com.yunos.tvtaobao.biz.base.IView;
import com.yunos.tvtaobao.biz.request.bo.SkuPriceNum;
import com.yunos.tvtaobao.biz.request.bo.TBDetailResultV6;
import com.yunos.tvtaobao.biz.widget.newsku.SkuItemViewStatus;
import java.util.List;

public interface ISkuView extends IView {
    void initSkuKuCunAndPrice(SkuPriceNum skuPriceNum);

    void initSkuView(List<TBDetailResultV6.SkuBaseBean.PropsBeanX> list);

    void initTitle(String str);

    void onDialogDismiss();

    void onPromptDialog(int i, String str);

    void onShowError(String str);

    void showUnitBuy(int i);

    void updateImage(String str);

    void updateSkuKuCunAndPrice(SkuPriceNum skuPriceNum);

    void updateValueViewStatus(Long l, Long l2, SkuItemViewStatus skuItemViewStatus);
}
