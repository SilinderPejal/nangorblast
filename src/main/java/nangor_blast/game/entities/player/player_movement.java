package nangor_blast.game.entities.player;

import nangor_blast.game.environment.game_map;
import nangor_blast.game.environment.ice_tile;
import nangor_blast.game.system.bomb_manager;
import nangor_blast.game.system.event.game_event;
import nangor_blast.game.system.event.game_event.event_type;
import nangor_blast.game.system.event.game_event_manager;

public class player_movement {

    private static final long base_move_interval_nanos = 200_000_000L;
    private final game_map map;
    private final ice_tile ice;

    private final bomb_manager bomb_manager;
    private final game_event_manager event_manager;
    private long next_move_nanos = System.nanoTime();

    public player_movement(game_map map) {
        this(map, null, null);
    }

    public player_movement(
        game_map map,
        bomb_manager bomb_manager,
        game_event_manager event_manager
    ) {
        this.map = map;
        this.bomb_manager = bomb_manager;
        this.event_manager = event_manager;

        this.ice = new ice_tile(map);
    }

    private boolean can_move_to(int x, int y) {

        if (!map.is_walkable(x, y)) {
            return false;
        }

        if (
            bomb_manager != null &&
            bomb_manager.get_bomb_at(x, y) != null
        ) {
            return false;
        }

        return true;
    }

    public boolean move_player(player player, int dx, int dy) {

        if (player == null || !player.is_alive() || !player.is_active()) {
            return false;
        }

        if (Math.abs(dx) + Math.abs(dy) != 1) {
            return false;
        }

        long now = System.nanoTime();

        if (now - next_move_nanos < 0) {
            return false;
        }

        int new_x = player.get_x() + dx;
        int new_y = player.get_y() + dy;

        if (!can_move_to(new_x, new_y)) {
            return false;
        }

        // Pindah ke tile yang sudah diperiksa, lalu ambil item jika ada.
        player.set_position(new_x, new_y);
        map.collect_power_up(player);

        while (ice.is_on_ice(player.get_x(), player.get_y())) {

            int next_x = player.get_x() + dx;
            int next_y = player.get_y() + dy;

            if (!can_move_to(next_x, next_y)) {
                break;
            }

            player.set_position(next_x, next_y);
            map.collect_power_up(player);
        }

        // Speed Up membuat gerakan berikutnya bisa dilakukan lebih cepat.
        next_move_nanos = now + (long) (base_move_interval_nanos / player.get_speed());

        if (event_manager != null) {
            event_manager.notify_event(
                new game_event(
                    event_type.player_moved,
                    player,
                    null,
                    player.get_x(),
                    player.get_y(),
                    0
                )
            );
        }

        return true;
    }
}
