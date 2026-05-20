# Laborator 10 — Prezentare (explicații pe cod)

## Fișiere

| Fișier | Scop (5–7 cuvinte) |
|--------|---------------------|
| `exercise1/TipTranzactie.java` | Enum care codifică dacă tranzacția este credit sau debit. |
| `exercise1/Tranzactie.java` | Obiect imutabil reprezentând o linie de tranzacție pentru afișare. |
| `exercise1/Main.java` | Interpret comenzi text pe o coadă implementată cu LinkedList. |
| `exercise2/Main.java` | Agregări, sortări și rapoarte pe o listă care poate conține duplicate. |
| `exercise3/Main.java` | Demonstrații Stream API pe date fixe și subclasă cu cont sursă. |

---

## exercise1 — linii importante

**`Tranzactie.java`** — *obiect imutabil reprezentând o linie de tranzacție pentru afișare*

- **L6–9:** câmpuri `private final` — obiect imutabil după construire; potrivit pentru `LinkedList` și sortări fără surprize de stare.
- **L11–16:** constructor — setează toate câmpurile; folosit din `readTx` în `Main`.
- **L34–37:** `toString()` — `String.format(Locale.US, ...)` pentru exact două zecimale la sumă și numele enum-ului în text (`CREDIT` / `DEBIT`).

**`Main.java`** — *interpret comenzi text pe o coadă implementată cu LinkedList*

- **L7:** `LinkedList<Tranzactie>` — structură deque: `addFirst` / `addLast` / `removeFirst` în O(1) la capete.
- **L13–15:** `ENQUEUE` — `readTx` + `addLast` (coadă FIFO la capătul din dreapta).
- **L17–19:** `PUSH` — `addFirst` (stivă / inserare la capătul din stânga).
- **L21–26 / L28–33:** `DEQUEUE` vs `POP` — ambele apelează `removeFirst()`, diferă doar prefixul mesajului (`Procesat:` vs `Extras:`); la listă goală același text `Coada goala.`.
- **L35–44:** `REMOVE_DEBIT` — `Iterator<Tranzactie>` din `coada.iterator()`; în buclă `itr.next()` testează tipul, `itr.remove()` șterge elementul curent fără `ConcurrentModificationException` (spre deosebire de enhanced-for + `remove` pe listă).
- **L46–56:** `REMOVE_BELOW` — același pattern iterator; prag citit cu `nextDouble()`; contor `n` pentru mesaj.
- **L70–75:** `readTx` — `TipTranzactie.valueOf` transformă tokenul text în enum.

---

## exercise2 — linii importante

**`Main.java`** — *agregări, sortări și rapoarte pe o listă care poate conține duplicate*

- **L13–16:** `ArrayList` păstrează **toate** liniile citite, inclusiv duplicate după `id` (cerință).
- **L22–25:** `UNIQUE_IDS` — stream `map(getId)` + `collect(toCollection(LinkedHashSet::new))`: păstrează ordinea primei apariții a fiecărui id și elimină duplicatele din mulțimea afișată.
- **L28–37:** `MONTHLY_REPORT` — `TreeMap` sortează cheile `yyyy-MM` lexicografic (= cronologic); `double[0]` / `[1]` = sume CREDIT / DEBIT; bucla for adună per tranzacție în funcție de `getTip()`.
- **L38–43:** afișare lună cu ambele sume, inclusiv `0.00` dacă un tip lipsește în luna respectivă.
- **L45–53:** `TOP k` — **copie** `new ArrayList<>(lista)`, sortare descrescătoare după sumă, primele `min(k, size)` — lista originală `lista` nu se modifică.
- **L55–71:** `SORT_ASC` / `SORT_DESC` / `REVERSE` — modifică direct `lista` (`sort` / `reverse`) apoi tipărește.
- **L73–77:** `MIN_MAX` — `Collections.min` / `max` cu același `Comparator.comparingDouble(getSuma)`.
- **L79–86:** `CME_DEMO` — intentionally `for (Tranzactie t : lista) lista.remove(t)` — la prima iterație JVM detectează modificare structurală în timpul foreach → `ConcurrentModificationException` prinsă în `catch`.

---

## exercise3 — linii importante

**`Main.java`** — *demonstrații Stream API pe date fixe și subclasă cu cont sursă*

- **L14–25:** clasă internă `Tx extends Tranzactie` — adaugă `contSursa` fără a modifica clasa din exercise1; constructorul apelează `super(...)`.
- **L41–43:** `stream().filter(tip == CREDIT)` — lanț lazy până la `forEach`, care consumă streamul.
- **L45–46:** `mapToDouble(getSuma).sum()` — primitive stream, sumă fără boxing acumulat.
- **L50–54:** `groupingBy(cheie lună, TreeMap::new, summingDouble)` — cheie `substring(0,7)`; `TreeMap` furnizează ordinea cheilor; downstream adună sumele.
- **L59–61:** `sorted(...).reversed().limit(3)` — top după sumă, maxim 3 elemente.
- **L65–69:** `map(getContSursa).distinct().sorted().collect(toList())` — proiecție pe cont, deduplicare, sortare alfabetică.
- **L72–73:** `average().orElse(0)` — `OptionalDouble` evitat pentru afișare când lista e nevidă aici.
- **L77–85:** al doilea `groupingBy` fără downstream numeric — valoare `List<Tx>` per lună; apoi `stream` pe intrări sortate și sumă + număr pentru extras.
