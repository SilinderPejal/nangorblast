
package nangor_blast.game.system.display.cli.view.gameplay;

import java.io.IOException;

import com.googlecode.lanterna.TextColor;
import com.googlecode.lanterna.graphics.TextGraphics;
import com.googlecode.lanterna.screen.Screen;

import nangor_blast.game.system.game_session;
import nangor_blast.game.entities.bomb.bomb;
import nangor_blast.game.entities.bomb.explosion;
import nangor_blast.game.entities.bomb.blast_tile;

public class cli_game {

    private final cli_map map_view;
    private final cli_player player_view;
    private final cli_life_ui life_view;

    public cli_game() {
        map_view = new cli_map();
        player_view = new cli_player();
        life_view = new cli_life_ui();
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

        int start_x = map_view.get_start_x(
            screen,
            session.get_current_map()
        );

        int start_y = map_view.get_start_y(
            screen,
            session.get_current_map()
        );

        TextGraphics graphics = screen.newTextGraphics();

        // Render bomb.
        for (bomb b : session.get_bomb_manager().get_bombs()) {

            char symbol = switch (b.get_type()) {
                case normal -> 'o';
                case pumped -> 'O';
                case remote -> '8';
            };

            graphics.setForegroundColor(TextColor.ANSI.RED);

            graphics.putString(
                start_x + b.get_x() * 2,
                start_y + b.get_y(),
                symbol + " "
            );
        }

        // Render explosion.
        for (explosion e : session.get_bomb_manager().get_explosions()) {

            graphics.setForegroundColor(TextColor.ANSI.YELLOW);

            for (blast_tile tile : e.get_area()) {

                graphics.putString(
                    start_x + tile.x() * 2,
                    start_y + tile.y(),
                    "* "
                );
            }
        }

        // Render player life UI.
        life_view.render(
            screen,
            session.get_player(),
            session.get_current_map()
        );

        // Render player terakhir.
        player_view.render(
            screen,
            session.get_player(),
            session.get_current_map()
        );

        screen.refresh();
    }
}
