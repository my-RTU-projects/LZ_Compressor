# LZ
This algorithm is based on the well-known LZ77 algorithm, that is, to compress data in a data block, matches are searched for with those already encountered earlier and instead of
re-recording of these data, a link to them is left.

It is assumed that for the algorithm to work correctly, the block size does not exceed 2 ^ 24B, otherwise it may not work normally to refer to a match.

To implement the algorithm, the following classes were used:
- LZEncoder (encoding logic)
- LZDecoder (decoding logic)
- [BitInputStream](./BitStreams.md)
- [BitOutputStream](./BitStreams.md)
- Match (an object that stores information about the length of the match and the offset before the match from the current element)

Note: Primitive linear search is used to find the longest match. Most likely - this slows down the compression of large files. The findLongestMatch (byte[] data, int fromIndex) method in LZEncoder is responsible for this.

### Changes from standard LZ77
This implementation has some modifications. In order not to make references to too short matches (for example, only 1B long), 1 flag bit is set, which indicates which data will be further encoded (flag bit - 1) or not (flag bit - 0). In addition, the size of the link is dynamic, it can take 2 (1B each for the length and offset), 4 (2B each for the length and offset)
or 6 (3B each for length and offset) bytes. This is necessary in order not to write a 6B link when the length and offset do not exceed 2 ^ 8 and can fit in 2 bytes. For this, additional flags have been introduced: 00 = link 2B, 1 = link 4B, 01 = link 6B.
Statistically, the most common link size is 4B.

Note: max. link size 6B, 3B each for length and offset, or 24 bits each. Hence the maximum block size is 2^24B.

### Principle of operation
Starting from the first element:
1) Find the longest match since the current element
2) - If there is no match or it is too short, bit 0 is written and the current byte is unchanged, the element counter is incremented by 1.
    - Otherwise, bit 1 is written and the length and offset are estimated
        - If each of these values fits into 1B (does not exceed 2 ^ 8),
      then 2 bits are written 0 and 1B of length, 1B of offset
        - If each of these values fits into 2B,
      then 1 bit 1 and 2B lengths, 2B offsets are written
        - Otherwise, bits 01 and 3B of length, 3B offsets are written

     The item count is incremented by the length of the match.

3) While the counter indicator is less than the size of the data array, repeat.

**Total**: <br>
0 - next bit is not encoded <br>
100 - next bit is encoded, link size 2B <br>
11 - next bit is encoded, reference size is 4B <br>
101 - next bit encoded, reference size 6B <br>