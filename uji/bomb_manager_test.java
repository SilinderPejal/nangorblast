import nangor_blast.game.entities.bomb.bomb_type;
import nangor_blast.game.entities.player.player1;
import nangor_blast.game.environment.game_map;
import nangor_blast.game.system.bomb_manager;
import nangor_blast.game.system.map_blast_world;

// Dijalankan lewat terminal (bukan gradle). Memakai game_map dan player1 asli milik Erin.
public class bomb_manager_test {

    static int pass = 0;
    static int fail = 0;

    static void check(String name, boolean ok) {
        if (ok) {
            pass++;
            System.out.println("PASS  " + name);
        } else {
            fail++;
            System.out.println("FAIL  " + name);
        }
    }

    static bomb_manager baru(player1 p) {
        bomb_manager m = new bomb_manager(new map_blast_world(new game_map()));
        m.add_target(p);
        return m;
    }

    // Maju n langkah masing-masing 0.25 detik (update() membatasi dt maksimum 0.25).
    static void langkah(bomb_manager m, int n) {
        for (int i = 0; i < n; i++) {
            m.update(0.25);
        }
    }

    public static void main(String[] args) {
        // 1. pasang bom
        player1 p = new player1(1, 1);
        bomb_manager m = baru(p);
        check("awal: tidak ada aktivitas", !m.has_activity());
        check("place normal berhasil", m.place_bomb(p, bomb_type.normal));
        check("bom tercatat di posisi player", m.get_bomb_at(1, 1) != null);
        check("active_bombs player = 1", p.get_active_bombs() == 1);
        check("ada aktivitas", m.has_activity());
        check("kapasitas 1 -> bom kedua ditolak", !m.place_bomb(p, bomb_type.normal));

        // 2. fuse dan ledakan
        langkah(m, 10);
        check("2.5 detik: bom belum meledak", m.get_bombs().size() == 1 && m.get_explosions().isEmpty());
        p.set_position(8, 8);
        langkah(m, 2);
        check("3.0 detik: bom hilang, explosion muncul", m.get_bombs().isEmpty() && m.get_explosions().size() == 1);
        check("active_bombs kembali 0", p.get_active_bombs() == 0);
        check("player yang menjauh tidak terkena", p.get_lives() == 3);
        m.update(0.25);
        check("0.25 detik: explosion masih ada", m.get_explosions().size() == 1);
        m.update(0.25);
        check("0.5 detik: explosion hilang", m.get_explosions().isEmpty() && !m.has_activity());

        // 3. player kena bom sendiri
        p = new player1(5, 5);
        m = baru(p);
        m.place_bomb(p, bomb_type.normal);
        langkah(m, 12);
        check("diam di tempat -> kehilangan 1 nyawa", p.get_lives() == 2);

        // 4. remote butuh unlock dan DETONATE
        p = new player1(3, 3);
        m = baru(p);
        check("remote ditolak sebelum unlock", !m.place_bomb(p, bomb_type.remote));
        p.unlock_remote();
        check("remote diterima setelah unlock", m.place_bomb(p, bomb_type.remote));
        langkah(m, 4);
        check("remote tidak meledak sendiri", m.get_bombs().size() == 1);
        p.set_position(9, 9);
        check("detonate_remote memicu 1 bom", m.detonate_remote(p) == 1);
        m.update(0.0);
        check("remote meledak setelah DETONATE", m.get_bombs().isEmpty() && m.get_explosions().size() == 1);
        check("detonate tanpa bom -> 0", m.detonate_remote(p) == 0);
        player1 lain = new player1(1, 1);
        check("detonate tanpa unlock -> 0", m.detonate_remote(lain) == 0);

        // 5. pumped
        p = new player1(2, 2);
        m = baru(p);
        check("pumped berhasil", m.place_bomb(p, bomb_type.pumped));
        langkah(m, 17);
        check("pumped: 4.25 detik belum meledak", m.get_bombs().size() == 1);
        m.update(0.25);
        check("pumped: 4.5 detik meledak", m.get_bombs().isEmpty());

        // 6. bom ganda di satu tile dan rantai
        p = new player1(4, 4);
        p.increase_bomb_capacity();
        p.increase_bomb_capacity();
        m = baru(p);
        m.place_bomb(p, bomb_type.normal);
        check("bom kedua di tile yang sama ditolak", !m.place_bomb(p, bomb_type.normal));
        p.set_position(5, 4);
        check("bom di tile sebelah diterima", m.place_bomb(p, bomb_type.normal));
        p.set_position(12, 8);
        langkah(m, 12);
        check("rantai: dua bom meledak -> dua explosion", m.get_bombs().isEmpty() && m.get_explosions().size() == 2);
        check("active_bombs kembali 0 setelah rantai", p.get_active_bombs() == 0);

        // 7. dt aneh
        p = new player1(1, 1);
        m = baru(p);
        m.place_bomb(p, bomb_type.normal);
        m.update(-5);
        check("dt negatif dianggap 0", m.get_bombs().size() == 1 && m.get_bombs().get(0).get_fuse_time() == 3.0);
        m.update(100);
        check("dt sangat besar dibatasi 0.25", m.get_bombs().size() == 1 && m.get_bombs().get(0).get_fuse_time() == 2.75);

        // 8. update() tanpa parameter
        m.update();
        m.update();
        check("update() tanpa parameter tidak error", true);

        // 9. player mati tidak bisa pasang
        p = new player1(1, 1);
        m = baru(p);
        p.set_active(false);
        check("player nonaktif tidak bisa pasang bom", !m.place_bomb(p, bomb_type.normal));
        check("owner null ditolak", !m.place_bomb(null, bomb_type.normal));

        // 10. pemilihan jenis bomb
        p = new player1(1, 1);
        m = baru(p);
        check("default jenis = normal", m.get_selected_type() == bomb_type.normal);
        check("pilih pumped berhasil", m.select_type(p, bomb_type.pumped));
        check("jenis terpilih = pumped", m.get_selected_type() == bomb_type.pumped);
        check("pilih remote ditolak sebelum unlock", !m.select_type(p, bomb_type.remote) && m.get_selected_type() == bomb_type.pumped);
        m.place_selected(p);
        check("place_selected memasang pumped", m.get_bomb_at(1, 1).get_type() == bomb_type.pumped);
        p.unlock_remote();
        check("pilih remote berhasil setelah unlock", m.select_type(p, bomb_type.remote));
        check("pilih jenis tidak mengubah bom yang sudah dipasang", m.get_bomb_at(1, 1).get_type() == bomb_type.pumped);

        System.out.println("\nHASIL: " + pass + " lulus, " + fail + " gagal");
        System.exit(fail == 0 ? 0 : 1);
    }
}
