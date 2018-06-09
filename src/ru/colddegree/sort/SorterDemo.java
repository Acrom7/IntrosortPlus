package ru.colddegree.sort;

public class SorterDemo {
    public static void main(String[] args) {
        Sorter sorter = new IntroSorter();

        int[] arr = {0, 48, 7, 8, 1, 2, 44};

        sorter.sort(arr);

        for (int i = 0; i < arr.length; i++) {
            System.out.print(arr[i]);
            System.out.print(' ');
        }
        System.out.println();

        System.out.println( "Comparisons: " + sorter.getComparisons() );
        System.out.println( "Exchanges: " + sorter.getExchanges() );
    }
}
