package project;


public class BitInputStream {
    byte[] bytes;
    private short currentBitNum;
    private int currentByteNum;

    private boolean readFromBegin;

    // binary: ...1000 0000
    private final int BIT_SET_MASK = 128;

    public BitInputStream(byte[] bytes, boolean readFromBegin, int wasteBitsCount) {
        this.bytes = bytes;
        this.readFromBegin = readFromBegin;
        if (readFromBegin) {
            currentBitNum = 0;
            currentByteNum = 0;
        } else {
            currentBitNum = 7;
            currentByteNum = bytes.length - 1;
            for (int i = 0; i < wasteBitsCount; i++) {
                this.readBit();
            }
        }
    }

    public BitInputStream(byte[] bytes) {
        this(bytes, true, 0);
    }

    public boolean readBit() {
        boolean result;
        if (readFromBegin) {
            if (currentBitNum == 8)
                getNextByteFromList();

            byte currentByte = bytes[currentByteNum];
            result = (currentByte & (BIT_SET_MASK >>> currentBitNum)) != 0;
            currentBitNum++;
        } else {
            if (currentBitNum == -1)
                getNextByteFromList();

            byte currentByte = bytes[currentByteNum];
            result = (currentByte & (BIT_SET_MASK >>> currentBitNum)) != 0;
            currentBitNum--;
        }
        return result;
    }

    public byte readByte() {
        byte result = 0;

        if (readFromBegin) {
            for (int i = 0; i < 8; i++) {
                if (readBit())
                    result = (byte) (result | (BIT_SET_MASK >>> i));
            }
        } else {
            for (int i = 7; i >= 0; i--) {
                if (readBit())
                    result = (byte) (result | (BIT_SET_MASK >>> i));
            }
        }

        return result;
    }

    public boolean hasNextBit() {
        return (readFromBegin && (currentByteNum < bytes.length - 1 || currentBitNum < 8)) ||
                (!readFromBegin && (currentByteNum > 0 || currentBitNum > 0));
    }

    public boolean hasNextByte() {
        return (readFromBegin && (currentByteNum < bytes.length - 1 || currentBitNum == 0)) ||
                (!readFromBegin && (currentByteNum > 0 || currentBitNum == 7));
    }

    private void getNextByteFromList() {
        if (readFromBegin) {
            currentBitNum = 0;
            currentByteNum++;
        } else {
            currentBitNum = 7;
            currentByteNum--;
        }
    }
}
