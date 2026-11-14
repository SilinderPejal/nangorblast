
package nangor_blast.game.system.display.cli.view.gameplay;

import com.googlecode.lanterna.TextColor;
import com.googlecode.lanterna.graphics.TextGraphics;
import com.googlecode.lanterna.screen.Screen;

import nangor_blast.game.entities.player.player;
import nangor_blast.game.environment.game_map;

public class cli_life_ui {

    public void render(
        Screen screen,
        player player,
        game_map map
    ) {

        if (player == null || map == null) {
            return;
        }

        int start_x = (screen.getTerminalSize().getColumns()
            - map.get_width() * 2) / 2;

        int start_y = (screen.getTerminalSize().getRows()
            - map.get_height()) / 2;

        int lives = Math.max(0, player.get_lives());
        int max_lives = player.get_max_lives();

        TextGraphics graphics = screen.newTextGraphics();

        int ui_x = start_x;
        int ui_y = start_y - 1;

        for (int i = 0; i < max_lives; i++) {

            if (i < lives) {
                graphics.setForegroundColor(TextColor.ANSI.RED);

                graphics.putString(
                    ui_x + (i * 2),
                    ui_y,
                    "\u2665"
                );

            } else {
                graphics.setForegroundColor(TextColor.ANSI.WHITE);

                graphics.putString(
                    ui_x + (i * 2),
                    ui_y,
                    "\u2661"
                );
            }
        }
    }
}
