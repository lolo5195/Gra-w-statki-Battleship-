import java.io.*;

public class ScoreManager implements Observer {
    private String nick;
    private int score;
    private static final String FILE = "results.txt";

    public ScoreManager(String nick){
        this.nick=nick;
        this.score=0;
    }
    
    @Override
    public void update(Board board, FireResult result) {
        if (result == FireResult.HIT) {
            addhit();
        } else if (result == FireResult.MISS) {
            addmiss();
        }
    }
    
    @Override
    public void onUndo(Board board, FireResult originalResult) {
        // Cofnij punkty - odwrotnosc oryginalnej akcji
        if (originalResult == FireResult.HIT) {
            this.score -= 10;
            System.out.println("[UNDO] -10 za cofniecie trafienia. Wynik: " + this.score);
        } else if (originalResult == FireResult.MISS) {
            this.score += 1;
            System.out.println("[UNDO] +1 za cofniecie pudla. Wynik: " + this.score);
        }
    }
    
    private void addhit(){
        this.score+=10;
        System.out.println("[PUNKTY] +10 za trafienie. Wynik: " + this.score);
    }
    
    private void addmiss(){
        this.score-=1;
        System.out.println("[PUNKTY] -1 za pudlo. Wynik: " + this.score);
    }
    
    public void saveScore(){
        try (FileWriter fw=new FileWriter(FILE, true);
             PrintWriter out=new PrintWriter(fw))
        {
            out.println(this.nick+" "+this.score);
            System.out.println("Score saved successfully.");
        } catch (IOException e) {
            System.out.println("Error saving score: " + e.getMessage());
        }
    }
}
