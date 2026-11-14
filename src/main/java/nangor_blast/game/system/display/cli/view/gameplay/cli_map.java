
package nangor_blast.game.system.display.cli.view.gameplay;

import com.googlecode.lanterna.screen.Screen;
import com.googlecode.lanterna.TextColor;
import com.googlecode.lanterna.graphics.TextGraphics;

import nangor_blast.game.entities.block.destructible_block;
import nangor_blast.game.entities.block.indestructible_block;
import nangor_blast.game.environment.game_map;

public class cli_map {

    public void render(Screen screen, game_map map) {

        TextGraphics graphics = screen.newTextGraphics();

        int map_width = map.get_width() * 2;
        int map_height = map.get_height();

        int terminal_width = screen.getTerminalSize().getColumns();
        int terminal_height = screen.getTerminalSize().getRows();

        // Menghitung posisi tengah map
        int start_x = (terminal_width - map_width) / 2;
        int start_y = (terminal_height - map_height) / 2;

        for (int y = 0; y < map.get_height(); y++) {

            for (int x = 0; x < map.get_width(); x++) {

                var tile_block = map.get_block_at(x, y).orElse(null);
                String tile = ". ";

                graphics.setForegroundColor(TextColor.ANSI.WHITE);

                if (map.is_exit(x, y)) {
                    graphics.setForegroundColor(TextColor.ANSI.GREEN);
                    tile = "X ";
                } else if (tile_block instanceof indestructible_block) {
                    tile = "# ";
                } else if (tile_block instanceof destructible_block) {
                    tile = "D ";
                }

                graphics.putString(start_x + x * 2, start_y + y, tile);
                
            }
        }
    }

    public int get_start_x(Screen screen, game_map map) {
        return (screen.getTerminalSize().getColumns() - map.get_width() * 2) / 2;
    }

    public int get_start_y(Screen screen, game_map map) {
        return (screen.getTerminalSize().getRows() - map.get_height()) / 2;
    }
}
