package com.taobao.atlas.dex.util;

import java.util.Iterator;

public interface PeekingIterator<E> extends Iterator<E> {
    E next();

    E peek();

    void remove();
}
