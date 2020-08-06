package com.taobao.atlas.dexmerge.dx.util;

import java.util.Arrays;

public class LabeledList extends FixedSizeList {
    private final IntList labelToIndex;

    public LabeledList(int size) {
        super(size);
        this.labelToIndex = new IntList(size);
    }

    public LabeledList(LabeledList old) {
        super(old.size());
        this.labelToIndex = old.labelToIndex.mutableCopy();
        int sz = old.size();
        for (int i = 0; i < sz; i++) {
            Object one = old.get0(i);
            if (one != null) {
                set0(i, one);
            }
        }
    }

    public final int getMaxLabel() {
        int i = this.labelToIndex.size() - 1;
        while (i >= 0 && this.labelToIndex.get(i) < 0) {
            i--;
        }
        int newSize = i + 1;
        this.labelToIndex.shrink(newSize);
        return newSize;
    }

    private void removeLabel(int oldLabel) {
        this.labelToIndex.set(oldLabel, -1);
    }

    private void addLabelIndex(int label, int index) {
        int origSz = this.labelToIndex.size();
        for (int i = 0; i <= label - origSz; i++) {
            this.labelToIndex.add(-1);
        }
        this.labelToIndex.set(label, index);
    }

    public final int indexOfLabel(int label) {
        if (label >= this.labelToIndex.size()) {
            return -1;
        }
        return this.labelToIndex.get(label);
    }

    public final int[] getLabelsInOrder() {
        int sz = size();
        int[] result = new int[sz];
        for (int i = 0; i < sz; i++) {
            LabeledItem li = (LabeledItem) get0(i);
            if (li == null) {
                throw new NullPointerException("null at index " + i);
            }
            result[i] = li.getLabel();
        }
        Arrays.sort(result);
        return result;
    }

    public void shrinkToFit() {
        super.shrinkToFit();
        rebuildLabelToIndex();
    }

    private void rebuildLabelToIndex() {
        int szItems = size();
        for (int i = 0; i < szItems; i++) {
            LabeledItem li = (LabeledItem) get0(i);
            if (li != null) {
                this.labelToIndex.set(li.getLabel(), i);
            }
        }
    }

    /* access modifiers changed from: protected */
    public void set(int n, LabeledItem item) {
        LabeledItem old = (LabeledItem) getOrNull0(n);
        set0(n, item);
        if (old != null) {
            removeLabel(old.getLabel());
        }
        if (item != null) {
            addLabelIndex(item.getLabel(), n);
        }
    }
}
