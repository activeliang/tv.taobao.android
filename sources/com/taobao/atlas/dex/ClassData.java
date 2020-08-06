package com.taobao.atlas.dex;

public final class ClassData {
    private final Method[] directMethods;
    private final Field[] instanceFields;
    private final Field[] staticFields;
    private final Method[] virtualMethods;

    public ClassData(Field[] staticFields2, Field[] instanceFields2, Method[] directMethods2, Method[] virtualMethods2) {
        this.staticFields = staticFields2;
        this.instanceFields = instanceFields2;
        this.directMethods = directMethods2;
        this.virtualMethods = virtualMethods2;
    }

    public Field[] getStaticFields() {
        return this.staticFields;
    }

    public Field[] getInstanceFields() {
        return this.instanceFields;
    }

    public Method[] getDirectMethods() {
        return this.directMethods;
    }

    public Method[] getVirtualMethods() {
        return this.virtualMethods;
    }

    public Field[] allFields() {
        Field[] result = new Field[(this.staticFields.length + this.instanceFields.length)];
        System.arraycopy(this.staticFields, 0, result, 0, this.staticFields.length);
        System.arraycopy(this.instanceFields, 0, result, this.staticFields.length, this.instanceFields.length);
        return result;
    }

    public Method[] allMethods() {
        Method[] result = new Method[(this.directMethods.length + this.virtualMethods.length)];
        System.arraycopy(this.directMethods, 0, result, 0, this.directMethods.length);
        System.arraycopy(this.virtualMethods, 0, result, this.directMethods.length, this.virtualMethods.length);
        return result;
    }

    public static class Field {
        private final int accessFlags;
        private final int fieldIndex;

        public Field(int fieldIndex2, int accessFlags2) {
            this.fieldIndex = fieldIndex2;
            this.accessFlags = accessFlags2;
        }

        public int getFieldIndex() {
            return this.fieldIndex;
        }

        public int getAccessFlags() {
            return this.accessFlags;
        }
    }

    public static class Method {
        private final int accessFlags;
        private final int codeOffset;
        private final int methodIndex;

        public Method(int methodIndex2, int accessFlags2, int codeOffset2) {
            this.methodIndex = methodIndex2;
            this.accessFlags = accessFlags2;
            this.codeOffset = codeOffset2;
        }

        public int getMethodIndex() {
            return this.methodIndex;
        }

        public int getAccessFlags() {
            return this.accessFlags;
        }

        public int getCodeOffset() {
            return this.codeOffset;
        }
    }
}
