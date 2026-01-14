# Gra w Statki (Battleship) - Projekt Java

## Opis projektu
Konsolowa gra w statki zaimplementowana w czystej Javie zgodnie wykorzystującą wzorce projektowe.

## Wzorce projektowe
1. **Singleton** - GameEngine (zarządzanie grą)
2. **Composite** - Statki (Warship, Mast, ArmoredMast)
3. **Command** - FireCommand (strzały z undo/redo)
4. **Observer** - ConsoleView, ScoreManager (powiadomienia o stanie gry)
5. **Builder** - BoardBuilder (budowa planszy z losowymi statkami)

## Struktura pakietów
```
battleship/
├── model/          # Wspólne zasoby (Board, FieldStatus, InputParser)
├── engine/         # Zarządzanie grą (GameEngine, Player, HumanPlayer, BotPlayer)
├── ships/          # Statki - Composite (ShipComponent, Warship, Mast, ArmoredMast, Coordinate)
├── logic/          # Komendy (Command, FireCommand)
├── ui/             # Interfejs użytkownika (Observer, ConsoleView, ScoreManager)
└── builder/        # Budowanie planszy (BoardBuilder)
```

## Funkcjonalności

### Główne cechy
- **Menu główne** z wyborem trybu gry, rankingu i wyjścia
- **Tryb PvE** - gracz przeciwko botowi
- **3 poziomy trudności bota:**
  - Łatwy (1 próba losowania)
  - Średni (2 próby losowania - jeśli pudło, dostaje drugą szansę)
  - Trudny "Cheater" (3 próby + wie gdzie są statki)
- **Fog of War** - przeciwnik nie widzi nietrafionych statków
- **System punktacji:**
  - Trafienie: +10 punktów
  - Zatopienie: +50 punktów
- **Ranking** zapisywany do pliku ranking.txt
- **Undo/Redo** dla ruchów gracza

### Flota
Każdy gracz ma następujące statki:
- 1x Czteromasztowiec (Pancernik) - z ArmoredMast
- 2x Trójmasztowiec (Krążownik) - z ArmoredMast
- 3x Dwumasztowiec (Niszczyciel)
- 4x Jednomasztowiec

**ArmoredMast** - specjalny maszt z 2 punktami HP. Wymaga 2 trafień do zniszczenia.

## Kompilacja i uruchomienie

### Kompilacja
```bash
cd src
javac battleship/model/*.java battleship/ships/*.java battleship/logic/*.java battleship/ui/*.java battleship/builder/*.java battleship/engine/*.java Main.java
```

### Uruchomienie
```bash
cd ..
java -cp src Main
```

lub z katalogu głównego:
```bash
java -cp src Main
```

## Jak grać
1. Uruchom grę
2. Podaj swój nick
3. Wybierz opcję z menu:
   - **1** - Nowa gra PvE (wybierz poziom trudności bota)
   - **2** - PvP (w przygotowaniu)
   - **3** - Zobacz ranking
   - **4** - Wyjście
4. Podczas gry podawaj współrzędne strzału w formacie: `wiersz kolumna` (np. `5 7`)
5. Wiersze i kolumny: 1-10
6. Legenda:
   - `~` - Woda
   - `X` - Trafiony
   - `O` - Pudło

## Wymagania
- Java 17 lub nowsza
- Brak zewnętrznych bibliotek

## Autorzy
Projekt stworzony zgodnie ze specyfikacją UML dla kursu Zaawansowane Techniki Programowania.

## Licencja
Projekt edukacyjny
