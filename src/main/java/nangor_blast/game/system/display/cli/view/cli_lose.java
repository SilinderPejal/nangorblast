
package nangor_blast.game.system.display.cli.view;

import java.io.IOException;

import com.googlecode.lanterna.TextColor;
import com.googlecode.lanterna.graphics.TextGraphics;
import com.googlecode.lanterna.screen.Screen;

public class cli_lose {

    public void render(Screen screen) throws IOException {

        screen.clear();

        TextGraphics graphics = screen.newTextGraphics();

        String[] art = {
            "YYYYYYY       YYYYYYY                                     LLLLLLLLLLL                                                                    !!!",
            "Y:::::Y       Y:::::Y                                     L:::::::::L                                                                   !!:!!",
            "Y:::::Y       Y:::::Y                                     L:::::::::L                                                                   !:::!",
            "Y::::::Y     Y::::::Y                                     LL:::::::LL                                                                   !:::!",
            "YYY:::::Y   Y:::::YYYooooooooooo   uuuuuu    uuuuuu         L:::::L                  ooooooooooo       ssssssssss       eeeeeeeeeeee    !:::!",
            "   Y:::::Y Y:::::Y oo:::::::::::oo u::::u    u::::u         L:::::L                oo:::::::::::oo   ss::::::::::s    ee::::::::::::ee  !:::!",
            "    Y:::::Y:::::Y o:::::::::::::::ou::::u    u::::u         L:::::L               o:::::::::::::::oss:::::::::::::s  e::::::eeeee:::::ee!:::!",
            "     Y:::::::::Y  o:::::ooooo:::::ou::::u    u::::u         L:::::L               o:::::ooooo:::::os::::::ssss:::::se::::::e     e:::::e!:::!",
            "      Y:::::::Y   o::::o     o::::ou::::u    u::::u         L:::::L               o::::o     o::::o s:::::s  ssssss e:::::::eeeee::::::e!:::!",
            "       Y:::::Y    o::::o     o::::ou::::u    u::::u         L:::::L               o::::o     o::::o   s::::::s      e:::::::::::::::::e !:::!",
            "       Y:::::Y    o::::o     o::::ou::::u    u::::u         L:::::L               o::::o     o::::o      s::::::s   e::::::eeeeeeeeeee  !!:!!",
            "       Y:::::Y    o::::o     o::::ou:::::uuuu:::::u         L:::::L         LLLLLLo::::o     o::::ossssss   s:::::s e:::::::e            !!!",
            "       Y:::::Y    o:::::ooooo:::::ou:::::::::::::::uu     LL:::::::LLLLLLLLL:::::Lo:::::ooooo:::::os:::::ssss::::::se::::::::e",
            "    YYYY:::::YYYY o:::::::::::::::o u:::::::::::::::u     L::::::::::::::::::::::Lo:::::::::::::::os::::::::::::::s  e::::::::eeeeeeee   !!!",
            "    Y:::::::::::Y  oo:::::::::::oo   uu::::::::uu:::u     L::::::::::::::::::::::L oo:::::::::::oo  s:::::::::::ss    ee:::::::::::::e  !!:!!",
            "    YYYYYYYYYYYYY    ooooooooooo       uuuuuuuu  uuuu     LLLLLLLLLLLLLLLLLLLLLLLL   ooooooooooo     sssssssssss        eeeeeeeeeeeeee   !!!"
        };

        int terminal_width = screen.getTerminalSize().getColumns();
        int terminal_height = screen.getTerminalSize().getRows();

        int art_width = 0;

        for (String line : art) {
            art_width = Math.max(art_width, line.length());
        }

        String message = "Press ENTER to exit";

        int total_height = art.length + 2;

        int start_x = (terminal_width - art_width) / 2;
        int start_y = (terminal_height - total_height) / 2;

        graphics.setForegroundColor(new TextColor.RGB(139, 0, 0));

        for (int i = 0; i < art.length; i++) {
            graphics.putString(
                start_x,
                start_y + i,
                art[i]
            );
        }

        graphics.setForegroundColor(TextColor.ANSI.WHITE);

        graphics.putString(
            (terminal_width - message.length()) / 2,
            start_y + art.length + 2,
            message
        );

        screen.refresh();
    }
}
