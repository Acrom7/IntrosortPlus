package ru.colddegree.sort;

public class QuickSorter extends SorterBase {
    @Override
    public void sort(int[] array, int from, int to) {
        super.sort(array, from, to);
        quicksort(from, to);
    }

    protected void quicksort(int low, int high) {
        if (low < high) {
            int mid = partition(low, high);
            quicksort(low, mid - 1);
            quicksort(mid + 1, high);
        }
    }

    protected int partition(int low, int high) {
        int pivot = getPivot(low, high);
        int i = low - 1;

        for (int j = low; j < high; j++) {
            if ( compare(array[j], pivot) <= 0 ) { // array[j] <= pivot
                i++;
                swap(i, j);
            }
        }

        swap(i + 1, high);

        return i + 1;
    }

    private int getPivot(int low, int high) {
//        return high; // trivial

        int mid = (low + high) / 2;

        // return median of array[low], array[mid] and array[high]
        return Math.max(
                Math.min(array[low], array[mid]),
                Math.min( Math.max(array[low], array[mid]), array[high] ));
    }
}
