import java.util.Random;

public class BotPlayer extends Player {
    private Random random;

    public BotPlayer() {
        this.random = new Random();
    }

    @Override
    public Coordinate getMove() {
        int x, y;
        char cell;
        int maxAttempts = 200;
        int attempts = 0;
        
        do {
            x = random.nextInt(enemyBoard.getGrid()[0].length);
            y = random.nextInt(enemyBoard.getGrid().length);
            cell = enemyBoard.getCell(new Coordinate(x, y));
            attempts++;
            
            if (attempts >= maxAttempts) {
                for (int i = 0; i < 10; i++) {
                    for (int j = 0; j < 10; j++) {
                        char c = enemyBoard.getCell(new Coordinate(i, j));
                        if (c != 'X' && c != 'O') {
                            return new Coordinate(i, j);
                        }
                    }
                }
                return new Coordinate(0, 0);
            }
        } while (cell == 'X' || cell == 'O');
        
        return new Coordinate(x, y);
    }

    @Override
    public String getName() {
        return "Bot";
    }
}