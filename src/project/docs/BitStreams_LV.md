# BitStreams
Straumes lasīšanai / rakstīšanai, kas ļauj rakstīt ne tikai visus baitus, bet arī atsevišķus bitus, izmantojot bitu operācijās un norādes uz pašreizējā baita skaitu un bitu baitā. Es neizliekos, ka šis risinājums ir labs, bet tas man ienāca prātā.

## BitOutputStream
Straume ierakstīšanai. Lai rakstītu bitus un baitus, lietotājs var izmantot attiecīgi metodes writeBit (boolean bit) un writeByte (byte b).

Pamatā ir ArrayList, jo struktūrai jābūt paplašināmai.

Ieraksta beigās straumi var pārveidot par baitu masīvu, izmantojot toByteArray () metodi. Bet, tā kā var palikt “tukši” biti, kas var izraisīt kļūdas, ir iespēja iegūt šo lieko bitu skaitu. Šim nolūkam tiek izmantots method flush (), tas pārvieto bitu rādītāju par 0 un palielina baitu skaitītāju, kā arī atgriež šo papildu bitu skaitu, kurus var izmantot pēc.

## BitInputStream
Straume lasīšanai. Lai lasītu bitus un baitus, lietotājs var izmantot attiecīgi readBit() un readByte() metodes.

Balstās uz baitu masīvu, jo paplašināmība šeit nav vajadzīga.

Tas var darboties divos režīmos: lasīt no straumes sākuma vai beigām.

Veidojot objektu,  jānodod baitu masīvu, būla vērtību (true - lasīt no sākuma, false - lasīt no beigām) un papildu bitu skaits (kas iegūti, izmantojot flush() BitOutputStream). Var arī pārsūtīt tikai masīvu, tad lasīšana būs no straumes sākuma, un papildu bitu skaits būs vienāds ar 0.