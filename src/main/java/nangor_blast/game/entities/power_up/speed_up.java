package nangor_blast.game.entities.power_up;

import nangor_blast.game.entities.player.player;

public class speed_up extends power_up {

    public speed_up(int x, int y) {
        super(x, y);
    }

    @Override
    protected void apply_effect(player collector) {
        // Kecepatan awal 1.0, jadi setiap item menambah 0.5.
        collector.increase_speed(0.5);
    }
}
