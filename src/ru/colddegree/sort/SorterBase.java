package ru.colddegree.sort;

/**
 * Базовый класс сортировщика
 */
public abstract class SorterBase implements Sorter {
    protected int[] array;

    private long comparisons;
    private long exchanges;

    protected int from;
    protected int to;

    @Override
    public void sort(int[] array, int from, int to) {
        this.array = array;
        comparisons = exchanges = 0;

        this.from = from;
        this.to = to;
    }

    @Override
    public void sort(int[] array) {
        sort(array, 0, array.length - 1);
    }

    protected int compare(int a, int b) {
        comparisons++;

        return Integer.compare(a, b);
    }

    protected void swap(int i, int j) {
        exchanges++;

        int tmp = array[i];
        array[i] = array[j];
        array[j] = tmp;
    }

    @Override
    public long getComparisons() {
        return comparisons;
    }

    @Override
    public long getExchanges() {
        return exchanges;
    }
}
