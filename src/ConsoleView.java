public class ConsoleView {
    
    // Wyswietlanie obu plansz obok siebie
    public void displayBothBoards(Board playerBoard, Board enemyBoard) {
        System.out.println();
        
        char[][] playerGrid = playerBoard.getGrid();
        char[][] enemyGrid = enemyBoard.getGrid();
        
        // Naglowki
        System.out.println("        TWOJA PLANSZA                  PLANSZA PRZECIWNIKA");
        System.out.println("   +---------------------+           +---------------------+");
        
        // Wiersze z danymi - wyswietlamy od dolu (wiersz 10 na gorze, wiersz 1 na dole)
        for(int i=9; i>=0; i--) {
            // Lewa plansza (gracza)
            System.out.printf("%2d | ", i+1);
            for(int j=0; j<10; j++) {
                System.out.print(playerGrid[i][j]+" ");
            }
            System.out.print("|        ");
            
            // Prawa plansza (przeciwnika) - ukrywamy statki 'S' jako '~'
            System.out.printf("%2d | ", i+1);
            for(int j=0; j<10; j++) {
                char cell = enemyGrid[i][j];
                if(cell == 'S') {
                    System.out.print("~ ");
                } else {
                    System.out.print(cell+" ");
                }
            }
            System.out.println("|");
        }
        
        // Separator dolny i wspolrzedne X
        System.out.println("   +---------------------+           +---------------------+");
        System.out.println("     1 2 3 4 5 6 7 8 9 10              1 2 3 4 5 6 7 8 9 10");
        System.out.println();
        System.out.println("Legenda:  ~ Woda  |  S Statek  |  X Trafienie  |  O Pudlo");
        System.out.println();
    }
    
    public void displayWelcomeScreen() {
        System.out.println();
        System.out.println("+===================================+");
        System.out.println("|     GRA W STATKI - BATTLESHIP     |");
        System.out.println("+===================================+");
        System.out.println("|  1 - Nowa gra                     |");
        System.out.println("|  2 - Ranking                      |");
        System.out.println("|  3 - Wyjscie                      |");
        System.out.println("+===================================+");
    }
}
