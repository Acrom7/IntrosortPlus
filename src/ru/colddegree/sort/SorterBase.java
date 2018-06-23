package ru.colddegree.sort;

/**
 * Базовый класс сортировщика
 *
 * Каждый потомок данного класса должен переопределять метод sort(int[], int, int) следующим образом:
 *
 * @Override
 * public void sort(int[] array, int from, int to) {
 *         super.sort(array, from, to);
 *
 *         [вызов алгоритма сортировки]
 *
 *         postProcess();
 * }
 *
 * Также, для сравнения двух элементов и их перемещения, потомок должен вызывать методы compare() и swap() соответственно.
 *
 */
public abstract class SorterBase implements Sorter {
    protected int[] array;

    private long comparisons;
    private long exchanges;
    private long executionTime;

    protected int from;
    protected int to;

    @Override
    public void sort(int[] array, int from, int to) {
        this.array = array;
        comparisons = exchanges = 0;

        this.from = from;
        this.to = to;

        executionTime = System.currentTimeMillis();
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

    protected void postProcess() {
        executionTime = System.currentTimeMillis() - executionTime;
    }

    @Override
    public long getComparisons() {
        return comparisons;
    }

    @Override
    public long getExchanges() {
        return exchanges;
    }

    @Override
    public long getExecutionTime() {
        return executionTime;
    }
}
