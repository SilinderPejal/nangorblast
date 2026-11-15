package nangor_blast.game.entities.enemy;

import java.util.ArrayList;
import java.util.List;

import nangor_blast.game.entities.entity;
import nangor_blast.interfaces.blast_world;

// Satu pola serangan boss. Alurnya diatur oleh boss_enemy:
//   prepare()  -> dipanggil saat WARNING mulai; arah dan target DIKUNCI di sini
//   (warning 1 detik, tile pada affected_tiles ditampilkan sebagai '!')
//   on_active_start() -> serangan aktif selama get_active_duration()
public abstract class boss_attack {

    // Tile {x, y} yang terkena serangan. Diisi di prepare().
    protected final List<int[]> affected_tiles = new ArrayList<>();

    public abstract String get_name();

    public abstract void prepare(boss_enemy boss, entity target, blast_world world);

    // Durasi aktif dalam detik, bergantung fase boss (1 atau 2).
    public abstract double get_active_duration(int phase);

    // True jika boss menjadi rentan setelah serangan ini selesai.
    public boolean grants_vulnerability() {
        return false;
    }

    // Syarat pemakaian (jarak, kesejajaran, dsb). Default: selalu boleh.
    public boolean can_use(boss_enemy boss, entity target, blast_world world) {
        return true;
    }

    public void on_active_start(boss_enemy boss, entity target, blast_world world) {
    }

    public List<int[]> get_affected_tiles() {
        return affected_tiles;
    }

    public boolean covers(int x, int y) {

        for (int[] tile : affected_tiles) {
            if (tile[0] == x && tile[1] == y) {
                return true;
            }
        }

        return false;
    }

    // Tambah tile jika di dalam map dan tidak tertutup block (serangan boss
    // tidak merusak block dan berhenti di block, F05.3.1 aturan 8).
    protected void add_tile(blast_world world, int x, int y) {

        if (world.is_out_of_bounds(x, y) || world.get_block_at(x, y).isPresent()) {
            return;
        }

        affected_tiles.add(new int[] { x, y });
    }
}
