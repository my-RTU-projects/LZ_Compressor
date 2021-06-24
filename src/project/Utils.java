package project;

import project.HUFFMAN.HuffmanTreeNode;

import java.util.PriorityQueue;

public class Utils {
    // FSE only
    public static final int P = 16;
    public static final int M = 1 << P;

    public static int getBitsMaximum(int freq) {
        int freqHighBitNum = 1;
        while ((freq >> freqHighBitNum) > 0) freqHighBitNum++;
        // счет от 0
        freqHighBitNum--;
        return Utils.P - freqHighBitNum;
    }

    public static int[] getAreaBegins(int[] bytesFrequencies) {
        int[] areaBegins = new int[bytesFrequencies.length];
        int begin = 0;

        for (int i = 0; i < bytesFrequencies.length; i++) {
            areaBegins[i] = begin;
            begin += bytesFrequencies[i];
        }

        return areaBegins;
    }

    // Huffman only
    public static HuffmanTreeNode buildHuffmanTree(int[] byteFrequencies) {
        PriorityQueue<HuffmanTreeNode> pq = new PriorityQueue<>((left, right) -> left.freq - right.freq);

        for (int i = 0; i < byteFrequencies.length; i++) {
            if (byteFrequencies[i] != 0) {
                pq.add(new HuffmanTreeNode((short) (i - 128), byteFrequencies[i]));
            }
        }

        while (pq.size() != 1) {
            HuffmanTreeNode left = pq.poll();
            HuffmanTreeNode right = pq.poll();

            int sum = left.freq + right.freq;
            pq.add(new HuffmanTreeNode((short) 128, sum, left, right)); // 128 не существующий байт
        }

        return pq.peek();
    }




    public static int[] getFrequencies(byte[] data) {
        int[] bytesFrequencies = new int[256]; // ид - значение байта + 128

        for (byte b : data) {
            bytesFrequencies[b + 128]++;
        }

        return bytesFrequencies;
    }

    public static void normalizeFrequencies(int[] bytesFrequencies, int dataLength) {
        final double L = Utils.M / (double) dataLength;

        int debt = 0;

        int c = 0;
        int q = 0;
        for (int i = 1; i <= bytesFrequencies.length; i++) {
            c = c + bytesFrequencies[i - 1];
            int newQ = (int) Math.round(c * L);
            int w = newQ - q;
            q = newQ;
            if (bytesFrequencies[i - 1] > 0 && w == 0) {
                debt++;
                w++;
            }
            bytesFrequencies[i - 1] = w;
        }

        int i = 0;
        while (debt > 0) {
            if (i > bytesFrequencies.length - 1) i = i % bytesFrequencies.length;
            if (bytesFrequencies[i] > 1) {
                bytesFrequencies[i]--;
                debt--;
            }
            i += 10;
        }
    }

    public static int[] readFrequencies(byte[] data) {
        int[] bytesFrequencies = new int[256];
        for (int i = 0; i < 256; i++) {
            int freq = 0;
            for (int j = 0; j < 2; j++) {
                byte b = data[i * 2 + j];
                freq = freq | ((b & 255) << (j * 8));
            }
            bytesFrequencies[i] = freq;
        }
        return bytesFrequencies;
    }
}
