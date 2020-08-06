package com.taobao.atlas.dex;

import com.taobao.atlas.dex.ClassData;
import com.taobao.atlas.dex.Code;
import com.taobao.atlas.dex.util.ByteInput;
import com.taobao.atlas.dex.util.ByteOutput;
import com.taobao.atlas.dex.util.FileUtils;
import com.taobao.atlas.dexmerge.MergeConstants;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.UTFDataFormatException;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.AbstractList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.RandomAccess;
import java.util.zip.Adler32;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;
import org.apache.commons.codec.digest.MessageDigestAlgorithms;

public final class Dex {
    private static final int CHECKSUM_OFFSET = 8;
    private static final int CHECKSUM_SIZE = 4;
    static final short[] EMPTY_SHORT_ARRAY = new short[0];
    private static final int SIGNATURE_OFFSET = 12;
    private static final int SIGNATURE_SIZE = 20;
    private ByteBuffer data;
    private final FieldIdTable fieldIds;
    private final MethodIdTable methodIds;
    private int nextSectionStart;
    private final ProtoIdTable protoIds;
    /* access modifiers changed from: private */
    public final StringTable strings;
    /* access modifiers changed from: private */
    public final TableOfContents tableOfContents;
    private final TypeIndexToDescriptorIndexTable typeIds;
    private final TypeIndexToDescriptorTable typeNames;

    public Dex(byte[] data2) throws IOException {
        this(ByteBuffer.wrap(data2));
    }

    private Dex(ByteBuffer data2) throws IOException {
        this.tableOfContents = new TableOfContents();
        this.nextSectionStart = 0;
        this.strings = new StringTable();
        this.typeIds = new TypeIndexToDescriptorIndexTable();
        this.typeNames = new TypeIndexToDescriptorTable();
        this.protoIds = new ProtoIdTable();
        this.fieldIds = new FieldIdTable();
        this.methodIds = new MethodIdTable();
        this.data = data2;
        this.data.order(ByteOrder.LITTLE_ENDIAN);
        this.tableOfContents.readFrom(this);
    }

    public Dex(int byteCount) throws IOException {
        this.tableOfContents = new TableOfContents();
        this.nextSectionStart = 0;
        this.strings = new StringTable();
        this.typeIds = new TypeIndexToDescriptorIndexTable();
        this.typeNames = new TypeIndexToDescriptorTable();
        this.protoIds = new ProtoIdTable();
        this.fieldIds = new FieldIdTable();
        this.methodIds = new MethodIdTable();
        this.data = ByteBuffer.wrap(new byte[byteCount]);
        this.data.order(ByteOrder.LITTLE_ENDIAN);
    }

    public Dex(InputStream in) throws IOException {
        this.tableOfContents = new TableOfContents();
        this.nextSectionStart = 0;
        this.strings = new StringTable();
        this.typeIds = new TypeIndexToDescriptorIndexTable();
        this.typeNames = new TypeIndexToDescriptorTable();
        this.protoIds = new ProtoIdTable();
        this.fieldIds = new FieldIdTable();
        this.methodIds = new MethodIdTable();
        try {
            loadFrom(in);
        } finally {
            in.close();
        }
    }

    public Dex(File file) throws IOException {
        this.tableOfContents = new TableOfContents();
        this.nextSectionStart = 0;
        this.strings = new StringTable();
        this.typeIds = new TypeIndexToDescriptorIndexTable();
        this.typeNames = new TypeIndexToDescriptorTable();
        this.protoIds = new ProtoIdTable();
        this.fieldIds = new FieldIdTable();
        this.methodIds = new MethodIdTable();
        if (FileUtils.hasArchiveSuffix(file.getName())) {
            ZipFile zipFile = new ZipFile(file);
            ZipEntry entry = zipFile.getEntry(DexFormat.DEX_IN_JAR_NAME);
            if (entry != null) {
                loadFrom(zipFile.getInputStream(entry));
                zipFile.close();
                return;
            }
            throw new DexException2("Expected classes.dex in " + file);
        } else if (file.getName().endsWith(MergeConstants.DEX_SUFFIX)) {
            loadFrom(new FileInputStream(file));
        } else {
            throw new DexException2("unknown output extension: " + file);
        }
    }

    public static Dex create(ByteBuffer data2) throws IOException {
        data2.order(ByteOrder.LITTLE_ENDIAN);
        if (data2.get(0) == 100 && data2.get(1) == 101 && data2.get(2) == 121 && data2.get(3) == 10) {
            data2.position(8);
            int offset = data2.getInt();
            int length = data2.getInt();
            data2.position(offset);
            data2.limit(offset + length);
            data2 = data2.slice();
        }
        return new Dex(data2);
    }

    private void loadFrom(InputStream in) throws IOException {
        ByteArrayOutputStream bytesOut = new ByteArrayOutputStream();
        byte[] buffer = new byte[8192];
        while (true) {
            int count = in.read(buffer);
            if (count != -1) {
                bytesOut.write(buffer, 0, count);
            } else {
                this.data = ByteBuffer.wrap(bytesOut.toByteArray());
                this.data.order(ByteOrder.LITTLE_ENDIAN);
                this.tableOfContents.readFrom(this);
                return;
            }
        }
    }

    /* access modifiers changed from: private */
    public static void checkBounds(int index, int length) {
        if (index < 0 || index >= length) {
            throw new IndexOutOfBoundsException("index:" + index + ", length=" + length);
        }
    }

    public void writeTo(OutputStream out) throws IOException {
        byte[] buffer = new byte[8192];
        ByteBuffer data2 = this.data.duplicate();
        data2.clear();
        while (data2.hasRemaining()) {
            int count = Math.min(buffer.length, data2.remaining());
            data2.get(buffer, 0, count);
            out.write(buffer, 0, count);
        }
    }

    public void writeTo(File dexOut) throws IOException {
        OutputStream out = new FileOutputStream(dexOut);
        writeTo(out);
        out.close();
    }

    public TableOfContents getTableOfContents() {
        return this.tableOfContents;
    }

    public Section open(int position) {
        if (position < 0 || position >= this.data.capacity()) {
            throw new IllegalArgumentException("position=" + position + " length=" + this.data.capacity());
        }
        ByteBuffer sectionData = this.data.duplicate();
        sectionData.order(ByteOrder.LITTLE_ENDIAN);
        sectionData.position(position);
        sectionData.limit(this.data.capacity());
        return new Section("section", sectionData);
    }

    public Section appendSection(int maxByteCount, String name) {
        if ((maxByteCount & 3) != 0) {
            throw new IllegalStateException("Not four byte aligned!");
        }
        int limit = this.nextSectionStart + maxByteCount;
        ByteBuffer sectionData = this.data.duplicate();
        sectionData.order(ByteOrder.LITTLE_ENDIAN);
        sectionData.position(this.nextSectionStart);
        sectionData.limit(limit);
        Section result = new Section(name, sectionData);
        this.nextSectionStart = limit;
        return result;
    }

    public int getLength() {
        return this.data.capacity();
    }

    public int getNextSectionStart() {
        return this.nextSectionStart;
    }

    public byte[] getBytes() {
        ByteBuffer data2 = this.data.duplicate();
        byte[] result = new byte[data2.capacity()];
        data2.position(0);
        data2.get(result);
        return result;
    }

    public List<String> strings() {
        return this.strings;
    }

    public List<Integer> typeIds() {
        return this.typeIds;
    }

    public List<String> typeNames() {
        return this.typeNames;
    }

    public List<ProtoId> protoIds() {
        return this.protoIds;
    }

    public List<FieldId> fieldIds() {
        return this.fieldIds;
    }

    public List<MethodId> methodIds() {
        return this.methodIds;
    }

    public Iterable<ClassDef> classDefs() {
        return new ClassDefIterable();
    }

    public TypeList readTypeList(int offset) {
        if (offset == 0) {
            return TypeList.EMPTY;
        }
        return open(offset).readTypeList();
    }

    public ClassData readClassData(ClassDef classDef) {
        int offset = classDef.getClassDataOffset();
        if (offset != 0) {
            return open(offset).readClassData();
        }
        throw new IllegalArgumentException("offset == 0");
    }

    public Code readCode(ClassData.Method method) {
        int offset = method.getCodeOffset();
        if (offset != 0) {
            return open(offset).readCode();
        }
        throw new IllegalArgumentException("offset == 0");
    }

    public byte[] computeSignature() throws IOException {
        try {
            MessageDigest digest = MessageDigest.getInstance(MessageDigestAlgorithms.SHA_1);
            byte[] buffer = new byte[8192];
            ByteBuffer data2 = this.data.duplicate();
            data2.limit(data2.capacity());
            data2.position(32);
            while (data2.hasRemaining()) {
                int count = Math.min(buffer.length, data2.remaining());
                data2.get(buffer, 0, count);
                digest.update(buffer, 0, count);
            }
            return digest.digest();
        } catch (NoSuchAlgorithmException e) {
            throw new AssertionError();
        }
    }

    public int computeChecksum() throws IOException {
        Adler32 adler32 = new Adler32();
        byte[] buffer = new byte[8192];
        ByteBuffer data2 = this.data.duplicate();
        data2.limit(data2.capacity());
        data2.position(12);
        while (data2.hasRemaining()) {
            int count = Math.min(buffer.length, data2.remaining());
            data2.get(buffer, 0, count);
            adler32.update(buffer, 0, count);
        }
        return (int) adler32.getValue();
    }

    public void writeHashes() throws IOException {
        open(12).write(computeSignature());
        open(8).writeInt(computeChecksum());
    }

    public int nameIndexFromFieldIndex(int fieldIndex) {
        checkBounds(fieldIndex, this.tableOfContents.fieldIds.size);
        return this.data.getInt(this.tableOfContents.fieldIds.off + (fieldIndex * 8) + 2 + 2);
    }

    public int findStringIndex(String s) {
        return Collections.binarySearch(this.strings, s);
    }

    public int findTypeIndex(String descriptor) {
        return Collections.binarySearch(this.typeNames, descriptor);
    }

    public int findFieldIndex(FieldId fieldId) {
        return Collections.binarySearch(this.fieldIds, fieldId);
    }

    public int findMethodIndex(MethodId methodId) {
        return Collections.binarySearch(this.methodIds, methodId);
    }

    public int findClassDefIndexFromTypeIndex(int typeIndex) {
        checkBounds(typeIndex, this.tableOfContents.typeIds.size);
        if (!this.tableOfContents.classDefs.exists()) {
            return -1;
        }
        for (int i = 0; i < this.tableOfContents.classDefs.size; i++) {
            if (typeIndexFromClassDefIndex(i) == typeIndex) {
                return i;
            }
        }
        return -1;
    }

    public int typeIndexFromFieldIndex(int fieldIndex) {
        checkBounds(fieldIndex, this.tableOfContents.fieldIds.size);
        return this.data.getShort(this.tableOfContents.fieldIds.off + (fieldIndex * 8) + 2) & 65535;
    }

    public int declaringClassIndexFromMethodIndex(int methodIndex) {
        checkBounds(methodIndex, this.tableOfContents.methodIds.size);
        return this.data.getShort(this.tableOfContents.methodIds.off + (methodIndex * 8)) & 65535;
    }

    public int nameIndexFromMethodIndex(int methodIndex) {
        checkBounds(methodIndex, this.tableOfContents.methodIds.size);
        return this.data.getInt(this.tableOfContents.methodIds.off + (methodIndex * 8) + 2 + 2);
    }

    public short[] parameterTypeIndicesFromMethodIndex(int methodIndex) {
        checkBounds(methodIndex, this.tableOfContents.methodIds.size);
        int protoIndex = this.data.getShort(this.tableOfContents.methodIds.off + (methodIndex * 8) + 2) & 65535;
        checkBounds(protoIndex, this.tableOfContents.protoIds.size);
        int parametersOffset = this.data.getInt(this.tableOfContents.protoIds.off + (protoIndex * 12) + 4 + 4);
        if (parametersOffset == 0) {
            return EMPTY_SHORT_ARRAY;
        }
        int position = parametersOffset;
        int size = this.data.getInt(position);
        if (size <= 0) {
            throw new AssertionError("Unexpected parameter type list size: " + size);
        }
        int position2 = position + 4;
        short[] types = new short[size];
        for (int i = 0; i < size; i++) {
            types[i] = this.data.getShort(position2);
            position2 += 2;
        }
        return types;
    }

    public int returnTypeIndexFromMethodIndex(int methodIndex) {
        checkBounds(methodIndex, this.tableOfContents.methodIds.size);
        int protoIndex = this.data.getShort(this.tableOfContents.methodIds.off + (methodIndex * 8) + 2) & 65535;
        checkBounds(protoIndex, this.tableOfContents.protoIds.size);
        return this.data.getInt(this.tableOfContents.protoIds.off + (protoIndex * 12) + 4);
    }

    public int descriptorIndexFromTypeIndex(int typeIndex) {
        checkBounds(typeIndex, this.tableOfContents.typeIds.size);
        return this.data.getInt(this.tableOfContents.typeIds.off + (typeIndex * 4));
    }

    public int typeIndexFromClassDefIndex(int classDefIndex) {
        checkBounds(classDefIndex, this.tableOfContents.classDefs.size);
        return this.data.getInt(this.tableOfContents.classDefs.off + (classDefIndex * 32));
    }

    public int annotationDirectoryOffsetFromClassDefIndex(int classDefIndex) {
        checkBounds(classDefIndex, this.tableOfContents.classDefs.size);
        return this.data.getInt(this.tableOfContents.classDefs.off + (classDefIndex * 32) + 4 + 4 + 4 + 4 + 4);
    }

    public short[] interfaceTypeIndicesFromClassDefIndex(int classDefIndex) {
        checkBounds(classDefIndex, this.tableOfContents.classDefs.size);
        int interfacesOffset = this.data.getInt(this.tableOfContents.classDefs.off + (classDefIndex * 32) + 4 + 4 + 4);
        if (interfacesOffset == 0) {
            return EMPTY_SHORT_ARRAY;
        }
        int position = interfacesOffset;
        int size = this.data.getInt(position);
        if (size <= 0) {
            throw new AssertionError("Unexpected interfaces list size: " + size);
        }
        int position2 = position + 4;
        short[] types = new short[size];
        for (int i = 0; i < size; i++) {
            types[i] = this.data.getShort(position2);
            position2 += 2;
        }
        return types;
    }

    public final class Section implements ByteInput, ByteOutput {
        private final ByteBuffer data;
        private final int initialPosition;
        private final String name;

        private Section(String name2, ByteBuffer data2) {
            this.name = name2;
            this.data = data2;
            this.initialPosition = data2.position();
        }

        public ByteBuffer getData() {
            return this.data;
        }

        public int getPosition() {
            return this.data.position();
        }

        public int readInt() {
            return this.data.getInt();
        }

        public short readShort() {
            return this.data.getShort();
        }

        public int readUnsignedShort() {
            return readShort() & 65535;
        }

        public byte readByte() {
            return this.data.get();
        }

        public byte[] readByteArray(int length) {
            byte[] result = new byte[length];
            this.data.get(result);
            return result;
        }

        public short[] readShortArray(int length) {
            if (length == 0) {
                return Dex.EMPTY_SHORT_ARRAY;
            }
            short[] result = new short[length];
            for (int i = 0; i < length; i++) {
                result[i] = readShort();
            }
            return result;
        }

        public int readUleb128() {
            return Leb128.readUnsignedLeb128(this);
        }

        public int readUleb128p1() {
            return Leb128.readUnsignedLeb128(this) - 1;
        }

        public int readSleb128() {
            return Leb128.readSignedLeb128(this);
        }

        public void writeUleb128p1(int i) {
            writeUleb128(i + 1);
        }

        public TypeList readTypeList() {
            short[] types = readShortArray(readInt());
            alignToFourBytes();
            return new TypeList(Dex.this, types);
        }

        public String readString() {
            int offset = readInt();
            int savedPosition = this.data.position();
            int savedLimit = this.data.limit();
            this.data.position(offset);
            this.data.limit(this.data.capacity());
            try {
                int expectedLength = readUleb128();
                String result = Mutf8.decode(this, new char[expectedLength]);
                if (result.length() != expectedLength) {
                    throw new DexException2("Declared length " + expectedLength + " doesn't match decoded length of " + result.length());
                }
                this.data.position(savedPosition);
                this.data.limit(savedLimit);
                return result;
            } catch (UTFDataFormatException e) {
                throw new DexException2((Throwable) e);
            } catch (Throwable th) {
                this.data.position(savedPosition);
                this.data.limit(savedLimit);
                throw th;
            }
        }

        public FieldId readFieldId() {
            return new FieldId(Dex.this, readUnsignedShort(), readUnsignedShort(), readInt());
        }

        public MethodId readMethodId() {
            return new MethodId(Dex.this, readUnsignedShort(), readUnsignedShort(), readInt());
        }

        public ProtoId readProtoId() {
            return new ProtoId(Dex.this, readInt(), readInt(), readInt());
        }

        public ClassDef readClassDef() {
            return new ClassDef(Dex.this, getPosition(), readInt(), readInt(), readInt(), readInt(), readInt(), readInt(), readInt(), readInt());
        }

        /* access modifiers changed from: private */
        public Code readCode() {
            Code.Try[] tries;
            Code.CatchHandler[] catchHandlers;
            int registersSize = readUnsignedShort();
            int insSize = readUnsignedShort();
            int outsSize = readUnsignedShort();
            int triesSize = readUnsignedShort();
            int debugInfoOffset = readInt();
            short[] instructions = readShortArray(readInt());
            if (triesSize > 0) {
                if (instructions.length % 2 == 1) {
                    readShort();
                }
                Section triesSection = Dex.this.open(this.data.position());
                skip(triesSize * 8);
                catchHandlers = readCatchHandlers();
                tries = triesSection.readTries(triesSize, catchHandlers);
            } else {
                tries = new Code.Try[0];
                catchHandlers = new Code.CatchHandler[0];
            }
            return new Code(registersSize, insSize, outsSize, debugInfoOffset, instructions, tries, catchHandlers);
        }

        private Code.CatchHandler[] readCatchHandlers() {
            int baseOffset = this.data.position();
            int catchHandlersSize = readUleb128();
            Code.CatchHandler[] result = new Code.CatchHandler[catchHandlersSize];
            for (int i = 0; i < catchHandlersSize; i++) {
                result[i] = readCatchHandler(this.data.position() - baseOffset);
            }
            return result;
        }

        private Code.Try[] readTries(int triesSize, Code.CatchHandler[] catchHandlers) {
            Code.Try[] result = new Code.Try[triesSize];
            for (int i = 0; i < triesSize; i++) {
                result[i] = new Code.Try(readInt(), readUnsignedShort(), findCatchHandlerIndex(catchHandlers, readUnsignedShort()));
            }
            return result;
        }

        private int findCatchHandlerIndex(Code.CatchHandler[] catchHandlers, int offset) {
            for (int i = 0; i < catchHandlers.length; i++) {
                if (catchHandlers[i].getOffset() == offset) {
                    return i;
                }
            }
            throw new IllegalArgumentException();
        }

        private Code.CatchHandler readCatchHandler(int offset) {
            int size = readSleb128();
            int handlersCount = Math.abs(size);
            int[] typeIndexes = new int[handlersCount];
            int[] addresses = new int[handlersCount];
            for (int i = 0; i < handlersCount; i++) {
                typeIndexes[i] = readUleb128();
                addresses[i] = readUleb128();
            }
            return new Code.CatchHandler(typeIndexes, addresses, size <= 0 ? readUleb128() : -1, offset);
        }

        /* access modifiers changed from: private */
        public ClassData readClassData() {
            return new ClassData(readFields(readUleb128()), readFields(readUleb128()), readMethods(readUleb128()), readMethods(readUleb128()));
        }

        private ClassData.Field[] readFields(int count) {
            ClassData.Field[] result = new ClassData.Field[count];
            int fieldIndex = 0;
            for (int i = 0; i < count; i++) {
                fieldIndex += readUleb128();
                result[i] = new ClassData.Field(fieldIndex, readUleb128());
            }
            return result;
        }

        private ClassData.Method[] readMethods(int count) {
            ClassData.Method[] result = new ClassData.Method[count];
            int methodIndex = 0;
            for (int i = 0; i < count; i++) {
                methodIndex += readUleb128();
                result[i] = new ClassData.Method(methodIndex, readUleb128(), readUleb128());
            }
            return result;
        }

        private byte[] getBytesFrom(int start) {
            byte[] result = new byte[(this.data.position() - start)];
            this.data.position(start);
            this.data.get(result);
            return result;
        }

        public Annotation readAnnotation() {
            byte visibility = readByte();
            int start = this.data.position();
            new EncodedValueReader((ByteInput) this, 29).skipValue();
            return new Annotation(Dex.this, visibility, new EncodedValue(getBytesFrom(start)));
        }

        public EncodedValue readEncodedArray() {
            int start = this.data.position();
            new EncodedValueReader((ByteInput) this, 28).skipValue();
            return new EncodedValue(getBytesFrom(start));
        }

        public void skip(int count) {
            if (count < 0) {
                throw new IllegalArgumentException();
            }
            this.data.position(this.data.position() + count);
        }

        public void alignToFourBytes() {
            this.data.position((this.data.position() + 3) & -4);
        }

        public void alignToFourBytesWithZeroFill() {
            while ((this.data.position() & 3) != 0) {
                this.data.put((byte) 0);
            }
        }

        public void assertFourByteAligned() {
            if ((this.data.position() & 3) != 0) {
                throw new IllegalStateException("Not four byte aligned!");
            }
        }

        public void write(byte[] bytes) {
            this.data.put(bytes);
        }

        public void writeByte(int b) {
            this.data.put((byte) b);
        }

        public void writeShort(short i) {
            this.data.putShort(i);
        }

        public void writeUnsignedShort(int i) {
            short s = (short) i;
            if (i != (65535 & s)) {
                throw new IllegalArgumentException("Expected an unsigned short: " + i);
            }
            writeShort(s);
        }

        public void write(short[] shorts) {
            for (short s : shorts) {
                writeShort(s);
            }
        }

        public void writeInt(int i) {
            this.data.putInt(i);
        }

        public void writeUleb128(int i) {
            try {
                Leb128.writeUnsignedLeb128(this, i);
            } catch (ArrayIndexOutOfBoundsException e) {
                throw new DexException2("Section limit " + this.data.limit() + " exceeded by " + this.name);
            }
        }

        public void writeSleb128(int i) {
            try {
                Leb128.writeSignedLeb128(this, i);
            } catch (ArrayIndexOutOfBoundsException e) {
                throw new DexException2("Section limit " + this.data.limit() + " exceeded by " + this.name);
            }
        }

        public void writeStringData(String value) {
            try {
                writeUleb128(value.length());
                write(Mutf8.encode(value));
                writeByte(0);
            } catch (UTFDataFormatException e) {
                throw new AssertionError();
            }
        }

        public void writeTypeList(TypeList typeList) {
            short[] types = typeList.getTypes();
            writeInt(types.length);
            for (short type : types) {
                writeShort(type);
            }
            alignToFourBytesWithZeroFill();
        }

        public int remaining() {
            return this.data.remaining();
        }

        public int used() {
            return this.data.position() - this.initialPosition;
        }
    }

    private final class StringTable extends AbstractList<String> implements RandomAccess {
        private StringTable() {
        }

        public String get(int index) {
            Dex.checkBounds(index, Dex.this.tableOfContents.stringIds.size);
            return Dex.this.open(Dex.this.tableOfContents.stringIds.off + (index * 4)).readString();
        }

        public int size() {
            return Dex.this.tableOfContents.stringIds.size;
        }
    }

    private final class TypeIndexToDescriptorIndexTable extends AbstractList<Integer> implements RandomAccess {
        private TypeIndexToDescriptorIndexTable() {
        }

        public Integer get(int index) {
            return Integer.valueOf(Dex.this.descriptorIndexFromTypeIndex(index));
        }

        public int size() {
            return Dex.this.tableOfContents.typeIds.size;
        }
    }

    private final class TypeIndexToDescriptorTable extends AbstractList<String> implements RandomAccess {
        private TypeIndexToDescriptorTable() {
        }

        public String get(int index) {
            return Dex.this.strings.get(Dex.this.descriptorIndexFromTypeIndex(index));
        }

        public int size() {
            return Dex.this.tableOfContents.typeIds.size;
        }
    }

    private final class ProtoIdTable extends AbstractList<ProtoId> implements RandomAccess {
        private ProtoIdTable() {
        }

        public ProtoId get(int index) {
            Dex.checkBounds(index, Dex.this.tableOfContents.protoIds.size);
            return Dex.this.open(Dex.this.tableOfContents.protoIds.off + (index * 12)).readProtoId();
        }

        public int size() {
            return Dex.this.tableOfContents.protoIds.size;
        }
    }

    private final class FieldIdTable extends AbstractList<FieldId> implements RandomAccess {
        private FieldIdTable() {
        }

        public FieldId get(int index) {
            Dex.checkBounds(index, Dex.this.tableOfContents.fieldIds.size);
            return Dex.this.open(Dex.this.tableOfContents.fieldIds.off + (index * 8)).readFieldId();
        }

        public int size() {
            return Dex.this.tableOfContents.fieldIds.size;
        }
    }

    private final class MethodIdTable extends AbstractList<MethodId> implements RandomAccess {
        private MethodIdTable() {
        }

        public MethodId get(int index) {
            Dex.checkBounds(index, Dex.this.tableOfContents.methodIds.size);
            return Dex.this.open(Dex.this.tableOfContents.methodIds.off + (index * 8)).readMethodId();
        }

        public int size() {
            return Dex.this.tableOfContents.methodIds.size;
        }
    }

    private final class ClassDefIterator implements Iterator<ClassDef> {
        private int count;
        private final Section in;

        private ClassDefIterator() {
            this.in = Dex.this.open(Dex.this.tableOfContents.classDefs.off);
            this.count = 0;
        }

        public boolean hasNext() {
            return this.count < Dex.this.tableOfContents.classDefs.size;
        }

        public ClassDef next() {
            if (!hasNext()) {
                throw new NoSuchElementException();
            }
            this.count++;
            return this.in.readClassDef();
        }

        public void remove() {
            throw new UnsupportedOperationException();
        }
    }

    private final class ClassDefIterable implements Iterable<ClassDef> {
        private ClassDefIterable() {
        }

        public Iterator<ClassDef> iterator() {
            return !Dex.this.tableOfContents.classDefs.exists() ? Collections.emptySet().iterator() : new ClassDefIterator();
        }
    }
}
