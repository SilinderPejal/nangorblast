package nangor_blast.game.entities.block;

import nangor_blast.game.entities.bomb.bomb;
import nangor_blast.interfaces.damageable;

public class destructible_block extends block implements damageable {

    public destructible_block(int x, int y) {
        super(x, y);
    }

    @Override
    public boolean is_destroyed_by(bomb source) {
        return is_active();
    }

    @Override
    public void destroy() {
        // Map nantinya mengabaikan block yang sudah tidak aktif.
        set_active(false);
    }

    @Override
    public boolean can_be_damaged() {
        return is_active();
    }

    @Override
    public void take_damage(int damage) {
        if (can_be_damaged() && damage > 0) {
            destroy();
        }
    }
}
