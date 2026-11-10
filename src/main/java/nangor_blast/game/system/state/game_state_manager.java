package nangor_blast.game.system.state;

import nangor_blast.interfaces.game_state;

public class game_state_manager {

    private game_state current_state;

    public void change_state(game_state new_state) {
        if (current_state != null) {
            current_state.exit();
        }

        current_state = new_state;
        current_state.enter();
    }

    public void update() {
        if (current_state != null) {
            current_state.update();
        }
    }

    public game_state get_current_state() {
        return current_state;
    }

    public game_state.game_state_type get_current_state_type() {
        if (current_state == null) {
            return null;
        }

        return current_state.get_type();
    }
}