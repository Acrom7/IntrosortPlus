package ru.colddegree;

import ru.colddegree.gen.NumberGenerator;
import ru.colddegree.gen.RandomNumberGenerator;
import ru.colddegree.gen.SequenceGenerator;
import ru.colddegree.sort.IntroSorter;
import ru.colddegree.sort.Sorter;

import java.io.File;
import java.util.Arrays;
import java.util.Scanner;

/**
 * Пример генерации последовательности в файл, считывания из файла и её сортировки
 */
public class Sandbox {
    private static final String FILE_PATH = "resources/sequence.txt";

    public static void main(String[] args) {

        generateSequenceToFile(FILE_PATH);
        int[] seq = getSequenceFromFile(FILE_PATH);

        System.out.println( "Initial sequence: " + Arrays.toString(seq) );

        // создаём сортировщик
        Sorter sorter = new IntroSorter();

        // засекаем время и начинаем сортировать
        long startTime = System.currentTimeMillis();
        sorter.sort(seq);
        long endTime = System.currentTimeMillis();


        System.out.println( "Sorted sequence:  " + Arrays.toString(seq) );



        System.out.println( "Comparisons: " + sorter.getComparisons() );
        System.out.println( "Exchanges: " + sorter.getExchanges() );

        System.out.println("Execution time: " + Long.toString(endTime - startTime) + " ms");
    }

    /**
     * Создаёт файл filepath и заполняет его числами
     *
     * @param filepath путь к файлу
     */
    private static void generateSequenceToFile(String filepath) {
        // можно использовать любой из этих генераторов чисел

//        NumberGenerator numGen = new SequentialNumberGenerator(50, -1);
//        NumberGenerator numGen = new MedianOf3KillerNumberGenerator(1000000);
        NumberGenerator numGen = new RandomNumberGenerator(0, 1000000);

        // а затем передавать их в генератор последовательности
        SequenceGenerator seqGen = new SequenceGenerator(numGen, 20);

        seqGen.generateFile(filepath);
    }

    /**
     * Считывает последовательность чисел из файла filepath в массив и возвращает его
     *
     * @param filepath путь к файлу
     * @return массив чисел из файла
     */
    private static int[] getSequenceFromFile(String filepath) {
        int[] seq = null;

        try (Scanner scanner = new Scanner( new File(filepath) )) {

            seq = new int[scanner.nextInt()];

            for (int i = 0; i < seq.length; i++) {
                seq[i] = scanner.nextInt();
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return seq;
    }

}
