// Kirils
class BitInputStream {
    byte[] bytes;
    private short currentBitNum;
    private int currentByteNum;

    // binary: ...1000 0000
    private final int BIT_SET_MASK = 128;

    public BitInputStream(byte[] bytes) {
        this.bytes = bytes;
        currentBitNum = 0;
        currentByteNum = 0;
    }

    public boolean readBit() {
        if (currentBitNum == 8)
            getNextByteFromList();

        byte currentByte = bytes[currentByteNum];
        boolean result = (currentByte & (BIT_SET_MASK >>> currentBitNum)) != 0;
        currentBitNum++;

        return result;
    }

    public byte readByte() {
        byte result = 0;

        for (int i = 0; i < 8; i++) {
            if (readBit())
                result = (byte) (result | (BIT_SET_MASK >>> i));
        }

        return result;
    }

    public boolean hasNextBit() {
        return currentByteNum < bytes.length - 1 || currentBitNum < 8;
    }

    public boolean hasNextByte() {
        return currentByteNum < bytes.length - 1 || currentBitNum == 0;
    }

    private void getNextByteFromList() {
        currentBitNum = 0;
        currentByteNum++;
    }
}
