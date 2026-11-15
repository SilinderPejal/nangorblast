package nangor_blast.game.entities.enemy;

// Normal Enemy: 1 nyawa, 100 poin (F05.2, F08).
public abstract class normal_enemy extends enemy {

    protected static final int normal_health = 1;
    protected static final int normal_score = 100;

    protected normal_enemy(int x, int y, double speed) {
        super(x, y, normal_health, speed, normal_score);
    }
}
