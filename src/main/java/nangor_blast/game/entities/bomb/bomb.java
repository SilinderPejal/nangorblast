
package nangor_blast.game.entities.bomb;

import nangor_blast.game.entities.entity;
import nangor_blast.game.entities.player.player;
import nangor_blast.interfaces.explodable;

public abstract class bomb extends entity implements explodable {

    private final player owner;
    private final int blast_range;
    private double fuse_time;
    private boolean triggered;
    private boolean exploded;

    protected bomb(
        int x,
        int y,
        player owner,
        int blast_range,
        double fuse_time
    ) {
        super(x, y);

        this.owner = owner;
        this.blast_range = blast_range;
        this.fuse_time = fuse_time;

        this.triggered = false;
        this.exploded = false;
    }

    public abstract bomb_type get_type();

    public abstract int damage_to_boss();

    public abstract boolean can_break_reinforced();

    public void update(double dt) {

        if (!is_active() || triggered) {
            return;
        }

        if (Double.isInfinite(fuse_time)) {
            return;
        }

        fuse_time -= dt;

        if (fuse_time <= 0) {
            trigger();
        }
    }

    @Override
    public void trigger() {
        triggered = true;
    }

    @Override
    public boolean is_triggered() {
        return triggered;
    }

    public boolean is_exploded() {
        return exploded;
    }

    void mark_exploded() {

        exploded = true;
        set_active(false);
    }

    public player get_owner() {
        return owner;
    }

    public int get_blast_range() {
        return blast_range;
    }

    public double get_fuse_time() {
        return fuse_time;
    }
}
