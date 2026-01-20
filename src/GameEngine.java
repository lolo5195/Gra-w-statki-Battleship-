import java.util.Stack;

public class GameEngine {
    private static GameEngine instance;
    private Board playerBoard;
    private Board botBoard;
    private Player player1;
    private Player player2;
    private boolean isRunning;
    private Stack<Command> undoStack;
    private Stack<Command> redoStack;
    private String playerName;

    // Pobranie planszy bota
    public Board getBotBoard() {
        return botBoard;
    }

    // Konstruktor prywatny (singleton)
    private GameEngine() {
        undoStack = new Stack<>();
        redoStack = new Stack<>();
        isRunning = false;
    }

    // Singleton
    public static GameEngine getInstance() {
        if (instance == null) {
            instance = new GameEngine();
        }
        return instance;
    }

    // Ustawienie nazwy gracza
    public void setPlayerName(String name) {
        this.playerName = name;
    }

    // Przygotowanie gry
    public void setupGame() {
        // Czyszczenie stosow undo/redo przy rozpoczeciu nowej gry
        undoStack.clear();
        redoStack.clear();
        isRunning = false;
        
        // Budowanie plansz
        playerBoard = new BoardBuilder().addPlayerShips().build();
        botBoard = new BoardBuilder().addBotShips(5).build();

        // Tworzymy graczy
        player1 = new HumanPlayer(playerName != null ? playerName : "Player1");
        player2 = new BotPlayer();

        // Ustawienie planszy przeciwnika
        player1.setEnemyBoard(botBoard);
        player2.setEnemyBoard(playerBoard);

        System.out.println("[INFO] Przygotowanie gry zakonczone.");
    }

    // Rozpoczecie gry
    public Player startGame() {
        System.out.println("\n[INFO] Gra rozpoczeta!");
        isRunning = true;
        ConsoleView view = new ConsoleView();
        
        // Wyswietl poczatkowy stan plansz
        view.displayBothBoards(playerBoard, botBoard);

        while (!playerBoard.isGameOver() && !botBoard.isGameOver() && isRunning) {
            processTurn(player1, botBoard, view);
            if (!botBoard.isGameOver() && isRunning) {
                processTurn(player2, playerBoard, view);
            }
        }

        isRunning = false;

        if (playerBoard.isGameOver()) {
            System.out.println();
            System.out.println("+===================================+");
            System.out.println("|         KONIEC GRY!               |");
            System.out.println("|         BOT WYGRYWA!              |");
            System.out.println("+===================================+");
            return player2;
        } else if (botBoard.isGameOver()) {
            System.out.println();
            System.out.println("+===================================+");
            System.out.println("|         KONIEC GRY!               |");
            System.out.println("|   " + String.format("%-20s", player1.getName()) + " WYGRYWA!  |");
            System.out.println("+===================================+");
            return player1;
        } else {
            System.out.println("[INFO] Gra zatrzymana.");
            return null;
        }
    }

    // Wykonanie tury gracza
    private void processTurn(Player player, Board targetBoard, ConsoleView view) {
        System.out.println();
        System.out.println("--- Tura: " + player.getName() + " ---");
        
        // Wyswietl plansze przed ruchem (tylko dla czlowieka)
        if(player instanceof HumanPlayer) {
            view.displayBothBoards(playerBoard, botBoard);
        }
        
        Coordinate move = player.getMove();
        
        // Obsluga akcji specjalnych dla gracza ludzkiego
        if (player instanceof HumanPlayer) {
            // Sprawdz czy to akcja undo
            if (move.equals(HumanPlayer.UNDO_ACTION)) {
                undoLastMove();
                view.displayBothBoards(playerBoard, botBoard);
                // Rekurencyjnie poproś o nowy ruch
                processTurn(player, targetBoard, view);
                return;
            }
            // Sprawdz czy to akcja redo
            if (move.equals(HumanPlayer.REDO_ACTION)) {
                redoLastMove();
                view.displayBothBoards(playerBoard, botBoard);
                // Rekurencyjnie poproś o nowy ruch
                processTurn(player, targetBoard, view);
                return;
            }
        }

        // Utworzenie komendy FireCommand
        Command fire = new FireCommand(targetBoard, move);
        undoStack.push(fire); // dodanie do stosu undo
        redoStack.clear();    // wyczyść stos redo po nowym ruchu
        fire.execute();        // wykonanie strzalu
        
        // Wyswietl plansze po ruchu
        view.displayBothBoards(playerBoard, botBoard);
        
        // Krotka przerwa dla czytelnosci
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }

    // Cofniecie ostatniego ruchu
    public void undoLastMove() {
        if (!undoStack.isEmpty()) {
            Command command = undoStack.pop();
            command.undo();
            redoStack.push(command);
            System.out.println("[INFO] Cofnieto ostatni ruch.");
        } else {
            System.out.println("[INFO] Brak ruchow do cofniecia.");
        }
    }

    // Powtorzenie cofnietego ruchu
    public void redoLastMove() {
        if (!redoStack.isEmpty()) {
            Command command = redoStack.pop();
            command.redo();
            undoStack.push(command);
            System.out.println("[INFO] Powtorzono cofniety ruch.");
        } else {
            System.out.println("[INFO] Brak ruchow do powtorzenia.");
        }
    }
}
