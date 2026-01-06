public class BoardBuilder {

    private Board board;

    public BoardBuilder() {
        this.board = new Board();
    }

    public BoardBuilder addShips() {

    board.placeShip(new Mast(2, 3));

 
    board.placeShip(new ArmoredMast(5, 5));

    Warship warship = new Warship();
    warship.addComponent(new Mast(1, 1));
    warship.addComponent(new ArmoredMast(1, 2));

    board.placeShip(warship);
        return this;
    }

    public Board build() {
        return board;
    }
}
