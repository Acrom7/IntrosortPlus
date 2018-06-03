package ru.colddegree;

public class QuicksortStrategy<T extends Comparable<T>> implements SortingStrategy<T> {
    private T[] array;

    @Override
    public void sort(T[] array) {
        this.array = array;
        quicksort(0, array.length - 1);
    }

    private void quicksort(int low, int high) {
        if (low < high) {
            int mid = partition(low, high);
            quicksort(low, mid - 1);
            quicksort(mid + 1, high);
        }
    }

    private int partition(int low, int high) {
        T pivot = array[high];
        int i = low - 1;

        for (int j = low; j < high; j++) {
            if (array[j].compareTo(pivot) <= 0) { // array[j] <= pivot
                i++;
                swap(i, j);
            }
        }

        swap(i + 1, high);

        return i + 1;
    }

    private void swap(int i, int j) {
        T tmp = array[i];
        array[i] = array[j];
        array[j] = tmp;
    }
}
