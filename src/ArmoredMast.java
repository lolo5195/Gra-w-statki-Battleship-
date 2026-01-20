public class ArmoredMast extends Mast {
    private int hp = 2; 

    public ArmoredMast(int x, int y) {
        super(x, y); 
    }

    @Override
    public void hit() {
        if (hp > 0) {
            hp--;
        }

        if (hp == 0) {
            super.hit();
        }
    }

    @Override
    public void repair() {
        if (hp < 2) {
            hp++;
        }
        if (hp > 0) {
            super.repair(); // przywróć isHit jeśli było ustawione
        }
    }
}