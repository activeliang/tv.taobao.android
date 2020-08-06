package com.taobao.atlas.dexmerge.dx.io.instructions;

public abstract class BaseCodeCursor implements CodeCursor {
    private final AddressMap baseAddressMap = new AddressMap();
    private int cursor = 0;

    public final int cursor() {
        return this.cursor;
    }

    public final int baseAddressForCursor() {
        int mapped = this.baseAddressMap.get(this.cursor);
        return mapped >= 0 ? mapped : this.cursor;
    }

    public final void setBaseAddress(int targetAddress, int baseAddress) {
        this.baseAddressMap.put(targetAddress, baseAddress);
    }

    /* access modifiers changed from: protected */
    public final void advance(int amount) {
        this.cursor += amount;
    }
}
