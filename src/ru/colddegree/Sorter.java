package ru.colddegree;

import java.util.List;

public class Sorter<T extends Comparable<T>> {
    private SortingStrategy<T> sortingStrategy;

    public Sorter(SortingStrategy<T> sortingStrategy) {
        this.sortingStrategy = sortingStrategy;
    }

    public void sort(T[] array) {
        sortingStrategy.sort(array);
    }
}
