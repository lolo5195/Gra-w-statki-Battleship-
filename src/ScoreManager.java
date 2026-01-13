import java.io.*;

public class ScoreManager implements Observer {
    private String nick;
    private int score;
    private static String File = "results.txt";

    public ScoreManager(String nick){
        this.nick=nick;
        this.score=0;
    }
    @Override
    public void update(Board board) {
    }
    public void addhit(){
        this.score+=10;
        System.out.println("\n>>> Hit! +10 points.\nTotal score: " + this.score);
    }
    public void addmiss(){
        this.score-=1;
        System.out.println("\n>>> Miss! -1 points.\nTotal score: " + this.score);
    }
    public void saveScore(){
        try (FileWriter fw=new FileWriter(File,true );
             PrintWriter out=new PrintWriter(fw))
        {
            out.println(this.nick+" "+this.score);
            System.out.println("Score saved successfully.");
        } catch (IOException e) {
            System.out.println("Error saving score: " + e.getMessage());
        }
    }
    public String getNick() { return nick; }
    public int getScore() { return score; }
}