
package nangor_blast.game.system;

import nangor_blast.game.environment.game_map;
import nangor_blast.game.entities.player.player;
import nangor_blast.game.entities.player.player1;
import nangor_blast.game.system.event.game_event;
import nangor_blast.game.system.event.game_event.event_type;
import nangor_blast.game.system.event.game_event_manager;
import nangor_blast.interfaces.game_state.game_state_type;

public class game_session {

    private game_map current_map;
    private player player;
    private game_state_type game_state;

    private final game_event_manager event_manager;
    private bomb_manager bomb_manager;

    public game_session() {

        current_map = null;
        player = null;
        game_state = null;

        event_manager = new game_event_manager();
        bomb_manager = null;
    }

    public boolean initialize() {
        return initialize(false);
    }

    private boolean initialize(boolean restarted) {

        current_map = null;
        player = null;
        game_state = null;
        bomb_manager = null;

        current_map = new game_map();

        if (!validate_map()) {

            event_manager.notify_event(
                new game_event(
                    event_type.map_validation_failed,
                    current_map,
                    null,
                    -1,
                    -1,
                    0
                )
            );

            current_map = null;
            return false;
        }

        player = new player1(
            current_map.get_spawn_x(),
            current_map.get_spawn_y()
        );

        bomb_manager = new bomb_manager(
            current_map,
            event_manager
        );

        bomb_manager.add_target(player);

        game_state = game_state_type.playing;

        event_manager.notify_event(
            new game_event(
                event_type.map_initialized,
                current_map,
                null,
                -1,
                -1,
                0
            )
        );

        event_manager.notify_event(
            new game_event(
                restarted
                    ? event_type.game_restarted
                    : event_type.game_started,
                this,
                player,
                player.get_x(),
                player.get_y(),
                0
            )
        );

        return true;
    }

    public boolean validate_map() {

        if (current_map == null) {
            return false;
        }

        if (
            current_map.get_width() != 14 ||
            current_map.get_height() != 10
        ) {
            return false;
        }

        if (!current_map.is_inside(
            current_map.get_spawn_x(),
            current_map.get_spawn_y()
        )) {
            return false;
        }

        if (!current_map.is_walkable(
            current_map.get_spawn_x(),
            current_map.get_spawn_y()
        )) {
            return false;
        }

        if (!current_map.is_inside(
            current_map.get_exit_x(),
            current_map.get_exit_y()
        )) {
            return false;
        }

        if (!current_map.is_walkable(
            current_map.get_exit_x(),
            current_map.get_exit_y()
        )) {
            return false;
        }

        return true;
    }

    public boolean restart() {
        return initialize(true);
    }

    public game_map get_current_map() {
        return current_map;
    }

    public player get_player() {
        return player;
    }

    public game_state_type get_game_state() {
        return game_state;
    }

    public void set_game_state(game_state_type game_state) {
        this.game_state = game_state;
    }

    public bomb_manager get_bomb_manager() {
        return bomb_manager;
    }

    public game_event_manager get_event_manager() {
        return event_manager;
    }
}
