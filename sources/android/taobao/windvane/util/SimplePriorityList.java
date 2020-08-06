package android.taobao.windvane.util;

import android.taobao.windvane.util.SimplePriorityList.PriorityInterface;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

public class SimplePriorityList<E extends PriorityInterface> implements List<E> {
    private List<E> cachedPriorityList = new CopyOnWriteArrayList();
    private List<E>[] list;
    private ReadWriteLock lock = new ReentrantReadWriteLock();
    private int prioritySize;

    public interface PriorityInterface {
        int getPriority();
    }

    public SimplePriorityList(int prioritySize2) {
        if (prioritySize2 <= 0) {
            throw new IllegalArgumentException("prioritySize < 0: " + prioritySize2);
        }
        this.list = new ArrayList[prioritySize2];
        for (int i = 0; i < prioritySize2; i++) {
            this.list[i] = new ArrayList();
        }
        this.cachedPriorityList.clear();
        this.prioritySize = prioritySize2;
    }

    public void add(int location, E e) {
        throw new UnsupportedOperationException();
    }

    public boolean add(E object) {
        if (!checkPriority(object)) {
            return false;
        }
        this.lock.writeLock().lock();
        boolean result = this.list[object.getPriority()].add(object);
        this.cachedPriorityList.clear();
        this.lock.writeLock().unlock();
        return result;
    }

    public boolean addAll(int location, Collection<? extends E> collection) {
        throw new UnsupportedOperationException();
    }

    public boolean addAll(Collection<? extends E> collection) {
        throw new UnsupportedOperationException();
    }

    public void clear() {
        this.lock.writeLock().lock();
        for (List<E> t : this.list) {
            t.clear();
        }
        this.cachedPriorityList.clear();
        this.lock.writeLock().unlock();
    }

    public boolean contains(Object object) {
        return getCachedPriorityList().contains(object);
    }

    public boolean containsAll(Collection<?> collection) {
        throw new UnsupportedOperationException();
    }

    public E get(int location) {
        throw new UnsupportedOperationException();
    }

    public int indexOf(Object object) {
        throw new UnsupportedOperationException();
    }

    public boolean isEmpty() {
        throw new UnsupportedOperationException();
    }

    public Iterator<E> iterator() {
        return getCachedPriorityList().iterator();
    }

    public int lastIndexOf(Object object) {
        throw new UnsupportedOperationException();
    }

    public ListIterator<E> listIterator() {
        return getCachedPriorityList().listIterator();
    }

    public ListIterator<E> listIterator(int location) {
        throw new UnsupportedOperationException();
    }

    public E remove(int location) {
        throw new UnsupportedOperationException();
    }

    public boolean remove(Object object) {
        if (object == null) {
            return false;
        }
        E o = (PriorityInterface) object;
        if (!checkPriority(o)) {
            return false;
        }
        this.lock.writeLock().lock();
        boolean remove = this.list[o.getPriority()].remove(o);
        this.cachedPriorityList.clear();
        this.lock.writeLock().unlock();
        return remove;
    }

    public boolean removeAll(Collection<?> collection) {
        throw new UnsupportedOperationException();
    }

    public boolean retainAll(Collection<?> collection) {
        throw new UnsupportedOperationException();
    }

    public E set(int location, E e) {
        throw new UnsupportedOperationException();
    }

    public int size() {
        return getCachedPriorityList().size();
    }

    public List<E> subList(int start, int end) {
        throw new UnsupportedOperationException();
    }

    public Object[] toArray() {
        return getCachedPriorityList().toArray();
    }

    public <T> T[] toArray(T[] array) {
        return getCachedPriorityList().toArray(array);
    }

    private boolean checkPriority(E o) {
        return o == null || (o.getPriority() >= 0 && o.getPriority() < this.prioritySize);
    }

    private List<E> getCachedPriorityList() {
        if (this.cachedPriorityList.isEmpty()) {
            this.lock.readLock().lock();
            for (List<E> t : this.list) {
                this.cachedPriorityList.addAll(t);
            }
            this.lock.readLock().unlock();
        }
        return this.cachedPriorityList;
    }
}
