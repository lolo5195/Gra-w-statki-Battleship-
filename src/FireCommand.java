public class FireCommand implements Command {

    private Board board;
    private Coordinate coord;

    private char previousValue;
    private char newValue;

    public FireCommand(Board board, Coordinate coord) {
        this.board = board;
        this.coord = coord;
    }

    @Override
    public void execute() {
        previousValue = board.getCell(coord);
        board.shotAt(coord);
        newValue = board.getCell(coord);
    }

    @Override
    public void undo() {
        board.setCell(coord, previousValue);
    }

    @Override
    public void redo() {
        board.setCell(coord, newValue);
    }
}