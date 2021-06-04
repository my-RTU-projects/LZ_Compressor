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
        scanner = new Scanner(System.in);

        String command = "";
        while (!command.equals("exit")) {
            System.out.println("Ievadiet komandu: ");
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

        System.out.print("Source file name: ");
        String fileName1 = scanner.nextLine();

        System.out.print("Target file name: ");
        String fileName2 = scanner.nextLine();

        try (FileInputStream fis = new FileInputStream(fileName1);
             FileOutputStream fos = new FileOutputStream(fileName2)) {

            while (true) {
                byte[] original;
                if (fis.available() > (1 << 24)) original = new byte[1 << 24];
                else original = new byte[fis.available()];

                fis.read(original);
                fos.write(LZEncoder.encode(original));

                if (fis.available() == 0) break;
            }
        } catch (Exception e) {
            System.out.println("Something goes wrong!");
        }

        System.out.println("Encoded");
    }


    public static void decompress() {

        System.out.print("Archive name: ");
        String fileName1 = scanner.nextLine();

        System.out.print("Target file name: ");
        String fileName2 = scanner.nextLine();

        try (FileOutputStream fos = new FileOutputStream(fileName2)) {
            byte[] encoded = Files.readAllBytes(Paths.get(fileName1));;
            fos.write(LZDecoder.decode(encoded));
        } catch (Exception e) {
            System.out.println("Something goes wrong!");
        }

        System.out.println("Decoded");
    }

    public static void size() {
        System.out.println("file name");
        String fileName = scanner.nextLine();
        File file = new File(fileName);
        System.out.println("size: " + file.length());
    }

    public static void equal() {
        System.out.println("first file name:");
        String first = scanner.nextLine();
        System.out.println("second file name:");
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