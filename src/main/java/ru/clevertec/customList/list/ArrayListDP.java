package ru.clevertec.customList.list;

import ru.clevertec.customList.iterator.IteratorDP;

import java.util.Arrays;

public class ArrayListDP<T> implements ListDP<T>, Iterable<T> {

    private static final int QUANTITY_ONE = 1;
    private T[] objects;
    private int position;
    private int maxSize = -1;

    public ArrayListDP() {
        this.objects = (T[]) new Object[1];
    }

    /**
     * Appends the specified element to the end of this list.
     * @param maxSize the size till the array can be increased.
     */
    @Override
    public void setMaxSize(int maxSize) {
        this.maxSize = maxSize;
    }

    /**
     * Appends the specified element to the end of this list.
     * @param element to be appended to this list
     */
    @Override
    public void add(T element) {
        if (position == maxSize) {
            System.out.println(element + " doesn't fit this array.");
        } else if (position == objects.length) {
            grow(QUANTITY_ONE);
            add(element);
        } else if (position < objects.length) {
            objects[position++] = element;
        }
    }

    /**
     * Enlarges current array capacity in case the place lack
     * while adding of new elements to ensure that the array can hold they.
     * @param quantity of adding elements.
     */
    private void grow(int quantity) {
        int newSize = objects.length + quantity;
        if (maxSize == -1 || newSize <= maxSize) {
            objects = Arrays.copyOf(objects, newSize);
        } else {
            objects = Arrays.copyOf(objects, maxSize);
        }
    }

    /**
     * Counts all elements of the current array.
     * @return quantity of array elements.
     */
    @Override
    public int size() {
        return objects.length;
    }

    /**
     * Displays current array as a string by displaying each non-null element with a space as a separator .
     * @return a string from the current array.
     */
    @Override
    public String toString() {
        return Arrays.toString(objects);
    }

    @Override
    public T set(int index, T element) {
        T result;
        if (maxSize == -1) {
            result = objects[index];
            objects[index] = element;
        } else {
            if (index <= maxSize) {
                result = objects[index];
                objects[index] = element;
            } else {
                throw new IllegalArgumentException("Wrong index.");
            }
        }
        return result;
    }

    /**
     * Appends all the elements in the current data structure to the end of
     * this list, in the order that they were added to the starting array.
     *
     * @param elements array containing elements to be added to this list
     */
    @Override
    public void addAll(T[] elements) {
        if (objects.length >= position + elements.length) {
            for (T t : elements) {
                objects[position++] = t;
            }
        } else if (maxSize == -1 || maxSize >= position + elements.length) {
            grow(elements.length);
            addAll(elements);
        } else {
            int availableSpace = maxSize - position;
            grow(maxSize - elements.length);
            for (int i = 0; i < availableSpace; i++) {
                objects[position++] = elements[i];
            }
        }
    }

    /**
     * Returns an iterator over the elements in this list in proper sequence.
     *
     * @return an iterator over the elements in this list.
     *
     */
    @Override
    public IteratorDP<T> iterator() {
        return new ArrayListIteratorDP();
    }

    /**
     * Removes the element at the specified position in this list.
     * Shifts any subsequent elements to the left.
     */
    @Override
    public T remove(int index) {
        if (index < objects.length) {
            T deletedObject = objects[index];
            T[] temp = (T[]) new Object[objects.length];
            objects[index] = null;
            int i = 0;
            for (T t : objects) {
                if (t != null) {
                    temp[i++] = t;
                }
            }
            objects = temp;
            position--;
            return deletedObject;
        } else {
            throw new IndexOutOfBoundsException("There is no such an index.");
        }
    }

    /**
     * Removes all the elements from this list.
     * The list will be empty after this method will finish working.
     */
    @Override
    public void clear() {
        for (int i = 0; i < objects.length; i++) {
            if (objects[i] != null) {
                objects[i] = null;
            }
        }
        position = 0;
    }

    /**
     * Returns the index of the first occurrence of the specified element in this list,
     * or -1 if this list does not contain this element.
     * @param element the specified element which is looking for in this list
     */
    @Override
    public int find(T element) {
        for (int i = 0; i < objects.length; i++) {
            if (objects[i].equals(element)) {
                return i;
            }
        }
        return -1;
    }

    /**
     * Returns the element at the specified position in this list if it exists or an exception.
     * @param  position index of the element to return
     * @return the element at the specified position in this list
     * @throws IndexOutOfBoundsException –
     */
    @Override
    public T get(int position) {
        if (position <= objects.length) {
            for (int i = 0; i < objects.length; i++) {
                if (i == position) {
                    return objects[i];
                }
            }
        }
        throw new IndexOutOfBoundsException("There is no such the element.");
    }

    /**
     * Looks for and returns an array of not-null element is containing in the current list if it contains they.
     * @return an array of not-null element or a zero-sized array in case the list doesn't contain they.
     */
    @Override
    public T[] toArray() {
        int counter = countNotNull();
        return getNotNullArray(counter);
    }

    /**
     * Trims the capacity of this list to be the list's current size.
     * Cuts off null-value array elements and in this case reduces array size.
     */
    @Override
    public void trim() {
        int counter = countNotNull();
        objects = getNotNullArray(counter);
        position = counter;
    }

    /**
     * Extracts not-null elements from the current array and creates a new array.
     * @param counter the size of a new not-null elements array.
     * @return a new array of not-null elements or a zero-sized array
     */
    private T[] getNotNullArray(int counter) {
        T[] temp = (T[]) new Object[counter];
        int i = 0;
        for (T t : objects) {
            if (t != null) {
                temp[i++] = t;
            }
        }
        return temp;
    }

    /**
     * Counts null-value elements in the current array.
     * @return the quantity of not-null-value elements in the array
     */
    private int countNotNull() {
        int counter = 0;
        for (T t : objects) {
            if (t != null) {
                counter++;
            }
        }
        return counter;
    }

    private class ArrayListIteratorDP implements IteratorDP<T> {

        private int currentIndex;

        ArrayListIteratorDP() {
            currentIndex = 0;
        }

        /**
         * Returns true if next() would return a next element.
         * @return true if the iteration has more elements.
         * @throws UnsupportedOperationException if the current list is empty.
         */
        @Override
        public boolean hasNext() {
            if (objects.length == 0) {
                throw new UnsupportedOperationException("This arrays is empty.");
            } else {
                return currentIndex < position;
            }
        }

        /**
         * Returns the next element in the iteration.
         * @return the next element in the iteration.
         * @throws UnsupportedOperationException if the current list is empty.
         */
        @Override
        public T next() {
            if (objects.length == 0) {
                throw new UnsupportedOperationException("This arrays is empty.");
            } else {
                return objects[currentIndex++];
            }
        }

        /**
         * Removes from the current array the last element returned by this iterator.
         * This method can be called only once per call to next().
         * @throws UnsupportedOperationException if the current list is empty or current element is null.
         */
        @Override
        public void remove() {
            if (objects.length == 0 || objects[currentIndex] == null) {
                throw new UnsupportedOperationException
                        ("The current element is null or The array is empty.");
            } else {
                objects[currentIndex] = null;
            }
        }

        /**
         * Adds the specified element to this list at position before the iterator.
         * In case the list bounded by maxSize() the last element of the current list will be lost.
         * @param object to be added to this list to the place before iterator
         * @throws UnsupportedOperationException if the array has the size 0 or iterator is at index 0.
         */
        @Override
        public void addBefore(T object) {
            if (objects.length == 0 || currentIndex == 0) {
                throw new UnsupportedOperationException("There is no place before.");
            } else {
                T[] tailElements = (T[]) new Object[objects.length - currentIndex];
                processArray(object, tailElements);
            }
        }

        /**
         * Adds the specified element to this list at position after the iterator.
         * In case the list bounded by maxSize() the last element of the current list will be lost.
         * @param object to be added to this list to the place after iterator
         * @throws UnsupportedOperationException if the array has the size 0 or iterator is at index 0.
         */
        @Override
        public void addAfter(T object) {
            if (objects.length == 0 || currentIndex == 0) {
                throw new UnsupportedOperationException("There is no place before.");
            } else {
                T[] tailElements = (T[]) new Object[objects.length - ++currentIndex];
                processArray(object, tailElements);
            }

        }

        /**
         * it Processes the current array: adds a new element and shifts others elements to the right.
         * @param object to be added to this list to the place before/after iterator
         * @param tailElements a pre-defined array for copying inside it the right part of the current array
         * @throws UnsupportedOperationException if the array has the size 0 or iterator is at index 0.
         */
        private void processArray (T object, T[] tailElements) {
            System.arraycopy(objects, currentIndex, tailElements, 0, tailElements.length);
            objects[currentIndex++] = object;
            if (maxSize == -1 || objects.length+1<=maxSize) {
                T[] temp = Arrays.copyOf(objects, objects.length + 1);
                System.arraycopy(tailElements, 0, temp, currentIndex, tailElements.length);
                objects = temp;
            } else {
                System.arraycopy(tailElements, 0, objects, currentIndex, tailElements.length-1);
            }
        }
    }
}
