package ru.colddegree;

import java.util.List;

public class Sorter {
    private SortingStrategy sortingStrategy;

    public Sorter(SortingStrategy sortingStrategy) {
        this.sortingStrategy = sortingStrategy;
    }

    public <T extends Comparable<T>> void sort(T[] array) {
        sortingStrategy.sort(array);
    }
}
