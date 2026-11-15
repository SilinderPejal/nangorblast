package nangor_blast.game.entities.enemy;

import nangor_blast.game.entities.entity;
import nangor_blast.interfaces.blast_world;

// Berpindah cepat menyusuri dinding arena; tidak memberi damage selama berpindah.
// Fase 2: setelah berpindah, langsung Scythe Swipe ke arah player.
// Tidak membuat boss rentan.
public class wall_crawl extends boss_attack {

    @Override
    public String get_name() {
        return "Wall Crawl";
    }

    @Override
    public void prepare(boss_enemy boss, entity target, blast_world world) {
        // TODO(keputusan tim): spek tidak mendefinisikan "dinding arena" pada map
        // berbentuk labirin. Tentukan tile tujuan (mis. tile walkable di tepi map).
    }

    @Override
    public void on_active_start(boss_enemy boss, entity target, blast_world world) {
        // TODO: pindahkan boss ke tile tujuan; fase 2 lanjutkan scythe_swipe.
    }

    @Override
    public double get_active_duration(int phase) {
        return 1.0;
    }
}
