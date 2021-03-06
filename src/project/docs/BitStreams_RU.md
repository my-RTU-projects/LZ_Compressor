# BitStreams
Потоки для чтения/записи, которые позволяют записывать не только байты целиком,
но и отдельные биты с помощью побитовых операций и указателей на номер текущего байта и бита внутри байта.
Не претендую на то, что это решение хорошее, но это то, что пришло мне в голову.

## BitOutputStream
Поток для записи. Для записи битов и байтов пользователь может использовать
методы writeBit(boolean bit) и writeByte(byte b) соответственно. 

Основан на ArrayList, так как структура должна быть расширяемой. 

В конце записи можно преобразовать поток в массив байт с помощью метода toByteArray().
Но так как могут остаться не "пустые" биты, что может вызвать ошибки, есть 
возможность получить количество этих лишних бит. Для этого используется 
метод flush(), он сдвигает указатель битов на 0 и увеличивает счетчик байтов, 
а также возвращает количество этих лишних битов, чо можно использовать после.

## BitInputStream
Поток для чтения. Для чтения битов и байтов пользователь может использовать
методы readBit() и readByte() соответственно.

Основан на байтовом массиве, так как расширяемость здесь не нужна.

Может работать в 2-х режимах: чтение с начала или конца потока.

При создании объекта нужно передать массив байт, булево значение 
(true - читать с начала, false - читать с конца) и количество лишних бит
(полученные при использовании flush() в BitOutputStream). Можно также передать
только массив, тогда чтение будет с начала потока, а количество лишних бит 
будет равно 0.