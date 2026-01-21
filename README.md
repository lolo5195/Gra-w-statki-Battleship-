# Gra w Statki (Battleship) - Dokumentacja Projektu

## Spis treści
1. [Opis projektu](#opis-projektu)
2. [Diagramy klas wzorców projektowych](#diagramy-klas-wzorców-projektowych)
3. [Szczegółowy opis wzorców](#szczegółowy-opis-wzorców)
4. [Rozwiązania specyficzne dla języka Java](#rozwiązania-specyficzne-dla-języka-java)
5. [Podział pracy w zespole](#podział-pracy-w-zespole)
6. [Instrukcja użytkownika](#instrukcja-użytkownika)
7. [Instrukcja instalacji](#instrukcja-instalacji)

---

## Opis projektu
Konsolowa gra w statki (Battleship) zaimplementowana w czystej Javie z wykorzystaniem pięciu wzorców projektowych: **Singleton**, **Composite**, **Command**, **Observer** oraz **Builder**.

---

## Diagramy klas wzorców projektowych

### Diagram 1: Wzorzec Singleton (GameEngine)

```
┌─────────────────────────────────────┐
│           GameEngine                │
├─────────────────────────────────────┤
│ - instance: GameEngine              │
│ - playerBoard: Board                │
│ - botBoard: Board                   │
│ - undoStack: Stack<Command>         │
│ - redoStack: Stack<Command>         │
│ - isRunning: boolean                │
├─────────────────────────────────────┤
│ - GameEngine()                      │
│ + getInstance(): GameEngine         │
│ + setupGame(): void                 │
│ + startGame(): Player               │
│ + undoLastMove(): void              │
│ + redoLastMove(): void              │
└─────────────────────────────────────┘
```

### Diagram 2: Wzorzec Composite (Hierarchia statków)

```
              ┌───────────────────────┐
              │   <<interface>>       │
              │    ShipComponent      │
              ├───────────────────────┤
              │ + hit(): void         │
              │ + isSunk(): boolean   │
              │ + repair(): void      │
              │ + getCoordinates()    │
              │ + containsCoordinate()│
              │ + registerHit()       │
              └───────────┬───────────┘
                          │
          ┌───────────────┼───────────────┐
          │               │               │
          ▼               ▼               ▼
┌─────────────────┐ ┌─────────────┐ ┌─────────────────┐
│     Warship     │ │    Mast     │ │   ArmoredMast   │
│   (Composite)   │ │   (Leaf)    │ │  (Leaf + HP)    │
├─────────────────┤ ├─────────────┤ ├─────────────────┤
│ - components:   │ │ - isHit     │ │ - hp: int = 2   │
│   List<Ship     │ │ - coordinate│ │                 │
│   Component>    │ │             │ │                 │
├─────────────────┤ ├─────────────┤ ├─────────────────┤
│ + addComponent()│ │ + hit()     │ │ + hit()         │
│ + hit()         │ │ + isSunk()  │ │ + repair()      │
│ + isSunk()      │ │ + repair()  │ │                 │
└─────────────────┘ └──────┬──────┘ └────────┬────────┘
                           │                  │
                           └───── extends ────┘
```

### Diagram 3: Wzorzec Command (Strzały z Undo/Redo)

```
┌───────────────────────┐         ┌────────────────────────┐
│    <<interface>>      │         │      GameEngine        │
│       Command         │◄────────│     (Invoker)          │
├───────────────────────┤         ├────────────────────────┤
│ + execute(): void     │         │ - undoStack: Stack     │
│ + undo(): void        │         │ - redoStack: Stack     │
│ + redo(): void        │         │ + undoLastMove()       │
└───────────┬───────────┘         │ + redoLastMove()       │
            │                     └────────────────────────┘
            │ implements
            ▼
┌───────────────────────┐         ┌────────────────────────┐
│     FireCommand       │────────▶│        Board           │
│  (ConcreteCommand)    │         │      (Receiver)        │
├───────────────────────┤         ├────────────────────────┤
│ - board: Board        │         │ + shotAt(coord)        │
│ - coord: Coordinate   │         │ + setCell(coord, val)  │
│ - previousValue: char │         │ + getCell(coord)       │
│ - newValue: char      │         │                        │
├───────────────────────┤         └────────────────────────┘
│ + execute()           │
│ + undo()              │
│ + redo()              │
└───────────────────────┘
```

### Diagram 4: Wzorzec Observer (Powiadomienia o stanie gry)

```
┌───────────────────────┐              ┌────────────────────────┐
│        Board          │              │     <<interface>>      │
│      (Subject)        │──────────────│        Observer        │
├───────────────────────┤   notifies   ├────────────────────────┤
│ - observers: List     │              │ + update(Board,        │
│                       │              │         FireResult)    │
├───────────────────────┤              │ + onUndo(Board,        │
│ + attach(observer)    │              │         FireResult)    │
│ + notifyObservers()   │              └───────────┬────────────┘
│ + notifyObserversUndo │                          │
└───────────────────────┘              ┌───────────┴────────────┐
                                       │                        │
                           ┌───────────▼────────┐   ┌───────────▼────────┐
                           │    ScoreManager    │   │     ConsoleView    │
                           │ (ConcreteObserver) │   │  (potencjalny obs) │
                           ├────────────────────┤   ├────────────────────┤
                           │ - nick: String     │   │ + displayBothBoards│
                           │ - score: int       │   │ + displayWelcome   │
                           ├────────────────────┤   └────────────────────┘
                           │ + update()         │
                           │ + onUndo()         │
                           │ + saveScore()      │
                           └────────────────────┘
```

### Diagram 5: Wzorzec Builder (Budowanie planszy)

```
┌─────────────────────────────────────────┐
│             BoardBuilder                │
│              (Builder)                  │
├─────────────────────────────────────────┤
│ - board: Board                          │
│ - scanner: Scanner                      │
│ - random: Random                        │
├─────────────────────────────────────────┤
│ + BoardBuilder()                        │
│ + addPlayerShips(): BoardBuilder        │
│ + addBotShips(int): BoardBuilder        │
│ + build(): Board                        │
│ - placeShipRandomly(int, boolean): void │
│ - isValidCoordinate(int, int): boolean  │
└────────────────────┬────────────────────┘
                     │ builds
                     ▼
           ┌─────────────────────┐
           │        Board        │
           │      (Product)      │
           ├─────────────────────┤
           │ - grid: char[][]    │
           │ - ships: List       │
           │ - observers: List   │
           └─────────────────────┘
```

---

## Szczegółowy opis wzorców

### 1. Wzorzec Singleton

**Cel użycia:**  
Zapewnienie, że w całej aplikacji istnieje tylko jedna instancja silnika gry (`GameEngine`), która zarządza stanem rozgrywki, planszami graczy oraz stosami operacji undo/redo. Singleton eliminuje problemy z wieloma instancjami silnika, które mogłyby prowadzić do niespójności stanu gry.

**Przyporządkowanie klas do ról wzorca:**
| Rola | Klasa |
|------|-------|
| Singleton | `GameEngine` |

**Lokalizacja w kodzie:**
- **Definicja:** `src/GameEngine.java`, linie 3-27 (prywatny konstruktor, statyczne pole `instance`, metoda `getInstance()`)
- **Użycie:** `src/Main.java`, linia 28 (`GameEngine.getInstance()`)

**Wektor zmian:**  
Dzięki Singletonowi można w przyszłości łatwo dodać nowe funkcjonalności do silnika gry (np. tryb multiplayer, zapis/odczyt stanu gry) bez ryzyka tworzenia wielu niezsynchronizowanych instancji. Możliwe rozszerzenia:
- Dodanie zapisu stanu gry do pliku
- Implementacja trybu sieciowego z jednym centralnym silnikiem
- Rozbudowa systemu statystyk

---

### 2. Wzorzec Composite

**Cel użycia:**  
Umożliwienie jednolitego traktowania pojedynczych masztów (`Mast`, `ArmoredMast`) oraz całych statków złożonych z wielu masztów (`Warship`). Dzięki temu plansza może operować na kolekcji `ShipComponent` bez rozróżniania czy jest to pojedynczy maszt czy złożony statek.

**Przyporządkowanie klas do ról wzorca:**
| Rola | Klasa |
|------|-------|
| Component (interfejs) | `ShipComponent` |
| Leaf (liść) | `Mast`, `ArmoredMast` |
| Composite (kompozyt) | `Warship` |

**Lokalizacja w kodzie:**
- **Definicja komponentu:** `src/ShipComponent.java`, linie 1-12
- **Definicja liścia Mast:** `src/Mast.java`, linie 1-40
- **Definicja liścia ArmoredMast:** `src/ArmoredMast.java`, linie 1-22
- **Definicja kompozytu:** `src/Warship.java`, linie 1-72
- **Użycie:** `src/BoardBuilder.java`, linie 91-102 (tworzenie statków), `src/Board.java`, linie 43-77 (operacje na statkach)

**Wektor zmian:**  
Wzorzec umożliwia łatwe dodawanie nowych typów masztów lub statków:
- Nowe typy masztów (np. `ShieldedMast`, `ExplosiveMast`)
- Statki o specjalnych właściwościach (np. podwodne z niewidzialnością)
- Formacje statków (grupy statków działające jako jeden element)

---

### 3. Wzorzec Command

**Cel użycia:**  
Enkapsulacja operacji strzału jako obiektu, co umożliwia implementację funkcjonalności cofania (undo) i ponawiania (redo) ruchów. Każdy strzał jest zapisywany jako komenda ze stanem przed i po wykonaniu, co pozwala na pełną odwracalność akcji.

**Przyporządkowanie klas do ról wzorca:**
| Rola | Klasa |
|------|-------|
| Command (interfejs) | `Command` |
| ConcreteCommand | `FireCommand` |
| Invoker | `GameEngine` (stosy undo/redo) |
| Receiver | `Board` |
| Client | `GameEngine.processTurn()` |

**Lokalizacja w kodzie:**
- **Definicja interfejsu:** `src/Command.java`, linie 1-5
- **Definicja komendy:** `src/FireCommand.java`, linie 1-53
- **Invoker (stosy):** `src/GameEngine.java`, linie 10-11, 118-145
- **Użycie:** `src/GameEngine.java`, linie 103-107 (tworzenie i wykonywanie komend)

**Wektor zmian:**  
Możliwość dodawania nowych typów komend:
- `RepairCommand` - naprawa uszkodzonego statku
- `MoveCommand` - przesunięcie statku (wariant gry)
- `SpecialAttackCommand` - atak specjalny obejmujący wiele pól
- `SaveGameCommand` - zapis stanu gry jako komenda

---

### 4. Wzorzec Observer

**Cel użycia:**  
Automatyczne powiadamianie komponentów (np. `ScoreManager`) o zdarzeniach podczas gry (trafienia, pudła). Dzięki temu komponenty mogą reagować na zmiany stanu planszy bez ścisłego powiązania z logiką gry.

**Przyporządkowanie klas do ról wzorca:**
| Rola | Klasa |
|------|-------|
| Subject | `Board` |
| Observer (interfejs) | `Observer` |
| ConcreteObserver | `ScoreManager` |

**Lokalizacja w kodzie:**
- **Definicja interfejsu:** `src/Observer.java`, linie 1-6
- **Subject (attach, notify):** `src/Board.java`, linie 110-124
- **ConcreteObserver:** `src/ScoreManager.java`, linie 1-61
- **Rejestracja obserwatora:** `src/Main.java`, linia 33 (`engine.getBotBoard().attach(scoreManager)`)
- **Powiadamianie:** `src/Board.java`, linia 79, `src/FireCommand.java`, linie 38, 50

**Wektor zmian:**  
Możliwość dodawania nowych obserwatorów:
- `SoundManager` - odtwarzanie dźwięków przy trafieniach
- `AchievementManager` - system osiągnięć
- `StatisticsCollector` - zbieranie szczegółowych statystyk
- `NetworkNotifier` - powiadamianie o zdarzeniach w trybie sieciowym

---

### 5. Wzorzec Builder

**Cel użycia:**  
Stopniowe budowanie złożonego obiektu planszy (`Board`) z losowo rozmieszczonymi statkami. Builder oddziela proces konstrukcji planszy od jej reprezentacji, umożliwiając różne konfiguracje (plansza gracza vs plansza bota).

**Przyporządkowanie klas do ról wzorca:**
| Rola | Klasa |
|------|-------|
| Builder | `BoardBuilder` |
| Product | `Board` |
| Director | `GameEngine` (wywołuje metody buildera) |

**Lokalizacja w kodzie:**
- **Definicja Buildera:** `src/BoardBuilder.java`, linie 1-122
- **Product:** `src/Board.java`
- **Użycie (Director):** `src/GameEngine.java`, linie 38-39

**Wektor zmian:**  
Możliwości rozbudowy:
- `addPlayerShipsManually()` - ręczne rozmieszczanie statków przez gracza
- `addShipsFromFile(path)` - wczytywanie układu z pliku
- `addCustomFleet(config)` - niestandardowa flota
- Różne rozmiary planszy (np. 15x15)

---

## Rozwiązania specyficzne dla języka Java

### 1. Interfejsy z metodami domyślnymi (Java 8+)
Interfejs `Observer` wykorzystuje metodę domyślną `onUndo()`:
```java
default void onUndo(Board board, FireResult originalResult) {
    // Domyślna pusta implementacja
}
```
Pozwala to na opcjonalne implementowanie metody przez konkretne obserwatory.

### 2. Pattern Matching dla instanceof (Java 16+)
W klasie `Board` używany jest pattern matching:
```java
if (ship instanceof Warship warship) {
    ShipComponent component = warship.getComponentAt(coord);
}
```

### 3. Switch Expressions (Java 14+)
W klasie `Main` i `Board` używane są wyrażenia switch:
```java
switch (choice) {
    case 1 -> { /* kod */ }
    case 2 -> displayHistory();
    case 3 -> { running = false; }
}
```

### 4. Fluent Interface w Builderze
`BoardBuilder` zwraca `this` w metodach budujących, umożliwiając łańcuchowe wywołania:
```java
new BoardBuilder().addPlayerShips().build();
```

### 5. Enum dla wyników strzału
`FireResult` jako enum zapewnia type-safety i czytelność:
```java
public enum FireResult { EMPTY, HIT, MISS }
```

### 6. Kolekcje generyczne i List.of() (Java 9+)
Niemodyfikowalne listy w klasie `Mast`:
```java
return List.of(this.coordinate);
```

### 7. Try-with-resources
Automatyczne zamykanie zasobów plikowych:
```java
try (FileWriter fw = new FileWriter(FILE, true);
     PrintWriter out = new PrintWriter(fw)) {
    // operacje na pliku
}
```

---

## Podział pracy w zespole

| Osoba | Zakres prac |
|-------|-------------|
| **Gabriel Kossakowski** | Implementacja wzorca **Singleton** (`GameEngine.java`), główna pętla gry, integracja komponentów, klasa `Main.java` z menu głównym i historią gier, klasy graczy (`Player.java`, `HumanPlayer.java`, `BotPlayer.java`) |
| **Mateusz Żmiejko** | Implementacja wzorca **Composite** (`ShipComponent.java`, `Mast.java`, `ArmoredMast.java`, `Warship.java`), model statków, logika trafień i zatopień |
| **Igor Wróblewski** | Implementacja wzorca **Command** (`Command.java`, `FireCommand.java`), mechanizm undo/redo, integracja ze stosami w `GameEngine` |
| **Bartosz Mroziewski** | Implementacja wzorca **Observer** (`Observer.java`, `ScoreManager.java`, `ConsoleView.java`), system punktacji, zapis wyników do pliku, powiadomienia o zdarzeniach, interfejs użytkownika |
| **Jakub Piotrowski** | Implementacja wzorca **Builder** (`BoardBuilder.java`), algorytm losowego rozmieszczania statków, walidacja pozycji, klasa `Board.java` |
| **Wspólnie** | Klasy pomocnicze (`Coordinate.java`, `FireResult.java`), testowanie, dokumentacja, code review |

---

## Instrukcja użytkownika

### Menu główne
Po uruchomieniu gry wyświetla się menu z trzema opcjami:
```
+===================================+
|     GRA W STATKI - BATTLESHIP     |
+===================================+
|  1 - Nowa gra                     |
|  2 - Historia gier                |
|  3 - Wyjscie                      |
+===================================+
```

### Rozpoczęcie gry (opcja 1)
1. Podaj swoją nazwę gracza
2. Statki zostają automatycznie rozmieszczone na obu planszach
3. Gra rozpoczyna się - grasz przeciwko botowi

### Interfejs planszy
```
        TWOJA PLANSZA                  PLANSZA PRZECIWNIKA
   +---------------------+           +---------------------+
10 | ~ ~ S S S S ~ ~ ~ ~ |        10 | ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ |
 9 | ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ |         9 | ~ ~ ~ X ~ ~ ~ ~ ~ ~ |
   ...                                ...
   +---------------------+           +---------------------+
     1 2 3 4 5 6 7 8 9 10              1 2 3 4 5 6 7 8 9 10

Legenda:  ~ Woda  |  S Statek  |  X Trafienie  |  O Pudło
```

### Wykonywanie ruchu
- Podaj współrzędne w formacie: `kolumna wiersz` (np. `5 3`)
- Wartości od 1 do 10 dla obu współrzędnych

### Cofanie i ponawianie ruchów
- Wpisz `u` lub `undo` - cofa ostatni ruch (twój i bota)
- Wpisz `r` lub `redo` - ponawia cofnięty ruch

### System punktacji
- **+10 punktów** za każde trafienie
- **-1 punkt** za każde pudło
- Wynik zapisywany po zakończeniu gry

### Flota (dla każdego gracza)
| Typ statku | Ilość | Rozmiar | Uwagi |
|------------|-------|---------|-------|
| Czteromasztowiec | 1 | 4 pola | - |
| Trójmasztowiec | 2 | 3 pola | - |
| Dwumasztowiec | 3 | 2 pola | - |
| Jednomasztowiec | 4 | 1 pole | 2 z pancerzem |

**Opancerzony maszt** wymaga 2 trafień do zniszczenia.

### Historia gier (opcja 2)
Wyświetla zapisane wyniki w formacie:
```
2025-01-21 14:30 | Gracz123 | 85 pkt | WYGRANA
```

---

## Instrukcja instalacji

### Wymagania
- **Java JDK 17** lub nowsza
- Terminal/konsola

### Kompilacja
```bash
cd src
javac *.java
```

### Uruchomienie
```bash
java Main
```

### Struktura plików
```
src/
├── Main.java           # Punkt wejścia
├── GameEngine.java     # Singleton - silnik gry
├── Board.java          # Plansza (Subject w Observer)
├── BoardBuilder.java   # Builder planszy
├── ShipComponent.java  # Interfejs Composite
├── Warship.java        # Composite
├── Mast.java           # Leaf
├── ArmoredMast.java    # Leaf z HP
├── Command.java        # Interfejs Command
├── FireCommand.java    # Concrete Command
├── Observer.java       # Interfejs Observer
├── ScoreManager.java   # Concrete Observer
├── ConsoleView.java    # Widok konsolowy
├── Player.java         # Abstrakcyjna klasa gracza
├── HumanPlayer.java    # Gracz ludzki
├── BotPlayer.java      # Gracz komputerowy
├── Coordinate.java     # Współrzędne
└── FireResult.java     # Enum wyników strzału
```

### Plik wyników
Wyniki zapisywane są automatycznie do pliku `results.txt` w katalogu uruchomienia.
