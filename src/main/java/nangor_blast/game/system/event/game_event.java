package nangor_blast.game.system.event;

public class game_event {

    public enum event_type {

        // Game
        game_started,
        game_paused,
        game_resumed,
        game_over,
        game_won,
        game_restarted,

        // Map
        map_initialized,
        map_validation_failed,

        // Player
        player_moved,
        player_damaged,
        player_died,
        player_respawned,

        // Bomb
        bomb_placed,
        bomb_detonated,
        bomb_exploded,

        // Explosion
        explosion_created,
        explosion_ended,

        // Entity
        entity_damaged,
        entity_died,

        // Block
        block_destroyed,

        // Enemy
        enemy_spawned,
        enemy_damaged,
        enemy_died,

        // Boss
        boss_spawned,
        boss_damaged,
        boss_defeated,

        // Power-up
        power_up_spawned,
        power_up_collected,

        // Score
        score_changed,

        // Timer
        timer_started,
        timer_expired
    }

    private final event_type type;
    private final Object source;
    private final Object target;

    private final int x;
    private final int y;
    private final int value;

    public game_event(
        event_type type,
        Object source,
        Object target,
        int x,
        int y,
        int value
    ) {
        this.type = type;
        this.source = source;
        this.target = target;
        this.x = x;
        this.y = y;
        this.value = value;
    }

    public event_type get_type() {
        return type;
    }

    public Object get_source() {
        return source;
    }

    public Object get_target() {
        return target;
    }

    public int get_x() {
        return x;
    }

    public int get_y() {
        return y;
    }

    public int get_value() {
        return value;
    }
}