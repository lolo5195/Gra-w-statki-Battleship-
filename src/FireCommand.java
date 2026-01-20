public class FireCommand implements Command {

    private Board board;
    private Coordinate coord;

    private char previousValue;
    private char newValue;
    private FireResult lastResult; // Zapamietaj wynik strzalu

    public FireCommand(Board board, Coordinate coord) {
        this.board = board;
        this.coord = coord;
    }

    @Override
    public void execute() {
        previousValue = board.getCell(coord);
        board.shotAt(coord);
        newValue = board.getCell(coord);
        // Okresl wynik strzalu
        if (newValue == 'X') {
            lastResult = FireResult.HIT;
        } else if (newValue == 'O') {
            lastResult = FireResult.MISS;
        } else {
            lastResult = FireResult.EMPTY;
        }
    }

    @Override
    public void undo() {
        board.setCell(coord, previousValue);
        // Znajdz statek i napraw go
        ShipComponent ship = board.getShipAt(coord);
        if (ship != null && previousValue == 'S') {
            ship.repair();
        }
        // Cofnij punkty - powiadom obserwatorow o odwrotnym wyniku
        if (lastResult == FireResult.HIT) {
            board.notifyObserversUndo(FireResult.HIT);
        } else if (lastResult == FireResult.MISS) {
            board.notifyObserversUndo(FireResult.MISS);
        }
    }

    @Override
    public void redo() {
        board.setCell(coord, newValue);
        // Jesli to bylo trafienie, zarejestruj je ponownie
        if (newValue == 'X') {
            ShipComponent ship = board.getShipAt(coord);
            if (ship != null) {
                ship.registerHit(coord);
            }
        }
        // Przywroc punkty
        if (lastResult != null && lastResult != FireResult.EMPTY) {
            board.notifyObservers(lastResult);
        }
    }
}
