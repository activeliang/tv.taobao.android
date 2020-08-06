package com.taobao.atlas.dexmerge.dx.merge;

import com.taobao.atlas.dex.ClassDef;
import com.taobao.atlas.dex.Dex;
import com.taobao.atlas.dex.DexException2;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

final class SortableType {
    public static final Comparator<SortableType> NULLS_LAST_ORDER = new Comparator<SortableType>() {
        public int compare(SortableType a, SortableType b) {
            if (a == b) {
                return 0;
            }
            if (b == null) {
                return -1;
            }
            if (a == null) {
                return 1;
            }
            if (a.depth != b.depth) {
                return a.depth - b.depth;
            }
            return a.getTypeIndex() - b.getTypeIndex();
        }
    };
    private ClassDef classDef;
    /* access modifiers changed from: private */
    public int depth = -1;
    private final Dex dex;
    private List<SortableType> dupTypes;
    private final IndexMap indexMap;

    public SortableType(Dex dex2, IndexMap indexMap2, ClassDef classDef2) {
        this.dex = dex2;
        this.indexMap = indexMap2;
        this.classDef = classDef2;
    }

    public Dex getDex() {
        return this.dex;
    }

    public IndexMap getIndexMap() {
        return this.indexMap;
    }

    public ClassDef getClassDef() {
        return this.classDef;
    }

    public int getTypeIndex() {
        return this.classDef.getTypeIndex();
    }

    public List<SortableType> getDupTypes() {
        return this.dupTypes;
    }

    public void addDupSortableType(SortableType sortableType) {
        if (this.dupTypes == null) {
            this.dupTypes = new ArrayList();
        }
        this.dupTypes.add(sortableType);
    }

    public boolean tryAssignDepth(SortableType[] types) {
        int max;
        int max2;
        if (this.classDef.getSupertypeIndex() == -1) {
            max = 0;
        } else if (this.classDef.getSupertypeIndex() == this.classDef.getTypeIndex()) {
            throw new DexException2("Class with type index " + this.classDef.getTypeIndex() + " extends itself");
        } else {
            SortableType sortableSupertype = types[this.classDef.getSupertypeIndex()];
            if (sortableSupertype == null) {
                max = 1;
            } else if (sortableSupertype.depth == -1) {
                return false;
            } else {
                max = sortableSupertype.depth;
            }
        }
        for (short interfaceIndex : this.classDef.getInterfaces()) {
            SortableType implemented = types[interfaceIndex];
            if (implemented == null) {
                max2 = Math.max(max2, 1);
            } else if (implemented.depth == -1) {
                return false;
            } else {
                max2 = Math.max(max2, implemented.depth);
            }
        }
        this.depth = max2 + 1;
        return true;
    }

    public boolean isDepthAssigned() {
        return this.depth != -1;
    }
}
