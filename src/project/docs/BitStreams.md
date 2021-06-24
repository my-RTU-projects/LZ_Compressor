# BitStreams
Streams for reading / writing, which allow writing not only the whole bytes, but also individual bits using bitwise operations and pointers to the number of the current byte and the bit within the byte. I do not pretend that this solution is good, but this is what came to my mind.

## BitOutputStream
Stream for writing. To write bits and bytes, the user can use the writeBit(boolean bit) and writeByte(byte b) methods, respectively.

Based on ArrayList, as the structure must be extensible.

At the end of the recording, you can convert the stream to a byte array using the toByteArray() method. But since "empty" bits can remain, which can cause errors, it is possible to get the number of these extra bits. For this it is used
method flush(), it shifts the bit pointer by 0 and increments the byte counter, and also returns the number of these extra bits, which can be used after.

## BitInputStream
Stream for reading. To read bits and bytes, the user can use the readBit() and readByte() methods, respectively.

Based on a byte array, since extensibility is not needed here.

It can work in 2 modes: reading from the beginning or the end of the stream.

When creating an object, you need to pass an array of bytes, a boolean value (true - read from the beginning, false - read from the end) and the number of extra bits (obtained when using flush() in BitOutputStream). You can also transfer only an array, then the reading will be from the beginning of the stream, and the number of extra bits will be equal to 0.