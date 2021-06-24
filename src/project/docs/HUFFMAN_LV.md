# Huffmana kods
Deivida Hufmana entropijas kodēšanas metode.

Algoritma ieviešanai tika izmantotas šādas klases:
- HuffmanEncoder (kodēšanas loģika)
- HuffmanDecoder (dekodēšanas loģika)
- HuffmanTreeNode (koka mezgls)
- [BitInputStream](./BitStreams_LV.md)
- [BitOutputStream](./BitStreams_LV.md)
- Utils (Lai izvairītos no dublēšanās, ir dažas metodes un konstantes)

### Darbības princips
1) Baitu sastopamības biežums masīvā tiek aprēķināts un normalizēts tā, lai frekvenču summa būtu vienāda ar 2 ^ 16
2) Tiek uzbūvēts Huffmana koks un ērtības labad uz tā balstīta tabula
3) Rezultātā tiek uzrakstīta normalizētā frekvences tabula un masīva sākotnējais lielums
4) Katram masīva baitam tiek atrasts un ierakstīts straumē tā bitu kods

### Kodētu datu formāts
- Normalizēta frekvences tabula - 512B (2 x 256)
- Sākotnējais bloka izmērs - 4B
- biti, kas iegūti kodēšanas rezultātā - NB