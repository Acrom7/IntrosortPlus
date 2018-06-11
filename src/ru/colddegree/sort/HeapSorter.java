package ru.colddegree.sort;

/**
 * Сортировщик, использующий алгоритм пирамидальной сортировки
 */
public class HeapSorter extends SorterBase {
    private int heapSize;

    @Override
    public void sort(int[] array, int from, int to) {
        super.sort(array, from, to);
        heapsort();
    }

    private void heapsort() {
        buildMaxHeap();

        for (int i = to; i >= from + 1; i--) {
            swap(from, i);
            heapSize -= 1;
            maxHeapify(from);
        }
    }

    private void buildMaxHeap() {
        int subarrLength = to - from + 1;
        heapSize = subarrLength;

        for (int i = from + subarrLength / 2 - 1; i >= from; i--) {
            maxHeapify(i);
        }
    }

    private void maxHeapify(int i) {
        int l = left(i);
        int r = right(i);

        int largest = i;

        if (l >= from && l < from + heapSize) {
            if ( compare(array[l], array[i]) > 0 ) // array[l] > array[i]
                largest = l;
        }

        if (r >= from && r < from + heapSize) {
            if ( compare(array[r], array[largest]) > 0 ) // array[r] > array[largest]
                largest = r;
        }

        if (largest != i) {
            swap(i, largest);
            maxHeapify(largest);
        }
    }

    private int left(int i) {
        return 2*i + 1 - from;
    }

    private int right(int i) {
        return 2*i + 2 - from;
    }
}
