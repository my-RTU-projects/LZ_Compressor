import java.util.ArrayList;

// Kirils
class BitOutputStream {
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
            if ((bait & (BIT_SET_MASK >>> i)) != 0) {
                writeBit(true);
            } else {
                writeBit(false);
            }
        }
    }

    private void pushFullByteInList() {
        bytes.add(currentByte);
        currentBitNum = 0;
        currentByte = 0;
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
