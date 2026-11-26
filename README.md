# Battleship: design patterns in Java

Console Battleship game written in pure Java around five design patterns: **Singleton**, **Composite**, **Command**, **Observer** and **Builder**. Team project (5 people) for a software design course. The in-game text is in Polish.

## Contents
1. [Features](#features)
2. [Class diagrams](#class-diagrams)
3. [Design patterns in detail](#design-patterns-in-detail)
4. [Java-specific solutions](#java-specific-solutions)
5. [Team and responsibilities](#team-and-responsibilities)
6. [How to play](#how-to-play)
7. [Build and run](#build-and-run)

---

## Features
- Player vs bot on a 10×10 board, fleets placed randomly
- **Undo / redo** of moves (Command pattern)
- Armored masts that need two hits to sink
- Scoring and game history saved to a file

---

## Class diagrams

### Diagram 1: Singleton (GameEngine)

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

### Diagram 2: Composite (ship hierarchy)

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

### Diagram 3: Command (shots with undo/redo)

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

### Diagram 4: Observer (game state notifications)

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
                           │ (ConcreteObserver) │   │ (potential observ.)│
                           ├────────────────────┤   ├────────────────────┤
                           │ - nick: String     │   │ + displayBothBoards│
                           │ - score: int       │   │ + displayWelcome   │
                           ├────────────────────┤   └────────────────────┘
                           │ + update()         │
                           │ + onUndo()         │
                           │ + saveScore()      │
                           └────────────────────┘
```

### Diagram 5: Builder (board construction)

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

## Design patterns in detail

### 1. Singleton

**Purpose:**  
Guarantees there is exactly one game engine (`GameEngine`) managing the game state, both boards and the undo/redo stacks. This prevents several engine instances from getting out of sync.

**Roles:**
| Role | Class |
|------|-------|
| Singleton | `GameEngine` |

**Where in the code:**
- **Definition:** `src/GameEngine.java`, lines 3–27 (private constructor, static `instance` field, `getInstance()`)
- **Usage:** `src/Main.java`, line 28 (`GameEngine.getInstance()`)

**Possible extensions:**  
New engine features can be added without the risk of multiple unsynchronized instances, e.g.:
- saving the game state to a file
- a network mode with one central engine
- extended statistics

---

### 2. Composite

**Purpose:**  
Treats single masts (`Mast`, `ArmoredMast`) and whole ships made of several masts (`Warship`) the same way. The board works on a collection of `ShipComponent` objects without checking whether something is a single mast or a full ship.

**Roles:**
| Role | Class |
|------|-------|
| Component (interface) | `ShipComponent` |
| Leaf | `Mast`, `ArmoredMast` |
| Composite | `Warship` |

**Where in the code:**
- **Component:** `src/ShipComponent.java`, lines 1–12
- **Leaf `Mast`:** `src/Mast.java`, lines 1–40
- **Leaf `ArmoredMast`:** `src/ArmoredMast.java`, lines 1–22
- **Composite:** `src/Warship.java`, lines 1–72
- **Usage:** `src/BoardBuilder.java`, lines 91–102 (creating ships), `src/Board.java`, lines 43–77 (operations on ships)

**Possible extensions:**
- new mast types (e.g. `ShieldedMast`, `ExplosiveMast`)
- ships with special abilities (e.g. an invisible submarine)
- formations: groups of ships acting as one element

---

### 3. Command

**Purpose:**  
Each shot is wrapped in an object that stores the cell state before and after the shot, which makes every move fully reversible (undo / redo).

**Roles:**
| Role | Class |
|------|-------|
| Command (interface) | `Command` |
| ConcreteCommand | `FireCommand` |
| Invoker | `GameEngine` (undo/redo stacks) |
| Receiver | `Board` |
| Client | `GameEngine.processTurn()` |

**Where in the code:**
- **Interface:** `src/Command.java`, lines 1–5
- **Command:** `src/FireCommand.java`, lines 1–53
- **Invoker (stacks):** `src/GameEngine.java`, lines 10–11, 118–145
- **Usage:** `src/GameEngine.java`, lines 103–107 (creating and executing commands)

**Possible extensions:**
- `RepairCommand`: repair a damaged ship
- `MoveCommand`: move a ship (game variant)
- `SpecialAttackCommand`: an attack covering several cells
- `SaveGameCommand`: saving the game as a command

---

### 4. Observer

**Purpose:**  
Notifies components such as `ScoreManager` about game events (hits, misses), so they react to board changes without being coupled to the game logic.

**Roles:**
| Role | Class |
|------|-------|
| Subject | `Board` |
| Observer (interface) | `Observer` |
| ConcreteObserver | `ScoreManager` |

**Where in the code:**
- **Interface:** `src/Observer.java`, lines 1–6
- **Subject (attach, notify):** `src/Board.java`, lines 110–124
- **ConcreteObserver:** `src/ScoreManager.java`, lines 1–61
- **Registration:** `src/Main.java`, line 33 (`engine.getBotBoard().attach(scoreManager)`)
- **Notification:** `src/Board.java`, line 79, `src/FireCommand.java`, lines 38, 50

**Possible extensions:**
- `SoundManager`: sounds on hits
- `AchievementManager`: achievements
- `StatisticsCollector`: detailed statistics
- `NetworkNotifier`: events in a network mode

---

### 5. Builder

**Purpose:**  
Builds the complex `Board` object step by step, with randomly placed ships. Construction is separated from the board itself, so the player's and the bot's boards can be configured differently.

**Roles:**
| Role | Class |
|------|-------|
| Builder | `BoardBuilder` |
| Product | `Board` |
| Director | `GameEngine` (calls the builder methods) |

**Where in the code:**
- **Builder:** `src/BoardBuilder.java`, lines 1–122
- **Product:** `src/Board.java`
- **Usage (Director):** `src/GameEngine.java`, lines 38–39

**Possible extensions:**
- `addPlayerShipsManually()`: manual ship placement
- `addShipsFromFile(path)`: loading a layout from a file
- `addCustomFleet(config)`: a custom fleet
- different board sizes (e.g. 15×15)

---

## Java-specific solutions

### 1. Default interface methods (Java 8+)
`Observer` has a default `onUndo()`, so concrete observers implement it only when they need it:
```java
default void onUndo(Board board, FireResult originalResult) {
    // empty by default
}
```

### 2. Pattern matching for instanceof (Java 16+)
Used in `Board`:
```java
if (ship instanceof Warship warship) {
    ShipComponent component = warship.getComponentAt(coord);
}
```

### 3. Switch expressions (Java 14+)
Used in `Main` and `Board`:
```java
switch (choice) {
    case 1 -> { /* code */ }
    case 2 -> displayHistory();
    case 3 -> { running = false; }
}
```

### 4. Fluent interface in the Builder
`BoardBuilder` methods return `this`, so calls can be chained:
```java
new BoardBuilder().addPlayerShips().build();
```

### 5. Enum for shot results
`FireResult` keeps results type-safe and readable:
```java
public enum FireResult { EMPTY, HIT, MISS }
```

### 6. Generic collections and List.of() (Java 9+)
Immutable lists in `Mast`:
```java
return List.of(this.coordinate);
```

### 7. Try-with-resources
File resources are closed automatically:
```java
try (FileWriter fw = new FileWriter(FILE, true);
     PrintWriter out = new PrintWriter(fw)) {
    // file operations
}
```

---

## Team and responsibilities

| Person | Responsibilities |
|--------|------------------|
| **Gabriel Kossakowski** | **Singleton** (`GameEngine.java`), player classes (`Player.java`, `HumanPlayer.java`, `BotPlayer.java`) |
| **Mateusz Żmiejko** | **Composite** (`ShipComponent.java`, `Mast.java`, `ArmoredMast.java`, `Warship.java`), ship model, hit and sink logic |
| **Igor Wróblewski** | **Command** (`Command.java`, `FireCommand.java`), undo/redo, integration with the stacks in `GameEngine` |
| **Bartosz Mroziewski** | **Observer** (`Observer.java`, `ScoreManager.java`, `ConsoleView.java`), scoring, saving results to a file, event notifications, user interface |
| **Jakub Piotrowski** | **Builder** (`BoardBuilder.java`), random ship placement, position validation, `Board.java` |
| **Together** | Helper classes (`Coordinate.java`, `FireResult.java`), main game loop, integration, `Main.java` with the main menu and game history, testing, documentation, code review |

---

## How to play

### Main menu
After start, the game shows a menu (in Polish): **1** new game, **2** game history, **3** exit.
```
+===================================+
|     GRA W STATKI - BATTLESHIP     |
+===================================+
|  1 - Nowa gra                     |
|  2 - Historia gier                |
|  3 - Wyjscie                      |
+===================================+
```

### Starting a game (option 1)
1. Enter your player name
2. Ships are placed automatically on both boards
3. The game starts: you play against a bot

### Board view
Your board on the left, the opponent's on the right. Legend: `~` water, `S` ship, `X` hit, `O` miss.
```
        TWOJA PLANSZA                  PLANSZA PRZECIWNIKA
   +---------------------+           +---------------------+
10 | ~ ~ S S S S ~ ~ ~ ~ |        10 | ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ |
 9 | ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ |         9 | ~ ~ ~ X ~ ~ ~ ~ ~ ~ |
   ...                                ...
   +---------------------+           +---------------------+
     1 2 3 4 5 6 7 8 9 10              1 2 3 4 5 6 7 8 9 10
```

### Making a move
- Enter coordinates as `column row` (e.g. `5 3`)
- Both values range from 1 to 10

### Undo and redo
- `u` or `undo`: undoes the last move (yours and the bot's)
- `r` or `redo`: redoes an undone move

### Scoring
- **+10 points** for every hit
- **−1 point** for every miss
- The score is saved when the game ends

### Fleet (per player)
| Ship | Count | Size | Notes |
|------|-------|------|-------|
| Four-master | 1 | 4 cells | – |
| Three-master | 2 | 3 cells | – |
| Two-master | 3 | 2 cells | – |
| One-master | 4 | 1 cell | 2 of them armored |

An **armored mast** needs 2 hits to be destroyed.

### Game history (option 2)
Shows saved results:
```
2025-01-21 14:30 | Gracz123 | 85 pkt | WYGRANA
```

---

## Build and run

### Requirements
- **Java JDK 17** or newer

### Compile and run
```bash
cd src
javac *.java
java Main
```

### Project structure
```
src/
├── Main.java           # entry point
├── GameEngine.java     # Singleton: game engine
├── Board.java          # board (Subject in Observer)
├── BoardBuilder.java   # board Builder
├── ShipComponent.java  # Composite interface
├── Warship.java        # Composite
├── Mast.java           # Leaf
├── ArmoredMast.java    # Leaf with HP
├── Command.java        # Command interface
├── FireCommand.java    # Concrete Command
├── Observer.java       # Observer interface
├── ScoreManager.java   # Concrete Observer
├── ConsoleView.java    # console view
├── Player.java         # abstract player
├── HumanPlayer.java    # human player
├── BotPlayer.java      # computer player
├── Coordinate.java     # coordinates
└── FireResult.java     # shot result enum
```

### Results file
Results are saved automatically to `results.txt` in the working directory.
