package nangor_blast.game.system;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import nangor_blast.game.entities.bomb.bomb;
import nangor_blast.game.entities.bomb.bomb_type;
import nangor_blast.game.entities.bomb.explosion;
import nangor_blast.game.entities.bomb.explosion_resolver;
import nangor_blast.game.entities.bomb.normal_bomb;
import nangor_blast.game.entities.bomb.pumped_bomb;
import nangor_blast.game.entities.bomb.remote_bomb;
import nangor_blast.game.entities.entity;
import nangor_blast.game.entities.player.player;
import nangor_blast.interfaces.blast_world;

// Pengelola semua bomb dan explosion yang sedang aktif.
// Dipanggil dari playing_state: place_bomb() saat tombol bomb ditekan, update() setiap frame.
public class bomb_manager {

    // Batas dt supaya lonjakan waktu (misalnya setelah jeda) tidak meledakkan semua bom sekaligus.
    private static final double max_dt = 0.25;

    private final blast_world world;
    private final List<bomb> bombs = new ArrayList<>();
    private final List<explosion> explosions = new ArrayList<>();
    private final List<entity> targets = new ArrayList<>();
    private long last_nano = -1;
    private bomb_type selected_type = bomb_type.normal;

    public bomb_manager(blast_world world) {
        this.world = world;
    }

    // Daftarkan entity yang bisa terkena ledakan (player, nanti enemy dan boss).
    public void add_target(entity target) {
        if (target != null && !targets.contains(target)) {
            targets.add(target);
        }
    }

    // Jenis bomb untuk pemasangan berikutnya (default Normal). Remote hanya bisa dipilih setelah unlock.
    public boolean select_type(player owner, bomb_type type) {
        if (type == bomb_type.remote && (owner == null || !owner.has_remote_unlocked())) {
            return false;
        }
        selected_type = type;
        return true;
    }

    public bomb_type get_selected_type() {
        return selected_type;
    }

    // Pasang bomb dengan jenis yang sedang dipilih.
    public boolean place_selected(player owner) {
        return place_bomb(owner, selected_type);
    }

    // Pasang bomb di posisi player. Mengembalikan false kalau tidak boleh.
    public boolean place_bomb(player owner, bomb_type type) {
        if (owner == null || !owner.is_active() || !owner.can_place_bomb()) {
            return false;
        }
        if (type == bomb_type.remote && !owner.has_remote_unlocked()) {
            return false;
        }
        if (get_bomb_at(owner.get_x(), owner.get_y()) != null) {
            return false;
        }
        int x = owner.get_x();
        int y = owner.get_y();
        bomb created = switch (type) {
            case normal -> new normal_bomb(x, y, owner);
            case pumped -> new pumped_bomb(x, y, owner);
            case remote -> new remote_bomb(x, y, owner);
        };
        owner.place_bomb();
        bombs.add(created);
        return true;
    }

    // DETONATE: picu semua remote bomb milik player. Mengembalikan jumlah bom yang dipicu.
    public int detonate_remote(player owner) {
        if (owner == null || !owner.has_remote_unlocked()) {
            return 0;
        }
        int count = 0;
        for (bomb b : bombs) {
            if (b.get_owner() == owner && b.get_type() == bomb_type.remote
                    && !b.is_triggered() && !b.is_exploded()) {
                b.trigger();
                count++;
            }
        }
        return count;
    }

    // Versi yang menghitung dt sendiri dari jam sistem, cocok dengan loop Erin yang tanpa parameter.
    public void update() {
        long now = System.nanoTime();
        double dt = last_nano < 0 ? 0 : (now - last_nano) / 1_000_000_000.0;
        last_nano = now;
        update(dt);
    }

    // Versi dengan dt eksplisit (detik). Dipakai juga oleh tes.
    public void update(double dt) {
        if (dt < 0) {
            dt = 0;
        }
        if (dt > max_dt) {
            dt = max_dt;
        }

        for (bomb b : new ArrayList<>(bombs)) {
            b.update(dt);
        }

        // Explosion lama menua dulu; explosion baru mulai menghitung 0.5 detik dari tick berikutnya.
        for (explosion e : explosions) {
            e.update(dt);
        }
        explosions.removeIf(e -> !e.is_active());

        // Ledakkan bom yang sudah terpicu (fuse habis atau DETONATE), termasuk rantainya.
        for (bomb b : new ArrayList<>(bombs)) {
            if (b.is_triggered() && !b.is_exploded()) {
                for (explosion e : explosion_resolver.detonate(world, bombs, b)) {
                    explosions.add(e);
                    explosion_resolver.apply_damage(targets, e);
                }
            }
        }
        bombs.removeIf(bomb::is_exploded);
    }

    public bomb get_bomb_at(int x, int y) {
        for (bomb b : bombs) {
            if (b.get_x() == x && b.get_y() == y) {
                return b;
            }
        }
        return null;
    }

    // True kalau masih ada bomb atau explosion; dipakai layar untuk tahu perlu digambar ulang.
    public boolean has_activity() {
        return !bombs.isEmpty() || !explosions.isEmpty();
    }

    public List<bomb> get_bombs() {
        return Collections.unmodifiableList(bombs);
    }

    public List<explosion> get_explosions() {
        return Collections.unmodifiableList(explosions);
    }
}
