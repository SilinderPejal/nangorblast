
package nangor_blast.game.entities.player;

import nangor_blast.game.entities.entity;
import nangor_blast.interfaces.playable;
import nangor_blast.interfaces.moveable;
import nangor_blast.interfaces.damageable;

public abstract class player extends entity implements playable, moveable, damageable {

    private static final int max_lives = 5;
    private static final int max_bomb_capacity = 3;
    private static final int max_blast_range = 5;
    private static final long invulnerability_duration = 1500;


    protected final int player_id;
    protected int lives;
    protected int bomb_capacity;
    protected int active_bombs;
    protected int blast_range;
    protected double speed;
    protected boolean invulnerable;
    protected boolean remote_unlocked;
    protected long invulnerability_end_time;

    protected player(int player_id, int x, int y) {
        super(x, y);

        this.player_id = player_id;

        this.lives = 3;
        this.bomb_capacity = 1;
        this.active_bombs = 0;
        this.blast_range = 1;

        this.speed = 1.0;

        this.invulnerable = false;
        this.remote_unlocked = false;

        this.invulnerability_end_time = 0;
    }

    @Override
    public int get_player_id() {
        return player_id;
    }

    @Override
    public int get_lives() {
        return lives;
    }

    @Override
    public void lose_life() {
        if (!can_be_damaged()) {
            return;
        }

        lives--;

        if (lives > 0) {
            respawn();
        } else {
            set_active(false);
        }
    }

    @Override
    public boolean is_alive() {
        return lives > 0;
    }

    public void increase_lives() {
    if (lives < max_lives) {
        lives++;
    }
}

    protected void respawn() {
        invulnerable = true;

        invulnerability_end_time =
            System.currentTimeMillis() + invulnerability_duration;
    }

    public void update_invulnerability() {
        if (invulnerable &&
            System.currentTimeMillis() >= invulnerability_end_time) {

            invulnerable = false;
        }
    }

    public boolean is_invulnerable() {
        return invulnerable;
    }

    public int get_bomb_capacity() {
        return bomb_capacity;
    }

    public int get_active_bombs() {
        return active_bombs;
    }

    public boolean can_place_bomb() {
        return is_alive() && active_bombs < bomb_capacity;
    }

    public void place_bomb() {
        if (can_place_bomb()) {
            active_bombs++;
        }
    }

    public void bomb_exploded() {
        if (active_bombs > 0) {
            active_bombs--;
        }
    }

    public void increase_bomb_capacity() {
        if (bomb_capacity < max_bomb_capacity) {
            bomb_capacity++;
        }
    }

    public int get_blast_range() {
        return blast_range;
    }

    public void increase_blast_range() {
        if (blast_range < max_blast_range) {
            blast_range++;
        }
    }

    @Override
    public double get_speed() {
        return speed;
    }

    public void increase_speed(double amount) {
        if (amount > 0) {
            speed += amount;
        }
    }

    public boolean has_remote_unlocked() {
        return remote_unlocked;
    }

    public void unlock_remote() {
        remote_unlocked = true;
    }


    @Override
    public void move(int dx, int dy, double delta_time) {
        if (!is_alive() || (dx == 0 && dy == 0)) {
            return;
        }

        double distance = speed * delta_time;

        int new_x = x + (int) Math.round(dx * distance);
        int new_y = y + (int) Math.round(dy * distance);

        set_position(new_x, new_y);
    }

    @Override
    public boolean can_be_damaged() {
        return is_alive() && !invulnerable;
    }

    @Override
    public void take_damage(int damage) {
        if (!can_be_damaged() || damage <= 0) {
            return;
        }

        lives -= damage;

        if (lives <= 0) {
            lives = 0;
            set_active(false);
        } else {
            respawn();
        }
    }
}
