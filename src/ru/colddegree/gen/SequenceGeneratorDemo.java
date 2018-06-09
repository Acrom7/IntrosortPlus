package ru.colddegree.gen;

import java.util.Arrays;

public class SequenceGeneratorDemo {
    public static void main(String[] args) {
        SequenceGenerator gen = new SequenceGeneratorImpl(20, 0, 0);

        int[] seq = gen.generate();

        System.out.println( Arrays.toString(seq) );
    }
}
