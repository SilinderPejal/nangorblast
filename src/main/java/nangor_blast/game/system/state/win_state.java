
package nangor_blast.game.system.state;

import nangor_blast.interfaces.game_state;

public class win_state implements game_state {

    @Override
    public void enter() {}

    @Override
    public void update() {}

    @Override
    public void exit() {}

    @Override
    public game_state_type get_type() {
        return game_state_type.win;
    }
}
