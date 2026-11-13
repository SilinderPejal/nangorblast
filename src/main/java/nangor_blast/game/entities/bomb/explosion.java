package nangor_blast.game.entities.bomb;

import java.util.Collections;
import java.util.Set;

import nangor_blast.game.entities.entity;

// Satu explosion per bom. Area dihitung sekali oleh explosion_resolver, lalu hanya menunggu 0.5 detik.
public class explosion extends entity {

    private static final double duration_seconds = 0.5;

    private final bomb source;
    private final Set<blast_tile> area;
    private double remaining = duration_seconds;

    explosion(bomb source, Set<blast_tile> area) {
        super(source.get_x(), source.get_y());
        this.source = source;
        this.area = Collections.unmodifiableSet(area);
    }

    // entity belum punya update(dt), jadi ini method milik explosion sendiri.
    public void update(double dt) {
        if (!is_active()) {
            return;
        }
        remaining -= dt;
        if (remaining <= 0) {
            set_active(false);
        }
    }

    public bomb get_source() {
        return source;
    }

    public Set<blast_tile> get_area() {
        return area;
    }

    public boolean covers(int x, int y) {
        return area.contains(new blast_tile(x, y));
    }
}
