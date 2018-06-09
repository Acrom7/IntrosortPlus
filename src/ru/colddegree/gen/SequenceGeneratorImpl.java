package ru.colddegree.gen;

public class SequenceGeneratorImpl implements SequenceGenerator {
    private int length;
    private int initialValue;
    private int step;

    public SequenceGeneratorImpl(int length, int initialValue, int step) {
        this.length = length;
        this.initialValue = initialValue;
        this.step = step;
    }

    @Override
    public int[] generate() {
        int[] result = new int[length];

        for (int i = 0; i < length; i++) {
            result[i] = initialValue + i * step;
        }

        return result;
    }
}
