package ru.colddegree.sort;

/**
 * Сортировщик, использующий алгоритм интроспективной сортировки
 */
public class IntroSorter extends QuickSorter {
    private Sorter heapSorter = new HeapSorter();

    @Override
    protected void quicksort(int from, int to) {
        final int depthLimit = log2nlz(array.length) * 2;
        introsort(from, to, depthLimit);
    }

    private void introsort(int low, int high, int depthLimit) {
        if (low < high) {
            if (depthLimit == 0) {
                heapSorter.sort(array, low, high);
            } else {
                int pivot = partition(low, high);
                introsort(low, pivot - 1, depthLimit - 1);
                introsort(pivot + 1, high, depthLimit - 1);
            }
        }
    }

    // https://stackoverflow.com/questions/3305059/how-do-you-calculate-log-base-2-in-java-for-integers
    private static int log2nlz(int bits) {
        if (bits == 0)
            return 0; // or throw exception

        return 31 - Integer.numberOfLeadingZeros( bits );
    }
}
