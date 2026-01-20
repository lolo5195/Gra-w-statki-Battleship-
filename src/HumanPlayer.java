import java.util.Scanner;

public class HumanPlayer extends Player {
    private Scanner scanner;
    private String name;
    
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
            
            if (input.equals("u") || input.equals("undo")) {
                return UNDO_ACTION;
            }
            if (input.equals("r") || input.equals("redo")) {
                return REDO_ACTION;
            }
            
            try {
                String[] parts = input.split("\\s+");
                if (parts.length != 2) {
                    System.out.println("[BLAD] Podaj dwie liczby oddzielone spacja!");
                    continue;
                }
                
                int col = Integer.parseInt(parts[0]) - 1;
                int row = Integer.parseInt(parts[1]) - 1;
                
                if (row < 0 || row >= 10 || col < 0 || col >= 10) {
                    System.out.println("[BLAD] Nieprawidlowe wspolrzedne! Podaj liczby od 1 do 10.");
                    continue;
                }
            
                char cell = enemyBoard.getCell(new Coordinate(col, row));
                if (cell == 'X' || cell == 'O') {
                    System.out.println("[BLAD] To pole bylo juz strzelane! Wybierz inne wspolrzedne.");
                    continue;
                }
            
                return new Coordinate(col, row);
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
