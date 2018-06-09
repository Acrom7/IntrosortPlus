package ru.colddegree.sort;

public interface Sorter {
    void sort(int[] array, int from, int to);
    void sort(int[] array);

    long getComparisons();
    long getExchanges();
}
