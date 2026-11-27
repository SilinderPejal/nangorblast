package nangor_blast.game.entities.power_up;

import nangor_blast.game.entities.player.player;

public class fire_up extends power_up {

    public fire_up(int x, int y) {
        super(x, y, power_up_type.fire_up);
    }

    @Override
    protected void apply_effect(player collector) {
        // Tambah jangkauan ledakan. Player membatasi jangkauannya sampai 5 tile.
        collector.increase_blast_range();
    }
}
