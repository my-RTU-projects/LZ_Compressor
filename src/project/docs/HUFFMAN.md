# Huffman code
David Huffman's entropy coding method.

To implement the algorithm, the following classes were used:
- HuffmanEncoder (coding logic)
- HuffmanDecoder (decoding logic)
- HuffmanTreeNode (tree node)
- [BitInputStream](./BitStreams.md)
- [BitOutputStream](./BitStreams.md)
- Utils (Some methods and constants are there to avoid duplication)

### Principle of operation
1) The frequencies of occurrence of bytes in the array are calculated and normalized so that the sum of the frequencies is equal to 2 ^ 16
2) A Huffman tree is built and, for convenience, a table based on it
3) The result is written the normalized frequency table and the initial size of the array
4) For each byte in the array, its bit code is found and written to the stream

### Format of encoded data
- Normalized frequency table - 512B (2 x 256)
- Initial block size - 4B
- Bits obtained as a result of encoding - NB