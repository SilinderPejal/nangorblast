package nangor_blast.game.entities.bomb;

import nangor_blast.game.entities.player.player;

// Tidak punya fuse: hanya meledak lewat trigger() dari aksi DETONATE.
// Pengecekan Remote Detonator (player.has_remote_unlocked()) dilakukan oleh yang menaruh bom.
public class remote_bomb extends bomb {

    public remote_bomb(int x, int y, player owner) {
        super(x, y, owner, owner.get_blast_range(), Double.POSITIVE_INFINITY);
    }

    @Override
    public bomb_type get_type() {
        return bomb_type.remote;
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
