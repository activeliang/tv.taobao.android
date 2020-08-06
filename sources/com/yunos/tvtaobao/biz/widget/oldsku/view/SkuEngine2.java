package com.yunos.tvtaobao.biz.widget.oldsku.view;

import android.text.TextUtils;
import com.alibaba.fastjson.JSON;
import com.yunos.tv.core.RtEnv;
import com.yunos.tv.core.config.Config;
import com.yunos.tvtaobao.biz.request.bo.SkuPriceNum;
import com.yunos.tvtaobao.biz.request.bo.TBDetailResultV6;
import com.yunos.tvtaobao.biz.widget.newsku.SkuItemViewStatus;
import com.zhiping.dev.android.logger.ZpLogger;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import mtopsdk.common.util.SymbolExpUtil;
import org.json.JSONException;
import org.json.JSONObject;

public class SkuEngine2 {
    private String TAG = "SkuEngine";
    private Iterator<Map.Entry<String, String>> iterator;
    private ArrayList<PropData> mAllPropList;
    private JSONObject mMockData;
    private OnSkuPropLayoutListener mOnSkuPropLayoutListener;
    private Map<String, String> mPPathIdmap;
    private Map<Long, PropData> mSelectedPropMap;
    private JSONObject mjsonObject;
    private TBDetailResultV6 tbDetailResultV6;
    private JSONObject value;

    public interface OnSkuPropLayoutListener {
        void updateItemImage(PropData propData);

        void updateSkuKuCunAndrPrice(String str);

        void updateSkuProp(long j, long j2, SkuItemViewStatus skuItemViewStatus);
    }

    public SkuEngine2(TBDetailResultV6 tbDetailResultV62) {
        this.tbDetailResultV6 = tbDetailResultV62;
        this.mAllPropList = new ArrayList<>();
        this.mSelectedPropMap = new HashMap();
        this.mPPathIdmap = new HashMap();
        initValidSkuMap();
    }

    private void initValidSkuMap() {
        List<TBDetailResultV6.SkuBaseBean.SkusBean> skus = this.tbDetailResultV6.getSkuBase().getSkus();
        if (this.tbDetailResultV6 == null || this.tbDetailResultV6.getSkuBase() == null || skus == null || this.tbDetailResultV6.getSkuBase().getProps() == null || this.mPPathIdmap == null) {
            ZpLogger.v(this.TAG, this.TAG + ".initValidSkuMap data null");
            return;
        }
        for (int i = 0; i < skus.size(); i++) {
            this.mPPathIdmap.put(skus.get(i).getPropPath(), skus.get(i).getSkuId());
        }
        if (Config.isDebug()) {
            ZpLogger.v(this.TAG, this.TAG + ".initValidSkuMap first mPPathIdmap.size = " + this.mPPathIdmap.size());
        }
        this.iterator = this.mPPathIdmap.entrySet().iterator();
        while (this.iterator.hasNext()) {
            Map.Entry<String, String> next = this.iterator.next();
            String key = next.getKey();
            String skuid = next.getValue();
            ZpLogger.e(this.TAG, key + "----" + skuid);
            if (!TextUtils.isEmpty(key)) {
                if (!TextUtils.isEmpty(skuid)) {
                    removeInVaildSkuId(skuid, key);
                } else {
                    this.iterator.remove();
                }
            }
        }
        List<TBDetailResultV6.SkuBaseBean.PropsBeanX> props1 = this.tbDetailResultV6.getSkuBase().getProps();
        for (int i2 = 0; i2 < props1.size(); i2++) {
            TBDetailResultV6.SkuBaseBean.PropsBeanX prop = props1.get(i2);
            if (!(prop == null || prop.getPid() == null || prop.getValues() == null || prop.getValues().size() == 0)) {
                for (int j = 0; j < prop.getValues().size(); j++) {
                    TBDetailResultV6.SkuBaseBean.PropsBeanX.ValuesBean valuesBean = prop.getValues().get(j);
                    PropData propData = new PropData();
                    propData.propKey = getPropKey(Long.parseLong(prop.getPid()), Long.parseLong(valuesBean.getVid()));
                    propData.propId = Long.parseLong(prop.getPid());
                    propData.valueId = Long.parseLong(valuesBean.getVid());
                    if (valuesBean.getImage() != null) {
                        propData.imageUrl = valuesBean.getImage();
                    }
                    addPropData(propData);
                }
            }
        }
        if (Config.isDebug()) {
            ZpLogger.v(this.TAG, this.TAG + ".initValidSkuMap second mPPathIdmap.size = " + this.mPPathIdmap.size());
        }
    }

    public Map<String, String> getPPathIdmap() {
        return this.mPPathIdmap;
    }

    private void removeInVaildSkuId(String skuid, String key) {
        if (this.tbDetailResultV6.getApiStack() != null) {
            if (this.value == null) {
                try {
                    this.value = new JSONObject(this.tbDetailResultV6.getApiStack().get(0).getValue());
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
            try {
                if (this.value != null) {
                    JSONObject sku2info = this.value.getJSONObject("skuCore").getJSONObject("sku2info");
                    if (sku2info.has(skuid)) {
                        SkuPriceNum skuPriceNum = (SkuPriceNum) JSON.parseObject(sku2info.getJSONObject(skuid).toString(), SkuPriceNum.class);
                        if (skuPriceNum == null || skuPriceNum.getQuantity() <= 0) {
                            this.iterator.remove();
                            if (Config.isDebug()) {
                                ZpLogger.v(this.TAG, "apistack" + this.TAG + ".initValidSkuMap skuPriceAndQuanity == 0 key = " + key + ".skuId = " + skuid);
                                return;
                            }
                            return;
                        }
                        return;
                    }
                    ZpLogger.v(this.TAG, this.TAG + ".removeInVaildSkuId skuId = " + skuid);
                    this.iterator.remove();
                }
            } catch (JSONException e2) {
                e2.printStackTrace();
            }
        } else if (this.tbDetailResultV6.getMockData() != null) {
            String mockData = this.tbDetailResultV6.getMockData();
            try {
                if (this.mjsonObject == null) {
                    this.mjsonObject = new JSONObject(mockData);
                }
                SkuPriceNum skuPriceNum2 = (SkuPriceNum) JSON.parseObject(this.mjsonObject.getJSONObject("skuCore").getJSONObject("sku2info").getJSONObject(skuid).toString(), SkuPriceNum.class);
                if (skuPriceNum2 == null || skuPriceNum2.getQuantity() <= 0) {
                    this.iterator.remove();
                    if (Config.isDebug()) {
                        ZpLogger.v(this.TAG, "mocadata" + this.TAG + ".initValidSkuMap skuPriceAndQuanity == 0 key = " + key + ".skuId = " + skuid);
                    }
                }
            } catch (JSONException e3) {
                e3.printStackTrace();
            }
        }
    }

    public ArrayList<PropData> getAllPropList() {
        return this.mAllPropList;
    }

    private void addPropData(PropData propData) {
        if (propData != null) {
            if (this.mAllPropList == null) {
                this.mAllPropList = new ArrayList<>();
            }
            if (TextUtils.isEmpty(propData.propKey)) {
                propData.propKey = getPropKey(propData.propId, propData.valueId);
            }
            propData.enable = true;
            propData.selected = false;
            this.mAllPropList.add(propData);
        }
    }

    public String getPropKey(long propId, long valueId) {
        return propId + SymbolExpUtil.SYMBOL_COLON + valueId;
    }

    public PropData getPropDataFromList(long propId, long valueId) {
        if (this.mAllPropList == null) {
            return null;
        }
        for (int i = 0; i < this.mAllPropList.size(); i++) {
            PropData propData = this.mAllPropList.get(i);
            if (propData.propId == propId && propData.valueId == valueId) {
                return propData;
            }
        }
        return null;
    }

    public PropData getPropDataFromList(String propKey) {
        if (this.mAllPropList == null) {
            return null;
        }
        for (int i = 0; i < this.mAllPropList.size(); i++) {
            PropData propData = this.mAllPropList.get(i);
            if (propData.propKey.equals(propKey)) {
                return propData;
            }
        }
        return null;
    }

    public void deletePropData(PropData propData) {
        if (propData != null && this.mAllPropList != null) {
            this.mAllPropList.remove(propData);
        }
    }

    public void clearPropDataList() {
        if (this.mAllPropList != null) {
            this.mAllPropList.clear();
            this.mAllPropList = null;
        }
    }

    public Map<Long, PropData> getSelectedPropDataMap() {
        return this.mSelectedPropMap;
    }

    public void addSelectedPropData(long propId, long valueId) {
        if (this.mSelectedPropMap == null) {
            this.mSelectedPropMap = new HashMap();
        }
        PropData propData = getPropDataFromList(propId, valueId);
        if (Config.isDebug()) {
            ZpLogger.v(this.TAG, this.TAG + ".addSelectedPropData.propId = " + propId + ".valueId = " + valueId + ".propData = " + propData);
        }
        if (propData != null) {
            if (this.mSelectedPropMap.containsKey(Long.valueOf(propId))) {
                if (this.mSelectedPropMap.get(Long.valueOf(propId)) != propData) {
                    PropData oldData = this.mSelectedPropMap.remove(Long.valueOf(propId));
                    if (oldData != null) {
                        oldData.selected = false;
                    }
                } else {
                    return;
                }
            }
            propData.selected = true;
            this.mSelectedPropMap.put(Long.valueOf(propId), propData);
            updateItemImage(propData);
            updatePropValueStatus();
        }
        if (Config.isDebug()) {
            ZpLogger.v(this.TAG, this.TAG + ".addSelectedPropData.mSelectedPropMap = " + this.mSelectedPropMap + ".size = " + this.mSelectedPropMap.size());
        }
    }

    public void deleteSelectedPropData(long propId) {
        if (this.mSelectedPropMap != null) {
            this.mSelectedPropMap.get(Long.valueOf(propId)).selected = false;
            this.mSelectedPropMap.remove(Long.valueOf(propId));
            updatePropValueStatus();
        }
    }

    public void cleanSelectpropDataMap() {
        if (this.mSelectedPropMap != null) {
            this.mSelectedPropMap.clear();
            this.mSelectedPropMap = null;
        }
    }

    private void updateItemImage(PropData propData) {
        if (this.mOnSkuPropLayoutListener != null) {
            this.mOnSkuPropLayoutListener.updateItemImage(propData);
        }
    }

    public void updatePropValueStatus() {
        SkuItemViewStatus status;
        if (this.tbDetailResultV6 != null && this.tbDetailResultV6.getSkuBase() != null && this.mPPathIdmap != null && this.tbDetailResultV6.getSkuBase().getProps() != null && this.mSelectedPropMap != null) {
            HashMap hashMap = new HashMap();
            Long curPropId = null;
            for (int i = 0; i < this.mAllPropList.size(); i++) {
                PropData data = this.mAllPropList.get(i);
                boolean propEnable = false;
                boolean useOldBranch = true;
                if (RtEnv.get("test_useNewBranch") != null) {
                    useOldBranch = false;
                }
                if (useOldBranch) {
                    if (curPropId == null || curPropId.longValue() != data.propId) {
                        curPropId = Long.valueOf(data.propId);
                        hashMap.clear();
                        for (Map.Entry<String, String> entry : this.mPPathIdmap.entrySet()) {
                            String key = entry.getKey();
                            String value2 = entry.getValue();
                            Iterator<Map.Entry<Long, PropData>> selectPropIter = this.mSelectedPropMap.entrySet().iterator();
                            boolean contain = true;
                            while (true) {
                                if (!selectPropIter.hasNext()) {
                                    break;
                                }
                                Map.Entry<Long, PropData> propEntry = selectPropIter.next();
                                PropData propData = propEntry.getValue();
                                if (data.propId != propEntry.getKey().longValue() && !key.contains(propData.propKey)) {
                                    contain = false;
                                    break;
                                }
                            }
                            if (contain) {
                                hashMap.put(key, value2);
                            }
                        }
                    }
                    if (Config.isDebug()) {
                        ZpLogger.v(this.TAG, this.TAG + ".updatePropValueStatus.theUnionPropIncludeMap = " + hashMap);
                    }
                    propEnable = false;
                    Iterator<Map.Entry<String, String>> unionIter = hashMap.entrySet().iterator();
                    while (true) {
                        if (unionIter.hasNext()) {
                            if (unionIter.next().getKey().contains(data.propKey)) {
                                propEnable = true;
                                break;
                            }
                        } else {
                            break;
                        }
                    }
                } else {
                    if (hashMap.isEmpty()) {
                        for (Map.Entry<String, String> skuPath : this.mPPathIdmap.entrySet()) {
                            boolean isSkuPathContainsAllSelectedProp = true;
                            for (Map.Entry<Long, PropData> selectedProp : this.mSelectedPropMap.entrySet()) {
                                if (!skuPath.getKey().contains("" + selectedProp.getValue().propId)) {
                                    isSkuPathContainsAllSelectedProp = false;
                                }
                            }
                            if (isSkuPathContainsAllSelectedProp) {
                                hashMap.put(skuPath.getKey(), skuPath.getValue());
                            }
                        }
                    }
                    Iterator<Map.Entry<String, String>> unionIter2 = hashMap.entrySet().iterator();
                    while (true) {
                        if (!unionIter2.hasNext()) {
                            break;
                        } else if (unionIter2.next().getKey().contains(data.propKey)) {
                            propEnable = true;
                            SkuPriceNum skuPriceNum = resolvePriceNum(getSkuIdById(data.propKey));
                            if (skuPriceNum != null && skuPriceNum.getQuantity() == 0) {
                                propEnable = false;
                            }
                        }
                    }
                }
                if (propEnable) {
                    data.enable = true;
                } else {
                    data.enable = false;
                }
            }
            for (int i2 = 0; i2 < this.mAllPropList.size(); i2++) {
                PropData data2 = this.mAllPropList.get(i2);
                if (!data2.enable) {
                    status = SkuItemViewStatus.DISABLE;
                } else if (data2.selected) {
                    status = SkuItemViewStatus.SELECTED;
                } else {
                    status = SkuItemViewStatus.ENABLE;
                }
                if (this.mOnSkuPropLayoutListener != null) {
                    this.mOnSkuPropLayoutListener.updateSkuProp(data2.propId, data2.valueId, status);
                }
            }
            int mapSize = this.mSelectedPropMap.size();
            int propSize = this.tbDetailResultV6.getSkuBase().getProps().size();
            ZpLogger.v(this.TAG, this.TAG + ".updatePropValueStatus.mSelectedPropMap.size = " + mapSize + ".skuProps.size = " + propSize);
            if (mapSize == propSize) {
                String skuId = getSkuId();
                ZpLogger.v(this.TAG, this.TAG + ".updatePropValueStatus.skuId = " + skuId);
                if (!TextUtils.isEmpty(skuId) && this.mOnSkuPropLayoutListener != null) {
                    this.mOnSkuPropLayoutListener.updateSkuKuCunAndrPrice(skuId);
                }
            }
        }
    }

    public String getSkuId() {
        if (this.tbDetailResultV6 == null || this.tbDetailResultV6.getSkuBase() == null || this.tbDetailResultV6.getSkuBase().getProps() == null || this.mPPathIdmap == null) {
            return null;
        }
        if (Config.isDebug()) {
            ZpLogger.v(this.TAG, this.TAG + ".getSkuId.mSelectedPropMap = " + this.mSelectedPropMap);
        }
        if (this.mSelectedPropMap.size() == this.tbDetailResultV6.getSkuBase().getProps().size()) {
            for (Map.Entry<String, String> entry : this.mPPathIdmap.entrySet()) {
                String key = entry.getKey();
                String value2 = entry.getValue();
                Iterator<Map.Entry<Long, PropData>> sPropIter = this.mSelectedPropMap.entrySet().iterator();
                boolean contain = true;
                while (true) {
                    if (!sPropIter.hasNext()) {
                        break;
                    }
                    PropData sPropValue = sPropIter.next().getValue();
                    if (sPropValue != null) {
                        if (TextUtils.isEmpty(sPropValue.propKey)) {
                            sPropValue.propKey = getPropKey(sPropValue.propId, sPropValue.valueId);
                        }
                        if (Config.isDebug()) {
                            ZpLogger.v(this.TAG, this.TAG + ".getSkuId.propKey = " + sPropValue.propKey + ".key = " + key + ", value = " + value2);
                        }
                        if (!key.contains(sPropValue.propKey)) {
                            contain = false;
                            continue;
                            break;
                        }
                    }
                }
                if (contain) {
                    return value2;
                }
            }
        }
        return null;
    }

    public String getSkuIdById(String propKey) {
        if (this.mPPathIdmap == null) {
            return null;
        }
        return this.mPPathIdmap.get(propKey);
    }

    private SkuPriceNum resolvePriceNum(String skuid) {
        if (!(this.tbDetailResultV6 == null || this.tbDetailResultV6.getApiStack() == null || this.tbDetailResultV6.getApiStack().size() <= 0)) {
            try {
                String value2 = this.tbDetailResultV6.getApiStack().get(0).getValue();
                if (value2 != null) {
                    JSONObject sku2info = new JSONObject(value2).getJSONObject("skuCore").getJSONObject("sku2info");
                    if (sku2info.has(skuid)) {
                        return (SkuPriceNum) JSON.parseObject(sku2info.getJSONObject(skuid).toString(), SkuPriceNum.class);
                    }
                    return null;
                }
            } catch (NullPointerException e) {
                e.printStackTrace();
            } catch (JSONException e2) {
                e2.printStackTrace();
            }
        }
        return null;
    }

    public Map<String, String> getDefaultPropFromSkuId(String skuId) {
        String[] propArray;
        String[] pair;
        SkuPriceNum skuPriceNum;
        Map<String, String> propMap = null;
        if (!TextUtils.isEmpty(skuId) && this.tbDetailResultV6 != null && this.tbDetailResultV6.getSkuBase() != null && ((this.tbDetailResultV6.getSkuBase().getProps() == null || ((skuPriceNum = resolvePriceNum(skuId)) != null && skuPriceNum.getQuantity() > 0)) && this.mPPathIdmap != null)) {
            String propPair = null;
            Iterator<Map.Entry<String, String>> iter = this.mPPathIdmap.entrySet().iterator();
            while (true) {
                if (!iter.hasNext()) {
                    break;
                }
                Map.Entry<String, String> entry = iter.next();
                String key = entry.getKey();
                if (entry.getValue().equals(skuId)) {
                    propPair = key;
                    break;
                }
            }
            if (!TextUtils.isEmpty(propPair) && (propArray = propPair.split(SymbolExpUtil.SYMBOL_SEMICOLON)) != null) {
                propMap = new HashMap<>();
                for (int i = 0; i < propArray.length; i++) {
                    if (!TextUtils.isEmpty(propArray[i]) && (pair = propArray[i].split(SymbolExpUtil.SYMBOL_COLON)) != null && pair.length == 2) {
                        propMap.put(pair[0], pair[1]);
                    }
                }
            }
        }
        return propMap;
    }

    public String getDefaultValueIdFromPropId(String propId, Map<String, String> defaultPropMap) {
        if (defaultPropMap == null || defaultPropMap.size() == 0 || TextUtils.isEmpty(propId)) {
            return null;
        }
        for (Map.Entry<String, String> entry : defaultPropMap.entrySet()) {
            if (entry != null && propId.equals(entry.getKey())) {
                return entry.getValue();
            }
        }
        return null;
    }

    public TBDetailResultV6.SkuBaseBean.PropsBeanX getUnSelectProp() {
        if (this.mSelectedPropMap == null || this.tbDetailResultV6 == null || this.tbDetailResultV6.getSkuBase() == null || this.tbDetailResultV6.getSkuBase().getProps() == null) {
            return null;
        }
        int size = this.tbDetailResultV6.getSkuBase().getProps().size();
        if (this.mSelectedPropMap.size() != size) {
            for (int i = 0; i < size; i++) {
                TBDetailResultV6.SkuBaseBean.PropsBeanX propsBeanX = this.tbDetailResultV6.getSkuBase().getProps().get(i);
                long propId = Long.parseLong(propsBeanX.getPid());
                boolean exist = false;
                Iterator<Map.Entry<Long, PropData>> iter = this.mSelectedPropMap.entrySet().iterator();
                while (true) {
                    if (iter.hasNext()) {
                        if (propId == iter.next().getKey().longValue()) {
                            exist = true;
                            break;
                        }
                    } else {
                        break;
                    }
                }
                if (!exist) {
                    return propsBeanX;
                }
            }
        }
        return null;
    }

    public void setOnSkuPropLayoutListener(OnSkuPropLayoutListener l) {
        this.mOnSkuPropLayoutListener = l;
    }

    public static class PropData {
        public boolean enable;
        public String imageUrl;
        public long propId;
        public String propKey;
        public boolean selected;
        public long valueId;

        public String toString() {
            return "{ propKey = " + this.propKey + ", propId = " + this.propId + ", valueId = " + this.valueId + ", selected = " + this.selected + ", enable = " + this.enable + ", imageUrl = " + this.imageUrl + "}";
        }
    }
}
