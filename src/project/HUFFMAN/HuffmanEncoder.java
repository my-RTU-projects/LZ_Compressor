package project.HUFFMAN;

import project.BitOutputStream;
import project.Encoder;
import project.Utils;

public class HuffmanEncoder extends Encoder {

    public byte[] encode(byte[] data) {
        int[] bytesFrequencies = Utils.getFrequencies(data);
        Utils.normalizeFrequencies(bytesFrequencies, Utils.M);
        HuffmanTreeNode huffmanTree = Utils.buildHuffmanTree(bytesFrequencies);
        String[] huffmanTable = new String[256];
        buildHuffmanTable(huffmanTree, huffmanTable, "");

        BitOutputStream bitOutputStream = new BitOutputStream(bytesFrequencies);
        for (int i = 0; i < 4; i++) {
            bitOutputStream.writeByte((byte) ((data.length >> (i * 8)) & 255));
        }

        for (byte b : data) {
            String bits = huffmanTable[b + 128];
            for (int i = 0; i < bits.length(); i++) {
                bitOutputStream.writeBit(bits.charAt(i) == '1');
            }
        }

        return bitOutputStream.toByteArray();
    }

    private static void buildHuffmanTable(HuffmanTreeNode huffmanTree, String[] table, String bits) {
        if (huffmanTree == null) return;

        // found a leaf node
        if (huffmanTree.left == null && huffmanTree.right == null ) {
            table[huffmanTree.b + 128] = bits;
        }

        buildHuffmanTable(huffmanTree.left, table, bits + "0");
        buildHuffmanTable(huffmanTree.right, table, bits + "1");
    }
}