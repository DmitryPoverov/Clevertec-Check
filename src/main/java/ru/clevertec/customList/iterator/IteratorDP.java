package ru.clevertec.customList.iterator;

import java.util.Iterator;
import java.util.function.Consumer;

public interface IteratorDP<T> extends Iterator<T> {

    boolean hasNext();
    T next();
    void remove();
    void addBefore(T object);
    void addAfter(T object);

    @Override
    default void forEachRemaining(Consumer action) {
        Iterator.super.forEachRemaining(action);
    }
}
