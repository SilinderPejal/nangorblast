package nangor_blast.game.entities.enemy;

import nangor_blast.game.entities.entity;
import nangor_blast.interfaces.blast_world;

public class wall_crawl extends boss_attack {

    @Override
    public String get_name() {
        return "Wall Crawl";
    }

    @Override
    public void prepare(boss_enemy boss, entity target, blast_world world) {
        // TODO(keputusan kelompok): spek ga mendefinisikan "dinding arena" di map berbentuk labirin. Tentuin tile tujuan (mis. tile walkable di tepi map).
    }

    @Override
    public void on_active_start(boss_enemy boss, entity target, blast_world world) {
        // TODO: pindahin boss ke tile tujuan; fase 2 lanjutin scythe_swipe.
    }

    @Override
    public double get_active_duration(int phase) {
        return 1.0;
    }
}
