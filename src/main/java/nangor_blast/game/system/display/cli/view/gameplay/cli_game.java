
package nangor_blast.game.system.display.cli.view.gameplay;

import java.io.IOException;

import com.googlecode.lanterna.screen.Screen;

import nangor_blast.game.system.game_session;

public class cli_game {

    private final cli_map map_view;
    private final cli_player player_view;

    public cli_game() {
        map_view = new cli_map();
        player_view = new cli_player();
    }

    public void render(
        Screen screen,
        game_session session
    ) throws IOException {

        screen.clear();

        map_view.render(
            screen,
            session.get_current_map()
        );

        player_view.render(
            screen,
            session.get_player(),
            session.get_current_map()
        );

        screen.refresh();
    }
}
