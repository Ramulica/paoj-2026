# Laborator 12 — Prezentare (explicații pe cod)

*(Exercise2 nu mai are sursă în repo — doar `Readme`; restul laboratorului e în pachetul principal și `exercise1` / `exercise3`.)*

## Fișiere

| Fișier | Scop (5–7 cuvinte) |
|--------|---------------------|
| `model/Author.java` | Obiect pentru un rând din tabela autor. |
| `model/Book.java` | Obiect pentru carte, autor și disponibilitate. |
| `model/Reader.java` | Obiect pentru cititor cu nume și email. |
| `model/Loan.java` | Obiect pentru împrumut: carte, cititor, date. |
| `repository/Repository.java` | Interfață CRUD generică pentru orice entitate. |
| `repository/AuthorRepository.java` | SQL parametrizat pentru insert, select, update, delete autor. |
| `repository/BookRepository.java` | SQL parametrizat pentru operații pe tabela carte. |
| `repository/ReaderRepository.java` | SQL parametrizat pentru operații pe tabela cititor. |
| `repository/LoanRepository.java` | SQL parametrizat pentru operații pe tabela împrumut. |
| `util/DatabaseConnection.java` | Singleton: citește db.properties și deschide JDBC. |
| `util/SchemaInitializer.java` | Încarcă schema SQL din resurse și o execută pe BD. |
| `service/LibraryService.java` | Tranzacții împrumut și rapoarte cu JOIN pe tabele. |
| `service/AuditService.java` | Scrie acțiuni în CSV sub blocare cu lock. |
| `Main.java` (rădăcină) | Demo complet: schemă, CRUD, împrumut, audit, ștergeri. |
| `exercise1/Main.java` | Demo scurt: doar autor și carte pentru smoke test. |
| `exercise3/Main.java` | Detectează SQLite și reaplică scriptul de schemă. |
| `resources/db.properties` | URL și credențiale pentru conexiunea JDBC. |
| `resources/schema-sqlite.sql` | DDL tabele bibliotecă și chei străine SQLite. |

---

## `DatabaseConnection.java` — *singleton: citește db.properties și deschide JDBC*

- **L17–25:** `Class.forName("org.sqlite.JDBC")` apoi fallback MySQL — înregistrează driverul în `DriverManager` înainte de `getConnection` (necesar pe unele medii fără SPI auto).
- **L27–32:** `getResourceAsStream("/com/pao/.../db.properties")` — cale absolută în classpath; evită depinde de directorul curent de lucru pentru fișierul de configurare.
- **L36–39:** `getConnection(url, null, null)` dacă user/parolă goale — convenție pentru SQLite fără autentificare.
- **L41–45:** `PRAGMA foreign_keys = ON` într-un `try` separat — pe MySQL instrucțiunea e invalidă, excepția e ignorată; pe SQLite activează aplicarea FK.

---

## `SchemaInitializer.java` — *încarcă schema SQL din resurse și o execută pe BD*

- **L21–28:** elimină liniile care încep cu `--` sau sunt goale — evită executarea textului de comentariu ca SQL.
- **L30–36:** `split(";")` + `trim` + `execute` per fragment — scriptul DDL e împărțit în statement-uri individuale (limitare: nu suportă `;` în interiorul stringurilor SQL; pentru DDL-ul dat e suficient).

---

## `AuthorRepository.java` — *SQL parametrizat pentru operații pe tabela autor*

*(Celelalte `*Repository` urmează același tipar: `mapRow`, `PreparedStatement`, `RETURN_GENERATED_KEYS`, `try-with-resources`.)*

- **L22–27:** `mapRow` — traduce un rând `ResultSet` în obiect Java (nume coloane = contract cu SQL).
- **L34–42:** `prepareStatement(..., RETURN_GENERATED_KEYS)` — după `executeUpdate`, `getGeneratedKeys()` citește id-ul auto-generat și îl scrie în obiectul `Author` pasat la `save`.
- **L49+:** `findById` / `findAll` — `try` imbricat: `PreparedStatement` + `ResultSet` ambele în `try-with-resources` pentru închidere garantată.

---

## `LibraryService.java` — *tranzacții împrumut și rapoarte cu JOIN pe tabele*

**`borrowBook`**

- **L35:** `setAutoCommit(false)` — următoarele DML-uri fac parte dintr-o singură tranzacție logică până la `commit`/`rollback`.
- **L37–47:** `SELECT available` — dacă nu există rând sau `available == 0`, aruncă excepție înainte de a modifica `loan` / `book`.
- **L50–60:** `INSERT INTO loan` cu `RETURN_GENERATED_KEYS` — creează împrumutul și citește `loanId`.
- **L63–66:** `UPDATE book SET available = 0` — marchează cartea indisponibilă; fără acest pas datele ar fi inconsistente.
- **L69:** `commit()` — persistă ambele modificări atomically.
- **L72–75:** `rollback()` în `catch` — orice eșec anulează ambele operații.
- **L76–78:** `finally` + `setAutoCommit(true)` — restaurează modul implicit JDBC chiar dacă apare excepție după `rollback`.

**`returnBook`**

- **L86–94:** citește `book_id` pentru `loanId` dat — legătură între tabele pentru a ști ce carte se eliberează.
- **L97–107:** actualizează `return_date` pe `loan` și `available = 1` pe `book` — oglindă logică a lui `borrowBook`.

**Metode cu `JOIN`**

- **L120–126:** `loan` ⋈ `book` ⋈ `reader` — restricție `return_date IS NULL` = împrumut activ; aliasuri în SQL pentru nume coloane clare în `ResultSet`.
- **L141–148:** `book` ⋈ `author` + `LEFT JOIN loan` — include cărți fără împrumuturi (`COUNT` poate fi 0); `GROUP BY` pe cheia logică a cărții.
- **L162–167:** `reader` `LEFT JOIN loan` — include cititori cu zero împrumuturi; `COUNT(l.id)` numără doar rândurile cu potrivire din dreapta.

---

## `AuditService.java` — *scrie acțiuni în CSV sub blocare cu lock*

- **L28:** `lock.lock()` — intrare exclusivă în secțiunea critică înainte de deschiderea fișierului.
- **L29:** `new FileWriter(AUDIT_FILE, true)` — al doilea argument `true` = append, nu truncare la fiecare apel.
- **L34–36:** `finally { lock.unlock(); }` — eliberarea lock-ului chiar dacă scrierea aruncă excepție (după `catch` local).

---

## `Main.java` (pachet `com.pao.laboratory12`) — *demo complet: schemă, CRUD, împrumut, audit, ștergeri*

- **L20:** `SchemaInitializer.initFromSqliteScript(...)` — asigură tabele existente înainte de repository-uri (idempotent dacă scriptul începe cu `DROP IF EXISTS`).
- **L31–40:** `save` autor + două cărți — `author.getId()` propagă FK-ul în `Book` fără valori inventate în Java.
- **L53–56:** `findById` cu `ifPresentOrElse` — demonstrează `Optional` din repository.
- **L63–65:** `borrowBook` — folosește serviciul cu tranzacție, nu insert manual separat în `Main`.
- **L76–80:** înainte de `delete(reader)` — șterge rândurile din `loan` care referă cititorul, pentru a respecta FK (`reader` nu poate fi șters dacă există împrumuturi care îl referă).

---

## `exercise1/Main.java` — *demo scurt: autor și carte pentru smoke test*

- Același `SchemaInitializer` + două `save` — verificare rapidă că repository + conexiune funcționează.

## `exercise3/Main.java` — *detectează SQLite și reaplică scriptul de schemă*

- `getMetaData().getURL()` + ramură SQLite — reaplicare DDL pentru scenariul „dialect SQLite / schemă reaplicată”.
