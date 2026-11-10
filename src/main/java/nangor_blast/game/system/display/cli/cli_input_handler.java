
package nangor_blast.game.system.display.cli;

import com.googlecode.lanterna.input.KeyStroke;
import com.googlecode.lanterna.input.KeyType;

import nangor_blast.game.system.menu.menu_state;
import nangor_blast.game.system.menu.main_menu;

public class cli_input_handler {

    private boolean invalid_input;

    public main_menu handle_input(
        KeyStroke key,
        menu_state menu_state
    ) {

        invalid_input = false;

        if (key == null) {
            return null;
        }

        KeyType key_type = key.getKeyType();

        switch (key_type) {

            case ArrowUp:
                menu_state.move_selection(-1);
                break;

            case ArrowDown:
                menu_state.move_selection(1);
                break;

            case Enter:
                return menu_state.select();

            default:
                invalid_input = true;
                break;
        }

        return null;
    }

    public boolean has_invalid_input() {
        return invalid_input;
    }
}
