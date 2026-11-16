import java.util.List;

import nangor_blast.game.entities.enemy.attack_phase;
import nangor_blast.game.entities.enemy.chase_enemy;
import nangor_blast.game.entities.enemy.enemy_state;
import nangor_blast.game.entities.enemy.mantis;
import nangor_blast.game.entities.enemy.scythe_swipe;
import nangor_blast.game.entities.enemy.wall_crawl;
import nangor_blast.game.entities.enemy.hatchling;
import nangor_blast.game.entities.enemy.patrol_enemy;
import nangor_blast.game.entities.player.player1;
import nangor_blast.game.environment.game_map;
import nangor_blast.game.system.map_blast_world;

public class enemy_test {

    static int pass = 0;
    static int fail = 0;

    static void check(String name, boolean ok) {
        if (ok) { pass++; System.out.println("PASS  " + name); }
        else { fail++; System.out.println("FAIL  " + name); }
    }

    public static void main(String[] args) {

        map_blast_world world = new map_blast_world(new game_map());
        player1 p = new player1(1, 1);

        patrol_enemy pe = new patrol_enemy(3, 3, 2.0, List.of(new int[] { 3, 3 }, new int[] { 6, 3 }));
        for (int i = 0; i < 20; i++) pe.update(0.25, world, p);
        check("patrol bergerak dari spawn", pe.get_x() != 3 || pe.get_y() != 3);
        pe.take_damage(1);
        check("normal enemy mati 1 damage", !pe.is_active());

        chase_enemy ce = new chase_enemy(9, 8, 1.0, List.of(new int[] { 9, 8 }));
        ce.update(0.1, world, p);
        check("chase PATROL saat player jauh", ce.get_state() == enemy_state.patrol);
        ce.set_position(3, 1);
        ce.update(0.1, world, p);
        check("chase CHASE saat player <= 3 tile", ce.get_state() == enemy_state.chase);

        mantis m = new mantis(9, 7);
        m.take_damage(1);
        check("boss kebal saat tidak rentan", m.get_health() == 8);

        boolean pernah_warning = false, pernah_rentan = false;
        p.set_position(9, 8);
        for (int i = 0; i < 600 && !pernah_rentan; i++) {
            m.update(0.1, world, p);
            if (m.get_attack_state() == attack_phase.warning) pernah_warning = true;
            if (m.is_vulnerable()) pernah_rentan = true;
        }
        check("boss melewati warning", pernah_warning);
        check("boss pernah rentan setelah swipe/slam", pernah_rentan);

        if (pernah_rentan) {
            m.take_damage(2);
            check("pumped (2 damage) mengurangi 2 health", m.get_health() == 6);
            m.take_damage(2);
            check("masuk fase 2 saat health <= 4", m.get_current_phase() == 2);
            check("wall crawl fase 2 diikuti scythe swipe",
                new wall_crawl().get_follow_up(m) instanceof scythe_swipe);
        }

        mantis crawler = new mantis(9, 7);
        player1 jauh = new player1(1, 1);
        wall_crawl crawl = new wall_crawl();
        int jarak_awal = Math.abs(crawler.get_x() - 1) + Math.abs(crawler.get_y() - 1);
        crawl.prepare(crawler, jauh, world);
        check("wall crawl tanpa area damage", crawl.get_affected_tiles().isEmpty());
        crawl.on_active_start(crawler, jauh, world);
        for (int i = 0; i < 10; i++) crawl.on_active_tick(crawler, 0.1, world);
        crawl.on_active_end(crawler, world);
        int jarak_akhir = Math.abs(crawler.get_x() - 1) + Math.abs(crawler.get_y() - 1);
        check("wall crawl mendekati player", jarak_akhir < jarak_awal);
        check("wall crawl berakhir di tile walkable", world.is_walkable(crawler.get_x(), crawler.get_y()));
        check("wall crawl fase 1 tanpa serangan lanjutan",
            crawl.get_follow_up(crawler) == null);

        hatchling h = new hatchling(5, 5, 1.5);
        check("hatchling = normal enemy (100 poin, 1 nyawa)",
            h.get_score_value() == 100 && h.get_health() == 1);

        System.out.println(pass + " lulus, " + fail + " gagal");
    }
}
