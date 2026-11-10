package nangor_blast.game.system.display.cli.view;

import java.io.IOException;

import com.googlecode.lanterna.SGR;
import com.googlecode.lanterna.TextColor;
import com.googlecode.lanterna.graphics.TextGraphics;
import com.googlecode.lanterna.screen.Screen;

public class menu_error {

    private static final long notification_duration = 2000;

    private static final String[] invalid_messages = {
        "Hey, nerd! You pressed the wrong button!",
        "Not a bright one are you?",
        "↓↓↓↓↓ WHAT SO HARD ABOUT READING THE TEXT BELOW??? ↓↓↓↓↓",
        "I'm watching you, don't test my patience",
        "i'll give you one more chance"
    };

    private static final String[] bill_cipher_art = {
        "                   @@@@                  ",
        "                   @@@@                  ",
        "                   @@@@                  ",
        "                   @@@@                  ",
        "                   @@@@                  ",
        "      +++++        @@@@                  ",
        "+++++++++++      @@@@@@@@                ",
        "+++++++++++         ::                   ",
        "+++++++            ::::                  ",
        "     ++ +         ::::::                 ",
        "     +++++       ::-:-:::                ",
        "     ++++++     ::=-.:+-::               ",
        "  +++++++++    ::...%:..:::              ",
        "++++++++++     :....@-....:::            ",
        "++++%%+@+++   ::....@-....::::           ",
        "+++++%@++++  ::::::.-...:::::::          ",
        " +++++@@++ @%::::::+:-::::::::=@@@@      ",
        "       @@@@@-::::-:-::::-:::::::-@@@@@@@ ",
        "           ::::::%@@#%@@-:::::::::   @@@@",
        "          :::::::--::::-:::::::::::#@@@@ ",
        "         :::::::::::::::::::::::::-@@@@@ ",
        "          ::::::-+:::::::::::::::::::    ",
        "               @@     @@@                ",
        "              @@@     @@                 ",
        "          @@@@@@  @@ @@@                 ",
        "           @@@@   @@@@@                  ",
        "             @     @@@                   "
    };

    private static final String error_text = "ERROR";

    private static final TextColor.RGB background_color =
        new TextColor.RGB(150, 0, 0);

    private static final TextColor.RGB[] error_colors = {
        new TextColor.RGB(90, 0, 0),
        new TextColor.RGB(75, 0, 0),
        new TextColor.RGB(65, 0, 0),
        new TextColor.RGB(55, 0, 0)
    };

    private static final long jitter_interval_min = 300;
    private static final long jitter_interval_max = 900;
    private static final long jitter_duration = 50;

    private static final int jitter_max_offset = 2;

    private int invalid_input_count;

    private String notification_message;
    private long notification_expiry_time;

    private boolean final_screen_active;

    private int jitter_x;
    private int jitter_y;

    private long next_jitter_time;
    private long jitter_expiry_time;

    public void register_invalid_input() {

        invalid_input_count++;

        if (invalid_input_count > invalid_messages.length) {

            notification_message = null;
            final_screen_active = true;

            next_jitter_time = System.currentTimeMillis() + 500;

            return;
        }

        notification_message =
            invalid_messages[invalid_input_count - 1];

        notification_expiry_time =
            System.currentTimeMillis() + notification_duration;
    }

    public boolean update() {

        boolean dirty = false;
        long current_time = System.currentTimeMillis();

        // Notification expiration
        if (
            notification_message != null &&
            current_time >= notification_expiry_time
        ) {
            notification_message = null;
            dirty = true;
        }

        // Screen jitter
        if (final_screen_active) {

            if (
                jitter_x == 0 &&
                jitter_y == 0 &&
                current_time >= next_jitter_time
            ) {

                jitter_x =
                    (int) (Math.random() * (jitter_max_offset * 2 + 1))
                    - jitter_max_offset;

                jitter_y =
                    (int) (Math.random() * 3) - 1;

                // Ensure the screen actually moves
                if (jitter_x == 0 && jitter_y == 0) {
                    jitter_x = 1;
                }

                jitter_expiry_time =
                    current_time + jitter_duration;

                dirty = true;
            }

            else if (
                (jitter_x != 0 || jitter_y != 0) &&
                current_time >= jitter_expiry_time
            ) {

                jitter_x = 0;
                jitter_y = 0;

                long random_interval =
                    jitter_interval_min +
                    (long) (
                        Math.random() *
                        (jitter_interval_max - jitter_interval_min)
                    );

                next_jitter_time =
                    current_time + random_interval;

                dirty = true;
            }
        }

        return dirty;
    }

    public boolean is_final_screen_active() {
        return final_screen_active;
    }

    public int get_invalid_input_count() {
        return invalid_input_count;
    }

    public void render_notification(Screen screen) throws IOException {

        if (notification_message == null) {
            return;
        }

        TextGraphics graphics = screen.newTextGraphics();

        int width = screen.getTerminalSize().getColumns();
        int height = screen.getTerminalSize().getRows();

        int notification_y = height - 4;

        if (notification_y < 0 || notification_y >= height) {
            return;
        }

        int max_width = width - 2;

        if (max_width <= 0) {
            return;
        }

        String notification = " " + notification_message + " ";

        if (notification.length() > max_width) {
            notification = notification.substring(0, max_width);
        }

        int notification_x =
            (width - notification.length()) / 2;

        graphics.setForegroundColor(TextColor.ANSI.WHITE);
        graphics.setBackgroundColor(TextColor.ANSI.RED);

        graphics.putString(
            notification_x,
            notification_y,
            notification,
            SGR.BOLD
        );

        screen.refresh();
    }

    public void render_final_screen(Screen screen) throws IOException {

        screen.clear();

        TextGraphics graphics = screen.newTextGraphics();

        int width = screen.getTerminalSize().getColumns();
        int height = screen.getTerminalSize().getRows();

        int center_x = width / 2 + jitter_x;
        int center_y = height / 2 + jitter_y;

        render_background(
            graphics,
            width,
            height
        );

        render_bill_cipher(
            graphics,
            center_x,
            center_y,
            width,
            height
        );

        render_exit_button(
            graphics,
            center_x,
            center_y,
            width,
            height
        );

        screen.refresh();
    }

    private void render_background(
        TextGraphics graphics,
        int width,
        int height
    ) {

        // Red background
        graphics.setBackgroundColor(background_color);

        String blank_line = " ".repeat(width);

        for (int y = 0; y < height; y++) {
            graphics.putString(0, y, blank_line);
        }

        // Repeated ERROR background
        for (int y = 0; y < height; y += 2) {

            int offset = (y / 2) % 2 == 0 ? 0 : 3;

            for (
                int x = offset;
                x + error_text.length() <= width;
                x += 9
            ) {

                int color_index =
                    ((x / 9) + (y / 2)) % error_colors.length;

                int draw_x = x + jitter_x;
                int draw_y = y + jitter_y;

                if (
                    draw_x >= 0 &&
                    draw_x + error_text.length() <= width &&
                    draw_y >= 0 &&
                    draw_y < height
                ) {

                    graphics.setForegroundColor(
                        error_colors[color_index]
                    );

                    graphics.putString(
                        draw_x,
                        draw_y,
                        error_text
                    );
                }
            }
        }
    }

    private void render_bill_cipher(
        TextGraphics graphics,
        int center_x,
        int center_y,
        int width,
        int height
    ) {

        // ASCII Art
        int art_start_y =
            center_y - bill_cipher_art.length / 2;

        for (int i = 0; i < bill_cipher_art.length; i++) {

            String line = bill_cipher_art[i];

            int art_x = center_x - line.length() / 2;
            int art_y = art_start_y + i;

            if (
                art_y >= 0 &&
                art_y < height &&
                art_x >= 0 &&
                art_x + line.length() <= width
            ) {

                graphics.setForegroundColor(TextColor.ANSI.YELLOW);
                graphics.setBackgroundColor(background_color);

                graphics.putString(
                    art_x,
                    art_y,
                    line,
                    SGR.BOLD
                );
            }
        }
    }

    private void render_exit_button(
        TextGraphics graphics,
        int center_x,
        int center_y,
        int width,
        int height
    ) {

        // Warning message
        String warning = "GET OUT OF HERE SUCKER";

        int warning_x = center_x - warning.length() / 2;
        int warning_y = center_y - 16;

        if (
            warning_y >= 0 &&
            warning_y < height &&
            warning_x >= 0 &&
            warning_x + warning.length() <= width
        ) {

            graphics.setForegroundColor(TextColor.ANSI.WHITE);
            graphics.setBackgroundColor(background_color);

            graphics.putString(
                warning_x,
                warning_y,
                warning,
                SGR.BOLD
            );
        }

        // Exit instructions
        String controls = "Enter is the only way out";

        int controls_x = center_x - controls.length() / 2;
        int controls_y = center_y + 16;

        if (
            controls_y >= 0 &&
            controls_y < height &&
            controls_x >= 0 &&
            controls_x + controls.length() <= width
        ) {

            graphics.setForegroundColor(TextColor.ANSI.WHITE);
            graphics.setBackgroundColor(TextColor.ANSI.BLACK);

            graphics.putString(
                controls_x,
                controls_y,
                controls
            );
        }
    }
}
