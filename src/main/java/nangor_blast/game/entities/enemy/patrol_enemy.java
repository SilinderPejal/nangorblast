package nangor_blast.game.entities.enemy;

import java.util.ArrayList;
import java.util.List;

import nangor_blast.game.entities.entity;
import nangor_blast.interfaces.blast_world;

// Patrol Enemy: berjalan terus mengikuti rute waypoint {x, y} secara melingkar.
public class patrol_enemy extends normal_enemy {

    private final List<int[]> route;
    private int route_index;

    public patrol_enemy(int x, int y, double speed, List<int[]> route) {
        super(x, y, speed);

        this.route = new ArrayList<>(route);
        this.route_index = 0;
    }

    @Override
    public void update(double delta_time, blast_world world, entity target) {
        patrol(delta_time, world);
    }

    // Dipakai bersama oleh chase_enemy untuk kondisi PATROL.
    protected void patrol(double delta_time, blast_world world) {

        if (!is_active() || route.isEmpty()) {
            return;
        }

        int[] waypoint = route.get(route_index);

        if (x == waypoint[0] && y == waypoint[1]) {
            route_index = (route_index + 1) % route.size();
            return;
        }

        if (!step_toward(world, waypoint[0], waypoint[1], delta_time)) {
            // Terhalang: lanjut ke waypoint berikutnya agar tidak macet.
            route_index = (route_index + 1) % route.size();
        }
    }
}
