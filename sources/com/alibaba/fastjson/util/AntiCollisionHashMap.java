package com.alibaba.fastjson.util;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.util.AbstractCollection;
import java.util.AbstractMap;
import java.util.AbstractSet;
import java.util.Collection;
import java.util.ConcurrentModificationException;
import java.util.Iterator;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.Random;
import java.util.Set;

public class AntiCollisionHashMap<K, V> extends AbstractMap<K, V> implements Map<K, V>, Cloneable, Serializable {
    static final int DEFAULT_INITIAL_CAPACITY = 16;
    static final float DEFAULT_LOAD_FACTOR = 0.75f;
    static final int KEY = 16777619;
    static final int MAXIMUM_CAPACITY = 1073741824;
    static final int M_MASK = -2023358765;
    static final int SEED = -2128831035;
    private static final long serialVersionUID = 362498820763181265L;
    private transient Set<Map.Entry<K, V>> entrySet;
    volatile transient Set<K> keySet;
    final float loadFactor;
    volatile transient int modCount;
    final int random;
    transient int size;
    transient Entry<K, V>[] table;
    int threshold;
    volatile transient Collection<V> values;

    private int hashString(String key) {
        int hash = SEED * this.random;
        for (int i = 0; i < key.length(); i++) {
            hash = (KEY * hash) ^ key.charAt(i);
        }
        return ((hash >> 1) ^ hash) & M_MASK;
    }

    public AntiCollisionHashMap(int initialCapacity, float loadFactor2) {
        this.keySet = null;
        this.values = null;
        this.random = new Random().nextInt(99999);
        this.entrySet = null;
        if (initialCapacity < 0) {
            throw new IllegalArgumentException("Illegal initial capacity: " + initialCapacity);
        }
        initialCapacity = initialCapacity > 1073741824 ? 1073741824 : initialCapacity;
        if (loadFactor2 <= 0.0f || Float.isNaN(loadFactor2)) {
            throw new IllegalArgumentException("Illegal load factor: " + loadFactor2);
        }
        int capacity = 1;
        while (capacity < initialCapacity) {
            capacity <<= 1;
        }
        this.loadFactor = loadFactor2;
        this.threshold = (int) (((float) capacity) * loadFactor2);
        this.table = new Entry[capacity];
        init();
    }

    public AntiCollisionHashMap(int initialCapacity) {
        this(initialCapacity, DEFAULT_LOAD_FACTOR);
    }

    public AntiCollisionHashMap() {
        this.keySet = null;
        this.values = null;
        this.random = new Random().nextInt(99999);
        this.entrySet = null;
        this.loadFactor = DEFAULT_LOAD_FACTOR;
        this.threshold = 12;
        this.table = new Entry[16];
        init();
    }

    public AntiCollisionHashMap(Map<? extends K, ? extends V> m) {
        this(Math.max(((int) (((float) m.size()) / DEFAULT_LOAD_FACTOR)) + 1, 16), DEFAULT_LOAD_FACTOR);
        putAllForCreate(m);
    }

    /* access modifiers changed from: package-private */
    public void init() {
    }

    static int hash(int h) {
        int h2 = h * h;
        int h3 = h2 ^ ((h2 >>> 20) ^ (h2 >>> 12));
        return ((h3 >>> 7) ^ h3) ^ (h3 >>> 4);
    }

    static int indexFor(int h, int length) {
        return (length - 1) & h;
    }

    public int size() {
        return this.size;
    }

    public boolean isEmpty() {
        return this.size == 0;
    }

    public V get(Object key) {
        int hash;
        Object k;
        if (key == null) {
            return getForNullKey();
        }
        if (key instanceof String) {
            hash = hash(hashString((String) key));
        } else {
            hash = hash(key.hashCode());
        }
        for (Entry<K, V> e = this.table[indexFor(hash, this.table.length)]; e != null; e = e.next) {
            if (e.hash == hash && ((k = e.key) == key || key.equals(k))) {
                return e.value;
            }
        }
        return null;
    }

    private V getForNullKey() {
        for (Entry<K, V> e = this.table[0]; e != null; e = e.next) {
            if (e.key == null) {
                return e.value;
            }
        }
        return null;
    }

    public boolean containsKey(Object key) {
        return getEntry(key) != null;
    }

    /* access modifiers changed from: package-private */
    public final Entry<K, V> getEntry(Object key) {
        int hash;
        if (key == null) {
            hash = 0;
        } else if (key instanceof String) {
            hash = hash(hashString((String) key));
        } else {
            hash = hash(key.hashCode());
        }
        for (Entry<K, V> e = this.table[indexFor(hash, this.table.length)]; e != null; e = e.next) {
            if (e.hash == hash) {
                Object k = e.key;
                if (k == key) {
                    return e;
                }
                if (key != null && key.equals(k)) {
                    return e;
                }
            }
        }
        return null;
    }

    public V put(K key, V value) {
        int hash;
        Object k;
        if (key == null) {
            return putForNullKey(value);
        }
        if (key instanceof String) {
            hash = hash(hashString((String) key));
        } else {
            hash = hash(key.hashCode());
        }
        int i = indexFor(hash, this.table.length);
        Entry<K, V> e = this.table[i];
        while (e != null) {
            if (e.hash != hash || ((k = e.key) != key && !key.equals(k))) {
                e = e.next;
            } else {
                V v = e.value;
                e.value = value;
                e.recordAccess(this);
                return v;
            }
        }
        this.modCount++;
        addEntry(hash, key, value, i);
        return null;
    }

    private V putForNullKey(V value) {
        for (Entry<K, V> e = this.table[0]; e != null; e = e.next) {
            if (e.key == null) {
                V oldValue = e.value;
                e.value = value;
                e.recordAccess(this);
                return oldValue;
            }
        }
        this.modCount++;
        addEntry(0, (Object) null, value, 0);
        return null;
    }

    private void putForCreate(K key, V value) {
        int hash;
        Object k;
        if (key == null) {
            hash = 0;
        } else if (key instanceof String) {
            hash = hash(hashString((String) key));
        } else {
            hash = hash(key.hashCode());
        }
        int i = indexFor(hash, this.table.length);
        Entry<K, V> e = this.table[i];
        while (e != null) {
            if (e.hash != hash || ((k = e.key) != key && (key == null || !key.equals(k)))) {
                e = e.next;
            } else {
                e.value = value;
                return;
            }
        }
        createEntry(hash, key, value, i);
    }

    private void putAllForCreate(Map<? extends K, ? extends V> m) {
        for (Map.Entry<? extends K, ? extends V> e : m.entrySet()) {
            putForCreate(e.getKey(), e.getValue());
        }
    }

    /* access modifiers changed from: package-private */
    public void resize(int newCapacity) {
        if (this.table.length == 1073741824) {
            this.threshold = Integer.MAX_VALUE;
            return;
        }
        Entry<K, V>[] newTable = new Entry[newCapacity];
        transfer(newTable);
        this.table = newTable;
        this.threshold = (int) (((float) newCapacity) * this.loadFactor);
    }

    /* access modifiers changed from: package-private */
    public void transfer(Entry[] newTable) {
        Entry<K, V>[] src = this.table;
        int newCapacity = newTable.length;
        for (int j = 0; j < src.length; j++) {
            Entry<K, V> e = src[j];
            if (e != null) {
                src[j] = null;
                do {
                    Entry<K, V> next = e.next;
                    int i = indexFor(e.hash, newCapacity);
                    e.next = newTable[i];
                    newTable[i] = e;
                    e = next;
                } while (e != null);
            }
        }
    }

    public void putAll(Map<? extends K, ? extends V> m) {
        int numKeysToBeAdded = m.size();
        if (numKeysToBeAdded != 0) {
            if (numKeysToBeAdded > this.threshold) {
                int targetCapacity = (int) ((((float) numKeysToBeAdded) / this.loadFactor) + 1.0f);
                if (targetCapacity > 1073741824) {
                    targetCapacity = 1073741824;
                }
                int newCapacity = this.table.length;
                while (newCapacity < targetCapacity) {
                    newCapacity <<= 1;
                }
                if (newCapacity > this.table.length) {
                    resize(newCapacity);
                }
            }
            for (Map.Entry<? extends K, ? extends V> e : m.entrySet()) {
                put(e.getKey(), e.getValue());
            }
        }
    }

    public V remove(Object key) {
        Entry<K, V> e = removeEntryForKey(key);
        if (e == null) {
            return null;
        }
        return e.value;
    }

    /* access modifiers changed from: package-private */
    /* JADX WARNING: Code restructure failed: missing block: B:11:0x0023, code lost:
        r7.modCount++;
        r7.size--;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:12:0x002f, code lost:
        if (r5 != r0) goto L_0x0052;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:13:0x0031, code lost:
        r7.table[r2] = r4;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:14:0x0035, code lost:
        r0.recordRemoval(r7);
     */
    /* JADX WARNING: Code restructure failed: missing block: B:20:0x0052, code lost:
        r5.next = r4;
     */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public final com.alibaba.fastjson.util.AntiCollisionHashMap.Entry<K, V> removeEntryForKey(java.lang.Object r8) {
        /*
            r7 = this;
            if (r8 != 0) goto L_0x0039
            r1 = 0
        L_0x0003:
            com.alibaba.fastjson.util.AntiCollisionHashMap$Entry<K, V>[] r6 = r7.table
            int r6 = r6.length
            int r2 = indexFor(r1, r6)
            com.alibaba.fastjson.util.AntiCollisionHashMap$Entry<K, V>[] r6 = r7.table
            r5 = r6[r2]
            r0 = r5
        L_0x000f:
            if (r0 == 0) goto L_0x0038
            com.alibaba.fastjson.util.AntiCollisionHashMap$Entry<K, V> r4 = r0.next
            int r6 = r0.hash
            if (r6 != r1) goto L_0x0055
            K r3 = r0.key
            if (r3 == r8) goto L_0x0023
            if (r8 == 0) goto L_0x0055
            boolean r6 = r8.equals(r3)
            if (r6 == 0) goto L_0x0055
        L_0x0023:
            int r6 = r7.modCount
            int r6 = r6 + 1
            r7.modCount = r6
            int r6 = r7.size
            int r6 = r6 + -1
            r7.size = r6
            if (r5 != r0) goto L_0x0052
            com.alibaba.fastjson.util.AntiCollisionHashMap$Entry<K, V>[] r6 = r7.table
            r6[r2] = r4
        L_0x0035:
            r0.recordRemoval(r7)
        L_0x0038:
            return r0
        L_0x0039:
            boolean r6 = r8 instanceof java.lang.String
            if (r6 == 0) goto L_0x0049
            r6 = r8
            java.lang.String r6 = (java.lang.String) r6
            int r6 = r7.hashString(r6)
            int r1 = hash(r6)
            goto L_0x0003
        L_0x0049:
            int r6 = r8.hashCode()
            int r1 = hash(r6)
            goto L_0x0003
        L_0x0052:
            r5.next = r4
            goto L_0x0035
        L_0x0055:
            r5 = r0
            r0 = r4
            goto L_0x000f
        */
        throw new UnsupportedOperationException("Method not decompiled: com.alibaba.fastjson.util.AntiCollisionHashMap.removeEntryForKey(java.lang.Object):com.alibaba.fastjson.util.AntiCollisionHashMap$Entry");
    }

    /* access modifiers changed from: package-private */
    public final Entry<K, V> removeMapping(Object o) {
        int hash;
        if (!(o instanceof Map.Entry)) {
            return null;
        }
        Map.Entry<K, V> entry = (Map.Entry) o;
        Object key = entry.getKey();
        if (key == null) {
            hash = 0;
        } else if (key instanceof String) {
            hash = hash(hashString((String) key));
        } else {
            hash = hash(key.hashCode());
        }
        int i = indexFor(hash, this.table.length);
        Entry<K, V> prev = this.table[i];
        Entry<K, V> e = prev;
        while (e != null) {
            Entry<K, V> next = e.next;
            if (e.hash != hash || !e.equals(entry)) {
                prev = e;
                e = next;
            } else {
                this.modCount++;
                this.size--;
                if (prev == e) {
                    this.table[i] = next;
                } else {
                    prev.next = next;
                }
                e.recordRemoval(this);
                return e;
            }
        }
        return e;
    }

    public void clear() {
        this.modCount++;
        Entry[] tab = this.table;
        for (int i = 0; i < tab.length; i++) {
            tab[i] = null;
        }
        this.size = 0;
    }

    public boolean containsValue(Object value) {
        if (value == null) {
            return containsNullValue();
        }
        Entry[] tab = this.table;
        for (Entry e : tab) {
            while (e != null) {
                if (value.equals(e.value)) {
                    return true;
                }
                e = e.next;
            }
        }
        return false;
    }

    private boolean containsNullValue() {
        Entry[] tab = this.table;
        for (Entry e : tab) {
            while (e != null) {
                if (e.value == null) {
                    return true;
                }
                e = e.next;
            }
        }
        return false;
    }

    /* JADX DEBUG: Multi-variable search result rejected for TypeSearchVarInfo{r2v5, resolved type: java.lang.Object} */
    /* JADX DEBUG: Multi-variable search result rejected for TypeSearchVarInfo{r0v0, resolved type: com.alibaba.fastjson.util.AntiCollisionHashMap} */
    /* JADX WARNING: Multi-variable type inference failed */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public java.lang.Object clone() {
        /*
            r4 = this;
            r3 = 0
            r1 = 0
            java.lang.Object r2 = super.clone()     // Catch:{ CloneNotSupportedException -> 0x001f }
            r0 = r2
            com.alibaba.fastjson.util.AntiCollisionHashMap r0 = (com.alibaba.fastjson.util.AntiCollisionHashMap) r0     // Catch:{ CloneNotSupportedException -> 0x001f }
            r1 = r0
        L_0x000a:
            com.alibaba.fastjson.util.AntiCollisionHashMap$Entry<K, V>[] r2 = r4.table
            int r2 = r2.length
            com.alibaba.fastjson.util.AntiCollisionHashMap$Entry[] r2 = new com.alibaba.fastjson.util.AntiCollisionHashMap.Entry[r2]
            r1.table = r2
            r2 = 0
            r1.entrySet = r2
            r1.modCount = r3
            r1.size = r3
            r1.init()
            r1.putAllForCreate(r4)
            return r1
        L_0x001f:
            r2 = move-exception
            goto L_0x000a
        */
        throw new UnsupportedOperationException("Method not decompiled: com.alibaba.fastjson.util.AntiCollisionHashMap.clone():java.lang.Object");
    }

    static class Entry<K, V> implements Map.Entry<K, V> {
        final int hash;
        final K key;
        Entry<K, V> next;
        V value;

        Entry(int h, K k, V v, Entry<K, V> n) {
            this.value = v;
            this.next = n;
            this.key = k;
            this.hash = h;
        }

        public final K getKey() {
            return this.key;
        }

        public final V getValue() {
            return this.value;
        }

        public final V setValue(V newValue) {
            V oldValue = this.value;
            this.value = newValue;
            return oldValue;
        }

        public final boolean equals(Object o) {
            if (!(o instanceof Map.Entry)) {
                return false;
            }
            Map.Entry e = (Map.Entry) o;
            Object k1 = getKey();
            Object k2 = e.getKey();
            if (k1 != k2 && (k1 == null || !k1.equals(k2))) {
                return false;
            }
            Object v1 = getValue();
            Object v2 = e.getValue();
            if (v1 == v2 || (v1 != null && v1.equals(v2))) {
                return true;
            }
            return false;
        }

        public final int hashCode() {
            int i = 0;
            int hashCode = this.key == null ? 0 : this.key.hashCode();
            if (this.value != null) {
                i = this.value.hashCode();
            }
            return hashCode ^ i;
        }

        public final String toString() {
            return getKey() + "=" + getValue();
        }

        /* access modifiers changed from: package-private */
        public void recordAccess(AntiCollisionHashMap<K, V> antiCollisionHashMap) {
        }

        /* access modifiers changed from: package-private */
        public void recordRemoval(AntiCollisionHashMap<K, V> antiCollisionHashMap) {
        }
    }

    /* access modifiers changed from: package-private */
    public void addEntry(int hash, K key, V value, int bucketIndex) {
        this.table[bucketIndex] = new Entry<>(hash, key, value, this.table[bucketIndex]);
        int i = this.size;
        this.size = i + 1;
        if (i >= this.threshold) {
            resize(this.table.length * 2);
        }
    }

    /* access modifiers changed from: package-private */
    public void createEntry(int hash, K key, V value, int bucketIndex) {
        this.table[bucketIndex] = new Entry<>(hash, key, value, this.table[bucketIndex]);
        this.size++;
    }

    private abstract class HashIterator<E> implements Iterator<E> {
        Entry<K, V> current;
        int expectedModCount;
        int index;
        Entry<K, V> next;

        HashIterator() {
            this.expectedModCount = AntiCollisionHashMap.this.modCount;
            if (AntiCollisionHashMap.this.size > 0) {
                Entry[] t = AntiCollisionHashMap.this.table;
                while (this.index < t.length) {
                    int i = this.index;
                    this.index = i + 1;
                    Entry entry = t[i];
                    this.next = entry;
                    if (entry != null) {
                        return;
                    }
                }
            }
        }

        public final boolean hasNext() {
            return this.next != null;
        }

        /* access modifiers changed from: package-private */
        public final Entry<K, V> nextEntry() {
            if (AntiCollisionHashMap.this.modCount != this.expectedModCount) {
                throw new ConcurrentModificationException();
            }
            Entry<K, V> e = this.next;
            if (e == null) {
                throw new NoSuchElementException();
            }
            Entry<K, V> entry = e.next;
            this.next = entry;
            if (entry == null) {
                Entry[] t = AntiCollisionHashMap.this.table;
                while (this.index < t.length) {
                    int i = this.index;
                    this.index = i + 1;
                    Entry entry2 = t[i];
                    this.next = entry2;
                    if (entry2 != null) {
                        break;
                    }
                }
            }
            this.current = e;
            return e;
        }

        public void remove() {
            if (this.current == null) {
                throw new IllegalStateException();
            } else if (AntiCollisionHashMap.this.modCount != this.expectedModCount) {
                throw new ConcurrentModificationException();
            } else {
                Object k = this.current.key;
                this.current = null;
                AntiCollisionHashMap.this.removeEntryForKey(k);
                this.expectedModCount = AntiCollisionHashMap.this.modCount;
            }
        }
    }

    private final class ValueIterator extends AntiCollisionHashMap<K, V>.HashIterator<V> {
        private ValueIterator() {
            super();
        }

        public V next() {
            return nextEntry().value;
        }
    }

    private final class KeyIterator extends AntiCollisionHashMap<K, V>.HashIterator<K> {
        private KeyIterator() {
            super();
        }

        public K next() {
            return nextEntry().getKey();
        }
    }

    private final class EntryIterator extends AntiCollisionHashMap<K, V>.HashIterator<Map.Entry<K, V>> {
        private EntryIterator() {
            super();
        }

        public Map.Entry<K, V> next() {
            return nextEntry();
        }
    }

    /* access modifiers changed from: package-private */
    public Iterator<K> newKeyIterator() {
        return new KeyIterator();
    }

    /* access modifiers changed from: package-private */
    public Iterator<V> newValueIterator() {
        return new ValueIterator();
    }

    /* access modifiers changed from: package-private */
    public Iterator<Map.Entry<K, V>> newEntryIterator() {
        return new EntryIterator();
    }

    public Set<K> keySet() {
        Set<K> ks = this.keySet;
        if (ks != null) {
            return ks;
        }
        Set<K> ks2 = new KeySet();
        this.keySet = ks2;
        return ks2;
    }

    private final class KeySet extends AbstractSet<K> {
        private KeySet() {
        }

        public Iterator<K> iterator() {
            return AntiCollisionHashMap.this.newKeyIterator();
        }

        public int size() {
            return AntiCollisionHashMap.this.size;
        }

        public boolean contains(Object o) {
            return AntiCollisionHashMap.this.containsKey(o);
        }

        public boolean remove(Object o) {
            return AntiCollisionHashMap.this.removeEntryForKey(o) != null;
        }

        public void clear() {
            AntiCollisionHashMap.this.clear();
        }
    }

    public Collection<V> values() {
        Collection<V> vs = this.values;
        if (vs != null) {
            return vs;
        }
        Collection<V> vs2 = new Values();
        this.values = vs2;
        return vs2;
    }

    private final class Values extends AbstractCollection<V> {
        private Values() {
        }

        public Iterator<V> iterator() {
            return AntiCollisionHashMap.this.newValueIterator();
        }

        public int size() {
            return AntiCollisionHashMap.this.size;
        }

        public boolean contains(Object o) {
            return AntiCollisionHashMap.this.containsValue(o);
        }

        public void clear() {
            AntiCollisionHashMap.this.clear();
        }
    }

    public Set<Map.Entry<K, V>> entrySet() {
        return entrySet0();
    }

    private Set<Map.Entry<K, V>> entrySet0() {
        Set<Map.Entry<K, V>> es = this.entrySet;
        if (es != null) {
            return es;
        }
        Set<Map.Entry<K, V>> es2 = new EntrySet();
        this.entrySet = es2;
        return es2;
    }

    private final class EntrySet extends AbstractSet<Map.Entry<K, V>> {
        private EntrySet() {
        }

        public Iterator<Map.Entry<K, V>> iterator() {
            return AntiCollisionHashMap.this.newEntryIterator();
        }

        public boolean contains(Object o) {
            if (!(o instanceof Map.Entry)) {
                return false;
            }
            Map.Entry<K, V> e = (Map.Entry) o;
            Entry<K, V> candidate = AntiCollisionHashMap.this.getEntry(e.getKey());
            if (candidate == null || !candidate.equals(e)) {
                return false;
            }
            return true;
        }

        public boolean remove(Object o) {
            return AntiCollisionHashMap.this.removeMapping(o) != null;
        }

        public int size() {
            return AntiCollisionHashMap.this.size;
        }

        public void clear() {
            AntiCollisionHashMap.this.clear();
        }
    }

    private void writeObject(ObjectOutputStream s) throws IOException {
        Iterator<Map.Entry<K, V>> i = this.size > 0 ? entrySet0().iterator() : null;
        s.defaultWriteObject();
        s.writeInt(this.table.length);
        s.writeInt(this.size);
        if (i != null) {
            while (i.hasNext()) {
                Map.Entry<K, V> e = i.next();
                s.writeObject(e.getKey());
                s.writeObject(e.getValue());
            }
        }
    }

    private void readObject(ObjectInputStream s) throws IOException, ClassNotFoundException {
        s.defaultReadObject();
        this.table = new Entry[s.readInt()];
        init();
        int size2 = s.readInt();
        for (int i = 0; i < size2; i++) {
            putForCreate(s.readObject(), s.readObject());
        }
    }

    /* access modifiers changed from: package-private */
    public int capacity() {
        return this.table.length;
    }

    /* access modifiers changed from: package-private */
    public float loadFactor() {
        return this.loadFactor;
    }
}
