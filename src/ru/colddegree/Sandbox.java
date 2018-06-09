package ru.colddegree;

import ru.colddegree.gen.SequenceGenerator;
import ru.colddegree.gen.SequenceGeneratorImpl;
import ru.colddegree.gen.SequenceInFileGenerator;

import java.io.BufferedWriter;
import java.io.File;
import java.io.IOException;
import java.nio.file.*;
import java.util.Scanner;
import java.util.Arrays;

public class Sandbox {
    public static void main(String[] args) {
//        generateAndWriteToFile();
        generateDirectlyInFile();
        readFromFileAndPrint();
    }

    public static void generateDirectlyInFile() {
        Path filepath = null;

        try {
            filepath = Paths.get("resources/input.txt");
        } catch (InvalidPathException e) {
            e.printStackTrace();
            return;
        }


        try (BufferedWriter writer = Files.newBufferedWriter(filepath, StandardOpenOption.CREATE)) {

            SequenceInFileGenerator gen = new SequenceInFileGenerator(50, 7, 8);
            gen.generateInFile(writer);

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void generateAndWriteToFile() {
        Path filepath = null;

        try {
            filepath = Paths.get("resources/input.txt");
        } catch (InvalidPathException e) {
            e.printStackTrace();
            return;
        }

        SequenceGenerator gen = new SequenceGeneratorImpl(20, 0, 5);
        int[] seq = gen.generate();

        try (BufferedWriter writer = Files.newBufferedWriter(filepath, StandardOpenOption.CREATE)) {

            writer.write( Integer.toString(seq.length) );
            writer.newLine();

            for (int num : seq) {
                writer.write( Integer.toString(num) );
                writer.write(' ');
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void readFromFileAndPrint() {
        File inputFile = new File("resources/input.txt");

        int[] arr = null;

        try (Scanner scanner = new Scanner(inputFile)) {

            arr = new int[scanner.nextInt()];

            for (int i = 0; i < arr.length; i++) {
                arr[i] = scanner.nextInt();
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        System.out.println( Arrays.toString(arr) );
    }
}
