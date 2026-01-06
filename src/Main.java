public class Main {
    public static void main(String[] args) {

        Board board = new BoardBuilder()
                .addShips()
                .build();

        System.out.println("Plansza została zbudowana");
    }
}
