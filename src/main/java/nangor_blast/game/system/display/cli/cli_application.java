
package nangor_blast.game.system.display.cli;

import java.io.IOException;

import com.googlecode.lanterna.input.KeyStroke;
import com.googlecode.lanterna.input.KeyType;
import com.googlecode.lanterna.screen.Screen;
import com.googlecode.lanterna.screen.TerminalScreen;
import com.googlecode.lanterna.terminal.DefaultTerminalFactory;
import com.googlecode.lanterna.terminal.Terminal;

import nangor_blast.exception.cli_exception;

import nangor_blast.game.system.display.cli.view.cli_main_menu;
import nangor_blast.game.system.display.cli.view.menu_error;
import nangor_blast.game.system.display.cli.view.cli_win;
import nangor_blast.game.system.display.cli.view.cli_lose;
import nangor_blast.game.system.display.cli.view.gameplay.cli_game;

import nangor_blast.game.system.menu.menu_state;
import nangor_blast.game.system.menu.main_menu;

import nangor_blast.game.system.game_session;
import nangor_blast.game.entities.player.player_movement;

import nangor_blast.game.system.state.game_state_manager;
import nangor_blast.game.system.state.playing_state;
import nangor_blast.game.system.state.lose_state;

import nangor_blast.game.system.event.game_event;
import nangor_blast.interfaces.game_event_listener;

import nangor_blast.interfaces.game_state.game_state_type;

public class cli_application implements game_event_listener {

    private Terminal terminal;
    private Screen screen;

    private menu_state menu_state;
    private cli_main_menu main_menu_view;
    private cli_game game_view;
    private cli_win win_view;
    private cli_lose lose_view;

    private cli_input_handler input_handler;
    private menu_error menu_error;

    private game_state_manager state_manager;

    private game_session session;
    private player_movement movement;

    private boolean player_lost;
    private boolean running;

    public void start() throws cli_exception {

        try {
            terminal = new DefaultTerminalFactory().createTerminal();
            screen = new TerminalScreen(terminal);

            screen.startScreen();
            screen.setCursorPosition(null);

            initialize();

            running = true;
            boolean dirty = true;

            while (running) {

                if (screen.doResizeIfNecessary() != null) {
                    dirty = true;
                }

                if (menu_error.update()) {
                    dirty = true;
                }

                if (dirty) {
                    render();
                    dirty = false;
                }

                KeyStroke key = screen.pollInput();

                if (menu_error.is_final_screen_active()) {

                    if (
                        key != null &&
                        key.getKeyType() == KeyType.Enter
                    ) {
                        running = false;
                    }

                } else {

                    game_state_type current_state =
                        state_manager.get_current_state_type();

                    // Handle win and lose screen input
                    if (
                        current_state == game_state_type.win ||
                        current_state == game_state_type.lose
                    ) {

                        if (
                            key != null &&
                            key.getKeyType() == KeyType.Enter
                        ) {
                            // Remove listener before discarding session
                            if (session != null) {
                                session.get_event_manager()
                                    .remove_listener(this);
                            }

                            session = null;
                            movement = null;
                            player_lost = false;

                            menu_error.reset();

                            state_manager.change_state(menu_state);

                            dirty = true;
                        }

                    } else {

                        int previous_index = -1;

                        if (current_state == game_state_type.menu) {
                            previous_index =
                                menu_state.get_selected_index();
                        }

                        main_menu selected = input_handler.handle_input(
                            key,
                            current_state,
                            menu_state,
                            session,
                            movement
                        );

                        if (
                            input_handler.has_invalid_input() &&
                            current_state == game_state_type.menu
                        ) {
                            menu_error.register_invalid_input();
                            dirty = true;
                        }

                        if (
                            current_state == game_state_type.menu &&
                            previous_index != menu_state.get_selected_index()
                        ) {
                            dirty = true;
                        }

                        if (selected != null) {

                            switch (selected) {

                                case start_game:

                                    session = new game_session();

                                    if (session.initialize()) {

                                        // Register CLI as event listener
                                        session.get_event_manager()
                                            .add_listener(this);

                                        player_lost = false;

                                        playing_state playing =
                                            new playing_state(
                                                state_manager,
                                                session
                                            );

                                        state_manager.change_state(playing);

                                        movement = playing.get_movement();

                                        dirty = true;

                                    } else {

                                        session = null;
                                        movement = null;

                                        menu_error.register_invalid_input();
                                        dirty = true;
                                    }

                                    break;

                                case how_to_play:
                                    // Instructions will be implemented later.
                                    break;

                                case exit:
                                    running = false;
                                    break;
                            }
                        }
                    }
                }

                // Update current state
                game_state_type previous_state =
                    state_manager.get_current_state_type();

                state_manager.update();

                // Handle player death event
                if (
                    player_lost &&
                    state_manager.get_current_state_type() ==
                    game_state_type.playing
                ) {
                    player_lost = false;

                    state_manager.change_state(new lose_state());

                    dirty = true;
                }

                // Refresh gameplay while playing
                if (
                    state_manager.get_current_state_type() ==
                    game_state_type.playing
                ) {
                    dirty = true;
                }

                // Detect state transition
                if (
                    previous_state !=
                    state_manager.get_current_state_type()
                ) {
                    dirty = true;
                }

                // Refresh gameplay after receiving input
                if (
                    key != null &&
                    state_manager.get_current_state_type() ==
                    game_state_type.playing
                ) {
                    dirty = true;
                }

                try {
                    Thread.sleep(16);

                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                    running = false;
                }
            }

        } catch (IOException e) {

            throw new cli_exception(
                "Failed to run CLI application.",
                e
            );
        }
    }

    private void initialize() {

        menu_state = new menu_state();

        main_menu_view = new cli_main_menu();
        game_view = new cli_game();
        win_view = new cli_win();
        lose_view = new cli_lose();

        input_handler = new cli_input_handler();
        menu_error = new menu_error();

        state_manager = new game_state_manager();
        state_manager.change_state(menu_state);

        player_lost = false;
    }

    @Override
    public void on_game_event(game_event event) {

        if (event == null || session == null) {
            return;
        }

        if (
            event.get_type() == game_event.event_type.player_died &&
            event.get_target() == session.get_player()
        ) {
            player_lost = true;
        }
    }

    private void render() throws IOException {

        if (menu_error.is_final_screen_active()) {

            menu_error.render_final_screen(screen);

        } else {

            switch (state_manager.get_current_state_type()) {

                case menu:

                    main_menu_view.render(
                        screen,
                        menu_state
                    );

                    menu_error.render_notification(screen);
                    break;

                case playing:

                    game_view.render(
                        screen,
                        session
                    );
                    break;

                case win:

                    win_view.render(screen);
                    break;

                case lose:

                    lose_view.render(screen);
                    break;
            }
        }
    }

    public void stop() throws cli_exception {

        // Unregister listener before closing
        if (session != null) {
            session.get_event_manager().remove_listener(this);
        }

        IOException error = null;

        if (screen != null) {

            try {
                screen.stopScreen();

            } catch (IOException e) {
                error = e;

            } finally {
                screen = null;
            }
        }

        if (terminal != null) {

            try {
                terminal.close();

            } catch (IOException e) {

                if (error == null) {
                    error = e;
                } else {
                    error.addSuppressed(e);
                }

            } finally {
                terminal = null;
            }
        }

        if (error != null) {

            throw new cli_exception(
                "Failed to close CLI terminal.",
                error
            );
        }
    }
}
