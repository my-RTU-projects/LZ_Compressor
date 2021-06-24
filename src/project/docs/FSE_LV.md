# FSE
Entropijas kodēšanas metode. Ieviešanas pamatā ir [šis](https://habr.com/ru/company/playrix/blog/324116/) raksts.

Algoritma ieviešanai tika izmantotas šādas klases:
- FSEEncoder (kodēšanas loģika)
- FSEDecoder (dekodēšanas loģika)
- [BitInputStream](./BitStreams_LV.md)
- [BitOutputStream](./BitStreams_LV.md)
- Utils (Lai izvairītos no dublēšanās, seit ir dažas metodes un konstantes)

Piezīme: atšķirībā no raksta tiek izmantota tikai frekvenču normalizēšana bez Alias ​​tabulām.

### Kodētu datu formāts
- Normalizēta frekvences tabula - 512B (2 x 256)
- Sākotnējais bloka izmērs - 4B
- Biti, kas iegūti kodēšanas rezultātā - NB
- *"Atkritumu" bitu skaits - 1B
- Galīgais stāvoklis - 4B

*Atkritumu biti ir BitOutputStream problēma. Tā kā informācija tiek glabāta baitos un kodēšanas rezultātā pēdējais baits var nebūt pilnībā aizpildīts, paliek tukši biti, kas būs problēma dekodēšanas laikā. Dekodējot, biti tiek nolasīti no straumes beigām, un, lai izvairītos no problēmām, šī vērtība ir jānodod BitInputStream, lai tā izlaistu šo bitu skaitu un atgrieztu nepieciešamos bitus.