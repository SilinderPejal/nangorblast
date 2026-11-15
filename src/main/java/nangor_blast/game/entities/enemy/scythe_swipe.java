package nangor_blast.game.entities.enemy;

import nangor_blast.game.entities.entity;
import nangor_blast.interfaces.blast_world;

// Mengayun sabit ke arah player: 3 tile melebar di depan Mantis.
// Fase 2: area 3 x 2. Durasi 1 detik. Membuat boss rentan.
public class scythe_swipe extends boss_attack {

    @Override
    public String get_name() {
        return "Scythe Swipe";
    }

    @Override
    public void prepare(boss_enemy boss, entity target, blast_world world) {

        // Arah dikunci saat warning mulai: sumbu dengan jarak terbesar ke player.
        int dist_x = target.get_x() - boss.get_x();
        int dist_y = target.get_y() - boss.get_y();

        int dx = 0;
        int dy = 0;

        if (Math.abs(dist_x) >= Math.abs(dist_y)) {
            dx = Integer.signum(dist_x);
        } else {
            dy = Integer.signum(dist_y);
        }

        if (dx == 0 && dy == 0) {
            dy = 1;
        }

        boss.set_facing(dx, dy);

        int depth = boss.get_current_phase() == 1 ? 1 : 2;

        // Tegak lurus arah hadap: (-dy, dx).
        for (int d = 1; d <= depth; d++) {
            for (int side = -1; side <= 1; side++) {
                add_tile(
                    world,
                    boss.get_x() + dx * d + (-dy) * side,
                    boss.get_y() + dy * d + dx * side
                );
            }
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
