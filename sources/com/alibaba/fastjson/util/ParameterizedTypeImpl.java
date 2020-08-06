package com.alibaba.fastjson.util;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.Arrays;

public class ParameterizedTypeImpl implements ParameterizedType {
    private final Type[] actualTypeArguments;
    private final Type ownerType;
    private final Type rawType;

    public ParameterizedTypeImpl(Type[] actualTypeArguments2, Type ownerType2, Type rawType2) {
        this.actualTypeArguments = actualTypeArguments2;
        this.ownerType = ownerType2;
        this.rawType = rawType2;
    }

    public Type[] getActualTypeArguments() {
        return this.actualTypeArguments;
    }

    public Type getOwnerType() {
        return this.ownerType;
    }

    public Type getRawType() {
        return this.rawType;
    }

    public boolean equals(Object o) {
        boolean z = true;
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        ParameterizedTypeImpl that = (ParameterizedTypeImpl) o;
        if (!Arrays.equals(this.actualTypeArguments, that.actualTypeArguments)) {
            return false;
        }
        if (this.ownerType != null) {
            if (!this.ownerType.equals(that.ownerType)) {
                return false;
            }
        } else if (that.ownerType != null) {
            return false;
        }
        if (this.rawType != null) {
            z = this.rawType.equals(that.rawType);
        } else if (that.rawType != null) {
            z = false;
        }
        return z;
    }

    public int hashCode() {
        int result;
        int i;
        int i2 = 0;
        if (this.actualTypeArguments != null) {
            result = Arrays.hashCode(this.actualTypeArguments);
        } else {
            result = 0;
        }
        int i3 = result * 31;
        if (this.ownerType != null) {
            i = this.ownerType.hashCode();
        } else {
            i = 0;
        }
        int i4 = (i3 + i) * 31;
        if (this.rawType != null) {
            i2 = this.rawType.hashCode();
        }
        return i4 + i2;
    }
}
