package ru.clevertec.customList.list;

import ru.clevertec.customList.iterator.IteratorDP;

public interface ListDP<T> extends Iterable<T> {

    void add(T element);

    void setMaxSize(int maxSize);

    int size();

    T set(int index, T element);

    void addAll(T[] elements);

    T remove(int index);

    void clear();

    int find(T element);

    T get(int position);

    T[] toArray();

    void trim();

    IteratorDP<T> iterator();
}
