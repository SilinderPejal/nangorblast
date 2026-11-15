package nangor_blast.game.entities.enemy;

public class mantis extends boss_enemy {

    private static final int mantis_health = 8;
    private static final int mantis_score = 500;
    private static final double mantis_speed = 1.0;

    public mantis(int x, int y) {
        super(x, y, mantis_health, mantis_speed, mantis_score);

        attacks.add(new scythe_swipe());
        attacks.add(new jump_slam());
        attacks.add(new spiderling());
        attacks.add(new wall_crawl());
    }
}
