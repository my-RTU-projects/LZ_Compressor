package project;

import project.FSE.FSEDecoder;
import project.FSE.FSEEncoder;
import project.HUFFMAN.HuffmanDecoder;
import project.HUFFMAN.HuffmanEncoder;
import project.LZ.LZDecoder;
import project.LZ.LZEncoder;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.*;
import java.util.Scanner;

class Main {

    static Scanner scanner;

    public static void main(String[] args) {
        menu();
    }

    public static void menu() {
        scanner = new Scanner(System.in);

        String command = "";
        while (!command.equals("exit")) {
            System.out.println("\nEnter the command (comp, decomp, size, equal, exit): ");
            command = scanner.nextLine();
            switch(command){
                case "comp":
                    compress();
                    break;
                case "decomp":
                    decompress();
                    break;
                case "size":
                    size();
                    break;
                case "equal":
                    equal();
                    break;
                case "exit":
                    break;
                default:
                    System.out.println("Wrong command!");
            }
        }
    }

    public static void compress() {
        System.out.println("Select compression algorithms. Use + to combine them. No more than 4 at a time. (LZ, FSE, HUFFMAN)");
        String[] algorithms = scanner.nextLine().toUpperCase().split("\s*\\+\s*");
        if (algorithms.length > 4) {
            System.out.println("ERROR! Too many algorithms at once!");
            return;
        }

        byte usedEncoders = 0;
        Encoder[] encoders = new Encoder[algorithms.length];
        for (int i = 0; i < algorithms.length; i++) {
            // ID: project.LZ - 1 (01), FSE - 2 (10), HUFFMAN - 3 (11)
            switch (algorithms[i]) {
                case "LZ" -> {
                    encoders[i] = new LZEncoder();
                    usedEncoders = (byte) (usedEncoders | (1 << i * 2));
                }
                case "FSE" -> {
                    encoders[i] = new FSEEncoder();
                    usedEncoders = (byte) (usedEncoders | (2 << i * 2));
                }
                case "HUFFMAN" -> {
                    encoders[i] = new HuffmanEncoder();
                    usedEncoders = (byte) (usedEncoders | (3 << i * 2));
                }
                default -> {
                    System.out.println("ERROR! Wrong encoder name!");
                    return;
                }
            }
        }

        System.out.print("Source file name: ");
        File file1 = new File(scanner.nextLine());

        System.out.print("Target file name: ");
        File file2 = new File(scanner.nextLine());

        try (FileInputStream fis = new FileInputStream(file1);
             FileOutputStream fos = new FileOutputStream(file2)) {

            // Записывается байт, в котором указанны использованные алгоритмы
            fos.write(usedEncoders);
            // Рассчитывается кол-во блоков
            byte dataBlockCount = (byte) (fis.available() >> 24);
            if (fis.available() % (1 << 24) > 0) dataBlockCount++;
            // Записывается кол-во блоков
            fos.write(dataBlockCount);

            while (fis.available() > 0) {
                // Читается блок максимаьный размер которого - 2^24 Б
                byte[] block;
                if (fis.available() > (1 << 24)) block = new byte[1 << 24];
                else block = new byte[fis.available()];
                fis.read(block);

                // Кодируется блок
                for (Encoder encoder : encoders) {
                    block = encoder.encode(block);
                }

                // Запмсывается размер закодированного блока
                for (int i = 0; i < 4; i++) {
                    fos.write((byte) ((block.length >> (i * 8)) & 255));
                }
                // Записываются закодированные данные
                fos.write(block);
            }
        } catch (Exception e) {
            System.out.println("Something goes wrong!");
            return;
        }

        long origSize = file1.length();
        long compSize = file2.length();
        System.out.printf("Encoded.\n" +
                "Original file size: %dB\n" +
                "Compressed file size: %dB\n" +
                "Compression ratio: %f\n",
                origSize, compSize, (double)origSize / compSize);
    }


    public static void decompress() {

        System.out.print("Archive name: ");
        String fileName1 = scanner.nextLine();

        System.out.print("Target file name: ");
        String fileName2 = scanner.nextLine();

        try (FileInputStream fis = new FileInputStream(fileName1);
             FileOutputStream fos = new FileOutputStream(fileName2)) {

            // Читается байт в котором указанны использованные алгоритмы и на его основе составляется список
            byte usedEncoders = (byte) fis.read();
            ArrayList<Decoder> decoders = new ArrayList<>();
            for (int i = 3; i >= 0; i--) {
                byte decoderId = (byte) ((usedEncoders >> (i * 2)) & 3);
                switch (decoderId) {
                    case 1:
                        decoders.add(new LZDecoder());
                        break;
                    case 2:
                        decoders.add(new FSEDecoder());
                        break;
                    case 3:
                        decoders.add(new HuffmanDecoder());
                        break;
                    default:
                }
            }

            byte dataBlockCount = (byte) fis.read();
            while (dataBlockCount > 0) {
                int encodedDataSize = 0;
                for (int i = 0; i < 4; i++) {
                    byte b = (byte) fis.read();
                    encodedDataSize = encodedDataSize | ((b & 255) << (i * 8));
                }

                byte[] block = fis.readNBytes(encodedDataSize);
                for (Decoder decoder : decoders) {
                    block = decoder.decode(block);
                }

                fos.write(block);
                dataBlockCount--;
            }
        } catch (Exception e) {
            System.out.println("Something goes wrong!");
        }

        System.out.println("Decoded.");
    }

    public static void size() {
        System.out.println("File name");
        String fileName = scanner.nextLine();
        File file = new File(fileName);
        System.out.println("Size: " + file.length());
    }

    public static void equal() {
        System.out.println("First file name:");
        String first = scanner.nextLine();
        System.out.println("Second file name:");
        String second = scanner.nextLine();

        try {
            byte[] array1 = Files.readAllBytes(Paths.get(first));
            byte[] array2 = Files.readAllBytes(Paths.get(second));
            System.out.println(Arrays.equals(array1, array2));
        } catch (IOException e) {
            System.out.println("Something goes wrong!");
            e.printStackTrace();
        }
    }
}