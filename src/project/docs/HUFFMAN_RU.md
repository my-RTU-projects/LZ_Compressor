# Код Хаффмана
Энтропийный метод кодирования Дэвида Хаффмана.

Для реализации алгоритма были использованы классы:
- HuffmanEncoder (логика кодирования)
- HuffmanDecoder (логика декодирования)
- HuffmanTreeNode (узел дерева)
- [BitInputStream](./BitStreams_RU.md)
- [BitOutputStream](./BitStreams_RU.md)
- Utils (Некоторые методы и константы находятся там для избегания дублирования)

### Принцип работы
1) Рассчитываются частоты встречаемости байт в массиве и нормализуются так, чтобы сумма
   частот была равной 2^16
2) Строится дерево Хаффмана и для удобства таблица на его основе
3) В результат записывается нормализованная частотная таблица и начальный размер массива
4) Для каждого байта в массиве находится его битовый код и записывается в поток

### Формат закодированных данных
- Нормализованная таблица частот - 512Б (2 х 256)
- Начальный размер блока - 4Б
- Биты полученные в результате кодирования - NБ
