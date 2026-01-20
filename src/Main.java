import java.io.*;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        ConsoleView consoleView = new ConsoleView();
        Scanner scanner = new Scanner(System.in);
        boolean running = true;

        while (running) {
            consoleView.displayWelcomeScreen();
            System.out.print("Wybierz opcje: ");
            
            int choice = -1;
            try {
                String input = scanner.nextLine();
                choice = Integer.parseInt(input);
            } catch (NumberFormatException e) {
                System.out.println("Nieprawidlowy wybor! Wpisz liczbe.");
                continue;
            }

            switch (choice) {
                case 1 -> { // Nowa gra
                    System.out.print("Podaj swoja nazwe: ");
                    String nick = scanner.nextLine();
                    ScoreManager scoreManager = new ScoreManager(nick);

                    GameEngine engine = GameEngine.getInstance();
                    engine.setPlayerName(nick);  // Ustaw nazwe gracza
                    engine.setupGame();

                    // ScoreManager podpinamy do planszy BOTA - gracz zdobywa punkty za trafianie bota
                    engine.getBotBoard().attach(scoreManager);

                    Player winner = engine.startGame();
                    
                    // Zapis wynikow po zakonczeniu gry
                    if(winner != null) {
                        scoreManager.saveScore();
                    }
                }
                case 2 -> displayRanking();
                case 3 -> {
                    System.out.println("Do widzenia!");
                    running = false;
                }
                default -> System.out.println("Nieprawidlowy wybor! Sprobuj ponownie.");
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
            System.out.println("Brak zapisanych wynikow.");
        }
    }
}
