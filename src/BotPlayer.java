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
        int x = random.nextInt(enemyBoard.getGrid().length);
        int y = random.nextInt(enemyBoard.getGrid()[0].length);
        return new Coordinate(x, y);
    }

    @Override
    public String getName() {
        return "Bot (difficulty: " + difficulty + ")";
    }
}