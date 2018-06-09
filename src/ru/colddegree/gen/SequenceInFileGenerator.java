package ru.colddegree.gen;

import java.io.BufferedWriter;
import java.io.IOException;

public class SequenceInFileGenerator {
    private int length;
    private int initialValue;
    private int step;

    public SequenceInFileGenerator(int length, int initialValue, int step) {
        this.length = length;
        this.initialValue = initialValue;
        this.step = step;
    }

    public void generateInFile(BufferedWriter writer) throws IOException {
        writer.write( Integer.toString(length) );
        writer.newLine();

        for (int i = 0; i < length; i++) {
            writer.write( Integer.toString(initialValue + i * step) );
            writer.write(' ');
        }
    }
}
