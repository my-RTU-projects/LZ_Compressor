class LZEncoder {

    // Kirils
    public static byte[] encode(byte[] data) {
        BitOutputStream encodedData = new BitOutputStream();

        // Индекс следующего байта для кодирования
        int i = 0;
        while (i < data.length) {
            Match match = findLongestMatch(data, i);

            // Если совпадений нет или найденное слишком короткое ставится флаг 0 и записывается 1 байт в незакодированном виде
            if (match == null || match.getLength() <= 4) {
                encodedData.writeBit(false);
                encodedData.writeByte(data[i]);
                i++;

                // Иначе записывается бит 1 и в зависимости от размера смещения и длины под ссылку выделяется определённое кол-во байт,
                // а в результат записывается еще 1 или 2 бита (00 - 2Б, 1 - 4Б, 01 - 6Б)
            } else {
                encodedData.writeBit(true);
                int allocatedBytesNum;
                if (match.getDistance() < (1 << 8) && match.getLength() < (1 << 8)) {
                    allocatedBytesNum = 1;
                    encodedData.writeBit(false);
                    encodedData.writeBit(false);
                } else if (match.getDistance() < (1 << 16) && match.getLength() < (1 << 16)) {
                    allocatedBytesNum = 2;
                    encodedData.writeBit(true);
                } else {
                    allocatedBytesNum = 3;
                    encodedData.writeBit(false);
                    encodedData.writeBit(true);
                }

                // Записываем длину в результат
                int length = match.getLength();
                for (int j = 0; j < allocatedBytesNum; j++) {
                    encodedData.writeByte((byte) (length >>> (j * 8)));
                }

                // Записываем смещение в результат
                int distance = match.getDistance();
                for (int j = 0; j < allocatedBytesNum; j++) {
                    encodedData.writeByte((byte) (distance >>> (j * 8)));
                }

                i += length;
            }
        }

        return encodedData.toByteArray();
    }

    private static Match findLongestMatch(byte[] data, int fromIndex) throws IllegalArgumentException {

        if (data.length <= fromIndex) throw new IllegalArgumentException();

        int length = 0;
        int distance = 0;


        int i = 0;
        while (i < fromIndex) {
            int count = 0;
            int j = i;
            int k = fromIndex;

            boolean hvz = true;
            //2.cikls salīdzina ar puses, labo pusi katru reizi bīdot 1 pa labi
            while (hvz) {
                //ja sakrīt palielina count+1 un pārbauda nākošos divus ja tie nav pēdējie
                if (data[j] == data[k]) {
                    count++;
                    if (k == data.length - 1)
                        hvz = false;
                    else {
                        j++;
                        k++;
                    }
                } else hvz = false;
            }

            if (count > length) {
                length = count;
                distance = fromIndex - i;
            }

            i++;
        }

        return length == 0 ? null : new Match(length, distance);
    }
}
