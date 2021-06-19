public class FSEEncoder {
    static int p = 16;
    static final int M = 1 << p;

    public static byte[] encode(byte[] data) {
        // Находятся частоты байт и нормалтзуются так, чтобы их сумма ранялась 2^16 (M)
        int[] bytesFrequencies = getFrequencies(data);
        normalizeFrequencies(bytesFrequencies, data.length);
        // Предполагается, что одинаковые символы сгрупированны. Здесь находятся индексы с которых начинаются эти группы
        // (от 0 до М)
        int[] areaBegins = getAreaBegins(bytesFrequencies);

        // Создаётся поток бит в который сразу записывается таблица частот
        BitOutputStream bitOutputStream = new BitOutputStream(bytesFrequencies);
        // Записываем число байт
        for (int i = 0; i < 4; i++) {
            bitOutputStream.writeByte((byte) ((data.length >> (i * 8)) & 255));
        }

        // Начальное состояние - индекс первого такого байта в таблиц распределения
        int state = areaBegins[data[data.length - 1] + 128];

        for (int i = data.length - 2; i >= 0; i--) {
            int freq = bytesFrequencies[data[i] + 128];
            // Для каждого уникального байта подразумевается ещё одна таблица (размером М), которая делится на freq
            // инервалов размерами maxbit - 1 и maxbit (сначала идут те, что меньше)
            int maxBit = getBitsMaximum(freq);

            // Определяется сколько бит записать в поток и номер интервала в котором находится состояние
            int rangeNum;
            int outBitsNum;
            if (M + state >= freq << maxBit) {
                outBitsNum = maxBit;
                rangeNum = (state >> outBitsNum) + freq - (M >> maxBit); // + количество больших поделенных интервалов
            } else {
                outBitsNum = maxBit - 1;
                rangeNum = state >> outBitsNum;
            }

            for (int j = 0; j < outBitsNum; j++) {
                bitOutputStream.writeBit((state & (1 << j)) > 0);
                //if (i < 10000 && i > 9980) System.out.print((state & (1 << j)) > 0 ? 1 : 0);
            } //if (i < 10000 && i > 9980) System.out.println(" " + freq);

            // Определяется новое состояние
            state = areaBegins[data[i] + 128] + rangeNum;
        }

        // Записывается конечное состояние
        int wasteBit = bitOutputStream.flush();
        bitOutputStream.writeByte((byte) (wasteBit));

        for (int i = 0; i < 4; i++) {
            bitOutputStream.writeByte((byte) ((state >> (i * 8)) & 255));
        }

        return bitOutputStream.toByteArray();
    }

    private static int getBitsMaximum(int freq) {
        int freqHighBitNum = 1;
        while ((freq >> freqHighBitNum) > 0) freqHighBitNum++;
        // счет от 0
        freqHighBitNum--;
        return p - freqHighBitNum;
    }

    private static int[] getFrequencies(byte[] data) {
        int[] bytesFrequencies = new int[256]; // ид - значение байта + 128

        for (byte b : data) {
            bytesFrequencies[b + 128]++;
        }

        return bytesFrequencies;
    }

    // TODO Отслеживать потери частот, не допускать 0, если его не было изначально
    private static void normalizeFrequencies(int[] bytesFrequencies, int dataLength) {
        final double L = M / (double) dataLength;

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
            if (bytesFrequencies[i] > 256) {
                bytesFrequencies[i]--;
                debt--;
            }
            i += 10;
        }
    }

    private static int[] getAreaBegins(int[] bytesFrequencies) {
        int[] areaBegins = new int[bytesFrequencies.length];
        int begin = 0;

        for (int i = 0; i < bytesFrequencies.length; i++) {
            areaBegins[i] = begin;
            begin += bytesFrequencies[i];
        }

        return areaBegins;
    }
}
