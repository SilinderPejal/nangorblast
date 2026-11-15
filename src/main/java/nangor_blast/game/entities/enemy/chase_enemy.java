package nangor_blast.game.entities.enemy;

import java.util.List;

import nangor_blast.game.entities.entity;
import nangor_blast.interfaces.blast_world;

public class chase_enemy extends patrol_enemy {

    private static final int detection_range = 3;

    private enemy_state state;

    public chase_enemy(int x, int y, double speed, List<int[]> route) {
        super(x, y, speed, route);

        this.state = enemy_state.patrol;
    }

    public enemy_state get_state() {
        return state;
    }

    @Override
    public void update(double delta_time, blast_world world, entity target) {

        if (!is_active()) {
            return;
        }

        boolean detected = target != null && target.is_active()
            && Math.abs(target.get_x() - x) + Math.abs(target.get_y() - y) <= detection_range;

        state = detected ? enemy_state.chase : enemy_state.patrol;

        if (state == enemy_state.chase) {
            step_toward(world, target.get_x(), target.get_y(), delta_time);
        } else {
            patrol(delta_time, world);
        }
    }
}
