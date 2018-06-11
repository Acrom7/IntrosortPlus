package ru.colddegree.sort;

/**
 * Интерфейс сортировщика
 */
public interface Sorter {
    /**
     * Сортирует массив array начиная с индекса from, заканчивая индексом to включительно
     *
     * @param array сортируемый массив
     * @param from  начальный индекс
     * @param to    конечный индекс
     */
    void sort(int[] array, int from, int to);

    /**
     * Сортирует массив array от начала, до конца
     *
     * @param array сортируемый массив
     */
    void sort(int[] array);


    /**
     * Возвращает количество сравнений после выполнения метода сортировки
     *
     * @return количество сравнений
     */
    long getComparisons();

    /**
     * Возвращает количество обменов после выполнения метода сортировки
     *
     * @return количество обменов
     */
    long getExchanges();
}
