# Laborator 09 — Prezentare (explicații pe cod)

## Fișiere

| Fișier | Scop (5–7 cuvinte) |
|--------|---------------------|
| `exercise1/TipTranzactie.java` | Enum pentru sensul tranzacției: credit sau debit bancar. |
| `exercise1/Tranzactie.java` | Model serializabil cu câmpuri persistente și notă exclusă din flux. |
| `exercise1/Main.java` | Citește tranzacții, salvează și reîncarcă lista, execută comenzi pe ea. |
| `exercise2/Main.java` | Scrie înregistrări binare fixe și le citește sau actualizează selectiv. |
| `exercise3/CoadaTranzactii.java` | Coadă limită partajată între fire, cu sincronizare și așteptare condiționată. |
| `exercise3/ATMThread.java` | Fir producător care generează patru tranzacții și le pune în coadă. |
| `exercise3/ProcessorThread.java` | Fir consumator care scoate tranzacții din coadă și le „procesează”. |
| `exercise3/Main.java` | Pornește producătorii și consumatorul, apoi le oprește în ordinea cerută. |

---

## exercise1 — linii importante

**`Tranzactie.java`** — *model serializabil cu câmpuri persistente și notă exclusă din flux*

- **L5–6:** `implements Serializable` + `serialVersionUID = 1L` — versiune stabilă a clasei la deserializare.
- **L14:** `private transient String note` — `transient` exclude câmpul din flux; după `readObject` valoarea devine cea implicită (`null` pentru referințe), indiferent ce fusese înainte de scriere.

**`Main.java`** — *citește tranzacții, salvează și reîncarcă lista, execută comenzi pe ea*

- **L10:** `useLocale(Locale.US)` — `nextDouble()` interpretează punctul zecimal ca la `en_US`, aliniat la formatul testelor.
- **L21:** `setNote("procesat")` — înainte de serializare nota are valoare; după deserializare dispare din flux (vezi `transient`).
- **L25–27:** `mkdirs()` + `try (ObjectOutputStream ...)` — creează directorul `output/` dacă lipsește; `try-with-resources` închide fluxul după `writeObject(lista)`.
- **L31–34:** `ObjectInputStream` + cast la `List<Tranzactie>` — reconstruiește aceeași structură (inclusiv ordinea elementelor din `ArrayList`).
- **L43–54:** `FILTER` — compară prefix `yyyy-MM` cu `data.startsWith(prefix)`; dacă niciun rând, afișează mesajul cerut.
- **L55–67:** `NOTE id` — afișează `getNote()` după deserializare (de obicei `null`) sau `not found` dacă id lipsă.
- **L72–74:** `String.format(Locale.US, ...)` — sumă cu punct zecimal, format fix pentru output.

---

## exercise2 — linii importante

**`Main.java`** — *scrie înregistrări binare fixe și le citește sau actualizează selectiv*

- **L12:** `RECORD_SIZE = 32` — fiecare înregistrare ocupă exact 32 octeți; index × 32 = offset în fișier.
- **L26–27:** `dos.write(leInt(id))` / `leDouble(suma)` — `DataOutputStream.write` scrie tablouri de octeți; `ByteBuffer` cu `LITTLE_ENDIAN` produce reprezentarea cerută pentru `int` și `double` (spre deosebire de endianness implicit Java la alte API-uri).
- **L29–33:** Data 10 caractere ASCII + tip 1 octet (0/1) + status 1 octet (inițial 0 = PENDING) + 8 octeți zero — layout-ul din enunț.
- **L38:** `RandomAccessFile(..., "rw")` — același fișier pentru citire și poziționare la scriere selectivă.
- **L47–54:** `seek(idx * RECORD_SIZE + 23)` — sare la octetul de status al înregistrării `idx`; `writeByte` suprascrie un singur octet fără a rescrie tot fișierul.
- **L65–70:** `leInt` / `leDouble` — `ByteBuffer.allocate` + `order(LITTLE_ENDIAN)` + `putInt`/`putDouble` + `array()` — conversie explicită în 4, respectiv 8 octeți.
- **L73–81:** `pad10` — copiază maxim 10 octeți din string, restul poziții umplute cu spațiu (byte 32).
- **L84–103:** `printRecord` — `seek` la începutul înregistrării, `readFully` 32 octeți, `ByteBuffer.wrap` cu little-endian pentru citire `id`, `suma`, 10 octeți dată, tip, status; `& 0xFF` pe octeți citiți ca `byte` pentru valori 0–255; `switch` mapează coduri status la text.

---

## exercise3 — linii importante

**`CoadaTranzactii.java`** — *coadă limită partajată între fire, cu sincronizare și așteptare condiționată*

- **L12:** `synchronized` pe metodă — lock pe `this`; toate apelurile la coadă trec serial prin același monitor.
- **L13–16:** `while (size >= CAPACITATE)` + `wait()` — așteaptă în buclă (nu `if`) ca după trezire să reverifice plinul (evită „lost wakeup” / stări false).
- **L17–18:** `addLast` + `notifyAll()` — eliberează eventualii consumatori blocați pe coadă goală.
- **L24–33:** `extrageSauTermina` — dacă coadă goală și `procesorActiv` e fals, returnează `null` (ieșire din bucla consumatorului); altfel `wait()` până apare element; `removeFirst` + `notifyAll()` pentru producători blocați la plin.

**`ATMThread.java`** — *fir producător care generează patru tranzacții și le pune în coadă*

- **L20–30:** bucla fixă 4 iterații — construiește `Tranzactie` cu iduri distincte per ATM (`startId`); `adauga` poate bloca în `wait` dacă banda e plină.
- **L32:** `Thread.sleep(50)` — întârziere între trimiteri (demonstrativ, nu pentru corectitudine logică).

**`ProcessorThread.java`** — *fir consumator care scoate tranzacții din coadă și le „procesează”*

- **L9:** `volatile boolean activ` — modificarea din alt fir (în `Main`) este vizibilă imediat consumatorului, fără cache stale.
- **L18–22:** bucla infinită până `extrageSauTermina` întoarce `null` — semnal că procesarea s-a oprit și coada e goală.

**`Main.java`** — *pornește producătorii și consumatorul, apoi le oprește în ordinea cerută*

- **L12–17:** `start()` pe `ATMThread` și pe `Thread(processorThread)` — `Runnable` rulează pe fir nou doar prin constructorul `Thread` + `start`, nu prin `run()` direct.
- **L19–21:** `join()` pe ATM-uri — firul principal așteaptă terminarea tuturor producătorilor înainte de a opri consumatorul.
- **L23–26:** `synchronized (coada)` — același lock ca metodele `synchronized` din `CoadaTranzactii`; setează `activ = false` și `notifyAll()` ca un fir blocat în `wait` pe coadă să reevalueze condiția și să poată ieși cu `null`.
