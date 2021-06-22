package project.HUFFMAN;

public class HuffmanTreeNode {
    short b;
    public int freq;
    HuffmanTreeNode left;  // 0
    HuffmanTreeNode right; // 1

    public HuffmanTreeNode(short b, int freq) {
        this.b = b;
        this.freq = freq;
    }

    public HuffmanTreeNode(short b, int freq, HuffmanTreeNode left, HuffmanTreeNode right) {
        this.b = b;
        this.freq = freq;
        this.left = left;
        this.right = right;
    }
}
