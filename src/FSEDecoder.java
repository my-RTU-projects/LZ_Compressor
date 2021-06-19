import java.util.ArrayList;
import java.util.Arrays;

public class FSEDecoder {
    static int p = 16;
    static final int M = 1 << p;

    public static byte[] decode(byte[] data) {

        // Читаем частотную таблицу
        int[] bytesFrequencies = readFrequencies(data);
        byte[] allocationTable = getAllocationTable(bytesFrequencies);
        int[] areaBegins = getAreaBegins(bytesFrequencies);

        // Читаем число закодированных байт
        int byteNum = 0;
        for (int i = 1024; i < 1028; i++) {
            byte b = data[i];
            byteNum = byteNum | ((b & 255) << (i * 8));
        }

        // колво лишних бит
        byte wasteBitsCount = data[data.length - 5];
        BitInputStream stream = new BitInputStream(Arrays.copyOfRange(data, 1028, data.length - 5), false, wasteBitsCount);

        byte[] result = new byte[byteNum];

        // Читаем конечное состояние
        int state = 0;
        for (int i = 0; i < 4; i++) {
            byte b = data[data.length - 4 + i];
            state = state | ((b & 255) << (i * 8));
        }

        // Декодируем
        for (int i = 0; i < byteNum - 1; i++) {
            result[i] = allocationTable[state];
            int freq = bytesFrequencies[allocationTable[state] + 128];
            int maxBit = getBitsMaximum(freq);

            int smallRangeCount = (freq - (M >> maxBit)) * 2;
            int byteNumInGroup = state - areaBegins[allocationTable[state] + 128];
            int inBitNum;
            int rangeBegin;
            if (smallRangeCount <= byteNumInGroup) {
                inBitNum = maxBit;
                rangeBegin = (byteNumInGroup - (smallRangeCount / 2)) << inBitNum;
            } else {
                inBitNum = maxBit - 1;
                rangeBegin = byteNumInGroup << inBitNum;
            }

            int offset = 0;
            for (int j = inBitNum - 1; j >= 0; j--) {
                int bit = stream.readBit() ? 1 : 0;
                offset = offset | (bit << j);
                //if (i < 10000 && i > 9980) System.out.print(bit);
            } //if (i < 10000 && i > 9980) System.out.println(" " + freq);

            state = rangeBegin + offset;
        }

        result[byteNum - 1] = allocationTable[state];
        return result;
    }

    private static int[] readFrequencies(byte[] data) {
        int[] bytesFrequencies = new int[256];
        for (int i = 0; i < 256; i++) {
            int freq = 0;
            for (int j = 0; j < 4; j++) {
                byte b = data[i * 4 + j];
                freq = freq | ((b & 255) << (j * 8));
            }
            bytesFrequencies[i] = freq;
        }
        return bytesFrequencies;
    }

    private static int[] getAreaBegins(int[] bytesFrequencies) {
        int[] areaBegins = new int[bytesFrequencies.length];
        int begin = 0;

        for (int i = 0; i < bytesFrequencies.length; i++) {
            areaBegins[i] = begin;
            begin += bytesFrequencies[i];
        }

        return areaBegins;
    }

    private static byte[] getAllocationTable(int[] bytesFrequencies) {
        byte[] allocationTable = new byte[M];

        int index = 0;
        for (int i = 0; i < bytesFrequencies.length; i++) {
            int freq = bytesFrequencies[i];
            for (int j = index; j < index + freq; j++) {
                allocationTable[j] = (byte) (i - 128);
            }
            index += freq;
        }

        return allocationTable;
    }

    private static int getBitsMaximum(int freq) {
        int freqHighBitNum = 1;
        while ((freq >> freqHighBitNum) > 0) freqHighBitNum++;
        freqHighBitNum--;
        return p - freqHighBitNum;
    }
}
