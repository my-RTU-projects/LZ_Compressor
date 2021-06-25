
[RU](README_RU.md)    
[LV](README_LV.md)
#Compressor
This project was made for a university in the first year. The goal was maximum
compression without giving too much attention to speed. Probably during programming
many unicycle were "invented". So some parts may
seem ridiculous and stupid to more experienced people.

## General working principle
After choosing the algorithms and specifying the file names, the program 
writes to the file information about the algorithms used (no more than 4 
at a time, since information about the algorithms used and their order is 
written in one byte (2 bits per algorithm)) and starts reading the file 
in blocks, the size of which does not exceed 2 ^ 24B, each block goes 
through encoding stages in the specified order, its total size is written 
in 4B, after which the encoded block itself is written

### General archive format
Information about the algorithms used - 1B <br>
Compressed block 1 size (X bytes) - 4B <br>
Compressed block 1 - XB <br>
...................................... <br>
Compressed block size N (Y bytes) - 4B <br>
Compressed block N - YB <br>

### Algorithms applied
- Modified LZ77. The index for the record is 1 (01). [More details](src/project/docs/LZ.md)
- FSE. The index for the record is 2 (10). [More details](src/project/docs/FSE.md)
- Huffman. The index for the record is 3 (11). [More details](src/project/docs/HUFFMAN.md)


## Interaction with the program
When starting in a loop, you are prompted to enter one of the commands:
- comp: Compression. It will be offered to choose one or several (in this 
  case, the names must be written with the "+" symbol in the middle) algorithms for this. 
  After that, you must specify the name of the file that you want to 
  compress, and the name of the archive that will result.
- decomp: Recover the original file. You also need to specify the names of 
  the archive and the resulting file
- size: Allows you to find out the size of a file by its name
- equal: Compares 2 files and prints true if they are equal, false otherwise
- exit: Ends the loop