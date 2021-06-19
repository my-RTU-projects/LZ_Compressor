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

    public static void main(String[] args) throws IOException {
        menu();

        //byte[] array = Files.readAllBytes(Paths.get("C:\\Users\\USER\\Desktop\\faili\\a.html"));
        //byte[] enc = FSEEncoder.encode(array);
        //byte[] dec = FSEDecoder.decode(enc);

        //System.out.println(Arrays.equals(array, dec));
        //System.out.println(array.length + " " + enc.length);
        //equalArr(array, dec);
    }

    public static void equalArr(byte[] a1, byte[] a2) {
        for (int i = 0; i < a1.length; i++) {
            if (a1[i] != a2[i]) {
                System.out.println(i + ": " + a1[i] + " != " + a2[i]);
            }
        }
    }

    public static void menu() {
        scanner = new Scanner(System.in);

        String command = "";
        while (!command.equals("exit")) {
            System.out.println("Ievadiet komandu (comp, decomp, size, equal, exit): ");
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

            // Рассчитывается кол-во блоков
            byte dataBlockCount = (byte) (fis.available() >> 24);
            if (fis.available() % (1 << 24) > 0) dataBlockCount++;
            // Записывается кол-во блоков
            fos.write(dataBlockCount);

            while (fis.available() > 0) {
                // Читается блок максимаьный размер которого - 2^24 Б
                byte[] original;
                if (fis.available() > (1 << 24)) original = new byte[1 << 24];
                else original = new byte[fis.available()];
                fis.read(original);

                // Кодируется блок
                byte[] encoded = FSEEncoder.encode(LZEncoder.encode(original));

                // Запмсывается размер закодированного блока
                for (int i = 0; i < 4; i++) {
                    fos.write((byte) ((encoded.length >> (i * 8)) & 255));
                }
                // Записываются закодированные данные
                fos.write(encoded);
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

        try (FileInputStream fis = new FileInputStream(fileName1);
             FileOutputStream fos = new FileOutputStream(fileName2)) {

            byte dataBlockCount = (byte) fis.read();


            while (dataBlockCount > 0) {
                int encodedDataSize = 0;
                for (int i = 0; i < 4; i++) {
                    byte b = (byte) fis.read();
                    encodedDataSize = encodedDataSize | ((b & 255) << (i * 8));
                }

                byte[] encodedBlock = fis.readNBytes(encodedDataSize);
                byte[] decodedBlock = LZDecoder.decode(FSEDecoder.decode(encodedBlock));

                fos.write(decodedBlock);
                dataBlockCount--;
            }
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