# Laborator 11 — Prezentare (explicații pe cod)

## Fișiere

| Fișier | Scop (5–7 cuvinte) |
|--------|---------------------|
| `exercise1/Transaction.java` | Înregistrare imutabilă cu datele unei tranzacții bancare de analizat. |
| `exercise1/Main.java` | Calculează scor de risc, verdict și răspunde la comenzi pe tranzacții. |
| `exercise2/Main.java` | Rapoarte lunare, pe cont și clasament canale cu Stream API. |
| `exercise3/Main.java` | Colector custom care produce un snapshot imutabil pentru interogări. |

---

## exercise1 — linii importante

**`Transaction.java`** — *înregistrare imutabilă cu datele unei tranzacții bancare de analizat*

- **Câmpuri `private final` + constructor** — obiectul nu își schimbă datele după creare; potrivit pentru chei în `Map` și liste de citire.

**`Main.java`** — *calculează scor de risc, verdict și răspunde la comenzi pe tranzacții*

- **L17:** `FLAG_THRESHOLD = 60` — prag fix pentru trecerea între `ALLOW` și `FLAG` după scor.
- **L19–30:** `HIGH_RISK_COUNTRIES` + `CHANNEL_SCORE` — date de configurare; `static` block umple map-ul de punctaj per canal.
- **L32–38:** `Predicate<Transaction>` denumite — expresii lambda reutilizabile (`amountOverThreshold`, `countryInRisk`, `channelSuspicious`) pentru compoziție / claritate (regulile din enunț Part A).
- **L41–42:** `Comparator` compus — `comparingInt(Main::riskScore).reversed()` apoi `thenComparingInt(Transaction::getId)` — sortare deterministă: scor descendent, la egalitate id crescător.
- **L91:** `flaggedRule = tx -> riskScore(tx) >= FLAG_THRESHOLD` — predicat derivat strict din scor (aceeași regulă ca la verdict).
- **L103–115:** `CHECK` — lookup în `byId`; dacă absent, `NOT_FOUND`; altfel `riskScore` + `verdict(score)` în mesaj.
- **L118–132:** `LIST_FLAGGED` — colectează tranzacțiile pentru care `flaggedRule.test(t)` e adevărat, sortează cu `BY_RISK_DESC_THEN_ID_ASC`, listează sau `NONE`.
- **L135–146:** `TOP_RISK k` — copiază `all`, sortează cu același comparator, limitează la `max(0, min(k, size))` — `k=0` nu tipărește linii.
- **L166–186:** `riskScore` — ramuri `if / else if` pentru trepte sumă (≥5000, ≥1000, ≥500); apoi **independent** `if (amount <= 100) score += 5`; apoi `+25` dacă țara e în setul de risc; apoi `CHANNEL_SCORE.getOrDefault(channel, 0)`.
- **L189–190:** `verdict` — compară scorul cu pragul 60, fără logică duplicată în alte locuri.
- **L193–195:** `formatRiskLine` — reutilizează `riskScore` și `verdict` pentru consistență între `CHECK`, listă și top.

---

## exercise2 — linii importante

**`Main.java`** — *rapoarte lunare, pe cont și clasament canale cu Stream API*

- **L41–47:** constructor `Tx` — `super(...)` populează câmpurile din `Transaction`; `accountId` e câmp suplimentar pentru raportul pe cont.
- **L61–68:** `REPORT_MONTH` — `filter(date.startsWith(month))`, `toList()` pe stream; `mapToDouble(Tx::getAmount).sum()` și `size()` pentru `count`; `printf` cu `Locale.US` pentru `%.2f`.
- **L70–77:** `REPORT_ACCOUNT` — `filter` pe egalitate `accountId`, aceeași agregare sumă + număr.
- **L79–97:** `TOP_CHANNELS` — dacă lista e goală, afișează `NONE` și iese din `case`; altfel `groupingBy(channel, counting())`; sortare pe intrări: valoare descrescătoare, apoi cheie alfabetică (`thenComparing(Map.Entry::getKey)`); `limit = min(k, entries.size())` — dacă `k` depășește numărul de canale unice, se tipăresc toate.
- **L99–101:** `default` — comenzi necunoscute ignorate (fără output), conform politicii din readme-ul exercise2.

---

## exercise3 — linii importante

**`Main.java` — clasa `Snapshot`** — *rezultat agregat read-only după colectarea din stream*

- **L45–48:** constructor copiază map-ul într-un `LinkedHashMap` apoi `Collections.unmodifiableMap` — apelantul nu poate modifica map-ul returnat; `List.copyOf` pentru listă imutabilă.
- **L51–60:** getteri returnează direct structurile deja imutabile / defensive copy din constructor.

**`AnalyticsCollectors`** — *fabrică un colector care termină într-un snapshot imutabil*

- **L68–73:** `Collector.of(supplier, accumulator, combiner, finisher, UNORDERED)` — definește pipeline de agregare: creare acumulator, adăugare element, fuziune pentru paralel (aici combinatorul mută date din `o` în `this`), transformare finală în `Snapshot`.

**`Agg` (clasă internă)** — *container mutabil folosit doar în timpul colectării streamului*

- **L82–85:** `add` — `merge` pe canal pentru numărare; acumulează `total`; păstrează referințe la toate tranzacțiile în `all` pentru calculul top în finisher.
- **L88–92:** `combine` — pentru compatibilitate cu `Collector` la stream-uri paralele: îmbină map-uri, sume și liste.
- **L95–101:** `finish` — sortare după sumă descrescător, `thenComparingInt(id)` pentru tie-break; `limit(topN)`; construiește `Snapshot` cu map-ul de canale, totalul și sublista top.
