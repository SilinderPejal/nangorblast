
package nangor_blast.game.entities.bomb;

import nangor_blast.game.entities.player.player;

public class pumped_bomb extends bomb {

    private static final double fuse_seconds = 4.5;

    public pumped_bomb(int x, int y, player owner) {
        super(
            x,
            y,
            owner,
            owner.get_pumped_blast_range(),
            fuse_seconds
        );
    }

    @Override
    public bomb_type get_type() {
        return bomb_type.pumped;
    }

    @Override
    public int damage_to_boss() {
        return 2;
    }

    @Override
    public boolean can_break_reinforced() {
        return true;
    }
}
