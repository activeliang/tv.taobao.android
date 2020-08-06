package com.taobao.wireless.trade.mbuy.sdk.co.basic;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.taobao.wireless.trade.mbuy.sdk.co.Component;
import com.taobao.wireless.trade.mbuy.sdk.engine.BuyEngine;
import com.taobao.wireless.trade.mbuy.sdk.engine.BuyEngineContext;
import com.taobao.wireless.trade.mbuy.sdk.engine.LinkageType;
import com.taobao.wireless.trade.mbuy.sdk.engine.RollbackProtocol;
import com.taobao.wireless.trade.mbuy.sdk.engine.ValidateResult;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class MultiSelectComponent extends Component {
    private List<MultiSelectGroup> groups = loadGroups(this.fields.getJSONArray("groups"));

    public MultiSelectComponent(JSONObject data, BuyEngine engine) {
        super(data, engine);
    }

    public String getTitle() {
        return this.fields.getString("title");
    }

    public List<MultiSelectGroup> getGroups() {
        return this.groups;
    }

    public List<String> getSelectedIds() {
        List<?> selectedIds = this.fields.getJSONArray("selectedIds");
        if (selectedIds == null) {
            return null;
        }
        List<String> selectedIdsCopy = new ArrayList<>(selectedIds.size());
        for (Object selectedId : selectedIds) {
            selectedIdsCopy.add(selectedId);
        }
        return selectedIdsCopy;
    }

    public SelectOption getOptionById(String optionId) {
        if (optionId == null || optionId.isEmpty()) {
            return null;
        }
        for (MultiSelectGroup group : getGroups()) {
            Iterator<SelectOption> it = group.getOptions().iterator();
            while (true) {
                if (it.hasNext()) {
                    SelectOption option = it.next();
                    if (option.getId().equals(optionId)) {
                        return option;
                    }
                }
            }
        }
        return null;
    }

    public void reload(JSONObject data) {
        super.reload(data);
        try {
            this.groups = loadGroups(this.fields.getJSONArray("groups"));
        } catch (Throwable th) {
        }
    }

    public String getSelectedOptionName() {
        List<?> selectedIds = this.fields.getJSONArray("selectedIds");
        if (selectedIds == null || selectedIds.isEmpty()) {
            return "";
        }
        StringBuilder builder = new StringBuilder();
        int size = selectedIds.size();
        for (int i = 0; i < size; i++) {
            SelectOption option = getOptionById((String) selectedIds.get(i));
            if (option != null) {
                if (i >= 1) {
                    builder.append("，");
                }
                builder.append(option.getName());
            }
        }
        return builder.toString();
    }

    /* JADX WARNING: Code restructure failed: missing block: B:3:0x0009, code lost:
        r1 = getSelectedIds();
     */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public void setSelectedId(java.lang.String r4) {
        /*
            r3 = this;
            if (r4 == 0) goto L_0x0008
            boolean r2 = r4.isEmpty()
            if (r2 == 0) goto L_0x0009
        L_0x0008:
            return
        L_0x0009:
            java.util.List r1 = r3.getSelectedIds()
            java.util.List r0 = r3.perform(r4, r1)
            if (r0 == 0) goto L_0x0008
            boolean r2 = r0.equals(r1)
            if (r2 != 0) goto L_0x0008
            r3.putSelectedIds(r0)
            goto L_0x0008
        */
        throw new UnsupportedOperationException("Method not decompiled: com.taobao.wireless.trade.mbuy.sdk.co.basic.MultiSelectComponent.setSelectedId(java.lang.String):void");
    }

    public List<String> tryChoosing(String selectedId, List<String> tmpSelectedIds) {
        if (tmpSelectedIds == null) {
            tmpSelectedIds = new ArrayList<>(getSelectedIds());
        }
        return selectedId != null ? perform(selectedId, tmpSelectedIds) : tmpSelectedIds;
    }

    public void setSelectedIds(List<String> selectedIds) {
        if (selectedIds != null && !isSelectedEquals(selectedIds)) {
            int size = selectedIds.size();
            for (int i = 0; i < size; i++) {
                int j = i + 1;
                while (j < size) {
                    if (!isInSameGroup(selectedIds.get(i), selectedIds.get(j))) {
                        j++;
                    } else {
                        return;
                    }
                }
            }
            for (MultiSelectGroup group : getGroups()) {
                if (group.isRequired()) {
                    boolean match = false;
                    Iterator<SelectOption> it = group.getOptions().iterator();
                    while (true) {
                        if (it.hasNext()) {
                            if (selectedIds.contains(it.next().getId())) {
                                match = true;
                                break;
                            }
                        } else {
                            break;
                        }
                    }
                    if (!match) {
                        return;
                    }
                }
            }
            putSelectedIds(new ArrayList(selectedIds));
        }
    }

    public ValidateResult validate() {
        ValidateResult result = new ValidateResult();
        boolean required = false;
        Iterator<MultiSelectGroup> it = this.groups.iterator();
        while (true) {
            if (it.hasNext()) {
                if (it.next().isRequired()) {
                    required = true;
                    break;
                }
            } else {
                break;
            }
        }
        if (required) {
            List<String> selectedIds = getSelectedIds();
            boolean pass = true;
            Iterator<MultiSelectGroup> it2 = this.groups.iterator();
            while (true) {
                if (!it2.hasNext()) {
                    break;
                }
                MultiSelectGroup group = it2.next();
                if (group.isRequired()) {
                    boolean hit = false;
                    Iterator<SelectOption> it3 = group.getOptions().iterator();
                    while (true) {
                        if (it3.hasNext()) {
                            if (selectedIds.contains(it3.next().getId())) {
                                hit = true;
                                break;
                            }
                        } else {
                            break;
                        }
                    }
                    if (!hit) {
                        pass = false;
                        break;
                    }
                }
            }
            if (!pass) {
                result.setValid(false);
                result.setErrorMsg("请选择" + getTitle());
            }
        }
        return result;
    }

    private List<MultiSelectGroup> loadGroups(JSONArray groupJSONArray) {
        if (groupJSONArray == null) {
            throw new IllegalStateException();
        }
        List<MultiSelectGroup> groups2 = new ArrayList<>(groupJSONArray.size());
        Iterator<Object> it = groupJSONArray.iterator();
        while (it.hasNext()) {
            groups2.add(new MultiSelectGroup((JSONObject) it.next()));
        }
        return groups2;
    }

    private void putSelectedIds(List<String> newSelectedIds) {
        BuyEngineContext context = this.engine.getContext();
        if (getLinkageType() == LinkageType.REQUEST) {
            final JSONArray originalSelectedIds = this.fields.getJSONArray("selectedIds");
            context.setRollbackProtocol(new RollbackProtocol() {
                public void rollback() {
                    MultiSelectComponent.this.fields.put("selectedIds", (Object) originalSelectedIds);
                }
            });
        }
        this.fields.put("selectedIds", (Object) newSelectedIds);
        notifyLinkageDelegate();
    }

    private List<String> perform(String selectedId, List<String> prevSelectedIds) {
        if (selectedId == null) {
            return null;
        }
        if (prevSelectedIds == null) {
            prevSelectedIds = new ArrayList<>();
        }
        boolean hasSelected = false;
        for (String prevSelectedId : prevSelectedIds) {
            if (selectedId.equals(prevSelectedId)) {
                hasSelected = true;
            }
        }
        if (hasSelected && isInRequiredGroup(selectedId)) {
            return prevSelectedIds;
        }
        List<String> newSelectedIds = new ArrayList<>(prevSelectedIds.size() + 1);
        for (String prevSelectedId2 : prevSelectedIds) {
            if (!isInSameGroup(prevSelectedId2, selectedId)) {
                newSelectedIds.add(prevSelectedId2);
            }
        }
        if (hasSelected) {
            return newSelectedIds;
        }
        newSelectedIds.add(selectedId);
        return newSelectedIds;
    }

    private boolean isInRequiredGroup(String selectedId) {
        for (MultiSelectGroup group : this.groups) {
            if (group.isRequired()) {
                for (SelectOption option : group.getOptions()) {
                    if (option.getId().equals(selectedId)) {
                        return true;
                    }
                }
                continue;
            }
        }
        return false;
    }

    private boolean isInSameGroup(String optionId1, String optionId2) {
        for (MultiSelectGroup group : this.groups) {
            Iterator<SelectOption> it = group.getOptions().iterator();
            while (true) {
                if (it.hasNext()) {
                    if (it.next().getId().equals(optionId1)) {
                        for (SelectOption option2 : group.getOptions()) {
                            if (option2.getId().equals(optionId2)) {
                                return true;
                            }
                        }
                        continue;
                    }
                }
            }
        }
        return false;
    }

    private boolean isSelectedEquals(List<String> selectedIds) {
        return getSelectedIds().hashCode() == selectedIds.hashCode();
    }
}
