package ru.colddegree.sort;

public class IntroSorter extends QuickSorter {
    private Sorter heapSorter = new HeapSorter();

    @Override
    public void sort(int[] array, int from, int to) {
        super.sort(array, from, to);
    }

    @Override
    protected void quicksort(int from, int to) {
        final int maxDepth = log2nlz(array.length) * 2;
        introsort(from, to, maxDepth);
    }

    private void introsort(int low, int high, int maxDepth) {
        if (low < high) {
            int mid = partition(low, high);

            if (mid > maxDepth) {
                heapSorter.sort(array, low, high);
            } else {
                introsort(low, mid - 1, maxDepth - 1);
                introsort(mid + 1, high, maxDepth - 1);
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
