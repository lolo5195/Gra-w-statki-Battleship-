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

    // Pobranie planszy gracza
    public Board getPlayerBoard() {
        return playerBoard;
    }

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

    // Przygotowanie gry
    public void setupGame() {
        // Czyszczenie stosów undo/redo przy rozpoczęciu nowej gry
        undoStack.clear();
        redoStack.clear();
        
        // Budowanie plansz
        playerBoard = new BoardBuilder().addPlayerShips().build();
        botBoard = new BoardBuilder().addBotShips(5).build();

        // Tworzymy graczy
        player1 = new HumanPlayer("Player1");
        player2 = new BotPlayer(1);

        // Ustawienie planszy przeciwnika
        player1.setEnemyBoard(botBoard);
        player2.setEnemyBoard(playerBoard);

        System.out.println("Game setup completed.");
    }

    // Rozpoczęcie gry
    public Player startGame() {
        System.out.println("\nGame started!");
        isRunning = true;

        while (!playerBoard.isGameOver() && !botBoard.isGameOver() && isRunning) {
            processTurn(player1, botBoard);
            if (!botBoard.isGameOver() && isRunning) {
                processTurn(player2, playerBoard);
            }
        }

        isRunning = false;

        if (playerBoard.isGameOver()) {
            System.out.println("Bot wins!");
            return player2;
        } else if (botBoard.isGameOver()) {
            System.out.println(player1.getName() + " wins!");
            return player1;
        } else {
            System.out.println("Game stopped.");
            return null;
        }
    }

    // Wykonanie tury gracza
    private void processTurn(Player player, Board targetBoard) {
        System.out.println("\n" + player.getName() + "'s turn.");
        Coordinate move = player.getMove();

        // Utworzenie komendy FireCommand
        Command fire = new FireCommand(targetBoard, move);
        undoStack.push(fire); // dodanie do stosu undo
        fire.execute();        // wykonanie strzału
        // notifyObservers wywoływane jest w shotAt() z odpowiednim FireResult
    }

    // Cofnięcie ostatniego ruchu
    public void undoLastMove() {
        if (!undoStack.isEmpty()) {
            Command command = undoStack.pop();
            command.undo();
            redoStack.push(command);
            System.out.println("Undo completed.");
        } else {
            System.out.println("No moves to undo.");
        }
    }

    // Powtórzenie cofniętego ruchu
    public void redoLastMove() {
        if (!redoStack.isEmpty()) {
            Command command = redoStack.pop();
            command.redo();
            undoStack.push(command);
            System.out.println("Redo completed.");
        } else {
            System.out.println("No moves to redo.");
        }
    }

    // Zatrzymanie gry
    public void stopGame() {
        isRunning = false;
        System.out.println("Game stopped.");
    }
}
