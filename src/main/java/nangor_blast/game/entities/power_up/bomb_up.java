package nangor_blast.game.entities.power_up;

import nangor_blast.game.entities.player.player;

public class bomb_up extends power_up {

    public bomb_up(int x, int y) {
        super(x, y, power_up_type.bomb_up);
    }

    @Override
    protected void apply_effect(player collector) {
        // Method di player menambah kapasitas bom dengan batas maksimum 3.
        collector.increase_bomb_capacity();
    }
}
