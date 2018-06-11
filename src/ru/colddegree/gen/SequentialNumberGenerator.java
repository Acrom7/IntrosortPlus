package ru.colddegree.gen;

/**
 * Генератор чисел последовательностей типа арифметической прогрессии
 */
public class SequentialNumberGenerator implements NumberGenerator {
    private int initialValue;
    private int step;

    private int index = 0;

    /**
     * Конструктор
     *
     * @param initialValue начальное значение последовательности
     * @param step         шаг
     */
    public SequentialNumberGenerator(int initialValue, int step) {
        this.initialValue = initialValue;
        this.step = step;
    }

    @Override
    public int generateNumber() {
        return initialValue + (index++ * step);
    }
}
