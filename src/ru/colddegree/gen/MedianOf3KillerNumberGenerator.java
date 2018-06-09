package ru.colddegree.gen;

public class MedianOf3KillerNumberGenerator implements NumberGenerator {
    private int sequenceLength;
    private int index = 0;
    private int half;

    public MedianOf3KillerNumberGenerator(int sequenceLength) {
        this.sequenceLength = sequenceLength;
        half = sequenceLength / 2;
    }

    @Override
    public int generateNumber() {
        if (index >= sequenceLength)
            throw new IndexOutOfBoundsException("You must not call this method more than "
                    + Integer.toString(sequenceLength) + " times");

        int result = 0;

        if (sequenceLength % 2 != 0 && index == sequenceLength - 1) {
            result = sequenceLength;
        }
        else if (index < half) {
            if (index % 2 == 0)
                result = index + 1;
            else
                result = half + index + (half % 2);
        }
        else if (index < sequenceLength) {
            result = (index - half + 1) * 2;
        }

        index++;

        return result;
    }
}
