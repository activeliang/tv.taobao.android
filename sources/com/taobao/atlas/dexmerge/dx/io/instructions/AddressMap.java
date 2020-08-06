package com.taobao.atlas.dexmerge.dx.io.instructions;

import java.util.HashMap;

public final class AddressMap {
    private final HashMap<Integer, Integer> map = new HashMap<>();

    public int get(int keyAddress) {
        Integer value = this.map.get(Integer.valueOf(keyAddress));
        if (value == null) {
            return -1;
        }
        return value.intValue();
    }

    public void put(int keyAddress, int valueAddress) {
        this.map.put(Integer.valueOf(keyAddress), Integer.valueOf(valueAddress));
    }
}
