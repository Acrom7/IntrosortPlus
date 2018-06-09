package ru.colddegree.gen;

public class SequentialNumberGenerator implements NumberGenerator {
    private int initialValue;
    private int step;

    private int index = 0;

    public SequentialNumberGenerator(int initialValue, int step) {
        this.initialValue = initialValue;
        this.step = step;
    }

    @Override
    public int generateNumber() {
        return initialValue + (index++ * step);
    }
}
