package ru.clevertec.customList;

import ru.clevertec.customList.iterator.IteratorDP;
import ru.clevertec.customList.list.ArrayListDP;
import ru.clevertec.customList.list.ListDP;

import java.util.Iterator;

public class ListDPRunner {

    public static void main(String[] args) {
        ListDP<Integer> integerArrayListDP = new ArrayListDP<>();

        integerArrayListDP.setMaxSize(20);
        System.out.println(integerArrayListDP.size());

        integerArrayListDP.add(11);
        integerArrayListDP.add(12);
        integerArrayListDP.add(13);

        System.out.println(integerArrayListDP);
        System.out.println(integerArrayListDP.size());

        integerArrayListDP.add(14);
        integerArrayListDP.add(15);
        integerArrayListDP.add(16);

        System.out.println(integerArrayListDP);
        System.out.println(integerArrayListDP.size());

        integerArrayListDP.add(17);
        integerArrayListDP.add(18);

        System.out.println(integerArrayListDP);
        System.out.println(integerArrayListDP.size());

        integerArrayListDP.addAll(new Integer[]{77, 88, 99});

        System.out.println("Array after adding 3 number array: " + integerArrayListDP);
        System.out.println(integerArrayListDP.size());

        integerArrayListDP.addAll(new Integer[]{110, 111, 88, 113});

        System.out.println("Array after adding 4 number array: " + integerArrayListDP);
        System.out.println(integerArrayListDP.size());

        System.out.println("\n" + integerArrayListDP.remove(10));
        System.out.println("Array after deleting of 10th element: " + integerArrayListDP);

        System.out.println("\nElement " + integerArrayListDP.set(0, 888) + " was changed.");

        Iterator<Integer> iterator1 = integerArrayListDP.iterator();

        System.out.println("\nWith while");
        while (iterator1.hasNext()) {
            System.out.print(iterator1.next() + " ");
        }

        System.out.println("\n\nWith for-each");
        for (int i : integerArrayListDP) {
            System.out.print(i + " ");
        }

        Iterator<Integer> iterator2 = integerArrayListDP.iterator();
        System.out.println("\n\nInput from iterator without next(): " + iterator2.next());
        System.out.println("\nInput from iterator after calling next(): " + iterator2.next());
        System.out.println("\nInput from iterator after calling next(): " + iterator2.next());
        iterator2.remove();
        System.out.println("\nAfter callings next() 3 times and remove() 1 time: " + integerArrayListDP);

        Integer set = integerArrayListDP.set(2, 44);
        System.out.println("\nAfter changing " + set + " by calling set(3, 44): " + integerArrayListDP);

        try {
            iterator2.remove();
            System.out.println("\nAfter calling remove() second time and catching an exception: " + integerArrayListDP);
        } catch (UnsupportedOperationException e){
            System.out.println("\nException was caught.");
        }

        Iterator<Integer> iterator3 = integerArrayListDP.iterator();
        iterator3.remove();
        try {
            iterator3.remove();   // A repeatable calling of remove() throws an exception
        } catch (UnsupportedOperationException e) {
            System.out.println("\nException was caught.");
        }
        iterator3.next();
        iterator3.next();
        iterator3.remove();
        System.out.println("\nAfter callings remove() 1 time and next() 2 times and remove() 1 time: " + integerArrayListDP);

        integerArrayListDP.trim();
        System.out.println("\nThe array after callings trim(): " + integerArrayListDP);

        System.out.println("\nThe index of 88 is 6 and method returned: " + integerArrayListDP.find(88));
        System.out.println("The index of 12 is 0 and method returned: " + integerArrayListDP.find(12));
        System.out.println("The index of 113 is 10 and method returned: " + integerArrayListDP.find(113));

        System.out.println("\nThe element in the index 6 is 88 and method returned: " + integerArrayListDP.get(6));
        System.out.println("The element in the index 0 is 12 and method returned " + integerArrayListDP.get(0));
        System.out.println("The element in the index 10 is 113 and method returned " + integerArrayListDP.get(10));

        integerArrayListDP.clear();
        System.out.println("\nThe array after callings clear(): " + integerArrayListDP);

        integerArrayListDP.trim();
        System.out.println("\nThe array after callings trim(): " + integerArrayListDP);
        System.out.println("The array size is: " + integerArrayListDP.size());

        integerArrayListDP.add(1);
        integerArrayListDP.add(null);
        integerArrayListDP.add(3);

        System.out.println("\nThe array after adding null element: " + integerArrayListDP);

        integerArrayListDP.set(1, 2);
        integerArrayListDP.add(4);
        integerArrayListDP.add(5);

        Object[] integers = integerArrayListDP.toArray();
        System.out.print("\nArray from the current list: ");
        for (Object i : integers) {
            System.out.print(i + " ");
        }

        System.out.println("\n\n" + integerArrayListDP + ". Now cursor is on index 2.");
        integerArrayListDP.setMaxSize(5);
        IteratorDP<Integer> iterator4 = integerArrayListDP.iterator();
        iterator4.next();
        iterator4.next();
        iterator4.addBefore(88);
        System.out.println(integerArrayListDP + ". Added to a bounded list.");

        integerArrayListDP.clear();
        integerArrayListDP.add(1);
        integerArrayListDP.add(2);
        integerArrayListDP.add(3);
        integerArrayListDP.add(4);
        integerArrayListDP.add(5);
        integerArrayListDP.setMaxSize(6);
        System.out.println("\n" + integerArrayListDP + ". Now cursor is on index 2.");

        IteratorDP<Integer> iterator5 = integerArrayListDP.iterator();
        iterator5.next();
        iterator5.next();
        iterator5.addAfter(88);
        System.out.println(integerArrayListDP + ". Added to a unbounded list.");
    }
}
