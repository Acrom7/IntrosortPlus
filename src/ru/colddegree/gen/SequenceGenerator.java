package ru.colddegree.gen;

import java.io.BufferedWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;


/**
 * Генератор последовательностей
 *
 * Используя конкретный генератор чисел, создаёт файл с последовательностью
 */
public class SequenceGenerator {
    private NumberGenerator numberGenerator;
    private int sequenceLength;

    /**
     * Конструктор.
     *
     * @param numberGenerator конкретный генератор чисел
     * @param sequenceLength  длина генерируемой последовательности
     */
    public SequenceGenerator(NumberGenerator numberGenerator, int sequenceLength) {
        this.numberGenerator = numberGenerator;
        this.sequenceLength = sequenceLength;
    }

    /**
     * Создаёт файл названием из filepath и заполняет его sequenceLength числами
     * используя генератор numberGenerator
     *
     * @param filepath путь к генерируемому файлу
     */
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
