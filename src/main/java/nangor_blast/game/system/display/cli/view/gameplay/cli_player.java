
package nangor_blast.game.system.display.cli.view.gameplay;

import com.googlecode.lanterna.TextColor;
import com.googlecode.lanterna.graphics.TextGraphics;
import com.googlecode.lanterna.screen.Screen;

import nangor_blast.game.environment.game_map;
import nangor_blast.game.entities.player.player;

public class cli_player {

    private static final long blink_interval = 150;

    public void render(
        Screen screen,
        player player,
        game_map map
    ) {

        if (player == null || !player.is_active()) {
            return;
        }

        // Efek blinking selama invulnerability.
        if (
            player.is_invulnerable() &&
            (System.currentTimeMillis() / blink_interval) % 2 == 0
        ) {
            return;
        }

        cli_map map_view = new cli_map();

        int start_x = map_view.get_start_x(screen, map);
        int start_y = map_view.get_start_y(screen, map);

        TextGraphics graphics = screen.newTextGraphics();

        graphics.setForegroundColor(TextColor.ANSI.BLUE);

        graphics.putString(
            start_x + player.get_x() * 2,
            start_y + player.get_y(),
            "P"
        );
    }
}
