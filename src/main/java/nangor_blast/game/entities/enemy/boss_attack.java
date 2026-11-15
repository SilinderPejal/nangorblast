package nangor_blast.game.entities.enemy;

import java.util.ArrayList;
import java.util.List;

import nangor_blast.game.entities.entity;
import nangor_blast.interfaces.blast_world;

public abstract class boss_attack {

    protected final List<int[]> affected_tiles = new ArrayList<>();

    public abstract String get_name();

    public abstract void prepare(boss_enemy boss, entity target, blast_world world);

    // Durasi aktif dalam detik, tergantung fase boss (1 atau 2).
    public abstract double get_active_duration(int phase);

    // True jika boss jadi rentan setelah serangan ini selesai.
    public boolean grants_vulnerability() {
        return false;
    }

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

    protected void add_tile(blast_world world, int x, int y) {

        if (world.is_out_of_bounds(x, y) || world.get_block_at(x, y).isPresent()) {
            return;
        }

        affected_tiles.add(new int[] { x, y });
    }
}
