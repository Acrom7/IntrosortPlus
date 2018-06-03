package ru.colddegree;

import java.util.List;

public class QuicksortStrategy implements SortingStrategy {
    @Override
    public <T extends Comparable<T>> void sort(T[] array) {
        quicksort(array, 0, array.length - 1);
    }

    private <T extends Comparable<T>> void quicksort(T[] array, int low, int high) {
        if (low < high) {
            int mid = partition(array, low, high);
            quicksort(array, low, mid - 1);
            quicksort(array, mid + 1, high);
        }
    }

    private <T extends Comparable<T>> int partition(T[] array, int low, int high) {
        T pivot = array[high];
        int i = low - 1;

        for (int j = low; j < high; j++) {
            if (array[j].compareTo(pivot) <= 0) { // array[j] <= pivot
                i++;
                swap(array, i, j);
            }
        }

        swap(array, i + 1, high);

        return i + 1;
    }

    private <T extends Comparable<T>> void swap(T[] array, int i, int j) {
        T tmp = array[i];
        array[i] = array[j];
        array[j] = tmp;
    }
}
