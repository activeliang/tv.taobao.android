package com.alibaba.fastjson.asm;

final class Item {
    int hashCode;
    int index;
    int intVal;
    long longVal;
    Item next;
    String strVal1;
    String strVal2;
    String strVal3;
    int type;

    Item() {
    }

    Item(int index2, Item i) {
        this.index = index2;
        this.type = i.type;
        this.intVal = i.intVal;
        this.longVal = i.longVal;
        this.strVal1 = i.strVal1;
        this.strVal2 = i.strVal2;
        this.strVal3 = i.strVal3;
        this.hashCode = i.hashCode;
    }

    /* access modifiers changed from: package-private */
    public void set(int type2, String strVal12, String strVal22, String strVal32) {
        this.type = type2;
        this.strVal1 = strVal12;
        this.strVal2 = strVal22;
        this.strVal3 = strVal32;
        switch (type2) {
            case 1:
            case 7:
            case 8:
            case 13:
                this.hashCode = (strVal12.hashCode() + type2) & Integer.MAX_VALUE;
                return;
            case 12:
                this.hashCode = ((strVal12.hashCode() * strVal22.hashCode()) + type2) & Integer.MAX_VALUE;
                return;
            default:
                this.hashCode = ((strVal12.hashCode() * strVal22.hashCode() * strVal32.hashCode()) + type2) & Integer.MAX_VALUE;
                return;
        }
    }

    /* access modifiers changed from: package-private */
    public void set(int intVal2) {
        this.type = 3;
        this.intVal = intVal2;
        this.hashCode = Integer.MAX_VALUE & (this.type + intVal2);
    }

    /* access modifiers changed from: package-private */
    public boolean isEqualTo(Item i) {
        switch (this.type) {
            case 1:
            case 7:
            case 8:
            case 13:
                return i.strVal1.equals(this.strVal1);
            case 3:
            case 4:
                if (i.intVal != this.intVal) {
                    return false;
                }
                return true;
            case 5:
            case 6:
            case 15:
                if (i.longVal != this.longVal) {
                    return false;
                }
                return true;
            case 12:
                if (!i.strVal1.equals(this.strVal1) || !i.strVal2.equals(this.strVal2)) {
                    return false;
                }
                return true;
            default:
                if (!i.strVal1.equals(this.strVal1) || !i.strVal2.equals(this.strVal2) || !i.strVal3.equals(this.strVal3)) {
                    return false;
                }
                return true;
        }
    }
}
