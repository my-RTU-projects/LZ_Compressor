package project.HUFFMAN;

import project.BitInputStream;
import project.Decoder;
import project.Utils;

import java.util.Arrays;

public class HuffmanDecoder extends Decoder {

    public byte[] decode(byte[] data) {
        int[] bytesFrequencies = Utils.readFrequencies(data);
        HuffmanTreeNode huffmanTree = Utils.buildHuffmanTree(bytesFrequencies);

        // Читаем число закодированных байт
        int byteNum = 0;
        for (int i = 512; i < 516; i++) {
            byte b = data[i];
            byteNum = byteNum | ((b & 255) << (i * 8));
        }

        byte[] result = new byte[byteNum];
        BitInputStream bitInputStream = new BitInputStream(Arrays.copyOfRange(data, 516, data.length));
        HuffmanTreeNode currentNode = huffmanTree;
        for (int i = 0; i < byteNum;) {
            if (currentNode.left == null && currentNode.right == null) {
                result[i] = (byte) currentNode.b;
                currentNode = huffmanTree;
                i++;
                continue;
            }

            boolean bit = bitInputStream.readBit();
            if (bit)
                currentNode = currentNode.right;
            else
                currentNode = currentNode.left;
        }

        return result;
    }
}
