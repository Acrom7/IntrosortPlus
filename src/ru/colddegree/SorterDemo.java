package ru.colddegree;

public class SorterDemo {

    public static void main(String[] args) {

        var sorter = new Sorter( new QuicksortStrategy() );

        Integer[] arr = {0, 48, 7, 8, 1, 2, 44};
        sorter.sort(arr);

        for (var i : arr) {
            System.out.print(i);
            System.out.print(' ');
        }
    }

}
