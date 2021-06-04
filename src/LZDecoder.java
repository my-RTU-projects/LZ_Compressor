import java.util.ArrayList;

// Kirils
class LZDecoder {

    public static byte[] decode(byte[] data) {
        BitInputStream stream = new BitInputStream(data);
        ArrayList<Byte> decodedData = new ArrayList<>();

        // В цикле из закодированных данных читаются флаги и, в зависимости от флагов, либо записывает в результат след.
        // байт без изменений, либо определяет размер ссылки, считывает её и декодирует
        while (true) {
            if (!stream.hasNextBit()) break;
            boolean flag = stream.readBit();

            if (flag) {

                int allocatedBytesNum = getAllocatedBytesNum(stream);
                Match match = getMatch(stream, allocatedBytesNum);

                int length = match.getLength();
                int dist = match.getDistance();
                for (int i = 0; i < length; i++) {
                    byte elem = decodedData.get(decodedData.size() - dist);
                    decodedData.add(elem);
                }
            } else {
                if (!stream.hasNextByte()) break;
                decodedData.add(stream.readByte());
            }
        }

        byte[] decodedDataArray = new byte[decodedData.size()];
        for (int i = 0; i < decodedData.size(); i++)
            decodedDataArray[i] = decodedData.get(i);

        return decodedDataArray;
    }

    // Этот метод считывает биты и определяет сколько байт выделенно под запись длины и смещения в ссылке
    private static int getAllocatedBytesNum(BitInputStream stream) {
        boolean a = stream.readBit();
        if (a) return 2;
        else {
            boolean b = stream.readBit();
            return b ? 3 : 1;
        }
    }

    // Этот метод читает нужное кол-во байт из потока и преобразует их в ссылку на совпадение
    private static Match getMatch(BitInputStream stream, int allocatedBytesNum) {
        int length = 0;
        for (int i = 0; i < allocatedBytesNum; i++) {
            byte b = stream.readByte();
            length = length | ((b & 255) << (i * 8));
        }

        int dist = 0;
        for (int i = 0; i < allocatedBytesNum; i++) {
            byte b = stream.readByte();
            dist = dist | ((b & 255) << (i * 8));
        }

        return new Match(length, dist);
    }
}
