public abstract class Player {
    protected Board enemyBoard;

    public abstract Coordinate getMove();

    public String getName() {
        return "Player";
    }

    public void setEnemyBoard(Board board) {
        this.enemyBoard = board;
    }
}