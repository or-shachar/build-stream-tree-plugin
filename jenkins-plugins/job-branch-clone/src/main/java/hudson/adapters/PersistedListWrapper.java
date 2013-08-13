package hudson.adapters;

import hudson.util.PersistedList;
import sun.reflect.generics.reflectiveObjects.NotImplementedException;

import java.io.IOException;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;

/**
 * Created by IntelliJ IDEA.
 * User: grunzwei
 * Date: 6/3/12
 * Time: 12:24 PM
 * To change this template use File | Settings | File Templates.
 *
 * this class allows us to treat a jenkins custom "PersistedListWrapper"
 * like a List some of the time... making it iterable and stuff.
 *
 * TODO: should be in jenkins-commons module
 */


/*
* nathang: TODO useful proxy -
* make a class correspond to an interface whose methods it supports, but doesn't "implement" officially.
* TODO: another useful proxy - synchronizing proxy. contains a lock and delegates to methods but locks first.
* */
public class PersistedListWrapper<T> implements List<T> {

    private PersistedList<T> wrappedList;

    public PersistedListWrapper(PersistedList<T> pListToWrap) {
        wrappedList = pListToWrap;
    }

    @Override
    public int size() {
        return wrappedList.size();
    }

    @Override
    public boolean isEmpty() {
        return wrappedList.isEmpty();
    }

    @Override
    public boolean contains(Object o) {
        return wrappedList.contains(o);
    }

    @Override
    public Iterator<T> iterator() {
        return wrappedList.iterator();
    }

    @Override
    public Object[] toArray() {
        throw new NotImplementedException();
    }

    @Override
    public <T> T[] toArray(T[] a) {
        throw new NotImplementedException();
    }

    @Override
    public boolean add(T t) {
        try {
            wrappedList.add(t);
            return true;
        } catch (IOException e) {
            throw new RuntimeException("add operation failed", e);
        }
    }

    @Override
    public boolean remove(Object o) {

        try {
            return wrappedList.remove((T) o);
        } catch (IOException e) {
            throw new RuntimeException("remove operation failed", e);
        }

    }

    @Override
    public boolean containsAll(Collection<?> c) {
        throw new NotImplementedException();
    }

    @Override
    public boolean addAll(Collection<? extends T> c) {
        throw new NotImplementedException();
    }

    @Override
    public boolean addAll(int index, Collection<? extends T> c) {
        throw new NotImplementedException();
    }

    @Override
    public boolean removeAll(Collection<?> c) {
        throw new NotImplementedException();
    }

    @Override
    public boolean retainAll(Collection<?> c) {
        throw new NotImplementedException();
    }

    @Override
    public void clear() {
        wrappedList.clear();
    }

    @Override
    public T get(int index) {
        return wrappedList.get(index);
    }

    @Override
    public T set(int index, T element) {
        throw new NotImplementedException();
    }

    @Override
    public void add(int index, T element) {
        throw new NotImplementedException();
    }

    @Override
    public T remove(int index) {
        throw new NotImplementedException();
    }

    @Override
    public int indexOf(Object o) {
        throw new NotImplementedException();
    }

    @Override
    public int lastIndexOf(Object o) {
        throw new NotImplementedException();
    }

    @Override
    public ListIterator<T> listIterator() {
        throw new NotImplementedException();
    }

    @Override
    public ListIterator<T> listIterator(int index) {
        throw new NotImplementedException();
    }

    @Override
    public List<T> subList(int fromIndex, int toIndex) {
        throw new NotImplementedException();
    }
}
