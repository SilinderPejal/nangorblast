
package nangor_blast.game.system.display.cli;

import com.googlecode.lanterna.input.KeyStroke;
import com.googlecode.lanterna.input.KeyType;

import nangor_blast.game.system.menu.menu_state;
import nangor_blast.game.system.menu.main_menu;
import nangor_blast.game.system.game_session;
import nangor_blast.game.entities.bomb.bomb_type;
import nangor_blast.game.entities.player.player_movement;
import nangor_blast.interfaces.game_state.game_state_type;

public class cli_input_handler {

    private boolean invalid_input;

    public main_menu handle_input(
        KeyStroke key,
        game_state_type state,
        menu_state menu_state,
        game_session session,
        player_movement movement
    ) {

        invalid_input = false;

        if (key == null) {
            return null;
        }

        KeyType key_type = key.getKeyType();

        switch (state) {

            case menu:
                return handle_menu_input(key_type, menu_state);

            case playing:
                handle_game_input(key, session, movement);
                break;

            case win:
                break;

            default:
                invalid_input = true;
                break;
        }

        return null;
    }

    private main_menu handle_menu_input(
        KeyType key_type,
        menu_state menu_state
    ) {

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

    private void handle_game_input(
        KeyStroke key,
        game_session session,
        player_movement movement
    ) {

        if (session == null || movement == null) {
            invalid_input = true;
            return;
        }

        KeyType key_type = key.getKeyType();
        Character character = key.getCharacter();

        if (key_type != KeyType.Character || character == null) {
            invalid_input = true;
            return;
        }

        char input = Character.toLowerCase(character);

        switch (input) {

            // SPACE atau B untuk memasang bomb.
            case ' ':
            case 'b':
                session.get_bomb_manager().place_selected(
                    session.get_player()
                );
                return;

            // Memilih jenis bomb.
            case '1':
                session.get_bomb_manager().select_type(
                    session.get_player(),
                    bomb_type.normal
                );
                return;

            case '2':
                session.get_bomb_manager().select_type(
                    session.get_player(),
                    bomb_type.pumped
                );
                return;

            case '3':
                session.get_bomb_manager().select_type(
                    session.get_player(),
                    bomb_type.remote
                );
                return;

            // Detonasi seluruh remote bomb milik player.
            case 'q':
                session.get_bomb_manager().detonate_remote(
                    session.get_player()
                );
                return;
        }

        int dx = 0;
        int dy = 0;

        switch (input) {

            case 'w':
                dy = -1;
                break;

            case 's':
                dy = 1;
                break;

            case 'a':
                dx = -1;
                break;

            case 'd':
                dx = 1;
                break;

            default:
                invalid_input = true;
                return;
        }

        movement.move_player(
            session.get_player(),
            dx,
            dy
        );
    }

    public boolean has_invalid_input() {
        return invalid_input;
    }
}
