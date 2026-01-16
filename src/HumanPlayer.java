import java.util.Scanner;

public class HumanPlayer extends Player {
    private Scanner scanner;
    private String name;

    public HumanPlayer(String name) {
        this.name = name;
        this.scanner = new Scanner(System.in);
    }

    @Override
    public Coordinate getMove() {
        System.out.println("Enter your move (x y): ");
        int x = scanner.nextInt() -1 ;
        int y = scanner.nextInt() -1;
        return new Coordinate(x, y);
    }

    @Override
    public String getName() {
        return name;
    }
}