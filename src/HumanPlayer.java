import java.util.Scanner;

public class HumanPlayer extends Player {
    private Scanner scanner;
    private String name;
    
    // Specjalne współrzędne dla akcji
    public static final Coordinate UNDO_ACTION = new Coordinate(-1, -1);
    public static final Coordinate REDO_ACTION = new Coordinate(-2, -2);

    public HumanPlayer(String name) {
        this.name = name;
        this.scanner = new Scanner(System.in);
    }

    @Override
    public Coordinate getMove() {
        while (true) {
            System.out.print("Podaj wspolrzedne [kolumna wiersz] (1-10) lub [u]ndo/[r]edo: ");
            String input = scanner.nextLine().trim().toLowerCase();
            
            // Sprawdzenie komend specjalnych
            if (input.equals("u") || input.equals("undo")) {
                return UNDO_ACTION;
            }
            if (input.equals("r") || input.equals("redo")) {
                return REDO_ACTION;
            }
            
            // Parsowanie współrzędnych
            try {
                String[] parts = input.split("\\s+");
                if (parts.length != 2) {
                    System.out.println("[BLAD] Podaj dwie liczby oddzielone spacja!");
                    continue;
                }
                
                int col = Integer.parseInt(parts[0]) - 1;    // X = kolumna - pierwsza liczba
                int row = Integer.parseInt(parts[1]) - 1;    // Y = wiersz - druga liczba
                
                // Walidacja wspolrzednych
                if (row < 0 || row >= 10 || col < 0 || col >= 10) {
                    System.out.println("[BLAD] Nieprawidlowe wspolrzedne! Podaj liczby od 1 do 10.");
                    continue;
                }
            
                // Sprawdzenie czy pole juz bylo strzelane
                char cell = enemyBoard.getCell(new Coordinate(col, row));
                if (cell == 'X' || cell == 'O') {
                    System.out.println("[BLAD] To pole bylo juz strzelane! Wybierz inne wspolrzedne.");
                    continue;
                }
            
                return new Coordinate(col, row);  // X = kolumna, Y = wiersz
            } catch (NumberFormatException e) {
                System.out.println("[BLAD] Nieprawidlowe dane! Podaj dwie liczby od 1 do 10.");
            }
        }
    }

    @Override
    public String getName() {
        return name;
    }
}
