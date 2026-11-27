package nangor_blast.game.entities.power_up;

import nangor_blast.game.entities.player.player;

public class remote_detonator extends power_up {

    public remote_detonator(int x, int y) {
        super(x, y, power_up_type.remote_detonator);
    }

    @Override
    protected void apply_effect(player collector) {
        // Buka akses ke Remote Bomb untuk sesi permainan ini.
        collector.unlock_remote();
    }
}
