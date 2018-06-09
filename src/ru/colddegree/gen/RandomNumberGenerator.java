package ru.colddegree.gen;

import java.util.Random;

public class RandomNumberGenerator implements NumberGenerator {
    private int from;
    private int to;

    private Random random = new Random();

    public RandomNumberGenerator(int from, int to) {
        this.from = from;
        this.to = to;
    }

    @Override
    public int generateNumber() {
        return random.nextInt(to - from + 1) + from;
    }

    public void setSeed(long seed) {
        random.setSeed(seed);
    }
}
