
package nangor_blast.game.system;

import nangor_blast.game.environment.game_map;
import nangor_blast.game.entities.player.player1;
import nangor_blast.interfaces.game_state.game_state_type;

public class game_session {

    private game_map current_map;
    private player1 player;
    private game_state_type game_state;
    private final score_tracker score = new score_tracker();

    public game_session() {
        current_map = null;
        player = null;
        game_state = null;
    }

    // Inisialisasi permainan
    public boolean initialize() {
        score.reset();

        // Reset kondisi permainan
        current_map = null;
        player = null;
        game_state = null;

        current_map = new game_map();

        // Validasi map
        if (!validate_map()) {
            current_map = null;
            return false;
        }

        // Membuat player pada titik spawn
        player = new player1(
            current_map.get_spawn_x(),
            current_map.get_spawn_y()
        );

        game_state = game_state_type.playing;

        return true;
    }

    // Validasi dasar map
    public boolean validate_map() {

        if (current_map == null) {
            return false;
        }

        // Validasi ukuran map
        if (
            current_map.get_width() != 14 ||
            current_map.get_height() != 10
        ) {
            return false;
        }

        // Validasi spawn player
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

        // Validasi exit
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

    // Restart permainan
    public boolean restart() {
        return initialize();
    }

    public score_tracker get_score_tracker() {
        return score;
    }
    
    public game_map get_current_map() {
        return current_map;
    }

    public player1 get_player() {
        return player;
    }

    public game_state_type get_game_state() {
        return game_state;
    }

    public void set_game_state(game_state_type game_state) {
        this.game_state = game_state;
    }
}
