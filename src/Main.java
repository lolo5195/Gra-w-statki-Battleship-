import java.io.*;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        ConsoleView consoleView = new ConsoleView();
        Scanner scanner = new Scanner(System.in);
        boolean running = true;

        while (running) {
            consoleView.displayWelcomeScreen();
            System.out.print("Wybierz opcję: ");
            
            int choice = -1;
            try {
                String input = scanner.nextLine();
                choice = Integer.parseInt(input);
            } catch (NumberFormatException e) {
                System.out.println("Nieprawidłowy wybór! Wpisz liczbę.");
                continue;
            }

            switch (choice) {
                case 1 -> { // Nowa gra
                    System.out.print("Podaj swoją nazwę: ");
                    String nick = scanner.nextLine();
                    ScoreManager scoreManager = new ScoreManager(nick);

                    GameEngine engine = GameEngine.getInstance();
                    engine.setupGame();

                    // ScoreManager podpinamy do planszy BOTA - gracz zdobywa punkty za trafianie bota
                    engine.getBotBoard().attach(scoreManager);
                    // ConsoleView podpinamy do planszy bota - gracz widzi planszę przeciwnika
                    engine.getBotBoard().attach(consoleView);

                    Player winner = engine.startGame();
                    
                    // Zapis wyników po zakończeniu gry
                    scoreManager.saveScore();
                }
                case 2 -> displayRanking();
                case 3 -> {
                    System.out.println("Do widzenia!");
                    running = false;
                }
                default -> System.out.println("Nieprawidłowy wybór! Spróbuj ponownie.");
            }
        }
    }

    private static void displayRanking() {
        try (BufferedReader reader = new BufferedReader(new FileReader("results.txt"))) {
            System.out.println("\n===== Ranking =====");
            String line;
            while ((line = reader.readLine()) != null) System.out.println(line);
            System.out.println("==================\n");
        } catch (IOException e) {
            System.out.println("Brak zapisanych wyników.");
        }
    }
}
