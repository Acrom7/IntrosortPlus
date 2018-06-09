package ru.colddegree;

import ru.colddegree.gen.NumberGenerator;
import ru.colddegree.gen.SequenceGenerator;
import ru.colddegree.gen.SequentialNumberGenerator;
import ru.colddegree.sort.IntroSorter;
import ru.colddegree.sort.Sorter;

import java.io.File;
import java.util.Arrays;
import java.util.Scanner;

public class Sandbox {
    private static final String FILE_PATH = "resources/sequence.txt";

    public static void main(String[] args) {

        generateSequenceToFile(FILE_PATH);
        int[] seq = getSequenceFromFile(FILE_PATH);

        System.out.println( "Initial sequence: " + Arrays.toString(seq) );

        Sorter sorter = new IntroSorter();
        sorter.sort(seq);

        System.out.println( "Sorted sequence:  " + Arrays.toString(seq) );

        System.out.println( "Comparisons: " + sorter.getComparisons() );
        System.out.println( "Exchanges: " + sorter.getExchanges() );
    }

    private static void generateSequenceToFile(String filepath) {
        NumberGenerator numGen = new SequentialNumberGenerator(50, -1);

        SequenceGenerator seqGen = new SequenceGenerator(numGen, 50);

        seqGen.generateFile(filepath);
    }

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
