package nangor_blast.game.entities.power_up;

import nangor_blast.game.entities.player.player;

public class heart extends power_up {

    public heart(int x, int y) {
        super(x, y, power_up_type.heart);
    }

    @Override
    protected void apply_effect(player collector) {
        // Tambah satu nyawa. Player membatasi jumlah nyawa sampai 5.
        collector.increase_lives();
    }
}
