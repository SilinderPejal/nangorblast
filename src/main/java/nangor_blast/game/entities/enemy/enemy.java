package nangor_blast.game.entities.enemy;

import nangor_blast.game.entities.entity;
import nangor_blast.interfaces.blast_world;
import nangor_blast.interfaces.damageable;
import nangor_blast.interfaces.moveable;

public abstract class enemy extends entity implements moveable, damageable {

    protected int health;
    protected final int max_health;
    protected final double speed;
    protected final int score_value;

    private double step_progress;

    protected enemy(int x, int y, int health, double speed, int score_value) {
        super(x, y);

        this.health = health;
        this.max_health = health;
        this.speed = speed;
        this.score_value = score_value;
        this.step_progress = 0;
    }

    public abstract void update(double delta_time, blast_world world, entity target);

    public int get_health() {
        return health;
    }

    public int get_max_health() {
        return max_health;
    }

    public int get_score_value() {
        return score_value;
    }

    public boolean is_alive() {
        return health > 0 && is_active();
    }

    @Override
    public double get_speed() {
        return speed;
    }

    @Override
    public void move(int dx, int dy, double delta_time) {

        if (!is_active() || (dx == 0 && dy == 0)) {
            return;
        }

        step_progress += speed * delta_time;

        if (step_progress >= 1.0) {
            step_progress -= 1.0;
            set_position(x + dx, y + dy);
        }
    }

    protected boolean can_step(blast_world world, int dx, int dy) {
        int nx = x + dx;
        int ny = y + dy;

        return !world.is_out_of_bounds(nx, ny) && world.is_walkable(nx, ny);
    }

    protected boolean step_toward(blast_world world, int tx, int ty, double delta_time) {

        int dx = Integer.signum(tx - x);
        int dy = Integer.signum(ty - y);

        if (dx == 0 && dy == 0) {
            return false;
        }

        boolean x_first = Math.abs(tx - x) >= Math.abs(ty - y);

        int[][] options = x_first
            ? new int[][] { { dx, 0 }, { 0, dy } }
            : new int[][] { { 0, dy }, { dx, 0 } };

        for (int[] option : options) {

            if ((option[0] != 0 || option[1] != 0) && can_step(world, option[0], option[1])) {
                move(option[0], option[1], delta_time);
                return true;
            }
        }

        return false;
    }

    @Override
    public boolean can_be_damaged() {
        return is_active() && health > 0;
    }

    @Override
    public void take_damage(int damage) {

        if (!can_be_damaged() || damage <= 0) {
            return;
        }

        health -= damage;

        if (health <= 0) {
            health = 0;
            set_active(false);
        }
    }
}
