# FSE
Entropy coding method. The implementation is based on [this](https://habr.com/ru/company/playrix/blog/324116/) article.

To implement the algorithm, the following classes were used:
- FSEEncoder (coding logic)
- FSEDecoder (decoding logic)
- [BitInputStream](./BitStreams.md)
- [BitOutputStream](./BitStreams.md)
- Utils (Some methods and constants are there to avoid duplication)

Note: in contrast to the article, just normalization of frequencies is used, without Alias ​​tables.

### Format of encoded data
- Normalized frequency table - 512B (2 x 256)
- Initial block size - 4B
- Bits obtained as a result of encoding - NB
- *Number of "garbage" bits - 1B
- Final state - 4B

*Garbage bits are a BitOutputStream problem. Since the information is stored in bytes, and as a result of encoding, the last byte may not be completely filled, empty bits remain, which will be a problem during decoding. When decoding, the bits are read from the end of the stream and, in order to avoid problems, this value must be passed to the BitInputStream so that it skips this number of bits and returns the necessary bits.