package com.taobao.wireless.detail.model;

import com.taobao.detail.clientDomain.TBDetailResultVO;
import com.taobao.detail.domain.DetailVO;
import com.taobao.wireless.detail.model.helper.DetailVOHelper;
import com.taobao.wireless.detail.model.vo.BaseControl;
import com.taobao.wireless.detail.model.vo.SkuInfoVO;
import com.taobao.wireless.detail.model.vo.sku.PropItemVO;
import com.taobao.wireless.detail.model.vo.sku.PropValuesVO;
import com.taobao.wireless.lang.CheckUtils;
import com.taobao.wireless.lang.PpathUtils;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import mtopsdk.common.util.SymbolExpUtil;

public class BaseSkuModel {
    private Set<String> descartesSetCache;
    public String propTexts;
    public String skuId;
    private List<PropItemVO> skuPropCache;
    protected TBDetailResultVO tbDetailResultVO;

    public BaseSkuModel(TBDetailResultVO tbDetailResultVO2) {
        this.tbDetailResultVO = tbDetailResultVO2;
    }

    public void update(TBDetailResultVO tbDetailResultVO2) {
        this.tbDetailResultVO = tbDetailResultVO2;
        this.skuPropCache = null;
        this.descartesSetCache = null;
        this.skuId = null;
        this.propTexts = null;
    }

    public boolean isSkuSelected() {
        return !CheckUtils.isEmpty(this.skuId) || !DetailVOHelper.isSkuItem(this.tbDetailResultVO);
    }

    public List<PropItemVO> getSkuProps() {
        if (this.skuPropCache != null) {
            return this.skuPropCache;
        }
        if (this.tbDetailResultVO == null || this.tbDetailResultVO.skuModel == null || this.tbDetailResultVO.skuModel.skuProps == null || this.tbDetailResultVO.skuModel.ppathIdmap == null) {
            return null;
        }
        String propIds = "";
        if (!CheckUtils.isEmpty(this.skuId)) {
            Iterator i$ = this.tbDetailResultVO.skuModel.ppathIdmap.keySet().iterator();
            while (true) {
                if (!i$.hasNext()) {
                    break;
                }
                String key = i$.next();
                if (this.skuId.equals(this.tbDetailResultVO.skuModel.ppathIdmap.get(key))) {
                    propIds = key;
                    break;
                }
            }
        }
        TBDetailResultVO.SkuModel skuModel = this.tbDetailResultVO.skuModel;
        List<DetailVO.StaticItem.SaleInfo.SkuProp> skuProps = skuModel.skuProps;
        DetailVO.StaticItem.SaleInfo.CascadeInfo cascadeInfo = skuModel.cascadeInfo;
        List<PropItemVO> propItemVOs = new ArrayList<>(skuProps.size());
        for (DetailVO.StaticItem.SaleInfo.SkuProp skuProp : skuProps) {
            PropItemVO propItemVO = new PropItemVO();
            propItemVO.propId = String.valueOf(skuProp.propId);
            propItemVO.propName = skuProp.propName;
            if (cascadeInfo == null || !cascadeInfo.rootPropIds.contains(skuProp.propId)) {
                propItemVO.values = new ArrayList();
                for (DetailVO.StaticItem.SaleInfo.SkuProp.SkuPropValue skuPropValue : skuProp.values) {
                    PropValuesVO propValuesVO = new PropValuesVO();
                    propValuesVO.name = skuPropValue.name;
                    propValuesVO.imgUrl = skuPropValue.imgUrl;
                    propValuesVO.propValue = propItemVO.propId + SymbolExpUtil.SYMBOL_COLON + skuPropValue.valueId;
                    if (propIds.contains(propValuesVO.propValue)) {
                        propValuesVO.checked = true;
                    }
                    propItemVO.values.add(propValuesVO);
                }
            } else {
                propItemVO.subValues = new ArrayList();
                for (Long rootIdLong : cascadeInfo.rootPropIds) {
                    for (DetailVO.StaticItem.SaleInfo.CascadeInfo.SKUCascadeVO skuCascadeVO : cascadeInfo.skuCascadeMap.get(String.valueOf(rootIdLong))) {
                        propItemVO.subValues.add(convertCascade(cascadeInfo.skuCascadeMap, skuCascadeVO));
                    }
                }
                String rootId = String.valueOf(cascadeInfo.rootPropIds.iterator().next());
                propItemVO.subTitles = new ArrayList();
                while (cascadeInfo.skuCascadeMap.get(rootId) != null) {
                    DetailVO.StaticItem.SaleInfo.CascadeInfo.SKUCascadeVO skuCascadeVO2 = (DetailVO.StaticItem.SaleInfo.CascadeInfo.SKUCascadeVO) cascadeInfo.skuCascadeMap.get(rootId).get(0);
                    propItemVO.subTitles.add(skuCascadeVO2.propertyText);
                    rootId = skuCascadeVO2.propertyValueId;
                }
            }
            propItemVOs.add(propItemVO);
        }
        if (cascadeInfo == null && propItemVOs.size() == 1) {
            PropItemVO first = propItemVOs.get(0);
            List<PropValuesVO> newValues = new ArrayList<>();
            for (PropValuesVO propValuesVO2 : first.values) {
                if (isSkuEnable(propValuesVO2.propValue)) {
                    newValues.add(propValuesVO2);
                }
            }
            if (!newValues.isEmpty()) {
                first.values = newValues;
            }
        }
        this.skuPropCache = propItemVOs;
        return propItemVOs;
    }

    private PropItemVO convertCascade(Map<String, List<DetailVO.StaticItem.SaleInfo.CascadeInfo.SKUCascadeVO>> map, DetailVO.StaticItem.SaleInfo.CascadeInfo.SKUCascadeVO skuCascadeVO) {
        PropItemVO propItemVO = new PropItemVO();
        propItemVO.propValue = skuCascadeVO.propertyValueId;
        propItemVO.propName = skuCascadeVO.actualValueText;
        List<DetailVO.StaticItem.SaleInfo.CascadeInfo.SKUCascadeVO> skuCascadeVOs = map.get(skuCascadeVO.propertyValueId);
        if (skuCascadeVOs != null) {
            propItemVO.subValues = new ArrayList();
            for (DetailVO.StaticItem.SaleInfo.CascadeInfo.SKUCascadeVO subSku : skuCascadeVOs) {
                propItemVO.subValues.add(convertCascade(map, subSku));
            }
        }
        return propItemVO;
    }

    public SkuInfoVO getSkuInfo() {
        SkuInfoVO skuInfoVO = new SkuInfoVO();
        BaseControl baseControl = new BaseControl();
        DetailVOHelper.calControl(baseControl, this.tbDetailResultVO, this.skuId);
        skuInfoVO.quantity = String.valueOf(baseControl.quantity);
        skuInfoVO.price = baseControl.price;
        return skuInfoVO;
    }

    public String getSelectedSku(List<String> valueids) {
        if (this.tbDetailResultVO.skuModel == null || this.tbDetailResultVO.skuModel.ppathIdmap == null || this.tbDetailResultVO.skuModel.ppathIdmap.size() == 0 || valueids == null || valueids.size() == 0) {
            setSkuId((String) null);
            return "";
        }
        String calSkuId = this.tbDetailResultVO.skuModel.ppathIdmap.get(PpathUtils.sortSkuPropValuesAsc(valueids));
        setSkuId(calSkuId);
        return calSkuId;
    }

    public void setSkuId(String calSkuId) {
        if (calSkuId == null || !calSkuId.equals(this.skuId)) {
            this.propTexts = null;
        }
        this.skuId = calSkuId;
    }

    public void setSelectedPropTexts(String propTexts2) {
        this.propTexts = propTexts2;
    }

    public boolean isSkuEnable(List<String> selectedIds, String checkId) {
        initDescartes();
        if (this.descartesSetCache == null) {
            return false;
        }
        List<String> ids = new ArrayList<>();
        String propId = PpathUtils.extractPropStrId(checkId);
        ids.add(checkId);
        for (String selId : selectedIds) {
            if (!propId.equals(PpathUtils.extractPropStrId(selId))) {
                ids.add(selId);
            }
        }
        return this.descartesSetCache.contains(PpathUtils.sortSkuPropValuesAsc(ids));
    }

    public boolean isSkuEnable(String props) {
        initDescartes();
        if (this.descartesSetCache == null) {
            return false;
        }
        return this.descartesSetCache.contains(props);
    }

    private void initDescartes() {
        if (this.descartesSetCache == null || this.descartesSetCache.isEmpty()) {
            Set<String> availablePPath = new HashSet<>();
            if (this.tbDetailResultVO.skuModel != null && this.tbDetailResultVO.skuModel.skus != null && this.tbDetailResultVO.skuModel.ppathIdmap != null) {
                Map<String, DetailVO.DynamicItem.SkuPriceAndQuanitiy> skus = this.tbDetailResultVO.skuModel.skus;
                for (String key : this.tbDetailResultVO.skuModel.ppathIdmap.keySet()) {
                    if (hasQuantity(skus, this.tbDetailResultVO.skuModel.ppathIdmap.get(key))) {
                        availablePPath.add(key);
                    }
                }
                this.descartesSetCache = DetailVOHelper.descartes(availablePPath);
            }
        }
    }

    private boolean hasQuantity(Map<String, DetailVO.DynamicItem.SkuPriceAndQuanitiy> skus, String skuId2) {
        DetailVO.DynamicItem.SkuPriceAndQuanitiy skuPriceAndQuanitiy = skus.get(skuId2);
        if (skuPriceAndQuanitiy == null || skuPriceAndQuanitiy.quantity == null || skuPriceAndQuanitiy.quantity.intValue() <= 0) {
            return false;
        }
        return true;
    }
}
