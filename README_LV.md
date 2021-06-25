[RU](README_RU.md)    
[EN](README.md)
#Compressor
Šis projekts tika izstrādāts universitātei pirmajā kursā. Mērķis bija maksimāli saspiests, nepievēršot uzmanību ātrumam. Iespējams, programmēšanas laikā daudzi vienriteņi tika "izdomāti". Tāpēc pieredzējušākiem cilvēkiem dažas daļas var šķist smieklīgas un dumjas.

## Vispārējs darba princips
Pēc algoritmu izvēles un failu nosaukumu norādes programma ieraksta informāciju par izmantotajiem algoritmiem (ne vairāk kā 4 vienlaicīgi, jo informācija par izmantotajiem algoritmiem un ta secība tiek ierakstīta vienā baitā (2 biti uz algoritmu)) un sāk lasīt failu pa blokiem, kuru izmērs nepārsniedz 2 ^ 24B, katrs bloks iet caur kodēšanas posmiem norādītajā secībā, tā galīgais lielums ir rakstīts 4B,
pēc kura tiek uzrakstīts pats kodētais bloks

### Vispārīgs faila formāts
Info par izmantotajiem algoritmiem - 1B <br>
Saspiestā bloka 1 lielums (X baiti) - 4B <br>
Saspiests bloks 1 - XB <br>
...................................... <br>
Saspiestā bloka N lielums (Y baiti) - 4B <br>
Saspiests bloks N - YB <br>

### Lietoti algoritmi
- Modificētais LZ77. Ieraksta indekss - 1 (01). [Papildinformācija](src/project/docs/LZ.md)
- FSE. Ieraksta indekss - 2 (10). [Papildinformācija](src/project/docs/FSE_LV.md)
- Huffman. Ieraksta indekss - 3 (11). [Papildinformācija](src/project/docs/HUFFMAN_LV.md)

## Mijiedarbība ar programmu
Kad tiek palaists ciklā, tiek ieteikts ievadīt vienu no komandām:
- comp: saspiešana. Tam tiks piedāvāts izvēlēties vienu vai vairākus (šajā gadījumā jāraksta simbolu "+" starp nosaukumiem). Pēc tam jānorāda faila nosaukumu, kuru vēlaties saspiest, un arhīva nosaukums, kas tiks iegūts.
- decomp: atgūt sākotnējo failu. Arī jānorāda arhīva un iegūtā faila nosaukumi
- size: ļauj uzzināt faila lielumu pēc tā nosaukuma
- equal: salīdzina 2 failus un izdrukā true, ja tie ir vienādi, citādi - false
- exit: Beidzas cilpa