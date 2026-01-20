import java.util.Random;

public class BotPlayer extends Player {
    private Random random;
    private int difficulty;

    public BotPlayer(int difficulty) {
        this.difficulty = difficulty;
        this.random = new Random();
    }

    @Override
    public Coordinate getMove() {
        int x, y;
        char cell;
        // Losuj dopóki nie znajdziesz nieostrzelanego pola (nie X ani O)
        do {
            x = random.nextInt(enemyBoard.getGrid()[0].length);
            y = random.nextInt(enemyBoard.getGrid().length);
            cell = enemyBoard.getCell(new Coordinate(x, y));
        } while (cell == 'X' || cell == 'O');
        
        return new Coordinate(x, y);
    }

    @Override
    public String getName() {
        return "Bot (difficulty: " + difficulty + ")";
    }
}