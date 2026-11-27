package nangor_blast.game.entities.power_up;

import nangor_blast.game.entities.entity;
import nangor_blast.game.entities.player.player;
import nangor_blast.interfaces.collectible;

public abstract class power_up extends entity implements collectible {
    private final power_up_type type;

    protected power_up(int x, int y, power_up_type type) {
        super(x, y);
        this.type = type;
    }

    public power_up_type get_type() {
        return type;
    }

    @Override
    public final boolean collect(player collector) {
        // Item hanya bisa diambil oleh pemain aktif dan hidup di tile yang sama.
        if (!is_active() || collector == null || !collector.is_active()
                || !collector.is_alive()
                || collector.get_x() != get_x() || collector.get_y() != get_y()) {
            return false;
        }

        apply_effect(collector);

        // Nonaktifkan item agar efeknya hanya diterapkan sekali.
        set_active(false);
        return true;
    }

    // Setiap jenis power-up menentukan efeknya sendiri.
    protected abstract void apply_effect(player collector);
}
