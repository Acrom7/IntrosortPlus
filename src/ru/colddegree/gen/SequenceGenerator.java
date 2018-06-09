package ru.colddegree.gen;

import java.io.BufferedWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;

public class SequenceGenerator {
    private NumberGenerator numberGenerator;
    private int sequenceLength;

    public SequenceGenerator(NumberGenerator numberGenerator, int sequenceLength) {
        this.numberGenerator = numberGenerator;
        this.sequenceLength = sequenceLength;
    }

    public void generateFile(String filepath) {

        try (BufferedWriter writer = Files.newBufferedWriter(Paths.get(filepath), StandardOpenOption.TRUNCATE_EXISTING)) {

            writer.write( Integer.toString(sequenceLength) );
            writer.newLine();

            for (int i = 0; i < sequenceLength; i++) {
                writer.write( Integer.toString(numberGenerator.generateNumber()) );
                writer.write(' ');
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
