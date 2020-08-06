package com.taobao.atlas.dexmerge.dx.merge;

import com.taobao.atlas.dex.Annotation;
import com.taobao.atlas.dex.ClassData;
import com.taobao.atlas.dex.ClassDef;
import com.taobao.atlas.dex.Code;
import com.taobao.atlas.dex.Dex;
import com.taobao.atlas.dex.FieldId;
import com.taobao.atlas.dex.MethodId;
import com.taobao.atlas.dex.ProtoId;
import com.taobao.atlas.dex.TableOfContents;
import com.taobao.atlas.dex.TypeList;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;

public final class DexTransform {
    static final /* synthetic */ boolean $assertionsDisabled = (!DexTransform.class.desiredAssertionStatus());
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
    /* access modifiers changed from: private */
    public final TableOfContents contentsOut;
    /* access modifiers changed from: private */
    public final Dex.Section debugInfoOut;
    private List<Integer> debugInfoRemoveMethodss;
    /* access modifiers changed from: private */
    public final Dex dex;
    private final Dex dexOut;
    /* access modifiers changed from: private */
    public final Dex.Section encodedArrayOut;
    /* access modifiers changed from: private */
    public final Dex.Section headerOut;
    /* access modifiers changed from: private */
    public final Dex.Section idsDefsOut;
    /* access modifiers changed from: private */
    public final IndexMap indexMap;
    private final InstructionTransformer instructionTransformer;
    /* access modifiers changed from: private */
    public final Dex.Section mapListOut;
    private Map<String, DexItem> methodCodeOffMaps;
    private final boolean onlyShrink;
    private Map<Integer, Integer> removeClassDefs;
    /* access modifiers changed from: private */
    public final Dex.Section stringDataOut;
    private TreeMap<String, DexItem> stringValueOffMaps;
    /* access modifiers changed from: private */
    public final Dex.Section typeListOut;
    private final WriterSizes writerSizes;

    public DexTransform(Dex dex2) throws IOException {
        this(dex2, new WriterSizes(dex2, true), false);
    }

    private DexTransform(Dex dex2, WriterSizes writerSizes2, boolean onlyShrink2) throws IOException {
        this.stringValueOffMaps = new TreeMap<>();
        this.methodCodeOffMaps = new HashMap();
        this.debugInfoRemoveMethodss = new ArrayList();
        this.removeClassDefs = new HashMap();
        this.dex = dex2;
        this.writerSizes = writerSizes2;
        this.onlyShrink = onlyShrink2;
        this.dexOut = new Dex(writerSizes2.size());
        this.indexMap = new IndexMap(this.dexOut, dex2.getTableOfContents());
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
        if (!onlyShrink2) {
            initIndexMaps();
        }
    }

    private void initIndexMaps() {
        intStringIndexMaps(this.dex);
        initCodeMaps(this.dex);
    }

    private void intStringIndexMaps(Dex dex2) {
        TableOfContents.Section stringIds = dex2.getTableOfContents().stringIds;
        Dex.Section stringDataSection = dex2.open(stringIds.off);
        for (int i = 0; i < stringIds.size; i++) {
            int position = stringDataSection.getPosition();
            int off = stringDataSection.readInt();
            if (off < 0) {
                this.stringValueOffMaps.put(String.valueOf(off), new DexItem(off, i, off));
            } else {
                stringDataSection.getData().position(position);
                this.stringValueOffMaps.put(stringDataSection.readString(), new DexItem(off, i, off));
            }
        }
    }

    private void initCodeMaps(Dex dex2) {
        for (ClassDef classDef : dex2.classDefs()) {
            if (classDef.getClassDataOffset() > 0) {
                ClassData classData = dex2.readClassData(classDef);
                ClassData.Method[] directMethods = classData.getDirectMethods();
                ClassData.Method[] virtualMethods = classData.getVirtualMethods();
                initMethodsMaps(dex2, directMethods);
                initMethodsMaps(dex2, virtualMethods);
            }
        }
    }

    private void initMethodsMaps(Dex dex2, ClassData.Method[] methods) {
        for (ClassData.Method method : methods) {
            MethodId methodId = dex2.methodIds().get(method.getMethodIndex());
            this.methodCodeOffMaps.put(dex2.typeNames().get(methodId.getDeclaringClassIndex()) + "." + (dex2.strings().get(methodId.getNameIndex()) + dex2.readTypeList(dex2.protoIds().get(methodId.getProtoIndex()).getParametersOffset())), new DexItem(method.getCodeOffset(), method.getMethodIndex(), method.getCodeOffset()));
        }
    }

    private Set<String> getMethodMaps(Dex dex2) {
        Set<String> methods = new HashSet<>();
        for (ClassDef classDef : dex2.classDefs()) {
            if (classDef.getClassDataOffset() > 0) {
                ClassData classData = dex2.readClassData(classDef);
                ClassData.Method[] directMethods = classData.getDirectMethods();
                ClassData.Method[] virtualMethods = classData.getVirtualMethods();
                initMethodsMaps(dex2, directMethods, methods);
                initMethodsMaps(dex2, virtualMethods, methods);
            }
        }
        return methods;
    }

    private void initMethodsMaps(Dex dex2, ClassData.Method[] methods, Set<String> methodSets) {
        for (ClassData.Method method : methods) {
            MethodId methodId = dex2.methodIds().get(method.getMethodIndex());
            methodSets.add(dex2.typeNames().get(methodId.getDeclaringClassIndex()) + "." + (dex2.strings().get(methodId.getNameIndex()) + dex2.readTypeList(dex2.protoIds().get(methodId.getProtoIndex()).getParametersOffset())));
        }
    }

    public DexTransform uninoStringValues(Dex baseDex) {
        TableOfContents.Section stringIds = baseDex.getTableOfContents().stringIds;
        Dex.Section stringSection = baseDex.open(stringIds.off);
        for (int i = 0; i < stringIds.size; i++) {
            String value = stringSection.readString();
            if (this.stringValueOffMaps.containsKey(value)) {
                DexItem dexItem = this.stringValueOffMaps.get(value);
                dexItem.newOffset = (i + 1) * -1;
                this.stringValueOffMaps.put(value, dexItem);
            }
        }
        return this;
    }

    public DexTransform markClassDef(Dex baseDex, List<String> classNames) {
        for (ClassDef classDef : this.dex.classDefs()) {
            int typeId = classDef.getTypeIndex();
            String value = this.dex.typeNames().get(typeId);
            if (classNames.contains(value)) {
                for (ClassDef baseClassDef : baseDex.classDefs()) {
                    if (baseDex.typeNames().get(baseClassDef.getTypeIndex()).equals(value)) {
                        this.removeClassDefs.put(Integer.valueOf(typeId), Integer.valueOf(baseClassDef.getClassDataOffset() * -1));
                    }
                }
            }
        }
        return this;
    }

    public DexTransform removeMethodCodes(HashMap<String, ArrayList<String>> modifyMethods) {
        if ($assertionsDisabled || modifyMethods != null) {
            List<String> fullModifyMethods = new ArrayList<>();
            for (Map.Entry<String, ArrayList<String>> entry : modifyMethods.entrySet()) {
                String className = entry.getKey();
                Iterator it = entry.getValue().iterator();
                while (it.hasNext()) {
                    fullModifyMethods.add(className + "." + ((String) it.next()));
                }
            }
            for (Map.Entry<String, DexItem> entry2 : this.methodCodeOffMaps.entrySet()) {
                String name = entry2.getKey();
                if (!fullModifyMethods.contains(name)) {
                    DexItem dexItem = entry2.getValue();
                    dexItem.newOffset = 1;
                    this.methodCodeOffMaps.put(name, dexItem);
                }
            }
            return this;
        }
        throw new AssertionError();
    }

    public DexTransform removeMethodCodes(HashMap<String, ArrayList<String>> modifyMethods, Dex baseDex) {
        if ($assertionsDisabled || modifyMethods != null) {
            List<String> fullModifyMethods = new ArrayList<>();
            for (Map.Entry<String, ArrayList<String>> entry : modifyMethods.entrySet()) {
                String className = entry.getKey();
                Iterator it = entry.getValue().iterator();
                while (it.hasNext()) {
                    fullModifyMethods.add(className + "." + ((String) it.next()));
                }
            }
            Set<String> baseMethods = getMethodMaps(baseDex);
            for (Map.Entry<String, DexItem> entry2 : this.methodCodeOffMaps.entrySet()) {
                String name = entry2.getKey();
                if (!fullModifyMethods.contains(name) && baseMethods.contains(name)) {
                    DexItem dexItem = entry2.getValue();
                    dexItem.newOffset = 1;
                    this.methodCodeOffMaps.put(name, dexItem);
                }
            }
            return this;
        }
        throw new AssertionError();
    }

    public DexTransform removeDebugInfos(List<String> removeClasses) {
        if ($assertionsDisabled || removeClasses != null) {
            for (Map.Entry<String, DexItem> entry : this.methodCodeOffMaps.entrySet()) {
                String name = entry.getKey();
                if (removeClasses.contains(name.substring(0, name.indexOf(".")))) {
                    this.debugInfoRemoveMethodss.add(Integer.valueOf(entry.getValue().index));
                }
            }
            return this;
        }
        throw new AssertionError();
    }

    /* access modifiers changed from: protected */
    public Dex transfromDex() throws IOException {
        transformStringIds();
        transformTypeIds();
        transformTypeLists();
        transformProtoIds();
        transformFieldIds();
        transformMethodIds();
        transformAnnotations();
        transformAnnotationSets(this.dex, this.indexMap);
        transformAnnotationSetRefLists(this.dex, this.indexMap);
        transformAnnotationDirectories(this.dex, this.indexMap);
        transformStaticValues(this.dex, this.indexMap);
        transformClassDefs();
        this.contentsOut.header.off = 0;
        this.contentsOut.header.size = 1;
        this.contentsOut.fileSize = this.dexOut.getLength();
        this.contentsOut.computeSizesFromOffsets();
        this.contentsOut.writeHeader(this.headerOut, this.dex.getTableOfContents().apiLevel);
        this.contentsOut.writeMap(this.mapListOut);
        this.dexOut.writeHashes();
        return this.dexOut;
    }

    private void transformStringIds() {
        if (this.onlyShrink) {
            this.contentsOut.stringIds.off = this.idsDefsOut.getPosition();
            TableOfContents.Section stringIds = this.dex.getTableOfContents().stringIds;
            Dex.Section stringDataSection = this.dex.open(stringIds.off);
            for (int i = 0; i < stringIds.size; i++) {
                int position = stringDataSection.getPosition();
                int off = stringDataSection.readInt();
                if (off < 0) {
                    this.idsDefsOut.writeInt(off);
                } else {
                    stringDataSection.getData().position(position);
                    this.contentsOut.stringDatas.size++;
                    this.idsDefsOut.writeInt(this.stringDataOut.getPosition());
                    this.stringDataOut.writeStringData(stringDataSection.readString());
                }
                this.indexMap.stringIds[i] = (short) i;
            }
            this.contentsOut.stringIds.size = stringIds.size;
            return;
        }
        this.contentsOut.stringIds.off = this.idsDefsOut.getPosition();
        int index = 0;
        for (Map.Entry<String, DexItem> entry : this.stringValueOffMaps.entrySet()) {
            String value = entry.getKey();
            DexItem item = entry.getValue();
            if (item.newOffset > 0) {
                this.contentsOut.stringDatas.size++;
                this.idsDefsOut.writeInt(this.stringDataOut.getPosition());
                this.stringDataOut.writeStringData(value);
            } else {
                this.idsDefsOut.writeInt(item.newOffset);
            }
            this.indexMap.stringIds[index] = (short) index;
            index++;
        }
        this.contentsOut.stringIds.size = this.stringValueOffMaps.size();
    }

    public Dex process() throws IOException {
        return new DexTransform(transfromDex(), new WriterSizes(this), true).transfromDex();
    }

    abstract class IdTransform<T extends Comparable<T>> {
        private final Dex.Section out;

        /* access modifiers changed from: package-private */
        public abstract TableOfContents.Section getSection(TableOfContents tableOfContents);

        /* access modifiers changed from: package-private */
        public abstract T read(Dex.Section section, IndexMap indexMap, int i);

        /* access modifiers changed from: package-private */
        public abstract void updateIndex(int i, IndexMap indexMap, int i2, int i3);

        /* access modifiers changed from: package-private */
        public abstract void write(T t);

        protected IdTransform(Dex.Section out2) {
            this.out = out2;
        }

        public void execute() {
            TableOfContents.Section dexSection = getSection(DexTransform.this.dex.getTableOfContents());
            int size = dexSection.size;
            if (dexSection.exists()) {
                Dex.Section inSection = DexTransform.this.dex.open(dexSection.off);
                getSection(DexTransform.this.contentsOut).off = this.out.getPosition();
                for (int i = 0; i < size; i++) {
                    updateIndex(inSection.getPosition(), DexTransform.this.indexMap, i, i);
                    write(read(inSection, DexTransform.this.indexMap, i));
                }
            } else {
                getSection(DexTransform.this.contentsOut).off = 0;
            }
            getSection(DexTransform.this.contentsOut).size = dexSection.size;
        }
    }

    private void transformTypeIds() {
        new IdTransform<Integer>(this.idsDefsOut) {
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
                indexMap.typeIds[oldIndex] = (short) newIndex;
            }

            /* access modifiers changed from: package-private */
            public void write(Integer value) {
                DexTransform.this.idsDefsOut.writeInt(value.intValue());
            }
        }.execute();
    }

    private void transformTypeLists() {
        new IdTransform<TypeList>(this.typeListOut) {
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
                indexMap.putTypeListOffset(offset, DexTransform.this.typeListOut.getPosition());
            }

            /* access modifiers changed from: package-private */
            public void write(TypeList value) {
                DexTransform.this.typeListOut.writeTypeList(value);
            }
        }.execute();
    }

    private void transformProtoIds() {
        new IdTransform<ProtoId>(this.idsDefsOut) {
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
                indexMap.protoIds[oldIndex] = (short) newIndex;
            }

            /* access modifiers changed from: package-private */
            public void write(ProtoId value) {
                value.writeTo(DexTransform.this.idsDefsOut);
            }
        }.execute();
    }

    private void transformFieldIds() {
        new IdTransform<FieldId>(this.idsDefsOut) {
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
                indexMap.fieldIds[oldIndex] = (short) newIndex;
            }

            /* access modifiers changed from: package-private */
            public void write(FieldId value) {
                value.writeTo(DexTransform.this.idsDefsOut);
            }
        }.execute();
    }

    private void transformMethodIds() {
        new IdTransform<MethodId>(this.idsDefsOut) {
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
                indexMap.methodIds[oldIndex] = (short) newIndex;
            }

            /* access modifiers changed from: package-private */
            public void write(MethodId methodId) {
                methodId.writeTo(DexTransform.this.idsDefsOut);
            }
        }.execute();
    }

    private void transformAnnotations() {
        new IdTransform<Annotation>(this.annotationOut) {
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
                indexMap.putAnnotationOffset(offset, DexTransform.this.annotationOut.getPosition());
            }

            /* access modifiers changed from: package-private */
            public void write(Annotation value) {
                value.writeTo(DexTransform.this.annotationOut);
            }
        }.execute();
    }

    private void transformClassDefs() {
        TableOfContents.Section dexSection = this.dex.getTableOfContents().classDefs;
        if (dexSection.exists()) {
            this.contentsOut.classDefs.off = this.idsDefsOut.getPosition();
            this.contentsOut.classDefs.size = dexSection.size;
            for (ClassDef oldClassDef : this.dex.classDefs()) {
                transformClassDef(this.dex, this.indexMap.adjust(oldClassDef), this.indexMap);
            }
        }
    }

    private void transformAnnotationSets(Dex in, IndexMap indexMap2) {
        TableOfContents.Section section = in.getTableOfContents().annotationSets;
        if (section.exists()) {
            Dex.Section setIn = in.open(section.off);
            for (int i = 0; i < section.size; i++) {
                transformAnnotationSet(indexMap2, setIn);
            }
        }
    }

    private void transformAnnotationSetRefLists(Dex in, IndexMap indexMap2) {
        TableOfContents.Section section = in.getTableOfContents().annotationSetRefLists;
        if (section.exists()) {
            Dex.Section setIn = in.open(section.off);
            for (int i = 0; i < section.size; i++) {
                transformAnnotationSetRefList(indexMap2, setIn);
            }
        }
    }

    private void transformAnnotationDirectories(Dex in, IndexMap indexMap2) {
        TableOfContents.Section section = in.getTableOfContents().annotationsDirectories;
        if (section.exists()) {
            Dex.Section directoryIn = in.open(section.off);
            for (int i = 0; i < section.size; i++) {
                transformAnnotationDirectory(directoryIn, indexMap2);
            }
        }
    }

    private void transformStaticValues(Dex in, IndexMap indexMap2) {
        TableOfContents.Section section = in.getTableOfContents().encodedArrays;
        if (section.exists()) {
            Dex.Section staticValuesIn = in.open(section.off);
            for (int i = 0; i < section.size; i++) {
                transformStaticValues(staticValuesIn, indexMap2);
            }
        }
    }

    private void transformClassDef(Dex in, ClassDef classDef, IndexMap indexMap2) {
        this.idsDefsOut.assertFourByteAligned();
        this.idsDefsOut.writeInt(classDef.getTypeIndex());
        this.idsDefsOut.writeInt(classDef.getAccessFlags());
        this.idsDefsOut.writeInt(classDef.getSupertypeIndex());
        this.idsDefsOut.writeInt(classDef.getInterfacesOffset());
        this.idsDefsOut.writeInt(indexMap2.adjustString(classDef.getSourceFileIndex()));
        this.idsDefsOut.writeInt(indexMap2.adjustAnnotationDirectory(classDef.getAnnotationsOffset()));
        int classDataOff = classDef.getClassDataOffset();
        if (this.removeClassDefs.containsKey(Integer.valueOf(classDef.getTypeIndex())) && !this.onlyShrink) {
            this.idsDefsOut.writeInt(this.removeClassDefs.get(Integer.valueOf(classDef.getTypeIndex())).intValue());
        } else if (this.onlyShrink && classDataOff < 0) {
            this.idsDefsOut.writeInt(classDataOff);
        } else if (classDataOff == 0) {
            this.idsDefsOut.writeInt(0);
        } else {
            this.idsDefsOut.writeInt(this.classDataOut.getPosition());
            transformClassData(in, in.readClassData(classDef), indexMap2);
        }
        this.idsDefsOut.writeInt(indexMap2.adjustStaticValues(classDef.getStaticValuesOffset()));
    }

    private void transformAnnotationDirectory(Dex.Section directoryIn, IndexMap indexMap2) {
        this.contentsOut.annotationsDirectories.size++;
        this.annotationsDirectoryOut.assertFourByteAligned();
        indexMap2.putAnnotationDirectoryOffset(directoryIn.getPosition(), this.annotationsDirectoryOut.getPosition());
        this.annotationsDirectoryOut.writeInt(indexMap2.adjustAnnotationSet(directoryIn.readInt()));
        int fieldsSize = directoryIn.readInt();
        this.annotationsDirectoryOut.writeInt(fieldsSize);
        int methodsSize = directoryIn.readInt();
        this.annotationsDirectoryOut.writeInt(methodsSize);
        int parameterListSize = directoryIn.readInt();
        this.annotationsDirectoryOut.writeInt(parameterListSize);
        for (int i = 0; i < fieldsSize; i++) {
            this.annotationsDirectoryOut.writeInt(indexMap2.adjustField(directoryIn.readInt()));
            this.annotationsDirectoryOut.writeInt(indexMap2.adjustAnnotationSet(directoryIn.readInt()));
        }
        for (int i2 = 0; i2 < methodsSize; i2++) {
            this.annotationsDirectoryOut.writeInt(indexMap2.adjustMethod(directoryIn.readInt()));
            this.annotationsDirectoryOut.writeInt(indexMap2.adjustAnnotationSet(directoryIn.readInt()));
        }
        for (int i3 = 0; i3 < parameterListSize; i3++) {
            this.annotationsDirectoryOut.writeInt(indexMap2.adjustMethod(directoryIn.readInt()));
            this.annotationsDirectoryOut.writeInt(indexMap2.adjustAnnotationSetRefList(directoryIn.readInt()));
        }
    }

    private void transformAnnotationSet(IndexMap indexMap2, Dex.Section setIn) {
        this.contentsOut.annotationSets.size++;
        this.annotationSetOut.assertFourByteAligned();
        indexMap2.putAnnotationSetOffset(setIn.getPosition(), this.annotationSetOut.getPosition());
        int size = setIn.readInt();
        this.annotationSetOut.writeInt(size);
        for (int j = 0; j < size; j++) {
            this.annotationSetOut.writeInt(indexMap2.adjustAnnotation(setIn.readInt()));
        }
    }

    private void transformAnnotationSetRefList(IndexMap indexMap2, Dex.Section refListIn) {
        this.contentsOut.annotationSetRefLists.size++;
        this.annotationSetRefListOut.assertFourByteAligned();
        indexMap2.putAnnotationSetRefListOffset(refListIn.getPosition(), this.annotationSetRefListOut.getPosition());
        int parameterCount = refListIn.readInt();
        this.annotationSetRefListOut.writeInt(parameterCount);
        for (int p = 0; p < parameterCount; p++) {
            this.annotationSetRefListOut.writeInt(indexMap2.adjustAnnotationSet(refListIn.readInt()));
        }
    }

    private void transformClassData(Dex in, ClassData classData, IndexMap indexMap2) {
        this.contentsOut.classDatas.size++;
        ClassData.Field[] staticFields = classData.getStaticFields();
        ClassData.Field[] instanceFields = classData.getInstanceFields();
        ClassData.Method[] directMethods = classData.getDirectMethods();
        ClassData.Method[] virtualMethods = classData.getVirtualMethods();
        this.classDataOut.writeUleb128(staticFields.length);
        this.classDataOut.writeUleb128(instanceFields.length);
        this.classDataOut.writeUleb128(directMethods.length);
        this.classDataOut.writeUleb128(virtualMethods.length);
        transformFields(indexMap2, staticFields);
        transformFields(indexMap2, instanceFields);
        transformMethods(in, indexMap2, directMethods);
        transformMethods(in, indexMap2, virtualMethods);
    }

    private void transformFields(IndexMap indexMap2, ClassData.Field[] fields) {
        int lastOutFieldIndex = 0;
        for (ClassData.Field field : fields) {
            int outFieldIndex = indexMap2.adjustField(field.getFieldIndex());
            this.classDataOut.writeUleb128(outFieldIndex - lastOutFieldIndex);
            lastOutFieldIndex = outFieldIndex;
            this.classDataOut.writeUleb128(field.getAccessFlags());
        }
    }

    private void transformMethods(Dex in, IndexMap indexMap2, ClassData.Method[] methods) {
        int lastOutMethodIndex = 0;
        for (ClassData.Method method : methods) {
            int outMethodIndex = indexMap2.adjustMethod(method.getMethodIndex());
            this.classDataOut.writeUleb128(outMethodIndex - lastOutMethodIndex);
            lastOutMethodIndex = outMethodIndex;
            this.classDataOut.writeUleb128(method.getAccessFlags());
            if (!this.onlyShrink) {
                MethodId methodId = this.dex.methodIds().get(method.getMethodIndex());
                DexItem dexItem = this.methodCodeOffMaps.get(this.dex.typeNames().get(methodId.getDeclaringClassIndex()) + "." + (this.dex.strings().get(methodId.getNameIndex()) + this.dex.readTypeList(this.dex.protoIds().get(methodId.getProtoIndex()).getParametersOffset())));
                if (dexItem.newOffset <= 1) {
                    this.classDataOut.writeUleb128(dexItem.newOffset);
                } else {
                    this.codeOut.alignToFourBytesWithZeroFill();
                    this.classDataOut.writeUleb128(this.codeOut.getPosition());
                    transformCode(in, in.readCode(method), indexMap2, method.getMethodIndex());
                }
            } else if (method.getCodeOffset() <= 1) {
                this.classDataOut.writeUleb128(method.getCodeOffset());
            } else {
                this.codeOut.alignToFourBytesWithZeroFill();
                this.classDataOut.writeUleb128(this.codeOut.getPosition());
                transformCode(in, in.readCode(method), indexMap2, method.getMethodIndex());
            }
        }
    }

    private void transformCode(Dex in, Code code, IndexMap indexMap2, int methodIndex) {
        this.contentsOut.codes.size++;
        this.codeOut.assertFourByteAligned();
        this.codeOut.writeUnsignedShort(code.getRegistersSize());
        this.codeOut.writeUnsignedShort(code.getInsSize());
        this.codeOut.writeUnsignedShort(code.getOutsSize());
        Code.Try[] tries = code.getTries();
        Code.CatchHandler[] catchHandlers = code.getCatchHandlers();
        this.codeOut.writeUnsignedShort(tries.length);
        int debugInfoOffset = code.getDebugInfoOffset();
        if (this.debugInfoRemoveMethodss.contains(Integer.valueOf(methodIndex)) || debugInfoOffset <= 0) {
            this.codeOut.writeInt(0);
        } else {
            this.codeOut.writeInt(this.debugInfoOut.getPosition());
            transformDebugInfoItem(in.open(debugInfoOffset), indexMap2);
        }
        short[] newInstructions = this.instructionTransformer.transform(indexMap2, code.getInstructions());
        this.codeOut.writeInt(newInstructions.length);
        this.codeOut.write(newInstructions);
        if (tries.length > 0) {
            if (newInstructions.length % 2 == 1) {
                this.codeOut.writeShort(0);
            }
            Dex.Section triesSection = this.dexOut.open(this.codeOut.getPosition());
            this.codeOut.skip(tries.length * 8);
            transformTries(triesSection, tries, transformCatchHandlers(indexMap2, catchHandlers));
        }
    }

    private int[] transformCatchHandlers(IndexMap indexMap2, Code.CatchHandler[] catchHandlers) {
        int baseOffset = this.codeOut.getPosition();
        this.codeOut.writeUleb128(catchHandlers.length);
        int[] offsets = new int[catchHandlers.length];
        for (int i = 0; i < catchHandlers.length; i++) {
            offsets[i] = this.codeOut.getPosition() - baseOffset;
            transformEncodedCatchHandler(catchHandlers[i], indexMap2);
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

    private void transformDebugInfoItem(Dex.Section in, IndexMap indexMap2) {
        this.contentsOut.debugInfos.size++;
        this.debugInfoOut.writeUleb128(in.readUleb128());
        int parametersSize = in.readUleb128();
        this.debugInfoOut.writeUleb128(parametersSize);
        for (int p = 0; p < parametersSize; p++) {
            this.debugInfoOut.writeUleb128p1(indexMap2.adjustString(in.readUleb128p1()));
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
                    this.debugInfoOut.writeUleb128p1(indexMap2.adjustString(in.readUleb128p1()));
                    this.debugInfoOut.writeUleb128p1(indexMap2.adjustType(in.readUleb128p1()));
                    if (opcode != 4) {
                        break;
                    } else {
                        this.debugInfoOut.writeUleb128p1(indexMap2.adjustString(in.readUleb128p1()));
                        break;
                    }
                case 5:
                case 6:
                    this.debugInfoOut.writeUleb128(in.readUleb128());
                    break;
                case 9:
                    this.debugInfoOut.writeUleb128p1(indexMap2.adjustString(in.readUleb128p1()));
                    break;
            }
        }
    }

    private void transformEncodedCatchHandler(Code.CatchHandler catchHandler, IndexMap indexMap2) {
        int catchAllAddress = catchHandler.getCatchAllAddress();
        int[] typeIndexes = catchHandler.getTypeIndexes();
        int[] addresses = catchHandler.getAddresses();
        if (catchAllAddress != -1) {
            this.codeOut.writeSleb128(-typeIndexes.length);
        } else {
            this.codeOut.writeSleb128(typeIndexes.length);
        }
        for (int i = 0; i < typeIndexes.length; i++) {
            this.codeOut.writeUleb128(indexMap2.adjustType(typeIndexes[i]));
            this.codeOut.writeUleb128(addresses[i]);
        }
        if (catchAllAddress != -1) {
            this.codeOut.writeUleb128(catchAllAddress);
        }
    }

    private void transformStaticValues(Dex.Section in, IndexMap indexMap2) {
        this.contentsOut.encodedArrays.size++;
        indexMap2.putStaticValuesOffset(in.getPosition(), this.encodedArrayOut.getPosition());
        indexMap2.adjustEncodedArray(in.readEncodedArray()).writeTo(this.encodedArrayOut);
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

        public WriterSizes(Dex dex, boolean exact) {
            plus(dex.getTableOfContents(), exact);
            fourByteAlign();
        }

        public WriterSizes(DexTransform dexTransform) {
            this.header = dexTransform.headerOut.used();
            this.idsDefs = dexTransform.idsDefsOut.used();
            this.mapList = dexTransform.mapListOut.used();
            this.typeList = dexTransform.typeListOut.used();
            this.classData = dexTransform.classDataOut.used();
            this.code = dexTransform.codeOut.used();
            this.stringData = dexTransform.stringDataOut.used();
            this.debugInfo = dexTransform.debugInfoOut.used();
            this.encodedArray = dexTransform.encodedArrayOut.used();
            this.annotationsDirectory = dexTransform.annotationsDirectoryOut.used();
            this.annotationsSet = dexTransform.annotationSetOut.used();
            this.annotationsSetRefList = dexTransform.annotationSetRefListOut.used();
            this.annotation = dexTransform.annotationOut.used();
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

    class DexItem {
        int index;
        int newOffset;
        int offset;

        public DexItem(int offset2, int index2, int newOffset2) {
            this.offset = offset2;
            this.index = index2;
            this.newOffset = newOffset2;
        }
    }
}
