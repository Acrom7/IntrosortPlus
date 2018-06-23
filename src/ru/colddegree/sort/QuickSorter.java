package ru.colddegree.sort;

/**
 * Сортировщик, использующий алгоритм быстрой сортировки
 */
public class QuickSorter extends SorterBase {
    @Override
    public void sort(int[] array, int from, int to) {
        super.sort(array, from, to);
        quicksort(from, to);
        postProcess();
    }

    protected void quicksort(int low, int high) {
        if (low < high) {
            int pivot = partition(low, high);
            quicksort(low, pivot - 1);
            quicksort(pivot + 1, high);
        }
    }

    // разбиение Ломуто
    protected int partition(int low, int high) {
        int pivot = medianOf3(low, high);
        int i = low;

        for (int j = low; j < high; j++) {
            if ( compare(array[j], pivot) <= 0 ) { // array[j] <= pivot
                swap(i, j);
                i++;
            }
        }

        swap(i, high);

        return i;
    }

    // получение медианы трёх для разбиения Ломуто
    private int medianOf3(int low, int high) {
        int mid = (low + high) / 2;

        if (array[mid] < array[low])
            swap(low, mid);
        if (array[high] < array[low])
            swap(low, high);
        if (array[mid] < array[high])
            swap(mid, high);

        return array[high];
    }
}
