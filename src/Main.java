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
                case 1 -> {
                    System.out.print("Podaj swoja nazwe: ");
                    String nick = scanner.nextLine();
                    ScoreManager scoreManager = new ScoreManager(nick);

                    GameEngine engine = GameEngine.getInstance();
                    engine.setPlayerName(nick);
                    engine.setupGame();

                    engine.getBotBoard().attach(scoreManager);

                    Player winner = engine.startGame();
                    
                    if(winner != null) {
                        scoreManager.setPlayerWon(winner instanceof HumanPlayer);
                        scoreManager.saveScore();
                    }
                }
                case 2 -> displayHistory();
                case 3 -> {
                    System.out.println("Do widzenia!");
                    running = false;
                }
                default -> System.out.println("Nieprawidlowy wybor! Sprobuj ponownie.");
            }
        }
    }

    private static void displayHistory() {
        try (BufferedReader reader = new BufferedReader(new FileReader("results.txt"))) {
            System.out.println();
            System.out.println("+============================================+");
            System.out.println("|            HISTORIA GIER                  |");
            System.out.println("+============================================+");
            String line;
            int count = 0;
            while ((line = reader.readLine()) != null) {
                System.out.println("  " + line);
                count++;
            }
            if (count == 0) {
                System.out.println("  Brak zapisanych gier.");
            }
            System.out.println("+============================================+");
            System.out.println();
        } catch (IOException e) {
            System.out.println("Brak zapisanych gier.");
        }
    }
}
