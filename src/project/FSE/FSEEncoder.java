package project.FSE;

import project.BitOutputStream;
import project.Encoder;
import project.Utils;

public class FSEEncoder extends Encoder {

    public byte[] encode(byte[] data) {
        // Находятся частоты байт и нормалтзуются так, чтобы их сумма ранялась 2^16 (M)
        int[] bytesFrequencies = Utils.getFrequencies(data);
        Utils.normalizeFrequencies(bytesFrequencies, data.length);
        // Предполагается, что одинаковые символы сгрупированны. Здесь находятся индексы с которых начинаются эти группы
        // (от 0 до М)

        int[] areaBegins = Utils.getAreaBegins(bytesFrequencies);

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
            int maxBit = Utils.getBitsMaximum(freq);

            // Определяется сколько бит записать в поток и номер интервала в котором находится состояние
            int rangeNum;
            int outBitsNum;
            if (Utils.M + state >= freq << maxBit) {
                outBitsNum = maxBit;
                rangeNum = (state >> outBitsNum) + freq - (Utils.M >> maxBit); // + количество больших поделенных интервалов
            } else {
                outBitsNum = maxBit - 1;
                rangeNum = state >> outBitsNum;
            }

            for (int j = 0; j < outBitsNum; j++) {
                bitOutputStream.writeBit((state & (1 << j)) > 0);
            }

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

}
