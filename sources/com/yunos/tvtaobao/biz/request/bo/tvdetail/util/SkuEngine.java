package com.yunos.tvtaobao.biz.request.bo.tvdetail.util;

import android.text.TextUtils;
import com.yunos.tvtaobao.biz.request.bo.tvdetail.bean.TBDetailResultV6;
import com.yunos.tvtaobao.biz.request.bo.tvdetail.bean.taobao.SkuBaseBean;
import com.yunos.tvtaobao.biz.request.bo.tvdetail.bean.taobao.Unit;
import com.yunos.tvtaobao.biz.request.bo.tvdetail.bean.util.DetailV6Utils;
import com.yunos.tvtaobao.biz.request.bo.tvdetail.bean.util.MockData;
import com.yunos.tvtaobao.biz.request.bo.tvdetail.bean.util.SkuPriceNum;
import com.yunos.tvtaobao.biz.request.bo.tvdetail.resolve.TaoBaoDetailV6Resolve;
import com.zhiping.dev.android.logger.ZpLogger;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import mtopsdk.common.util.SymbolExpUtil;
import org.json.JSONException;
import org.json.JSONObject;

public class SkuEngine {
    private static final String TAG = "SkuEngine";
    private static int lastBuyCount = 0;
    private static String lastSkuId = null;
    private static boolean mIsReSotrePreSku;
    private boolean hasSku = false;
    private String mCurPrice;
    private OnSkuPropLayoutListener mOnSkuPropLayoutListener;
    private Map<Long, PropItem> mSelectedPropMap = new HashMap();
    private JSONObject mSku2info;
    private String mSubCurPrice;
    private LinkedHashMap<Long, List<PropItem>> propMap = new LinkedHashMap<>();
    private List<SkuItem> skuCollection = new ArrayList();
    private SkuPriceNum zeroSkuPrice;

    public interface OnSkuPropLayoutListener {
        void updateItemImage(PropItem propItem);

        void updateSkuKuCunAndPrice(SkuItem skuItem);

        void updateSkuProp(PropItem propItem);
    }

    public static class SkuItem {
        /* access modifiers changed from: private */
        public HashSet<String> propItems = new HashSet<>();
        public String skuId;
        public SkuPriceNum skuPriceNum;
    }

    public static void clearLastSelectSkuId() {
        lastSkuId = null;
        lastBuyCount = 0;
    }

    public static boolean isReSotrePreSku() {
        return mIsReSotrePreSku;
    }

    public static int getLastBuyCount() {
        return lastBuyCount;
    }

    public static void setLastBuyCount(int lastBuyCount2) {
        lastBuyCount = lastBuyCount2;
    }

    public SkuEngine(TBDetailResultV6 tbDetailResultV6) {
        if (tbDetailResultV6 != null) {
            Unit unit = TaoBaoDetailV6Resolve.getUnit(tbDetailResultV6);
            MockData mMockData = DetailV6Utils.getMockdata(tbDetailResultV6);
            if (tbDetailResultV6.getApiStack() != null) {
                String value = tbDetailResultV6.getApiStack().get(0).getValue();
                if (value != null) {
                    try {
                        this.mSku2info = new JSONObject(value).getJSONObject("skuCore").getJSONObject("sku2info");
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                } else {
                    this.mSku2info = null;
                }
            } else if (tbDetailResultV6.getMockData() != null) {
                try {
                    this.mSku2info = new JSONObject(tbDetailResultV6.getMockData()).getJSONObject("skuCore").getJSONObject("sku2info");
                } catch (JSONException e2) {
                    e2.printStackTrace();
                }
            } else {
                this.mSku2info = null;
            }
            final HashMap<String, Integer> mCheckOrder = new HashMap<>();
            List<SkuBaseBean.PropsBean> props = null;
            try {
                props = tbDetailResultV6.getSkuBase().getProps();
            } catch (Exception e3) {
                e3.printStackTrace();
            }
            for (SkuBaseBean.PropsBean beanX : props == null ? Collections.emptyList() : props) {
                if (!(beanX == null || beanX.getPid() == null || beanX.getValues() == null || beanX.getValues().size() == 0)) {
                    long pid = Long.valueOf(beanX.getPid()).longValue();
                    String name = beanX.getName();
                    List<SkuBaseBean.PropsBean.ValuesBean> values = beanX.getValues();
                    List<PropItem> items = new ArrayList<>();
                    for (int i = 0; i < values.size(); i++) {
                        SkuBaseBean.PropsBean.ValuesBean bean = values.get(i);
                        Long valueId = Long.valueOf(Long.parseLong(bean.getVid()));
                        PropItem propData = new PropItem();
                        propData.propId = pid;
                        propData.propName = name;
                        long unused = propData.valueId = valueId.longValue();
                        propData.valueName = bean.getName();
                        propData.imageUrl = bean.getImage();
                        if (!(propData.propId == 0 || propData.valueId == 0)) {
                            items.add(propData);
                            mCheckOrder.put(pid + SymbolExpUtil.SYMBOL_COLON + valueId, Integer.valueOf(i));
                        }
                    }
                    if (items.size() > 0) {
                        this.propMap.put(Long.valueOf(pid), items);
                    }
                }
            }
            SkuBaseBean skuBase = tbDetailResultV6.getSkuBase();
            List<SkuBaseBean.SkusBean> skuList = skuBase != null ? skuBase.getSkus() : null;
            int i2 = 0;
            while (skuList != null && i2 < skuList.size()) {
                String propPath = skuList.get(i2).getPropPath();
                String skuId = skuList.get(i2).getSkuId();
                SkuItem item = new SkuItem();
                item.skuId = skuId;
                item.skuPriceNum = resolvePriceNum(skuId);
                item.propItems.addAll(Arrays.asList(propPath == null ? new String[0] : propPath.split(SymbolExpUtil.SYMBOL_SEMICOLON)));
                if (!TextUtils.isEmpty(skuId) && item.propItems.size() != 0 && item.skuPriceNum != null && item.skuPriceNum.getQuantity() > 0) {
                    this.skuCollection.add(item);
                }
                i2++;
            }
            Collections.sort(this.skuCollection, new Comparator<SkuItem>() {
                public int compare(SkuItem lhs, SkuItem rhs) {
                    if (lhs == null || rhs == null) {
                        return 0;
                    }
                    int l = 0;
                    Iterator it = lhs.propItems.iterator();
                    while (it.hasNext()) {
                        Integer integer = (Integer) mCheckOrder.get((String) it.next());
                        l += integer == null ? 0 : integer.intValue();
                    }
                    int r = 0;
                    Iterator it2 = rhs.propItems.iterator();
                    while (it2.hasNext()) {
                        Integer integer2 = (Integer) mCheckOrder.get((String) it2.next());
                        r += integer2 == null ? 0 : integer2.intValue();
                    }
                    return l - r;
                }
            });
            SkuItem defaultSelect = null;
            mIsReSotrePreSku = false;
            if (lastSkuId != null) {
                Iterator<SkuItem> it = this.skuCollection.iterator();
                while (true) {
                    if (!it.hasNext()) {
                        break;
                    }
                    SkuItem item2 = it.next();
                    if (lastSkuId.equals(item2.skuId)) {
                        defaultSelect = item2;
                        mIsReSotrePreSku = true;
                        break;
                    }
                }
            }
            if (defaultSelect == null && this.skuCollection.size() > 0) {
                defaultSelect = this.skuCollection.get(0);
            }
            if (defaultSelect != null) {
                Iterator it2 = defaultSelect.propItems.iterator();
                while (it2.hasNext()) {
                    String[] sp = ((String) it2.next()).split(SymbolExpUtil.SYMBOL_COLON);
                    if (sp.length < 2) {
                        break;
                    }
                    PropItem item3 = getByPropIdAndValueId(Long.valueOf(sp[0]).longValue(), Long.valueOf(sp[1]).longValue());
                    if (item3 != null) {
                        item3.enable = true;
                        item3.selected = true;
                        this.mSelectedPropMap.put(Long.valueOf(item3.propId), item3);
                    }
                }
            }
            if (unit != null) {
                if (!(unit.getFeature() == null || unit.getFeature().getHasSku() == null || unit.getFeature().getHasSku().equals("") || !unit.getFeature().getHasSku().equals("true") || tbDetailResultV6.getSkuBase() == null || tbDetailResultV6.getSkuBase().getProps() == null || tbDetailResultV6.getSkuBase().getProps().size() <= 0)) {
                    this.hasSku = true;
                }
            } else if (!(mMockData == null || mMockData.getFeature() == null || !mMockData.getFeature().isHasSku())) {
                this.hasSku = true;
            }
            if (unit != null) {
                if (unit.getPrice() != null) {
                    if (unit.getPrice().getPrice() != null) {
                        this.mCurPrice = unit.getPrice().getPrice().getPriceText();
                    }
                    if (unit.getPrice().getSubPrice() != null) {
                        this.mSubCurPrice = unit.getPrice().getSubPrice().getPriceText();
                    }
                }
            } else if (!(mMockData == null || mMockData.getPrice().getPrice().getPriceText() == null)) {
                this.mCurPrice = mMockData.getPrice().getPrice().getPriceText();
            }
            ZpLogger.i(TAG, "skuEngine price " + this.mCurPrice);
            if (this.mCurPrice != null && this.mCurPrice.startsWith("¥")) {
                this.mCurPrice = this.mCurPrice.substring(1);
            }
            if (!TextUtils.isEmpty(this.mCurPrice) && this.mCurPrice.contains("-")) {
                this.mCurPrice = this.mCurPrice.substring(0, this.mCurPrice.indexOf("-"));
            }
        }
    }

    public boolean hasSku() {
        return this.hasSku;
    }

    public SkuItem getSkuId() {
        if (this.hasSku && this.mSelectedPropMap.size() == this.propMap.size()) {
            for (SkuItem entry : this.skuCollection) {
                HashSet<String> proSet = entry.propItems;
                Iterator<Map.Entry<Long, PropItem>> sPro = this.mSelectedPropMap.entrySet().iterator();
                boolean contain = true;
                while (true) {
                    if (sPro.hasNext()) {
                        PropItem sPropValue = sPro.next().getValue();
                        if (sPropValue != null && !proSet.contains(sPropValue.getKey())) {
                            contain = false;
                            continue;
                            break;
                        }
                    } else {
                        break;
                    }
                }
                if (contain) {
                    return entry;
                }
            }
        }
        return null;
    }

    public void setOnSkuPropLayoutListener(OnSkuPropLayoutListener l) {
        this.mOnSkuPropLayoutListener = l;
    }

    public SkuPriceNum getPrice() {
        if (this.mSelectedPropMap.size() == this.mSelectedPropMap.size() && this.mSelectedPropMap.size() > 0) {
            Iterator<SkuItem> it = this.skuCollection.iterator();
            while (true) {
                if (!it.hasNext()) {
                    break;
                }
                SkuItem entry = it.next();
                HashSet<String> proSet = entry.propItems;
                Iterator<Map.Entry<Long, PropItem>> sPro = this.mSelectedPropMap.entrySet().iterator();
                boolean contain = true;
                while (true) {
                    if (sPro.hasNext()) {
                        PropItem sPropValue = sPro.next().getValue();
                        if (sPropValue != null && !proSet.contains(sPropValue.getKey())) {
                            contain = false;
                            continue;
                            break;
                        }
                    } else {
                        break;
                    }
                }
                if (contain) {
                    if (entry.skuPriceNum != null) {
                        lastSkuId = entry.skuId;
                        return entry.skuPriceNum;
                    }
                }
            }
        }
        if (this.zeroSkuPrice == null) {
            this.zeroSkuPrice = resolvePriceNum("0");
        }
        if (this.zeroSkuPrice != null) {
            return this.zeroSkuPrice;
        }
        SkuPriceNum skuPriceNum = new SkuPriceNum();
        SkuPriceNum.PriceBean priceBean = new SkuPriceNum.PriceBean();
        SkuPriceNum.PriceBean subPriceBean = new SkuPriceNum.PriceBean();
        priceBean.setPriceText(this.mCurPrice);
        subPriceBean.setPriceText(this.mSubCurPrice);
        skuPriceNum.setPrice(priceBean);
        skuPriceNum.setSubPrice(subPriceBean);
        return skuPriceNum;
    }

    private PropItem getByPropIdAndValueId(long propId, long vid) {
        List<PropItem> item = this.propMap.get(Long.valueOf(propId));
        if (item == null) {
            return null;
        }
        for (PropItem bean : item) {
            if (bean.valueId == vid) {
                return bean;
            }
        }
        return null;
    }

    public LinkedHashMap<Long, List<PropItem>> getPropMap() {
        return this.propMap;
    }

    public void selectedPropAdd(PropItem item) {
        if (this.mSelectedPropMap.containsKey(Long.valueOf(item.propId))) {
            if (this.mSelectedPropMap.get(Long.valueOf(item.propId)) != item) {
                PropItem oldData = this.mSelectedPropMap.remove(Long.valueOf(item.propId));
                if (oldData != null) {
                    oldData.selected = false;
                }
            } else {
                return;
            }
        }
        item.selected = true;
        this.mSelectedPropMap.put(Long.valueOf(item.propId), item);
        updatePropValueStatus();
        if (this.mOnSkuPropLayoutListener != null) {
            this.mOnSkuPropLayoutListener.updateItemImage(item);
        }
    }

    public void selectedPropDelete(PropItem item) {
        if (item != null && this.mSelectedPropMap.containsKey(Long.valueOf(item.propId))) {
            this.mSelectedPropMap.get(Long.valueOf(item.propId)).selected = false;
            this.mSelectedPropMap.remove(Long.valueOf(item.propId));
            updatePropValueStatus();
        }
    }

    public boolean checkPropIdHasSelect(long propId) {
        return this.mSelectedPropMap.containsKey(Long.valueOf(propId));
    }

    public void updatePropValueStatus() {
        SkuItem skuId;
        HashSet<SkuItem> includeSet = new HashSet<>();
        for (Map.Entry<Long, List<PropItem>> it : this.propMap.entrySet()) {
            Long key = it.getKey();
            List<PropItem> value = it.getValue();
            includeSet.clear();
            for (SkuItem entry : this.skuCollection) {
                HashSet<String> propItems = entry.propItems;
                Iterator<Map.Entry<Long, PropItem>> selectIterator = this.mSelectedPropMap.entrySet().iterator();
                boolean contain = true;
                while (true) {
                    if (!selectIterator.hasNext()) {
                        break;
                    }
                    Map.Entry<Long, PropItem> propEntry = selectIterator.next();
                    long propId = propEntry.getKey().longValue();
                    PropItem propData = propEntry.getValue();
                    if (key.longValue() != propId && !propItems.contains(propData.getKey())) {
                        contain = false;
                        break;
                    }
                }
                if (contain) {
                    includeSet.add(entry);
                }
            }
            for (PropItem item : value) {
                boolean contain2 = false;
                Iterator<SkuItem> it2 = includeSet.iterator();
                while (true) {
                    if (it2.hasNext()) {
                        if (it2.next().propItems.contains(item.getKey())) {
                            contain2 = true;
                            break;
                        }
                    } else {
                        break;
                    }
                }
                item.enable = contain2;
            }
        }
        for (Map.Entry<Long, List<PropItem>> it3 : this.propMap.entrySet()) {
            for (PropItem item2 : it3.getValue()) {
                if (this.mOnSkuPropLayoutListener != null) {
                    this.mOnSkuPropLayoutListener.updateSkuProp(item2);
                }
            }
        }
        if (this.hasSku && (skuId = getSkuId()) != null && this.mOnSkuPropLayoutListener != null) {
            this.mOnSkuPropLayoutListener.updateSkuKuCunAndPrice(skuId);
        }
    }

    /* JADX DEBUG: Multi-variable search result rejected for TypeSearchVarInfo{r4v9, resolved type: java.lang.Object} */
    /* JADX DEBUG: Multi-variable search result rejected for TypeSearchVarInfo{r0v0, resolved type: com.yunos.tvtaobao.biz.request.bo.tvdetail.bean.util.SkuPriceNum} */
    /* JADX WARNING: Multi-variable type inference failed */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    private com.yunos.tvtaobao.biz.request.bo.tvdetail.bean.util.SkuPriceNum resolvePriceNum(java.lang.String r8) {
        /*
            r7 = this;
            r3 = 0
            org.json.JSONObject r4 = r7.mSku2info
            if (r4 == 0) goto L_0x0030
            org.json.JSONObject r4 = r7.mSku2info
            boolean r4 = r4.has(r8)
            if (r4 == 0) goto L_0x0030
            org.json.JSONObject r4 = r7.mSku2info     // Catch:{ Exception -> 0x0022 }
            java.lang.String r2 = r4.optString(r8)     // Catch:{ Exception -> 0x0022 }
            com.yunos.tvtaobao.biz.request.bo.tvdetail.util.SkuEngine$2 r4 = new com.yunos.tvtaobao.biz.request.bo.tvdetail.util.SkuEngine$2     // Catch:{ Exception -> 0x0022 }
            r4.<init>()     // Catch:{ Exception -> 0x0022 }
            java.lang.Object r4 = com.yunos.tv.core.util.GsonUtil.parseJson(r2, r4)     // Catch:{ Exception -> 0x0022 }
            r0 = r4
            com.yunos.tvtaobao.biz.request.bo.tvdetail.bean.util.SkuPriceNum r0 = (com.yunos.tvtaobao.biz.request.bo.tvdetail.bean.util.SkuPriceNum) r0     // Catch:{ Exception -> 0x0022 }
            r3 = r0
        L_0x0020:
            r4 = r3
        L_0x0021:
            return r4
        L_0x0022:
            r1 = move-exception
            r1.printStackTrace()
            java.lang.String r4 = "SkuEngine"
            java.lang.String r5 = "resolvePriceNum error jsonFate "
            com.zhiping.dev.android.logger.ZpLogger.e(r4, r5)
            goto L_0x0020
        L_0x0030:
            java.lang.String r4 = "SkuEngine"
            java.lang.StringBuilder r5 = new java.lang.StringBuilder
            r5.<init>()
            java.lang.String r6 = "resolvePriceNum error no mSku2info or mSku2info has not SkuId :"
            java.lang.StringBuilder r5 = r5.append(r6)
            java.lang.StringBuilder r5 = r5.append(r8)
            java.lang.String r5 = r5.toString()
            com.zhiping.dev.android.logger.ZpLogger.e(r4, r5)
            r4 = 0
            goto L_0x0021
        */
        throw new UnsupportedOperationException("Method not decompiled: com.yunos.tvtaobao.biz.request.bo.tvdetail.util.SkuEngine.resolvePriceNum(java.lang.String):com.yunos.tvtaobao.biz.request.bo.tvdetail.bean.util.SkuPriceNum");
    }

    public static class PropItem {
        public boolean enable;
        public String imageUrl;
        public long propId;
        public String propName;
        public boolean selected;
        /* access modifiers changed from: private */
        public long valueId;
        public String valueName;

        public String getKey() {
            return this.propId + SymbolExpUtil.SYMBOL_COLON + this.valueId;
        }

        public String toString() {
            return "PropItem{propId=" + this.propId + ", propName='" + this.propName + '\'' + ", valueId=" + this.valueId + ", valueName='" + this.valueName + '\'' + ", imageUrl='" + this.imageUrl + '\'' + ", selected=" + this.selected + ", enable=" + this.enable + '}';
        }
    }
}
