package nangor_blast.game.entities.bomb;

import nangor_blast.game.entities.player.player;

public class pumped_bomb extends bomb {

    private static final double fuse_seconds = 4.5;
    private static final int extra_range = 2;
    private static final int max_range = 7;

    public pumped_bomb(int x, int y, player owner) {
        super(x, y, owner, Math.min(max_range, owner.get_blast_range() + extra_range), fuse_seconds);
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
