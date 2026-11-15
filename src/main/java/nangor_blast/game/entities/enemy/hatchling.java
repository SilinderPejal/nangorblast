package nangor_blast.game.entities.enemy;

import nangor_blast.game.entities.entity;
import nangor_blast.interfaces.blast_world;

public class hatchling extends normal_enemy {

    public hatchling(int x, int y, double speed) {
        super(x, y, speed);
    }

    @Override
    public void update(double delta_time, blast_world world, entity target) {

        if (!is_active() || target == null || !target.is_active()) {
            return;
        }

        step_toward(world, target.get_x(), target.get_y(), delta_time);
    }
}
