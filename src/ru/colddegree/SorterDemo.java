package ru.colddegree;

import java.util.ArrayList;
import java.util.List;

public class SorterDemo {

    public static void main(String[] args) {

        Sorter sorter = new Sorter( new QuicksortStrategy() );

        Integer[] arr = {0, 48, 7, 8, 1, 2, 44};
        String[] a = {" ", ""};
        sorter.sort(a);
        sorter.sort(arr);


        List asd  = new ArrayList();
        asd.add("");
        asd.add(4);

    }

}
