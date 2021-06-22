package project;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.stream.Collectors;

// Kirils
public class BitOutputStream {
    private ArrayList<Byte> bytes;
    private short currentBitNum;
    private byte currentByte;

    // binary: ...1000 0000
    private final int BIT_SET_MASK = 128;

    public BitOutputStream() {
        bytes = new ArrayList<>();
        currentBitNum = 0;
        currentByte = 0;
    }

    public BitOutputStream(int[] startValues) {
        this();
        for (int value : startValues)
            for (int i = 0; i < 2; i++)
                bytes.add((byte) ((value >> (i * 8)) & 255));
    }

    public void writeBit(boolean bit) {
        if (currentBitNum == 8)
            pushFullByteInList();

        if (bit)
            currentByte = (byte) (currentByte | (BIT_SET_MASK >>> currentBitNum));

        currentBitNum++;
    }

    public void writeByte(byte bait) {
        byte mask = 1;
        for (int i = 0; i < 8; i++) {
            writeBit((bait & (BIT_SET_MASK >>> i)) != 0);
        }
    }

    private void pushFullByteInList() {
        bytes.add(currentByte);
        currentBitNum = 0;
        currentByte = 0;
    }

    public int flush() {
        int wasteBits = 8 - currentBitNum;
        bytes.add(currentByte);
        currentByte = 0;
        currentBitNum = 0;
        return wasteBits;
    }

    public byte[] toByteArray() {
        int size = currentBitNum == 0 ? bytes.size() : bytes.size() + 1;
        byte[] array = new byte[size];

        for (int i = 0; i < bytes.size(); i++) {
            array[i] = bytes.get(i);
        }

        if (currentBitNum != 0)
            array[size - 1] = currentByte;

        return array;
    }
}
