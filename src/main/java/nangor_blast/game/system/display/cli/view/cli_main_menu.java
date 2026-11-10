
package nangor_blast.game.system.display.cli.view;

import java.io.IOException;

import com.googlecode.lanterna.SGR;
import com.googlecode.lanterna.TextColor;
import com.googlecode.lanterna.graphics.TextGraphics;
import com.googlecode.lanterna.screen.Screen;

import nangor_blast.game.system.menu.menu_state;
import nangor_blast.game.system.menu.main_menu;

public class cli_main_menu {

    public void render(
        Screen screen,
        menu_state menu_state
    ) throws IOException {

        screen.clear();

        TextGraphics graphics = screen.newTextGraphics();

        int width = screen.getTerminalSize().getColumns();
        int height = screen.getTerminalSize().getRows();

        int center_x = width / 2;
        int menu_start_y = height / 2 - 2;

        // Title
        String title = "NANGOR BLAST";

        graphics.setForegroundColor(TextColor.ANSI.YELLOW);
        graphics.setBackgroundColor(TextColor.ANSI.DEFAULT);

        if (menu_start_y - 3 >= 0) {

            graphics.putString(
                center_x - title.length() / 2,
                menu_start_y - 3,
                title,
                SGR.BOLD
            );
        }

        // Menu options
        main_menu[] options = main_menu.values();
        int selected_index = menu_state.get_selected_index();

        for (int i = 0; i < options.length; i++) {

            String label = options[i].get_label();

            int option_y = menu_start_y + i;

            String display_label = (i == selected_index)
                ? "> " + label + " <"
                : label;

            int label_x = center_x - display_label.length() / 2;

            if (
                option_y < 0 ||
                option_y >= height ||
                label_x < 0 ||
                label_x + display_label.length() > width
            ) {
                continue;
            }

            if (i == selected_index) {

                graphics.setForegroundColor(TextColor.ANSI.YELLOW);

                graphics.putString(
                    label_x,
                    option_y,
                    display_label,
                    SGR.BOLD
                );

            } else {

                graphics.setForegroundColor(TextColor.ANSI.WHITE);

                graphics.putString(
                    label_x,
                    option_y,
                    display_label
                );
            }
        }

        // Controls
        String controls = "Use UP/DOWN to navigate | ENTER to select";

        int controls_x = center_x - controls.length() / 2;
        int controls_y = height - 2;

        if (
            controls_y >= 0 &&
            controls_x >= 0 &&
            controls_x + controls.length() <= width
        ) {

            graphics.setForegroundColor(TextColor.ANSI.CYAN);
            graphics.setBackgroundColor(TextColor.ANSI.DEFAULT);

            graphics.putString(
                controls_x,
                controls_y,
                controls
            );
        }

        screen.refresh();
    }
}
