import java.util.Random;
import java.util.Scanner;

public class BoardBuilder {

    private Board board;
    private Scanner scanner;
    private Random random;

    public BoardBuilder() {
        this.board = new Board();
        this.scanner = new Scanner(System.in);
        this.random = new Random();
    }

    // --- Dodanie statków gracza ręcznie ---
    public BoardBuilder addPlayerShips() {
        System.out.println("Rozmieść swoje statki:");
        boolean adding = true;

        while (adding) {
            System.out.println("Wybierz typ statku:");
            System.out.println("1 - Mast");
            System.out.println("2 - Armored Mast");
            System.out.println("3 - Warship (2 komponenty)");
            System.out.println("0 - Zakończ rozmieszczanie");

            int choice = scanner.nextInt();
            if (choice == 0) {
                adding = false;
                continue;
            }

            System.out.print("Podaj współrzędną X (1-10): ");
            int x = scanner.nextInt() - 1; // konwersja na indeks 0-9
            System.out.print("Podaj współrzędną Y (1-10): ");
            int y = scanner.nextInt() - 1;

            switch (choice) {
                case 1 -> placeIfValid(new Mast(x, y));
                case 2 -> placeIfValid(new ArmoredMast(x, y));
                case 3 -> {
                    Warship warship = new Warship();
                    System.out.println("Dodaj komponent 1 do Warship:");
                    int x1 = scanner.nextInt() - 1;
                    int y1 = scanner.nextInt() - 1;
                    if (!isValidCoordinate(x1, y1)) {
                        System.out.println("Niepoprawne współrzędne komponentu 1!");
                        continue;
                    }
                    warship.addComponent(new Mast(x1, y1));

                    System.out.println("Dodaj komponent 2 do Warship:");
                    int x2 = scanner.nextInt() - 1;
                    int y2 = scanner.nextInt() - 1;
                    if (!isValidCoordinate(x2, y2)) {
                        System.out.println("Niepoprawne współrzędne komponentu 2!");
                        continue;
                    }
                    warship.addComponent(new ArmoredMast(x2, y2));

                    placeIfValid(warship);
                }
                default -> System.out.println("Nieprawidłowy wybór!");
            }
        }
        return this;
    }

    // --- Dodanie statków Bota losowo ---
    public BoardBuilder addBotShips(int numberOfShips) {
        System.out.println("Rozmieszczanie statków Bota...");
        int shipsPlaced = 0;

        while (shipsPlaced < numberOfShips) {
            int type = random.nextInt(3); // 0 - Mast, 1 - Armored, 2 - Warship
            boolean placed = false;

            switch (type) {
                case 0 -> {
                    int x = random.nextInt(10);
                    int y = random.nextInt(10);
                    placed = placeIfValid(new Mast(x, y));
                }
                case 1 -> {
                    int x = random.nextInt(10);
                    int y = random.nextInt(10);
                    placed = placeIfValid(new ArmoredMast(x, y));
                }
                case 2 -> {
                    Warship warship = new Warship();
                    int x1, y1, x2, y2;
                    int attempts = 0;
                    do {
                        x1 = random.nextInt(10);
                        y1 = random.nextInt(10);
                        x2 = random.nextInt(10);
                        y2 = random.nextInt(10);
                        attempts++;
                        if (attempts > 100) break; // zabezpieczenie przed zapętleniem
                    } while (!isValidCoordinate(x1, y1) || !isValidCoordinate(x2, y2));

                    if (isValidCoordinate(x1, y1) && isValidCoordinate(x2, y2)) {
                        warship.addComponent(new Mast(x1, y1));
                        warship.addComponent(new ArmoredMast(x2, y2));
                        placed = placeIfValid(warship);
                    }
                }
            }

            if (placed) shipsPlaced++;
        }

        return this;
    }

    // --- Budowa planszy ---
    public Board build() {
        return board;
    }

    // --- Sprawdzenie, czy można postawić statek ---
    private boolean placeIfValid(ShipComponent ship) {
        for (Coordinate c : ship.getCoordinates()) {
            if (!isValidCoordinate(c.getX(), c.getY())) {
                System.out.println("Nie można postawić statku na polu " + c + " lub obok niego.");
                return false;
            }
        }
        board.placeShip(ship);
        return true;
    }

    // --- Walidacja pojedynczej współrzędnej ---
    private boolean isValidCoordinate(int x, int y) {
        if (x < 0 || x >= 10 || y < 0 || y >= 10) return false;

        // Sprawdzenie 8 sąsiednich pól + własne
        for (int dx = -1; dx <= 1; dx++) {
            for (int dy = -1; dy <= 1; dy++) {
                int nx = x + dx;
                int ny = y + dy;
                if (nx >= 0 && nx < 10 && ny >= 0 && ny < 10) {
                    if (board.getCell(new Coordinate(nx, ny)) == 'S') return false;
                }
            }
        }
        return true;
    }
}
