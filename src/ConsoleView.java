public class ConsoleView implements Observer {
    @Override
    public void update(Board board, FireResult result) {
        System.out.println("\n\n\n\n\n\n\n\n\n\n");
        System.out.println("========Current Board State:=======");
        char[][] grid = board.getGrid();
        System.out.print("     ");
        for(int i=0;i<grid[0].length;i++){
            System.out.print((char)('A'+i)+" ");
        }
        System.out.println(" ");
        System.out.println("   " + "---------------------");

        for(int i=0;i<grid.length;i++){
            System.out.printf("%2d | ",i+1);
            for(int j=0;j<grid[i].length;j++){
                System.out.print(grid[i][j]+" ");
            }
            System.out.println();
        }
        System.out.println("===================================");
        System.out.println("Legend: ~ Water | S Ship | X Hit | O Missed");
    }
    public void displayWelcomeScreen() {
        System.out.println("===================================");
        System.out.println(" WELCOME TO BATTLESHIP GAME! ");
        System.out.println("===================================");
        System.out.println("1 - Start New Game");
        System.out.println("2 - Rankings");
        System.out.println("3 - Exit");
    }
    
}
