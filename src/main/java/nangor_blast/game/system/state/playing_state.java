
package nangor_blast.game.system.state;

import nangor_blast.interfaces.game_state;
import nangor_blast.game.system.game_session;
import nangor_blast.game.environment.game_map;
import nangor_blast.game.entities.player.player1;
import nangor_blast.game.entities.player.player_movement;

public class playing_state implements game_state {

    private final game_state_manager state_manager;
    private final game_session session;

    private player_movement movement;

    public playing_state(
        game_state_manager state_manager,
        game_session session
    ) {
        this.state_manager = state_manager;
        this.session = session;
    }

    @Override
    public void enter() {
        movement = new player_movement(session.get_current_map());
    }

    @Override
    public void update() {
        player1 player_1 = session.get_player();

        if (session.get_current_map().is_exit(
            player_1.get_x(),
            player_1.get_y()
        )) {
            state_manager.change_state(new win_state());
        }
    }

    @Override
    public void exit() {
    }

    @Override
    public game_state_type get_type() {
        return game_state_type.playing;
    }

    public player1 get_player_1() {
        return session.get_player();
    }

    public player_movement get_movement() {
        return movement;
    }

    public game_session get_session() {
        return session;
    }

    public game_map get_map() {
        return session.get_current_map();
    }
}
