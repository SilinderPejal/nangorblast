package nangor_blast.game.entities.bomb;

import nangor_blast.game.entities.player.player;

public class normal_bomb extends bomb {

    private static final double fuse_seconds = 3.0;

    public normal_bomb(int x, int y, player owner) {
        super(x, y, owner, owner.get_blast_range(), fuse_seconds);
    }

    @Override
    public bomb_type get_type() {
        return bomb_type.normal;
    }

    @Override
    public int damage_to_boss() {
        return 1;
    }

    @Override
    public boolean can_break_reinforced() {
        return false;
    }
}
