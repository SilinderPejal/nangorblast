
package nangor_blast.game.system.display.cli.view;

import java.io.IOException;

import com.googlecode.lanterna.TextColor;
import com.googlecode.lanterna.graphics.TextGraphics;
import com.googlecode.lanterna.screen.Screen;

public class cli_win {

    public void render(Screen screen, int final_score) throws IOException {

        screen.clear();

        TextGraphics graphics = screen.newTextGraphics();

        String[] art = {
            "YYYYYYY       YYYYYYY                                     WWWWWWWW                           WWWWWWWW iiii                    !!!",
            "Y:::::Y       Y:::::Y                                     W::::::W                           W::::::Wi::::i                  !!:!!",
            "Y:::::Y       Y:::::Y                                     W::::::W                           W::::::W iiii                   !:::!",
            "Y::::::Y     Y::::::Y                                     W::::::W                           W::::::W                        !:::!",
            "YYY:::::Y   Y:::::YYYooooooooooo   uuuuuu    uuuuuu        W:::::W           WWWWW           W:::::Wiiiiiiinnnn  nnnnnnnn    !:::!",
            "   Y:::::Y Y:::::Y oo:::::::::::oo u::::u    u::::u         W:::::W         W:::::W         W:::::W i:::::in:::nn::::::::nn  !:!!",
            "    Y:::::Y:::::Y o:::::::::::::::ou::::u    u::::u          W:::::W       W:::::::W       W:::::W   i::::in::::::::::::::nn !:::!",
            "     Y:::::::::Y  o:::::ooooo:::::ou::::u    u::::u           W:::::W     W:::::::::W     W:::::W    i::::inn:::::::::::::::n!:::!",
            "      Y:::::::Y   o::::o     o::::ou::::u    u::::u            W:::::W   W:::::W:::::W   W:::::W     i::::i  n:::::nnnn:::::n!:::!",
            "       Y:::::Y    o::::o     o::::ou::::u    u::::u             W:::::W W:::::W W:::::W W:::::W      i::::i  n::::n    n::::n!:::!",
            "       Y:::::Y    o::::o     o::::ou::::u    u::::u              W:::::W:::::W   W:::::W:::::W       i::::i  n::::n    n::::n!!:!!",
            "       Y:::::Y    o::::o     o::::ou:::::uuuu:::::u               W:::::::::W     W:::::::::W        i::::i  n::::n    n::::n !!!",
            "       Y:::::Y    o:::::ooooo:::::ou:::::::::::::::uu              W:::::::W       W:::::::W        i::::::i n::::n    n::::n",
            "    YYYY:::::YYYY o:::::::::::::::o u:::::::::::::::u               W:::::W         W:::::W         i::::::i n::::n    n::::n !!!",
            "    Y:::::::::::Y  oo:::::::::::oo   uu::::::::uu:::u                W:::W           W:::W          i::::::i n::::n    n::::n!!:!!",
            "    YYYYYYYYYYYYY    ooooooooooo       uuuuuuuu  uuuu                 WWW             WWW           iiiiiiii nnnnnn    nnnnnn !!!"
        };

        int terminal_width = screen.getTerminalSize().getColumns();
        int terminal_height = screen.getTerminalSize().getRows();

        int art_width = 0;

        for (String line : art) {
            art_width = Math.max(art_width, line.length());
        }

        String message = "Press ENTER to exit";

        int total_height = art.length + 3;

        int start_x = (terminal_width - art_width) / 2;
        int start_y = (terminal_height - total_height) / 2;

        // Warna hijau tua
        graphics.setForegroundColor(new TextColor.RGB(0, 100, 0));

        // Render ASCII art
        for (int i = 0; i < art.length; i++) {

            graphics.putString(
                start_x,
                start_y + i,
                art[i]
            );
        }

        graphics.setForegroundColor(TextColor.ANSI.WHITE);

        String score_message = "Final Score: " + final_score;

        graphics.putString(
            Math.max(0, (terminal_width - score_message.length()) / 2),
            start_y + art.length + 1,
            score_message
        );
        
        graphics.putString(
            (terminal_width - message.length()) / 2,
            start_y + art.length + 2,
            message
        );

        screen.refresh();
    }
}
