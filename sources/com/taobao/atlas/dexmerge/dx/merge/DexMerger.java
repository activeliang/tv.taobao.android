package com.taobao.atlas.dexmerge.dx.merge;

import android.util.Log;
import com.taobao.atlas.dex.Annotation;
import com.taobao.atlas.dex.ClassData;
import com.taobao.atlas.dex.ClassDef;
import com.taobao.atlas.dex.Code;
import com.taobao.atlas.dex.Dex;
import com.taobao.atlas.dex.DexException2;
import com.taobao.atlas.dex.DexIndexOverflowException;
import com.taobao.atlas.dex.FieldId;
import com.taobao.atlas.dex.MethodId;
import com.taobao.atlas.dex.ProtoId;
import com.taobao.atlas.dex.TableOfContents;
import com.taobao.atlas.dex.TypeList;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

public final class DexMerger {
    private static final byte DBG_ADVANCE_LINE = 2;
    private static final byte DBG_ADVANCE_PC = 1;
    private static final byte DBG_END_LOCAL = 5;
    private static final byte DBG_END_SEQUENCE = 0;
    private static final byte DBG_RESTART_LOCAL = 6;
    private static final byte DBG_SET_EPILOGUE_BEGIN = 8;
    private static final byte DBG_SET_FILE = 9;
    private static final byte DBG_SET_PROLOGUE_END = 7;
    private static final byte DBG_START_LOCAL = 3;
    private static final byte DBG_START_LOCAL_EXTENDED = 4;
    /* access modifiers changed from: private */
    public final Dex.Section annotationOut;
    /* access modifiers changed from: private */
    public final Dex.Section annotationSetOut;
    /* access modifiers changed from: private */
    public final Dex.Section annotationSetRefListOut;
    /* access modifiers changed from: private */
    public final Dex.Section annotationsDirectoryOut;
    /* access modifiers changed from: private */
    public final Dex.Section classDataOut;
    /* access modifiers changed from: private */
    public final Dex.Section codeOut;
    private final CollisionPolicy collisionPolicy;
    private int compactWasteThreshold;
    /* access modifiers changed from: private */
    public final TableOfContents contentsOut;
    /* access modifiers changed from: private */
    public final Dex.Section debugInfoOut;
    private final Dex dexOut;
    /* access modifiers changed from: private */
    public final Dex[] dexes;
    /* access modifiers changed from: private */
    public final Dex.Section encodedArrayOut;
    /* access modifiers changed from: private */
    public final Dex.Section headerOut;
    /* access modifiers changed from: private */
    public final Dex.Section idsDefsOut;
    /* access modifiers changed from: private */
    public final IndexMap[] indexMaps;
    private final InstructionTransformer instructionTransformer;
    /* access modifiers changed from: private */
    public final Dex.Section mapListOut;
    private List<Integer> removeClasses;
    private List<String> removeTypeClasses;
    /* access modifiers changed from: private */
    public final Dex.Section stringDataOut;
    /* access modifiers changed from: private */
    public final Dex.Section typeListOut;
    private final WriterSizes writerSizes;

    public void setRemoveTypeClasses(List<String> removeTypeClasses2) {
        this.removeTypeClasses = removeTypeClasses2;
    }

    public DexMerger(Dex[] dexes2, CollisionPolicy collisionPolicy2) throws IOException {
        this(dexes2, collisionPolicy2, new WriterSizes(dexes2, false));
    }

    private DexMerger(Dex[] dexes2, CollisionPolicy collisionPolicy2, WriterSizes writerSizes2) throws IOException {
        this.compactWasteThreshold = 1048576;
        this.removeClasses = new ArrayList();
        this.removeTypeClasses = new ArrayList();
        this.dexes = dexes2;
        this.collisionPolicy = collisionPolicy2;
        this.writerSizes = writerSizes2;
        this.dexOut = new Dex(writerSizes2.size());
        this.indexMaps = new IndexMap[dexes2.length];
        for (int i = 0; i < dexes2.length; i++) {
            this.indexMaps[i] = new IndexMap(this.dexOut, dexes2[i].getTableOfContents());
        }
        this.instructionTransformer = new InstructionTransformer();
        this.headerOut = this.dexOut.appendSection(writerSizes2.header, "header");
        this.idsDefsOut = this.dexOut.appendSection(writerSizes2.idsDefs, "ids defs");
        this.contentsOut = this.dexOut.getTableOfContents();
        this.contentsOut.dataOff = this.dexOut.getNextSectionStart();
        this.contentsOut.mapList.off = this.dexOut.getNextSectionStart();
        this.contentsOut.mapList.size = 1;
        this.mapListOut = this.dexOut.appendSection(writerSizes2.mapList, "map list");
        this.contentsOut.typeLists.off = this.dexOut.getNextSectionStart();
        this.typeListOut = this.dexOut.appendSection(writerSizes2.typeList, "type list");
        this.contentsOut.annotationSetRefLists.off = this.dexOut.getNextSectionStart();
        this.annotationSetRefListOut = this.dexOut.appendSection(writerSizes2.annotationsSetRefList, "annotation set ref list");
        this.contentsOut.annotationSets.off = this.dexOut.getNextSectionStart();
        this.annotationSetOut = this.dexOut.appendSection(writerSizes2.annotationsSet, "annotation sets");
        this.contentsOut.classDatas.off = this.dexOut.getNextSectionStart();
        this.classDataOut = this.dexOut.appendSection(writerSizes2.classData, "class data");
        this.contentsOut.codes.off = this.dexOut.getNextSectionStart();
        this.codeOut = this.dexOut.appendSection(writerSizes2.code, "code");
        this.contentsOut.stringDatas.off = this.dexOut.getNextSectionStart();
        this.stringDataOut = this.dexOut.appendSection(writerSizes2.stringData, "string data");
        this.contentsOut.debugInfos.off = this.dexOut.getNextSectionStart();
        this.debugInfoOut = this.dexOut.appendSection(writerSizes2.debugInfo, "debug info");
        this.contentsOut.annotations.off = this.dexOut.getNextSectionStart();
        this.annotationOut = this.dexOut.appendSection(writerSizes2.annotation, "annotation");
        this.contentsOut.encodedArrays.off = this.dexOut.getNextSectionStart();
        this.encodedArrayOut = this.dexOut.appendSection(writerSizes2.encodedArray, "encoded array");
        this.contentsOut.annotationsDirectories.off = this.dexOut.getNextSectionStart();
        this.annotationsDirectoryOut = this.dexOut.appendSection(writerSizes2.annotationsDirectory, "annotations directory");
        this.contentsOut.dataSize = this.dexOut.getNextSectionStart() - this.contentsOut.dataOff;
    }

    public void setCompactWasteThreshold(int compactWasteThreshold2) {
        this.compactWasteThreshold = compactWasteThreshold2;
    }

    /* access modifiers changed from: protected */
    public Dex mergeDexes() throws IOException {
        mergeStringIds();
        mergeTypeIds();
        mergeTypeLists();
        mergeProtoIds();
        mergeFieldIds();
        mergeMethodIds();
        mergeAnnotations();
        unionAnnotationSetsAndDirectories();
        mergeClassDefs();
        this.contentsOut.header.off = 0;
        this.contentsOut.header.size = 1;
        this.contentsOut.fileSize = this.dexOut.getLength();
        this.contentsOut.computeSizesFromOffsets();
        this.contentsOut.writeHeader(this.headerOut, mergeApiLevels());
        this.contentsOut.writeMap(this.mapListOut);
        this.dexOut.writeHashes();
        return this.dexOut;
    }

    public Dex merge() throws IOException {
        if (this.dexes.length == 1) {
            return this.dexes[0];
        }
        if (this.dexes.length == 0) {
            return null;
        }
        long start = System.nanoTime();
        Dex result = mergeDexes();
        WriterSizes compactedSizes = new WriterSizes(this);
        int wastedByteCount = this.writerSizes.size() - compactedSizes.size();
        if (wastedByteCount > this.compactWasteThreshold) {
            result = new DexMerger(new Dex[]{this.dexOut, new Dex(0)}, CollisionPolicy.FAIL, compactedSizes).mergeDexes();
            System.out.printf("Result compacted from %.1fKiB to %.1fKiB to save %.1fKiB%n", new Object[]{Float.valueOf(((float) this.dexOut.getLength()) / 1024.0f), Float.valueOf(((float) result.getLength()) / 1024.0f), Float.valueOf(((float) wastedByteCount) / 1024.0f)});
        }
        long elapsed = System.nanoTime() - start;
        for (int i = 0; i < this.dexes.length; i++) {
            System.out.printf("Merged dex #%d (%d defs/%.1fKiB)%n", new Object[]{Integer.valueOf(i + 1), Integer.valueOf(this.dexes[i].getTableOfContents().classDefs.size), Float.valueOf(((float) this.dexes[i].getLength()) / 1024.0f)});
        }
        System.out.printf("Result is %d defs/%.1fKiB. Took %.1fs%n", new Object[]{Integer.valueOf(result.getTableOfContents().classDefs.size), Float.valueOf(((float) result.getLength()) / 1024.0f), Float.valueOf(((float) elapsed) / 1.0E9f)});
        return result;
    }

    abstract class IdMerger<T extends Comparable<T>> {
        private final Dex.Section out;

        /* access modifiers changed from: package-private */
        public abstract TableOfContents.Section getSection(TableOfContents tableOfContents);

        /* access modifiers changed from: package-private */
        public abstract T read(Dex.Section section, IndexMap indexMap, int i);

        /* access modifiers changed from: package-private */
        public abstract void updateIndex(int i, IndexMap indexMap, int i2, int i3);

        /* access modifiers changed from: package-private */
        public abstract void write(T t);

        protected IdMerger(Dex.Section out2) {
            this.out = out2;
        }

        public final void mergeSorted() {
            TableOfContents.Section[] sections = new TableOfContents.Section[DexMerger.this.dexes.length];
            Dex.Section[] dexSections = new Dex.Section[DexMerger.this.dexes.length];
            int[] offsets = new int[DexMerger.this.dexes.length];
            int[] indexes = new int[DexMerger.this.dexes.length];
            TreeMap<T, List<Integer>> values = new TreeMap<>();
            boolean hasValue = false;
            for (int i = 0; i < DexMerger.this.dexes.length; i++) {
                sections[i] = getSection(DexMerger.this.dexes[i].getTableOfContents());
                dexSections[i] = sections[i].exists() ? DexMerger.this.dexes[i].open(sections[i].off) : null;
                offsets[i] = readIntoMap(dexSections[i], sections[i], DexMerger.this.indexMaps[i], indexes[i], values, i);
                if (offsets[i] > 0) {
                    hasValue = true;
                }
            }
            if (hasValue) {
                getSection(DexMerger.this.contentsOut).off = this.out.getPosition();
            } else {
                getSection(DexMerger.this.contentsOut).off = 0;
            }
            int outCount = 0;
            while (!values.isEmpty()) {
                Map.Entry<T, List<Integer>> first = values.pollFirstEntry();
                for (Integer dex : first.getValue()) {
                    int i2 = offsets[dex.intValue()];
                    IndexMap indexMap = DexMerger.this.indexMaps[dex.intValue()];
                    int intValue = dex.intValue();
                    int i3 = indexes[intValue];
                    indexes[intValue] = i3 + 1;
                    updateIndex(i2, indexMap, i3, outCount);
                    offsets[dex.intValue()] = readIntoMap(dexSections[dex.intValue()], sections[dex.intValue()], DexMerger.this.indexMaps[dex.intValue()], indexes[dex.intValue()], values, dex.intValue());
                }
                write((Comparable) first.getKey());
                outCount++;
            }
            getSection(DexMerger.this.contentsOut).size = outCount;
        }

        private int readIntoMap(Dex.Section in, TableOfContents.Section section, IndexMap indexMap, int index, TreeMap<T, List<Integer>> values, int dex) {
            int offset = in != null ? in.getPosition() : -1;
            if (index < section.size) {
                T v = read(in, indexMap, index);
                List<Integer> l = values.get(v);
                if (l == null) {
                    l = new ArrayList<>();
                    values.put(v, l);
                }
                l.add(new Integer(dex));
            }
            return offset;
        }

        public final void mergeUnsorted() {
            getSection(DexMerger.this.contentsOut).off = this.out.getPosition();
            List<IdMerger<T>.UnsortedValue> all = new ArrayList<>();
            for (int i = 0; i < DexMerger.this.dexes.length; i++) {
                all.addAll(readUnsortedValues(DexMerger.this.dexes[i], DexMerger.this.indexMaps[i]));
            }
            Collections.sort(all);
            int outCount = 0;
            int i2 = 0;
            while (i2 < all.size()) {
                int i3 = i2 + 1;
                IdMerger<T>.UnsortedValue e1 = all.get(i2);
                updateIndex(e1.offset, e1.indexMap, e1.index, outCount - 1);
                while (true) {
                    i2 = i3;
                    if (i2 >= all.size() || e1.compareTo(all.get(i2)) != 0) {
                        write(e1.value);
                        outCount++;
                    } else {
                        i3 = i2 + 1;
                        IdMerger<T>.UnsortedValue e2 = all.get(i2);
                        updateIndex(e2.offset, e2.indexMap, e2.index, outCount - 1);
                    }
                }
                write(e1.value);
                outCount++;
            }
            getSection(DexMerger.this.contentsOut).size = outCount;
        }

        private List<IdMerger<T>.UnsortedValue> readUnsortedValues(Dex source, IndexMap indexMap) {
            TableOfContents.Section section = getSection(source.getTableOfContents());
            if (!section.exists()) {
                return Collections.emptyList();
            }
            List<IdMerger<T>.UnsortedValue> result = new ArrayList<>();
            Dex.Section in = source.open(section.off);
            for (int i = 0; i < section.size; i++) {
                Dex dex = source;
                IndexMap indexMap2 = indexMap;
                result.add(new UnsortedValue(dex, indexMap2, read(in, indexMap, 0), i, in.getPosition()));
            }
            return result;
        }

        class UnsortedValue implements Comparable<IdMerger<T>.UnsortedValue> {
            final int index;
            final IndexMap indexMap;
            final int offset;
            final Dex source;
            final T value;

            UnsortedValue(Dex source2, IndexMap indexMap2, T value2, int index2, int offset2) {
                this.source = source2;
                this.indexMap = indexMap2;
                this.value = value2;
                this.index = index2;
                this.offset = offset2;
            }

            public int compareTo(IdMerger<T>.UnsortedValue unsortedValue) {
                return this.value.compareTo(unsortedValue.value);
            }
        }
    }

    private int mergeApiLevels() {
        int maxApi = -1;
        for (Dex tableOfContents : this.dexes) {
            int dexMinApi = tableOfContents.getTableOfContents().apiLevel;
            if (maxApi < dexMinApi) {
                maxApi = dexMinApi;
            }
        }
        return maxApi;
    }

    private void mergeStringIds() {
        String value;
        TableOfContents.Section[] sections = new TableOfContents.Section[this.dexes.length];
        Dex.Section[] dexSections = new Dex.Section[this.dexes.length];
        int[] offsets = new int[this.dexes.length];
        int[] indexes = new int[this.dexes.length];
        TreeMap<String, List<Integer>> values = new TreeMap<>();
        boolean hasValue = false;
        for (int i = 0; i < this.dexes.length; i++) {
            sections[i] = this.dexes[i].getTableOfContents().stringIds;
            dexSections[i] = sections[i].exists() ? this.dexes[i].open(sections[i].off) : null;
            if (dexSections[i] != null) {
                offsets[i] = dexSections[i].getPosition();
                if (offsets[i] > 0) {
                    hasValue = true;
                }
            }
            for (int j = 0; j < sections[i].size; j++) {
                int position = dexSections[i].getPosition();
                int off = dexSections[i].readInt();
                if (off < 0) {
                    int nexDexOff = (off * -1) - 1;
                    if (this.dexes.length <= i + 1 || this.dexes[i + 1].strings().size() < nexDexOff + 1) {
                        throw new IllegalArgumentException("The string index: " + nexDexOff + " does not existed on next dex!");
                    }
                    value = this.dexes[i + 1].strings().get(nexDexOff);
                } else {
                    dexSections[i].getData().position(position);
                    value = dexSections[i].readString();
                }
                List<Integer> l = values.get(value);
                if (l == null) {
                    l = new ArrayList<>();
                }
                l.add(new Integer(i));
                values.put(value, l);
            }
        }
        if (hasValue) {
            this.contentsOut.stringIds.off = this.idsDefsOut.getPosition();
        } else {
            this.contentsOut.stringIds.off = 0;
        }
        int outCount = 0;
        while (!values.isEmpty()) {
            Map.Entry<String, List<Integer>> first = values.pollFirstEntry();
            for (Integer dex : first.getValue()) {
                int intValue = dex.intValue();
                int i2 = indexes[intValue];
                indexes[intValue] = i2 + 1;
                this.indexMaps[dex.intValue()].stringIds[i2] = outCount;
            }
            this.contentsOut.stringDatas.size++;
            this.idsDefsOut.writeInt(this.stringDataOut.getPosition());
            this.stringDataOut.writeStringData(first.getKey());
            outCount++;
        }
        this.contentsOut.stringIds.size = outCount;
    }

    private void mergeStringIdsOld() {
        new IdMerger<String>(this.idsDefsOut) {
            /* access modifiers changed from: package-private */
            public TableOfContents.Section getSection(TableOfContents tableOfContents) {
                return tableOfContents.stringIds;
            }

            /* access modifiers changed from: package-private */
            public String read(Dex.Section in, IndexMap indexMap, int index) {
                return in.readString();
            }

            /* access modifiers changed from: package-private */
            public void updateIndex(int offset, IndexMap indexMap, int oldIndex, int newIndex) {
                indexMap.stringIds[oldIndex] = newIndex;
            }

            /* access modifiers changed from: package-private */
            public void write(String value) {
                DexMerger.this.contentsOut.stringDatas.size++;
                DexMerger.this.idsDefsOut.writeInt(DexMerger.this.stringDataOut.getPosition());
                DexMerger.this.stringDataOut.writeStringData(value);
            }
        }.mergeSorted();
    }

    private void mergeTypeIds() {
        new IdMerger<Integer>(this.idsDefsOut) {
            /* access modifiers changed from: package-private */
            public TableOfContents.Section getSection(TableOfContents tableOfContents) {
                return tableOfContents.typeIds;
            }

            /* access modifiers changed from: package-private */
            public Integer read(Dex.Section in, IndexMap indexMap, int index) {
                return Integer.valueOf(indexMap.adjustString(in.readInt()));
            }

            /* access modifiers changed from: package-private */
            public void updateIndex(int offset, IndexMap indexMap, int oldIndex, int newIndex) {
                if (newIndex < 0 || newIndex > 65535) {
                    throw new DexIndexOverflowException("type ID not in [0, 0xffff]: " + newIndex);
                }
                indexMap.typeIds[oldIndex] = (short) newIndex;
            }

            /* access modifiers changed from: package-private */
            public void write(Integer value) {
                DexMerger.this.idsDefsOut.writeInt(value.intValue());
            }
        }.mergeSorted();
    }

    private void mergeTypeLists() {
        new IdMerger<TypeList>(this.typeListOut) {
            /* access modifiers changed from: package-private */
            public TableOfContents.Section getSection(TableOfContents tableOfContents) {
                return tableOfContents.typeLists;
            }

            /* access modifiers changed from: package-private */
            public TypeList read(Dex.Section in, IndexMap indexMap, int index) {
                return indexMap.adjustTypeList(in.readTypeList());
            }

            /* access modifiers changed from: package-private */
            public void updateIndex(int offset, IndexMap indexMap, int oldIndex, int newIndex) {
                indexMap.putTypeListOffset(offset, DexMerger.this.typeListOut.getPosition());
            }

            /* access modifiers changed from: package-private */
            public void write(TypeList value) {
                DexMerger.this.typeListOut.writeTypeList(value);
            }
        }.mergeUnsorted();
    }

    private void mergeProtoIds() {
        new IdMerger<ProtoId>(this.idsDefsOut) {
            /* access modifiers changed from: package-private */
            public TableOfContents.Section getSection(TableOfContents tableOfContents) {
                return tableOfContents.protoIds;
            }

            /* access modifiers changed from: package-private */
            public ProtoId read(Dex.Section in, IndexMap indexMap, int index) {
                return indexMap.adjust(in.readProtoId());
            }

            /* access modifiers changed from: package-private */
            public void updateIndex(int offset, IndexMap indexMap, int oldIndex, int newIndex) {
                if (newIndex < 0 || newIndex > 65535) {
                    throw new DexIndexOverflowException("proto ID not in [0, 0xffff]: " + newIndex);
                }
                indexMap.protoIds[oldIndex] = (short) newIndex;
            }

            /* access modifiers changed from: package-private */
            public void write(ProtoId value) {
                value.writeTo(DexMerger.this.idsDefsOut);
            }
        }.mergeSorted();
    }

    private void mergeFieldIds() {
        new IdMerger<FieldId>(this.idsDefsOut) {
            /* access modifiers changed from: package-private */
            public TableOfContents.Section getSection(TableOfContents tableOfContents) {
                return tableOfContents.fieldIds;
            }

            /* access modifiers changed from: package-private */
            public FieldId read(Dex.Section in, IndexMap indexMap, int index) {
                return indexMap.adjust(in.readFieldId());
            }

            /* access modifiers changed from: package-private */
            public void updateIndex(int offset, IndexMap indexMap, int oldIndex, int newIndex) {
                if (newIndex < 0 || newIndex > 65535) {
                    throw new DexIndexOverflowException("field ID not in [0, 0xffff]: " + newIndex);
                }
                indexMap.fieldIds[oldIndex] = (short) newIndex;
            }

            /* access modifiers changed from: package-private */
            public void write(FieldId value) {
                value.writeTo(DexMerger.this.idsDefsOut);
            }
        }.mergeSorted();
    }

    private void mergeMethodIds() {
        new IdMerger<MethodId>(this.idsDefsOut) {
            /* access modifiers changed from: package-private */
            public TableOfContents.Section getSection(TableOfContents tableOfContents) {
                return tableOfContents.methodIds;
            }

            /* access modifiers changed from: package-private */
            public MethodId read(Dex.Section in, IndexMap indexMap, int index) {
                return indexMap.adjust(in.readMethodId());
            }

            /* access modifiers changed from: package-private */
            public void updateIndex(int offset, IndexMap indexMap, int oldIndex, int newIndex) {
                if (newIndex < 0 || newIndex > 65535) {
                    throw new DexIndexOverflowException("method ID not in [0, 0xffff]: " + newIndex);
                }
                indexMap.methodIds[oldIndex] = (short) newIndex;
            }

            /* access modifiers changed from: package-private */
            public void write(MethodId methodId) {
                methodId.writeTo(DexMerger.this.idsDefsOut);
            }
        }.mergeSorted();
    }

    private void mergeAnnotations() {
        new IdMerger<Annotation>(this.annotationOut) {
            /* access modifiers changed from: package-private */
            public TableOfContents.Section getSection(TableOfContents tableOfContents) {
                return tableOfContents.annotations;
            }

            /* access modifiers changed from: package-private */
            public Annotation read(Dex.Section in, IndexMap indexMap, int index) {
                return indexMap.adjust(in.readAnnotation());
            }

            /* access modifiers changed from: package-private */
            public void updateIndex(int offset, IndexMap indexMap, int oldIndex, int newIndex) {
                indexMap.putAnnotationOffset(offset, DexMerger.this.annotationOut.getPosition());
            }

            /* access modifiers changed from: package-private */
            public void write(Annotation value) {
                value.writeTo(DexMerger.this.annotationOut);
            }
        }.mergeUnsorted();
    }

    private void mergeClassDefs() {
        SortableType[] types = getSortedTypes();
        this.contentsOut.classDefs.off = this.idsDefsOut.getPosition();
        this.contentsOut.classDefs.size = types.length;
        for (SortableType type : types) {
            Dex in = type.getDex();
            List<DexSectionItem<ClassDef>> dupClassDefs = new ArrayList<>();
            if (type.getDupTypes() != null) {
                for (SortableType dupType : type.getDupTypes()) {
                    DexSectionItem<ClassDef> classDefDexItem = new DexSectionItem<>();
                    classDefDexItem.indexMap = dupType.getIndexMap();
                    classDefDexItem.item = dupType.getClassDef();
                    classDefDexItem.target = dupType.getDex();
                    dupClassDefs.add(classDefDexItem);
                }
            }
            transformClassDef(in, type.getClassDef(), type.getIndexMap(), dupClassDefs);
        }
    }

    private SortableType[] getSortedTypes() {
        boolean allDone;
        SortableType[] sortableTypes = new SortableType[this.contentsOut.typeIds.size];
        for (int i = 0; i < this.dexes.length; i++) {
            readSortableTypes(sortableTypes, this.dexes[i], this.indexMaps[i]);
        }
        do {
            allDone = true;
            for (SortableType sortableType : sortableTypes) {
                if (sortableType != null && !sortableType.isDepthAssigned()) {
                    allDone &= sortableType.tryAssignDepth(sortableTypes);
                }
            }
        } while (!allDone);
        Arrays.sort(sortableTypes, SortableType.NULLS_LAST_ORDER);
        int firstNull = Arrays.asList(sortableTypes).indexOf((Object) null);
        return firstNull != -1 ? (SortableType[]) Arrays.copyOfRange(sortableTypes, 0, firstNull) : sortableTypes;
    }

    private void readSortableTypes(SortableType[] sortableTypes, Dex buffer, IndexMap indexMap) {
        for (ClassDef classDef : buffer.classDefs()) {
            int classDataOffSet = classDef.getClassDataOffset();
            if (classDef.getClassDataOffset() < 0) {
                this.removeClasses.add(Integer.valueOf(classDataOffSet * -1));
            } else if (this.removeClasses.contains(Integer.valueOf(classDataOffSet))) {
                continue;
            } else {
                if (this.removeTypeClasses.size() > 0) {
                    String type = buffer.typeNames().get(classDef.getTypeIndex());
                    if (this.removeTypeClasses.contains(type)) {
                        Log.e("DexMerger", "remove oringal class:" + type);
                    }
                }
                SortableType sortableType = indexMap.adjust(new SortableType(buffer, indexMap, classDef));
                int t = sortableType.getTypeIndex();
                if (sortableTypes[t] == null) {
                    sortableTypes[t] = sortableType;
                } else if (this.collisionPolicy == CollisionPolicy.MERGE_CLASS_KEEP_FIRST) {
                    sortableTypes[t].addDupSortableType(sortableType);
                } else if (this.collisionPolicy != CollisionPolicy.KEEP_FIRST) {
                    throw new DexException2("Multiple dex files define " + buffer.typeNames().get(classDef.getTypeIndex()));
                }
            }
        }
    }

    private void unionAnnotationSetsAndDirectories() {
        for (int i = 0; i < this.dexes.length; i++) {
            transformAnnotationSets(this.dexes[i], this.indexMaps[i]);
        }
        for (int i2 = 0; i2 < this.dexes.length; i2++) {
            transformAnnotationSetRefLists(this.dexes[i2], this.indexMaps[i2]);
        }
        for (int i3 = 0; i3 < this.dexes.length; i3++) {
            transformAnnotationDirectories(this.dexes[i3], this.indexMaps[i3]);
        }
        for (int i4 = 0; i4 < this.dexes.length; i4++) {
            transformStaticValues(this.dexes[i4], this.indexMaps[i4]);
        }
    }

    private void transformAnnotationSets(Dex in, IndexMap indexMap) {
        TableOfContents.Section section = in.getTableOfContents().annotationSets;
        if (section.exists()) {
            Dex.Section setIn = in.open(section.off);
            for (int i = 0; i < section.size; i++) {
                transformAnnotationSet(indexMap, setIn);
            }
        }
    }

    private void transformAnnotationSetRefLists(Dex in, IndexMap indexMap) {
        TableOfContents.Section section = in.getTableOfContents().annotationSetRefLists;
        if (section.exists()) {
            Dex.Section setIn = in.open(section.off);
            for (int i = 0; i < section.size; i++) {
                transformAnnotationSetRefList(indexMap, setIn);
            }
        }
    }

    private void transformAnnotationDirectories(Dex in, IndexMap indexMap) {
        TableOfContents.Section section = in.getTableOfContents().annotationsDirectories;
        if (section.exists()) {
            Dex.Section directoryIn = in.open(section.off);
            for (int i = 0; i < section.size; i++) {
                transformAnnotationDirectory(directoryIn, indexMap);
            }
        }
    }

    private void transformStaticValues(Dex in, IndexMap indexMap) {
        TableOfContents.Section section = in.getTableOfContents().encodedArrays;
        if (section.exists()) {
            Dex.Section staticValuesIn = in.open(section.off);
            for (int i = 0; i < section.size; i++) {
                transformStaticValues(staticValuesIn, indexMap);
            }
        }
    }

    private void transformClassDef(Dex in, ClassDef classDef, IndexMap indexMap, List<DexSectionItem<ClassDef>> dupClassDefs) {
        this.idsDefsOut.assertFourByteAligned();
        this.idsDefsOut.writeInt(classDef.getTypeIndex());
        this.idsDefsOut.writeInt(classDef.getAccessFlags());
        this.idsDefsOut.writeInt(classDef.getSupertypeIndex());
        this.idsDefsOut.writeInt(classDef.getInterfacesOffset());
        this.idsDefsOut.writeInt(indexMap.adjustString(classDef.getSourceFileIndex()));
        this.idsDefsOut.writeInt(indexMap.adjustAnnotationDirectory(classDef.getAnnotationsOffset()));
        if (classDef.getClassDataOffset() == 0) {
            this.idsDefsOut.writeInt(0);
        } else {
            this.idsDefsOut.writeInt(this.classDataOut.getPosition());
            ClassData classData = in.readClassData(classDef);
            List<DexSectionItem<ClassData>> dupClassDatas = new ArrayList<>();
            for (DexSectionItem<ClassDef> dupClassDef : dupClassDefs) {
                ClassData dupClassData = dupClassDef.target.readClassData((ClassDef) dupClassDef.item);
                DexSectionItem<ClassData> dupClassDataItem = new DexSectionItem<>();
                dupClassDataItem.target = dupClassDef.target;
                dupClassDataItem.item = dupClassData;
                dupClassDataItem.indexMap = dupClassDef.indexMap;
                dupClassDatas.add(dupClassDataItem);
            }
            transformClassData(in, classData, indexMap, dupClassDatas);
        }
        this.idsDefsOut.writeInt(indexMap.adjustStaticValues(classDef.getStaticValuesOffset()));
    }

    private void transformAnnotationDirectory(Dex.Section directoryIn, IndexMap indexMap) {
        this.contentsOut.annotationsDirectories.size++;
        this.annotationsDirectoryOut.assertFourByteAligned();
        indexMap.putAnnotationDirectoryOffset(directoryIn.getPosition(), this.annotationsDirectoryOut.getPosition());
        this.annotationsDirectoryOut.writeInt(indexMap.adjustAnnotationSet(directoryIn.readInt()));
        int fieldsSize = directoryIn.readInt();
        this.annotationsDirectoryOut.writeInt(fieldsSize);
        int methodsSize = directoryIn.readInt();
        this.annotationsDirectoryOut.writeInt(methodsSize);
        int parameterListSize = directoryIn.readInt();
        this.annotationsDirectoryOut.writeInt(parameterListSize);
        for (int i = 0; i < fieldsSize; i++) {
            this.annotationsDirectoryOut.writeInt(indexMap.adjustField(directoryIn.readInt()));
            this.annotationsDirectoryOut.writeInt(indexMap.adjustAnnotationSet(directoryIn.readInt()));
        }
        for (int i2 = 0; i2 < methodsSize; i2++) {
            this.annotationsDirectoryOut.writeInt(indexMap.adjustMethod(directoryIn.readInt()));
            this.annotationsDirectoryOut.writeInt(indexMap.adjustAnnotationSet(directoryIn.readInt()));
        }
        for (int i3 = 0; i3 < parameterListSize; i3++) {
            this.annotationsDirectoryOut.writeInt(indexMap.adjustMethod(directoryIn.readInt()));
            this.annotationsDirectoryOut.writeInt(indexMap.adjustAnnotationSetRefList(directoryIn.readInt()));
        }
    }

    private void transformAnnotationSet(IndexMap indexMap, Dex.Section setIn) {
        this.contentsOut.annotationSets.size++;
        this.annotationSetOut.assertFourByteAligned();
        indexMap.putAnnotationSetOffset(setIn.getPosition(), this.annotationSetOut.getPosition());
        int size = setIn.readInt();
        this.annotationSetOut.writeInt(size);
        for (int j = 0; j < size; j++) {
            this.annotationSetOut.writeInt(indexMap.adjustAnnotation(setIn.readInt()));
        }
    }

    private void transformAnnotationSetRefList(IndexMap indexMap, Dex.Section refListIn) {
        this.contentsOut.annotationSetRefLists.size++;
        this.annotationSetRefListOut.assertFourByteAligned();
        indexMap.putAnnotationSetRefListOffset(refListIn.getPosition(), this.annotationSetRefListOut.getPosition());
        int parameterCount = refListIn.readInt();
        this.annotationSetRefListOut.writeInt(parameterCount);
        for (int p = 0; p < parameterCount; p++) {
            this.annotationSetRefListOut.writeInt(indexMap.adjustAnnotationSet(refListIn.readInt()));
        }
    }

    private String getMethodIdIndex(MethodId methodId) {
        new StringBuilder().append(methodId.getDeclaringClassIndex()).append(".").append(methodId.getNameIndex()).append("-").append(methodId.getProtoIndex());
        return methodId.toString();
    }

    private void transformClassData(Dex in, ClassData classData, IndexMap indexMap, List<DexSectionItem<ClassData>> dupClassDatas) {
        this.contentsOut.classDatas.size++;
        ClassData.Field[] staticFields = classData.getStaticFields();
        ClassData.Field[] instanceFields = classData.getInstanceFields();
        ClassData.Method[] directMethods = classData.getDirectMethods();
        ClassData.Method[] virtualMethods = classData.getVirtualMethods();
        this.classDataOut.writeUleb128(staticFields.length);
        this.classDataOut.writeUleb128(instanceFields.length);
        this.classDataOut.writeUleb128(directMethods.length);
        this.classDataOut.writeUleb128(virtualMethods.length);
        transformFields(indexMap, staticFields);
        transformFields(indexMap, instanceFields);
        Map<String, ArrayList<DexSectionItem<ClassData.Method>>> dupMethods = new HashMap<>();
        for (DexSectionItem<ClassData> dupClassData : dupClassDatas) {
            ClassData.Method[] methods = ((ClassData) dupClassData.item).getDirectMethods();
            if (methods != null && methods.length > 0) {
                int length = methods.length;
                int i = 0;
                while (true) {
                    int i2 = i;
                    if (i2 >= length) {
                        break;
                    }
                    ClassData.Method method = methods[i2];
                    DexSectionItem<ClassData.Method> methodItem = new DexSectionItem<>();
                    methodItem.target = dupClassData.target;
                    methodItem.indexMap = dupClassData.indexMap;
                    methodItem.item = method;
                    String index = getMethodIdIndex(dupClassData.indexMap.adjust(dupClassData.target.methodIds().get(method.getMethodIndex())));
                    ArrayList<DexSectionItem<ClassData.Method>> methodList = dupMethods.get(index);
                    if (methodList == null) {
                        methodList = new ArrayList<>();
                    }
                    methodList.add(methodItem);
                    dupMethods.put(index, methodList);
                    i = i2 + 1;
                }
            }
        }
        for (DexSectionItem<ClassData> dupClassData2 : dupClassDatas) {
            ClassData.Method[] methods2 = ((ClassData) dupClassData2.item).getVirtualMethods();
            if (methods2 != null && methods2.length > 0) {
                int length2 = methods2.length;
                int i3 = 0;
                while (true) {
                    int i4 = i3;
                    if (i4 >= length2) {
                        break;
                    }
                    ClassData.Method method2 = methods2[i4];
                    DexSectionItem<ClassData.Method> methodItem2 = new DexSectionItem<>();
                    methodItem2.target = dupClassData2.target;
                    methodItem2.indexMap = dupClassData2.indexMap;
                    methodItem2.item = method2;
                    String index2 = getMethodIdIndex(dupClassData2.indexMap.adjust(dupClassData2.target.methodIds().get(method2.getMethodIndex())));
                    ArrayList<DexSectionItem<ClassData.Method>> methodList2 = dupMethods.get(index2);
                    if (methodList2 == null) {
                        methodList2 = new ArrayList<>();
                    }
                    methodList2.add(methodItem2);
                    dupMethods.put(index2, methodList2);
                    i3 = i4 + 1;
                }
            }
        }
        transformMethods(in, indexMap, directMethods, dupMethods);
        transformMethods(in, indexMap, virtualMethods, dupMethods);
    }

    private void transformFields(IndexMap indexMap, ClassData.Field[] fields) {
        int lastOutFieldIndex = 0;
        for (ClassData.Field field : fields) {
            int outFieldIndex = indexMap.adjustField(field.getFieldIndex());
            this.classDataOut.writeUleb128(outFieldIndex - lastOutFieldIndex);
            lastOutFieldIndex = outFieldIndex;
            this.classDataOut.writeUleb128(field.getAccessFlags());
        }
    }

    private void transformMethods(Dex in, IndexMap indexMap, ClassData.Method[] methods, Map<String, ArrayList<DexSectionItem<ClassData.Method>>> dupMethodMaps) {
        int lastOutMethodIndex = 0;
        for (ClassData.Method method : methods) {
            int outMethodIndex = indexMap.adjustMethod(method.getMethodIndex());
            this.classDataOut.writeUleb128(outMethodIndex - lastOutMethodIndex);
            lastOutMethodIndex = outMethodIndex;
            this.classDataOut.writeUleb128(method.getAccessFlags());
            if (method.getCodeOffset() == 0) {
                this.classDataOut.writeUleb128(0);
            } else if (method.getCodeOffset() == 1) {
                MethodId methodId = indexMap.adjust(in.methodIds().get(method.getMethodIndex()));
                ArrayList<DexSectionItem<ClassData.Method>> dupMethods = dupMethodMaps.get(getMethodIdIndex(methodId));
                if (dupMethods == null || dupMethods.size() <= 0) {
                    throw new IllegalArgumentException("Method does not existed in pre dex! method is:" + methodId.toString());
                }
                boolean matchCodes = false;
                Iterator<DexSectionItem<ClassData.Method>> it = dupMethods.iterator();
                while (true) {
                    if (!it.hasNext()) {
                        break;
                    }
                    DexSectionItem<ClassData.Method> methodDexSectionItem = it.next();
                    ClassData.Method preMethod = (ClassData.Method) methodDexSectionItem.item;
                    if (preMethod.getCodeOffset() > 1) {
                        this.codeOut.alignToFourBytesWithZeroFill();
                        this.classDataOut.writeUleb128(this.codeOut.getPosition());
                        transformCode(methodDexSectionItem.target, methodDexSectionItem.target.readCode(preMethod), methodDexSectionItem.indexMap);
                        matchCodes = true;
                        break;
                    }
                }
                if (!matchCodes) {
                    this.classDataOut.writeUleb128(0);
                }
            } else {
                this.codeOut.alignToFourBytesWithZeroFill();
                this.classDataOut.writeUleb128(this.codeOut.getPosition());
                transformCode(in, in.readCode(method), indexMap);
            }
        }
    }

    private void transformCode(Dex in, Code code, IndexMap indexMap) {
        this.contentsOut.codes.size++;
        this.codeOut.assertFourByteAligned();
        this.codeOut.writeUnsignedShort(code.getRegistersSize());
        this.codeOut.writeUnsignedShort(code.getInsSize());
        this.codeOut.writeUnsignedShort(code.getOutsSize());
        Code.Try[] tries = code.getTries();
        Code.CatchHandler[] catchHandlers = code.getCatchHandlers();
        this.codeOut.writeUnsignedShort(tries.length);
        int debugInfoOffset = code.getDebugInfoOffset();
        if (debugInfoOffset != 0) {
            this.codeOut.writeInt(this.debugInfoOut.getPosition());
            transformDebugInfoItem(in.open(debugInfoOffset), indexMap);
        } else {
            this.codeOut.writeInt(0);
        }
        short[] newInstructions = this.instructionTransformer.transform(indexMap, code.getInstructions());
        this.codeOut.writeInt(newInstructions.length);
        this.codeOut.write(newInstructions);
        if (tries.length > 0) {
            if (newInstructions.length % 2 == 1) {
                this.codeOut.writeShort(0);
            }
            Dex.Section triesSection = this.dexOut.open(this.codeOut.getPosition());
            this.codeOut.skip(tries.length * 8);
            transformTries(triesSection, tries, transformCatchHandlers(indexMap, catchHandlers));
        }
    }

    private int[] transformCatchHandlers(IndexMap indexMap, Code.CatchHandler[] catchHandlers) {
        int baseOffset = this.codeOut.getPosition();
        this.codeOut.writeUleb128(catchHandlers.length);
        int[] offsets = new int[catchHandlers.length];
        for (int i = 0; i < catchHandlers.length; i++) {
            offsets[i] = this.codeOut.getPosition() - baseOffset;
            transformEncodedCatchHandler(catchHandlers[i], indexMap);
        }
        return offsets;
    }

    private void transformTries(Dex.Section out, Code.Try[] tries, int[] catchHandlerOffsets) {
        for (Code.Try tryItem : tries) {
            out.writeInt(tryItem.getStartAddress());
            out.writeUnsignedShort(tryItem.getInstructionCount());
            out.writeUnsignedShort(catchHandlerOffsets[tryItem.getCatchHandlerIndex()]);
        }
    }

    private void transformDebugInfoItem(Dex.Section in, IndexMap indexMap) {
        this.contentsOut.debugInfos.size++;
        this.debugInfoOut.writeUleb128(in.readUleb128());
        int parametersSize = in.readUleb128();
        this.debugInfoOut.writeUleb128(parametersSize);
        for (int p = 0; p < parametersSize; p++) {
            this.debugInfoOut.writeUleb128p1(indexMap.adjustString(in.readUleb128p1()));
        }
        while (true) {
            int opcode = in.readByte();
            this.debugInfoOut.writeByte(opcode);
            switch (opcode) {
                case 0:
                    return;
                case 1:
                    this.debugInfoOut.writeUleb128(in.readUleb128());
                    break;
                case 2:
                    this.debugInfoOut.writeSleb128(in.readSleb128());
                    break;
                case 3:
                case 4:
                    this.debugInfoOut.writeUleb128(in.readUleb128());
                    this.debugInfoOut.writeUleb128p1(indexMap.adjustString(in.readUleb128p1()));
                    this.debugInfoOut.writeUleb128p1(indexMap.adjustType(in.readUleb128p1()));
                    if (opcode != 4) {
                        break;
                    } else {
                        this.debugInfoOut.writeUleb128p1(indexMap.adjustString(in.readUleb128p1()));
                        break;
                    }
                case 5:
                case 6:
                    this.debugInfoOut.writeUleb128(in.readUleb128());
                    break;
                case 9:
                    this.debugInfoOut.writeUleb128p1(indexMap.adjustString(in.readUleb128p1()));
                    break;
            }
        }
    }

    private void transformEncodedCatchHandler(Code.CatchHandler catchHandler, IndexMap indexMap) {
        int catchAllAddress = catchHandler.getCatchAllAddress();
        int[] typeIndexes = catchHandler.getTypeIndexes();
        int[] addresses = catchHandler.getAddresses();
        if (catchAllAddress != -1) {
            this.codeOut.writeSleb128(-typeIndexes.length);
        } else {
            this.codeOut.writeSleb128(typeIndexes.length);
        }
        for (int i = 0; i < typeIndexes.length; i++) {
            this.codeOut.writeUleb128(indexMap.adjustType(typeIndexes[i]));
            this.codeOut.writeUleb128(addresses[i]);
        }
        if (catchAllAddress != -1) {
            this.codeOut.writeUleb128(catchAllAddress);
        }
    }

    private void transformStaticValues(Dex.Section in, IndexMap indexMap) {
        this.contentsOut.encodedArrays.size++;
        indexMap.putStaticValuesOffset(in.getPosition(), this.encodedArrayOut.getPosition());
        indexMap.adjustEncodedArray(in.readEncodedArray()).writeTo(this.encodedArrayOut);
    }

    private static class WriterSizes {
        /* access modifiers changed from: private */
        public int annotation;
        /* access modifiers changed from: private */
        public int annotationsDirectory;
        /* access modifiers changed from: private */
        public int annotationsSet;
        /* access modifiers changed from: private */
        public int annotationsSetRefList;
        /* access modifiers changed from: private */
        public int classData;
        /* access modifiers changed from: private */
        public int code;
        /* access modifiers changed from: private */
        public int debugInfo;
        /* access modifiers changed from: private */
        public int encodedArray;
        /* access modifiers changed from: private */
        public int header = 112;
        /* access modifiers changed from: private */
        public int idsDefs;
        /* access modifiers changed from: private */
        public int mapList;
        /* access modifiers changed from: private */
        public int stringData;
        /* access modifiers changed from: private */
        public int typeList;

        public WriterSizes(Dex[] dexes, boolean exact) {
            for (Dex tableOfContents : dexes) {
                plus(tableOfContents.getTableOfContents(), exact);
            }
            fourByteAlign();
        }

        public WriterSizes(DexMerger dexMerger) {
            this.header = dexMerger.headerOut.used();
            this.idsDefs = dexMerger.idsDefsOut.used();
            this.mapList = dexMerger.mapListOut.used();
            this.typeList = dexMerger.typeListOut.used();
            this.classData = dexMerger.classDataOut.used();
            this.code = dexMerger.codeOut.used();
            this.stringData = dexMerger.stringDataOut.used();
            this.debugInfo = dexMerger.debugInfoOut.used();
            this.encodedArray = dexMerger.encodedArrayOut.used();
            this.annotationsDirectory = dexMerger.annotationsDirectoryOut.used();
            this.annotationsSet = dexMerger.annotationSetOut.used();
            this.annotationsSetRefList = dexMerger.annotationSetRefListOut.used();
            this.annotation = dexMerger.annotationOut.used();
            fourByteAlign();
        }

        private void plus(TableOfContents contents, boolean exact) {
            this.idsDefs += (contents.stringIds.size * 4) + (contents.typeIds.size * 4) + (contents.protoIds.size * 12) + (contents.fieldIds.size * 8) + (contents.methodIds.size * 8) + (contents.classDefs.size * 32);
            this.mapList = (contents.sections.length * 12) + 4;
            this.typeList += fourByteAlign(contents.typeLists.byteCount);
            this.stringData += contents.stringDatas.byteCount;
            this.annotationsDirectory += contents.annotationsDirectories.byteCount;
            this.annotationsSet += contents.annotationSets.byteCount;
            this.annotationsSetRefList += contents.annotationSetRefLists.byteCount;
            if (exact) {
                this.code += contents.codes.byteCount;
                this.classData += contents.classDatas.byteCount;
                this.encodedArray += contents.encodedArrays.byteCount;
                this.annotation += contents.annotations.byteCount;
                this.debugInfo += contents.debugInfos.byteCount;
                return;
            }
            this.code += (int) Math.ceil(((double) contents.codes.byteCount) * 1.25d);
            this.classData += (int) Math.ceil(((double) contents.classDatas.byteCount) * 1.34d);
            this.encodedArray += contents.encodedArrays.byteCount * 2;
            this.annotation += (int) Math.ceil((double) (contents.annotations.byteCount * 2));
            this.debugInfo += contents.debugInfos.byteCount * 2;
        }

        private void fourByteAlign() {
            this.header = fourByteAlign(this.header);
            this.idsDefs = fourByteAlign(this.idsDefs);
            this.mapList = fourByteAlign(this.mapList);
            this.typeList = fourByteAlign(this.typeList);
            this.classData = fourByteAlign(this.classData);
            this.code = fourByteAlign(this.code);
            this.stringData = fourByteAlign(this.stringData);
            this.debugInfo = fourByteAlign(this.debugInfo);
            this.encodedArray = fourByteAlign(this.encodedArray);
            this.annotationsDirectory = fourByteAlign(this.annotationsDirectory);
            this.annotationsSet = fourByteAlign(this.annotationsSet);
            this.annotationsSetRefList = fourByteAlign(this.annotationsSetRefList);
            this.annotation = fourByteAlign(this.annotation);
        }

        private static int fourByteAlign(int position) {
            return (position + 3) & -4;
        }

        public int size() {
            return this.header + this.idsDefs + this.mapList + this.typeList + this.classData + this.code + this.stringData + this.debugInfo + this.encodedArray + this.annotationsDirectory + this.annotationsSet + this.annotationsSetRefList + this.annotation;
        }
    }

    public static void main(String[] args) throws IOException {
        if (args.length < 2) {
            printUsage();
            return;
        }
        Dex[] dexes2 = new Dex[(args.length - 1)];
        for (int i = 1; i < args.length; i++) {
            dexes2[i - 1] = new Dex(new File(args[i]));
        }
        new DexMerger(dexes2, CollisionPolicy.KEEP_FIRST).merge().writeTo(new File(args[0]));
    }

    private static void printUsage() {
        System.out.println("Usage: DexMerger <out.dex> <a.dex> <b.dex> ...");
        System.out.println();
        System.out.println("If a class is defined in several dex, the class found in the first dex will be used.");
    }
}
