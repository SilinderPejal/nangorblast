
package nangor_blast.game.system.display.cli.view.gameplay;

import com.googlecode.lanterna.TextColor;
import com.googlecode.lanterna.graphics.TextGraphics;
import com.googlecode.lanterna.screen.Screen;

import nangor_blast.game.environment.game_map;
import nangor_blast.game.entities.player.player1;

public class cli_player {

    public void render(
        Screen screen,
        player1 player,
        game_map map
    ) {

        if (player == null || !player.is_active()) {
            return;
        }

        cli_map map_view = new cli_map();

        int start_x = map_view.get_start_x(screen, map);
        int start_y = map_view.get_start_y(screen, map);

        TextGraphics graphics = screen.newTextGraphics();

        graphics.setForegroundColor(TextColor.ANSI.YELLOW);

        graphics.putString(
            start_x + player.get_x() * 2,
            start_y + player.get_y(),
            "P "
        );
    }
}
