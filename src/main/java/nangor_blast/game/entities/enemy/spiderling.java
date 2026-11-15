package nangor_blast.game.entities.enemy;

import nangor_blast.game.entities.entity;
import nangor_blast.interfaces.blast_world;

public class spiderling extends boss_attack {

    private static final double hatchling_speed = 1.5;
    private static final int search_radius = 3;

    @Override
    public String get_name() {
        return "Spiderling";
    }

    @Override
    public void prepare(boss_enemy boss, entity target, blast_world world) {
        // Tidak ada area damage. Tile pemanggilan ditentukan saat serangan aktif.
    }

    @Override
    public void on_active_start(boss_enemy boss, entity target, blast_world world) {

        int count = boss.get_current_phase() == 1 ? 3 : 4;

        for (int radius = 1; radius <= search_radius && count > 0; radius++) {
            for (int dy = -radius; dy <= radius && count > 0; dy++) {
                for (int dx = -radius; dx <= radius && count > 0; dx++) {

                    if (Math.max(Math.abs(dx), Math.abs(dy)) != radius) {
                        continue;
                    }

                    int tx = boss.get_x() + dx;
                    int ty = boss.get_y() + dy;

                    boolean on_target = target != null
                        && target.get_x() == tx && target.get_y() == ty;

                    if (
                        !world.is_out_of_bounds(tx, ty)
                        && world.is_walkable(tx, ty)
                        && !on_target
                    ) {
                        boss.request_spawn(new hatchling(tx, ty, hatchling_speed));
                        count--;
                    }
                }
            }
        }
    }


    @Override
    public double get_active_duration(int phase) {
        return 1.0;
    }
}
