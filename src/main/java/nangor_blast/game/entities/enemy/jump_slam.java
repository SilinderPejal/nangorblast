package nangor_blast.game.entities.enemy;

import nangor_blast.game.entities.entity;
import nangor_blast.interfaces.blast_world;

public class jump_slam extends boss_attack {

    private int landing_x;
    private int landing_y;

    @Override
    public String get_name() {
        return "Jump Slam";
    }

    @Override
    public void prepare(boss_enemy boss, entity target, blast_world world) {

        landing_x = target.get_x();
        landing_y = target.get_y();

        int radius = boss.get_current_phase() == 1 ? 1 : 2;

        for (int dy = -radius; dy <= radius; dy++) {
            for (int dx = -radius; dx <= radius; dx++) {
                add_tile(world, landing_x + dx, landing_y + dy);
            }
        }
    }

    @Override
    public void on_active_start(boss_enemy boss, entity target, blast_world world) {

        if (world.is_walkable(landing_x, landing_y)) {
            boss.set_position(landing_x, landing_y);
        }
    }

    @Override
    public double get_active_duration(int phase) {
        return 1.0;
    }

    @Override
    public boolean grants_vulnerability() {
        return true;
    }
}
