import java.io.*;
import java.util.Stack;

public class GameEngine {
    private static GameEngine instance;
    private Board board;
    private Player player1;
    private Player player2;
    private boolean isRunning;
    private Stack<Command> undoStack;
    private Stack<Command> redoStack;

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

    public void setupGame() {
        BoardBuilder builder = new BoardBuilder();
        board = builder.addShips().build(); // Budowa planszy
        player1 = new HumanPlayer("Player1");
        player2 = new BotPlayer(1); // Bot z poziomem trudności
        System.out.println("Game setup completed.");
    }

    public void startGame() {
        System.out.println("Game started!");
        isRunning = true;
        while (!board.isGameOver() && isRunning) {
            processTurn(player1);
            if (!board.isGameOver()) {
                processTurn(player2);
            }
        }
        isRunning = false;
        System.out.println("Game Over!");
    }

    private void processTurn(Player player) {
        System.out.println(player.getName() + "'s turn.");
        Coordinate move = player.getMove();
        Command fire = new FireCommand(board, move);
        undoStack.push(fire); // Zapis komendy w historii
        fire.execute(); // Wykonanie komendy strzału
        board.notifyObservers();
    }

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

    public void saveGame(String filename) throws IOException {
        try (FileWriter writer = new FileWriter(filename)) {
            // Serializacja stanu planszy
            writer.write(board.toString());
            System.out.println("Game saved to " + filename);
        }
    }

    public void loadGame(String filename) throws IOException {
        try (BufferedReader reader = new BufferedReader(new FileReader(filename))) {
            String line = reader.readLine();
            // Deserializacja stanu planszy (do późniejszego zaimplementowania)
            System.out.println("Game loaded from " + filename);
        }
    }
}