public class Main {
    public static void main(String[] args) {

        Board board = new BoardBuilder()
                .addShips()
                .build();

        System.out.println("Plansza została zbudowana");
        ConsoleView consoleView = new ConsoleView();
        consoleView.displayWelcomeScreen();
        board.attach(consoleView);
        board.notifyObservers();//ta komeda do wyswietlenia planszy jak cos fajnie dac po usatwieniu statkow 
    }
}
