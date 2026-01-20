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

    public Board getBotBoard() {
        return botBoard;
    }

    private GameEngine() {
        undoStack = new Stack<>();
        redoStack = new Stack<>();
        isRunning = false;
    }

    public static GameEngine getInstance() {
        if (instance == null) {
            instance = new GameEngine();
        }
        return instance;
    }

    public void setPlayerName(String name) {
        this.playerName = name;
    }

    public void setupGame() {
        undoStack.clear();
        redoStack.clear();
        isRunning = false;
        
        playerBoard = new BoardBuilder().addPlayerShips().build();
        botBoard = new BoardBuilder().addBotShips(5).build();

        player1 = new HumanPlayer(playerName != null ? playerName : "Player1");
        player2 = new BotPlayer();

        player1.setEnemyBoard(botBoard);
        player2.setEnemyBoard(playerBoard);

        System.out.println("[INFO] Przygotowanie gry zakonczone.");
    }

    public Player startGame() {
        System.out.println("\n[INFO] Gra rozpoczeta!");
        isRunning = true;
        ConsoleView view = new ConsoleView();
        
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

    private void processTurn(Player player, Board targetBoard, ConsoleView view) {
        System.out.println();
        System.out.println("--- Tura: " + player.getName() + " ---");
        
        if(player instanceof HumanPlayer) {
            view.displayBothBoards(playerBoard, botBoard);
        }
        
        Coordinate move = player.getMove();
        
        if (player instanceof HumanPlayer) {
            if (move.equals(HumanPlayer.UNDO_ACTION)) {
                undoLastMove();
                view.displayBothBoards(playerBoard, botBoard);
                processTurn(player, targetBoard, view);
                return;
            }
            if (move.equals(HumanPlayer.REDO_ACTION)) {
                redoLastMove();
                view.displayBothBoards(playerBoard, botBoard);
                processTurn(player, targetBoard, view);
                return;
            }
        }

        Command fire = new FireCommand(targetBoard, move);
        undoStack.push(fire);
        redoStack.clear();
        fire.execute();
        
        // Wyswietl plansze po ruchu
        view.displayBothBoards(playerBoard, botBoard);
        
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }

    public void undoLastMove() {
        int movesToUndo = Math.min(2, undoStack.size());
        
        if (movesToUndo == 0) {
            System.out.println("[INFO] Brak ruchow do cofniecia.");
            return;
        }
        
        for (int i = 0; i < movesToUndo; i++) {
            Command command = undoStack.pop();
            command.undo();
            redoStack.push(command);
        }
        System.out.println("[INFO] Cofnieto " + movesToUndo + " ruch(y).");
    }

    public void redoLastMove() {
        int movesToRedo = Math.min(2, redoStack.size());
        
        if (movesToRedo == 0) {
            System.out.println("[INFO] Brak ruchow do powtorzenia.");
            return;
        }
        
        for (int i = 0; i < movesToRedo; i++) {
            Command command = redoStack.pop();
            command.redo();
            undoStack.push(command);
        }
        System.out.println("[INFO] Powtorzono " + movesToRedo + " ruch(y).");
    }
}
