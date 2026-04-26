Proiect PAO - Catalog Școlar (Etapa I)


1. Definirea sistemului


Acest proiect reprezintă nucleul unui sistem de tip "Catalog Școlar", implementat în Java. Sistemul permite gestiunea elevilor, a notelor și a absențelor, fiind modelat folosind concepte avansate de Programare Orientată pe Obiecte (OOP).


1.1 Lista acțiunilor / interogărilor posibile în sistem (10 acțiuni)


Adaugă un elev: Înregistrează un elev nou în sistem pe baza matricolei.

Listează elevii alfabetic: Returnează și afișează toți elevii în ordine alfabetică (după nume și prenume).

Găsește elev după matricolă: Caută detaliile unui elev specific.

Adaugă o notă: Atribuie o notă unui elev la o materie specifică (cu validare 1-10).

Listează notele unui elev: Afișează toate notele unui elev la o anumită materie.

Calculează media: Determină media aritmetică a notelor unui elev pentru o materie.

Adaugă o absență: Înregistrează o absență pentru un elev la o anumită dată.

Motivează o absență: Transformă statusul unei absențe din "nemotivată" în "motivată".

Afișează absențele nemotivate: Extrage doar lista cu absențe nemotivate ale unui elev.

Șterge un elev: Elimină definitiv un elev din gestiunea școlii (pe baza matricolei).


1.2 Tipurile de obiecte din domeniu (8 clase)


Persoana (Clasă abstractă) - Modelează atributele de bază ale unui om (nume, prenume).

Elev - Subclasă a Persoana.

Profesor - Subclasă a Persoana.

Matricola (Clasă imutabilă) - Identificator unic și constant pentru fiecare elev.

Clasa - Grupează un set de elevi și este coordonată de un diriginte.

Materie - Disciplina de studiu.

Nota - Modelează evaluarea la o materie la o anumită dată.

Absenta - Modelează prezența la ore.